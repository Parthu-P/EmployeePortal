package com.parthu.service;

import java.util.List;

import com.parthu.binding.DashBoardReponseForm;
import com.parthu.binding.EnquiryForm;
import com.parthu.binding.EnquirySearchCriteria;
import com.parthu.entity.StudentEnqEntity;

public interface EnquiryService {

	public List<String> getCourses();

	public List<String> getEnqStatus();

	public boolean addEnquiry(EnquiryForm form);

	public DashBoardReponseForm getDashBoard(Integer userId);

	public List<StudentEnqEntity> getEnquiries();

	public List<StudentEnqEntity> getFilterEnq(EnquirySearchCriteria criteria, Integer userId);
	public StudentEnqEntity getStudentEnq(Integer enquiryId);
}
