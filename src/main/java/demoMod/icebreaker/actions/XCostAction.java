package demoMod.icebreaker.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.ChemicalX;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

import java.util.function.Consumer;

public class XCostAction extends AbstractGameAction {
    private final AbstractCard card;
    private final Consumer<Integer> action;
    private final AbstractPlayer p;

    public XCostAction(AbstractCard card, Consumer<Integer> action) {
        this.card = card;
        this.action = action;
        this.p = AbstractDungeon.player;
    }

    @Override
    public void update() {
        if (this.isDone) {
            return;
        }

        int effect = EnergyPanel.totalCount;

        if (card.costForTurn != -1) {
            effect = card.energyOnUse;
        }

        if (this.p.hasRelic(ChemicalX.ID)) {
            effect += 2;
            this.p.getRelic(ChemicalX.ID).flash();
        }

        this.action.accept(effect);

        if (!card.freeToPlayOnce) {
            this.p.energy.use(EnergyPanel.totalCount);
        }

        this.isDone = true;
    }
}
