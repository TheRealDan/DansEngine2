package me.therealdan.dansengine.window.input;

import me.therealdan.dansengine.window.input.controller.ArrowKeysController;
import me.therealdan.dansengine.window.input.controller.TextController;
import me.therealdan.dansengine.window.input.controller.WASDController;
import me.therealdan.dansengine.window.input.listener.*;

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

    private HashSet<ArrowKeysController> arrowKeysControllers = new HashSet<>();
    private HashSet<TextController> textControllers = new HashSet<>();
    private HashSet<WASDController> wasdControllers = new HashSet<>();

    private HashSet<KeyPressedListener> keyPressedListeners = new HashSet<>();
    private HashSet<KeyReleasedListener> keyReleasedListeners = new HashSet<>();
    private HashSet<KeyTypedListener> keyTypedListeners = new HashSet<>();
    private HashSet<MouseClickedListener> mouseClickedListeners = new HashSet<>();
    private HashSet<MouseDraggedListener> mouseDraggedListeners = new HashSet<>();
    private HashSet<MouseEnteredListener> mouseEnteredListeners = new HashSet<>();
    private HashSet<MouseExitedListener> mouseExitedListeners = new HashSet<>();
    private HashSet<MouseMovedListener> mouseMovedListeners = new HashSet<>();
    private HashSet<MousePressedListener> mousePressedListeners = new HashSet<>();
    private HashSet<MouseReleasedListener> mouseReleasedListeners = new HashSet<>();
    private HashSet<MouseWheelMovedListener> mouseWheelMovedListeners = new HashSet<>();

    @Override
    public void mousePressed(MouseEvent event) {
        Click click = byEvent(event);

        for (MousePressedListener mousePressedListener : mousePressedListeners)
            mousePressedListener.mousePressed(event, this, click);

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
        Click click = byEvent(event);

        for (MouseReleasedListener mouseReleasedListener : mouseReleasedListeners)
            mouseReleasedListener.mouseReleased(event, this, click);

        mouseReleased.put(click, false);
        mousePressed.put(click, false);

        mouseHeld.put(click, false);
    }

    @Override
    public void mouseClicked(MouseEvent event) {
        Click click = byEvent(event);

        for (MouseClickedListener mouseClickedListener : mouseClickedListeners)
            mouseClickedListener.mouseClicked(event, this, click);
    }

    @Override
    public void mouseMoved(MouseEvent event) {
        for (MouseMovedListener mouseMovedListener : mouseMovedListeners)
            mouseMovedListener.mouseMoved(event, this);

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
        for (MouseDraggedListener mouseDraggedListener : mouseDraggedListeners)
            mouseDraggedListener.mouseDragged(event, this);

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
        for (MouseEnteredListener mouseEnteredListener : mouseEnteredListeners)
            mouseEnteredListener.mouseEntered(event, this);

        inWindow = true;
    }

    @Override
    public void mouseExited(MouseEvent event) {
        for (MouseExitedListener mouseExitedListener : mouseExitedListeners)
            mouseExitedListener.mouseExcited(event, this);

        inWindow = false;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent event) {
        for (MouseWheelMovedListener mouseWheelMovedListener : mouseWheelMovedListeners)
            mouseWheelMovedListener.mouseWheelMoved(event, this);

        this.mouseWheel = event.getWheelRotation();
    }

    @Override
    public void keyPressed(KeyEvent event) {
        for (KeyPressedListener keyPressedListener : keyPressedListeners)
            keyPressedListener.keyPressed(event, this);

        if (!releasedKeys[event.getKeyCode()]) {
            releasedKeys[event.getKeyCode()] = true;
            pressedKeys[event.getKeyCode()] = true;
        }

        switch (event.getKeyCode()) {
            case KeyEvent.VK_W:
                for (WASDController wasdController : wasdControllers)
                    wasdController.up();
                break;
            case KeyEvent.VK_A:
                for (WASDController wasdController : wasdControllers)
                    wasdController.left();
                break;
            case KeyEvent.VK_S:
                for (WASDController wasdController : wasdControllers)
                    wasdController.down();
                break;
            case KeyEvent.VK_D:
                for (WASDController wasdController : wasdControllers)
                    wasdController.right();
                break;
        }

        switch (event.getKeyCode()) {
            case KeyEvent.VK_UP:
                for (ArrowKeysController arrowKeysController : arrowKeysControllers)
                    arrowKeysController.up();
                break;
            case KeyEvent.VK_LEFT:
                for (ArrowKeysController arrowKeysController : arrowKeysControllers)
                    arrowKeysController.left();
                break;
            case KeyEvent.VK_DOWN:
                for (ArrowKeysController arrowKeysController : arrowKeysControllers)
                    arrowKeysController.down();
                break;
            case KeyEvent.VK_RIGHT:
                for (ArrowKeysController arrowKeysController : arrowKeysControllers)
                    arrowKeysController.right();
                break;
        }

        switch (event.getKeyCode()) {
            case KeyEvent.VK_BACK_SPACE:
                for (TextController textController : getTextControllers())
                    textController.backspace();
                break;
            case KeyEvent.VK_ENTER:
                for (TextController textController : getTextControllers())
                    textController.enter();
                break;
        }

        switch (event.getKeyCode()) {
            case KeyEvent.VK_BACK_SPACE:
            case KeyEvent.VK_ENTER:
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
                break;
            default:
                for (TextController textController : getTextControllers())
                    textController.type(event.getKeyChar() + "");
        }
    }

    @Override
    public void keyReleased(KeyEvent event) {
        for (KeyReleasedListener keyReleasedListener : keyReleasedListeners)
            keyReleasedListener.keyReleased(event, this);

        pressedKeys[event.getKeyCode()] = false;
        releasedKeys[event.getKeyCode()] = false;

        heldKeys[event.getKeyCode()] = false;
    }

    @Override
    public void keyTyped(KeyEvent event) {
        for (KeyTypedListener keyTypedListener : keyTypedListeners)
            keyTypedListener.keyTyped(event, this);
    }

    public void register(ArrowKeysController arrowKeysController) {
        this.arrowKeysControllers.add(arrowKeysController);
    }

    public void register(TextController textController) {
        this.textControllers.add(textController);
    }

    public void register(WASDController wasdController) {
        this.wasdControllers.add(wasdController);
    }

    public void register(KeyPressedListener keyPressedListener) {
        keyPressedListeners.add(keyPressedListener);
    }

    public void register(KeyReleasedListener keyReleasedListener) {
        keyReleasedListeners.add(keyReleasedListener);
    }

    public void register(KeyTypedListener keyTypedListener) {
        keyTypedListeners.add(keyTypedListener);
    }

    public void register(MouseClickedListener mouseClickedListener) {
        mouseClickedListeners.add(mouseClickedListener);
    }

    public void register(MouseDraggedListener mouseDraggedListener) {
        mouseDraggedListeners.add(mouseDraggedListener);
    }

    public void register(MouseEnteredListener mouseEnteredListener) {
        mouseEnteredListeners.add(mouseEnteredListener);
    }

    public void register(MouseExitedListener mouseExitedListener) {
        mouseExitedListeners.add(mouseExitedListener);
    }

    public void register(MouseMovedListener mouseMovedListener) {
        mouseMovedListeners.add(mouseMovedListener);
    }

    public void register(MousePressedListener mousePressedListener) {
        mousePressedListeners.add(mousePressedListener);
    }

    public void register(MouseReleasedListener mouseReleasedListener) {
        mouseReleasedListeners.add(mouseReleasedListener);
    }

    public void register(MouseWheelMovedListener mouseWheelMovedListener) {
        mouseWheelMovedListeners.add(mouseWheelMovedListener);
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
        return (x < getAbsoluteMouseX() && getAbsoluteMouseX() < x + width) && (y < getAbsoluteMouseY() && getAbsoluteMouseY() < y + height);
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

    private HashSet<TextController> getTextControllers() {
        return textControllers;
    }

    public static Click byEvent(MouseEvent event) {
        switch (event.getButton()) {
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