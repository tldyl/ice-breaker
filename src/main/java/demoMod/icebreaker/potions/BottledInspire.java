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
import com.megacrit.cardcrawl.relics.SacredBark;
import demoMod.icebreaker.IceBreaker;
import demoMod.icebreaker.actions.DiscoverMagicCardAction;

public class BottledInspire extends CustomPotion {
    public static final String ID = IceBreaker.makeID("BottledInspire");
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(ID);
    private static final String NAME = potionStrings.NAME;
    private static final PotionRarity RARITY = PotionRarity.COMMON;
    private static final PotionSize POTION_SIZE = PotionSize.BOTTLE;
    private static final PotionColor POTION_COLOR = PotionColor.ELIXIR;

    public BottledInspire() {
        super(NAME, ID, RARITY, POTION_SIZE, POTION_COLOR);
        this.isThrown = false;
        this.targetRequired = false;
        this.labOutlineColor = IceBreaker.mainIceBreakerColor;
        this.liquidColor = Color.PURPLE.cpy();
    }

    @Override
    public void initializeData() {
        this.potency = this.getPotency();
        if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(SacredBark.ID)) {
            this.description = potionStrings.DESCRIPTIONS[1];
        } else {
            this.description = potionStrings.DESCRIPTIONS[0];
        }

        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
    }

    @Override
    public void use(AbstractCreature target) {
        addToBot(new DiscoverMagicCardAction(AbstractCard.CardType.SKILL, getPotency()));
    }

    @Override
    public int getPotency(int ascensionLevel) {
        return 1;
    }

    @Override
    public AbstractPotion makeCopy() {
        return new BottledInspire();
    }
}
