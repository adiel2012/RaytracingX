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

        double a = rayDirection.getX() * rayDirection.getX() + rayDirection.getZ() * rayDirection.getZ();
        double b = 2.0 * (oc.getX() * rayDirection.getX() + oc.getZ() * rayDirection.getZ());
        double c = oc.getX() * oc.getX() + oc.getZ() * oc.getZ() - radius * radius;

        double discriminant = b * b - 4.0 * a * c;

        if (discriminant < 0) {
            return false;
        }

        double sqrtDiscriminant = Math.sqrt(discriminant);
        double t1 = (-b - sqrtDiscriminant) / (2.0 * a);
        double t2 = (-b + sqrtDiscriminant) / (2.0 * a);

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

        double tCap = Double.POSITIVE_INFINITY;
        if (rayDirection.getY() != 0) {
            double tBottom = (minY - rayOrigin.getY()) / rayDirection.getY();
            double tTop = (maxY - rayOrigin.getY()) / rayDirection.getY();

            if (tBottom > 0) {
                Vector3D pBottom = ray.pointAtParameter(tBottom);
                if (pBottom.subtract(new Vector3D(center.getX(), minY, center.getZ())).magnitude() <= radius) {
                    tCap = tBottom;
                }
            }

            if (tTop > 0 && tTop < tCap) {
                Vector3D pTop = ray.pointAtParameter(tTop);
                if (pTop.subtract(new Vector3D(center.getX(), maxY, center.getZ())).magnitude() <= radius) {
                    tCap = tTop;
                }
            }
        }

        if (tMin < t[0] || tCap < t[0]) {
            t[0] = Math.min(tMin, tCap);
            return true;
        }

        return false;
    }

    @Override
    public Vector3D getNormal(Vector3D point) {
        double minY = center.getY() - height / 2.0;
        double maxY = center.getY() + height / 2.0;
        double epsilon = 1e-6;

        if (Math.abs(point.getY() - minY) < epsilon) {
            return new Vector3D(0, -1, 0);
        } else if (Math.abs(point.getY() - maxY) < epsilon) {
            return new Vector3D(0, 1, 0);
        } else {
            Vector3D sideNormal = new Vector3D(point.getX() - center.getX(), 0, point.getZ() - center.getZ());
            return sideNormal.normalize();
        }
    }
}