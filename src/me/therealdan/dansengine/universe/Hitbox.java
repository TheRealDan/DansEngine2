package me.therealdan.dansengine.universe;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class Hitbox {

    private HashSet<Location> points = new HashSet<>();

    public Hitbox(Location point) {
        add(point);
    }

    public Hitbox(Location... points) {
        add(points);
    }

    public void render2D(Graphics2D graphics2D, boolean fill, Camera camera) {
        if (fill) {
            graphics2D.fillPolygon(getXPoints(camera), getYPoints(camera), getPoints().size());
        } else {
            graphics2D.drawPolygon(getXPoints(camera), getYPoints(camera), getPoints().size());
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

    private int[] getXPoints(Camera camera) {
        int[] xPoints = new int[getPoints().size()];
        int i = 0;
        for (Location point : getPoints()) {
            xPoints[i++] = point.getXInteger() + camera.getXInteger();
        }
        return xPoints;
    }

    private int[] getYPoints(Camera camera) {
        int[] yPoints = new int[getPoints().size()];
        int i = 0;
        for (Location point : getPoints()) {
            yPoints[i++] = point.getYInteger() + camera.getYInteger();
        }
        return yPoints;
    }

    public List<Location> getPoints() {
        return new ArrayList<>(points);
    }
}