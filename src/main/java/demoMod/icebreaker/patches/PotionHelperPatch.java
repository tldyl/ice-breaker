package demoMod.icebreaker.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.PotionHelper;
import com.megacrit.cardcrawl.potions.CultistPotion;
import com.megacrit.cardcrawl.potions.SteroidPotion;
import com.megacrit.cardcrawl.potions.StrengthPotion;
import demoMod.icebreaker.enums.AbstractPlayerEnum;

public class PotionHelperPatch {
    @SpirePatch(
            clz = PotionHelper.class,
            method = "initialize"
    )
    public static class PatchInitialize {
        public static void Postfix(AbstractPlayer.PlayerClass chosenClass) {
            if (chosenClass == AbstractPlayerEnum.ICEBREAKER) {
                PotionHelper.potions.remove(StrengthPotion.POTION_ID);
                PotionHelper.potions.remove(CultistPotion.POTION_ID);
                PotionHelper.potions.remove(SteroidPotion.POTION_ID);
            }
        }
    }
}
