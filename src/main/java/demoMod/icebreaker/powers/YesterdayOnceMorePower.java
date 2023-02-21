package demoMod.icebreaker.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import demoMod.icebreaker.IceBreaker;
import demoMod.icebreaker.interfaces.TriggerFetterSubscriber;

import java.util.List;

public class YesterdayOnceMorePower extends AbstractPower implements TriggerFetterSubscriber {
    public static final String POWER_ID = IceBreaker.makeID("YesterdayOnceMorePower");
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESC;

    public YesterdayOnceMorePower(AbstractCreature owner, int amount) {
        this.owner = owner;
        this.amount = amount;
        this.ID = POWER_ID;
        this.name = NAME;
        this.updateDescription();
        this.loadRegion("time");
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESC[0], this.amount);
    }

    @Override
    public void onTriggerFetter() {
        this.flash();
        addToBot(new ApplyPowerAction(this.owner, this.owner, new TimeStasisPower(this.owner, this.amount)));
    }

    @Override
    public void onOtherCardTriggerFetter(AbstractCard playedCard, List<AbstractCard> fetterCards) {

    }

    @Override
    public void onTriggerFetterFailed() {

    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESC = powerStrings.DESCRIPTIONS;
    }
}
