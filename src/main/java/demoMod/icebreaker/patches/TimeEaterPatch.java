package demoMod.icebreaker.patches;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.beyond.TimeEater;
import com.megacrit.cardcrawl.vfx.combat.WhirlwindEffect;
import demoMod.icebreaker.IceBreaker;
import demoMod.icebreaker.characters.IceBreakerCharacter;
import demoMod.icebreaker.effects.DefeatTimeEaterEffect;

public class TimeEaterPatch {
    @SpirePatch(
            clz = TimeEater.class,
            method = "die"
    )
    public static class PatchDie {
        public static void Prefix(TimeEater timeEater) {
            if (!AbstractDungeon.getCurrRoom().cannotLose && AbstractDungeon.player instanceof IceBreakerCharacter) {
                IceBreaker.addToTop(new VFXAction(null, new WhirlwindEffect(new Color(1.0F, 0.9F, 0.4F, 1.0F), true), 0.0F, true));
                IceBreaker.addToTop(new VFXAction(null, new DefeatTimeEaterEffect(0.0F, Color.WHITE), 0.0F, true));
                Color color = Color.BLUE.cpy();
                color.a = 0.5F;
                IceBreaker.addToTop(new VFXAction(null, new DefeatTimeEaterEffect(0.0F * Settings.scale, color), 0.05F, true));
                color = Color.TEAL.cpy();
                color.a = 0.5F;
                IceBreaker.addToTop(new VFXAction(null, new DefeatTimeEaterEffect(0.0F * Settings.scale, color), 0.05F, true));
                color = Color.FIREBRICK.cpy();
                color.a = 0.5F;
                IceBreaker.addToTop(new VFXAction(null, new DefeatTimeEaterEffect(0.0F * Settings.scale, color), 0.05F, true));
                AbstractDungeon.topLevelEffects.add(new WhirlwindEffect(new Color(1.0F, 0.9F, 0.4F, 1.0F), true));
            }
        }
    }
}
