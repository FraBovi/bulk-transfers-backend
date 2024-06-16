package com.mycompany.bulk_transfer_application.service;

import org.springframework.stereotype.Service;

import com.mycompany.bulk_transfer_application.entity.BankAccounts;
import com.mycompany.bulk_transfer_application.entity.Transfers;

@Service
public class TransferServiceImpl implements TransferService{

	@Override
	public BankAccounts getBankAccountsById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int addTransfer(Transfers transfer) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public BankAccounts updateBankAccount(BankAccounts account) {
		// TODO Auto-generated method stub
		return null;
	}

}
