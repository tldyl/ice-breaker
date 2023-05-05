package demoMod.icebreaker.powers;

import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import demoMod.icebreaker.IceBreaker;
import demoMod.icebreaker.utils.PowerRegionLoader;

public class FantasyDreamPower extends AbstractPower {
    public static final String POWER_ID = IceBreaker.makeID("FantasyDreamPower");
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESC;

    public FantasyDreamPower(AbstractCreature owner, int amount) {
        this.owner = owner;
        this.ID = POWER_ID;
        this.name = NAME;
        this.amount = amount;
        this.updateDescription();
        PowerRegionLoader.load(this);
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESC[0], this.amount);
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (!cardFromDeck(card) && !action.exhaustCard && card.type != AbstractCard.CardType.POWER) {
            this.flash();
            action.exhaustCard = true;
            card.exhaust = true;
            for (int i=0;i<this.amount;i++) {
                card.use(AbstractDungeon.player, (AbstractMonster) action.target);
            }
        }
    }

    private boolean cardFromDeck(AbstractCard card) {
        for (AbstractCard card1 : AbstractDungeon.player.masterDeck.group) {
            if (card1.uuid.equals(card.uuid)) {
                return true;
            }
        }
        return false;
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESC = powerStrings.DESCRIPTIONS;
    }
}
