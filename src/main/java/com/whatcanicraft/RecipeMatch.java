package com.whatcanicraft;

/** A recipe the player can make, with the ingredient list that satisfied it. */
public class RecipeMatch
{
	private final String name;
	private final int level;
	private final String ingredients;

	public RecipeMatch(String name, int level, String ingredients)
	{
		this.name = name;
		this.level = level;
		this.ingredients = ingredients;
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
}
