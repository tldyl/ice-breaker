package demoMod.icebreaker.patches;

import basemod.ReflectionHacks;
import basemod.abstracts.CustomCard;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.curses.AscendersBane;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import demoMod.icebreaker.IceBreaker;
import demoMod.icebreaker.cards.lightlemon.AbstractLightLemonCard;
import demoMod.icebreaker.characters.IceBreakerCharacter;
import demoMod.icebreaker.interfaces.ModifyMagicNumberSubscriber;

public class AbstractCardPatch {
    @SpirePatch(
            clz = AbstractCard.class,
            method = "calculateCardDamage"
    )
    public static class PatchCalculateCardDamage {
        public static void Prefix(AbstractCard card, AbstractMonster mo) {
            if (card instanceof AbstractLightLemonCard) {
                AbstractLightLemonCard lightLemonCard = (AbstractLightLemonCard) card;
                lightLemonCard.m2 = lightLemonCard.baseM2;
            }
            card.magicNumber = card.baseMagicNumber;
            for (AbstractPower p : AbstractDungeon.player.powers) {
                if (p instanceof ModifyMagicNumberSubscriber) {
                    ModifyMagicNumberSubscriber subscriber = (ModifyMagicNumberSubscriber) p;
                    card.magicNumber = subscriber.onModifyMagicNumber(card.magicNumber, card);
                    if (card instanceof AbstractLightLemonCard) {
                        AbstractLightLemonCard lightLemonCard = (AbstractLightLemonCard) card;
                        lightLemonCard.m2 = subscriber.onModifyAnotherMagicNumber(lightLemonCard.m2, card);
                    }
                }
            }
            if (card.magicNumber != card.baseMagicNumber) card.isMagicNumberModified = true;
        }
    }

    public static class AscendersBanePatch {
        @SpirePatch(
                clz = AscendersBane.class,
                method = SpirePatch.CONSTRUCTOR
        )
        public static class PatchConstructor {
            public static final String ID = IceBreaker.makeID("AscendersBane");
            public static final String NAME = CardCrawlGame.languagePack.getCardStrings(ID).NAME;
            public static final String IMG_PATH = IceBreaker.getResourcePath("cards/ascendersBane.png");
            public static final Texture IMG = ImageMaster.loadImage(IMG_PATH);

            @SpireInsertPatch(rloc = 1)
            public static void Insert(AscendersBane card) {
                if (AbstractDungeon.player instanceof IceBreakerCharacter) {
                    card.name = NAME;
                    card.assetUrl = IMG_PATH;
                    Texture cardTexture = IMG;
                    cardTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
                    int tw = cardTexture.getWidth();
                    int th = cardTexture.getHeight();
                    TextureAtlas.AtlasRegion cardImg = new TextureAtlas.AtlasRegion(cardTexture, 0, 0, tw, th);
                    ReflectionHacks.setPrivateInherited(card, CustomCard.class, "portrait", cardImg);
                }
            }
        }
    }

    @SpirePatch(
            clz = SingleCardViewPopup.class,
            method = "loadPortraitImg"
    )
    public static class PatchLoadPortraitImg {
        public static SpireReturn<Void> Prefix(SingleCardViewPopup popup) {
            if (AbstractDungeon.player instanceof IceBreakerCharacter) {
                AbstractCard card = ReflectionHacks.getPrivate(popup, SingleCardViewPopup.class, "card");
                if (card.assetUrl != null && !card.assetUrl.equals("status/beta")) {
                    int endingIndex = card.assetUrl.lastIndexOf(".");
                    if (endingIndex == -1) return SpireReturn.Continue();
                    String newPath = card.assetUrl.substring(0, endingIndex) + "_p" + card.assetUrl.substring(endingIndex);
                    Texture portraitImg = ImageMaster.loadImage(newPath);
                    if (portraitImg != null) {
                        ReflectionHacks.setPrivate(popup, SingleCardViewPopup.class, "portraitImg", portraitImg);
                        return SpireReturn.Return(null);
                    }
                }
            }
            return SpireReturn.Continue();
        }
    }
}
