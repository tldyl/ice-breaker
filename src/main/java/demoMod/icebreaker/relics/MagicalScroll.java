package demoMod.icebreaker.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import demoMod.icebreaker.IceBreaker;
import demoMod.icebreaker.actions.DiscoverMagicCardAction;

public class MagicalScroll extends CustomRelic {
    public static final String ID = IceBreaker.makeID("MagicalScroll");
    private static final Texture IMG = new Texture(IceBreaker.getResourcePath("relics/MagicalScroll.png"));
    private static final Texture IMG_OUTLINE = new Texture(IceBreaker.getResourcePath("relics/MagicalScroll_outline.png"));

    public MagicalScroll() {
        super(ID, IMG, IMG_OUTLINE, RelicTier.COMMON, LandingSound.MAGICAL);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void atBattleStart() {
        this.flash();
        this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        this.addToBot(new DiscoverMagicCardAction());
    }
}
