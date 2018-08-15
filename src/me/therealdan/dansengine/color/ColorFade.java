package me.therealdan.dansengine.color;

import java.awt.*;

public class ColorFade extends Color {

    private static long DEFAULT_FADE_TIME = 1000;

    private Color low, high;
    private long fadeTime;

    private long lastHigh = System.currentTimeMillis();

    public ColorFade(Color low, Color high) {
        this(low, high, DEFAULT_FADE_TIME);
    }

    public ColorFade(Color low, Color high, long fadeTime) {
        super(low.getRed(), low.getGreen(), low.getBlue(), low.getAlpha());

        this.low = low;
        this.high = high;
        this.fadeTime = fadeTime;
    }

    public void high() {
        this.lastHigh = System.currentTimeMillis();
    }

    public void low() {
        this.lastHigh = System.currentTimeMillis() - getFadeTime();
    }

    public void setLow(Color low) {
        this.low = low;
    }

    public void setHigh(Color high) {
        this.high = high;
    }

    public void setFadeTime(long fadeTime) {
        this.fadeTime = fadeTime;
    }

    private int scale(int low, int high) {
        int distance = Math.abs(high - low);
        double msPerPoint = (double) (this.getFadeTime() / (long) Math.max(distance, 1));
        double timepassed = (double) Math.min(System.currentTimeMillis() - this.lastHigh, this.getFadeTime());

        int value = high;
        if (high > low) {
            while (timepassed > 0) {
                value -= 1;
                timepassed -= msPerPoint;
            }
        } else {
            while (timepassed > 0) {
                value += 1;
                timepassed -= msPerPoint;
            }
        }
        return Math.min(Math.max(value, 0), 255);
    }

    @Override
    public int getRed() {
        return scale(getLow().getRed(), getHigh().getRed());
    }

    @Override
    public int getGreen() {
        return scale(getLow().getGreen(), getHigh().getGreen());
    }

    @Override
    public int getBlue() {
        return scale(getLow().getBlue(), getHigh().getBlue());
    }

    @Override
    public int getAlpha() {
        return scale(getLow().getAlpha(), getHigh().getAlpha());
    }

    public long getFadeTime() {
        return fadeTime;
    }

    public Color getLow() {
        return low;
    }

    public Color getHigh() {
        return high;
    }

    public Color getColor() {
        return new Color(getRed(), getGreen(), getBlue(), getAlpha());
    }
}