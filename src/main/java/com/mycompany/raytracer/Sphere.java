package com.mycompany.raytracer;

import java.awt.Color;

public class Sphere implements Shape {
    private Vector3D center;
    private double radius;
    private Color color;

    public Sphere(Vector3D center, double radius, Color color) {
        this.center = center;
        this.radius = radius;
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

        // Vector from the ray origin to the sphere center
        Vector3D oc = rayOrigin.subtract(center);

        // Coefficients for the quadratic equation
        double a = rayDirection.dot(rayDirection);
        double b = 2.0 * oc.dot(rayDirection);
        double c = oc.dot(oc) - radius * radius;

        // Discriminant of the quadratic equation
        double discriminant = b * b - 4 * a * c;

        // No intersection if the discriminant is negative
        if (discriminant < 0) {
            return false;
        }

        // Compute the smallest positive root of the quadratic equation
        double sqrtDiscriminant = Math.sqrt(discriminant);
        double root1 = (-b - sqrtDiscriminant) / (2.0 * a);
        double root2 = (-b + sqrtDiscriminant) / (2.0 * a);

        if (root1 > 0 && root1 < t[0]) {
            t[0] = root1;
            return true;
        }

        if (root2 > 0 && root2 < t[0]) {
            t[0] = root2;
            return true;
        }

        return false;
    }

    @Override
    public Vector3D getNormal(Vector3D point) {
        // The normal at any point on a sphere is the normalized vector 
        // from the center of the sphere to that point
        return point.subtract(center).normalize();
    }

    public Vector3D getCenter() {
        return center;
    }

    public double getRadius() {
        return radius;
    }
}