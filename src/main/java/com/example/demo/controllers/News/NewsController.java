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

import com.example.demo.models.General_folder.ModelsForRequest.InputId;
import com.example.demo.models.General_folder.ModelsForResponse.BasicResponse;
import com.example.demo.models.News_folder.News;
import com.example.demo.models.News_folder.ModelsForRequest.NewsInfoWithoutImg;
import com.example.demo.models.Users_folder.User;
import com.example.demo.models.Users_folder.ModelsForRequest.UserModifierPrivateInfo;
import com.example.demo.models.Users_folder.ModelsForResponse.UserInfWihoutImg;
import com.example.demo.models.Users_folder.ModelsForResponse.UserPrivateInfo;
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

  @PostMapping(path = "/editnews", produces = "application/json")
  public @ResponseBody ResponseEntity<String> editnews(@RequestBody News userInput) {
    try {
      BasicResponse response = setNewsInfo(userInput);
      String jsonResponse = new ObjectMapper().writeValueAsString(response); // Преобразование объекта в JSON строку
      return ResponseEntity.ok()
          .contentType(MediaType.APPLICATION_JSON)
          .body(jsonResponse);
    } catch (Exception ex) {
      System.out.println(ex.getMessage());
      return ResponseEntity.ok(null);
    }
  }

  @PostMapping("/getimg")
  public ResponseEntity<byte[]> getimg(@RequestBody InputId userInput) {
    try {
      byte[] image = getImageNews(userInput.getId());
      if (image == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
      }
      return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
    } catch (Exception ex) {
      System.out.println(ex.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
  }

  private byte[] getImageNews(int id) {
    byte[] response = null;
    Optional<News> newsOpt = getNewsById(id);
    try {
      if (newsOpt.isEmpty()) {
        throw new Exception("No news with this id");
      }
      News news = newsOpt.get();
      response = blobToByteArray(news.getImage());
      return response;
    } catch (Exception err) {
      System.out.println(err.getMessage());
      return response;
    }
  }

  private byte[] blobToByteArray(Blob blob) throws SQLException {
    if (blob == null) {
      return null; // Обработка случая, когда blob равен null
    }
    return blob.getBytes(1, (int) blob.length());
  }


  @PostMapping(path = "/editimg", produces = "application/json")
  public ResponseEntity<String> editimg(@RequestParam("image") MultipartFile file, @RequestParam("id") String id) {
    try {
      BasicResponse response = setImageNews(Integer.parseInt(id), file);
      String jsonResponse = new ObjectMapper().writeValueAsString(response); // Преобразование объекта в JSON строку
      return ResponseEntity.ok()
          .contentType(MediaType.APPLICATION_JSON)
          .body(jsonResponse);
    } catch (Exception ex) {
      System.out.println(ex.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
  }

  @PostMapping(path = "/allnewswithoutimg", produces = "application/json")
  public @ResponseBody ResponseEntity<String> allnewswithoutimg() {
    try {
      List<NewsInfoWithoutImg> response = getNewsInfo();
      String jsonResponse = new ObjectMapper().writeValueAsString(response); // Преобразование объекта в JSON строку
      return ResponseEntity.ok()
          .contentType(MediaType.APPLICATION_JSON)
          .body(jsonResponse);
    } catch (Exception ex) {
      System.out.println(ex.getMessage());
      return ResponseEntity.ok(null);
    }
  }

  private List<NewsInfoWithoutImg> getNewsInfo() throws Exception {
    List<News> allNews = getAllNews();
    List<NewsInfoWithoutImg> response= new LinkedList<NewsInfoWithoutImg>();
    for (News news : allNews) {
      response.add(new NewsInfoWithoutImg(news.getId(), news.getAuthor(), 
      news.getDate(), news.getTitle(), news.getContent()));
    }
    return response;
  }

  private BasicResponse setImageNews(int id, MultipartFile file) {
    BasicResponse response = new BasicResponse();
    if (file.isEmpty()) {
      response.setSuccess(false);
      response.setError("Файл не должен быть пустым");
      return response;
    }
    Optional<News> newsOpt = getNewsById(id);
    try {
      if (newsOpt.isEmpty()) {
        response.setSuccess(false);
        response.setError("No users with this id");
      }
      News news = newsOpt.get();
      byte[] fileBytes = file.getBytes();
      Blob blob = new SerialBlob(fileBytes);
      System.out.println(blob);
      news.setImage(blob);
      createNews(news);
      response.setSuccess(true);
      return response;
    } catch (Exception err) {
      System.out.println(err.getMessage());
      return response;
    }
  }

  private BasicResponse setNewsInfo(News newInf) {
    BasicResponse response = new BasicResponse();
    Optional<News> newsOpt = getNewsById(newInf.getId());
    try {
      if (newsOpt.isEmpty()) {
        createNews(newInf);
        response.setSuccess(true);
        return response;
        //response.setError("No news with this id");
      }
      News news = newsOpt.get();
      if(!(newInf.getTitle().equals(""))){
        news.setTitle(newInf.getTitle());
      }
      if(!(newInf.getContent().equals(""))){
        news.setContent(newInf.getContent());
      }
      createNews(news);
      response.setSuccess(true);
      return response;
    } catch (Exception err) {
      System.out.println(err.getMessage());
      response.setSuccess(false);
      response.setError(err.getMessage());
      return response;
    }
  }

}