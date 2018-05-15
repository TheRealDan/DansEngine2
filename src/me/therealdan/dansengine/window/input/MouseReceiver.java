package me.therealdan.dansengine.window.input;

import java.awt.event.MouseEvent;

public interface MouseReceiver {

    void mousePressed(MouseEvent event, Input input);

    void mouseReleased(MouseEvent event, Input input);

    void mouseMoved(MouseEvent event, Input input);

    void mouseDragged(MouseEvent event, Input input);
}