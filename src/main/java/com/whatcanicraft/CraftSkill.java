package com.whatcanicraft;

import java.util.Collections;
import java.util.List;

/** The skills shown as tabs in the side panel, in tab order. */
public enum CraftSkill
{
	HERBLORE("Herblore", HerbloreRecipes.ALL, Collections.emptyList()),
	CRAFTING("Crafting", CraftingRecipes.ALL, CraftingRecipes.CATEGORIES);

	private final String displayName;
	private final List<Recipe> recipes;
	private final List<String> categories;

	CraftSkill(String displayName, List<Recipe> recipes, List<String> categories)
	{
		this.displayName = displayName;
		this.recipes = recipes;
		this.categories = categories;
	}

	public String getDisplayName()
	{
		return displayName;
	}

	public List<Recipe> getRecipes()
	{
		return recipes;
	}

	/** @return filter categories for this skill's tab; empty means no dropdown is shown */
	public List<String> getCategories()
	{
		return categories;
	}
}
