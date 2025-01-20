package com.mycompany.bulk_transfer_application.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mycompany.bulk_transfer_application.BulkUtils;
import com.mycompany.bulk_transfer_application.dao.TransferDAO;
import com.mycompany.bulk_transfer_application.dto.Request;
import com.mycompany.bulk_transfer_application.dto.SearchParameters;
import com.mycompany.bulk_transfer_application.dto.Transfer;
import com.mycompany.bulk_transfer_application.entity.BankAccount;
import com.mycompany.bulk_transfer_application.entity.TransferEntity;
import com.mycompany.bulk_transfer_application.exception.CreditNotSufficientException;
import com.mycompany.bulk_transfer_application.exception.NoBankAccountFoundException;

/**
 * This is the service where all business logic is applied and
 * it uses the TransferDAO class that interacts with the DB for CRUD operations
 */
@Service
public class TransferService {

  private static final Logger logger = LoggerFactory.getLogger(TransferService.class);

  private TransferDAO transferDAO;

  @Autowired
  public TransferService(TransferDAO transferDAO) {
    this.transferDAO = transferDAO;
  }

  /**
   * This function implements business logic in which at first the BankAccount
   * info
   * are retrieved from DB, using IBAN e BIC, then it checks if it's possible to
   * handle
   * the request and in that case updates the DB.
   * 
   * @param request represents the request JSON
   * @return Response which can be with code 1 for successful and -99 otherwise
   * @throws CreditNotSufficientException
   */
  public List<TransferEntity> insertTransfers(Request request)
      throws CreditNotSufficientException, NumberFormatException {

    String organizationBic = request.getOrganizationBic();
    String organizationIban = request.getOrganizationIban();

    logger.info("Getting bank account info from DB using BIC {} and IBAN {}", organizationBic, organizationIban);

    SearchParameters searchParameters = new SearchParameters(organizationBic, organizationIban);

    // Getting BankAccount info from DB using BIC and IBAN
    List<BankAccount> accountsFound = transferDAO.searchBankAccounts(searchParameters);

    if (accountsFound.isEmpty())
      throw new NoBankAccountFoundException();

    BankAccount account = accountsFound.get(0);

    logger.info("Bank Account information retrieved {}", account);

    List<Transfer> incomingTransfers = request.getCreditTransfers();

    // Calculates the total amount need to handle the bulk transfer
    Integer totalAmountTransfer = calculateTotalAmount(incomingTransfers);
    Integer accountBalance = Integer.parseInt(account.getBalanceCents());

    logger.info("Balance in cents {} - Total transfer amount in cents{}", accountBalance, totalAmountTransfer);

    // If the organization has not enough money the bulk transfer is not allowed
    // -99 is returned as response
    if (totalAmountTransfer > accountBalance) {

      logger.info("Operation not allowed - CREDIT NOT SUFFICIENT");
      throw new CreditNotSufficientException();

    }

    logger.info("Operation allowed");

    List<TransferEntity> newTransfers = new ArrayList<>();

    // For each transfer in the bulk we add it to DB and update organization balance
    for (Transfer transfer : incomingTransfers) {

      logger.info("Insert transfer {} in DB", transfer);

      TransferEntity transferEntity = createTransferEntity(transfer, account);
      transferDAO.insertTransfers(transferEntity);
      newTransfers.add(transferEntity);

      accountBalance -= transferEntity.getAmountCents();
      account.setBalanceCents(accountBalance.toString());
      transferDAO.updateBankAccount(account);

      logger.info("Update bank account {} in DB", account);

    }

    return newTransfers;

  }

  /**
   * Calculates the total amount need to process the bulk transfer
   * 
   * @param transfers a list with all the transfers
   * @return an integer with the total value of the bulk transfer
   */
  private Integer calculateTotalAmount(List<Transfer> transfers) throws NumberFormatException {
    return transfers.stream().mapToInt(transfer -> BulkUtils.getCentsOfEuros(transfer.getAmount())).sum();
  }

  /**
   * Insert transfer in DB by combining the info of the request @param transfer
   * and the @param account ID
   * 
   * @param transfer one of the transfer received in the bulk request
   * @param account  the account performing the request
   * 
   * @return the TransferEntity added in the DB table
   */
  private TransferEntity createTransferEntity(Transfer transfer, BankAccount account) throws NumberFormatException {
    return transfer.toTransferEntity(account);
  }

}
