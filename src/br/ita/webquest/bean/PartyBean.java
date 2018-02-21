package br.ita.webquest.bean;

import br.ita.webquest.dao.ActivityDaoImpl;
import br.ita.webquest.dao.PartyActivityDaoImpl;
import br.ita.webquest.dao.PartyDaoImpl;
import br.ita.webquest.dao.PartyPhaseDaoImpl;
import br.ita.webquest.dao.PhaseDaoImpl;
import br.ita.webquest.model.Activity;
import br.ita.webquest.model.Party;
import br.ita.webquest.model.PartyActivity;
import br.ita.webquest.model.PartyPhase;
import br.ita.webquest.model.Phase;
import java.util.ArrayList;
import java.util.List;
import javax.el.ELContext;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

@ManagedBean
@SessionScoped
public class PartyBean {

    private Party party;
    private PartyPhase phase;
    private PartyActivity activity;
    private boolean complete;
    private List<Party> listParties;
    private List<String> listChecked;
    private boolean operacaoOK;

    public PartyBean() {
        listChecked = new ArrayList<String>();
    }

    public PartyBean(Party party, PartyPhase phase, PartyActivity activity, boolean complete, boolean operacaoOK) {
        this.party = party;
        this.phase = phase;
        this.activity = activity;
        this.complete = complete;
        this.operacaoOK = operacaoOK;
    }

    //<editor-fold defaultstate="collapsed" desc="getters-setters">
    public List<PartyActivity> getPartyActivities() {

        List<PartyActivity> partyActivities = new ArrayList<PartyActivity>();
        for (int i = 0; i < party.getPartyActivities().size(); i++) {
            if (party.getPartyActivities().get(i).getActivity().getPhase().getId().compareTo(phase.getPhase().getId()) == 0) {
                partyActivities.add(party.getPartyActivities().get(i));
            }
        }

        return partyActivities;
    }

    public List<PartyPhase> getPartyPhases() {
        return party.getPartyPhases();
    }

    public void setListParties(List<Party> listParties) {
        this.listParties = listParties;
    }

    public List<Party> getListParties() {
        return listParties;
    }

    public List<Phase> getPhases() {

        List<Phase> list = new ArrayList<Phase>();

        for (int i = 0; i < party.getPartyPhases().size(); i++) {
            list.add(party.getPartyPhases().get(i).getPhase());
        }
        return list;
    }

