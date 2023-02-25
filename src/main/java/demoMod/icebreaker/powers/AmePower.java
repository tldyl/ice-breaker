package demoMod.icebreaker.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.MetallicizePower;
import demoMod.icebreaker.IceBreaker;
import demoMod.icebreaker.interfaces.EnterOrExitExtraTurnSubscriber;

public class AmePower extends AbstractPower implements EnterOrExitExtraTurnSubscriber {
    public static final String POWER_ID = IceBreaker.makeID("AmePower");
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESC;

    public AmePower(AbstractCreature owner, int amount) {
        this.owner = owner;
        this.ID = POWER_ID;
        this.name = NAME;
        this.amount = amount;
        this.updateDescription();
        this.loadRegion("storm");
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESC[0], this.amount);
    }

    @Override
    public void onEnterExtraTurn() {
        this.flash();
        addToBot(new ApplyPowerAction(owner, owner, new MetallicizePower(owner, this.amount)));
    }

    @Override
    public void onExitExtraTurn() {

    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESC = powerStrings.DESCRIPTIONS;
    }
}
