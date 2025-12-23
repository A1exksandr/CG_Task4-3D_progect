package main.java.com.vsucg.math;

public class Vector4f {
    public double x, y, z, w;
    public Vector4f(double x, double y, double z, double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }
    public Vector4f add(Vector4f v) {
        return new Vector4f(x + v.x, y + v.y, z + v.z, w + v.w);
    }
    public Vector4f sub(Vector4f v) {
        return new Vector4f(x - v.x, y - v.y, z - v.z, w - v.w);
    }
    public Vector4f mul(double scalar) {
        return new Vector4f(this.x * scalar, this.y * scalar, this.z * scalar, this.w * scalar);
    }
    public Vector4f div(double scalar) {
        if (scalar == 0) throw new ArithmeticException("Division by zero");
        return new Vector4f(this.x / scalar, this.y / scalar, this.z / scalar, this.w / scalar);
    }
    public double length() {
        return Math.sqrt(x * x + y * y + z * z + w * w);
    }
    public Vector4f normalize() {
        double length = length();
        if (length == 0) throw new ArithmeticException("Division by zero");
        return div(length);
    }
    public double dot(Vector4f v) {
        return x * v.x + y * v.y + z * v.z + w * v.w;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vector4f)) return false;
        Vector4f v = (Vector4f) o;
        return Math.abs(x - v.x) < 1e-9 && Math.abs(y - v.y) < 1e-9 &&
                Math.abs(z - v.z) < 1e-9 && Math.abs(w - v.w) < 1e-9;
    }

    @Override
    public String toString() {
        return String.format("Vector4f(%.3f, %.3f, %.3f, %.3f)", x, y, z, w);
    }
}
