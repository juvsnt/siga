package br.ita.webquest.dao;

import br.ita.webquest.model.Outcome;
import java.util.List;

public interface OutcomeDao {

    public Outcome getOutcome(Long id);

    public Outcome getOutcome(String title);

    public void save(Outcome outcome);
    
    public void update(Outcome outcome);
    
    public void remove(Outcome outcome);
    
    public List<Outcome> listOutcomes(Long id);
}
