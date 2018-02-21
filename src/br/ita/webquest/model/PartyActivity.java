package br.ita.webquest.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity(name = "PartyActivity")
public class PartyActivity implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    @OneToOne
    private Activity activity;
    private boolean complete;
    @ManyToOne
    private Party party;

    public PartyActivity() {
    }

    public PartyActivity(Long id, Activity activity, boolean complete, Party party) {
        this.id = id;
        this.activity = activity;
        this.complete = complete;
        this.party = party;
    }

    public Activity getActivity() {
        return activity;
    }

    public Long getId() {
        return id;
    }

    public Party getParty() {
        return party;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setParty(Party party) {
        this.party = party;
    }

    @Override
    public String toString() {
        
        String text = "";

        text += "Party phase id: " + id + "\n";
        text += "complete: " + complete + "\n";

        try {
            text += "Actor Title: " + party.getActorTitle() + "\n";
            party.getPartyActivities().size();
            party.getPartyPhases().size();
        } catch (Exception e) {
            text += "Actor Title: " + e.getMessage() + "\n";
        }

        try {
            text += "Activity: " + activity.getTitle() + "\n";
            activity.getResources().size();
        } catch (Exception e) {
            text += "Activity: " + e.getMessage() + "\n";
        }

        return text;
    }    
}

