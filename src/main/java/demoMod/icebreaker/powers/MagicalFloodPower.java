package demoMod.icebreaker.powers;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import demoMod.icebreaker.IceBreaker;
import demoMod.icebreaker.interfaces.EnterOrExitExtraTurnSubscriber;

public class MagicalFloodPower extends AbstractPower implements EnterOrExitExtraTurnSubscriber {
    public static final String POWER_ID = IceBreaker.makeID("MagicalFloodPower");
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESC;

    public MagicalFloodPower(AbstractCreature owner, int amount) {
        this.owner = owner;
        this.amount = amount;
        this.ID = POWER_ID;
        this.name = NAME;
        this.updateDescription();
        this.loadRegion("rushdown");
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESC[0], this.amount);
    }

    @Override
    public void onEnterExtraTurn() {
        this.flash();
        addToBot(new DrawCardAction(this.amount));
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
