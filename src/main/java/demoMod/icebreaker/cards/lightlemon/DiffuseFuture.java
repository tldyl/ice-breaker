package demoMod.icebreaker.cards.lightlemon;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.watcher.PressEndTurnButtonAction;
import com.megacrit.cardcrawl.actions.watcher.SkipEnemiesTurnAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.WhirlwindEffect;
import demoMod.icebreaker.IceBreaker;
import demoMod.icebreaker.powers.ExtraTurnPower;
import demoMod.icebreaker.powers.NextTurnTimeStasisPower;

public class DiffuseFuture extends AbstractLightLemonCard {
    public static final String ID = IceBreaker.makeID("DiffuseFuture");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "cards/AirCut.png";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.NONE;

    private static final int COST = 1;

    public DiffuseFuture() {
        super(ID, NAME, IceBreaker.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET);
        this.exhaust = true;
        this.extraEffectOnExtraTurn = true;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBaseCost(0);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (p.hasPower(ExtraTurnPower.POWER_ID)) {
            addToBot(new VFXAction(new WhirlwindEffect(new Color(1.0F, 0.9F, 0.4F, 1.0F), true)));
            addToBot(new ApplyPowerAction(p, p, new NextTurnTimeStasisPower(p, 12)));
            addToBot(new SkipEnemiesTurnAction());
            addToBot(new PressEndTurnButtonAction());
        } else {
            addToBot(new ApplyPowerAction(p, p, new ExtraTurnPower(p)));
        }
    }
}
