package hattabi.youness.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import hattabi.youness.builder.BankDirector;
import hattabi.youness.model.AccountStatus;
import hattabi.youness.model.AccountType;
import hattabi.youness.model.BankAccount;

public class AccountRepositoryImpl implements AccountRepository {
    private static final AccountRepositoryImpl INSTANCE;
    private final Object lock = new Object();
    private final Map<Long, BankAccount> accounts = new HashMap<>();
    private long accountsCount = 0;

    static {
        System.out.println("Singleton instantiation");
        INSTANCE = new AccountRepositoryImpl();
    }

    private AccountRepositoryImpl() {
    }

    public static AccountRepositoryImpl getInstance() {
        return INSTANCE;
    }

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
        synchronized (lock) {
            account.setAccountId(++accountsCount);
            accounts.put(account.getAccountId(), account);
        }
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

    public void populate() {
        BankDirector director = new BankDirector();
        for (int i = 0; i < 10; i++) {
            BankAccount acc = director.accountBuilder()
                    .balance(10000 + Math.random() * 90000)
                    .type(Math.random() > .5 ? AccountType.CURRENT : AccountType.SAVING)
                    .status(Math.random() > .5 ? AccountStatus.CREATED : AccountStatus.ACTIVATED)
                    .currency(Math.random() > .5 ? "MAD" : "EUR")
                    .build();

            save(acc);
        }
    }
}
