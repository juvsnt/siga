package br.ita.webquest.dao;

import br.ita.webquest.model.Actor;
import java.util.List;

public interface ActorDao {

    public Actor getActor(Long id);

    public Actor getActor(String name);

    public void save(Actor actor);
    
    public void update(Actor actor);
    
    public void remove(Actor actor);
    
    public List<Actor> listActorsByActivity(Long id);
    
    public List<Actor> listActorsByPhase(Long id);
}
