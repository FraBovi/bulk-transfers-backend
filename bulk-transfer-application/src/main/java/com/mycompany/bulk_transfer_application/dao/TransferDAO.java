package com.mycompany.bulk_transfer_application.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mycompany.bulk_transfer_application.entity.BankAccount;
import com.mycompany.bulk_transfer_application.entity.TransferEntity;
import com.mycompany.bulk_transfer_application.exception.NoBankAccountFoundException;

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
     * Returns BankAccount DB information using @param orgBic and
     * 
     * @param orgIban
     * 
     * @param orgBic  organization bic value
     * @param orgIban organization iban value
     * @return BankAccount class with DB info
     */
    public List<BankAccount> getBankAccountByBicAndIban(String orgBic, String orgIban) {

        String sqlQuery = "from BankAccount where iban = :iban AND bic = :bic";
        TypedQuery<BankAccount> query = entityManager.createQuery(sqlQuery, BankAccount.class);
        query.setParameter("iban", orgIban);
        query.setParameter("bic", orgBic);

        List<BankAccount> accounts = null;
        // BUG: the account might not exist. Why are you throwing up an exception?
        // This BUG needs to be addressed.
        accounts = query.getResultList();

        if (accounts.isEmpty())
            throw new NoBankAccountFoundException();

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
     * Returns a list of BankAccount information from DB using organization Bic code
     * 
     * @param orgBic organization bic value
     * @return List<BankAccount> class with DB info
     */
    public List<BankAccount> searchBankAccountsByBic(String orgBic) {

        String sqlQuery = "from BankAccount where bic = :bic";
        TypedQuery<BankAccount> query = entityManager.createQuery(sqlQuery, BankAccount.class);
        query.setParameter("bic", orgBic);

        List<BankAccount> accounts = null;
        accounts = query.getResultList();

        if (accounts.isEmpty())
            throw new NoBankAccountFoundException();

        return accounts;
    }

    /**
     * Returns a list of BankAccount information from DB using organization Iban
     * code
     * 
     * @param orgIban organization iban value
     * @return List<BankAccount> class with DB info
     */
    public List<BankAccount> searchBankAccountsByIban(String orgIban) {

        String sqlQuery = "from BankAccount where iban = :iban";
        TypedQuery<BankAccount> query = entityManager.createQuery(sqlQuery, BankAccount.class);
        query.setParameter("iban", orgIban);

        List<BankAccount> accounts = null;
        accounts = query.getResultList();

        if (accounts.isEmpty())
            throw new NoBankAccountFoundException();

        return accounts;

    }

    /**
     * Returns a list of BankAccount information from DB using organization Name
     *
     * 
     * @param orgName organization name value
     * @return List<BankAccount> class with DB info
     */
    public List<BankAccount> searchBankAccountsByName(String orgName) {

        String sqlQuery = "from BankAccount where organization_name = :name";
        TypedQuery<BankAccount> query = entityManager.createQuery(sqlQuery, BankAccount.class);
        query.setParameter("name", orgName);

        List<BankAccount> accounts = null;
        accounts = query.getResultList();

        if (accounts.isEmpty())
            throw new NoBankAccountFoundException();

        return accounts;

    }

    /**
     * @return all the BankAccount items from the DB
     */
    public List<BankAccount> findAllBankAccounts() {

        String sqlQuery = "from BankAccount";
        TypedQuery<BankAccount> query = entityManager.createQuery(sqlQuery, BankAccount.class);

        List<BankAccount> accounts = null;
        accounts = query.getResultList();

        if (accounts.isEmpty())
            throw new NoBankAccountFoundException();

        return accounts;

    }

}
