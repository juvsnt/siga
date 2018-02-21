package br.ita.webquest.model;

import br.ita.webquest.dao.EmailDaoImpl;
import br.ita.webquest.dao.TeamDaoImpl;
import br.ita.webquest.dao.WebQuestDaoImpl;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity(name="Team")
public class Team implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @ManyToOne
    private User user;
    @ManyToMany(mappedBy = "partTeams")
    private List<User> users;
    @OneToMany(mappedBy = "team")
    private List<Email> emails;
    @ManyToMany
    private List<WebQuest> webQuests;

    public Team() {
    }
    
    //<editor-fold defaultstate="collapsed" desc="getters-setters">
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<WebQuest> getWebQuests() {
        return webQuests;
    }

    public void setWebQuests(List<WebQuest> webQuests) {
        this.webQuests = webQuests;
    }

    public List<Email> getEmails() {
        return emails;
    }

    public void setEmails(List<Email> emails) {
        this.emails = emails;
    }
    //</editor-fold>
    
    @Override
    public String toString() {
        
        String text = "";
        
        text += "Team id: "+id+"\n";
        text += "name: "+name+"\n";
        text += "user: "+user.getName()+"\n";
        
        users = new TeamDaoImpl().listUsersByTeam(id);
        if (users.size() > 0) {
            for (int i = 0; i < users.size(); i++) {
                text += "users: "+ users.get(i).getName()+"\n";
            }
        } else {
            text += "0 users\n";
        }
        
        emails = new EmailDaoImpl().listEmails(user.getId());
        if (emails.size() > 0) {
            for (int i = 0; i < emails.size(); i++) {
                text += "emails: "+ emails.get(i).getEmail()+"\n";
            }
        } else {
            text += "0 emails\n";
        }
        
        webQuests = new WebQuestDaoImpl().listWebQuestsByTeam(id);
        if (webQuests.size() > 0) {
            for (int i = 0; i < webQuests.size(); i++) {
                text += "webQuests: "+ webQuests.get(i).getTitle()+"\n";
            }
        } else {
            text += "0 webQuests\n";
        }
        
        return text;
    }
}
