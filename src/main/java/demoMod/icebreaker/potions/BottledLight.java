package demoMod.icebreaker.potions;

import basemod.abstracts.CustomPotion;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import demoMod.icebreaker.IceBreaker;

public class BottledLight extends CustomPotion {
    public static final String ID = IceBreaker.makeID("BottledLight");
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(ID);
    private static final String NAME = potionStrings.NAME;
    private static final PotionRarity RARITY = PotionRarity.UNCOMMON;
    private static final PotionSize POTION_SIZE = PotionSize.BOTTLE;
    private static final PotionColor POTION_COLOR = PotionColor.ENERGY;

    public BottledLight() {
        super(NAME, ID, RARITY, POTION_SIZE, POTION_COLOR);
        this.isThrown = false;
        this.targetRequired = false;
        this.labOutlineColor = IceBreaker.mainIceBreakerColor;
        this.liquidColor = Color.YELLOW.cpy();
    }

    @Override
    public void initializeData() {
        this.potency = this.getPotency();
        this.description = potionStrings.DESCRIPTIONS[0] + this.potency + potionStrings.DESCRIPTIONS[1];

        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
    }

    @Override
    public void use(AbstractCreature target) {
        for (AbstractCard card : AbstractDungeon.player.hand.group) {
            if (card.costForTurn > 0) {
                card.setCostForTurn(card.costForTurn - this.potency);
                card.flash();
            }
        }
    }

    @Override
    public int getPotency(int ascensionLevel) {
        return 1;
    }

    @Override
    public AbstractPotion makeCopy() {
        return new BottledLight();
    }
}
