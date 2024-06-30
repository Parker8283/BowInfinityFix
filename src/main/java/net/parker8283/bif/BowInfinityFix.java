package net.parker8283.bif;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// We're extending Entity here to get access to the `level()` function.
@Mixin(Player.class)
public abstract class BowInfinityFix extends Entity {
    // We're required to have a constructor that matches `Entity`s constructor.
    // But I guess Mixin does some fancy stuff to not need to instantiate this. So we can make it private.
    private BowInfinityFix(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(at = @At("TAIL"), cancellable = true, method = "getProjectile(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/item/ItemStack;")
    private void init(ItemStack weaponStack, CallbackInfoReturnable<ItemStack> cir) {
        if (this.level() instanceof ServerLevel level &&
            cir.getReturnValue().isEmpty()) {
            var ret = Items.ARROW.getDefaultInstance();
            if (EnchantmentHelper.processAmmoUse(level, weaponStack, ret, 1) == 0) {
                cir.setReturnValue(ret);
            }
        }
    }
}
