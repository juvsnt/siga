package br.ita.webquest.dao;

import br.ita.webquest.model.Email;
import java.util.List;
import br.ita.webquest.model.Team;
import br.ita.webquest.model.User;
import br.ita.webquest.model.WebQuest;

public interface TeamDao {

    public Team getTeam(Long id);

    public Team getTeamByUser(Long user_id);

    public void save(Team team);

    public void remove(Team team);

    public void update(Team team);

    public List<Team> listTeams();
    
    public List<Team> listTeamsByUser(Long User_id);
    
    public List<Team> listTeamsByWebquest(Long User_id);
    
    public List<Team> listPartTeamsByUser(Long User_id);
    
    public List<User> listUsersByTeam(Long Team_id);
    
    public List<Email> listEmailsByTeam(Long Team_id);
    
    public List<WebQuest> listWebQuestsByTeam(Long Team_id);
}
