package demoMod.icebreaker.cards.lightlemon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import demoMod.icebreaker.IceBreaker;
import demoMod.icebreaker.powers.NextTurnPlayCardPower;

public class ExtraTrigger extends AbstractLightLemonCard {
    public static final String ID = IceBreaker.makeID("ExtraTrigger");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "cards/ExtraTrigger.png";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    private static final int COST = 1;

    public ExtraTrigger() {
        super(ID, NAME, IceBreaker.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET);
        this.baseMagicNumber = this.magicNumber = 2;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBaseCost(0);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new AbstractGameAction() {
            private boolean firstFrame = true;

            @Override
            public void update() {
                if (p.hand.isEmpty() && firstFrame) {
                    this.isDone = true;
                    return;
                }

                if (firstFrame) {
                    AbstractDungeon.handCardSelectScreen.open(ExhaustAction.TEXT[0], ExtraTrigger.this.magicNumber, true, true);
                    AbstractDungeon.player.hand.applyPowers();
                    firstFrame = false;
                    return;
                }

                if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
                    for (AbstractCard card : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                        p.hand.moveToExhaustPile(card);
                        addToBot(new ApplyPowerAction(p, p, new NextTurnPlayCardPower(p, card, 1)));
                    }

                    AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
                    this.isDone = true;
                }
            }
        });
    }
}
