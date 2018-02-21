package br.ita.webquest.dao;

import java.util.List;
import br.ita.webquest.model.WebQuest;

public interface WebQuestDao {

    public WebQuest getWebQuest(Long id);

    public WebQuest getWebQuest(String title);

    public void save(WebQuest WebQuest);

    public void remove(WebQuest WebQuest);

    public void update(WebQuest WebQuest);

    public List<WebQuest> listWebQuests(Long id);
    
    public List<WebQuest> listAllWebQuests();
    
    public List<WebQuest> listWebQuestsByKey(String key);
    
    public List<WebQuest> listWebQuestsByTeam(Long team_id);
    
}
