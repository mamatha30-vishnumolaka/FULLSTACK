package com.example.studentdal.controller;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import com.example.studentdal.model.Student;
import com.example.studentdal.service.StudentService;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }

    // GET by Department
    @GetMapping("/department/{dept}")
    public List<Student> getByDepartment(@PathVariable String dept) {
        return service.getStudentsByDepartment(dept);
    }

    // GET by Age
    @GetMapping("/age/{age}")
    public List<Student> getByAge(@PathVariable int age) {
        return service.getStudentsByAge(age);
    }

    // GET students older than age
    @GetMapping("/age/greater/{age}")
    public List<Student> getOlderThan(@PathVariable int age) {
        return service.getStudentsOlderThan(age);
    }

    // GET by Department and Age
    @GetMapping("/filter")
    public List<Student> getByDeptAndAge(
            @RequestParam String department,
            @RequestParam int age) {
        return service.getByDepartmentAndAge(department, age);
    }
}