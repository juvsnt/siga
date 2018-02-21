package br.ita.webquest.dao;

import br.ita.webquest.model.Email;
import java.util.List;

public interface EmailDao {

    public Email getEmail(Long id);

    public int getNumEmail(String email);
            
    public Email getEmail2(String email);

    public void save(Email email);

    public void update(Email email);
    
    public void remove(Email email);
    
    public List<Email> listEmails(Long id);
}
