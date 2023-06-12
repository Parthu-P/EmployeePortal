package com.parthu.service;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.parthu.binding.LogInForm;
import com.parthu.binding.SignUpForm;
import com.parthu.binding.UnlockForm;
import com.parthu.entity.UserDtlsEntity;
import com.parthu.repo.UserDtlsRepo;
import com.parthu.utils.EmailUtil;
import com.parthu.utils.PwdUtil;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private EmailUtil emailUtil;

	@Autowired
	private UserDtlsRepo userRepo;
	
	
	@Autowired
	private HttpSession session;

	@Override
	public boolean signUp(SignUpForm signUp) {
		UserDtlsEntity user = userRepo.findByEmail(signUp.getEmail());

		if (user != null) {
			return false;
		}

		// copy data from binding obj to entity obj
		UserDtlsEntity entity = new UserDtlsEntity();
		BeanUtils.copyProperties(signUp, entity);

		// generate the ramdom pwd and set to obj
		String tempPwd = PwdUtil.generatedRamdomPwd();
		entity.setPwd(tempPwd);

		// set acc status as locked
		entity.setAccountStatus("Locked");

		// insert record
		userRepo.save(entity);

		// send mail to unlock the account
		String to = signUp.getEmail();
		String subject = "Unlock your account";
		StringBuilder body = new StringBuilder();
		body.append("<h1>use below temporary password to unlock your account</h1> ");
		body.append("Temporary Pwd: " + tempPwd);
		body.append("<br/>");
		body.append("<a href=\"http://localhost:8080/unlock?email=" + to + "\">click here to unlock your account");
		emailUtil.sendEmail(to, subject, body.toString());

		return true;
	}

	@Override
	public boolean unLock(UnlockForm unLock) {
		UserDtlsEntity entity = userRepo.findByEmail(unLock.getEmail());

		if (entity.getPwd().equals(unLock.getTempPwd())) {

			entity.setPwd(unLock.getNewPwd());
			entity.setAccountStatus("Unlocked");
			userRepo.save(entity);
			return true;
		}

		return false;
	}

	@Override

	public String logIn(LogInForm logIn) {
		UserDtlsEntity entity = userRepo.findByEmailAndPwd(logIn.getEmail(), logIn.getPwd());

		if (entity == null) {
			return "Invalid Credentials";
		}
		if (entity.getAccountStatus().equals("Locked")) {
			return "Your account locked";
		}
		session.setAttribute("userId", entity.getUserId());

		return "success";
	}

	@Override
	public boolean forgotPwd(String email) {
		
		// check record presence in db with given email
		UserDtlsEntity entity = userRepo.findByEmail(email);
		
		// if record not available send to false
		if (entity == null) {
			return false;
		}

		// if record available send pwd to email and send true
		String subject = "Recover Password";
		String body = "Your Pwd :: " + entity.getPwd();

		emailUtil.sendEmail(email, subject, body);

		return true;
	}

}
