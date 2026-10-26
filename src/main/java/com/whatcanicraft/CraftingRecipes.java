package com.whatcanicraft;

import static com.whatcanicraft.Recipe.Ingredient.item;

import com.whatcanicraft.Recipe.Ingredient;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Crafting recipe data from the Armour, Glass, Jewellery and Weaponry tables on the OSRS Wiki Crafting page
 * (level, XP, materials). Names must match in-game item names exactly (matching is case-insensitive).
 *
 * Tools and moulds (chisel, hammer, glassblowing pipe, moulds) are required items where the wiki says a recipe
 * needs them. Armour pieces also need a needle and thread, except snelms and crab armour (chisel only).
 * Spinning, weaving and pottery aren't included.
 */
public final class CraftingRecipes
{
	public static final String ARMOR = "Armor";
	public static final String GEMS = "Gems";
	public static final String JEWELRY = "Jewelry";
	public static final String WEAPONRY = "Weaponry";
	public static final String GLASS = "Glass";

	/** Filter categories in dropdown order. */
	public static final List<String> CATEGORIES = List.of(ARMOR, GEMS, JEWELRY, WEAPONRY, GLASS);

	private static final Ingredient NEEDLE = item("Needle");
	private static final Ingredient THREAD = item("Thread");
	private static final Ingredient HAMMER = item("Hammer");

	private static final List<Recipe> ARMOUR_RECIPES = List.of(
		// Leather
		armor("Leather gloves", 1, 13.8, item("Leather")),
		armor("Leather boots", 7, 16.3, item("Leather")),
		armor("Leather cowl", 9, 18.5, item("Leather")),
		armor("Leather vambraces", 11, 22, item("Leather")),
		armor("Leather body", 14, 25, item("Leather")),
		armor("Leather chaps", 18, 27, item("Leather")),
		armor("Hardleather body", 28, 35, item("Hard leather")),
		spikyVambraces(),
		armor("Coif", 38, 37, item("Leather")),
		armor("Hard leather shield", 41, 70, item("Hard leather", 2), item("Oak shield"), item("Bronze nails", 15), HAMMER),
		armor("Studded body", 41, 40, item("Leather body"), item("Steel studs")),
		armor("Studded chaps", 44, 42, item("Leather chaps"), item("Steel studs")),

		// Dragonhides
		armor("Green d'hide vambraces", 57, 62, item("Green dragon leather")),
		armor("Green d'hide chaps", 60, 124, item("Green dragon leather", 2)),
		armor("Green d'hide shield", 62, 124, item("Green dragon leather", 2), item("Maple shield"), item("Steel nails", 15), HAMMER),
		armor("Green d'hide body", 63, 186, item("Green dragon leather", 3)),
		armor("Blue d'hide vambraces", 66, 70, item("Blue dragon leather")),
		armor("Blue d'hide chaps", 68, 140, item("Blue dragon leather", 2)),
		armor("Blue d'hide shield", 69, 140, item("Blue dragon leather", 2), item("Yew shield"), item("Mithril nails", 15), HAMMER),
		armor("Blue d'hide body", 71, 210, item("Blue dragon leather", 3)),
		armor("Red d'hide vambraces", 73, 78, item("Red dragon leather")),
		armor("Red d'hide chaps", 75, 156, item("Red dragon leather", 2)),
		armor("Red d'hide shield", 76, 156, item("Red dragon leather", 2), item("Magic shield"), item("Adamantite nails", 15), HAMMER),
		armor("Red d'hide body", 77, 234, item("Red dragon leather", 3)),
		armor("Black d'hide vambraces", 79, 86, item("Black dragon leather")),
		armor("Black d'hide chaps", 82, 172, item("Black dragon leather", 2)),
		armor("Black d'hide shield", 83, 172, item("Black dragon leather", 2), item("Redwood shield"), item("Rune nails", 15), HAMMER),
		armor("Black d'hide body", 84, 258, item("Black dragon leather", 3)),

		// Snakeskin
		armor("Snakeskin boots", 45, 30, item("Snakeskin", 6)),
		armor("Snakeskin vambraces", 47, 35, item("Snakeskin", 8)),
		armor("Snakeskin bandana", 48, 45, item("Snakeskin", 5)),
		armor("Snakeskin chaps", 51, 50, item("Snakeskin", 12)),
		armor("Snakeskin body", 53, 55, item("Snakeskin", 15)),
		armor("Snakeskin shield", 56, 100, item("Snakeskin", 2), item("Willow shield"), item("Iron nails", 15), HAMMER),

		// Yak hide
		armor("Yak-hide armour (legs)", 43, 32, item("Cured yak-hide")),
		armor("Yak-hide armour (top)", 46, 32, item("Cured yak-hide", 2)),

		// Snelms (chisel + shell)
		chiselled("Pointed blood'n'tar snelm", "Blamish red shell (pointed)"),
		chiselled("Blood'n'tar snelm", "Blamish red shell (round)"),
		chiselled("Broken bark snelm", "Blamish bark shell"),
		chiselled("Pointed bruise blue snelm", "Blamish blue shell (pointed)"),
		chiselled("Bruise blue snelm", "Blamish blue shell (round)"),
		chiselled("Pointed myre snelm", "Blamish myre shell (pointed)"),
		chiselled("Myre snelm", "Blamish myre shell (round)"),
		chiselled("Pointed ochre snelm", "Blamish ochre shell (pointed)"),
		chiselled("Ochre snelm", "Blamish ochre shell (round)"),

		// Crab armour
		chiselled("Crab helmet", "Fresh crab shell"),
		chiselled("Crab claw", "Fresh crab claw"),

		// Xerician robes
		armor("Xerician hat", 14, 66, item("Xerician fabric", 3)),
		armor("Xerician robe", 17, 88, item("Xerician fabric", 4)),
		armor("Xerician top", 22, 110, item("Xerician fabric", 5)),

		// Splitbark armour
		armor("Splitbark gauntlets", 60, 62, item("Bark"), item("Fine cloth")),
		armor("Splitbark boots", 60, 62, item("Bark"), item("Fine cloth")),
		armor("Splitbark helm", 61, 124, item("Bark", 2), item("Fine cloth", 2)),
		armor("Splitbark legs", 62, 186, item("Bark", 3), item("Fine cloth", 3)),
		armor("Splitbark body", 62, 248, item("Bark", 4), item("Fine cloth", 4)),

		// Mixed hide armour
		armor("Mixed hide cape", 68, 62, item("Mixed hide base"), item("Jaguar fur")),
		armor("Mixed hide boots", 69, 75, item("Mixed hide base"), item("Sunlight antelope fur")),
		armor("Mixed hide legs", 71, 210, item("Mixed hide base"), item("Fox fur", 3)),
		armor("Mixed hide top", 72, 150, item("Mixed hide base"), item("Sunlight antelope fur", 2)),

		// Hueycoatl hide armour
		armor("Hueycoatl hide vambraces", 76, 95, item("Hueycoatl hide")),
		armor("Hueycoatl hide coif", 76, 190, item("Hueycoatl hide", 2)),
		armor("Hueycoatl hide chaps", 77, 190, item("Hueycoatl hide", 2)),
		armor("Hueycoatl hide body", 78, 285, item("Hueycoatl hide", 3))
	);

