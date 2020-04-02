package ua.epam.springapp.repository.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.epam.springapp.model.Developer;
import ua.epam.springapp.repository.DeveloperRepository;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Repository
@Transactional
public class DeveloperDaoImpl implements DeveloperRepository {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void add(Developer entity) {
        Session session = sessionFactory.getCurrentSession();
        session.save(entity);
    }

    @Override
    public Developer get(UUID id) {
        Session session = sessionFactory.getCurrentSession();
        Developer developer = session.get(Developer.class, id);
        return Objects.requireNonNull(developer, "Account not found by id: " + id);
    }

    @Override
    public List<Developer> getAll() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from Developer", Developer.class).list();
    }

    @Override
    public Developer remove(UUID id) {
        Developer developer = get(id);
        sessionFactory.getCurrentSession().delete(developer);
        return developer;
    }
}
