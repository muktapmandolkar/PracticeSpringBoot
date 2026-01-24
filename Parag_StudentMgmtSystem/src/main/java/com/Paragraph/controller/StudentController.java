package com.Paragraph.controller;

import com.Paragraph.dto.StudentRequest;
import com.Paragraph.dto.StudentResponse;
import com.Paragraph.dto.StatisticsDTO;
import com.Paragraph.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "*")
public class StudentController {
    
    @Autowired
    private StudentService studentService;
    
    // 1. Get all students
    @GetMapping
    public ResponseEntity<List<StudentResponse>> getAllStudents() {
        List<StudentResponse> students = studentService.getAllStudents();
        return ResponseEntity.ok(students);
    }
    
    // 2. Get student by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getStudentById(@PathVariable Long id) {
        try {
            StudentResponse student = studentService.getStudentById(id);
            return ResponseEntity.ok(student);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }
    
    // 3. Create student
    @PostMapping
    public ResponseEntity<?> createStudent(@Valid @RequestBody StudentRequest request) {
        try {
            StudentResponse response = studentService.createStudent(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    // 4. Update student
    @PutMapping("/{id}")
    public ResponseEntity<?> updateStudent(@PathVariable Long id, 
                                           @Valid @RequestBody StudentRequest request) {
        try {
            StudentResponse response = studentService.updateStudent(id, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    // 5. Delete student
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable Long id) {
        try {
            studentService.deleteStudent(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Student deleted successfully");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }
    
    // 6. Search students
    @GetMapping("/search")
    public ResponseEntity<List<StudentResponse>> searchStudents(@RequestParam String name) {
        List<StudentResponse> students = studentService.searchStudents(name);
        return ResponseEntity.ok(students);
    }
    
    // 7. Get students by course
    @GetMapping("/course/{course}")
    public ResponseEntity<List<StudentResponse>> getStudentsByCourse(@PathVariable String course) {
        List<StudentResponse> students = studentService.getStudentsByCourse(course);
        return ResponseEntity.ok(students);
    }
    
    // 8. Get students by year
    @GetMapping("/year/{year}")
    public ResponseEntity<List<StudentResponse>> getStudentsByYear(@PathVariable int year) {
        List<StudentResponse> students = studentService.getStudentsByYear(year);
        return ResponseEntity.ok(students);
    }
    
    // 9. Get active students
    @GetMapping("/active")
    public ResponseEntity<List<StudentResponse>> getActiveStudents() {
        List<StudentResponse> students = studentService.getActiveStudents();
        return ResponseEntity.ok(students);
    }
    
    // 10. Get statistics
    @GetMapping("/statistics")
    public ResponseEntity<StatisticsDTO> getStatistics() {
        StatisticsDTO statistics = studentService.getStatistics();
        return ResponseEntity.ok(statistics);
    }
    
    // 11. Health check
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "Student Management System");
        response.put("version", "1.0.0");
        response.put("timestamp", java.time.LocalDateTime.now().toString());
        return ResponseEntity.ok(response);
    }
    
    // 12. Test endpoint
    @GetMapping("/test")
    public String test() {
        return "Student Management System API is working!";
    }
}