package demoMod.icebreaker.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import demoMod.icebreaker.IceBreaker;

public class DefeatTimeEaterEffect extends AbstractGameEffect {
    private static final Texture img = new Texture(IceBreaker.getResourcePath("effects/time_eater.png"));

    private static final FrameBuffer fb = new FrameBuffer(Pixmap.Format.RGBA8888, Settings.WIDTH, Settings.HEIGHT, false);

    private float imgX;
    private float imgY;

    private float upPartX;
    private final float upPartY;
    private final float offsetY;

    public DefeatTimeEaterEffect(float offsetY, Color tint) {
        this.imgX = (Settings.WIDTH - img.getWidth()) / 2.0F * 1.1F;
        this.imgY = (Settings.HEIGHT - img.getHeight()) / 2.0F * 1.1F;
        this.duration = 3.0F;
        this.startingDuration = this.duration;
        this.upPartY = Settings.HEIGHT / 2.0F;
        this.offsetY = offsetY;
        this.color = tint.cpy();
    }

    @Override
    public void update() {
        this.imgX -= 30.0F * Gdx.graphics.getDeltaTime();
        this.imgY -= 22.0F * Gdx.graphics.getDeltaTime();
        if (this.duration >= this.startingDuration / 2.0F) {
            this.upPartX = Interpolation.exp10Out.apply(0, Settings.WIDTH / 2.0F, (this.startingDuration - this.duration) / this.startingDuration * 2.0F);
        } else {
            this.upPartX = Interpolation.exp10In.apply(Settings.WIDTH / 2.0F, Settings.WIDTH, ((this.startingDuration - this.duration) / this.startingDuration - 0.5F) * 2.0F);
        }

        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0) {
            this.isDone = true;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.end();
        fb.begin();
        sb.begin();
        Gdx.gl.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        sb.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        sb.draw(img, imgX, imgY, img.getWidth() / 2.0F, img.getHeight() / 2.0F, img.getWidth(), img.getHeight(), Settings.scale, Settings.scale, 0.0F, 0, 0, 1300, 885, false, true);
        sb.end();
        fb.end();
        sb.begin();

        sb.setColor(this.color);
        sb.draw(fb.getColorBufferTexture(), this.upPartX - Settings.WIDTH / 2.2F, this.upPartY + this.offsetY,
                fb.getWidth(), 300.0F * Settings.scale,
                (int) ((Settings.WIDTH - img.getWidth()) / 2.0F), (int) (this.upPartY - 300.0F * Settings.scale),
                fb.getWidth(), (int) (300.0F * Settings.scale),
                false, false);
        sb.draw(fb.getColorBufferTexture(), Settings.WIDTH - this.upPartX - Settings.WIDTH / 2.2F, this.upPartY - 300.0F * Settings.scale - this.offsetY,
                fb.getWidth(), 300.0F * Settings.scale,
                (int) ((Settings.WIDTH - img.getWidth()) / 2.0F), (int) this.upPartY,
                fb.getWidth(), (int) (300.0F * Settings.scale),
                false, false);
    }

    @Override
    public void dispose() {

    }
}
