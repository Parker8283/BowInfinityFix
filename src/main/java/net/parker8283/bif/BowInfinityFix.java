package net.parker8283.bif;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.enchantment.Enchantments;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingGetProjectileEvent;
import net.neoforged.fml.common.Mod;
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
        if (event.getEntity() instanceof Player &&
                event.getProjectileWeaponItemStack().getItem() instanceof ProjectileWeaponItem &&
                event.getProjectileItemStack().isEmpty() &&
                event.getProjectileWeaponItemStack().getEnchantmentLevel(Enchantments.INFINITY) > 0) {
            event.setProjectileItemStack(new ItemStack(Items.ARROW));
        }
    }
}
