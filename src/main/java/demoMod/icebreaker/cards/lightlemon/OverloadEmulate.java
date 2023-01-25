package demoMod.icebreaker.cards.lightlemon;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import demoMod.icebreaker.IceBreaker;
import demoMod.icebreaker.powers.ResonancePower;

@Deprecated
public class OverloadEmulate extends AbstractLightLemonCard {
    public static final String ID = IceBreaker.makeID("OverloadEmulate");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "cards/OverloadEmulate.png";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;

    private static final int COST = 1;

    public OverloadEmulate() {
        super(ID, NAME, IceBreaker.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET);
        this.exhaust = true;
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
        int powerAmount;
        if (p.hasPower(ResonancePower.POWER_ID)) {
            powerAmount = p.getPower(ResonancePower.POWER_ID).amount;
            if (!this.upgraded) {
                addToBot(new ApplyPowerAction(p, p, new ResonancePower(p, powerAmount)));
            } else {
                addToBot(new ApplyPowerAction(p, p, new ResonancePower(p, powerAmount * 2)));
            }
        }
    }
}
