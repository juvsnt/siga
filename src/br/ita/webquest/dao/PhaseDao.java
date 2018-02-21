package br.ita.webquest.dao;

import br.ita.webquest.model.Phase;
import java.util.List;

public interface PhaseDao {

    public Phase getPhase(Long id);

    public Phase getPhase(String name);

    public void save(Phase phase);
    
    public void update(Phase phase);
    
    public void remove(Phase phase);
    
    public List<Phase> listPhase(Long id);
  
}
