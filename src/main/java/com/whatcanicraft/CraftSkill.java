package com.whatcanicraft;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

	/** @return filter categories for this skill's tab; empty means no dropdown is shown */
	public List<String> getCategories()
	{
		return categories;
	}

	/** @return this skill's craftable recipes, sorted by required level */
	public List<Recipe.Match> findCraftable(Stock owned)
	{
		List<Recipe.Match> matches = new ArrayList<>();
		for (Recipe recipe : recipes)
		{
			Recipe.Match match = recipe.match(owned);
			if (match != null)
			{
				matches.add(match);
			}
		}
		matches.sort(Comparator.comparingInt(match -> match.getRecipe().getLevel()));
		return matches;
	}
}
