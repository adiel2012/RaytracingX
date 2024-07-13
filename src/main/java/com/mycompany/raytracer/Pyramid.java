package com.mycompany.raytracer;

import java.awt.Color;

public class Pyramid implements Shape {
    private Vector3D baseCenter;
    private double baseWidth;
    private double baseDepth;
    private double height;
    private Color color;
    private Vector3D apex;
    private Vector3D[] baseVertices;

    public Pyramid(double baseCenterX, double baseCenterY, double baseCenterZ,
                   double baseWidth, double baseDepth, double height, Color color) {
        this.baseCenter = new Vector3D(baseCenterX, baseCenterY, baseCenterZ);
        this.baseWidth = baseWidth;
        this.baseDepth = baseDepth;
        this.height = height;
        this.color = color;
        
        // Calculate apex
        this.apex = new Vector3D(baseCenterX, baseCenterY + height, baseCenterZ);
        
        // Calculate base vertices
        double halfWidth = baseWidth / 2.0;
        double halfDepth = baseDepth / 2.0;
        this.baseVertices = new Vector3D[4];
        this.baseVertices[0] = new Vector3D(baseCenterX - halfWidth, baseCenterY, baseCenterZ - halfDepth);
        this.baseVertices[1] = new Vector3D(baseCenterX + halfWidth, baseCenterY, baseCenterZ - halfDepth);
        this.baseVertices[2] = new Vector3D(baseCenterX + halfWidth, baseCenterY, baseCenterZ + halfDepth);
        this.baseVertices[3] = new Vector3D(baseCenterX - halfWidth, baseCenterY, baseCenterZ + halfDepth);
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public boolean intersect(Ray ray, double[] t) {
        double closestT = Double.POSITIVE_INFINITY;
        boolean hit = false;

        // Check intersection with base
        Plane basePlane = new Plane(baseVertices[0], baseVertices[1], baseVertices[2], color);
        double[] baseT = { Double.POSITIVE_INFINITY };
        if (basePlane.intersect(ray, baseT) && isPointInBase(ray.pointAtParameter(baseT[0]))) {
            closestT = baseT[0];
            hit = true;
        }

        // Check intersection with triangular faces
        for (int i = 0; i < 4; i++) {
            Vector3D v1 = baseVertices[i];
            Vector3D v2 = baseVertices[(i + 1) % 4];
            Plane facePlane = new Plane(v1, v2, apex, color);
            double[] faceT = { Double.POSITIVE_INFINITY };
            if (facePlane.intersect(ray, faceT) && isPointInTriangle(ray.pointAtParameter(faceT[0]), v1, v2, apex)) {
                if (faceT[0] < closestT) {
                    closestT = faceT[0];
                    hit = true;
                }
            }
        }

        if (hit && closestT < t[0]) {
            t[0] = closestT;
            return true;
        }

        return false;
    }

    @Override
    public Vector3D getNormal(Vector3D point) {
        double epsilon = 1e-6;

        // Check if point is on the base
        if (Math.abs(point.getY() - baseCenter.getY()) < epsilon) {
            return new Vector3D(0, -1, 0);
        }

        // Find the face the point is on and return its normal
        for (int i = 0; i < 4; i++) {
            Vector3D v1 = baseVertices[i];
            Vector3D v2 = baseVertices[(i + 1) % 4];
            Vector3D faceNormal = v2.subtract(v1).cross(apex.subtract(v1)).normalize();
            
            // If the point is very close to the plane of this face, return the face normal
            if (Math.abs(faceNormal.dot(point.subtract(v1))) < epsilon) {
                return faceNormal;
            }
        }

        // This should not happen if the point is on the pyramid surface
        return new Vector3D(0, 1, 0);
    }

    private boolean isPointInBase(Vector3D point) {
        Vector3D v0 = baseVertices[1].subtract(baseVertices[0]);
        Vector3D v1 = baseVertices[3].subtract(baseVertices[0]);
        Vector3D v2 = point.subtract(baseVertices[0]);

        double dot00 = v0.dot(v0);
        double dot01 = v0.dot(v1);
        double dot02 = v0.dot(v2);
        double dot11 = v1.dot(v1);
        double dot12 = v1.dot(v2);

        double invDenom = 1 / (dot00 * dot11 - dot01 * dot01);
        double u = (dot11 * dot02 - dot01 * dot12) * invDenom;
        double v = (dot00 * dot12 - dot01 * dot02) * invDenom;

        return (u >= 0) && (v >= 0) && (u + v <= 1);
    }

    private boolean isPointInTriangle(Vector3D point, Vector3D v1, Vector3D v2, Vector3D v3) {
        Vector3D edge1 = v2.subtract(v1);
        Vector3D edge2 = v3.subtract(v1);
        Vector3D h = point.subtract(v1);

        Vector3D normal = edge1.cross(edge2);
        double a = normal.dot(h);
        double b = normal.dot(edge1);
        double c = normal.dot(edge2);

        if (a >= 0 && a <= b && a <= c) {
            return true;
        }
        return false;
    }
}