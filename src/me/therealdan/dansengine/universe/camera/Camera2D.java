package me.therealdan.dansengine.universe.camera;

import me.therealdan.dansengine.universe.Hitbox;
import me.therealdan.dansengine.universe.Location;
import me.therealdan.dansengine.window.Window;

public class Camera2D extends Location implements Camera {

    private final double PIXELS_PER_LOCATION = 10.0;

    private int width, height;
    private double scale = 1.0;

    public Camera2D(Window window) {
        this(window.getWidth(), window.getHeight());
    }

    public Camera2D(int width, int height) {
        setSize(width, height);
    }

    @Override
    public void setScale(double scale) {
        this.scale = Math.max(scale, 1.0 / PIXELS_PER_LOCATION);
    }

    @Override
    public void setSize(Window window) {
        setSize(window.getWidth(), window.getHeight());
    }

    @Override
    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public double getScale() {
        return scale;
    }

    @Override
    public double getPixels() {
        return PIXELS_PER_LOCATION * getScale();
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    public int getHalfWidth() {
        return (int) (getWidth() / 2.0);
    }

    public int getHalfHeight() {
        return (int) (getHeight() / 2.0);
    }

    @Override
    public int[] getXPoints(Location origin, Hitbox hitbox) {
        int[] xPoints = new int[hitbox.getPoints().size()];
        int i = 0;
        for (Location point : hitbox.getPoints()) {
            double pointInSpace = origin.getX() + point.getX();
            double relativeToCamera = pointInSpace - getX();
            double pixel = relativeToCamera * getPixels();
            pixel += getWidth() / 2.0;

            xPoints[i++] = (int) pixel;
        }
        return xPoints;
    }

    @Override
    public int[] getYPoints(Location origin, Hitbox hitbox) {
        int[] yPoints = new int[hitbox.getPoints().size()];
        int i = 0;
        for (Location point : hitbox.getPoints()) {
            double pointInSpace = origin.getY() + point.getY();
            double relativeToCamera = pointInSpace - getY();
            double pixel = relativeToCamera * getPixels();
            pixel += getHeight() / 2.0;

            yPoints[i++] = (int) pixel;
        }
        return yPoints;
    }

    public int getScreenX(double locX) {
        return (int) ((locX - getX()) * getPixels()) + getHalfWidth();
    }

    public int getScreenY(double locY) {
        return (int) ((locY - getY()) * getPixels()) + getHalfHeight();
    }

    public int getScreenX(Location location) {
        return (int) ((location.getX() - getX()) * getPixels()) + getHalfWidth();
    }

    public int getScreenY(Location location) {
        return (int) ((location.getY() - getY()) * getPixels()) + getHalfHeight();
    }

    public Location getLocation(int screenX, int screenY) {
        Location location = clone();

        double xOffset = (getHalfWidth() - screenX) / getPixels();
        double yOffset = (getHalfHeight() - screenY) / getPixels();

        location.addX(-xOffset);
        location.addY(-yOffset);

        return location;
    }
}