package demoMod.icebreaker.patches;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.VictoryScreen;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import demoMod.icebreaker.effects.VictoryClockEffect;
import demoMod.icebreaker.enums.AbstractPlayerEnum;

import java.util.ArrayList;

public class VictoryScreenPatch {
    @SpirePatch(
            clz = VictoryScreen.class,
            method = "updateVfx"
    )
    public static class PatchUpdateVfx {
        public static void Postfix(VictoryScreen screen) {
            if (AbstractDungeon.player.chosenClass == AbstractPlayerEnum.ICEBREAKER) {
                ArrayList<AbstractGameEffect> effect = ReflectionHacks.getPrivate(screen, VictoryScreen.class, "effect");
                if (effect.isEmpty()) {
                    effect.add(new VictoryClockEffect());
                }
            }
        }
    }
}
