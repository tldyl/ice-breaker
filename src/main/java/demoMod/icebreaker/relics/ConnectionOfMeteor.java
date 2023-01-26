package demoMod.icebreaker.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import demoMod.icebreaker.IceBreaker;
import demoMod.icebreaker.cards.lightlemon.AbstractLightLemonCard;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.player;

public class ConnectionOfMeteor extends CustomRelic {
    public static final String ID = IceBreaker.makeID("ConnectionOfMeteor");
    private static final Texture IMG = new Texture(IceBreaker.getResourcePath("relics/ConnectionOfMeteor.png"));
    private static final Texture IMG_OUTLINE = new Texture(IceBreaker.getResourcePath("relics/ConnectionOfMeteor_outline.png"));

    public ConnectionOfMeteor() {
        super(ID, IMG, IMG_OUTLINE, RelicTier.COMMON, LandingSound.MAGICAL);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
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
            if (c instanceof AbstractLightLemonCard && !((AbstractLightLemonCard) c).isFetter) {
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
            AbstractCard card = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
            if (card instanceof AbstractLightLemonCard) {
                AbstractLightLemonCard card1 = (AbstractLightLemonCard)card;
                card1.isFetter = true; card1.fetterAmount = 1;
                card1.onAddToMasterDeck();
            }
            AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
        }
    }
}
