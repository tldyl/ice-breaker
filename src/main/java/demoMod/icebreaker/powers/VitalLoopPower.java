package demoMod.icebreaker.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import demoMod.icebreaker.IceBreaker;
import demoMod.icebreaker.actions.OptionalDiscardPileToTopOfDeckAction;
import demoMod.icebreaker.interfaces.EnterOrExitExtraTurnSubscriber;
import demoMod.icebreaker.utils.PowerRegionLoader;

public class VitalLoopPower extends AbstractPower implements EnterOrExitExtraTurnSubscriber {
    public static final String POWER_ID = IceBreaker.makeID("VitalLoopPower");
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESC;

    public VitalLoopPower(AbstractCreature owner, int amount) {
        this.owner = owner;
        this.amount = amount;
        this.ID = POWER_ID;
        this.name = NAME;
        this.updateDescription();
        PowerRegionLoader.load(this, "FrostCountry");
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESC[0], this.amount);
    }

    @Override
    public void atStartOfTurn() {
        triggerPower();
    }

    @Override
    public void onEnterExtraTurn() {
        triggerPower();
    }

    @Override
    public void onExitExtraTurn() {

    }

    private void triggerPower() {
        this.flash();
        addToBot(new OptionalDiscardPileToTopOfDeckAction(this.amount));
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESC = powerStrings.DESCRIPTIONS;
    }
}
