package com.codealpha.gradetracker.service;

import com.codealpha.gradetracker.model.GradeReport;
import com.codealpha.gradetracker.model.Student;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Service managing student grade tracker data using ArrayLists.
 */
public class GradeTrackerService {
    private final List<Student> students;

    public GradeTrackerService() {
        this.students = new ArrayList<>();
    }

    public List<Student> getAllStudents() {
        return new ArrayList<>(students);
    }

    public boolean addStudent(Student student) {
        if (student == null || findById(student.getId()).isPresent()) {
            return false;
        }
        return students.add(student);
    }

    public boolean updateStudentName(String id, String newName) {
        Optional<Student> studentOpt = findById(id);
        if (studentOpt.isPresent()) {
            studentOpt.get().setName(newName);
            return true;
        }
        return false;
    }

    public boolean deleteStudent(String id) {
        return students.removeIf(s -> s.getId().equalsIgnoreCase(id));
    }

    public Optional<Student> findById(String id) {
        return students.stream()
                .filter(s -> s.getId().equalsIgnoreCase(id))
                .findFirst();
    }

    public List<Student> searchByName(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllStudents();
        }
        String lower = query.toLowerCase().trim();
        List<Student> results = new ArrayList<>();
        for (Student s : students) {
            if (s.getName().toLowerCase().contains(lower) || s.getId().toLowerCase().contains(lower)) {
                results.add(s);
            }
        }
        return results;
    }

    public boolean addGradeToStudent(String studentId, double grade) {
        Optional<Student> opt = findById(studentId);
        if (opt.isPresent()) {
            opt.get().addGrade(grade);
            return true;
        }
        return false;
    }

    public GradeReport generateReport() {
        return new GradeReport(students);
    }

    public void clearAll() {
        students.clear();
    }

    public int getStudentCount() {
        return students.size();
    }

    public List<Student> getStudentsSortedByAverage(boolean descending) {
        List<Student> sorted = getAllStudents();
        if (descending) {
            sorted.sort(Comparator.comparingDouble(Student::getAverageScore).reversed());
        } else {
            sorted.sort(Comparator.comparingDouble(Student::getAverageScore));
        }
        return sorted;
    }

    /**
     * Pre-populates sample data for quick demonstration.
     */
    public void loadSampleData() {
        clearAll();
        addStudent(new Student("STU101", "Alice Johnson", Arrays.asList(88.5, 92.0, 95.0, 91.0)));
        addStudent(new Student("STU102", "Bob Smith", Arrays.asList(72.0, 68.5, 75.0, 70.0)));
        addStudent(new Student("STU103", "Charlie Davis", Arrays.asList(98.0, 96.5, 100.0, 94.0)));
        addStudent(new Student("STU104", "Diana Prince", Arrays.asList(55.0, 62.0, 58.0, 61.0)));
        addStudent(new Student("STU105", "Ethan Hunt", Arrays.asList(82.0, 84.5, 80.0, 86.0)));
        addStudent(new Student("STU106", "Fiona Gallagher", Arrays.asList(45.0, 50.0, 48.0, 52.0)));
    }
}
