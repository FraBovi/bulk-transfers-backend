package com.mycompany.bulk_transfer_application.service;

import java.util.List;

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
	
	private TransferDAO transferDAO;
	
	@Autowired
	public TransferServiceImpl(TransferDAO transferDAO) {
		this.transferDAO = transferDAO;
	}

	@Override
	public Integer getOrganizationBalance(int id) {
		
		BankAccount dbOrganizationAccount = transferDAO.getBankAccountsById(id);
		
		if(dbOrganizationAccount != null) {
			return Integer.parseInt(dbOrganizationAccount.getBalanceCents());
		}
		return null;
	}
	
	
	@Override
	public Response insertTransfers(Request request) {
		
		String organizationBic = request.getOrganizationBic();
		String organizationIban = request.getOrganizationIban();
		
		BankAccount account = transferDAO.getBankAccountsByBicIban(organizationBic, organizationIban);
		
		List<Transfer> incomingTransfers = request.getCreditTransfers();
		
		Integer totalAmountTransfer = calculateTotalAmount(incomingTransfers);
		Integer accountBalance = Integer.parseInt(account.getBalanceCents());
		
		Response response = new Response();
		
		if(totalAmountTransfer > accountBalance)  {
			response.setCode(-99);
			response.setDescription("Credit not sufficient");
		} else {
			
			for(Transfer transfer : incomingTransfers) {
				
				TransferEntity transferEntity = createTransferEntity(transfer, account);
				transferDAO.insertTransfers(transferEntity);
				
				accountBalance -= transferEntity.getAmountCents();
				account.setBalanceCents(accountBalance.toString());
				
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
