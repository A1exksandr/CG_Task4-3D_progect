package com.vsucg;

import com.vsucg.math.Transform;
import com.vsucg.math.Vector3f;
import com.vsucg.math.Vector2f;
import com.vsucg.math.Matrix4f;
import com.vsucg.render_engine.RenderEngine;
import javafx.fxml.FXML;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.io.File;

import com.vsucg.model.Model;
import com.vsucg.objreader.ObjReader;
import com.vsucg.render_engine.Camera;
import com.vsucg.writer.ObjWriter;

public class GuiController {

    final private float TRANSLATION = 2.0F;
    final private float ROTATION_SENSITIVITY = 0.01f;
    final private float ZOOM_SENSITIVITY = 0.1f;

    @FXML
    AnchorPane anchorPane;

    @FXML
    private Canvas canvas;

    @FXML
    private Slider scaleXSlider;

    @FXML
    private Slider scaleYSlider;

    @FXML
    private Slider scaleZSlider;

    @FXML
    private Slider rotateXSlider;

    @FXML
    private Slider rotateYSlider;

    @FXML
    private Slider rotateZSlider;

    @FXML
    private Slider translateXSlider;

    @FXML
    private Slider translateYSlider;

    @FXML
    private Slider translateZSlider;

    @FXML
    private CheckBox applyTransformCheckBox;

    @FXML
    private Label statusLabel;

    private Model mesh = null;
    private Model originalMesh = null; // Для хранения оригинальной модели

    private Camera camera = new Camera(
            new Vector3f(0, 0, 100),
            new Vector3f(0, 0, 0),
            1.0F, 1, 0.01F, 100);

    private Timeline timeline;

    // Параметры трансформации модели
    private float modelScaleX = 1.0f;
    private float modelScaleY = 1.0f;
    private float modelScaleZ = 1.0f;
    private float modelRotateX = 0.0f;
    private float modelRotateY = 0.0f;
    private float modelRotateZ = 0.0f;
    private float modelTranslateX = 0.0f;
    private float modelTranslateY = 0.0f;
    private float modelTranslateZ = 0.0f;

    // Для управления мышью
    private double mousePrevX, mousePrevY;
    private boolean isMousePressed = false;
    private boolean isRightMousePressed = false;
    private int mouseMode = 0; // 0 - вращение, 1 - панорамирование, 2 - масштабирование

    @FXML
    private void initialize() {
        // Привязка размеров канваса к размеру панели
        anchorPane.prefWidthProperty().addListener((ov, oldValue, newValue) -> {
            canvas.setWidth(newValue.doubleValue());
            updateStatus();
        });
        anchorPane.prefHeightProperty().addListener((ov, oldValue, newValue) -> {
            canvas.setHeight(newValue.doubleValue());
            updateStatus();
        });

        // Инициализация слайдеров
        initSliders();

        // Настройка Timeline для анимации
        timeline = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);

        KeyFrame frame = new KeyFrame(Duration.millis(16), event -> {
            double width = canvas.getWidth();
            double height = canvas.getHeight();

            canvas.getGraphicsContext2D().clearRect(0, 0, width, height);
            camera.setAspectRatio((float) (width / height));

            if (mesh != null) {
                RenderEngine.render(
                        canvas.getGraphicsContext2D(),
                        camera,
                        mesh,
                        (int) width,
                        (int) height,
                        modelScaleX, modelScaleY, modelScaleZ,
                        modelRotateX, modelRotateY, modelRotateZ,
                        modelTranslateX, modelTranslateY, modelTranslateZ
                );
            }
        });

        timeline.getKeyFrames().add(frame);
        timeline.play();

        // Инициализация обработчиков мыши
        initMouseHandlers();

