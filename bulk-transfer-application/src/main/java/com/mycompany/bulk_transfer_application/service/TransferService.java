package com.mycompany.bulk_transfer_application.service;

import com.mycompany.bulk_transfer_application.entity.BankAccount;
import com.mycompany.bulk_transfer_application.pojo.Request;
import com.mycompany.bulk_transfer_application.pojo.Response;

public interface TransferService {

    public Response insertTransfers(Request request);
    public BankAccount findBankAccountByBicIban(String bic, String iban);
    
}
