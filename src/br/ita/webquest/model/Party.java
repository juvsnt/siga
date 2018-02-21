package br.ita.webquest.model;

import br.ita.webquest.dao.PartyActivityDaoImpl;
import br.ita.webquest.dao.PartyPhaseDaoImpl;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity(name = "Party")
public class Party implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    private boolean complete;
    @ManyToOne
    private User user;
    private String actorTitle;
    @OneToMany(mappedBy = "party")
    private List<PartyPhase> partyPhases;
    @OneToMany(mappedBy = "party")
    private List<PartyActivity> partyActivities;

    public Party() {
        partyActivities = new ArrayList<PartyActivity>();
        partyPhases = new ArrayList<PartyPhase>();
    }

    public Party(Long id, User user, String actorTitle, List<PartyPhase> partyPhases, List<PartyActivity> partyActivities) {
        this.id = id;
        this.user = user;
        this.actorTitle = actorTitle;
        this.partyPhases = partyPhases;
        this.partyActivities = partyActivities;
    }

    public String getActorTitle() {
        return actorTitle;
    }

    public void setActorTitle(String actorName) {
        this.actorTitle = actorName;
    }

    public Long getId() {
        return id;
    }

    public void setUser(User User) {
        this.user = User;
    }

    public void setPartyPhases(List<PartyPhase> partyPhases) {
        this.partyPhases = partyPhases;
    }

    public void setPartyActivities(List<PartyActivity> partyActivities) {
        this.partyActivities = partyActivities;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public User getUser() {
        return user;
    }

    public List<PartyPhase> getPartyPhases() {
        return partyPhases;
    }

    public List<PartyActivity> getPartyActivities() {
        return partyActivities;
    }

    @Override
    public String toString() {
        
        String text = "";

        text += "Party id: " + id + "\n";
        text += "Actor Title: " + actorTitle + "\n";

        try {
            text += "User: " + user + "\n";
        } catch (Exception e) {
            text += "User: " + e.getMessage() + "\n";
        }

        partyPhases = new PartyPhaseDaoImpl().listPartyPhases(id);
        if (partyPhases.size() > 0) {
            for (int i = 0; i < partyPhases.size(); i++) {
                text += "Actor title: "+ partyPhases.get(i).getParty().getActorTitle()+"\n";
                text += "Phase title: "+ partyPhases.get(i).getPhase().getTitle()+"\n";
            }
        } else {
            text += "0 partyPhases\n";
        }
        
        partyActivities = new PartyActivityDaoImpl().listActivities(id);
        if (partyActivities.size() > 0) {
            for (int i = 0; i < partyActivities.size(); i++) {
                text += "Actor title: "+ partyActivities.get(i).getParty().getActorTitle()+"\n";
                text += "Activity title: "+ partyActivities.get(i).getActivity().getTitle()+"\n";
            }
        } else {
            text += "0 partyPhases\n";
        }

        return text;
    }
}

