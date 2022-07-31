package demoMod.icebreaker.cards.lightlemon;

import basemod.abstracts.CustomCard;
import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import demoMod.icebreaker.IceBreaker;
import demoMod.icebreaker.actions.SelectCardInCardGroupAction;
import demoMod.icebreaker.enums.AbstractCardEnum;
import demoMod.icebreaker.interfaces.CardAddToDeckSubscriber;
import demoMod.icebreaker.interfaces.TriggerFetterSubscriber;
import demoMod.icebreaker.powers.ExtraTurnPower;

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
            IceBreaker.addToBot(new SelectCardInCardGroupAction(1, card -> card != this, card -> this.fetterTarget.add(card.uuid), AbstractDungeon.player.masterDeck));
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
    }
}
