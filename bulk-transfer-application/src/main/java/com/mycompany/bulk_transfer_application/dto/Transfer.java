package com.mycompany.bulk_transfer_application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mycompany.bulk_transfer_application.BulkUtils;
import com.mycompany.bulk_transfer_application.entity.BankAccount;
import com.mycompany.bulk_transfer_application.entity.TransferEntity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Transfer represent a single transfer in the request 'credit_transfers'
 */
@Data
@AllArgsConstructor
public class Transfer {
	
	@JsonProperty("amount")
	private String amount;
	
	@JsonProperty("counterparty_name")
	private String counterpartyName;
	
	@JsonProperty("counterparty_bic")
	private String counterpartyBic;
	
	@JsonProperty("counterparty_iban")
	private String counterpartyIban;
	
	@JsonProperty("description")
	private String description;

	public TransferEntity toTransferEntity(BankAccount account) {
		return new TransferEntity()
			.setBankAccountId(account)
			.setCounterpartyName(counterpartyName)
			.setCounterpartyBic(counterpartyBic)
			.setDescription(description)
			.setCounterpartyIban(counterpartyIban)
			.setAmountCents(BulkUtils.getCentsOfEuros(amount));
	}

}
