package com.example.demo;

/*import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SqlAccessApplication {

	public static void main(String[] args) {
		SpringApplication.run(SqlAccessApplication.class, args);
	}

}
*/

/*import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SqlAccessApplication {
    public static void main(String[] args) {*/
        /*String url = "jdbc:mysql://localhost:3306/demo1";
        String username = "root";
        String password = "123";
        System.out.println("Connecting...");

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            System.out.println("Connection successful!");
        } catch (SQLException e) {
            System.out.println("Connection failed!");
            e.printStackTrace();
        }*/



import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SqlAccessApplication {

	public static void main(String[] args) {
		SpringApplication.run(SqlAccessApplication.class, args);
	}

}
