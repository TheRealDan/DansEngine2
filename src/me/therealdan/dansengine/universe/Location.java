package me.therealdan.dansengine.universe;

import java.text.DecimalFormat;

public class Location {

    private static DecimalFormat decimalFormat = new DecimalFormat("#.##");

    private int universe;
    private double x, y, z;
    private double yaw, pitch;

    public Location() {
        this(0.0, 0.0, 0.0);
    }

    public Location(double x, double y) {
        this(x, y, 0);
    }

    public Location(double x, double y, double z) {
        this(0, x, y, z);
    }

    public Location(double x, double y, double z, double yaw, double pitch) {
        this(0, x, y, z, yaw, pitch);
    }

    public Location(int universe, double x, double y, double z) {
        this(universe, x, y, z, 0.0, 0.0);
    }

    public Location(int universe, double x, double y, double z, double yaw, double pitch) {
        this.universe = universe;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public void add(Vector vector) {
        this.x += vector.getX();
        this.y += vector.getY();
        this.z += vector.getZ();
    }

    public void setUniverse(int universe) {
        this.universe = universe;
    }

    public void setYaw(double yaw) {
        this.yaw = yaw;
    }

    public void setPitch(double pitch) {
        this.pitch = pitch;
    }

    public void set(double x, double y, double z) {
        setX(x);
        setY(y);
        setZ(z);
    }

    public void add(double x, double y, double z) {
        addX(x);
        addY(y);
        addZ(z);
    }

    public void subtract(double x, double y, double z) {
        subtractX(x);
        subtractY(y);
        subtractZ(z);
    }

    public void mutliply(double x, double y, double z) {
        multiplyX(x);
        multiplyY(y);
        multiplyZ(z);
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
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

    public void subtractX(double x) {
        this.x -= x;
    }

    public void subtractY(double y) {
        this.y -= y;
    }

    public void subtractZ(double z) {
        this.z -= z;
    }

    public void multiplyX(double x) {
        this.x *= x;
    }

    public void multiplyY(double y) {
        this.y *= y;
    }

    public void multiplyZ(double z) {
        this.z *= z;
    }

    public double distance(Location location) {
        return Math.sqrt(this.distanceSquared(location));
    }

    public double distanceSquared(Location location) {
        return this.square(this.x - location.x) + square(this.y - location.y) + square(this.z - location.z);
    }

    private double square(double number) {
        return number * number;
    }

    public int getUniverse() {
        return universe;
    }

    public double getYaw() {
        return yaw;
    }

    public double getPitch() {
        return pitch;
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

    public int getXInteger() {
        return (int) Math.floor(getX());
    }

    public int getYInteger() {
        return (int) Math.floor(getY());
    }

    public int getZInteger() {
        return (int) Math.floor(getZ());
    }

    public Location add(Location location) {
        addX(location.getX());
        addY(location.getY());
        addZ(location.getZ());
        return this;
    }

    public Location subtract(Location location) {
        subtractX(location.getX());
        subtractY(location.getY());
        subtractZ(location.getZ());
        return this;
    }

    public Location add(double add) {
        this.x += add;
        this.y += add;
        this.z += add;
        return this;
    }

    public Location subtract(double subtract) {
        this.x -= subtract;
        this.y -= subtract;
        this.z -= subtract;
        return this;
    }

    public Location multiply(double multiply) {
        this.x *= multiply;
        this.y *= multiply;
        this.z *= multiply;
        return this;
    }

    public Location zero() {
        this.x = 0.0;
        this.y = 0.0;
        this.z = 0.0;
        return this;
    }

    public Location floor() {
        this.x = getXInteger();
        this.y = getYInteger();
        this.z = getZInteger();
        return this;
    }

    public Location clone() {
        return new Location(this.universe, this.x, this.y, this.z);
    }

    public Vector toVector() {
        return new Vector(x, y, z);
    }

    public String formatXY() {
        return decimalFormat.format(getX()) + "x," +
                decimalFormat.format(getY()) + "y";
    }

    public String formatXYZ() {
        return decimalFormat.format(getX()) + "x, " +
                decimalFormat.format(getY()) + "y, " +
                        decimalFormat.format(getZ()) + "z";
    }

    public String toString() {
        return "u=" + getUniverse() + ", x=" + getX() + ", y=" + getY() + ", z=" + getZ() + ", yaw=" + getYaw() + ", pitch=" + getPitch();
    }
}