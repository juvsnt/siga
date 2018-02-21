package br.ita.webquest.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity(name = "Email")
public class Email implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    private String email;
    private String user_level;
    private boolean registered;
    @ManyToOne
    private Team team;
    @ManyToOne
    private User user;

    public Email() {
        registered = false;
    }

    //<editor-fold defaultstate="collapsed" desc="getters-setters">
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUser_level() {
        return user_level;
    }

    public void setUser_level(String user_level) {
        this.user_level = user_level;
    }

    public boolean isRegistered() {
        return registered;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    //</editor-fold>

    @Override
    public String toString() {

        String text = "";

        text += "Email id: " + id + "\n";
        text += "email: " + email + "\n";
        text += "user: " + user.getName() + "\n";
        text += "user_level: " + user_level + "\n";
        text += "registered: " + registered + "\n";
        
        if (team != null) {
            text += "team: " + team.getName() + "\n";
        }
        
        return text;
    }
}
