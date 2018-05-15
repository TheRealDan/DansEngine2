package me.therealdan.dansengine.main;

import me.therealdan.dansengine.window.Window;

public class DansEngine implements Runnable {

    private static DansEngine dansEngine;

    private Thread thread;
    private boolean running = false;
    private static double TARGET_UPS;
    private static double UPDATE_INTERVAL;
    private static int UPS = 0;

    public DansEngine() {
        this(60.0);
    }

    public DansEngine(double targetUPS) {
        this.dansEngine = this;

        setTargetUPS(targetUPS);

        this.thread = new Thread(this, "DansEngine");
        this.thread.start();
    }

    @Override
    public void run() {
        running = true;
        long time = System.currentTimeMillis();
        long now, last = System.nanoTime();
        double updates = 0;
        int ups = 0;
        while (running) {
            try {
                now = System.nanoTime();
                updates += (now - last) / UPDATE_INTERVAL;
                last = now;
                while (updates >= 1) {
                    update();
                    updates--;
                    ups++;
                }
                render();
                if (System.currentTimeMillis() - time >= 1000) {
                    time = System.currentTimeMillis();
                    DansEngine.UPS = ups;
                    ups = 0;
                }
            } catch (Exception e) {
                new Error(e);
                running = false;
            }
        }

        try {
            thread.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void render() {
        for (Window window : Window.values())
            window.render(window.getRenderers());
    }

    public void update() {
        for (Window window : Window.values())
            window.update(window.getRenderers());
    }

    public void onWindowClosed(Window window) {
    }

    public static void setTargetUPS(double targetUPS) {
        TARGET_UPS = targetUPS;
        UPDATE_INTERVAL = 1000000000.0 / targetUPS;
    }

    public static double getTargetUPS() {
        return TARGET_UPS;
    }

    public static int getUPS() {
        return UPS;
    }

    public static DansEngine getInstance() {
        return dansEngine;
    }
}