package demoMod.icebreaker.cards.lightlemon;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.DiscardToHandAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import demoMod.icebreaker.IceBreaker;
import demoMod.icebreaker.enums.CardTagEnum;

import java.util.ArrayList;

public class ManaAgitation extends AbstractLightLemonCard {
    public static final String ID = IceBreaker.makeID("ManaAgitation");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "cards/strike_I.png";

    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    private static final int COST = 0;

    public ManaAgitation() {
        super(ID, NAME, IceBreaker.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET);
        this.damage = this.baseDamage = 4;
        this.tags = new ArrayList<>();
        this.tags.add(CardTagEnum.MAGIC);
        this.tags.add(CardTagEnum.REMOTE);
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(2);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
    }

    @Override
    public void onTriggerFetterFailed() {
        if (AbstractDungeon.player.discardPile.contains(this)) {
            addToBot(new DiscardToHandAction(this));
        } else if (AbstractDungeon.player.drawPile.contains(this)) {
            addToBot(new AbstractGameAction() {
                @Override
                public void update() {
                    if (AbstractDungeon.player.drawPile.contains(ManaAgitation.this) && AbstractDungeon.player.hand.size() < BaseMod.MAX_HAND_SIZE) {
                        AbstractDungeon.player.hand.addToHand(ManaAgitation.this);
                        ManaAgitation.this.unhover();
                        ManaAgitation.this.setAngle(0.0F, true);
                        ManaAgitation.this.lighten(false);
                        ManaAgitation.this.drawScale = 0.12F;
                        ManaAgitation.this.targetDrawScale = 0.75F;
                        ManaAgitation.this.applyPowers();
                        AbstractDungeon.player.drawPile.removeCard(ManaAgitation.this);
                    }

                    AbstractDungeon.player.hand.refreshHandLayout();
                    AbstractDungeon.player.hand.glowCheck();

                    isDone = true;
                }
            });
        }
    }
}
