package demoMod.icebreaker.cards.lightlemon;

import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import demoMod.icebreaker.IceBreaker;
import demoMod.icebreaker.characters.IceBreakerCharacter;
import demoMod.icebreaker.interfaces.EnterOrExitExtraTurnSubscriber;

public class OtherPartyOfMissing extends AbstractLightLemonCard implements EnterOrExitExtraTurnSubscriber {
    public static final String ID = IceBreaker.makeID("OtherPartyOfMissing");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "cards/OtherPartyOfMissing.png";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    private static final int COST = 1;

    public OtherPartyOfMissing() {
        super(ID, NAME, IceBreaker.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET);
        this.block = this.baseBlock = 4;
        this.baseMagicNumber = this.magicNumber = 4;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBlock(2);
            this.upgradeMagicNumber(2);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, this.block));
        if (p instanceof IceBreakerCharacter) {
            addToBot(new TalkAction(true, cardStrings.EXTENDED_DESCRIPTION[0], 1.0F, 1.0F));
        }
    }

    @Override
    public void triggerOnEndOfPlayerTurn() {
        super.triggerOnEndOfPlayerTurn();
        this.baseBlock += this.magicNumber;
    }

    @Override
    public void onRetained() {
        triggerOnEndOfPlayerTurn();
    }

    @Override
    public void onEnterExtraTurn() {
        this.baseBlock += this.magicNumber;
    }

    @Override
    public void onExitExtraTurn() {

    }
}
