package demoMod.icebreaker.relics;

import basemod.BaseMod;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.powers.AbstractPower;
import demoMod.icebreaker.IceBreaker;
import demoMod.icebreaker.powers.DoubleBlockPower;
import demoMod.icebreaker.powers.ExtraTurnPower;
import demoMod.icebreaker.powers.TimeStasisPower;

public class StaffBlizzard extends CustomRelic {
    public static final String ID = IceBreaker.makeID("StaffBlizzard");
    private static final Texture IMG = new Texture(IceBreaker.getResourcePath("relics/StaffBlizzard.png"));
    private static final Texture IMG_OUTLINE = new Texture(IceBreaker.getResourcePath("relics/StaffBlizzard_outline.png"));

    public StaffBlizzard() {
        super(ID, IMG, IMG_OUTLINE, RelicTier.STARTER, LandingSound.MAGICAL);
        AbstractPower extraTurnPower = new ExtraTurnPower(AbstractDungeon.player);
        this.tips.add(new PowerTip(TipHelper.capitalize(BaseMod.getKeywordProper("im:extraturn")), BaseMod.getKeywordDescription("im:extraturn"), extraTurnPower.region48));
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void atBattleStart() {
        this.flash();
        AbstractPlayer p = AbstractDungeon.player;
        addToBot(new RelicAboveCreatureAction(p, this));
        addToBot(new ApplyPowerAction(p, p, new DoubleBlockPower(p, 1)));
        addToBot(new ApplyPowerAction(p, p, new TimeStasisPower(p, 4)));
    }
}
