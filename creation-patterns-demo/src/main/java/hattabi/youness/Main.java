package hattabi.youness;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import hattabi.youness.builder.BankDirector;
import hattabi.youness.model.AccountStatus;
import hattabi.youness.model.AccountType;
import hattabi.youness.model.BankAccount;
import hattabi.youness.model.Customer;
import hattabi.youness.repository.AccountRepositoryImpl;
import hattabi.youness.utils.JsonUtil;

public class Main {
    private static final int THREADS = 10;
    private static final int ACCOUNTS_PER_POPULATE = 10;

    public static void main(String[] args) throws InterruptedException {
        demoBuilder();
        AccountRepositoryImpl repo = demoSingleton();
        demoRepository(repo);
        demoPrototype();
        demoConcurrency(repo);
    }

    private static void demoBuilder() {
        section("1. Pattern Builder");

        BankDirector director = new BankDirector();
        BankAccount account = director.accountBuilder()
                .accountId(0L)
                .balance(5000)
                .currency("MAD")
                .type(AccountType.CURRENT)
                .status(AccountStatus.ACTIVATED)
                .build();

        System.out.println("Compte construit avec le Builder : " + account);
        System.out.println("Meme compte via BankAccount.builder() : "
                + BankAccount.builder().balance(100).currency("USD").build());
    }

    private static AccountRepositoryImpl demoSingleton() {
        section("2. Pattern Singleton");

        AccountRepositoryImpl r1 = AccountRepositoryImpl.getInstance();
        AccountRepositoryImpl r2 = AccountRepositoryImpl.getInstance();
        AccountRepositoryImpl r3 = AccountRepositoryImpl.getInstance();

        System.out.println("r1 == r2 : " + (r1 == r2));
        System.out.println("r2 == r3 : " + (r2 == r3));
        System.out.println("hash r1=" + System.identityHashCode(r1)
                + " r2=" + System.identityHashCode(r2)
                + " r3=" + System.identityHashCode(r3));
        return r1;
    }

    private static void demoRepository(AccountRepositoryImpl repo) {
        section("3. Repository : findById, search, JSON");

        repo.populate();
        System.out.println("Comptes en memoire : " + repo.findAll().size());

        System.out.println("findById(1) en JSON :");
        System.out.println(repo.findById(1L).map(JsonUtil::toJson).orElse("introuvable"));

        System.out.println("findById(9999) present ? " + repo.findById(9999L).isPresent());

        System.out.println("--- Comptes courants ---");
        repo.search(a -> a.getType() == AccountType.CURRENT)
                .forEach(a -> System.out.println(JsonUtil.toJson(a)));

        System.out.println("--- Comptes courants ET actives ---");
        repo.search(a -> a.getType() == AccountType.CURRENT
                && a.getStatus() == AccountStatus.ACTIVATED)
                .forEach(a -> System.out.println(JsonUtil.toJson(a)));

        repo.delete(1L);
        System.out.println("Apres delete(1) -> findById(1) present ? "
                + repo.findById(1L).isPresent());
    }

    private static void demoPrototype() {
        section("4. Pattern Prototype");

        BankAccount original = new BankDirector().accountBuilder()
                .accountId(100L)
                .balance(8000)
                .currency("MAD")
                .type(AccountType.SAVING)
                .status(AccountStatus.ACTIVATED)
                .build();
        original.setCustomer(new Customer(1L, "Hassan"));

        BankAccount alias = original;
        BankAccount copy = original.clone();

        System.out.println("Adresses memoires (hash) : original=" + System.identityHashCode(original)
                + " alias=" + System.identityHashCode(alias)
                + " copy=" + System.identityHashCode(copy));
        System.out.println("original == alias : " + (original == alias));
        System.out.println("original == copy  : " + (original == copy));

        original.setBalance(1000);
        original.getCustomer().setName("Changed");

        System.out.println("Apres modification de l'original :");
        System.out.println("  original : balance=" + original.getBalance()
                + " customer=" + original.getCustomer().getName());
        System.out.println("  alias    : balance=" + alias.getBalance()
                + " customer=" + alias.getCustomer().getName()
                + "   (suit l'original : meme objet)");
        System.out.println("  copy     : balance=" + copy.getBalance()
                + " customer=" + copy.getCustomer().getName()
                + "   (independant : deep copy; le solde est remis a 0 par clone())");
    }

    private static void demoConcurrency(AccountRepositoryImpl repo) throws InterruptedException {
        section("5. Singleton et threads (zone critique)");

        long before = repo.getAccountsCount();
        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < THREADS; i++) {
            Thread t = new Thread(repo::populate, "worker-" + i);
            threads.add(t);
            t.start();
        }
        for (Thread t : threads) {
            t.join();
        }

        long expected = before + (long) THREADS * ACCOUNTS_PER_POPULATE;
        long actual = repo.getAccountsCount();

        System.out.println("Compteur attendu : " + expected);
        System.out.println("Compteur obtenu  : " + actual);
        System.out.println(actual == expected
                ? "OK : la section critique est correctement synchronisee."
                : "ERREUR : race condition detectee (incrementations perdues).");
    }

    private static void section(String title) {
        System.out.println();
        System.out.println("===== " + title + " =====");
    }
}