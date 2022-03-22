package com.bank.sure.exception.message;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

public class ExceptionMessage {
	
	//%d is place holder for long and integer-
	
	public final static String MESSAGE_NOT_FOUND_MESSAGE="Message with id %d not found";
	
	public final static String USER_NOT_FOUND_MESSAGE="User with username %s not found";
	
	public final static String USERNAME_ALREADY_EXIST_MESSAGE="Username %s already exist";
	
	public final static String EMAIL_ALREADY_EXIST_MESSAGE="Email %s already exist";
	
	public final static String SSN_ALREADY_EXIST_MESSAGE="SSN %s already exist";
	
	public final static String ROLE_NOT_EXIST_MESSAGE="Role %s not found";
	
	public final static String ACCOUNT_NOT_FOUND_MESSAGE="Account %d not found";
	
	public final static String CURRENTUSER_NOT_FOUND_MESSAGE="Current User not found";
	
	

	

}
