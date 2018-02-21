package br.ita.webquest.dao;

import br.ita.webquest.model.PartyPhase;
import br.ita.webquest.util.HibernateUtil;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class PartyPhaseDaoImpl implements PartyPhaseDao {

    @Override
    public PartyPhase getPartyPhase(Long id) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();

        PartyPhase phase = (PartyPhase) session.load(PartyPhase.class, id);

        //System.out.println(phase);
        phase.toString();

        if (!transaction.wasCommitted()) {
            transaction.commit();
        }

        return phase;
    }

    @Override
    public List<PartyPhase> listPartyPhases(Long id) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();

        List<PartyPhase> list = session.createQuery("From PartyPhase where party_id = '" + id + "'").list();

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
    public void save(PartyPhase partyPhasse) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();

        session.save(partyPhasse);

            //System.out.println("qq "+partyPhasse);
        if (!transaction.wasCommitted()) {
            transaction.commit();
        }
    }

    @Override
    public void update(PartyPhase partyPhasse) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();

        session.update(partyPhasse);

        if (!transaction.wasCommitted()) {
            transaction.commit();
        }
    }

    @Override
    public void remove(PartyPhase partyPhasse) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();

        session.delete(partyPhasse);

        if (!transaction.wasCommitted()) {
            transaction.commit();
        }
    }
}

