package com.dhankher.easybubble;

/**
 * Created by Dhankher on 2/2/2017.
 */

public class CoordinateEvent {
    private int x, y;

    public CoordinateEvent(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
