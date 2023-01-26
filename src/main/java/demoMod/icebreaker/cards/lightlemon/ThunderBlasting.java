package demoMod.icebreaker.cards.lightlemon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;
import demoMod.icebreaker.IceBreaker;
import demoMod.icebreaker.enums.CardTagEnum;
import demoMod.icebreaker.powers.ExtraTurnPower;

import java.util.ArrayList;

@Deprecated
public class ThunderBlasting extends AbstractLightLemonCard {
    public static final String ID = IceBreaker.makeID("ThunderBlasting");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "cards/strike_I.png";

    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    private static final int COST = 1;

    public ThunderBlasting() {
        super(ID, NAME, IceBreaker.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET);
        this.damage = this.baseDamage = 6;
        this.baseMagicNumber = this.magicNumber = 3;
        this.tags = new ArrayList<>();
        this.tags.add(CardTagEnum.MAGIC);
        this.tags.add(CardTagEnum.REMOTE);
        this.isFetter = true;
        this.extraEffectOnExtraTurn = true;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(3);
            this.upgradeMagicNumber(1);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (p.hasPower(ExtraTurnPower.POWER_ID)) {
            addToBot(new ApplyPowerAction(m, p, new VulnerablePower(m, this.magicNumber, false)));
        }
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                int amount;
                if (m.hasPower(VulnerablePower.POWER_ID)) {
                    amount = m.getPower(VulnerablePower.POWER_ID).amount;
                    addToBot(new RemoveSpecificPowerAction(m, p, VulnerablePower.POWER_ID));
                    addToBot(new AbstractGameAction() {
                        @Override
                        public void update() {
                            applyPowers();
                            isDone = true;
                        }
                    });
                    for (int i=0;i<amount;i++) {
                        addToBot(new SFXAction("THUNDERCLAP", 0.05F));
                        addToBot(new VFXAction(new LightningEffect(m.drawX, m.drawY), 0.05F));
                        addToBot(new DamageAction(m, new DamageInfo(p, ThunderBlasting.this.damage, ThunderBlasting.this.damageTypeForTurn), AbstractGameAction.AttackEffect.NONE));
                    }
                }
                isDone = true;
            }
        });

    }
}
