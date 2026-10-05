package com.codealpha.gradetracker.service;

import com.codealpha.gradetracker.model.GradeSummary;
import com.codealpha.gradetracker.model.Student;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Service class for managing student records, grade processing, analytics, and file persistence.
 */
public class GradeTrackerService {
    private final List<Student> students;

    public GradeTrackerService() {
        this.students = new ArrayList<>();
    }

    public List<Student> getStudents() {
        return Collections.unmodifiableList(students);
    }

    public void addStudent(Student student) {
        if (findStudentById(student.getId()).isPresent()) {
            throw new IllegalArgumentException("Student with ID '" + student.getId() + "' already exists.");
        }
        students.add(student);
    }

    public Optional<Student> findStudentById(String id) {
        return students.stream()
                .filter(s -> s.getId().equalsIgnoreCase(id.trim()))
                .findFirst();
    }

    public boolean removeStudent(String id) {
        return students.removeIf(s -> s.getId().equalsIgnoreCase(id.trim()));
    }

    public boolean addGradeToStudent(String studentId, double grade) {
        Optional<Student> studentOpt = findStudentById(studentId);
        if (studentOpt.isPresent()) {
            studentOpt.get().addGrade(grade);
            return true;
        }
        return false;
    }

    public GradeSummary calculateSummary() {
        if (students.isEmpty()) {
            return new GradeSummary(0, 0, 0.0, 0.0, 0.0, null, null, Collections.emptyMap());
        }

        int totalStudents = students.size();
        int totalGradesCount = 0;
        double overallScoreSum = 0.0;
        double overallMax = Double.MIN_VALUE;
        double overallMin = Double.MAX_VALUE;

        Student topStudent = null;
        Student lowestStudent = null;
        double highestAvg = -1.0;
        double lowestAvg = 101.0;

        Map<String, Integer> distribution = new LinkedHashMap<>();
        distribution.put("A (90-100)", 0);
        distribution.put("B (80-89)", 0);
        distribution.put("C (70-79)", 0);
        distribution.put("D (60-69)", 0);
        distribution.put("F (<60)", 0);

        for (Student s : students) {
            double avg = s.getAverageScore();
            if (!s.getGrades().isEmpty()) {
                if (avg > highestAvg) {
                    highestAvg = avg;
                    topStudent = s;
                }
                if (avg < lowestAvg) {
                    lowestAvg = avg;
                    lowestStudent = s;
                }
            }

            for (double g : s.getGrades()) {
                totalGradesCount++;
                overallScoreSum += g;
                if (g > overallMax) overallMax = g;
                if (g < overallMin) overallMin = g;

                if (g >= 90.0) distribution.put("A (90-100)", distribution.get("A (90-100)") + 1);
                else if (g >= 80.0) distribution.put("B (80-89)", distribution.get("B (80-89)") + 1);
                else if (g >= 70.0) distribution.put("C (70-79)", distribution.get("C (70-79)") + 1);
                else if (g >= 60.0) distribution.put("D (60-69)", distribution.get("D (60-69)") + 1);
                else distribution.put("F (<60)", distribution.get("F (<60)") + 1);
            }
        }

        double classAverage = totalGradesCount > 0 ? overallScoreSum / totalGradesCount : 0.0;
        if (overallMax == Double.MIN_VALUE) overallMax = 0.0;
        if (overallMin == Double.MAX_VALUE) overallMin = 0.0;

        return new GradeSummary(
                totalStudents,
                totalGradesCount,
                classAverage,
                overallMax,
                overallMin,
                topStudent,
                lowestStudent,
                distribution
        );
    }

    public void loadSampleData() {
        students.clear();

        Student s1 = new Student("STU-101", "Alice Johnson", "Computer Science");
        s1.addGrade(95.5);
        s1.addGrade(88.0);
        s1.addGrade(92.0);

        Student s2 = new Student("STU-102", "Bob Smith", "Electrical Eng");
        s2.addGrade(78.0);
        s2.addGrade(82.5);
        s2.addGrade(75.0);

        Student s3 = new Student("STU-103", "Carol Williams", "Computer Science");
        s3.addGrade(99.0);
        s3.addGrade(94.0);
        s3.addGrade(97.5);

        Student s4 = new Student("STU-104", "David Brown", "Mechanical Eng");
        s4.addGrade(62.0);
        s4.addGrade(58.5);
        s4.addGrade(70.0);

        Student s5 = new Student("STU-105", "Emma Davis", "Data Science");
        s5.addGrade(89.0);
        s5.addGrade(91.5);
        s5.addGrade(85.0);

        students.add(s1);
        students.add(s2);
        students.add(s3);
        students.add(s4);
        students.add(s5);
    }

    public void exportToCSV(String filePath) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath))) {
            writer.write("ID,Name,Department,Grades,Average,Letter Grade,GPA\n");
            for (Student s : students) {
                StringBuilder gradesStr = new StringBuilder();
                for (int i = 0; i < s.getGrades().size(); i++) {
                    gradesStr.append(s.getGrades().get(i));
                    if (i < s.getGrades().size() - 1) gradesStr.append(";");
                }
                writer.write(String.format("%s,\"%s\",\"%s\",\"%s\",%.2f,%s,%.1f\n",
                        s.getId(),
                        s.getName(),
                        s.getDepartment(),
                        gradesStr.toString(),
                        s.getAverageScore(),
                        s.getLetterGrade(),
                        s.getGPA()));
            }
        }
    }

    public void exportSummaryReport(String filePath) throws IOException {
        GradeSummary summary = calculateSummary();
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath))) {
            writer.write("====================================================\n");
            writer.write("         STUDENT GRADE TRACKER - SUMMARY REPORT     \n");
            writer.write("====================================================\n\n");
            writer.write(String.format("Total Students:          %d\n", summary.getTotalStudents()));
            writer.write(String.format("Total Grades Recorded:   %d\n", summary.getTotalGradesRecorded()));
            writer.write(String.format("Class Average Score:     %.2f\n", summary.getClassAverage()));
            writer.write(String.format("Class Highest Score:     %.2f\n", summary.getClassHighestScore()));
            writer.write(String.format("Class Lowest Score:      %.2f\n", summary.getClassLowestScore()));

            if (summary.getTopPerformingStudent() != null) {
                writer.write(String.format("Top Student:             %s (%s) - Avg: %.2f\n",
                        summary.getTopPerformingStudent().getName(),
                        summary.getTopPerformingStudent().getId(),
                        summary.getTopPerformingStudent().getAverageScore()));
            }

            writer.write("\n----------------------------------------------------\n");
            writer.write("GRADE DISTRIBUTION:\n");
            for (Map.Entry<String, Integer> entry : summary.getGradeDistribution().entrySet()) {
                writer.write(String.format("  %-12s : %d\n", entry.getKey(), entry.getValue()));
            }
            writer.write("----------------------------------------------------\n\n");

            writer.write("STUDENT DETAILS:\n");
            writer.write(String.format("%-10s | %-20s | %-15s | %-8s | %-6s\n",
                    "ID", "Name", "Department", "Average", "Grade"));
            writer.write("------------------------------------------------------------------\n");
            for (Student s : students) {
                writer.write(String.format("%-10s | %-20s | %-15s | %-8.2f | %-6s\n",
                        s.getId(), s.getName(), s.getDepartment(), s.getAverageScore(), s.getLetterGrade()));
            }
            writer.write("====================================================\n");
        }
    }
}
