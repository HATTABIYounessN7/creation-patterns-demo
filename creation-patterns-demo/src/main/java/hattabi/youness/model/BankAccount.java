package hattabi.youness.model;

public class BankAccount {
    private Long accountId;
    private double balance;
    private String currency;
    private AccountType type;
    private AccountStatus status;
    private Customer customer;

    public BankAccount() {
    }

    public BankAccount(Long accountId, double balance, String currency, AccountType type, AccountStatus status,
            Customer customer) {
        this.accountId = accountId;
        this.balance = balance;
        this.currency = currency;
        this.type = type;
        this.status = status;
        this.customer = customer;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public AccountType getType() {
        return type;
    }

    public void setType(AccountType type) {
        this.type = type;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public static class AccountBuilder {
        private final BankAccount account = new BankAccount();

        public AccountBuilder accountId(Long id) {
            account.accountId = id;
            return this;
        }

        public AccountBuilder balance(double balance) {
            account.balance = balance;
            return this;
        }

        public AccountBuilder currency(String currency) {
            account.currency = currency;
            return this;
        }

        public AccountBuilder type(AccountType type) {
            account.type = type;
            return this;
        }

        public AccountBuilder status(AccountStatus s) {
            account.status = s;
            return this;
        }

        public BankAccount build() {
            return account;
        }
    }

    public static AccountBuilder builder() {
        return new AccountBuilder();
    }
}
