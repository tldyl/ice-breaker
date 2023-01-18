package demoMod.icebreaker.cards.lightlemon;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import demoMod.icebreaker.IceBreaker;
import demoMod.icebreaker.enums.CardTagEnum;
import demoMod.icebreaker.interfaces.EnterOrExitExtraTurnSubscriber;
import demoMod.icebreaker.powers.ExtraTurnPower;

import java.util.ArrayList;

public class DeepColdSwamp extends AbstractLightLemonCard implements EnterOrExitExtraTurnSubscriber {
    public static final String ID = IceBreaker.makeID("DeepColdSwamp");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "cards/DeepColdSwamp.png";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    private static final int COST = 1;

    private boolean updateCheck = false;
    private boolean inExtraTurn = false;

    public DeepColdSwamp() {
        super(ID, NAME, IceBreaker.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET);
        this.baseMagicNumber = this.magicNumber = 1;
        this.baseBlock = this.block = 7;
        this.tags = new ArrayList<>();
        this.tags.add(CardTagEnum.MAGIC);
        this.isFetter = true;
        this.extraEffectOnExtraTurn = true;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBlock(3);
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public void update() {
        super.update();
        if (this.upgraded && this.fetterTarget.size() < 2 && !updateCheck &&
                AbstractDungeon.getCurrMapNode() != null &&
                AbstractDungeon.handCardSelectScreen.upgradePreviewCard != this &&
                AbstractDungeon.gridSelectScreen.targetGroup.contains(this) &&
                !AbstractDungeon.cardRewardScreen.rewardGroup.contains(this)) {
            onAddToMasterDeck();
            updateCheck = true;
        }
    }

    @Override
    public void upgradeName() {
        this.timesUpgraded += 1;
        this.upgraded = true;
        this.name = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, this.block));
    }

    @Override
    public void triggerWhenDrawn() {
        if (AbstractDungeon.player.hasPower(ExtraTurnPower.POWER_ID)) {
            this.setCostForTurn(0);
        }
    }

    @Override
    public void onEnterExtraTurn() {
        if (!inExtraTurn) {
            this.setCostForTurn(0);
            inExtraTurn = true;
        }
    }

    @Override
    public void onExitExtraTurn() {
        if (inExtraTurn) {
            this.modifyCostForCombat(this.cost);
            inExtraTurn = false;
        }
    }
}
