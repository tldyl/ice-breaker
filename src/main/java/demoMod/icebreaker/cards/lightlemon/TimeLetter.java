package demoMod.icebreaker.cards.lightlemon;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.NightmarePower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import demoMod.icebreaker.IceBreaker;
import demoMod.icebreaker.actions.SelectCardInHandAction;
import demoMod.icebreaker.enums.CardTagEnum;

import java.util.ArrayList;

public class TimeLetter extends AbstractLightLemonCard {
    public static final String ID = IceBreaker.makeID("TimeLetter");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "cards/SoulTremor.png";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

    private static final int COST = 1;

    public TimeLetter() {
        super(ID, NAME, IceBreaker.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET);
        this.baseMagicNumber = this.magicNumber = 2;
        this.tags = new ArrayList<>();
        this.tags.add(CardTagEnum.MAGIC);
        this.isFetter = true;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(1);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters) {
            if (!mo.isDeadOrEscaped()) {
                addToBot(new ApplyPowerAction(mo, p, new WeakPower(mo, this.magicNumber, false)));
            }
        }
    }

    @Override
    public void onTriggerFetter() {
        for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters) {
            if (!mo.isDeadOrEscaped()) {
                addToBot(new ApplyPowerAction(mo, AbstractDungeon.player, new VulnerablePower(mo, this.magicNumber, false)));
            }
        }
    }
}
