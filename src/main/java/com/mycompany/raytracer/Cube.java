package com.mycompany.raytracer;

import java.awt.Color;

public class Cube implements Shape {
    private Vector3D center;
    private double sideLength;
    private Color color;

    public Cube(Vector3D center, double sideLength, Color color) {
        this.center = center;
        this.sideLength = sideLength;
        this.color = color;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public boolean intersect(Ray ray, double[] t) {
        Vector3D rayOrigin = ray.getOrigin();
        Vector3D rayDirection = ray.getDirection();

        double tNear = Double.NEGATIVE_INFINITY;
        double tFar = Double.POSITIVE_INFINITY;

        for (int i = 0; i < 3; ++i) {
            double minSlab = center.getComponent(i) - sideLength / 2;
            double maxSlab = center.getComponent(i) + sideLength / 2;

            if (Math.abs(rayDirection.getComponent(i)) < 1e-6) {
                if (rayOrigin.getComponent(i) < minSlab || rayOrigin.getComponent(i) > maxSlab) {
                    return false;
                }
            } else {
                double t1 = (minSlab - rayOrigin.getComponent(i)) / rayDirection.getComponent(i);
                double t2 = (maxSlab - rayOrigin.getComponent(i)) / rayDirection.getComponent(i);

                if (t1 > t2) {
                    double temp = t1;
                    t1 = t2;
                    t2 = temp;
                }

                tNear = Math.max(tNear, t1);
                tFar = Math.min(tFar, t2);

                if (tNear > tFar || tFar < 0) {
                    return false;
                }
            }
        }

        if (tNear > 0 && tNear < t[0]) {
            t[0] = tNear;
            return true;
        }

        return false;
    }

    @Override
    public Vector3D getNormal(Vector3D point) {
        Vector3D normal = new Vector3D(0, 0, 0);
        double epsilon = 1e-6;

        for (int i = 0; i < 3; ++i) {
            double minSlab = center.getComponent(i) - sideLength / 2;
            double maxSlab = center.getComponent(i) + sideLength / 2;

            if (Math.abs(point.getComponent(i) - minSlab) < epsilon) {
                normal = new Vector3D(
                    i == 0 ? -1 : 0,
                    i == 1 ? -1 : 0,
                    i == 2 ? -1 : 0
                );
                break;
            } else if (Math.abs(point.getComponent(i) - maxSlab) < epsilon) {
                normal = new Vector3D(
                    i == 0 ? 1 : 0,
                    i == 1 ? 1 : 0,
                    i == 2 ? 1 : 0
                );
                break;
            }
        }

        return normal;
    }
}