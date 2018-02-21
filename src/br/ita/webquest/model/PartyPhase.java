package br.ita.webquest.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity(name = "PartyPhase")
public class PartyPhase implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    private Phase phase;
    private boolean complete;
    @ManyToOne
    private Party party;

    public PartyPhase() {
        complete = false;
    }

    public PartyPhase(Long id, Phase phase, boolean complete, Party party) {
        this.id = id;
        this.phase = phase;
        this.complete = complete;
        this.party = party;
    }

    public Long getId() {
        return id;
    }

    public Party getParty() {
        return party;
    }

    public Phase getPhase() {
        return phase;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPhase(Phase phase) {
        this.phase = phase;
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
        } catch (Exception e) {
            text += "Actor Title: " + e.getMessage() + "\n";
        }

        try {
            text += "Phase: \n" + phase.toString() + "\n";
        } catch (Exception e) {
            text += "Phase: " + e.getMessage() + "\n";
        }

        return text;
    }
}
