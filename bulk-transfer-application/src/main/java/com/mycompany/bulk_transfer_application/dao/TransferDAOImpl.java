package com.mycompany.bulk_transfer_application.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mycompany.bulk_transfer_application.controller.TransferController;
import com.mycompany.bulk_transfer_application.entity.BankAccount;
import com.mycompany.bulk_transfer_application.entity.TransferEntity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

@Repository
public class TransferDAOImpl implements TransferDAO {
	
	private static final Logger logger = LoggerFactory.getLogger(TransferDAOImpl.class);
	
	public EntityManager entityManager;
	
	@Autowired
	public TransferDAOImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public BankAccount getBankAccountsById(int id) {
		
		
		// get organization information
		BankAccount organizationAccount = entityManager.find(BankAccount.class, id);
		
		// return the db information
		return organizationAccount;
	}

	@Override
	@Transactional
	public BankAccount updateBankAccount(BankAccount account) {
		return entityManager.merge(account);
	}

	@Override
	public BankAccount getBankAccountsByBicIban(String orgBic, String orgIban) {
		
		String sqlQuery = "from BankAccount where iban = :iban AND bic = :bic";
		TypedQuery<BankAccount> query = entityManager.createQuery(sqlQuery, BankAccount.class);
		query.setParameter("iban", orgIban);
		query.setParameter("bic", orgBic);
		
		BankAccount account = null;
	    try {
	        account = query.getSingleResult();
	    } catch (NoResultException e) {
	        logger.error("Unexpected exception ", e);
	    }
	    
	    return account;
	}

	@Override
	@Transactional
	public void insertTransfers(TransferEntity transfer) {
		
		entityManager.persist(transfer);
	
	}

}
