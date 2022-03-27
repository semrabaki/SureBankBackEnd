package com.bank.sure.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bank.sure.domain.Account;
import com.bank.sure.domain.Recipient;
import com.bank.sure.domain.User;

@Repository
public interface RecipientRepository extends JpaRepository<Recipient, Long> { 
	

	Boolean existsByUserAndAccount(User user, Account account);
	
	
	//This is how we can create custom method in the repository (how to create custom JPLQuery)
	@Query("SELECT r from Recipient r  where r.user.id=:userId and r.account.id=:accountId") //we need to use the field names in the atrgeted table
	Optional<Recipient> findRecipientByUserAndAccountId(@Param ("userId") Long userId, @Param("accountId") Long accountId);
		
	}

