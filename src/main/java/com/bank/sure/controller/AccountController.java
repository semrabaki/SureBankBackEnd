package com.bank.sure.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bank.sure.controller.dto.RecipientDTO;
import com.bank.sure.controller.request.RecipientRequest;
import com.bank.sure.controller.request.TransactionRequest;
import com.bank.sure.controller.response.RecipientListResponse;
import com.bank.sure.controller.response.Response;
import com.bank.sure.domain.Recipient;
import com.bank.sure.domain.User;
import com.bank.sure.security.service.UserDetailsImpl;
import com.bank.sure.service.AccountService;
import com.bank.sure.service.RecipientService;
import com.bank.sure.service.UserService;

@RestController
@RequestMapping("/account")
public class AccountController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private RecipientService recipientService;
	
	@Autowired
	private AccountService accountService;
	
	
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
	
	@GetMapping("/recipient")
	@PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
	public ResponseEntity<RecipientListResponse> getRecipient(){
		//i need to get the current user`s recipients
		UserDetailsImpl userDetails=(UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal(); //getting currrent user
		User user=userService.findById(userDetails.getId()); //current userin id si ile usera ulastm
		List<Recipient> recipients = user.getRecepients();
		
		List<RecipientDTO> recipientDTOList = recipients.stream().map(this::convertoDTO).collect(Collectors.toList()); //converting recipents into recipientsDTO object
		
		RecipientListResponse response=new RecipientListResponse(recipientDTOList);
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	
	@DeleteMapping("/recipient/{id}")
	@PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
	public ResponseEntity<Response>deleteRecipient(@PathVariable Long id){
		UserDetailsImpl userDetails=(UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal(); //getting currrent user 
		User user=userService.findById(userDetails.getId());
		recipientService.removeRecipient(user, id);
		
		Response response =new Response();
		response.setMessage("Recipient deleted successfully");
		response.setSuccess(true);
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/deposit")
	@PreAuthorize("hasRole('CUSTOMER')")
	
	public ResponseEntity<Response> deposit(@Valid @RequestBody TransactionRequest transactionRequest){
		UserDetailsImpl userDetails=(UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User user=userService.findById(userDetails.getId());
		accountService.deposit(transactionRequest, user);
		
		Response response=new Response();  //response uretiyoruz client icin
		
		response.setMessage("Amount successfully deposited");
		response.setSuccess(true);
		
		return new ResponseEntity<>(response,HttpStatus.CREATED);
	}
	
	
	@PostMapping("/withdraw")
	@PreAuthorize("hasRole('CUSTOMER')")
	
	public ResponseEntity<Response> withdraw(@Valid @RequestBody TransactionRequest transactionRequest){
		UserDetailsImpl userDetails=(UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User user=userService.findById(userDetails.getId());
		accountService.withdraw(transactionRequest, user);
		
		Response response=new Response();
		
		response.setMessage("Amount successfully withdraw");
		response.setSuccess(true);
		
		return new ResponseEntity<>(response,HttpStatus.CREATED);
	}
	
	private RecipientDTO convertoDTO(Recipient recipient) {
		RecipientDTO recipientDTO=new RecipientDTO();
		User user=recipient.getAccount().getUser();
		recipientDTO.setId(recipient.getId());
		recipientDTO.setFirstName(user.getFirstName());
		recipientDTO.setLastName(user.getLastName());
		recipientDTO.setPhoneNumber(user.getPhoneNumber());
		recipientDTO.setEmail(user.getEmail());
		recipientDTO.setAccountNumber(recipient.getAccount().getAccountNumber());
		return recipientDTO;
	}
	
	
	
	

}
