package com.bank.sure.controller;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.bank.sure.controller.request.TransactionRequest;
import com.bank.sure.controller.response.Response;
import com.bank.sure.domain.User;
import com.bank.sure.security.service.UserDetailsImpl;
import com.bank.sure.service.AccountService;
import com.bank.sure.service.UserService;

@ExtendWith(MockitoExtension.class)
public class AccountControllerTest {

	@Mock
	AccountService mockAccountService;
	
	
	@Mock
	UserService mockUserService;
	
	
	@InjectMocks
	AccountController underTest;
	
	
	
	@Test
	void create_shouldReturnResponseWhenDeposit() {
		
		TransactionRequest tr=new TransactionRequest();
		tr.setAmount(600.0);
		tr.setComment("It is deposit");
		
		
		User user =new User();
		user.setId(1L);
		
		
		UserDetailsImpl currentUser=Mockito.mock(UserDetailsImpl.class);
		currentUser.setId(1L);
		
		
		Authentication authentication=mock(Authentication.class);
		SecurityContext securityContext=mock(SecurityContext.class);
		
		when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		
		when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(currentUser);
		
		
		
		when(mockUserService.findById(currentUser.getId())).thenReturn(user);
		
		
		doNothing().when(mockAccountService).deposit(tr, user);
		
		ResponseEntity<Response> depositResponse = underTest.deposit(tr);
		Response actual=depositResponse.getBody();
		
		
		assertAll(
				  ()->assertNotNull(actual),
				  ()->assertEquals(HttpStatus.CREATED, depositResponse.getStatusCode()),
				  ()->assertEquals(true,actual.isSuccess())
				);
		
	}
	

}