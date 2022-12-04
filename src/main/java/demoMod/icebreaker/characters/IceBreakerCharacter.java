package demoMod.icebreaker.characters;

import basemod.abstracts.CustomPlayer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.events.city.Vampires;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.relics.Circlet;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import demoMod.icebreaker.IceBreaker;
import demoMod.icebreaker.cards.lightlemon.Defend_IceBreaker;
import demoMod.icebreaker.cards.lightlemon.IceShield;
import demoMod.icebreaker.cards.lightlemon.MagicFlame;
import demoMod.icebreaker.cards.lightlemon.Strike_IceBreaker;
import demoMod.icebreaker.enums.AbstractCardEnum;
import demoMod.icebreaker.enums.AbstractPlayerEnum;
import demoMod.icebreaker.relics.StaffBlizzard;

import java.util.ArrayList;

public class IceBreakerCharacter extends CustomPlayer {
    private static final CharacterStrings charStrings;
    public static final String NAME;
    public static final String DESCRIPTION;

    private static final String[] orbTextures = {
            IceBreaker.getResourcePath("char/orb/layer1.png"),
            IceBreaker.getResourcePath("char/orb/layer2.png"),
            IceBreaker.getResourcePath("char/orb/layer3.png"),
            /*
            "scapegoatImages/char/orb/layer4.png",
            "scapegoatImages/char/orb/layer5.png",
            "scapegoatImages/char/orb/layer6.png",
            "scapegoatImages/char/orb/layer1d.png",
            "scapegoatImages/char/orb/layer2d.png",
            "scapegoatImages/char/orb/layer3d.png",
            "scapegoatImages/char/orb/layer4d.png",
            "scapegoatImages/char/orb/layer5d.png"
            */
    };

    public IceBreakerCharacter(String name, PlayerClass setClass) {
        super(name, setClass, orbTextures, IceBreaker.getResourcePath("char/orb/vfx.png"), null, (String) null);
        this.initializeClass(IceBreaker.getResourcePath("char/character.png"), IceBreaker.getResourcePath("char/shoulder2.png"), IceBreaker.getResourcePath("char/shoulder.png"), IceBreaker.getResourcePath("char/corpse.png"), this.getLoadout(), 0.0F, -10F, 258.0F, 282.0F, new EnergyManager(3));
        if (ModHelper.enabledMods.size() > 0 && (ModHelper.isModEnabled("Diverse") || ModHelper.isModEnabled("Chimera") || ModHelper.isModEnabled("Blue Cards"))) {
            this.masterMaxOrbs = 1;
        }
        Pixmap pixmap = new Pixmap(258, 282, Pixmap.Format.RGBA8888);
        pixmap.setColor(IceBreaker.mainIceBreakerColor.cpy());
        pixmap.fill();
        this.img = new Texture(pixmap); //TODO
    }

    @Override
    public ArrayList<String> getStartingDeck() {
        ArrayList<String> ret = new ArrayList<>();
        ret.add(Strike_IceBreaker.ID);
        ret.add(Strike_IceBreaker.ID);
        ret.add(Strike_IceBreaker.ID);
        ret.add(Strike_IceBreaker.ID);
        ret.add(Defend_IceBreaker.ID);
        ret.add(Defend_IceBreaker.ID);
        ret.add(Defend_IceBreaker.ID);
        ret.add(Defend_IceBreaker.ID);
        ret.add(MagicFlame.ID);
        ret.add(IceShield.ID);
        return ret;
    }

    @Override
    public ArrayList<String> getStartingRelics() {
        ArrayList<String> ret = new ArrayList<>();
        ret.add(StaffBlizzard.ID);
        return ret;
    }

    @Override
    public CharSelectInfo getLoadout() {
        return new CharSelectInfo(NAME, DESCRIPTION, 60, 60, 0, 99, 5, this,
                getStartingRelics(), getStartingDeck(), false);
    }

    @Override
    public String getTitle(PlayerClass playerClass) {
        return NAME;
    }

    @Override
    public AbstractCard.CardColor getCardColor() {
        return AbstractCardEnum.ICEBREAKER;
    }

    @Override
    public Color getCardRenderColor() {
        return IceBreaker.mainIceBreakerColor;
    }

    @Override
    public AbstractCard getStartCardForEvent() {
        return new MagicFlame();
    }

    @Override
    public Color getCardTrailColor() {
        return IceBreaker.mainIceBreakerColor.cpy();
    }

    @Override
    public int getAscensionMaxHPLoss() {
        return 6;
    }

    @Override
    public BitmapFont getEnergyNumFont() {
        return FontHelper.energyNumFontGreen;
    }

    @Override
    public void doCharSelectScreenSelectEffect() {
        CardCrawlGame.sound.playA("POWER_TIME_WARP", -0.15F);
        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED, ScreenShake.ShakeDur.SHORT, true);
    }

    @Override
    public String getCustomModeCharacterButtonSoundKey() {
        return "POWER_TIME_WARP";
    }

    @Override
    public String getLocalizedCharacterName() {
        return NAME;
    }

    @Override
    public AbstractPlayer newInstance() {
        return new IceBreakerCharacter("IceBreaker", AbstractPlayerEnum.ICEBREAKER);
    }

    @Override
    public String getSpireHeartText() {
        return charStrings.TEXT[1];
    }

    @Override
    public Color getSlashAttackColor() {
        return Color.FIREBRICK;
    }

    @Override
    public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect() {
        return new AbstractGameAction.AttackEffect[]{
                AbstractGameAction.AttackEffect.BLUNT_LIGHT,
                AbstractGameAction.AttackEffect.BLUNT_LIGHT,
                AbstractGameAction.AttackEffect.BLUNT_LIGHT,
                AbstractGameAction.AttackEffect.BLUNT_HEAVY,
                AbstractGameAction.AttackEffect.BLUNT_HEAVY,
                AbstractGameAction.AttackEffect.BLUNT_HEAVY,
                AbstractGameAction.AttackEffect.BLUNT_HEAVY
        };
    }

    @Override
    public String getVampireText() {
        return Vampires.DESCRIPTIONS[0];
    }

    static {
        charStrings = CardCrawlGame.languagePack.getCharacterString("IceBreaker");
        NAME = charStrings.NAMES[0];
        DESCRIPTION = charStrings.TEXT[0];
    }
}
