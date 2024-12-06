package com.example.demo;

import javax.persistence.*;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    User() {};

    User(int id, String password, String role, String image, String mail) {
        this.id = id;
        this.password = password;
        this.role = role;
        this.image = image;
        this.mail = mail;
    }
    /*@Column(name = "login")
   // @Type(type = "text")
    private String login;*/

    @Column(name = "password")
   // @Type(type = "text")
    private String password;

    @Column(name = "role")
   // @Type(type = "text")
    private String role;

    @Column(name = "image")
   // @Type(type = "text")
    private String image;

    @Column(name = "mail")
   // @Type(type = "text")
    private String mail;

    public Integer getId() { 
        return id; 
    }

    public String getPassword() { 
        return password; 
    }

    public String getRole() { 
        return role; 
    }

    public String getImage() { 
        return image; 
    }

    public String getMail() { 
        return mail; 
    }

    public void setMail(String _mail) {
        this.mail = _mail;
    }

    public void setPassword(String _password) {
        this.password = _password;
    }

    public void setRole(String _role) {
        this.role = _role;
    }

    public void setImage(String _image) {
        this.image = _image;
    }

    public void setId(Integer _id) {
        this.id = _id;
    }
}
