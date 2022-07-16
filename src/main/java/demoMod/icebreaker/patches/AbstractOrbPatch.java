package demoMod.icebreaker.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Plasma;
import com.megacrit.cardcrawl.powers.AbstractPower;
import demoMod.icebreaker.powers.ResonancePower;

public class AbstractOrbPatch {
    @SpirePatch(
            clz = AbstractOrb.class,
            method = "applyFocus"
    )
    public static class PatchApplyFocus {
        public static void Postfix(AbstractOrb orb) {
            AbstractPower power = AbstractDungeon.player.getPower(ResonancePower.POWER_ID);
            if (power != null && !(orb instanceof Plasma)) {
                orb.passiveAmount = Math.max(0, orb.passiveAmount + power.amount);
                orb.evokeAmount = Math.max(0, orb.evokeAmount + power.amount);
            }
        }
    }
}
