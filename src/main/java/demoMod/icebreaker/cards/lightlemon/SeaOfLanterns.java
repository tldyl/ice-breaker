package demoMod.icebreaker.cards.lightlemon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import demoMod.icebreaker.IceBreaker;
import demoMod.icebreaker.cards.lightlemon.tempCards.Spark;
import demoMod.icebreaker.enums.CardTagEnum;
import demoMod.icebreaker.powers.ExtraTurnPower;

import java.util.ArrayList;

public class SeaOfLanterns extends AbstractLightLemonCard {
    public static final String ID = IceBreaker.makeID("SeaOfLanterns");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "cards/SeaOfLanterns.png";

    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

    private static final int COST = 1;

    public SeaOfLanterns() {
        super(ID, NAME, IceBreaker.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET);
        this.damage = this.baseDamage = 8;
        this.baseMagicNumber = this.magicNumber = 3;
        this.isMultiDamage = true;
        this.tags = new ArrayList<>();
        this.tags.add(CardTagEnum.MAGIC);
        this.tags.add(CardTagEnum.REMOTE);
        this.cardsToPreview = new Spark();
        this.extraEffectOnExtraTurn = true;
        this.isFetter = true;
    }

    @Override
    protected void upgradeName() {
        this.timesUpgraded += 1;
        this.upgraded = true;
        this.name = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(4);
            this.upgradeMagicNumber(1);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.FIRE));
        if (p.hasPower(ExtraTurnPower.POWER_ID)) {
            addToBot(new MakeTempCardInDrawPileAction(new Spark(), this.magicNumber, true, true, false));
        }
    }
}
