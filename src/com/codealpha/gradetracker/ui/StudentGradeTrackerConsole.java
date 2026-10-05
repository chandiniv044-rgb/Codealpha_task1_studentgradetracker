package com.codealpha.gradetracker.ui;

import com.codealpha.gradetracker.model.GradeSummary;
import com.codealpha.gradetracker.model.Student;
import com.codealpha.gradetracker.service.GradeTrackerService;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

/**
 * Interactive Console / Terminal interface for Student Grade Tracker.
 */
public class StudentGradeTrackerConsole {
    private final GradeTrackerService service;
    private final Scanner scanner;

    public StudentGradeTrackerConsole(GradeTrackerService service) {
        this.service = service;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        printHeader();
        boolean running = true;

        while (running) {
            printMenu();
            System.out.print("Enter choice [1-8]: ");
            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    displayAllStudents();
                    break;
                case "2":
                    addStudent();
                    break;
                case "3":
                    addGrade();
                    break;
                case "4":
                    displayClassSummary();
                    break;
                case "5":
                    removeStudent();
                    break;
                case "6":
                    service.loadSampleData();
                    System.out.println("\n[SUCCESS] Sample student dataset loaded successfully!\n");
                    break;
                case "7":
                    exportReport();
                    break;
                case "8":
                    running = false;
                    System.out.println("\nThank you for using CodeAlpha Student Grade Tracker! Exiting...");
                    break;
                default:
                    System.out.println("\n[ERROR] Invalid option! Please select a number between 1 and 8.\n");
            }
        }
    }

    private void printHeader() {
        System.out.println("==================================================================");
        System.out.println("            CODEALPHA JAVA TASK 1: STUDENT GRADE TRACKER           ");
        System.out.println("==================================================================");
    }

    private void printMenu() {
        System.out.println("------------------------------------------------------------------");
        System.out.println(" 1. View All Students & Grades");
        System.out.println(" 2. Add New Student");
        System.out.println(" 3. Add Grade to Student");
        System.out.println(" 4. View Class Statistics & Summary");
        System.out.println(" 5. Remove Student");
        System.out.println(" 6. Load Sample Dataset");
        System.out.println(" 7. Export Class Summary Report to File");
        System.out.println(" 8. Exit");
        System.out.println("------------------------------------------------------------------");
    }

    private void displayAllStudents() {
        if (service.getStudents().isEmpty()) {
            System.out.println("\n[INFO] No student records found. Select option 2 or 6 to populate data.\n");
            return;
        }

        System.out.println("\n=======================================================================================================");
        System.out.printf("%-10s | %-20s | %-16s | %-8s | %-7s | %-7s | %-7s | %-5s | %-4s\n",
                "ID", "Name", "Department", "Count", "Average", "Max", "Min", "Grade", "GPA");
        System.out.println("-------------------------------------------------------------------------------------------------------");

        for (Student s : service.getStudents()) {
            System.out.printf("%-10s | %-20s | %-16s | %-8d | %-7.2f | %-7.2f | %-7.2f | %-5s | %-4.1f\n",
                    s.getId(),
                    s.getName(),
                    s.getDepartment(),
                    s.getGrades().size(),
                    s.getAverageScore(),
                    s.getHighestScore(),
                    s.getLowestScore(),
                    s.getLetterGrade(),
                    s.getGPA());
        }
        System.out.println("=======================================================================================================\n");
    }

    private void addStudent() {
        System.out.print("Enter Student ID (e.g., STU-101): ");
        String id = scanner.nextLine().trim();

        System.out.print("Enter Student Full Name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Enter Department / Major: ");
        String dept = scanner.nextLine().trim();

        if (id.isEmpty() || name.isEmpty() || dept.isEmpty()) {
            System.out.println("\n[ERROR] Fields cannot be empty.\n");
            return;
        }

        try {
            service.addStudent(new Student(id, name, dept));
            System.out.println("\n[SUCCESS] Student " + name + " (" + id + ") added successfully!\n");
        } catch (Exception e) {
            System.out.println("\n[ERROR] " + e.getMessage() + "\n");
        }
    }

    private void addGrade() {
        System.out.print("Enter Student ID: ");
        String id = scanner.nextLine().trim();

        Optional<Student> studentOpt = service.findStudentById(id);
        if (!studentOpt.isPresent()) {
            System.out.println("\n[ERROR] Student with ID '" + id + "' not found.\n");
            return;
        }

        Student student = studentOpt.get();
        System.out.print("Enter grade score (0.0 - 100.0) for " + student.getName() + ": ");
        String input = scanner.nextLine().trim();

        try {
            double grade = Double.parseDouble(input);
            service.addGradeToStudent(id, grade);
            System.out.printf("\n[SUCCESS] Added grade %.2f to %s. New Average: %.2f\n\n",
                    grade, student.getName(), student.getAverageScore());
        } catch (NumberFormatException e) {
            System.out.println("\n[ERROR] Invalid grade input. Please enter a valid number.\n");
        } catch (IllegalArgumentException e) {
            System.out.println("\n[ERROR] " + e.getMessage() + "\n");
        }
    }

    private void displayClassSummary() {
        GradeSummary summary = service.calculateSummary();

        System.out.println("\n==================================================");
        System.out.println("            CLASS PERFORMANCE SUMMARY             ");
        System.out.println("==================================================");
        System.out.printf(" Total Students Enrolled:  %d\n", summary.getTotalStudents());
        System.out.printf(" Total Grades Recorded:    %d\n", summary.getTotalGradesRecorded());
        System.out.printf(" Class Average Score:      %.2f\n", summary.getClassAverage());
        System.out.printf(" Class Highest Score:      %.2f\n", summary.getClassHighestScore());
        System.out.printf(" Class Lowest Score:       %.2f\n", summary.getClassLowestScore());

        if (summary.getTopPerformingStudent() != null) {
            System.out.printf(" Top Performer:            %s (%s) [Avg: %.2f]\n",
                    summary.getTopPerformingStudent().getName(),
                    summary.getTopPerformingStudent().getId(),
                    summary.getTopPerformingStudent().getAverageScore());
        }

        System.out.println("\nGRADE DISTRIBUTION:");
        for (Map.Entry<String, Integer> entry : summary.getGradeDistribution().entrySet()) {
            System.out.printf("  %-12s : %d\n", entry.getKey(), entry.getValue());
        }
        System.out.println("==================================================\n");
    }

    private void removeStudent() {
        System.out.print("Enter Student ID to remove: ");
        String id = scanner.nextLine().trim();

        if (service.removeStudent(id)) {
            System.out.println("\n[SUCCESS] Student '" + id + "' removed successfully.\n");
        } else {
            System.out.println("\n[ERROR] Student ID '" + id + "' not found.\n");
        }
    }

    private void exportReport() {
        String fileName = "grade_summary_report.txt";
        try {
            service.exportSummaryReport(fileName);
            System.out.println("\n[SUCCESS] Summary report generated: " + fileName + "\n");
        } catch (IOException e) {
            System.out.println("\n[ERROR] Failed to write report file: " + e.getMessage() + "\n");
        }
    }
}
