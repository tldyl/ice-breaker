package demoMod.icebreaker.dynamicVariables;

import basemod.abstracts.DynamicVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import demoMod.icebreaker.cards.lightlemon.AbstractLightLemonCard;

public class AnotherMagicNumber extends DynamicVariable {
    @Override
    public String key() {
        return "IM2";
    }

    @Override
    public boolean isModified(AbstractCard card) {
        if (card instanceof AbstractLightLemonCard) {
            return ((AbstractLightLemonCard) card).m2 != ((AbstractLightLemonCard) card).baseM2;
        }
        return false;
    }

    @Override
    public int value(AbstractCard card) {
        if (card instanceof AbstractLightLemonCard) {
            return ((AbstractLightLemonCard) card).m2;
        }
        return 0;
    }

    @Override
    public int baseValue(AbstractCard card) {
        if (card instanceof AbstractLightLemonCard) {
            return ((AbstractLightLemonCard) card).baseM2;
        }
        return 0;
    }

    @Override
    public boolean upgraded(AbstractCard card) {
        if (card instanceof AbstractLightLemonCard) {
            return ((AbstractLightLemonCard) card).isM2Upgraded;
        }
        return false;
    }
}
