package demoMod.icebreaker.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import demoMod.icebreaker.IceBreaker;
import demoMod.icebreaker.interfaces.EnterOrExitExtraTurnSubscriber;

public class Letter extends CustomRelic implements EnterOrExitExtraTurnSubscriber {
    public static final String ID = IceBreaker.makeID("Letter");
    private static final Texture IMG = new Texture(IceBreaker.getResourcePath("relics/Letter.png"));
    private static final Texture IMG_OUTLINE = new Texture(IceBreaker.getResourcePath("relics/Letter_outline.png"));

    public Letter() {
        super(ID, IMG, IMG_OUTLINE, RelicTier.BOSS, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void onEnterExtraTurn() {
        this.flash();
        addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        addToBot(new DrawCardAction(1));
        addToBot(new GainEnergyAction(1));
    }

    @Override
    public void onExitExtraTurn() {

    }
}
