package me.therealdan.dansengine.universe;

public class Camera extends Location {

    private int width, height;
    private double fieldOfView;

    public Camera() {
        this(0, 0);
    }

    public Camera(int width, int height) {
        this(width, height, 0.0);
    }

    public Camera(int width, int height, double fieldOfView) {
        this.width = width;
        this.height = height;
        this.fieldOfView = fieldOfView;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setFieldOfView(double fieldOfView) {
        this.fieldOfView = fieldOfView;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public double getFieldOfView() {
        return fieldOfView;
    }
}