package demoMod.icebreaker.actions;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.unique.DiscoveryAction;
import com.megacrit.cardcrawl.actions.utility.ScryAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.screens.select.GridCardSelectScreen;
import demoMod.icebreaker.IceBreaker;
import demoMod.icebreaker.cards.lightlemon.tempCards.Spark;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Ref;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class SelectCardInCardGroupAction extends AbstractGameAction {
    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString(IceBreaker.makeID("SelectCardInCardGroupAction")).TEXT;
    private final Predicate<AbstractCard> condition;
    private final Consumer<AbstractCard> action;
    private final CardGroup cardGroup;

    public SelectCardInCardGroupAction(int amount, Predicate<AbstractCard> condition, Consumer<AbstractCard> action, CardGroup cardGroup) {
        this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
        this.duration = (this.startDuration = Settings.ACTION_DUR_FAST);
        this.condition = condition;
        this.action = action;
        this.cardGroup = cardGroup;
        this.amount = amount;
    }

    @Override
    public void update() {
        if (this.duration == this.startDuration && AbstractDungeon.screen != AbstractDungeon.CurrentScreen.GRID) {
            CardGroup temp;
            if (this.cardGroup.isEmpty()) {
                this.isDone = true;
                return;
            }
            temp = new CardGroup(com.megacrit.cardcrawl.cards.CardGroup.CardGroupType.UNSPECIFIED);
            for (AbstractCard c : this.cardGroup.group) {
                if (this.condition.test(c)) temp.addToTop(c);
            }
            if (temp.isEmpty()) {
                this.isDone = true;
                return;
            }
            temp.sortAlphabetically(true);
            temp.sortByRarityPlusStatusCardType(false);
            if (AbstractDungeon.isScreenUp) {
                AbstractDungeon.dynamicBanner.hide();
                AbstractDungeon.overlayMenu.cancelButton.hide();
                AbstractDungeon.previousScreen = AbstractDungeon.screen;
            }
            AbstractDungeon.gridSelectScreen.open(temp, this.amount, true, String.format(TEXT[0], this.amount));
            tickDuration();
            return;
        }
        /*
            本来感觉应该和预见选牌一样啊
            但是战斗内的Action的update时机和战斗外的不同 战斗内的只有在!AbstractDungeon.isScreenUp的时候才update
            然而这个会一直在update 所以为了防止点了设置键之类的之后立刻进入下面的if 要多加几个判断
        */
        if (AbstractDungeon.screen != AbstractDungeon.CurrentScreen.GRID &&
                AbstractDungeon.screen != AbstractDungeon.CurrentScreen.MASTER_DECK_VIEW &&
                AbstractDungeon.screen != AbstractDungeon.CurrentScreen.SETTINGS &&
                AbstractDungeon.screen != AbstractDungeon.CurrentScreen.MAP) {
            for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
                this.action.accept(c);
            }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            AbstractDungeon.player.hand.refreshHandLayout();
            AbstractDungeon.overlayMenu.cancelButton.hide();
            isDone = true;
        }
    }

    /*
        以下代码的功能：选择羁绊哪些牌时不会一选到牌数上限马上结束选择 必须要点击确认后才选择
        其他使用此action选牌的功能同样受到影响 当然专门写一个类给羁绊选牌也可以
        不保证不出现bug 所以可以酌情加入 不加影响也没有那么大
    */

    @SpirePatch(clz = GridCardSelectScreen.class, method = "update")
    public static class StupidCaseyYanoCodePatch1 {
        @SpireInsertPatch(rloc = 155-83)
        public static SpireReturn<Void> Insert(GridCardSelectScreen __) {
            int c1 = ReflectionHacks.getPrivate(__, GridCardSelectScreen.class, "cardSelectAmount");
            int c2 = ReflectionHacks.getPrivate(__, GridCardSelectScreen.class, "numCards");
            /*
                矢野靠选到可选牌数上限之后马上就结束选择来限制选牌数量
                后面应该是没有任何操作了 所以就直接return了
            */
            if (c1 == c2) return SpireReturn.Return();
            else return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = GridCardSelectScreen.class, method = "update")
    public static class StupidCaseyYanoCodePatch2 {
         /*
            那预见选牌为什么选到上限了也不马上结束？哦原来傻逼矢野写了个莫名其妙的 this.cardSelectAmount < this.targetGroup.size() 时才马上结束
            直接给你 this.targetGroup.size() 改成 -1
         */
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getClassName().equals(CardGroup.class.getName()) && m.getMethodName().equals("size")) {
                        m.replace("$_ = -1;");
                    }
                }
            };
        }
    }
    // 以上代码的功能：选择羁绊哪些牌时不会一选到牌数上限马上结束选择 必须要点击确认后才选择
}
