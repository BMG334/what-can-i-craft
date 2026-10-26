package com.whatcanicraft;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * A recipe for any skill. A recipe has one or more "options" (alternative ways to make it), and each
 * option is a list of ingredients with an XP value. Herblore uses two options for most potions:
 * herb + vial of water, or the unfinished potion.
 */
public class Recipe
{
	private final String category;
	private final String name;
	private final int level;
	private final List<Option> options;

	/**
	 * @param category category used by the panel's filter dropdown, or null if the skill has none
	 * @param options  alternative ways to make the recipe, in order of preference
	 */
	public Recipe(String category, String name, int level, List<Option> options)
	{
		this.category = category;
		this.name = name;
		this.level = level;
		this.options = List.copyOf(options);
	}

	/** Single-option recipe: one of each listed item is required. XP 0 means no XP data. */
	public static Recipe of(String category, String name, int level, double xp, String... items)
	{
		return of(category, name, level, xp, Ingredient.items(items));
	}

	/** Single-option recipe with explicit ingredients (quantities, dosed potions, per-dose items). */
	public static Recipe of(String category, String name, int level, double xp, List<Ingredient> ingredients)
	{
		return new Recipe(category, name, level, List.of(new Option(xp, ingredients)));
	}

	/** A recipe made from any one of several ingredient lists, all giving the same XP. */
	public static Recipe anyOf(String category, String name, int level, double xp, List<List<Ingredient>> alternatives)
	{
		return new Recipe(category, name, level, alternatives.stream()
			.map(ingredients -> new Option(xp, ingredients))
			.collect(Collectors.toList()));
	}

