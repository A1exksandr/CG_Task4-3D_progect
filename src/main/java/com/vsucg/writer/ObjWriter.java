package com.vsucg.writer;

import com.vsucg.model.Model;
import com.vsucg.model.Polygon;
import com.vsucg.math.Vector3f;
import com.vsucg.math.Vector2f;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ObjWriter {
    public static void write(Model model, String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Записываем вершины
            for (Vector3f vertex : model.vertices) {
                writer.write(String.format("v %.6f %.6f %.6f\n", vertex.x, vertex.y, vertex.z));
            }

            // Записываем текстурные координаты
            for (Vector2f texVert : model.textureVertices) {
                writer.write(String.format("vt %.6f %.6f\n", texVert.x, texVert.y));
            }

            // Записываем нормали
            for (Vector3f normal : model.normals) {
                writer.write(String.format("vn %.6f %.6f %.6f\n", normal.x, normal.y, normal.z));
            }

            writer.write("\n");

            // Записываем полигоны
            for (Polygon polygon : model.polygons) {
                writer.write("f");
                List<Integer> vertexIndices = polygon.getVertexIndices();
                List<Integer> textureIndices = polygon.getTextureVertexIndices();
                List<Integer> normalIndices = polygon.getNormalIndices();

                for (int i = 0; i < vertexIndices.size(); i++) {
                    int vIndex = vertexIndices.get(i) + 1; // OBJ индексы начинаются с 1

                    if (!textureIndices.isEmpty() && !normalIndices.isEmpty()) {
                        int tIndex = textureIndices.get(i) + 1;
                        int nIndex = normalIndices.get(i) + 1;
                        writer.write(String.format(" %d/%d/%d", vIndex, tIndex, nIndex));
                    } else if (!textureIndices.isEmpty()) {
                        int tIndex = textureIndices.get(i) + 1;
                        writer.write(String.format(" %d/%d", vIndex, tIndex));
                    } else if (!normalIndices.isEmpty()) {
                        int nIndex = normalIndices.get(i) + 1;
                        writer.write(String.format(" %d//%d", vIndex, nIndex));
                    } else {
                        writer.write(String.format(" %d", vIndex));
                    }
                }
                writer.write("\n");
            }
        }
    }
}
