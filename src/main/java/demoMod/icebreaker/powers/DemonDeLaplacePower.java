package demoMod.icebreaker.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import demoMod.icebreaker.IceBreaker;
import demoMod.icebreaker.utils.PowerRegionLoader;

public class DemonDeLaplacePower extends AbstractPower {
    public static final String POWER_ID = IceBreaker.makeID("DemonDeLaplacePower");
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESC;

    public DemonDeLaplacePower(AbstractCreature owner, int amount) {
        this.owner = owner;
        this.amount = amount;
        this.ID = POWER_ID;
        this.name = NAME;
        this.updateDescription();
        PowerRegionLoader.load(this);
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESC[0], this.amount);
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        boolean flag = false;
        for (AbstractCard card1 : AbstractDungeon.player.masterDeck.group) {
            if (card.uuid.equals(card1.uuid)) {
                flag = true;
                break;
            }
        }
        if (!flag) {
            this.flash();
            addToBot(new ApplyPowerAction(this.owner, this.owner, new ResonancePower(this.owner, this.amount)));
        }
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESC = powerStrings.DESCRIPTIONS;
    }
}
