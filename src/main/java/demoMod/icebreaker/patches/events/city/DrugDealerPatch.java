package demoMod.icebreaker.patches.events.city;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.city.DrugDealer;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import demoMod.icebreaker.IceBreaker;
import demoMod.icebreaker.characters.IceBreakerCharacter;

public class DrugDealerPatch {
    public static final String ID = IceBreaker.makeID("Drug Dealer");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    public static final String NAME = eventStrings.NAME;
    public static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    public static final String[] OPTIONS = eventStrings.OPTIONS;
    private static int selectedOption = 0;

    @SpirePatch(
            clz = DrugDealer.class,
            method = SpirePatch.CONSTRUCTOR
    )
    public static class PatchConstructor {
        public static SpireReturn<Void> Prefix(DrugDealer event) {
            if (AbstractDungeon.player instanceof IceBreakerCharacter) {
                event.imageEventText.updateBodyText(DESCRIPTIONS[0]);
                boolean hasRemovableCards = !CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck.getPurgeableCards()).isEmpty();
                event.imageEventText.setDialogOption(hasRemovableCards ? OPTIONS[0] + 7 + OPTIONS[1] : OPTIONS[5], !hasRemovableCards);
                boolean hasTwoRemovableCards = CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck.getPurgeableCards()).size() >= 2;
                event.imageEventText.setDialogOption(hasTwoRemovableCards ? OPTIONS[2] : OPTIONS[4], !hasTwoRemovableCards);
                event.imageEventText.setDialogOption(OPTIONS[3]);
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz = DrugDealer.class,
            method = "buttonEffect"
    )
    public static class PatchButtonEffect {
        public static SpireReturn<Void> Prefix(DrugDealer event, int buttonPressed) {
            if (AbstractDungeon.player instanceof IceBreakerCharacter) {
                int screenNum = ReflectionHacks.getPrivate(event, DrugDealer.class, "screenNum");
                if (screenNum == 0) {
                    selectedOption = buttonPressed;
                    switch (buttonPressed) {
                        case 0:
                            AbstractDungeon.player.damage(new DamageInfo(null, 7));
                            AbstractDungeon.effectList.add(new FlashAtkImgEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, AbstractGameAction.AttackEffect.POISON));
                            if (!AbstractDungeon.isScreenUp) {
                                AbstractDungeon.gridSelectScreen.open(CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck.getPurgeableCards()), 1, OPTIONS[7], false, false, false, true);
                            } else {
                                AbstractDungeon.dynamicBanner.hide();
                                AbstractDungeon.previousScreen = AbstractDungeon.screen;
                                AbstractDungeon.gridSelectScreen.open(CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck.getPurgeableCards()), 1, OPTIONS[7], false, false, false, true);
                            }
                            event.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                            event.imageEventText.updateDialogOption(0, OPTIONS[3]);
                            event.imageEventText.clearRemainingOptions();
                            break;
                        case 1:
                            return SpireReturn.Continue();
                        case 2:
                            event.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                            event.imageEventText.updateDialogOption(0, OPTIONS[3]);
                            event.imageEventText.clearRemainingOptions();
                            break;
                    }
                    ReflectionHacks.setPrivate(event, DrugDealer.class, "screenNum", 1);
                    return SpireReturn.Return(null);
                }
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz = DrugDealer.class,
            method = "update"
    )
    public static class PatchUpdate {
        public static SpireReturn<Void> Prefix(DrugDealer event) {
            if (AbstractDungeon.player instanceof IceBreakerCharacter) {
                boolean cardsSelected = ReflectionHacks.getPrivate(event, DrugDealer.class, "cardsSelected");
                if (selectedOption == 0 && !cardsSelected) {
                    if (AbstractDungeon.gridSelectScreen.selectedCards.size() == 1) {
                        AbstractCard card = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
                        AbstractDungeon.topLevelEffects.add(new PurgeCardEffect(card, (float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2)));
                        AbstractEvent.logMetricCardRemovalAndDamage("Drug Dealer", "Card Removal", card, 7);
                        AbstractDungeon.player.masterDeck.removeCard(card);
                        AbstractDungeon.gridSelectScreen.selectedCards.clear();
                        AbstractDungeon.getCurrRoom().rewardPopOutTimer = 0.25F;
                        ReflectionHacks.setPrivate(event, DrugDealer.class, "cardsSelected", true);
                        return SpireReturn.Return(null);
                    }
                }
            }
            return SpireReturn.Continue();
        }
    }
}