	private static final List<Recipe> GEM_RECIPES = List.of(
		cut("Opal", 1, 15),
		cut("Jade", 13, 20),
		cut("Red topaz", 16, 25),
		cut("Sapphire", 20, 50),
		cut("Emerald", 27, 67.5),
		cut("Ruby", 34, 85),
		cut("Diamond", 43, 107.5),
		cut("Dragonstone", 55, 137.5),
		cut("Onyx", 67, 167.5),
		cut("Zenyte", 89, 50)
	);

	/** Gold and silver jewellery: bar + mould (+ cut gem). */
	private static final List<Recipe> JEWELLERY_RECIPES = List.of(
		// Gold
		jewel("Gold ring", 5, 15, "Gold bar", null, "Ring mould"),
		jewel("Gold necklace", 6, 20, "Gold bar", null, "Necklace mould"),
		jewel("Gold bracelet", 7, 25, "Gold bar", null, "Bracelet mould"),
		jewel("Gold amulet (u)", 8, 30, "Gold bar", null, "Amulet mould"),

		// Gold with cut gems
		jewel("Sapphire ring", 20, 40, "Gold bar", "Sapphire", "Ring mould"),
		jewel("Sapphire necklace", 22, 55, "Gold bar", "Sapphire", "Necklace mould"),
		jewel("Sapphire bracelet", 23, 60, "Gold bar", "Sapphire", "Bracelet mould"),
		jewel("Sapphire amulet (u)", 24, 65, "Gold bar", "Sapphire", "Amulet mould"),
		jewel("Emerald ring", 27, 55, "Gold bar", "Emerald", "Ring mould"),
		jewel("Emerald necklace", 29, 60, "Gold bar", "Emerald", "Necklace mould"),
		jewel("Emerald bracelet", 30, 65, "Gold bar", "Emerald", "Bracelet mould"),
		jewel("Emerald amulet (u)", 31, 70, "Gold bar", "Emerald", "Amulet mould"),
		jewel("Ruby ring", 34, 70, "Gold bar", "Ruby", "Ring mould"),
		jewel("Ruby necklace", 40, 75, "Gold bar", "Ruby", "Necklace mould"),
		jewel("Ruby bracelet", 42, 80, "Gold bar", "Ruby", "Bracelet mould"),
		jewel("Ruby amulet (u)", 50, 85, "Gold bar", "Ruby", "Amulet mould"),
		jewel("Gold tiara", 42, 35, "Gold bar", null, "Tiara mould"),
		jewel("Diamond ring", 43, 85, "Gold bar", "Diamond", "Ring mould"),
		jewel("Diamond necklace", 56, 90, "Gold bar", "Diamond", "Necklace mould"),
		jewel("Diamond bracelet", 58, 95, "Gold bar", "Diamond", "Bracelet mould"),
		jewel("Diamond amulet (u)", 70, 100, "Gold bar", "Diamond", "Amulet mould"),
		jewel("Dragonstone ring", 55, 100, "Gold bar", "Dragonstone", "Ring mould"),
		jewel("Dragon necklace", 72, 105, "Gold bar", "Dragonstone", "Necklace mould"),
		jewel("Dragonstone bracelet", 74, 110, "Gold bar", "Dragonstone", "Bracelet mould"),
		jewel("Dragonstone amulet (u)", 80, 150, "Gold bar", "Dragonstone", "Amulet mould"),
		jewel("Slayer ring", 75, 15, "Gold bar", "Enchanted gem", "Ring mould"),
		jewel("Slayer ring (eternal)", 75, 15, "Gold bar", "Eternal gem", "Ring mould"),
		jewel("Onyx ring", 67, 115, "Gold bar", "Onyx", "Ring mould"),
		jewel("Onyx necklace", 82, 120, "Gold bar", "Onyx", "Necklace mould"),
		jewel("Onyx bracelet", 84, 125, "Gold bar", "Onyx", "Bracelet mould"),
		jewel("Onyx amulet (u)", 90, 165, "Gold bar", "Onyx", "Amulet mould"),
		jewel("Zenyte ring", 89, 150, "Gold bar", "Zenyte", "Ring mould"),
		jewel("Zenyte necklace", 92, 165, "Gold bar", "Zenyte", "Necklace mould"),
		jewel("Zenyte bracelet", 95, 180, "Gold bar", "Zenyte", "Bracelet mould"),
		jewel("Zenyte amulet (u)", 98, 200, "Gold bar", "Zenyte", "Amulet mould"),

		// Silver with semiprecious gems
		jewel("Opal ring", 1, 10, "Silver bar", "Opal", "Ring mould"),
		jewel("Opal necklace", 16, 35, "Silver bar", "Opal", "Necklace mould"),
		jewel("Opal bracelet", 22, 45, "Silver bar", "Opal", "Bracelet mould"),
		jewel("Opal amulet (u)", 27, 55, "Silver bar", "Opal", "Amulet mould"),
		jewel("Jade ring", 13, 32, "Silver bar", "Jade", "Ring mould"),
		jewel("Jade necklace", 25, 54, "Silver bar", "Jade", "Necklace mould"),
		jewel("Jade bracelet", 29, 60, "Silver bar", "Jade", "Bracelet mould"),
		jewel("Jade amulet (u)", 34, 70, "Silver bar", "Jade", "Amulet mould"),
		jewel("Topaz ring", 16, 35, "Silver bar", "Red topaz", "Ring mould"),
		jewel("Topaz necklace", 32, 70, "Silver bar", "Red topaz", "Necklace mould"),
		jewel("Topaz bracelet", 38, 75, "Silver bar", "Red topaz", "Bracelet mould"),
		jewel("Topaz amulet (u)", 45, 80, "Silver bar", "Red topaz", "Amulet mould"),

		// Silver
		jewel("Unstrung symbol", 16, 50, "Silver bar", null, "Holy mould"),
		jewel("Unstrung emblem", 17, 50, "Silver bar", null, "Unholy mould"),
		jewel("Tiara", 23, 52.5, "Silver bar", null, "Tiara mould")
	);

