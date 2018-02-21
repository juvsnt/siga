package br.ita.webquest.dao;

import br.ita.webquest.model.Qualifying;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import br.ita.webquest.util.HibernateUtil;

public class QualifyingDaoImpl implements QualifyingDao {

    @Override
    public Qualifying getQualifying(Long id) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        
        Qualifying qualifying = (Qualifying) session.load(Qualifying.class, id);

//        System.out.println(qualifying);
        qualifying.toString();
        
        if (!transaction.wasCommitted()) {
            transaction.commit();
        }

        return qualifying;
    }

    @Override
    public Qualifying getQualifying(String title) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        
        Qualifying qualifying = (Qualifying) session.createQuery("from Qualifying where title = '" + title + "'").list().get(0);

//        System.out.println(qualifying);
        qualifying.toString();

        if (!transaction.wasCommitted()) {
            transaction.commit();
        }

        return qualifying;
    }

    @Override
    public List<Qualifying> listQualifiers(Long id) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();

        List<Qualifying> list = session.createQuery("From Qualifying where webquest_id = '" + id + "'").list();
        
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
    public void save(Qualifying qualifying) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();

        session.save(qualifying);

        if (!transaction.wasCommitted()) {
            transaction.commit();
        }
    }

    @Override
    public void update(Qualifying qualifying) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();

        session.update(qualifying);

        if (!transaction.wasCommitted()) {
            transaction.commit();
        }
    }

    @Override
    public void remove(Qualifying qualifying) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();

        session.delete(qualifying);

        if (!transaction.wasCommitted()) {
            transaction.commit();
        }
    }
}
