package com.example.demo.controllers.General;

import java.net.http.HttpHeaders;
import java.sql.Blob;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Date;

import javax.sql.rowset.serial.SerialBlob;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Optionals;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.example.demo.models.General.General;
import com.example.demo.models.General.ModelsForResponse.BasicResponse;
import com.example.demo.models.General.ModelsForResponse.Footer;
import com.example.demo.models.Users.User;
import com.example.demo.models.Users.ModelsForRequest.UserEnter;
import com.example.demo.models.Users.ModelsForRequest.UserId;
import com.example.demo.models.Users.ModelsForRequest.UserModifierPrivateInfo;
import com.example.demo.models.Users.ModelsForResponse.UserEnterResponse;
import com.example.demo.models.Users.ModelsForResponse.UserPrivateInfo;
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

  @PostMapping
  public General upDateVisits(@RequestBody General general) {
    return generalService.updateVisits(general);
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

@PostMapping(path = "/update")
  public @ResponseBody ResponseEntity<String> update() {
    BasicResponse response = processUpdate();
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

  private BasicResponse processUpdate(){
    BasicResponse response = new BasicResponse();
    response.setSuccess(false);
    response.setError("Ошибка запроса");
    try {
      Optional<General> generalOptional = getVisitsById(1);
      if (generalOptional.isEmpty()) {
        return response;
      }
      General gen = generalOptional.get();
      int vis = gen.getVisits();
      vis+=1;
      gen.setVisits(vis);
      upDateVisits(gen);
      response.setSuccess(true);
      return response;
    } catch (Exception ex) {
      System.out.println(ex.getMessage());
      return response;
    }
  }

  private Footer getFooter() {
    Footer response = new Footer();

    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    Date date = new Date();
    String dateString = sdf.format(date);

    /* String LD_PATTERN = "dd.MM.yyyy";
    DateTimeFormatter LD_FORMATTER = DateTimeFormatter.ofPattern(LD_PATTERN);
    String dateString = LD_FORMATTER.format(LocalDate.now()); */
    response.setDate(dateString);

    Optional<General> generalOptional = getVisitsById(1);
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