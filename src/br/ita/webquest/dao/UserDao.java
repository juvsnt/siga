package br.ita.webquest.dao;

import java.util.List;
import br.ita.webquest.model.User;

public interface UserDao {

    public User getUser(Long id);

    public User getUser(String email);

    public void save(User user);

    public void remove(User user);

    public void update(User user);

    public List<User> listUsers();
    
    public List<User> listUsersStaff();
}
