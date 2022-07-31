package demoMod.icebreaker.powers;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import demoMod.icebreaker.IceBreaker;
import demoMod.icebreaker.cards.lightlemon.AbstractLightLemonCard;
import demoMod.icebreaker.enums.CardTagEnum;
import demoMod.icebreaker.interfaces.ModifyMagicNumberSubscriber;
import demoMod.icebreaker.utils.PowerRegionLoader;

public class ResonancePower extends AbstractPower implements ModifyMagicNumberSubscriber {
    public static final String POWER_ID = IceBreaker.makeID("ResonancePower");
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESC;

    public ResonancePower(AbstractCreature owner, int amount) {
        this.owner = owner;
        this.amount = amount;
        this.ID = POWER_ID;
        this.name = NAME;
        this.updateDescription();
        PowerRegionLoader.load(this);
    }

    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        if (this.amount == 0) {
            this.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
        }

        if (this.amount >= 999) {
            this.amount = 999;
        }

        if (this.amount <= -999) {
            this.amount = -999;
        }
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESC[0], this.amount);
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type, AbstractCard card) {
        return type == DamageInfo.DamageType.NORMAL && card.hasTag(CardTagEnum.MAGIC) ? damage + this.amount : damage;
    }

    @Override
    public float modifyBlock(float blockAmount, AbstractCard card) {
        return card.hasTag(CardTagEnum.MAGIC) && owner.hasPower(ExtraTurnPower.POWER_ID) ? blockAmount + this.amount : blockAmount;
    }

    @Override
    public int onModifyMagicNumber(int magicNumber, AbstractCard card) {
        return card.hasTag(CardTagEnum.MAGIC) && owner.hasPower(ExtraTurnPower.POWER_ID) ? this.amount + magicNumber : magicNumber;
    }

    @Override
    public int onModifyAnotherMagicNumber(int magicNumber, AbstractCard card) {
        if (card instanceof AbstractLightLemonCard) {
            return card.hasTag(CardTagEnum.MAGIC) && owner.hasPower(ExtraTurnPower.POWER_ID) ? this.amount + magicNumber : magicNumber;
        }
        return magicNumber;
    }

    @Override
    public void onAfterCardPlayed(AbstractCard usedCard) {
        if (usedCard.hasTag(CardTagEnum.MAGIC) && owner.hasPower(ExtraTurnPower.POWER_ID)) {
            addToBot(new ReducePowerAction(owner, owner, this, 1));
        }
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESC = powerStrings.DESCRIPTIONS;
    }
}
