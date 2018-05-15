package me.therealdan.dansengine.window.input;

public interface TextReceiver {

    void type(String text);

    void enter();

    void backspace();
}