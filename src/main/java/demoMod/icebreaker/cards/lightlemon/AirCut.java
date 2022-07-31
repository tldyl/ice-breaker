package demoMod.icebreaker.cards.lightlemon;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.EnergizedBluePower;
import com.megacrit.cardcrawl.vfx.combat.AnimatedSlashEffect;
import demoMod.icebreaker.IceBreaker;
import demoMod.icebreaker.enums.CardTagEnum;
import demoMod.icebreaker.interfaces.EnterOrExitExtraTurnSubscriber;

import java.util.ArrayList;

public class AirCut extends AbstractLightLemonCard implements EnterOrExitExtraTurnSubscriber {
    public static final String ID = IceBreaker.makeID("AirCut");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "cards/AirCut.png";

    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    private static final int COST = 1;

    public AirCut() {
        super(ID, NAME, IceBreaker.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET);
        this.damage = this.baseDamage = 9;
        this.baseMagicNumber = this.magicNumber = 1;
        this.extraEffectOnExtraTurn = true;
        this.tags = new ArrayList<>();
        this.tags.add(CardTagEnum.MAGIC);
        this.tags.add(CardTagEnum.REMOTE);
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(3);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new SFXAction("ATTACK_WHIFF_2", 0.3F));
        addToBot(new SFXAction("ATTACK_FAST", 0.2F));
        addToBot(new VFXAction(new AnimatedSlashEffect(m.hb.cX, m.hb.cY - 30.0F * Settings.scale, 500.0F, 200.0F, 290.0F, 3.0F, Color.VIOLET, Color.PINK)));
        addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.NONE));
        addToBot(new ApplyPowerAction(p, p, new EnergizedBluePower(p, this.magicNumber)));
    }

    @Override
    public void onEnterExtraTurn() {
        this.modifyCostForCombat(0);
    }

    @Override
    public void onExitExtraTurn() {
        this.modifyCostForCombat(COST);
    }
}
