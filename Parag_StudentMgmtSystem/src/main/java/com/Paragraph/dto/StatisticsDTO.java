package com.Paragraph.dto;

import java.time.LocalDate;

public class StatisticsDTO {
    private long totalStudents;
    private long activeStudents;
    private double averageGPA;
    private String timestamp;
    
    // Constructors
    public StatisticsDTO() {
        this.timestamp = LocalDate.now().toString();
    }
    
    public StatisticsDTO(long totalStudents, long activeStudents, double averageGPA) {
        this.totalStudents = totalStudents;
        this.activeStudents = activeStudents;
        this.averageGPA = averageGPA;
        this.timestamp = LocalDate.now().toString();
    }
    
    // Getters
    public long getTotalStudents() {
        return totalStudents;
    }
    
    public long getActiveStudents() {
        return activeStudents;
    }
    
    public double getAverageGPA() {
        return averageGPA;
    }
    
    public String getTimestamp() {
        return timestamp;
    }
    
    // Setters
    public void setTotalStudents(long totalStudents) {
        this.totalStudents = totalStudents;
    }
    
    public void setActiveStudents(long activeStudents) {
        this.activeStudents = activeStudents;
    }
    
    public void setAverageGPA(double averageGPA) {
        this.averageGPA = averageGPA;
    }
    
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}