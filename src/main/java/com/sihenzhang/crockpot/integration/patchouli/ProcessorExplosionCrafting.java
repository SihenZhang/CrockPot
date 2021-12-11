package com.sihenzhang.crockpot.integration.patchouli;

public class ProcessorExplosionCrafting {
}

//public class ProcessorExplosionCrafting implements IComponentProcessor {
//    private ExplosionCraftingRecipe recipe;
//
//    @Override
//    public void setup(IVariableProvider variables) {
//        recipe = PatchouliUtils.getRecipe(variables.get("recipe").asString());
//    }
//
//    @Override
//    public IVariable process(String key) {
//        switch (key) {
//            case "input":
//                return PatchouliUtils.ingredientVariable(recipe.getIngredient());
//            case "output":
//                return IVariable.from(recipe.getResult());
//            case "explosionTooltip":
//                return IVariable.from(new TranslationTextComponent("integration.crockpot.book.explosion_crafting.explosion"));
//            case "isOnlyBlock":
//                return IVariable.wrap(recipe.isOnlyBlock());
//            case "onlyBlockTooltip":
//                return IVariable.from(new TranslationTextComponent("integration.crockpot.book.explosion_crafting.only_block"));
//            case "rate":
//                return IVariable.wrap(MathUtils.format(1.0F - recipe.getLossRate(), "0.##%"));
//            case "title":
//                return IVariable.from(recipe.getResult().getHoverName());
//            default:
//                return null;
//        }
//    }
//}
