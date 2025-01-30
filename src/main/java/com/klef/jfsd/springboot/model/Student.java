package com.klef.jfsd.springboot.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "student_table")
public class Student {
	
	@Id
	private String sid;
	private String name;
	private String password;
	
	
}
