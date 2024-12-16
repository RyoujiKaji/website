package com.example.demo.models.Users_folder.ModelsForResponse;

public class UserPrivateInfo {
    private String name;
    private String date;
    private String email;

    public UserPrivateInfo() {
    };

    public UserPrivateInfo(String email, String name, String date) {
        this.email = email;
        this.name = name;
        this.date = date;
    }

    public String getName() { 
        return name; 
    }

    public String getDate() { 
        return date; 
    }

    public String getEmail() { 
        return email; 
    }

    public void setEmail(String _mail) {
        this.email = _mail;
    }

    public void setName(String _name) {
        this.name = _name;
    }

    public void setDate(String _date) {
        this.date = _date;
    }
}
