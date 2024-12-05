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

    @Column(name = "login")
   // @Type(type = "text")
    private String login;

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

    public String getLogin() { 
        return login; 
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
}
