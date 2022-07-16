package demoMod.icebreaker.patches;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import demoMod.icebreaker.powers.ExtraTurnPower;

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

        @SpireInsertPatch(rloc = 25)
        public static void Insert(AbstractDungeon dungeon, SpriteBatch sb) {
            sb.setShader(null);
            CardCrawlGame.psb.setShader(null);
        }
    }
}
