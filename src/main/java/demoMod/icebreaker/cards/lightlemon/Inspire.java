package demoMod.icebreaker.cards.lightlemon;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import demoMod.icebreaker.IceBreaker;
import demoMod.icebreaker.powers.FreeMagicPower;

public class Inspire extends AbstractLightLemonCard {
    public static final String ID = IceBreaker.makeID("Inspire");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "cards/Inspire.png";
    private static final TextureAtlas.AtlasRegion UPGRADE_IMG = new TextureAtlas.AtlasRegion(new Texture(IceBreaker.getResourcePath("cards/Inspire+.png")), 0, 0, 250, 190);

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    private static final int COST = 2;

    public Inspire() {
        super(ID, NAME, IceBreaker.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET);
        this.baseBlock = 8;
        this.isFetter = true;
        this.extraEffectOnExtraTurn = true;
    }

    @Override
    public void upgradeName() {
        ++this.timesUpgraded;
        this.upgraded = true;
        this.name = cardStrings.EXTENDED_DESCRIPTION[0];
        this.initializeTitle();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBlock(4);
            this.portrait = UPGRADE_IMG;
            this.textureImg = IceBreaker.getResourcePath("cards/Inspire+.png");
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, this.block));
        addToBot(new ApplyPowerAction(p, p, new FreeMagicPower(p, 1)));
    }
}
