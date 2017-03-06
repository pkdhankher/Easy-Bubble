package com.eworl.easybubble.eventBus;

/**
 * Created by Dhankher on 2/13/2017.
 */
public class PointerYDiffEvent {
    float pointerYDiff;
    public PointerYDiffEvent(float pointerYdiff) {
        this.pointerYDiff = pointerYdiff;
    }
    public float getPointerYDiff(){
        return pointerYDiff;
    }
}
