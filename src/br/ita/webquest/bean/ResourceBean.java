package br.ita.webquest.bean;

import br.ita.webquest.dao.ActivityDaoImpl;
import br.ita.webquest.dao.PhaseDaoImpl;
import br.ita.webquest.dao.ResourceDao;
import br.ita.webquest.dao.ResourceDaoImpl;
import br.ita.webquest.model.Activity;
import br.ita.webquest.model.Resource;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.el.ELContext;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class ResourceBean {

    private Resource resource;
    private List<Resource> resources;
    private List<String> resourceTypes;
    private boolean operacaoOK;

    @PostConstruct
    public void init() {

        resourceTypes = new ArrayList<String>();

        resourceTypes.add("Chat");
        resourceTypes.add("Email");
        resourceTypes.add("Monitor");
        resourceTypes.add("Objeto de aprendizagem");
    }

    public ResourceBean() {
    }

    //<editor-fold defaultstate="collapsed" desc="getters-setters">
    public Resource getResource() {
        if (resource == null) {
            resource = new Resource();
        }
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public boolean isOperacaoOK() {
        return operacaoOK;
    }

    public void setOperacaoOK(boolean operacaoOK) {
        this.operacaoOK = operacaoOK;
    }

    public List<String> getResourceTypes() {
        return resourceTypes;
    }

    public void setResourceTypes(List<String> resourceTypes) {
        this.resourceTypes = resourceTypes;
    }

    public List<Resource> getListResourcesByActivity() {

        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        ActivityBean activityBean = (ActivityBean) FacesContext
                .getCurrentInstance().getApplication().getELResolver()
                .getValue(elContext, null, "activityBean");

        return new ResourceDaoImpl().listResourcesByActivity(activityBean.getActivity().getId());
    }

    public List<Resource> getListResourcesByPhase() {

        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        PhaseBean phaseBean = (PhaseBean) FacesContext
                .getCurrentInstance().getApplication().getELResolver()
                .getValue(elContext, null, "phaseBean");

        return new ResourceDaoImpl().listResourcesByPhase(phaseBean.getPhase().getId());
    }
    //</editor-fold>

    public void prepareRegisterResource() {

        try {
            resource = new Resource();

            setOperacaoOK(true);

            System.out.println("################################################"
                    + "prepareRegisterResource");

        } catch (Exception e) {
            setOperacaoOK(false);
        }
    }

    public void registerResource() {

        Long phase_id = Long.parseLong(FacesContext.getCurrentInstance()
                .getExternalContext().getRequestParameterMap().get("id"));

        try {

            resource.setPhase(new PhaseDaoImpl().getPhase(phase_id));

            new ResourceDaoImpl().save(resource);

            setOperacaoOK(true);

            FacesMessage msg = new FacesMessage();
            msg.setSeverity(FacesMessage.SEVERITY_INFO);
            msg.setSummary("Sucesso");
            msg.setDetail("Recurso registrado com sucesso");

            FacesContext.getCurrentInstance().addMessage(null, msg);

        } catch (Exception e) {
            setOperacaoOK(false);
        }
    }

    public String updateResource() {

        try {
            new ResourceDaoImpl().update(resource);

            for (int i = 0; i < resources.size(); i++) {
                resources.get(i).setTitle(resource.getTitle());
                resources.get(i).setHref(resource.getHref());
                resources.get(i).setType(resource.getType());

                new ResourceDaoImpl().update(resources.get(i));
            }
            
            resources.clear();
            
            setOperacaoOK(true);
            
            return "edit-task-resources?faces-redirect=true";

        } catch (Exception e) {
            setOperacaoOK(false);
        }
        
        return null;
    }

    public String prepareEdit() {

        Long resource_id = Long.parseLong(FacesContext.getCurrentInstance()
                .getExternalContext().getRequestParameterMap().get("id"));

        try {
            
            resources = new ArrayList<Resource>();

            resource = new ResourceDaoImpl().getResource(resource_id);

            List<Resource> listResources;
            List<Activity> listActivities = new ActivityDaoImpl().listActivitiesByPhase(resource.getPhase().getId());
            
            for (int i = 0; i < listActivities.size(); i++) {
                listResources = new ResourceDaoImpl().listResourcesByActivity(listActivities.get(i).getId());

                for (int j = 0; j < listResources.size(); j++) {
                    if (listResources.get(j).getTitle().equals(resource.getTitle())) {
                        resources.add(listResources.get(j));
                    }
                }
            }
            
            setOperacaoOK(true);

            return "edit-task-resource?faces-redirect=true";

        } catch (Exception e) {
            setOperacaoOK(false);
        }

        return null;
    }

    public void prepareRemove() {

        Long resource_id = Long.parseLong(FacesContext.getCurrentInstance()
                .getExternalContext().getRequestParameterMap().get("id"));

        try {
            resource = new ResourceDaoImpl().getResource(resource_id);

            setOperacaoOK(true);

            System.out.println("################################################"
                    + "prepareRemove id  " + resource_id + "title " + resource.getTitle());

            removeResource();

        } catch (Exception e) {
            setOperacaoOK(false);
        }
    }

    public void removeResource() {
        try {

            ResourceDao dao = new ResourceDaoImpl();

            String title = resource.getTitle();

            while (!title.equals("")) {
                dao.remove(resource);
                resource = dao.getResource(title);
                title = resource.getTitle();
                System.out.println(title);
            }

            setOperacaoOK(true);

            System.out.println("################################################"
                    + "removeResource  resource.getId() " + resource.getId());

            FacesMessage msg = new FacesMessage();
            msg.setSeverity(FacesMessage.SEVERITY_INFO);
            msg.setSummary("Sucesso");
            msg.setDetail("Recurso Removido");

            FacesContext.getCurrentInstance().addMessage(null, msg);

        } catch (Exception e) {
            setOperacaoOK(false);
        }
    }
}
