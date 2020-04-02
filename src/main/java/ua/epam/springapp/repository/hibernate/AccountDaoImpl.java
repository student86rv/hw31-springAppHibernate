package ua.epam.springapp.repository.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.epam.springapp.model.Account;
import ua.epam.springapp.repository.AccountRepository;

import java.util.List;
import java.util.UUID;

@Repository
@Transactional
public class AccountDaoImpl implements AccountRepository {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void add(Account entity) {
        Session session = sessionFactory.getCurrentSession();
        session.save(entity);
    }

    @Override
    public Account get(UUID id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Account.class, id);
    }

    @Override
    public List<Account> getAll() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from Account", Account.class).list();
    }

    @Override
    public Account remove(UUID id) {
        Account account = get(id);
        sessionFactory.getCurrentSession().delete(account);
        return account;
    }
}
