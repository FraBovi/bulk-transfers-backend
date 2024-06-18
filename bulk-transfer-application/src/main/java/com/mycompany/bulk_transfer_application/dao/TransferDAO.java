package com.mycompany.bulk_transfer_application.dao;

import com.mycompany.bulk_transfer_application.entity.BankAccount;
import com.mycompany.bulk_transfer_application.entity.TransferEntity;

public interface TransferDAO {
	
	public BankAccount getBankAccountsById(int id);
	public BankAccount getBankAccountsByBicIban(String orgBic, String orgIban);
	public BankAccount updateBankAccount(BankAccount account);
	public void insertTransfers(TransferEntity transfer);

}
