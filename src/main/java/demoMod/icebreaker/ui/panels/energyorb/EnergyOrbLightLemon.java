package demoMod.icebreaker.ui.panels.energyorb;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.ui.panels.energyorb.EnergyOrbInterface;
import demoMod.icebreaker.IceBreaker;

public class EnergyOrbLightLemon implements EnergyOrbInterface {
    private static final Texture[] orbTextures = {
            new Texture(IceBreaker.getResourcePath("char/orb/1.png")),
            new Texture(IceBreaker.getResourcePath("char/orb/2.png")),
            new Texture(IceBreaker.getResourcePath("char/orb/3.png")),
            new Texture(IceBreaker.getResourcePath("char/orb/4.png")),
            new Texture(IceBreaker.getResourcePath("char/orb/5.png")),
            new Texture(IceBreaker.getResourcePath("char/orb/6.png")),
            new Texture(IceBreaker.getResourcePath("char/orb/7.png")),
            new Texture(IceBreaker.getResourcePath("char/orb/8.png")),
            new Texture(IceBreaker.getResourcePath("char/orb/9.png")),
            new Texture(IceBreaker.getResourcePath("char/orb/10.png")),
            new Texture(IceBreaker.getResourcePath("char/orb/11.png")),
            new Texture(IceBreaker.getResourcePath("char/orb/12.png")),
    };

    private static final Texture[] orbTexturesNoEnergy = {
            new Texture(IceBreaker.getResourcePath("char/orb/1d.png")),
            new Texture(IceBreaker.getResourcePath("char/orb/2d.png")),
            new Texture(IceBreaker.getResourcePath("char/orb/3d.png")),
            new Texture(IceBreaker.getResourcePath("char/orb/4d.png")),
            new Texture(IceBreaker.getResourcePath("char/orb/5d.png")),
            new Texture(IceBreaker.getResourcePath("char/orb/6d.png")),
            new Texture(IceBreaker.getResourcePath("char/orb/7d.png")),
            new Texture(IceBreaker.getResourcePath("char/orb/8d.png")),
            new Texture(IceBreaker.getResourcePath("char/orb/9d.png")),
            new Texture(IceBreaker.getResourcePath("char/orb/10d.png")),
            new Texture(IceBreaker.getResourcePath("char/orb/11d.png")),
            new Texture(IceBreaker.getResourcePath("char/orb/12d.png")),
    };

    private static final float ORB_IMG_SCALE = 1.3F * Settings.scale;

    private final int[] layerSpeeds = new int[]{1, -1, 0, 2, -2, 1, 2, 3, -3, -2, -1, 0};
    private final float[] angles = new float[orbTextures.length];

    @Override
    public void renderOrb(SpriteBatch sb, boolean enabled, float current_x, float current_y) {
        sb.setColor(Color.WHITE);
        if (enabled) {
            for(int i = 0; i < orbTextures.length; ++i) {
                sb.draw(orbTextures[i], current_x - 64.0F, current_y - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, ORB_IMG_SCALE, ORB_IMG_SCALE, this.angles[i], 0, 0, 128, 128, false, false);
            }
        } else {
            for(int i = 0; i < orbTextures.length; ++i) {
                sb.draw(orbTexturesNoEnergy[i], current_x - 64.0F, current_y - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, ORB_IMG_SCALE, ORB_IMG_SCALE, this.angles[i], 0, 0, 128, 128, false, false);
            }
        }
    }

    @Override
    public void updateOrb(int energyCount) {
        float energizedSpeed = 20.0F;
        float noEnergySpeed = 5.0F;
        if (energyCount <= 0) {
            for (int i=0;i<angles.length;i++) {
                angles[i] -= Gdx.graphics.getDeltaTime() * noEnergySpeed * layerSpeeds[i];
            }
        } else {
            for (int i=0;i<angles.length;i++) {
                angles[i] -= Gdx.graphics.getDeltaTime() * energizedSpeed * layerSpeeds[i];
            }
        }
    }
}
