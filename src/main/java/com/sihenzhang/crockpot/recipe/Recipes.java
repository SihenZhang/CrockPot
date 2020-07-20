package com.sihenzhang.crockpot.recipe;

import java.util.*;

public final class Recipes {
    private static final Random random = new Random();
    private static boolean dirty = true;
    static List<Recipe> recipes = new LinkedList<>();

    public static void addRecipe(Recipe r) {
        dirty = true;
        recipes.add(r);
    }

    private static void sort() {
        recipes.sort(Comparator.comparingInt(r -> r.priority));
    }

    public static Recipe match(RecipeInput input) {
        if (dirty) {
            sort();
        }
        Iterator<Recipe> itr = recipes.iterator();
        Recipe r;

        List<Recipe> matched = new LinkedList<>();

        int p = -1;
        while ((r = itr.next()) != null) {
            if (p == -1) {
                if (r.test(input)) {
                    p = r.priority;
                    matched.add(r);
                }
            } else {
                if (r.priority != p) {
                    int sum = 0;
                    for (Recipe e : matched) sum += e.weight;
                    int rand = random.nextInt(sum);
                    for (Recipe e : matched) {
                        rand -= e.weight;
                        if (rand <= 0) return e;
                    }
                } else {
                    if (r.test(input)) matched.add(r);
                }
            }
        }
        return null;
    }
}
