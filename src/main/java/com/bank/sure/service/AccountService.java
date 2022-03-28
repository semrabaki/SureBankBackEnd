package com.bank.sure.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bank.sure.controller.request.TransactionRequest;
import com.bank.sure.controller.request.TransferRequest;
import com.bank.sure.domain.Account;
import com.bank.sure.domain.Transaction;
import com.bank.sure.domain.User;
import com.bank.sure.domain.enumeration.TransactionType;
import com.bank.sure.exception.BalanceNotAvailableException;
import com.bank.sure.exception.ResourceNotFoundException;
import com.bank.sure.exception.message.ExceptionMessage;
import com.bank.sure.repository.AccountRepository;

@Service
@Transactional
public class AccountService {
	
	@Autowired
	AccountRepository accountRepository;
	
	@Autowired
	TransactionService transactionService;
	
	
	public Account createAccount(User user) {
		
		
		//when user registers the account will be created automatically
		Account account=new Account();
		Long accountNumber = getAccountNumber();
		account.setAccountBalance(BigDecimal.valueOf(0.0));
		account.setAccountNumber(accountNumber);
		account.setUser(user);
		
		accountRepository.save(account);
		return accountRepository.findByAccountNumber(accountNumber).
			orElseThrow(()->new ResourceNotFoundException(String.format(ExceptionMessage.ACCOUNT_NOT_FOUND_MESSAGE, accountNumber)));
	}
	
	private Long getAccountNumber() {
		long smallest=1000_0000_0000_0000L;
		long biggest=9999_9999_9999_9999L;
		
		return ThreadLocalRandom.current().nextLong(smallest, biggest);
	}
	
	public void deposit(TransactionRequest request,User user) {
		
	     Account account= getAccount(user);
				
		double amount=request.getAmount();

		account.setAccountBalance(account.getAccountBalance().add(BigDecimal.valueOf(amount))); //we are calculating the new balance with the transfer amount and current balance
		
		
		Transaction transaction= new Transaction(LocalDateTime.now(), //we create a transaction and save it 
				request.getComment(),
				amount,
				account.getAccountBalance(),
				TransactionType.DEPOSIT,
				account);
		
		transactionService.saveTransaction(transaction);   //1.transaction
		accountRepository.save(account); //2.transaction
	}
	
	public void withdraw(TransactionRequest request, User user) {
		 Account account= getAccount(user);
		 
		double amount=request.getAmount();

		
		if(account.getAccountBalance().longValue()<request.getAmount()) {
			throw new BalanceNotAvailableException(ExceptionMessage.BALANCE_NOT_AVAILABLE_MESSAGE);
		}

		
		account.setAccountBalance(account.getAccountBalance().subtract(BigDecimal.valueOf(amount)));
		
		Transaction transaction= new Transaction(LocalDateTime.now(),
				request.getComment(),
				amount,
				account.getAccountBalance(),
				TransactionType.WITHDRAW,
				account);
		
		transactionService.saveTransaction(transaction);
		accountRepository.save(account);
	}
	
	public Account getAccount(User user) { // /we check the account if it is not exist we throw exception
		Account account=accountRepository.findByUser(user).orElseThrow(()->new ResourceNotFoundException
				(String.format(ExceptionMessage.USERACCOUNT_NOT_FOUND_MESSAGE,user.getId())));
		
		return account;
	}
	
	public Account findByAccountNumber(Long number) {
		return accountRepository.findByAccountNumber(number)
				.orElseThrow(()->new ResourceNotFoundException(String.format(ExceptionMessage.ACCOUNT_NOT_FOUND_MESSAGE,number)));
	}

	
	public void transfer(TransferRequest request,User user) {
		
		
		 Account account= getAccount(user);  //we check the account if it is not exist we throw exception
		
		
		if(account.getAccountBalance().longValue()<request.getAmount()) {  //checking the balance
			throw new BalanceNotAvailableException(ExceptionMessage.BALANCE_NOT_AVAILABLE_MESSAGE);
		}
		
		
		Account toAccount = accountRepository.findByAccountNumber(request.getRecipientNumber()).   //checking the recipient account in exist 
							orElseThrow(()->new ResourceNotFoundException(String.format(ExceptionMessage.RECIPIENT_ACCOUNT_NOT_FOUND_MESSAGE, request.getRecipientNumber())));
		
		
		double amount=request.getAmount();  //getting the transfer amount
		
		//it is the sender (setting the account balance)
		account.setAccountBalance(account.getAccountBalance().subtract(BigDecimal.valueOf(amount)));
		
		//it is the receiver
		toAccount.setAccountBalance(toAccount.getAccountBalance().add(BigDecimal.valueOf(amount)));
		
		LocalDateTime now  = LocalDateTime.now();
		
		//sender transaction
		Transaction transactionFrom= new Transaction(now,
				request.getComment()+" Transferred to "+toAccount.getAccountNumber(),
				amount,
				account.getAccountBalance(),
				TransactionType.TRANSFER,
				account);
		
		//receiver transaction
		Transaction transactionTo= new Transaction(now,
				"Transferred from "+account.getAccountNumber(),
				amount,
				toAccount.getAccountBalance(),
				TransactionType.TRANSFER,
				toAccount);
		
		
		transactionService.saveTransaction(transactionFrom);
		transactionService.saveTransaction(transactionTo);
		
		accountRepository.save(account);
		accountRepository.save(toAccount);
		
	}


}
