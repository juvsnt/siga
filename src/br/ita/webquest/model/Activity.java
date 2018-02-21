package br.ita.webquest.model;

import br.ita.webquest.dao.ActorDaoImpl;
import br.ita.webquest.dao.ResourceDaoImpl;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;

@Entity(name = "Activity")
public class Activity implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    private String title;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date date_;
    private String learningObjectives;
    private String prerequisites;
    private String activityDescription;
    private String completeActivity;
    private boolean complete;
    @ManyToOne
    private Phase phase;
    @ManyToOne
    private WebQuest webQuest;
    @OneToMany(mappedBy = "activity")
    private List<Actor> actors;
    @OneToMany(mappedBy = "activity")
    private List<Resource> resources;

    public Activity() {
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

    public String getLearningObjectives() {
        return learningObjectives;
    }

    public void setLearningObjectives(String learningObjectives) {
        this.learningObjectives = learningObjectives;
    }

    public String getPrerequisites() {
        return prerequisites;
    }

    public void setPrerequisites(String prerequisites) {
        this.prerequisites = prerequisites;
    }

    public String getActivityDescription() {
        return activityDescription;
    }

    public void setActivityDescription(String activityDescription) {
        this.activityDescription = activityDescription;
    }

    public String getCompleteActivity() {
        return completeActivity;
    }

    public Date getDate() {
        return date_;
    }

    public void setDate(Date date_) {
//        java.util.Date dataUtil = new java.util.Date();  
//        java.sql.Date dataSql = new java.sql.Date(dataUtil.getTime());
        this.date_ = date_;
    }
    
    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public void setCompleteActivity(String completeActivity) {
        this.completeActivity = completeActivity;
    }

    public List<Actor> getActors() {
        return actors;
    }

    public void setActors(List<Actor> actors) {
        this.actors = actors;
    }

    public List<Resource> getResources() {
        return resources;
    }

    public void setResources(List<Resource> resources) {
        this.resources = resources;
    }

    public Phase getPhase() {
        return phase;
    }

    public void setPhase(Phase phase) {
        this.phase = phase;
    }

    public WebQuest getWebQuest() {
        return webQuest;
    }

    public void setWebQuest(WebQuest webQuest) {
        this.webQuest = webQuest;
    }

    @Override
    public String toString() {

        String text = "";

        text += "Activity id :"+ id + "\n";
        text += "title :"+ title + "\n";
        text += "date_ :"+ date_+"\n";
        text += "learningObjectives :"+ learningObjectives + "\n";
        text += "prerequisites :"+ prerequisites + "\n";
        text += "activityDescription :"+ activityDescription + "\n";
        text += "completeActivity :"+ completeActivity + "\n";
        text += "complete :"+ complete + "\n";
        text += "phase: "+ phase.getTitle() + "\n";
        text += "webQuest :"+ webQuest.getTitle() + "\n";

        actors = new ActorDaoImpl().listActorsByActivity(id);
        if (actors.size() > 0) {
            for (int i = 0; i < actors.size(); i++) {
                text += "actors: "+ actors.get(i).getTitle()+"\n";
            }
        } else {
            text += "0 actors\n";
        }
        
        resources = new ResourceDaoImpl().listResourcesByActivity(id);
        if (resources.size() > 0) {
            for (int i = 0; i < resources.size(); i++) {
                text += "resources: "+ resources.get(i).getTitle()+"\n";
            }
        } else {
            text += "0 resources\n";
        }

        return text;
    }
}
