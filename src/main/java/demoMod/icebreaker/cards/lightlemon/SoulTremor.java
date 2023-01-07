package demoMod.icebreaker.cards.lightlemon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.vfx.combat.DamageNumberEffect;
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect;
import demoMod.icebreaker.IceBreaker;
import demoMod.icebreaker.enums.CardTagEnum;
import demoMod.icebreaker.powers.ExtraTurnPower;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class SoulTremor extends AbstractLightLemonCard {
    public static final String ID = IceBreaker.makeID("SoulTremor");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "cards/SoulTremor.png";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

    private static final int COST = 1;

    public SoulTremor() {
        super(ID, NAME, IceBreaker.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET);
        this.baseMagicNumber = this.magicNumber = 1;
        this.baseM2 = this.m2 = 8;
        this.tags = new ArrayList<>();
        this.tags.add(CardTagEnum.MAGIC);
        this.extraEffectOnExtraTurn = true;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(1);
            this.upgradeM2(4);
            this.selfRetain = true;
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (p.hasPower(ExtraTurnPower.POWER_ID)) {
            CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.HIGH, ScreenShake.ShakeDur.LONG, true);
            addToBot(new SFXAction("SOUL_TREMOR", -0.1F, true));
            for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters.stream().filter(monster -> !monster.isDeadOrEscaped()).collect(Collectors.toList())) {
                addToBot(new AbstractGameAction() {
                    @Override
                    public void update() {
                        mo.maxHealth -= SoulTremor.this.m2;
                        mo.currentHealth -= SoulTremor.this.m2;
                        if (mo.currentHealth < 0) mo.currentHealth = 0;
                        if (mo.maxHealth < 1) mo.maxHealth = 1;
                        AbstractDungeon.effectsQueue.add(new DamageNumberEffect(mo, mo.hb.cX, mo.hb.cY, SoulTremor.this.m2));
                        isDone = true;
                    }
                });
            }
            addToBot(new VFXAction(p, new ShockWaveEffect(this.hb.cX, this.hb.cY, Settings.BLUE_TEXT_COLOR, ShockWaveEffect.ShockWaveType.CHAOTIC), 0.5F));
        }
        for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters) {
            if (!mo.isDeadOrEscaped()) {
                addToBot(new ApplyPowerAction(mo, p, new WeakPower(mo, this.magicNumber, false)));
                addToBot(new ApplyPowerAction(mo, p, new VulnerablePower(mo, this.magicNumber, false)));
            }
        }
    }
}
