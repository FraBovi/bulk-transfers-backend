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

@Service
public class TransferServiceImpl implements TransferService{
	
	private static final Logger logger = LoggerFactory.getLogger(TransferServiceImpl.class);
	
	private TransferDAO transferDAO;
	
	@Autowired
	public TransferServiceImpl(TransferDAO transferDAO) {
		this.transferDAO = transferDAO;
	}

	@Override
	public Integer getOrganizationBalance(int id) {
		
		logger.info("Getting bank account info from DB with ID {}", id);
		
		BankAccount dbOrganizationAccount = transferDAO.getBankAccountsById(id);
		
		logger.info("Bank Account information retrieved {}", dbOrganizationAccount);
		
		if(dbOrganizationAccount != null) {
			return Integer.parseInt(dbOrganizationAccount.getBalanceCents());
		}
		return null;
	}
	
	
	@Override
	public Response insertTransfers(Request request) {
		
		String organizationBic = request.getOrganizationBic();
		String organizationIban = request.getOrganizationIban();
		
		logger.info("Getting bank account info from DB using BIC {} and IBAN {}", organizationBic, organizationIban);
		
		BankAccount account = transferDAO.getBankAccountsByBicIban(organizationBic, organizationIban);
		
		logger.info("Bank Account information retrieved {}", account);
		
		List<Transfer> incomingTransfers = request.getCreditTransfers();
		
		Integer totalAmountTransfer = calculateTotalAmount(incomingTransfers);
		Integer accountBalance = Integer.parseInt(account.getBalanceCents());
		
		logger.info("Balance in cents {} - Total transfer amount in cents{}", accountBalance, totalAmountTransfer);
		
		Response response = new Response();
		
		if(totalAmountTransfer > accountBalance)  {
	
			logger.info("Operation not allowed - CREDIT NOT SUFFICIENT");
			response.setCode(-99);
			response.setDescription("Credit not sufficient");
		
		} else {
			
			logger.info("Operation allowed");
			
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

	private Integer calculateTotalAmount(List<Transfer> transfers) {
		Integer total = 0;
		for(Transfer transfer : transfers) {
			total += getCentsOfEuros(transfer.getAmount());
		}
		
		return total;
	}
	
	private Integer getCentsOfEuros(String euros) {
		String[] decimalSplit = euros.split("\\.");
		Integer centsOfEuros = Integer.parseInt(decimalSplit[0]) * 100;
		if(decimalSplit.length > 1) {
			String cents = decimalSplit[1];
			if(cents.length() == 2) centsOfEuros += Integer.parseInt(cents);
			else {
				centsOfEuros += Integer.parseInt(cents.concat("0"));
			}
		}
		
		return centsOfEuros;
	}
	
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
}
