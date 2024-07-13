package com.mycompany.bulk_transfer_application.pojo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Request is the class that represents the request JSON
 */
@Data
@AllArgsConstructor
public class Request {
	
	@NotNull
	@JsonProperty("organization_name")
	private String organizationName;
	
	@NotNull
	@JsonProperty("organization_bic")
	private String organizationBic;
	
	@NotNull
	@JsonProperty("organization_iban")
	private String organizationIban;
	
	@NotNull
	@JsonProperty("credit_transfers")
	private List<Transfer> creditTransfers;

}
