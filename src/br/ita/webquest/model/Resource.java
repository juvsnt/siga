package br.ita.webquest.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity(name = "Resource")
public class Resource implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private String type;
    private String href;
    @ManyToOne
    private Activity activity;
    @ManyToOne
    private Phase phase;

    public Resource() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public Phase getPhase() {
        return phase;
    }

    public void setPhase(Phase phase) {
        this.phase = phase;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }
    
    @Override
    public String toString() {
        
        String text = "";
        
        text += "Resource id: "+id+"\n";
        text += "title: "+title+"\n";
        text += "type: "+type+"\n";
        text += "href: "+href+"\n";
        
        
        try {
            text += "activity: " + activity.getTitle() + "\n";
        } catch (Exception e) {
            text += "activity: " + e.getMessage() + "\n";
        }
        
        try {
            text += "phase: " + phase.getTitle() + "\n";
        } catch (Exception e) {
            text += "phase: " + e.getMessage() + "\n";
        }
        
        return text;
    }
}
