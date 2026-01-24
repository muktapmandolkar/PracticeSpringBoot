package com.Paragraph.dto;

import java.time.LocalDate;

public class StudentResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String course;
    private int year;
    private double gpa;
    private LocalDate enrollmentDate;
    private boolean active;
    
    // Getters
    public Long getId() {
        return id;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public String getCourse() {
        return course;
    }
    
    public int getYear() {
        return year;
    }
    
    public double getGpa() {
        return gpa;
    }
    
    public LocalDate getEnrollmentDate() {
        return enrollmentDate;
    }
    
    public boolean isActive() {
        return active;
    }
    
    // Setters
    public void setId(Long id) {
        this.id = id;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public void setCourse(String course) {
        this.course = course;
    }
    
    public void setYear(int year) {
        this.year = year;
    }
    
    public void setGpa(double gpa) {
        this.gpa = gpa;
    }
    
    public void setEnrollmentDate(LocalDate enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }
}