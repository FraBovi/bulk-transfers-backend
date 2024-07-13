INSERT INTO bank_accounts (organization_name, balance_cents, iban, bic)
VALUES ('COMPANY 1', 10000000, 'IT10474608000005006107XXXXX', 'OIVUSCLQXXX');

INSERT INTO bank_accounts (organization_name, balance_cents, iban, bic)
VALUES ('COMPANY 43', 2323425, 'SPT0474677000005006107YYXXX', 'VUVUSCLQXXX');

INSERT INTO bank_accounts (organization_name, balance_cents, iban, bic)
VALUES ('COMPANY 26', 10340, 'EN10474608000005006109YYYY', 'NRSTDVQXXX');

INSERT INTO bank_accounts (organization_name, balance_cents, iban, bic)
VALUES ('COMPANY 459', 166660000, 'GM10474608000005006107KKKKK', 'EGDTSJCKKK');

INSERT INTO transfers (counterparty_name, counterparty_iban, counterparty_bic, amount_cents, bank_account_id, description)
VALUES ('COMPANY 26', 'EE382200221020145685', 'TTOPFRPPXXX', 11000000, 1, 'Salary');

INSERT INTO transfers (counterparty_name, counterparty_iban, counterparty_bic, amount_cents, bank_account_id, description)
VALUES ('COMPANY 1', 'EE383680981021245685', 'RTLYFRPPTOU', 1000000, 1, 'Bonus Salary');