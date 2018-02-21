package br.ita.webquest.bean;

import br.ita.webquest.dao.OutcomeDao;
import br.ita.webquest.dao.OutcomeDaoImpl;
import br.ita.webquest.dao.QualifyingDao;
import br.ita.webquest.dao.QualifyingDaoImpl;
import br.ita.webquest.dao.WebQuestDaoImpl;
import br.ita.webquest.model.Outcome;
import br.ita.webquest.model.Qualifying;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.el.ELContext;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 */
@ManagedBean
@SessionScoped
public class QualifyingBean {

    private Qualifying qualifying;
    private boolean operacaoOK;

    @PostConstruct
    public void init() {
    }

    //<editor-fold defaultstate="collapsed" desc="constructors">
    public QualifyingBean() {
        qualifying = new Qualifying();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getters ans setters">
    public boolean isOperacaoOK() {
        return operacaoOK;
    }

    public void setOperacaoOK(boolean operacaoOK) {
        this.operacaoOK = operacaoOK;
    }

    public Qualifying getQualifying() {
        if (qualifying == null) {
            qualifying = new Qualifying();
        }
        return qualifying;
    }

    public void setQualifying(Qualifying qualifying) {
        this.qualifying = qualifying;
    }

    public List<Qualifying> getListQualifiers() {

        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        WebQuestBean webQuestBean = (WebQuestBean) FacesContext
                .getCurrentInstance().getApplication().getELResolver()
                .getValue(elContext, null, "webQuestBean");

        return new QualifyingDaoImpl().listQualifiers(webQuestBean.getWebQuest().getId());
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="prepare">
    public void prepareAddQualitying() {

        try {
            qualifying = new Qualifying();

            setOperacaoOK(true);

            System.out.println("################################################"
                    + "prepareAddQualitying");

        } catch (Exception e) {
            setOperacaoOK(false);
        }
    }

    public void addNewQualifiyng() {

        Long webQuest_id = Long.parseLong(FacesContext.getCurrentInstance()
                .getExternalContext().getRequestParameterMap().get("id"));

        try {

            qualifying.setWebQuest(new WebQuestDaoImpl().getWebQuest(webQuest_id));

            new QualifyingDaoImpl().save(qualifying);

            Outcome out;
            for (int i = 0; i < 4; i++) {
                out = qualifying.getOutcomes().get(i);
                out.setQualifying(qualifying);
                new OutcomeDaoImpl().save(out);
            }

            setOperacaoOK(true);

            FacesMessage msg = new FacesMessage();
            msg.setSeverity(FacesMessage.SEVERITY_INFO);
            msg.setSummary("Sucesso");
            msg.setDetail("Qualificador registrado com sucesso");

            FacesContext.getCurrentInstance().addMessage(null, msg);

            qualifying = new Qualifying();

        } catch (Exception e) {
            setOperacaoOK(false);
        }
    }

    public String prepareEditQualifying() {

        Long qualifying_id = Long.parseLong(FacesContext.getCurrentInstance()
                .getExternalContext().getRequestParameterMap().get("id"));

        try {

            qualifying = new QualifyingDaoImpl().getQualifying(qualifying_id);

            //qualifying.getOutcomes().toString();

            setOperacaoOK(true);

            return "edit-evaluation-qualifying?faces-redirect=true";

        } catch (Exception e) {

            System.out.println("############################################################" + e);
            setOperacaoOK(false);
        }

        return null;
    }

    public void PrepareRemoveQualifying() {

        Long qualifying_id = Long.parseLong(FacesContext.getCurrentInstance()
                .getExternalContext().getRequestParameterMap().get("id"));

        QualifyingDao daoQuali = new QualifyingDaoImpl();
        OutcomeDao daoOut = new OutcomeDaoImpl();

        try {
            qualifying = new QualifyingDaoImpl().getQualifying(qualifying_id);

            for (int i = 0; i < 4; i++) {
                daoOut.remove(qualifying.getOutcomes().get(i));
            }

            daoQuali.remove(qualifying);

            FacesMessage msg = new FacesMessage();
            msg.setSeverity(FacesMessage.SEVERITY_INFO);
            msg.setSummary("Sucesso");
            msg.setDetail("Qualificador removido com sucesso");

            FacesContext.getCurrentInstance().addMessage(null, msg);

            setOperacaoOK(true);

            System.out.println("################################################"
                    + "PrepareRemoveQualifying id  " + qualifying_id + "id " + qualifying.getId());

        } catch (Exception e) {
            FacesMessage msg = new FacesMessage();
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            msg.setSummary("Erro");
            msg.setDetail("Não foi possível remover o qualificador " + e.getMessage());

            FacesContext.getCurrentInstance().addMessage(null, msg);

            setOperacaoOK(false);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="update">
    public String updateQualifying() {

        try {

            new QualifyingDaoImpl().update(qualifying);

            Outcome out;
            for (int i = 0; i < 4; i++) {
                out = qualifying.getOutcomes().get(i);
                out.setQualifying(qualifying);
                new OutcomeDaoImpl().update(out);
            }

            FacesMessage msg = new FacesMessage();
            msg.setSeverity(FacesMessage.SEVERITY_INFO);
            msg.setSummary("Sucesso");
            msg.setDetail("Dados alterados com sucesso");

            FacesContext.getCurrentInstance().addMessage(null, msg);

            setOperacaoOK(true);

            return "edit-evaluation-qualifiers?faces-redirect=true";
            
        } catch (Exception e) {

            FacesMessage msg = new FacesMessage();
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            msg.setSummary("Erro");
            msg.setDetail("Não foi possível atualizar o qualificador " + e.getMessage());

            FacesContext.getCurrentInstance().addMessage(null, msg);

            setOperacaoOK(false);
        }
        
        return null;
    }
    //</editor-fold>
}
