package demoMod.icebreaker.actions;

import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.actions.utility.UnlimboAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import demoMod.icebreaker.characters.IceBreakerCharacter;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

public class SnapAction extends AbstractGameAction {
    public static final String[] TEXT = (CardCrawlGame.languagePack.getUIString("WishAction")).TEXT;

    private AbstractPlayer player;

    private int numberOfCards; // 1, 如果之后有想让它不是1的需求请联系我。

    private boolean optional;

    public SnapAction() {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        this.player = AbstractDungeon.player;
        this.numberOfCards = 1; // = numberOfCards;
        this.optional = false;
    }

    private void play(AbstractCard c) {
        player.drawPile.group.remove(c);
        (AbstractDungeon.getCurrRoom()).souls.remove(c);
        player.limbo.group.add(c);
        c.current_y = -200.0F * Settings.scale;
        c.target_x = Settings.WIDTH / 2.0F - 300.0F * Settings.xScale;
        c.target_y = Settings.HEIGHT / 2.0F;
        c.targetAngle = 0.0F;
        c.lighten(false);
        c.drawScale = 0.12F;
        c.targetDrawScale = 0.75F;
        c.applyPowers();
        this.target = AbstractDungeon.getCurrRoom().monsters
                .getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
        addToTop(new NewQueueCardAction(c, this.target, false, true));
        addToTop(new UnlimboAction(c));
        if (!Settings.FAST_MODE) {
            addToTop(new WaitAction(Settings.ACTION_DUR_MED));
        } else {
            addToTop(new WaitAction(Settings.ACTION_DUR_FASTER));
        }
    }

    public void update() {
        if (this.duration == this.startDuration) {
            if (this.player.drawPile.isEmpty() || this.numberOfCards <= 0) {
                this.isDone = true;
                return;
            }
            if (this.player.drawPile.size() <= 1 && !this.optional) {
                ArrayList<AbstractCard> cardsToMove = new ArrayList<>();
                for (AbstractCard c : this.player.drawPile.group)
                    if (c.type == AbstractCard.CardType.ATTACK) {
                        cardsToMove.add(c);
                    }
                for (AbstractCard c : cardsToMove) {
                    play(c);
                }
                this.isDone = true;
                return;
            }
            CardGroup temp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            for (AbstractCard c : this.player.drawPile.group) {
                if (c.type == AbstractCard.CardType.ATTACK) {
                    temp.group.add(c);
                }
            }
            if (temp.isEmpty()) {
                isDone = true;
                return;
            }
            temp.sortAlphabetically(true);
            temp.sortByRarityPlusStatusCardType(false);
            if (this.numberOfCards == 1) {
                if (this.optional) {
                    AbstractDungeon.gridSelectScreen.open(temp, this.numberOfCards, true, TEXT[0]);
                } else {
                    AbstractDungeon.gridSelectScreen.open(temp, this.numberOfCards, TEXT[0], false);
                }
            }
//            else if (this.optional) {
//                AbstractDungeon.gridSelectScreen.open(temp, this.numberOfCards, true, TEXT[1] + this.numberOfCards + TEXT[2]);
//            } else {
//                AbstractDungeon.gridSelectScreen.open(temp, this.numberOfCards, TEXT[1] + this.numberOfCards + TEXT[2], false);
//            }
            tickDuration();
            return;
        }
        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
                play(c);
            }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            AbstractDungeon.player.hand.refreshHandLayout();
        }
        tickDuration();
    }
}
