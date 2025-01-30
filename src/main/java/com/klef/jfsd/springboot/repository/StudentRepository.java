package com.klef.jfsd.springboot.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.klef.jfsd.springboot.model.Student;


@Repository
public interface StudentRepository extends JpaRepository<Student, String> {
	Optional<Student> findById(String id);
	
	Student findBySidAndPassword(String sid, String password);
	
}
