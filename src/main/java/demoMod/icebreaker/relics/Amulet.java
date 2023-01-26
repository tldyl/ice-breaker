package demoMod.icebreaker.relics;

import basemod.abstracts.CustomRelic;
import basemod.devcommands.relic.Relic;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
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

public class Amulet extends CustomRelic {
    public static final String ID = IceBreaker.makeID("Amulet");
    private static final Texture IMG = new Texture(IceBreaker.getResourcePath("relics/Amulet.png"));
    private static final Texture IMG_OUTLINE = new Texture(IceBreaker.getResourcePath("relics/Amulet_outline.png"));

    public Amulet() {
        super(ID, IMG, IMG_OUTLINE, RelicTier.RARE, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    public boolean used;

    @Override
    public void atBattleStart() { this.grayscale = this.used = false; }

    @Override
    public void onLoseHp(int damageAmount) {
        if (!this.used && damageAmount > 0) {
            this.flash();
            this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            this.addToBot(new HealAction(AbstractDungeon.player, AbstractDungeon.player, 8));
            this.used = this.grayscale = true;
        }
    }

    @Override
    public void onVictory() { this.grayscale = false; }
}
