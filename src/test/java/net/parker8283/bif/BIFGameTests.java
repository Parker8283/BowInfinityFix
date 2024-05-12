package net.parker8283.bif;

import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.GameType;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

@PrefixGameTestTemplate(false)
@GameTestHolder(BowInfinityFix.MODID)
public class BIFGameTests {
    @GameTest(template = "empty_1x2x1")
    public static void normalBowSurvival(GameTestHelper helper) {
        Player player = makeBIFMockPlayer(helper, GameType.SURVIVAL, false, false);
        helper.succeedIf(runAmmoTest(helper, player, false, false, false));
    }

    @GameTest(template = "empty_1x2x1")
    public static void enchantedBowSurvival(GameTestHelper helper) {
        Player player = makeBIFMockPlayer(helper, GameType.SURVIVAL, false, true);
        helper.succeedIf(runAmmoTest(helper, player, false, true, true));
    }

    @GameTest(template = "empty_1x2x1")
    public static void normalBowCreative(GameTestHelper helper) {
        Player player = makeBIFMockPlayer(helper, GameType.CREATIVE, false, false);
        helper.succeedIf(runAmmoTest(helper, player, false, false, true));
    }

    @GameTest(template = "empty_1x2x1")
    public static void enchantedBowCreative(GameTestHelper helper) {
        Player player = makeBIFMockPlayer(helper, GameType.CREATIVE, false, true);
        helper.succeedIf(runAmmoTest(helper, player, false, true, true));
    }

    @GameTest(template = "empty_1x2x1")
    public static void normalCrossbowSurvival(GameTestHelper helper) {
        Player player = makeBIFMockPlayer(helper, GameType.SURVIVAL, true, false);
        helper.succeedIf(runAmmoTest(helper, player, true, false, false));
    }

    @GameTest(template = "empty_1x2x1")
    public static void enchantedCrossbowSurvival(GameTestHelper helper) {
        Player player = makeBIFMockPlayer(helper, GameType.SURVIVAL, true, true);
        helper.succeedIf(runAmmoTest(helper, player, true, true, true));
    }

    @GameTest(template = "empty_1x2x1")
    public static void normalCrossbowCreative(GameTestHelper helper) {
        Player player = makeBIFMockPlayer(helper, GameType.CREATIVE, true, false);
        helper.succeedIf(runAmmoTest(helper, player, true, false, true));
    }

    @GameTest(template = "empty_1x2x1")
    public static void enchantedCrossbowCreative(GameTestHelper helper) {
        Player player = makeBIFMockPlayer(helper, GameType.CREATIVE, true, true);
        helper.succeedIf(runAmmoTest(helper, player, true, true, true));
    }

    private static Player makeBIFMockPlayer(GameTestHelper helper, GameType gameType, boolean isCrossbow, boolean isEnchanted) {
        Player ret = helper.makeMockPlayer(gameType);
        gameType.updatePlayerAbilities(ret.getAbilities()); // Not sure why I have to manually do this...
        ItemStack weaponStack;
        if (isCrossbow) {
            weaponStack = new ItemStack(Items.CROSSBOW);
        } else {
            weaponStack = new ItemStack(Items.BOW);
        }
        if (isEnchanted) {
            weaponStack.enchant(Enchantments.INFINITY, 1);
        }
        ret.setItemInHand(InteractionHand.MAIN_HAND, weaponStack);
        return ret;
    }

    private static Runnable runAmmoTest(GameTestHelper helper, Player player, boolean shouldBeCrossbow, boolean shouldBeEnchanted, boolean shouldGiveArrow) {
        return () -> {
            // Make sure we actually got the bow/crossbow...
            ItemStack weapon = player.getItemInHand(InteractionHand.MAIN_HAND);
            if (shouldBeCrossbow && weapon.getItem() != Items.CROSSBOW) {
                helper.fail("Main Hand item isn't a crossbow??!? (" + weapon + ")");
            } else if (!shouldBeCrossbow && weapon.getItem() != Items.BOW) {
                helper.fail("Main Hand item isn't a bow??!? (" + weapon + ")");
            }
            // ...and that the enchantment level is correct
            int enchLevel = weapon.getEnchantmentLevel(Enchantments.INFINITY);
            if (shouldBeEnchanted && enchLevel == 0) {
                helper.fail("Weapon wasn't enchanted. (" + weapon + ", infinity_level=" + enchLevel + ")");
            } else if (!shouldBeEnchanted && enchLevel > 0) {
                helper.fail("Weapon was enchanted. (" + weapon + ", infinity_level=" + enchLevel + ")");
            }
            // Now ask the player for the projectile to use. It should be an arrow if creative or enchanted.
            ItemStack projectileStack = player.getProjectile(weapon);
            if (shouldGiveArrow && projectileStack.isEmpty()) {
                helper.fail("Weapon did not nock arrow! (" + projectileStack + ", " + weapon + ", infinity_level=" + enchLevel + ")");
            } else if (!shouldGiveArrow && !projectileStack.isEmpty()) {
                helper.fail("Weapon nocked arrow! (" + projectileStack + ", " + weapon + ", infinity_level=" + enchLevel + ")");
            }
            // GameTest framework assumes success if failure is not returned.
        };
    }
}
