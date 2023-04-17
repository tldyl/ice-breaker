package demoMod.icebreaker.relics;

import basemod.abstracts.CustomBottleRelic;
import basemod.abstracts.CustomRelic;
import basemod.abstracts.CustomSavable;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import demoMod.icebreaker.IceBreaker;
import demoMod.icebreaker.cards.lightlemon.AbstractLightLemonCard;

import java.util.function.Predicate;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.player;

// 使用basemod的CustomBottleRelic接口，这样在查看牌组时被这个遗物选择的牌右上角会有遗物标志
public class ConnectionOfMeteor extends CustomRelic implements CustomBottleRelic, CustomSavable<Integer> {
    public static final String ID = IceBreaker.makeID("ConnectionOfMeteor");
    private static final Texture IMG = new Texture(IceBreaker.getResourcePath("relics/ConnectionOfMeteor.png"));
    private static final Texture IMG_OUTLINE = new Texture(IceBreaker.getResourcePath("relics/ConnectionOfMeteor_outline.png"));
    private static AbstractCard card;
    public ConnectionOfMeteor() {
        super(ID, IMG, IMG_OUTLINE, RelicTier.COMMON, LandingSound.MAGICAL);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public Predicate<AbstractCard> isOnCard() {
        return c -> c instanceof AbstractLightLemonCard && ((AbstractLightLemonCard) c).ConnectionOfMeteor;
    }

    @Override
    public Integer onSave() {
        if (card != null) {
            return AbstractDungeon.player.masterDeck.group.indexOf(card);
        } else {
            return -1;
        }
    }

    @Override
    public void onLoad(Integer cardIndex) {
        if (cardIndex == null) {
            return;
        }
        if (cardIndex >= 0 && cardIndex < AbstractDungeon.player.masterDeck.group.size()) {
            card = AbstractDungeon.player.masterDeck.group.get(cardIndex);
            if (card instanceof AbstractLightLemonCard) {
                ((AbstractLightLemonCard) card).ConnectionOfMeteor = true;
            }
        }
    }

    private boolean cardSelected = true;

    @Override
    public void onEquip() {
        cardSelected = false;
        if (AbstractDungeon.isScreenUp) {
            AbstractDungeon.dynamicBanner.hide();
            AbstractDungeon.overlayMenu.cancelButton.hide();
            AbstractDungeon.previousScreen = AbstractDungeon.screen;
        }
        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.INCOMPLETE;
        CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AbstractCard c : player.masterDeck.group) {
            if (c instanceof AbstractLightLemonCard &&
                    !((AbstractLightLemonCard) c).isFetter &&
                    !((AbstractLightLemonCard) c).ConnectionOfMeteor) {
                group.addToBottom(c);
            }
        }
        AbstractDungeon.gridSelectScreen.open(group, 1, DESCRIPTIONS[1], false, false, false, false);
    }
    @Override
    public void update() {
        super.update();
        if (!cardSelected && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            cardSelected = true;
            AbstractCard c = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
            if (c instanceof AbstractLightLemonCard) {
                card = c;
                AbstractLightLemonCard card1 = (AbstractLightLemonCard)c;
                card1.ConnectionOfMeteor = true;
                card1.isFetter = true; card1.fetterAmount = 1;
                card1.onAddToMasterDeck();
            }
            AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
        }
    }

    @Override
    public boolean canSpawn() {
        return player.masterDeck.group.stream().anyMatch(card -> card.rarity != AbstractCard.CardRarity.BASIC);
    }
}
