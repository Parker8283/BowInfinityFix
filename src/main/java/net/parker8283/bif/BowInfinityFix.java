package net.parker8283.bif;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = BowItem.class, priority = BowInfinityFix.PRIORITY)
public class BowInfinityFix {
    // To be compatible with the Auditory, make sure our mixin gets applied before it. Its mixin applies on all RETURN
    // instructions, which our mixin will generate one. So we need to be in before it. Auditory uses the default 1000
    // priority currently, so we will use 999 for now (I could just do like 1 or something, but that feels wrong).
    static final int PRIORITY = 999;

    @Inject(at = @At("HEAD"), cancellable = true, method = "use(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/InteractionResultHolder;")
    private void init(Level level, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {
        ItemStack stack = player.getItemInHand(hand);
        if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, stack) > 0) {
            player.startUsingItem(hand);
            cir.setReturnValue(InteractionResultHolder.success(stack));
        }
    }
}