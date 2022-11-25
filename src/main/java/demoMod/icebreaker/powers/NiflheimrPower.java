package demoMod.icebreaker.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import demoMod.icebreaker.IceBreaker;
import demoMod.icebreaker.interfaces.EnterOrExitExtraTurnSubscriber;
import demoMod.icebreaker.interfaces.TriggerFetterSubscriber;

import java.util.stream.Collectors;

public class NiflheimrPower extends AbstractPower implements EnterOrExitExtraTurnSubscriber {
    public static final String POWER_ID = IceBreaker.makeID("NiflheimrPower");
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESC;
    private boolean upgraded;

    public NiflheimrPower(AbstractCreature owner, int amount, boolean upgraded) {
        this.owner = owner;
        this.amount = amount;
        this.ID = POWER_ID;
        this.name = NAME;
        this.upgraded = upgraded;
        this.updateDescription();
        this.loadRegion("time");
        if (this.upgraded) {
            this.name += "+";
        }
    }

    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        if (power instanceof NiflheimrPower && target == this.owner && !this.upgraded) {
            this.upgraded = ((NiflheimrPower) power).upgraded;
            if (this.upgraded) {
                this.name += "+";
            }
        }
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
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters.stream().filter(monster -> !monster.isDeadOrEscaped()).collect(Collectors.toList())) {
            addToBot(new ApplyPowerAction(m, owner, new StrengthPower(m, -this.amount)));
        }
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESC = powerStrings.DESCRIPTIONS;
    }
}
