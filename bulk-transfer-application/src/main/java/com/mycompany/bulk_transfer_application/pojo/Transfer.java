package com.mycompany.bulk_transfer_application.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

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

}
