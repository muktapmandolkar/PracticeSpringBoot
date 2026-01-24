package com.Paragraph;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StudentManagementApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(StudentManagementApplication.class, args);
        System.out.println("🎓🎓🎓🎓🎓🎓🎓🎓🎓🎓🎓🎓🎓🎓🎓🎓🎓🎓");
        System.out.println("  STUDENT MANAGEMENT SYSTEM STARTED!");
        System.out.println("🎓🎓🎓🎓🎓🎓🎓🎓🎓🎓🎓🎓🎓🎓🎓🎓🎓🎓");
        System.out.println();
        System.out.println("🌐 FRONTEND URLS:");
        System.out.println("   http://localhost:8090/");
        System.out.println("   http://localhost:8090/index.html");
        System.out.println();
        System.out.println("🔧 API ENDPOINTS:");
        System.out.println("   http://localhost:8090/api/students");
        System.out.println("   http://localhost:8090/api/students/health");
        System.out.println("   http://localhost:8090/api/students/statistics");
        System.out.println();
        System.out.println("💾 DATABASE: student_db (MySQL)");
        System.out.println("==========================================");
    }
}