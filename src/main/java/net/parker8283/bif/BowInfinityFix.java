package net.parker8283.bif;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingGetProjectileEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(BowInfinityFix.MODID)
public class BowInfinityFix {
    public static final String MODID = "bowinfinityfix";
    private static final Logger LOGGER = LogManager.getLogger("BowInfinityFix");

    public BowInfinityFix() {
        NeoForge.EVENT_BUS.addListener(this::infinityFix);
        LOGGER.info("Fix Registered!");
    }

    private void infinityFix(final LivingGetProjectileEvent event) {
        if (event.getEntity() instanceof Player player &&
                player.level() instanceof ServerLevel level &&
                event.getProjectileWeaponItemStack().getItem() instanceof ProjectileWeaponItem pwi &&
                event.getProjectileItemStack().isEmpty()) {
            var defaultStack = pwi.getDefaultCreativeAmmo(player, event.getProjectileWeaponItemStack());
            int countToUse;
            if (defaultStack.getItem() instanceof ArrowItem ai && ai.isInfinite(defaultStack, event.getProjectileWeaponItemStack(), player)) {
                countToUse = 0;
            } else {
                countToUse = EnchantmentHelper.processAmmoUse(level, event.getProjectileWeaponItemStack(), defaultStack, 1);
            }
            if (countToUse == 0) {
                event.setProjectileItemStack(defaultStack);
            }
        }
    }
}
