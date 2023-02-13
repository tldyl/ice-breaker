package demoMod.icebreaker.cards.lightlemon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MinionPower;
import com.megacrit.cardcrawl.vfx.combat.CleaveEffect;
import demoMod.icebreaker.IceBreaker;
import demoMod.icebreaker.enums.CardTagEnum;
import demoMod.icebreaker.powers.ExtraTurnPower;

import java.util.ArrayList;
import java.util.List;

public class OnChronosBehalf extends AbstractLightLemonCard {
    public static final String ID = IceBreaker.makeID("OnChronosBehalf");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "cards/DeepColdSwamp.png";

    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

    private static final int COST = 1;

    public OnChronosBehalf() {
        super(ID, NAME, IceBreaker.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET);
        this.tags = new ArrayList<>();
        this.tags.add(CardTagEnum.MAGIC);
        this.tags.add(CardTagEnum.REMOTE);
        this.tags.add(CardTags.HEALING);
        this.baseDamage = 10;
        this.isMultiDamage = true;
        this.extraEffectOnExtraTurn = true;
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
        calculateCardDamage(null);
        final List<AbstractMonster> aliveMonsters = new ArrayList<>();
        for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters) {
            if (!mo.isDeadOrEscaped() && !mo.hasPower(MinionPower.POWER_ID)) {
                aliveMonsters.add(mo);
            }
        }
        AbstractGameAction action = new AbstractGameAction() {
            @Override
            public void update() {
                int cnt = 0;
                for (AbstractMonster mo : aliveMonsters) {
                    if (mo.isDeadOrEscaped()) ++cnt;
                }

                // 找到主牌组中的对应牌
                AbstractLightLemonCard card = null;
                for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
                    if (c.uuid.equals(OnChronosBehalf.this.uuid)) {
                        card = (AbstractLightLemonCard)c; break;
                    }
                }
                if (cnt > 0 && card != null) {
                    card.isFetter = true;
                    card.fetterAmount = cnt;
                    card.onAddToMasterDeck();
                }
                isDone = true;
            }
        };
        action.actionType = AbstractGameAction.ActionType.DAMAGE;
        addToTop(action);
        addToTop(new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.NONE));
        addToTop(new VFXAction(p, new CleaveEffect(), 0.1F));
        addToTop(new SFXAction("ATTACK_HEAVY"));
    }
}