        updateStatus();
    }

    private void initSliders() {
        // Масштабирование
        scaleXSlider.setMin(0.1);
        scaleXSlider.setMax(3.0);
        scaleXSlider.setValue(1.0);
        scaleXSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            modelScaleX = newVal.floatValue();
            updateStatus();
        });

        scaleYSlider.setMin(0.1);
        scaleYSlider.setMax(3.0);
        scaleYSlider.setValue(1.0);
        scaleYSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            modelScaleY = newVal.floatValue();
            updateStatus();
        });

        scaleZSlider.setMin(0.1);
        scaleZSlider.setMax(3.0);
        scaleZSlider.setValue(1.0);
        scaleZSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            modelScaleZ = newVal.floatValue();
            updateStatus();
        });

        // Вращение (в радианах)
        rotateXSlider.setMin(0);
        rotateXSlider.setMax(Math.PI * 2);
        rotateXSlider.setValue(0);
        rotateXSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            modelRotateX = newVal.floatValue();
            updateStatus();
        });

        rotateYSlider.setMin(0);
        rotateYSlider.setMax(Math.PI * 2);
        rotateYSlider.setValue(0);
        rotateYSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            modelRotateY = newVal.floatValue();
            updateStatus();
        });

        rotateZSlider.setMin(0);
        rotateZSlider.setMax(Math.PI * 2);
        rotateZSlider.setValue(0);
        rotateZSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            modelRotateZ = newVal.floatValue();
            updateStatus();
        });

        // Перенос
        translateXSlider.setMin(-50);
        translateXSlider.setMax(50);
        translateXSlider.setValue(0);
        translateXSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            modelTranslateX = newVal.floatValue();
            updateStatus();
        });

        translateYSlider.setMin(-50);
        translateYSlider.setMax(50);
        translateYSlider.setValue(0);
        translateYSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            modelTranslateY = newVal.floatValue();
            updateStatus();
        });

        translateZSlider.setMin(-50);
        translateZSlider.setMax(50);
        translateZSlider.setValue(0);
        translateZSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            modelTranslateZ = newVal.floatValue();
            updateStatus();
        });
    }

    private void initMouseHandlers() {
        // Левая кнопка мыши - вращение камеры
        canvas.setOnMousePressed(event -> {
            mousePrevX = event.getSceneX();
            mousePrevY = event.getSceneY();

            if (event.isPrimaryButtonDown()) {
                isMousePressed = true;
                mouseMode = 0; // Вращение
            } else if (event.isSecondaryButtonDown()) {
                isRightMousePressed = true;
                mouseMode = 1; // Панорамирование
            }
        });

        canvas.setOnMouseReleased(event -> {
            isMousePressed = false;
            isRightMousePressed = false;
        });

        canvas.setOnMouseDragged(event -> {
            if (isMousePressed || isRightMousePressed) {
                double deltaX = event.getSceneX() - mousePrevX;
                double deltaY = event.getSceneY() - mousePrevY;

                if (mouseMode == 0) { // Вращение камеры
                    handleCameraRotation(deltaX, deltaY);
                } else if (mouseMode == 1) { // Панорамирование
                    handleCameraPan(deltaX, deltaY);
                }

                mousePrevX = event.getSceneX();
                mousePrevY = event.getSceneY();
            }
        });

        // Колесико мыши - масштабирование
        canvas.setOnScroll(event -> {
            float delta = (float) event.getDeltaY();
            handleCameraZoom(delta);
        });

        // Средняя кнопка мыши - сброс камеры
        canvas.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Двойной клик - сброс камеры
                resetCamera();
            }
        });
    }

    private void handleCameraRotation(double deltaX, double deltaY) {
        Vector3f target = camera.getTarget();
        Vector3f position = camera.getPosition();

        // Вычисляем вектор направления от камеры к цели
        Vector3f direction = target.sub(position);
        float distance = (float) direction.length();
        direction = direction.normalize();

        // Вращение по оси Y (горизонтальное)
        float angleY = (float) -deltaX * ROTATION_SENSITIVITY;
        Matrix4f rotationY = Matrix4f.createRotationYMatrix(angleY);
        direction = rotationY.multiply(direction, 0);

        // Вращение по оси X (вертикальное) с ограничением
        float angleX = (float) deltaY * ROTATION_SENSITIVITY;

        // Вычисляем текущий угол наклона
        Vector3f up = new Vector3f(0, 1, 0);
        float currentAngle = (float) Math.acos(direction.dot(up));

        // Ограничиваем угол, чтобы камера не переворачивалась
        if ((currentAngle + angleX > 0.1f) && (currentAngle + angleX < Math.PI - 0.1f)) {
            Vector3f right = up.cross(direction).normalize();
            Matrix4f rotationX = Matrix4f.createRotationAroundAxisMatrix(right, angleX);
            direction = rotationX.multiply(direction, 0);
        }

        // Обновляем позицию камеры
        Vector3f newPosition = target.sub(direction.mul(distance));
        camera.setPosition(newPosition);
    }

    private void handleCameraPan(double deltaX, double deltaY) {
        Vector3f target = camera.getTarget();
        Vector3f position = camera.getPosition();

        // Вычисляем векторы right и up камеры
        Vector3f direction = target.sub(position).normalize();
        Vector3f up = new Vector3f(0, 1, 0);
        Vector3f right = up.cross(direction).normalize();
        Vector3f cameraUp = direction.cross(right).normalize();

        // Панорамирование
        float panSpeed = 0.5f;
        Vector3f panVector = right.mul((float) -deltaX * panSpeed)
                .add(cameraUp.mul((float) deltaY * panSpeed));

        camera.setPosition(position.add(panVector));
        camera.setTarget(target.add(panVector));
    }

    private void handleCameraZoom(float delta) {
        Vector3f target = camera.getTarget();
        Vector3f position = camera.getPosition();
        Vector3f direction = target.sub(position).normalize();

        float zoomAmount = delta * ZOOM_SENSITIVITY;

        // Ограничиваем минимальное расстояние
        float currentDistance = (float) position.sub(target).length();
        if (currentDistance + zoomAmount > 1.0f) {
            camera.movePosition(direction.mul(zoomAmount));
        }
    }

    private void resetCamera() {
        camera = new Camera(
                new Vector3f(0, 0, 100),
                new Vector3f(0, 0, 0),
                1.0F, 1, 0.01F, 100
        );

        // Сброс трансформаций модели
        modelScaleX = modelScaleY = modelScaleZ = 1.0f;
        modelRotateX = modelRotateY = modelRotateZ = 0.0f;
        modelTranslateX = modelTranslateY = modelTranslateZ = 0.0f;

        // Сброс слайдеров
        scaleXSlider.setValue(1.0);
        scaleYSlider.setValue(1.0);
        scaleZSlider.setValue(1.0);
        rotateXSlider.setValue(0.0);
        rotateYSlider.setValue(0.0);
        rotateZSlider.setValue(0.0);
        translateXSlider.setValue(0.0);
        translateYSlider.setValue(0.0);
        translateZSlider.setValue(0.0);

        updateStatus();
    }

    @FXML
    private void onOpenModelMenuItemClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Model (*.obj)", "*.obj"));
        fileChooser.setTitle("Load Model");

        File file = fileChooser.showOpenDialog((Stage) canvas.getScene().getWindow());
        if (file == null) {
            return;
        }

        Path fileName = Path.of(file.getAbsolutePath());

        try {
            String fileContent = Files.readString(fileName);
            mesh = ObjReader.read(fileContent);
            originalMesh = deepCopyModel(mesh); // Сохраняем оригинальную копию

            // Автоматически настраиваем камеру под модель
            autoAdjustCamera();

            // Сброс трансформаций при загрузке новой модели
            resetModelTransform();

            statusLabel.setText("Model loaded: " + file.getName());
        } catch (IOException exception) {
            statusLabel.setText("Error loading model: " + exception.getMessage());
        } catch (Exception e) {
            statusLabel.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    private void onSaveModelMenuItemClick() {
        if (mesh == null) {
            statusLabel.setText("No model to save");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Model (*.obj)", "*.obj"));
        fileChooser.setTitle("Save Model");
        fileChooser.setInitialFileName("model.obj");

        File file = fileChooser.showSaveDialog((Stage) canvas.getScene().getWindow());
        if (file == null) {
            return;
        }

        try {
            Model modelToSave;
            if (applyTransformCheckBox.isSelected()) {
                // Применяем трансформации к модели
                modelToSave = applyTransformationsToModel(mesh);
            } else {
                // Сохраняем оригинальную модель
                modelToSave = originalMesh != null ? originalMesh : mesh;
            }

            ObjWriter.write(modelToSave, file.getAbsolutePath());
            statusLabel.setText("Model saved: " + file.getName());
        } catch (IOException exception) {
            statusLabel.setText("Error saving model: " + exception.getMessage());
        }
    }

    private Transform modelTransform = new Transform();

    @FXML
    private void handleModelScaleX(ActionEvent event) {
        Vector3f currentScale = modelTransform.getScale();
        modelTransform.setScale(new Vector3f(
                currentScale.x * 1.1f,
                currentScale.y,
                currentScale.z
        ));
    }

    @FXML
    private void handleModelRotateY(ActionEvent event) {
        Vector3f currentRotation = modelTransform.getRotation();
        modelTransform.setRotation(new Vector3f(
                currentRotation.x,
                currentRotation.y + 10.0f, // поворот на 10 градусов
                currentRotation.z
        ));
    }

    private Model applyTransformationsToModel(Model model) {
        Model transformedModel = new Model();

        // Создаем матрицу трансформации
        Matrix4f transformMatrix = Matrix4f.createScaleMatrix(modelScaleX, modelScaleY, modelScaleZ);

        Matrix4f rotationX = Matrix4f.createRotationXMatrix(modelRotateX);
        Matrix4f rotationY = Matrix4f.createRotationYMatrix(modelRotateY);
        Matrix4f rotationZ = Matrix4f.createRotationZMatrix(modelRotateZ);
        Matrix4f rotation = rotationZ.multiply(rotationY).multiply(rotationX);

        Matrix4f translation = Matrix4f.createTranslationMatrix(
                modelTranslateX, modelTranslateY, modelTranslateZ);

        transformMatrix = rotation.multiply(transformMatrix);
        transformMatrix = translation.multiply(transformMatrix);

        // Применяем трансформацию к вершинам
        for (Vector3f vertex : model.vertices) {
            Vector3f transformedVertex = transformMatrix.multiply(vertex, 1.0f);
            transformedModel.vertices.add(transformedVertex);
        }

        // Копируем остальные данные без изменений
        transformedModel.textureVertices.addAll(model.textureVertices);
        transformedModel.normals.addAll(model.normals);
        transformedModel.polygons.addAll(model.polygons);

        return transformedModel;
    }

    private Model deepCopyModel(Model model) {
        Model copy = new Model();

        // Копируем вершины
        for (Vector3f vertex : model.vertices) {
            copy.vertices.add(new Vector3f(vertex.x, vertex.y, vertex.z));
        }

        // Копируем текстурные координаты
        for (Vector2f texVert : model.textureVertices) {
            copy.textureVertices.add(new Vector2f(texVert.x, texVert.y));
        }

        // Копируем нормали
        for (Vector3f normal : model.normals) {
            copy.normals.add(new Vector3f(normal.x, normal.y, normal.z));
        }

        // Копируем полигоны
        for (com.vsucg.model.Polygon polygon : model.polygons) {
            com.vsucg.model.Polygon polyCopy = new com.vsucg.model.Polygon();
            polyCopy.setVertexIndices(new java.util.ArrayList<>(polygon.getVertexIndices()));
            polyCopy.setTextureVertexIndices(new java.util.ArrayList<>(polygon.getTextureVertexIndices()));
            polyCopy.setNormalIndices(new java.util.ArrayList<>(polygon.getNormalIndices()));
            copy.polygons.add(polyCopy);
        }

        return copy;
    }

    private void autoAdjustCamera() {
        if (mesh == null || mesh.vertices.isEmpty()) return;

        // Вычисляем bounding box модели
        float minX = Float.MAX_VALUE, maxX = Float.MIN_VALUE;
        float minY = Float.MAX_VALUE, maxY = Float.MIN_VALUE;
        float minZ = Float.MAX_VALUE, maxZ = Float.MIN_VALUE;

        for (Vector3f vertex : mesh.vertices) {
            minX = Math.min(minX, vertex.x);
            maxX = Math.max(maxX, vertex.x);
            minY = Math.min(minY, vertex.y);
            maxY = Math.max(maxY, vertex.y);
            minZ = Math.min(minZ, vertex.z);
            maxZ = Math.max(maxZ, vertex.z);
        }

        // Вычисляем центр модели
        float centerX = (minX + maxX) / 2.0f;
        float centerY = (minY + maxY) / 2.0f;
        float centerZ = (minZ + maxZ) / 2.0f;

        // Вычисляем размер модели
        float sizeX = maxX - minX;
        float sizeY = maxY - minY;
        float sizeZ = maxZ - minZ;
        float maxSize = Math.max(sizeX, Math.max(sizeY, sizeZ));

        // Настраиваем камеру
        float cameraDistance = maxSize * 2.0f;
        camera = new Camera(
                new Vector3f(centerX, centerY, centerZ + cameraDistance),
                new Vector3f(centerX, centerY, centerZ),
                1.0F, 1, 0.01F, cameraDistance * 10
        );
    }

    private void resetModelTransform() {
        modelScaleX = modelScaleY = modelScaleZ = 1.0f;
        modelRotateX = modelRotateY = modelRotateZ = 0.0f;
        modelTranslateX = modelTranslateY = modelTranslateZ = 0.0f;

        scaleXSlider.setValue(1.0);
        scaleYSlider.setValue(1.0);
        scaleZSlider.setValue(1.0);
        rotateXSlider.setValue(0.0);
        rotateYSlider.setValue(0.0);
        rotateZSlider.setValue(0.0);
        translateXSlider.setValue(0.0);
        translateYSlider.setValue(0.0);
        translateZSlider.setValue(0.0);

        updateStatus();
    }

    @FXML
    private void onResetTransformButtonClick() {
        resetModelTransform();
        statusLabel.setText("Model transformations reset");
    }

    @FXML
    private void onResetCameraButtonClick() {
        resetCamera();
        statusLabel.setText("Camera reset");
    }

    private void updateStatus() {
        String status = String.format(
                "Camera: (%.1f, %.1f, %.1f) -> (%.1f, %.1f, %.1f) | " +
                        "Model: S(%.2f, %.2f, %.2f) R(%.2f, %.2f, %.2f) T(%.2f, %.2f, %.2f)",
                camera.getPosition().x, camera.getPosition().y, camera.getPosition().z,
                camera.getTarget().x, camera.getTarget().y, camera.getTarget().z,
                modelScaleX, modelScaleY, modelScaleZ,
                modelRotateX, modelRotateY, modelRotateZ,
                modelTranslateX, modelTranslateY, modelTranslateZ
        );

        if (statusLabel != null) {
            statusLabel.setText(status);
        }
    }

    // Управление камерой с клавиатуры
    @FXML
    public void handleCameraForward(ActionEvent actionEvent) {
        Vector3f direction = camera.getTarget().sub(camera.getPosition()).normalize();
        camera.movePosition(direction.mul(TRANSLATION));
        updateStatus();
    }

    @FXML
    public void handleCameraBackward(ActionEvent actionEvent) {
        Vector3f direction = camera.getTarget().sub(camera.getPosition()).normalize();
        camera.movePosition(direction.mul(-TRANSLATION));
        updateStatus();
    }

    @FXML
    public void handleCameraLeft(ActionEvent actionEvent) {
        Vector3f direction = camera.getTarget().sub(camera.getPosition()).normalize();
        Vector3f up = new Vector3f(0, 1, 0);
        Vector3f right = up.cross(direction).normalize();
        camera.movePosition(right.mul(TRANSLATION));
        updateStatus();
    }

    @FXML
    public void handleCameraRight(ActionEvent actionEvent) {
        Vector3f direction = camera.getTarget().sub(camera.getPosition()).normalize();
        Vector3f up = new Vector3f(0, 1, 0);
        Vector3f right = up.cross(direction).normalize();
        camera.movePosition(right.mul(-TRANSLATION));
        updateStatus();
    }

    @FXML
    public void handleCameraUp(ActionEvent actionEvent) {
        camera.movePosition(new Vector3f(0, TRANSLATION, 0));
        updateStatus();
    }

    @FXML
    public void handleCameraDown(ActionEvent actionEvent) {
        camera.movePosition(new Vector3f(0, -TRANSLATION, 0));
        updateStatus();
    }

    // Управление трансформацией модели
    @FXML
    public void handleModelScaleXPlus(ActionEvent actionEvent) {
        modelScaleX += 0.1f;
        scaleXSlider.setValue(modelScaleX);
        updateStatus();
    }

    @FXML
    public void handleModelScaleXMinus(ActionEvent actionEvent) {
        modelScaleX = Math.max(0.1f, modelScaleX - 0.1f);
        scaleXSlider.setValue(modelScaleX);
        updateStatus();
    }

    @FXML
    public void handleModelScaleYPlus(ActionEvent actionEvent) {
        modelScaleY += 0.1f;
        scaleYSlider.setValue(modelScaleY);
        updateStatus();
    }

    @FXML
    public void handleModelScaleYMinus(ActionEvent actionEvent) {
        modelScaleY = Math.max(0.1f, modelScaleY - 0.1f);
        scaleYSlider.setValue(modelScaleY);
        updateStatus();
    }

    @FXML
    public void handleModelScaleZPlus(ActionEvent actionEvent) {
        modelScaleZ += 0.1f;
        scaleZSlider.setValue(modelScaleZ);
        updateStatus();
    }

    @FXML
    public void handleModelScaleZMinus(ActionEvent actionEvent) {
        modelScaleZ = Math.max(0.1f, modelScaleZ - 0.1f);
        scaleZSlider.setValue(modelScaleZ);
        updateStatus();
    }

    @FXML
    public void handleModelRotateXPlus(ActionEvent actionEvent) {
        modelRotateX += 0.1f;
        rotateXSlider.setValue(modelRotateX);
        updateStatus();
    }

    @FXML
    public void handleModelRotateXMinus(ActionEvent actionEvent) {
        modelRotateX -= 0.1f;
        rotateXSlider.setValue(modelRotateX);
        updateStatus();
    }

    @FXML
    public void handleModelRotateYPlus(ActionEvent actionEvent) {
        modelRotateY += 0.1f;
        rotateYSlider.setValue(modelRotateY);
        updateStatus();
    }

    @FXML
    public void handleModelRotateYMinus(ActionEvent actionEvent) {
        modelRotateY -= 0.1f;
        rotateYSlider.setValue(modelRotateY);
        updateStatus();
    }

    @FXML
    public void handleModelRotateZPlus(ActionEvent actionEvent) {
        modelRotateZ += 0.1f;
        rotateZSlider.setValue(modelRotateZ);
        updateStatus();
    }

    @FXML
    public void handleModelRotateZMinus(ActionEvent actionEvent) {
        modelRotateZ -= 0.1f;
        rotateZSlider.setValue(modelRotateZ);
        updateStatus();
    }

    // Методы для горячих клавиш
    @FXML
    public void handleFocusOnModel(ActionEvent actionEvent) {
        if (mesh != null && !mesh.vertices.isEmpty()) {
            autoAdjustCamera();
            statusLabel.setText("Camera focused on model");
        }
    }

    @FXML
    public void handleToggleWireframe(ActionEvent actionEvent) {
        // Здесь можно добавить переключение режима отрисовки
        statusLabel.setText("Wireframe mode toggled (not implemented)");
    }
}