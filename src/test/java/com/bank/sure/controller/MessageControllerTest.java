package com.bank.sure.controller;


import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.bank.sure.controller.dto.MessageDTO;
import com.bank.sure.domain.Message;
import com.bank.sure.service.MessageService;

@ExtendWith(MockitoExtension.class)
public class MessageControllerTest {
	
@Mock
MessageService mockMessageService;

@Spy
ModelMapper modelMapper;


@InjectMocks
MessageController underTest;


@Test
void getMessages_shouldReturnMessageDtoList() {
	
	Message message=new Message();
	message.setId(1L);
	message.setBody("It is a body");
	message.setName("Test1");
	message.setPhoneNumber("45-45-45");
	message.setSubject("It is subject");
	message.setEmail("t@email.com");
	
	
	Message message2=new Message();
	message2.setId(1L);
	message2.setBody("It is a body");
	message2.setName("Test1");
	message2.setPhoneNumber("45-45-45");
	message2.setSubject("It is subject");
	message2.setEmail("t@email.com");
	
	
	List<Message> expected=Arrays.asList(message,message2);
	
	
	when(mockMessageService.getAll()).thenReturn(expected);
	
	
	ResponseEntity<List<MessageDTO>> response=underTest.getAll();
	
	List<MessageDTO> actual=response.getBody();
	
	assertAll(
			
			()->assertNotNull(actual),
			()->assertEquals(HttpStatus.OK, response.getStatusCode()),
			()->assertEquals(expected.size(), actual.size())
			);	
	
}
	
	

}