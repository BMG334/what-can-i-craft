package com.whatcanicraft;

import com.whatcanicraft.Recipe.Ingredient;
import com.whatcanicraft.Recipe.Option;
import java.util.ArrayList;
import java.util.List;

/**
 * Herblore potion data, taken from the Potions table on the OSRS Wiki Herblore page (level, XP, ingredients).
 * Names must match in-game item names exactly (matching is case-insensitive).
 *
 * Recipe shapes used below:
 *  - brew():  base + herb -> unfinished potion, then + secondaries. Craftable from the raw
 *             ingredients OR from the unfinished potion alone.
 *  - mix():   a single list of items that must all be owned (one-step recipes, fixed-dose upgrades).
 *  - upgrade(): turns a finished potion into a better one using an ingredient needed once PER DOSE
 *             (e.g. one amylase crystal per dose of Stamina potion). The potion can be 4, 3, 2 or 1 doses,
 *             and the ingredient quantity scales with it, so a 3-dose potion needs 3 of the ingredient.
 *
 * XP is the amount for finishing one potion. For per-dose upgrades it is the 4-dose total and scales with doses.
 * Finished potions are named by dose in game, e.g. "Super restore(4)". A freshly mixed potion has 3 doses.
 */
public final class HerbloreRecipes
{
	private static final String VIAL_OF_WATER = "Vial of water";
	private static final String COCONUT_MILK = "Coconut milk";
	private static final String VIAL_OF_BLOOD = "Vial of blood";
	private static final String CRYSTAL_DUST = "Crystal dust";

	public static final List<Recipe> ALL = List.of(
		// Vial of water + herb + secondary
		potion("Attack potion", 3, 25, "Guam leaf", "Eye of newt"),
		potion("Antipoison", 5, 37.5, "Marrentill", "Unicorn horn dust"),
		brew("Relicym's balm", 8, 40, VIAL_OF_WATER, "Rogue's purse", "Relicym's balm (unf)", "Snake weed"),
		potion("Strength potion", 12, 50, "Tarromin", "Limpwurt root"),
		potion("Serum 207", 15, 50, "Tarromin", "Ashes"),
		recipe("Guthix rest tea", 18, new Option(59, List.of(
			Ingredient.item("Cup of hot water"), Ingredient.item("Harralander"),
			Ingredient.item("Guam leaf", 2), Ingredient.item("Marrentill")))),
		potion("Compost potion", 22, 60, "Harralander", "Volcanic ash"),
		potion("Restore potion", 22, 62.5, "Harralander", "Red spiders' eggs"),
		mix("Guthix balance", 22, 50, "Restore potion(4)", "Garlic", "Silver dust"),
		potion("Blamish oil", 25, 80, "Harralander", "Blamish snail slime"),
		potion("Energy potion", 26, 67.5, "Harralander", "Chocolate dust"),
		potion("Defence potion", 30, 75, "Ranarr weed", "White berries"),
		potion("Agility potion", 34, 80, "Toadflax", "Toad's legs"),
		potion("Combat potion", 36, 84, "Harralander", "Goat horn dust"),
		potion("Prayer potion", 38, 87.5, "Ranarr weed", "Snape grass"),
		potion("Super attack", 45, 100, "Irit leaf", "Eye of newt"),
		potion("Superantipoison", 48, 106.3, "Irit leaf", "Unicorn horn dust"),
		potion("Fishing potion", 50, 112.5, "Avantoe", "Snape grass"),
		potion("Super energy", 52, 117.5, "Avantoe", "Mort myre fungus"),
		potion("Hunter potion", 53, 120, "Avantoe", "Kebbit teeth dust"),
		potion("Goading potion", 54, 132, "Harralander", "Aldarium"),
		potion("Super strength", 55, 125, "Kwuarm", "Limpwurt root"),
		mix("Haemostatic poultice", 56, 27, VIAL_OF_WATER, "Elkhorn coral", "Squid paste"),
		mix("Haemostatic dressing", 56, 100, "Haemostatic poultice", "Cotton yarn"),
		potion("Prayer regeneration potion", 58, 132, "Huasca", "Aldarium"),
		potion("Weapon poison", 60, 137.5, "Kwuarm", "Dragon scale dust"),
		mix("Super fishing potion", 62, 140.5, VIAL_OF_WATER, "Pillar coral", "Haddock eye"),
		potion("Super restore", 63, 142.5, "Snapdragon", "Red spiders' eggs"),
		mix("Sanfew serum", 65, 160, "Super restore(4)", "Unicorn horn dust", "Snake weed", "Nail beast nails"),
		upgrade("Extreme energy potion", 66, 84, "Super energy", "Yellow fin", 1),
		potion("Super defence", 66, 150, "Cadantine", "White berries"),
		mix("Super hunter potion", 67, 154, VIAL_OF_WATER, "Pillar coral", "Crab paste"),

		// Coconut milk / vial of blood bases
		brew("Antidote+", 68, 155, COCONUT_MILK, "Toadflax", "Antidote+ (unf)", "Yew roots"),
		potion("Antifire potion", 69, 157.5, "Lantadyme", "Dragon scale dust"),
		upgrade("Divine super attack potion", 70, 2, "Super attack", CRYSTAL_DUST, 1),
		upgrade("Divine super defence potion", 70, 2, "Super defence", CRYSTAL_DUST, 1),
		upgrade("Divine super strength potion", 70, 2, "Super strength", CRYSTAL_DUST, 1),
		potion("Ranging potion", 72, 162.5, "Dwarf weed", "Wine of Zamorak"),
		brew("Weapon poison+", 73, 190, COCONUT_MILK, "Cactus spine", "Weapon poison+ (unf)", "Red spiders' eggs"),
		upgrade("Divine ranging potion", 74, 2, "Ranging potion", CRYSTAL_DUST, 1),
		potion("Magic potion", 76, 172.5, "Lantadyme", "Potato cactus"),
		upgrade("Stamina potion", 77, 102, "Super energy", "Amylase crystal", 1),
		potion("Zamorak brew", 78, 175, "Torstol", "Jangerberries"),
		upgrade("Divine magic potion", 78, 2, "Magic potion", CRYSTAL_DUST, 1),
		brew("Antidote++", 79, 177.5, COCONUT_MILK, "Irit leaf", "Antidote++ (unf)", "Magic roots"),
		brew("Bastion potion", 80, 155, VIAL_OF_BLOOD, "Cadantine", "Cadantine blood potion (unf)", "Wine of Zamorak"),
		brew("Battlemage potion", 80, 155, VIAL_OF_BLOOD, "Cadantine", "Cadantine blood potion (unf)", "Potato cactus"),
		potion("Saradomin brew", 81, 180, "Toadflax", "Crushed nest"),
		potion("Surge potion", 81, 185, "Torstol", "Demonic tallow"),
		brew("Weapon poison++", 82, 190, COCONUT_MILK, "Cave nightshade", "Weapon poison++ (unf)", "Poison ivy berries"),
		upgrade("Extended antifire", 84, 110, "Antifire potion", "Lava scale shard", 1),
		potion("Ancient brew", 85, 190, "Dwarf weed", "Nihil dust"),
		upgrade("Extended stamina potion", 85, 110, "Stamina potion", "Marlin scales", 1),
		upgrade("Divine bastion potion", 86, 2, "Bastion potion", CRYSTAL_DUST, 1),
		upgrade("Divine battlemage potion", 86, 2, "Battlemage potion", CRYSTAL_DUST, 1),
		upgrade("Anti-venom", 87, 120, "Antidote++", "Zulrah's scales", 5),
		potion("Menaphite remedy", 88, 200, "Dwarf weed", "Lily of the Sands"),
		mix("Armadyl brew", 89, 205, VIAL_OF_WATER, "Umbral coral", "Rainbow crab paste"),
		mix("Super combat potion", 90, 150, "Super attack(4)", "Super strength(4)", "Super defence(4)", "Torstol"),
		upgrade("Forgotten brew", 91, 145, "Ancient brew", "Ancient essence", 20),
		mix("Super antifire potion", 92, 130, "Antifire potion(4)", "Crushed superior dragon bones"),
		mix("Anti-venom+", 94, 125, "Anti-venom(4)", "Torstol"),
		upgrade("Extended anti-venom+", 94, 80, "Anti-venom+", "Araxyte venom sac", 1),
		upgrade("Divine super combat potion", 97, 2, "Super combat potion", CRYSTAL_DUST, 1),
		recipe("Extended super antifire", 98,
			upgradeOption(160, "Super antifire potion", "Lava scale shard", 1),
			upgradeOption(180, "Extended antifire", "Crushed superior dragon bones", 1))
	);

