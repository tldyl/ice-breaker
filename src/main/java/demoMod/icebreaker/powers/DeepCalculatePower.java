package demoMod.icebreaker.powers;

import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import demoMod.icebreaker.IceBreaker;
import demoMod.icebreaker.cards.lightlemon.AbstractLightLemonCard;
import demoMod.icebreaker.utils.PowerRegionLoader;

public class DeepCalculatePower extends AbstractPower {
    public static final String POWER_ID = IceBreaker.makeID("DeepCalculatePower");
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESC;

    public DeepCalculatePower(AbstractCreature owner, int amount) {
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
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card instanceof AbstractLightLemonCard) {
            this.flash();
            for (int i=0;i<this.amount;i++) {
                ((AbstractLightLemonCard) card).onTriggerFetter();
            }
        }
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESC = powerStrings.DESCRIPTIONS;
    }
}
