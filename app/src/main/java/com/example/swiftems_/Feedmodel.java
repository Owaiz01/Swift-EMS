package com.example.swiftems_;

public class Feedmodel {
    String email;

    String feedback;
    String name;

    public Feedmodel() {
    }

    public Feedmodel(String email, String feedback, String name) {
        this.email = email;
        this.feedback = feedback;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
