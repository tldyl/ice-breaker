package demoMod.icebreaker.cards.lightlemon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.OfferingEffect;
import demoMod.icebreaker.IceBreaker;
import demoMod.icebreaker.cards.lightlemon.tempCards.*;

import java.util.*;
import java.util.stream.Collectors;

public class RainbowViolet extends AbstractLightLemonCard {
    public static final String ID = IceBreaker.makeID("RainbowViolet");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "cards/RainbowViolet.png";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;

    private static final int COST = 0;

    private List<String> cardPool = new ArrayList<>();

    public RainbowViolet() {
        super(ID, NAME, IceBreaker.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET);
        this.baseMagicNumber = this.magicNumber = 7;
        this.exhaust = true;
    }

    @Override
    public void onAddToMasterDeck() {
        super.onAddToMasterDeck();
        cardPool.add(TsukishiRin.ID);
        cardPool.add(Shiroikumo.ID);
        cardPool.add(Fukuhi.ID);
        cardPool.add(Ame.ID);
        cardPool.add(Brandy.ID);
        cardPool.add(Disobey.ID);
    }

    @Override
    public AbstractCard makeStatEquivalentCopy() {
        AbstractCard card = super.makeStatEquivalentCopy();
        if (card instanceof RainbowViolet) {
            ((RainbowViolet) card).cardPool = this.cardPool;
        }
        return card;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.initializeDescription();
            this.isInnate = true;
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (Settings.FAST_MODE) {
            this.addToBot(new VFXAction(new OfferingEffect(), 0.1F));
        } else {
            this.addToBot(new VFXAction(new OfferingEffect(), 0.5F));
        }

        this.addToBot(new LoseHPAction(p, p, this.magicNumber));
        this.addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                p.decreaseMaxHealth(RainbowViolet.this.magicNumber);
                isDone = true;
            }
        });
        this.addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                if (cardPool.isEmpty()) {
                    List<AbstractCard> cards = p.masterDeck.group.stream().filter(card -> card.uuid.equals(RainbowViolet.this.uuid)).collect(Collectors.toList());
                    if (!cards.isEmpty()) {
                        p.masterDeck.removeCard(cards.get(0));
                    }
                    addToTop(new MakeTempCardInHandAction(getVioletCard(TsukishiRitsu.ID)));
                } else {
                    addToTop(new MakeTempCardInHandAction(getVioletCard(cardPool.remove(AbstractDungeon.cardRandomRng.random(0, cardPool.size() - 1)))));
                }
                isDone = true;
            }
        });
    }

    @Override
    public List<String> onSave() {
        List<String> save = new ArrayList<>();
        save.add(Integer.toString(fetterTarget.size()));
        save.addAll(super.onSave());
        save.add(Integer.toString(cardPool.size()));
        save.addAll(cardPool);
        return save;
    }

    @Override
    public void onLoad(List<String> s) {
        cardPool.clear();
        int index = 1;
        int size = Integer.parseInt(s.get(0));
        List<String> fetterTargetSave = new ArrayList<>();
        for (int i=index;i<index + size;i++) {
            fetterTargetSave.add(s.get(i));
        }
        fetterTarget = fetterTargetSave.stream().map(UUID::fromString).collect(Collectors.toList());

        index += size + 1;
        size = Integer.parseInt(s.get(index - 1));
        for (int i=index;i<index + size;i++) {
            cardPool.add(s.get(i));
        }
    }

    private AbstractCard getVioletCard(String id) {
        switch (id) {
            case "IceBreaker:TsukishiRin":
                return new TsukishiRin();
            case "IceBreaker:Shiroikumo":
                return new Shiroikumo();
            case "IceBreaker:Fukuhi":
                return new Fukuhi();
            case "IceBreaker:Ame":
                return new Ame();
            case "IceBreaker:Brandy":
                return new Brandy();
            case "IceBreaker:Disobey":
                return new Disobey();
            case "IceBreaker:TsukishiRitsu":
            default:
                return new TsukishiRitsu();
        }
    }
}
