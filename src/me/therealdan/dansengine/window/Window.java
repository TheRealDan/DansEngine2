package me.therealdan.dansengine.window;

import me.therealdan.dansengine.main.DansEngine;
import me.therealdan.dansengine.main.Error;
import me.therealdan.dansengine.window.input.Input;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class Window extends Canvas {

    private static HashSet<Window> windows = new HashSet<>();
    private static int ID = 0;

    private HashSet<Renderer> renderers = new HashSet<>();

    private JFrame frame;
    private Input input;
    private int id;

    private int FPS = 0;
    private int frames = 0;
    private long time = System.currentTimeMillis();

    public Window() {
        this("");
    }

    public Window(String title) {
        this(title, 800, 600);
    }

    public Window(String title, int width, int height) {
        this(title, width, height, true, true, false, false);
    }

    public Window(String title, int width, int height, boolean resizeable, boolean exitOnClose, boolean alwaysOnTop, boolean undecorated) {
        this.frame = new JFrame(title);
        this.input = new Input();
        this.id = ID++;

        getFrame().setMinimumSize(new Dimension(width, height));
        getFrame().setMaximumSize(Toolkit.getDefaultToolkit().getScreenSize());
        getFrame().setLocationRelativeTo(null);
        getFrame().setResizable(resizeable);
        getFrame().setDefaultCloseOperation(exitOnClose ? WindowConstants.EXIT_ON_CLOSE : WindowConstants.DISPOSE_ON_CLOSE);
        getFrame().setAlwaysOnTop(alwaysOnTop);
        getFrame().setUndecorated(undecorated);
        getFrame().add(this);
        getFrame().addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent event) {
                close();
            }
        });

        addMouseListener(getInput());
        addMouseMotionListener(getInput());
        addMouseWheelListener(getInput());
        addKeyListener(getInput());

        getFrame().setVisible(true);

        windows.add(this);
    }

    public void update(Renderer... renderers) {
        update(new ArrayList<>(Arrays.asList(renderers)));
    }

    public void update(List<Renderer> renderers) {
        for (Renderer renderer : renderers)
            if (renderer != null)
                renderer.update(this);
    }

    public void render(Renderer... renderers) {
        render(new ArrayList<>(Arrays.asList(renderers)));
    }

    public void render(List<Renderer> renderers) {
        BufferStrategy bufferStrategy = getBufferStrategy();
        if (bufferStrategy == null) {
            createBufferStrategy(2);
            return;
        }

        try {
            Graphics2D graphics2D = (Graphics2D) bufferStrategy.getDrawGraphics();

            for (Renderer renderer : renderers)
                if (renderer != null)
                    renderer.render(graphics2D, this);

            this.frames++;
            if (System.currentTimeMillis() - this.time >= 1000) {
                this.time = System.currentTimeMillis();
                this.FPS = this.frames;
                this.frames = 0;
            }

            graphics2D.dispose();
            bufferStrategy.show();
        } catch (IllegalStateException e) {
            new Error(e, "Window Render - illegal state exception");
        } catch (Exception e) {
            new Error(e, "Window Render");
        }
    }

    public void register(Renderer renderer) {
        renderers.add(renderer);
    }

    public void unregister(Renderer renderer) {
        renderers.remove(renderer);
    }

    public void close() {
        WindowClosedEvent event = new WindowClosedEvent(this);
        if (getFrame().getDefaultCloseOperation() == WindowConstants.EXIT_ON_CLOSE) event.setDispose(true);
        DansEngine.getInstance().onWindowClosed(event);

        if (event.shouldDispose()) {
            getFrame().dispose();
            windows.remove(this);
        }
    }

    public void register() {
        // TODO - Input !
    }

    public int getFPS() {
        return FPS;
    }

    public int getID() {
        return id;
    }

    public JFrame getFrame() {
        return frame;
    }

    public Input getInput() {
        return input;
    }

    public List<Renderer> getRenderers() {
        return new ArrayList<>(renderers);
    }

    public static void input() {
        for (Window window : values())
            window.getInput().update();
    }

    public static List<Window> values() {
        return new ArrayList<>(windows);
    }

    public static Window get(int id) {
        for (Window window : values())
            if (window.getID() == id)
                return window;
        return null;
    }
}