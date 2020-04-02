package ua.epam.springapp.repository.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.epam.springapp.model.Skill;
import ua.epam.springapp.repository.SkillRepository;

import java.util.List;
import java.util.UUID;

@Repository
@Transactional
public class SkillDaoImpl implements SkillRepository {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void add(Skill entity) {
        Session session = sessionFactory.getCurrentSession();
        session.save(entity);
    }

    @Override
    public Skill get(UUID id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Skill.class, id);
    }

    @Override
    public List<Skill> getAll() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from Skill", Skill.class).list();
    }

    @Override
    public Skill remove(UUID id) {
        Skill skill = get(id);
        sessionFactory.getCurrentSession().delete(skill);
        return skill;
    }
}
