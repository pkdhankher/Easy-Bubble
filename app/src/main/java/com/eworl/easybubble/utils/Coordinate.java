package com.eworl.easybubble.utils;

/**
 * Created by root on 3/2/17.
 */

public class Coordinate {
    private double x, y;

    public void set(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public String toString() {
        return "x: " + x + ", y: " + y;
    }

}
