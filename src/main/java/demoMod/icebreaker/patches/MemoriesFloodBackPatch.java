package demoMod.icebreaker.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import java.util.ArrayList;

@SpirePatch(
        clz = AbstractRoom.class,
        method = "update"
)
public class MemoriesFloodBackPatch {
    public static ArrayList<AbstractCard> cards;

    private static boolean activated;

    static {
        cards = new ArrayList<>();

        activated = false;
    }

    @SpireInsertPatch(rloc = 317 - 252)
    public static void Insert(AbstractRoom self) {
        if (!activated) {
            AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
                @Override
                public void update() {
                    AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
                        @Override
                        public void update() {
                            for (AbstractCard c : AbstractDungeon.player.hand.group) {
                                cards.add(c.makeCopy());
                            }
                            this.isDone = true;
                        }
                    });
                    this.isDone = true;
                }
            });
            activated = true;
        }
    }
}
