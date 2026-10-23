package com.whatcanicraft;

import com.google.inject.Provides;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import javax.inject.Inject;
import javax.swing.SwingUtilities;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@PluginDescriptor(
	name = "What Can I Craft",
	description = "Scans your bank for Herblore ingredients and lists the potions you can make",
	tags = {"herblore", "bank", "potion", "recipes"}
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

	/** Called from the Swing thread when the Scan button is pressed. */
	private void scanBank()
	{
		// Item containers and item definitions must be read on the client thread
		clientThread.invoke(() ->
		{
			if (client.getGameState() != GameState.LOGGED_IN)
			{
				showMessage("Log in to scan your bank.");
				return;
			}

			ItemContainer bank = client.getItemContainer(InventoryID.BANK);
			if (bank == null)
			{
				showMessage("You must open your bank at least once before you can scan it.");
				return;
			}

			Set<String> owned = new HashSet<>();
			collectNames(bank, owned);

			if (config.includeInventory())
			{
				ItemContainer inventory = client.getItemContainer(InventoryID.INVENTORY);
				if (inventory != null)
				{
					collectNames(inventory, owned);
				}
			}

			List<HerbloreRecipes.Match> matches = HerbloreRecipes.findCraftable(owned);
			SwingUtilities.invokeLater(() ->
			{
				if (panel != null)
				{
					panel.showResults(matches);
				}
			});
		});
	}

	private void collectNames(ItemContainer container, Set<String> names)
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
			if (composition.getPlaceholderTemplateId() > 0)
			{
				continue;
			}

			names.add(composition.getName().toLowerCase(Locale.ROOT));
		}
	}

	private void showMessage(String message)
	{
		SwingUtilities.invokeLater(() ->
		{
			if (panel != null)
			{
				panel.showMessage(message);
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
