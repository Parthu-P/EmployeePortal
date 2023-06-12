package com.parthu.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.parthu.binding.DashBoardReponseForm;
import com.parthu.binding.EnquiryForm;
import com.parthu.binding.EnquirySearchCriteria;
import com.parthu.constent.AppConstent;
import com.parthu.entity.StudentEnqEntity;
import com.parthu.repo.StudentEnqRepo;
import com.parthu.service.EnquiryService;

@Controller
public class EnquiryController {

	@Autowired
	private HttpSession session;

	@Autowired
	private EnquiryService enqService;
	@Autowired
	private StudentEnqRepo repo;

	@GetMapping("/logout")
	public String logout() {
		session.invalidate();

		return "index";
	}

	@GetMapping("/dashBoard")
	public String dashBoardPage(Model model) {

		Integer userId = (Integer) session.getAttribute("userId");
		DashBoardReponseForm dashboardData = enqService.getDashBoard(userId);

		model.addAttribute("dashBoard", dashboardData);

		return "dashBoard";
	}

	@GetMapping("/addEnquiry")
	public String addEnquiryPage(Model model) {

		List<String> courses = enqService.getCourses();

		List<String> enqStatus = enqService.getEnqStatus();

		EnquiryForm formObj = new EnquiryForm();

		model.addAttribute(AppConstent.COURCR_NAMES, courses);
		model.addAttribute(AppConstent.STATUS_NAMES, enqStatus);
		model.addAttribute(AppConstent.FORM_OBJ, formObj);

		return "addEnquiry";
	}

	private void initForm(Model model) {
		List<String> courses = enqService.getCourses();

		List<String> enqStatus = enqService.getEnqStatus();

		EnquiryForm formObj = new EnquiryForm();

		model.addAttribute(AppConstent.COURCR_NAMES, courses);
		model.addAttribute(AppConstent.STATUS_NAMES, enqStatus);
		model.addAttribute(AppConstent.FORM_OBJ, formObj);
	}

	@PostMapping("/addEnquiry")
	public String addEnquiry(@ModelAttribute("formObj") EnquiryForm formObj, Model model) {

		boolean status = enqService.addEnquiry(formObj);

		if (status) {
			model.addAttribute("successMsg", "Enquiry Added");
		} else {
			model.addAttribute("errorMsg", "Problem Occured");
		}

		return "addEnquiry";
	}

	@GetMapping("/viewEnquiries")
	public String viewEnquiriesPage(EnquirySearchCriteria criteria, Model model) {
		initForm(model);
		model.addAttribute("searchForm", new EnquirySearchCriteria());
		List<StudentEnqEntity> enquiries = enqService.getEnquiries();
		model.addAttribute("enquiries", enquiries);
		return "viewEnquiries";
	}

	@GetMapping("/filter-enquiries")
	public String getFilteredEnqs(@RequestParam String cname, @RequestParam String mode, @RequestParam String status,
			Model model) {
		EnquirySearchCriteria criteria = new EnquirySearchCriteria();
		criteria.setClassMode(mode);
		criteria.setCourseName(cname);
		criteria.setEnquiryStatus(status);

		Integer userId = (Integer) session.getAttribute("userId");
		List<StudentEnqEntity> filterEnq = enqService.getFilterEnq(criteria, userId);
		model.addAttribute("enquiries", filterEnq);

		return "filter-enq";
	}

	@GetMapping("/edit")
	public String editEnq(@RequestParam("enquiryId") Integer enquiryId, Model model) {

		EnquiryForm formObj = new EnquiryForm();

		Optional<StudentEnqEntity> form = repo.findById(enquiryId);
		if (form.isPresent()) {
			StudentEnqEntity f = form.get();
			BeanUtils.copyProperties(f, formObj);

			List<String> courses = enqService.getCourses();

			List<String> enqStatus = enqService.getEnqStatus();

			model.addAttribute(AppConstent.COURCR_NAMES, courses);
			model.addAttribute(AppConstent.STATUS_NAMES, enqStatus);
			model.addAttribute(AppConstent.FORM_OBJ, formObj);

			return "addEnquiry";
		}
		return null;
	}

}
