package demoMod.icebreaker.cards.lightlemon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import demoMod.icebreaker.IceBreaker;
import demoMod.icebreaker.actions.SelectCardInHandAction;
import demoMod.icebreaker.enums.CardTagEnum;
import demoMod.icebreaker.powers.NextTurnDamageAllEnemiesPower;

import java.util.ArrayList;

public class Detonate extends AbstractLightLemonCard {
    public static final String ID = IceBreaker.makeID("Detonate");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "cards/Detonate.png";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    private static final int COST = 1;

    public Detonate() {
        super(ID, NAME, IceBreaker.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET);
        this.baseMagicNumber = this.magicNumber = 4;
        this.tags = new ArrayList<>();
        this.tags.add(CardTagEnum.MAGIC);
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
        if (this.upgraded) {
            addToBot(new SelectCardInHandAction(99, card -> true, card -> {
                p.hand.moveToDiscardPile(card);
                addToTop(new ApplyPowerAction(p, p, new NextTurnDamageAllEnemiesPower(p, this.magicNumber)));
            }, true));
        } else {
            addToBot(new AbstractGameAction() {
                @Override
                public void update() {
                    int theSize = p.hand.size();
                    if (theSize > 0) {
                        this.addToTop(new ApplyPowerAction(p, p, new NextTurnDamageAllEnemiesPower(p, theSize * Detonate.this.magicNumber)));
                        this.addToTop(new DiscardAction(p, p, theSize, false));
                    }
                    this.isDone = true;
                }
            });
        }
    }
}
