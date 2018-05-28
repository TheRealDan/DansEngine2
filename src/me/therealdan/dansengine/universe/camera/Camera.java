package me.therealdan.dansengine.universe.camera;

import me.therealdan.dansengine.universe.Hitbox;
import me.therealdan.dansengine.universe.Location;
import me.therealdan.dansengine.window.Window;

public interface Camera {

    void setScale(double scale);

    void setSize(Window window);

    void setSize(int width, int height);

    double getScale();

    double getPixelsPerLocation();

    double getPixels();

    int getWidth();

    int getHeight();

    int[] getXPoints(Location origin, Hitbox hitbox);

    int[] getYPoints(Location origin, Hitbox hitbox);
}