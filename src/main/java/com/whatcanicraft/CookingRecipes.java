package com.whatcanicraft;

import com.whatcanicraft.Recipe.Ingredient;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Cooking data from the "Types of food" tables on the OSRS Wiki Cooking page (level and XP). Brewing and gnome
 * cooking aren't included.
 *
 * The wiki tables list the finished food but not what goes on the range, so each recipe's ingredient is the item
 * you cook or combine to make it: the raw food for meat and fish, the uncooked or unbaked item for pies, bread,
 * stews, pizzas and cakes, and so on. Names follow the game's naming ("Raw shrimps", "Uncooked cake").
 * Some foods can be made from several ingredients, which are alternative options.
 * XP is for the cooking step. Preparation steps that give no XP aren't recipes.
 */
public final class CookingRecipes
{
	public static final String MEAT_FISH = "Meat / fish";
	public static final String BREAD = "Bread";
	public static final String PIES = "Pies";
	public static final String STEWS = "Stews";
	public static final String PIZZAS = "Pizzas";
	public static final String CAKES = "Cakes";
	public static final String WINE = "Wine";
	public static final String HOT_DRINKS = "Hot drinks";
	public static final String VEGETABLES = "Vegetables";
	public static final String DAIRY = "Dairy";

	/** Filter categories in dropdown order. */
	public static final List<String> CATEGORIES = List.of(
		MEAT_FISH, BREAD, PIES, STEWS, PIZZAS, CAKES, WINE, HOT_DRINKS, VEGETABLES, DAIRY);

