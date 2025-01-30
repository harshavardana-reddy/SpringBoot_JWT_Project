package com.klef.jfsd.springboot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.klef.jfsd.springboot.model.Admin;
import com.klef.jfsd.springboot.model.Student;
import com.klef.jfsd.springboot.repository.AdminRepository;
import com.klef.jfsd.springboot.repository.StudentRepository;

@Service
public class AdminServiceImpl implements AdminService {
	
	@Autowired
	AdminRepository adminRepository;
	
	@Autowired
	StudentRepository studentRepository;

	@Override
	public Admin checkAdminlogin(Admin admin) {
		Admin tempAdmin = adminRepository.findByUsernameAndPassword(admin.getUsername(), admin.getPassword());
		return tempAdmin;
	}

	@Override
	public String AddStudent(Student student) {
		studentRepository.save(student);
		return "Student Added succesfully";
	}

	@Override
	public List<Student> getAllStudents() {
		return studentRepository.findAll();
	}
	
}
