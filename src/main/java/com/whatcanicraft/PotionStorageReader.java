package com.whatcanicraft;

import java.util.Locale;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.EnumComposition;
import net.runelite.api.EnumID;
import net.runelite.api.ScriptID;
import net.runelite.client.game.ItemManager;

/**
 * Reads the bank's potion storage into a {@link Stock}, the same way RuneLite's Bank Tags plugin
 * (net.runelite.client.plugins.banktags.tabs.PotionStorage) does.
 *
 * Potion storage isn't an item container. The game has two enums listing the storable potions
 * (EnumID.POTIONSTORE_POTIONS and POTIONSTORE_UNFINISHED_POTIONS). Each value in them is the id of another
 * enum, one per potion, which maps a dose count (1-4) to that potion's item id. Running the
 * POTIONSTORE_DOSES client script with a potion's enum id leaves the amount stored on the int stack.
 *
 * Finished potions are stored as loose doses, which can be withdrawn as potions of any dose size, so they're
 * added to the stock as a pool of doses. Unfinished potions are stored one per unit and added as plain items.
 * (Empty vials are also stored, but recipes need vials of water, so they're ignored.)
 *
 * Must be called on the client thread.
 */
@Slf4j
public class PotionStorageReader
{
	private final Client client;
	private final ItemManager itemManager;

	public PotionStorageReader(Client client, ItemManager itemManager)
	{
		this.client = client;
		this.itemManager = itemManager;
	}

	public void addTo(Stock stock)
	{
		read(EnumID.POTIONSTORE_POTIONS, false, stock);
		read(EnumID.POTIONSTORE_UNFINISHED_POTIONS, true, stock);
	}

	private void read(int listEnumId, boolean unfinished, Stock stock)
	{
		EnumComposition potionList = client.getEnum(listEnumId);
		if (potionList == null)
		{
			return;
		}

		// The list's values are enum ids (one per potion), not item ids
		for (int potionEnumId : potionList.getIntVals())
		{
			client.runScript(ScriptID.POTIONSTORE_DOSES, potionEnumId);
			int stored = client.getIntStack()[0];
			if (stored <= 0)
			{
				continue;
			}

			String name = potionName(client.getEnum(potionEnumId));
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
}
