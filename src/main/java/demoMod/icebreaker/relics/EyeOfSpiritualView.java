package demoMod.icebreaker.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import demoMod.icebreaker.IceBreaker;
import demoMod.icebreaker.powers.ResonancePower;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.player;

public class EyeOfSpiritualView extends CustomRelic {
    public static final String ID = IceBreaker.makeID("EyeOfSpiritualView");
    private static final Texture IMG = new Texture(IceBreaker.getResourcePath("relics/EyeOfSpiritualView.png"));
    private static final Texture IMG_OUTLINE = new Texture(IceBreaker.getResourcePath("relics/EyeOfSpiritualView_outline.png"));

    public EyeOfSpiritualView() {
        super(ID, IMG, IMG_OUTLINE, RelicTier.UNCOMMON, LandingSound.SOLID);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void atBattleStart() {
        AbstractPlayer p = player;
        this.flash();
        this.addToBot(new RelicAboveCreatureAction(p, this));
        this.addToBot(new ApplyPowerAction(p, p, new ResonancePower(p, 5)));
    }
}
