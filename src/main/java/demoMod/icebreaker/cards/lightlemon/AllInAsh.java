package demoMod.icebreaker.cards.lightlemon;

import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import demoMod.icebreaker.IceBreaker;
import demoMod.icebreaker.actions.XCostAction;
import demoMod.icebreaker.cards.lightlemon.tempCards.Embers;

public class AllInAsh extends AbstractLightLemonCard {
    public static final String ID = IceBreaker.makeID("AllInAsh");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "cards/AllInAsh.png";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;

    private static final int COST = -1;

    public AllInAsh() {
        super(ID, NAME, IceBreaker.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET);
        this.cardsToPreview = new Embers();
        this.exhaust = true;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.initializeDescription();
            this.cardsToPreview.upgrade();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new XCostAction(this, effect -> {
            Embers embers = new Embers();
            if (this.upgraded) {
                embers.upgrade();
            }
            embers.setAmount(effect);
            addToTop(new MakeTempCardInHandAction(embers));

            for(int i = 0; i < p.hand.size(); i++) {
                if (Settings.FAST_MODE) {
                    this.addToTop(new ExhaustAction(1, true, true, false, Settings.ACTION_DUR_XFAST));
                } else {
                    this.addToTop(new ExhaustAction(1, true, true));
                }
            }
        }));
    }
}
