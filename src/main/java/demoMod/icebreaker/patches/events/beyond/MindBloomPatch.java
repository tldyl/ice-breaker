package demoMod.icebreaker.patches.events.beyond;

import basemod.ReflectionHacks;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.curses.Doubt;
import com.megacrit.cardcrawl.cards.curses.Normality;
import com.megacrit.cardcrawl.cards.curses.Regret;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.beyond.MindBloom;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.vfx.RainingGoldEffect;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import demoMod.icebreaker.IceBreaker;
import demoMod.icebreaker.cards.lightlemon.RainbowViolet;
import demoMod.icebreaker.characters.IceBreakerCharacter;

import java.util.ArrayList;
import java.util.List;

public class MindBloomPatch {
    public static final String ID = IceBreaker.makeID("MindBloom");
    private static final EventStrings eventStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;
    public static final String[] OPTIONS;

    @SpirePatch(
            clz = MindBloom.class,
            method = SpirePatch.CONSTRUCTOR
    )
    public static class PatchConstructor {
        public static void Postfix(MindBloom event) {
            if (AbstractDungeon.player instanceof IceBreakerCharacter) {
                event.imageEventText.setDialogOption(OPTIONS[4], new RainbowViolet());
            }
        }
    }

    @SpirePatch(
            clz = MindBloom.class,
            method = "buttonEffect"
    )
    public static class PatchButtonEffect {
        public static SpireReturn<Void> Prefix(MindBloom event, int buttonPressed) {
            if (AbstractDungeon.player instanceof IceBreakerCharacter) {
                try {
                    Enum screen = ReflectionHacks.getPrivate(event, MindBloom.class, "screen");
                    switch (screen.name()) {
                        case "INTRO":
                            switch (buttonPressed) {
                                case 0:
                                    return SpireReturn.Continue();
                                case 1:
                                    event.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                                    int effectCount = 0;
                                    List<String> upgradedCards = new ArrayList<>();
                                    List<String> obtainedRelic = new ArrayList<>();

                                    for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
                                        if (c.canUpgrade()) {
                                            ++effectCount;
                                            if (effectCount <= 20) {
                                                float x = MathUtils.random(0.1F, 0.9F) * (float) Settings.WIDTH;
                                                float y = MathUtils.random(0.2F, 0.8F) * (float) Settings.HEIGHT;
                                                AbstractDungeon.effectList.add(new ShowCardBrieflyEffect(c.makeStatEquivalentCopy(), x, y));
                                                AbstractDungeon.topLevelEffects.add(new UpgradeShineEffect(x, y));
                                            }

                                            upgradedCards.add(c.cardID);
                                            c.upgrade();
                                            AbstractDungeon.player.bottledCardUpgradeCheck(c);
                                        }
                                    }

                                    AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F, RelicLibrary.getRelic("Mark of the Bloom").makeCopy());
                                    obtainedRelic.add("Mark of the Bloom");
                                    AbstractEvent.logMetric("MindBloom", "Upgrade", null, null, null, upgradedCards, obtainedRelic, null, null, 0, 0, 0, 0, 0, 0);
                                    event.imageEventText.updateDialogOption(0, OPTIONS[5]);
                                    break;
                                case 2:
                                    if (AbstractDungeon.floorNum % 50 <= 40) {
                                        event.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                                        List<String> cardsAdded = new ArrayList<>();
                                        cardsAdded.add("Normality");
                                        cardsAdded.add("Normality");
                                        AbstractEvent.logMetric("MindBloom", "Gold", cardsAdded, null, null, null, null, null, null, 0, 0, 0, 0, 999, 0);
                                        AbstractDungeon.effectList.add(new RainingGoldEffect(999));
                                        AbstractDungeon.player.gainGold(999);
                                        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(new Normality(), (float)Settings.WIDTH * 0.6F, (float)Settings.HEIGHT / 2.0F));
                                        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(new Normality(), (float)Settings.WIDTH * 0.3F, (float)Settings.HEIGHT / 2.0F));
                                        event.imageEventText.updateDialogOption(0, OPTIONS[5]);
                                    } else {
                                        event.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                                        AbstractCard curse = new Doubt();
                                        AbstractEvent.logMetricObtainCardAndHeal("MindBloom", "Heal", curse, AbstractDungeon.player.maxHealth - AbstractDungeon.player.currentHealth);
                                        AbstractDungeon.player.heal(AbstractDungeon.player.maxHealth);
                                        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(curse, (float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
                                        event.imageEventText.updateDialogOption(0, OPTIONS[5]);
                                    }
                                    break;
                                case 3:
                                    event.imageEventText.updateBodyText(DESCRIPTIONS[4]);
                                    AbstractCard curse = new Regret();
                                    AbstractCard card = new RainbowViolet();
                                    List<String> cardsAdded = new ArrayList<>();
                                    cardsAdded.add(curse.cardID);
                                    cardsAdded.add(card.cardID);
                                    AbstractEvent.logMetric("MindBloom", "Gold", cardsAdded, null, null, null, null, null, null, 0, 0, 0, 0, 999, 0);
                                    AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(curse, (float)Settings.WIDTH * 0.6F, (float)Settings.HEIGHT / 2.0F));
                                    AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(card, (float)Settings.WIDTH * 0.3F, (float)Settings.HEIGHT / 2.0F));
                                    event.imageEventText.updateDialogOption(0, OPTIONS[5]);
                                    break;
                            }
                            ReflectionHacks.setPrivate(event, MindBloom.class, "screen", Enum.valueOf((Class<Enum>) Class.forName("com.megacrit.cardcrawl.events.beyond.MindBloom$CurScreen"), "LEAVE"));
                            event.imageEventText.clearAllDialogs();
                            event.imageEventText.setDialogOption(OPTIONS[5]);
                            return SpireReturn.Return(null);
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            return SpireReturn.Continue();
        }
    }

    static {
        eventStrings = CardCrawlGame.languagePack.getEventString(ID);
        NAME = eventStrings.NAME;
        DESCRIPTIONS = eventStrings.DESCRIPTIONS;
        OPTIONS = eventStrings.OPTIONS;
    }
}
