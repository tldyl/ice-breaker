package demoMod.icebreaker.cards.lightlemon;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import demoMod.icebreaker.IceBreaker;

public class SwingStaff extends AbstractLightLemonCard {
    public static final String ID = IceBreaker.makeID("SwingStaff");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "cards/SwingStaff.png";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    private static final int COST = 1;

    public SwingStaff() {
        super(ID, NAME, IceBreaker.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET);
        this.tags.add(CardTags.HEALING);
        this.isFetter = true;
        this.fetterAmount = 3;
        this.baseMagicNumber = this.magicNumber = 3;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(1);
            this.fetterAmount = this.baseMagicNumber;
            if (AbstractDungeon.player!= null && AbstractDungeon.player.masterDeck.contains(this)) {
                this.fetterAmount = 1;
                onAddToMasterDeck();
            }
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

    }
}
