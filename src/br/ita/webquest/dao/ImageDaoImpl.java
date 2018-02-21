package br.ita.webquest.dao;

import br.ita.webquest.model.Image;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import br.ita.webquest.util.HibernateUtil;

public class ImageDaoImpl implements ImageDao {

    @Override
    public Image getImage(Long id) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        
        Image image = (Image) session.load(Image.class, id);
        
//        System.out.println(image);
        image.toString();
        
        if (!transaction.wasCommitted()) {
            transaction.commit();
        }

        return image;
    }
    
    @Override
    public Image getImage(String name) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        
        Image image = (Image) session.createQuery("from Image where name = '" + name + "'").list().get(0);
        
//        System.out.println(image);
        image.toString();
        
        if (!transaction.wasCommitted()) {
            transaction.commit();
        }

        return image;
    }

    @Override
    public List<Image> listImages(Long id) {
        
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        
        List list = session.createQuery("From Image where user_id = '"+id+"'").list();
        
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
    public void save(Image image) {
        
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();

        session.save(image);

        if (!transaction.wasCommitted()) {
            transaction.commit();
        }
    }
    
    @Override
    public void remove(Image image) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();

        session.delete(image);

        if (!transaction.wasCommitted()) {
            transaction.commit();
        }
    }
}
