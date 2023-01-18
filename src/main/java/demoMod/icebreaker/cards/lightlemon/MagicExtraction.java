package demoMod.icebreaker.cards.lightlemon;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import demoMod.icebreaker.IceBreaker;

public class MagicExtraction extends AbstractLightLemonCard {
    public static final String ID = IceBreaker.makeID("MagicExtraction");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "cards/MagicExtraction.png";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    private static final int COST = 0;

    public MagicExtraction() {
        super(ID, NAME, IceBreaker.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET);
        this.baseMagicNumber = this.magicNumber = 2;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(1);
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m != null) {
            if (m.hasPower(WeakPower.POWER_ID)) {
                AbstractPower power = m.getPower(WeakPower.POWER_ID);
                if (power.amount <= this.magicNumber) {
                    addToBot(new GainEnergyAction(power.amount));
                    addToBot(new RemoveSpecificPowerAction(m, p, power));
                } else {
                    addToBot(new GainEnergyAction(this.magicNumber));
                    addToBot(new ReducePowerAction(m, p, power, this.magicNumber));
                }
            }
        }
    }
}
