package com.example.demo.controllers.Users;

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
import com.example.demo.models.Users_folder.User;
import com.example.demo.models.Users_folder.ModelsForRequest.UserEnter;
import com.example.demo.models.Users_folder.ModelsForRequest.UserModifierPrivateInfo;
import com.example.demo.models.Users_folder.ModelsForRequest.UserRole;
import com.example.demo.models.Users_folder.ModelsForResponse.UserEnterResponse;
import com.example.demo.models.Users_folder.ModelsForResponse.UserInfWihoutImg;
import com.example.demo.models.Users_folder.ModelsForResponse.UserPrivateInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

  private List<UserInfWihoutImg> getAllUsersWithoutImg(){
    List<User> users = getAllUsers();
    List<UserInfWihoutImg> response= new LinkedList<UserInfWihoutImg>();
    for (User user : users) {
      response.add(new UserInfWihoutImg(user.getId(), user.getName(), user.getDate(), user.getEmail(), user.getRole()));
    }
    return response;
  }

  @PostMapping(path = "/allUserswithoutImg", produces = "application/json")
  public @ResponseBody ResponseEntity<String> allUserswithoutImg() {
    List<UserInfWihoutImg> UsersList = getAllUsersWithoutImg();
    try {
      String jsonResponse = new ObjectMapper().writeValueAsString(UsersList); // Преобразование объекта в JSON строку
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
    BasicResponse response = checkUserForRegistration(userInput);
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

  @PostMapping(path = "/fixprivateinfo", produces = "application/json")
  public @ResponseBody ResponseEntity<String> fixprivateinfo(@RequestBody UserModifierPrivateInfo userInput) {
    try {
      BasicResponse response = setUserInfo(userInput);
      String jsonResponse = new ObjectMapper().writeValueAsString(response); // Преобразование объекта в JSON строку
      return ResponseEntity.ok()
          .contentType(MediaType.APPLICATION_JSON)
          .body(jsonResponse);
    } catch (Exception ex) {
      System.out.println(ex.getMessage());
      return ResponseEntity.ok(null);
    }
  }

  @PostMapping(path = "/fixrole", produces = "application/json")
  public @ResponseBody ResponseEntity<String> fixrole(@RequestBody UserRole userInput) {
    try {
      BasicResponse response = setUserRole(userInput);
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
  public @ResponseBody ResponseEntity<String> privateinfo(@RequestBody InputId userInput) {
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

  @PostMapping("/avatar")
  public ResponseEntity<byte[]> avatar(@RequestBody InputId userInput) {
    try {
      byte[] image = getAvatar(userInput.getId());
      if (image == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
      }
      return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
    } catch (Exception ex) {
      System.out.println(ex.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
  }

  @PostMapping(path = "/fixavatar", produces = "application/json")
  public ResponseEntity<String> fixavatar(@RequestParam("image") MultipartFile file, @RequestParam("id") String id) {
    try {
      BasicResponse response = setAvatar(Integer.parseInt(id), file);
      String jsonResponse = new ObjectMapper().writeValueAsString(response); // Преобразование объекта в JSON строку
      return ResponseEntity.ok()
          .contentType(MediaType.APPLICATION_JSON)
          .body(jsonResponse);
    } catch (Exception ex) {
      System.out.println(ex.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
  }

  private BasicResponse setAvatar(int id, MultipartFile file) {
    BasicResponse response = new BasicResponse();
    if (file.isEmpty()) {
      response.setSuccess(false);
      response.setError("Файл не должен быть пустым");
      return response;
    }
    Optional<User> userOpt = getUserById(id);
    try {
      if (userOpt.isEmpty()) {
        response.setSuccess(false);
        response.setError("No users with this id");
      }
      User user = userOpt.get();
      byte[] fileBytes = file.getBytes();
      Blob blob = new SerialBlob(fileBytes);
      System.out.println(blob);
      user.setImage(blob);
      createUser(user);
      response.setSuccess(true);
      return response;
    } catch (Exception err) {
      System.out.println(err.getMessage());
      return response;
    }
  }

  private byte[] getAvatar(int id) {
    byte[] response = null;
    Optional<User> userOpt = getUserById(id);
    try {
      if (userOpt.isEmpty()) {
        throw new Exception("No users with this id");
      }
      User user = userOpt.get();
      response = blobToByteArray(user.getImage());
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

  private BasicResponse checkUserForRegistration(User user) {
    BasicResponse response = new BasicResponse();
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

  private UserPrivateInfo getUserInfo(int id) throws Exception {
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

  private BasicResponse setUserInfo(UserModifierPrivateInfo newInf) {
    BasicResponse response = new BasicResponse();
    Optional<User> userOpt = getUserById(newInf.getId());
    try {
      if (userOpt.isEmpty()) {
        response.setSuccess(false);
        response.setError("No users with this id");
      }
      User user = userOpt.get();
      if (!(user.getName().equals(newInf.getName()))) {
        user.setName(newInf.getName());
      }
      if (!(user.getDate().equals(newInf.getDate()))) {
        user.setDate(newInf.getDate());
      }
      if (!(user.getEmail().equals(newInf.getEmail()))) {
        user.setEmail(newInf.getEmail());
      }
      createUser(user);
      response.setSuccess(true);
      return response;
    } catch (Exception err) {
      System.out.println(err.getMessage());
      return response;
    }
  }

  private BasicResponse setUserRole(UserRole newInf) {
    BasicResponse response = new BasicResponse();
    Optional<User> userOpt = getUserById(newInf.getId());
    try {
      if (userOpt.isEmpty()) {
        response.setSuccess(false);
        response.setError("No users with this id");
      }
      User user = userOpt.get();
      user.setRole(newInf.getRole());
      createUser(user);
      response.setSuccess(true);
      return response;
    } catch (Exception err) {
      System.out.println(err.getMessage());
      return response;
    }
  }
}