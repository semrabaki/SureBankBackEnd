package com.bank.sure.controller;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bank.sure.controller.dto.LoginDTO;
import com.bank.sure.controller.request.RegisterRequest;
import com.bank.sure.controller.response.LoginResponse;
import com.bank.sure.controller.response.Response;
import com.bank.sure.security.JwtUtils;
import com.bank.sure.service.UserService;

@RestController
@RequestMapping
public class UserJWTController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtUtils jwtUtils;
		
	@PostMapping("/register")
	public ResponseEntity<Response> registerUser(@Valid @RequestBody RegisterRequest registerRequest) //this is get the data form client{
	{
		userService.registerUser(registerRequest);
		
		Response response = new Response();
		
		response.setMessage("User Registered Successfully");
		
		response.setSuccess(true);
		
		return new ResponseEntity<>(response,HttpStatus.CREATED);
	}
	
	
	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginDTO loginDTO){
	
		//we are aytehnticatiing the user.if your user name and password correct you will log in or it will throw exception	
	Authentication authentication=	authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getUserName(),loginDTO.getPassword()));
		
	SecurityContextHolder.getContext().setAuthentication(authentication); //we give the authentictaion in security context and we will use it later
	
	String token=jwtUtils.generateToken(authentication); //then we create token with the autentictaion information
	
	LoginResponse response= new LoginResponse(token);//we out the token into the login response and
	
	return ResponseEntity.ok(response);//returned it to the cilent
	
	}
	
	
	

}
