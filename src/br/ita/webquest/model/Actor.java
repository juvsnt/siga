package br.ita.webquest.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity(name = "Actor")
public class Actor implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private String act;
    private String type;
    private boolean complete;
    @ManyToOne
    private Activity activity;
    @ManyToOne
    private Phase phase;

    public Actor() {
        complete = false;
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

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public String getAct() {
        return act;
    }

    public void setAct(String act) {
        this.act = act;
    }

    public Phase getPhase() {
        return phase;
    }

    public void setPhase(Phase phase) {
        this.phase = phase;
    }

    @Override
    public String toString() {

        String text = "";

        text += "Actor id: " + id + "\n";
        text += "title: " + title + "\n";
        text += "act: " + act + "\n";
        text += "type: " + type + "\n";
        text += "complete: " + complete + "\n";
        
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
