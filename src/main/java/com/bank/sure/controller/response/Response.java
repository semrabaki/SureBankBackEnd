package com.bank.sure.controller.response;

import lombok.Data;

@Data  //This annotation contains @Getter @Setter @AllArgsConstrutir
public class Response {
	
	boolean isSuccess;
	
	String message;
	

}
