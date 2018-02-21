package br.ita.webquest.dao;

import br.ita.webquest.model.Activity;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import br.ita.webquest.util.HibernateUtil;

public class ActivityDaoImpl implements ActivityDao {

    @Override
    public Activity getActivity(Long id) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();

        Activity activity = (Activity) session.load(Activity.class, id);

//        System.out.println(activity);
        activity.toString();

        if (!transaction.wasCommitted()) {
            transaction.commit();
        }

        return activity;
    }

    @Override
    public Activity getActivity(String name) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();

        Activity activity = (Activity) session.createQuery("from Activity where name = '" + name + "'").list().get(0);

//        System.out.println(activity);
        activity.toString();

        if (!transaction.wasCommitted()) {
            transaction.commit();
        }

        return activity;
    }

    @Override
    public List<Activity> listActivitiesByPhase(Long id) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();

        List<Activity> list = session.createQuery("From Activity where phase_id = '" + id + "'").list();

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
    public List<Activity> listActivitiesByWebquest(Long id) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();

        List<Activity> list = session.createQuery("From Activity where webquest_id = '" + id + "'").list();

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
    public List<Activity> listActivities(Long id) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();

        List<Activity> list = session.createQuery("From Activity where phase_id = '" + id + "'").list();

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
    public void save(Activity activity) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();

        session.save(activity);

        if (!transaction.wasCommitted()) {
            transaction.commit();
        }
    }

    @Override
    public void update(Activity activity) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();

        session.update(activity);

        if (!transaction.wasCommitted()) {
            transaction.commit();
        }
    }

    @Override
    public void remove(Activity activity) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();

        session.delete(activity);

        if (!transaction.wasCommitted()) {
            transaction.commit();
        }
    }
}
