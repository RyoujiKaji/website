package com.example.demo.controllers;

import java.sql.Blob;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Optionals;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.MediaType;

import com.example.demo.models.User;
import com.example.demo.models.UserId;
import com.example.demo.models.UserPrivateInfo;
import com.example.demo.models.UserEnter;
import com.example.demo.models.UserEnterResponse;
import com.example.demo.models.UserRegistrationResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller

@RequestMapping(path = "/users")
public class UserController {

  @Autowired
  private UserService userService;
  /*
   * @Autowired
   * private UserRepository userRepository;
   */

  @GetMapping
  public List<User> getAllUsers() {
    return userService.getAllUsers();
  }

  @GetMapping("/{id}")
  public Optional<User> getUserById(@PathVariable int id) {
    return userService.getUserById(id);
  }

  @PostMapping
  public User createUser(@RequestBody User user) {
    return userService.createUser(user);
  }

  @DeleteMapping("/{id}")
  public void deleteStudent(@PathVariable int id) {
    userService.deleteUser(id);
  }

  // Map ONLY POST Requests
  /*
   * @PostMapping(path = "/adduser", produces = "application/json")
   * public @ResponseBody User addUsers(@RequestParam String mail, @RequestParam
   * String password,
   * 
   * @RequestParam String role,
   * 
   * @RequestParam Blob image) {
   * 
   * // @ResponseBody means the returned String
   * // is the response, not a view name
   * // @RequestParam means it is a parameter
   * // from the GET or POST request
   * 
   * User user = new User();
   * user.setMail(mail);
   * user.setPassword(password);
   * user.setRole(role);
   * user.setImage(image);
   * if (checkUserExistence(user) == true) {
   * userRepository.save(user);
   * return user;
   * // return "Details got Saved";
   * }
   * return new User();
   * // else return "This email has already used";
   * }
   */

  /**
   * проверяет наличие пользователя с таким email в бд и, при его наличии,
   * возвращает его id
   * 
   * @param email - почта нового пользователя
   * @return id>0 - если пользователь найден, 0 - если нет
   */
  private int checkUserExistence(String email) {
    List<User> listOfAllUsers = getAllUsers();
    for (User user : listOfAllUsers) {
      if (email.equals(user.getEmail())) {
        return user.getId();
      }
    }
    return 0;
  }

  /**
   * обрабатывает попытку входа
   * 
   * @param email    - введенная почта пользователя
   * @param password - введенный пароль пользователя
   * @return Объект с результатом обработки
   */
  private UserEnterResponse checkUserForEnter(String email, String password) {
    UserEnterResponse response = new UserEnterResponse();
    int userId = checkUserExistence(email);
    if (userId < 1) { // такого пользователя нет
      response.setSuccess(false);
      return response;
    }
    // Если пользователь с такой почтой есть
    Optional<User> userOpt = getUserById(userId);
    try {
      if (userOpt.isEmpty()) {
        throw new Exception("No users with this id");
      }
      User user = userOpt.get();
      // Если введен неверный пароль
      if (!(password.equals(user.getPassword()))) {
        response.setSuccess(false);
        return response;
      }
      response.setSuccess(true);
      response.setId(userId);
      response.setRole(user.getRole());
      return response;

    } catch (Exception err) {
      System.out.println(err.getMessage());
      return response;
    }
  }

  /**
   * обрабатывает POST-запросы на вход в аккаунт
   * 
   * @param email    - введенная почта
   * @param password - введенный пароль
   * @return json объект с результатом обработки запроса
   */
  @PostMapping(path = "/enter", produces = "application/json")
  public @ResponseBody ResponseEntity<String> enter(@RequestBody UserEnter userInput) {
    UserEnterResponse response = checkUserForEnter(userInput.getEmail(), userInput.getPassword());

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

  @PostMapping(path = "/registration", produces = "application/json")
  public @ResponseBody ResponseEntity<String> registration(@RequestBody User userInput) {
    UserRegistrationResponse response = checkUserForRegistration(userInput);
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

  @PostMapping(path = "/privateinfo", produces = "application/json")
  public @ResponseBody ResponseEntity<String> privateinfo(@RequestBody UserId userInput) {
    try {
      UserPrivateInfo response = getUserInfo(userInput.getId());
      String jsonResponse = new ObjectMapper().writeValueAsString(response); // Преобразование объекта в JSON строку
      return ResponseEntity.ok()
          .contentType(MediaType.APPLICATION_JSON)
          .body(jsonResponse);
    } catch (Exception ex) {
      System.out.println(ex.getMessage());
      return ResponseEntity.ok(null);
    }
  }

  private UserRegistrationResponse checkUserForRegistration(User user) {
    UserRegistrationResponse response = new UserRegistrationResponse();
    int userId = checkUserExistence(user.getEmail());
    if (userId > 0) { // такой пользователь есть
      response.setSuccess(false);
      response.setError("Пользователь с такой почтой уже зарегистрирован");
      return response;
    }
    // Если пользователя с такой почтой нет
    try {
      createUser(user);
      response.setSuccess(true);
      return response;
    } catch (Exception err) {
      System.out.println(err.getMessage());
      response.setSuccess(false);
      response.setError(err.getMessage());
      return response;
    }
  }

  private UserPrivateInfo getUserInfo(int id) throws Exception{
    UserPrivateInfo response = new UserPrivateInfo();
    Optional<User> userOpt = getUserById(id);
    try {
      if (userOpt.isEmpty()) {
        throw new Exception("No users with this id");
      }
      User user = userOpt.get();
      response.setName(user.getName());
      response.setDate(user.getDate());
      response.setEmail(user.getEmail());
      return response;
    } catch (Exception err) {
      System.out.println(err.getMessage());
      return response;
    }
  }
}