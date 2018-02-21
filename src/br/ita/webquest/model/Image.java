package br.ita.webquest.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity(name="Image")
public class Image implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @ManyToOne
    private User user;

    public Image() {
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
    //</editor-fold>
    
    @Override
    public String toString() {
        
        String text = "";
        
        text += "Image id: "+id+"\n";
        text += "name: "+name+"\n";
        text += "user: "+user.getName()+"\n";
        
        return text;
    }
}
