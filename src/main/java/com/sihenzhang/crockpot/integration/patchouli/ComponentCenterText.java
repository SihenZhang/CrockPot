package com.sihenzhang.crockpot.integration.patchouli;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import vazkii.patchouli.api.IComponentRenderContext;
import vazkii.patchouli.api.ICustomComponent;
import vazkii.patchouli.api.IVariable;

import java.util.function.UnaryOperator;

public class ComponentCenterText implements ICustomComponent {
    public int x, y;
    public IVariable guard;
    public IVariable text;
    public IVariable color;

    transient boolean guardPass;
    transient String actualText;
    transient int colorInt;

    @Override
    public void build(int componentX, int componentY, int pageNum) {
        x = componentX;
        y = componentY;
    }

    @Override
    public void render(PoseStack ms, IComponentRenderContext context, float pticks, int mouseX, int mouseY) {
        if (guardPass) {
            Font font = Minecraft.getInstance().font;
            int color = colorInt == 0 ? context.getTextColor() : colorInt;
            Component renderedText = new TextComponent(actualText).withStyle(context.getFont());
            int width = font.width(renderedText);
            font.draw(ms, renderedText, x - width / 2.0F, y, color);
        }
    }

    @Override
    public void onVariablesAvailable(UnaryOperator<IVariable> lookup) {
        guardPass = lookup.apply(guard).asBoolean(true);
        actualText = lookup.apply(text).asString();
        try {
            colorInt = Integer.parseInt(lookup.apply(color).asString(""), 16);
        } catch (NumberFormatException e) {
            colorInt = 0;
        }
    }
}
