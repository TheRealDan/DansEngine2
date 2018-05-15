package me.therealdan.dansengine.universe;

import java.awt.*;

public interface Entity {

    void render(Graphics2D graphics2D, Camera camera);

    boolean isVisable();

    boolean isImmoveable();

    boolean isSolid();

    void teleport(Location location);

    Location getLocation();

    Hitbox getHitbox();
}