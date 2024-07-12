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

        // Calculate intersections with each slab of the cube along x-axis
        for (int i = 0; i < 3; ++i) {
            double minSlab = center.getComponent(i) - sideLength / 2;
            double maxSlab = center.getComponent(i) + sideLength / 2;

            double t1 = (minSlab - rayOrigin.getComponent(i)) / rayDirection.getComponent(i);
            double t2 = (maxSlab - rayOrigin.getComponent(i)) / rayDirection.getComponent(i);

            if (t1 > t2) {
                double temp = t1;
                t1 = t2;
                t2 = temp;
            }

            if (t1 > tNear) {
                tNear = t1;
            }
            if (t2 < tFar) {
                tFar = t2;
            }

            if (tNear > tFar || tFar < 0) {
                return false;
            }
        }

        // Check if intersection is in front of the ray's origin
        if (tNear > 0 && tNear < t[0]) {
            t[0] = tNear;
            return true;
        }

        return false;
    }
}
