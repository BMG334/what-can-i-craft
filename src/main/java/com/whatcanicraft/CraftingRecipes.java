package com.whatcanicraft;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Crafting recipe data (leather, dragonhide, gem cutting, jewellery, battlestaves, glassblowing).
 * Names must match in-game item names exactly. Tools (needle, chisel, moulds, pipe) are required items.
 */
public final class CraftingRecipes
{
	private CraftingRecipes()
	{
	}

	public static final String ARMOR = "Armor";
	public static final String GEMS = "Gems";
	public static final String JEWELRY = "Jewelry";
	public static final String STAVES = "Staves";
	public static final String GLASS = "Glass";

	/** Filter categories in dropdown order. */
	public static final List<String> CATEGORIES = Collections.unmodifiableList(
		Arrays.asList(ARMOR, GEMS, JEWELRY, STAVES, GLASS));

	public static final List<Recipe> ALL;

	static
	{
		List<Recipe> r = new ArrayList<>();

		// Leather
		r.add(sewn("Leather gloves", 1, "Leather"));
		r.add(sewn("Leather boots", 7, "Leather"));
		r.add(sewn("Leather cowl", 9, "Leather"));
		r.add(sewn("Leather vambraces", 11, "Leather"));
		r.add(sewn("Leather body", 14, "Leather"));
		r.add(sewn("Leather chaps", 18, "Leather"));
		r.add(sewn("Hardleather body", 28, "Hard leather"));
		r.add(sewn("Coif", 38, "Leather"));
		r.add(sewn("Studded body", 41, "Leather body", "Steel studs"));
		r.add(sewn("Studded chaps", 44, "Leather chaps", "Steel studs"));

		// Dragonhide
		r.add(sewn("Green d'hide vambraces", 57, "Green dragon leather"));
		r.add(sewn("Green d'hide chaps", 60, "Green dragon leather"));
		r.add(sewn("Green d'hide body", 63, "Green dragon leather"));
		r.add(sewn("Blue d'hide vambraces", 66, "Blue dragon leather"));
		r.add(sewn("Blue d'hide chaps", 68, "Blue dragon leather"));
		r.add(sewn("Blue d'hide body", 71, "Blue dragon leather"));
		r.add(sewn("Red d'hide vambraces", 73, "Red dragon leather"));
		r.add(sewn("Red d'hide chaps", 75, "Red dragon leather"));
		r.add(sewn("Red d'hide body", 77, "Red dragon leather"));
		r.add(sewn("Black d'hide vambraces", 79, "Black dragon leather"));
		r.add(sewn("Black d'hide chaps", 82, "Black dragon leather"));
		r.add(sewn("Black d'hide body", 84, "Black dragon leather"));

		// Gem cutting
		r.add(cut("Opal", 1, "Uncut opal"));
		r.add(cut("Jade", 13, "Uncut jade"));
		r.add(cut("Red topaz", 16, "Uncut red topaz"));
		r.add(cut("Sapphire", 20, "Uncut sapphire"));
		r.add(cut("Emerald", 27, "Uncut emerald"));
		r.add(cut("Ruby", 34, "Uncut ruby"));
		r.add(cut("Diamond", 43, "Uncut diamond"));
		r.add(cut("Dragonstone", 55, "Uncut dragonstone"));
		r.add(cut("Onyx", 67, "Uncut onyx"));
		r.add(cut("Zenyte", 89, "Uncut zenyte"));

		// Jewellery (gold bar + mould [+ cut gem])
		r.add(recipe(JEWELRY, "Gold ring", 5, "Gold bar", "Ring mould"));
		r.add(recipe(JEWELRY, "Gold necklace", 6, "Gold bar", "Necklace mould"));
		r.add(recipe(JEWELRY, "Gold amulet (u)", 8, "Gold bar", "Amulet mould"));
		r.add(jewellery("Sapphire ring", 20, "Ring mould", "Sapphire"));
		r.add(jewellery("Sapphire necklace", 22, "Necklace mould", "Sapphire"));
		r.add(jewellery("Sapphire amulet (u)", 24, "Amulet mould", "Sapphire"));
		r.add(jewellery("Emerald ring", 27, "Ring mould", "Emerald"));
		r.add(jewellery("Emerald necklace", 29, "Necklace mould", "Emerald"));
		r.add(jewellery("Emerald amulet (u)", 31, "Amulet mould", "Emerald"));
		r.add(jewellery("Ruby ring", 34, "Ring mould", "Ruby"));
		r.add(jewellery("Ruby necklace", 40, "Necklace mould", "Ruby"));
		r.add(jewellery("Ruby amulet (u)", 50, "Amulet mould", "Ruby"));
		r.add(jewellery("Diamond ring", 43, "Ring mould", "Diamond"));
		r.add(jewellery("Diamond necklace", 56, "Necklace mould", "Diamond"));
		r.add(jewellery("Diamond amulet (u)", 70, "Amulet mould", "Diamond"));
		r.add(jewellery("Dragonstone ring", 55, "Ring mould", "Dragonstone"));
		r.add(jewellery("Dragon necklace", 72, "Necklace mould", "Dragonstone"));
		r.add(jewellery("Dragonstone amulet (u)", 80, "Amulet mould", "Dragonstone"));
		r.add(jewellery("Onyx ring", 67, "Ring mould", "Onyx"));
		r.add(jewellery("Onyx necklace", 82, "Necklace mould", "Onyx"));
		r.add(jewellery("Onyx amulet (u)", 90, "Amulet mould", "Onyx"));

		// Battlestaves (battlestaff + orb)
		r.add(recipe(STAVES, "Water battlestaff", 54, "Battlestaff", "Water orb"));
		r.add(recipe(STAVES, "Earth battlestaff", 58, "Battlestaff", "Earth orb"));
		r.add(recipe(STAVES, "Fire battlestaff", 62, "Battlestaff", "Fire orb"));
		r.add(recipe(STAVES, "Air battlestaff", 66, "Battlestaff", "Air orb"));

		// Glassblowing
		r.add(glass("Beer glass", 1));
		r.add(glass("Empty candle lantern", 4));
		r.add(glass("Empty oil lamp", 12));
		r.add(glass("Vial", 33));
		r.add(glass("Empty fishbowl", 42));
		r.add(glass("Unpowered orb", 46));
		r.add(glass("Lantern lens", 49));

		ALL = Collections.unmodifiableList(r);
	}

	/** Needle + thread + materials (leather and dragonhide armour). */
	private static Recipe sewn(String name, int level, String... materials)
	{
		List<String> items = new ArrayList<>(Arrays.asList("Needle", "Thread"));
		items.addAll(Arrays.asList(materials));
		return recipe(ARMOR, name, level, items.toArray(new String[0]));
	}

	/** Chisel + uncut gem. */
	private static Recipe cut(String name, int level, String uncutGem)
	{
		return recipe(GEMS, name, level, "Chisel", uncutGem);
	}

	/** Gold bar + cut gem + mould. */
	private static Recipe jewellery(String name, int level, String mould, String gem)
	{
		return recipe(JEWELRY, name, level, "Gold bar", gem, mould);
	}

	/** Glassblowing pipe + molten glass. */
	private static Recipe glass(String name, int level)
	{
		return recipe(GLASS, name, level, "Glassblowing pipe", "Molten glass");
	}

	private static Recipe recipe(String category, String name, int level, String... items)
	{
		return new Recipe(name, level, items).inCategory(category);
	}
}
