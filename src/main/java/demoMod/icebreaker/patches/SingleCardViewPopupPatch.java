package demoMod.icebreaker.patches;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import demoMod.icebreaker.cards.lightlemon.AbstractLightLemonCard;
import demoMod.icebreaker.enums.CardTagEnum;

public class SingleCardViewPopupPatch {
    @SpirePatch(
            clz = SingleCardViewPopup.class,
            method = "renderCardBack"
    )
    public static class PatchRenderCardBack {
        public static void Postfix(SingleCardViewPopup singleCardViewPopup, SpriteBatch sb) {
            AbstractCard card = ReflectionHacks.getPrivate(singleCardViewPopup, SingleCardViewPopup.class, "card");
            if (card != null) {
                if (card.hasTag(CardTagEnum.MAGIC)) {
                    sb.draw(AbstractLightLemonCard.MAGIC_CARD_BG_TEXTURE_1024, (float) Settings.WIDTH / 2.0F - 512.0F, (float)Settings.HEIGHT / 2.0F - 512.0F, 512.0F, 512.0F, 1024.0F, 1024.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 1024, 1024, false, false);
                }
            }
        }
    }
}
