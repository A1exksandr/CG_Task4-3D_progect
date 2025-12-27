package com.vsucg.render_engine;

import com.vsucg.math.*;

public class GraphicConveyor {

    // Теперь для векторов-столбцов!
    public static Matrix4f rotateScaleTranslate(float scaleX, float scaleY, float scaleZ,
                                                float rotateX, float rotateY, float rotateZ,
                                                float translateX, float translateY, float translateZ) {
        // Порядок: масштабирование -> вращение -> перенос
        Matrix4f scale = Matrix4f.createScaleMatrix(scaleX, scaleY, scaleZ);
        Matrix4f rotateXMat = Matrix4f.createRotationXMatrix(rotateX);
        Matrix4f rotateYMat = Matrix4f.createRotationYMatrix(rotateY);
        Matrix4f rotateZMat = Matrix4f.createRotationZMatrix(rotateZ);
        Matrix4f translate = Matrix4f.createTranslationMatrix(translateX, translateY, translateZ);

        // Умножение в правильном порядке для векторов-столбцов
        Matrix4f rotation = rotateZMat.multiply(rotateYMat).multiply(rotateXMat);
        Matrix4f scaleRotate = rotation.multiply(scale);
        return translate.multiply(scaleRotate);
    }

    public static Matrix4f lookAt(Vector3f eye, Vector3f target) {
        return lookAt(eye, target, new Vector3f(0F, 1.0F, 0F));
    }

    public static Matrix4f lookAt(Vector3f eye, Vector3f target, Vector3f up) {
        Vector3f zAxis = target.sub(eye).normalize();
        Vector3f xAxis = up.cross(zAxis).normalize();
        Vector3f yAxis = zAxis.cross(xAxis);

        // Для векторов-столбцов матрица выглядит иначе
        return new Matrix4f(
                xAxis.x, yAxis.x, zAxis.x, 0,
                xAxis.y, yAxis.y, zAxis.y, 0,
                xAxis.z, yAxis.z, zAxis.z, 0,
                (float)-xAxis.dot(eye), (float)-yAxis.dot(eye), (float)-zAxis.dot(eye), 1
        );
    }

    public static Matrix4f perspective(
            final float fov,
            final float aspectRatio,
            final float nearPlane,
            final float farPlane) {
        Matrix4f result = new Matrix4f();
        float tangentMinusOnDegree = (float) (1.0F / (Math.tan(fov * 0.5F)));
        result.m00 = tangentMinusOnDegree / aspectRatio;
        result.m11 = tangentMinusOnDegree;
        result.m22 = (farPlane + nearPlane) / (farPlane - nearPlane);
        result.m23 = 1.0F;
        result.m32 = -2 * (nearPlane * farPlane) / (farPlane - nearPlane);
        result.m33 = 0;
        return result;
    }

    public static Vector3f multiplyMatrix4ByVector3(final Matrix4f matrix, final Vector3f vertex) {
        Vector4f result = matrix.multiply(new Vector4f(vertex.x, vertex.y, vertex.z, 1.0f));
        return new Vector3f(result.x / result.w, result.y / result.w, result.z / result.w);
    }

    public static Point2f vertexToPoint(final Vector3f vertex, final int width, final int height) {
        return new Point2f(vertex.x * width + width / 2.0F, -vertex.y * height + height / 2.0F);
    }
}