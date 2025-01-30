package com.klef.jfsd.springboot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.klef.jfsd.springboot.model.Student;
import com.klef.jfsd.springboot.repository.StudentRepository;

@Service
public class StudentServiceImpl implements StudentService {
	
	@Autowired
	StudentRepository studentRepository;

	@Override
	public Student checkStudentLogin(String username, String password) {
		Student student = studentRepository.findBySidAndPassword(username, password);
		return student;
	}

	@Override
	public Student getStudentByID(String sid) {
		Student student = studentRepository.findById(sid).get();
		return student;
	}

}
