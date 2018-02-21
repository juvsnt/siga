package br.ita.webquest.dao;

import java.util.List;
import br.ita.webquest.model.PartyPhase;

public interface PartyPhaseDao {

    public PartyPhase getPartyPhase(Long id);

    public void save(PartyPhase partyPhase);
    
    public void update(PartyPhase partyPhase);
    
    public void remove(PartyPhase partyPhase);
    
    public List<PartyPhase> listPartyPhases(Long id);
}
