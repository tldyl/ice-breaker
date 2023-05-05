package demoMod.icebreaker;

import basemod.BaseMod;
import basemod.helpers.RelicType;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.*;
import demoMod.icebreaker.cards.lightlemon.*;
import demoMod.icebreaker.cards.lightlemon.tempCards.Spark;
import demoMod.icebreaker.characters.IceBreakerCharacter;
import demoMod.icebreaker.dynamicVariables.AnotherMagicNumber;
import demoMod.icebreaker.effects.VictoryClockEffect;
import demoMod.icebreaker.enums.AbstractCardEnum;
import demoMod.icebreaker.enums.AbstractPlayerEnum;
import demoMod.icebreaker.potions.BottledInspire;
import demoMod.icebreaker.potions.BottledLight;
import demoMod.icebreaker.potions.BottledTime;
import demoMod.icebreaker.powers.ExtraTurnPower;
import demoMod.icebreaker.powers.TimeStasisPower;
import demoMod.icebreaker.relics.*;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SpireInitializer
public class IceBreaker implements EditStringsSubscriber,
                                   EditCardsSubscriber,
                                   EditKeywordsSubscriber,
                                   EditCharactersSubscriber,
                                   EditRelicsSubscriber,
                                   PostInitializeSubscriber,
                                   AddAudioSubscriber,
                                   PostUpdateSubscriber,
                                   PostDungeonInitializeSubscriber {

    public static final String ATTACK_CARD = "512/bg_attack_icebreaker.png";
    public static final String SKILL_CARD = "512/bg_skill_icebreaker.png";
    public static final String POWER_CARD = "512/bg_power_icebreaker.png";
    private static final String ENERGY_ORB = "512/card_icebreaker_orb.png";
    private static final String CARD_ENERGY_ORB = "512/card_small_orb.png";
    private static final String ATTACK_CARD_PORTRAIT = "1024/bg_attack_icebreaker.png";
    private static final String SKILL_CARD_PORTRAIT = "1024/bg_skill_icebreaker.png";
    private static final String POWER_CARD_PORTRAIT = "1024/bg_power_icebreaker.png";
    private static final String ENERGY_ORB_PORTRAIT = "1024/card_icebreaker_orb.png";
    public static Color mainIceBreakerColor = new Color(0.992F, 0.945F, 0.635F, 1.0F);

    private static final List<AbstractGameAction> actionQueue = new ArrayList<>();

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
            //这里加可以在控制台调出来的buff
            powerMap.put(TimeStasisPower.POWER_ID, TimeStasisPower.class);
            powerMap.put(ExtraTurnPower.POWER_ID, ExtraTurnPower.class);
            field.set(null, powerMap);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        VictoryClockEffect.initialize();
        BaseMod.addPotion(BottledInspire.class, Color.PURPLE, Color.PURPLE, null, BottledInspire.ID, AbstractPlayerEnum.ICEBREAKER);
        BaseMod.addPotion(BottledLight.class, Color.YELLOW, Color.ORANGE, null, BottledLight.ID);
        BaseMod.addPotion(BottledTime.class, Color.LIGHT_GRAY, Color.SKY, null, BottledTime.ID);
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
        BaseMod.addCard(new BloodyPath());
        BaseMod.addCard(new RedMoon());
        BaseMod.addCard(new SoulTremor());
        BaseMod.addCard(new GhostGlim());
        BaseMod.addCard(new SilverDoor());
        //BaseMod.addCard(new Blizzard());
        BaseMod.addCard(new WheelOfHeat());
        BaseMod.addCard(new Electrospark());
        BaseMod.addCard(new SeaOfLanterns());
        //BaseMod.addCard(new ThunderBlasting());
        BaseMod.addCard(new Pyroblast());
        BaseMod.addCard(new Flarier());
        //BaseMod.addCard(new IcyBurst());
        BaseMod.addCard(new ManaAgitation());
        BaseMod.addCard(new Snap());
        BaseMod.addCard(new HauntHell());
        BaseMod.addCard(new FreezeKing());
        BaseMod.addCard(new Perception());
        BaseMod.addCard(new FakeParley());
        BaseMod.addCard(new Oracle());
        //BaseMod.addCard(new OverloadEmulate());
        BaseMod.addCard(new EliminateDistractions());
        BaseMod.addCard(new MagicExtraction());
        BaseMod.addCard(new OtherPartyOfMissing());
        BaseMod.addCard(new Leap());
        BaseMod.addCard(new GhostStar());
        BaseMod.addCard(new IceShield());
        BaseMod.addCard(new CascadeIceWall());
        BaseMod.addCard(new SnowWalk());
        BaseMod.addCard(new DeepColdSwamp());
        BaseMod.addCard(new TimeRing());
        BaseMod.addCard(new DistortReality());
        BaseMod.addCard(new TimeLetter());
        BaseMod.addCard(new MaterialDecomposition());
        BaseMod.addCard(new MaterialCreation());
        BaseMod.addCard(new TriggerLightning());
        BaseMod.addCard(new MemoriesFloodBack());
        BaseMod.addCard(new HolyZone());
        BaseMod.addCard(new TimeCreation());
        BaseMod.addCard(new Overgrow());
        BaseMod.addCard(new Negate());
        BaseMod.addCard(new TimeShadow());
        BaseMod.addCard(new TimeLeap());
        BaseMod.addCard(new SpaceOfChaos());
        BaseMod.addCard(new StarDust());
        BaseMod.addCard(new DiffuseFuture());
        BaseMod.addCard(new InfinityFortress());
        BaseMod.addCard(new Niflheimr());
        BaseMod.addCard(new DemonDeLaplace());
        BaseMod.addCard(new DeepCalculate());
        BaseMod.addCard(new Chronover());
        BaseMod.addCard(new ExtraTrigger());
        BaseMod.addCard(new Detonate());
        BaseMod.addCard(new IceDebrisSplashed());
        BaseMod.addCard(new CTL());
        BaseMod.addCard(new OnChronosBehalf());
        BaseMod.addCard(new GhostFantasy());
        BaseMod.addCard(new AllInAsh());
        BaseMod.addCard(new Inspire());
        BaseMod.addCard(new MagicalFlood());
        BaseMod.addCard(new SeeYouTomorrow());
        BaseMod.addCard(new NightOfFireworks());
        BaseMod.addCard(new VitalLoop());
        BaseMod.addCard(new FantasyDream());
        BaseMod.addCard(new HarshTemperament());
        BaseMod.addCard(new AsterismForm());
        BaseMod.addCard(new YesterdayOnceMore());

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
        BaseMod.addRelicToCustomPool(new StaffMidwinter(), AbstractCardEnum.ICEBREAKER);
        BaseMod.addRelicToCustomPool(new MagicalScroll(), AbstractCardEnum.ICEBREAKER);
        BaseMod.addRelic(new Amulet(), RelicType.SHARED);
        BaseMod.addRelic(new Telescope(), RelicType.SHARED);
        BaseMod.addRelicToCustomPool(new ConnectionOfMeteor(), AbstractCardEnum.ICEBREAKER);
        BaseMod.addRelicToCustomPool(new Constellation(), AbstractCardEnum.ICEBREAKER);
        BaseMod.addRelicToCustomPool(new CrystalOfRadiance(), AbstractCardEnum.ICEBREAKER);
        BaseMod.addRelicToCustomPool(new EyeOfSpiritualView(), AbstractCardEnum.ICEBREAKER);
        BaseMod.addRelicToCustomPool(new Letter(), AbstractCardEnum.ICEBREAKER);
    }

    @Override
    public void receiveAddAudio() {
        BaseMod.addAudio("SNAP", "IceAudio/sfx/snap.wav");
        BaseMod.addAudio("EARTH_COLLAPSE", "IceAudio/sfx/earthCollapse.wav");
        BaseMod.addAudio("SOUL_TREMOR", "IceAudio/sfx/soulTremor.wav");
        BaseMod.addAudio("SFX_CARNAGE", "IceAudio/sfx/carnage.wav");
        BaseMod.addAudio("SFX_ICE_SPLASH", "IceAudio/sfx/ice_blast_projectile_spell_01.wav");
    }

    @Override
    public void receivePostUpdate() {
        if (!actionQueue.isEmpty()) {
            actionQueue.get(0).update();
            if (actionQueue.get(0).isDone) {
                actionQueue.remove(0);
            }
        }
    }

    public static void addToBot(AbstractGameAction action) {
        actionQueue.add(action);
    }

    public static void addToTop(AbstractGameAction action) {
        actionQueue.add(0, action);
    }

    @Override
    public void receivePostDungeonInitialize() {
        if (AbstractDungeon.player instanceof IceBreakerCharacter) {
            AbstractDungeon.commonRelicPool.remove(Vajra.ID);
            AbstractDungeon.uncommonRelicPool.remove(Shuriken.ID);
            AbstractDungeon.rareRelicPool.remove(Girya.ID);
            AbstractDungeon.rareRelicPool.remove(DuVuDoll.ID);
            AbstractDungeon.shopRelicPool.remove(Sling.ID);
            AbstractDungeon.commonRelicPool.remove(PenNib.ID);
            AbstractDungeon.commonRelicPool.remove(Akabeko.ID);
        }
    }
}
