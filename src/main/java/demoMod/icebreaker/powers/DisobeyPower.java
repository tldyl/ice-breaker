package demoMod.icebreaker.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.BufferPower;
import demoMod.icebreaker.IceBreaker;

public class DisobeyPower extends AbstractPower {
    public static final String POWER_ID = IceBreaker.makeID("DisobeyPower");
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESC;

    public DisobeyPower(AbstractCreature owner, int amount) {
        this.owner = owner;
        this.ID = POWER_ID;
        this.name = NAME;
        this.amount = amount;
        this.updateDescription();
        this.loadRegion("draw");
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESC[0], this.amount);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (owner instanceof AbstractPlayer == isPlayer && !owner.hasPower(BufferPower.POWER_ID)) {
            this.flash();
            addToBot(new ApplyPowerAction(owner, owner, new BufferPower(owner, this.amount)));
        }
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESC = powerStrings.DESCRIPTIONS;
    }
}
