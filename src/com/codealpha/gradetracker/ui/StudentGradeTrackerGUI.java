package com.codealpha.gradetracker.ui;

import com.codealpha.gradetracker.model.GradeSummary;
import com.codealpha.gradetracker.model.Student;
import com.codealpha.gradetracker.service.GradeTrackerService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Modern Swing GUI for CodeAlpha Student Grade Tracker.
 */
public class StudentGradeTrackerGUI extends JFrame {

    private final GradeTrackerService service;
    private DefaultTableModel tableModel;
    private JTable studentTable;

    private JLabel lblTotalStudents;
    private JLabel lblClassAverage;
    private JLabel lblHighestScore;
    private JLabel lblLowestScore;

    // Color Palette
    private static final Color COLOR_PRIMARY = new Color(30, 41, 59);     // Dark Slate
    private static final Color COLOR_ACCENT = new Color(14, 165, 233);    // Vibrant Sky Blue
    private static final Color COLOR_BG = new Color(241, 245, 249);        // Light Gray/Blue
    private static final Color COLOR_CARD_BG = Color.WHITE;
    private static final Color COLOR_TEXT_MAIN = new Color(15, 23, 42);
    private static final Color COLOR_TEXT_MUTED = new Color(100, 116, 139);

    public StudentGradeTrackerGUI(GradeTrackerService service) {
        this.service = service;

        setTitle("CodeAlpha - Student Grade Tracker");
        setSize(1050, 680);
        setMinimumSize(new Dimension(850, 550));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initUI();
        refreshData();
    }

    private void initUI() {
        JPanel root = new JPanel(new BorderLayout(15, 15));
        root.setBackground(COLOR_BG);
        root.setBorder(new EmptyBorder(15, 15, 15, 15));
        setContentPane(root);

        // Header Panel
        JPanel headerPanel = createHeaderPanel();
        root.add(headerPanel, BorderLayout.NORTH);

        // Center Panel (Stats + Table)
        JPanel centerPanel = new JPanel(new BorderLayout(15, 15));
        centerPanel.setOpaque(false);

        // Metrics Row
        JPanel metricsPanel = createMetricsPanel();
        centerPanel.add(metricsPanel, BorderLayout.NORTH);

        // Table Panel
        JPanel tableContainer = createTablePanel();
        centerPanel.add(tableContainer, BorderLayout.CENTER);

        root.add(centerPanel, BorderLayout.CENTER);

        // Bottom Action Bar
        JPanel actionPanel = createActionPanel();
        root.add(actionPanel, BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_PRIMARY);
        panel.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel title = new JLabel("Student Grade Tracker");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel("CodeAlpha Internship Task 1 | Object-Oriented Grade Management");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitle.setForeground(new Color(203, 213, 225));

        JPanel titles = new JPanel(new GridLayout(2, 1, 0, 4));
        titles.setOpaque(false);
        titles.add(title);
        titles.add(subtitle);

        panel.add(titles, BorderLayout.WEST);
        return panel;
    }

