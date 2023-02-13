package demoMod.icebreaker.powers;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import demoMod.icebreaker.IceBreaker;
import demoMod.icebreaker.utils.PowerRegionLoader;

import java.util.UUID;

public class NextTurnPlayCardPower extends AbstractPower {
    public static final String POWER_ID = IceBreaker.makeID("NextTurnPlayCardPower");
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESC;
    private final AbstractCard card;

    public NextTurnPlayCardPower(AbstractCreature owner, AbstractCard card, int amount) {
        this.owner = owner;
        this.amount = amount;
        this.name = NAME;
        this.card = card;
        this.ID = POWER_ID + UUID.randomUUID();
        this.updateDescription();
        PowerRegionLoader.load(this, "AppendTrigger");
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESC[0], this.card.name, this.amount);
    }

    @Override
    public void atStartOfTurn() {
        this.flash();
        AbstractCard c = card.makeStatEquivalentCopy();
        c.freeToPlayOnce = true;
        this.addToBot(new MakeTempCardInHandAction(c, this.amount));

//        for (int i=0;i<this.amount;i++) {
//            AbstractMonster m = null;
//            if (this.card.target == AbstractCard.CardTarget.ENEMY) {
//                m = AbstractDungeon.getRandomMonster();
//            }
//
//            AbstractCard tmp = card.makeSameInstanceOf();
//            AbstractDungeon.player.limbo.addToBottom(tmp);
//            tmp.current_x = card.current_x;
//            tmp.current_y = card.current_y;
//            tmp.target_x = (float) Settings.WIDTH / 2.0F - 300.0F * Settings.scale;
//            tmp.target_y = (float)Settings.HEIGHT / 2.0F;
//            if (m != null) {
//                tmp.calculateCardDamage(m);
//            }
//
//            tmp.purgeOnUse = true;
//            AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(tmp, m, card.energyOnUse, true, true), true);
//        }

        addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESC = powerStrings.DESCRIPTIONS;
    }
}
