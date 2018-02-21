package br.ita.webquest.bean;

import br.ita.webquest.dao.ActivityDaoImpl;
import br.ita.webquest.dao.ActorDao;
import br.ita.webquest.dao.ActorDaoImpl;
import br.ita.webquest.dao.PhaseDaoImpl;
import br.ita.webquest.dao.ResourceDao;
import br.ita.webquest.dao.ResourceDaoImpl;
import br.ita.webquest.dao.WebQuestDaoImpl;
import br.ita.webquest.model.Activity;
import br.ita.webquest.model.Actor;
import br.ita.webquest.model.Phase;
import br.ita.webquest.model.Resource;
import java.util.ArrayList;
import java.util.List;
import javax.el.ELContext;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class ActivityBean {

    private Activity activity;
    //private Activity activityRun;
    private String addActor;
    private Phase phaseRun;
    private String addResource;
    private List<String> actors;
    private List<Long> actorsId;
    private List<String> resources;
    private List<Long> resourcesId;
    private boolean operacaoOK;

    public ActivityBean() {
    }

    //<editor-fold defaultstate="collapsed" desc="getters-setters">
    public Activity getActivity() {
        if (activity == null) {
            activity = new Activity();
        }
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public boolean isOperacaoOK() {
        return operacaoOK;
    }

    public void setOperacaoOK(boolean operacaoOK) {
        this.operacaoOK = operacaoOK;
    }

    public String getAddActor() {
        return addActor;
    }

    public void setAddActor(String addActor) {
        this.addActor = addActor;
    }

    public String getAddResource() {
        return addResource;
    }

    public void setAddResource(String addResource) {
        this.addResource = addResource;
    }

    public Phase getPhaseRun() {
        return phaseRun;
    }

    public void setPhaseRun(Phase phaseRun) {
        this.phaseRun = phaseRun;
    }
    
    public List<String> getActors() {
        return actors;
    }

    public void setActors(List<String> actors) {
        this.actors = actors;
    }

    public List<Long> getActorsId() {
        return actorsId;
    }

    public void setActorsId(List<Long> actorsId) {
        this.actorsId = actorsId;
    }

    public List<Long> getResourcesId() {
        return resourcesId;
    }

    public void setResourcesId(List<Long> resourcesId) {
        this.resourcesId = resourcesId;
    }

    public List<String> getResources() {
        return resources;
    }

    public void setResources(List<String> resources) {
        this.resources = resources;
    }

    public List<Activity> getListActivities() {

        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        PhaseBean phaseBean = (PhaseBean) FacesContext
                .getCurrentInstance().getApplication().getELResolver()
                .getValue(elContext, null, "phaseBean");

        return new ActivityDaoImpl().listActivities(phaseBean.getPhase().getId());
    }
    
    public List<Resource> getResourcesR(){
         return activity.getResources();
    }
    
    //</editor-fold>

    public String prepareRegisterActivity() {

        Long phase_id = Long.parseLong(FacesContext.getCurrentInstance()
                .getExternalContext().getRequestParameterMap().get("id"));

        try {
            activity = new Activity();

            setOperacaoOK(true);

            System.out.println("################################################"
                    + "prepareRegisterActivity");

            registerActivity(phase_id);

            return "edit-process-activity?faces-redirect=true";

        } catch (Exception e) {
            setOperacaoOK(false);
        }
        return null;
    }

    public void registerActivity(Long id) {

        try {

            activity.setPhase(new PhaseDaoImpl().getPhase(id));
            activity.setWebQuest(new WebQuestDaoImpl().getWebQuest(activity.getPhase().getWebQuest().getId()));

            new ActivityDaoImpl().save(activity);

            FacesMessage msg = new FacesMessage();
            msg.setSeverity(FacesMessage.SEVERITY_INFO);
            msg.setSummary("Sucesso");
            msg.setDetail("Atividade registrada com sucesso");

            FacesContext.getCurrentInstance().addMessage(null, msg);

            setOperacaoOK(true);

        } catch (Exception e) {

            FacesMessage msg = new FacesMessage();
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            msg.setSummary("Erro");
            msg.setDetail("Não foi possível registrar a atividade. " + e.getMessage());

            setOperacaoOK(false);
        }
    }

    public void isComplete() {

        try {

            activity.setComplete(false);

            if (activity.getActivityDescription() == null || activity.getActivityDescription().equals("")) {
                return;
            }
            if (activity.getLearningObjectives() == null || activity.getLearningObjectives().equals("")) {
                return;
            }
            if (activity.getPrerequisites() == null || activity.getPrerequisites().equals("")) {
                return;
            }
            if (activity.getActors() == null || activity.getActors().isEmpty()) {
                return;
            }
            if (activity.getResources() == null || activity.getResources().isEmpty()) {
                return;
            }
            if (activity.getCompleteActivity() == null) {
                return;
            }
            activity.setComplete(true);

            setOperacaoOK(true);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            setOperacaoOK(false);
        }
    }
    
     public String run() {

        Long activity_id = Long.parseLong(FacesContext.getCurrentInstance()
                .getExternalContext().getRequestParameterMap().get("id"));

        try {
            activity = new ActivityDaoImpl().getActivity(activity_id);

            //System.out.println(staff.getName());
            setOperacaoOK(true);

            return "run-activity?faces-redirect=true";

        } catch (Exception e) {
            setOperacaoOK(false);
        }

        return null;
    }

    public void updateActivity() {

        try {

            isComplete();

            new ActivityDaoImpl().update(activity);

            FacesMessage msg = new FacesMessage();
            msg.setSeverity(FacesMessage.SEVERITY_INFO);
            msg.setSummary("Sucesso");
            msg.setDetail("Dados alterados com sucesso");

            FacesContext.getCurrentInstance().addMessage(null, msg);

            setOperacaoOK(true);

        } catch (Exception e) {

            FacesMessage msg = new FacesMessage();
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            msg.setSummary("Erro");
            msg.setDetail("Não foi possível realizar a alteração dos dados. " + e.getMessage());

            setOperacaoOK(false);
        }
    }

    public String prepareEdit() {

        Long activity_id = Long.parseLong(FacesContext.getCurrentInstance()
                .getExternalContext().getRequestParameterMap().get("id"));

        try {

            activity = new ActivityDaoImpl().getActivity(activity_id);
            
            actors = new ArrayList<String>();
            actorsId = new ArrayList<Long>();
            resources = new ArrayList<String>();
            resourcesId = new ArrayList<Long>();

            List<Actor> listActors = new ActorDaoImpl().listActorsByPhase(activity.getPhase().getId());
            List<Resource> listResources = new ResourceDaoImpl().listResourcesByPhase(activity.getPhase().getId());

            for (int i = 0; i < listActors.size(); i++) {
                actors.add(listActors.get(i).getTitle());
                actorsId.add(listActors.get(i).getId());
            }

            for (int i = 0; i < listResources.size(); i++) {
                resources.add(listResources.get(i).getTitle());
                resourcesId.add(listResources.get(i).getId());
            }

            setOperacaoOK(true);

            return "edit-process-activity?faces-redirect=true";

        } catch (Exception e) {
            setOperacaoOK(false);
        }

        return null;
    }

    public void addNewActor() {

        try {

            long id = 0;
            for (int i = 0; i < actors.size(); i++) {
                if(actors.get(i).equals(addActor)){
                    id = actorsId.get(i);
                }
            }
            
            ActorDao dao = new ActorDaoImpl();

            Actor actor1 = new Actor();
            Actor actor2 = dao.getActor(id);
            actor1.setTitle(actor2.getTitle());
            actor1.setType(actor2.getType());
            actor1.setAct(actor2.getAct());

            actor1.setActivity(activity);
            dao.save(actor1);

            activity.getActors().add(actor1);

            setOperacaoOK(true);

        } catch (Exception e) {
            setOperacaoOK(false);
        }
    }

    public void addNewResource() {

        try {

            long id = 0;
            for (int i = 0; i < resources.size(); i++) {
                if(resources.get(i).equals(addResource)){
                    id = resourcesId.get(i);
                }
            }
            
            ResourceDao dao = new ResourceDaoImpl();

            Resource rec1 = new Resource();
            Resource rec2 = dao.getResource(id);
            rec1.setTitle(rec2.getTitle());
            rec1.setType(rec2.getType());
            rec1.setHref(rec2.getHref());

            rec1.setActivity(activity);
            dao.save(rec1);

            activity.getResources().add(rec1);
            
            setOperacaoOK(true);

        } catch (Exception e) {
            setOperacaoOK(false);
        }
    }

    public void prepareRemove() {

        Long activity_id = Long.parseLong(FacesContext.getCurrentInstance()
                .getExternalContext().getRequestParameterMap().get("id"));

        try {
            activity = new ActivityDaoImpl().getActivity(activity_id);

            System.out.println("################################################"
                    + "prepareRemove id  " + activity_id + "title " + activity.getTitle());

            removeActivity();

            setOperacaoOK(true);

        } catch (Exception e) {
            setOperacaoOK(false);
        }
    }

    public void removeActivity() {
        try {

            new ActivityDaoImpl().remove(activity);

            FacesMessage msg = new FacesMessage();
            msg.setSeverity(FacesMessage.SEVERITY_INFO);
            msg.setSummary("Sucesso");
            msg.setDetail("Atividade removida com sucesso");

            FacesContext.getCurrentInstance().addMessage(null, msg);

            setOperacaoOK(true);

        } catch (Exception e) {

            FacesMessage msg = new FacesMessage();
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            msg.setSummary("Erro");
            msg.setDetail("Não foi possível remover a atividade. " + e.getMessage());

            setOperacaoOK(false);
        }
    }
    
    public void prepareRemoveActor() {

        Long actor_id = Long.parseLong(FacesContext.getCurrentInstance()
                .getExternalContext().getRequestParameterMap().get("id"));

        try {
            Actor actor = new ActorDaoImpl().getActor(actor_id);

            removeActor(actor);

            setOperacaoOK(true);

        } catch (Exception e) {
            setOperacaoOK(false);
        }
    }

    public void removeActor(Actor actor) {
        try {

            new ActorDaoImpl().remove(actor);
            
            for (int i = 0; i < activity.getActors().size(); i++) {
                if(activity.getActors().get(i).getId().equals(actor.getId())){
                    activity.getActors().remove(i);
                }
            }
            
            updateActivity();
            
            setOperacaoOK(true);
            
        } catch (Exception e) {
            setOperacaoOK(false);
        }
    }
    
    public void prepareRemoveResource() {

        Long resource_id = Long.parseLong(FacesContext.getCurrentInstance()
                .getExternalContext().getRequestParameterMap().get("id"));

        try {
            Resource resource = new ResourceDaoImpl().getResource(resource_id);

            removeResource(resource);

            setOperacaoOK(true);

        } catch (Exception e) {
            setOperacaoOK(false);
        }
    }

    public void removeResource(Resource resource) {
        try {

            new ResourceDaoImpl().remove(resource);
            
            for (int i = 0; i < activity.getResources().size(); i++) {
                if(activity.getResources().get(i).getId().equals(resource.getId())){
                    activity.getResources().remove(i);
                }
            }
            
            updateActivity();
            
            setOperacaoOK(true);
            
        } catch (Exception e) {
            setOperacaoOK(false);
        }
    }
}
