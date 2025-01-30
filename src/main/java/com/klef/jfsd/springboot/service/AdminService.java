package com.klef.jfsd.springboot.service;

import java.util.List;

import com.klef.jfsd.springboot.model.Admin;
import com.klef.jfsd.springboot.model.Student;

public interface AdminService {
	
	public Admin checkAdminlogin(Admin admin);
	public String AddStudent(Student student);
	public List<Student> getAllStudents();

}
