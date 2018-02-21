package br.ita.webquest.dao;

import br.ita.webquest.model.PartyActivity;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import br.ita.webquest.util.HibernateUtil;

public class PartyActivityDaoImpl implements PartyActivityDao {

    @Override
    public PartyActivity getPartyActivity(Long id) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();

        PartyActivity partyActivity = (PartyActivity) session.load(PartyActivity.class, id);

//        System.out.println(partyActivity);
        partyActivity.toString();

        if (!transaction.wasCommitted()) {
            transaction.commit();
        }

        return partyActivity;
    }

    @Override
    public List<PartyActivity> listActivitiesByPhase(Long id) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();

        List<PartyActivity> list = session.createQuery("From PartyActivity where phase_id = '" + id + "'").list();

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
    public List<PartyActivity> listActivitiesByWebquest(Long id) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();

        List<PartyActivity> list = session.createQuery("From partyActivity where webquest_id = '" + id + "'").list();

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
    public List<PartyActivity> listActivities(Long id) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();

        List<PartyActivity> list = session.createQuery("From PartyActivity where party_id = '" + id + "'").list();

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
    public void save(PartyActivity partyActivity) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();

        session.save(partyActivity);

        if (!transaction.wasCommitted()) {
            transaction.commit();
        }
    }

    @Override
    public void update(PartyActivity partyActivity) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();

        session.update(partyActivity);

        if (!transaction.wasCommitted()) {
            transaction.commit();
        }
    }

    @Override
    public void remove(PartyActivity partyActivity) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();

        session.delete(partyActivity);

        if (!transaction.wasCommitted()) {
            transaction.commit();
        }
    }
}
