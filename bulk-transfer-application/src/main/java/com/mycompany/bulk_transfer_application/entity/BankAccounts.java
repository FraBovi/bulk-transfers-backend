package com.mycompany.bulk_transfer_application.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "bank_accounts")
@Data
public class BankAccounts {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;
	
	@Column(name = "organization_name")
	private String organizationName;
	
	@Column(name = "balance_cents")
	private String balanceCents;
	
	@Column(name = "iban")
	private String iban;
	
	@Column(name = "bic")
	private String bic;

}
