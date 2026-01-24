package com.Paragraph.service;

import com.Paragraph.dto.StudentRequest;
import com.Paragraph.dto.StudentResponse;
import com.Paragraph.dto.StatisticsDTO;
import com.Paragraph.entity.Student;
import com.Paragraph.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class StudentService {
    
    @Autowired
    private StudentRepository studentRepository;
    
    // Convert Student to StudentResponse
    private StudentResponse convertToResponse(Student student) {
        StudentResponse response = new StudentResponse();
        response.setId(student.getId());
        response.setFirstName(student.getFirstName());
        response.setLastName(student.getLastName());
        response.setEmail(student.getEmail());
        response.setPhone(student.getPhone());
        response.setCourse(student.getCourse());
        response.setYear(student.getYear());
        response.setGpa(student.getGpa());
        response.setEnrollmentDate(student.getEnrollmentDate());
        response.setActive(student.isActive());
        return response;
    }
    
    // Convert StudentRequest to Student Entity
    private Student convertToEntity(StudentRequest request) {
        Student student = new Student();
        student.setFirstName(request.getFirstName());
        student.setLastName(request.getLastName());
        student.setEmail(request.getEmail());
        student.setPhone(request.getPhone());
        student.setCourse(request.getCourse());
        student.setYear(request.getYear());
        student.setGpa(request.getGpa());
        student.setActive(true);
        return student;
    }
    
    // 1. Get all students
    public List<StudentResponse> getAllStudents() {
        List<Student> students = studentRepository.findAll();
        return students.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    // 2. Get student by ID
    public StudentResponse getStudentById(Long id) {
        Optional<Student> student = studentRepository.findById(id);
        if (student.isPresent()) {
            return convertToResponse(student.get());
        } else {
            throw new RuntimeException("Student not found with id: " + id);
        }
    }
    
    // 3. Create student
    public StudentResponse createStudent(StudentRequest request) {
        // Check if email already exists
        Optional<Student> existingStudent = studentRepository.findByEmail(request.getEmail());
        if (existingStudent.isPresent()) {
            throw new RuntimeException("Email already exists: " + request.getEmail());
        }
        
        Student student = convertToEntity(request);
        Student savedStudent = studentRepository.save(student);
        return convertToResponse(savedStudent);
    }
    
    // 4. Update student
    public StudentResponse updateStudent(Long id, StudentRequest request) {
        Optional<Student> existingStudent = studentRepository.findById(id);
        if (existingStudent.isPresent()) {
            Student student = existingStudent.get();
            
            // Check if email is being changed to an existing email
            if (!student.getEmail().equals(request.getEmail())) {
                Optional<Student> emailCheck = studentRepository.findByEmail(request.getEmail());
                if (emailCheck.isPresent() && !emailCheck.get().getId().equals(id)) {
                    throw new RuntimeException("Email already exists: " + request.getEmail());
                }
            }
            
            // Update fields
            student.setFirstName(request.getFirstName());
            student.setLastName(request.getLastName());
            student.setEmail(request.getEmail());
            student.setPhone(request.getPhone());
            student.setCourse(request.getCourse());
            student.setYear(request.getYear());
            student.setGpa(request.getGpa());
            
            Student updatedStudent = studentRepository.save(student);
            return convertToResponse(updatedStudent);
        } else {
            throw new RuntimeException("Student not found with id: " + id);
        }
    }
    
    // 5. Delete student (soft delete)
    public void deleteStudent(Long id) {
        Optional<Student> student = studentRepository.findById(id);
        if (student.isPresent()) {
            Student s = student.get();
            s.setActive(false);
            studentRepository.save(s);
        } else {
            throw new RuntimeException("Student not found with id: " + id);
        }
    }
    
    // 6. Search students by name
    public List<StudentResponse> searchStudents(String name) {
        List<Student> students = studentRepository.searchByName(name);
        return students.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    // 7. Get students by course
    public List<StudentResponse> getStudentsByCourse(String course) {
        List<Student> students = studentRepository.findByCourse(course);
        return students.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    // 8. Get students by year
    public List<StudentResponse> getStudentsByYear(int year) {
        List<Student> students = studentRepository.findByYear(year);
        return students.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    // 9. Get active students
    public List<StudentResponse> getActiveStudents() {
        List<Student> students = studentRepository.findByActiveTrue();
        return students.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    // 10. Get statistics
    public StatisticsDTO getStatistics() {
        List<Student> students = studentRepository.findAll();
        long total = students.size();
        long active = studentRepository.findByActiveTrue().size();
        
        double avgGpa = students.stream()
                .mapToDouble(Student::getGpa)
                .average()
                .orElse(0.0);
        
        double roundedGpa = Math.round(avgGpa * 100.0) / 100.0;
        
        return new StatisticsDTO(total, active, roundedGpa);
    }
}