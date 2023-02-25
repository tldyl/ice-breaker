package demoMod.icebreaker.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import demoMod.icebreaker.cards.lightlemon.RainbowViolet;

public class CardLibraryPatch {
    @SpirePatch(
            clz = CardLibrary.class,
            method = "getCopy",
            paramtypez = {
                    String.class,
                    int.class,
                    int.class
            }
    )
    public static class PatchGetCopy {
        public static SpireReturn<AbstractCard> Prefix(String key, int upgradeTime, int misc) {
            if (RainbowViolet.ID.equals(key)) {
                AbstractCard card = new RainbowViolet();
                card.misc = misc;
                for(int i = 0; i < upgradeTime; i++) {
                    card.upgrade();
                }
                return SpireReturn.Return(card);
            }
            return SpireReturn.Continue();
        }
    }
}
