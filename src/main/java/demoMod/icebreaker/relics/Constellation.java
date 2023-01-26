package demoMod.icebreaker.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.TimeWarpTurnEndEffect;
import demoMod.icebreaker.IceBreaker;
import demoMod.icebreaker.powers.ExtraTurnPower;

public class Constellation extends CustomRelic {
    public static final String ID = IceBreaker.makeID("Constellation");
    private static final Texture IMG = new Texture(IceBreaker.getResourcePath("relics/Constellation.png"));
    private static final Texture IMG_OUTLINE = new Texture(IceBreaker.getResourcePath("relics/Constellation_outline.png"));

    public Constellation() {
        super(ID, IMG, IMG_OUTLINE, RelicTier.RARE, LandingSound.MAGICAL);
        this.counter = 0;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void atTurnStart() {
        if (++this.counter == 7) {
            AbstractPlayer p = AbstractDungeon.player;
            this.flash(); this.counter = 0;
            this.addToBot(new RelicAboveCreatureAction(p, this));
            CardCrawlGame.sound.play("POWER_TIME_WARP", 0.05F);
            AbstractDungeon.effectsQueue.add(new BorderFlashEffect(Color.GOLD, true));
            AbstractDungeon.topLevelEffectsQueue.add(new TimeWarpTurnEndEffect());
            addToBot(new ApplyPowerAction(p, p, new ExtraTurnPower(p)));
        }
    }
}
