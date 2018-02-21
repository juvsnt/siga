package br.ita.webquest.bean;

import br.ita.webquest.dao.ActivityDao;
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
import javax.faces.event.ActionEvent;

@ManagedBean
@SessionScoped
public class PhaseBean {

    private Phase phase;
    private Actor actorRun;
    private boolean operacaoOK;

    public PhaseBean() {
    }

    //<editor-fold defaultstate="collapsed" desc="getters-setters">
    public Phase getPhase() {
        if (phase == null) {
            phase = new Phase();
        }
        return phase;
    }

    public void setPhase(Phase phase) {
        this.phase = phase;
    }

    public void setActorRun(Actor actorRun) {
        this.actorRun = actorRun;
    }

    public Actor getActorRun() {
        return actorRun;
    }

    public boolean isOperacaoOK() {
        return operacaoOK;
    }

    public void setOperacaoOK(boolean operacaoOK) {
        this.operacaoOK = operacaoOK;
    }

    public List<Phase> getListPhases() {

        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        WebQuestBean webQuestBean = (WebQuestBean) FacesContext
                .getCurrentInstance().getApplication().getELResolver()
                .getValue(elContext, null, "webQuestBean");

        return new PhaseDaoImpl().listPhase(webQuestBean.getWebQuest().getId());
    }

    public List<Phase> getListPhasesByActor() {

        List<Phase> listR = new ArrayList<Phase>();
        List<Phase> list = getListPhases();

        for (int i = 0; i < list.size(); i++) {
//        System.out.println(list.get(i).getTitle());
//        System.out.println(list.get(i).getActors().size());
            for (int j = 0; j < list.get(i).getActors().size(); j++) {
                if (list.get(i).getActors().get(j).getTitle().equalsIgnoreCase(actorRun.getTitle())) {
                    listR.add(list.get(i));
                    break;
                }
            }
        }

        return listR;
    }

    public List<Activity> getListActivityByActor() {

        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        ActivityBean activityBean = (ActivityBean) FacesContext
                .getCurrentInstance().getApplication().getELResolver()
                .getValue(elContext, null, "activityBean");

        List<Activity> listR = new ArrayList<Activity>();
        List<Activity> list = new ActivityDaoImpl().listActivitiesByPhase(activityBean.getPhaseRun().getId());

        for (int i = 0; i < list.size(); i++) {
//        System.out.println(list.get(i).getTitle());
//        System.out.println(list.get(i).getActors().size());
            for (int j = 0; j < list.get(i).getActors().size(); j++) {
                if (list.get(i).getActors().get(j).getTitle().equalsIgnoreCase(actorRun.getTitle())) {
                    listR.add(list.get(i));
                    break;
                }
            }
        }

        return listR;
    }
    //</editor-fold>

    public void prepareRegisterPhase(ActionEvent actionEvent) {

        try {
            phase = new Phase();

            setOperacaoOK(true);

        } catch (Exception e) {
            setOperacaoOK(false);
        }
    }

    public void registerPhase() {

        Long webQuest_id = Long.parseLong(FacesContext.getCurrentInstance()
                .getExternalContext().getRequestParameterMap().get("id"));

        try {

            phase.setWebQuest(new WebQuestDaoImpl().getWebQuest(webQuest_id));

            new PhaseDaoImpl().save(phase);

            FacesMessage msg = new FacesMessage();
            msg.setSeverity(FacesMessage.SEVERITY_INFO);
            msg.setSummary("Sucesso");
            msg.setDetail("Fase registrada com sucesso. Próximos passos ");

            FacesContext.getCurrentInstance().addMessage(null, msg);

            setOperacaoOK(true);

        } catch (Exception e) {

            FacesMessage msg = new FacesMessage();
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            msg.setSummary("Erro");
            msg.setDetail("Não foi possível registrar a fase. " + e.getMessage());

            setOperacaoOK(false);
        }
    }
    
    public String selectPhase() {

        Long phase_id = Long.parseLong(FacesContext.getCurrentInstance()
                .getExternalContext().getRequestParameterMap().get("id"));

        try {

            phase = new PhaseDaoImpl().getPhase(phase_id);
            
            System.out.println("################################################"
                    + "select phase id  " + phase_id + "title " + phase.getTitle());

            setOperacaoOK(true);

           return "processb?faces-redirect=true";

        } catch (Exception e) {

            FacesMessage msg = new FacesMessage();
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            msg.setSummary("Erro");
            msg.setDetail("Não foi possível selecionar a fase. " + e.getMessage());

            setOperacaoOK(false);
            
           return null;
        }
    }

    public void isCompleteTask() {

        try {

            phase.setCompleteTask(true);

            if (phase.getDescriprion() == null || phase.getDescriprion().equals("")) {
                phase.setCompleteTask(false);
                return;
            }

            if (phase.getActors() == null || phase.getActors().isEmpty()) {
                phase.setCompleteTask(false);
                return;
            }

            if (phase.getResources() == null || phase.getResources().isEmpty()) {
                phase.setCompleteTask(false);
                return;
            }

            setOperacaoOK(true);

        } catch (Exception e) {
            setOperacaoOK(false);
        }
    }

