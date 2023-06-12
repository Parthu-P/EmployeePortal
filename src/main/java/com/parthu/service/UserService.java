package com.parthu.service;

import com.parthu.binding.LogInForm;
import com.parthu.binding.SignUpForm;
import com.parthu.binding.UnlockForm;

public interface UserService {
	
	public boolean signUp(SignUpForm signUp);

	public boolean unLock(UnlockForm unLock);

	public String logIn(LogInForm logIn);

	public boolean forgotPwd(String email);
}
