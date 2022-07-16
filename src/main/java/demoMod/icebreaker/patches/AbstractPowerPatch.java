package demoMod.icebreaker.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.powers.*;
import demoMod.icebreaker.enums.CardTagEnum;

public class AbstractPowerPatch {
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
            if (card.hasTag(CardTagEnum.REMOTE) && !power.ID.equals(FlightPower.POWER_ID) && !power.ID.equals(IntangiblePower.POWER_ID) && !power.ID.equals(InvinciblePower.POWER_ID) && !power.ID.equals(ShiftingPower.POWER_ID)) {
                return SpireReturn.Return(damage);
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz = AbstractPower.class,
            method = "atDamageGive",
            paramtypez = {
                    float.class,
                    DamageInfo.DamageType.class,
                    AbstractCard.class
            }
    )
    public static class PatchAtDamageGive {
        public static SpireReturn<Float> Prefix(AbstractPower power, float damage, DamageInfo.DamageType type, AbstractCard card) {
            if (card.hasTag(CardTagEnum.MAGIC) && !power.ID.equals(FlightPower.POWER_ID) && !power.ID.equals(IntangiblePower.POWER_ID) && !power.ID.equals(InvinciblePower.POWER_ID) && !power.ID.equals(ShiftingPower.POWER_ID)) {
                return SpireReturn.Return(damage);
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz = AbstractPower.class,
            method = "modifyBlock",
            paramtypez = {
                    float.class,
                    AbstractCard.class
            }
    )
    public static class PatchModifyBlock {
        public static SpireReturn<Float> Prefix(AbstractPower power, float block, AbstractCard card) {
            if (card.hasTag(CardTagEnum.MAGIC)) {
                return SpireReturn.Return(block);
            }
            return SpireReturn.Continue();
        }
    }
}
