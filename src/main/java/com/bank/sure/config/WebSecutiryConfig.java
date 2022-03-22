package com.bank.sure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.bank.sure.security.AuthEntryPointJwt;
import com.bank.sure.security.AuthTokenFilter;
import com.bank.sure.security.service.UserDetailsServiceImpl;

import lombok.AllArgsConstructor;


@Configuration //configuration class oldugu icin bu anatasyon
@AllArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class WebSecutiryConfig extends WebSecurityConfigurerAdapter {
	
	//we ddid not use autowired annotaion here because we used the @AllArgsConstructor which is the an other way to  make dependency injection 
    private UserDetailsServiceImpl userDetailsServiceImpl;
	
	private final AuthEntryPointJwt unauthorizedHandler;
	
	
	@Bean 
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	
	@Bean 
	public AuthTokenFilter authenticationJwtTokenFilter() {
		return new AuthTokenFilter();
	}
	
	
	@Bean
	protected AuthenticationManager authenticationManager() throws Exception{
		return super.authenticationManager();
	}
	
	
	//We set the object userDetailsServiceImpl and passwordEncoder for spring security that will 
	//use for authentication and authorization.
	@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		 	auth.userDetailsService(userDetailsServiceImpl).passwordEncoder(passwordEncoder());
		
		}
	
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//it is (CSRF) cross site request forgery.
		//I disable because this app(API app) open to the public.
		//It may be inconvenient when you are in under development 
		http.csrf().disable()
		.exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
		.sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
		.authorizeRequests().antMatchers("/register","/login")  //we apply autharization
		.permitAll()
		.anyRequest().authenticated();
		
		http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
	}
	
	
	
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		// TODO Auto-generated method stub
		super.configure(web);
	}
	
	

}
