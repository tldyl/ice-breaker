package demoMod.icebreaker.utils;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.powers.AbstractPower;
import demoMod.icebreaker.IceBreaker;

import java.util.HashMap;
import java.util.Map;

public class PowerRegionLoader {
    private static final Map<String, Texture> powerRegionMap = new HashMap<>();

    public static void load(AbstractPower power) {
        String region48 = power.getClass().getSimpleName().replace("Power", "") + "48";
        String region128 = power.getClass().getSimpleName().replace("Power", "") + "128";
        if (!powerRegionMap.containsKey(region48)) {
            powerRegionMap.put(region48, new Texture(IceBreaker.getResourcePath("powers/" + region48 + ".png")));
        }
        if (!powerRegionMap.containsKey(region128)) {
            powerRegionMap.put(region128, new Texture(IceBreaker.getResourcePath("powers/" + region128 + ".png")));
        }
        Texture texture48 = powerRegionMap.get(region48);
        Texture texture128 = powerRegionMap.get(region128);
        power.region48 = new TextureAtlas.AtlasRegion(texture48, 0, 0, texture48.getWidth(), texture48.getHeight());
        power.region128 = new TextureAtlas.AtlasRegion(texture128, 0, 0, texture128.getWidth(), texture128.getHeight());
    }
}
