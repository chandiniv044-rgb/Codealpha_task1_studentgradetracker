package com.codealpha.gradetracker.ui.gui;

import com.codealpha.gradetracker.model.GradeReport;
import com.codealpha.gradetracker.model.Student;
import com.codealpha.gradetracker.service.GradeTrackerService;
import com.codealpha.gradetracker.util.FileExportUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Modern Java Swing GUI Main Dashboard for Student Grade Tracker.
 */
public class MainFrame extends JFrame {
    private final GradeTrackerService service;

    private JLabel lblTotalStudents;
    private JLabel lblClassAvg;
    private JLabel lblHighestScore;
    private JLabel lblLowestScore;
    private JLabel lblPassRate;

    private JTable tableStudents;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;

    private JPanel panelGradeBar;

    // Colors for modern UI
    private static final Color DARK_BG = new Color(24, 28, 36);
    private static final Color CARD_BG = new Color(34, 40, 52);
    private static final Color ACCENT_PRIMARY = new Color(99, 102, 241);
    private static final Color ACCENT_SUCCESS = new Color(34, 197, 94);
    private static final Color ACCENT_WARNING = new Color(245, 158, 11);
    private static final Color ACCENT_DANGER = new Color(239, 68, 68);
    private static final Color TEXT_MAIN = new Color(243, 244, 246);
    private static final Color TEXT_MUTED = new Color(156, 163, 175);

    public MainFrame(GradeTrackerService service) {
        this.service = service;
        setTitle("CodeAlpha - Student Grade Tracker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1050, 700);
        setMinimumSize(new Dimension(850, 550));
        setLocationRelativeTo(null);

        // Load sample data by default for immediate visual feedback
        if (service.getStudentCount() == 0) {
            service.loadSampleData();
        }

        initComponents();
        refreshDashboard();
    }

    private void initComponents() {
        JPanel rootPanel = new JPanel(new BorderLayout(15, 15));
        rootPanel.setBackground(DARK_BG);
        rootPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // 1. Header Panel
        JPanel headerPanel = createHeaderPanel();
        rootPanel.add(headerPanel, BorderLayout.NORTH);

        // 2. Center Panel (Metrics + Table)
        JPanel centerPanel = new JPanel(new BorderLayout(15, 15));
        centerPanel.setOpaque(false);

        // Cards layout
        JPanel metricsPanel = createMetricsPanel();
        centerPanel.add(metricsPanel, BorderLayout.NORTH);

        // Table & Search Layout
        JPanel tableContainer = createTableContainer();
        centerPanel.add(tableContainer, BorderLayout.CENTER);

        rootPanel.add(centerPanel, BorderLayout.CENTER);

        // 3. Right Analytics Panel
        JPanel sidePanel = createSideAnalyticsPanel();
        rootPanel.add(sidePanel, BorderLayout.EAST);

        setContentPane(rootPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel title = new JLabel("Student Grade Tracker");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(TEXT_MAIN);

        JLabel subtitle = new JLabel("CodeAlpha Internship Project - Task 1");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(TEXT_MUTED);

        JPanel textGroup = new JPanel(new GridLayout(2, 1, 0, 2));
        textGroup.setOpaque(false);
        textGroup.add(title);
        textGroup.add(subtitle);

        header.add(textGroup, BorderLayout.WEST);

        // Quick action buttons
        JPanel actionGroup = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actionGroup.setOpaque(false);

        JButton btnAddStudent = createStyledButton("+ Add Student", ACCENT_PRIMARY);
        btnAddStudent.addActionListener(e -> onAddStudent());

        JButton btnAddGrade = createStyledButton("+ Add Grade", new Color(14, 165, 233));
        btnAddGrade.addActionListener(e -> onAddGradeToStudent());

        JButton btnExport = createStyledButton("Export Report", ACCENT_SUCCESS);
        btnExport.addActionListener(e -> onExportReport());

        actionGroup.add(btnAddStudent);
        actionGroup.add(btnAddGrade);
        actionGroup.add(btnExport);

        header.add(actionGroup, BorderLayout.EAST);

        return header;
    }

    private JPanel createMetricsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 5, 12, 0));
        panel.setOpaque(false);

