package br.ita.webquest.bean;

import br.ita.webquest.dao.EmailDaoImpl;
import br.ita.webquest.dao.UserDao;
import br.ita.webquest.dao.UserDaoImpl;
import br.ita.webquest.model.Email;
import br.ita.webquest.model.Team;
import br.ita.webquest.model.User;
import java.io.FileOutputStream;
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
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletContext;
import org.primefaces.event.FileUploadEvent;

@ManagedBean
@SessionScoped
public class UserBean {

    private User user;
    private String newPassword;
    private String oldPassword;
    private String newPasswordCheck;
    private String tempImage;
    private boolean loggedIn;
    private boolean operacaoOK;
    private String to;
    private String from;
    private String message;
    private String subject;
    private String smtpServ;
    private String tt;

    //<editor-fold defaultstate="collapsed" desc="constructors">
    public UserBean() {
    }

    public UserBean(User user, boolean loggedIn, boolean operacaoOK) {
        this.user = user;
        this.loggedIn = loggedIn;
        this.operacaoOK = operacaoOK;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getters-setters">
    public User getUser() {
        if (user == null) {
            user = new User();
        }
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public String getTt() {
        return tt;
    }

    public void setTt(String tt) {
        this.tt = tt;
    }

    public boolean isOperacaoOK() {
        return operacaoOK;
    }

    public void setOperacaoOK(boolean operacaoOK) {
        this.operacaoOK = operacaoOK;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
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

    public String getTempImage() {
        return tempImage;
    }

    public void setTempImage(String tempImage) {
        this.tempImage = tempImage;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPasswordCheck() {
        return newPasswordCheck;
    }

    public void setNewPasswordCheck(String newPasswordCheck) {
        this.newPasswordCheck = newPasswordCheck;
    }

    public List<User> getListUsers() {
        return new UserDaoImpl().listUsers();
    }
    
    public List<User> getListUsersStaff() {
        return new UserDaoImpl().listUsersStaff();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="prepare">
    public void prepareRegisterUser(ActionEvent actionEvent) {

        try {
            user = new User();

            setOperacaoOK(true);

            System.out.println("################################################"
                    + "prepareRegisterUser");

        } catch (Exception e) {
            setOperacaoOK(false);
        }
    }

    public void prepare(ActionEvent actionEvent) {

        Long user_id = Long.parseLong(FacesContext.getCurrentInstance()
                .getExternalContext().getRequestParameterMap().get("id"));

        try {
            user = new UserDaoImpl().getUser(user_id);

            setOperacaoOK(true);

            System.out.println("################################################"
                    + "prepare " + user.getName());

        } catch (Exception e) {
            setOperacaoOK(false);
        }
    }
    //</editor-fold>

    public boolean isLearner() {

        return user.getUser_level().equalsIgnoreCase("learner");
    }

    public String template() {
        return isLearner() ? "template-principal-1" : "template-principal";
    }

    //<editor-fold defaultstate="collapsed" desc="register">
    public String registerUser() {

        if (new EmailDaoImpl().getNumEmail(user.getEmail()) > 0) {

            Email email = new EmailDaoImpl().getEmail2(user.getEmail());

            if (!newPassword.equals(newPasswordCheck)) {
                FacesMessage msg = new FacesMessage();
                msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                msg.setSummary("Erro");
                msg.setDetail("Nova senha não confere.");

                FacesContext.getCurrentInstance().addMessage(null, msg);

                setOperacaoOK(false);

                return null;
            }

            user.setPassword(newPassword);
            user.setUser_level(email.getUser_level());
            if (email.getTeam() != null) {
                user.setPartTeams(new ArrayList<Team>());
                user.getPartTeams().add(email.getTeam());
            }

            new UserDaoImpl().save(user);

            new EmailDaoImpl().remove(email);

            return login();
        }

        FacesMessage msg = new FacesMessage();
        msg.setSeverity(FacesMessage.SEVERITY_ERROR);
        msg.setSummary("Erro");
        msg.setDetail("Email não autorizado.");

        FacesContext.getCurrentInstance().addMessage(null, msg);

        setOperacaoOK(false);

        return null;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="update">
    public void updateUser() {

        try {

            new UserDaoImpl().update(user);

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

    public void updatePasswordUser() {

        try {

            if (!oldPassword.equals(user.getPassword())) {
                FacesMessage msg = new FacesMessage();
                msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                msg.setSummary("Erro");
                msg.setDetail("Senha atual não confere.");

                FacesContext.getCurrentInstance().addMessage(null, msg);

                setOperacaoOK(false);

                return;
            }

            if (!newPassword.equals(newPasswordCheck)) {
                FacesMessage msg = new FacesMessage();
                msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                msg.setSummary("Erro");
                msg.setDetail("Nova senha não confere.");

                FacesContext.getCurrentInstance().addMessage(null, msg);

                setOperacaoOK(false);

                return;
            }

            user.setPassword(newPassword);

            new UserDaoImpl().update(user);

            FacesMessage msg = new FacesMessage();
            msg.setSeverity(FacesMessage.SEVERITY_INFO);
            msg.setSummary("Sucesso");
            msg.setDetail("Senha alterada com sucesso.");

            FacesContext.getCurrentInstance().addMessage(null, msg);

            setOperacaoOK(true);

        } catch (Exception e) {

            FacesMessage msg = new FacesMessage();
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            msg.setSummary("Erro");
            msg.setDetail("Não foi possível realizar a alteração.");

            FacesContext.getCurrentInstance().addMessage(null, msg);

            setOperacaoOK(false);
        }

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="remove">
    public void removeUser() {

        try {
            new UserDaoImpl().remove(user);

            setOperacaoOK(true);

            FacesMessage msg = new FacesMessage();
            msg.setSeverity(FacesMessage.SEVERITY_INFO);
            msg.setSummary("Sucesso");
            msg.setDetail("Usuário removido com sucesso.");

            FacesContext.getCurrentInstance().addMessage(null, msg);

        } catch (Exception e) {
            setOperacaoOK(false);
        }
    }
    //</editor-fold>

    public void sendEmail() {

        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", "mail.webquest-ld.com.br");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");

            Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("convite@webquest-ld.com.br", "kypsta");
                }
            });

            MimeMessage mesg = new MimeMessage(session);
            mesg.setFrom(new InternetAddress("convite@webquest-ld.com.br"));
            mesg.addRecipient(Message.RecipientType.TO, new InternetAddress(tt));
            mesg.setSubject("Assunto");
            mesg.setText("Mensagem");

            Transport.send(mesg);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Email enviado para " + tt));
        } catch (MessagingException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Não foi possível enviar o convite " + e));
        }
    }

    public void register(String email1) {

        setSubject("Cadastro no webquest-ld");
        setMessage("Você foi cadastrado no webquest-ld pelo professor " + user.getName() + "."
                + "\nPara completar o seu cadastro acesse o site www.webquest-ld.com.br/editor");

        try {

            Properties props = System.getProperties();
            // -- Attaching to default Session, or we could start a new one --
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.starttls.enable", true);

            this.setFrom("suporte@webquest-ld.com.br");
            this.setSmtpServ("mail.webquest-ld.com.br");
            props.setProperty("mail.smtp.ssl.trust", "mail.webquest-ld.com.br");

            setTo(email1);
            
            props.put("mail.smtp.host", smtpServ);
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", true);

            Authenticator auth = new SMTPAuthenticator();
            Session session = Session.getInstance(props, auth);
            // -- Create a new message --
            Message msg = new MimeMessage(session);
            // -- Set the FROM and TO fields --
            msg.setFrom(new InternetAddress(from));
            msg.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to, false));
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

            message = "<p align=\"center\"><font color=\"#000099\" face=\"Comic Sans MS\"><span style=\"background-color: rgb(51, 204, 255);\"><font size=\"6\">Convite</font></span></font><br/></p><hr/><p align=\"center\">Você está sendo convidado por " + user.getName() + " para utilizar o Editor de WebQuests IMSLD.</p><p align=\"center\">Para começar a utilizar a ferramenta faça o seu cadastro acessando o link abaixo.</p> <div align=\"center\"><a href=\"http://www.webquest-ld.com.br/editor\">Cadastro</a>.<br/></div>";
            
        } catch (MessagingException ex) {
        }
    }

    public int sendInvitation() {

        setSubject("Convite webquest-ld");
//        setMessage("Você está sendo convidado para utilizar o Editort de WebQuest/IMSLD."
//                + "Para realizar o seu cadastro acessando o site www.webquest-ld.com.br/editor");

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
            msg.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to, false));
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

            Email email = new Email();
            email.setEmail(to);
            email.setUser_level("staff");
            email.setUser(user);

            new EmailDaoImpl().save(email);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Convite enviado para " + to));

            return 0;
        } catch (MessagingException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Não foi possível enviar o convite " + ex));
            return -1;
        }
    }

    public int sendPassword() {

        setSubject("webquest-ld - Recuperação de senha");
        setMessage("Sua senha no webquest-ld é: "
                + user.getPassword());

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

            setTo(user.getEmail());

            Authenticator auth = new SMTPAuthenticator();
            Session session = Session.getInstance(props, auth);
            // -- Create a new message --
            Message msg = new MimeMessage(session);
            // -- Set the FROM and TO fields --
            msg.setFrom(new InternetAddress(from));
            msg.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to, false));
            // -- We could include CC recipients too --
            // if (cc != null)
            // msg.setRecipients(Message.RecipientType.CC
            // ,InternetAddress.parse(cc, false));
            // -- Set the subject and body text --
            msg.setSubject(subject);
            msg.setText(message);
            // -- Set some other header information --
            msg.setHeader("MyMail", "Java Mail Test");
            msg.setSentDate(new Date());
            // -- Send the message --
            Transport.send(msg);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Senha enviada para " + to));

            return 0;
        } catch (MessagingException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Não foi possível enviar a senha " + ex));
            return -1;
        }
    }

    public int sendPasswordLogin() {

        User user_o = new UserDaoImpl().getUser(user.getEmail());

        if (user_o == null) {
            FacesMessage msg = new FacesMessage();
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            msg.setSummary("Erro");
            msg.setDetail("Email não cadastrado.");

            FacesContext.getCurrentInstance().addMessage(null, msg);

            setOperacaoOK(false);

            return -1;

        } else {

            setSubject("webquest-ld - Recuperação de senha");
            setMessage("Sua senha no webquest-ld é: "
                    + user_o.getPassword());

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

                setTo(user.getEmail());

                Authenticator auth = new SMTPAuthenticator();
                Session session = Session.getInstance(props, auth);
                // -- Create a new message --
                Message msg = new MimeMessage(session);
                // -- Set the FROM and TO fields --
                msg.setFrom(new InternetAddress(from));
                msg.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse(to, false));
                // -- We could include CC recipients too --
                // if (cc != null)
                // msg.setRecipients(Message.RecipientType.CC
                // ,InternetAddress.parse(cc, false));
                // -- Set the subject and body text --
                msg.setSubject(subject);
                msg.setText(message);
                // -- Set some other header information --
                msg.setHeader("MyMail", "Java Mail Test");
                msg.setSentDate(new Date());
                // -- Send the message --
                Transport.send(msg);

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Senha enviada para " + to));

                return 0;

            } catch (MessagingException ex) {

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Não foi possível enviar a senha " + ex));
                return -1;

            }
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

    //<editor-fold defaultstate="collapsed" desc="login">
    public String login() {

        try{
        UserDao dao = new UserDaoImpl();
        String senha = user.getPassword();
        user = dao.getUser(user.getEmail());

        if (user != null && user.getPassword().compareTo(senha) == 0) {

            loggedIn = true;

            message = "<p align=\"center\"><font color=\"#000099\" face=\"Comic Sans MS\"><span style=\"background-color: rgb(51, 204, 255);\"><font size=\"6\">Convite</font></span></font><br/></p><hr/><p align=\"center\">Você está sendo convidado por " + user.getName() + " para utilizar o Editor de WebQuests IMSLD.</p><p align=\"center\">Para começar a utilizar a ferramenta faça o seu cadastro acessando o link abaixo.</p> <div align=\"center\"><a href=\"http://www.webquest-ld.com.br/editor\">Cadastro</a>.<br/></div>";

            return "index?faces-redirect=true";
        } else {

            loggedIn = false;

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "E-mail e/ou senha incorretos!", ""));
        }
        }catch (Exception e){
            loggedIn = false;

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "E-mail e/ou senha incorretos!", ""));
            
        }
        return null;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="logout">
    public String logout() {
        user = new User();
        loggedIn = false;
        return "login?faces-redirect=true";
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="upload-images">
    public void createFile(byte[] bytes, String arquivo) {

        FileOutputStream fos;
        try {
            fos = new FileOutputStream(arquivo);
            fos.write(bytes);
            fos.close();
        } catch (Exception ex) {
        }
    }

    public void uploadImage(FileUploadEvent event) {

        ELContext elContext = FacesContext.getCurrentInstance().getELContext();

        UserBean user_bean = (UserBean) FacesContext
                .getCurrentInstance().getApplication().getELResolver()
                .getValue(elContext, null, "userBean");

        byte[] img = event.getFile().getContents();

        setTempImage("profile_" + user_bean.getUser().getId());

        FacesContext facesContext = FacesContext.getCurrentInstance();
        ServletContext scontext = (ServletContext) facesContext.getExternalContext().getContext();
        String file = scontext.getRealPath("/upload/" + tempImage);

        createFile(img, file);

        user_bean.getUser().setImage(tempImage);

        new UserDaoImpl().update(user_bean.getUser());

        FacesMessage msg = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    //</editor-fold>
}
