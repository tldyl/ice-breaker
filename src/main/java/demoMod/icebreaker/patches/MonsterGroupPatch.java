package demoMod.icebreaker.patches;

import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglGraphics;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import demoMod.icebreaker.powers.ExtraTurnPower;

public class MonsterGroupPatch {
    @SpirePatch(
            clz = MonsterGroup.class,
            method = "update"
    )
    public static class PatchUpdate {
        public static void Prefix(MonsterGroup group) {
            if (AbstractDungeon.player.hasPower(ExtraTurnPower.POWER_ID)) {
                ReflectionHacks.setPrivate(Gdx.graphics, LwjglGraphics.class, "deltaTime", Gdx.graphics.getDeltaTime() / 10.0F);
            }
        }

        public static void Postfix(MonsterGroup group) {
            if (AbstractDungeon.player.hasPower(ExtraTurnPower.POWER_ID)) {
                ReflectionHacks.setPrivate(Gdx.graphics, LwjglGraphics.class, "deltaTime", Gdx.graphics.getDeltaTime() * 10.0F);
            }
        }
    }

    @SpirePatch(
            clz = AbstractMonster.class,
            method = "render"
    )
    public static class PatchRender {
        public static void Prefix(AbstractMonster monster, SpriteBatch sb) {
            if (AbstractDungeon.player.hasPower(ExtraTurnPower.POWER_ID)) {
                ReflectionHacks.setPrivate(Gdx.graphics, LwjglGraphics.class, "deltaTime", Gdx.graphics.getDeltaTime() / 10.0F);
            }
        }

        public static void Postfix(AbstractMonster monster, SpriteBatch sb) {
            if (AbstractDungeon.player.hasPower(ExtraTurnPower.POWER_ID)) {
                ReflectionHacks.setPrivate(Gdx.graphics, LwjglGraphics.class, "deltaTime", Gdx.graphics.getDeltaTime() * 10.0F);
            }
        }
    }
}
