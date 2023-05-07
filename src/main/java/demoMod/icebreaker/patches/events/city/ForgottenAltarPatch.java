package demoMod.icebreaker.patches.events.city;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.city.ForgottenAltar;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.EventStrings;
import demoMod.icebreaker.IceBreaker;
import demoMod.icebreaker.characters.IceBreakerCharacter;

public class ForgottenAltarPatch {
    public static final String ID = IceBreaker.makeID("Forgotten Altar");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    public static final String NAME = eventStrings.NAME;
    public static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    public static final String[] OPTIONS = eventStrings.OPTIONS;

    @SpirePatch(
            clz = ForgottenAltar.class,
            method = SpirePatch.CONSTRUCTOR
    )
    public static class PatchConstructor {
        public static void Postfix(ForgottenAltar event) {
            if (AbstractDungeon.player instanceof IceBreakerCharacter) {
                event.imageEventText.updateDialogOption(2, OPTIONS[5] + (int)(AbstractDungeon.player.maxHealth * 0.25F) + OPTIONS[6]);
            }
        }
    }

    @SpirePatch(
            clz = ForgottenAltar.class,
            method = "buttonEffect"
    )
    public static class PatchButtonEffect {
        public static SpireReturn<Void> Prefix(ForgottenAltar event, int buttonPressed) {
            if (AbstractDungeon.player instanceof IceBreakerCharacter) {
                int screenNum = ReflectionHacks.getPrivate(event, AbstractEvent.class, "screenNum");
                if (screenNum == 0) {
                    switch (buttonPressed) {
                        case 2:
                            CardCrawlGame.sound.play("BLUNT_HEAVY");
                            CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.HIGH, ScreenShake.ShakeDur.MED, true);
                            int healAmount = (int)(AbstractDungeon.player.maxHealth * 0.25F);
                            AbstractDungeon.player.heal(healAmount);
                            AbstractDungeon.player.decreaseMaxHealth(5);
                            event.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                            event.imageEventText.updateDialogOption(0, OPTIONS[7]);
                            event.imageEventText.clearRemainingOptions();
                            ReflectionHacks.setPrivate(event, AbstractEvent.class, "screenNum", 99);
                            AbstractEvent.logMetricHeal("Forgotten Altar", "Smashed Altar", healAmount);
                            return SpireReturn.Return(null);
                    }
                }
            }
            return SpireReturn.Continue();
        }
    }
}