	private static final List<Recipe> MEAT_FISH_RECIPES = List.of(
		anyOf(MEAT_FISH, "Cooked meat", 1, 30, each("Raw beef", "Raw rat meat", "Raw bear meat", "Raw yak meat")),
		food(MEAT_FISH, "Shrimps", 1, 30, "Raw shrimps"),
		food(MEAT_FISH, "Cooked chicken", 1, 30, "Raw chicken"),
		food(MEAT_FISH, "Cooked rabbit", 1, 30, "Raw rabbit"),
		food(MEAT_FISH, "Anchovies", 1, 30, "Raw anchovies"),
		food(MEAT_FISH, "Sardine", 1, 40, "Raw sardine"),
		food(MEAT_FISH, "Ugthanki meat", 1, 40, "Raw ugthanki meat"),
		food(MEAT_FISH, "Cooked mystery meat", 1, 30, "Raw mystery meat"),
		food(MEAT_FISH, "Herring", 5, 50, "Raw herring"),
		food(MEAT_FISH, "Guppy", 7, 12, "Raw guppy"),
		food(MEAT_FISH, "Mackerel", 10, 60, "Raw mackerel"),
		food(MEAT_FISH, "Roast bird meat", 11, 60, "Raw bird meat"),
		food(MEAT_FISH, "Thin snail meat", 12, 70, "Thin snail"),
		food(MEAT_FISH, "Trout", 15, 70, "Raw trout"),
		food(MEAT_FISH, "Roast rabbit", 16, 70, "Skewered rabbit"),
		food(MEAT_FISH, "Lean snail meat", 17, 80, "Lean snail"),
		food(MEAT_FISH, "Cod", 18, 75, "Raw cod"),
		food(MEAT_FISH, "Pike", 20, 80, "Raw pike"),
		food(MEAT_FISH, "Cavefish", 20, 23, "Raw cavefish"),
		food(MEAT_FISH, "Roast beast meat", 21, 82.5, "Skewered beast"),
		food(MEAT_FISH, "Red crab meat", 21, 85, "Raw red crab meat"),
		food(MEAT_FISH, "Cooked giant crab meat", 21, 100, "Raw giant crab meat"),
		food(MEAT_FISH, "Fat snail meat", 22, 95, "Fat snail"),
		food(MEAT_FISH, "Cooked wild kebbit", 23, 73, "Raw wild kebbit"),
		food(MEAT_FISH, "Salmon", 25, 90, "Raw salmon"),
		food(MEAT_FISH, "Cooked slimy eel", 28, 95, "Raw slimy eel"),
		food(MEAT_FISH, "Tuna", 30, 100, "Raw tuna"),
		food(MEAT_FISH, "Cooked karambwan", 30, 190, "Raw karambwan"),
		food(MEAT_FISH, "Cooked chompy", 30, 100, "Raw chompy"),
		food(MEAT_FISH, "Cooked fishcake", 31, 100, "Raw fishcake"),
		food(MEAT_FISH, "Cooked larupia", 31, 92, "Raw larupia"),
		food(MEAT_FISH, "Cooked barb-tailed kebbit", 32, 106, "Raw barb-tailed kebbit"),
		food(MEAT_FISH, "Tetra", 33, 31, "Raw tetra"),
		food(MEAT_FISH, "Rainbow fish", 35, 110, "Raw rainbow fish"),
		food(MEAT_FISH, "Cave eel", 38, 115, "Raw cave eel"),
		food(MEAT_FISH, "Lobster", 40, 120, "Raw lobster"),
		food(MEAT_FISH, "Cooked jubbly", 41, 160, "Raw jubbly"),
		food(MEAT_FISH, "Cooked graahk", 41, 124, "Raw graahk"),
		food(MEAT_FISH, "Bass", 43, 130, "Raw bass"),
		food(MEAT_FISH, "Swordfish", 45, 140, "Raw swordfish"),
		food(MEAT_FISH, "Catfish", 46, 43, "Raw catfish"),
		food(MEAT_FISH, "Blue crab meat", 48, 145, "Raw blue crab meat"),
		food(MEAT_FISH, "Cooked oomlie wrap", 50, 30, "Raw oomlie wrap"),
		food(MEAT_FISH, "Cooked kyatt", 51, 143, "Raw kyatt"),
		food(MEAT_FISH, "Lava eel", 53, 30, "Raw lava eel"),
		food(MEAT_FISH, "Swordtip squid", 56, 150, "Raw swordtip squid"),
		food(MEAT_FISH, "Cooked pyre fox", 59, 154, "Raw pyre fox"),
		food(MEAT_FISH, "Monkfish", 62, 150, "Raw monkfish"),
		food(MEAT_FISH, "Cooked sunlight antelope", 68, 175, "Raw sunlight antelope"),
		food(MEAT_FISH, "Giant krill", 69, 177.5, "Raw giant krill"),
		food(MEAT_FISH, "Jumbo squid", 71, 180, "Raw jumbo squid"),
		food(MEAT_FISH, "Haddock", 73, 180, "Raw haddock"),
		food(MEAT_FISH, "Rainbow crab meat", 77, 200, "Raw rainbow crab meat"),
		food(MEAT_FISH, "Yellowfin", 79, 200, "Raw yellowfin"),
		food(MEAT_FISH, "Shark", 80, 210, "Raw shark"),
		food(MEAT_FISH, "Sea turtle", 82, 211.3, "Raw sea turtle"),
		food(MEAT_FISH, "Cooked dashing kebbit", 82, 215, "Raw dashing kebbit"),
		food(MEAT_FISH, "Halibut", 83, 212.5, "Raw halibut"),
		food(MEAT_FISH, "Anglerfish", 84, 230, "Raw anglerfish"),
		food(MEAT_FISH, "Bluefin", 87, 215, "Raw bluefin"),
		food(MEAT_FISH, "Dark crab", 90, 215, "Raw dark crab"),
		food(MEAT_FISH, "Manta ray", 91, 216.2, "Raw manta ray"),
		food(MEAT_FISH, "Marlin", 91, 225, "Raw marlin"),
		food(MEAT_FISH, "Cooked moonlight antelope", 92, 220, "Raw moonlight antelope")
	);

