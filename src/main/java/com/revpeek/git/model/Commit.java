package com.revpeek.git.model;

public class Commit {
    private String hash;
    private String author;
    private String date;
    private String message;
    
    private java.util.List<String> files;
    
    public Commit(String hash, String author, String date, String message) {
        this.hash = hash;
        this.author = author;
        this.date = date;
        this.message = message;
        this.files = new java.util.ArrayList<>();
    }
    
    public String getHash() {
        return hash;
    }
    
    public String getAuthor() {
        return author;
    }
    
    public String getDate() {
        return date;
    }
    
    public String getMessage() {
        return message;
    }
    
    public java.util.List<String> getFiles() {
        return files;
    }
    
    public void addFileChange(String file) {
        this.files.add(file);
    }
    
    @Override
    public String toString() {
        return hash.substring(0, 8) + " " + author + " " + date + " " + message;
    }
}