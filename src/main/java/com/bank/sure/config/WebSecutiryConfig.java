package com.bank.sure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import lombok.AllArgsConstructor;


@Configuration
@AllArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity
public class WebSecutiryConfig extends WebSecurityConfigurerAdapter {


    @Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {//it is used for creating user details
		
		super.configure(auth);
	}


	@Override
	protected void configure(HttpSecurity http) throws Exception {  //We create permission for the operations
		super.configure(http);
	}
	
	

	@Override
	public void configure(WebSecurity web) throws Exception {//For static resources
		
		
		super.configure(web);
	}
	

}
