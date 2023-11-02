package dev.tildejustin.legacy_debug_pause.mixin;

import dev.tildejustin.legacy_debug_pause.interfaces.IGameMenuScreen;
import net.minecraft.class_4107;
import net.minecraft.class_4110;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.GameMenuScreen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(class_4110.class)
public abstract class class_4110Mixin {
    @Shadow
    @Final
    private MinecraftClient field_19926;

    @Redirect(
            method = "method_18182",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;openGameMenuScreen()V")
    )
    private void openGameMenuWithPause(MinecraftClient instance) {
        boolean hide = class_4107.method_18154(292);
        openGameMenuScreen(hide);
    }

    @SuppressWarnings("DataFlowIssue")
    @Unique
    public void openGameMenuScreen(boolean pause) {
        if (this.field_19926.currentScreen == null) {
            GameMenuScreen screen = new GameMenuScreen();
            if (pause) ((IGameMenuScreen) screen).hideMenu();
            this.field_19926.setScreen(screen);
            if (this.field_19926.isInSingleplayer() && !this.field_19926.getServer().shouldBroadcastConsoleToIps()) {
                this.field_19926.getSoundManager().pauseAll();
            }
        }
    }
}
