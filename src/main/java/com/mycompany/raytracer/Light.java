package com.mycompany.raytracer;

import java.awt.Color;

public class Light {
    private Vector3D position;
    private Color color;
    private double intensity;

    public Light(Vector3D position, Color color, double intensity) {
        this.position = position;
        this.color = color;
        this.intensity = intensity;
    }

    public Vector3D getPosition() {
        return position;
    }

    public Color getColor() {
        return color;
    }

    public double getIntensity() {
        return intensity;
    }

    // You might want to add methods to modify the light properties
    public void setPosition(Vector3D position) {
        this.position = position;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setIntensity(double intensity) {
        this.intensity = intensity;
    }

    // Optional: method to get the direction from a point to this light
    public Vector3D getDirectionFrom(Vector3D point) {
        return this.position.subtract(point).normalize();
    }

    // Optional: method to calculate attenuation based on distance
    public double getAttenuationAt(Vector3D point) {
        double distance = this.position.subtract(point).magnitude();
        // You can implement different attenuation models here
        // This is a simple inverse square law attenuation
        return 1.0 / (1.0 + distance * distance);
    }

    @Override
    public String toString() {
        return "Light{" +
               "position=" + position +
               ", color=" + color +
               ", intensity=" + intensity +
               '}';
    }
}