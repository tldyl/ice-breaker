package demoMod.icebreaker.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.BetterDrawPileToHandAction;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import demoMod.icebreaker.IceBreaker;

public class Telescope extends CustomRelic {
    public static final String ID = IceBreaker.makeID("Telescope");
    private static final Texture IMG = new Texture(IceBreaker.getResourcePath("relics/Telescope.png"));
    private static final Texture IMG_OUTLINE = new Texture(IceBreaker.getResourcePath("relics/Telescope_outline.png"));

    public Telescope() {
        super(ID, IMG, IMG_OUTLINE, RelicTier.BOSS, LandingSound.HEAVY);
    }

    @Override
    public void onEquip() {
        AbstractDungeon.player.masterHandSize--;
    }

    @Override
    public void onUnequip() {
        AbstractDungeon.player.masterHandSize++;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void atTurnStartPostDraw() {
        this.flash();
        this.addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                if (AbstractDungeon.player.drawPile.isEmpty()) {
                    addToTop(new EmptyDeckShuffleAction());
                }
                isDone = true;
            }
        });
        this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        this.addToBot(new BetterDrawPileToHandAction(1));
    }
}
