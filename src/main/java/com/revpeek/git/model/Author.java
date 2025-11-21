package com.revpeek.git.model;

// This class is now deprecated but kept for compatibility
// The system now uses String author directly in Commit
public class Author {
    private String name;
    private String email;
    
    public Author(String name, String email) {
        this.name = name;
        this.email = email;
    }
    
    public String getName() {
        return name;
    }
    
    public String getEmail() {
        return email;
    }
    
    @Override
    public String toString() {
        return name;
    }
}