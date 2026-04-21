package com.example.studentdal.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.example.studentdal.model.Student;
import com.example.studentdal.repository.StudentRepository;

@Service
public class StudentService {

    private final StudentRepository repo;

    // Constructor Injection
    public StudentService(StudentRepository repo) {
        this.repo = repo;
    }

    public List<Student> getStudentsByDepartment(String dept) {
        return repo.findByDepartment(dept);
    }

    public List<Student> getStudentsByAge(int age) {
        return repo.findByAge(age);
    }

    public List<Student> getStudentsOlderThan(int age) {
        return repo.findByAgeGreaterThan(age);
    }

    public List<Student> getByDepartmentAndAge(String dept, int age) {
        return repo.findByDepartmentAndAge(dept, age);
    }
}