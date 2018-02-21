package br.ita.webquest.dao;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import br.ita.webquest.model.Template;
import br.ita.webquest.util.HibernateUtil;

public class TemplateDaoImpl implements TemplateDao {

    @Override
    public Template getTemplate(Long id) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        
        Template template = (Template) session.load(Template.class, id);
        
//        System.out.println(template);
        template.toString();
        
        if (!transaction.wasCommitted()) {
            transaction.commit();
        }
        
        return template;
    }
    
    @Override
    public Template getTemplate(String name) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        
        Template template = (Template) session.createQuery("from Template where name = '" + name + "'").list().get(0);
        
//        System.out.println(template);
        template.toString();
        
        if (!transaction.wasCommitted()) {
            transaction.commit();
        }
        
        return template;
    }

    @Override
    public List<Template> listTemplates() {
        
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        
        List list = session.createQuery("From Template").list();
        
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
    public void save(Template template) {
        
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();

        session.save(template);

        if (!transaction.wasCommitted()) {
            transaction.commit();
        }
    }
}
