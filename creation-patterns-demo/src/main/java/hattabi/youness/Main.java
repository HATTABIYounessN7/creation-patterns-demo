package hattabi.youness;

import hattabi.youness.model.AccountStatus;
import hattabi.youness.model.AccountType;
import hattabi.youness.model.BankAccount;
import hattabi.youness.repository.AccountRepositoryImpl;

public class Main {
    public static void main(String[] args) {
        AccountRepositoryImpl repo = new AccountRepositoryImpl();
        repo.populate();

        BankAccount account = repo.findById(1L).orElse(null);
        System.out.println(account);

        repo.search(a -> a.getType() == AccountType.CURRENT).forEach(System.out::println);
        repo.search(acc -> acc.getType() == AccountType.CURRENT
                && acc.getStatus() == AccountStatus.ACTIVATED).forEach(System.out::println);
    }
}