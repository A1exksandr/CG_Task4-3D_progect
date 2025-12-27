package com.vsucg.render_engine;

import com.vsucg.math.Vector3f;
import com.vsucg.math.Matrix4f;
import com.vsucg.math.Point2f;
import javafx.scene.canvas.GraphicsContext;
import com.vsucg.model.Model;
import static com.vsucg.render_engine.GraphicConveyor.*;

public class RenderEngine {
    // Добавьте параметры для трансформации модели
    private static float modelScaleX = 1.0f, modelScaleY = 1.0f, modelScaleZ = 1.0f;
    private static float modelRotateX = 0.0f, modelRotateY = 0.0f, modelRotateZ = 0.0f;
    private static float modelTranslateX = 0.0f, modelTranslateY = 0.0f, modelTranslateZ = 0.0f;

    public static void setModelTransform(
            float scaleX, float scaleY, float scaleZ,
            float rotateX, float rotateY, float rotateZ,
            float translateX, float translateY, float translateZ) {
        modelScaleX = scaleX;
        modelScaleY = scaleY;
        modelScaleZ = scaleZ;
        modelRotateX = rotateX;
        modelRotateY = rotateY;
        modelRotateZ = rotateZ;
        modelTranslateX = translateX;
        modelTranslateY = translateY;
        modelTranslateZ = translateZ;
    }

    public static void render(
            final GraphicsContext graphicsContext,
            final Camera camera,
            final Model mesh,
            final int width,
            final int height,
            float scaleX, float scaleY, float scaleZ,
            float rotateX, float rotateY, float rotateZ,
            float translateX, float translateY, float translateZ) {
        // Используем параметры трансформации
        Matrix4f modelMatrix = GraphicConveyor.rotateScaleTranslate(
                modelScaleX, modelScaleY, modelScaleZ,
                modelRotateX, modelRotateY, modelRotateZ,
                modelTranslateX, modelTranslateY, modelTranslateZ);

        Matrix4f viewMatrix = camera.getViewMatrix();
        Matrix4f projectionMatrix = camera.getProjectionMatrix();

        // Порядок умножения для векторов-столбцов: projection * view * model * vertex
        Matrix4f modelViewMatrix = viewMatrix.multiply(modelMatrix);
        Matrix4f modelViewProjectionMatrix = projectionMatrix.multiply(modelViewMatrix);

        // Остальной код остается прежним, но использует ваши классы
        final int nPolygons = mesh.polygons.size();
        for (int polygonInd = 0; polygonInd < nPolygons; ++polygonInd) {
            final int nVerticesInPolygon = mesh.polygons.get(polygonInd).getVertexIndices().size();

            java.util.ArrayList<Point2f> resultPoints = new java.util.ArrayList<>();
            for (int vertexInPolygonInd = 0; vertexInPolygonInd < nVerticesInPolygon; ++vertexInPolygonInd) {
                Vector3f vertex = mesh.vertices.get(mesh.polygons.get(polygonInd).getVertexIndices().get(vertexInPolygonInd));

                Vector3f transformedVertex = multiplyMatrix4ByVector3(modelViewProjectionMatrix, vertex);
                Point2f resultPoint = vertexToPoint(transformedVertex, width, height);
                resultPoints.add(resultPoint);
            }

            // Отрисовка линий
            for (int vertexInPolygonInd = 1; vertexInPolygonInd < nVerticesInPolygon; ++vertexInPolygonInd) {
                graphicsContext.strokeLine(
                        resultPoints.get(vertexInPolygonInd - 1).x,
                        resultPoints.get(vertexInPolygonInd - 1).y,
                        resultPoints.get(vertexInPolygonInd).x,
                        resultPoints.get(vertexInPolygonInd).y);
            }

            if (nVerticesInPolygon > 0)
                graphicsContext.strokeLine(
                        resultPoints.get(nVerticesInPolygon - 1).x,
                        resultPoints.get(nVerticesInPolygon - 1).y,
                        resultPoints.get(0).x,
                        resultPoints.get(0).y);
        }
    }
}