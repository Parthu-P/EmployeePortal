package com.parthu.runner;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.parthu.entity.CourseEntity;
import com.parthu.entity.EnqStatusEntity;
import com.parthu.repo.CourseRepo;
import com.parthu.repo.EnqStatusRepo;

@Component
public class DataLoader implements ApplicationRunner {

	@Autowired
	private CourseRepo courseRepo;
	
	@Autowired
	private EnqStatusRepo statusRepo;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		
		courseRepo.deleteAll();

		CourseEntity course1 = new CourseEntity();
		course1.setCourseName("Java Fullstack");
		
		CourseEntity course2 = new CourseEntity();
		course2.setCourseName("DevOps");
		
		CourseEntity course3 = new CourseEntity();
		course3.setCourseName("AWS");

		List<CourseEntity> asList = Arrays.asList(course1, course2, course3);
		courseRepo.saveAll(asList);
		
		statusRepo.deleteAll();
		
		EnqStatusEntity status1 = new EnqStatusEntity();
		status1.setStatusName("New");
		
		EnqStatusEntity status2 = new EnqStatusEntity();
		status2.setStatusName("Enrolled");
		
		EnqStatusEntity status3 = new EnqStatusEntity();
		status3.setStatusName("Lost");

		List<EnqStatusEntity> statusList = Arrays.asList(status1, status2, status3);
		statusRepo.saveAll(statusList);

	}
}
