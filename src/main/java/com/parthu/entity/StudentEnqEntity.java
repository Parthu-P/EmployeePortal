package com.parthu.entity;

import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;

@Entity
@Data
public class StudentEnqEntity {
	
	@Id
	@GeneratedValue
	private Integer enquiryId;
	private String studentName;
	private Long phNo;
	private String classMode;
	private String courseName;
	
	private String enquiryStatus;
	
	@CreationTimestamp
	private LocalDate createdDate;
	
	@UpdateTimestamp
	private LocalDate updatedDate;
	
	@ManyToOne
	@JoinColumn(name="user_Id")
	private UserDtlsEntity user;

}
