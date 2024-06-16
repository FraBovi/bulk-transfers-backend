package com.mycompany.bulk_transfer_application.pojo;

import java.util.List;

import lombok.Data;

@Data
public class Request {
	
	private String organizationName;
	private String organizationBic;
	private String organizationIban;
	
	private List<Transfer> creditTransfers;

}
