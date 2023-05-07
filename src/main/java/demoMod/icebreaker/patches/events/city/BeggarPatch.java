package demoMod.icebreaker.patches.events.city;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.city.Beggar;
import com.megacrit.cardcrawl.localization.EventStrings;
import demoMod.icebreaker.IceBreaker;
import demoMod.icebreaker.characters.IceBreakerCharacter;
import demoMod.icebreaker.enums.BeggarCurScreenEnum;

public class BeggarPatch {
    public static final String ID = IceBreaker.makeID("Beggar");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    public static final String NAME = eventStrings.NAME;
    public static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    public static final String[] OPTIONS = eventStrings.OPTIONS;

    @SpirePatch(
            clz = Beggar.class,
            method = SpirePatch.CONSTRUCTOR
    )
    public static class PatchConstructor {
        public static SpireReturn<Void> Prefix(Beggar event) {
            if (AbstractDungeon.player instanceof IceBreakerCharacter) {
                event.imageEventText.setDialogOption(OPTIONS[3]);
                event.hasDialog = true;
                event.hasFocus = true;
                ReflectionHacks.setPrivate(event, Beggar.class, "screen", Beggar.CurScreen.INTRO);
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz = Beggar.class,
            method = "update"
    )
    public static class PatchUpdate {
        @SpireInsertPatch(rloc = 17)
        public static SpireReturn<Void> Insert(Beggar event) {
            Beggar.CurScreen screen = ReflectionHacks.getPrivate(event, Beggar.class, "screen");
            if (screen.name().equals("SELECT")) {
                ReflectionHacks.setPrivate(event, Beggar.class, "screen", Beggar.CurScreen.LEAVE);
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz = Beggar.class,
            method = "buttonEffect"
    )
    public static class PatchButtonEffect {
        public static SpireReturn<Void> Prefix(Beggar event, int buttonPressed) {
            if (AbstractDungeon.player instanceof IceBreakerCharacter) {
                Beggar.CurScreen screen = ReflectionHacks.getPrivate(event, Beggar.class, "screen");
                switch (screen.name()) {
                    case "INTRO":
                        event.imageEventText.loadImage("images/events/cleric.jpg");
                        event.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        event.imageEventText.clearAllDialogs();
                        event.imageEventText.setDialogOption(OPTIONS[0]);
                        event.imageEventText.setDialogOption(OPTIONS[1] + (int)(AbstractDungeon.player.maxHealth * 0.25F) + OPTIONS[2]);
                        event.imageEventText.setDialogOption(OPTIONS[4]);
                        ReflectionHacks.setPrivate(event, Beggar.class, "screen", BeggarCurScreenEnum.SELECT);
                        return SpireReturn.Return(null);
                    case "SELECT":
                        switch (buttonPressed) {
                            case 0:
                                AbstractDungeon.gridSelectScreen.open(CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck.getPurgeableCards()), 1, OPTIONS[5], false, false, false, true);
                                event.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                                break;
                            case 1:
                                AbstractDungeon.player.heal((int)(AbstractDungeon.player.maxHealth * 0.25F));
                                event.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                                ReflectionHacks.setPrivate(event, Beggar.class, "screen", Beggar.CurScreen.LEAVE);
                                break;
                            default:
                                event.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        }
                        event.imageEventText.clearAllDialogs();
                        event.imageEventText.setDialogOption(OPTIONS[4]);
                        return SpireReturn.Return(null);
                }
            }
            return SpireReturn.Continue();
        }
    }
}
