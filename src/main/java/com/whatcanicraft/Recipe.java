package com.whatcanicraft;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * A recipe for any skill. A recipe has one or more "options"; each option is a list of item names
 * that must ALL be owned. The recipe is craftable if any single option is fully satisfied.
 * (Herblore uses two options: herb + vial of water, or the unfinished potion.)
 * A recipe may optionally carry a category, used by the panel's filter dropdown.
 */
public class Recipe
{
	private final String name;
	private final int level;
	private final List<List<String>> options;
	private final String category;

	/** Single-option recipe: every listed item is required. */
	public Recipe(String name, int level, String... ingredients)
	{
		this(name, level, Collections.singletonList(Arrays.asList(ingredients)));
	}

	/** Multi-option recipe: any one option being fully owned is enough. */
	public Recipe(String name, int level, List<List<String>> options)
	{
		this(name, level, options, null);
	}

	private Recipe(String name, int level, List<List<String>> options, String category)
	{
		this.name = name;
		this.level = level;
		this.category = category;
		List<List<String>> copy = new ArrayList<>();
		for (List<String> option : options)
		{
			copy.add(Collections.unmodifiableList(new ArrayList<>(option)));
		}
		this.options = Collections.unmodifiableList(copy);
	}

	/** Returns a copy of this recipe tagged with a category. */
	public Recipe inCategory(String category)
	{
		return new Recipe(name, level, options, category);
	}

	/** @return the category, or null if the recipe is uncategorised */
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

	public List<List<String>> getOptions()
	{
		return options;
	}
}
