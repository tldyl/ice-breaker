package demoMod.icebreaker.cards.lightlemon;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import demoMod.icebreaker.IceBreaker;
import demoMod.icebreaker.actions.CascadeIceWallAction;
import demoMod.icebreaker.enums.CardTagEnum;
import demoMod.icebreaker.powers.ExtraTurnPower;

import java.util.ArrayList;

public class CascadeIceWall extends AbstractLightLemonCard {
    public static final String ID = IceBreaker.makeID("CascadeIceWall");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "cards/CascadeIceWall.png";
    private static final TextureAtlas.AtlasRegion UPGRADE_IMG = new TextureAtlas.AtlasRegion(new Texture(IceBreaker.getResourcePath("cards/CascadeIceWall+.png")), 0, 0, 250, 190);

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    private static final int COST = 1;

    public CascadeIceWall() {
        super(ID, NAME, IceBreaker.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET);
        this.baseBlock = this.block = 7;
        this.tags = new ArrayList<>();
        this.tags.add(CardTagEnum.MAGIC);
        this.extraEffectOnExtraTurn = true;
        this.isFetter = true;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBlock(3);
            this.portrait = UPGRADE_IMG;
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
        if (p.hasPower(ExtraTurnPower.POWER_ID) && !this.exhaustOnUseOnce) {
            addToBot(new CascadeIceWallAction(p));
        } else if (!this.isInAutoplay) {
            addToBot(new MakeTempCardInDiscardAction(makeSameInstanceOf(), true));
        }
    }
}
