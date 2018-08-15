package me.therealdan.dansengine.util;

import java.awt.*;

public class Util {

    public static void drawString(Graphics2D graphics2D, Position position, String text, int x, int y) {
        FontMetrics fontMetrics = graphics2D.getFontMetrics();
        switch (position) {
            case TOP_LEFT:
                graphics2D.drawString(
                        text,
                        x,
                        y + fontMetrics.getAscent()
                );
                return;
            case CENTER_LEFT:
                graphics2D.drawString(
                        text,
                        x,
                        y - (fontMetrics.getHeight() / 2) + fontMetrics.getAscent()
                );
                return;
            case CENTER:
                graphics2D.drawString(
                        text,
                        x - (fontMetrics.stringWidth(text) / 2),
                        y - (fontMetrics.getHeight() / 2) + fontMetrics.getAscent()
                );
                return;
        }
    }

    public static void fillRect(Graphics2D graphics2D, Box box) {
        graphics2D.fillRect(box.getX(), box.getY(), box.getWidth(), box.getHeight());
    }

    public static void drawRect(Graphics2D graphics2D, Box box) {
        graphics2D.drawRect(box.getX(), box.getY(), box.getWidth(), box.getHeight());
    }

    public enum Position {
        CENTER_LEFT, CENTER, TOP_LEFT,
    }
}