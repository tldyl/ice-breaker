package demoMod.icebreaker.cards.lightlemon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import demoMod.icebreaker.IceBreaker;
import demoMod.icebreaker.actions.PutSpecifiedCardToHandAction;
import demoMod.icebreaker.enums.CardTagEnum;

import java.util.ArrayList;

public class ManaAgitation extends AbstractLightLemonCard {
    public static final String ID = IceBreaker.makeID("ManaAgitation");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "cards/ManaAgitation.png";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    private static final int COST = 0;

    public ManaAgitation() {
        super(ID, NAME, IceBreaker.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = 3;
        this.tags = new ArrayList<>();
        this.tags.add(CardTagEnum.MAGIC);
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(1);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int magicsInDrawPile = (int) p.drawPile.group.stream().filter(card -> card.hasTag(CardTagEnum.MAGIC)).count();
        addToBot(new PutSpecifiedCardToHandAction(this.magicNumber, card -> card.hasTag(CardTagEnum.MAGIC)));
        if (magicsInDrawPile < this.magicNumber) {
            addToBot(new PutSpecifiedCardToHandAction(this.magicNumber - magicsInDrawPile, p.discardPile, card -> card.hasTag(CardTagEnum.MAGIC)));
        }
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                addToBot(new DamageAction(p, new DamageInfo(p, p.hand.size(), ManaAgitation.this.damageTypeForTurn), AbstractGameAction.AttackEffect.FIRE));
                isDone = true;
            }
        });
    }
}
