package br.ita.webquest.dao;

import br.ita.webquest.model.Resource;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import br.ita.webquest.util.HibernateUtil;

public class ResourceDaoImpl implements ResourceDao {

    @Override
    public Resource getResource(Long id) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        
        Resource resource = (Resource) session.load(Resource.class, id);
        
//        System.out.println(resource);
        resource.toString();
        
        if (!transaction.wasCommitted()) {
            transaction.commit();
        }
        
        return resource;
    }
    
    @Override
    public Resource getResource(String title) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        
        Resource resource = (Resource) session.createQuery("from Resource where title = '" + title + "'").list().get(0);
        
//        System.out.println(resource);
        resource.toString();
        
        if (!transaction.wasCommitted()) {
            transaction.commit();
        }
        
        return resource;
    }

    @Override
    public List<Resource> listResourcesByActivity(Long id) {
        
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        
        List<Resource> list = session.createQuery("From Resource where activity_id = '"+id+"'").list();
        
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
    public List<Resource> listResourcesByPhase(Long id) {
        
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        
        List<Resource> list = session.createQuery("From Resource where phase_id = '"+id+"'").list();
        
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
    public void save(Resource resource) {
        
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();

        session.save(resource);

        if (!transaction.wasCommitted()) {
            transaction.commit();
        }
    }
    
    @Override
    public void update(Resource resource) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();

        session.update(resource);

        if (!transaction.wasCommitted()) {
            transaction.commit();
        }
    }
    
    @Override
    public void remove(Resource resource) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();

        session.delete(resource);

        if (!transaction.wasCommitted()) {
            transaction.commit();
        }
    }
}
