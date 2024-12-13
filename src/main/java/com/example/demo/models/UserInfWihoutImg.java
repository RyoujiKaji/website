package com.example.demo.models;

public class UserInfWihoutImg {

    private int id;

    public UserInfWihoutImg() {};

    public UserInfWihoutImg(int id, String name, String date, String email, String role) {
        this.id = id;
        this.role = role;
        this.email = email;
        this.name=name;
        this.date=date;
    }
    private String name;

    private String date;

    private String role;

    private String email;

    public Integer getId() { 
        return id; 
    }

    public String getRole() { 
        return role; 
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

    public void setRole(String _role) {
        this.role = _role;
    }

    public void setName(String _name) {
        this.name = _name;
    }

    public void setDate(String _date) {
        this.date = _date;
    }

    public void setId(Integer _id) {
        this.id = _id;
    }
}
