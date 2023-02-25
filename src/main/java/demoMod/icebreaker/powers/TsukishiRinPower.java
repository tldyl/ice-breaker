package demoMod.icebreaker.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import demoMod.icebreaker.IceBreaker;
import demoMod.icebreaker.utils.PowerRegionLoader;

public class TsukishiRinPower extends AbstractPower {
    public static final String POWER_ID = IceBreaker.makeID("TsukishiRinPower");
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESC;

    public TsukishiRinPower(AbstractCreature owner, int amount) {
        this.owner = owner;
        this.ID = POWER_ID;
        this.name = NAME;
        this.amount = amount;
        this.updateDescription();
        PowerRegionLoader.load(this, "DeepCalculation");
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESC[0], this.amount);
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.type == AbstractCard.CardType.ATTACK && card.target == AbstractCard.CardTarget.ENEMY && action.target instanceof AbstractMonster) {
            this.flash();
            addToBot(new ApplyPowerAction(action.target, owner, new StrengthPower(action.target, -this.amount)));
        }
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESC = powerStrings.DESCRIPTIONS;
    }
}
