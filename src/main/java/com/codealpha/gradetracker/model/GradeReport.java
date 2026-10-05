package com.codealpha.gradetracker.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Summary statistics class aggregating calculations across all students.
 */
public class GradeReport {
    private int totalStudents;
    private double classAverage;
    private double overallHighestScore;
    private double overallLowestScore;
    private List<Student> topStudents;
    private List<Student> lowestStudents;
    private int countA, countB, countC, countD, countF;

    public GradeReport(List<Student> students) {
        this.topStudents = new ArrayList<>();
        this.lowestStudents = new ArrayList<>();
        calculateReport(students);
    }

    private void calculateReport(List<Student> students) {
        if (students == null || students.isEmpty()) {
            this.totalStudents = 0;
            this.classAverage = 0.0;
            this.overallHighestScore = 0.0;
            this.overallLowestScore = 0.0;
            return;
        }

        this.totalStudents = students.size();
        double totalSum = 0.0;
        int totalGradesCount = 0;
        double maxScore = -1.0;
        double minScore = 101.0;

        for (Student s : students) {
            double avg = s.getAverageScore();
            if (!s.getGrades().isEmpty()) {
                totalSum += avg;
                totalGradesCount++;

                double high = s.getHighestScore();
                double low = s.getLowestScore();

                if (high > maxScore) {
                    maxScore = high;
                }
                if (low < minScore) {
                    minScore = low;
                }
            }

            switch (s.getLetterGrade()) {
                case "A": countA++; break;
                case "B": countB++; break;
                case "C": countC++; break;
                case "D": countD++; break;
                case "F": countF++; break;
            }
        }

        this.classAverage = (totalGradesCount > 0) ? (totalSum / totalGradesCount) : 0.0;
        this.overallHighestScore = (maxScore == -1.0) ? 0.0 : maxScore;
        this.overallLowestScore = (minScore == 101.0) ? 0.0 : minScore;

        // Find top and lowest performing students by average
        double highestAvg = -1.0;
        double lowestAvg = 101.0;

        for (Student s : students) {
            if (s.getGrades().isEmpty()) continue;
            double avg = s.getAverageScore();
            if (avg > highestAvg) {
                highestAvg = avg;
            }
            if (avg < lowestAvg) {
                lowestAvg = avg;
            }
        }

        for (Student s : students) {
            if (s.getGrades().isEmpty()) continue;
            double avg = s.getAverageScore();
            if (Math.abs(avg - highestAvg) < 0.001) {
                topStudents.add(s);
            }
            if (Math.abs(avg - lowestAvg) < 0.001) {
                lowestStudents.add(s);
            }
        }
    }

    public int getTotalStudents() {
        return totalStudents;
    }

    public double getClassAverage() {
        return classAverage;
    }

    public double getOverallHighestScore() {
        return overallHighestScore;
    }

    public double getOverallLowestScore() {
        return overallLowestScore;
    }

    public List<Student> getTopStudents() {
        return topStudents;
    }

    public List<Student> getLowestStudents() {
        return lowestStudents;
    }

    public int getCountA() { return countA; }
    public int getCountB() { return countB; }
    public int getCountC() { return countC; }
    public int getCountD() { return countD; }
    public int getCountF() { return countF; }

    public int getPassedCount() {
        return countA + countB + countC + countD;
    }

    public int getFailedCount() {
        return countF;
    }

    public double getPassRate() {
        if (totalStudents == 0) return 0.0;
        return ((double) getPassedCount() / totalStudents) * 100.0;
    }
}
