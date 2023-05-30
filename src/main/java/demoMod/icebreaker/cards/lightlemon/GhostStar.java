package demoMod.icebreaker.cards.lightlemon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import demoMod.icebreaker.IceBreaker;
import demoMod.icebreaker.actions.SelectCardInCardGroupAction;

public class GhostStar extends AbstractLightLemonCard {
    public static final String ID = IceBreaker.makeID("GhostStar");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "cards/GhostStar.png";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    private static final int COST = 1;

    public GhostStar() {
        super(ID, NAME, IceBreaker.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET);
        this.baseMagicNumber = this.magicNumber = 2;
        this.baseBlock = this.block = 8;
        this.isFetter = true;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBlock(4);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (AbstractCard card : p.discardPile.group) {
            if (fetterTarget.contains(card.uuid)) {
                card.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
                card.beginGlowing();
            }
        }
        addToBot(new GainBlockAction(p, p, this.block));
        addToBot(new SelectCardInCardGroupAction(1, card -> true, card -> {
            p.discardPile.removeCard(card);
            p.hand.moveToBottomOfDeck(card);
        }, p.discardPile));
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                for (AbstractCard card : p.drawPile.group) {
                    card.stopGlowing();
                }
                for (AbstractCard card : p.hand.group) {
                    card.stopGlowing();
                }
                for (AbstractCard card : p.discardPile.group) {
                    card.stopGlowing();
                }
                for (AbstractCard card : p.exhaustPile.group) {
                    card.stopGlowing();
                }
                for (AbstractCard card : p.limbo.group) {
                    card.stopGlowing();
                }
                isDone = true;
            }
        });
    }
}
