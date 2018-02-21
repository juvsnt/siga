package br.ita.webquest.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity(name = "Qualifying")
public class Qualifying implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private String description;
    @ManyToOne
    private WebQuest webQuest;
    @OneToMany(mappedBy = "qualifying")
    private List<Outcome> outcomes;

    public Qualifying() {
        Outcome outcome;

        outcomes = new ArrayList<Outcome>();

        outcome = new Outcome();
        outcome.setDescription("Descrição 1");
        outcomes.add(outcome);

        outcome = new Outcome();
        outcome.setDescription("Descrição 2");
        outcomes.add(outcome);

        outcome = new Outcome();
        outcome.setDescription("Descrição 3");
        outcomes.add(outcome);

        outcome = new Outcome();
        outcome.setDescription("Descrição 4");
        outcomes.add(outcome);
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public WebQuest getWebQuest() {
        return webQuest;
    }

    public void setWebQuest(WebQuest webQuest) {
        this.webQuest = webQuest;
    }

    public List<Outcome> getOutcomes() {
        return outcomes;
    }

    public void setOutcomes(List<Outcome> outcomes) {
        this.outcomes = outcomes;
    }

    @Override
    public String toString() {

        String text = "";

        text += "Qualifying id: " + id + "\n";
        text += "title: " + title + "\n";
        text += "description: " + description + "\n";
        text += "webQuest: " + webQuest.getTitle() + "\n";

        if (outcomes.size() > 0) {
            for (int i = 0; i < outcomes.size(); i++) {
                text += "outcomes: " + outcomes.get(i).getDescription()+ "\n";
            }
        } else {
            text += "0 outcomes\n";
        }

        return text;
    }
}
