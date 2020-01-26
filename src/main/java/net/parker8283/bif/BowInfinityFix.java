package net.parker8283.bif;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.util.ActionResult;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("bowinfinityfix")
public class BowInfinityFix {

    private static final Logger LOGGER = LogManager.getLogger("BowInfinityFix");

    public BowInfinityFix() {
        MinecraftForge.EVENT_BUS.addListener(this::infinityFix);
        LOGGER.info("Fix Registered!");
    }

    private void infinityFix(final ArrowNockEvent event) {
        if (EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, event.getBow()) > 0) {
            event.getPlayer().setActiveHand(event.getHand());
            event.setAction(ActionResult.func_226248_a_(event.getBow()));
        }
    }
}
