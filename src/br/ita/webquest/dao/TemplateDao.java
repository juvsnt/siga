package br.ita.webquest.dao;

import br.ita.webquest.model.Template;
import java.util.List;

public interface TemplateDao {

    public Template getTemplate(Long id);

    public Template getTemplate(String name);

    public void save(Template template);
    
    public List<Template> listTemplates();
}
