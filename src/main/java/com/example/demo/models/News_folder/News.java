package com.example.demo.models.News_folder;

import java.sql.Blob;

import javax.persistence.*;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "news")
public class News {
    @Id
    @Column(name = "id")
   // @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;

    @Column(name = "author")
    private String author;

    @Column(name = "date")
    private String date;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "image")
    private Blob image;

    public Integer getId() { 
        return id; 
    }

    public String getAuthor() { 
        return author; 
    }

    public String getDate() { 
        return date; 
    }

    public String getTitle() { 
        return title; 
    }

    public String getContent() { 
        return content; 
    }

    public Blob getImage() { 
        return image; 
    }

    public void setAuthor(String _author) {
        this.author = _author;
    }

    public void setDate(String _date) {
        this.date = _date;
    }

    public void setTitle(String _title) {
        this.title = _title;
    }

    public void setContent(String _content) {
        this.content = _content;
    }

    public void setImage(Blob _image) {
        this.image = _image;
    }
}