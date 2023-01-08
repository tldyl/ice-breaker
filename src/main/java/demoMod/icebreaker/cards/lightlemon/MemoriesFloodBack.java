package demoMod.icebreaker.cards.lightlemon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import demoMod.icebreaker.IceBreaker;
import demoMod.icebreaker.patches.MemoriesFloodBackPatch;

public class MemoriesFloodBack extends AbstractLightLemonCard {
    public static final String ID = IceBreaker.makeID("MemoriesFloodBack");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "cards/MemoriesFloodBack.png";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;

    private static final int COST = 0;

    // todo: 等链接卡的copy完成后进行链接的copy测试。(包括本家卡和非本家卡)
    public MemoriesFloodBack() {
        super(ID, NAME, IceBreaker.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET);

        this.isBottom = true;
        this.exhaust = true;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();

            this.selfRetain = true;
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                int count = AbstractDungeon.player.hand.size();
                int i;
                for (i = 0; i < count; i++) {
                    if (Settings.FAST_MODE) {
                        addToBot(new ExhaustAction(1, true, true, false, Settings.ACTION_DUR_XFAST));
                    } else {
                        addToBot(new ExhaustAction(1, true, true));
                    }
                }

                for (AbstractCard c : MemoriesFloodBackPatch.cards) {
                    addToBot(new MakeTempCardInHandAction(c.makeStatEquivalentCopy()));
                }
                this.isDone = true;
            }
        });
    }
}
