package demoMod.icebreaker.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.IntangiblePower;
import com.megacrit.cardcrawl.powers.InvinciblePower;
import com.megacrit.cardcrawl.powers.ReactivePower;
import com.megacrit.cardcrawl.powers.ShiftingPower;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

import java.util.ArrayList;
import java.util.List;

public class AbstractMonsterPatch {
    @SpirePatch(
            clz = AbstractMonster.class,
            method = "damage"
    )
    public static class PatchDamage {
        public static List<String> whiteList = new ArrayList<>();
        private static String containsRemote = "((com.megacrit.cardcrawl.cards.AbstractCard)com.megacrit.cardcrawl.dungeons.AbstractDungeon.actionManager.cardsPlayedThisTurn.get(com.megacrit.cardcrawl.dungeons.AbstractDungeon.actionManager.cardsPlayedThisTurn.size() - 1)).tags.contains(demoMod.icebreaker.enums.CardTagEnum.REMOTE)";
        private static String exceptPowers = "(!demoMod.icebreaker.patches.AbstractMonsterPatch$PatchDamage.whiteList.contains(p.ID))";

        public static ExprEditor Instrument() {
            return new ExprEditor() {
                public void edit(MethodCall m) throws CannotCompileException {
                    String clsName = m.getClassName();
                    String methodName = m.getMethodName();
                    if (whiteList.isEmpty()) {
                        whiteList.add(IntangiblePower.POWER_ID);
                        whiteList.add(InvinciblePower.POWER_ID);
                        whiteList.add(ShiftingPower.POWER_ID);
                        whiteList.add(ReactivePower.POWER_ID);
                    }
                    if (clsName.equals("com.megacrit.cardcrawl.relics.AbstractRelic") && methodName.equals("onAttackToChangeDamage")) {
                        m.replace("$_ = com.megacrit.cardcrawl.dungeons.AbstractDungeon.player.hasPower(\"IceBreaker:ExtraTurnPower\") || " + containsRemote + " ? damageAmount : $proceed($$);");
                    }
                    if (clsName.equals("com.megacrit.cardcrawl.powers.AbstractPower") && methodName.equals("onAttackToChangeDamage")) {
                        m.replace("if ((com.megacrit.cardcrawl.dungeons.AbstractDungeon.player.hasPower(\"IceBreaker:ExtraTurnPower\") || " + containsRemote + ") && " + exceptPowers + ") {$_ = $proceed($$);} else {$_ = damageAmount;}");
                    }
                    if (clsName.equals("com.megacrit.cardcrawl.powers.AbstractPower") && methodName.equals("onAttackedToChangeDamage")) {
                        m.replace("if ((com.megacrit.cardcrawl.dungeons.AbstractDungeon.player.hasPower(\"IceBreaker:ExtraTurnPower\") || " + containsRemote + ") && " + exceptPowers + ") {$_ = $proceed($$);} else {$_ = damageAmount;}");
                    }
                    if (clsName.equals("com.megacrit.cardcrawl.relics.AbstractRelic") && methodName.equals("onAttack")) {
                        m.replace("if (!com.megacrit.cardcrawl.dungeons.AbstractDungeon.player.hasPower(\"IceBreaker:ExtraTurnPower\") || " + containsRemote + ") {$_ = $proceed($$);}");
                    }
                    if (clsName.equals("com.megacrit.cardcrawl.powers.AbstractPower") && methodName.equals("wasHPLost")) {
                        m.replace("if ((!com.megacrit.cardcrawl.dungeons.AbstractDungeon.player.hasPower(\"IceBreaker:ExtraTurnPower\") || " + containsRemote + ") && " + exceptPowers + ") {$_ = $proceed($$);}");
                    }
                    if (clsName.equals("com.megacrit.cardcrawl.powers.AbstractPower") && methodName.equals("onAttack")) {
                        m.replace("if ((!com.megacrit.cardcrawl.dungeons.AbstractDungeon.player.hasPower(\"IceBreaker:ExtraTurnPower\") || " + containsRemote + ") && " + exceptPowers + ") {$_ = $proceed($$);}");
                    }
                    if (clsName.equals("com.megacrit.cardcrawl.powers.AbstractPower") && methodName.equals("onAttacked")) {
                        m.replace("if ((com.megacrit.cardcrawl.dungeons.AbstractDungeon.player.hasPower(\"IceBreaker:ExtraTurnPower\") || " + containsRemote + ") && " + exceptPowers + ") {$_ = damageAmount;} else {$_ = $proceed($$);}");
                    }
                    if (clsName.equals("com.megacrit.cardcrawl.monsters.AbstractMonster") && methodName.equals("useStaggerAnimation")) {
                        m.replace("if (!com.megacrit.cardcrawl.dungeons.AbstractDungeon.player.hasPower(\"IceBreaker:ExtraTurnPower\")) {$_ = $proceed($$);}");
                    }
                }
            };
        }
    }
}
