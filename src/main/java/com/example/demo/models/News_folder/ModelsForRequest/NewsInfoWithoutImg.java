package com.example.demo.models.News_folder.ModelsForRequest;

import java.sql.Blob;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

public class NewsInfoWithoutImg {

    private int id;

    private String author;

    private String date;

    private String title;

    private String content;

    public NewsInfoWithoutImg(){}

    public NewsInfoWithoutImg(int id, String author, String date, String title, String content){
        this.author=author;
        this.date=date;
        this.id=id;
        this.title=title;
        this.content=content;
    }

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
}
