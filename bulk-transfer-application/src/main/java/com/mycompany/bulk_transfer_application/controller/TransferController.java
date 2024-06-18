package com.mycompany.bulk_transfer_application.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.bulk_transfer_application.pojo.Request;
import com.mycompany.bulk_transfer_application.pojo.Response;
import com.mycompany.bulk_transfer_application.service.TransferService;

@RestController
@RequestMapping("/api")
public class TransferController {
	
	private static final Logger logger = LoggerFactory.getLogger(TransferController.class); 
	
	private TransferService transferService;
	
	@Autowired
	public TransferController(TransferService transferService) {
		this.transferService = transferService;
	}
	
	@PostMapping(path = "/transfer", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> bulkTransfer(@RequestBody Request transferRequest) {
		
		Response response = transferService.insertTransfers(transferRequest);
		
		if(response.getCode() == 1) return ResponseEntity.status(HttpStatus.CREATED).body(response);
		else {
			return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
		}
	}

}
