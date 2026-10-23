package com.whatcanicraft;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/** Matches recipes against owned item names (case-insensitive, name-based). */
public final class RecipeMatcher
{
	private RecipeMatcher()
	{
	}

	/**
	 * @param ownedNames lower-case names of items the player owns
	 * @return craftable recipes sorted by required level
	 */
	public static List<RecipeMatch> findCraftable(List<Recipe> recipes, Set<String> ownedNames)
	{
		List<RecipeMatch> matches = new ArrayList<>();

		for (Recipe recipe : recipes)
		{
			for (List<String> option : recipe.getOptions())
			{
				if (ownsAll(ownedNames, option))
				{
					matches.add(new RecipeMatch(recipe.getName(), recipe.getLevel(), String.join(", ", option)));
					break; // first satisfied option wins
				}
			}
		}

		matches.sort((a, b) -> Integer.compare(a.getLevel(), b.getLevel()));
		return matches;
	}

	private static boolean ownsAll(Set<String> owned, List<String> names)
	{
		for (String name : names)
		{
			if (!owned.contains(name.toLowerCase(Locale.ROOT)))
			{
				return false;
			}
		}
		return true;
	}
}
