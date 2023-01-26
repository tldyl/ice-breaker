package demoMod.icebreaker.actions;

import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.unique.DiscoveryAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.colorless.Madness;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import demoMod.icebreaker.IceBreaker;
import demoMod.icebreaker.enums.CardTagEnum;

import java.util.ArrayList;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.*;
import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.cardRandomRng;

public class DiscoverMagicCardAction extends DiscoveryAction {
    // 从3张随机魔法牌中选择一张加入手牌，其本回合耗能为0
    // 遗物：魔法卷轴

    @SpirePatch(clz = DiscoveryAction.class, method = "generateCardChoices")
    public static class MagicCardPatch {
        @SpireInsertPatch(rloc = 10, localvars = {"tmp"})
        public static void Insert(DiscoveryAction __instance, AbstractCard.CardType type, @ByRef AbstractCard tmp[]) {
            if (__instance instanceof DiscoverMagicCardAction) {
                tmp[0] = generateRandomMagicCard();
            }
        }
    }

    public static AbstractCard generateRandomMagicCard() {
        ArrayList<AbstractCard> list = new ArrayList<>();
        for (AbstractCard c : srcCommonCardPool.group) {
            if (c.hasTag(CardTagEnum.MAGIC) && !c.hasTag(AbstractCard.CardTags.HEALING))
                list.add(c);
        }
        for (AbstractCard c : srcUncommonCardPool.group) {
            if (c.hasTag(CardTagEnum.MAGIC) && !c.hasTag(AbstractCard.CardTags.HEALING))
                list.add(c);
        }
        for (AbstractCard c : srcRareCardPool.group) {
            if (c.hasTag(CardTagEnum.MAGIC) && !c.hasTag(AbstractCard.CardTags.HEALING))
                list.add(c);
        }
        if (list.isEmpty()) return AbstractDungeon.returnTrulyRandomCardInCombat();
        else return list.get(cardRandomRng.random(list.size()-1));
    }
}

