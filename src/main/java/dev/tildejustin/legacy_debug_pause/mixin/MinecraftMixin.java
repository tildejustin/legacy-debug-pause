package dev.tildejustin.legacy_debug_pause.mixin;

import dev.tildejustin.legacy_debug_pause.interfaces.IGameMenuScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
    @Shadow
    @Nullable
    public Screen currentScreen;

    @Shadow
    public abstract void openScreen(Screen screen);

    @SuppressWarnings("DataFlowIssue")
    @Unique
    public void openGameMenuScreen(boolean pause) {
        if (this.currentScreen == null) {
            GameMenuScreen screen = new GameMenuScreen();
            if (pause) ((IGameMenuScreen) screen).hideMenu();
            this.openScreen(screen);
        }
    }

    @Redirect(
            method = "tick",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;openGameMenuScreen()V")
    )
    private void openGameMenuWithPauseInTick(Minecraft instance) {
        boolean hide = Keyboard.isKeyDown(Keyboard.KEY_F3);
        openGameMenuScreen(hide);
    }
}
