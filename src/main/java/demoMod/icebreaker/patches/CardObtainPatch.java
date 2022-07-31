package demoMod.icebreaker.patches;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.FastCardObtainEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import demoMod.icebreaker.interfaces.CardAddToDeckSubscriber;

public class CardObtainPatch {
    @SpirePatch(
            clz = FastCardObtainEffect.class,
            method = "update"
    )
    @SpirePatch(
            clz = ShowCardAndObtainEffect.class,
            method = "update"
    )
    public static class PatchCardObtain {
        public static void Postfix(AbstractGameEffect effect) {
            if (effect.isDone) {
                AbstractCard card = ReflectionHacks.getPrivate(effect, effect.getClass(), "card");
                if (card instanceof CardAddToDeckSubscriber) {
                    ((CardAddToDeckSubscriber) card).onAddToMasterDeck();
                }
            }
        }
    }
}
