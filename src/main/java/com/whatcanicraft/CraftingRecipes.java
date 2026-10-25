package com.whatcanicraft;

import java.util.ArrayList;
import java.util.Arrays;
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
	public static final List<String> CATEGORIES = List.of(ARMOR, GEMS, JEWELRY, STAVES, GLASS);

	public static final List<Recipe> ALL = List.of(
		// Leather
		sewn("Leather gloves", 1, "Leather"),
		sewn("Leather boots", 7, "Leather"),
		sewn("Leather cowl", 9, "Leather"),
		sewn("Leather vambraces", 11, "Leather"),
		sewn("Leather body", 14, "Leather"),
		sewn("Leather chaps", 18, "Leather"),
		sewn("Hardleather body", 28, "Hard leather"),
		sewn("Coif", 38, "Leather"),
		sewn("Studded body", 41, "Leather body", "Steel studs"),
		sewn("Studded chaps", 44, "Leather chaps", "Steel studs"),

		// Dragonhide
		hide("Green d'hide vambraces", 57, "Green dragon leather", 1),
		hide("Green d'hide chaps", 60, "Green dragon leather", 2),
		hide("Green d'hide body", 63, "Green dragon leather", 3),
		hide("Blue d'hide vambraces", 66, "Blue dragon leather", 1),
		hide("Blue d'hide chaps", 68, "Blue dragon leather", 2),
		hide("Blue d'hide body", 71, "Blue dragon leather", 3),
		hide("Red d'hide vambraces", 73, "Red dragon leather", 1),
		hide("Red d'hide chaps", 75, "Red dragon leather", 2),
		hide("Red d'hide body", 77, "Red dragon leather", 3),
		hide("Black d'hide vambraces", 79, "Black dragon leather", 1),
		hide("Black d'hide chaps", 82, "Black dragon leather", 2),
		hide("Black d'hide body", 84, "Black dragon leather", 3),

		// Gem cutting
		cut("Opal", 1, "Uncut opal"),
		cut("Jade", 13, "Uncut jade"),
		cut("Red topaz", 16, "Uncut red topaz"),
		cut("Sapphire", 20, "Uncut sapphire"),
		cut("Emerald", 27, "Uncut emerald"),
		cut("Ruby", 34, "Uncut ruby"),
		cut("Diamond", 43, "Uncut diamond"),
		cut("Dragonstone", 55, "Uncut dragonstone"),
		cut("Onyx", 67, "Uncut onyx"),
		cut("Zenyte", 89, "Uncut zenyte"),

		// Jewellery (gold bar + mould [+ cut gem])
		Recipe.of(JEWELRY, "Gold ring", 5, "Gold bar", "Ring mould"),
		Recipe.of(JEWELRY, "Gold necklace", 6, "Gold bar", "Necklace mould"),
		Recipe.of(JEWELRY, "Gold amulet (u)", 8, "Gold bar", "Amulet mould"),
		jewellery("Sapphire ring", 20, "Ring mould", "Sapphire"),
		jewellery("Sapphire necklace", 22, "Necklace mould", "Sapphire"),
		jewellery("Sapphire amulet (u)", 24, "Amulet mould", "Sapphire"),
		jewellery("Emerald ring", 27, "Ring mould", "Emerald"),
		jewellery("Emerald necklace", 29, "Necklace mould", "Emerald"),
		jewellery("Emerald amulet (u)", 31, "Amulet mould", "Emerald"),
		jewellery("Ruby ring", 34, "Ring mould", "Ruby"),
		jewellery("Ruby necklace", 40, "Necklace mould", "Ruby"),
		jewellery("Ruby amulet (u)", 50, "Amulet mould", "Ruby"),
		jewellery("Diamond ring", 43, "Ring mould", "Diamond"),
		jewellery("Diamond necklace", 56, "Necklace mould", "Diamond"),
		jewellery("Diamond amulet (u)", 70, "Amulet mould", "Diamond"),
		jewellery("Dragonstone ring", 55, "Ring mould", "Dragonstone"),
		jewellery("Dragon necklace", 72, "Necklace mould", "Dragonstone"),
		jewellery("Dragonstone amulet (u)", 80, "Amulet mould", "Dragonstone"),
		jewellery("Onyx ring", 67, "Ring mould", "Onyx"),
		jewellery("Onyx necklace", 82, "Necklace mould", "Onyx"),
		jewellery("Onyx amulet (u)", 90, "Amulet mould", "Onyx"),

		// Battlestaves (battlestaff + orb)
		Recipe.of(STAVES, "Water battlestaff", 54, "Battlestaff", "Water orb"),
		Recipe.of(STAVES, "Earth battlestaff", 58, "Battlestaff", "Earth orb"),
		Recipe.of(STAVES, "Fire battlestaff", 62, "Battlestaff", "Fire orb"),
		Recipe.of(STAVES, "Air battlestaff", 66, "Battlestaff", "Air orb"),

		// Glassblowing
		glass("Beer glass", 1),
		glass("Empty candle lantern", 4),
		glass("Empty oil lamp", 12),
		glass("Vial", 33),
		glass("Empty fishbowl", 42),
		glass("Unpowered orb", 46),
		glass("Lantern lens", 49)
	);

	/** Needle + thread + materials (leather and dragonhide armour). */
	private static Recipe sewn(String name, int level, String... materials)
	{
		List<String> items = new ArrayList<>(List.of("Needle", "Thread"));
		items.addAll(Arrays.asList(materials));
		return Recipe.of(ARMOR, name, level, items.toArray(new String[0]));
	}

	/** Needle + thread + dragon leather (vambraces 1, chaps 2, body 3). */
	private static Recipe hide(String name, int level, String leather, int quantity)
	{
		return Recipe.of(ARMOR, name, level,
			List.of(Recipe.Ingredient.item("Needle"), Recipe.Ingredient.item("Thread"), Recipe.Ingredient.item(leather, quantity)));
	}

	/** Chisel + uncut gem. */
	private static Recipe cut(String name, int level, String uncutGem)
	{
		return Recipe.of(GEMS, name, level, "Chisel", uncutGem);
	}

	/** Gold bar + cut gem + mould. */
	private static Recipe jewellery(String name, int level, String mould, String gem)
	{
		return Recipe.of(JEWELRY, name, level, "Gold bar", gem, mould);
	}

	/** Glassblowing pipe + molten glass. */
	private static Recipe glass(String name, int level)
	{
		return Recipe.of(GLASS, name, level, "Glassblowing pipe", "Molten glass");
	}
}
