package demoMod.icebreaker.cards.lightlemon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import demoMod.icebreaker.IceBreaker;
import demoMod.icebreaker.powers.ResonancePower;
import demoMod.icebreaker.powers.TimeStasisPower;

import java.util.ArrayList;

public class Chronover extends AbstractLightLemonCard {
    public static final String ID = IceBreaker.makeID("Chronover");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "cards/DeepColdSwamp.png";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    private static final int COST = 1;

    public Chronover() {
        super(ID, NAME, IceBreaker.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET);
        this.baseMagicNumber = this.magicNumber = 3;
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
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                CardGroup drawPile = new CardGroup(CardGroup.CardGroupType.DRAW_PILE);
                CardGroup discardPile = new CardGroup(CardGroup.CardGroupType.DISCARD_PILE);
                drawPile.group.addAll(p.drawPile.group);
                discardPile.group.addAll(p.discardPile.group);
                for (AbstractCard card : new ArrayList<>(drawPile.group)) {
                    drawPile.moveToDiscardPile(card);
                    p.drawPile.removeCard(card);
                }
                for (AbstractCard card : new ArrayList<>(discardPile.group)) {
                    discardPile.moveToDeck(card, false);
                    p.discardPile.removeCard(card);
                }
                isDone = true;
            }
        });
        addToBot(new ApplyPowerAction(p, p, new TimeStasisPower(p, this.magicNumber)));
        if (this.upgraded) {
            addToBot(new ApplyPowerAction(p, p, new ResonancePower(p, this.magicNumber)));
        }
    }
}
