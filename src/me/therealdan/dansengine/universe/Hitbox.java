package me.therealdan.dansengine.universe;

import me.therealdan.dansengine.universe.camera.Camera;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;

public class Hitbox {

    private static DecimalFormat decimalFormat = new DecimalFormat("#.##");

    private List<Location> points = new LinkedList<>();

    private double raw = 0;
    private double rotation = 0;

    private double area = 0;

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

    public void rotate(double angle) {
        raw += angle;
        if (raw > Math.PI * 2) raw = 0;
        if (raw < 0) raw = Math.PI * 2;
        rotation = raw / (Math.PI * 2);

        double s = Math.sin(angle);
        double c = Math.cos(angle);

        for (Location each : getPoints()) {
//            each.subtractX(point.getX());
//            each.subtractY(point.getY());

            double x = each.getX() * c - each.getY() * s;
            double y = each.getX() * s + each.getY() * c;

            each.setX(x);
            each.setY(y);
        }
    }

    public void calculateArea() {
        List<Location> points = getPoints();
        double area = 0.0;

        int j = points.size() - 1;
        for (int i = 0; i < points.size(); i++) {
            area += (points.get(j).getX() + points.get(i).getX()) * (points.get(j).getY() - points.get(i).getY());
            j = i;
        }

        this.area = Double.parseDouble(decimalFormat.format(Math.abs(area) / 2));
    }

    public void optimize() {
        for (Location point : getPoints()) {
            for (Location other : getPoints()) {
                if (point != other && point.distance(other) < 0.1) {
                    points.remove(point);
                }
            }
        }
    }

    public void round() {
        for (Location point : getPoints())
            if (!isUseful(point)) points.remove(point);
    }

    public void addRelative(Location location, Location point) {
        add(point.subtract(location));
    }

    public void add(Location point) {
        points.add(point);
    }

    public void add(Location... points) {
        this.points.addAll(Arrays.asList(points));
    }

    public double getRawRotation() {
        return raw;
    }

    public double getRotation() {
        return rotation;
    }

    public double getArea() {
        return area;
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

    public Location getCenter() {
        int x = 0;
        int y = 0;
        int z = 0;

        for (Location point : getPoints()) {
            x += point.getX();
            y += point.getY();
            z += point.getZ();
        }

        x /= getPoints().size();
        y /= getPoints().size();
        z /= getPoints().size();

        return new Location(x, y, z);
    }

    public List<Location> getPoints() {
        return new ArrayList<>(points);
    }

    public String format() {
        String string = rotation + " ";

        for (Location point : getPoints())
            string += ", " + point.formatXY();

        return string.replaceFirst(", ", "");
    }

    public String toString() {
        String string = raw + "H" + rotation;

        for (Location point : getPoints())
            string += "H" + point.toString();

        return string;
    }

    public static Hitbox fromString(String string) {
        String[] args = string.split("H");
        Hitbox hitbox = new Hitbox();
        hitbox.raw = Double.parseDouble(args[0]);
        hitbox.rotation = Double.parseDouble(args[1]);

        for (int i = 2; i < args.length; i++)
            hitbox.add(Location.fromString(args[i]));

        return hitbox;
    }

}