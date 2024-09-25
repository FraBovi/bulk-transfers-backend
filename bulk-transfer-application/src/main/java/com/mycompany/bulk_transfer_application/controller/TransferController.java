package com.mycompany.bulk_transfer_application.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.bulk_transfer_application.dao.TransferDAO;
import com.mycompany.bulk_transfer_application.dto.Request;
import com.mycompany.bulk_transfer_application.dto.SearchParameters;
import com.mycompany.bulk_transfer_application.entity.BankAccount;
import com.mycompany.bulk_transfer_application.entity.TransferEntity;
import com.mycompany.bulk_transfer_application.exception.AmountFormatException;
import com.mycompany.bulk_transfer_application.exception.CreditNotSufficientException;
import com.mycompany.bulk_transfer_application.service.TransferService;

import jakarta.validation.Valid;

/**
 * TransferController handles all the request that have /api as prefix
 */
@RestController
@RequestMapping("/api")
public class TransferController {

  private static final Logger logger = LoggerFactory.getLogger(TransferController.class);

  private TransferService transferService;

  private TransferDAO transferDAO;

  @Autowired
  public TransferController(TransferService transferService, TransferDAO transferDAO) {
    this.transferService = transferService;
    this.transferDAO = transferDAO;
  }

  /**
   * function that handle a POST for bulk transfers
   * 
   * @param transferRequest represents the body of the request
   * @return an entity with code 201 or 422 if the request can be processed
   * @throws CreditNotSufficientException
   */
  @PostMapping(path = "/customer/transfers", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> bulkTransfer(@Valid @RequestBody Request transferRequest)
      throws CreditNotSufficientException, NumberFormatException {

    logger.info("Request for /customer/transfers arrived, with body {}", transferRequest);

    // call a service to handle the request
    try {

      List<TransferEntity> response = transferService.insertTransfers(transferRequest);
      logger.info("Sending response {}", response);
      return ResponseEntity.status(HttpStatus.CREATED).body(response);

    } catch (NumberFormatException e) {
      throw new AmountFormatException();
    }

  }

  // FIXME: implement validations on the query params (e.g. "iban" must be a valid
  // IBAN, "name" must not be empty if set. optional: "bic" validation). If errors
  // happen, return 400 BadRequest error.
  /*
   * {
   * "code": "INTERNAL_SERVER_ERROR",
   * "message": "No static resource api/accounts."
   * }
   */
  @GetMapping(path = "/accounts", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> searchAccount(@Valid @ModelAttribute SearchParameters params) {

    logger.info("Request for /accounts arrived, with params IBAN {} - BIC {} - NAME {}", params.getIban(),
        params.getBic(), params.getName());

    // call a service to handle the request
    List<BankAccount> accounts = transferDAO.searchBankAccounts(params);

    return ResponseEntity.status(HttpStatus.OK).body(accounts);
  }

}
