package br.ita.webquest.dao;

import br.ita.webquest.model.Qualifying;
import java.util.List;

public interface QualifyingDao {

    public Qualifying getQualifying(Long id);

    public Qualifying getQualifying(String title);

    public void save(Qualifying qualifying);
    
    public void update(Qualifying qualifying);
    
    public void remove(Qualifying qualifying);
    
    public List<Qualifying> listQualifiers(Long id);
}
