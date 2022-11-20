package demoMod.icebreaker.patches;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.NoDrawPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import demoMod.icebreaker.characters.IceBreakerCharacter;
import javassist.CtBehavior;

import java.util.ArrayList;

public class ApplyPowerActionPatch {
    @SpirePatch(
            clz = ApplyPowerAction.class,
            method = "update"
    )
    public static class PatchUpdate {
        @SpireInsertPatch(locator = Locator.class)
        public static SpireReturn<Void> Insert(ApplyPowerAction action) {
            AbstractPower powerToApply = ReflectionHacks.getPrivate(action, ApplyPowerAction.class, "powerToApply");
            AbstractPlayer p = AbstractDungeon.player;
            if (powerToApply instanceof StrengthPower && powerToApply.owner instanceof IceBreakerCharacter) {
                int amount = 0;
                if (p.hasPower(StrengthPower.POWER_ID)) {
                    AbstractPower strength = p.getPower(StrengthPower.POWER_ID);
                    amount = strength.amount;
                    if (amount + powerToApply.amount > 0) {
                        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(p, p, strength));
                    }
                }
                if (amount + powerToApply.amount > 0) {
                    AbstractDungeon.effectList.add(new ThoughtBubble(p.dialogX, p.dialogY, AbstractPowerPatch.uiStrings.TEXT[0], true));
                    action.isDone = true;
                    return SpireReturn.Return(null);
                }
            }
            return SpireReturn.Continue();
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher matcher = new Matcher.InstanceOfMatcher(NoDrawPower.class);
                return LineFinder.findInOrder(ctBehavior, new ArrayList<>(), matcher);
            }
        }
    }
}
