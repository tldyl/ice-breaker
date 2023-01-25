package demoMod.icebreaker.cards.lightlemon.tempCards;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import demoMod.icebreaker.IceBreaker;
import demoMod.icebreaker.cards.lightlemon.AbstractLightLemonCard;

public class ChooseOneCardWrapper extends AbstractLightLemonCard {
    public static final String ID = IceBreaker.makeID("ChooseOneCardWrapper");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "cards/DeepColdSwamp.png";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.NONE;

    private static final int COST = 0;

    private AbstractCard card = null;

    public ChooseOneCardWrapper() {
        super(ID, NAME, IceBreaker.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET);
        this.upgraded = true;
    }

    public void setCard(AbstractCard card) {
        this.card = card;
        this.name = card.name;
        this.upgraded = card.upgraded;
        this.baseDamage = card.baseDamage;
        this.baseBlock = card.baseBlock;
        this.baseMagicNumber = this.magicNumber = card.magicNumber;
        if (card instanceof AbstractLightLemonCard) {
            this.baseM2 = this.m2 = ((AbstractLightLemonCard) card).m2;
        }
        this.cost = card.cost;
        this.type = card.type;
        this.portrait = card.portrait;
        this.rawDescription = card.rawDescription;
        this.rarity = card.rarity;
        this.initializeTitle();
        this.initializeDescription();
    }

    @Override
    public void onChoseThisOption() {
        if (this.card != null) {
            addToTop(new MakeTempCardInHandAction(this.card, true, true));
        }
    }

    @Override
    public void upgrade() {

    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

    }
}
