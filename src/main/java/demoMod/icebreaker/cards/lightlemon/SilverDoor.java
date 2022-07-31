package demoMod.icebreaker.cards.lightlemon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import demoMod.icebreaker.IceBreaker;
import demoMod.icebreaker.enums.CardTagEnum;
import demoMod.icebreaker.powers.DodgePower;
import demoMod.icebreaker.powers.ExtraTurnPower;

import java.util.ArrayList;

public class SilverDoor extends AbstractLightLemonCard {
    public static final String ID = IceBreaker.makeID("SilverDoor");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "cards/strike_I.png";

    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    private static final int COST = 1;

    public SilverDoor() {
        super(ID, NAME, IceBreaker.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET);
        this.damage = this.baseDamage = 10;
        this.magicNumber = this.baseMagicNumber = 1;
        this.tags = new ArrayList<>();
        this.tags.add(CardTagEnum.MAGIC);
        this.tags.add(CardTagEnum.REMOTE);
        this.extraEffectOnExtraTurn = true;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(4);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (p.hasPower(ExtraTurnPower.POWER_ID)) {
            addToBot(new ApplyPowerAction(p, p, new DodgePower(p, this.magicNumber)));
        }
        addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
    }
}
