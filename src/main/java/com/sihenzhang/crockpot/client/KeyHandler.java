package com.sihenzhang.crockpot.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.client.settings.KeyModifier;
import org.lwjgl.glfw.GLFW;

/**
 * These codes are from Mekanism with MIT License.
 * https://github.com/mekanism/Mekanism/blob/1.15x/src/main/java/mekanism/client/MekKeyHandler.java
 */
public class KeyHandler {
    public static boolean getIsKeyPressed(KeyBinding keyBinding) {
        if (keyBinding.isKeyDown()) {
            return true;
        }
        if (keyBinding.getKeyConflictContext().isActive() && keyBinding.getKeyModifier().isActive(keyBinding.getKeyConflictContext())) {
            // Manually check in case keyBinding#pressed just never got a chance to be updated
            return isKeyPressed(keyBinding);
        }
        // If we failed, due to us being a key modifier as our key, check the old way
        return KeyModifier.isKeyCodeModifier(keyBinding.getKey()) && isKeyPressed(keyBinding);
    }

    private static boolean isKeyPressed(KeyBinding keyBinding) {
        InputMappings.Input key = keyBinding.getKey();
        int keyCode = key.getKeyCode();
        if (keyCode != InputMappings.INPUT_INVALID.getKeyCode()) {
            long windowHandle = Minecraft.getInstance().getMainWindow().getHandle();
            try {
                if (key.getType() == InputMappings.Type.KEYSYM) {
                    return InputMappings.isKeyDown(windowHandle, keyCode);
                } else if (key.getType() == InputMappings.Type.MOUSE) {
                    return GLFW.glfwGetMouseButton(windowHandle, keyCode) == GLFW.GLFW_PRESS;
                }
            } catch (Exception ignored) {
            }
        }
        return false;
    }
}
