package dev.tildejustin.legacy_debug_pause.mixin;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import dev.tildejustin.legacy_debug_pause.interfaces.IGameMenuScreen;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameMenuScreen.class)
public abstract class GameMenuScreenMixin extends Screen implements IGameMenuScreen {
    @Unique
    private boolean showMenu = true;

    public GameMenuScreenMixin() {
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Override
    public void hideMenu() {
        showMenu = false;
    }

    @Inject(method = "method_2224", at = @At("HEAD"), cancellable = true)
    private void stopButtonRender(CallbackInfo ci) {
        if (!showMenu) ci.cancel();
    }

    @SuppressWarnings("unused")
    @WrapWithCondition(method = "method_2214", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/GameMenuScreen;method_2240()V"))
    private boolean stopBackgroundRender(GameMenuScreen screen) {
        return showMenu;
    }


    @ModifyArg(method = "method_2214", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/GameMenuScreen;method_1789(Lnet/minecraft/class_0_681;Ljava/lang/String;III)V"), index = 3)
    private int modifyPauseTextHeight(int y) {
        if (!showMenu) return 10;
        return y;
    }
}
