package com.sihenzhang.crockpot.integration.patchouli;

public class ComponentCenterText {
}

//public class ComponentCenterText implements ICustomComponent {
//    public int x, y;
//    public IVariable guard;
//    public IVariable text;
//    public IVariable color;
//
//    transient boolean guardPass;
//    transient String actualText;
//    transient int colorInt;
//
//    @Override
//    public void build(int componentX, int componentY, int pageNum) {
//        x = componentX;
//        y = componentY;
//    }
//
//    @Override
//    public void render(MatrixStack ms, IComponentRenderContext context, float pticks, int mouseX, int mouseY) {
//        if (guardPass) {
//            FontRenderer font = Minecraft.getInstance().font;
//            int color = colorInt == 0 ? context.getTextColor() : colorInt;
//            ITextComponent renderedText = new StringTextComponent(actualText).withStyle(context.getFont());
//            int width = font.width(renderedText);
//            font.draw(ms, renderedText, x - width / 2.0F, y, color);
//        }
//    }
//
//    @Override
//    public void onVariablesAvailable(UnaryOperator<IVariable> lookup) {
//        guardPass = lookup.apply(guard).asBoolean(true);
//        actualText = lookup.apply(text).asString();
//        try {
//            colorInt = Integer.parseInt(lookup.apply(color).asString(""), 16);
//        } catch (NumberFormatException e) {
//            colorInt = 0;
//        }
//    }
//}
