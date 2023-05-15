package demoMod.icebreaker.cards.lightlemon;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import demoMod.icebreaker.IceBreaker;
import demoMod.icebreaker.enums.CardTagEnum;
import demoMod.icebreaker.powers.TimeStasisPower;

import java.util.ArrayList;

public class DeepColdSwamp extends AbstractLightLemonCard {
    public static final String ID = IceBreaker.makeID("DeepColdSwamp");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "cards/DeepColdSwamp.png";
    private static final TextureAtlas.AtlasRegion UPGRADE_IMG = new TextureAtlas.AtlasRegion(new Texture(IceBreaker.getResourcePath("cards/DeepColdSwamp+.png")), 0, 0, 250, 190);

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    private static final int COST = 1;

    public DeepColdSwamp() {
        super(ID, NAME, IceBreaker.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET);
        this.baseMagicNumber = this.magicNumber = 1;
        this.baseBlock = this.block = 6;
        this.tags = new ArrayList<>();
        this.tags.add(CardTags.HEALING);
        this.isFetter = true;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBlock(2);
            this.upgradeMagicNumber(1);
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.initializeDescription();
            this.portrait = UPGRADE_IMG;
            this.textureImg = IceBreaker.getResourcePath("cards/DeepColdSwamp+.png");
            this.fetterAmount++;
            if (AbstractDungeon.player != null && AbstractDungeon.player.masterDeck.contains(this)) {
                onAddToMasterDeck();
            }
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
    public void onTriggerFetter() {
        AbstractPlayer p = AbstractDungeon.player;
        calculateCardDamage(null);
        addToBot(new ApplyPowerAction(p, p, new TimeStasisPower(p, this.magicNumber)));
    }
}
