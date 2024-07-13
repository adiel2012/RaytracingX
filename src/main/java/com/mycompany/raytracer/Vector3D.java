package com.mycompany.raytracer;

public class Vector3D {
    private double x;
    private double y;
    private double z;

    public Vector3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public double getComponent(int index) {
        switch (index) {
            case 0: return x;
            case 1: return y;
            case 2: return z;
            default: throw new IllegalArgumentException("Index must be 0, 1, or 2.");
        }
    }

    public Vector3D add(Vector3D other) {
        return new Vector3D(this.x + other.x, this.y + other.y, this.z + other.z);
    }

    public Vector3D subtract(Vector3D other) {
        return new Vector3D(this.x - other.x, this.y - other.y, this.z - other.z);
    }

    public Vector3D scale(double scalar) {
        return new Vector3D(this.x * scalar, this.y * scalar, this.z * scalar);
    }

    public double dot(Vector3D other) {
        return this.x * other.x + this.y * other.y + this.z * other.z;
    }

    public Vector3D cross(Vector3D other) {
        return new Vector3D(
            this.y * other.z - this.z * other.y,
            this.z * other.x - this.x * other.z,
            this.x * other.y - this.y * other.x
        );
    }

    public Vector3D normalize() {
        double magnitude = magnitude();
        if (magnitude == 0) {
            throw new IllegalStateException("Cannot normalize the zero vector.");
        }
        return new Vector3D(this.x / magnitude, this.y / magnitude, this.z / magnitude);
    }

    public double magnitude() {
        return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    }

    public Vector3D rotate(Vector3D axis, double angle) {
        double cosTheta = Math.cos(angle);
        double sinTheta = Math.sin(angle);
        Vector3D crossProduct = this.cross(axis);
        double dotProduct = this.dot(axis);

        return new Vector3D(
            this.x * cosTheta + crossProduct.x * sinTheta + axis.x * dotProduct * (1 - cosTheta),
            this.y * cosTheta + crossProduct.y * sinTheta + axis.y * dotProduct * (1 - cosTheta),
            this.z * cosTheta + crossProduct.z * sinTheta + axis.z * dotProduct * (1 - cosTheta)
        );
    }

    @Override
    public String toString() {
        return "(" + this.x + ", " + this.y + ", " + this.z + ")";
    }
}