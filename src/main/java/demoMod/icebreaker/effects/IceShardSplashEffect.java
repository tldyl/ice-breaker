package demoMod.icebreaker.effects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class IceShardSplashEffect extends AbstractGameEffect {
    private final boolean flipX;

    public IceShardSplashEffect(boolean shouldFlip) {
        this.flipX = shouldFlip;
    }

    @Override
    public void update() {
        float x;
        CardCrawlGame.sound.play("SFX_ICE_SPLASH", 0.1F);
        if (this.flipX) {
            for(int i = 12; i > 0; i--) {
                x = AbstractDungeon.player.hb.cX - MathUtils.random(0.0F, 450.0F) * Settings.scale;
                AbstractDungeon.effectsQueue.add(new FlyingIceEffect(x, AbstractDungeon.player.hb.cY + (float)i * -18.0F * Settings.scale, (float)(i * 4) - 30.0F, true));
            }
        } else {
            for(int i = 0; i < 12; i++) {
                x = AbstractDungeon.player.hb.cX + MathUtils.random(0.0F, 450.0F) * Settings.scale;
                AbstractDungeon.effectsQueue.add(new FlyingIceEffect(x, AbstractDungeon.player.hb.cY + 120.0F * Settings.scale + (float)i * 18.0F * Settings.scale, (float)(i * 4) - 20.0F, false));
            }
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
