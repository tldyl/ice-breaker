package demoMod.icebreaker.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import demoMod.icebreaker.cards.lightlemon.AbstractLightLemonCard;
import demoMod.icebreaker.interfaces.ModifyMagicNumberSubscriber;

public class AbstractCardPatch {
    @SpirePatch(
            clz = AbstractCard.class,
            method = "calculateCardDamage"
    )
    public static class PatchCalculateCardDamage {
        public static void Prefix(AbstractCard card, AbstractMonster mo) {
            if (card instanceof AbstractLightLemonCard) {
                AbstractLightLemonCard lightLemonCard = (AbstractLightLemonCard) card;
                lightLemonCard.m2 = lightLemonCard.baseM2;
            }
            card.magicNumber = card.baseMagicNumber;
            for (AbstractPower p : AbstractDungeon.player.powers) {
                if (p instanceof ModifyMagicNumberSubscriber) {
                    ModifyMagicNumberSubscriber subscriber = (ModifyMagicNumberSubscriber) p;
                    card.magicNumber = subscriber.onModifyMagicNumber(card.magicNumber, card);
                    if (card instanceof AbstractLightLemonCard) {
                        AbstractLightLemonCard lightLemonCard = (AbstractLightLemonCard) card;
                        lightLemonCard.m2 = subscriber.onModifyAnotherMagicNumber(lightLemonCard.m2, card);
                    }
                }
            }
            if (card.magicNumber != card.baseMagicNumber) card.isMagicNumberModified = true;
        }
    }
}
