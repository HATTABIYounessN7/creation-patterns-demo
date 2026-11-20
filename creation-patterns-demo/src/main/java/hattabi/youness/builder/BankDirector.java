package hattabi.youness.builder;

import hattabi.youness.model.BankAccount;

public class BankDirector {
    public BankAccount.AccountBuilder accountBuilder() {
        return new BankAccount.AccountBuilder();
    }
}
