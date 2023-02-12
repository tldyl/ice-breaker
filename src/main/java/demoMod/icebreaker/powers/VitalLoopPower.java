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
    private final boolean upgraded;

    public VitalLoopPower(AbstractCreature owner, int amount, boolean upgraded) {
        this.owner = owner;
        this.amount = amount;
        this.ID = POWER_ID;
        this.name = NAME;
        this.upgraded = upgraded;
        if (this.upgraded) {
            this.ID += "+";
            this.name += "+";
        }
        this.updateDescription();
        PowerRegionLoader.load(this, "LifeCycle");
    }

    @Override
    public void updateDescription() {
        this.description = String.format(this.upgraded ? DESC[1] : DESC[0], this.amount);
    }

    @Override
    public void atStartOfTurn() {
        triggerPower();
    }

    @Override
    public void onEnterExtraTurn() {
        if (this.upgraded) {
            triggerPower();
        }
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
