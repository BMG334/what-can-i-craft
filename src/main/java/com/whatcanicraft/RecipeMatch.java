package com.whatcanicraft;

/** A recipe the player can make, with the ingredient list that satisfied it. */
public class RecipeMatch
{
	private final String name;
	private final int level;
	private final String ingredients;
	private final String category;

	public RecipeMatch(String name, int level, String ingredients, String category)
	{
		this.name = name;
		this.level = level;
		this.ingredients = ingredients;
		this.category = category;
	}

	public String getName()
	{
		return name;
	}

	public int getLevel()
	{
		return level;
	}

	public String getIngredients()
	{
		return ingredients;
	}

	/** @return the category, or null if the recipe is uncategorised */
	public String getCategory()
	{
		return category;
	}
}
