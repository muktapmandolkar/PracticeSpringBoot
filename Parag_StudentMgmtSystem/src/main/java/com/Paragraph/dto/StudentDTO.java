package com.Paragraph.dto;

public class StudentDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String course;
    private int year;
    private double gpa;
    
    // Constructors
    public StudentDTO() {}
    
    public StudentDTO(Long id, String firstName, String lastName, String email, 
                     String phone, String course, int year, double gpa) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.course = course;
        this.year = year;
        this.gpa = gpa;
    }
    
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
    
    // toString() method
    @Override
    public String toString() {
        return "StudentDTO [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName 
                + ", email=" + email + ", phone=" + phone + ", course=" + course 
                + ", year=" + year + ", gpa=" + gpa + "]";
    }
}