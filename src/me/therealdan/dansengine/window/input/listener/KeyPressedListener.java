package me.therealdan.dansengine.window.input.listener;

import me.therealdan.dansengine.window.input.Input;

import java.awt.event.KeyEvent;

public interface KeyPressedListener {

    void keyPressed(KeyEvent event, Input input);
}
