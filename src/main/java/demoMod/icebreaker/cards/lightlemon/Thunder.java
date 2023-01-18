package demoMod.icebreaker.cards.lightlemon;

import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;
import demoMod.icebreaker.IceBreaker;
import demoMod.icebreaker.actions.PutSpecifiedCardToHandAction;
import demoMod.icebreaker.enums.CardTagEnum;

import java.util.ArrayList;
import java.util.List;

public class Thunder extends AbstractLightLemonCard {
    public static final String ID = IceBreaker.makeID("Thunder");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "cards/Thunder.png";

    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    private static final int COST = 0;

    public Thunder() {
        super(ID, NAME, IceBreaker.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET);
        this.damage = this.baseDamage = 3;
        this.tags = new ArrayList<>();
        this.tags.add(CardTagEnum.MAGIC);
        this.tags.add(CardTagEnum.REMOTE);
        this.isMultiDamage = true;
        this.extraEffectOnExtraTurn = true;
        this.isFetter = true;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(3);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new SFXAction("THUNDERCLAP", 0.05F));
        addToBot(new VFXAction(new LightningEffect(m.drawX, m.drawY), 0.0F));
        addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn)));
    }

    @Override
    public void onOtherCardTriggerFetter(AbstractCard playedCard, List<AbstractCard> fetterCards) {
        AbstractPlayer p = AbstractDungeon.player;
        if (p.drawPile.contains(this)) {
            addToBot(new PutSpecifiedCardToHandAction(1, card -> card == this));
        } else if (p.discardPile.contains(this)) {
            addToBot(new PutSpecifiedCardToHandAction(1, p.discardPile, card -> card == this));
        }
    }
}
