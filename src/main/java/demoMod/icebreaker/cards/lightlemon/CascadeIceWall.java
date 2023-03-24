package demoMod.icebreaker.cards.lightlemon;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
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

<<<<<<<HEAD
=======
        >>>>>>>a5a7631da31541ef7bed03679d522c1106c0be00

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

    public boolean isTemp;

    private static final int COST = 1;

    public CascadeIceWall() {
        super(ID, NAME, IceBreaker.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET);
        this.baseBlock = this.block = 7;
        this.tags = new ArrayList<>();
        this.tags.add(CardTagEnum.MAGIC);
        this.extraEffectOnExtraTurn = true;
        this.isFetter = true;
        this.isTemp = false;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
<<<<<<< HEAD
            this.upgradeBlock(3);
=======
            this.upgradeMagicNumber(1);
            this.upgradeM2(2);
            this.portrait = UPGRADE_IMG;
>>>>>>> a5a7631da31541ef7bed03679d522c1106c0be00
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
        System.out.print("CASSSSSSSSSSSSSCADE");
        if (p.hasPower(ExtraTurnPower.POWER_ID) && !this.isTemp) {
            addToBot(new CascadeIceWallAction(p));
        } else {
            addToBot((AbstractGameAction)new MakeTempCardInDiscardAction(makeStatEquivalentCopy(), 1));
        }
    }
}
