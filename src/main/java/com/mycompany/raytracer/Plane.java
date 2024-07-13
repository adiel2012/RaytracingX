package com.mycompany.raytracer;

import java.awt.Color;

public class Plane implements Shape {
    private Vector3D normal;
    private double distance;
    private Color color;

    // Constructor using a normal vector and distance from the origin
    public Plane(Vector3D normal, double distance, Color color) {
        this.normal = normal.normalize(); // Normalize the normal vector
        this.distance = distance;
        this.color = color;
    }

    // Constructor using three points on the plane
    public Plane(Vector3D p1, Vector3D p2, Vector3D p3, Color color) {
        Vector3D v1 = p2.subtract(p1);
        Vector3D v2 = p3.subtract(p1);
        this.normal = v1.cross(v2).normalize(); // Calculate the normal vector
        this.distance = this.normal.dot(p1); // Calculate the distance from the origin
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

        double denominator = normal.dot(rayDirection);

        // Check if ray is parallel to the plane
        if (Math.abs(denominator) < 1e-6) {
            return false;
        }

        double numerator = -(normal.dot(rayOrigin) + distance);
        double intersectT = numerator / denominator;

        // Check if intersection is in front of the ray's origin
        if (intersectT > 0 && intersectT < t[0]) {
            t[0] = intersectT;
            return true;
        }

        return false;
    }

    @Override
    public Vector3D getNormal(Vector3D point) {
        // The normal is the same for all points on the plane
        return normal;
    }

    public Vector3D getNormal() {
        return normal;
    }

    public double getDistance() {
        return distance;
    }
}