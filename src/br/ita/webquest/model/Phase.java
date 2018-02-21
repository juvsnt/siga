package br.ita.webquest.model;

import br.ita.webquest.dao.ActivityDaoImpl;
import br.ita.webquest.dao.ActorDaoImpl;
import br.ita.webquest.dao.ResourceDaoImpl;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity(name = "Phase")
public class Phase implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private String descriprion;
    private boolean completeTask;
    private boolean completeProcess;
    @ManyToOne
    private WebQuest webQuest;
    @OneToMany(mappedBy = "phase")
    private List<Activity> activityies;
    @OneToMany(mappedBy = "phase")
    private List<Actor> actors;
    @OneToMany(mappedBy = "phase")
    private List<Resource> resources;
    @OneToMany(mappedBy = "phase")
    private List<PartyPhase> partyPhases;

    public Phase() {
        completeTask = false;
        completeProcess = false;
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

    public List<Activity> getActivityies() {
        return activityies;
    }

    public void setActivityies(List<Activity> activityies) {
        this.activityies = activityies;
    }

    public String getDescriprion() {
        return descriprion;
    }

    public void setDescriprion(String descriprion) {
        this.descriprion = descriprion;
    }

    public void setPartyPhases(List<PartyPhase> partyPhases) {
        this.partyPhases = partyPhases;
    }

    public List<PartyPhase> getPartyPhases() {
        return partyPhases;
    }

    public boolean isCompleteTask() {
        return completeTask;
    }

    public void setCompleteTask(boolean completeTask) {
        this.completeTask = completeTask;
    }

    public boolean isCompleteProcess() {
        return completeProcess;
    }

    public void setCompleteProcess(boolean completeProcess) {
        this.completeProcess = completeProcess;
    }

    public WebQuest getWebQuest() {
        return webQuest;
    }

    public void setWebQuest(WebQuest webQuest) {
        this.webQuest = webQuest;
    }
    
    public List<Resource> getResources() {
        return resources;
    }

    public void setResources(List<Resource> resources) {
        this.resources = resources;
    }
    
    public List<Actor> getActors() {
        return actors;
    }

    public void setActors(List<Actor> actors) {
        this.actors = actors;
    }
    
    @Override
    public String toString() {
        
        String text = "";
        
        text += "Phase id: "+id+"\n";
        text += "title: "+title+"\n";
        text += "descriprion: "+descriprion+"\n";
        text += "completeTask: "+completeTask+"\n";
        text += "completeProcess: "+completeProcess+"\n";
        text += "webQuest: "+webQuest.getTitle()+"\n";
        
        activityies = new ActivityDaoImpl().listActivities(id);
        if (activityies.size() > 0) {
            for (int i = 0; i < activityies.size(); i++) {
                text += "activityies: "+ activityies.get(i).getTitle()+"\n";
            }
        } else {
            text += "0 activityies\n";
        }
        
        actors = new ActorDaoImpl().listActorsByPhase(id);
        if (actors.size() > 0) {
            for (int i = 0; i < actors.size(); i++) {
                text += "actors: "+ actors.get(i).getTitle()+"\n";
            }
        } else {
            text += "0 actors\n";
        }
        
        resources = new ResourceDaoImpl().listResourcesByPhase(id);
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
