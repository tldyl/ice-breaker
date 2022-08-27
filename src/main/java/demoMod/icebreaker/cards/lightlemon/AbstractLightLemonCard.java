package demoMod.icebreaker.cards.lightlemon;

import basemod.abstracts.CustomCard;
import basemod.abstracts.CustomSavable;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import demoMod.icebreaker.IceBreaker;
import demoMod.icebreaker.actions.SelectCardInCardGroupAction;
import demoMod.icebreaker.enums.AbstractCardEnum;
import demoMod.icebreaker.interfaces.CardAddToDeckSubscriber;
import demoMod.icebreaker.interfaces.TriggerFetterSubscriber;
import demoMod.icebreaker.powers.ExtraTurnPower;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public abstract class AbstractLightLemonCard extends CustomCard implements CardAddToDeckSubscriber, CustomSavable<List<String>>, TriggerFetterSubscriber {
    public int m2 = 0;
    public int baseM2 = 0;
    public boolean isM2Upgraded = false;
    public boolean extraEffectOnExtraTurn = false;
    public boolean isFetter = false;
    public List<UUID> fetterTarget = new ArrayList<>();
    private List<AbstractCard> myCardsToPreview = new ArrayList<>();
    private float previewTimer = 0.0F;

    public AbstractLightLemonCard(String id, String name, String img, int cost, String rawDescription, CardType type, CardRarity rarity, CardTarget target) {
        super(id, name, img, cost, rawDescription, type, AbstractCardEnum.ICEBREAKER, rarity, target);
    }

    public boolean isM2Buffed() {
        return m2 > baseM2;
    }

    public void upgradeM2(int amount) {
        this.baseM2 += amount;
        this.m2 = this.baseM2;
        isM2Upgraded = true;
    }

    @Override
    public void triggerOnGlowCheck() {
        this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        if (AbstractDungeon.player.hasPower(ExtraTurnPower.POWER_ID) && extraEffectOnExtraTurn) {
            this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
        }
    }

    @Override
    public void onAddToMasterDeck() {
        if (isFetter) {
            IceBreaker.addToBot(new SelectCardInCardGroupAction(1, card -> card != this, card -> {
                this.fetterTarget.add(card.uuid);
                this.myCardsToPreview.add(card);
            }, AbstractDungeon.player.masterDeck));
        }
    }

    @Override
    public void onTriggerFetter() {

    }

    @Override
    public List<String> onSave() {
        return fetterTarget.stream().map(UUID::toString).collect(Collectors.toList());
    }

    @Override
    public void onLoad(List<String> s) {
        fetterTarget = s.stream().map(UUID::fromString).collect(Collectors.toList());
        for (UUID uuid : this.fetterTarget) {
            for (AbstractCard card1 : AbstractDungeon.player.masterDeck.group) {
                if (card1.uuid.equals(uuid)) {
                    this.myCardsToPreview.add(card1);
                }
            }
        }
    }

    @Override
    public Type savedType() {
        return new TypeToken<List<String>>(){}.getType();
    }

    @Override
    public AbstractCard makeStatEquivalentCopy() {
        AbstractCard card = super.makeStatEquivalentCopy();
        if (card instanceof AbstractLightLemonCard) {
            AbstractLightLemonCard lightLemonCard = (AbstractLightLemonCard) card;
            lightLemonCard.fetterTarget = this.fetterTarget;
            for (UUID uuid : AbstractLightLemonCard.this.fetterTarget) {
                for (AbstractCard card1 : AbstractDungeon.player.masterDeck.group) {
                    if (card1.uuid.equals(uuid)) {
                        lightLemonCard.myCardsToPreview.add(card1);
                    }
                }
            }
        }
        return card;
    }

    @Override
    public void renderCardTip(SpriteBatch sb) {
        super.renderCardTip(sb);
        previewTimer += Gdx.graphics.getDeltaTime();
        if (previewTimer > 1.0F) {
            previewTimer = 0.0F;
            if (this.cardsToPreview != null) {
                this.myCardsToPreview.add(this.cardsToPreview);
                this.cardsToPreview = null;
            }
            if (!this.myCardsToPreview.isEmpty()) {
                this.cardsToPreview = this.myCardsToPreview.remove(0);
            }
        }
    }
}
