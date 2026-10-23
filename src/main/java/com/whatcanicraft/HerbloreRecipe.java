package com.whatcanicraft;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * A single Herblore potion recipe.
 *
 * A potion is made in two stages:
 *   1. herb + Vial of water -> unfinished potion
 *   2. unfinished potion + secondary ingredient(s) -> finished potion
 *
 * The player "has" the recipe if they own the secondaries AND either
 * (herb + vial of water) or the matching unfinished potion.
 */
public class HerbloreRecipe
{
	public static final String VIAL_OF_WATER = "Vial of water";

	private final String potionName;
	private final int level;
	private final String herb;
	private final String unfinishedPotion;
	private final List<String> secondaries;

	public HerbloreRecipe(String potionName, int level, String herb, String... secondaries)
	{
		this.potionName = potionName;
		this.level = level;
		this.herb = herb;
		// Dwarf weed -> "Dwarf weed potion (unf)", Irit leaf -> "Irit potion (unf)", etc.
		this.unfinishedPotion = herb.replace(" leaf", "").replace(" weed", "").equals("Dwarf")
			? "Dwarf weed potion (unf)"
			: herb.replace(" leaf", "").replace(" weed", "") + " potion (unf)";
		this.secondaries = Collections.unmodifiableList(Arrays.asList(secondaries));
	}

	public String getPotionName()
	{
		return potionName;
	}

	public int getLevel()
	{
		return level;
	}

	public String getHerb()
	{
		return herb;
	}

	public String getUnfinishedPotion()
	{
		return unfinishedPotion;
	}

	public List<String> getSecondaries()
	{
		return secondaries;
	}
}
