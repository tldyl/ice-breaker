package demoMod.icebreaker.interfaces;

import basemod.interfaces.ISubscriber;
import com.megacrit.cardcrawl.cards.AbstractCard;

public interface ModifyMagicNumberSubscriber extends ISubscriber {
    int onModifyMagicNumber(int magicNumber, AbstractCard card);

    int onModifyAnotherMagicNumber(int magicNumber, AbstractCard card);
}
