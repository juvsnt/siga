package br.ita.webquest.bean;

import br.ita.webquest.dao.ImageDao;
import br.ita.webquest.dao.ImageDaoImpl;
import br.ita.webquest.model.Image;
import java.io.FileOutputStream;
import java.util.List;
import javax.el.ELContext;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import org.primefaces.event.FileUploadEvent;

@ManagedBean
@SessionScoped
public class ImageBean {

    private Image image;
    private String tempImage;

    //<editor-fold defaultstate="collapsed" desc="constructors">
    public ImageBean() {
    }

    public ImageBean(Image image, String tempImage) {
        this.image = image;
        this.tempImage = tempImage;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="getters-setters">
    public Image getImage() {
        if (image == null) {
            image = new Image();
        }
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
    
    public String getTempImage() {
        return tempImage;
    }

    public void setTempImage(String tempImage) {
        this.tempImage = tempImage;
    }
    
    public List<Image> getListImages() {
        
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        UserBean user = (UserBean) FacesContext
                .getCurrentInstance().getApplication().getELResolver()
                .getValue(elContext, null, "userBean");

        return new ImageDaoImpl().listImages(user.getUser().getId());
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="upload-images">
    public void createFile(byte[] bytes, String arquivo) {
        
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(arquivo);
            fos.write(bytes);
            fos.close();
        } catch (Exception ex) {
        }
    }

    public void uploadImage(FileUploadEvent event) {
        
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        
        UserBean user = (UserBean) FacesContext
                .getCurrentInstance().getApplication().getELResolver()
                .getValue(elContext, null, "userBean");

        byte[] img = event.getFile().getContents();
        
        setTempImage(getRandomImageName());
        
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ServletContext scontext = (ServletContext) facesContext.getExternalContext().getContext();
        String file = scontext.getRealPath("/upload/" + tempImage);
        
        createFile(img, file);

        image = new Image();
        image.setName(tempImage);
        image.setUser(user.getUser());

        ImageDao dao = new ImageDaoImpl();

        dao.save(image);

        FacesMessage msg = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    
    private String getRandomImageName() {  
        int i = (int) (Math.random() * 100000);  
          
        return String.valueOf(i);  
    }  
    //</editor-fold>
}
