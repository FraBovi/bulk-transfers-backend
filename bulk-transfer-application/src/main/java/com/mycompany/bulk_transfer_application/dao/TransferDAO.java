package com.mycompany.bulk_transfer_application.dao;

import com.mycompany.bulk_transfer_application.entity.BankAccounts;
import com.mycompany.bulk_transfer_application.entity.Transfers;

public interface TransferDAO {
	
	public BankAccounts getBankAccountsById(int id);
	public BankAccounts updateBankAccount(BankAccounts account);
	
	public int addTransfer(Transfers transfer);

}