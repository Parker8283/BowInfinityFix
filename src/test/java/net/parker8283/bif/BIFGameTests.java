package net.parker8283.bif;

import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

@PrefixGameTestTemplate(false)
@GameTestHolder(BowInfinityFix.MODID)
public class BIFGameTests {
    @GameTest(template = "empty_1x2x1")
    public static void normalBowSurvival(GameTestHelper helper) {
        Player player = makeBIFMockPlayer(helper, false, false);
        helper.succeedIf(runShootingTest(helper, player, false, false));
    }

    @GameTest(template = "empty_1x2x1")
    public static void enchantedBowSurvival(GameTestHelper helper) {
        Player player = makeBIFMockPlayer(helper, false, true);
        helper.succeedIf(runShootingTest(helper, player, true, true));
    }

    @GameTest(template = "empty_1x2x1")
    public static void normalBowCreative(GameTestHelper helper) {
        Player player = makeBIFMockPlayer(helper, true, false);
        helper.succeedIf(runShootingTest(helper, player, false, true));
    }

    @GameTest(template = "empty_1x2x1")
    public static void enchantedBowCreative(GameTestHelper helper) {
        Player player = makeBIFMockPlayer(helper, true, true);
        helper.succeedIf(runShootingTest(helper, player, true, true));
    }

    private static Player makeBIFMockPlayer(GameTestHelper helper, boolean isCreative, boolean isBowEnchanted) {
        Player ret = helper.makeMockPlayer();
        ret.getAbilities().instabuild = isCreative; // BowItem#use checks instabuild for determining if ammo should be checked for.
        ItemStack bow = new ItemStack(Items.BOW);
        if (isBowEnchanted) {
            bow.enchant(Enchantments.INFINITY_ARROWS, 1);
        }
        ret.setItemInHand(InteractionHand.MAIN_HAND, bow);
        return ret;
    }

    private static Runnable runShootingTest(GameTestHelper helper, Player player, boolean shouldBeEnchanted, boolean shouldFireArrow) {
        return () -> {
            // Make sure we actually got the bow...
            ItemStack bow = player.getItemInHand(InteractionHand.MAIN_HAND);
            if (bow.getItem() != Items.BOW) {
                helper.fail("Main Hand item isn't a bow??!? (" + bow + ")");
            }
            // ...and that the enchantment level is correct
            int enchLevel = bow.getEnchantmentLevel(Enchantments.INFINITY_ARROWS);
            if (shouldBeEnchanted && enchLevel == 0) {
                helper.fail("Bow wasn't enchanted. (" + bow + ", infinity_level=" + enchLevel + ")");
            } else if (!shouldBeEnchanted && enchLevel > 0) {
                helper.fail("Bow was enchanted. (" + bow + ", infinity_level=" + enchLevel + ")");
            }
            // Now pretend to use the bow, see what result we get.
            InteractionResult result = bow.use(helper.getLevel(), player, InteractionHand.MAIN_HAND).getResult();
            if (shouldFireArrow && !result.consumesAction()) {
                helper.fail("Bow did not shoot arrow! (" + result + ", " + bow + ", infinity_level=" + enchLevel + ")");
            } else if (!shouldFireArrow && result.consumesAction()) {
                helper.fail("Bow shot arrow! (" + result + ", " + bow + ", infinity_level=" + enchLevel + ")");
            }
            // GameTest framework assumes success if failure is not returned.
        };
    }
}
