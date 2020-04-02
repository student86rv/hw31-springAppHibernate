package ua.epam.springapp.repository;

import ua.epam.springapp.model.Account;

import java.util.UUID;

public interface AccountRepository extends GenericRepository<Account, UUID> {
}
