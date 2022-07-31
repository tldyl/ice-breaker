package demoMod.icebreaker.interfaces;

import basemod.interfaces.ISubscriber;

public interface EnterOrExitExtraTurnSubscriber extends ISubscriber {
    void onEnterExtraTurn();

    void onExitExtraTurn();
}
