package com.bank.sure.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bank.sure.controller.request.RecipientRequest;
import com.bank.sure.domain.Account;
import com.bank.sure.domain.Recipient;
import com.bank.sure.domain.User;
import com.bank.sure.exception.ConflictException;
import com.bank.sure.exception.ResourceNotFoundException;
import com.bank.sure.exception.message.ExceptionMessage;
import com.bank.sure.repository.RecipientRepository;

@Service
public class RecipientService {
	
	@Autowired
	private RecipientRepository recipientRepository;
	
	@Autowired
	AccountService accountService;
	
	public void addRecipient(RecipientRequest recipientRequest,User user) {
			Account account=accountService.findByAccountNumber(recipientRequest.getAccountNumber()); //With this code we check if the recipient exist or not
			
			if(user.getId().equals(account.getUser().getId())) {
				throw new ConflictException(ExceptionMessage.RECIPIENT_ADD_ERROR_MESSAGE);
			}
			
			validateRecipient(recipientRequest,account);
			
			Recipient recipient=new Recipient();
			recipient.setUser(user);
			recipient.setAccount(account);
			
			recipientRepository.save(recipient);
	}
	
	private void validateRecipient(RecipientRequest recipientRequest,Account account) {
		String name=account.getUser().getFirstName()+" "+account.getUser().getLastName();
		
		if(!name.equalsIgnoreCase(recipientRequest.getName())) {
			throw new ResourceNotFoundException(ExceptionMessage.RECIPIENT_VALIDATION_ERROR_MESSAGE);
		}
	}
}
