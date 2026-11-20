package hattabi.youness;

import java.io.IOException;

import hattabi.youness.repository.AccountRepositoryImpl;

public class Main {
    public static void main(String[] args) {
        AccountRepositoryImpl repo = AccountRepositoryImpl.getInstance();
        for (int i = 0; i < 1000; i++) {
            new Thread(() -> {
                repo.populate();
                System.out.println(Thread.currentThread().getName()
                        + " count=" + repo.getAccountsCount()
                        + " size=" + repo.findAll().size());
            }).start();
        }

        try {
            System.in.read();
        } catch (IOException e) {
            System.out.println("IOEXCEPTION: " + e);
        }
    }
}