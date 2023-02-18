package demoMod.icebreaker.cards.lightlemon;

import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import demoMod.icebreaker.IceBreaker;
import demoMod.icebreaker.effects.IceShardSplashEffect;
import demoMod.icebreaker.enums.CardTagEnum;
import demoMod.icebreaker.powers.ExtraTurnPower;
import demoMod.icebreaker.powers.ResonancePower;
import demoMod.icebreaker.powers.TimeStasisPower;

import java.util.ArrayList;

public class IceDebrisSplashed extends AbstractLightLemonCard {
    public static final String ID = IceBreaker.makeID("IceDebrisSplashed");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "cards/DeepColdSwamp.png";

    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    private static final int COST = 1;

    public IceDebrisSplashed() {
        super(ID, NAME, IceBreaker.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET);
        this.baseDamage = this.damage = 0;
        this.baseMagicNumber = this.magicNumber = 2;
        this.tags = new ArrayList<>();
        this.tags.add(CardTagEnum.MAGIC);
        this.tags.add(CardTagEnum.REMOTE);
        this.isFetter = true;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(1);
        }
    }

    @Override
    public void onMoveToDiscard() {
        this.rawDescription = cardStrings.DESCRIPTION;
        this.initializeDescription();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        AbstractPlayer p = AbstractDungeon.player;
        if (p.hasPower(ResonancePower.POWER_ID) && p.hasPower(ExtraTurnPower.POWER_ID)) {
            this.baseDamage -= p.getPower(ResonancePower.POWER_ID).amount;
        }
        super.calculateCardDamage(mo);
        if (p.hasPower(ResonancePower.POWER_ID) && p.hasPower(ExtraTurnPower.POWER_ID)) {
            this.baseDamage += p.getPower(ResonancePower.POWER_ID).amount;
        }
        this.rawDescription = cardStrings.DESCRIPTION;
        this.rawDescription = this.rawDescription + cardStrings.EXTENDED_DESCRIPTION[0];
        this.initializeDescription();
    }

    @Override
    public void applyPowers() {
        AbstractPlayer p = AbstractDungeon.player;
        if (p.hasPower(TimeStasisPower.POWER_ID)) {
            AbstractPower power = p.getPower(TimeStasisPower.POWER_ID);
            this.baseDamage = power.amount;
        } else {
            this.baseDamage = 0;
        }
        if (p.hasPower(ResonancePower.POWER_ID) && p.hasPower(ExtraTurnPower.POWER_ID)) {
            this.baseDamage -= p.getPower(ResonancePower.POWER_ID).amount;
        }
        super.applyPowers();
        if (p.hasPower(ResonancePower.POWER_ID) && p.hasPower(ExtraTurnPower.POWER_ID)) {
            this.baseDamage += p.getPower(ResonancePower.POWER_ID).amount;
        }
        this.rawDescription = cardStrings.DESCRIPTION;
        this.rawDescription = this.rawDescription + cardStrings.EXTENDED_DESCRIPTION[0];
        this.initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (p.hasPower(TimeStasisPower.POWER_ID)) {
            AbstractPower power = p.getPower(TimeStasisPower.POWER_ID);
            this.baseDamage = power.amount;
        } else {
            this.baseDamage = 0;
        }
        calculateCardDamage(m);
        for (int i=0;i<this.magicNumber;i++) {
            VFXAction vfxAction = new VFXAction(new IceShardSplashEffect(AbstractDungeon.getMonsters().shouldFlipVfx()), 0.0F);
            vfxAction.actionType = null;
            addToBot(vfxAction);
            addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn)));
        }
    }
}
