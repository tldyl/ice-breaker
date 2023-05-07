package demoMod.icebreaker.patches.events.shrines;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import demoMod.icebreaker.characters.IceBreakerCharacter;

public class WeMeetAgainPatch {
    @SpirePatch(
            clz = AbstractDungeon.class,
            method = "initializeSpecialOneTimeEventList"
    )
    public static class PatchInitializeSpecialOneTimeEventList {
        public static void Postfix(AbstractDungeon dungeon) {
            if (AbstractDungeon.player instanceof IceBreakerCharacter) {
                AbstractDungeon.specialOneTimeEventList.remove("WeMeetAgain");
            }
        }
    }
}
