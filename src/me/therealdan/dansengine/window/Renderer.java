package me.therealdan.dansengine.window;

import java.awt.*;

public interface Renderer {

    void update(Window window);

    void render(Graphics2D graphics2D, Window window);
}