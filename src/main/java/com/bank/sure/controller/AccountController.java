package com.bank.sure.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bank.sure.controller.request.RecipientRequest;
import com.bank.sure.controller.response.Response;
import com.bank.sure.domain.User;
import com.bank.sure.security.service.UserDetailsImpl;
import com.bank.sure.service.RecipientService;
import com.bank.sure.service.UserService;

@RestController
@RequestMapping("/account")
public class AccountController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private RecipientService recipientService;
	
	
	@PostMapping("/recipient")
	@PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
	public ResponseEntity<Response> addRecipient(@Valid @RequestBody RecipientRequest recipientRequest){
		
		 UserDetailsImpl userDetail= (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal(); //I am getting current user with this code.!!!
	     //and  i am casting it to the userDetailsImpl type
		 
		 
		 User user= userService.findById(userDetail.getId());
		 
		 recipientService.addRecipient(recipientRequest, user);
		 
		 Response response= new Response();
		 
		 response.setMessage("Recipient added successfully");
		 response.setSuccess(true);
		 
		 return new ResponseEntity<>(response, HttpStatus.CREATED);
		 
		 
		 
	
	}
	
	

}
