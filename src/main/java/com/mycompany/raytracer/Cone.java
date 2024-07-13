package com.mycompany.raytracer;

import java.awt.Color;

public class Cone implements Shape {
    private Vector3D baseCenter;
    private Vector3D axisDirection;
    private double height;
    private double radius;
    private Color color;

    public Cone(Vector3D baseCenter, Vector3D axisDirection, double height, double radius, Color color) {
        this.baseCenter = baseCenter;
        this.axisDirection = axisDirection.normalize();
        this.height = height;
        this.radius = radius;
        this.color = color;
    }
    
    @Override
    public Vector3D getNormal(Vector3D point) {
        Vector3D baseToPoint = point.subtract(baseCenter);
        double projectionLength = baseToPoint.dot(axisDirection);
        Vector3D projectionPoint = baseCenter.add(axisDirection.scale(projectionLength));
        
        if (projectionLength <= 0) {
            return axisDirection.scale(-1);
        }
        
        Vector3D normal = point.subtract(projectionPoint);
        return normal.normalize();
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public boolean intersect(Ray ray, double[] t) {
        Vector3D rayOrigin = ray.getOrigin();
        Vector3D rayDirection = ray.getDirection();

        Vector3D oc = rayOrigin.subtract(baseCenter);

        double k = Math.tan(Math.atan(radius / height));
        k = k * k;

        double a = rayDirection.dot(rayDirection) - (1 + k) * Math.pow(rayDirection.dot(axisDirection), 2);
        double b = 2 * (oc.dot(rayDirection) - (1 + k) * rayDirection.dot(axisDirection) * oc.dot(axisDirection));
        double c = oc.dot(oc) - (1 + k) * Math.pow(oc.dot(axisDirection), 2);

        double discriminant = b * b - 4 * a * c;

        if (discriminant < 0) {
            return false;
        }

        double sqrtDiscriminant = Math.sqrt(discriminant);
        double root1 = (-b - sqrtDiscriminant) / (2.0 * a);
        double root2 = (-b + sqrtDiscriminant) / (2.0 * a);

        double tCone = -1.0;
        if (root1 > 0 && root1 < t[0]) {
            tCone = root1;
        } else if (root2 > 0 && root2 < t[0]) {
            tCone = root2;
        } else {
            return false;
        }

        Vector3D intersectionPoint = ray.pointAtParameter(tCone);
        Vector3D baseToIntersection = intersectionPoint.subtract(baseCenter);
        double projection = baseToIntersection.dot(axisDirection);
        if (projection < 0 || projection > height) {
            return false;
        }

        t[0] = tCone;
        return true;
    }
}