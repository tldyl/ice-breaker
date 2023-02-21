package demoMod.icebreaker.patches;

import basemod.abstracts.CustomSavable;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.saveAndContinue.SaveAndContinue;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import demoMod.icebreaker.cards.lightlemon.AsterismForm;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SaveAndContinuePatch {
    @SpirePatch(
            clz = SaveAndContinue.class,
            method = "save"
    )
    public static class PatchSave {
        public static void Postfix(SaveFile saveFile) {
            File cardUuidSave = new File("saves/ICE_BREAKER.cardUuidSave");
            try {
                if (cardUuidSave.exists()) {
                    cardUuidSave.delete();
                }
                cardUuidSave.createNewFile();
                OutputStream os = new FileOutputStream(cardUuidSave);
                OutputStreamWriter writer = new OutputStreamWriter(os);
                Gson gson = CustomSavable.saveFileGson;
                List<String> uuidList = new ArrayList<>();
                for (AbstractCard card : AbstractDungeon.player.masterDeck.group) {
                    uuidList.add(card.uuid.toString());
                }
                writer.write(gson.toJson(uuidList));
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @SpirePatch(
            clz = SaveAndContinue.class,
            method = "deleteSave"
    )
    public static class PatchDeleteSave {
        public static void Postfix(AbstractPlayer p) {
            File cardUuidSave = new File("saves/ICE_BREAKER.cardUuidSave");
            if (cardUuidSave.exists()) {
                cardUuidSave.delete();
            }
            AsterismForm.cardPool.clear();
        }
    }
}
