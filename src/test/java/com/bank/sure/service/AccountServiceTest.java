package com.bank.sure.service;


import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bank.sure.domain.Account;
import com.bank.sure.repository.AccountRepository;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
	
	@Mock
	AccountRepository mockAccountRespository;
	
	@InjectMocks
	AccountService underTest;

	
	@Test
	void create_shouldCreateSuccessfuly() {
		
		Account expected=new Account();
		expected.setAccountBalance(BigDecimal.valueOf(500.0));
		expected.setAccountNumber(234334333433433L);
		expected.setId(1L);
		
		
		when(mockAccountRespository.save(any())).thenReturn(expected);
		
		Optional<Account> optExpected = Optional.of(expected);
		
		
		when(mockAccountRespository.findByAccountNumber(any())).thenReturn(optExpected);
		
		Account actual=underTest.createAccount(any());
		
		assertAll(
				  ()->assertNotNull(actual),
				  ()->assertEquals(expected.getId(), actual.getId())
				);
		
		
		
	}
	
}