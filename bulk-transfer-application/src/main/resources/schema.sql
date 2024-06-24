GRANT ALL PRIVILEGES ON *.* TO 'bulk'@'%' IDENTIFIED BY 'bulk' WITH GRANT OPTION;
FLUSH PRIVILEGES;

DROP TABLE IF EXISTS transfers;
DROP TABLE IF EXISTS bank_accounts;

CREATE TABLE bank_accounts (
    id INT PRIMARY KEY AUTO_INCREMENT,
    organization_name VARCHAR(255),
    balance_cents INT,
    iban VARCHAR(255),
    bic VARCHAR(255)
);

CREATE TABLE transfers (
    id INT PRIMARY KEY AUTO_INCREMENT,
    counterparty_name VARCHAR(255),
    counterparty_iban VARCHAR(255),
    counterparty_bic VARCHAR(255),
    amount_cents INT,
    bank_account_id INT,
    description VARCHAR(255),
    FOREIGN KEY (bank_account_id) REFERENCES bank_accounts(id)
);
