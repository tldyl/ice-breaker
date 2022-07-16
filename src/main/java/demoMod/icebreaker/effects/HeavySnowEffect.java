package demoMod.icebreaker.effects;

import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;

public class HeavySnowEffect extends AbstractGameEffect {
    private final boolean flipped;

    public HeavySnowEffect(boolean flipped) {
        this.flipped = flipped;
    }

    @Override
    public void update() {
        CardCrawlGame.sound.playA("GHOST_ORB_IGNITE_1", -0.6F);
        AbstractGameEffect effect = new BorderLongFlashEffect(Color.SKY) {
            @Override
            public void update() {
                float startAlpha = ReflectionHacks.getPrivate(this, BorderLongFlashEffect.class, "startAlpha");
                if (this.startingDuration - this.duration < 0.2F) {
                    this.color.a = Interpolation.fade.apply(0.0F, startAlpha, (this.startingDuration - this.duration) * 10.0F);
                } else {
                    this.color.a = Interpolation.pow2Out.apply(0.0F, startAlpha, this.duration / this.startingDuration);
                }
                this.duration -= Gdx.graphics.getDeltaTime();
                if (this.duration < 0.0F) {
                    this.isDone = true;
                }
            }
        };
        effect.duration = 4.0F;
        effect.startingDuration = effect.duration;
        AbstractDungeon.effectsQueue.add(effect);
        for (int i = 0; i < 450; i++) {
            AbstractDungeon.effectsQueue.add(new FallingSnowEffect(this.flipped));
        }
        this.isDone = true;
    }

    @Override
    public void render(SpriteBatch sb) {

    }

    @Override
    public void dispose() {

    }
}