        lblTotalStudents = new JLabel("0", SwingConstants.CENTER);
        lblClassAvg = new JLabel("0.00", SwingConstants.CENTER);
        lblHighestScore = new JLabel("0.00", SwingConstants.CENTER);
        lblLowestScore = new JLabel("0.00", SwingConstants.CENTER);
        lblPassRate = new JLabel("0.0%", SwingConstants.CENTER);

        panel.add(createMetricCard("TOTAL STUDENTS", lblTotalStudents, ACCENT_PRIMARY));
        panel.add(createMetricCard("CLASS AVERAGE", lblClassAvg, new Color(14, 165, 233)));
        panel.add(createMetricCard("HIGHEST SCORE", lblHighestScore, ACCENT_SUCCESS));
        panel.add(createMetricCard("LOWEST SCORE", lblLowestScore, ACCENT_DANGER));
        panel.add(createMetricCard("PASS RATE", lblPassRate, ACCENT_WARNING));

        return panel;
    }

    private JPanel createMetricCard(String title, JLabel valueLabel, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 4, 0, 0, accentColor),
                new EmptyBorder(12, 12, 12, 12)
        ));

        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        titleLbl.setForeground(TEXT_MUTED);

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        valueLabel.setForeground(TEXT_MAIN);

        card.add(titleLbl, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createTableContainer() {
        JPanel container = new JPanel(new BorderLayout(10, 10));
        container.setOpaque(false);

        // Search & Filter Toolbar
        JPanel toolbar = new JPanel(new BorderLayout());
        toolbar.setOpaque(false);

        txtSearch = new JTextField();
        txtSearch.setPreferredSize(new Dimension(250, 32));
        txtSearch.setToolTipText("Search student by ID or Name...");
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filterTable(txtSearch.getText());
            }
        });

        JLabel searchLabel = new JLabel("Search: ");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        searchLabel.setForeground(TEXT_MAIN);

        JPanel searchGroup = new JPanel(new BorderLayout(5, 0));
        searchGroup.setOpaque(false);
        searchGroup.add(searchLabel, BorderLayout.WEST);
        searchGroup.add(txtSearch, BorderLayout.CENTER);

        toolbar.add(searchGroup, BorderLayout.WEST);

        // Action Toolbar
        JPanel tableBtnGroup = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        tableBtnGroup.setOpaque(false);

        JButton btnDelete = createStyledButton("Delete Selected", ACCENT_DANGER);
        btnDelete.addActionListener(e -> onDeleteSelected());

        JButton btnReset = createStyledButton("Load Sample Data", new Color(107, 114, 128));
        btnReset.addActionListener(e -> {
            service.loadSampleData();
            refreshDashboard();
        });

        tableBtnGroup.add(btnDelete);
        tableBtnGroup.add(btnReset);

        toolbar.add(tableBtnGroup, BorderLayout.EAST);

        container.add(toolbar, BorderLayout.NORTH);

        // Table
        String[] columns = {"ID", "Student Name", "Grades Count", "Avg Score", "Highest", "Lowest", "Grade", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableStudents = new JTable(tableModel);
        tableStudents.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tableStudents.setRowHeight(32);
        tableStudents.setBackground(CARD_BG);
        tableStudents.setForeground(TEXT_MAIN);
        tableStudents.setGridColor(new Color(55, 65, 81));
        tableStudents.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tableStudents.getTableHeader().setBackground(new Color(45, 53, 70));
        tableStudents.getTableHeader().setForeground(TEXT_MAIN);

        // Custom Cell Renderer for Grade Status
        tableStudents.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? CARD_BG : new Color(28, 33, 44));
                    c.setForeground(TEXT_MAIN);
                }
                if (column == 7) { // Status column
                    String valStr = String.valueOf(value);
                    if ("PASSED".equalsIgnoreCase(valStr)) {
                        c.setForeground(ACCENT_SUCCESS);
                    } else if ("FAILED".equalsIgnoreCase(valStr)) {
                        c.setForeground(ACCENT_DANGER);
                    }
                }
                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(tableStudents);
        scrollPane.getViewport().setBackground(CARD_BG);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(55, 65, 81)));

        container.add(scrollPane, BorderLayout.CENTER);

        return container;
    }

    private JPanel createSideAnalyticsPanel() {
        JPanel side = new JPanel();
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
        side.setPreferredSize(new Dimension(240, 0));
        side.setBackground(CARD_BG);
        side.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel lblHeader = new JLabel("Grade Breakdown");
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblHeader.setForeground(TEXT_MAIN);
        lblHeader.setAlignmentX(Component.LEFT_ALIGNMENT);

        side.add(lblHeader);
        side.add(Box.createRigidArea(new Dimension(0, 15)));

        panelGradeBar = new JPanel();
        panelGradeBar.setLayout(new BoxLayout(panelGradeBar, BoxLayout.Y_AXIS));
        panelGradeBar.setOpaque(false);
        panelGradeBar.setAlignmentX(Component.LEFT_ALIGNMENT);

        side.add(panelGradeBar);

        return side;
    }

    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(8, 14, 8, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public void refreshDashboard() {
        tableModel.setRowCount(0);
        List<Student> list = service.getAllStudents();
        for (Student s : list) {
            tableModel.addRow(new Object[]{
                    s.getId(),
                    s.getName(),
                    s.getGrades().size(),
                    String.format("%.2f", s.getAverageScore()),
                    String.format("%.2f", s.getHighestScore()),
                    String.format("%.2f", s.getLowestScore()),
                    s.getLetterGrade(),
                    s.isPassed() ? "PASSED" : "FAILED"
            });
        }

        GradeReport report = service.generateReport();
        lblTotalStudents.setText(String.valueOf(report.getTotalStudents()));
        lblClassAvg.setText(String.format("%.2f", report.getClassAverage()));
        lblHighestScore.setText(String.format("%.2f", report.getOverallHighestScore()));
        lblLowestScore.setText(String.format("%.2f", report.getOverallLowestScore()));
        lblPassRate.setText(String.format("%.1f%%", report.getPassRate()));

        updateGradeBar(report);
    }

    private void filterTable(String text) {
        tableModel.setRowCount(0);
        List<Student> list = service.searchByName(text);
        for (Student s : list) {
            tableModel.addRow(new Object[]{
                    s.getId(),
                    s.getName(),
                    s.getGrades().size(),
                    String.format("%.2f", s.getAverageScore()),
                    String.format("%.2f", s.getHighestScore()),
                    String.format("%.2f", s.getLowestScore()),
                    s.getLetterGrade(),
                    s.isPassed() ? "PASSED" : "FAILED"
            });
        }
    }

    private void updateGradeBar(GradeReport report) {
        panelGradeBar.removeAll();

        int total = report.getTotalStudents();
        addGradeBarItem("Grade A (90-100)", report.getCountA(), total, ACCENT_SUCCESS);
        addGradeBarItem("Grade B (80-89)", report.getCountB(), total, new Color(14, 165, 233));
        addGradeBarItem("Grade C (70-79)", report.getCountC(), total, ACCENT_WARNING);
        addGradeBarItem("Grade D (60-69)", report.getCountD(), total, new Color(249, 115, 22));
        addGradeBarItem("Grade F (<60)", report.getCountF(), total, ACCENT_DANGER);

        panelGradeBar.revalidate();
        panelGradeBar.repaint();
    }

    private void addGradeBarItem(String label, int count, int total, Color color) {
        JPanel item = new JPanel(new BorderLayout(5, 5));
        item.setOpaque(false);
        item.setMaximumSize(new Dimension(210, 45));

        int percent = (total > 0) ? (int) Math.round(((double) count / total) * 100) : 0;

        JLabel title = new JLabel(String.format("%s: %d (%d%%)", label, count, percent));
        title.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        title.setForeground(TEXT_MAIN);

        JProgressBar bar = new JProgressBar(0, 100);
        bar.setValue(percent);
        bar.setForeground(color);
        bar.setBackground(new Color(55, 65, 81));
        bar.setBorderPainted(false);
        bar.setPreferredSize(new Dimension(200, 8));

        item.add(title, BorderLayout.NORTH);
        item.add(bar, BorderLayout.CENTER);

        panelGradeBar.add(item);
        panelGradeBar.add(Box.createRigidArea(new Dimension(0, 10)));
    }

    private void onAddStudent() {
        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField gradesField = new JTextField();

        Object[] message = {
                "Student ID (e.g. STU107):", idField,
                "Student Name:", nameField,
                "Initial Grades (comma separated, e.g. 85,90,78):", gradesField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Add New Student", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            if (id.isEmpty() || name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Student ID and Name cannot be empty!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Student newStudent = new Student(id, name);
            String gradesInput = gradesField.getText().trim();
            if (!gradesInput.isEmpty()) {
                String[] parts = gradesInput.split(",");
                for (String p : parts) {
                    try {
                        double val = Double.parseDouble(p.trim());
                        newStudent.addGrade(val);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Invalid grade value ignored: " + p, "Warning", JOptionPane.WARNING_MESSAGE);
                    } catch (IllegalArgumentException ex) {
                        JOptionPane.showMessageDialog(this, ex.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }

            if (service.addStudent(newStudent)) {
                refreshDashboard();
                JOptionPane.showMessageDialog(this, "Student added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Student with ID '" + id + "' already exists!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void onAddGradeToStudent() {
        int selectedRow = tableStudents.getSelectedRow();
        String defaultId = "";
        if (selectedRow != -1) {
            defaultId = tableModel.getValueAt(selectedRow, 0).toString();
        }

        String id = JOptionPane.showInputDialog(this, "Enter Student ID to add grade to:", defaultId);
        if (id == null || id.trim().isEmpty()) return;

        Optional<Student> studentOpt = service.findById(id.trim());
        if (studentOpt.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Student with ID '" + id + "' not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String gradeStr = JOptionPane.showInputDialog(this, "Enter Numerical Grade (0.0 - 100.0) for " + studentOpt.get().getName() + ":");
        if (gradeStr == null || gradeStr.trim().isEmpty()) return;

        try {
            double grade = Double.parseDouble(gradeStr.trim());
            if (service.addGradeToStudent(id.trim(), grade)) {
                refreshDashboard();
                JOptionPane.showMessageDialog(this, "Grade added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number!", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onDeleteSelected() {
        int selectedRow = tableStudents.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a student from the table to delete.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String id = tableModel.getValueAt(selectedRow, 0).toString();
        String name = tableModel.getValueAt(selectedRow, 1).toString();

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete student: " + name + " (" + id + ")?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            service.deleteStudent(id);
            refreshDashboard();
        }
    }

    private void onExportReport() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Grade Summary Report");
        fileChooser.setSelectedFile(new File("GradeReport_Summary.txt"));

        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            try {
                FileExportUtil.exportSummaryReport(fileToSave, service.getAllStudents(), service.generateReport());

                // Also generate CSV in same dir
                File csvFile = new File(fileToSave.getParentFile(), "Students_Grades.csv");
                FileExportUtil.exportToCSV(csvFile, service.getAllStudents());

                JOptionPane.showMessageDialog(this,
                        "Report exported successfully!\n- Text Report: " + fileToSave.getAbsolutePath() + "\n- CSV File: " + csvFile.getAbsolutePath(),
                        "Export Complete", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Failed to export report: " + ex.getMessage(), "Export Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
