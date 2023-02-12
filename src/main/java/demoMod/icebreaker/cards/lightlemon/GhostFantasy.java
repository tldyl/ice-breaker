package demoMod.icebreaker.cards.lightlemon;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.watcher.MasterRealityPower;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import demoMod.icebreaker.IceBreaker;
import demoMod.icebreaker.cards.lightlemon.tempCards.ChooseOneCardWrapper;
import demoMod.icebreaker.effects.GhostFantasyEffect;

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

    private static final int COST = 1;

    public GhostFantasy() {
        super(ID, NAME, IceBreaker.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET);
        this.baseMagicNumber = this.magicNumber = 4;
        this.tags.add(CardTags.HEALING);
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
        addToBot(new VFXAction(new BorderFlashEffect(new Color(0.5F, 1.0F, 0.8F, 1.0F))));
        addToBot(new VFXAction(new GhostFantasyEffect(p.hb.cX, p.hb.cY), 1.5F));
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
                    cards.add(tmp.makeCopy());
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
                    card.setCostForTurn(0);
                }
                wrappedCards.add(wrappedCard);
            }
            addToBot(new ChooseOneAction(wrappedCards));
        }
    }
}
