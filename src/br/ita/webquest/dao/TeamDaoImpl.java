package br.ita.webquest.dao;

import br.ita.webquest.model.Email;
import br.ita.webquest.model.Team;
import br.ita.webquest.model.User;
import br.ita.webquest.model.WebQuest;
import br.ita.webquest.util.HibernateUtil;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class TeamDaoImpl implements TeamDao {

    @Override
    public Team getTeam(Long id) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        
        Team team = (Team) session.load(Team.class, id);

//        System.out.println(team);
        team.toString();

        if (!transaction.wasCommitted()) {
            transaction.commit();
        }

        return team;
    }

    @Override
    public Team getTeamByUser(Long user_id) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();

        List list = session.createQuery("from Team where user_id = '" + user_id + "'").list();

        Team team = null;
        if (list.size() > 0) {
            team = (Team) list.get(0);
        }
        
//        System.out.println(team);
        team.toString();
        
        if (!transaction.wasCommitted()) {
            transaction.commit();
        }

        return team;
    }

    @Override
    public void save(Team team) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();

        session.save(team);

        if (!transaction.wasCommitted()) {
            transaction.commit();
        }
    }

    @Override
    public void remove(Team team) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();

        session.delete(team);

        if (!transaction.wasCommitted()) {
            transaction.commit();
        }

    }

    @Override
    public void update(Team team) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();

        session.update(team);

        if (!transaction.wasCommitted()) {
            transaction.commit();
        }
    }

    @Override
    public List<Team> listTeams() {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();

        List<Team> list = session.createQuery("From Team").list();

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
    public List<Team> listTeamsByUser(Long user_id) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();

        List<Team> list = session.createQuery("from Team where user_id = '" + user_id + "'").list();

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
     public List<Team> listTeamsByWebquest(Long webQuest_id) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();

        List<Team> list = session.createQuery("from Team").list();
        boolean bool;
        for (int i = 0; i < list.size(); i++) {
            bool = false;
            for (int j = 0; j < list.get(i).getWebQuests().size(); j++) {
                if (list.get(i).getWebQuests().get(j).getId().equals(webQuest_id)) {
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
    
    @Override
     public List<Team> listPartTeamsByUser(Long user_id) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();

        List<Team> list = session.createQuery("from Team").list();
        boolean bool;
        for (int i = 0; i < list.size(); i++) {
            bool = false;
            for (int j = 0; j < list.get(i).getUsers().size(); j++) {
                if (list.get(i).getUsers().get(j).getId().equals(user_id)) {
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

    @Override
    public List<User> listUsersByTeam(Long team_id) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();

        List<User> list = session.createQuery("from User").list();
        boolean bool;
        for (int i = 0; i < list.size(); i++) {
            bool = false;
            for (int j = 0; j < list.get(i).getPartTeams().size(); j++) {
                if (list.get(i).getPartTeams().get(j).getId().equals(team_id)) {
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

    @Override
    public List<Email> listEmailsByTeam(Long team_id) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();

        List<Email> list = session.createQuery("from Email where team_id = '" + team_id + "'").list();

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
