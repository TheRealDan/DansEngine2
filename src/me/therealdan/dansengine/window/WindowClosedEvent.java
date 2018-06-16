package me.therealdan.dansengine.window;

public class WindowClosedEvent {

    private Window window;
    private boolean dispose = false;

    public WindowClosedEvent(Window window) {
        this.window = window;
    }

    public void setDispose(boolean dispose) {
        this.dispose = dispose;
    }

    public boolean shouldDispose() {
        return dispose;
    }

    public Window getWindow() {
        return window;
    }
}