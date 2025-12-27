package com.vsucg.math;

public class Matrix4f {
    public float m00, m01, m02, m03;
    public float m10, m11, m12, m13;
    public float m20, m21, m22, m23;
    public float m30, m31, m32, m33;

    public Matrix4f() {
        setIdentity();
    }

    public Matrix4f(float[] matrix) {
        set(matrix);
    }

    public Matrix4f(
            float m00, float m01, float m02, float m03,
            float m10, float m11, float m12, float m13,
            float m20, float m21, float m22, float m23,
            float m30, float m31, float m32, float m33) {
        set(
                m00, m01, m02, m03,
                m10, m11, m12, m13,
                m20, m21, m22, m23,
                m30, m31, m32, m33
        );
    }

    public void setIdentity() {
        m00 = 1; m01 = 0; m02 = 0; m03 = 0;
        m10 = 0; m11 = 1; m12 = 0; m13 = 0;
        m20 = 0; m21 = 0; m22 = 1; m23 = 0;
        m30 = 0; m31 = 0; m32 = 0; m33 = 1;
    }

    public void set(float[] matrix) {
        m00 = matrix[0];  m01 = matrix[1];  m02 = matrix[2];  m03 = matrix[3];
        m10 = matrix[4];  m11 = matrix[5];  m12 = matrix[6];  m13 = matrix[7];
        m20 = matrix[8];  m21 = matrix[9];  m22 = matrix[10]; m23 = matrix[11];
        m30 = matrix[12]; m31 = matrix[13]; m32 = matrix[14]; m33 = matrix[15];
    }

    public void set(
            float m00, float m01, float m02, float m03,
            float m10, float m11, float m12, float m13,
            float m20, float m21, float m22, float m23,
            float m30, float m31, float m32, float m33) {
        this.m00 = m00; this.m01 = m01; this.m02 = m02; this.m03 = m03;
        this.m10 = m10; this.m11 = m11; this.m12 = m12; this.m13 = m13;
        this.m20 = m20; this.m21 = m21; this.m22 = m22; this.m23 = m23;
        this.m30 = m30; this.m31 = m31; this.m32 = m32; this.m33 = m33;
    }

    public Matrix4f multiply(Matrix4f other) {
        Matrix4f result = new Matrix4f();

        result.m00 = m00 * other.m00 + m01 * other.m10 + m02 * other.m20 + m03 * other.m30;
        result.m01 = m00 * other.m01 + m01 * other.m11 + m02 * other.m21 + m03 * other.m31;
        result.m02 = m00 * other.m02 + m01 * other.m12 + m02 * other.m22 + m03 * other.m32;
        result.m03 = m00 * other.m03 + m01 * other.m13 + m02 * other.m23 + m03 * other.m33;

        result.m10 = m10 * other.m00 + m11 * other.m10 + m12 * other.m20 + m13 * other.m30;
        result.m11 = m10 * other.m01 + m11 * other.m11 + m12 * other.m21 + m13 * other.m31;
        result.m12 = m10 * other.m02 + m11 * other.m12 + m12 * other.m22 + m13 * other.m32;
        result.m13 = m10 * other.m03 + m11 * other.m13 + m12 * other.m23 + m13 * other.m33;

        result.m20 = m20 * other.m00 + m21 * other.m10 + m22 * other.m20 + m23 * other.m30;
        result.m21 = m20 * other.m01 + m21 * other.m11 + m22 * other.m21 + m23 * other.m31;
        result.m22 = m20 * other.m02 + m21 * other.m12 + m22 * other.m22 + m23 * other.m32;
        result.m23 = m20 * other.m03 + m21 * other.m13 + m22 * other.m23 + m23 * other.m33;

        result.m30 = m30 * other.m00 + m31 * other.m10 + m32 * other.m20 + m33 * other.m30;
        result.m31 = m30 * other.m01 + m31 * other.m11 + m32 * other.m21 + m33 * other.m31;
        result.m32 = m30 * other.m02 + m31 * other.m12 + m32 * other.m22 + m33 * other.m32;
        result.m33 = m30 * other.m03 + m31 * other.m13 + m32 * other.m23 + m33 * other.m33;

        return result;
    }

    public Vector4f multiply(Vector4f vector) {
        float x = m00 * vector.x + m01 * vector.y + m02 * vector.z + m03 * vector.w;
        float y = m10 * vector.x + m11 * vector.y + m12 * vector.z + m13 * vector.w;
        float z = m20 * vector.x + m21 * vector.y + m22 * vector.z + m23 * vector.w;
        float w = m30 * vector.x + m31 * vector.y + m32 * vector.z + m33 * vector.w;
        return new Vector4f(x, y, z, w);
    }

    public Vector3f multiply(Vector3f vector, float w) {
        Vector4f result = multiply(new Vector4f(vector.x, vector.y, vector.z, w));
        return new Vector3f(result.x, result.y, result.z);
    }

    public Matrix4f transpose() {
        return new Matrix4f(
                m00, m10, m20, m30,
                m01, m11, m21, m31,
                m02, m12, m22, m32,
                m03, m13, m23, m33
        );
    }

    // Методы для аффинных преобразований
    public static Matrix4f createTranslationMatrix(float tx, float ty, float tz) {
        return new Matrix4f(
                1, 0, 0, tx,
                0, 1, 0, ty,
                0, 0, 1, tz,
                0, 0, 0, 1
        );
    }

    public static Matrix4f createScaleMatrix(float sx, float sy, float sz) {
        return new Matrix4f(
                sx, 0, 0, 0,
                0, sy, 0, 0,
                0, 0, sz, 0,
                0, 0, 0, 1
        );
    }

    public static Matrix4f createRotationXMatrix(float angle) {
        float cos = (float) Math.cos(angle);
        float sin = (float) Math.sin(angle);
        return new Matrix4f(
                1, 0, 0, 0,
                0, cos, -sin, 0,
                0, sin, cos, 0,
                0, 0, 0, 1
        );
    }

    public static Matrix4f createRotationYMatrix(float angle) {
        float cos = (float) Math.cos(angle);
        float sin = (float) Math.sin(angle);
        return new Matrix4f(
                cos, 0, sin, 0,
                0, 1, 0, 0,
                -sin, 0, cos, 0,
                0, 0, 0, 1
        );
    }

    public static Matrix4f createRotationZMatrix(float angle) {
        float cos = (float) Math.cos(angle);
        float sin = (float) Math.sin(angle);
        return new Matrix4f(
                cos, -sin, 0, 0,
                sin, cos, 0, 0,
                0, 0, 1, 0,
                0, 0, 0, 1
        );
    }

    public static Matrix4f createRotationAroundAxisMatrix(Vector3f axis, float angle) {
        float cos = (float) Math.cos(angle);
        float sin = (float) Math.sin(angle);
        float oneMinusCos = 1.0f - cos;

        float x = axis.x;
        float y = axis.y;
        float z = axis.z;

        return new Matrix4f(
                cos + x*x*oneMinusCos,     x*y*oneMinusCos - z*sin, x*z*oneMinusCos + y*sin, 0,
                y*x*oneMinusCos + z*sin,   cos + y*y*oneMinusCos,   y*z*oneMinusCos - x*sin, 0,
                z*x*oneMinusCos - y*sin,   z*y*oneMinusCos + x*sin, cos + z*z*oneMinusCos,   0,
                0, 0, 0, 1
        );
    }
}