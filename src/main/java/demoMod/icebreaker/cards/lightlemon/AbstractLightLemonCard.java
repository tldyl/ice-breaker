package demoMod.icebreaker.cards.lightlemon;

import basemod.abstracts.CustomCard;
import demoMod.icebreaker.enums.AbstractCardEnum;

public abstract class AbstractLightLemonCard extends CustomCard {
    public int m2 = 0;
    public int baseM2 = 0;
    public boolean isM2Upgraded = false;

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
}
