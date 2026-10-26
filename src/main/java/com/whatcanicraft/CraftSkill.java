package com.whatcanicraft;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/** The skills shown as tabs in the side panel, in tab order. */
public enum CraftSkill
{
	HERBLORE("Herblore", HerbloreRecipes.ALL, List.of(), true),
	CRAFTING("Crafting", CraftingRecipes.ALL, CraftingRecipes.CATEGORIES, false),
	COOKING("Cooking", CookingRecipes.ALL, CookingRecipes.CATEGORIES, false);

	private final String displayName;
	private final List<Recipe> recipes;
	private final List<String> categories;
	private final boolean usesPotionStorage;

	CraftSkill(String displayName, List<Recipe> recipes, List<String> categories, boolean usesPotionStorage)
	{
		this.displayName = displayName;
		this.recipes = recipes;
		this.categories = categories;
		this.usesPotionStorage = usesPotionStorage;
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

	/** @return true if this skill's recipes can use potions, so reading potion storage is worthwhile */
	public boolean usesPotionStorage()
	{
		return usesPotionStorage;
	}

	/** @return this skill's craftable recipes, sorted by required level */
	public List<Recipe.Match> findCraftable(Stock owned)
	{
		return recipes.stream()
			.map(recipe -> recipe.match(owned))
			.filter(Objects::nonNull)
			.sorted(Comparator.comparingInt(match -> match.getRecipe().getLevel()))
			.collect(Collectors.toList());
	}
}
