package demoMod.icebreaker.cards.lightlemon;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import demoMod.icebreaker.IceBreaker;
import demoMod.icebreaker.powers.ChronoChimePower;
import demoMod.icebreaker.powers.ExtraTurnPower;
import demoMod.icebreaker.powers.ResonancePower;
import demoMod.icebreaker.powers.TimeStasisPower;

public class FakeParley extends AbstractLightLemonCard {
    public static final String ID = IceBreaker.makeID("FakeParley");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "cards/FakeParley.png";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ALL;

    private static final int COST = 1;

    public FakeParley() {
        super(ID, NAME, IceBreaker.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET);
        this.tags.add(CardTags.HEALING);
        this.baseMagicNumber = this.magicNumber = 2;
        this.isFetter = true;
        this.extraEffectOnExtraTurn = true;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(1);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new TimeStasisPower(p, this.magicNumber)));
        if (p.hasPower(ExtraTurnPower.POWER_ID)) {
            addToBot(new DrawCardAction(this.magicNumber));
        } else {
            addToBot(new ApplyPowerAction(p, p, new ChronoChimePower(p, 1)));
        }
    }
}
