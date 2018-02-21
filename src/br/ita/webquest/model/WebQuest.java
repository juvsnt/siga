package br.ita.webquest.model;

import br.ita.webquest.dao.ActivityDaoImpl;
import br.ita.webquest.dao.PhaseDaoImpl;
import br.ita.webquest.dao.QualifyingDaoImpl;
import br.ita.webquest.dao.TeamDaoImpl;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity(name = "WebQuest")
public class WebQuest implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    private User user;
    private String title;
    private String version;
    private String template;
    private String keywords;
    private String introduction;
    private String taskTitle;
    private String task;
    private String process;
    private String credits;
    private String colaboration;
    private String evaluation;
    private String conclusion;
    private boolean published;
    private String prerequisites;
    private String learningObjectives;
    private String intellectualProperty;
    private String technicalPrerequisites;
    private String target;
    @OneToMany(mappedBy = "webQuest")
    private List<Phase> phases;
    @OneToMany(mappedBy = "webQuest")
    private List<Activity> activities;
    @OneToMany(mappedBy = "webQuest")
    private List<Qualifying> qualifiers;
    @ManyToMany(mappedBy = "webQuests")
    private List<Team> teams;

    public WebQuest() {
        published = false;
    }

    //<editor-fold defaultstate="collapsed" desc="getters-setters">
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

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getKeywords() {
        return keywords;
    }

    public String getCredits() {
        return credits;
    }

    public void setCredits(String credits) {
        this.credits = credits;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getColaboration() {
        return colaboration;
    }

    public void setColaboration(String colaboration) {
        this.colaboration = colaboration;
    }

    public String getIntellectualProperty() {
        return intellectualProperty;
    }

    public void setIntellectualProperty(String intellectualProperty) {
        this.intellectualProperty = intellectualProperty;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public String getTechnicalPrerequisites() {
        return technicalPrerequisites;
    }

    public void setTechnicalPrerequisites(String technicalPrerequisites) {
        this.technicalPrerequisites = technicalPrerequisites;
    }

    public String getPrerequisites() {
        return prerequisites;
    }

    public void setPrerequisites(String prerequisites) {
        this.prerequisites = prerequisites;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getConclusion() {
        return conclusion;
    }

    public void setConclusion(String conclusion) {
        this.conclusion = conclusion;
    }

    public String getLearningObjectives() {
        return learningObjectives;
    }

    public void setLearningObjectives(String learningObjectives) {
        this.learningObjectives = learningObjectives;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(String evaluation) {
        this.evaluation = evaluation;
    }

    public List<Phase> getPhases() {
        return phases;
    }

    public void setPhases(List<Phase> phases) {
        this.phases = phases;
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }

    public List<Qualifying> getQualifiers() {
        return qualifiers;
    }

    public void setQualifiers(List<Qualifying> qualifiers) {
        this.qualifiers = qualifiers;
    }

    //</editor-fold>
    @Override
    public String toString() {

        String text = "";

        text += "WebQuest id: " + id + "\n";
        text += "user: " + user.getName() + "\n";
        text += "title: " + title + "\n";
        text += "version: " + version + "\n";
        text += "template: " + template + "\n";
        text += "keywords: " + keywords + "\n";
        text += "introduction: " + introduction + "\n";
        text += "taskTitle: " + taskTitle + "\n";
        text += "task: " + task + "\n";
        text += "process: " + process + "\n";
        text += "credits: " + credits + "\n";
        text += "colaboration: " + colaboration + "\n";
        text += "evaluation: " + evaluation + "\n";
        text += "conclusion: " + conclusion + "\n";
        text += "published: " + published + "\n";
        text += "prerequisites: " + prerequisites + "\n";
        text += "learningObjectives: " + learningObjectives + "\n";
        text += "intellectualProperty: " + intellectualProperty + "\n";
        text += "technicalPrerequisites: " + technicalPrerequisites + "\n";
        text += "target: " + target + "\n";

        phases = new PhaseDaoImpl().listPhase(id);
        if (phases.size() > 0) {
            for (int i = 0; i < phases.size(); i++) {
                text += "phases: " + phases.get(i).getTitle() + "\n";
            }
        } else {
            text += "0 phases\n";
        }

        activities = new ActivityDaoImpl().listActivitiesByWebquest(id);
        if (activities.size() > 0) {
            for (int i = 0; i < activities.size(); i++) {
                text += "activities: " + activities.get(i).getTitle() + "\n";
            }
        } else {
            text += "0 activities\n";
        }

        qualifiers = new QualifyingDaoImpl().listQualifiers(id);
        if (qualifiers.size() > 0) {
            for (int i = 0; i < qualifiers.size(); i++) {
                text += "qualifiers: " + qualifiers.get(i).getTitle() + "\n";
            }
        } else {
            text += "0 qualifiers\n";
        }

        teams = new TeamDaoImpl().listTeamsByWebquest(id);
        if (teams.size() > 0) {
            for (int i = 0; i < teams.size(); i++) {
                text += "teams: " + teams.get(i).getName() + "\n";
            }
        } else {
            text += "0 teams\n";
        }

        return text;
    }
}
