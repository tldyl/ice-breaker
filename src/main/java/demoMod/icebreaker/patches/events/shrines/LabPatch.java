package demoMod.icebreaker.patches.events.shrines;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.GenericEventDialog;
import com.megacrit.cardcrawl.events.shrines.Lab;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.potions.EntropicBrew;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import demoMod.icebreaker.IceBreaker;
import demoMod.icebreaker.characters.IceBreakerCharacter;

public class LabPatch {
    public static final String ID = IceBreaker.makeID("Lab");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    public static final String NAME = eventStrings.NAME;
    public static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    public static final String[] OPTIONS = eventStrings.OPTIONS;

    @SpirePatch(
            clz = Lab.class,
            method = SpirePatch.CONSTRUCTOR
    )
    public static class PatchConstructor {
        public static void Postfix(Lab event) {
            if (AbstractDungeon.player instanceof IceBreakerCharacter) {
                event.imageEventText.updateBodyText(DESCRIPTIONS[0]);
                event.imageEventText.setDialogOption(OPTIONS[1]);
            }
        }
    }

    @SpirePatch(
            clz = Lab.class,
            method = "buttonEffect"
    )
    public static class PatchButtonEffect {
        public static SpireReturn<Void> Prefix(Lab event, int buttonPressed) {
            try {
                if (AbstractDungeon.player instanceof IceBreakerCharacter) {
                    Enum screen = ReflectionHacks.getPrivate(event, Lab.class, "screen");
                    switch (screen.name()) {
                        case "INTRO":
                            if (buttonPressed == 1) {
                                GenericEventDialog.hide();
                                AbstractDungeon.getCurrRoom().rewards.clear();
                                AbstractDungeon.getCurrRoom().rewards.add(new RewardItem(new EntropicBrew()));
                                AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
                                AbstractDungeon.combatRewardScreen.open();
                                ReflectionHacks.setPrivate(event, Lab.class, "screen", Enum.valueOf((Class<Enum>) Class.forName("com.megacrit.cardcrawl.events.shrines.Lab$CUR_SCREEN"), "COMPLETE"));
                                AbstractEvent.logMetric("Lab", "Got Potions");
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
