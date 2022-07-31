package demoMod.icebreaker.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import demoMod.icebreaker.IceBreaker;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class SelectCardInCardGroupAction extends AbstractGameAction {
    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString(IceBreaker.makeID("SelectCardInCardGroupAction")).TEXT;
    private final AbstractPlayer player;
    private final Predicate<AbstractCard> condition;
    private final Consumer<AbstractCard> action;
    private final CardGroup cardGroup;

    public SelectCardInCardGroupAction(int amount, Predicate<AbstractCard> condition, Consumer<AbstractCard> action, CardGroup cardGroup) {
        this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
        this.duration = (this.startDuration = Settings.ACTION_DUR_FAST);
        this.player = AbstractDungeon.player;
        this.condition = condition;
        this.action = action;
        this.cardGroup = cardGroup;
        this.amount = amount;
    }

    @Override
    public void update() {
        if (this.duration == this.startDuration) {
            CardGroup temp;
            if (this.cardGroup.isEmpty()) {
                this.isDone = true;
                return;
            }
            temp = new CardGroup(com.megacrit.cardcrawl.cards.CardGroup.CardGroupType.UNSPECIFIED);
            for (AbstractCard c : this.cardGroup.group) {
                if (this.condition.test(c)) temp.addToTop(c);
            }
            temp.sortAlphabetically(true);
            temp.sortByRarityPlusStatusCardType(false);
            AbstractDungeon.gridSelectScreen.open(temp, this.amount, String.format(TEXT[0], this.amount), false);
            tickDuration();
            return;
        }

        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
                this.action.accept(c);
            }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            AbstractDungeon.player.hand.refreshHandLayout();
        }
        tickDuration();
    }
}
