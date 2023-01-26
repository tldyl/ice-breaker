package demoMod.icebreaker.relics;

import basemod.abstracts.CustomRelic;
import basemod.devcommands.relic.Relic;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.BetterDrawPileToHandAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.defect.SeekAction;
import com.megacrit.cardcrawl.actions.unique.DiscoveryAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import demoMod.icebreaker.IceBreaker;
import demoMod.icebreaker.actions.DiscoverMagicCardAction;
import demoMod.icebreaker.enums.CardTagEnum;
import demoMod.icebreaker.powers.DoubleBlockPower;
import demoMod.icebreaker.powers.TimeStasisPower;
import sun.jvmstat.perfdata.monitor.AbstractPerfDataBuffer;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.*;

public class Telescope extends CustomRelic {
    public static final String ID = IceBreaker.makeID("Telescope");
    private static final Texture IMG = new Texture(IceBreaker.getResourcePath("relics/Telescope.png"));
    private static final Texture IMG_OUTLINE = new Texture(IceBreaker.getResourcePath("relics/Telescope_outline.png"));

    public Telescope() {
        super(ID, IMG, IMG_OUTLINE, RelicTier.BOSS, LandingSound.HEAVY);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void atTurnStart() {
        this.flash();
        this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        this.addToBot(new BetterDrawPileToHandAction(1));
    }
}
