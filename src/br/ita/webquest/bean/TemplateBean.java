package br.ita.webquest.bean;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import br.ita.webquest.dao.TemplateDaoImpl;
import br.ita.webquest.model.Template;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

/**
 *
 * @author
 */
@ManagedBean
@SessionScoped
public class TemplateBean {

    private Template template;
    private DataModel listTemplate;

    public Template getTemplate() {
        if (template == null) {
            template = new Template();
        }
        return template;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }

    public DataModel getListTemplates() {
        List<Template> list = new TemplateDaoImpl().listTemplates();
        listTemplate = new ListDataModel(list);

        return listTemplate;
    }
}
