package br.ita.webquest.bean;

import br.ita.webquest.dao.ActivityDaoImpl;
import br.ita.webquest.dao.ActorDaoImpl;
import br.ita.webquest.dao.PhaseDaoImpl;
import br.ita.webquest.model.Activity;
import br.ita.webquest.model.Actor;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.el.ELContext;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

@ManagedBean
@SessionScoped
public class ActorBean {

    private Actor actor;
    private List<String> actorType;
    private List<Actor> actors;
    private boolean operacaoOK;

    @PostConstruct
    public void init() {

        actorType = new ArrayList<String>();

        actorType.add("Aluno");
        actorType.add("Professor");
    }

    public ActorBean() {
    }

    //<editor-fold defaultstate="collapsed" desc="getters-setters">
    public Actor getActor() {
        if (actor == null) {
            actor = new Actor();
        }
        return actor;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
    }

    public boolean isOperacaoOK() {
        return operacaoOK;
    }

    public void setOperacaoOK(boolean operacaoOK) {
        this.operacaoOK = operacaoOK;
    }

    public List<String> getActorType() {
        return actorType;
    }

    public void setActorType(List<String> actorType) {
        this.actorType = actorType;
    }

    public List<Actor> getListActorsByActivity() {

        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        ActivityBean activityBean = (ActivityBean) FacesContext
                .getCurrentInstance().getApplication().getELResolver()
                .getValue(elContext, null, "activityBean");

        return new ActorDaoImpl().listActorsByActivity(activityBean.getActivity().getId());
    }

    public List<Actor> getListActorsByPhase() {

        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        PhaseBean phaseBean = (PhaseBean) FacesContext
                .getCurrentInstance().getApplication().getELResolver()
                .getValue(elContext, null, "phaseBean");

        return new ActorDaoImpl().listActorsByPhase(phaseBean.getPhase().getId());
    }
    //</editor-fold>

    public void prepareRegisterActor(ActionEvent actionEvent) {

        try {
            actor = new Actor();

            setOperacaoOK(true);

        } catch (Exception e) {
            setOperacaoOK(false);
        }
    }

    public void prepareAddActor(ActionEvent actionEvent) {

        try {
            actor = new Actor();

            setOperacaoOK(true);

        } catch (Exception e) {
            setOperacaoOK(false);
        }
    }

    public void registerActor() {

        Long phase_id = Long.parseLong(FacesContext.getCurrentInstance()
                .getExternalContext().getRequestParameterMap().get("id"));

        try {
            actor.setPhase(new PhaseDaoImpl().getPhase(phase_id));

            new ActorDaoImpl().save(actor);

            FacesMessage msg = new FacesMessage();
            msg.setSeverity(FacesMessage.SEVERITY_INFO);
            msg.setSummary("Sucesso");
            msg.setDetail("Ator registrado com sucesso");

            FacesContext.getCurrentInstance().addMessage(null, msg);

            setOperacaoOK(true);

        } catch (Exception e) {
            setOperacaoOK(false);
        }
    }

    public void isComplete() {

        try {

            actor.setComplete(true);

            if (actor.getAct() == null || actor.getAct().equals("")) {
                actor.setComplete(false);
                return;
            }

            setOperacaoOK(true);

        } catch (Exception e) {
            setOperacaoOK(false);
        }
    }

    public String updateActor() {

        try {

            isComplete();

            new ActorDaoImpl().update(actor);

            for (int i = 0; i < actors.size(); i++) {
                actors.get(i).setTitle(actor.getTitle());
                actors.get(i).setAct(actor.getAct());
                actors.get(i).setType(actor.getType());

                new ActorDaoImpl().update(actors.get(i));
            }
            
            actors.clear();

            return "edit-task-roles?faces-redirect=true";

        } catch (Exception e) {
            setOperacaoOK(false);
        }

        return null;
    }

    public String prepareEdit() {

        Long actor_id = Long.parseLong(FacesContext.getCurrentInstance()
                .getExternalContext().getRequestParameterMap().get("id"));

        try {

            actors = new ArrayList<Actor>();

            actor = new ActorDaoImpl().getActor(actor_id);

            List<Actor> listActors;
            List<Activity> listActivities = new ActivityDaoImpl().listActivitiesByPhase(actor.getPhase().getId());

            for (int i = 0; i < listActivities.size(); i++) {
                listActors = new ActorDaoImpl().listActorsByActivity(listActivities.get(i).getId());

                for (int j = 0; j < listActors.size(); j++) {
                    if (listActors.get(j).getTitle().equals(actor.getTitle())) {
                        actors.add(listActors.get(j));
                    }
                }
            }

            setOperacaoOK(true);

            return "edit-task-role?faces-redirect=true";

        } catch (Exception e) {
            setOperacaoOK(false);
        }

        return null;
    }

    public void prepareRemove() {

        Long actor_id = Long.parseLong(FacesContext.getCurrentInstance()
                .getExternalContext().getRequestParameterMap().get("id"));

        try {
            actor = new ActorDaoImpl().getActor(actor_id);

            setOperacaoOK(true);

            removeActor();

        } catch (Exception e) {
            setOperacaoOK(false);
        }
    }

    public void removeActor() {
        try {

            new ActorDaoImpl().remove(actor);

            setOperacaoOK(true);

            System.out.println("################################################"
                    + "removeActor  actor.getId() " + actor.getId());

            FacesMessage msg = new FacesMessage();
            msg.setSeverity(FacesMessage.SEVERITY_INFO);
            msg.setSummary("Sucesso");
            msg.setDetail("Ator Removido");

            FacesContext.getCurrentInstance().addMessage(null, msg);

        } catch (Exception e) {
            setOperacaoOK(false);
        }
    }
}
