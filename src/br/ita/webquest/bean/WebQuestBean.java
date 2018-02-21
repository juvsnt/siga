package br.ita.webquest.bean;

import br.ita.webquest.dao.ActivityDaoImpl;
import br.ita.webquest.dao.ActorDaoImpl;
import br.ita.webquest.dao.OutcomeDaoImpl;
import br.ita.webquest.dao.PartyActivityDaoImpl;
import br.ita.webquest.dao.PartyDaoImpl;
import br.ita.webquest.dao.PartyPhaseDaoImpl;
import br.ita.webquest.dao.PhaseDaoImpl;
import br.ita.webquest.dao.QualifyingDaoImpl;
import br.ita.webquest.dao.ResourceDaoImpl;
import br.ita.webquest.dao.UserDaoImpl;
import br.ita.webquest.dao.WebQuestDao;
import br.ita.webquest.dao.WebQuestDaoImpl;
import br.ita.webquest.model.Activity;
import br.ita.webquest.model.Actor;
import br.ita.webquest.model.Outcome;
import br.ita.webquest.model.Party;
import br.ita.webquest.model.PartyActivity;
import br.ita.webquest.model.PartyPhase;
import br.ita.webquest.model.Phase;
import br.ita.webquest.model.Qualifying;
import br.ita.webquest.model.Resource;
import br.ita.webquest.model.User;
import br.ita.webquest.model.WebQuest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.annotation.PostConstruct;
import javax.el.ELContext;
import javax.faces.application.FacesMessage;
import javax.faces.application.NavigationHandler;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.tagcloud.DefaultTagCloudItem;
import org.primefaces.model.tagcloud.DefaultTagCloudModel;
import org.primefaces.model.tagcloud.TagCloudItem;
import org.primefaces.model.tagcloud.TagCloudModel;

/**
 *
 */
@ManagedBean
@SessionScoped
public class WebQuestBean {

    private WebQuest webQuest;
    private String newActivityType;
    private String newActivityTitle;
    private String key;
    private int numPhases;
    private int numActivitiesPerPhases;
    private int numQualifiers;
    private User staff;
    private Actor actorRun;
    private List<String> images;
    private List<String> intellectualProperty;
    private TagCloudModel tagModel;
    private List<String> targets;
    private boolean operacaoOK;
    //
    private String name;
    private String email;
    private String msg;