	private static final List<Recipe> WEAPONRY_RECIPES = List.of(
		// Silver weapons
		Recipe.of(WEAPONRY, "Silver sickle", 18, 50, "Silver bar", "Sickle mould"),
		Recipe.of(WEAPONRY, "Silver bolts (unf)", 21, 50, "Silver bar", "Bolt mould"),

		// Battlestaves (battlestaff + charged orb)
		Recipe.of(WEAPONRY, "Water battlestaff", 54, 100, "Battlestaff", "Water orb"),
		Recipe.of(WEAPONRY, "Earth battlestaff", 58, 112.5, "Battlestaff", "Earth orb"),
		Recipe.of(WEAPONRY, "Fire battlestaff", 62, 125, "Battlestaff", "Fire orb"),
		Recipe.of(WEAPONRY, "Air battlestaff", 66, 137.5, "Battlestaff", "Air orb"),

		// Amethyst (chisel + amethyst)
		Recipe.of(WEAPONRY, "Amethyst bolt tips", 83, 60, "Chisel", "Amethyst"),
		Recipe.of(WEAPONRY, "Amethyst arrowtips", 85, 60, "Chisel", "Amethyst"),
		Recipe.of(WEAPONRY, "Amethyst javelin tips", 87, 60, "Chisel", "Amethyst"),
		Recipe.of(WEAPONRY, "Amethyst dart tip", 89, 60, "Chisel", "Amethyst")
	);

