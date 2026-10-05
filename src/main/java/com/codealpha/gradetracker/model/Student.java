package com.codealpha.gradetracker.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Model class representing a Student with grades and score statistics.
 */
public class Student {
    private String id;
    private String name;
    private List<Double> grades;

    public Student(String id, String name) {
        this.id = id;
        this.name = name;
        this.grades = new ArrayList<>();
    }

    public Student(String id, String name, List<Double> initialGrades) {
        this.id = id;
        this.name = name;
        this.grades = new ArrayList<>();
        if (initialGrades != null) {
            for (Double g : initialGrades) {
                addGrade(g);
            }
        }
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

    public List<Double> getGrades() {
        return Collections.unmodifiableList(grades);
    }

    public void addGrade(double grade) {
        if (grade < 0 || grade > 100) {
            throw new IllegalArgumentException("Grade must be between 0.0 and 100.0");
        }
        this.grades.add(grade);
    }

    public void setGrades(List<Double> newGrades) {
        this.grades.clear();
        if (newGrades != null) {
            for (double g : newGrades) {
                addGrade(g);
            }
        }
    }

    public void clearGrades() {
        this.grades.clear();
    }

    public double getAverageScore() {
        if (grades.isEmpty()) {
            return 0.0;
        }
        double sum = 0.0;
        for (double grade : grades) {
            sum += grade;
        }
        return sum / grades.size();
    }

    public double getHighestScore() {
        if (grades.isEmpty()) {
            return 0.0;
        }
        double highest = grades.get(0);
        for (double grade : grades) {
            if (grade > highest) {
                highest = grade;
            }
        }
        return highest;
    }

    public double getLowestScore() {
        if (grades.isEmpty()) {
            return 0.0;
        }
        double lowest = grades.get(0);
        for (double grade : grades) {
            if (grade < lowest) {
                lowest = grade;
            }
        }
        return lowest;
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

    public boolean isPassed() {
        return getAverageScore() >= 60.0 && !grades.isEmpty();
    }

    @Override
    public String toString() {
        return String.format("Student[ID=%s, Name=%s, GradesCount=%d, Avg=%.2f, Highest=%.2f, Lowest=%.2f, Grade=%s]",
                id, name, grades.size(), getAverageScore(), getHighestScore(), getLowestScore(), getLetterGrade());
    }
}
