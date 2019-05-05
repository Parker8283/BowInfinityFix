package net.parker8283.bif;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = "bowinfinityfix", name = "Bow Infinity Fix", version = "rv5.1", dependencies = "required-after:forge@[14.23,)")
public class BowInfinityFix {
    private static final Logger LOGGER = LogManager.getLogger("BowInfinityFix");

    @GameRegistry.ObjectHolder("minecraft:infinity")
    public static final Enchantment INFINITY = null;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        LOGGER.info("Fix Registered!");
    }

    @SubscribeEvent
    public void infinityFix(ArrowNockEvent event) {
        if (EnchantmentHelper.getEnchantmentLevel(INFINITY, event.getBow()) > 0) {
            event.getEntityPlayer().setActiveHand(event.getHand());
            event.setAction(new ActionResult<>(EnumActionResult.SUCCESS, event.getBow()));
        }
    }
}
