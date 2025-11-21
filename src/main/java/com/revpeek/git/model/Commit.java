package com.revpeek.git.model;

public class Commit {
    private String hash;
    private String author;
    private String date;
    private String message;
    
    public Commit(String hash, String author, String date, String message) {
        this.hash = hash;
        this.author = author;
        this.date = date;
        this.message = message;
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
    
    @Override
    public String toString() {
        return hash.substring(0, 8) + " " + author + " " + date + " " + message;
    }
}