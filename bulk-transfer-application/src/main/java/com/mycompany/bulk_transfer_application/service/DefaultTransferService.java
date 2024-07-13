package com.mycompany.bulk_transfer_application.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mycompany.bulk_transfer_application.BulkUtils;
import com.mycompany.bulk_transfer_application.dao.TransferDAO;
import com.mycompany.bulk_transfer_application.entity.BankAccount;
import com.mycompany.bulk_transfer_application.entity.TransferEntity;
import com.mycompany.bulk_transfer_application.pojo.Request;
import com.mycompany.bulk_transfer_application.pojo.Response;
import com.mycompany.bulk_transfer_application.pojo.Transfer;

/**
 * This is the service where all business logic is applied and
 * it uses the TransferDAO class that interacts with the DB for CRUD operations 
 */
@Service
// TODO: why "Default" prefix? Only to differentiate it from the interface name? Why not prepending the latter with the prefix "I" or leave it as it is if the compiler doesn't complain.
// My fist idea was to use the prefix "I" as you suggest, but after reading this https://stackoverflow.com/questions/2814805/java-interfaces-implementation-naming-convention/2815440 I change my mind. 
public class DefaultTransferService implements TransferService {
	
	private static final Logger logger = LoggerFactory.getLogger(DefaultTransferService.class);
	
	private TransferDAO transferDAO;
	
	@Autowired
	public DefaultTransferService(TransferDAO transferDAO) {
		this.transferDAO = transferDAO;
	}
	
	/**
	 * This function implements business logic in which at first the BankAccount info
	 * are retrieved from DB, using IBAN e BIC, then it checks if it's possible to handle
	 * the request and in that case updates the DB.
	 * 
	 * @param request represents the request JSON 
	 * @return Response which can be with code 1 for successful and -99 otherwise
	 */
	@Override
	public Response insertTransfers(Request request) {
		
		String organizationBic = request.getOrganizationBic();
		String organizationIban = request.getOrganizationIban();
		
		logger.info("Getting bank account info from DB using BIC {} and IBAN {}", organizationBic, organizationIban);
		
		// Getting BankAccount info from DB using BIC and IBAN
		BankAccount account = transferDAO.getBankAccountByBicAndIban(organizationBic, organizationIban).get(0);
		
		logger.info("Bank Account information retrieved {}", account);
		
		List<Transfer> incomingTransfers = request.getCreditTransfers();
		
		// Calculates the total amount need to handle the bulk transfer
		Integer totalAmountTransfer = calculateTotalAmount(incomingTransfers);
		Integer accountBalance = Integer.parseInt(account.getBalanceCents());
		
		logger.info("Balance in cents {} - Total transfer amount in cents{}", accountBalance, totalAmountTransfer);
		
		Response response = new Response();
		
		// If the organization has not enough money the bulk transfer is not allowed
		// -99 is returned as response
		if(totalAmountTransfer > accountBalance)  {
	
			logger.info("Operation not allowed - CREDIT NOT SUFFICIENT");
			response.setCode(422);
			response.setDescription("Credit not sufficient");

			return response;
		
		}

		logger.info("Operation allowed");
		
		// For each transfer in the bulk we add it to DB and update organization balance
		for(Transfer transfer : incomingTransfers) {
			
			logger.info("Insert transfer {} in DB", transfer);
			
			TransferEntity transferEntity = createTransferEntity(transfer, account);
			transferDAO.insertTransfers(transferEntity);
			
			accountBalance -= transferEntity.getAmountCents();
			account.setBalanceCents(accountBalance.toString());
			
			logger.info("Update bank account {} in DB", account);
			
			transferDAO.updateBankAccount(account);
			
			response.setCode(200);
			response.setDescription("Transfers done successfully");
		
		}
			
		
		return response;
		
	}

	/**
	 * Calculates the total amount need to process the bulk transfer
	 * 
	 * @param transfers a list with all the transfers
	 * @return an integer with the total value of the bulk transfer
	 */
	private Integer calculateTotalAmount(List<Transfer> transfers) {
		return transfers.stream().mapToInt(transfer -> BulkUtils.getCentsOfEuros(transfer.getAmount())).sum();
	}
	
	/**
	 * Insert transfer in DB by combining the info of the request @param transfer
	 * and the @param account ID
	 *  
	 * @param transfer one of the transfer received in the bulk request
	 * @param account the account performing the request
	 * 
	 * @return the TransferEntity added in the DB table
	 */
	private TransferEntity createTransferEntity(Transfer transfer, BankAccount account) {
		
		TransferEntity transferEntity = new TransferEntity();
		
		transferEntity.setBankAccountId(account);
		transferEntity.setCounterpartyName(transfer.getCounterpartyName());
		transferEntity.setCounterpartyBic(transfer.getCounterpartyBic());
		transferEntity.setDescription(transfer.getDescription());
		transferEntity.setCounterpartyIban(transfer.getCounterpartyIban());
		transferEntity.setAmountCents(BulkUtils.getCentsOfEuros(transfer.getAmount()));
		
		return transferEntity;
	}

	@Override
	// TODO: this could be skipped by invoking directly the DAO?
	// Yes , idk if calling the DAO directly by the Controller would be better than this boilerplate code
	// however this can be removed if you think.
	public List<BankAccount> findBankAccountByBicIban(String bic, String iban) {
		
		if(iban != null && bic != null) return transferDAO.getBankAccountByBicAndIban(bic, iban);
		if(iban != null) return transferDAO.searchBankAccountsByIban(iban);
		if(bic != null) return transferDAO.searchBankAccountsByBic(bic);

		return transferDAO.findAllBankAccounts();
		
	}
}
