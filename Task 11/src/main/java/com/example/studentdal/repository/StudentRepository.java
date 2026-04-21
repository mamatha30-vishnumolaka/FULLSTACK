package com.example.studentdal.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.studentdal.model.Student;

public interface StudentRepository extends JpaRepository<Student, Integer> {

    // Find by Department
    List<Student> findByDepartment(String department);

    // Find by Age
    List<Student> findByAge(int age);

    // Find students older than given age
    List<Student> findByAgeGreaterThan(int age);

    // Find by Department and Age
    List<Student> findByDepartmentAndAge(String department, int age);
}