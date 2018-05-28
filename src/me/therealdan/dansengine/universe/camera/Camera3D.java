package me.therealdan.dansengine.universe.camera;

import me.therealdan.dansengine.universe.Hitbox;
import me.therealdan.dansengine.universe.Location;
import me.therealdan.dansengine.window.Window;

public class Camera3D implements Camera {

    // TODO - The whole class

    @Override
    public void setScale(double scale) {

    }

    @Override
    public void setSize(Window window) {

    }

    @Override
    public void setSize(int width, int height) {

    }

    @Override
    public double getScale() {
        return 0;
    }

    @Override
    public double getPixelsPerLocation() {
        return 0;
    }

    @Override
    public double getPixels() {
        return 0;
    }

    @Override
    public int getWidth() {
        return 0;
    }

    @Override
    public int getHeight() {
        return 0;
    }

    @Override
    public int[] getXPoints(Location origin, Hitbox hitbox) {
        return new int[0];
    }

    @Override
    public int[] getYPoints(Location origin, Hitbox hitbox) {
        return new int[0];
    }
}