package demoMod.icebreaker.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class FallingSnowEffect extends AbstractGameEffect {
    private float waitTimer;
    private float x;
    private float y;
    private float vX;
    private float vY;
    private final Texture img;

    public FallingSnowEffect(boolean flipped) {
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

        this.waitTimer = MathUtils.random(0.0F, 1.5F);
        if (flipped) {
            this.x = MathUtils.random(-200.0F, Settings.WIDTH);
            this.vX = MathUtils.random(-600.0F, -300.0F);
            this.vX += 125.0F;
        } else {
            this.x = MathUtils.random(-200.0F, Settings.WIDTH);
            this.vX = MathUtils.random(300.0F, 600.0F);
            this.vX -= 125.0F;
        }

        this.y = (Settings.HEIGHT + MathUtils.random(100.0F, 300.0F) - 48.0F);
        this.vY = MathUtils.random(500.0F, 2000.0F);
        this.vY -= 50.0F;
        this.vY *= Settings.scale;
        this.vX *= Settings.scale;
        this.scale = MathUtils.random(0.2F, 0.4F);
        this.vX *= this.scale;
        this.vY *= (1.2F - this.scale) * 1.25F;
        this.color = new Color(0.9F, 0.9F, 1.0F, MathUtils.random(0.9F, 1.0F));
        Vector2 drp = new Vector2(this.vX, this.vY);
        if (flipped) {
            this.rotation = (drp.angle() + 225.0F - 25.0F / 3.0F);
        } else {
            this.rotation = (drp.angle() - 45.0F + 25.0F / 3.0F);
        }
        this.renderBehind = MathUtils.randomBoolean();
    }

    @Override
    public void update() {
        this.waitTimer -= Gdx.graphics.getDeltaTime();
        if (this.waitTimer > 0.0F) {
            return;
        }
        this.x += this.vX * Gdx.graphics.getDeltaTime();
        this.y -= this.vY * Gdx.graphics.getDeltaTime();
        if (this.y < 0) {
            this.isDone = true;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        if (this.waitTimer < 0.0F) {
            sb.setBlendFunction(770, 1);
            sb.setColor(this.color);
            sb.draw(this.img, this.x, this.y, 48.0F, 48.0F, 96.0F, 96.0F, this.scale, this.scale, this.rotation, 0, 0, 96, 96, false, false);
            sb.setBlendFunction(770, 771);
        }
    }

    @Override
    public void dispose() {

    }
}
