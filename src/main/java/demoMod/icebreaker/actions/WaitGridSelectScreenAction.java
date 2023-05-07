package demoMod.icebreaker.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class WaitGridSelectScreenAction extends AbstractGameAction {
    private final Runnable action;
    public static boolean enabled = true;

    public WaitGridSelectScreenAction(Runnable action) {
        this.action = action;
    }

    @Override
    public void update() {
        if (AbstractDungeon.gridSelectScreen.selectedCards.isEmpty() && enabled) {
            action.run();
            isDone = true;
        }
    }
}
