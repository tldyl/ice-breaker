package demoMod.icebreaker.interfaces;

import com.megacrit.cardcrawl.cards.AbstractCard;

import java.util.List;

public interface TriggerFetterSubscriber {
    void onTriggerFetter();

    void onOtherCardTriggerFetter(AbstractCard playedCard, List<AbstractCard> fetterCards);

    void onTriggerFetterFailed();
}