    @PostConstruct
    public void init() {
        images = new ArrayList<String>();
        targets = new ArrayList<String>();
        tagModel = new DefaultTagCloudModel();
        intellectualProperty = new ArrayList<String>();

        numPhases = 1;
        numQualifiers = 1;
        numActivitiesPerPhases = 1;

        List<String> list = new ArrayList<String>();
        List<WebQuest> listw = new WebQuestDaoImpl().listAllWebQuests();

        String[] s;
        for (int i = 0; i < listw.size(); i++) {
            if (listw.get(i).getKeywords() != null && listw.get(i).isPublished()) {
                s = listw.get(i).getKeywords().split(";");
                list.addAll(Arrays.asList(s));
            }
        }

        int count;
        String d;
        A:
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < tagModel.getTags().size(); j++) {
                d = (String) tagModel.getTags().get(j).getLabel();
                if (d.equalsIgnoreCase(list.get(i))) {
                    continue A;
                }
            }
            count = 0;
            for (int j = 0; j < list.size(); j++) {
                if (list.get(j).equalsIgnoreCase(list.get(i))) {
                    count++;
                }
            }
            tagModel.addTag(new DefaultTagCloudItem(list.get(i), count));
        }

        for (int i = 1; i <= 6; i++) {
            images.add("galleria" + i + ".jpg");
        }

        intellectualProperty.add("Nenhum");
        intellectualProperty.add("Copyright");
        intellectualProperty.add("Copyleft");

        targets.add("Ensino Fundamental");
        targets.add("Ensino Médio");
        targets.add("Graduação");
        targets.add("Pós-Graduação");
    }

    //<editor-fold defaultstate="collapsed" desc="constructors">
    public WebQuestBean() {
        webQuest = new WebQuest();
    }

    public WebQuestBean(WebQuest webQuest, List<String> images, boolean operacaoOK) {
        this.webQuest = webQuest;
        this.images = images;
        this.operacaoOK = operacaoOK;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getters ans setters">
    public boolean isOperacaoOK() {
        return operacaoOK;
    }

    public void setOperacaoOK(boolean operacaoOK) {
        this.operacaoOK = operacaoOK;
    }

    public WebQuest getWebQuest() {
        return webQuest;
    }

    public void setWebQuest(WebQuest webQuest) {
        this.webQuest = webQuest;
    }

    public User getStaff() {
        return staff;
    }

    public void setStaff(User staff) {
        this.staff = staff;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getNewActivityType() {
        return newActivityType;
    }

    public void setNewActivityType(String newActivityType) {
        this.newActivityType = newActivityType;
    }

    public String getNewActivityTitle() {
        return newActivityTitle;
    }

    public void setNewActivityTitle(String newActivityTitle) {
        this.newActivityTitle = newActivityTitle;
    }

    public String getEmail() {
        return email;
    }

    public String getMsg() {
        return msg;
    }

    public String getName() {
        return name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public int getNumPhases() {
        return numPhases;
    }

    public void setNumPhases(int numPhases) {
        this.numPhases = numPhases;
    }

    public int getNumActivitiesPerPhases() {
        return numActivitiesPerPhases;
    }

    public void setNumActivitiesPerPhases(int numActivitiesPerPhases) {
        this.numActivitiesPerPhases = numActivitiesPerPhases;
    }

    public int getNumQualifiers() {
        return numQualifiers;
    }

    public void setNumQualifiers(int numQualifiers) {
        this.numQualifiers = numQualifiers;
    }

    public List<String> getIntellectualProperty() {
        return intellectualProperty;
    }

    public void setIntellectualProperty(List<String> intellectualProperty) {
        this.intellectualProperty = intellectualProperty;
    }

    public List<String> getTargets() {
        return targets;
    }

    public void setTargets(List<String> targets) {
        this.targets = targets;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<WebQuest> getListWebQuests() {

        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        UserBean userBean = (UserBean) FacesContext
                .getCurrentInstance().getApplication().getELResolver()
                .getValue(elContext, null, "userBean");

        return new WebQuestDaoImpl().listWebQuests(userBean.getUser().getId());
    }

    public List<Actor> getListActorByWebQuest() {

        List<Actor> list = new ArrayList<Actor>();

        for (int i = 0; i < webQuest.getPhases().size(); i++) {
            for (int j = 0; j < webQuest.getPhases().get(i).getActors().size(); j++) {
                list.add(webQuest.getPhases().get(i).getActors().get(j));
            }
        }
        return list;
    }

    public List<WebQuest> getListWebQuestsStaff() {

        List<WebQuest> l = new ArrayList<WebQuest>();

        try {

            List<WebQuest> l2 = new WebQuestDaoImpl().listWebQuests(staff.getId());
            System.out.println(l2.size());
            for (int i = 0; i < l2.size(); i++) {
                if (l2.get(i).getIntellectualProperty().equalsIgnoreCase("Nenhum") || l2.get(i).getIntellectualProperty().equalsIgnoreCase("Copyleft")) {
                    l.add(l2.get(i));
                }
            }
        } catch (Exception e) {
        }

        return l;
    }

    public List<WebQuest> getListWebQuestsByKey() {

        return new WebQuestDaoImpl().listWebQuestsByKey(key);
    }

    public List<WebQuest> getListWebQuestsByTeam() {

        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        TeamBean teamBean = (TeamBean) FacesContext
                .getCurrentInstance().getApplication().getELResolver()
                .getValue(elContext, null, "teamBean");

        return new WebQuestDaoImpl().listWebQuestsByTeam(teamBean.getTeam().getId());
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="prepare">
    public String prepareRegisterWebQuest() {

        try {
            webQuest = new WebQuest();

            setOperacaoOK(true);

            System.out.println("################################################"
                    + "prepareRegisterWebQuest");

            return "new-webquest?faces-redirect=true";

        } catch (Exception e) {
            setOperacaoOK(false);
        }

        return null;
    }

    public String prepareSearchKey() {

        key = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("key");

        try {

            setOperacaoOK(true);

            return "search-manager?faces-redirect=true";

        } catch (Exception e) {
            setOperacaoOK(false);
        }

        return null;
    }

    public String prepareRegisterWebQuestFast() {

        try {
            webQuest = new WebQuest();

            setOperacaoOK(true);

            System.out.println("################################################"
                    + "prepareRegisterWebQuest");

            return "new-webquest-fast?faces-redirect=true";

        } catch (Exception e) {
            setOperacaoOK(false);
        }

        return null;
    }

    public String prepareEdit() {

        Long webQuest_id = Long.parseLong(FacesContext.getCurrentInstance()
                .getExternalContext().getRequestParameterMap().get("id"));

        try {
            webQuest = new WebQuestDaoImpl().getWebQuest(webQuest_id);

            setOperacaoOK(true);

            return "edit-cover?faces-redirect=true";

        } catch (Exception e) {
            setOperacaoOK(false);
        }

        return null;
    }

    public String copyWebQuest() {

        Long webQuest_id = Long.parseLong(FacesContext.getCurrentInstance()
                .getExternalContext().getRequestParameterMap().get("id"));

        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        UserBean userBean = (UserBean) FacesContext
                .getCurrentInstance().getApplication().getELResolver()
                .getValue(elContext, null, "userBean");

        try {

            webQuest = new WebQuestDaoImpl().getWebQuest(webQuest_id);

            WebQuest copy = webQuest;

            copy.setColaboration(webQuest.getUser().getName());
            copy.setUser(userBean.getUser());

            //System.out.println(copy.getPhases().size());
            new WebQuestDaoImpl().save(copy);

            copy = new WebQuestDaoImpl().getWebQuest(copy.getId());

            copy.setPhases(webQuest.getPhases());
            for (int i = 0; i < copy.getPhases().size(); i++) {
                copy.getPhases().get(i).setWebQuest(copy);
                new PhaseDaoImpl().save(copy.getPhases().get(i));

                copy.getPhases().get(i).setActors(webQuest.getPhases().get(i).getActors());
                for (int j = 0; j < copy.getPhases().get(i).getActors().size(); j++) {
                    copy.getPhases().get(i).getActors().get(j).setPhase(copy.getPhases().get(i));
                    new ActorDaoImpl().save(copy.getPhases().get(i).getActors().get(j));
                }

                copy.getPhases().get(i).setResources(webQuest.getPhases().get(i).getResources());
                for (int j = 0; j < copy.getPhases().get(i).getResources().size(); j++) {
                    copy.getPhases().get(i).getResources().get(j).setPhase(copy.getPhases().get(i));
                    new ResourceDaoImpl().save(copy.getPhases().get(i).getResources().get(j));
                }

                copy.getPhases().get(i).setActivityies(webQuest.getPhases().get(i).getActivityies());
                for (int j = 0; j < copy.getPhases().get(i).getActivityies().size(); j++) {
                    copy.getPhases().get(i).getActivityies().get(j).setWebQuest(copy);
                    copy.getPhases().get(i).getActivityies().get(j).setPhase(copy.getPhases().get(i));
                    new ActivityDaoImpl().save(copy.getPhases().get(i).getActivityies().get(j));

                    copy.getPhases().get(i).getActivityies().get(j).setActors(webQuest.getPhases().get(i).getActivityies().get(j).getActors());
                    for (int k = 0; k < copy.getPhases().get(i).getActivityies().get(j).getActors().size(); k++) {
                        copy.getPhases().get(i).getActivityies().get(j).getActors().get(k).setActivity(copy.getPhases().get(i).getActivityies().get(j));
                        new ActorDaoImpl().save(copy.getPhases().get(i).getActivityies().get(j).getActors().get(k));
                    }

                    copy.getPhases().get(i).getActivityies().get(j).setResources(webQuest.getPhases().get(i).getActivityies().get(j).getResources());
                    for (int k = 0; k < copy.getPhases().get(i).getActivityies().get(j).getResources().size(); k++) {
                        copy.getPhases().get(i).getActivityies().get(j).getResources().get(k).setActivity(copy.getPhases().get(i).getActivityies().get(j));
                        new ResourceDaoImpl().save(copy.getPhases().get(i).getActivityies().get(j).getResources().get(k));
                    }
                }
            }

            copy.setQualifiers(webQuest.getQualifiers());
            for (int i = 0; i < copy.getQualifiers().size(); i++) {
                copy.getQualifiers().get(i).setWebQuest(copy);
                new QualifyingDaoImpl().save(copy.getQualifiers().get(i));

                copy.getQualifiers().get(i).setOutcomes(webQuest.getQualifiers().get(i).getOutcomes());
                for (int j = 0; j < copy.getQualifiers().get(i).getOutcomes().size(); j++) {
                    copy.getQualifiers().get(i).getOutcomes().get(j).setQualifying(copy.getQualifiers().get(i));
                    new OutcomeDaoImpl().save(copy.getQualifiers().get(i).getOutcomes().get(j));
                }
            }

            //new WebQuestDaoImpl().update(copy);
            webQuest = new WebQuestDaoImpl().getWebQuest(copy.getId());

            setOperacaoOK(true);

            return "webquest-manager?faces-redirect=true";

        } catch (Exception e) {
            setOperacaoOK(false);
        }

        return null;
    }

    public String prepareEditTextProcess() {

        Long webQuest_id = Long.parseLong(FacesContext.getCurrentInstance()
                .getExternalContext().getRequestParameterMap().get("id"));

        try {
            webQuest = new WebQuestDaoImpl().getWebQuest(webQuest_id);

            editProcess();

            new WebQuestDaoImpl().update(webQuest);

            setOperacaoOK(true);

            return "edit-processs?faces-redirect=true";

        } catch (Exception e) {
            setOperacaoOK(false);
        }

        return null;
    }

    private void editProcess() {

        String process = "";

        for (int i = 0; i < webQuest.getPhases().size(); i++) {

            process += "<p>Na fase " + (i + 1) + " você deverá realizar " + webQuest.getPhases().get(i).getActivityies().size() + " atividade(s), descrita(s) abaixo:<p/>";
            for (int j = 0; j < webQuest.getPhases().get(i).getActivityies().size(); j++) {

                process += "<b>Atividade: " + (i + 1) + "." + (j + 1) + "</b><br/>";
                process += "<b>Título: </b> " + webQuest.getPhases().get(i).getActivityies().get(j).getTitle() + "<br/>";
                process += "<b>Papéis: </b><br/>";

                for (int k = 0; k < webQuest.getPhases().get(i).getActivityies().get(j).getActors().size(); k++) {
                    process += webQuest.getPhases().get(i).getActivityies().get(j).getActors().get(k).getTitle() + ": " + webQuest.getPhases().get(i).getActivityies().get(j).getActors().get(k).getAct() + "<br/>";
                }

                process += "<b>Pré-requisitos: </b>" + webQuest.getPhases().get(i).getActivityies().get(j).getPrerequisites() + "<br/>";

                process += "<b>Objetivos: </b>" + webQuest.getPhases().get(i).getActivityies().get(j).getLearningObjectives() + "<br/>";

                process += "<b>Recursos: </b><br/>";

                for (int k = 0; k < webQuest.getPhases().get(i).getActivityies().get(j).getResources().size(); k++) {
                    process += "<a href=\"" + webQuest.getPhases().get(i).getActivityies().get(j).getResources().get(k).getHref() + "\" target=\"_blank\">";
                    process += webQuest.getPhases().get(i).getActivityies().get(j).getResources().get(k).getTitle() + "</a><br>";
                }

                process += "<b>Descrição: </b>" + webQuest.getPhases().get(i).getActivityies().get(j).getActivityDescription() + "<br/>";

                if (j < webQuest.getPhases().get(i).getActivityies().size() - 1) {
                    process += "<br/>";
                }
            }
        }

        webQuest.setProcess(process);
    }

    public void publish() {

        try {

            WebQuestDao dao = new WebQuestDaoImpl();

            webQuest.setPublished(true);

            dao.update(webQuest);

            updateWebQuestCredits(null);

            FacesMessage msg = new FacesMessage();
            msg.setSeverity(FacesMessage.SEVERITY_INFO);
            msg.setSummary("Sucesso");
            msg.setDetail("WebQuest publicada com sucesso " + webQuest.getId());

            FacesContext.getCurrentInstance().addMessage(null, msg);

            setOperacaoOK(true);

        } catch (Exception e) {
            setOperacaoOK(false);
        }
    }

    public void unpublish() {

        try {

            WebQuestDao dao = new WebQuestDaoImpl();

            webQuest.setPublished(false);

            dao.update(webQuest);

            updateWebQuestCredits(null);

            FacesMessage msg = new FacesMessage();
            msg.setSeverity(FacesMessage.SEVERITY_INFO);
            msg.setSummary("Sucesso");
            msg.setDetail("WebQuest despublicada com sucesso " + webQuest.getId());

            FacesContext.getCurrentInstance().addMessage(null, msg);

            setOperacaoOK(true);

        } catch (Exception e) {
            setOperacaoOK(false);
        }
    }

    public String prepareEditTextTask() {

        Long webQuest_id = Long.parseLong(FacesContext.getCurrentInstance()
                .getExternalContext().getRequestParameterMap().get("id"));

        try {
            webQuest = new WebQuestDaoImpl().getWebQuest(webQuest_id);

            editTask();

            new WebQuestDaoImpl().update(webQuest);

            setOperacaoOK(true);

            return "edit-task?faces-redirect=true";

        } catch (Exception e) {
            setOperacaoOK(false);
        }

        return null;
    }

    public void restoreTaskTextDefault(ActionEvent actionEvent) {

        Long webQuest_id = Long.parseLong(FacesContext.getCurrentInstance()
                .getExternalContext().getRequestParameterMap().get("id"));

        try {

            webQuest = new WebQuestDaoImpl().getWebQuest(webQuest_id);

            editTask();

            FacesMessage msg = new FacesMessage();
            msg.setSeverity(FacesMessage.SEVERITY_INFO);
            msg.setSummary("Sucesso");
            msg.setDetail("Texto automático gerado com sucesso");

            FacesContext.getCurrentInstance().addMessage(null, msg);

            setOperacaoOK(true);

        } catch (Exception e) {
            setOperacaoOK(false);
        }
    }

    public void restoreProcessTextDefault() {

        Long webQuest_id = Long.parseLong(FacesContext.getCurrentInstance()
                .getExternalContext().getRequestParameterMap().get("id"));
        try {

            webQuest = new WebQuestDaoImpl().getWebQuest(webQuest_id);

            editProcess();

            FacesMessage msg = new FacesMessage();
            msg.setSeverity(FacesMessage.SEVERITY_INFO);
            msg.setSummary("Sucesso");
            msg.setDetail("Texto automático gerado com sucesso");

            FacesContext.getCurrentInstance().addMessage(null, msg);

            setOperacaoOK(true);

        } catch (Exception e) {
            setOperacaoOK(false);
        }
    }

    private void editTask() {

        String task = "";

        task += "<p><b>Título da tarefa: </b>" + webQuest.getTaskTitle() + "</p>";
        task += "<p>Sua tarefa será composta por " + webQuest.getPhases().size() + " fase(s):</p>";
        for (int i = 0; i < webQuest.getPhases().size(); i++) {
            task += "<b>Fase " + (i + 1) + ":</b><br/>"
                    + "<b>Título: </b>" + webQuest.getPhases().get(i).getTitle() + "<br/>"
                    + "<b>Descrição: </b>" + webQuest.getPhases().get(i).getDescriprion() + "<br/><br/>";
        }

        webQuest.setTask(task);
    }

    private void editEvaluation() {

        String evaluation = "";

        evaluation += "<table border=\"1\" cellpadding=\"2\" style=\"width: 80%;\">"
                + "<thead>"
                + "<tr>"
                + "<th>Qualificador</th>"
                + "<th>Ruim</th>"
                + "<th>Bom</th>"
                + "<th>Muito Bom</th>"
                + "<th>Ótimo</th>"
                + "</tr>"
                + "</thead>"
                + "<tbody>";
        for (int i = 0; i < webQuest.getQualifiers().size(); i++) {
            evaluation += "<tr>"
                    + "<td style=\"width: 100px;\">" + webQuest.getQualifiers().get(i).getTitle() + "</td>"
                    + "<td style=\"width: 100px;\">" + webQuest.getQualifiers().get(i).getOutcomes().get(0).getDescription() + "</td>"
                    + "<td style=\"width: 100px;\">" + webQuest.getQualifiers().get(i).getOutcomes().get(1).getDescription() + "</td>"
                    + "<td style=\"width: 100px;\">" + webQuest.getQualifiers().get(i).getOutcomes().get(2).getDescription() + "</td>"
                    + "<td style=\"width: 100px;\">" + webQuest.getQualifiers().get(i).getOutcomes().get(3).getDescription() + "</td>"
                    + "</tr>";
        }
        evaluation += "<tr>"
                + "<td>valor</td>"
                + "<td style=\"text-align: center\">(6,5 - 7,4)</td>"
                + "<td style=\"text-align: center\">(7,5 - 8,4)</td>"
                + "<td style=\"text-align: center\">(8,5 - 9,4)</td>"
                + "<td style=\"text-align: center\">(9,5 - 10,0)</td>"
                + "</tr>"
                + "</tbody>"
                + "</table>";

        webQuest.setEvaluation(evaluation);
    }

    private void editCredits() {

        String credits = "";

        credits += "<b>WebQuest criada por: </b>" + webQuest.getUser().getName() + "<br/>";

        credits += "<p align=\"justify\"><b>Currículo:</b> " + webQuest.getUser().getDescription() + "<br/></p>";

        credits += "<b>WebQuest versão:</b> " + webQuest.getVersion() + "<br/><br/>";

        credits += "<b>Propriedade intelectual:</b> " + webQuest.getIntellectualProperty() + "<br/><br/>";

        credits += "<b>Pré-requisitos técnicos: </b>" + webQuest.getTechnicalPrerequisites() + "<br/><br/>";

        credits += "<b>Palavras chaves: </b>" + webQuest.getKeywords() + "<br/><br/>";

        credits += "<b>Colaboração: </b>" + webQuest.getColaboration() + "<br/>";

        webQuest.setCredits(credits);
    }

    public void prepareTemplate() {

        Long webQuest_id = Long.parseLong(FacesContext.getCurrentInstance()
                .getExternalContext().getRequestParameterMap().get("id"));

        try {
            webQuest = new WebQuestDaoImpl().getWebQuest(webQuest_id);

            setOperacaoOK(true);

            System.out.println("################################################"
                    + "prepare id  " + webQuest_id + "title " + webQuest.getTitle());

        } catch (Exception e) {
            setOperacaoOK(false);
        }
    }

    public TagCloudModel getModel() {
        return tagModel;
    }

    public void onSelect(SelectEvent selectEvent) throws IOException {

        TagCloudItem item = (TagCloudItem) selectEvent.getObject();
        key = item.getLabel();

        System.out.println("################################################"
                + "key  " + key);

//        
//        FacesContext context = FacesContext.getCurrentInstance();
//        HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
//        response.sendRedirect("search-manager?faces-redirect=true");
        FacesContext facesContext = FacesContext.getCurrentInstance();
        String redirect = "search-manager.xhtml";// define the navigation rule that must be used in order to redirect the user to the adequate page...
        NavigationHandler myNav = facesContext.getApplication().getNavigationHandler();
        myNav.handleNavigation(facesContext, null, redirect);

        System.out.println("################################################"
                + "key  " + key);
        //return "search-manager?faces-redirect=true";
    }

    public void prepareRemove() {

        Long webQuest_id = Long.parseLong(FacesContext.getCurrentInstance()
                .getExternalContext().getRequestParameterMap().get("id"));

        try {
            webQuest = new WebQuestDaoImpl().getWebQuest(webQuest_id);

            setOperacaoOK(true);

            System.out.println("################################################"
                    + "prepare id  " + webQuest_id + "title " + webQuest.getTitle());

        } catch (Exception e) {
            setOperacaoOK(false);
        }
    }

    public void prepareMem() {

        Long webQuest_id = Long.parseLong(FacesContext.getCurrentInstance()
                .getExternalContext().getRequestParameterMap().get("id"));

        try {
            webQuest = new WebQuestDaoImpl().getWebQuest(webQuest_id);

            setOperacaoOK(true);

            System.out.println("################################################"
                    + "prepare id  " + webQuest_id + "title " + webQuest.getTitle());

        } catch (Exception e) {
            setOperacaoOK(false);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="remove">
    public void removeWebQuest() {

        try {

            for (int i = 0; i < webQuest.getActivities().size(); i++) {

                for (int j = 0; j < webQuest.getActivities().get(i).getActors().size(); j++) {
                    new ActorDaoImpl().remove(webQuest.getActivities().get(i).getActors().get(j));
                }

                for (int j = 0; j < webQuest.getActivities().get(i).getResources().size(); j++) {
                    new ResourceDaoImpl().remove(webQuest.getActivities().get(i).getResources().get(j));
                }

                new ActivityDaoImpl().remove(webQuest.getActivities().get(i));
            }

            for (int i = 0; i < webQuest.getPhases().size(); i++) {

                for (int j = 0; j < webQuest.getPhases().get(i).getActors().size(); j++) {
                    new ActorDaoImpl().remove(webQuest.getPhases().get(i).getActors().get(j));
                }

                for (int j = 0; j < webQuest.getPhases().get(i).getResources().size(); j++) {
                    new ResourceDaoImpl().remove(webQuest.getPhases().get(i).getResources().get(j));
                }

                new PhaseDaoImpl().remove(webQuest.getPhases().get(i));
            }

            for (int i = 0; i < webQuest.getQualifiers().size(); i++) {
                for (int j = 0; j < webQuest.getQualifiers().get(i).getOutcomes().size(); j++) {
                    new OutcomeDaoImpl().remove(webQuest.getQualifiers().get(i).getOutcomes().get(j));
                }

                new QualifyingDaoImpl().remove(webQuest.getQualifiers().get(i));
            }

            new WebQuestDaoImpl().remove(webQuest);

            setOperacaoOK(true);

            System.out.println("################################################"
                    + "removeWebQuest  webQuest.getId() " + webQuest.getId());

            FacesMessage msg = new FacesMessage();
            msg.setSeverity(FacesMessage.SEVERITY_INFO);
            msg.setSummary("Sucesso");
            msg.setDetail("WebQuest Removida");

            FacesContext.getCurrentInstance().addMessage(null, msg);

        } catch (Exception e) {
            FacesMessage msg = new FacesMessage();
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            msg.setSummary("Erro");
            msg.setDetail("WebQuest não pode ser removida " + e.getMessage());

            FacesContext.getCurrentInstance().addMessage(null, msg);
            setOperacaoOK(false);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="view">
    public String viewWebQuest() {

        Long webQuest_id = Long.parseLong(FacesContext.getCurrentInstance()
                .getExternalContext().getRequestParameterMap().get("id"));

        try {
            webQuest = new WebQuestDaoImpl().getWebQuest(webQuest_id);

            editTask();
            editProcess();

            editEvaluation();

            editCredits();

            setOperacaoOK(true);

            return "cover?faces-redirect=true";

        } catch (Exception e) {
            setOperacaoOK(false);
        }

        return null;
    }

    public String runWebQuest() {

        List<Party> listP;
        List<PartyActivity> listPA;
        List<PartyPhase> listPP;

        Long webQuest_id = Long.valueOf("0");

        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        UserBean userBean = (UserBean) FacesContext
                .getCurrentInstance().getApplication().getELResolver()
                .getValue(elContext, null, "userBean");

        if (userBean.getUser().getUser_level().equalsIgnoreCase("learner")) {
            webQuest_id = Long.parseLong(FacesContext.getCurrentInstance()
                    .getExternalContext().getRequestParameterMap().get("id"));
        }

        try {
            if (userBean.getUser().getUser_level().equalsIgnoreCase("learner")) {
                webQuest = new WebQuestDaoImpl().getWebQuest(webQuest_id);
            } else {
                listP = new PartyDaoImpl().listPartiesByUser(userBean.getUser().getId());
                for (int j = 0; j < listP.size(); j++) {
                    listPA = new PartyActivityDaoImpl().listActivities(listP.get(j).getId());
                    for (int k = 0; k < listPA.size(); k++) {
                        new PartyActivityDaoImpl().remove(listPA.get(j));
                    }
                    listPP = new PartyPhaseDaoImpl().listPartyPhases(listP.get(j).getId());
                    for (int k = 0; k < listPP.size(); k++) {
                        new PartyPhaseDaoImpl().remove(listPP.get(j));
                    }
                    new PartyDaoImpl().remove(listP.get(j));
                }
            }

            setOperacaoOK(true);

            return "run-actors?faces-redirect=true";

        } catch (Exception e) {
            setOperacaoOK(false);
        }

        return null;
    }

    public String viewWebQuestsStaff() {

        Long user_id = Long.parseLong(FacesContext.getCurrentInstance()
                .getExternalContext().getRequestParameterMap().get("id"));

        try {
            staff = new UserDaoImpl().getUser(user_id);

            //System.out.println(staff.getName());
            setOperacaoOK(true);

            return "webquests-list?faces-redirect=true";

        } catch (Exception e) {
            setOperacaoOK(false);
        }

        return null;
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="register">
    public String registerWebQuest() {

        ELContext elContext = FacesContext.getCurrentInstance()
                .getELContext();
        UserBean userBean = (UserBean) FacesContext
                .getCurrentInstance().getApplication().getELResolver()
                .getValue(elContext, null, "userBean");

        try {

            webQuest.setUser(userBean.getUser());

            webQuest.setTemplate("001");

            new WebQuestDaoImpl().save(webQuest);

            FacesMessage msg = new FacesMessage();
            msg.setSeverity(FacesMessage.SEVERITY_INFO);
            msg.setSummary("Sucesso");
            msg.setDetail("WebQuest registrada com sucesso");

            FacesContext.getCurrentInstance().addMessage(null, msg);

            setOperacaoOK(true);

            return "webquest-manager?faces-redirect=true";

        } catch (Exception e) {
            setOperacaoOK(false);
        }

        return null;

    }
    
    public void requestInvitation() {

        try {
            
//            setSubject("Convite webquest-ld");
//        setMessage("Você está sendo convidado para utilizar o Editort de WebQuest/IMSLD."
//                + "Para realizar o seu cadastro acessando o site www.webquest-ld.com.br/editor");

            Properties props = System.getProperties();
            // -- Attaching to default Session, or we could start a new one --
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.starttls.enable", true);

//            this.setFrom("suporte@webquest-ld.com.br");
//            this.setSmtpServ("mail.webquest-ld.com.br");
            props.setProperty("mail.smtp.ssl.trust", "mail.webquest-ld.com.br");

            props.put("mail.smtp.host", "mail.webquest-ld.com.br");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", true);

            Authenticator auth = new WebQuestBean.SMTPAuthenticator();
            Session session = Session.getInstance(props, auth);
            // -- Create a new message --
            Message msg = new MimeMessage(session);
            // -- Set the FROM and TO fields --
            msg.setFrom(new InternetAddress("suporte@webquest-ld.com.br"));
            msg.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse("juvenalneto@unemat.br", false));
            // -- We could include CC recipients too --
            // if (cc != null)
            // msg.setRecipients(Message.RecipientType.CC
            // ,InternetAddress.parse(cc, false));
            // -- Set the subject and body text --
            msg.setSubject("Convite webquest-ld");
            msg.setContent(getEmail() + " -- " +getMsg(), "text/html; charset=UTF-8");
            // -- Set some other header information --
            msg.setHeader("MyMail", "Java Mail Test");
            msg.setSentDate(new Date());
            // -- Send the message --
            Transport.send(msg);
            
            FacesMessage msg1 = new FacesMessage();
            msg1.setSeverity(FacesMessage.SEVERITY_INFO);
            msg1.setSummary("Sucesso");
            msg1.setDetail("WebQuest solicitação enviada com sucesso");

            FacesContext.getCurrentInstance().addMessage(null, msg1);

            setOperacaoOK(true);

//            return "webquest-manager?faces-redirect=true";

        } catch (Exception e) {
            setOperacaoOK(false);
        }

//        return null;
    }
    
    private class SMTPAuthenticator extends javax.mail.Authenticator {

        @Override
        public PasswordAuthentication getPasswordAuthentication() {
//            String username = "guelmim4ever@gmail.com";
//            String password = "********";
            return new PasswordAuthentication("suporte@webquest-ld.com.br", "kipsta");
        }
    }

    public String registerWebQuestFast() {

        ELContext elContext = FacesContext.getCurrentInstance()
                .getELContext();
        UserBean userBean = (UserBean) FacesContext
                .getCurrentInstance().getApplication().getELResolver()
                .getValue(elContext, null, "userBean");

        try {

            webQuest.setUser(userBean.getUser());

            webQuest.setTemplate("001");

            webQuest.setIntroduction("<p>O objetivo desta webQuest, sobre " + webQuest.getTitle()
                    + ", é " + webQuest.getLearningObjectives() + ".</p>"
                    + "<p>Para realizar esta webQuest o aluno precisa de " + webQuest.getPrerequisites());

            webQuest.setTaskTitle(webQuest.getTitle());

            webQuest.setVersion("1.0");
            webQuest.setColaboration("none");
            webQuest.setIntellectualProperty("Nenhum");
            webQuest.setTechnicalPrerequisites("Nenhum");
            webQuest.setKeywords("none");

            new WebQuestDaoImpl().save(webQuest);

            Phase phase;
            Actor actor;
            Resource resource;
            for (int i = 0; i < numPhases; i++) {
                phase = new Phase();
                actor = new Actor();

                phase.setTitle("Fase " + (i + 1));
                phase.setDescriprion("Descrição da fase " + (i + 1));
                phase.setCompleteProcess(true);
                phase.setCompleteTask(true);
                phase.setWebQuest(webQuest);

                new PhaseDaoImpl().save(phase);

                actor.setTitle("Ator " + (i + 1));
                actor.setType("Aluno");
                actor.setAct("Papel do ator " + +(i + 1));
                actor.setPhase(phase);
                actor.setComplete(true);

                new ActorDaoImpl().save(actor);

                resource = new Resource();

                resource.setTitle("Recurso " + (i + 1));
                resource.setType("Objeto de aprendizagem");
                resource.setHref("web");
                resource.setPhase(phase);

                new ResourceDaoImpl().save(resource);

                Activity activity;
                for (int j = 0; j < numActivitiesPerPhases; j++) {
                    activity = new Activity();

                    activity.setTitle("Atividade " + (j + 1));
                    activity.setActivityDescription("Descrição da atividade " + (j + 1));
                    activity.setLearningObjectives("Objetivos de aprendizagem da atividade " + (j + 1));
                    activity.setPrerequisites("Pré-requisitos da atividade " + (j + 1));
                    activity.setCompleteActivity("1");

                    activity.setComplete(true);
                    activity.setPhase(phase);
                    activity.setWebQuest(webQuest);

                    new ActivityDaoImpl().save(activity);

                    Actor actor1 = new Actor();
                    actor1.setTitle(actor.getTitle());
                    actor1.setType(actor.getType());
                    actor1.setAct(actor.getAct());

                    actor1.setActivity(activity);

                    new ActorDaoImpl().save(actor1);

                    Resource resource1 = new Resource();
                    resource1.setTitle(resource.getTitle());
                    resource1.setType(resource.getType());
                    resource1.setHref(resource.getHref());

                    resource1.setActivity(activity);

                    new ResourceDaoImpl().save(resource1);
                }
            }

            Qualifying qualifying;
            for (int i = 0; i < numQualifiers; i++) {
                qualifying = new Qualifying();

                qualifying.setTitle("Qualificador " + (i + 1));
                qualifying.setWebQuest(webQuest);

                new QualifyingDaoImpl().save(qualifying);

                Outcome out;
                for (int j = 0; j < 4; j++) {
                    out = qualifying.getOutcomes().get(j);
                    out.setQualifying(qualifying);
                    new OutcomeDaoImpl().save(out);
                }
            }

            webQuest.setConclusion("Agora que você concluiu com êxito esta webQuest sobre " + webQuest.getTitle()
                    + " você possui um maior conhecimento sobre o tema estudado, que tal agora ir além e pesquisar sobre outros assuntos relacionados com esse tema.<br/>");

            new WebQuestDaoImpl().update(webQuest);

            FacesMessage msg = new FacesMessage();
            msg.setSeverity(FacesMessage.SEVERITY_INFO);
            msg.setSummary("Sucesso");
            msg.setDetail("WebQuest registrada com sucesso");

            FacesContext.getCurrentInstance().addMessage(null, msg);

            setOperacaoOK(true);

            return "webquest-manager?faces-redirect=true";

        } catch (Exception e) {
            FacesMessage msg = new FacesMessage();
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            msg.setSummary("Erro");
            msg.setDetail("A WebQuest não pode ser criada " + e.getMessage());

            FacesContext.getCurrentInstance().addMessage(null, msg);

            setOperacaoOK(false);
        }

        return null;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="update">
    public String updateWebQuestProcess() {

        try {
            new WebQuestDaoImpl().update(webQuest);

            setOperacaoOK(true);

            return "edit-process?faces-redirect=true";

        } catch (Exception e) {
            setOperacaoOK(false);
        }

        return null;
    }

    public String updateWebQuestTask() {

        try {
            new WebQuestDaoImpl().update(webQuest);

            setOperacaoOK(true);

            return "edit-task-phases?faces-redirect=true";

        } catch (Exception e) {
            setOperacaoOK(false);
        }

        return null;
    }

    public void updateWebQuest(ActionEvent actionEvent) {

        try {
            new WebQuestDaoImpl().update(webQuest);

            setOperacaoOK(true);

            FacesMessage msg = new FacesMessage();
            msg.setSeverity(FacesMessage.SEVERITY_INFO);
            msg.setSummary("Sucesso");
            msg.setDetail("Dados alterados com sucesso");

            FacesContext.getCurrentInstance().addMessage(null, msg);

        } catch (Exception e) {
            setOperacaoOK(false);
        }
    }

    public void updateWebQuestCredits(ActionEvent actionEvent) {

        try {
            new WebQuestDaoImpl().update(webQuest);

            tagModel = new DefaultTagCloudModel();

            List<String> list = new ArrayList<String>();
            List<WebQuest> listw = new WebQuestDaoImpl().listAllWebQuests();

            String[] s;
            for (int i = 0; i < listw.size(); i++) {
                if (listw.get(i).getKeywords() != null && listw.get(i).isPublished()) {
                    s = listw.get(i).getKeywords().split(";");
                    list.addAll(Arrays.asList(s));
                }
            }

            int count;
            String d;
            A:
            for (int i = 0; i < list.size(); i++) {
                for (int j = 0; j < tagModel.getTags().size(); j++) {
                    d = (String) tagModel.getTags().get(j).getLabel();
                    if (d.equalsIgnoreCase(list.get(i))) {
                        continue A;
                    }
                }
                count = 0;
                for (int j = 0; j < list.size(); j++) {
                    if (list.get(j).equalsIgnoreCase(list.get(i))) {
                        count++;
                    }
                }
                tagModel.addTag(new DefaultTagCloudItem(list.get(i), count));
            }

            setOperacaoOK(true);

            FacesMessage msg = new FacesMessage();
            msg.setSeverity(FacesMessage.SEVERITY_INFO);
            msg.setSummary("Sucesso");
            msg.setDetail("Dados alterados com sucesso");

            FacesContext.getCurrentInstance().addMessage(null, msg);

        } catch (Exception e) {
            setOperacaoOK(false);
        }
    }

    public void updateWebQuestNull(ActionEvent actionEvent) {

        setOperacaoOK(true);

        FacesMessage msg = new FacesMessage();
        msg.setSeverity(FacesMessage.SEVERITY_INFO);
        msg.setSummary("Sucesso");
        msg.setDetail("Dados alterados com sucesso");

        FacesContext.getCurrentInstance().addMessage(null, msg);

    }

    public void updateWebQuestTemplate(ActionEvent actionEvent) {

        String template = FacesContext.getCurrentInstance()
                .getExternalContext().getRequestParameterMap().get("name");

        try {
            webQuest.setTemplate(template);

            new WebQuestDaoImpl().update(webQuest);

            setOperacaoOK(true);

            System.out.println("########################################### updateWebQuestTemplate "
                    + webQuest.getTitle() + "/" + webQuest.getId());

        } catch (Exception ex) {
            setOperacaoOK(false);
        }
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="util">
    //</editor-fold>
}
