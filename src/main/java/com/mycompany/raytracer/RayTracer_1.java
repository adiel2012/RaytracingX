package com.mycompany.raytracer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import javax.swing.JPanel;

public class RayTracer_1 {
    public void draw(List<Shape> shapes, JPanel panel, Vector3D cameraPosition, Vector3D lookAtPoint) {
        int width = panel.getWidth();
        int height = panel.getHeight();
        BufferedImage canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        double viewportHeight = 2.0;
        double viewportWidth = (double) width / height * viewportHeight;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double u = (double) (x - width / 2) / width;
                double v = (double) (y - height / 2) / height;
                Vector3D direction = new Vector3D(u * viewportWidth, v * viewportHeight, lookAtPoint.getZ()).normalize();

                Ray ray = new Ray(cameraPosition, direction);
                Color pixelColor = trace(ray, shapes);
                canvas.setRGB(x, y, pixelColor.getRGB());
            }
        }

        if (panel instanceof RenderPanel) {
            ((RenderPanel) panel).setCanvas(canvas);
        }
    }

    private Color trace(Ray ray, List<Shape> shapes) {
        double[] t = { Double.MAX_VALUE };
        Color hitColor = Color.BLACK;

        for (Shape shape : shapes) {
            if (shape.intersect(ray, t)) {
                hitColor = shape.getColor();
            }
        }

        return hitColor;
    }
}
