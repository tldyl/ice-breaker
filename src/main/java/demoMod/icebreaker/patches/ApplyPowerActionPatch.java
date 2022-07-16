package demoMod.icebreaker.patches;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import demoMod.icebreaker.IceBreaker;
import demoMod.icebreaker.characters.IceBreakerCharacter;

public class ApplyPowerActionPatch {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(IceBreaker.makeID("ApplyStrengthNotice"));

    @SpirePatch(
            clz = ApplyPowerAction.class,
            method = "update"
    )
    public static class PatchUpdate{
        @SpireInsertPatch(rloc = 24)
        public static SpireReturn<Void> Insert(ApplyPowerAction action) {
            AbstractPlayer p = AbstractDungeon.player;
            if (action.target instanceof IceBreakerCharacter) {
                if (((AbstractPower) ReflectionHacks.getPrivate(action, ApplyPowerAction.class, "powerToApply")).ID.equals(StrengthPower.POWER_ID)) {
                    action.isDone = true;
                    AbstractDungeon.effectList.add(new ThoughtBubble(p.dialogX, p.dialogY, uiStrings.TEXT[0], true));
                    return SpireReturn.Return(null);
                }
            }
            return SpireReturn.Continue();
        }
    }
}
