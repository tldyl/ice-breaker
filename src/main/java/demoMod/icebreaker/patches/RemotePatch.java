package demoMod.icebreaker.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.FlightPower;
import com.megacrit.cardcrawl.powers.ThornsPower;
import demoMod.icebreaker.enums.CardTagEnum;

public class RemotePatch {
    @SpirePatch(
            clz = AbstractPower.class,
            method = "atDamageFinalReceive",
            paramtypez = {
                    float.class,
                    DamageInfo.DamageType.class,
                    AbstractCard.class
            }
    )
    public static class PatchAtDamageFinalReceive {
        public static SpireReturn<Float> Prefix(AbstractPower power, float damage, DamageInfo.DamageType type, AbstractCard card) {
            if ((power instanceof FlightPower) || (power instanceof ThornsPower)) {
                if (card.hasTag(CardTagEnum.REMOTE) && type == DamageInfo.DamageType.NORMAL) {
                    return SpireReturn.Return(damage);
                }
            }
            return SpireReturn.Continue();
        }
    }
}