	private static final List<Recipe> BAKED_RECIPES = List.of(
		// Bread
		food(BREAD, "Bread", 1, 40, "Bread dough"),
		food(BREAD, "Pitta bread", 58, 40, "Pitta dough"),

		// Pies
		food(PIES, "Redberry pie", 10, 78, "Uncooked berry pie"),
		food(PIES, "Meat pie", 20, 110, "Uncooked meat pie"),
		food(PIES, "Mud pie", 29, 128, "Raw mud pie"),
		food(PIES, "Apple pie", 30, 130, "Uncooked apple pie"),
		food(PIES, "Garden pie", 34, 138, "Raw garden pie"),
		food(PIES, "Fish pie", 47, 164, "Raw fish pie"),
		food(PIES, "Botanical pie", 52, 180, "Uncooked botanical pie"),
		food(PIES, "Mushroom pie", 60, 200, "Uncooked mushroom pie"),
		food(PIES, "Admiral pie", 70, 210, "Raw admiral pie"),
		food(PIES, "Dragonfruit pie", 73, 220, "Uncooked dragonfruit pie"),
		food(PIES, "Wild pie", 85, 240, "Raw wild pie"),
		food(PIES, "Summer pie", 95, 260, "Raw summer pie"),

		// Stews
		food(STEWS, "Stew", 25, 117, "Uncooked stew"),
		food(STEWS, "Curry", 60, 280, "Uncooked curry"),

		// Pizzas: the plain pizza is baked from the uncooked one, toppings are added to the plain pizza
		food(PIZZAS, "Plain pizza", 35, 143, "Uncooked pizza"),
		anyOf(PIZZAS, "Meat pizza", 45, 26,
			new String[]{"Plain pizza", "Cooked meat"}, new String[]{"Plain pizza", "Cooked chicken"}),
		food(PIZZAS, "Anchovy pizza", 55, 39, "Plain pizza", "Anchovies"),
		anyOf(PIZZAS, "Pineapple pizza", 65, 45,
			new String[]{"Plain pizza", "Pineapple ring"}, new String[]{"Plain pizza", "Pineapple chunks"}),

		// Cakes
		food(CAKES, "Cake", 40, 180, "Uncooked cake"),
		food(CAKES, "Chocolate cake", 50, 30, "Cake", "Chocolate bar")
	);

	private static final List<Recipe> OTHER_RECIPES = List.of(
		// Wine
		food(WINE, "Jug of wine", 35, 200, "Jug of water", "Grapes"),
		food(WINE, "Wine of Zamorak", 65, 200, "Jug of water", "Zamorak's grapes"),

		// Hot drinks
		food(HOT_DRINKS, "Cup of tea (nettle)", 20, 52, "Nettle water"),

		// Vegetables
		food(VEGETABLES, "Baked potato", 7, 15, "Potato"),
		food(VEGETABLES, "Scrambled egg", 13, 50, "Uncooked egg"),
		food(VEGETABLES, "Egg and tomato", 23, 0, "Scrambled egg", "Chopped tomato"),
		food(VEGETABLES, "Cooked sweetcorn", 28, 104, "Sweetcorn"),
		food(VEGETABLES, "Potato with butter", 39, 40, "Baked potato", "Pat of butter"),
		food(VEGETABLES, "Chilli potato", 41, 15, "Baked potato", "Chilli con carne"),
		food(VEGETABLES, "Fried onions", 42, 60, "Chopped onion"),
		food(VEGETABLES, "Fried mushrooms", 46, 60, "Sliced mushrooms"),
		food(VEGETABLES, "Potato with cheese", 47, 40, "Baked potato", "Cheese"),
		food(VEGETABLES, "Egg potato", 51, 45, "Baked potato", "Egg and tomato"),
		food(VEGETABLES, "Mushroom & onion", 57, 0, "Fried mushrooms", "Fried onions"),
		food(VEGETABLES, "Mushroom potato", 64, 55, "Baked potato", "Mushroom & onion"),
		food(VEGETABLES, "Tuna and corn", 67, 0, "Chopped tuna", "Cooked sweetcorn"),
		food(VEGETABLES, "Tuna potato", 68, 10, "Baked potato", "Tuna and corn"),

		// Dairy (dairy churn)
		food(DAIRY, "Pot of cream", 21, 18, "Bucket of milk", "Pot"),
		food(DAIRY, "Pat of butter", 38, 40.5, "Pot of cream"),
		food(DAIRY, "Cheese", 48, 64, "Pat of butter")
	);

	public static final List<Recipe> ALL = Stream.of(MEAT_FISH_RECIPES, BAKED_RECIPES, OTHER_RECIPES)
		.flatMap(List::stream)
		.collect(Collectors.toUnmodifiableList());

	private CookingRecipes()
	{
	}

	/** A food made from every listed item. */
	private static Recipe food(String category, String name, int level, double xp, String... ingredients)
	{
		return Recipe.of(category, name, level, xp, ingredients);
	}

	/** A food that can be made from any one of the listed ingredient sets. */
	private static Recipe anyOf(String category, String name, int level, double xp, String[]... sets)
	{
		return Recipe.anyOf(category, name, level, xp,
			Arrays.stream(sets).map(Ingredient::items).collect(Collectors.toList()));
	}

	/** Alternatives that are each a single ingredient, for {@link #anyOf}. */
	private static String[][] each(String... items)
	{
		return Arrays.stream(items).map(item -> new String[]{item}).toArray(String[][]::new);
	}
}
