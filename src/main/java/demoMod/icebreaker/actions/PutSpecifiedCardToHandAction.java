package demoMod.icebreaker.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class PutSpecifiedCardToHandAction extends AbstractGameAction {
    private final AbstractPlayer p;
    private final Predicate<AbstractCard> condition;
    private final Consumer<AbstractCard> extraAction;
    private final CardGroup targetGroup;

    public PutSpecifiedCardToHandAction(int amount, Predicate<AbstractCard> condition) {
        this(amount, AbstractDungeon.player.drawPile, condition);
    }

    public PutSpecifiedCardToHandAction(int amount, CardGroup targetGroup, Predicate<AbstractCard> condition) {
        this(amount, targetGroup, condition, card -> {});
    }

    public PutSpecifiedCardToHandAction(int amount, CardGroup targetGroup, Predicate<AbstractCard> condition, Consumer<AbstractCard> extraAction) {
        this.p = AbstractDungeon.player;
        this.setValues(this.p, this.p, amount);
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_MED;
        this.condition = condition;
        this.targetGroup = targetGroup;
        this.extraAction = extraAction;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_MED) {
            if (this.targetGroup.isEmpty()) {
                this.isDone = true;
                return;
            }

            CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

            for (AbstractCard card : this.targetGroup.group) {
                if (condition.test(card)) {
                    tmp.addToRandomSpot(card);
                }
            }

            if (tmp.size() == 0) {
                this.isDone = true;
                return;
            }

            for(int i = 0; i < this.amount; ++i) {
                if (!tmp.isEmpty()) {
                    tmp.shuffle();
                    AbstractCard card = tmp.getBottomCard();
                    tmp.removeCard(card);
                    if (this.p.hand.size() == 10) {
                        this.targetGroup.moveToDiscardPile(card);
                        this.p.createHandIsFullDialog();
                    } else {
                        card.unhover();
                        card.lighten(true);
                        card.setAngle(0.0F);
                        card.drawScale = 0.12F;
                        card.targetDrawScale = 0.75F;
                        if (AbstractDungeon.player.drawPile.contains(card)) {
                            card.current_x = CardGroup.DRAW_PILE_X;
                        } else {
                            card.current_x = CardGroup.DISCARD_PILE_X;
                        }
                        card.current_y = CardGroup.DRAW_PILE_Y;
                        this.targetGroup.removeCard(card);
                        AbstractDungeon.player.hand.addToTop(card);
                        this.extraAction.accept(card);
                    }
                }
            }
            AbstractDungeon.player.hand.refreshHandLayout();
            AbstractDungeon.player.hand.applyPowers();
            this.isDone = true;
        }

        this.tickDuration();
    }
}
