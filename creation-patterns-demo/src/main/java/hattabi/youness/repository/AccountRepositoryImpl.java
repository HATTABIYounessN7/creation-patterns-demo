package hattabi.youness.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import hattabi.youness.model.BankAccount;

public class AccountRepositoryImpl implements AccountRepository {
    private final Map<Long, BankAccount> accounts = new HashMap<>();
    private long accountsCount = 0;

    @Override
    public void delete(Long id) {
        accounts.remove(id);
    }

    @Override
    public List<BankAccount> findAll() {
        return new ArrayList<>(accounts.values());
    }

    @Override
    public Optional<BankAccount> findById(Long id) {
        return Optional.ofNullable(accounts.get(id));
    }

    @Override
    public BankAccount save(BankAccount account) {
        account.setAccountId(++accountsCount);
        accounts.put(account.getAccountId(), account);
        return account;
    }

    @Override
    public List<BankAccount> search(Predicate<BankAccount> predicate) {
        return accounts.values().stream().filter(predicate).collect(Collectors.toList());
    }

    @Override
    public BankAccount update(BankAccount account) {
        accounts.put(account.getAccountId(), account);
        return account;
    }

    public long getAccountsCount() {
        return accountsCount;
    }
}
