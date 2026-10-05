package com.codealpha.gradetracker.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Model representing a Student in the Grade Tracker system.
 * Manages student attributes, grade list, and metric calculations.
 */
public class Student {
    private String id;
    private String name;
    private String department;
    private List<Double> grades;

    public Student(String id, String name, String department) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.grades = new ArrayList<>();
    }

    public Student(String id, String name, String department, List<Double> grades) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.grades = new ArrayList<>(grades);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public List<Double> getGrades() {
        return Collections.unmodifiableList(grades);
    }

    public void addGrade(double grade) {
        if (grade >= 0.0 && grade <= 100.0) {
            grades.add(grade);
        } else {
            throw new IllegalArgumentException("Grade must be between 0.0 and 100.0");
        }
    }

    public void clearGrades() {
        grades.clear();
    }

    public double getAverageScore() {
        if (grades.isEmpty()) {
            return 0.0;
        }
        double sum = 0.0;
        for (double g : grades) {
            sum += g;
        }
        return sum / grades.size();
    }

    public double getHighestScore() {
        if (grades.isEmpty()) {
            return 0.0;
        }
        double max = grades.get(0);
        for (double g : grades) {
            if (g > max) {
                max = g;
            }
        }
        return max;
    }

    public double getLowestScore() {
        if (grades.isEmpty()) {
            return 0.0;
        }
        double min = grades.get(0);
        for (double g : grades) {
            if (g < min) {
                min = g;
            }
        }
        return min;
    }

    public String getLetterGrade() {
        if (grades.isEmpty()) {
            return "N/A";
        }
        double avg = getAverageScore();
        if (avg >= 90.0) return "A";
        if (avg >= 80.0) return "B";
        if (avg >= 70.0) return "C";
        if (avg >= 60.0) return "D";
        return "F";
    }

    public double getGPA() {
        if (grades.isEmpty()) {
            return 0.0;
        }
        double avg = getAverageScore();
        if (avg >= 90.0) return 4.0;
        if (avg >= 80.0) return 3.0;
        if (avg >= 70.0) return 2.0;
        if (avg >= 60.0) return 1.0;
        return 0.0;
    }

    @Override
    public String toString() {
        return String.format("Student[ID=%s, Name=%s, Dept=%s, Avg=%.2f, GradesCount=%d]",
                id, name, department, getAverageScore(), grades.size());
    }
}
