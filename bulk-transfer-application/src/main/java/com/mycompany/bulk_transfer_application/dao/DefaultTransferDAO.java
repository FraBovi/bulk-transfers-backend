package com.mycompany.bulk_transfer_application.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mycompany.bulk_transfer_application.entity.BankAccount;
import com.mycompany.bulk_transfer_application.entity.TransferEntity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

/**
 * Repository class in which all the DB operation are performed
 */
@Repository
public class DefaultTransferDAO implements TransferDAO {
	
	private EntityManager entityManager;
	
	@Autowired
	public DefaultTransferDAO(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	/**
	 * Returns BankAccount DB information using @param id
	 * 
	 * @param id integer representing the account identifier
	 * @return BankAccount class with DB info
	 */
	@Override
    // TODO: is this function used somewhere else?
	public BankAccount findBankAccountById(int id) {
		
		// get organization information
		BankAccount organizationAccount = entityManager.find(BankAccount.class, id);
		
		// return the db information
		return organizationAccount;
	}

	
	/**
	 * Update account information on DB using with the @param account
	 * 
	 * @param account with updated information
	 * @return updated BankAccount
	 */
	@Override
	@Transactional
	public BankAccount updateBankAccount(BankAccount account) {
		return entityManager.merge(account);
	}

	/**
	 * Returns BankAccount DB information using @param orgBic and
	 * @param orgIban
	 * 
	 * @param orgBic organization bic value
	 * @param orgIban organization iban value
	 * @return BankAccount class with DB info
	 */
	@Override
	public BankAccount getBankAccountByBicAndIban(String orgBic, String orgIban) {
		
		String sqlQuery = "from BankAccount where iban = :iban AND bic = :bic";
		TypedQuery<BankAccount> query = entityManager.createQuery(sqlQuery, BankAccount.class);
		query.setParameter("iban", orgIban);
		query.setParameter("bic", orgBic);
		
		BankAccount account = null;
        // BUG: the account might not exist. Why are you throwing up an exception?
	    account = query.getSingleResult();
	    
	    return account;
	}

	/**
	 * Create a new transfer in the DB table
	 * 
	 * @param transfer is the new entity to insert in DB
	 */
	@Override
	@Transactional
	public void insertTransfers(TransferEntity transfer) {
		
		entityManager.persist(transfer);
	
	}

}
