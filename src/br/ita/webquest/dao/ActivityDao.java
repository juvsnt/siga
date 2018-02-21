package br.ita.webquest.dao;

import br.ita.webquest.model.Activity;
import java.util.List;

public interface ActivityDao {

    public Activity getActivity(Long id);

    public Activity getActivity(String name);

    public void save(Activity activity);
    
    public void update(Activity activity);
    
    public void remove(Activity activity);
    
    public List<Activity> listActivitiesByPhase(Long id);
    
    public List<Activity> listActivitiesByWebquest(Long id);
    
    public List<Activity> listActivities(Long id);
}
