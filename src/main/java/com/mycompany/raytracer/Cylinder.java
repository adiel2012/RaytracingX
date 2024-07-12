package com.mycompany.raytracer;

import java.awt.Color;

public class Cylinder implements Shape {
    private Vector3D center;
    private double radius;
    private double height;
    private Color color;

    public Cylinder(Vector3D center, double radius, double height, Color color) {
        this.center = center;
        this.radius = radius;
        this.height = height;
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

        Vector3D oc = rayOrigin.subtract(center);

        // Calculate coefficients for the quadratic equation
        double a = rayDirection.getX() * rayDirection.getX() + rayDirection.getZ() * rayDirection.getZ();
        double b = 2.0 * (oc.getX() * rayDirection.getX() + oc.getZ() * rayDirection.getZ());
        double c = oc.getX() * oc.getX() + oc.getZ() * oc.getZ() - radius * radius;

        // Solve the quadratic equation
        double discriminant = b * b - 4.0 * a * c;

        if (discriminant < 0) {
            return false;
        }

        double sqrtDiscriminant = Math.sqrt(discriminant);
        double t1 = (-b - sqrtDiscriminant) / (2.0 * a);
        double t2 = (-b + sqrtDiscriminant) / (2.0 * a);

        // Check if intersections are within the cylinder's height
        double minY = center.getY() - height / 2.0;
        double maxY = center.getY() + height / 2.0;
        double intersectY1 = rayOrigin.getY() + t1 * rayDirection.getY();
        double intersectY2 = rayOrigin.getY() + t2 * rayDirection.getY();

        double tMin = Double.POSITIVE_INFINITY;

        if (t1 >= 0 && intersectY1 >= minY && intersectY1 <= maxY) {
            tMin = t1;
        }

        if (t2 >= 0 && intersectY2 >= minY && intersectY2 <= maxY) {
            if (t2 < tMin) {
                tMin = t2;
            }
        }

        // Update the closest intersection distance
        if (tMin < t[0]) {
            t[0] = tMin;
            return true;
        }

        return false;
    }
}
