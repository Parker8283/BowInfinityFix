package net.parker8283.bif.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.InteractionHand;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public abstract class BIFLocalPlayer {
    @Shadow private @Nullable InteractionHand usingItemHand;
    @Shadow private boolean startedUsingItem;

    // If the Client and the Server disagree about which hand is using an item, it will look odd (Client will see the
    // "other" hand win, compared to what the server sees). Since the Server will win out in the end when the item is
    // actually used, force the client to restart its use animation if it gets a different hand from the Server.
    // NOTE: This injection point is just after the InteractionHand local, but just before the if statement.
    @Inject(method = "onSyncedDataUpdated(Lnet/minecraft/network/syncher/EntityDataAccessor;)V", at = @At(value = "JUMP", opcode = Opcodes.IFEQ, ordinal = 1, shift = At.Shift.BY, by = -1))
    private void correctHand(EntityDataAccessor<?> dataAccessor, CallbackInfo ci, @Local InteractionHand realUseHand) {
        if (this.usingItemHand != realUseHand) {
            this.startedUsingItem = false;
        }
    }
}
