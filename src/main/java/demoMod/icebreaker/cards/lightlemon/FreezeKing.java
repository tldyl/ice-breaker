package demoMod.icebreaker.cards.lightlemon;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import demoMod.icebreaker.IceBreaker;
import demoMod.icebreaker.actions.SelectCardInCardGroupAction;
import demoMod.icebreaker.enums.CardTagEnum;
import demoMod.icebreaker.powers.ExtraTurnPower;

import java.util.ArrayList;

public class FreezeKing extends AbstractLightLemonCard {
    public static final String ID = IceBreaker.makeID("FreezeKing");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "cards/FreezeKing.png";

    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    private static final int COST = 1;
    private static final TextureAtlas.AtlasRegion UPGRADE_IMG = new TextureAtlas.AtlasRegion(new Texture(IceBreaker.getResourcePath("cards/FreezeKing+.png")), 0, 0, 250, 190);

    public FreezeKing() {
        super(ID, NAME, IceBreaker.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET);
        this.damage = this.baseDamage = 10;
        this.block = this.baseBlock = 10;
        this.baseMagicNumber = this.magicNumber = 2;
        this.tags = new ArrayList<>();
        this.tags.add(CardTagEnum.MAGIC);
        this.tags.add(CardTagEnum.REMOTE);
        this.extraEffectOnExtraTurn = true;
    }

    @Override
    protected void upgradeName() {
        this.timesUpgraded += 1;
        this.upgraded = true;
        this.name = cardStrings.EXTENDED_DESCRIPTION[0];
        this.portrait = UPGRADE_IMG;
        initializeTitle();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(4);
            this.upgradeBlock(4);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        addToBot(new ApplyPowerAction(m, p, new WeakPower(m, this.magicNumber, false)));
        addToBot(new SelectCardInCardGroupAction(this.magicNumber, card -> true, card -> {
            p.discardPile.removeCard(card);
            p.discardPile.moveToDeck(card, true);
        }, p.discardPile));
        AbstractDungeon.overlayMenu.cancelButton.show(AbstractDungeon.overlayMenu.cancelButton.buttonText);
        if (p.hasPower(ExtraTurnPower.POWER_ID)) {
            addToBot(new GainBlockAction(p, p, this.block));
        }
    }
}
