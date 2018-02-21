package br.ita.webquest.dao;

import br.ita.webquest.model.WebQuest;
import br.ita.webquest.util.HibernateUtil;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class WebQuestDaoImpl implements WebQuestDao {

    @Override
    public WebQuest getWebQuest(Long id) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction =  session.beginTransaction();

        WebQuest webQuest = (WebQuest) session.load(WebQuest.class, id);
        
//        System.out.println(webQuest);
        webQuest.toString();

        if (!transaction.wasCommitted()) {
            transaction.commit();
        }
        
        return webQuest;
    }

    @Override
    public WebQuest getWebQuest(String title) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction =  session.beginTransaction();

        WebQuest webQuest = (WebQuest) session.createQuery("from WebQuest where title = '" + title + "'").list().get(0);
        
//        System.out.println(webQuest);
        webQuest.toString();

        if (!transaction.wasCommitted()) {
            transaction.commit();
        }
        
        return webQuest;
    }

    @Override
    public void save(WebQuest WebQuest) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        
        session.save(WebQuest);

        if (!transaction.wasCommitted()) {
            transaction.commit();
        }
    }

    @Override
    public void remove(WebQuest WebQuest) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();

        session.delete(WebQuest);

        if (!transaction.wasCommitted()) {
            transaction.commit();
        }

    }

    @Override
    public void update(WebQuest WebQuest) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();

        session.update(WebQuest);

        if (!transaction.wasCommitted()) {
            transaction.commit();
        }
    }

    @Override
    public List<WebQuest> listWebQuests(Long id) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();

        List<WebQuest> list = (List<WebQuest>) session.createQuery("From WebQuest where user_id = '" + id + "'").list();

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
    public List<WebQuest> listAllWebQuests() {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();

        List<WebQuest> list = (List<WebQuest>) session.createQuery("From WebQuest").list();

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
    public List<WebQuest> listWebQuestsByKey(String key) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();

        List<WebQuest> list = (List<WebQuest>) session.createQuery("From WebQuest").list();

        if (!key.equals("")) {
            String[] s;
            boolean bool;
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getKeywords() != null && !list.get(i).getKeywords().equals("") && list.get(i).isPublished()) {
                    bool = false;
                    s = list.get(i).getKeywords().split(";");
                    for (String item : s) {
                        if (item.equalsIgnoreCase(key)) {
                            bool = true;
                        }
                    }
                    if (!bool) {
                        list.remove(i);
                        i--;
                    }
                }else{
                    list.remove(i);
                        i--;
                }
            }
        }

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
    public List<WebQuest> listWebQuestsByTeam(Long team_id) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();

        List<WebQuest> list = (List<WebQuest>) session.createQuery("From WebQuest").list();

        boolean bool;
        for (int i = 0; i < list.size(); i++) {
            bool = false;
            for (int j = 0; j < list.get(i).getTeams().size(); j++) {
                if (list.get(i).getTeams().get(j).getId().equals(team_id)) {
                    bool = true;
                    break;
                }
            }
            if (!bool) {
                list.remove(i);
                i--;
            }
        }

        if (!transaction.wasCommitted()) {
            transaction.commit();
        }

        return list;
    }
}