	private HerbloreRecipes()
	{
	}

	/** Vial of water + herb + secondaries, with the unfinished potion named after the herb. */
	private static Recipe potion(String name, int level, double xp, String herb, String... secondaries)
	{
		// "Guam leaf" -> "Guam potion (unf)", "Dwarf weed" -> "Dwarf weed potion (unf)"
		String unfinished = (herb.equals("Dwarf weed") ? herb : herb.replace(" leaf", "").replace(" weed", ""))
			+ " potion (unf)";
		return brew(name, level, xp, VIAL_OF_WATER, herb, unfinished, secondaries);
	}

	/**
	 * Two-option recipe: (base + herb + secondaries) from scratch, or (unfinished potion + secondaries)
	 * if the unfinished potion is already made. Making the unfinished potion gives no XP.
	 */
	private static Recipe brew(String name, int level, double xp, String base, String herb, String unfinished,
		String... secondaries)
	{
		List<Ingredient> fromScratch = new ArrayList<>(Ingredient.items(herb, base));
		List<Ingredient> fromUnfinished = new ArrayList<>(Ingredient.items(unfinished));
		fromScratch.addAll(Ingredient.items(secondaries));
		fromUnfinished.addAll(Ingredient.items(secondaries));

		return Recipe.anyOf(null, name, level, xp, List.of(fromScratch, fromUnfinished));
	}

	/** Single-option recipe: every listed item must be owned. */
	private static Recipe mix(String name, int level, double xp, String... items)
	{
		return recipe(name, level, new Option(xp, Ingredient.items(items)));
	}

	/**
	 * Finished potion (any dose count) + an ingredient needed once per dose.
	 * The XP is for a 4-dose potion and scales with the doses actually used.
	 */
	private static Recipe upgrade(String name, int level, double xp, String potion, String perDoseItem,
		int quantityPerDose)
	{
		return recipe(name, level, upgradeOption(xp, potion, perDoseItem, quantityPerDose));
	}

	private static Option upgradeOption(double xp, String potion, String perDoseItem, int quantityPerDose)
	{
		return new Option(xp, List.of(Ingredient.potion(potion), Ingredient.perDose(perDoseItem, quantityPerDose)));
	}

	/** A Herblore recipe (these have no category) made from one or more alternative options. */
	private static Recipe recipe(String name, int level, Option... options)
	{
		return new Recipe(null, name, level, List.of(options));
	}
}
