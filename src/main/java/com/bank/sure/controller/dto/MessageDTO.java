package com.bank.sure.controller.dto;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;


@Data
public class MessageDTO {
	
//This class for data transfer - teh data to the client
	
	@NotBlank(message="Please Provide not blank name")
	@NotNull(message="Please provide your name")
	@Size(min=1, max=15, message="Your name '${validatedValue}' must be between {min} and {max} chars long")
	private String name;
	
	@NotBlank(message="Please Provide not blank subject")
	@NotNull(message="Please provide your subject")
	@Size(min=5, max=20, message="Your subject '${validatedValue}' must be between {min} and {max} chars long")
	private String subject;
	
	
	@NotBlank(message="Please Provide not blank body")
	@NotNull(message="Please provide your message body")
	@Size(min=20, max=200, message="Your body '${validatedValue}' must be between {min} and {max} chars long")
	private String body;
	
	
	@Email
	private String email;
	
	
	//555-555-5555
	//(555) 555 5555
	//555.555.5555
	@Pattern(regexp = "^((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$",
     message = "Please provide valid phone number")
	@Column(length=14,nullable = false)
	private String phoneNumber;
	

}
