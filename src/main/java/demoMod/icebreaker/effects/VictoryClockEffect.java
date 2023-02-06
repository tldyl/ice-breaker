package demoMod.icebreaker.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.scene.SilentVictoryStarEffect;
import demoMod.icebreaker.IceBreaker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class VictoryClockEffect extends AbstractGameEffect {
    private static Texture mark5;
    private static Texture mark;
    private static Texture center;
    private static Texture secondHand;
    private static Texture minuteHand;
    private static Texture hourHand;
    private static Texture numbers;
    private static Texture circle;
    private static Texture hexagon;
    private static Texture hexagram;

    private static Texture mark5Effect;
    private static Texture markEffect;
    private static Texture centerEffect;
    private static Texture secondHandEffect;
    private static Texture minuteHandEffect;
    private static Texture hourHandEffect;
    private static Texture numbersEffect;
    private static Texture hexagramEffect;
    private static boolean loaded = false;

    private static Texture lowAlpha;

    private static Texture highAlpha;

    private static Texture lowAlphaEffect;

    private static Texture highAlphaEffect;

    private float effectScale;
    private float bgScale;
    private List<AbstractGameEffect> effectList;

    public static void initialize() {
        if (!loaded) {
            mark5 = new Texture(IceBreaker.getResourcePath("effects/victoryEffect/clock_mark_5.png"));
            mark = new Texture(IceBreaker.getResourcePath("effects/victoryEffect/clock_mark.png"));
            center = new Texture(IceBreaker.getResourcePath("effects/victoryEffect/clock_center.png"));
            secondHand = new Texture(IceBreaker.getResourcePath("effects/victoryEffect/clock_second_hand.png"));
            minuteHand = new Texture(IceBreaker.getResourcePath("effects/victoryEffect/clock_minute_hand.png"));
            hourHand = new Texture(IceBreaker.getResourcePath("effects/victoryEffect/clock_hour_hand.png"));
            numbers = new Texture(IceBreaker.getResourcePath("effects/victoryEffect/clock_numbers.png"));
            circle = new Texture(IceBreaker.getResourcePath("effects/victoryEffect/clock_circle.png"));
            hexagon = new Texture(IceBreaker.getResourcePath("effects/victoryEffect/clock_hexagon.png"));
            hexagram = new Texture(IceBreaker.getResourcePath("effects/victoryEffect/clock_hexagram.png"));

            mark5Effect = new Texture(IceBreaker.getResourcePath("effects/victoryEffect/clock_mark_5_effect.png"));
            markEffect = new Texture(IceBreaker.getResourcePath("effects/victoryEffect/clock_mark_effect.png"));
            centerEffect = new Texture(IceBreaker.getResourcePath("effects/victoryEffect/clock_center_effect.png"));
            secondHandEffect = new Texture(IceBreaker.getResourcePath("effects/victoryEffect/clock_second_hand_effect.png"));
            minuteHandEffect = new Texture(IceBreaker.getResourcePath("effects/victoryEffect/clock_minute_hand_effect.png"));
            hourHandEffect = new Texture(IceBreaker.getResourcePath("effects/victoryEffect/clock_hour_hand_effect.png"));
            numbersEffect = new Texture(IceBreaker.getResourcePath("effects/victoryEffect/clock_numbers_effect.png"));
            hexagramEffect = new Texture(IceBreaker.getResourcePath("effects/victoryEffect/clock_hexagram_effect.png"));

            lowAlpha = new Texture(IceBreaker.getResourcePath("effects/victoryEffect/others_low_alpha.png"));
            highAlpha = new Texture(IceBreaker.getResourcePath("effects/victoryEffect/others_high_alpha.png"));
            lowAlphaEffect = new Texture(IceBreaker.getResourcePath("effects/victoryEffect/others_low_alpha_effect.png"));
            highAlphaEffect = new Texture(IceBreaker.getResourcePath("effects/victoryEffect/others_high_alpha_effect.png"));
            loaded = true;
        }
    }

    public VictoryClockEffect() {
        this.duration = 0.0F;
        this.scale = Settings.scale * 0.75F;
        this.color = Color.WHITE.cpy();
        this.effectList = new ArrayList<>();
    }

    @Override
    public void update() {
        this.effectScale = MathUtils.sin(this.duration * 2.0F) * 0.01F + 1.0F;
        this.bgScale = MathUtils.cos(this.duration * 2.0F) * 0.01F + 1.0F;
        List<AbstractGameEffect> toRemove = new ArrayList<>();
        for (AbstractGameEffect effect : effectList) {
            if (!effect.isDone) {
                effect.update();
            } else {
                toRemove.add(effect);
            }
        }
        effectList.removeAll(toRemove);
        if (effectList.size() < 100) {
            effectList.add(new SilentVictoryStarEffect());
            effectList.add(new SilentVictoryStarEffect());
            effectList.add(new SilentVictoryStarEffect());
            effectList.add(new SilentVictoryStarEffect());
        }
        this.duration += Gdx.graphics.getDeltaTime();
    }

    private void renderLayer(SpriteBatch sb, Texture texture, float scale, float rotationDeg, boolean shining) {
        sb.draw(texture,
                Settings.WIDTH / 2.0F - 640.0F, Settings.HEIGHT / 2.0F - 640.0F,
                640.0F, 640.0F,
                1280.0F, 1280.0F,
                this.scale * scale * (shining ? MathUtils.random(0.995F, 1.005F) : 1.0F), this.scale * scale * (shining ? MathUtils.random(0.99F, 1.01F) : 1.0F),
                rotationDeg,
                0, 0,
                1280, 1280,
                false, false
        );
    }

    private void renderBgAndFgLayer(SpriteBatch sb, Texture texture, float scale, float power, boolean shining) {
        sb.draw(texture,
                (Settings.WIDTH / 2.0F - 960.0F) * (float) Math.pow(scale, 3.5F), Settings.HEIGHT / 2.0F - 540.0F,
                960.0F, 540.0F,
                1920.0F, 1080.0F,
                Settings.scale * scale * (shining ? MathUtils.random(0.997F, 1.003F) : 1.0F) * (float) Math.pow(scale, power), Settings.scale * scale * (shining ? MathUtils.random(0.99F, 1.01F) : 1.0F),
                0,
                0, 0,
                1920, 1080,
                false, false
        );
    }

    @Override
    public void render(SpriteBatch sb) {
        if (!loaded) return;
        this.color.a = 0.62F;
        sb.setColor(this.color);
        renderBgAndFgLayer(sb, highAlphaEffect, bgScale, 3, true);
        renderBgAndFgLayer(sb, highAlpha, bgScale, 3.5F, false);

        this.color.a = 1.0F;
        sb.setColor(this.color);
        renderLayer(sb, hexagramEffect, effectScale, 0.0F, true);
        renderLayer(sb, numbersEffect, effectScale, 0.0F, true);
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR);
        int min = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        renderLayer(sb, hourHandEffect, effectScale, -30.0F * hour, true);
        renderLayer(sb, minuteHandEffect, effectScale, -6.0F * min, true);
        renderLayer(sb, secondHandEffect, effectScale, -6.0F * second, true);
        renderLayer(sb, centerEffect, effectScale, 0.0F, true);
        for (int i=0;i<60;i++) {
            renderLayer(sb, markEffect, effectScale, 6.0F * i, true);
        }
        for (int i=0;i<12;i++) {
            renderLayer(sb, mark5Effect, effectScale, 30.0F * i, true);
        }

        renderLayer(sb, hexagram, 1.0F, 0.0F, false);
        renderLayer(sb, hexagon, 1.0F, 0.0F, false);
        renderLayer(sb, circle, 1.0F, 0.0F, false);
        renderLayer(sb, numbers, 1.0F, 0.0F, false);
        renderLayer(sb, hourHand, 1.0F, -30.0F * hour, false);
        renderLayer(sb, minuteHand, 1.0F, -6.0F * min, false);
        renderLayer(sb, secondHand, 1.0F, -6.0F * second, false);
        renderLayer(sb, center, 1.0F, 0.0F, false);
        for (int i=0;i<60;i++) {
            renderLayer(sb, mark, 1.0F, 6.0F * i, false);
        }
        for (int i=0;i<12;i++) {
            renderLayer(sb, mark5, 1.0F, 30.0F * i, false);
        }

        this.color.a = 0.38F;
        sb.setColor(this.color);
        renderBgAndFgLayer(sb, lowAlphaEffect, effectScale, 4, true);
        renderBgAndFgLayer(sb, lowAlpha, effectScale * 0.99F, 4.7F, false);
        for (AbstractGameEffect effect : effectList) {
            effect.render(sb);
        }
    }

    @Override
    public void dispose() {

    }
}
