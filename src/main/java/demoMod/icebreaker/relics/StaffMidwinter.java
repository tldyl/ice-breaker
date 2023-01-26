package demoMod.icebreaker.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import demoMod.icebreaker.IceBreaker;
import demoMod.icebreaker.powers.DoubleBlockPower;
import demoMod.icebreaker.powers.TimeStasisPower;

public class StaffMidwinter extends CustomRelic {
    public static final String ID = IceBreaker.makeID("StaffMidwinter");
    private static final Texture IMG = new Texture(IceBreaker.getResourcePath("relics/StaffMidwinter.png"));
    private static final Texture IMG_OUTLINE = new Texture(IceBreaker.getResourcePath("relics/StaffMidwinter_outline.png"));

    public StaffMidwinter() {
        super(ID, IMG, IMG_OUTLINE, RelicTier.BOSS, LandingSound.MAGICAL);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void obtain() {
        this.instantObtain(AbstractDungeon.player, 0, true);
        this.flash();
    }

    @Override
    public boolean canSpawn() {
        return AbstractDungeon.player.hasRelic(StaffBlizzard.ID);
    }

    public int turns;
    @Override
    public void atBattleStart() { turns = 0; }

    @Override
    public void atTurnStart() {
        this.flash();
        AbstractPlayer p = AbstractDungeon.player;
        this.addToBot(new RelicAboveCreatureAction(p, this));
        this.addToBot(new ApplyPowerAction(p, p, new DoubleBlockPower(p, 1)));
        this.addToBot(new ApplyPowerAction(p, p, new TimeStasisPower(p, ++this.turns)));
    }
}
