package demoMod.icebreaker.effects;

import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.GiantFireEffect;

public class ScreenOnFireEffect extends AbstractGameEffect {
    private float timer = 0.0F;
    private static final float INTERVAL = 0.05F;

    public ScreenOnFireEffect() {
        this.duration = 3.0F;
        this.startingDuration = this.duration;
    }

    public void update() {
        if (this.duration == this.startingDuration) {
            CardCrawlGame.sound.play("GHOST_FLAMES");
            CardCrawlGame.sound.play("SOUL_TREMOR");
            AbstractDungeon.effectsQueue.add(new BorderLongFlashEffect(Color.CHARTREUSE));
        }

        this.duration -= Gdx.graphics.getDeltaTime();
        this.timer -= Gdx.graphics.getDeltaTime();
        if (this.timer < 0.0F) {
            AbstractDungeon.effectsQueue.add(getGreenFireEffect());
            AbstractDungeon.effectsQueue.add(getGreenFireEffect());
            AbstractDungeon.effectsQueue.add(getGreenFireEffect());
            AbstractDungeon.effectsQueue.add(getGreenFireEffect());
            AbstractDungeon.effectsQueue.add(getGreenFireEffect());
            AbstractDungeon.effectsQueue.add(getGreenFireEffect());
            AbstractDungeon.effectsQueue.add(getGreenFireEffect());
            AbstractDungeon.effectsQueue.add(getGreenFireEffect());
            this.timer = 0.05F;
        }

        if (this.duration < 0.0F) {
            this.isDone = true;
        }
    }

    private AbstractGameEffect getGreenFireEffect() {
        AbstractGameEffect effect = new GiantFireEffect();
        Color fireColor = Color.WHITE.cpy();
        fireColor.a = 0.0F;
        fireColor.r -= MathUtils.random(0.5F);
        fireColor.b -= fireColor.r - MathUtils.random(0.0F, 0.2F);
        ReflectionHacks.setPrivate(effect, AbstractGameEffect.class, "color", fireColor);
        return effect;
    }

    public void render(SpriteBatch sb) {
    }

    public void dispose() {
    }
}