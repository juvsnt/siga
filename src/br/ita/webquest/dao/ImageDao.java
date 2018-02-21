package br.ita.webquest.dao;

import br.ita.webquest.model.Image;
import java.util.List;

public interface ImageDao {

    public Image getImage(Long id);

    public Image getImage(String name);

    public void save(Image image);
    
    public void remove(Image image);
    
    public List<Image> listImages(Long id);
}
