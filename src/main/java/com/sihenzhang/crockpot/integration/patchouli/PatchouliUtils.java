package com.sihenzhang.crockpot.integration.patchouli;

public final class PatchouliUtils {
//    public static IVariable ingredientVariable(Ingredient ingredient) {
//        return IVariable.wrapList(Arrays.stream(ingredient.getItems()).map(IVariable::from).collect(Collectors.toList()));
//    }
//
//    public static List<IVariable> pagedItemVariables(List<ItemStack> stacks, int size) {
//        if (stacks.size() <= size) {
//            return stacks.stream().map(IVariable::from).collect(Collectors.toList());
//        }
//        List<List<ItemStack>> pagedStacks = new ArrayList<>();
//        for (int i = 0; i < size; i++) {
//            pagedStacks.add(new ArrayList<>(Arrays.asList(stacks.get(i))));
//        }
//        int pages = (int) Math.ceil((double) stacks.size() / size);
//        for (int i = 1; i < pages; i++) {
//            for (int j = 0; j < size; j++) {
//                pagedStacks.get(j).add(i * size + j < stacks.size() ? stacks.get(i * size + j) : ItemStack.EMPTY);
//            }
//        }
//        return pagedStacks.stream().map(e -> IVariable.wrapList(e.stream().map(IVariable::from).collect(Collectors.toList()))).collect(Collectors.toList());
//    }
//
//    @SuppressWarnings("unchecked")
//    public static <T extends IRecipe<?>> T getRecipe(String name) {
//        return (T) Minecraft.getInstance().level.getRecipeManager().byKey(new ResourceLocation(name)).orElse(null);
//    }
}