    public List<String> getListChecked() {
        return listChecked;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public void setParty(Party party) {
        this.party = party;
    }

    public void setPhase(PartyPhase phase) {
        this.phase = phase;
    }

    public void setActivity(PartyActivity activity) {
        this.activity = activity;
    }

    public PartyPhase getPhase() {
        return phase;
    }

    public PartyActivity getActivity() {
        return activity;
    }

    public Party getParty() {
        if (party == null) {
            party = new Party();
        }
        return party;
    }

    public boolean isOperacaoOK() {
        return operacaoOK;
    }

    public void setOperacaoOK(boolean operacaoOK) {
        this.operacaoOK = operacaoOK;
    }

    @Override
    public String toString() {
        return super.toString(); //To change body of generated methods, choose Tools | Templates.
    }

    //</editor-fold>
    public String prepareRegisterParty() {

        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        UserBean userBean = (UserBean) FacesContext
                .getCurrentInstance().getApplication().getELResolver()
                .getValue(elContext, null, "userBean");

        Long actor_id = Long.parseLong(FacesContext.getCurrentInstance()
                .getExternalContext().getRequestParameterMap().get("id"));

        try {

            setOperacaoOK(true);

            System.out.println("################################################"
                    + "prepareRegisterParty");

            registerParty(actor_id);

            return "edit-process-activity?faces-redirect=true";

        } catch (Exception e) {
            setOperacaoOK(false);
        }
        return null;
    }

    public void registerParty(Long id) {

        try {

            FacesMessage msg = new FacesMessage();
            msg.setSeverity(FacesMessage.SEVERITY_INFO);
            msg.setSummary("Sucesso");
            msg.setDetail("Participação registrada com sucesso");

            FacesContext.getCurrentInstance().addMessage(null, msg);

            setOperacaoOK(true);

        } catch (Exception e) {

            FacesMessage msg = new FacesMessage();
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            msg.setSummary("Erro");
            msg.setDetail("Não foi possível registrar a participação. " + e.getMessage());

            setOperacaoOK(false);
        }
    }

    public void isComplete() {

        try {

            complete = false;
            for (int i = 0; i < party.getPartyActivities().size(); i++) {
                if (!party.getPartyActivities().get(i).isComplete()) {
                    return;
                }
            }

            for (int i = 0; i < party.getPartyPhases().size(); i++) {
                if (!party.getPartyPhases().get(i).isComplete()) {
                    return;
                }
            }

            complete = true;

            setOperacaoOK(true);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            setOperacaoOK(false);
        }
    }

    public void setParties() {

        Long role_id = Long.parseLong(FacesContext.getCurrentInstance()
                .getExternalContext().getRequestParameterMap().get("id"));
        try {

            listParties = new PartyDaoImpl().listPartiesByUser(role_id);

            getListCheckedParties();

            setOperacaoOK(true);

        } catch (Exception e) {
            setOperacaoOK(false);
        }
    }

    public void showMessage() {
        FacesMessage msg = new FacesMessage();
        msg.setSeverity(FacesMessage.SEVERITY_ERROR);
        msg.setSummary("Erro");
        msg.setDetail("Não foi possível realizar a operação. ");

        FacesContext.getCurrentInstance().addMessage(null, msg);
        //RequestContext.getCurrentInstance().showMessageInDialog(message);
    }

    public void showgialog() {

        RequestContext.getCurrentInstance().execute("dlgTask.show()");

    }

    public void getListCheckedParties() {

        listChecked.clear();

        if (listParties != null && !listParties.isEmpty()) {
            System.out.println(listParties.size());
            for (int i = 0; i < listParties.size(); i++) {

                listChecked.add(listParties.get(i).getActorTitle() + " " + (listParties.get(i).isComplete() ? "<input type=\"checkbox\" name=\"\" value=\"ON\" checked=\"checked\" disabled=\"disabled\" />" : "<input type=\"checkbox\" name=\"\" value=\"ON\" disabled=\"disabled\" />"));
                for (int j = 0; j < listParties.get(i).getPartyPhases().size(); j++) {
                    listChecked.add("&nbsp;&nbsp;&nbsp;&nbsp;" + listParties.get(i).getPartyPhases().get(j).getPhase().getTitle() + " " + (listParties.get(i).getPartyPhases().get(j).isComplete() ? "<input type=\"checkbox\" name=\"\" value=\"ON\" checked=\"checked\" disabled=\"disabled\" />" : "<input type=\"checkbox\" name=\"\" value=\"ON\" disabled=\"disabled\" />"));
                    for (int k = 0; k < listParties.get(i).getPartyActivities().size(); k++) {
                        if (listParties.get(i).getPartyActivities().get(k).getActivity().getPhase().getId().compareTo(listParties.get(i).getPartyPhases().get(j).getPhase().getId()) == 0) {
                            listChecked.add("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + listParties.get(i).getPartyActivities().get(k).getActivity().getTitle() + " " + (listParties.get(i).getPartyActivities().get(k).isComplete() ? "<input type=\"checkbox\" name=\"\" value=\"ON\" checked=\"checked\" disabled=\"disabled\" />" : "<input type=\"checkbox\" name=\"\" value=\"ON\" disabled=\"disabled\" />"));
                        }
                    }
                }
            }
        } else {
            listChecked.add("Nenhuma run criada");
        }
        
        showgialog();
        
        System.out.println("Checkeddd");
    }

    public String runAs() {

        ELContext elContext = FacesContext.getCurrentInstance().getELContext();

        WebQuestBean webQuestBean = (WebQuestBean) FacesContext
                .getCurrentInstance().getApplication().getELResolver()
                .getValue(elContext, null, "webQuestBean");

        UserBean userBean = (UserBean) FacesContext
                .getCurrentInstance().getApplication().getELResolver()
                .getValue(elContext, null, "userBean");

        String title = FacesContext.getCurrentInstance()
                .getExternalContext().getRequestParameterMap().get("title");

        try {
            //String title = new ActorDaoImpl().getActor(actor_id).getTitle();

            List<Party> list = new PartyDaoImpl().listPartiesByUser(userBean.getUser().getId());

            if (!list.isEmpty()) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getActorTitle().equalsIgnoreCase(title)) {
                        party = list.get(i);
                        setOperacaoOK(true);

                        return "run-phases?faces-redirect=true";
                    }
                }
                party = new Party();

                party.setUser(userBean.getUser());
                party.setActorTitle(title);

