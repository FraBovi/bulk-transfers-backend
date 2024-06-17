package com.mycompany.bulk_transfer_application.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mycompany.bulk_transfer_application.dao.TransferDAO;
import com.mycompany.bulk_transfer_application.entity.BankAccount;
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
	
	private Integer calculateTotalAmount(List<Transfer> transfers) {
		Integer total = 0;
		for(Transfer transfer : transfers) {
			String[] decimalSplit = transfer.getAmount().split("\\.");
			Integer centsOfEuros = Integer.parseInt(decimalSplit[0]) * 100;
			if(decimalSplit.length > 1) {
				centsOfEuros += Integer.parseInt(decimalSplit[1]);
			}
			total += centsOfEuros;
		}
		
		return total;
	}
	
	@Override
	public Response insertTransfers(Request request) {
		
		String organizationBic = request.getOrganizationBic();
		String organizationIban = request.getOrganizationIban();
		
		BankAccount account = transferDAO.getBankAccountsByBicIban(organizationBic, organizationIban);
		
		Integer totalAmountTransfer = calculateTotalAmount(request.getCreditTransfers());
		Integer accountBalance = Integer.parseInt(account.getBalanceCents());
		
		Response response = new Response();
		
		if(totalAmountTransfer > accountBalance)  {
			response.setCode(-99);
			response.setDescription("Credit not sufficient");
		} else {
			response.setCode(1);
			response.setDescription("Operation successful");
		}
		
		return response;
		
	}

}
