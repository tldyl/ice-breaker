package demoMod.icebreaker.patches;

import basemod.ReflectionHacks;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import basemod.patches.com.megacrit.cardcrawl.cards.AbstractCard.CardModifierPatches;
import basemod.patches.com.megacrit.cardcrawl.saveAndContinue.SaveFile.ModSaves;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.screens.runHistory.RunHistoryScreen;
import com.megacrit.cardcrawl.screens.stats.RunData;
import demoMod.icebreaker.cards.lightlemon.RainbowViolet;

import java.util.ArrayList;

public class RunHistoryScreenPatch {
    @SpirePatch(
            clz = RunHistoryScreen.class,
            method = "cardForName"
    )
    public static class PatchCardForName {
        public static SpireReturn<AbstractCard> Prefix(RunHistoryScreen screen, RunData runData, String cardID) {
            if (RainbowViolet.ID.equals(cardID)) {
                return SpireReturn.Return(new RainbowViolet());
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz = CardModifierPatches.ApplyCardModsToCards.class,
            method = "applyMods"
    )
    public static class PatchApplyCardModsToCards {
        public static SpireReturn<Void> Prefix(RunData runData, String[] cardID, AbstractCard card) {
            if (cardID[0].equals(RainbowViolet.ID)) {
                card = new RainbowViolet();
                ModSaves.ArrayListOfJsonElement cardmodData = ReflectionHacks.getPrivateStatic(CardModifierPatches.ApplyCardModsToCards.class, "cardmodData");
                ArrayList<AbstractCardModifier> loadedMods = ReflectionHacks.getPrivateStatic(CardModifierPatches.ApplyCardModsToCards.class, "loadedMods");
                if (cardmodData != null) {
                    CardModifierManager.removeAllModifiers(card, true);
                    for (AbstractCardModifier mod : loadedMods) {
                        CardModifierManager.addModifier(card, mod.makeCopy());
                    }
                }
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }
}
