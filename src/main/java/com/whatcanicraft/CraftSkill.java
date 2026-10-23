package com.whatcanicraft;

import java.util.List;

/** The skills shown as tabs in the side panel, in tab order. */
public enum CraftSkill
{
	HERBLORE("Herblore", HerbloreRecipes.ALL),
	CRAFTING("Crafting", CraftingRecipes.ALL);

	private final String displayName;
	private final List<Recipe> recipes;

	CraftSkill(String displayName, List<Recipe> recipes)
	{
		this.displayName = displayName;
		this.recipes = recipes;
	}

	public String getDisplayName()
	{
		return displayName;
	}

	public List<Recipe> getRecipes()
	{
		return recipes;
	}
}
