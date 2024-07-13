package com.mycompany.bulk_transfer_application.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.bulk_transfer_application.entity.BankAccount;
import com.mycompany.bulk_transfer_application.exception.BadRequestException;
import com.mycompany.bulk_transfer_application.pojo.Request;
import com.mycompany.bulk_transfer_application.pojo.Response;
import com.mycompany.bulk_transfer_application.service.TransferService;

import jakarta.persistence.NoResultException;
import jakarta.validation.Valid;

/**
 * TransferController handles all the request that have /api as prefix
 */
@RestController
@RequestMapping("/api")
public class TransferController {
	
	private static final Logger logger = LoggerFactory.getLogger(TransferController.class); 
	
	private TransferService transferService;
	
	@Autowired
	public TransferController(TransferService transferService) {
		this.transferService = transferService;
	}
	
	/**
	 * function that handle a POST for bulk transfers
	 * @param transferRequest represents the body of the request
	 * @return an entity with code 201 or 422 if the request can be processed
	 */
	@PostMapping(path = "/customer/transfers", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> bulkTransfer(@Valid @RequestBody Request transferRequest) {
		
		logger.info("Request for /customer/transfers arrived, with body {}", transferRequest);
		
		// call a service to handle the request
		Response response = transferService.insertTransfers(transferRequest);
		
		logger.info("Sending response {}", response);
		
		if(response.getCode() == 1) return ResponseEntity.status(HttpStatus.CREATED).body(response);
		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
	}
	
    // TODO: use also the organizationName to lookup the record (handle it in the lower levels)
	@GetMapping(path = "/accounts", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getBankAccountByBicAndIban(@RequestParam(required = false) String organizationIban, @RequestParam(required = false) String organizationBic) throws BadRequestException {
		
		logger.info("Request for /accounts arrived, with params IBAN {} and BIC {}", organizationIban, organizationBic);
		
		// call a service to handle the request
		List<BankAccount> accounts = transferService.findBankAccountByBicIban(organizationBic, organizationIban);
		if(accounts.isEmpty()) throw new NoResultException();

		return ResponseEntity.status(HttpStatus.OK).body(accounts);
	}

}
