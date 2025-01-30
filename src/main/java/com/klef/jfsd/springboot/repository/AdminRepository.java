package com.klef.jfsd.springboot.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.klef.jfsd.springboot.model.Admin;



@Repository
public interface AdminRepository extends JpaRepository<Admin, String> {
	Optional<Admin> findByUsername(String username);
	
	Admin findByUsernameAndPassword(String username, String password);
}
