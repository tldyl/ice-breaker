package demoMod.icebreaker.patches.events.exordium;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.curses.Writhe;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.exordium.LivingWall;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import demoMod.icebreaker.IceBreaker;
import demoMod.icebreaker.actions.WaitGridSelectScreenAction;
import demoMod.icebreaker.characters.IceBreakerCharacter;

public class LivingWallPatch {
    public static final String ID = IceBreaker.makeID("Living Wall");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    public static final String NAME = eventStrings.NAME;
    public static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    public static final String[] OPTIONS = eventStrings.OPTIONS;

    @SpirePatch(
            clz = LivingWall.class,
            method = SpirePatch.CLASS
    )
    public static class PatchAddFields {
        public static SpireField<Boolean> chooseAll = new SpireField<>(() -> false);
    }

    @SpirePatch(
            clz = LivingWall.class,
            method = SpirePatch.CONSTRUCTOR
    )
    public static class PatchConstructor {
        public static void Postfix(LivingWall event) {
            if (AbstractDungeon.player instanceof IceBreakerCharacter) {
                event.imageEventText.setDialogOption(OPTIONS[3], new Writhe());
            }
        }
    }

    @SpirePatch(
            clz = LivingWall.class,
            method = "buttonEffect"
    )
    public static class PatchButtonEffect {
        public static SpireReturn<Void> Prefix(LivingWall event, int buttonPressed) {
            try {
                if (AbstractDungeon.player instanceof IceBreakerCharacter) {
                    Enum screen = ReflectionHacks.getPrivate(event, LivingWall.class, "screen");
                    switch (screen.name()) {
                        case "INTRO":
                            switch (buttonPressed) {
                                case 3:
                                    PatchAddFields.chooseAll.set(event, true);
                                    ReflectionHacks.setPrivate(event, LivingWall.class, "choice", Enum.valueOf((Class<Enum>) Class.forName("com.megacrit.cardcrawl.events.exordium.LivingWall$Choice"), "FORGET"));
                                    if (CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck.getPurgeableCards()).size() > 0) {
                                        AbstractDungeon.gridSelectScreen.open(CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck.getPurgeableCards()), 1, OPTIONS[4], false, false, false, true);
                                    }

                                    ReflectionHacks.setPrivate(event, LivingWall.class, "pickCard", true);
                                    if (PatchAddFields.chooseAll.get(event)) {
                                        event.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                                    } else {
                                        event.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                                    }
                                    event.imageEventText.clearAllDialogs();
                                    event.imageEventText.setDialogOption(OPTIONS[7]);
                                    ReflectionHacks.setPrivate(event, LivingWall.class, "screen", Enum.valueOf((Class<Enum>) Class.forName("com.megacrit.cardcrawl.events.exordium.LivingWall$CurScreen"), "RESULT"));
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

    @SpirePatch(
            clz = LivingWall.class,
            method = "update"
    )
    public static class PatchUpdate {
        @SpireInsertPatch(rloc = 1)
        public static SpireReturn<Void> Insert(LivingWall event) {
            boolean pickCard = ReflectionHacks.getPrivate(event, LivingWall.class, "pickCard");
            try {
                if (pickCard && !AbstractDungeon.isScreenUp &&
                        !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty() &&
                        PatchAddFields.chooseAll.get(event)) {
                    Enum choice = ReflectionHacks.getPrivate(event, LivingWall.class, "choice");
                    switch (choice.name()) {
                        case "FORGET":
                            WaitGridSelectScreenAction.enabled = false;
                            CardCrawlGame.sound.play("CARD_EXHAUST");
                            AbstractDungeon.topLevelEffects.add(new PurgeCardEffect(AbstractDungeon.gridSelectScreen.selectedCards.get(0), (float) (Settings.WIDTH / 2), (float) (Settings.HEIGHT / 2)));
                            AbstractEvent.logMetricCardRemoval("Living Wall", "Forget", AbstractDungeon.gridSelectScreen.selectedCards.get(0));
                            AbstractDungeon.player.masterDeck.removeCard(AbstractDungeon.gridSelectScreen.selectedCards.get(0));
                            ReflectionHacks.setPrivate(event, LivingWall.class, "choice", Enum.valueOf((Class<Enum>) Class.forName("com.megacrit.cardcrawl.events.exordium.LivingWall$Choice"), "CHANGE"));
                            if (CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck.getPurgeableCards()).size() > 0) {
                                AbstractDungeon.gridSelectScreen.open(CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck.getPurgeableCards()), 1, OPTIONS[5], false, true, false, false);
                            }
                            break;
                        case "CHANGE":
                            AbstractCard c = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
                            AbstractDungeon.player.masterDeck.removeCard(c);
                            AbstractDungeon.transformCard(c, false, AbstractDungeon.miscRng);
                            AbstractCard transCard = AbstractDungeon.getTransformedCard();
                            AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(transCard, c.current_x, c.current_y));
                            ReflectionHacks.setPrivate(event, LivingWall.class, "choice", Enum.valueOf((Class<Enum>) Class.forName("com.megacrit.cardcrawl.events.exordium.LivingWall$Choice"), "GROW"));
                            if (CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck.getPurgeableCards()).size() > 0) {
                                AbstractDungeon.gridSelectScreen.open(CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck.getPurgeableCards()), 1, OPTIONS[6], true, false, false, false);
                            }
                            break;
                        case "GROW":
                            AbstractDungeon.effectsQueue.add(new UpgradeShineEffect(Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                            AbstractDungeon.gridSelectScreen.selectedCards.get(0).upgrade();
                            AbstractCard upgCard = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
                            AbstractDungeon.player.bottledCardUpgradeCheck(upgCard);
                            ReflectionHacks.setPrivate(event, LivingWall.class, "pickCard", false);
                            AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(new Writhe(), Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                            WaitGridSelectScreenAction.enabled = true;
                            break;
                    }
                    AbstractDungeon.gridSelectScreen.selectedCards.clear();
                    return SpireReturn.Return(null);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return SpireReturn.Continue();
        }
    }
}
