package com.whatcanicraft;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/** Herblore recipe data. Names must match in-game item names exactly. */
public final class HerbloreRecipes
{
	private static final String VIAL_OF_WATER = "Vial of water";

	private HerbloreRecipes()
	{
	}

	public static final List<Recipe> ALL = Collections.unmodifiableList(Arrays.asList(
		potion("Attack potion", 3, "Guam leaf", "Eye of newt"),
		potion("Antipoison", 5, "Marrentill", "Unicorn horn dust"),
		potion("Strength potion", 12, "Tarromin", "Limpwurt root"),
		potion("Serum 207", 15, "Tarromin", "Ashes"),
		potion("Compost potion", 22, "Harralander", "Volcanic ash"),
		potion("Restore potion", 22, "Harralander", "Red spiders' eggs"),
		potion("Guthix balance", 22, "Harralander", "Red spiders' eggs", "Garlic", "Silver dust"),
		potion("Energy potion", 26, "Harralander", "Chocolate dust"),
		potion("Defence potion", 30, "Ranarr weed", "White berries"),
		potion("Agility potion", 34, "Toadflax", "Toad's legs"),
		potion("Combat potion", 36, "Harralander", "Goat horn dust"),
		potion("Prayer potion", 38, "Ranarr weed", "Snape grass"),
		potion("Super attack", 45, "Irit leaf", "Eye of newt"),
		potion("Superantipoison", 48, "Irit leaf", "Unicorn horn dust"),
		potion("Fishing potion", 50, "Avantoe", "Snape grass"),
		potion("Super energy", 52, "Avantoe", "Mort myre fungus"),
		potion("Hunter potion", 53, "Avantoe", "Kebbit teeth dust"),
		potion("Super strength", 55, "Kwuarm", "Limpwurt root"),
		potion("Weapon poison", 60, "Kwuarm", "Dragon scale dust"),
		potion("Menaphite remedy", 62, "Kwuarm", "Lily of the sands"),
		potion("Super restore", 63, "Snapdragon", "Red spiders' eggs"),
		potion("Super defence", 66, "Cadantine", "White berries"),
		potion("Antidote+", 68, "Toadflax", "Yew roots"),
		potion("Antifire potion", 69, "Lantadyme", "Dragon scale dust"),
		potion("Ranging potion", 72, "Dwarf weed", "Wine of zamorak"),
		potion("Magic potion", 76, "Lantadyme", "Potato cactus"),
		potion("Zamorak brew", 78, "Torstol", "Jangerberries"),
		potion("Antidote++", 79, "Irit leaf", "Magic roots"),
		potion("Saradomin brew", 81, "Toadflax", "Crushed nest")
	));

	/**
	 * Builds a potion recipe with two options: (herb + vial of water + secondaries)
	 * or (unfinished potion + secondaries).
	 */
	private static Recipe potion(String name, int level, String herb, String... secondaries)
	{
		List<String> fromHerb = new ArrayList<>(Arrays.asList(herb, VIAL_OF_WATER));
		fromHerb.addAll(Arrays.asList(secondaries));

		List<String> fromUnf = new ArrayList<>();
		fromUnf.add(unfinishedName(herb));
		fromUnf.addAll(Arrays.asList(secondaries));

		return new Recipe(name, level, Arrays.asList(fromHerb, fromUnf));
	}

	/** "Guam leaf" -> "Guam potion (unf)", "Dwarf weed" -> "Dwarf weed potion (unf)". */
	private static String unfinishedName(String herb)
	{
		if (herb.equals("Dwarf weed"))
		{
			return "Dwarf weed potion (unf)";
		}
		return herb.replace(" leaf", "").replace(" weed", "") + " potion (unf)";
	}
}
