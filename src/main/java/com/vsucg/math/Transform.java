package com.vsucg.math;

public class Transform {
    private Vector3f translation;
    private Vector3f rotation; // углы Эйлера или кватернион
    private Vector3f scale;

    public Transform() {
        translation = new Vector3f(0, 0, 0);
        rotation = new Vector3f(0, 0, 0);
        scale = new Vector3f(1, 1, 1);
    }

    public Matrix4f getModelMatrix() {
        Matrix4f translationMatrix = createTranslationMatrix(translation);
        Matrix4f rotationMatrix = createRotationMatrix(rotation);
        Matrix4f scaleMatrix = createScaleMatrix(scale);

        // Порядок умножения важен: сначала масштаб, потом поворот, потом перемещение
        Matrix4f result = scaleMatrix;
        result = result.multiply(rotationMatrix);
        result = result.multiply(translationMatrix);

        return result;
    }
    private Matrix4f createTranslationMatrix(Vector3f translation) {
        Matrix4f result = new Matrix4f();
        return result;
    }
    private Matrix4f createRotationMatrix(Vector3f rotation) {
        Matrix4f result = new Matrix4f();
        return result;
    }
    private Matrix4f createScaleMatrix(Vector3f scale) {
        Matrix4f result = new Matrix4f();
        return result;
    }
    public Vector3f getTranslation() {
        return translation;
    }
    public void setTranslation(Vector3f translation) {
        this.translation = translation;
    }
    public Vector3f getRotation() {
        return rotation;
    }
    public void setRotation(Vector3f rotation) {
        this.rotation = rotation;
    }
    public Vector3f getScale() {
        return scale;
    }
    public void setScale(Vector3f scale) {
        this.scale = scale;
    }
}