    public String isCompleteProcess() {

        try {

            phase.setCompleteProcess(false);

            List<Activity> list = new ActivityDaoImpl().listActivities(phase.getId());
            for (int i = 0; i < list.size(); i++) {
                if (!list.get(i).isComplete()) {
                    new PhaseDaoImpl().update(phase);
                    return null;
                }
            }

            phase.setCompleteProcess(true);

            new PhaseDaoImpl().update(phase);

            setOperacaoOK(true);

            return "edit-process-activities?faces-redirect=true";

        } catch (Exception e) {
            setOperacaoOK(false);
        }

        return null;
    }

    public String updatePhase() {

        try {

            isCompleteTask();

            new PhaseDaoImpl().update(phase);

            FacesMessage msg = new FacesMessage();
            msg.setSeverity(FacesMessage.SEVERITY_INFO);
            msg.setSummary("Sucesso");
            msg.setDetail("Dados alterados com sucesso");

            FacesContext.getCurrentInstance().addMessage(null, msg);

            setOperacaoOK(true);

            return "edit-task-phases?faces-redirect=true";

        } catch (Exception e) {

            FacesMessage msg = new FacesMessage();
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            msg.setSummary("Erro");
            msg.setDetail("Não foi possível realizar a alteração dos dados. " + e.getMessage());

            setOperacaoOK(false);
        }

        return null;
    }

    public String prepareEdit() {

        Long phase_id = Long.parseLong(FacesContext.getCurrentInstance()
                .getExternalContext().getRequestParameterMap().get("id"));

        int case_n = Integer.parseInt(FacesContext.getCurrentInstance()
                .getExternalContext().getRequestParameterMap().get("case"));

        try {

            phase = new PhaseDaoImpl().getPhase(phase_id);

            setOperacaoOK(true);

            switch (case_n) {
                case 0:
                    return "edit-task-phase?faces-redirect=true";
                case 1:
                    return "edit-task-resources?faces-redirect=true";
                case 2:
                    return "edit-task-roles?faces-redirect=true";
                default:
                    throw new AssertionError();
            }

        } catch (Exception e) {
            setOperacaoOK(false);
        }

        return null;
    }

    public String prepareEditActivities() {

        Long phase_id = Long.parseLong(FacesContext.getCurrentInstance()
                .getExternalContext().getRequestParameterMap().get("id"));

        try {

            phase = new PhaseDaoImpl().getPhase(phase_id);

            setOperacaoOK(true);

            return "edit-process-activities?faces-redirect=true";

        } catch (Exception e) {
            setOperacaoOK(false);
        }

        return null;
    }

    public void prepareRemove() {

        Long phase_id = Long.parseLong(FacesContext.getCurrentInstance()
                .getExternalContext().getRequestParameterMap().get("id"));

        try {
            phase = new PhaseDaoImpl().getPhase(phase_id);

            setOperacaoOK(true);

            System.out.println("################################################"
                    + "prepareRemove id  " + phase_id + "title " + phase.getTitle());

            removePhase();

        } catch (Exception e) {
            setOperacaoOK(false);
        }
    }
    
    public void first() {

        Long webquest_id = Long.parseLong(FacesContext.getCurrentInstance()
                .getExternalContext().getRequestParameterMap().get("id"));

        try {
            phase = new WebQuestDaoImpl().getWebQuest(webquest_id).getPhases().get(0);

            setOperacaoOK(true);

            System.out.println("################################################"
                    + "primeira id  " + webquest_id + "title " + phase.getTitle());

        } catch (Exception e) {
            setOperacaoOK(false);
        }
    }

    public void removePhase() {
        try {

            ActivityDao daoActi = new ActivityDaoImpl();
            ActorDao daoActo = new ActorDaoImpl();
            ResourceDao daoRes = new ResourceDaoImpl();

            List<Activity> listActivities = daoActi.listActivities(phase.getId());
            List<Actor> listActors;
            List<Resource> listResources;
            for (int i = 0; i < listActivities.size(); i++) {

                listResources = daoRes.listResourcesByActivity(listActivities.get(i).getId());
                for (int j = 0; j < listResources.size(); j++) {
                    listResources.get(j).setActivity(null);
                    daoRes.update(listResources.get(j));
                }

                listActors = daoActo.listActorsByActivity(listActivities.get(i).getId());
                for (int j = 0; j < listActors.size(); j++) {
                    listActors.get(j).setActivity(null);
                    daoActo.update(listActors.get(j));
                }

                daoActi.remove(listActivities.get(i));
            }

            new PhaseDaoImpl().remove(phase);

            System.out.println("################################################"
                    + "removePhase  phase.getId() " + phase.getId());

            FacesMessage msg = new FacesMessage();
            msg.setSeverity(FacesMessage.SEVERITY_INFO);
            msg.setSummary("Sucesso");
            msg.setDetail("Fase removida com sucesso");

            FacesContext.getCurrentInstance().addMessage(null, msg);

            setOperacaoOK(true);

        } catch (Exception e) {

            FacesMessage msg = new FacesMessage();
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            msg.setSummary("Erro");
            msg.setDetail("Não foi possível remover a fase. " + e.getMessage());

            setOperacaoOK(false);
        }
    }
}
