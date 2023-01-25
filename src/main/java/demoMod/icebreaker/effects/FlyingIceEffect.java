package demoMod.icebreaker.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class FlyingIceEffect extends AbstractGameEffect {
    private float x;
    private float y;
    private boolean playedSound = false;
    private final float scaleMultiplier;
    private final Texture img;

    public FlyingIceEffect(float x, float y, float fAngle, boolean shouldFlip) {
        switch (MathUtils.random(2)) {
            case 0:
                this.img = ImageMaster.FROST_ORB_RIGHT;
                break;
            case 1:
                this.img = ImageMaster.FROST_ORB_LEFT;
                break;
            default:
                this.img = ImageMaster.FROST_ORB_MIDDLE;
        }

        this.startingDuration = 0.5F;
        this.duration = 0.5F;
        this.scaleMultiplier = MathUtils.random(1.2F, 1.5F);
        this.scale = Settings.scale * 0.75F;
        if (shouldFlip) {
            this.rotation = fAngle - 180.0F;
        } else {
            this.rotation = fAngle;
        }
        this.x = x - (float)this.img.getWidth() / 2.0F;
        this.y = y - (float)this.img.getHeight() / 2.0F;
        this.color = Color.WHITE.cpy();
        this.color.a = 0.0F;
    }

    @Override
    public void update() {
        if (!this.playedSound) {
            //TODO play sfx
            this.playedSound = true;
        }

        this.duration -= Gdx.graphics.getDeltaTime();
        Vector2 derp = new Vector2(MathUtils.cos(0.017453292F * this.rotation), MathUtils.sin(0.017453292F * this.rotation));
        this.x += derp.x * Gdx.graphics.getDeltaTime() * 6000.0F * this.scaleMultiplier * Settings.scale;
        this.y += derp.y * Gdx.graphics.getDeltaTime() * 6000.0F * this.scaleMultiplier * Settings.scale;
        if (this.duration < 0.0F) {
            this.isDone = true;
        }

        if (this.duration > 0.25F) {
            this.color.a = Interpolation.pow5In.apply(1.0F, 0.0F, (this.duration - 0.25F) * 4.0F);
        } else {
            this.color.a = Interpolation.fade.apply(0.0F, 1.0F, this.duration * 4.0F);
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(this.color);
        sb.draw(this.img, this.x, this.y, (float)this.img.getWidth() / 2.0F, (float)this.img.getHeight() / 2.0F, (float)this.img.getWidth(), (float)this.img.getHeight(), this.scale, this.scale * 1.5F, this.rotation, 0, 0, img.getWidth(), img.getHeight(), false, false);
        sb.setBlendFunction(770, 1);
        sb.draw(this.img, this.x, this.y, (float)this.img.getWidth() / 2.0F, (float)this.img.getHeight() / 2.0F, (float)this.img.getWidth(), (float)this.img.getHeight(), this.scale * 0.75F, this.scale * 0.75F, this.rotation, 0, 0, img.getWidth(), img.getHeight(), false, false);
        sb.setBlendFunction(770, 771);
    }

    @Override
    public void dispose() {

    }
}
