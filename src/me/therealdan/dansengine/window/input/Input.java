package me.therealdan.dansengine.window.input;

import java.awt.event.*;
import java.util.HashMap;
import java.util.HashSet;

public class Input implements MouseListener, MouseMotionListener, MouseWheelListener, KeyListener {

    private int mouseX, mouseY;
    private int mouseDragX, mouseDragY;
    private int absoluteX, absoluteY;
    private int mouseScreenX, mouseScreenY;
    private boolean dragging = false;
    private boolean inWindow = false;
    private HashMap<Click, Boolean> mousePressed = new HashMap<>();
    private HashMap<Click, Boolean> mouseReleased = new HashMap<>();
    private HashMap<Click, Boolean> mouseHeld = new HashMap<>();

    private int mouseWheel = 0;

    private boolean[] pressedKeys = new boolean[1024];
    private boolean[] releasedKeys = new boolean[1024];
    private boolean[] heldKeys = new boolean[1024];

    private HashSet<TextReceiver> textReceivers = new HashSet<>();
    private HashSet<MouseReceiver> mouseReceivers = new HashSet<>();

    @Override
    public void mousePressed(MouseEvent event) {
        for (MouseReceiver mouseReceiver : mouseReceivers)
            mouseReceiver.mousePressed(event, this);

        Click click = getClick(event.getButton());

        if (!mousePressed.containsKey(click)) mousePressed.put(click, false);
        if (!mouseReleased.containsKey(click)) mouseReleased.put(click, false);

        if (!mouseReleased.get(click)) {
            mouseReleased.put(click, true);
            mousePressed.put(click, true);
        }

        mouseHeld.put(click, true);
    }

    @Override
    public void mouseReleased(MouseEvent event) {
        for (MouseReceiver mouseReceiver : mouseReceivers)
            mouseReceiver.mouseReleased(event, this);

        Click click = getClick(event.getButton());

        mouseReleased.put(click, false);
        mousePressed.put(click, false);

        mouseHeld.put(click, false);
    }

    @Override
    public void mouseClicked(MouseEvent event) {
    }

    @Override
    public void mouseMoved(MouseEvent event) {
        for (MouseReceiver mouseReceiver : mouseReceivers)
            mouseReceiver.mouseMoved(event, this);

        this.mouseX = event.getX();
        this.mouseY = event.getY();
        this.absoluteX = event.getX();
        this.absoluteY = event.getY();
        this.mouseScreenX = event.getXOnScreen();
        this.mouseScreenY = event.getYOnScreen();
        this.dragging = false;
    }

    @Override
    public void mouseDragged(MouseEvent event) {
        for (MouseReceiver mouseReceiver : mouseReceivers)
            mouseReceiver.mouseDragged(event, this);

        this.mouseDragX = event.getX();
        this.mouseDragY = event.getY();
        this.absoluteX = event.getX();
        this.absoluteY = event.getY();
        this.mouseScreenX = event.getXOnScreen();
        this.mouseScreenY = event.getYOnScreen();
        this.dragging = true;
    }

    @Override
    public void mouseEntered(MouseEvent event) {
        inWindow = true;
    }

    @Override
    public void mouseExited(MouseEvent event) {
        inWindow = false;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent event) {
        this.mouseWheel = event.getWheelRotation();
    }

    @Override
    public void keyPressed(KeyEvent event) {
        if (!releasedKeys[event.getKeyCode()]) {
            releasedKeys[event.getKeyCode()] = true;
            pressedKeys[event.getKeyCode()] = true;
        }

        heldKeys[event.getKeyCode()] = true;

        switch (event.getKeyCode()) {
            case KeyEvent.VK_BACK_SPACE:
                for (TextReceiver textReceiver : getTextReceivers())
                    textReceiver.backspace();
                return;
            case KeyEvent.VK_ENTER:
                for (TextReceiver textReceiver : getTextReceivers())
                    textReceiver.enter();
                return;
            case KeyEvent.VK_DELETE:
            case KeyEvent.VK_SHIFT:
            case KeyEvent.VK_ESCAPE:
            case KeyEvent.VK_CONTROL:
            case KeyEvent.VK_ALT:
            case KeyEvent.VK_WINDOWS:
            case KeyEvent.VK_CAPS_LOCK:
            case KeyEvent.VK_NUM_LOCK:
            case KeyEvent.VK_SCROLL_LOCK:
            case KeyEvent.VK_INSERT:
            case KeyEvent.VK_HOME:
            case KeyEvent.VK_END:
            case KeyEvent.VK_PAGE_UP:
            case KeyEvent.VK_PAGE_DOWN:
            case KeyEvent.VK_PRINTSCREEN:
            case KeyEvent.VK_PAUSE:
            case KeyEvent.VK_F1:
            case KeyEvent.VK_F2:
            case KeyEvent.VK_F3:
            case KeyEvent.VK_F4:
            case KeyEvent.VK_F5:
            case KeyEvent.VK_F6:
            case KeyEvent.VK_F7:
            case KeyEvent.VK_F8:
            case KeyEvent.VK_F9:
            case KeyEvent.VK_F10:
            case KeyEvent.VK_F11:
            case KeyEvent.VK_F12:
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_UP:
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_CONTEXT_MENU:
            case KeyEvent.VK_CONVERT:
                return;
        }

        for (TextReceiver textReceiver : getTextReceivers())
            textReceiver.type(event.getKeyChar() + "");
    }

