package demoMod.icebreaker.patches;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import demoMod.icebreaker.powers.ExtraTurnPower;
import javassist.CtBehavior;

import java.util.ArrayList;

public class AbstractDungeonPatch {
    private static ShaderProgram silverShader = new ShaderProgram(
            Gdx.files.internal("iceShaders/grayscale/vertexShader.vsh"),
            Gdx.files.internal("iceShaders/grayscale/fragShader.fsh")
    );

    @SpirePatch(
            clz = AbstractDungeon.class,
            method = "render"
    )
    public static class PatchRender {
        public static void Prefix(AbstractDungeon dungeon, SpriteBatch sb) {
            if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && AbstractDungeon.player.hasPower(ExtraTurnPower.POWER_ID)) {
                sb.setShader(silverShader);
                CardCrawlGame.psb.setShader(silverShader);
            }
        }

        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(AbstractDungeon dungeon, SpriteBatch sb) {
            sb.setShader(null);
            CardCrawlGame.psb.setShader(null);
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher matcher = new Matcher.MethodCallMatcher(AbstractRoom.class, "render");
                return LineFinder.findInOrder(ctBehavior, new ArrayList<>(), matcher);
            }
        }
    }
}
