package demoMod.icebreaker.patches.events.exordium;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.exordium.Cleric;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import demoMod.icebreaker.IceBreaker;
import demoMod.icebreaker.characters.IceBreakerCharacter;

public class ClericPatch {
    public static final String ID = IceBreaker.makeID("The Cleric");
    private static final EventStrings eventStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;
    public static final String[] OPTIONS;

    @SpirePatch(
            clz = Cleric.class,
            method = SpirePatch.CONSTRUCTOR
    )
    public static class PatchConstructor {
        @SpireInsertPatch(rloc = 1)
        public static SpireReturn<Void> Insert(Cleric event) {
            if (AbstractDungeon.player instanceof IceBreakerCharacter) {
                ReflectionHacks.setPrivate(event, AbstractEvent.class, "body", DESCRIPTIONS[0]);
                int healAmt = (int)(AbstractDungeon.player.maxHealth * 0.25F);
                event.imageEventText.setDialogOption(OPTIONS[0] + healAmt + OPTIONS[4]);
                event.imageEventText.setDialogOption(OPTIONS[1]);
                event.imageEventText.setDialogOption(OPTIONS[2]);
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz = Cleric.class,
            method = "buttonEffect"
    )
    public static class PatchButtonEffect {
        public static SpireReturn<Void> Prefix(Cleric event, int buttonPressed) {
            if (AbstractDungeon.player instanceof IceBreakerCharacter) {
                int screenNum = ReflectionHacks.getPrivate(event, AbstractEvent.class, "screenNum");
                switch (screenNum) {
                    case 0:
                        switch (buttonPressed) {
                            case 0:
                                AbstractDungeon.player.heal((int)(AbstractDungeon.player.maxHealth * 0.25F));
                                event.showProceedScreen(DESCRIPTIONS[1]);
                                AbstractEvent.logMetricHealAtCost("The Cleric", "Healed", 0, (int)(AbstractDungeon.player.maxHealth * 0.25F));
                                break;
                            case 1:
                                if (CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck.getPurgeableCards()).size() > 0) {
                                    AbstractDungeon.gridSelectScreen.open(CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck.getPurgeableCards()), 1, OPTIONS[3], false, false, false, true);
                                }
                                event.showProceedScreen(DESCRIPTIONS[2]);
                                break;
                            default:
                                event.showProceedScreen(DESCRIPTIONS[3]);
                                AbstractEvent.logMetric("The Cleric", "Leave");
                                break;
                        }
                        break;
                    default:
                        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
                        AbstractDungeon.dungeonMapScreen.open(false);
                }
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }

    static {
        eventStrings = CardCrawlGame.languagePack.getEventString(ID);
        NAME = eventStrings.NAME;
        DESCRIPTIONS = eventStrings.DESCRIPTIONS;
        OPTIONS = eventStrings.OPTIONS;
    }
}
