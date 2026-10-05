package com.codealpha.gradetracker;

import com.codealpha.gradetracker.service.GradeTrackerService;
import com.codealpha.gradetracker.ui.StudentGradeTrackerConsole;
import com.codealpha.gradetracker.ui.StudentGradeTrackerGUI;

import javax.swing.*;
import java.awt.*;

/**
 * Entry point for CodeAlpha Student Grade Tracker application.
 */
public class Main {

    public static void main(String[] args) {
        GradeTrackerService service = new GradeTrackerService();
        service.loadSampleData(); // Pre-fill with clean sample data

        boolean forceConsole = false;
        for (String arg : args) {
            if ("--console".equalsIgnoreCase(arg) || "-c".equalsIgnoreCase(arg) || "--cli".equalsIgnoreCase(arg)) {
                forceConsole = true;
                break;
            }
        }

        if (forceConsole || GraphicsEnvironment.isHeadless()) {
            System.out.println("[INFO] Starting Student Grade Tracker in Console CLI Mode...");
            StudentGradeTrackerConsole consoleUI = new StudentGradeTrackerConsole(service);
            consoleUI.start();
        } else {
            SwingUtilities.invokeLater(() -> {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception ignored) {
                }

                StudentGradeTrackerGUI gui = new StudentGradeTrackerGUI(service);
                gui.setVisible(true);
            });
        }
    }
}
