package com.codealpha.gradetracker;

import com.codealpha.gradetracker.service.GradeTrackerService;
import com.codealpha.gradetracker.ui.cli.ConsoleInterface;
import com.codealpha.gradetracker.ui.gui.MainFrame;

import javax.swing.*;
import java.awt.GraphicsEnvironment;

/**
 * Entry point for CodeAlpha Student Grade Tracker application.
 */
public class Main {
    public static void main(String[] args) {
        boolean forceCli = false;

        for (String arg : args) {
            if ("--cli".equalsIgnoreCase(arg) || "-c".equalsIgnoreCase(arg)) {
                forceCli = true;
                break;
            }
        }

        GradeTrackerService service = new GradeTrackerService();

        boolean isHeadless = GraphicsEnvironment.isHeadless();

        if (forceCli || isHeadless) {
            ConsoleInterface cli = new ConsoleInterface(service);
            cli.start();
        } else {
            SwingUtilities.invokeLater(() -> {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception ignored) {
                }
                MainFrame frame = new MainFrame(service);
                frame.setVisible(true);
            });
        }
    }
}