    @Override
    public void keyReleased(KeyEvent event) {
        pressedKeys[event.getKeyCode()] = false;
        releasedKeys[event.getKeyCode()] = false;

        heldKeys[event.getKeyCode()] = false;
    }

    @Override
    public void keyTyped(KeyEvent event) {
    }

    public void register(TextReceiver textReceiver) {
        this.textReceivers.add(textReceiver);
    }

    public void unregister(TextReceiver textReceiver) {
        if (!textReceivers.contains(textReceiver)) return;
        this.textReceivers.remove(textReceiver);
    }

    public void register(MouseReceiver mouseReceiver) {
        this.mouseReceivers.add(mouseReceiver);
    }

    public void unregister(MouseReceiver mouseReceiver) {
        if (!mouseReceivers.contains(mouseReceiver)) return;
        this.mouseReceivers.remove(mouseReceiver);
    }

    public boolean wasClicked(Click click) {
        if (!mousePressed.containsKey(click)) mousePressed.put(click, false);

        if (mousePressed.get(click)) {
            mousePressed.put(click, false);
            return true;
        }
        return false;
    }

    public boolean isHeld(Click click) {
        if (!mouseHeld.containsKey(click)) mouseHeld.put(click, false);
        return this.mouseHeld.get(click);
    }

    public boolean wasPressed(int key) {
        if (pressedKeys[key]) {
            pressedKeys[key] = false;
            return true;
        }
        return false;
    }

    public boolean wasPressed(int... keyOR) {
        for (int key : keyOR) {
            if (pressedKeys[key]) {
                pressedKeys[key] = false;
                return true;
            }
        }
        return false;
    }

    public boolean isHeld(int key) {
        return this.heldKeys[key];
    }

    public boolean isHeld(int... keyAND) {
        for (int key : keyAND)
            if (!this.heldKeys[key]) return false;
        return true;
    }

    public boolean isHeldOR(int... keyOR) {
        for (int key : keyOR)
            if (this.heldKeys[key]) return true;
        return false;
    }

    public boolean isMouseDragging() {
        return dragging;
    }

    public boolean containsMouse(int x, int y, int width, int height) {
        if (!inWindow()) return false;
        if (x < getAbsoluteMouseX() && getAbsoluteMouseX() < x + width)
            if (y < getAbsoluteMouseY() && getAbsoluteMouseY() < y + height)
                return true;
        return false;
    }

    public boolean inWindow() {
        return inWindow;
    }

    public int getMouseX() {
        return mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }

    public int getMouseDragX() {
        return mouseDragX;
    }

    public int getMouseDragY() {
        return mouseDragY;
    }

    public int getAbsoluteMouseX() {
        return absoluteX;
    }

    public int getAbsoluteMouseY() {
        return absoluteY;
    }

    public int getMouseScreenX() {
        return mouseScreenX;
    }

    public int getMouseScreenY() {
        return mouseScreenY;
    }

    public int getMouseWheel() {
        int value = mouseWheel;
        mouseWheel = 0;
        return value;
    }

    private HashSet<TextReceiver> getTextReceivers() {
        return textReceivers;
    }

    private Click getClick(int button) {
        switch (button) {
            case 1:
                return Click.LEFT;
            case 2:
                return Click.MIDDLE;
            case 3:
                return Click.RIGHT;
            case 4:
                return Click.FOUR;
            case 5:
                return Click.FIVE;
        }
        return Click.OTHER;
    }

    public enum Click {
        LEFT, MIDDLE, RIGHT, FOUR, FIVE, OTHER
    }
}