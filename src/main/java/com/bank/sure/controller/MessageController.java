package com.bank.sure.controller;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bank.sure.controller.dto.MessageDTO;
import com.bank.sure.controller.response.Response;
import com.bank.sure.domain.Message;
import com.bank.sure.service.MessageService;

@RestController
@RequestMapping("/message") //this annotation for matching the URL
public class MessageController {
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	
	
	//If you do not want to use Autowired annotation you can use this type of constructor OR if you do not want to use autowired or constructor you can use @AllArgsConstrutor@Lomlock
	//annotation on the class these three all the same (creates the object
//	public MessageController(MessageService messageService, ModelMapper modelMapper) {
//		
//		this.messageService = messageService;
//		this.modelMapper = modelMapper;
//	}

	//this needed for recording the unusual situation in the program we can write on the file or console(console only for development time info)
	//we do not unnessary things. Log level(trace-> debug-> info-> warn-> error)
	private static Logger logger=LoggerFactory.getLogger(MessageController.class);
	
	private Message convertTo(MessageDTO messageDTO) {
		Message message=modelMapper.map(messageDTO, Message.class);
		return message;
	}
	
	private MessageDTO converttoDTO(Message message)
	{
		MessageDTO messageDTO=modelMapper.map(message,MessageDTO.class);
		return messageDTO;
	}
	//@Valid annotation checks the validation condiitons which are created in the entity class with different annotations such as @Size,@NotNull
	
	
	@PostMapping("/visitor")
	public ResponseEntity<Response>createMessage(@Valid @RequestBody MessageDTO messageDTO)
	{
		Message message=convertTo(messageDTO);
		messageService.createMessage(message);
		
		Response response= new Response();
		
		response.setMessage("Message saved successfully");
		response.setSuccess(true);
		
		//Static
		//return ResponseEntity.ok(response); //this is returning the success message and sttaus to the client-if we want to return anything to client we shoudl use response entity
		return  new ResponseEntity<>(response, HttpStatus.CREATED);// this is how you create a new response entity for post
	}
	
	@GetMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<MessageDTO>>getAll(){
		
		List<Message> allMessage= messageService.getAll();
		
		List<MessageDTO> messageList=allMessage.stream().map(this::converttoDTO).collect(Collectors.toList());
		
		return new ResponseEntity<>(messageList, HttpStatus.OK);  //in the avove method we used the response entity static method in this method we created new response entity and put the http status  there are two usage
	}
	
	//localhost:8080/message/id
		@GetMapping("/{id}")
		@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<MessageDTO> getMessage(@PathVariable Long id){
		
		Message message= messageService.getMessage(id);
		
		MessageDTO messageDTO=converttoDTO(message);
		
		return ResponseEntity.ok(messageDTO);
	}

		@GetMapping("/request")
		@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<MessageDTO> getMessagebyRequest(@RequestParam Long id){
		
		
		Message message= messageService.getMessage(id);
		
		MessageDTO messageDTO=converttoDTO(message);
		
		return ResponseEntity.ok(messageDTO);
	}
	
		@DeleteMapping("/{id}")
		@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Response>deleteMessage(@PathVariable Long id){
		logger.info("Client want to delete message id {}:",id);
		messageService.deleteMessage(id);
		
		Response response= new Response();
		response.setMessage("Message deleted successfully");
		response.setSuccess(true);
		
		return ResponseEntity.ok(response);
		
		
	}
	
	// for update and post we use valid annotation since we use response body
		@PutMapping("/{id}")
		@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Response>updateMessage(@PathVariable Long id, @Valid @RequestBody MessageDTO messageDTO){
		
		Message message=convertTo(messageDTO);
		messageService.updateMessage(id,message);
		
		Response response= new Response();
		response.setMessage("Message updated successfully");
		response.setSuccess(true);
		
		return ResponseEntity.ok(response);
	
		
	}
	
	
	
	
}
