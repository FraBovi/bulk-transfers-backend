package com.mycompany.bulk_transfer_application.pojo;

import lombok.Data;

@Data
public class Transfer {
	
	private String amount;
	private String counterpartyName;
	private String counterpartyBic;
	private String counterpartyIban;
	private String description;

}
