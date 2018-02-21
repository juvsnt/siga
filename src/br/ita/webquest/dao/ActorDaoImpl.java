package br.ita.webquest.dao;

import br.ita.webquest.model.Actor;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import br.ita.webquest.util.HibernateUtil;

public class ActorDaoImpl implements ActorDao {

    @Override
    public Actor getActor(Long id) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();

        Actor actor = (Actor) session.load(Actor.class, id);

//        System.out.println(actor);
        actor.toString();

        if (!transaction.wasCommitted()) {
            transaction.commit();
        }

        return actor;
    }

    @Override
    public Actor getActor(String title) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();

        Actor actor = (Actor) session.createQuery("from Actor where title = '" + title + "'").list().get(0);

//        System.out.println(actor);
        actor.toString();

        if (!transaction.wasCommitted()) {
            transaction.commit();
        }

        return actor;
    }

    @Override
    public List<Actor> listActorsByActivity(Long id) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();

        List<Actor> list = session.createQuery("From Actor where activity_id = '" + id + "'").list();

        for (int i = 0; i < list.size(); i++) {
//            System.out.println(list.get(i));
            list.get(i).toString();
        }

        if (!transaction.wasCommitted()) {
            transaction.commit();
        }

        return list;
    }

    @Override
    public List<Actor> listActorsByPhase(Long id) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();

        List<Actor> list = session.createQuery("From Actor where phase_id = '" + id + "'").list();

        for (int i = 0; i < list.size(); i++) {
//            System.out.println(list.get(i));
            list.get(i).toString();
        }
        
        if (!transaction.wasCommitted()) {
            transaction.commit();
        }

        return list;
    }

    @Override
    public void save(Actor actor) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();

        session.save(actor);

        if (!transaction.wasCommitted()) {
            transaction.commit();
        }
    }

    @Override
    public void update(Actor actor) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();

        session.update(actor);

        if (!transaction.wasCommitted()) {
            transaction.commit();
        }
    }

    @Override
    public void remove(Actor actor) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();

        session.delete(actor);

        if (!transaction.wasCommitted()) {
            transaction.commit();
        }
    }
}
