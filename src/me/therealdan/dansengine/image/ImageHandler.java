package me.therealdan.dansengine.image;

import me.therealdan.dansengine.main.Error;

import java.awt.image.BufferedImage;
import java.util.HashMap;

public class ImageHandler implements Runnable {

    public static String PROGRAM_LOCATION = "";

    private Thread thread;
    private double total = 0;
    private double current = -1;
    private String currentOperation = "Loading...";

    private HashMap<String, Image> images = new HashMap<>();

    public ImageHandler() {
        try {
            PROGRAM_LOCATION = this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath().replace(".jar", "/").replace(".exe", "/");
        } catch (Exception e) {
            new Error(e);
        }

        thread = new Thread(this, "Load");

        load(false);
        total = current;
        current = -1;
        currentOperation = "Loading...";
    }

    @Override
    public void run() {
        load(true);
        try {
            this.thread.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void load(boolean load) {
    }

    public void reload() {
        // Im assuming this works.
        // If the thread is already running, we return
        // Otherwise, we run();
        if (this.thread.isAlive()) return;
        this.thread.start();
    }

    public void next(String message) {
        currentOperation = message;
        current++;
    }

    public void loadImage(String string, BufferedImage image) {
        this.images.put(string.replace(".png", ""), new Image(image));
    }

    public boolean isDone() {
        return getPercentageLoaded() >= 1.0;
    }

    public double getPercentageLoaded() {
        if (current == -1) return 0;
        return current / total;
    }

    public String getCurrentOperation() {
        return currentOperation;
    }

    public BufferedImage getImage(String string, int width, int height) {
        if (width <= 0) width = 1;
        if (height <= 0) height = 1;
        if (this.images.containsKey(string))
            return this.images.get(string).getImage(width, height);
        return null;
    }
}