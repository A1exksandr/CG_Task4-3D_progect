package com.vsucg.math;

import java.util.Arrays;

public class Matrix3f {
    public double[][] data = new double[3][3];

    public Matrix3f() {}

    public Matrix3f(double[][] data) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                this.data[i][j] = data[i][j];
            }
        }
    }
    public static Matrix3f identity() {
        Matrix3f m = new Matrix3f();
        for (int i = 0; i < 3; i++) {
            m.data[i][i] = 1.0;
        }
        return m;
    }

    public static Matrix3f zero(){
        return new Matrix3f();
    }

    public static Matrix3f add(Matrix3f m) {
     Matrix3f res = new Matrix3f();
     for (int i = 0; i < 3; i++) {
         for (int j = 0; j < 3; j++) {
             res.data[i][j] += m.data[i][j];
         }
     }
     return res;
    }
    public static Matrix3f subtract(Matrix3f m) {
        Matrix3f res = new Matrix3f();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                res.data[i][j] -= m.data[i][j];
            }
        }
        return res;
    }
    public static Matrix3f multiply(Matrix3f m) {
        Matrix3f res = new Matrix3f();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++) {
                    res.data[i][j] *= m.data[i][k] * m.data[k][j];
                }
            }
        }
        return res;
    }

    public Vector3f multiply(Vector3f v) {
        return new Vector3f((float) (data[0][0]*v.x + data[0][1]*v.y + data[0][2]*v.z),
                (float) (data[1][0]*v.x + data[1][1]*v.y + data[1][2]*v.z),
                (float) (data[2][0]*v.x + data[2][1]*v.y + data[2][2]*v.z)
        );
    }

    public Matrix3f transpose() {
        Matrix3f res = new Matrix3f();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                res.data[j][i] = data[j][i];
            }
        }
        return res;
    }

    public double determinant(){
        return data[0][0] * (data[1][1] * data[2][2] - data[1][2] * data[2][1])
                - data[0][1] * (data[1][0] * data[2][2] - data[1][2] * data[2][0])
                + data[0][2] * (data[1][0] * data[2][1] - data[1][1] * data[2][0]);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Matrix3f)) return false;
        Matrix3f m = (Matrix3f) obj;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (data[i][j] != m.data[i][j]) return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (double[] row : data){
            sb.append("[");
            for (double v : row) sb.append(String.format("%.3f ", v));
            sb.append("]\n");
        }
        return sb.toString();
    }
}
