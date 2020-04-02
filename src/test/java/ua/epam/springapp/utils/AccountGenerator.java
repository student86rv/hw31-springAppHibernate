package ua.epam.springapp.utils;

import ua.epam.springapp.model.Account;
import ua.epam.springapp.model.AccountStatus;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AccountGenerator {

    public static List<Account> generateAccounts(int count) {
        return IntStream.range(0, count)
                .mapToObj(AccountGenerator::generateSingleAccount)
                .collect(Collectors.toList());
    }

    public static Account generateSingleAccount(int count) {
        UUID id = UUID.randomUUID();
        String email = "testMail" + count + "@test.com";
        return new Account(id, email, AccountStatus.ACTIVE);
    }

    public static Account generateSingleAccount() {
        UUID id = UUID.randomUUID();
        String email = "testMail" + "@test.com";
        return new Account(id, email, AccountStatus.ACTIVE);
    }
}
