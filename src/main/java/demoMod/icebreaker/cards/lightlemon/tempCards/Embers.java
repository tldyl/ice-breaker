package demoMod.icebreaker.cards.lightlemon.tempCards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import demoMod.icebreaker.IceBreaker;

public class Embers extends CustomCard {
    public static final String ID = IceBreaker.makeID("Embers");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "cards/Embers.png";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.NONE;

    private static final int COST = 1;

    public Embers() {
        super(ID, NAME, IceBreaker.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, CardColor.COLORLESS, RARITY, TARGET);
        this.cardsToPreview = new Spark();
        this.selfRetain = true;
        this.baseMagicNumber = this.magicNumber = 0;
        this.exhaust = true;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.initializeDescription();
            this.upgradeMagicNumber(1);
        }
    }

    public void setAmount(int amount) {
        this.baseMagicNumber = this.magicNumber += amount;
        this.rawDescription = cardStrings.EXTENDED_DESCRIPTION[0];
        this.initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new MakeTempCardInHandAction(this.cardsToPreview, this.baseMagicNumber));
        addToBot(new GainEnergyAction(this.baseMagicNumber));
    }

    @Override
    public AbstractCard makeStatEquivalentCopy() {
        AbstractCard ret = super.makeStatEquivalentCopy();
        ret.rawDescription = this.rawDescription;
        ret.initializeDescription();
        return ret;
    }
}
