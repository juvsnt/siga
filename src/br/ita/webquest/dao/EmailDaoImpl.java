package br.ita.webquest.dao;

import br.ita.webquest.model.Email;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import br.ita.webquest.util.HibernateUtil;

public class EmailDaoImpl implements EmailDao {

    @Override
    public Email getEmail(Long id) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        
        Email email = (Email) session.load(Email.class, id);
        
//        System.out.println(email);
        email.toString();
        
        if (!transaction.wasCommitted()) {
            transaction.commit();
        }
        
        return email;
    }
    
    @Override
    public int getNumEmail(String email) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        
        int num_emails = session.createQuery("from Email where email = '" + email + "'").list().size();
        
        System.out.println(num_emails);
        
        if (!transaction.wasCommitted()) {
            transaction.commit();
        }
        
        return num_emails;
    }
    
    @Override
    public Email getEmail2(String email) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        
        Email email2 = (Email) session.createQuery("from Email where email = '" + email + "'").list().get(0);
        
//        System.out.println(email2);
        email2.toString();
        
        if (!transaction.wasCommitted()) {
            transaction.commit();
        }
        
        return email2;
    }

    @Override
    public List<Email> listEmails(Long id) {
        
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        
        List<Email> list = session.createQuery("From Email where user_id = '"+id+"'").list();
        
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
    public void save(Email email) {
        
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();

        session.save(email);

        if (!transaction.wasCommitted()) {
            transaction.commit();
        }
    }

    @Override
    public void update(Email email) {
        
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();

        session.update(email);

        if (!transaction.wasCommitted()) {
            transaction.commit();
        }
    }
    
    @Override
    public void remove(Email email) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();

        session.delete(email);

        if (!transaction.wasCommitted()) {
            transaction.commit();
        }
    }
}
