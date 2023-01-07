package demoMod.icebreaker.patches;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.cutscenes.Cutscene;
import com.megacrit.cardcrawl.cutscenes.CutscenePanel;
import demoMod.icebreaker.IceBreaker;
import demoMod.icebreaker.enums.AbstractPlayerEnum;

import java.util.ArrayList;

public class CutscenePatch {
    @SpirePatch(
            clz = Cutscene.class,
            method = SpirePatch.CONSTRUCTOR
    )
    public static class PatchConstructor {
        public static void Postfix(Cutscene cutscene, AbstractPlayer.PlayerClass chosenClass) {
            if (chosenClass == AbstractPlayerEnum.ICEBREAKER) {
                ArrayList<CutscenePanel> cutscenePanels = new ArrayList<>();
                Texture bgImg = new Texture(IceBreaker.getResourcePath("scenes/yellowBg.png"));
                cutscenePanels.add(
                        new CutscenePanel(IceBreaker.getResourcePath("scenes/ice1.png"), "ATTACK_FIRE")
                );
                cutscenePanels.add(
                        new CutscenePanel(IceBreaker.getResourcePath("scenes/ice2.png"))
                );
                cutscenePanels.add(
                        new CutscenePanel(IceBreaker.getResourcePath("scenes/ice3.png"))
                );
                cutscenePanels.add(
                        new CutscenePanel(IceBreaker.getResourcePath("scenes/ice4.png"))
                );
                cutscenePanels.add(
                        new CutscenePanel(IceBreaker.getResourcePath("scenes/ice5.png"))
                );
                cutscenePanels.add(
                        new CutscenePanel(IceBreaker.getResourcePath("scenes/ice6.png"))
                );
                cutscenePanels.add(
                        new CutscenePanel(IceBreaker.getResourcePath("scenes/ice7.png"))
                );
                cutscenePanels.add(
                        new CutscenePanel(IceBreaker.getResourcePath("scenes/ice8.png"))
                );
                cutscenePanels.add(
                        new CutscenePanel(IceBreaker.getResourcePath("scenes/ice9.png"))
                );
                ArrayList<CutscenePanel> panels = ReflectionHacks.getPrivate(cutscene, Cutscene.class, "panels");
                panels.clear();
                panels.addAll(cutscenePanels);
                ReflectionHacks.setPrivate(cutscene, Cutscene.class, "bgImg", bgImg);
            }
        }
    }
}
