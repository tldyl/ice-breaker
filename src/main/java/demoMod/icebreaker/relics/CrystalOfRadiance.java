package demoMod.icebreaker.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import demoMod.icebreaker.IceBreaker;
import demoMod.icebreaker.cards.lightlemon.tempCards.Spark;
import demoMod.icebreaker.powers.ExtraTurnPower;
import demoMod.icebreaker.powers.TimeStasisPower;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.player;

public class CrystalOfRadiance extends CustomRelic {
    public static final String ID = IceBreaker.makeID("CrystalOfRadiance");
    private static final Texture IMG = new Texture(IceBreaker.getResourcePath("relics/CrystalOfRadiance.png"));
    private static final Texture IMG_OUTLINE = new Texture(IceBreaker.getResourcePath("relics/CrystalOfRadiance_outline.png"));

    public CrystalOfRadiance() {
        super(ID, IMG, IMG_OUTLINE, RelicTier.RARE, LandingSound.SOLID);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void onUseCard(AbstractCard c, UseCardAction action) {
        AbstractPlayer p = player;
        if (c instanceof Spark && !p.hasPower(ExtraTurnPower.POWER_ID)) {
            this.flash();
            this.addToBot(new ApplyPowerAction(p, p, new TimeStasisPower(p, 1)));
        }
    }
}
