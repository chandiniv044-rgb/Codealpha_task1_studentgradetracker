package com.codealpha.gradetracker;

import com.codealpha.gradetracker.model.GradeReport;
import com.codealpha.gradetracker.model.Student;
import com.codealpha.gradetracker.service.GradeTrackerService;

import java.util.Arrays;
import java.util.List;

/**
 * Lightweight test suite verifying core business logic and math calculations.
 */
public class TestRunner {
    private static int passedTests = 0;
    private static int failedTests = 0;

    public static void main(String[] args) {
        System.out.println("=========================================================");
        System.out.println("  RUNNING AUTOMATED UNIT TESTS FOR STUDENT GRADE TRACKER  ");
        System.out.println("=========================================================");

        testStudentAverage();
        testHighestAndLowest();
        testLetterGradeAndGPA();
        testGradeReportStatistics();
        testServiceCrudOperations();
        testEdgeCaseEmptyGrades();

        System.out.println("---------------------------------------------------------");
        System.out.printf("TEST SUMMARY: %d PASSED, %d FAILED%n", passedTests, failedTests);
        System.out.println("=========================================================");

        if (failedTests > 0) {
            System.exit(1);
        }
    }

    private static void assertEquals(double expected, double actual, String testName) {
        if (Math.abs(expected - actual) < 0.001) {
            System.out.println("[PASS] " + testName);
            passedTests++;
        } else {
            System.err.printf("[FAIL] %s - Expected: %.2f, Got: %.2f%n", testName, expected, actual);
            failedTests++;
        }
    }

    private static void assertEquals(Object expected, Object actual, String testName) {
        if ((expected == null && actual == null) || (expected != null && expected.equals(actual))) {
            System.out.println("[PASS] " + testName);
            passedTests++;
        } else {
            System.err.printf("[FAIL] %s - Expected: %s, Got: %s%n", testName, expected, actual);
            failedTests++;
        }
    }

    private static void assertTrue(boolean condition, String testName) {
        if (condition) {
            System.out.println("[PASS] " + testName);
            passedTests++;
        } else {
            System.err.println("[FAIL] " + testName + " - Condition was false!");
            failedTests++;
        }
    }

    private static void testStudentAverage() {
        Student s = new Student("T1", "Alice", Arrays.asList(80.0, 90.0, 100.0));
        assertEquals(90.0, s.getAverageScore(), "Student average calculation (80, 90, 100 -> 90.0)");
    }

    private static void testHighestAndLowest() {
        Student s = new Student("T2", "Bob", Arrays.asList(45.0, 95.5, 67.0, 88.0));
        assertEquals(95.5, s.getHighestScore(), "Highest score calculation (95.5)");
        assertEquals(45.0, s.getLowestScore(), "Lowest score calculation (45.0)");
    }

    private static void testLetterGradeAndGPA() {
        Student s1 = new Student("T3", "Charlie", Arrays.asList(92.0, 95.0));
        assertEquals("A", s1.getLetterGrade(), "Letter grade A assignment");
        assertEquals(4.0, s1.getGPA(), "GPA 4.0 assignment");

        Student s2 = new Student("T4", "Dave", Arrays.asList(50.0, 55.0));
        assertEquals("F", s2.getLetterGrade(), "Letter grade F assignment");
        assertEquals(0.0, s2.getGPA(), "GPA 0.0 assignment");
    }

    private static void testGradeReportStatistics() {
        GradeTrackerService service = new GradeTrackerService();
        service.addStudent(new Student("ST1", "Alex", Arrays.asList(90.0, 100.0))); // Avg 95 (A)
        service.addStudent(new Student("ST2", "Beth", Arrays.asList(70.0, 80.0)));  // Avg 75 (C)

        GradeReport report = service.generateReport();
        assertEquals(2, report.getTotalStudents(), "Report total students");
        assertEquals(85.0, report.getClassAverage(), "Report class average");
        assertEquals(100.0, report.getOverallHighestScore(), "Report overall highest score");
        assertEquals(70.0, report.getOverallLowestScore(), "Report overall lowest score");
        assertEquals(1, report.getCountA(), "Report count for Grade A");
        assertEquals(1, report.getCountC(), "Report count for Grade C");
    }

    private static void testServiceCrudOperations() {
        GradeTrackerService service = new GradeTrackerService();
        Student s = new Student("ST100", "John Doe");
        assertTrue(service.addStudent(s), "Service add student");
        assertTrue(service.findById("ST100").isPresent(), "Service find by ID");
        assertTrue(service.addGradeToStudent("ST100", 85.0), "Service add grade");
        assertEquals(85.0, service.findById("ST100").get().getAverageScore(), "Service verify added grade");
        assertTrue(service.deleteStudent("ST100"), "Service delete student");
        assertTrue(service.findById("ST100").isEmpty(), "Service verify deletion");
    }

    private static void testEdgeCaseEmptyGrades() {
        Student s = new Student("E1", "Empty");
        assertEquals(0.0, s.getAverageScore(), "Empty grades average is 0.0");
        assertEquals(0.0, s.getHighestScore(), "Empty grades highest is 0.0");
        assertEquals(0.0, s.getLowestScore(), "Empty grades lowest is 0.0");
        assertEquals("N/A", s.getLetterGrade(), "Empty grades letter is N/A");
    }
}
