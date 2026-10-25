package com.whatcanicraft;

import com.google.inject.Provides;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;
import javax.inject.Inject;
import javax.swing.SwingUtilities;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemComposition;
import net.runelite.api.ItemContainer;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;

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
	protected void startUp() throws Exception
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
	protected void shutDown() throws Exception
	{
		clientToolbar.removeNavigation(navButton);
		panel = null;
		navButton = null;
	}

	/** Called from the Swing thread when a tab's Scan button is pressed. */
	private void scanBank(CraftSkill skill)
	{
		// Item containers and item definitions must be read on the client thread
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

			Map<String, Integer> owned = new HashMap<>();
			countItems(bank, owned);

			ItemContainer inventory = config.includeInventory() ? client.getItemContainer(InventoryID.INVENTORY) : null;
			if (inventory != null)
			{
				countItems(inventory, owned);
			}

			updatePanel(p -> p.showResults(skill, skill.findCraftable(owned)));
		});
	}

	/** Adds the container's item quantities to the running totals, keyed by lower-case item name. */
	private void countItems(ItemContainer container, Map<String, Integer> totals)
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
				totals.merge(composition.getName().toLowerCase(Locale.ROOT), item.getQuantity(), Integer::sum);
			}
		}
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
