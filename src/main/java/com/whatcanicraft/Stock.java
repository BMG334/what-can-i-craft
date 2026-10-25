package com.whatcanicraft;

import java.util.HashMap;
import java.util.Map;

/**
 * What the player owns, as seen by the recipe matcher: item quantities (bank, optionally inventory) plus
 * doses of potions held in the bank's potion storage. All keys are lower-case.
 *
 * Stored doses can be withdrawn as potions of any dose size, so they are tracked as a pool of doses per
 * potion rather than as items.
 */
public class Stock
{
	private final Map<String, Integer> items = new HashMap<>();
	private final Map<String, Integer> potionDoses = new HashMap<>();

	public void addItem(String key, int quantity)
	{
		items.merge(key, quantity, Integer::sum);
	}

	/** @param potionKey lower-case potion name without a dose suffix, e.g. "super restore" */
	public void addPotionDoses(String potionKey, int doses)
	{
		potionDoses.merge(potionKey, doses, Integer::sum);
	}

	public int items(String key)
	{
		return items.getOrDefault(key, 0);
	}

	public int potionDoses(String potionKey)
	{
		return potionDoses.getOrDefault(potionKey, 0);
	}

	void removeItem(String key, int quantity)
	{
		items.merge(key, -quantity, Integer::sum);
	}

	void removePotionDoses(String potionKey, int doses)
	{
		potionDoses.merge(potionKey, -doses, Integer::sum);
	}

	/** A copy the matcher can consume from without affecting the original. */
	Stock copy()
	{
		Stock copy = new Stock();
		copy.items.putAll(items);
		copy.potionDoses.putAll(potionDoses);
		return copy;
	}
}
