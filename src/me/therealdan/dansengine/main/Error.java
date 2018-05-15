package me.therealdan.dansengine.main;

import javax.swing.*;
import java.awt.*;

public class Error {

    private static int MAX_TO_DISPLAY = 16;
    private static int TOTAL = 0;
    private static boolean SHOW_ERRORS = true;

    public Error(Exception exception) {
        this(exception, "");
    }

    public Error(String message) {
        this(new Exception("NoException"), message);
    }

    public Error(Exception exception, String message) {
        TOTAL++;

        if (!showErrors()) return;
        if (getTotal() > getMax()) return;

        JFrame frame = new JFrame("Error " + getTotal() + " - " + exception.getMessage() + " - " + message);

        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setMinimumSize(new Dimension(800, 600));
        frame.setMaximumSize(Toolkit.getDefaultToolkit().getScreenSize());
        frame.setLocationRelativeTo(null);
        frame.setResizable(true);

        int y = 20;
        for (StackTraceElement stackTraceElement : exception.getStackTrace()) {
            Label label = new Label();
            label.setText(stackTraceElement.toString());
            label.setBounds(20, y += 20, (int) frame.getMaximumSize().getWidth(), 20);
            frame.add(label);
        }

        frame.setVisible(true);
    }

    public static void setMax(int maxErrorsToDisplay) {
        MAX_TO_DISPLAY = maxErrorsToDisplay;
    }

    public static void show(boolean showErrors) {
        SHOW_ERRORS = showErrors;
    }

    public static int getMax() {
        return MAX_TO_DISPLAY;
    }

    public static int getTotal() {
        return TOTAL;
    }

    public static boolean showErrors() {
        return SHOW_ERRORS;
    }
}