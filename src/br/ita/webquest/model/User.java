package br.ita.webquest.model;

import br.ita.webquest.dao.EmailDaoImpl;
import br.ita.webquest.dao.ImageDaoImpl;
import br.ita.webquest.dao.TeamDaoImpl;
import br.ita.webquest.dao.WebQuestDaoImpl;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

@Entity(name="User")
public class User implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String image;
    private String email;
    private String description;
    private String password;
    private String user_level;
    @OneToMany(mappedBy = "user")
    private List<WebQuest> webQuests;
    @OneToMany(mappedBy = "user")
    private List<Image> images;
    @OneToMany(mappedBy = "user")
    private List<Email> emails;
    @OneToMany(mappedBy = "user")
    private List<Team> teams;
    @ManyToMany
    private List<Team> partTeams;
    @OneToMany(mappedBy = "user")
    private List<Party> parties;

    public User() {
        image = "no_image_64.png";
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    public List<Party> getParties() {
        return parties;
    }

    public void setParties(List<Party> parties) {
        this.parties = parties;
    }
    
    public List<Team> getPartTeams() {
        return partTeams;
    }

    public void setPartTeams(List<Team> partTeams) {
        this.partTeams = partTeams;
    }
    
    public List<Email> getEmails() {
        return emails;
    }

    public void setEmails(List<Email> emails) {
        this.emails = emails;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<WebQuest> getListWebQuests() {
        return webQuests;
    }

    public void setListWebQuests(List<WebQuest> listWebQuests) {
        this.webQuests = listWebQuests;
    }

    public List<Image> getListImages() {
        return images;
    }

    public void setListImages(List<Image> listImages) {
        this.images = listImages;
    }

    public String getUser_level() {
        return user_level;
    }

    public void setUser_level(String user_level) {
        this.user_level = user_level;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    //</editor-fold>
    
    @Override
    public String toString() {
        
        String text = "";
        
        text += "User id: "+id+"\n";
        text += "name: "+name+"\n";
        text += "email: "+email+"\n";
        text += "description: "+description+"\n";
        text += "user_level: "+user_level+"\n";
        
        webQuests = new WebQuestDaoImpl().listWebQuests(id);
        if (webQuests.size() > 0) {
            for (int i = 0; i < webQuests.size(); i++) {
                text += "WebQuests: "+ webQuests.get(i).getTitle()+"\n";
            }
        } else {
            text += "0 WebQuests\n";
        }
        
        images = new ImageDaoImpl().listImages(id);
        if (images.size() > 0) {
            for (int i = 0; i < images.size(); i++) {
                text += "images: "+ images.get(i).getName()+"\n";
            }
        } else {
            text += "0 images\n";
        }
        
        emails = new EmailDaoImpl().listEmails(id);
        if (emails.size() > 0) {
            for (int i = 0; i < emails.size(); i++) {
                text += "images: "+ emails.get(i).getEmail()+"\n";
            }
        } else {
            text += "0 emails\n";
        }
        
        teams = new TeamDaoImpl().listTeamsByUser(id);
        if (teams.size() > 0) {
            for (int i = 0; i < teams.size(); i++) {
                text += "teams: "+ teams.get(i).getName()+"\n";
            }
        } else {
            text += "0 teams\n";
        }
        
        partTeams = new TeamDaoImpl().listPartTeamsByUser(id);
        if (partTeams.size() > 0) {
            for (int i = 0; i < partTeams.size(); i++) {
                text += "partTeams: "+ partTeams.get(i).getName()+"\n";
            }
        } else {
            text += "0 partTeams\n";
        }
        
        return text;
    }
}
