package net.parker8283.bif;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("bowinfinityfix")
public class BowInfinityFix {

    private static final Logger LOGGER = LogManager.getLogger("BowInfinityFix");

    @ObjectHolder("minecraft:infinity")
    public static final Enchantment INFINITY = null;

    public BowInfinityFix() {
        MinecraftForge.EVENT_BUS.addListener(this::infinityFix);
        LOGGER.info("Fix Registered!");
    }

    private void infinityFix(final ArrowNockEvent event) {
        if (EnchantmentHelper.getEnchantmentLevel(INFINITY, event.getBow()) > 0) {
            event.getEntityPlayer().setActiveHand(event.getHand());
            event.setAction(new ActionResult<>(EnumActionResult.SUCCESS, event.getBow()));
        }
    }
}
