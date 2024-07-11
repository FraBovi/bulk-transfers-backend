package com.mycompany.bulk_transfer_application.dao;

import java.util.List;

import com.mycompany.bulk_transfer_application.entity.BankAccount;
import com.mycompany.bulk_transfer_application.entity.TransferEntity;

public interface TransferDAO {
	
	public BankAccount getBankAccountByBicAndIban(String orgBic, String orgIban);
	public List<BankAccount> searchBankAccountsByBic(String orgBic);
	public List<BankAccount> searchBankAccountsByIban(String orgIban);
	public List<BankAccount> findAllBankAccounts();
	public BankAccount updateBankAccount(BankAccount account);
	public void insertTransfers(TransferEntity transfer);

}
