package demoMod.icebreaker.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.exordium.ScrapOoze;
import com.megacrit.cardcrawl.ui.buttons.ProceedButton;
import javassist.CtBehavior;

import java.util.ArrayList;
import java.util.List;

public class ProceedButtonPatch {
    @SpirePatch(
            clz = ProceedButton.class,
            method = "update"
    )
    public static class PatchUpdate {
        private static final List<Class<? extends AbstractEvent>> combatEvents = new ArrayList<>();

        static {
            combatEvents.add(ScrapOoze.class);
        }

        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(ProceedButton button) {
            for (Class<? extends AbstractEvent> eventCls : combatEvents) {
                AbstractEvent event = AbstractDungeon.getCurrRoom().event;
                if (event.combatTime && eventCls.isInstance(event)) {
                    button.show();
                    AbstractDungeon.dungeonMapScreen.open(false);
                    AbstractDungeon.previousScreen = AbstractDungeon.CurrentScreen.COMBAT_REWARD;
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher.MethodCallMatcher methodCallMatcher = new Matcher.MethodCallMatcher(ProceedButton.class, "hide");
                return LineFinder.findInOrder(ctBehavior, new ArrayList<>(), methodCallMatcher);
            }
        }
    }
}
