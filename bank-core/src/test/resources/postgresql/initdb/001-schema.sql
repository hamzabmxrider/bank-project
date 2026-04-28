-- CREATE DATABASE account;

\c bank_account;
CREATE SEQUENCE compte_bancaire_seq START 10000000000;
GRANT USAGE, SELECT ON SEQUENCE compte_bancaire_seq TO bank_account;