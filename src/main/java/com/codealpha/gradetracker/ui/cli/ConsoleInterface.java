package com.codealpha.gradetracker.ui.cli;

import com.codealpha.gradetracker.model.GradeReport;
import com.codealpha.gradetracker.model.Student;
import com.codealpha.gradetracker.service.GradeTrackerService;
import com.codealpha.gradetracker.util.FileExportUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Console-based menu interface for Student Grade Tracker.
 */
public class ConsoleInterface {
    private final GradeTrackerService service;
    private final Scanner scanner;

    public ConsoleInterface(GradeTrackerService service) {
        this.service = service;
        this.scanner = new Scanner(System.in);
        if (service.getStudentCount() == 0) {
            service.loadSampleData();
        }
    }

    public void start() {
        boolean running = true;
        System.out.println("=========================================================");
        System.out.println("    WELCOME TO CODEALPHA STUDENT GRADE TRACKER (CLI)     ");
        System.out.println("=========================================================");

        while (running) {
            printMenu();
            System.out.print("Select an option (1-9): ");
            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    displayAllStudents();
                    break;
                case "2":
                    addNewStudent();
                    break;
                case "3":
                    addGradeToStudent();
                    break;
                case "4":
                    displaySummaryReport();
                    break;
                case "5":
                    searchStudent();
                    break;
                case "6":
                    deleteStudent();
                    break;
                case "7":
                    exportReportToFile();
                    break;
                case "8":
                    service.loadSampleData();
                    System.out.println("[INFO] Sample student records reloaded successfully.");
                    break;
                case "9":
                    running = false;
                    System.out.println("Exiting Student Grade Tracker. Thank you!");
                    break;
                default:
                    System.out.println("[ERROR] Invalid choice. Please enter a number between 1 and 9.");
            }
            System.out.println();
        }
    }

    private void printMenu() {
        System.out.println("---------------------------------------------------------");
        System.out.println("  1. View All Students & Grades");
        System.out.println("  2. Add New Student");
        System.out.println("  3. Add Grade to Existing Student");
        System.out.println("  4. View Class Summary & Statistics Report");
        System.out.println("  5. Search Student by ID / Name");
        System.out.println("  6. Delete Student Record");
        System.out.println("  7. Export Report & Data to File");
        System.out.println("  8. Load Sample Data");
        System.out.println("  9. Exit CLI");
        System.out.println("---------------------------------------------------------");
    }

    private void displayAllStudents() {
        List<Student> students = service.getAllStudents();
        if (students.isEmpty()) {
            System.out.println("[INFO] No student records found.");
            return;
        }

        System.out.println("\n--- ALL STUDENT RECORDS ---");
        System.out.printf("%-10s %-20s %-12s %-10s %-10s %-10s %-6s%n",
                "ID", "Name", "Grades", "Avg Score", "Highest", "Lowest", "Grade");
        System.out.println("-----------------------------------------------------------------------------");
        for (Student s : students) {
            System.out.printf("%-10s %-20s %-12s %-10.2f %-10.2f %-10.2f %-6s%n",
                    s.getId(),
                    s.getName(),
                    s.getGrades().toString(),
                    s.getAverageScore(),
                    s.getHighestScore(),
                    s.getLowestScore(),
                    s.getLetterGrade());
        }
    }

    private void addNewStudent() {
        System.out.print("Enter Student ID (e.g., STU107): ");
        String id = scanner.nextLine().trim();
        if (id.isEmpty()) {
            System.out.println("[ERROR] Student ID cannot be empty.");
            return;
        }

        if (service.findById(id).isPresent()) {
            System.out.println("[ERROR] Student ID already exists.");
            return;
        }

        System.out.print("Enter Student Name: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("[ERROR] Student Name cannot be empty.");
            return;
        }

        Student student = new Student(id, name);
        System.out.print("Enter initial numerical grades separated by space/comma (optional, or press Enter): ");
        String gradesLine = scanner.nextLine().trim();
        if (!gradesLine.isEmpty()) {
            String[] tokens = gradesLine.split("[,\\s]+");
            for (String t : tokens) {
                try {
                    double g = Double.parseDouble(t);
                    student.addGrade(g);
                } catch (NumberFormatException e) {
                    System.out.println("[WARN] Skipping invalid numerical grade: " + t);
                } catch (IllegalArgumentException e) {
                    System.out.println("[WARN] " + e.getMessage());
                }
            }
        }

        if (service.addStudent(student)) {
            System.out.println("[SUCCESS] Student '" + name + "' (" + id + ") added.");
        }
    }

    private void addGradeToStudent() {
        System.out.print("Enter Student ID: ");
        String id = scanner.nextLine().trim();
        Optional<Student> studentOpt = service.findById(id);
        if (studentOpt.isEmpty()) {
            System.out.println("[ERROR] Student not found with ID: " + id);
            return;
        }

        Student s = studentOpt.get();
        System.out.printf("Adding grade for %s (%s). Current Avg: %.2f%n", s.getName(), s.getId(), s.getAverageScore());
        System.out.print("Enter Numerical Score (0.0 - 100.0): ");
        String input = scanner.nextLine().trim();
        try {
            double score = Double.parseDouble(input);
            s.addGrade(score);
            System.out.printf("[SUCCESS] Added score %.2f. Updated Avg: %.2f (Grade: %s)%n",
                    score, s.getAverageScore(), s.getLetterGrade());
        } catch (NumberFormatException e) {
            System.out.println("[ERROR] Invalid score entered.");
        } catch (IllegalArgumentException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    private void displaySummaryReport() {
        GradeReport report = service.generateReport();
        System.out.println("\n=========================================================");
        System.out.println("                 CLASS SUMMARY REPORT                    ");
        System.out.println("=========================================================");
        System.out.printf("Total Students    : %d%n", report.getTotalStudents());
        System.out.printf("Class Average     : %.2f%n", report.getClassAverage());
        System.out.printf("Highest Score     : %.2f%n", report.getOverallHighestScore());
        System.out.printf("Lowest Score      : %.2f%n", report.getOverallLowestScore());
        System.out.printf("Pass Rate         : %.1f%%%n", report.getPassRate());
        System.out.println("---------------------------------------------------------");
        System.out.println("Grade Distribution:");
        System.out.printf("  Grade A (90-100) : %d student(s)%n", report.getCountA());
        System.out.printf("  Grade B (80-89)  : %d student(s)%n", report.getCountB());
        System.out.printf("  Grade C (70-79)  : %d student(s)%n", report.getCountC());
        System.out.printf("  Grade D (60-69)  : %d student(s)%n", report.getCountD());
        System.out.printf("  Grade F (<60)    : %d student(s)%n", report.getCountF());
        System.out.println("---------------------------------------------------------");
        System.out.println("Top Performing Student(s):");
        for (Student top : report.getTopStudents()) {
            System.out.printf("  - %s (%s) with Avg: %.2f%n", top.getName(), top.getId(), top.getAverageScore());
        }
        System.out.println("=========================================================");
    }

    private void searchStudent() {
        System.out.print("Enter ID or Name query: ");
        String q = scanner.nextLine().trim();
        List<Student> results = service.searchByName(q);
        if (results.isEmpty()) {
            System.out.println("[INFO] No matching student records.");
            return;
        }

        System.out.println("\n--- SEARCH RESULTS ---");
        for (Student s : results) {
            System.out.println(s);
        }
    }

    private void deleteStudent() {
        System.out.print("Enter Student ID to delete: ");
        String id = scanner.nextLine().trim();
        if (service.deleteStudent(id)) {
            System.out.println("[SUCCESS] Student deleted.");
        } else {
            System.out.println("[ERROR] Student ID not found.");
        }
    }

    private void exportReportToFile() {
        File txtFile = new File("GradeReport_Summary.txt");
        File csvFile = new File("Students_Grades.csv");
        try {
            FileExportUtil.exportSummaryReport(txtFile, service.getAllStudents(), service.generateReport());
            FileExportUtil.exportToCSV(csvFile, service.getAllStudents());
            System.out.println("[SUCCESS] Exported summary report to: " + txtFile.getAbsolutePath());
            System.out.println("[SUCCESS] Exported CSV data to: " + csvFile.getAbsolutePath());
        } catch (IOException e) {
            System.out.println("[ERROR] Failed to export report: " + e.getMessage());
        }
    }
}
