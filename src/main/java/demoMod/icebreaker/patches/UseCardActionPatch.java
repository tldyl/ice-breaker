package demoMod.icebreaker.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

public class UseCardActionPatch {
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
    }
}
