package me.therealdan.dansengine.window.input.listener;

import me.therealdan.dansengine.window.input.Input;

import java.awt.event.MouseEvent;

public interface MouseClickedListener {

    void mouseClicked(MouseEvent event, Input input, Input.Click click);
}