    private JPanel createMetricsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 15, 0));
        panel.setOpaque(false);

        lblTotalStudents = new JLabel("0", SwingConstants.CENTER);
        lblClassAverage = new JLabel("0.00", SwingConstants.CENTER);
        lblHighestScore = new JLabel("0.00", SwingConstants.CENTER);
        lblLowestScore = new JLabel("0.00", SwingConstants.CENTER);

        panel.add(createStatCard("Total Students", lblTotalStudents, new Color(59, 130, 246)));
        panel.add(createStatCard("Class Average", lblClassAverage, new Color(16, 185, 129)));
        panel.add(createStatCard("Highest Score", lblHighestScore, new Color(139, 92, 246)));
        panel.add(createStatCard("Lowest Score", lblLowestScore, new Color(245, 158, 11)));

        return panel;
    }

    private JPanel createStatCard(String title, JLabel valueLabel, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBackground(COLOR_CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(4, 0, 0, 0, accentColor),
                new EmptyBorder(12, 15, 12, 15)
        ));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTitle.setForeground(COLOR_TEXT_MUTED);

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        valueLabel.setForeground(COLOR_TEXT_MAIN);

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_CARD_BG);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        String[] columns = {"ID", "Student Name", "Department", "Grades Count", "Average Score", "Highest", "Lowest", "Letter Grade", "GPA"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        studentTable = new JTable(tableModel);
        studentTable.setRowHeight(32);
        studentTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        studentTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        studentTable.getTableHeader().setBackground(COLOR_PRIMARY);
        studentTable.getTableHeader().setForeground(Color.WHITE);
        studentTable.getTableHeader().setPreferredSize(new Dimension(0, 36));
        studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < studentTable.getColumnCount(); i++) {
            if (i != 1 && i != 2) {
                studentTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
        }

        JScrollPane scrollPane = new JScrollPane(studentTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createActionPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        panel.setOpaque(false);

        JButton btnAddStudent = createButton("Add Student", COLOR_ACCENT, Color.WHITE);
        JButton btnAddGrade = createButton("Add Grade", new Color(16, 185, 129), Color.WHITE);
        JButton btnRemove = createButton("Remove Student", new Color(239, 68, 68), Color.WHITE);
        JButton btnLoadSample = createButton("Load Sample Data", new Color(100, 116, 139), Color.WHITE);
        JButton btnExportCSV = createButton("Export CSV", COLOR_PRIMARY, Color.WHITE);
        JButton btnExportReport = createButton("Export Report", COLOR_PRIMARY, Color.WHITE);

        btnAddStudent.addActionListener(e -> onAddStudent());
        btnAddGrade.addActionListener(e -> onAddGrade());
        btnRemove.addActionListener(e -> onRemoveStudent());
        btnLoadSample.addActionListener(e -> {
            service.loadSampleData();
            refreshData();
            JOptionPane.showMessageDialog(this, "Sample student dataset loaded successfully!", "Data Loaded", JOptionPane.INFORMATION_MESSAGE);
        });
        btnExportCSV.addActionListener(e -> onExportCSV());
        btnExportReport.addActionListener(e -> onExportReport());

        panel.add(btnLoadSample);
        panel.add(btnAddStudent);
        panel.add(btnAddGrade);
        panel.add(btnRemove);
        panel.add(btnExportCSV);
        panel.add(btnExportReport);

        return panel;
    }

    private JButton createButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));
        return btn;
    }

    private void refreshData() {
        tableModel.setRowCount(0);
        List<Student> students = service.getStudents();

        for (Student s : students) {
            Object[] row = {
                    s.getId(),
                    s.getName(),
                    s.getDepartment(),
                    s.getGrades().size(),
                    String.format("%.2f", s.getAverageScore()),
                    String.format("%.2f", s.getHighestScore()),
                    String.format("%.2f", s.getLowestScore()),
                    s.getLetterGrade(),
                    String.format("%.1f", s.getGPA())
            };
            tableModel.addRow(row);
        }

        GradeSummary summary = service.calculateSummary();
        lblTotalStudents.setText(String.valueOf(summary.getTotalStudents()));
        lblClassAverage.setText(String.format("%.2f", summary.getClassAverage()));
        lblHighestScore.setText(String.format("%.2f", summary.getClassHighestScore()));
        lblLowestScore.setText(String.format("%.2f", summary.getClassLowestScore()));
    }

    private void onAddStudent() {
        JTextField txtId = new JTextField();
        JTextField txtName = new JTextField();
        JTextField txtDept = new JTextField();

        Object[] message = {
                "Student ID (e.g. STU-101):", txtId,
                "Student Full Name:", txtName,
                "Department / Major:", txtDept
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Add New Student", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String id = txtId.getText().trim();
            String name = txtName.getText().trim();
            String dept = txtDept.getText().trim();

            if (id.isEmpty() || name.isEmpty() || dept.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required!", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                service.addStudent(new Student(id, name, dept));
                refreshData();
                JOptionPane.showMessageDialog(this, "Student added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void onAddGrade() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a student from the table first.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String studentId = (String) tableModel.getValueAt(selectedRow, 0);
        String studentName = (String) tableModel.getValueAt(selectedRow, 1);

        String input = JOptionPane.showInputDialog(this, "Enter grade score (0.0 to 100.0) for " + studentName + " (" + studentId + "):");
        if (input != null && !input.trim().isEmpty()) {
            try {
                double grade = Double.parseDouble(input.trim());
                if (grade < 0 || grade > 100) {
                    JOptionPane.showMessageDialog(this, "Grade must be between 0 and 100.", "Invalid Grade", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                service.addGradeToStudent(studentId, grade);
                refreshData();
                JOptionPane.showMessageDialog(this, "Grade added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid numeric grade.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void onRemoveStudent() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a student to remove.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String studentId = (String) tableModel.getValueAt(selectedRow, 0);
        String studentName = (String) tableModel.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to remove " + studentName + " (" + studentId + ")?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            service.removeStudent(studentId);
            refreshData();
        }
    }

    private void onExportCSV() {
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File("student_grades.csv"));
        int userSelection = chooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = chooser.getSelectedFile();
            try {
                service.exportToCSV(fileToSave.getAbsolutePath());
                JOptionPane.showMessageDialog(this, "Exported successfully to " + fileToSave.getAbsolutePath(), "Export Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Failed to export CSV: " + e.getMessage(), "Export Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void onExportReport() {
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File("grade_summary_report.txt"));
        int userSelection = chooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = chooser.getSelectedFile();
            try {
                service.exportSummaryReport(fileToSave.getAbsolutePath());
                JOptionPane.showMessageDialog(this, "Summary report saved to " + fileToSave.getAbsolutePath(), "Export Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Failed to export report: " + e.getMessage(), "Export Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
