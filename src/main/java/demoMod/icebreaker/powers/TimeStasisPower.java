package demoMod.icebreaker.powers;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.TimeWarpTurnEndEffect;
import demoMod.icebreaker.IceBreaker;
import demoMod.icebreaker.utils.PowerRegionLoader;

public class TimeStasisPower extends AbstractPower {
    public static final String POWER_ID = IceBreaker.makeID("TimeStasisPower");
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESC;

    public TimeStasisPower(AbstractCreature owner, int amount) {
        this.owner = owner;
        this.amount = amount;
        this.ID = POWER_ID;
        this.name = NAME;
        this.updateDescription();
        this.loadRegion("time");
    }

    @Override
    public void updateDescription() {
        this.description = DESC[0];
    }

    @Override
    public void onInitialApplication() {
        if (this.amount >= 12) {
            this.flashWithoutSound();
            CardCrawlGame.sound.play("POWER_TIME_WARP", 0.05F);
            AbstractDungeon.effectsQueue.add(new BorderFlashEffect(Color.GOLD, true));
            AbstractDungeon.topLevelEffectsQueue.add(new TimeWarpTurnEndEffect());
            this.addToTop(new ApplyPowerAction(this.owner, this.owner, new ExtraTurnPower(this.owner)));
            this.amount %= 12;
            if (this.amount <= 0) {
                this.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
            }
        }
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        if (this.amount >= 12) {
            this.flashWithoutSound();
            CardCrawlGame.sound.play("POWER_TIME_WARP", 0.05F);
            AbstractDungeon.effectsQueue.add(new BorderFlashEffect(Color.GOLD, true));
            AbstractDungeon.topLevelEffectsQueue.add(new TimeWarpTurnEndEffect());
            this.addToTop(new ApplyPowerAction(this.owner, this.owner, new ExtraTurnPower(this.owner)));
            this.amount %= 12;
            if (this.amount <= 0) {
                this.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
            }
        }
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESC = powerStrings.DESCRIPTIONS;
    }
}
