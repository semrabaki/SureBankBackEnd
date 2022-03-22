package com.bank.sure.security;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class SecurityUtils {
	
	//This is for getting current user name
	public static Optional<String> getCurrentUserLogin(){
		SecurityContext securityContext=SecurityContextHolder.getContext();
		return Optional.ofNullable(extractPrincipal(securityContext.getAuthentication()));
	}
	
	private static String extractPrincipal(Authentication authentication) {
		if(authentication==null) {
			return null;
		}else if(authentication.getPrincipal() instanceof UserDetails) { //with instanceof we check the sub type of useer details
			UserDetails springSecurityUser=(UserDetails)authentication.getPrincipal();
			return springSecurityUser.getUsername();
		}else if(authentication.getPrincipal() instanceof String) {
			return (String) authentication.getPrincipal();
		}
		return null;
	}

}
