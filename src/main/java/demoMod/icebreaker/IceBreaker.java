package demoMod.icebreaker;

import basemod.BaseMod;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.powers.AbstractPower;
import demoMod.icebreaker.cards.lightlemon.*;
import demoMod.icebreaker.cards.lightlemon.tempCards.Spark;
import demoMod.icebreaker.characters.IceBreakerCharacter;
import demoMod.icebreaker.dynamicVariables.AnotherMagicNumber;
import demoMod.icebreaker.enums.AbstractCardEnum;
import demoMod.icebreaker.enums.AbstractPlayerEnum;
import demoMod.icebreaker.powers.ExtraTurnPower;
import demoMod.icebreaker.powers.TimeStasisPower;
import demoMod.icebreaker.relics.StaffBlizzard;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

@SpireInitializer
public class IceBreaker implements EditStringsSubscriber,
                                   EditCardsSubscriber,
                                   EditKeywordsSubscriber,
                                   EditCharactersSubscriber,
                                   EditRelicsSubscriber,
                                   PostInitializeSubscriber,
                                   AddAudioSubscriber {

    private static final String ATTACK_CARD = "512/bg_attack_icebreaker.png";
    private static final String SKILL_CARD = "512/bg_skill_icebreaker.png";
    private static final String POWER_CARD = "512/bg_power_icebreaker.png";
    private static final String ENERGY_ORB = "512/card_icebreaker_orb.png";
    private static final String CARD_ENERGY_ORB = "512/card_small_orb.png";
    private static final String ATTACK_CARD_PORTRAIT = "1024/bg_attack_icebreaker.png";
    private static final String SKILL_CARD_PORTRAIT = "1024/bg_skill_icebreaker.png";
    private static final String POWER_CARD_PORTRAIT = "1024/bg_power_icebreaker.png";
    private static final String ENERGY_ORB_PORTRAIT = "1024/card_icebreaker_orb.png";
    public static Color mainIceBreakerColor = new Color(0.992F, 0.945F, 0.635F, 1.0F);

    public static void initialize() {
        new IceBreaker();
        BaseMod.addColor(AbstractCardEnum.ICEBREAKER,
                mainIceBreakerColor, mainIceBreakerColor, mainIceBreakerColor, mainIceBreakerColor, mainIceBreakerColor, mainIceBreakerColor, mainIceBreakerColor,
                getResourcePath(ATTACK_CARD), getResourcePath(SKILL_CARD),
                getResourcePath(POWER_CARD), getResourcePath(ENERGY_ORB),
                getResourcePath(ATTACK_CARD_PORTRAIT), getResourcePath(SKILL_CARD_PORTRAIT),
                getResourcePath(POWER_CARD_PORTRAIT), getResourcePath(ENERGY_ORB_PORTRAIT), getResourcePath(CARD_ENERGY_ORB));
    }

    public IceBreaker() {
        BaseMod.subscribe(this);
    }

    public static String makeID(String name) {
        return "IceBreaker:" + name;
    }

    public static String getResourcePath(String path) {
        return "IceImages/" + path;
    }

    public static String getLanguageString() {
        String language;
        switch (Settings.language) {
            case ZHS:
                language = "zhs";
                break;
                /*
            case KOR:
                language = "kor";
                break;
            case JPN:
                language = "jpn";
                break;
                */
            default:
                language = "eng";
        }
        return language;
    }

    @Override
    public void receiveEditStrings() {
        String language;
        language = getLanguageString();

        String cardStrings = Gdx.files.internal("localization/" + language + "/IceBreaker-CardStrings.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(CardStrings.class, cardStrings);
        String powerStrings = Gdx.files.internal("localization/" + language + "/IceBreaker-PowerStrings.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(PowerStrings.class, powerStrings);
        String relicStrings = Gdx.files.internal("localization/" + language + "/IceBreaker-RelicStrings.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(RelicStrings.class, relicStrings);
        String uiStrings = Gdx.files.internal("localization/" + language + "/IceBreaker-UIStrings.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(UIStrings.class, uiStrings);
        String charStrings = Gdx.files.internal("localization/" + language + "/IceBreaker-CharacterStrings.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(CharacterStrings.class, charStrings);
        String eventStrings = Gdx.files.internal("localization/" + language + "/IceBreaker-EventStrings.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(EventStrings.class, eventStrings);
        String monsterStrings = Gdx.files.internal("localization/" + language + "/IceBreaker-MonsterStrings.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(MonsterStrings.class, monsterStrings);
        String potionStrings = Gdx.files.internal("localization/" + language + "/IceBreaker-PotionStrings.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(PotionStrings.class, potionStrings);
    }

    @Override
    public void receivePostInitialize() {
        try {
            Field field = BaseMod.class.getDeclaredField("powerMap");
            field.setAccessible(true);
            HashMap<String, Class<? extends AbstractPower>> powerMap = (HashMap) field.get(null);
            //???????????????????????????????????????buff
            powerMap.put(TimeStasisPower.POWER_ID, TimeStasisPower.class);
            powerMap.put(ExtraTurnPower.POWER_ID, ExtraTurnPower.class);
            field.set(null, powerMap);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void receiveEditCards() {
        BaseMod.addDynamicVariable(new AnotherMagicNumber());
        BaseMod.addCard(new Strike_IceBreaker());
        BaseMod.addCard(new Defend_IceBreaker());
        BaseMod.addCard(new MagicFlame());
        BaseMod.addCard(new StaffStrike());
        BaseMod.addCard(new SwingStaff());
        BaseMod.addCard(new FrostWind());
        BaseMod.addCard(new HeavySnow());
        BaseMod.addCard(new Flash());
        BaseMod.addCard(new Thunder());
        BaseMod.addCard(new AirCut());
        BaseMod.addCard(new Light());
        BaseMod.addCard(new EarthCollapse());
        BaseMod.addCard(new SoulTremor());
        BaseMod.addCard(new GhostGlim());
        BaseMod.addCard(new SilverDoor());
        BaseMod.addCard(new WheelOfHeat());
        BaseMod.addCard(new Electrospark());
        BaseMod.addCard(new SeaOfLanterns());
        BaseMod.addCard(new ThunderBlasting());
        BaseMod.addCard(new Pyroblast());
        BaseMod.addCard(new IcyBurst());
        BaseMod.addCard(new ManaAgitation());
        BaseMod.addCard(new Snap());
        BaseMod.addCard(new HauntHell());
        BaseMod.addCard(new FreezeKing());
        BaseMod.addCard(new Perception());
        BaseMod.addCard(new FakeParley());
        BaseMod.addCard(new Oracle());
        BaseMod.addCard(new OverloadEmulate());
        BaseMod.addCard(new EliminateDistractions());
        BaseMod.addCard(new MagicExtraction());
        BaseMod.addCard(new OtherPartyOfMissing());
        BaseMod.addCard(new GhostStar());
        BaseMod.addCard(new IceShield());
        BaseMod.addCard(new CascadeIceWall());
        BaseMod.addCard(new SnowWalk());
        BaseMod.addCard(new CoordinateMovement());
        BaseMod.addCard(new DeepColdSwamp());
        BaseMod.addCard(new Flarier());
        BaseMod.addCard(new DistortReality());
        BaseMod.addCard(new TimeLetter());
        BaseMod.addCard(new MaterialDecomposition());
        BaseMod.addCard(new MaterialCreation());

        BaseMod.addCard(new Spark());
    }

    @Override
    public void receiveEditCharacters() {
        BaseMod.addCharacter(new IceBreakerCharacter("IceBreaker", AbstractPlayerEnum.ICEBREAKER), getResourcePath("charSelect/button.png"), getResourcePath("charSelect/portrait.png"), AbstractPlayerEnum.ICEBREAKER);
    }

    @Override
    public void receiveEditKeywords() {
        final Gson gson = new Gson();
        String language;
        language = getLanguageString();
        final String json = Gdx.files.internal("localization/" + language + "/IceBreaker-KeywordStrings.json").readString(String.valueOf(StandardCharsets.UTF_8));
        com.evacipated.cardcrawl.mod.stslib.Keyword[] keywords = gson.fromJson(json, com.evacipated.cardcrawl.mod.stslib.Keyword[].class);
        if (keywords != null) {
            for (Keyword keyword : keywords) {
                BaseMod.addKeyword("im", keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION);
            }
        }
    }

    @Override
    public void receiveEditRelics() {
        BaseMod.addRelicToCustomPool(new StaffBlizzard(), AbstractCardEnum.ICEBREAKER);
    }

    @Override
    public void receiveAddAudio() {
        BaseMod.addAudio("SNAP", "IceAudio/sfx/snap.wav");
    }
}
