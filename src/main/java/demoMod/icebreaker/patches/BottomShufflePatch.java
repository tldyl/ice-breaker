package demoMod.icebreaker.patches;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.actions.common.ShuffleAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import demoMod.icebreaker.cards.lightlemon.AbstractLightLemonCard;

import java.util.ArrayList;
import java.util.List;

public class BottomShufflePatch {
    @SpirePatch(clz = ShuffleAction.class, method = "update")
    public static class PatchUpdate {
        @SpireInsertPatch(rloc = 6)
        public static void Insert(ShuffleAction action) {
            CardGroup group = (CardGroup) ReflectionHacks.getPrivate(action, ShuffleAction.class, "group");
            List<AbstractCard> tmp = new ArrayList<>();
            for (AbstractCard card : group.group) {
                if (card instanceof AbstractLightLemonCard) {
                    if (((AbstractLightLemonCard)card).isBottom) {
                        tmp.add(card);
                    }
                }
            }
            group.group.removeAll(tmp);
            group.group.addAll(0, tmp);
        }
    }

    @SpirePatch(clz = EmptyDeckShuffleAction.class, method = "update")
    public static class PatchEmptyShuffle {
        @SpireInsertPatch(rloc = 3)
        public static void Insert(EmptyDeckShuffleAction action) {
            List<AbstractCard> tmp = new ArrayList<>();
            for (AbstractCard card : AbstractDungeon.player.discardPile.group) {
                if (card instanceof AbstractLightLemonCard) {
                    if (((AbstractLightLemonCard)card).isBottom) {
                        tmp.add(card);
                    }
                }
            }
            AbstractDungeon.player.discardPile.group.removeAll(tmp);
            AbstractDungeon.player.discardPile.group.addAll(0, tmp);
        }
    }
}
