package com.klef.jfsd.springboot.service;

import com.klef.jfsd.springboot.model.Student;

public interface StudentService {
	public Student checkStudentLogin(String username,String password);
	public Student getStudentByID(String sid);
}
