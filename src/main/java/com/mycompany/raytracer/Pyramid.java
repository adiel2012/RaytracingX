package com.mycompany.raytracer;

import java.awt.Color;

public class Pyramid implements Shape {
    private Vector3D baseCenter;
    private double baseWidth;
    private double baseDepth;
    private double height;
    private Color color;

    public Pyramid(double baseCenterX, double baseCenterY, double baseCenterZ,
                   double baseWidth, double baseDepth, double height, Color color) {
        this.baseCenter = new Vector3D(baseCenterX, baseCenterY, baseCenterZ);
        this.baseWidth = baseWidth;
        this.baseDepth = baseDepth;
        this.height = height;
        this.color = color;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public boolean intersect(Ray ray, double[] t) {
        // Center of the base
        double baseCenterX = baseCenter.getX();
        double baseCenterY = baseCenter.getY();
        double baseCenterZ = baseCenter.getZ();

        // Half-dimensions of the base
        double halfBaseWidth = baseWidth / 2.0;
        double halfBaseDepth = baseDepth / 2.0;

        // Vertices of the base
        Vector3D v0 = new Vector3D(baseCenterX - halfBaseWidth, baseCenterY, baseCenterZ - halfBaseDepth);
        Vector3D v1 = new Vector3D(baseCenterX + halfBaseWidth, baseCenterY, baseCenterZ - halfBaseDepth);
        Vector3D v2 = new Vector3D(baseCenterX + halfBaseWidth, baseCenterY, baseCenterZ + halfBaseDepth);
        Vector3D v3 = new Vector3D(baseCenterX - halfBaseWidth, baseCenterY, baseCenterZ + halfBaseDepth);

        // Apex of the pyramid
        Vector3D apex = new Vector3D(baseCenterX, baseCenterY + height, baseCenterZ);

        // Check intersection with the base rectangle
        Plane basePlane = new Plane(v0, v1, v2, color);
        double[] tBase = { Double.POSITIVE_INFINITY };
        boolean intersectsBase = basePlane.intersect(ray, tBase);

        if (!intersectsBase || tBase[0] <= 0) {
            return false;
        }

        Vector3D intersectionPointBase = ray.pointAtParameter(tBase[0]);

        // Check if intersection point is within the base rectangle
        if (!isPointInRectangle(intersectionPointBase, v0, v1, v2, v3)) {
            return false;
        }

        // Check intersection with each triangle face of the pyramid
        Vector3D[] vertices = { v0, v1, v2, v3 };

        for (int i = 0; i < 4; i++) {
            Plane trianglePlane = new Plane(vertices[i], vertices[(i + 1) % 4], apex, color);
            double[] tTriangle = { Double.POSITIVE_INFINITY };
            boolean intersectsTriangle = trianglePlane.intersect(ray, tTriangle);

            if (intersectsTriangle && tTriangle[0] > 0 && tTriangle[0] < t[0]) {
                t[0] = tTriangle[0];
            }
        }

        return t[0] != Double.POSITIVE_INFINITY;
    }

    // Helper method to check if a point is within a rectangle defined by four vertices
    private boolean isPointInRectangle(Vector3D point, Vector3D v0, Vector3D v1, Vector3D v2, Vector3D v3) {
        Vector3D v0v1 = v1.subtract(v0);
        Vector3D v0p = point.subtract(v0);

        double dot00 = v0v1.dot(v0v1);
        double dot01 = v0v1.dot(v0p);

        Vector3D v2v3 = v3.subtract(v2);
        Vector3D v2p = point.subtract(v2);

        double dot11 = v2v3.dot(v2v3);
        double dot10 = v2v3.dot(v2p);

        return (dot01 >= 0 && dot01 <= dot00 && dot10 >= 0 && dot10 <= dot11);
    }
}
