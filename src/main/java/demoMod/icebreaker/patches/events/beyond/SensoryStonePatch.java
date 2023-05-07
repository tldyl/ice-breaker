package demoMod.icebreaker.patches.events.beyond;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.beyond.SensoryStone;
import com.megacrit.cardcrawl.localization.EventStrings;
import demoMod.icebreaker.IceBreaker;
import demoMod.icebreaker.characters.IceBreakerCharacter;

public class SensoryStonePatch {
    public static final String ID = IceBreaker.makeID("SensoryStone");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    public static final String NAME = eventStrings.NAME;
    public static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;

    @SpirePatch(
            clz = SensoryStone.class,
            method = "getRandomMemory"
    )
    public static class PatchGetRandomMemory {
        public static SpireReturn<Void> Prefix(SensoryStone event) {
            if (AbstractDungeon.player instanceof IceBreakerCharacter) {
                event.imageEventText.updateBodyText(DESCRIPTIONS[6]);
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }
}
