package com.whatcanicraft;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A recipe for any skill. A recipe has one or more "options"; each option is a list of item names
 * that must ALL be owned. The recipe is craftable if any single option is fully satisfied.
 * (Herblore uses two options: herb + vial of water, or the unfinished potion.)
 */
public class Recipe
{
	private final String category;
	private final String name;
	private final int level;
	private final List<List<String>> options;

	/**
	 * @param category category used by the panel's filter dropdown, or null if the skill has none
	 * @param options  alternative ingredient lists; any one fully owned list makes the recipe craftable
	 */
	public Recipe(String category, String name, int level, List<List<String>> options)
	{
		this.category = category;
		this.name = name;
		this.level = level;
		this.options = options.stream().map(List::copyOf).collect(Collectors.toUnmodifiableList());
	}

	/** Single-option recipe: every listed item is required. */
	public static Recipe of(String category, String name, int level, String... ingredients)
	{
		return new Recipe(category, name, level, List.of(Arrays.asList(ingredients)));
	}

	/** @return a match using the first fully owned option, or null if the recipe isn't craftable */
	public Match match(Set<String> ownedNames)
	{
		for (List<String> option : options)
		{
			if (option.stream().allMatch(item -> ownedNames.contains(item.toLowerCase(Locale.ROOT))))
			{
				return new Match(this, String.join(", ", option));
			}
		}
		return null;
	}

	public String getCategory()
	{
		return category;
	}

	public String getName()
	{
		return name;
	}

	public int getLevel()
	{
		return level;
	}

	/** A recipe the player can make, with the ingredient list that satisfied it. */
	public static class Match
	{
		private final Recipe recipe;
		private final String ingredients;

		private Match(Recipe recipe, String ingredients)
		{
			this.recipe = recipe;
			this.ingredients = ingredients;
		}

		public Recipe getRecipe()
		{
			return recipe;
		}

		public String getIngredients()
		{
			return ingredients;
		}
	}
}