                new PartyDaoImpl().save(party);
            } else {
                party = new Party();

                party.setUser(userBean.getUser());
                party.setActorTitle(title);

                new PartyDaoImpl().save(party);
            }

            PartyPhase partyPhase;
            PartyActivity partyActivity;
            List<Activity> listActivities;
            List<Phase> listPhases = new PhaseDaoImpl().listPhase(webQuestBean.getWebQuest().getId());

            for (int i = 0; i < listPhases.size(); i++) {

                for (int j = 0; j < listPhases.get(i).getActors().size(); j++) {

                    if (listPhases.get(i).getActors().get(j).getTitle().equalsIgnoreCase(title)) {

                        partyPhase = new PartyPhase();
                        partyPhase.setComplete(false);
                        partyPhase.setParty(party);
                        partyPhase.setPhase(listPhases.get(i));

                        new PartyPhaseDaoImpl().save(partyPhase);
                        party.getPartyPhases().add(partyPhase);

                        listActivities = new ActivityDaoImpl().listActivities(listPhases.get(i).getId());
                        for (int k = 0; k < listActivities.size(); k++) {
                            for (int l = 0; l < listActivities.get(k).getActors().size(); l++) {
                                if (listActivities.get(k).getActors().get(l).getTitle().equalsIgnoreCase(title)) {
                                    partyActivity = new PartyActivity();
                                    partyActivity.setComplete(false);
                                    partyActivity.setParty(party);
                                    partyActivity.setActivity(listActivities.get(k));

                                    new PartyActivityDaoImpl().save(partyActivity);
                                    party.getPartyActivities().add(partyActivity);
                                }
                            }
                        }

                        break;
                    }
                }
            }

            setOperacaoOK(true);

            return "run-phases?faces-redirect=true";

        } catch (Exception e) {

            FacesMessage msg = new FacesMessage();
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            msg.setSummary("Erro");
            msg.setDetail("Não foi possível realizar a operação. " + e.getMessage());
            System.out.println(e);

            setOperacaoOK(false);
        }

        return null;
    }

    public String completeActivity() {

        int cout = 0;

        try {
            activity.setComplete(true);
            new PartyActivityDaoImpl().update(activity);

            party.setPartyActivities(new PartyActivityDaoImpl().listActivities(party.getId()));

            A:
            for (int i = 0; i < party.getPartyPhases().size(); i++) {
                if (!party.getPartyPhases().get(i).isComplete()) {
                    for (int j = 0; j < party.getPartyActivities().size(); j++) {
                        if (party.getPartyActivities().get(j).getActivity().getPhase().getId().compareTo(party.getPartyPhases().get(i).getPhase().getId()) == 0) {
                            //System.out.println(party.getPartyActivities().get(j).isComplete());
                            if (!party.getPartyActivities().get(j).isComplete()) {
                                setOperacaoOK(true);

                                continue A;
                            }
                        }
                    }
                    party.getPartyPhases().get(i).setComplete(true);
                    new PartyPhaseDaoImpl().update(party.getPartyPhases().get(i));
                    cout++;
                } else {
                    cout++;
                }
            }

            System.out.println(cout + " " + party.getPartyPhases().size());

            if (cout == party.getPartyPhases().size()) {
                party.setComplete(true);
                new PartyDaoImpl().update(party);
            }

            party.setPartyPhases(new PartyPhaseDaoImpl().listPartyPhases(party.getId()));

//            for (int i = 0; i < phase.getPhase().getActivityies().size(); i++) {
//                System.out.println(phase.getPhase().getTitle());
//                System.out.println(phase.getPhase().getActivityies().get(i).getTitle());
//            }
//            for (int i = 0; i < party.getPartyActivities().size(); i++) {
//                System.out.println(party.getPartyActivities().get(i).getActivity().getPhase().getTitle());
//                System.out.println(party.getPartyActivities().get(i).getActivity().getTitle());
//                System.out.println(party.getPartyActivities().get(i).isComplete());
//            }
//            System.out.println(phase);
            setOperacaoOK(true);

            return runPhase();

        } catch (Exception e) {
            setOperacaoOK(false);
        }

        return null;
    }

    public String runPhase() {

        Long phase_id = Long.parseLong(FacesContext.getCurrentInstance()
                .getExternalContext().getRequestParameterMap().get("id"));
        //System.out.println(phase_id);

        try {
            phase = new PartyPhaseDaoImpl().getPartyPhase(phase_id);

            //System.out.println(phase);
            setOperacaoOK(true);

            return "run-activities?faces-redirect=true";

        } catch (Exception e) {
            setOperacaoOK(false);
        }

        return null;
    }

    public String runActivity() {

        Long activity_id = Long.parseLong(FacesContext.getCurrentInstance()
                .getExternalContext().getRequestParameterMap().get("id"));

        try {

            activity = new PartyActivityDaoImpl().getPartyActivity(activity_id);

            setOperacaoOK(true);

            return "run-activity?faces-redirect=true";

        } catch (Exception e) {
            setOperacaoOK(false);
        }

        return null;
    }

    public void updateParty() {

        try {

            isComplete();

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

            setOperacaoOK(true);

            return "edit-process-activity?faces-redirect=true";

        } catch (Exception e) {
            setOperacaoOK(false);
        }

        return null;
    }

    public void prepareRemove() {

        Long party_id = Long.parseLong(FacesContext.getCurrentInstance()
                .getExternalContext().getRequestParameterMap().get("id"));

        try {

            System.out.println("################################################"
                    + "prepareRemove");

            removeParty();

            setOperacaoOK(true);

        } catch (Exception e) {
            setOperacaoOK(false);
        }
    }

    public void removeParty() {

        try {

            FacesMessage msg = new FacesMessage();
            msg.setSeverity(FacesMessage.SEVERITY_INFO);
            msg.setSummary("Sucesso");
            msg.setDetail("Participação removida com sucesso");

            FacesContext.getCurrentInstance().addMessage(null, msg);

            setOperacaoOK(true);

        } catch (Exception e) {

            FacesMessage msg = new FacesMessage();
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            msg.setSummary("Erro");
            msg.setDetail("Não foi possível remover a Participação. " + e.getMessage());

            FacesContext.getCurrentInstance().addMessage(null, msg);

            setOperacaoOK(false);
        }
    }
}
