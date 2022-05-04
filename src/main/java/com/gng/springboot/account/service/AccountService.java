package com.gng.springboot.account.service;

import com.gng.springboot.account.model.AccountLoginDto;
import com.gng.springboot.account.model.AccountRegisterDto;

public interface AccountService {
	public String accountRegister(AccountRegisterDto accountRegisterDto);
	public boolean checkAccountExist(String id, String currentPwd);
	public boolean accountConfirm(String id);
	public AccountLoginDto accountLogin(AccountLoginDto accountLoginDto);
}
