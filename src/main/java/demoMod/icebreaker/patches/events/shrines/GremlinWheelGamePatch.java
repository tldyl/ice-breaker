package demoMod.icebreaker.patches.events.shrines;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.shrines.GremlinWheelGame;
import com.megacrit.cardcrawl.localization.EventStrings;
import demoMod.icebreaker.IceBreaker;
import demoMod.icebreaker.characters.IceBreakerCharacter;

public class GremlinWheelGamePatch {
    public static final String ID = IceBreaker.makeID("Wheel of Change");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    public static final String NAME = eventStrings.NAME;
    public static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    public static final String[] OPTIONS = eventStrings.OPTIONS;

    @SpirePatch(
            clz = GremlinWheelGame.class,
            method = SpirePatch.CONSTRUCTOR
    )
    public static class PatchConstructor {
        public static void Postfix(GremlinWheelGame event) {
            if (AbstractDungeon.player instanceof IceBreakerCharacter) {
                event.imageEventText.updateDialogOption(0, OPTIONS[0]);
                event.imageEventText.setDialogOption(OPTIONS[1]);
            }
        }
    }

    @SpirePatch(
            clz = GremlinWheelGame.class,
            method = "buttonEffect"
    )
    public static class PatchButtonEffect {
        public static SpireReturn<Void> Prefix(GremlinWheelGame event, int buttonPressed) {
            try {
                if (AbstractDungeon.player instanceof IceBreakerCharacter) {
                    Enum screen = ReflectionHacks.getPrivate(event, GremlinWheelGame.class, "screen");
                    if ("INTRO".equals(screen.name())) {
                        if (buttonPressed == 1) {
                            event.imageEventText.updateBodyText(DESCRIPTIONS[8]);
                            event.imageEventText.clearAllDialogs();
                            event.imageEventText.setDialogOption(OPTIONS[9]);
                            ReflectionHacks.setPrivate(event, GremlinWheelGame.class, "screen", Enum.valueOf((Class<Enum>) Class.forName("com.megacrit.cardcrawl.events.shrines.GremlinWheelGame$CUR_SCREEN"), "LEAVE"));
                            return SpireReturn.Return(null);
                        }
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return SpireReturn.Continue();
        }
    }
}
