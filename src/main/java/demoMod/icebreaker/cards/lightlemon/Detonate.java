package demoMod.icebreaker.cards.lightlemon;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import demoMod.icebreaker.IceBreaker;
import demoMod.icebreaker.actions.XCostAction;
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

    private static final int COST = -1;

    public Detonate() {
        super(ID, NAME, IceBreaker.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET);
        this.baseMagicNumber = this.magicNumber = 8;
        this.tags = new ArrayList<>();
        this.tags.add(CardTagEnum.MAGIC);
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(4);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new XCostAction(this, effect -> {
            if (effect > 0) {
                addToTop(new ApplyPowerAction(p, p, new NextTurnDamageAllEnemiesPower(p, effect * Detonate.this.magicNumber)));
            }
        }));
    }
}
