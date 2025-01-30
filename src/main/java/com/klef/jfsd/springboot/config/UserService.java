package com.klef.jfsd.springboot.config;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.klef.jfsd.springboot.model.Admin;
import com.klef.jfsd.springboot.model.Student;
import com.klef.jfsd.springboot.repository.AdminRepository;
import com.klef.jfsd.springboot.repository.StudentRepository;

@Service
/**
 * The `UserService` class implements `UserDetailsService` to load user details by username, checking if the user is an admin or a student based on repositories.
 */
public class UserService implements UserDetailsService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("UserDetailsService called with username: " + username);

        // Check if user is an Admin
        Admin admin = adminRepository.findByUsername(username).orElse(null);
        if (admin != null) {
            System.out.println("Admin found: " + admin.getUsername());
            return new User(admin.getUsername(), admin.getPassword(),
                    Collections.singleton(() -> "ROLE_ADMIN"));
        }

        // Check if user is a Student
        Student student = studentRepository.findById(username).orElse(null);
        if (student != null) {
            System.out.println("Student found: " + student.getSid());
            return new User(student.getSid(), student.getPassword(),
                    Collections.singleton(() -> "ROLE_STUDENT"));
        }

        // Throw exception if user is not found
        throw new UsernameNotFoundException("User not found with username: " + username);
    }
}
