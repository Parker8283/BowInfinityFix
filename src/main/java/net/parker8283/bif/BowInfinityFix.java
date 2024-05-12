package net.parker8283.bif;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class BowInfinityFix {
    @Inject(at = @At("TAIL"), cancellable = true, method = "getProjectile(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/item/ItemStack;")
    private void init(ItemStack weaponStack, CallbackInfoReturnable<ItemStack> cir) {
        if (cir.getReturnValue().isEmpty() && EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY, weaponStack) > 0) {
            cir.setReturnValue(new ItemStack(Items.ARROW));
        }
    }
}
