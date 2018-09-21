package me.therealdan.dansengine.image;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Image {

    private static long TIMEOUT = 1000; // Time (millis) before a saved image is removed to free memory

    private BufferedImage original;

    private HashMap<String, BufferedImage> saved = new HashMap<>();
    private HashMap<String, Long> recents = new HashMap<>();

    public Image(BufferedImage bufferedImage) {
        this.original = bufferedImage;
    }

    private void createImage(int width, int height) {
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = bufferedImage.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics.drawImage(getOriginal(), 0, 0, width, height, null);
        graphics.dispose();

        this.saved.put(width + ";" + height, bufferedImage);
        this.recents.put(width + ";" + height, System.currentTimeMillis());

        List<String> recents = new ArrayList<>();
        recents.addAll(this.recents.keySet());
        for (String recent : recents) {
            if (System.currentTimeMillis() - this.recents.get(recent) > TIMEOUT) {
                this.saved.remove(recent);
                this.recents.remove(recent);
            }
        }
    }

    private BufferedImage getOriginal() {
        return original;
    }

    public BufferedImage getImage(int width, int height) {
        if (!this.saved.containsKey(width + ";" + height))
            createImage(width, height);

        return this.saved.get(width + ";" + height);
    }
}