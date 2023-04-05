package demoMod.icebreaker.actions;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import demoMod.icebreaker.IceBreaker;

import java.util.function.Predicate;

public class SelectSpecifiedCardInHandAction extends AbstractGameAction {
    private AbstractPlayer p;
    private Predicate<AbstractCard> condition;
    private static final UIStrings uiStrings;
    public static final String[] TEXT;

    public SelectSpecifiedCardInHandAction(int amount, Predicate<AbstractCard> condition) {
        this.p = AbstractDungeon.player;
        this.setValues(this.p, AbstractDungeon.player, amount);
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_MED;
        this.condition = condition;
    }

    @Override
    public void update() {
        AbstractCard card;
        if (this.duration == Settings.ACTION_DUR_MED) {
            CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

            for (AbstractCard c : this.p.drawPile.group) {
                if (condition.test(c)) {
                    tmp.addToRandomSpot(c);
                }
            }

            if (tmp.size() == 0) {
                this.isDone = true;
            } else if (tmp.size() == 1) {
                card = tmp.getTopCard();
                if (this.p.hand.size() == BaseMod.MAX_HAND_SIZE) {
                    this.p.drawPile.moveToDiscardPile(card);
                    this.p.createHandIsFullDialog();
                } else {
                    card.unhover();
                    card.lighten(true);
                    card.setAngle(0.0F);
                    card.drawScale = 0.12F;
                    card.targetDrawScale = 0.75F;
                    card.current_x = CardGroup.DRAW_PILE_X;
                    card.current_y = CardGroup.DRAW_PILE_Y;
                    this.p.drawPile.removeCard(card);
                    AbstractDungeon.player.hand.addToTop(card);
                    AbstractDungeon.player.hand.refreshHandLayout();
                    AbstractDungeon.player.hand.applyPowers();
                }

                this.isDone = true;
            } else {
                AbstractDungeon.gridSelectScreen.open(tmp, this.amount, String.format(TEXT[0], this.amount), false);
                this.tickDuration();
            }
        } else {
            if (AbstractDungeon.gridSelectScreen.selectedCards.size() != 0) {

                for (AbstractCard selectedCard : AbstractDungeon.gridSelectScreen.selectedCards) {
                    card = selectedCard;
                    card.unhover();
                    if (this.p.hand.size() == 10) {
                        this.p.drawPile.moveToDiscardPile(card);
                        this.p.createHandIsFullDialog();
                    } else {
                        this.p.drawPile.removeCard(card);
                        this.p.hand.addToTop(card);
                    }

                    this.p.hand.refreshHandLayout();
                    this.p.hand.applyPowers();
                }

                AbstractDungeon.gridSelectScreen.selectedCards.clear();
                this.p.hand.refreshHandLayout();
            }

            this.tickDuration();
        }
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString(IceBreaker.makeID("SelectSpecifiedCardInHandAction"));
        TEXT = uiStrings.TEXT;
    }
}
