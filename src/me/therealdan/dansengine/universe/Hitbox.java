package me.therealdan.dansengine.universe;

import me.therealdan.dansengine.universe.camera.Camera;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Hitbox {

    private List<Location> points = new LinkedList<>();

    public Hitbox(Location point) {
        add(point);
    }

    public Hitbox(Location... points) {
        add(points);
    }

    public void render(Graphics2D graphics2D, Location origin, Camera camera, boolean fill) {
        if (fill) {
            graphics2D.fillPolygon(camera.getXPoints(origin, this), camera.getYPoints(origin, this), getPoints().size());
        } else {
            graphics2D.drawPolygon(camera.getXPoints(origin, this), camera.getYPoints(origin, this), getPoints().size());
        }
    }

    public void optimize() {
        for (Location point : getPoints())
            if (!isUseful(point)) points.remove(point);
    }

    public void add(Location point) {
        points.add(point);
    }

    public void add(Location... points) {
        this.points.addAll(Arrays.asList(points));
    }

    public boolean contains(Location point) {
        boolean lowerX = false;
        boolean lowerY = false;
        boolean lowerZ = false;
        boolean higherX = false;
        boolean higherY = false;
        boolean higherZ = false;

        for (Location point1 : getPoints()) {
            if (point != point1) {
                if (point.getX() <= point1.getX()) lowerX = true;
                if (point.getY() <= point1.getY()) lowerY = true;
                if (point.getZ() <= point1.getZ()) lowerZ = true;
                if (point.getX() >= point1.getX()) higherX = true;
                if (point.getY() >= point1.getY()) higherY = true;
                if (point.getZ() >= point1.getZ()) higherZ = true;
            }
        }

        return lowerX && lowerY && lowerZ && higherX && higherY && higherZ;
    }

    public boolean contains(Hitbox hitbox) {
        for (Location point : hitbox.getPoints())
            if (contains(point)) return true;
        return false;
    }

    private boolean isUseful(Location point) {
        boolean greatestX = true;
        boolean greatestY = true;
        boolean greatestZ = true;
        boolean lowestX = true;
        boolean lowestY = true;
        boolean lowestZ = true;

        for (Location point1 : getPoints()) {
            if (point != point1) {
                if (point.getX() < point1.getX()) greatestX = false;
                if (point.getY() < point1.getY()) greatestY = false;
                if (point.getZ() < point1.getZ()) greatestZ = false;
                if (point.getX() > point1.getX()) lowestX = false;
                if (point.getY() > point1.getY()) lowestY = false;
                if (point.getZ() > point1.getZ()) lowestZ = false;
            }
        }

        return greatestX || greatestY || greatestZ || lowestX || lowestY || lowestZ;
    }

    public List<Location> getPoints() {
        return new ArrayList<>(points);
    }
}