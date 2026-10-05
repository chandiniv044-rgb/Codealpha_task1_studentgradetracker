package com.codealpha.gradetracker.util;

import com.codealpha.gradetracker.model.GradeReport;
import com.codealpha.gradetracker.model.Student;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Utility for exporting grade reports and student datasets to files.
 */
public class FileExportUtil {

    public static void exportToCSV(File file, List<Student> students) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            writer.println("Student ID,Name,Grades Count,Average Score,Highest Score,Lowest Score,Letter Grade,GPA,Status");
            for (Student s : students) {
                writer.printf("%s,\"%s\",%d,%.2f,%.2f,%.2f,%s,%.1f,%s%n",
                        escapeCSV(s.getId()),
                        escapeCSV(s.getName()),
                        s.getGrades().size(),
                        s.getAverageScore(),
                        s.getHighestScore(),
                        s.getLowestScore(),
                        s.getLetterGrade(),
                        s.getGPA(),
                        s.isPassed() ? "Passed" : "Failed"
                );
            }
        }
    }

    public static void exportSummaryReport(File file, List<Student> students, GradeReport report) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            String dateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            writer.println("=================================================================");
            writer.println("               CODEALPHA STUDENT GRADE TRACKER REPORT            ");
            writer.println("=================================================================");
            writer.println("Generated Date : " + dateStr);
            writer.println("Total Students : " + report.getTotalStudents());
            writer.println("Class Average  : " + String.format("%.2f", report.getClassAverage()));
            writer.println("Highest Score  : " + String.format("%.2f", report.getOverallHighestScore()));
            writer.println("Lowest Score   : " + String.format("%.2f", report.getOverallLowestScore()));
            writer.println("Pass Rate      : " + String.format("%.1f%%", report.getPassRate()));
            writer.println("-----------------------------------------------------------------");
            writer.println("GRADE DISTRIBUTION:");
            writer.printf("  Grade A (90-100): %d%n", report.getCountA());
            writer.printf("  Grade B (80-89) : %d%n", report.getCountB());
            writer.printf("  Grade C (70-79) : %d%n", report.getCountC());
            writer.printf("  Grade D (60-69) : %d%n", report.getCountD());
            writer.printf("  Grade F (<60)   : %d%n", report.getCountF());
            writer.println("-----------------------------------------------------------------");
            writer.println("TOP PERFORMING STUDENT(S):");
            for (Student s : report.getTopStudents()) {
                writer.printf("  - %s (%s) - Avg: %.2f%n", s.getName(), s.getId(), s.getAverageScore());
            }
            writer.println("-----------------------------------------------------------------");
            writer.println("DETAILED STUDENT SUMMARY:");
            writer.printf("%-10s %-20s %-10s %-10s %-10s %-8s %-6s%n",
                    "ID", "Name", "Avg Score", "Highest", "Lowest", "Grade", "Status");
            writer.println("-----------------------------------------------------------------");
            for (Student s : students) {
                writer.printf("%-10s %-20s %-10.2f %-10.2f %-10.2f %-8s %-6s%n",
                        s.getId(),
                        truncate(s.getName(), 19),
                        s.getAverageScore(),
                        s.getHighestScore(),
                        s.getLowestScore(),
                        s.getLetterGrade(),
                        s.isPassed() ? "PASS" : "FAIL");
            }
            writer.println("=================================================================");
        }
    }

    private static String escapeCSV(String value) {
        if (value == null) return "";
        return value.replace("\"", "\"\"");
    }

    private static String truncate(String text, int maxLen) {
        if (text == null) return "";
        if (text.length() <= maxLen) return text;
        return text.substring(0, maxLen - 3) + "...";
    }
}
