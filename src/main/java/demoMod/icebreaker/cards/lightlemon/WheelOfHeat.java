package demoMod.icebreaker.cards.lightlemon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.FireballEffect;
import com.megacrit.cardcrawl.vfx.combat.FlameBarrierEffect;
import demoMod.icebreaker.IceBreaker;
import demoMod.icebreaker.cards.lightlemon.tempCards.Spark;
import demoMod.icebreaker.enums.CardTagEnum;

import java.util.ArrayList;

public class WheelOfHeat extends AbstractLightLemonCard {
    public static final String ID = IceBreaker.makeID("WheelOfHeat");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "cards/WheelOfHeat.png";

    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

    private static final int COST = 1;

    public WheelOfHeat() {
        super(ID, NAME, IceBreaker.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET);
        this.damage = this.baseDamage = 5;
        this.tags = new ArrayList<>();
        this.tags.add(CardTagEnum.MAGIC);
        this.tags.add(CardTagEnum.REMOTE);
        this.cardsToPreview = new Spark();
    }

    private int getSparksPlayedThisTurn() {
        int sparksPlayedThisTurn = 0;
        for (AbstractCard card : AbstractDungeon.actionManager.cardsPlayedThisTurn) {
            if (card instanceof Spark) {
                sparksPlayedThisTurn++;
            }
        }
        return sparksPlayedThisTurn;
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
        int sparksPlayedThisTurn = getSparksPlayedThisTurn();
        if (sparksPlayedThisTurn > 0) {
            addToBot(new VFXAction(p, new FlameBarrierEffect(p.hb.cX, p.hb.cY), 0.3F));
            for (int i=0;i<sparksPlayedThisTurn;i++) {
                addToBot(new AbstractGameAction() {
                    @Override
                    public void update() {
                        AbstractMonster mo = AbstractDungeon.getRandomMonster();
                        if (mo != null) {
                            calculateCardDamage(mo);
                            AbstractGameEffect effect = new FireballEffect(p.hb.cX, p.hb.cY, mo.hb.cX, mo.hb.cY);
                            effect.duration = 0.5F;
                            effect.startingDuration = 0.5F;
                            addToTop(new DamageAction(mo, new DamageInfo(p, damage, damageTypeForTurn), AttackEffect.FIRE));
                            addToTop(new SFXAction("GHOST_ORB_IGNITE_1", 0.3F));
                            addToTop(new VFXAction(p, effect, 0.1F));
                        }
                        isDone = true;
                    }
                });
            }
        }
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        this.rawDescription = cardStrings.DESCRIPTION;
        this.rawDescription += cardStrings.EXTENDED_DESCRIPTION[0];
        this.rawDescription = String.format(this.rawDescription, getSparksPlayedThisTurn());
        initializeDescription();
    }

    @Override
    public void onMoveToDiscard() {
        this.rawDescription = cardStrings.DESCRIPTION;
        initializeDescription();
    }
}
