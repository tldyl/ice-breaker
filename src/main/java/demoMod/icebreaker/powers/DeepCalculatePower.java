package demoMod.icebreaker.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import demoMod.icebreaker.IceBreaker;
import demoMod.icebreaker.utils.PowerRegionLoader;

public class DeepCalculatePower extends AbstractPower {
    public static final String POWER_ID = IceBreaker.makeID("DeepCalculatePower");
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESC;

    public DeepCalculatePower(AbstractCreature owner) {
        this.owner = owner;
        this.ID = POWER_ID;
        this.name = NAME;
        this.updateDescription();
        PowerRegionLoader.load(this, "DeepCalculation");
    }

    @Override
    public void updateDescription() {
        this.description = DESC[0];
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESC = powerStrings.DESCRIPTIONS;
    }
}
