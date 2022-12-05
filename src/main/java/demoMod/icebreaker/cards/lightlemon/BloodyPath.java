package demoMod.icebreaker.cards.lightlemon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.VerticalImpactEffect;
import demoMod.icebreaker.IceBreaker;
import demoMod.icebreaker.enums.CardTagEnum;
import demoMod.icebreaker.interfaces.EnterOrExitExtraTurnSubscriber;
import demoMod.icebreaker.powers.ExtraTurnPower;
import demoMod.icebreaker.powers.ResonancePower;

import java.util.ArrayList;

public class BloodyPath extends AbstractLightLemonCard implements EnterOrExitExtraTurnSubscriber {
    public static final String ID = IceBreaker.makeID("BloodyPath");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "cards/strike_I.png";

    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    private static final int COST = 1;

    public BloodyPath() {
        super(ID, NAME, IceBreaker.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET);
        this.baseDamage = 8;
        this.tags = new ArrayList<>();
        this.tags.add(CardTagEnum.REMOTE);
        this.tags.add(CardTagEnum.MAGIC);
        this.extraEffectOnExtraTurn = true;
        this.isFetter = true;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (!p.hasPower(ExtraTurnPower.POWER_ID)) {
            this.isMultiDamage = false;
            if (m != null) {
                this.addToBot(new VFXAction(new VerticalImpactEffect(m.hb.cX + m.hb.width / 4.0F, m.hb.cY - m.hb.height / 4.0F)));
            }
            this.calculateCardDamage(m);
            addToBot(new DamageAction(p, new DamageInfo(m, this.damage, this.damageTypeForTurn)));
        } else {
            this.isMultiDamage = true;
            for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters) {
                if (!mo.isDeadOrEscaped()) {
                    this.addToBot(new VFXAction(new VerticalImpactEffect(mo.hb.cX + mo.hb.width / 4.0F, mo.hb.cY - mo.hb.height / 4.0F)));
                }
            }
            this.calculateCardDamage(null);
            addToBot(new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.NONE));
        }
    }

    @Override
    public void applyPowers() {
        int realBaseDamage = this.baseDamage;
        int t = 0;
        if (AbstractDungeon.player.hasPower(ResonancePower.POWER_ID)) {
            t = AbstractDungeon.player.getPower(ResonancePower.POWER_ID).amount;
        }
        this.baseDamage += t * (this.upgraded ? 3 : 2);
        super.applyPowers();
        this.baseDamage = realBaseDamage;
        this.isDamageModified = this.damage != this.baseDamage;
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        int t = 0;
        int realBaseDamage = this.baseDamage;
        if (AbstractDungeon.player.hasPower(ResonancePower.POWER_ID)) {
            t = AbstractDungeon.player.getPower(ResonancePower.POWER_ID).amount;
        }
        this.baseDamage += t * (this.upgraded ? 3 : 2);
        super.calculateCardDamage(mo);
        this.baseDamage = realBaseDamage;
        this.isDamageModified = this.damage != this.baseDamage;
    }

    @Override
    public void onEnterExtraTurn() {
        this.target = CardTarget.ALL_ENEMY;
        this.isMultiDamage = true;
    }

    @Override
    public void onExitExtraTurn() {
        this.target = CardTarget.ENEMY;
        this.isMultiDamage = false;
    }
}
