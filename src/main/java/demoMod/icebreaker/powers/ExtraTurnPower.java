package demoMod.icebreaker.powers;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import demoMod.icebreaker.IceBreaker;
import demoMod.icebreaker.interfaces.EnterOrExitExtraTurnSubscriber;

import java.util.ArrayList;
import java.util.List;

public class ExtraTurnPower extends AbstractPower {
    public static final String POWER_ID = IceBreaker.makeID("ExtraTurnPower");
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESC;
    private static final List<EnterOrExitExtraTurnSubscriber> subscribers = new ArrayList<>();

    public ExtraTurnPower(AbstractCreature owner) {
        this.owner = owner;
        this.ID = POWER_ID;
        this.name = NAME;
        this.updateDescription();
        this.loadRegion("time");
    }

    @Override
    public void updateDescription() {
        this.description = DESC[0];
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        onInitialApplication();
    }

    @Override
    public void onInitialApplication() {
        for (EnterOrExitExtraTurnSubscriber subscriber : subscribers) {
            subscriber.onEnterExtraTurn();
        }
        for (AbstractCard card : AbstractDungeon.player.drawPile.group) {
            if (card instanceof EnterOrExitExtraTurnSubscriber) {
                EnterOrExitExtraTurnSubscriber subscriber = (EnterOrExitExtraTurnSubscriber) card;
                subscriber.onEnterExtraTurn();
            }
        }
        for (AbstractCard card : AbstractDungeon.player.hand.group) {
            if (card instanceof EnterOrExitExtraTurnSubscriber) {
                EnterOrExitExtraTurnSubscriber subscriber = (EnterOrExitExtraTurnSubscriber) card;
                subscriber.onEnterExtraTurn();
            }
        }
        for (AbstractCard card : AbstractDungeon.player.discardPile.group) {
            if (card instanceof EnterOrExitExtraTurnSubscriber) {
                EnterOrExitExtraTurnSubscriber subscriber = (EnterOrExitExtraTurnSubscriber) card;
                subscriber.onEnterExtraTurn();
            }
        }
        for (AbstractCard card : AbstractDungeon.player.exhaustPile.group) {
            if (card instanceof EnterOrExitExtraTurnSubscriber) {
                EnterOrExitExtraTurnSubscriber subscriber = (EnterOrExitExtraTurnSubscriber) card;
                subscriber.onEnterExtraTurn();
            }
        }
        for (AbstractPower power : AbstractDungeon.player.powers) {
            if (power instanceof EnterOrExitExtraTurnSubscriber) {
                EnterOrExitExtraTurnSubscriber subscriber = (EnterOrExitExtraTurnSubscriber) power;
                subscriber.onEnterExtraTurn();
            }
        }
        addToTop(new GainEnergyAction(2));
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) {
            addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        }
    }

    @Override
    public void onRemove() {
        this.flashWithoutSound();
        CardCrawlGame.sound.play("POWER_TIME_WARP", 0.05F);
        for (EnterOrExitExtraTurnSubscriber subscriber : subscribers) {
            subscriber.onExitExtraTurn();
        }
        for (AbstractCard card : AbstractDungeon.player.drawPile.group) {
            if (card instanceof EnterOrExitExtraTurnSubscriber) {
                EnterOrExitExtraTurnSubscriber subscriber = (EnterOrExitExtraTurnSubscriber) card;
                subscriber.onExitExtraTurn();
            }
        }
        for (AbstractCard card : AbstractDungeon.player.hand.group) {
            if (card instanceof EnterOrExitExtraTurnSubscriber) {
                EnterOrExitExtraTurnSubscriber subscriber = (EnterOrExitExtraTurnSubscriber) card;
                subscriber.onExitExtraTurn();
            }
        }
        for (AbstractCard card : AbstractDungeon.player.discardPile.group) {
            if (card instanceof EnterOrExitExtraTurnSubscriber) {
                EnterOrExitExtraTurnSubscriber subscriber = (EnterOrExitExtraTurnSubscriber) card;
                subscriber.onExitExtraTurn();
            }
        }
        for (AbstractCard card : AbstractDungeon.player.exhaustPile.group) {
            if (card instanceof EnterOrExitExtraTurnSubscriber) {
                EnterOrExitExtraTurnSubscriber subscriber = (EnterOrExitExtraTurnSubscriber) card;
                subscriber.onExitExtraTurn();
            }
        }
        for (AbstractPower power : AbstractDungeon.player.powers) {
            if (power instanceof EnterOrExitExtraTurnSubscriber) {
                EnterOrExitExtraTurnSubscriber subscriber = (EnterOrExitExtraTurnSubscriber) power;
                subscriber.onExitExtraTurn();
            }
        }
    }

    public static void subscribe(EnterOrExitExtraTurnSubscriber subscriber) {
        subscribers.add(subscriber);
    }

    public static void unsubscribe(EnterOrExitExtraTurnSubscriber subscriber) {
        subscribers.remove(subscriber);
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESC = powerStrings.DESCRIPTIONS;
    }
}
