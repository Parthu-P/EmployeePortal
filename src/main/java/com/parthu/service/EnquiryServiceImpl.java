package com.parthu.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.parthu.binding.DashBoardReponseForm;
import com.parthu.binding.EnquiryForm;
import com.parthu.binding.EnquirySearchCriteria;
import com.parthu.entity.CourseEntity;
import com.parthu.entity.EnqStatusEntity;
import com.parthu.entity.StudentEnqEntity;
import com.parthu.entity.UserDtlsEntity;
import com.parthu.repo.CourseRepo;
import com.parthu.repo.EnqStatusRepo;
import com.parthu.repo.StudentEnqRepo;
import com.parthu.repo.UserDtlsRepo;

@Service
public class EnquiryServiceImpl implements EnquiryService {

	@Autowired
	private UserDtlsRepo userRepo;

	@Autowired
	private CourseRepo courseRepo;

	@Autowired
	private EnqStatusRepo statusRepo;

	@Autowired
	private StudentEnqRepo studentRepo;

	@Autowired
	private HttpSession session;

	@Override
	public List<String> getCourses() {
		List<CourseEntity> findAll = courseRepo.findAll();
		List<String> names = new ArrayList<>();

		for (CourseEntity entity : findAll) {
			names.add(entity.getCourseName());
		}
		return names;
	}

	@Override
	public List<String> getEnqStatus() {
		List<EnqStatusEntity> findAll = statusRepo.findAll();
		List<String> status = new ArrayList<>();

		for (EnqStatusEntity entity : findAll) {
			status.add(entity.getStatusName());
		}
		return status;
	}

	@Override
	public boolean addEnquiry(EnquiryForm form) {
		StudentEnqEntity enqEntity = new StudentEnqEntity();
		BeanUtils.copyProperties(form, enqEntity);

		Integer userId = (Integer) session.getAttribute("userId");
		Optional<UserDtlsEntity> userEntity = userRepo.findById(userId);
		if (userEntity.isPresent()) {
			enqEntity.setUser(userEntity.get());
			studentRepo.save(enqEntity);
			return true;
		}
		return false;

	}

	@Override
	public DashBoardReponseForm getDashBoard(Integer userId) {

		DashBoardReponseForm response = new DashBoardReponseForm();

		Optional<UserDtlsEntity> findById = userRepo.findById(userId);

		if (findById.isPresent()) {
			UserDtlsEntity userEntity = findById.get();

			List<StudentEnqEntity> enquiries = userEntity.getEnquiries();
			Integer total = enquiries.size();

			Integer enrolledCnt = enquiries.stream().filter(e -> e.getEnquiryStatus().equals("Enrolled"))
					.collect(Collectors.toList()).size();

			Integer lostCnt = enquiries.stream().filter(e -> e.getEnquiryStatus().equals("Lost"))
					.collect(Collectors.toList()).size();
			response.setTotalEnquCunt(total);
			response.setEnrolledCunt(enrolledCnt);
			response.setLostCunt(lostCnt);

		}
		return response;
	}

	@Override
	public List<StudentEnqEntity> getEnquiries() {
		Integer userId = (Integer) session.getAttribute("userId");
		Optional<UserDtlsEntity> findById = userRepo.findById(userId);
		if (findById.isPresent()) {
			UserDtlsEntity userDtlsEntity = findById.get();
			return userDtlsEntity.getEnquiries();
		}
		return Collections.emptyList();
	}

	@Override
	public List<StudentEnqEntity> getFilterEnq(EnquirySearchCriteria criteria, Integer userId) {
	    Optional<UserDtlsEntity> findById = userRepo.findById(userId);
	    if (findById.isPresent()) {
	        UserDtlsEntity userDtlsEntity = findById.get();
	        List<StudentEnqEntity> enquiries = userDtlsEntity.getEnquiries();
	        if (null != criteria.getCourseName() && !"".equals(criteria.getCourseName())) {
	            enquiries = enquiries.stream().filter(e -> e.getCourseName().equals(criteria.getCourseName()))
	                    .collect(Collectors.toList());
	        }
	        if (null != criteria.getEnquiryStatus() && !"".equals(criteria.getEnquiryStatus())) {
	            enquiries = enquiries.stream().filter(e -> e.getEnquiryStatus().equals(criteria.getEnquiryStatus()))
	                    .collect(Collectors.toList());
	        }
	        if (null != criteria.getClassMode() && !"".equals(criteria.getClassMode())) {
	            enquiries = enquiries.stream().filter(e -> e.getClassMode().equals(criteria.getClassMode()))
	                    .collect(Collectors.toList());
	        }
	        return enquiries;
	    }
	    return Collections.emptyList(); 
	}


	public StudentEnqEntity getStudentEnq(Integer enquiryId) {
	    Optional<StudentEnqEntity> findById = studentRepo.findById(enquiryId);
	    return findById.orElse(null); 
	}


}
