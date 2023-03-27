package demoMod.icebreaker.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.actions.utility.UnlimboAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import demoMod.icebreaker.IceBreaker;

import java.util.ArrayList;

public class CascadeIceWallAction extends AbstractGameAction {

    private static final float DURATION = Settings.ACTION_DUR_XFAST;

    public CascadeIceWallAction(AbstractCreature target) {
        this.duration = DURATION;
        this.actionType = AbstractGameAction.ActionType.WAIT;
        this.source = AbstractDungeon.player;
        this.target = target;
    }

    private static class CardAndFrom {
        public CardAndFrom(AbstractCard c, ArrayList<AbstractCard> cl) {
            this.c = c;
            this.cl = cl;
        }

        public AbstractCard c;
        public ArrayList<AbstractCard> cl;
    }

    public void update() {
        if (this.duration == DURATION) {
            AbstractPlayer p = AbstractDungeon.player;

            ArrayList<CardAndFrom> cards = new ArrayList<>();
            for (AbstractCard c: AbstractDungeon.player.drawPile.group) {
                if (c.cardID.equals(IceBreaker.makeID("CascadeIceWall"))) {
                    cards.add(new CardAndFrom(c, AbstractDungeon.player.drawPile.group));
                }
            }
            for (AbstractCard c: AbstractDungeon.player.hand.group) {
                if (c.cardID.equals(IceBreaker.makeID("CascadeIceWall"))) {
                    cards.add(new CardAndFrom(c, AbstractDungeon.player.hand.group));
                }
            }
            for (AbstractCard c: AbstractDungeon.player.discardPile.group) {
                if (c.cardID.equals(IceBreaker.makeID("CascadeIceWall"))) {
                    cards.add(new CardAndFrom(c, AbstractDungeon.player.discardPile.group));
                }
            }
            for (CardAndFrom c : cards) {
                c.cl.remove(c.c);
                (AbstractDungeon.getCurrRoom()).souls.remove(c.c);
                c.c.exhaustOnUseOnce = true;
                p.limbo.group.add(c.c);
                c.c.current_y = -200.0F * Settings.scale;
                c.c.target_x = Settings.WIDTH / 2.0F - 300.0F * Settings.xScale;
                c.c.target_y = Settings.HEIGHT / 2.0F;
                c.c.targetAngle = 0.0F;
                c.c.lighten(false);
                c.c.drawScale = 0.12F;
                c.c.targetDrawScale = 0.75F;
                c.c.applyPowers();
                addToTop(new NewQueueCardAction(c.c, null, false, true));
                addToTop(new UnlimboAction(c.c));
                if (!Settings.FAST_MODE) {
                    addToTop(new WaitAction(Settings.ACTION_DUR_MED));
                } else {
                    addToTop(new WaitAction(Settings.ACTION_DUR_FASTER));
                }
            }
            this.isDone = true;
        }
    }
}
