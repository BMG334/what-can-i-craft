package com.whatcanicraft;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Static list of herblore recipes plus the logic for matching them against a set of owned item names.
 * Items are matched by name (case-insensitive) so this doesn't depend on item ID constants.
 */
public final class HerbloreRecipes
{
	private HerbloreRecipes()
	{
	}

	/** A recipe the player can make, with a human-readable ingredient string. */
	public static class Match
	{
		private final HerbloreRecipe recipe;
		private final String ingredients;

		Match(HerbloreRecipe recipe, String ingredients)
		{
			this.recipe = recipe;
			this.ingredients = ingredients;
		}

		public String getPotionName()
		{
			return recipe.getPotionName();
		}

		public int getLevel()
		{
			return recipe.getLevel();
		}

		public String getIngredients()
		{
			return ingredients;
		}
	}

	public static final List<HerbloreRecipe> ALL = Collections.unmodifiableList(Arrays.asList(
		new HerbloreRecipe("Attack potion", 3, "Guam leaf", "Eye of newt"),
		new HerbloreRecipe("Antipoison", 5, "Marrentill", "Unicorn horn dust"),
		new HerbloreRecipe("Strength potion", 12, "Tarromin", "Limpwurt root"),
		new HerbloreRecipe("Serum 207", 15, "Tarromin", "Ashes"),
		new HerbloreRecipe("Compost potion", 22, "Harralander", "Volcanic ash"),
		new HerbloreRecipe("Restore potion", 22, "Harralander", "Red spiders' eggs"),
		new HerbloreRecipe("Guthix balance", 22, "Harralander", "Red spiders' eggs", "Garlic", "Silver dust"),
		new HerbloreRecipe("Energy potion", 26, "Harralander", "Chocolate dust"),
		new HerbloreRecipe("Defence potion", 30, "Ranarr weed", "White berries"),
		new HerbloreRecipe("Agility potion", 34, "Toadflax", "Toad's legs"),
		new HerbloreRecipe("Combat potion", 36, "Harralander", "Goat horn dust"),
		new HerbloreRecipe("Prayer potion", 38, "Ranarr weed", "Snape grass"),
		new HerbloreRecipe("Super attack", 45, "Irit leaf", "Eye of newt"),
		new HerbloreRecipe("Superantipoison", 48, "Irit leaf", "Unicorn horn dust"),
		new HerbloreRecipe("Fishing potion", 50, "Avantoe", "Snape grass"),
		new HerbloreRecipe("Super energy", 52, "Avantoe", "Mort myre fungus"),
		new HerbloreRecipe("Hunter potion", 53, "Avantoe", "Kebbit teeth dust"),
		new HerbloreRecipe("Super strength", 55, "Kwuarm", "Limpwurt root"),
		new HerbloreRecipe("Weapon poison", 60, "Kwuarm", "Dragon scale dust"),
		new HerbloreRecipe("Menaphite remedy", 62, "Kwuarm", "Lily of the sands"),
		new HerbloreRecipe("Super restore", 63, "Snapdragon", "Red spiders' eggs"),
		new HerbloreRecipe("Super defence", 66, "Cadantine", "White berries"),
		new HerbloreRecipe("Antidote+", 68, "Toadflax", "Yew roots"),
		new HerbloreRecipe("Antifire potion", 69, "Lantadyme", "Dragon scale dust"),
		new HerbloreRecipe("Ranging potion", 72, "Dwarf weed", "Wine of zamorak"),
		new HerbloreRecipe("Magic potion", 76, "Lantadyme", "Potato cactus"),
		new HerbloreRecipe("Zamorak brew", 78, "Torstol", "Jangerberries"),
		new HerbloreRecipe("Antidote++", 79, "Irit leaf", "Magic roots"),
		new HerbloreRecipe("Saradomin brew", 81, "Toadflax", "Crushed nest")
	));

	/**
	 * @param ownedNames lower-case names of items the player owns
	 * @return every recipe the owned items can satisfy, sorted by required level
	 */
	public static List<Match> findCraftable(Set<String> ownedNames)
	{
		List<Match> matches = new ArrayList<>();

		for (HerbloreRecipe recipe : ALL)
		{
			if (!hasAll(ownedNames, recipe.getSecondaries()))
			{
				continue;
			}

			List<String> parts = new ArrayList<>();
			if (has(ownedNames, recipe.getHerb()) && has(ownedNames, HerbloreRecipe.VIAL_OF_WATER))
			{
				parts.add(recipe.getHerb());
				parts.add(HerbloreRecipe.VIAL_OF_WATER);
			}
			else if (has(ownedNames, recipe.getUnfinishedPotion()))
			{
				parts.add(recipe.getUnfinishedPotion());
			}
			else
			{
				continue;
			}
			parts.addAll(recipe.getSecondaries());

			matches.add(new Match(recipe, String.join(", ", parts)));
		}

		matches.sort((a, b) -> Integer.compare(a.getLevel(), b.getLevel()));
		return matches;
	}

	private static boolean has(Set<String> owned, String name)
	{
		return owned.contains(name.toLowerCase(Locale.ROOT));
	}

	private static boolean hasAll(Set<String> owned, List<String> names)
	{
		for (String name : names)
		{
			if (!has(owned, name))
			{
				return false;
			}
		}
		return true;
	}
}
