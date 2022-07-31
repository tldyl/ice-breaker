package demoMod.icebreaker.cards.lightlemon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import demoMod.icebreaker.IceBreaker;
import demoMod.icebreaker.enums.CardTagEnum;
import demoMod.icebreaker.powers.ExtraTurnPower;

import java.util.ArrayList;

public class FreezeKing extends AbstractLightLemonCard {
    public static final String ID = IceBreaker.makeID("FreezeKing");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "cards/strike_I.png";

    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    private static final int COST = 1;

    public FreezeKing() {
        super(ID, NAME, IceBreaker.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET);
        this.damage = this.baseDamage = 6;
        this.block = this.baseBlock = 6;
        this.tags = new ArrayList<>();
        this.tags.add(CardTagEnum.MAGIC);
        this.tags.add(CardTagEnum.REMOTE);
    }

    @Override
    protected void upgradeName() {
        this.timesUpgraded += 1;
        this.upgraded = true;
        this.name = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(3);
            this.upgradeBlock(3);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        addToBot(new GainBlockAction(p, p, this.block));
        if (m.hasPower(WeakPower.POWER_ID) && !this.purgeOnUse && p.hasPower(ExtraTurnPower.POWER_ID)) {
            AbstractCard tmp = this.makeSameInstanceOf();
            AbstractDungeon.player.limbo.addToBottom(tmp);
            tmp.current_x = this.current_x;
            tmp.current_y = this.current_y;
            tmp.target_x = (Settings.WIDTH / 2.0F - 300.0F * Settings.scale);
            tmp.target_y = (Settings.HEIGHT / 2.0F);
            tmp.calculateCardDamage(m);
            tmp.purgeOnUse = true;
            AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(tmp, m, this.energyOnUse, true, true), true);
        }
    }

    @Override
    public void triggerOnGlowCheck() {
        this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        boolean shouldTrigger = false;
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if (!m.isDeadOrEscaped() && m.hasPower(WeakPower.POWER_ID)) {
                shouldTrigger = true;
                break;
            }
        }
        if (AbstractDungeon.player.hasPower(ExtraTurnPower.POWER_ID) && extraEffectOnExtraTurn && shouldTrigger) {
            this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
        }
    }
}
