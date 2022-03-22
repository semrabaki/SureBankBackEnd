package com.bank.sure.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bank.sure.controller.dto.UserDTO;
import com.bank.sure.controller.response.UserInfoResponse;
import com.bank.sure.domain.User;
import com.bank.sure.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
	
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	private UserDTO convertToDTO(User user) {
		UserDTO userDTO=modelMapper.map(user, UserDTO.class);
		return userDTO;
	}
	
	@GetMapping("/userInfo")
	@PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")  //if user has a role it is accessable.
	public ResponseEntity<UserInfoResponse> getUserInfo(){
		
		
		User user=userService.findOneWithAuthoritiesByUserName();
		
		UserDTO userDTO=convertToDTO(user);
		UserInfoResponse response=new UserInfoResponse(userDTO);
		
		return ResponseEntity.ok(response);
		
		
	}
	
	
	   

}
