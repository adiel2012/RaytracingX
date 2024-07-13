package com.mycompany.raytracer;

import java.awt.Color;

public interface Shape {
    boolean intersect(Ray ray, double[] t);
    Color getColor();
    Vector3D getNormal(Vector3D point);
}