package demoMod.icebreaker.cards.lightlemon;

import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.watcher.MasterRealityPower;
import demoMod.icebreaker.IceBreaker;
import demoMod.icebreaker.cards.lightlemon.tempCards.ChooseOneCardWrapper;

import java.util.ArrayList;
import java.util.List;

public class GhostFantasy extends AbstractLightLemonCard {
    public static final String ID = IceBreaker.makeID("GhostFantasy");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "cards/DeepColdSwamp.png";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.NONE;

    private static final int COST = 0;

    public GhostFantasy() {
        super(ID, NAME, IceBreaker.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET);
        this.baseMagicNumber = this.magicNumber = 6;
    }

    @Override
    protected void upgradeName() {
        ++this.timesUpgraded;
        this.upgraded = true;
        this.name = cardStrings.EXTENDED_DESCRIPTION[0];
        this.initializeTitle();
    }

    @Override
    public void triggerOnGlowCheck() {
        this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        if (canUse(AbstractDungeon.player, null)) {
            this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
        }
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
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if (p.hand.size() > 1) this.cantUseMessage = cardStrings.EXTENDED_DESCRIPTION[1];
        return super.canUse(p, m) && p.hand.size() <= 1;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i=0;i<this.magicNumber;i++) {
            List<AbstractCard> cards = new ArrayList<>();
            while (cards.size() < 3) {
                AbstractCard tmp = AbstractDungeon.returnTrulyRandomCardInCombat();
                boolean skip = false;
                for (AbstractCard card : cards) {
                    if (tmp.cardID.equals(card.cardID)) {
                        skip = true;
                        break;
                    }
                }
                if (!skip) {
                    cards.add(tmp);
                }
            }
            ArrayList<AbstractCard> wrappedCards = new ArrayList<>();
            for (AbstractCard card : cards) {
                ChooseOneCardWrapper wrappedCard = new ChooseOneCardWrapper();
                if (this.upgraded || p.hasPower(MasterRealityPower.POWER_ID)) {
                    card.upgrade();
                }
                wrappedCard.setCard(card);
                if (this.upgraded) {
                    card.modifyCostForCombat(-9);
                }
                wrappedCards.add(wrappedCard);
            }
            addToBot(new ChooseOneAction(wrappedCards));
        }
    }
}
