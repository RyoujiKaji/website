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
import com.example.demo.models.UserEnter;
import com.example.demo.models.UserEnterResponse;
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
  public User createStudent(@RequestBody User user) {
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
   * проверяет наличие пользователя с такими данными в бд
   * 
   * @param new_user - пользователь, которого нужно проверить
   * @return найденного пользователя или нового незаполненного пользователя
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
      if(userOpt.isEmpty()){
        throw new Exception("No users with this id");
      }
      User user = userOpt.get();
      //Если введен неверный пароль
      if(!(password.equals(user.getPassword()))){
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
   * @return найденного пользователя или нового незаполненного пользователя
   */
  @PostMapping(path = "/enter", produces = "application/json")
  public @ResponseBody ResponseEntity<String> enter(@RequestBody UserEnter userInput) {
    
    //System.out.println(checkUserForEnter(userInput.getEmail(), userInput.getPassword()).getSuccess());
    //return checkUserForEnter(userInput.getEmail(), userInput.getPassword());
    UserEnterResponse response = checkUserForEnter(userInput.getEmail(), userInput.getPassword());

    try{
    String jsonResponse = new ObjectMapper().writeValueAsString(response); // Преобразование объекта в JSON строку
    return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(jsonResponse);
    }catch (Exception ex){
      System.out.println(ex.getMessage());
      return ResponseEntity.ok(null);
    }
  }

  /*
   * /**
   * обрабатывает POST-запросы на вход в аккаунт
   * 
   * @param mail - введенная почта
   * 
   * @param password - введенный пароль
   * 
   * @return найденного пользователя или нового незаполненного пользователя
   * 
   * @PostMapping(path = "/enter", produces = "application/json")
   * public @ResponseBody User enter(@RequestParam String mail, @RequestParam
   * String password) {
   * User user = new User();
   * user.setMail(mail);
   * user.setPassword(password);
   * User us1 = checkUserForEnter(user);
   * if (us1.getMail() == null) {
   * // return "OK";
   * return new User();
   * }
   * return us1;
   * }
   */
}