	/**
	 * Works out how many times this recipe can be made from the owned items, and the XP that gives.
	 * Options are used in order; items consumed by one option aren't available to the next.
	 *
	 * @return the result, or null if the recipe can't be made even once
	 */
	public Match match(Stock owned)
	{
		Stock remaining = owned.copy();
		int total = 0;
		double xp = 0;
		String ingredients = null;

		for (Option option : options)
		{
			boolean dosed = option.isDosed();

			// A dosed potion is tried at 4, 3, 2, then 1 doses; non-dosed options run once
			for (int doses = dosed ? 4 : 0; doses >= (dosed ? 1 : 0); doses--)
			{
				int crafts = option.maxCrafts(remaining, doses);
				if (crafts == 0)
				{
					continue;
				}

				option.consume(remaining, doses, crafts);
				total += crafts;
				xp += crafts * option.xp * (dosed ? doses / 4.0 : 1);
				if (ingredients == null)
				{
					ingredients = option.describe(doses);
				}
			}
		}

		return total == 0 ? null : new Match(this, ingredients, total, xp);
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

	/**
	 * One way to make a recipe: ingredients plus the XP gained per craft.
	 * If the option has a dosed potion, XP is the amount for a 4-dose potion and scales with the doses used
	 * (the wiki lists these as "4 x per-dose XP"). An XP of 0 means no XP data.
	 */
	public static class Option
	{
		private final double xp;
		private final List<Ingredient> ingredients;

		public Option(double xp, List<Ingredient> ingredients)
		{
			this.xp = xp;
			this.ingredients = List.copyOf(ingredients);
		}

		private boolean isDosed()
		{
			return ingredients.stream().anyMatch(i -> i.dosed);
		}

		/** How many times this option can run with the stock that's left. */
		private int maxCrafts(Stock stock, int doses)
		{
			int crafts = Integer.MAX_VALUE;
			for (Ingredient ingredient : ingredients)
			{
				crafts = Math.min(crafts, ingredient.available(stock, doses) / ingredient.needed(doses));
			}
			return crafts;
		}

		private void consume(Stock stock, int doses, int crafts)
		{
			for (Ingredient ingredient : ingredients)
			{
				ingredient.consume(stock, doses, ingredient.needed(doses) * crafts);
			}
		}

		/** Ingredient text for one craft, e.g. "Super energy(3), Amylase crystal x3". */
		private String describe(int doses)
		{
			return ingredients.stream().map(i -> i.label(doses)).collect(Collectors.joining(", "));
		}
	}

	/**
	 * One required item: a name plus how many are needed per craft.
	 * Potions (dosed potions, or names ending in a dose like "Super attack(4)") can also be supplied from
	 * potion storage: stored doses count towards the potion at any dose size.
	 */
	public static class Ingredient
	{
		private static final Pattern DOSE_SUFFIX = Pattern.compile("^(.*)\\(([1-4])\\)$");

		private final String name;
		private final int quantity;
		private final boolean dosed;
		private final boolean perDose;

		/** Lower-case potion name without dose suffix (e.g. "super attack"), or null if this isn't a potion. */
		private final String potionKey;
		/** Dose size of a fixed-dose potion name like "Super attack(4)"; 0 otherwise. */
		private final int fixedDoses;

		private Ingredient(String name, int quantity, boolean dosed, boolean perDose)
		{
			this.name = name;
			this.quantity = quantity;
			this.dosed = dosed;
			this.perDose = perDose;

			Matcher suffix = DOSE_SUFFIX.matcher(name);
			if (dosed)
			{
				potionKey = name.toLowerCase(Locale.ROOT);
				fixedDoses = 0;
			}
			else if (!perDose && suffix.matches())
			{
				potionKey = suffix.group(1).toLowerCase(Locale.ROOT);
				fixedDoses = Integer.parseInt(suffix.group(2));
			}
			else
			{
				potionKey = null;
				fixedDoses = 0;
			}
		}

		/** A plain item, one required. */
		public static Ingredient item(String name)
		{
			return item(name, 1);
		}

		/** A plain item, the given quantity required. Names ending in a dose, like "Super attack(4)", are potions. */
		public static Ingredient item(String name, int quantity)
		{
			return new Ingredient(name, quantity, false, false);
		}

		/**
		 * A potion in any dose count: matches "name(4)", "name(3)", "name(2)" or "name(1)".
		 * Its dose count is the recipe's dose count, which scales any perDose ingredients.
		 */
		public static Ingredient potion(String name)
		{
			return new Ingredient(name, 1, true, false);
		}

		/** An item needed once per dose of the recipe's dosed potion (e.g. one amylase crystal per dose). */
		public static Ingredient perDose(String name, int quantityPerDose)
		{
			return new Ingredient(name, quantityPerDose, false, true);
		}

		/** One plain item for each name. */
		public static List<Ingredient> items(String... names)
		{
			return Arrays.stream(names).map(Ingredient::item).collect(Collectors.toList());
		}

		private String itemName(int doses)
		{
			return dosed ? name + "(" + doses + ")" : name;
		}

		/** Lower-case item name used to look the ingredient up in the stock. */
		private String key(int doses)
		{
			return itemName(doses).toLowerCase(Locale.ROOT);
		}

		/** Quantity needed for one craft. */
		private int needed(int doses)
		{
			return perDose ? quantity * doses : quantity;
		}

		/** Dose size of the potion this ingredient needs when the recipe runs at the given dose count. */
		private int potionDoses(int doses)
		{
			return dosed ? doses : fixedDoses;
		}

		/** How many of this ingredient the stock can supply: matching items, plus stored potion doses. */
		private int available(Stock stock, int doses)
		{
			int items = stock.items(key(doses));
			return potionKey == null ? items : items + stock.potionDoses(potionKey) / potionDoses(doses);
		}

		/** Takes units of this ingredient from the stock: matching items first, then potion storage doses. */
		private void consume(Stock stock, int doses, int units)
		{
			int fromItems = Math.min(units, stock.items(key(doses)));
			stock.removeItem(key(doses), fromItems);

			if (units > fromItems)
			{
				stock.removePotionDoses(potionKey, (units - fromItems) * potionDoses(doses));
			}
		}

		private String label(int doses)
		{
			int needed = needed(doses);
			return needed > 1 ? itemName(doses) + " x" + needed : itemName(doses);
		}
	}

	/** A recipe the player can make: how many times, the XP that gives, and the ingredients for one craft. */
	public static class Match
	{
		private final Recipe recipe;
		private final String ingredients;
		private final int count;
		private final double xp;

		private Match(Recipe recipe, String ingredients, int count, double xp)
		{
			this.recipe = recipe;
			this.ingredients = ingredients;
			this.count = count;
			this.xp = xp;
		}

		public Recipe getRecipe()
		{
			return recipe;
		}

		public String getIngredients()
		{
			return ingredients;
		}

		/** @return how many times the recipe can be made */
		public int getCount()
		{
			return count;
		}

		/** @return total XP from making it that many times, or 0 if there's no XP data */
		public double getXp()
		{
			return xp;
		}
	}
}
