package com.bank.sure.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bank.sure.controller.request.TransactionRequest;
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
		
	//Account account= getAccount(user);
		
		Account account= accountRepository.findByUser(user).orElseThrow(()-> new ResourceNotFoundException
				(String.format(ExceptionMessage.USERACCOUNT_NOT_FOUND_MESSAGE, user.getId())));
		
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
		Account account= accountRepository.findByUser(user).orElseThrow(()-> new ResourceNotFoundException
				(String.format(ExceptionMessage.USERACCOUNT_NOT_FOUND_MESSAGE, user.getId())));
		
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
	
	
	public Account findByAccountNumber(Long number) {
		return accountRepository.findByAccountNumber(number)
				.orElseThrow(()->new ResourceNotFoundException(String.format(ExceptionMessage.ACCOUNT_NOT_FOUND_MESSAGE,number)));
	}



}
