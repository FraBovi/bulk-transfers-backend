package com.mycompany.bulk_transfer_application.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mycompany.bulk_transfer_application.dto.SearchParameters;
import com.mycompany.bulk_transfer_application.entity.BankAccount;
import com.mycompany.bulk_transfer_application.entity.TransferEntity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

/**
 * Repository class in which all the DB operation are performed
 */
@Repository
public class TransferDAO {

    private EntityManager entityManager;

    @Autowired
    public TransferDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * Update account information on DB using with the @param account
     * 
     * @param account with updated information
     * @return updated BankAccount
     */
    @Transactional
    public BankAccount updateBankAccount(BankAccount account) {
        return entityManager.merge(account);
    }

    /**
     * Returns BankAccount DB information using @param params
     * 
     * @return BankAccount class with DB info
     */
    public List<BankAccount> searchBankAccounts(SearchParameters params) {

        String sqlQuery = "from BankAccount where iban = :iban OR bic = :bic OR organizationName = :organizationName";
        TypedQuery<BankAccount> query = entityManager.createQuery(sqlQuery, BankAccount.class);
        query.setParameter("iban", params.getIban());
        query.setParameter("bic", params.getBic());
        query.setParameter("organizationName", params.getName());

        List<BankAccount> accounts = null;
        accounts = query.getResultList();

        return accounts;
    }

    /**
     * Create a new transfer in the DB table
     * 
     * @param transfer is the new entity to insert in DB
     */
    @Transactional
    public void insertTransfers(TransferEntity transfer) {

        entityManager.persist(transfer);

    }

    /**
     * @return all the BankAccount items from the DB
     */
    public List<BankAccount> findAllBankAccounts() {

        String sqlQuery = "from BankAccount";
        TypedQuery<BankAccount> query = entityManager.createQuery(sqlQuery, BankAccount.class);

        List<BankAccount> accounts = null;
        accounts = query.getResultList();

        return accounts;

    }

}
