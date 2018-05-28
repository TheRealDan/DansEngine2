package me.therealdan.dansengine.universe;

import me.therealdan.dansengine.universe.camera.Camera;

import java.awt.*;

public interface Entity {

    void render(Graphics2D graphics, Camera camera);

    boolean isVisable();

    boolean isImmovable();

    boolean isSolid();

    void teleport(Location location);

    Location getLocation();

    Vector getVector();

    Hitbox getHitbox();
}