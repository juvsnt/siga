package br.ita.webquest.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity(name = "Outcome")
public class Outcome implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    private String description;
    @ManyToOne
    private Qualifying qualifying;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Qualifying getQualifying() {
        return qualifying;
    }

    public void setQualifying(Qualifying qualifying) {
        this.qualifying = qualifying;
    }
    
    @Override
    public String toString() {
        
        String text = "";
        
        text += "Outcome id: "+id+"\n";
        text += "qualifying: "+qualifying.getTitle()+"\n";
        text += "description: "+description+"\n";
        
        return text;
    }
}
