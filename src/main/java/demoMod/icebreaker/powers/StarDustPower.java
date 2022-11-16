package demoMod.icebreaker.powers;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import demoMod.icebreaker.IceBreaker;
import demoMod.icebreaker.interfaces.TriggerFetterSubscriber;

public class StarDustPower extends AbstractPower implements TriggerFetterSubscriber {
    public static final String POWER_ID = IceBreaker.makeID("StarDustPower");
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESC;
    private int initialAmount;

    public StarDustPower(AbstractCreature owner, int amount) {
        this.owner = owner;
        this.amount = amount;
        this.ID = POWER_ID;
        this.name = NAME;
        this.updateDescription();
        this.initialAmount = amount;
        this.loadRegion("time");
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESC[0], this.amount);
    }

    @Override
    public void stackPower(int stackAmount) {
        this.initialAmount += stackAmount;
        this.amount += stackAmount;
    }

    @Override
    public void atStartOfTurn() {
        this.amount = this.initialAmount;
    }

    @Override
    public void onTriggerFetter() {
        if (this.amount > 0) {
            this.amount--;
            this.flash();
            addToBot(new GainEnergyAction(1));
        }
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
