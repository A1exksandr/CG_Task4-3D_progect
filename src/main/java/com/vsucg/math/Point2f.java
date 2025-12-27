package com.vsucg.math;

public class Point2f {
    public float x, y;

    public Point2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return String.format("Point2f(%.3f, %.3f)", x, y);
    }
}
