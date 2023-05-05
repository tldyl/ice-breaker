package demoMod.icebreaker.potions;

import basemod.abstracts.CustomPotion;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.watcher.PressEndTurnButtonAction;
import com.megacrit.cardcrawl.actions.watcher.SkipEnemiesTurnAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.vfx.combat.WhirlwindEffect;
import demoMod.icebreaker.IceBreaker;

public class BottledTime extends CustomPotion {
    public static final String ID = IceBreaker.makeID("BottledTime");
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(ID);
    private static final String NAME = potionStrings.NAME;
    private static final PotionRarity RARITY = PotionRarity.RARE;
    private static final PotionSize POTION_SIZE = PotionSize.SPHERE;
    private static final PotionColor POTION_COLOR = PotionColor.ELIXIR;

    public BottledTime() {
        super(NAME, ID, RARITY, POTION_SIZE, POTION_COLOR);
        this.isThrown = false;
        this.targetRequired = false;
        this.labOutlineColor = IceBreaker.mainIceBreakerColor;
        this.liquidColor = Color.YELLOW.cpy();
    }

    @Override
    public void initializeData() {
        this.potency = this.getPotency();
        this.description = potionStrings.DESCRIPTIONS[0];

        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
    }

    @Override
    public void use(AbstractCreature target) {
        addToBot(new VFXAction(new WhirlwindEffect(new Color(1.0F, 0.9F, 0.4F, 1.0F), true)));
        addToBot(new SkipEnemiesTurnAction());
        addToBot(new PressEndTurnButtonAction());
    }

    @Override
    public int getPotency(int ascensionLevel) {
        return 0;
    }

    @Override
    public AbstractPotion makeCopy() {
        return new BottledTime();
    }
}
