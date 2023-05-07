package demoMod.icebreaker.patches.events.shrines;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import demoMod.icebreaker.characters.IceBreakerCharacter;

public class NoteForYourselfPatch {
    @SpirePatch(
            clz = AbstractDungeon.class,
            method = "isNoteForYourselfAvailable"
    )
    public static class PatchIsNoteForYourselfAvailable {
        public static SpireReturn<Boolean> Prefix(AbstractDungeon dungeon) {
            if (AbstractDungeon.player instanceof IceBreakerCharacter) {
                return SpireReturn.Return(false);
            }
            return SpireReturn.Continue();
        }
    }
}
