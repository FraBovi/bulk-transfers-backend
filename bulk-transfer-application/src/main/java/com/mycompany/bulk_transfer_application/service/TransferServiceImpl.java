// FIXME: the suffix "impl" looks weird to me. Not sure if Java wants it. The same applies for the class name.
package com.mycompany.bulk_transfer_application.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class TransferServiceImpl implements TransferService{
	
	private static final Logger logger = LoggerFactory.getLogger(TransferServiceImpl.class);
	
	private TransferDAO transferDAO;
	
	@Autowired
	public TransferServiceImpl(TransferDAO transferDAO) {
		this.transferDAO = transferDAO;
	}

	/**
	 * @param id represents id of the BankAccount in DB
	 * @return the BankAccout balance in cents of euros
	 */
	@Override
	// [Q]: is this function used somewhere?
	public Integer getOrganizationBalance(int id) {
		
		logger.info("Getting bank account info from DB with ID {}", id);
		
		BankAccount dbOrganizationAccount = transferDAO.getBankAccountsById(id);
		
		logger.info("Bank Account information retrieved {}", dbOrganizationAccount);
		
		if(dbOrganizationAccount != null) {
			return Integer.parseInt(dbOrganizationAccount.getBalanceCents());
		}
		return null;
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
		BankAccount account = transferDAO.getBankAccountsByBicIban(organizationBic, organizationIban);
		
		logger.info("Bank Account information retrieved {}", account);
		
		List<Transfer> incomingTransfers = request.getCreditTransfers();
		
		// Calculates the total amount need to handle the bulk transfer
		Integer totalAmountTransfer = calculateTotalAmount(incomingTransfers);
		Integer accountBalance = Integer.parseInt(account.getBalanceCents());
		
		logger.info("Balance in cents {} - Total transfer amount in cents{}", accountBalance, totalAmountTransfer);
		
		Response response = new Response();
		
		// If the organization has enough money the bulk transfer is handled
		// otherwise -99 is returned as response
		if(totalAmountTransfer > accountBalance)  {
	
			logger.info("Operation not allowed - CREDIT NOT SUFFICIENT");
			response.setCode(-99);
			response.setDescription("Credit not sufficient");
		
		} else {
			// FIXME: "else" branches are never a good option. If you stumb across a blocker, immediately return. 
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
				
				response.setCode(1);
				response.setDescription("Transfers done successfully");
			
			}
			
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
		Integer total = 0;
		// [x]: maybe there's a better way of sum up the values in a list. Something like LINQ in C#.
		for(Transfer transfer : transfers) {
			total += getCentsOfEuros(transfer.getAmount());
		}
		
		return total;
	}
	
	/**
	 * The function converts a String that represents a value in euros to
	 * an Integer that represents the same value in cents of euros
	 * 
	 * @param euros String value in euros
	 * @return integer value representing cents of euros of the input param
	 */
	// FIXME: this could be moved somewhere else in a "utils" class and file.
	private Integer getCentsOfEuros(String euros) {
		String[] decimalSplit = euros.split("\\.");
		Integer centsOfEuros = Integer.parseInt(decimalSplit[0]) * 100;
		if(decimalSplit.length > 1) {
			String cents = decimalSplit[1];
			// TODO: probably you can convert those values to floating point numbers and multiply by 100.
			// even if it works, let's write it in a cleaner way.
			if(cents.length() == 2) centsOfEuros += Integer.parseInt(cents);
			else {
				centsOfEuros += Integer.parseInt(cents.concat("0"));
			}
		}
		
		return centsOfEuros;
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
	// FIXME: this could be moved to a sort of "mapping" layer to declutter the code here.
	private TransferEntity createTransferEntity(Transfer transfer, BankAccount account) {
		
		TransferEntity transferEntity = new TransferEntity();
		
		transferEntity.setBankAccountId(account);
		transferEntity.setCounterpartyName(transfer.getCounterpartyName());
		transferEntity.setCounterpartyBic(transfer.getCounterpartyBic());
		transferEntity.setDescription(transfer.getDescription());
		transferEntity.setCounterpartyIban(transfer.getCounterpartyIban());
		transferEntity.setAmountCents(getCentsOfEuros(transfer.getAmount()));
		
		return transferEntity;
	}

	@Override
	// [Q]: this could be skipped by invoking directly the DAO?
	public BankAccount getBankAccountByBicIban(String bic, String iban) {
		
		return transferDAO.getBankAccountsByBicIban(bic, iban);
		
	}
}
