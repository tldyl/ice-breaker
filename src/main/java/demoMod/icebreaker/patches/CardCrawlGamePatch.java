package demoMod.icebreaker.patches;

import basemod.abstracts.CustomSavable;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import demoMod.icebreaker.cards.lightlemon.AbstractLightLemonCard;

import java.io.*;
import java.util.List;
import java.util.UUID;

public class CardCrawlGamePatch {
    @SpirePatch(
            clz = CardCrawlGame.class,
            method = "loadPlayerSave",
            paramtypez = {
                    AbstractPlayer.class
            }
    )
    public static class PatchLoadPlayerSave {
        public static void Postfix(CardCrawlGame cardCrawlGame, AbstractPlayer p) {
            File cardUuidSave = new File("saves/ICE_BREAKER.cardUuidSave");
            if (cardUuidSave.exists()) {
                try {
                    Reader reader = new FileReader(cardUuidSave);
                    List<String> uuidList = CustomSavable.saveFileGson.fromJson(reader, new TypeToken<List<String>>(){}.getType());
                    if (uuidList == null) return;
                    int index = 0;
                    for (AbstractCard card : p.masterDeck.group) {
                        if (index < uuidList.size()) {
                            card.uuid = UUID.fromString(uuidList.get(index));
                            index++;
                        }
                    }

                    // MODIFIED BY AKDREAM10086
                    for (AbstractCard card : p.masterDeck.group) {
                        if (card instanceof AbstractLightLemonCard) {
                            ((AbstractLightLemonCard) card).loadCardsToPreview();
                        }
                    }
                    // load cardsToPreview after uuid is loaded so that it's correctly loaded

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
