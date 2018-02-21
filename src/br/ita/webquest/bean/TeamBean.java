package br.ita.webquest.bean;

import br.ita.webquest.dao.EmailDaoImpl;
import br.ita.webquest.dao.PartyActivityDaoImpl;
import br.ita.webquest.dao.PartyDaoImpl;
import br.ita.webquest.dao.PartyPhaseDaoImpl;
import br.ita.webquest.dao.TeamDaoImpl;
import br.ita.webquest.dao.UserDaoImpl;
import br.ita.webquest.dao.WebQuestDaoImpl;
import br.ita.webquest.model.Email;
import br.ita.webquest.model.Party;
import br.ita.webquest.model.PartyActivity;
import br.ita.webquest.model.PartyPhase;
import br.ita.webquest.model.Team;
import br.ita.webquest.model.User;
import br.ita.webquest.model.WebQuest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.el.ELContext;
import javax.faces.application.FacesMessage;
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

@ManagedBean
@SessionScoped
public class TeamBean {

    private Team team;
    private boolean loggedIn;
    private boolean operacaoOK;
    private String team_name;
    private String webQuest;
    private List<String> webQuests;
    private String to;
    private String email;
    private String from;
    private String message;
    private String subject;
    private String smtpServ;

    //<editor-fold defaultstate="collapsed" desc="constructors">
    public TeamBean() {
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getters-setters">
    public Team getTeam() {
        if (team == null) {
            team = new Team();
        }
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public String getWebQuest() {
        return webQuest;
    }

    public void setWebQuest(String webQuest) {
        this.webQuest = webQuest;
    }

    public List<String> getWebQuests() {
        return webQuests;
    }

    public void setWebQuests(List<String> webQuests) {
        this.webQuests = webQuests;
    }

    public boolean isOperacaoOK() {
        return operacaoOK;
    }

    public void setOperacaoOK(boolean operacaoOK) {
        this.operacaoOK = operacaoOK;
    }

    public String getTeam_name() {
        return team_name;
    }

    public void setTeam_name(String team_name) {
        this.team_name = team_name;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSmtpServ() {
        return smtpServ;
    }

    public void setSmtpServ(String smtpServ) {
        this.smtpServ = smtpServ;
    }

    public List<Team> getListTeams() {
        return new TeamDaoImpl().listTeams();
    }

    public List<Team> getListTeamsByUser() {
        ELContext elContext = FacesContext.getCurrentInstance()
                .getELContext();
        UserBean userBean = (UserBean) FacesContext
                .getCurrentInstance().getApplication().getELResolver()
                .getValue(elContext, null, "userBean");

        if (userBean.getUser().getUser_level().equalsIgnoreCase("learner")) {
            return userBean.getUser().getPartTeams();
        } else {
            return new TeamDaoImpl().listTeamsByUser(userBean.getUser().getId());
        }
    }

    public List<User> getListUserByTeam() {
        ELContext elContext = FacesContext.getCurrentInstance()
                .getELContext();
        TeamBean teamBean = (TeamBean) FacesContext
                .getCurrentInstance().getApplication().getELResolver()
                .getValue(elContext, null, "teamBean");

        return new TeamDaoImpl().listUsersByTeam(teamBean.getTeam().getId());
    }

    public List<Email> getListEmailsByTeam() {
        ELContext elContext = FacesContext.getCurrentInstance()
                .getELContext();
        TeamBean teamBean = (TeamBean) FacesContext
                .getCurrentInstance().getApplication().getELResolver()
                .getValue(elContext, null, "teamBean");

        return new TeamDaoImpl().listEmailsByTeam(teamBean.getTeam().getId());
    }

    public List<WebQuest> getListWebQuestsByTeam() {
        ELContext elContext = FacesContext.getCurrentInstance()
                .getELContext();
        TeamBean teamBean = (TeamBean) FacesContext
                .getCurrentInstance().getApplication().getELResolver()
                .getValue(elContext, null, "teamBean");

        return new TeamDaoImpl().listWebQuestsByTeam(teamBean.getTeam().getId());
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="prepare">
    public void prepareRegisterTeam(ActionEvent actionEvent) {

        try {
            team = new Team();
            email = "";

            setOperacaoOK(true);

        } catch (Exception e) {
            setOperacaoOK(false);
        }
    }

    public void prepare(ActionEvent actionEvent) {

        Long team_id = Long.parseLong(FacesContext.getCurrentInstance()
                .getExternalContext().getRequestParameterMap().get("id"));

        try {
            team = new TeamDaoImpl().getTeam(team_id);

            setOperacaoOK(true);

        } catch (Exception e) {
            setOperacaoOK(false);
        }
    }

    public String prepareEdit() {

        Long team_id = Long.parseLong(FacesContext.getCurrentInstance()
                .getExternalContext().getRequestParameterMap().get("id"));

        try {

            team = new TeamDaoImpl().getTeam(team_id);

            webQuests = new ArrayList<String>();

            List<WebQuest> list = new WebQuestDaoImpl().listWebQuests(team.getUser().getId());
            for (int i = 0; i < list.size(); i++) {
                webQuests.add(list.get(i).getTitle());
            }

            setOperacaoOK(true);

            return "edit-team?faces-redirect=true";

        } catch (Exception e) {
            setOperacaoOK(false);
        }

        return null;
    }

    public String prepareE() {

        Long team_id = Long.parseLong(FacesContext.getCurrentInstance()
                .getExternalContext().getRequestParameterMap().get("id"));

        try {

            team = new TeamDaoImpl().getTeam(team_id);

            team.getName();
            team.getEmails().toString();

            setOperacaoOK(true);

            return "view-team?faces-redirect=true";

        } catch (Exception e) {
            setOperacaoOK(false);
        }

        return null;
    }

    public void prepareRemove() {

        Long team_id = Long.parseLong(FacesContext.getCurrentInstance()
                .getExternalContext().getRequestParameterMap().get("id"));

        try {
            team = new TeamDaoImpl().getTeam(team_id);

            setOperacaoOK(true);

        } catch (Exception e) {
            setOperacaoOK(false);
        }
    }

    public void removeTeam() {

        try {

            String n = team.getName();

            List<Team> listT;
            List<Party> listP;
            List<PartyActivity> listPA;
            List<PartyPhase> listPP;
            List<User> listU = team.getUsers();
            for (int i = 0; i < listU.size(); i++) {

                if (listU.get(i).getUser_level().equals("learner")) {
                    listP = new PartyDaoImpl().listPartiesByUser(listU.get(i).getId());
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
                    new UserDaoImpl().remove(listU.get(i));
                } else {
                    for (int j = 0; j < team.getUsers().size(); j++) {
                        if (team.getUsers().get(j).getId().equals(listU.get(i).getId())) {

                            if (team.getUsers().size() == 1) {
                                team.getUsers().clear();
                            } else {
                                team.getUsers().remove(j);
                                j--;
                            }
                        }
                    }

                    for (int j = 0; j < listU.get(j).getPartTeams().size(); j++) {
                        if (listU.get(j).getPartTeams().get(j).getId().equals(team.getId())) {

                            if (listU.get(i).getPartTeams().size() == 1) {
                                listU.get(i).getPartTeams().clear();
                            } else {
                                listU.get(i).getPartTeams().remove(j);
                                j--;
                            }
                        }
                    }
                    new UserDaoImpl().update(listU.get(i));
                }
            }

            List<Email> listE = team.getEmails();
            for (int i = 0; i < listE.size(); i++) {
                new EmailDaoImpl().remove(listE.get(i));
            }

            new TeamDaoImpl().remove(team);

            setOperacaoOK(true);

            FacesMessage msg = new FacesMessage();
            msg.setSeverity(FacesMessage.SEVERITY_INFO);
            msg.setSummary("Sucesso");
            msg.setDetail("Turma " + n + " Removida");

            FacesContext.getCurrentInstance().addMessage(null, msg);

        } catch (Exception e) {
            setOperacaoOK(false);
        }
    }

    public void registerRole() {

        ELContext elContext = FacesContext.getCurrentInstance()
                .getELContext();
        UserBean userBean = (UserBean) FacesContext
                .getCurrentInstance().getApplication().getELResolver()
                .getValue(elContext, null, "userBean");

        try {

            String[] emails = email.split(";");

            User user = null;
            for (String email1 : emails) {
                try {
                    user = new UserDaoImpl().getUser(email1);
                } catch (Exception e) {

                }

                if (user != null) {
                    user.getPartTeams().add(team);
                    new UserDaoImpl().update(user);
                } else {
                    Email email2 = new Email();
                    email2.setUser(userBean.getUser());
                    email2.setTeam(team);
                    email2.setUser_level("learner");
                    email2.setEmail(email1);

//                System.out.println(email2.getUser().getName());
                    userBean.register(email1);

                    new EmailDaoImpl().save(email2);

                    team.getEmails().add(email2);
                }
//                System.out.println(email1);
            }

            FacesMessage msg = new FacesMessage();
            msg.setSeverity(FacesMessage.SEVERITY_INFO);
            msg.setSummary("Sucesso");
            msg.setDetail("Emails registrados com sucesso. Aguardando cadastro.");

            FacesContext.getCurrentInstance().addMessage(null, msg);

            setOperacaoOK(true);

        } catch (Exception e) {

            FacesMessage msg = new FacesMessage();
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            msg.setSummary("ERRO");
            msg.setDetail("Emails não registrados (" + e.getMessage() + ").");

            FacesContext.getCurrentInstance().addMessage(null, msg);

            setOperacaoOK(false);
        }
    }

    public void prepareRemoveRegistered() {

        Long role_id = Long.parseLong(FacesContext.getCurrentInstance()
                .getExternalContext().getRequestParameterMap().get("id"));

        try {

            List<Party> listP;
            List<PartyActivity> listPA;
            List<PartyPhase> listPP;

            User user = new UserDaoImpl().getUser(role_id);

            Email email2;

            if (user != null) {

                try {
                    email2 = new EmailDaoImpl().getEmail(user.getId());

                    new EmailDaoImpl().remove(email2);
                } catch (Exception e) {
                }

                if (user.getUser_level().equals("learner")) {

                    listP = new PartyDaoImpl().listPartiesByUser(user.getId());
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

                    new UserDaoImpl().remove(user);
                } else {
                    for (int i = 0; i < team.getUsers().size(); i++) {
                        if (team.getUsers().get(i).getId().equals(user.getId())) {

                            if (team.getUsers().size() == 1) {
                                team.getUsers().clear();
                            } else {
                                team.getUsers().remove(i);
                                i--;
                            }
                        }
                    }

                    for (int i = 0; i < user.getPartTeams().size(); i++) {
                        if (user.getPartTeams().get(i).getId().equals(team.getId())) {

                            if (user.getPartTeams().size() == 1) {
                                user.getPartTeams().clear();
                            } else {
                                user.getPartTeams().remove(i);
                                i--;
                            }
                        }
                    }
                    new UserDaoImpl().update(user);
                }

                new TeamDaoImpl().update(team);

                setOperacaoOK(true);

                FacesMessage msg = new FacesMessage();
                msg.setSeverity(FacesMessage.SEVERITY_INFO);
                msg.setSummary("Sucesso");
                msg.setDetail("Aluno Removido com sucesso");

                FacesContext.getCurrentInstance().addMessage(null, msg);
            }
        } catch (Exception e) {
            setOperacaoOK(false);
        }
    }

    public void prepareRemoveWebQuest() {

        Long webquest_id = Long.parseLong(FacesContext.getCurrentInstance()
                .getExternalContext().getRequestParameterMap().get("id"));

        try {

            for (int i = 0; i < team.getWebQuests().size(); i++) {
                if (team.getWebQuests().get(i).getId().equals(webquest_id)) {

                    if (team.getWebQuests().size() == 1) {
                        team.getWebQuests().clear();
                    } else {
                        team.getWebQuests().remove(i);
                        i--;
                    }
                }
            }

            new TeamDaoImpl().update(team);

            setOperacaoOK(true);

            FacesMessage msg = new FacesMessage();
            msg.setSeverity(FacesMessage.SEVERITY_INFO);
            msg.setSummary("Sucesso");
            msg.setDetail("WebQuest Removida com sucesso");

            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (Exception e) {
            setOperacaoOK(false);
        }
    }

    public void prepareRemoveUnregistered() {

        Long email_id = Long.parseLong(FacesContext.getCurrentInstance()
                .getExternalContext().getRequestParameterMap().get("id"));

        try {

            Email email2 = new EmailDaoImpl().getEmail(email_id);

            if (email2 != null) {

                new EmailDaoImpl().remove(email2);

                setOperacaoOK(true);

                FacesMessage msg = new FacesMessage();
                msg.setSeverity(FacesMessage.SEVERITY_INFO);
                msg.setSummary("Sucesso");
                msg.setDetail("Email Removido com sucesso");

                FacesContext.getCurrentInstance().addMessage(null, msg);
            }
        } catch (Exception e) {
            setOperacaoOK(false);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="register">
    public void registerTeam() {

        ELContext elContext = FacesContext.getCurrentInstance()
                .getELContext();
        UserBean userBean = (UserBean) FacesContext
                .getCurrentInstance().getApplication().getELResolver()
                .getValue(elContext, null, "userBean");

        try {

            team.setName(team_name);
            team.setUser(userBean.getUser());

            new TeamDaoImpl().save(team);

            FacesMessage msg = new FacesMessage();
            msg.setSeverity(FacesMessage.SEVERITY_INFO);
            msg.setSummary("Sucesso");
            msg.setDetail("Turma registrada com sucesso.");

            FacesContext.getCurrentInstance().addMessage(null, msg);

            setOperacaoOK(true);

        } catch (Exception e) {
            setOperacaoOK(false);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="update">
    public void updateTeam() {

        try {

            new TeamDaoImpl().update(team);

            FacesMessage msg = new FacesMessage();
            msg.setSeverity(FacesMessage.SEVERITY_INFO);
            msg.setSummary("Sucesso");
            msg.setDetail("Dados alterados com sucesso.");

            FacesContext.getCurrentInstance().addMessage(null, msg);

            setOperacaoOK(true);

        } catch (Exception e) {
            setOperacaoOK(false);
        }

    }

    public void addWebQuest() {

        try {

            if (team.getWebQuests() == null) {
                team.setWebQuests(new ArrayList<WebQuest>());
            }

            team.getWebQuests().add(new WebQuestDaoImpl().getWebQuest(webQuest));

            new TeamDaoImpl().update(team);

            FacesMessage msg = new FacesMessage();
            msg.setSeverity(FacesMessage.SEVERITY_INFO);
            msg.setSummary("Sucesso");
            msg.setDetail("Dados alterados com sucesso.");

            FacesContext.getCurrentInstance().addMessage(null, msg);

            setOperacaoOK(true);

        } catch (Exception e) {
            setOperacaoOK(false);
        }

    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="remove">
    public void removeUser() {

        try {
            new TeamDaoImpl().remove(team);

            setOperacaoOK(true);

            FacesMessage msg = new FacesMessage();
            msg.setSeverity(FacesMessage.SEVERITY_INFO);
            msg.setSummary("Sucesso");
            msg.setDetail("Turma removido com sucesso.");

            FacesContext.getCurrentInstance().addMessage(null, msg);

        } catch (Exception e) {
            setOperacaoOK(false);
        }
    }
    //</editor-fold>

    public String sendInvitation() {

        setSubject("Convite webquest-ld");
        setMessage("<p align=\"center\">Você foi inscrito na turma " + team.getName() + " pelo professor " + team.getUser().getName()
                + "<p align=\"center\">Para vizualizar a(s) WebQuest(s) voce precisa completar o seu cadastro acessando o link abaixo.</p>"
                + "<div align=\"center\"><a href=\"http://www.webquest-ld.com.br/editor\">Cadastro</a>.<br/></div>");
        if (!team.getEmails().isEmpty()) {
            try {

                Properties props = System.getProperties();
                // -- Attaching to default Session, or we could start a new one --
                props.put("mail.transport.protocol", "smtp");
                props.put("mail.smtp.starttls.enable", true);

                this.setFrom("suporte@webquest-ld.com.br");
                this.setSmtpServ("mail.webquest-ld.com.br");
                props.setProperty("mail.smtp.ssl.trust", "mail.webquest-ld.com.br");

                props.put("mail.smtp.host", smtpServ);
                props.put("mail.smtp.port", "587");
                props.put("mail.smtp.auth", true);

                Authenticator auth = new SMTPAuthenticator();
                Session session = Session.getInstance(props, auth);
                // -- Create a new message --
                Message msg = new MimeMessage(session);
                // -- Set the FROM and TO fields --
                msg.setFrom(new InternetAddress(from));

                InternetAddress[] adress = new InternetAddress[team.getEmails().size()];
                for (int i = 0; i < team.getEmails().size(); i++) {
                    adress[i] = new InternetAddress(team.getEmails().get(i).getEmail(), false);
                }
                msg.setRecipients(Message.RecipientType.TO, adress);
                // -- We could include CC recipients too --
                // if (cc != null)
                // msg.setRecipients(Message.RecipientType.CC
                // ,InternetAddress.parse(cc, false));
                // -- Set the subject and body text --
                msg.setSubject(subject);
                msg.setContent(message, "text/html; charset=UTF-8");
                // -- Set some other header information --
                msg.setHeader("MyMail", "Java Mail Test");
                msg.setSentDate(new Date());
                // -- Send the message --
                Transport.send(msg);

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Email enviado para " + adress));

                return "edit-teams-team?faces-redirect=true";
            } catch (Exception ex) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Não foi possível enviar o email " + ex));
                return null;
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Nenhum email cadastrado para esta turma"));
            return null;

        }
    }

    private class SMTPAuthenticator extends javax.mail.Authenticator {

        @Override
        public PasswordAuthentication getPasswordAuthentication() {
//            String username = "guelmim4ever@gmail.com";
//            String password = "********";
            return new PasswordAuthentication("suporte@webquest-ld.com.br", "kipsta");
        }
    }
}
