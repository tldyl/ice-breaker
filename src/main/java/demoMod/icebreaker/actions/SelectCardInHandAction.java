package demoMod.icebreaker.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class SelectCardInHandAction extends AbstractGameAction {
    private final Predicate<AbstractCard> condition;
    private final Consumer<AbstractCard> action;
    private final AbstractPlayer p;
    private final List<AbstractCard> filteredCards = new ArrayList<>();
    private final boolean anyNumber;

    public SelectCardInHandAction(int amount, Predicate<AbstractCard> condition, Consumer<AbstractCard> action) {
        this(amount, condition, action, false);
    }

    public SelectCardInHandAction(int amount, Predicate<AbstractCard> condition, Consumer<AbstractCard> action, boolean anyNumber) {
        setValues(AbstractDungeon.player, AbstractDungeon.player, amount);
        this.actionType = AbstractGameAction.ActionType.DRAW;
        this.duration = 0.25F;
        this.p = AbstractDungeon.player;
        this.condition = condition;
        this.action = action;
        this.anyNumber = anyNumber;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            for (AbstractCard c : this.p.hand.group) {
                if (!condition.test(c)) {
                    this.filteredCards.add(c);
                }
            }
            if (this.filteredCards.size() == this.p.hand.group.size()) {
                this.isDone = true;
                return;
            }
            if (this.p.hand.group.size() - this.filteredCards.size() == 1 && !this.anyNumber) {
                for (AbstractCard c : this.p.hand.group) {
                    if (condition.test(c)) {
                        action.accept(c);
                        this.isDone = true;
                        return;
                    }
                }
            }
            this.p.hand.group.removeAll(this.filteredCards);
            if (this.p.hand.group.size() > 1 || this.anyNumber) {
                AbstractDungeon.handCardSelectScreen.open(SelectSpecifiedCardInHandAction.TEXT[0], this.amount, this.anyNumber, this.anyNumber, false, false);
                tickDuration();
                return;
            }
            if (this.p.hand.group.size() == 1) {
                action.accept(this.p.hand.getTopCard());
                returnCards();
                this.isDone = true;
            }
        }

        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                this.p.hand.addToTop(c);
                action.accept(c);
            }

            returnCards();

            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
            AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
            this.isDone = true;
        }
        tickDuration();
    }

    private void returnCards() {
        for (AbstractCard c : this.filteredCards) {
            this.p.hand.addToTop(c);
        }
        this.p.hand.refreshHandLayout();
    }
}
