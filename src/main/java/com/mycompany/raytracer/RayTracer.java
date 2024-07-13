package com.mycompany.raytracer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import javax.swing.JPanel;

public class RayTracer {
    private List<Light> lights;

    public RayTracer(List<Light> lights) {
        this.lights = lights;
    }

    public void draw(List<Shape> shapes, JPanel panel, Vector3D cameraPosition, Vector3D lookAtPoint) {
        int width = panel.getWidth();
        int height = panel.getHeight();
        BufferedImage canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        double viewportHeight = 2.0;
        double viewportWidth = (double) width / height * viewportHeight;

        Vector3D cameraDirection = lookAtPoint.subtract(cameraPosition).normalize();
        Vector3D cameraRight = new Vector3D(0, 1, 0).cross(cameraDirection).normalize();
        Vector3D cameraUp = cameraDirection.cross(cameraRight);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double u = (double) (x - width / 2) / width;
                double v = (double) (y - height / 2) / height;
                Vector3D direction = cameraDirection
                    .add(cameraRight.scale(u * viewportWidth))
                    .add(cameraUp.scale(v * viewportHeight))
                    .normalize();

                Ray ray = new Ray(cameraPosition, direction);
                Color pixelColor = trace(ray, shapes, 0);
                canvas.setRGB(x, y, pixelColor.getRGB());
            }
        }

        if (panel instanceof RenderPanel) {
            ((RenderPanel) panel).setCanvas(canvas);
        }
    }

    private Color trace(Ray ray, List<Shape> shapes, int depth) {
        if (depth > 5) {
            return Color.BLACK;
        }

        double[] t = { Double.MAX_VALUE };
        Shape hitShape = null;

        for (Shape shape : shapes) {
            if (shape.intersect(ray, t)) {
                hitShape = shape;
            }
        }

        if (hitShape != null) {
            Vector3D hitPoint = ray.pointAtParameter(t[0]);
            Vector3D normal = hitShape.getNormal(hitPoint);
            return shade(hitShape, hitPoint, normal, ray.getDirection(), shapes, depth);
        }

        return Color.BLACK;
    }

    private Color shade(Shape shape, Vector3D hitPoint, Vector3D normal, Vector3D viewDirection, List<Shape> shapes, int depth) {
        Color objectColor = shape.getColor();
        double ambientIntensity = 0.1;
        Color ambientColor = new Color(
            (int) (objectColor.getRed() * ambientIntensity),
            (int) (objectColor.getGreen() * ambientIntensity),
            (int) (objectColor.getBlue() * ambientIntensity)
        );

        Color diffuseColor = new Color(0, 0, 0);
        Color specularColor = new Color(0, 0, 0);

        for (Light light : lights) {
            Vector3D lightDir = light.getPosition().subtract(hitPoint).normalize();
            
            Ray shadowRay = new Ray(hitPoint.add(normal.scale(0.001)), lightDir);
            boolean inShadow = false;
            for (Shape s : shapes) {
                if (s != shape && s.intersect(shadowRay, new double[]{light.getPosition().subtract(hitPoint).magnitude()})) {
                    inShadow = true;
                    break;
                }
            }
            
            if (!inShadow) {
                double diffuseFactor = Math.max(0, normal.dot(lightDir));
                diffuseColor = addColors(diffuseColor, multiplyColor(objectColor, light.getColor(), diffuseFactor * light.getIntensity()));

                Vector3D reflectDir = reflect(lightDir.scale(-1), normal);
                double specularFactor = Math.pow(Math.max(0, reflectDir.dot(viewDirection.scale(-1))), 32);
                specularColor = addColors(specularColor, multiplyColor(light.getColor(), new Color(255, 255, 255), specularFactor * light.getIntensity()));
            }
        }

        Color finalColor = addColors(ambientColor, diffuseColor);
        finalColor = addColors(finalColor, specularColor);

        if (depth < 5) {
            Vector3D reflectDir = reflect(viewDirection, normal);
            Ray reflectRay = new Ray(hitPoint.add(normal.scale(0.001)), reflectDir);
            Color reflectionColor = trace(reflectRay, shapes, depth + 1);
            finalColor = addColors(finalColor, multiplyColor(reflectionColor, new Color(255, 255, 255), 0.3));
        }

        return finalColor;
    }

    private Vector3D reflect(Vector3D incident, Vector3D normal) {
        return incident.subtract(normal.scale(2 * incident.dot(normal)));
    }

    private Color addColors(Color c1, Color c2) {
        return new Color(
            Math.min(255, c1.getRed() + c2.getRed()),
            Math.min(255, c1.getGreen() + c2.getGreen()),
            Math.min(255, c1.getBlue() + c2.getBlue())
        );
    }

    private Color multiplyColor(Color c1, Color c2, double factor) {
        return new Color(
            (int) (c1.getRed() * c2.getRed() / 255.0 * factor),
            (int) (c1.getGreen() * c2.getGreen() / 255.0 * factor),
            (int) (c1.getBlue() * c2.getBlue() / 255.0 * factor)
        );
    }
}