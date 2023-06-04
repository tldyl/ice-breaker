package demoMod.icebreaker.cards.lightlemon;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import demoMod.icebreaker.IceBreaker;
import demoMod.icebreaker.actions.PlayCardInCardGroupAction;
import demoMod.icebreaker.cards.lightlemon.tempCards.Spark;

import java.util.stream.Collectors;

public class Light extends AbstractLightLemonCard {
    public static final String ID = IceBreaker.makeID("Light");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "cards/Light.png";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    private static final int COST = 1;

    public Light() {
        super(ID, NAME, IceBreaker.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET);
        this.cardsToPreview = new Spark();
        this.exhaust = true;
        this.selfRetain = true;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.initializeDescription();
            this.exhaust = false;
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (AbstractCard card : p.drawPile.group.stream().filter(card -> card instanceof Spark).collect(Collectors.toList())) {
            addToBot(new PlayCardInCardGroupAction(card, p.drawPile, m, null));
        }
    }
}
