package test.com.cgvsu.math;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

class Matrix4fTest {

    @Test
    void testMatrixMultiplication() {
        Matrix4f m1 = new Matrix4f();
        Matrix4f m2 = new Matrix4f();

        // Настройка матриц

        Matrix4f result = m1.multiply(m2);

        // Проверка результата
        assertTrue(result.equals(expectedResult));
    }

    @Test
    void testVectorTransformation() {
        Matrix4f transform = new Matrix4f();
        Vector4f vector = new Vector4f(1, 0, 0, 1);

        Vector4f result = transform.multiply(vector);

        // Проверка
        assertEquals(expectedX, result.x, 0.001f);
    }
}