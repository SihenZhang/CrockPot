package com.sihenzhang.crockpot.integration.patchouli;

public class ProcessorFoodValues {
}

//public class ProcessorFoodValues implements IComponentProcessor {
//    private String categoryName;
//    private Set<Item> items;
//    private List<IVariable> pagedItems;
//
//    @Override
//    public void setup(IVariableProvider variables) {
//        categoryName = variables.get("category").asString();
//        FoodCategory category = EnumUtils.getEnum(FoodCategory.class, this.categoryName.toUpperCase());
//        items = FoodValuesDefinition.getMatchedItems(category, Minecraft.getInstance().level.getRecipeManager());
//        pagedItems = PatchouliUtils.pagedItemVariables(items.stream().map(Item::getDefaultInstance).collect(Collectors.toList()), 42);
//    }
//
//    @Override
//    public IVariable process(String key) {
//        if (key.startsWith("item")) {
//            int index = Integer.parseInt(key.substring(4)) - 1;
//            if (index < 0 || index >= Math.min(items.size(), 42)) {
//                return IVariable.from(ItemStack.EMPTY);
//            }
//            return pagedItems.get(index);
//        } else if ("title".equals(key)) {
//            return IVariable.wrap(I18n.get("item." + CrockPot.MOD_ID + ".food_category_" + categoryName.toLowerCase()));
//        }
//        return null;
//    }
//}
