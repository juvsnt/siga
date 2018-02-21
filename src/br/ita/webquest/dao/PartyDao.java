package br.ita.webquest.dao;

import br.ita.webquest.model.Party;
import java.util.List;

public interface PartyDao {

    public Party getParty(Long id);

    public Party getParty(String name);

    public void save(Party party);
    
    public void update(Party party);
    
    public void remove(Party party);
    
    public List<Party> listPartiesByUser(Long id);
    
    public List<Party> listPartiesByWebquest(Long id);
    
    public List<Party> listParties(Long id);
}
