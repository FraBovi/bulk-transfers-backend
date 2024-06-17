package com.mycompany.bulk_transfer_application.pojo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Request {
	
	@JsonProperty("organization_name")
	private String organizationName;
	
	@JsonProperty("organization_bic")
	private String organizationBic;
	
	@JsonProperty("organization_iban")
	private String organizationIban;
	
	@JsonProperty("credit_transfers")
	private List<Transfer> creditTransfers;

}
