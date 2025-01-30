package com.klef.jfsd.springboot.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.klef.jfsd.springboot.config.JwtService;
import com.klef.jfsd.springboot.config.UserService;
import com.klef.jfsd.springboot.model.Student;
import com.klef.jfsd.springboot.service.StudentService;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    
    /**
     * The function `checkStudentLogin` handles student login authentication, verification, and token generation using JWT in a Java Spring application.
     * 
     * @param request The `checkStudentLogin` method is a POST mapping that handles student login authentication. It takes a request body in the form of a Map<String, String> containing the username and password of the student trying to log in.
     * @return The method `checkStudentLogin` is returning a `ResponseEntity` object. If the student login is successful, it returns a response with a JWT token and a success message. If there is an issue with the login credentials or any other error occurs, it returns an appropriate error response with the corresponding status code and message.
     */
    @PostMapping("/studentlogin")
    public ResponseEntity<?> checkStudentLogin(@RequestBody Map<String, String> request) {
        try {
            String username = request.get("username");
            String password = request.get("password");

            // Authenticate the student credentials
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
            );

            // Verify the student in the database
            Student student = studentService.checkStudentLogin(username, password);
            if (student == null) {
                return ResponseEntity.status(404).body("Invalid Credentials");
            }

            // Generate JWT token for the authenticated student
            UserDetails userDetails = userService.loadUserByUsername(username);
            String token = jwtService.generateToken(userDetails);

            // Prepare the response
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("message", "Login successful");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Server Error: " + e.getMessage());
        }
    }


    
    // These two methods in the `StudentController` class are related to role-based access control (RBAC) using Spring Security annotations:
    @GetMapping("/studenthome")
    @PreAuthorize("hasRole('STUDENT')")//For RBAC -> Role Based Access-Control
    public String studentHome() {
        return "This is Student Home Page";
    }
    
    @GetMapping("/view")
    @PreAuthorize("hasRole('STUDENT')")//For RBAC -> Role Based Access-Control
    public Student studentProfile(@RequestParam("id") String sid) {
    	return studentService.getStudentByID(sid);
    }
}