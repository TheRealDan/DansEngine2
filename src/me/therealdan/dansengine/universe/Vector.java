package me.therealdan.dansengine.universe;

public class Vector {

    private double x, y, z;

    public Vector() {
        this(0.0, 0.0, 0.0);
    }

    public Vector(double x, double y, double z) {
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

    public double dot(Vector vector) {
        return this.x * vector.x + this.y * vector.y + this.z * vector.z;
    }

    public String toString() {
        return "x=" + this.x  + ", y=" + this.y + ", z=" + this.z;
    }

    public Vector add(Vector vector) {
        this.x += vector.x;
        this.y += vector.y;
        this.z += vector.z;
        return this;
    }

    public Vector subtract(Vector vector) {
        this.x -= vector.x;
        this.y -= vector.y;
        this.z -= vector.z;
        return this;
    }

    public Vector crossProduct(Vector vector) {
        return new Vector(
                this.y * vector.z - vector.y * this.z,
                this.z * vector.x - vector.z * this.x,
                this.x * vector.y - vector.x * this.y
        );
    }

    public Vector clone() {
        return new Vector(x, y, z);
    }
}