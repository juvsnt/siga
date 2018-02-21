package br.ita.webquest.dao;

import br.ita.webquest.model.Actor;
import br.ita.webquest.model.User;
import br.ita.webquest.util.HibernateUtil;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class UserDaoImpl implements UserDao {

    @Override
    public User getUser(Long id) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();

        User user = (User) session.load(User.class, id);

//        System.out.println(user);
        user.toString();

        if (!transaction.wasCommitted()) {
            transaction.commit();
        }

        return user;
    }

    @Override
    public User getUser(String email) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();

        List list = session.createQuery("from User where email = '" + email + "'").list();

        User user = null;
        if (list.size() > 0) {
            user = (User) list.get(0);
        }

        //System.out.println(user);
//        if (user == null) {
//            user = new User();
//        }else{
            user.toString();
//        }

        if (!transaction.wasCommitted()) {
            transaction.commit();
        }

        return user;
    }

    @Override
    public void save(User user) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();

        session.save(user);

        if (!transaction.wasCommitted()) {
            transaction.commit();
        }
    }

    @Override
    public void remove(User user) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();

        session.delete(user);

        if (!transaction.wasCommitted()) {
            transaction.commit();
        }

    }

    @Override
    public void update(User user) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();

        session.update(user);

        if (!transaction.wasCommitted()) {
            transaction.commit();
        }
    }

    @Override
    public List<User> listUsers() {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();

        List list = session.createQuery("From User").list();

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
    public List<User> listUsersStaff() {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();

        List list = session.createQuery("From User where user_level = 'staff'").list();

        for (int i = 0; i < list.size(); i++) {
//            System.out.println(list.get(i));
            list.get(i).toString();
        }

        if (!transaction.wasCommitted()) {
            transaction.commit();
        }

        return list;
    }
}
