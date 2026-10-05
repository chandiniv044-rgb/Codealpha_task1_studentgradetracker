package com.codealpha.gradetracker.model;

import java.util.Map;

/**
 * Data transfer object encapsulating class-wide aggregate metrics and summary stats.
 */
public class GradeSummary {
    private int totalStudents;
    private int totalGradesRecorded;
    private double classAverage;
    private double classHighestScore;
    private double classLowestScore;
    private Student topPerformingStudent;
    private Student lowestPerformingStudent;
    private Map<String, Integer> gradeDistribution;

    public GradeSummary(int totalStudents, int totalGradesRecorded, double classAverage,
                        double classHighestScore, double classLowestScore,
                        Student topPerformingStudent, Student lowestPerformingStudent,
                        Map<String, Integer> gradeDistribution) {
        this.totalStudents = totalStudents;
        this.totalGradesRecorded = totalGradesRecorded;
        this.classAverage = classAverage;
        this.classHighestScore = classHighestScore;
        this.classLowestScore = classLowestScore;
        this.topPerformingStudent = topPerformingStudent;
        this.lowestPerformingStudent = lowestPerformingStudent;
        this.gradeDistribution = gradeDistribution;
    }

    public int getTotalStudents() {
        return totalStudents;
    }

    public int getTotalGradesRecorded() {
        return totalGradesRecorded;
    }

    public double getClassAverage() {
        return classAverage;
    }

    public double getClassHighestScore() {
        return classHighestScore;
    }

    public double getClassLowestScore() {
        return classLowestScore;
    }

    public Student getTopPerformingStudent() {
        return topPerformingStudent;
    }

    public Student getLowestPerformingStudent() {
        return lowestPerformingStudent;
    }

    public Map<String, Integer> getGradeDistribution() {
        return gradeDistribution;
    }
}
