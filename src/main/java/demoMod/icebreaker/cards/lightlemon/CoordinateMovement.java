package demoMod.icebreaker.cards.lightlemon;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import demoMod.icebreaker.IceBreaker;
import demoMod.icebreaker.enums.CardTagEnum;

import java.util.ArrayList;

public class CoordinateMovement extends AbstractLightLemonCard {
    public static final String ID = IceBreaker.makeID("CoordinateMovement");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "cards/SoulTremor.png";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    private static final int COST = 1;

    public CoordinateMovement() {
        super(ID, NAME, IceBreaker.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET);
        this.baseBlock = this.block = 6;
        this.tags = new ArrayList<>();
        this.tags.add(CardTagEnum.MAGIC);
        this.isFetter = true;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBlock(3);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new GainBlockAction(p, p, this.block));
    }

    @Override
    public void onTriggerFetter() {
        addToBot(new GainEnergyAction(1));
    }
}
