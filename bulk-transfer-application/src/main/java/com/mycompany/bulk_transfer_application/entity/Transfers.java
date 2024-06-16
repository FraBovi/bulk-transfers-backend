package com.mycompany.bulk_transfer_application.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "transfers")
@Data
public class Transfers {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;
	
	@Column(name = "counterparty_name")
	private String counterpartyName;
	
	@Column(name = "counterparty_iban")
	private String counterpartyIban;
	
	@Column(name = "counterparty_bic")
	private String counterpartyBic;
	
	@ManyToOne
	@JoinColumn(name = "bank_account_id")
    private BankAccounts bankAccountId;
	
	@Column(name = "description")
	private String description;

}
