package demoMod.icebreaker.cards.lightlemon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import demoMod.icebreaker.IceBreaker;
import demoMod.icebreaker.actions.SelectCardInHandAction;
import demoMod.icebreaker.actions.SnapAction;

public class Snap extends AbstractLightLemonCard {
    public static final String ID = IceBreaker.makeID("Snap");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "cards/Snap.png";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;

    private static final int COST = 1;

    public Snap() {
        super(ID, NAME, IceBreaker.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET);
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBaseCost(0);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new SFXAction("SNAP", 0.1F));
//        addToBot(new SelectCardInHandAction(1, card -> card.type == CardType.ATTACK, card -> {
//            Snap.this.tags.addAll(card.tags);
//            card.use(p, m);
//        }));
//        addToBot(new AbstractGameAction() {
//            @Override
//            public void update() {
//                Snap.this.tags.clear();
//                isDone = true;
//            }
//        });

        // vodka: 响指效果变为: 从卡组选1张攻击牌打出。
        addToBot(new SnapAction());
    }
}
