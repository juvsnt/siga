package br.ita.webquest.dao;

import br.ita.webquest.model.PartyActivity;
import java.util.List;

public interface PartyActivityDao {

    public PartyActivity getPartyActivity(Long id);

    public void save(PartyActivity partyActivity);
    
    public void update(PartyActivity partyActivity);
    
    public void remove(PartyActivity partyActivity);
    
    public List<PartyActivity> listActivitiesByPhase(Long id);
    
    public List<PartyActivity> listActivitiesByWebquest(Long id);
    
    public List<PartyActivity> listActivities(Long id);
}
