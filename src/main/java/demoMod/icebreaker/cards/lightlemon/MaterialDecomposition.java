package demoMod.icebreaker.cards.lightlemon;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import demoMod.icebreaker.IceBreaker;
import demoMod.icebreaker.interfaces.EnterOrExitExtraTurnSubscriber;
import demoMod.icebreaker.powers.ResonancePower;

public class MaterialDecomposition extends AbstractLightLemonCard implements EnterOrExitExtraTurnSubscriber {
    public static final String ID = IceBreaker.makeID("MaterialDecomposition");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "cards/MaterialDecomposition.png";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    private static final int COST = 0;

    public MaterialDecomposition() {
        super(ID, NAME, IceBreaker.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET);
        this.baseMagicNumber = this.magicNumber = 1;
        this.baseM2 = this.m2 = 2;
        this.exhaust = true;
        this.extraEffectOnExtraTurn = true;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeM2(1);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ExhaustAction(this.magicNumber, false));
        addToBot(new ApplyPowerAction(p, p, new ResonancePower(p, this.m2)));
    }

    @Override
    public void onEnterExtraTurn() {
        this.exhaust = false;
        this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
        this.initializeDescription();
    }

    @Override
    public void onExitExtraTurn() {
        this.exhaust = true;
        this.rawDescription = cardStrings.DESCRIPTION;
        this.initializeDescription();
    }
}
