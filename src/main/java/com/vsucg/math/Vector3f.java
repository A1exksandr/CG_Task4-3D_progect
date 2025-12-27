package com.vsucg.math;

public class Vector3f {
    public float x, y, z;

    public Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3f add(Vector3f v) {
        return new Vector3f (this.x + v.x, this.y + v.y, this.z + v.z);
    }

    public Vector3f sub(Vector3f v) {
        return new Vector3f(this.x - v.x, this.y - v.y, this.z - v.z);
    }

    public Vector3f mul(float scalar) {
        return new Vector3f(this.x * scalar, this.y * scalar, this.z * scalar);
    }

    public Vector3f div(float scalar) {
        if (scalar == 0) throw new ArithmeticException("Division by zero");
        return new Vector3f(this.x / scalar, this.y / scalar, this.z / scalar);
    }

    public double length() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    public Vector3f normalize() {
        double len = length();
        if (len == 0) throw new ArithmeticException("Cannot normalize zero vector");
        return div((float) len);
    }

    public double dot(Vector3f v) {
        return this.x * v.x + this.y * v.y + this.z * v.z;
    }

    public Vector3f cross(Vector3f v) {
        return new Vector3f(
                this.y * v.z - this.z * v.y,
                this.z * v.x - this.x * v.z,
                this.x * v.y - this.y * v.x
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vector3f)) return false;
        Vector3f v = (Vector3f) o;
        return Math.abs(x - v.x) < 1e-9 && Math.abs(y - v.y) < 1e-9 && Math.abs(z - v.z) < 1e-9;
    }

    @Override
    public String toString() {
        return String.format("Vector3f(%.3f, %.3f, %.3f)", x, y, z);
    }
}
