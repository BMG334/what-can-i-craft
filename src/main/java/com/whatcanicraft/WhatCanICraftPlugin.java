package com.whatcanicraft;

import com.google.inject.Provides;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Locale;
import java.util.function.Consumer;
import javax.inject.Inject;
import javax.swing.SwingUtilities;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.EnumComposition;
import net.runelite.api.EnumID;
import net.runelite.api.GameState;
import net.runelite.api.Item;
import net.runelite.api.ItemComposition;
import net.runelite.api.ItemContainer;
import net.runelite.api.ScriptID;
import net.runelite.api.gameval.InventoryID;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;

@Slf4j
@PluginDescriptor(
	name = "What Can I Craft",
	description = "Scans your bank for skilling ingredients and lists what you can make (Herblore, Crafting)",
	tags = {"herblore", "crafting", "bank", "recipes"}
)
public class WhatCanICraftPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private ClientThread clientThread;

	@Inject
	private ClientToolbar clientToolbar;

	@Inject
	private ItemManager itemManager;

	@Inject
	private WhatCanICraftConfig config;

	private WhatCanICraftPanel panel;
	private NavigationButton navButton;

	@Override
	protected void startUp()
	{
		panel = new WhatCanICraftPanel(this::scanBank);

		navButton = NavigationButton.builder()
			.tooltip("What Can I Craft")
			.icon(createIcon())
			.priority(7)
			.panel(panel)
			.build();

		clientToolbar.addNavigation(navButton);
	}

	@Override
	protected void shutDown()
	{
		clientToolbar.removeNavigation(navButton);
		panel = null;
		navButton = null;
	}

	/** Called from the Swing thread when a tab's Scan button is pressed. */
	private void scanBank(CraftSkill skill)
	{
		// Item containers, item definitions and client scripts must be used on the client thread
		clientThread.invoke(() ->
		{
			if (client.getGameState() != GameState.LOGGED_IN)
			{
				updatePanel(p -> p.showMessage(skill, "Log in to scan your bank."));
				return;
			}

			ItemContainer bank = client.getItemContainer(InventoryID.BANK);
			if (bank == null)
			{
				updatePanel(p -> p.showMessage(skill, "Bank contents unavailable. Open your bank once, then scan again."));
				return;
			}

			Stock owned = new Stock();
			countItems(bank, owned);

			ItemContainer inventory = config.includeInventory() ? client.getItemContainer(InventoryID.INV) : null;
			if (inventory != null)
			{
				countItems(inventory, owned);
			}

			if (config.includePotionStorage())
			{
				countPotionStorage(owned);
			}

			updatePanel(p -> p.showResults(skill, skill.findCraftable(owned)));
		});
	}

	/** Adds the container's item quantities to the stock, keyed by lower-case item name. */
	private void countItems(ItemContainer container, Stock stock)
	{
		for (Item item : container.getItems())
		{
			// Empty slots have id -1
			if (item.getId() <= 0 || item.getQuantity() <= 0)
			{
				continue;
			}

			ItemComposition composition = itemManager.getItemComposition(item.getId());

			// Bank placeholders reuse the real item's name but aren't actual items.
			// They carry a placeholder template id (14401); normal items have -1.
			if (composition.getPlaceholderTemplateId() <= 0)
			{
				stock.addItem(composition.getName().toLowerCase(Locale.ROOT), item.getQuantity());
			}
		}
	}

	/**
	 * Adds the bank's potion storage to the stock, the same way RuneLite's Bank Tags plugin reads it.
	 *
	 * Potion storage isn't an item container. The game has two enums listing the storable potions
	 * (finished and unfinished). Each value in them is the id of another enum, one per potion, that maps a
	 * dose count (1-4) to the potion's item id. Running the POTIONSTORE_DOSES client script with a potion's
	 * enum id leaves the amount stored on the int stack.
	 *
	 * Finished potions are stored as loose doses that can be withdrawn as potions of any dose size, so they're
	 * added as a pool of doses. Unfinished potions are stored one per unit and added as plain items.
	 * (Stored empty vials are ignored: recipes need vials of water.)
	 */
	private void countPotionStorage(Stock stock)
	{
		for (boolean unfinished : new boolean[]{false, true})
		{
			EnumComposition potionList = client.getEnum(unfinished
				? EnumID.POTIONSTORE_UNFINISHED_POTIONS
				: EnumID.POTIONSTORE_POTIONS);
			if (potionList == null)
			{
				continue;
			}

			for (int potionEnumId : potionList.getIntVals())
			{
				client.runScript(ScriptID.POTIONSTORE_DOSES, potionEnumId);
				int stored = client.getIntStack()[0];
				String name = stored > 0 ? potionName(client.getEnum(potionEnumId)) : null;
				if (name == null)
				{
					continue;
				}
				log.debug("Potion storage: {} = {}", name, stored);

				if (unfinished)
				{
					stock.addItem(name.toLowerCase(Locale.ROOT), stored);
				}
				else
				{
					// "Super energy(4)" -> "super energy"
					stock.addPotionDoses(name.replaceAll("\\(\\d\\)$", "").toLowerCase(Locale.ROOT), stored);
				}
			}
		}
	}

	/** @return the potion's item name from any of its dose items, or null if the enum has none */
	private String potionName(EnumComposition potionEnum)
	{
		if (potionEnum == null)
		{
			return null;
		}

		for (int doses = 4; doses >= 1; doses--)
		{
			int itemId = potionEnum.getIntValue(doses);
			if (itemId > 0)
			{
				return itemManager.getItemComposition(itemId).getName();
			}
		}
		return null;
	}

	/** Runs a panel update on the Swing thread, if the plugin is still running. */
	private void updatePanel(Consumer<WhatCanICraftPanel> update)
	{
		SwingUtilities.invokeLater(() ->
		{
			if (panel != null)
			{
				update.accept(panel);
			}
		});
	}

	/** Simple generated flask icon so the plugin needs no image resources. */
	private static BufferedImage createIcon()
	{
		BufferedImage image = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = image.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(new Color(60, 200, 90));
		g.fillOval(2, 6, 12, 9);
		g.setColor(new Color(200, 200, 200));
		g.fillRect(6, 1, 4, 6);
		g.dispose();
		return image;
	}

	@Provides
	WhatCanICraftConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(WhatCanICraftConfig.class);
	}
}
