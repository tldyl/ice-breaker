package demoMod.icebreaker.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
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
import demoMod.icebreaker.enums.CardTagEnum;
import demoMod.icebreaker.interfaces.EnterOrExitExtraTurnSubscriber;
import demoMod.icebreaker.utils.PowerRegionLoader;

public class HarshTemperamentPower extends AbstractPower implements EnterOrExitExtraTurnSubscriber {
    public static final String POWER_ID = IceBreaker.makeID("HarshTemperamentPower");
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESC;
    private boolean activated = false;

    public HarshTemperamentPower(AbstractCreature owner, int amount) {
        this.owner = owner;
        this.ID = POWER_ID;
        this.name = NAME;
        this.amount = amount;
        this.updateDescription();
        PowerRegionLoader.load(this, "DeepCalculation");
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESC[0], this.amount);
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.tags.contains(CardTagEnum.MAGIC) && this.activated && !card.purgeOnUse) {
            this.flash();
            for (int i=0;i<this.amount;i++) {
                addToBot(new AbstractGameAction() {
                    @Override
                    public void update() {
                        AbstractMonster m = null;
                        if (card.target == AbstractCard.CardTarget.ENEMY) {
                            m = AbstractDungeon.getRandomMonster();
                        }

                        AbstractCard tmp = card.makeSameInstanceOf();
                        AbstractDungeon.player.limbo.addToBottom(tmp);
                        tmp.current_x = card.current_x;
                        tmp.current_y = card.current_y;
                        tmp.target_x = (float) Settings.WIDTH / 2.0F - 300.0F * Settings.scale;
                        tmp.target_y = (float)Settings.HEIGHT / 2.0F;
                        if (m != null) {
                            tmp.calculateCardDamage(m);
                        }

                        tmp.purgeOnUse = true;
                        AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(tmp, m, card.energyOnUse, true, true), true);
                        isDone = true;
                    }
                });
            }
            this.activated = false;
        }
    }

    @Override
    public void onEnterExtraTurn() {
        this.activated = true;
    }

    @Override
    public void onExitExtraTurn() {
        this.activated = false;
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESC = powerStrings.DESCRIPTIONS;
    }
}
