//package demoMod.icebreaker.patches;
//
//import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
//import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
//import com.megacrit.cardcrawl.monsters.AbstractMonster;
//import com.megacrit.cardcrawl.powers.IntangiblePower;
//import com.megacrit.cardcrawl.powers.InvinciblePower;
//import com.megacrit.cardcrawl.powers.ReactivePower;
//import com.megacrit.cardcrawl.powers.ShiftingPower;
//import demoMod.icebreaker.enums.CardTagEnum;
//import demoMod.icebreaker.powers.ExtraTurnPower;
//import javassist.CannotCompileException;
//import javassist.expr.ExprEditor;
//import javassist.expr.MethodCall;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class AbstractMonsterPatch {
//    @SpirePatch(
//            clz = AbstractMonster.class,
//            method = "damage"
//    )
//    public static class PatchDamage {
//        public static List<String> whiteList = new ArrayList<>();
//
//        public static ExprEditor Instrument() {
//            return new ExprEditor() {
//                public void edit(MethodCall m) throws CannotCompileException {
//                    String clsName = m.getClassName();
//                    String methodName = m.getMethodName();
//                    if (whiteList.isEmpty()) {
//                        whiteList.add(IntangiblePower.POWER_ID);
//                        whiteList.add(InvinciblePower.POWER_ID);
//                        whiteList.add(ShiftingPower.POWER_ID);
//                        whiteList.add(ReactivePower.POWER_ID);
//                    }
//                    if (clsName.equals("com.megacrit.cardcrawl.relics.AbstractRelic") && methodName.equals("onAttackToChangeDamage")) {
//                        m.replace(String.format("$_ = %s.ignoreAttackedEffectLogic() ? damageAmount : $proceed($$);", PatchDamage.class.getName()));
//                    }
//                    if (clsName.equals("com.megacrit.cardcrawl.powers.AbstractPower") && methodName.equals("onAttackToChangeDamage")) {
//                        m.replace(String.format("if (%s.ignoreAttackedEffectLogic() || !%s.exceptPowers(p.ID)) {$_ = damageAmount;} else {$_ = $proceed($$);}", PatchDamage.class.getName(), PatchDamage.class.getName()));
//                    }
//                    if (clsName.equals("com.megacrit.cardcrawl.powers.AbstractPower") && methodName.equals("onAttackedToChangeDamage")) {
//                        m.replace(String.format("if (%s.ignoreAttackedEffectLogic() || !%s.exceptPowers(p.ID)) {$_ = damageAmount;} else {$_ = $proceed($$);}", PatchDamage.class.getName(), PatchDamage.class.getName()));
//                    }
//                    if (clsName.equals("com.megacrit.cardcrawl.relics.AbstractRelic") && methodName.equals("onAttack")) {
//                        m.replace(String.format("if (!%s.ignoreAttackedEffectLogic()) {$_ = $proceed($$);}", PatchDamage.class.getName()));
//                    }
//                    if (clsName.equals("com.megacrit.cardcrawl.powers.AbstractPower") && methodName.equals("wasHPLost")) {
//                        m.replace(String.format("if (!(%s.ignoreAttackedEffectLogic() || %s.exceptPowers(p.ID))) {$_ = $proceed($$);}", PatchDamage.class.getName(), PatchDamage.class.getName()));
//                    }
//                    if (clsName.equals("com.megacrit.cardcrawl.powers.AbstractPower") && methodName.equals("onAttack")) {
//                        m.replace(String.format("if (!(%s.ignoreAttackedEffectLogic() || %s.exceptPowers(p.ID))) {$_ = $proceed($$);}", PatchDamage.class.getName(), PatchDamage.class.getName()));
//                    }
//                    if (clsName.equals("com.megacrit.cardcrawl.powers.AbstractPower") && methodName.equals("onAttacked")) {
//                        m.replace(String.format("if (%s.ignoreAttackedEffectLogic() || !%s.exceptPowers(p.ID)) {$_ = damageAmount;} else {$_ = $proceed($$);}", PatchDamage.class.getName(), PatchDamage.class.getName()));
//                    }
//                    if (clsName.equals("com.megacrit.cardcrawl.monsters.AbstractMonster") && methodName.equals("useStaggerAnimation")) {
//                        m.replace("if (!com.megacrit.cardcrawl.dungeons.AbstractDungeon.player.hasPower(\"IceBreaker:ExtraTurnPower\")) {$_ = $proceed($$);}");
//                    }
//                }
//            };
//        }
//
//        public static boolean ignoreAttackedEffectLogic() {
//            return AbstractDungeon.player.hasPower(ExtraTurnPower.POWER_ID) || containsRemote();
//        }
//
//        public static boolean containsRemote() {
//            return AbstractDungeon.actionManager.cardsPlayedThisTurn.get(AbstractDungeon.actionManager.cardsPlayedThisTurn.size() - 1).tags.contains(CardTagEnum.REMOTE);
//        }
//
//        public static boolean exceptPowers(String powerId) {
//            return PatchDamage.whiteList.contains(powerId);
//        }
//    }
//}
