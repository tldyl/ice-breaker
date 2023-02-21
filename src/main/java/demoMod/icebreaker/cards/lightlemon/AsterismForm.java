package demoMod.icebreaker.cards.lightlemon;

import basemod.abstracts.CustomSavable;
import basemod.helpers.BaseModCardTags;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import demoMod.icebreaker.IceBreaker;
import demoMod.icebreaker.powers.AsterismFormPower;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class AsterismForm extends AbstractLightLemonCard implements CustomSavable<List<String>> {
    public static final String ID = IceBreaker.makeID("AsterismForm");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "cards/AsterismForm.png";

    private static final CardType TYPE = CardType.POWER;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;

    private static final int COST = 3;

    public static final List<AbstractCard> cardPool = new ArrayList<>();

    public AsterismForm() {
        super(ID, NAME, IceBreaker.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET);
        this.tags.add(BaseModCardTags.FORM);
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (cardPool.isEmpty()) {
            cardPool.addAll(CardLibrary.getAllCards());
        }
        addToBot(new ApplyPowerAction(p, p, new AsterismFormPower(p, 1, this.upgraded)));
    }

    @Override
    public List<String> onSave() {
        List<String> save = new ArrayList<>();
        save.add(Integer.toString(fetterTarget.size()));
        save.addAll(super.onSave());
        save.add(Integer.toString(cardPool.size()));
        save.addAll(cardPool.stream().map(card -> card.cardID).collect(Collectors.toList()));
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
        List<String> cardPoolSave = new ArrayList<>();
        for (int i=index;i<index + size;i++) {
            cardPoolSave.add(s.get(i));
        }
        cardPool.addAll(cardPoolSave.stream().map(CardLibrary::getCard).collect(Collectors.toList()));
    }
}
