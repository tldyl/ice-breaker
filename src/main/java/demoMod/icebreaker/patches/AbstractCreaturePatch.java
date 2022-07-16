package demoMod.icebreaker.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.AbstractCreature;
import demoMod.icebreaker.powers.TimeStasisPower;

public class AbstractCreaturePatch {
    @SpirePatch(
            clz = AbstractCreature.class,
            method = "loseBlock",
            paramtypez = {
                    int.class,
                    boolean.class
            }
    )
    public static class PatchLoseBlock {
        public static SpireReturn<Void> Prefix(AbstractCreature creature, int amount, boolean noAnimation) {
            if (creature.hasPower(TimeStasisPower.POWER_ID)) {
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }
}
