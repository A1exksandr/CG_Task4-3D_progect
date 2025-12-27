package com.vsucg.math;

public class Vector2f {
    public float x, y;

    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2f add(Vector2f v) {
        return new Vector2f(this.x + v.x, this.y + v.y);
    }

    public Vector2f sub(Vector2f v) {
        return new Vector2f(this.x - v.x, this.y - v.y);
    }

    public Vector2f mul(float scalar) {
        return new Vector2f(this.x * scalar, this.y * scalar);
    }

    public Vector2f div(float scalar) {
        if (scalar == 0) throw new ArithmeticException("Division by zero");
        return new Vector2f(this.x / scalar, this.y / scalar);
    }

    public double length() {
        return Math.sqrt(x * x + y * y);
    }

    public Vector2f normalize() {
        double len = length();
        if (len == 0) throw new ArithmeticException("Cannot normalize zero vector");
        return div((float) len);
    }

    public double dot(Vector2f v) {
        return this.x * v.x + this.y * v.y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vector2f)) return false;
        Vector2f v = (Vector2f) o;
        return Math.abs(x - v.x) < 1e-9 && Math.abs(y - v.y) < 1e-9;
    }

    @Override
    public String toString() {
        return String.format("Vector2f(%.3f, %.3f)", x, y);
    }
}
