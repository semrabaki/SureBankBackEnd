package com.bank.sure.controller.response;

import java.util.ArrayList;
import java.util.List;

import com.bank.sure.controller.dto.RecipientDTO;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class RecipientListResponse {
	
	public List<RecipientDTO> recipients= new ArrayList<>();
	

}
