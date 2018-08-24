package me.therealdan.dansengine.universe;

import java.text.DecimalFormat;

public class Vector {

    private static DecimalFormat decimalFormat = new DecimalFormat("#.##");

    private double x, y, z;

    public Vector() {
        this(0.0, 0.0, 0.0);
    }

    public Vector(double x, double y) {
        this(x, y, 0.0);
    }

    public Vector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void set(Vector vector) {
        this.x = vector.x;
        this.y = vector.y;
        this.z = vector.z;
    }

    public void set(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void set(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void add(double x, double y) {
        this.x += x;
        this.y += y;
    }

    public void add(double x, double y, double z) {
        this.x += x;
        this.y += y;
        this.z += z;
    }

    public void addX(double x) {
        this.x += x;
    }

    public void addY(double y) {
        this.y += y;
    }

    public void addZ(double z) {
        this.z += z;
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

    public String formatXY() {
        return decimalFormat.format(getX()) + "x, " +
                decimalFormat.format(getY()) + "y";
    }

    public String formatXYZ() {
        return decimalFormat.format(getX()) + "x, " +
                decimalFormat.format(getY()) + "y, " +
                decimalFormat.format(getZ()) + "z";
    }

    public String toString() {
        return "x=" + getX() + ", y=" + getY() + ", z=" + getZ();
    }
}