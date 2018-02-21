package br.ita.webquest.dao;

import br.ita.webquest.model.Outcome;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import br.ita.webquest.util.HibernateUtil;

public class OutcomeDaoImpl implements OutcomeDao {

    @Override
    public Outcome getOutcome(Long id) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        
        Outcome outcome = (Outcome) session.load(Outcome.class, id);

//        System.out.println(outcome);
        outcome.toString();
        
        if (!transaction.wasCommitted()) {
            transaction.commit();
        }

        return outcome;
    }
    
    @Override
    public Outcome getOutcome(String title) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        
        Outcome outcome = (Outcome) session.createQuery("from Outcome where title = '" + title + "'").list().get(0);

//        System.out.println(outcome);
        outcome.toString();

        if (!transaction.wasCommitted()) {
            transaction.commit();
        }

        return outcome;
    }

    @Override
    public List<Outcome> listOutcomes(Long id) {
        
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        
        List list = session.createQuery("From Outcome where qualifying_id = '"+id+"'").list();
        
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
    public void save(Outcome outcome) {
        
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();

        session.save(outcome);

        if (!transaction.wasCommitted()) {
            transaction.commit();
        }
    }
    
    @Override
    public void update(Outcome outcome) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();

        session.update(outcome);

        if (!transaction.wasCommitted()) {
            transaction.commit();
        }
    }
    
    @Override
    public void remove(Outcome outcome) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();

        session.delete(outcome);

        if (!transaction.wasCommitted()) {
            transaction.commit();
        }
    }
}

