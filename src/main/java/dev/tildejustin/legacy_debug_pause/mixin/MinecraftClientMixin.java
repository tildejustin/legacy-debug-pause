package dev.tildejustin.legacy_debug_pause.mixin;

import dev.tildejustin.legacy_debug_pause.interfaces.IGameMenuScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.server.integrated.IntegratedServer;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @Shadow
    @Nullable
    public Screen currentScreen;

    @Shadow
    public abstract void setScreen(@Nullable Screen screen);

    @Shadow
    public abstract boolean isInSingleplayer();

    @Shadow
    private @Nullable IntegratedServer server;

    @Shadow
    private SoundManager soundManager;

    @SuppressWarnings("DataFlowIssue")
    @Unique
    public void openGameMenuScreen(boolean pause) {
        if (this.currentScreen == null) {
            GameMenuScreen screen = new GameMenuScreen();
            if (pause) ((IGameMenuScreen) screen).hideMenu();
            this.setScreen(screen);
            if (this.isInSingleplayer() && !this.server.method_3860()) {
                this.soundManager.pauseAll();
            }
        }
    }

    @Redirect(
            method = "method_0_9750",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;method_1486()V")
    )
    private void openGameMenuWithPause(MinecraftClient instance) {
        boolean hide = Keyboard.isKeyDown(Keyboard.KEY_F3);
        openGameMenuScreen(hide);
    }
}
