package com.example.demo.controllers.News;

import java.net.http.HttpHeaders;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.LinkedList;
import java.util.Optional;

import javax.sql.rowset.serial.SerialBlob;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Optionals;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.example.demo.models.News.News;
import com.example.demo.models.Users.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController

@RequestMapping(path = "/news")
public class NewsController {

  @Autowired
  private NewsService newsService;

  @GetMapping
  public List<News> getAllNews() {
    return newsService.getAllNews();
  }

  @GetMapping("/{id}")
  public Optional<News> getNewsById(@PathVariable int id) {
    return newsService.getNewsById(id);
  }

  @PostMapping
  public News createNews(@RequestBody News news) {
    return newsService.createNews(news);
  }

  @DeleteMapping("/{id}")
  public void deleteNews(@PathVariable int id) {
    newsService.deleteNews(id);
  }

  
}