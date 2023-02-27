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
            if (cardID != null && cardID.startsWith(RainbowViolet.ID)) {
                AbstractCard card = new RainbowViolet();
                if (cardID.contains("+")) {
                    String[] tokens = cardID.split("\\+");
                    int upgradeTimes = Integer.parseInt(tokens[1]);
                    for (int i=0;i<upgradeTimes;i++) {
                        card.upgrade();
                    }
                }
                return SpireReturn.Return(card);
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
            if (cardID[0].contains(RainbowViolet.ID)) {
                card = new RainbowViolet();
                if (cardID[0].contains("+")) {
                    String[] tokens = cardID[0].split("\\+");
                    int upgradeTimes = Integer.parseInt(tokens[1]);
                    for (int i=0;i<upgradeTimes;i++) {
                        card.upgrade();
                    }
                }
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
