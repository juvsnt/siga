package br.ita.webquest.dao;

import br.ita.webquest.model.Resource;
import java.util.List;

public interface ResourceDao {

    public Resource getResource(Long id);

    public Resource getResource(String title);

    public void save(Resource resource);
    
    public void update(Resource resource);
    
    public void remove(Resource resource);
    
    public List<Resource> listResourcesByActivity(Long id);
    
    public List<Resource> listResourcesByPhase(Long id);
}
