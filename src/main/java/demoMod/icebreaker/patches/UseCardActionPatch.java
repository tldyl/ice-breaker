package demoMod.icebreaker.patches;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import demoMod.icebreaker.actions.PutSpecifiedCardToHandAction;
import demoMod.icebreaker.cards.lightlemon.AbstractLightLemonCard;
import demoMod.icebreaker.interfaces.TriggerFetterSubscriber;
import demoMod.icebreaker.powers.NegatePower;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class UseCardActionPatch {

    @SpirePatch(
            clz = UseCardAction.class,
            method = SpirePatch.CONSTRUCTOR,
            paramtypez = {
                    AbstractCard.class,
                    AbstractCreature.class
            }
    )
    public static class PatchConstructor {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                public void edit(MethodCall m) throws CannotCompileException {
                    String clsName = m.getClassName();
                    String methodName = m.getMethodName();
                    if (clsName.equals("com.megacrit.cardcrawl.powers.AbstractPower") && methodName.equals("onUseCard")) {
                        m.replace("if (!(p.owner instanceof com.megacrit.cardcrawl.monsters.AbstractMonster) || !com.megacrit.cardcrawl.dungeons.AbstractDungeon.player.hasPower(\"IceBreaker:ExtraTurnPower\")) {$_ = $proceed($$);}");
                    }
                }
            };
        }
    }

    @SpirePatch(
            clz = UseCardAction.class,
            method = "update"
    )
    public static class PatchUpdate {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                public void edit(MethodCall m) throws CannotCompileException {
                    String clsName = m.getClassName();
                    String methodName = m.getMethodName();
                    if (clsName.equals("com.megacrit.cardcrawl.powers.AbstractPower") && methodName.equals("onAfterUseCard")) {
                        m.replace("if (!(p.owner instanceof com.megacrit.cardcrawl.monsters.AbstractMonster) || !com.megacrit.cardcrawl.dungeons.AbstractDungeon.player.hasPower(\"IceBreaker:ExtraTurnPower\")) {$_ = $proceed($$);}");
                    }
                }
            };
        }

        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(UseCardAction action) {
            AbstractCard targetCard = ReflectionHacks.getPrivate(action, UseCardAction.class, "targetCard");
            if (targetCard instanceof AbstractLightLemonCard) {
                AbstractLightLemonCard lightLemonCard = (AbstractLightLemonCard) targetCard;
                if (lightLemonCard.isFetter) {
                    boolean containsFetter = false;
                    if (!AbstractDungeon.player.hasPower(NegatePower.POWER_ID)) {
                        for (AbstractCard card : AbstractDungeon.player.drawPile.group) {
                            if (lightLemonCard.fetterTarget.contains(card.uuid)) {
                                containsFetter = true;
                                if (card instanceof TriggerFetterSubscriber) {
                                    ((TriggerFetterSubscriber) card).onTriggerFetter();
                                }
                            }
                        }
                    }

                    for (AbstractPower power : AbstractDungeon.player.powers.stream().filter(power -> power instanceof TriggerFetterSubscriber).collect(Collectors.toList())) {
                        if (containsFetter) {
                            ((TriggerFetterSubscriber) power).onTriggerFetter();
                        } else {
                            ((TriggerFetterSubscriber) power).onTriggerFetterFailed();
                        }
                    }
                    if (containsFetter) {
                        lightLemonCard.onTriggerFetter();
                    } else {
                        for (AbstractCard card : AbstractDungeon.player.drawPile.group) {
                            if (card instanceof TriggerFetterSubscriber) {
                                ((TriggerFetterSubscriber) card).onTriggerFetterFailed();
                            }
                        }
                        for (AbstractCard card : AbstractDungeon.player.discardPile.group) {
                            if (card instanceof TriggerFetterSubscriber) {
                                ((TriggerFetterSubscriber) card).onTriggerFetterFailed();
                            }
                        }
                    }
                    if (!AbstractDungeon.player.hasPower(NegatePower.POWER_ID)) {
                        AbstractDungeon.actionManager.addToBottom(new PutSpecifiedCardToHandAction(lightLemonCard.fetterTarget.size(), card -> lightLemonCard.fetterTarget.contains(card.uuid)));
                    }
                }
            }
        }

        private static final class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(CardGroup.class, "moveToDiscardPile");
                return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<>(), finalMatcher);
            }
        }
    }
}
