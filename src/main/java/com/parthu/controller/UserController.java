package com.parthu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.parthu.binding.LogInForm;
import com.parthu.binding.SignUpForm;
import com.parthu.binding.UnlockForm;
import com.parthu.constent.AppConstent;
import com.parthu.service.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping("/signup")
	public String handleSignup(@ModelAttribute("user") SignUpForm form, Model model) {
		boolean status = userService.signUp(form);
		if (status) {
			model.addAttribute(AppConstent.USER_SUCC);
		} else {
			model.addAttribute(AppConstent.USER_ERR);
		}
		return "signup";
	}

	@GetMapping("/signup")
	public String signupPage(Model model) {
		model.addAttribute("user", new SignUpForm());
		return "signup";
	}

	@GetMapping("/login")
	public String loginPage(Model model) {
		model.addAttribute("login", new LogInForm());
		return "login";
	}

	@PostMapping("/login")
	public String loginAccount(@ModelAttribute("login") LogInForm form, Model model) {
		String status = userService.logIn(form);

		if (status.contains("success")) {
			return "redirect:/dashBoard";
		}
		model.addAttribute(AppConstent.ERR_MSG, status);
		return "login";
	}

	@GetMapping("/unlock")
	public String unlockPage(@RequestParam String email, Model model) {
		UnlockForm unlockFormObj = new UnlockForm();
		unlockFormObj.setEmail(email);
		model.addAttribute(AppConstent.UNLOCK, unlockFormObj);
		return AppConstent.UNLOCK;
	}

	@PostMapping("/unlock")
	public String ulockUserAccount(@ModelAttribute("unlock") UnlockForm unlock, Model model) {

		if (unlock.getNewPwd().equals(unlock.getConfirmPwd())) {
			boolean status = userService.unLock(unlock);

			if (status) {
				model.addAttribute(AppConstent.UNLOCK_SUCC);
			} else {
				model.addAttribute(AppConstent.UNLOCK_ERR);
			}
		} else {
			model.addAttribute("errorMsg", "New pwd and Confirm pwd should be same");
		}
		return AppConstent.UNLOCK;
	}

	@GetMapping("/forgotPwd")
	public String forgotPwdPage() {

		return "forgotPwd";
	}

	@PostMapping("/forgot")
	public String forgotPwd(@RequestParam("email") String email, Model model) {
		boolean status = userService.forgotPwd(email);
		if (status) {
			model.addAttribute("successMsg", "Password sent to your email");

		} else {
			model.addAttribute("errorMsg", "Invalid Email");

		}
		return "forgotPwd";
	}

}
