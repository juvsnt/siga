package br.ita.webquest.dao;

import br.ita.webquest.model.Phase;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import br.ita.webquest.util.HibernateUtil;

public class PhaseDaoImpl implements PhaseDao {

    @Override
    public Phase getPhase(Long id) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        
        Phase phase = (Phase) session.load(Phase.class, id);

//        System.out.println(phase);
          phase.toString();

        if (!transaction.wasCommitted()) {
            transaction.commit();
        }

        return phase;
    }

    @Override
    public Phase getPhase(String name) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        
        Phase phase = (Phase) session.createQuery("from Phase where name = '" + name + "'").list().get(0);

//        System.out.println(phase);
        phase.toString();

        if (!transaction.wasCommitted()) {
            transaction.commit();
        }

        return phase;
    }

    @Override
    public List<Phase> listPhase(Long id) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();

        List<Phase> list = session.createQuery("From Phase where webquest_id = '" + id + "'").list();

        for (int i = 0; i < list.size(); i++) {
            //System.out.println(list.get(i));
            list.get(i).toString();
        }
        
        if (!transaction.wasCommitted()) {
            transaction.commit();
        }

        return list;
    }

    @Override
    public void save(Phase phase) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();

        session.save(phase);

        if (!transaction.wasCommitted()) {
            transaction.commit();
        }
    }

    @Override
    public void update(Phase phase) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();

        session.update(phase);

        if (!transaction.wasCommitted()) {
            transaction.commit();
        }
    }

    @Override
    public void remove(Phase phase) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();

        session.delete(phase);

        if (!transaction.wasCommitted()) {
            transaction.commit();
        }
    }
}
