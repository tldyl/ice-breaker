package demoMod.icebreaker.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import demoMod.icebreaker.IceBreaker;
import demoMod.icebreaker.cards.lightlemon.AbstractLightLemonCard;
import demoMod.icebreaker.interfaces.TriggerFetterSubscriber;
import demoMod.icebreaker.utils.PowerRegionLoader;

import java.util.List;

public class YesterdayOnceMorePower extends AbstractPower implements TriggerFetterSubscriber {
    public static final String POWER_ID = IceBreaker.makeID("YesterdayOnceMorePower");
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESC;
    private final boolean upgraded;

    public YesterdayOnceMorePower(AbstractCreature owner, int amount, boolean upgraded) {
        this.owner = owner;
        this.amount = amount;
        this.upgraded = upgraded;
        this.ID = POWER_ID;
        this.name = NAME;
        if (upgraded) {
            this.ID += "+";
            this.name += "+";
        }
        this.updateDescription();
        PowerRegionLoader.load(this);
    }

    @Override
    public void updateDescription() {
        this.description = String.format(upgraded ? DESC[1] : DESC[0], this.amount);
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card instanceof AbstractLightLemonCard && this.upgraded) {
            if (((AbstractLightLemonCard) card).isFetter) {
                this.flash();
                addToBot(new ApplyPowerAction(owner, owner, new TimeStasisPower(owner, this.amount)));
            }
        }
    }

    @Override
    public void onTriggerFetter() {
        if (!upgraded) {
            this.flash();
            addToBot(new ApplyPowerAction(this.owner, this.owner, new TimeStasisPower(this.owner, this.amount)));
        }
    }

    @Override
    public void onOtherCardTriggerFetter(AbstractCard playedCard, List<AbstractCard> fetterCards) {

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
