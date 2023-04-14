package demoMod.icebreaker.cards.lightlemon;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import demoMod.icebreaker.IceBreaker;
import demoMod.icebreaker.powers.MagicalFloodPower;

public class MagicalFlood extends AbstractLightLemonCard {
    public static final String ID = IceBreaker.makeID("MagicalFlood");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "cards/MagicalFlood.png";
    private static final TextureAtlas.AtlasRegion UPGRADE_IMG = new TextureAtlas.AtlasRegion(new Texture(IceBreaker.getResourcePath("cards/MagicalFlood+.png")), 0, 0, 250, 190);

    private static final CardType TYPE = CardType.POWER;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    private static final int COST = 1;

    public MagicalFlood() {
        super(ID, NAME, IceBreaker.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET);
        this.baseMagicNumber = this.magicNumber = 2;
    }

    @Override
    protected void upgradeName() {
        ++this.timesUpgraded;
        this.upgraded = true;
        this.name = cardStrings.EXTENDED_DESCRIPTION[0];
        this.initializeTitle();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBaseCost(0);
            this.portrait = UPGRADE_IMG;
            this.textureImg = IceBreaker.getResourcePath("cards/MagicalFlood+.png");
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new MagicalFloodPower(p, this.magicNumber)));
    }
}
