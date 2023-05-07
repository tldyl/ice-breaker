package demoMod.icebreaker.patches.events.city;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.events.city.Vampires;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.BloodVial;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import demoMod.icebreaker.IceBreaker;
import demoMod.icebreaker.characters.IceBreakerCharacter;

import java.util.ArrayList;

public class VampiresPatch {
    public static final String ID = IceBreaker.makeID("Vampires");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    public static final String NAME = eventStrings.NAME;
    public static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    public static final String[] OPTIONS = eventStrings.OPTIONS;

    @SpirePatch(
            clz = Vampires.class,
            method = SpirePatch.CONSTRUCTOR
    )
    public static class PatchConstructor {
        public static void Postfix(Vampires event) {
            if (AbstractDungeon.player instanceof IceBreakerCharacter) {
                int maxHpLoss = ReflectionHacks.getPrivate(event, Vampires.class, "maxHpLoss");
                event.imageEventText.updateDialogOption(0, OPTIONS[0] + maxHpLoss + OPTIONS[1]);
                if (AbstractDungeon.player.hasRelic(BloodVial.ID)) {
                    event.imageEventText.updateDialogOption(1, OPTIONS[3] + new BloodVial().name + OPTIONS[4]);
                }
            }
            PatchButtonEffect.cardsSelected = false;
        }
    }

    @SpirePatch(
            clz = AbstractImageEvent.class,
            method = "update"
    )
    public static class PatchUpdate {
        public static void Postfix(AbstractImageEvent event) {
            if (event instanceof Vampires && AbstractDungeon.player instanceof IceBreakerCharacter) {
                if (!PatchButtonEffect.cardsSelected && AbstractDungeon.gridSelectScreen.selectedCards.size() == 3) {
                    PatchButtonEffect.deleteCards(AbstractDungeon.gridSelectScreen.selectedCards);
                }
            }
        }
    }

    @SpirePatch(
            clz = Vampires.class,
            method = "buttonEffect"
    )
    public static class PatchButtonEffect {
        public static boolean cardsSelected = false;

        public static SpireReturn<Void> Prefix(Vampires event, int buttonPressed) {
            if (AbstractDungeon.player instanceof IceBreakerCharacter) {
                int screenNum = ReflectionHacks.getPrivate(event, Vampires.class, "screenNum");
                if (screenNum == 0) {
                    switch (buttonPressed) {
                        case 0:
                            CardCrawlGame.sound.play("EVENT_VAMP_BITE");
                            event.imageEventText.updateBodyText(DESCRIPTIONS[2] + DESCRIPTIONS[3]);
                            int maxHpLoss = ReflectionHacks.getPrivate(event, Vampires.class, "maxHpLoss");
                            AbstractDungeon.player.decreaseMaxHealth(maxHpLoss);
                            prepareDeleteCards();
                            AbstractEvent.logMetricObtainCardsLoseMapHP("Vampires", "Became a vampire", new ArrayList<>(), maxHpLoss);
                            ReflectionHacks.setPrivate(event, Vampires.class, "screenNum", 1);
                            event.imageEventText.updateDialogOption(0, OPTIONS[6]);
                            event.imageEventText.clearRemainingOptions();
                            break;
                        case 1:
                            if (AbstractDungeon.player.hasRelic(BloodVial.ID)) {
                                CardCrawlGame.sound.play("EVENT_VAMP_BITE");
                                event.imageEventText.updateBodyText(DESCRIPTIONS[5] + DESCRIPTIONS[6]);
                                AbstractDungeon.player.loseRelic(BloodVial.ID);
                                prepareDeleteCards();
                                AbstractEvent.logMetricObtainCardsLoseRelic("Vampires", "Became a vampire (Vial)", new ArrayList<>(), new BloodVial());
                            } else {
                                event.imageEventText.updateBodyText(DESCRIPTIONS[4]);
                                AbstractEvent.logMetricIgnored("Vampires");
                            }
                            ReflectionHacks.setPrivate(event, Vampires.class, "screenNum", 1);
                            event.imageEventText.updateDialogOption(0, OPTIONS[6]);
                            event.imageEventText.clearRemainingOptions();
                            break;
                        default:
                            event.imageEventText.updateBodyText(DESCRIPTIONS[4]);
                            AbstractEvent.logMetricIgnored("Vampires");
                            ReflectionHacks.setPrivate(event, Vampires.class, "screenNum", 1);
                            event.imageEventText.updateDialogOption(0, OPTIONS[6]);
                            event.imageEventText.clearRemainingOptions();
                            break;
                    }
                    return SpireReturn.Return(null);
                }
            }
            return SpireReturn.Continue();
        }

        private static void prepareDeleteCards() {
            CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

            for (AbstractCard card : AbstractDungeon.player.masterDeck.getPurgeableCards().group) {
                if (card.type == AbstractCard.CardType.ATTACK) {
                    tmp.addToTop(card);
                }
            }

            if (tmp.group.isEmpty()) {
                cardsSelected = true;
            } else {
                if (tmp.group.size() <= 3) {
                    deleteCards(tmp.group);
                } else {
                    CardGroup group = AbstractDungeon.player.masterDeck.getPurgeableCards();
                    group.group.removeIf(card -> card.type != AbstractCard.CardType.ATTACK);
                    AbstractDungeon.gridSelectScreen.open(group, 3, OPTIONS[7], false, false, false, true);
                }
            }
        }

        public static void deleteCards(ArrayList<AbstractCard> group) {
            cardsSelected = true;
            float displayCount = 0.0F;
            for (AbstractCard card : group) {
                card.untip();
                card.unhover();
                AbstractDungeon.topLevelEffects.add(new PurgeCardEffect(card, (float) Settings.WIDTH / 3.0F + displayCount, (float) Settings.HEIGHT / 2.0F));
                displayCount += (float) Settings.WIDTH / 6.0F;
                AbstractDungeon.player.masterDeck.removeCard(card);
            }

            AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
        }
    }
}