	private static final List<Recipe> GLASS_RECIPES = List.of(
		blown("Beer glass", 1, 17.5),
		blown("Empty candle lantern", 4, 19),
		blown("Empty oil lamp", 12, 25),
		Recipe.of(GLASS, "Oil lantern", 26, 50, "Empty oil lamp", "Oil lantern frame"),
		blown("Vial", 33, 35),
		blown("Fishbowl", 42, 42.5),
		blown("Unpowered orb", 46, 52.5),
		blown("Lantern lens", 49, 55),
		blown("Empty light orb", 87, 70),
		Recipe.of(GLASS, "Light orb", 87, 104, "Empty light orb", "Cave goblin wire")
	);

	public static final List<Recipe> ALL = Stream.of(
			ARMOUR_RECIPES, GEM_RECIPES, JEWELLERY_RECIPES, WEAPONRY_RECIPES, GLASS_RECIPES)
		.flatMap(List::stream)
		.collect(Collectors.toUnmodifiableList());

	private CraftingRecipes()
	{
	}

	/** Sewn armour: a needle and thread, plus the given materials and tools. */
	private static Recipe armor(String name, int level, double xp, Ingredient... materials)
	{
		return Recipe.of(ARMOR, name, level, xp, sewing(materials));
	}

	/** A needle and thread followed by the given ingredients. */
	private static List<Ingredient> sewing(Ingredient... materials)
	{
		List<Ingredient> ingredients = new ArrayList<>(List.of(NEEDLE, THREAD));
		ingredients.addAll(Arrays.asList(materials));
		return ingredients;
	}

	/** Spiky vambraces: kebbit claws attached to any vambraces, leather or dragonhide. */
	private static Recipe spikyVambraces()
	{
		List<List<Ingredient>> alternatives = Stream.of(
				"Leather vambraces", "Green d'hide vambraces", "Blue d'hide vambraces",
				"Red d'hide vambraces", "Black d'hide vambraces")
			.map(vambraces -> sewing(item(vambraces), item("Kebbit claws")))
			.collect(Collectors.toList());
		return Recipe.anyOf(ARMOR, "Spiky vambraces", 32, 5.5, alternatives);
	}

	/** Snelms and crab armour: a chisel and a shell, with no needle and thread. */
	private static Recipe chiselled(String name, String shell)
	{
		return Recipe.of(ARMOR, name, 15, 32.5, "Chisel", shell);
	}

	/** Gem cutting: chisel + uncut gem. */
	private static Recipe cut(String gem, int level, double xp)
	{
		return Recipe.of(GEMS, gem, level, xp, "Chisel", "Uncut " + gem.toLowerCase(Locale.ROOT));
	}

	/** Jewellery: bar + mould, plus a cut gem if one is given. */
	private static Recipe jewel(String name, int level, double xp, String bar, String gem, String mould)
	{
		return gem == null
			? Recipe.of(JEWELRY, name, level, xp, bar, mould)
			: Recipe.of(JEWELRY, name, level, xp, bar, gem, mould);
	}

	/** Glassblowing: pipe + molten glass. */
	private static Recipe blown(String name, int level, double xp)
	{
		return Recipe.of(GLASS, name, level, xp, "Glassblowing pipe", "Molten glass");
	}
}
