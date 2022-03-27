package com.bank.sure.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bank.sure.domain.Transaction;
import com.bank.sure.repository.AccountRepository;
import com.bank.sure.repository.TransactionRepository;

@Service
@Transactional
public class TransactionService {
	
	
	@Autowired
	private TransactionRepository transactionRepository;
	
	
	public void saveTransaction(Transaction transaction) {
		transactionRepository.save(transaction);
	}

}
