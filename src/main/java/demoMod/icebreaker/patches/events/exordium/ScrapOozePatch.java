package demoMod.icebreaker.patches.events.exordium;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.exordium.ScrapOoze;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.monsters.exordium.AcidSlime_L;
import com.megacrit.cardcrawl.monsters.exordium.SpikeSlime_L;
import demoMod.icebreaker.IceBreaker;
import demoMod.icebreaker.characters.IceBreakerCharacter;

public class ScrapOozePatch {
    public static final String ID = IceBreaker.makeID("Scrap Ooze");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    public static final String NAME = eventStrings.NAME;
    public static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    public static final String[] OPTIONS = eventStrings.OPTIONS;

    @SpirePatch(
            clz = ScrapOoze.class,
            method = SpirePatch.CONSTRUCTOR
    )
    public static class PatchConstructor {
        public static void Postfix(ScrapOoze event) {
            if (AbstractDungeon.player instanceof IceBreakerCharacter) {
                event.imageEventText.updateDialogOption(0, OPTIONS[0]);
            }
        }
    }

    @SpirePatch(
            clz = ScrapOoze.class,
            method = "buttonEffect"
    )
    public static class PatchButtonEffect {
        public static SpireReturn<Void> Prefix(ScrapOoze event, int buttonPressed) {
            if (AbstractDungeon.player instanceof IceBreakerCharacter) {
                int screenNum = ReflectionHacks.getPrivate(event, ScrapOoze.class, "screenNum");
                switch (screenNum) {
                    case 0:
                        switch (buttonPressed) {
                            case 0:
                                event.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                                ReflectionHacks.setPrivate(event, ScrapOoze.class, "screenNum", 1);
                                event.imageEventText.updateDialogOption(0, OPTIONS[1]);
                                event.imageEventText.removeDialogOption(1);
                                break;
                        }
                        return SpireReturn.Return(null);
                    case 1:
                        AbstractMonster slime = AbstractDungeon.miscRng.randomBoolean() ? new AcidSlime_L(0.0F, 0.0F) : new SpikeSlime_L(0.0F, 0.0F);
                        AbstractDungeon.getCurrRoom().monsters = new MonsterGroup(new AbstractMonster[] {slime});
                        AbstractDungeon.getCurrRoom().rewards.clear();
                        AbstractDungeon.getCurrRoom().addRelicToRewards(AbstractDungeon.returnRandomRelicTier());
                        event.noCardsInRewards = true;
                        event.enterCombatFromImage();
                        return SpireReturn.Return(null);
                }
            }
            return SpireReturn.Continue();
        }
    }
}
