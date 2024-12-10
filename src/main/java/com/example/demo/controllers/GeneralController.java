package com.example.demo.controllers;

import java.net.http.HttpHeaders;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
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

import com.example.demo.models.Footer;
import com.example.demo.models.General;
import com.example.demo.models.User;
import com.example.demo.models.UserId;
import com.example.demo.models.UserModifierPrivateInfo;
import com.example.demo.models.UserPrivateInfo;
import com.example.demo.models.UserEnter;
import com.example.demo.models.UserEnterResponse;
import com.example.demo.models.UserRegistrationResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller

@RequestMapping(path = "/general")
public class GeneralController {

  @Autowired
  private GeneralService generalService;
  /*
   * @Autowired
   * private UserRepository userRepository;
   */

  @GetMapping("/{id}")
  public Optional<General> getVisitsById(@PathVariable int id) {
    return generalService.getVisitsById(id);
  }

  /**
   * обрабатывает POST-запросы на вход в аккаунт
   * 
   * @param email    - введенная почта
   * @param password - введенный пароль
   * @return json объект с результатом обработки запроса
   */
  @PostMapping(path = "/footer", produces = "application/json")
  public @ResponseBody ResponseEntity<String> enter() {
    Footer response = getFooter();

    try {
      String jsonResponse = new ObjectMapper().writeValueAsString(response); // Преобразование объекта в JSON строку
      return ResponseEntity.ok()
          .contentType(MediaType.APPLICATION_JSON)
          .body(jsonResponse);
    } catch (Exception ex) {
      System.out.println(ex.getMessage());
      return ResponseEntity.ok(null);
    }
  }

  private Footer getFooter(){
    Footer response = new Footer();

    String LD_PATTERN = "yyyy-MM-dd";
    DateTimeFormatter LD_FORMATTER= DateTimeFormatter.ofPattern(LD_PATTERN);
    String dateString = LD_FORMATTER.format(LocalDate.now());
    response.setDate(dateString);

    Optional <General> generalOptional = getVisitsById(1);
    try {
      if (generalOptional.isEmpty()) {
        response.setVisits(-1);
        return response;
      }
      General gen = generalOptional.get();
      response.setVisits(gen.getVisits());
      return response;
    } catch (Exception err) {
      System.out.println(err.getMessage());
      return response;
    }
  }
}