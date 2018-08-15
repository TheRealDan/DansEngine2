package me.therealdan.dansengine.color;

import java.awt.*;

public class ColorPalate {

    private Color light, medium, dark, text;

    public ColorPalate(Color color) {
        this(
                color,
                Color.WHITE
        );
    }

    public ColorPalate(Color color, Color text) {
        this(
                color.brighter(),
                color,
                color.darker(),
                text
        );
    }

    public ColorPalate(Color light, Color medium, Color dark, Color text) {
        this.light = light;
        this.medium = medium;
        this.dark = dark;
        this.text = text;
    }

    public void setLight(Color light) {
        this.light = light;
    }

    public void setMedium(Color medium) {
        this.medium = medium;
    }

    public void setDark(Color dark) {
        this.dark = dark;
    }

    public void setText(Color text) {
        this.text = text;
    }

    public Color getLight() {
        return light;
    }

    public Color getMedium() {
        return medium;
    }

    public Color getDark() {
        return dark;
    }

    public Color getText() {
        return text;
    }
}