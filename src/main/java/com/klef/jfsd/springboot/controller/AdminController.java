package com.klef.jfsd.springboot.controller;

import java.util.HashMap;
import java.util.List;
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
import org.springframework.web.bind.annotation.RestController;

import com.klef.jfsd.springboot.config.JwtService;
import com.klef.jfsd.springboot.config.UserService;
import com.klef.jfsd.springboot.model.Admin;
import com.klef.jfsd.springboot.model.Student;
import com.klef.jfsd.springboot.service.AdminService;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    
    /**
     * This Java function handles the authentication and login process for an admin user, generating a JWT token upon successful authentication.
     * 
     * @param admin The `adminLogin` method is a POST mapping that handles the authentication process for admin users. It takes an `Admin` object as a request body, which likely contains the admin's username and password for authentication.
     * @return The method `adminLogin` is returning a `ResponseEntity` object. If the authentication and verification of the admin credentials are successful, it returns a response with a JWT token and a success message. If there is an issue during the process, it returns an appropriate error response with the corresponding status code and message.
     */
    @PostMapping("/adminlogin")
    public ResponseEntity<?> adminLogin(@RequestBody Admin admin) {
        try {
            // Authenticate the admin credentials
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(admin.getUsername(), admin.getPassword())
            );

            // Verify the admin in the database
            Admin verifiedAdmin = adminService.checkAdminlogin(admin);
            if (verifiedAdmin == null) {
                return ResponseEntity.status(404).body("Invalid Credentials");
            }

            // Generate JWT token for authenticated admin
            UserDetails userDetails = userService.loadUserByUsername(verifiedAdmin.getUsername());
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

    
    // These two methods in the `AdminController` class are responsible for handling requests related to the admin home page and viewing all students.are related to role-based access control (RBAC) using Spring Security annotations. Here's a breakdown of each method:
    @GetMapping("/adminhome")
    @PreAuthorize("hasRole('ADMIN')")//For RBAC -> Role Based Access-Control
    public String adminHome() {
        System.out.println("Admin home request received");
        return "This is Admin Home Page";
    }
    
    @GetMapping("/viewall")
    @PreAuthorize("hasRole('ADMIN')")//For RBAC -> Role Based Access-Control
    public List<Student> viewAllStudents(){
    	return adminService.getAllStudents();
    }
}
