package br.ita.webquest.bean;

import br.ita.webquest.dao.ActivityDaoImpl;
import br.ita.webquest.dao.WebQuestDaoImpl;
import br.ita.webquest.model.Activity;
import br.ita.webquest.model.WebQuest;
import com.sun.org.apache.xerces.internal.jaxp.datatype.DatatypeFactoryImpl;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.Duration;
import org.imsglobal.jaxb.content.Manifest;
import org.imsglobal.jaxb.content.ManifestMetadata;
import org.imsglobal.jaxb.content.Organizations;
import org.imsglobal.jaxb.content.Resource;
import org.imsglobal.jaxb.content.Resources;
import org.imsglobal.jaxb.ld.Act;
import org.imsglobal.jaxb.ld.Activities;
import org.imsglobal.jaxb.ld.ActivityStructure;
import org.imsglobal.jaxb.ld.ActivityStructureRef;
import org.imsglobal.jaxb.ld.CompleteAct;
import org.imsglobal.jaxb.ld.CompleteActivity;
import org.imsglobal.jaxb.ld.CompletePlay;
import org.imsglobal.jaxb.ld.CompleteUnitOfLearning;
import org.imsglobal.jaxb.ld.Components;
import org.imsglobal.jaxb.ld.EmailData;
import org.imsglobal.jaxb.ld.Environment;
import org.imsglobal.jaxb.ld.EnvironmentRef;
import org.imsglobal.jaxb.ld.Environments;
import org.imsglobal.jaxb.ld.Item;
import org.imsglobal.jaxb.ld.ItemModel;
import org.imsglobal.jaxb.ld.Learner;
import org.imsglobal.jaxb.ld.LearningActivity;
import org.imsglobal.jaxb.ld.LearningActivityRef;
import org.imsglobal.jaxb.ld.LearningDesign;
import org.imsglobal.jaxb.ld.LearningObject;
import org.imsglobal.jaxb.ld.Method;
import org.imsglobal.jaxb.ld.Play;
import org.imsglobal.jaxb.ld.RolePart;
import org.imsglobal.jaxb.ld.RoleRef;
import org.imsglobal.jaxb.ld.Roles;
import org.imsglobal.jaxb.ld.SendMail;
import org.imsglobal.jaxb.ld.Service;
import org.imsglobal.jaxb.ld.Staff;
import org.imsglobal.jaxb.ld.TimeLimit;
import org.imsglobal.jaxb.ld.UserChoice;
import org.imsglobal.jaxb.ld.WhenLastActCompleted;
import org.imsglobal.jaxb.ld.WhenPlayCompleted;
import org.imsglobal.jaxb.ld.WhenRolePartCompleted;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 */
@ManagedBean
@SessionScoped
public class XmlBean implements Serializable {

    private WebQuest webQuest;
    private StreamedContent downloadContentProperty;

    public WebQuest getWebQuest() {
        return webQuest;
    }

    public void setWebQuest(WebQuest webQuest) {
        this.webQuest = webQuest;
    }
    
    public void prepareEdit() {

        Long webQuest_id = Long.parseLong(FacesContext.getCurrentInstance()
                .getExternalContext().getRequestParameterMap().get("id"));

        try {
            webQuest = new WebQuestDaoImpl().getWebQuest(webQuest_id);

        } catch (Exception e) {
        }

    }

    public String export() throws JAXBException, FileNotFoundException, IOException {

        FacesContext facesContext = FacesContext.getCurrentInstance();
        ServletContext scontext = (ServletContext) facesContext.getExternalContext().getContext();

        ArrayList<String> listFiles = new ArrayList<String>();

//        Long webquest_id = Long.parseLong(FacesContext.getCurrentInstance()
//                .getExternalContext().getRequestParameterMap().get("id"));

        //webQuest = new WebQuestDaoImpl().getWebQuest(webquest_id);

        org.imsglobal.jaxb.content.ObjectFactory imscpFactory = new org.imsglobal.jaxb.content.ObjectFactory();
        org.imsglobal.jaxb.ld.ObjectFactory imsldFactory = new org.imsglobal.jaxb.ld.ObjectFactory();

        //cria manifest
        Manifest manifest = new Manifest();
        manifest.setIdentifier(webQuest.getTitle());

        //cria metadata
        ManifestMetadata metadata = new ManifestMetadata();
        //preenche metadata
        metadata.setSchema("IMS Metadata");
        metadata.setSchemaversion("1.2");

        //adiciona metadata à manifest
        manifest.setMetadata(metadata);

        //cria organizations
        Organizations organizations = new Organizations();

        //cria learningDesign
        LearningDesign learningDesign = new LearningDesign();
        //preenche learningDesign
        learningDesign.setTitle(webQuest.getTitle());
        learningDesign.setIdentifier("LD-" + webQuest.getTitle() + "-" + new Random().nextInt(1000));
        learningDesign.setLevel("A");
        learningDesign.setVersion("1.0");
        learningDesign.setUri("http://webquest-ld.com.br");

        //cria resources
        Resources resources = new Resources();

        if (!webQuest.getLearningObjectives().equals("")) {

            //cria itemModel
            ItemModel itemModel = new ItemModel();

            //preenche itemModel
            itemModel.setTitle("Webquest learning objectives");

            //cria item
            Item item = new Item();

            //preenche item
            item.setTitle(webQuest.getTitle() + "-Learning-Objectives");
            item.setIdentifier("IT-LearningObjectives-" + new Random().nextInt(1000) + "-" + new Random().nextInt(1000));

            //cria resource
            Resource resource = new Resource();
            //preenche resource
            resource.setIdentifier("RE-Learning-Objectives-" + new Random().nextInt(1000) + "-" + new Random().nextInt(1000));
            resource.setType("localcontent");

            //cria html com LearningObjectives
            File fLearningObjectives;

            String sLearningObjectives = new Random().nextInt(1000) + "-Learning-Objectives-" + new Random().nextInt(1000);
            try {
                // Gravando no arquivo  
                fLearningObjectives = new File(scontext.getRealPath("/export/" + sLearningObjectives + ".html"));
                FileOutputStream fos = new FileOutputStream(fLearningObjectives);

                fos.write(webQuest.getLearningObjectives().getBytes());

                fos.close();

                listFiles.add(fLearningObjectives.toString());
            } catch (IOException ee) {
            }

            resource.setHref(sLearningObjectives + ".html");

            //cria arquivo para o xml
            org.imsglobal.jaxb.content.File f = new org.imsglobal.jaxb.content.File();
            f.setHref(sLearningObjectives + ".html");

            //adiciona file à resource
            resource.getFileList().add(f);

            //adiciona resource à item
            item.setIdentifierref(resource);

            //adiciona resource à resources
            resources.getResourceList().add(resource);

            //adiciona item à itemModel
            itemModel.getItemList().add(item);

            //adiciona itemModel à learningDesign LearningObjectives
            learningDesign.setLearningObjectives(itemModel);
        }

        if (!webQuest.getPrerequisites().equals("")) {

            //cria itemModel
            ItemModel itemModel = new ItemModel();

            //preenche itemModel
            itemModel.setTitle("Webquest prerequisites");

            //cria item
            Item item = new Item();
            //preenche item
            item.setTitle(webQuest.getTitle() + "-Prerequisites");
            item.setIdentifier("IT-Prerequisites-" + new Random().nextInt(1000) + "-" + new Random().nextInt(1000));

            //cria resource
            Resource resource = new Resource();
            //preenche resource
            resource.setIdentifier("RE-Prerequisites-" + new Random().nextInt(1000) + "-" + new Random().nextInt(1000));
            resource.setType("localcontent");

            //cria html com Prerequisites
            File fPrerequisites;

            String sPrerequisites = new Random().nextInt(1000) + "-Prerequisites-" + new Random().nextInt(1000);
            try {
                // Gravando no arquivo  
                fPrerequisites = new File(scontext.getRealPath("/export/" + sPrerequisites + ".html"));
                FileOutputStream fos = new FileOutputStream(fPrerequisites);

                fos.write(webQuest.getPrerequisites().getBytes());

                fos.close();

                listFiles.add(fPrerequisites.toString());
            } catch (IOException ee) {
            }

            resource.setHref(sPrerequisites + ".html");

            //cria arquivo para o xml
            org.imsglobal.jaxb.content.File f = new org.imsglobal.jaxb.content.File();
            f.setHref(sPrerequisites + ".html");

            //adiciona file à resource
            resource.getFileList().add(f);

            //adiciona resource à item
            item.setIdentifierref(resource);

            //adiciona resource à resources
            resources.getResourceList().add(resource);

            //adiciona item à itemModel
            itemModel.getItemList().add(item);

            //adiciona itemModel à learningDesign Prerequisites
            learningDesign.setPrerequisites(itemModel);
        }

        //cria components
        Components components = new Components();

        //cria roles
        Roles roles = new Roles();

        for (int i = 0; i < webQuest.getPhases().size(); i++) {

            for (int j = 0; j < webQuest.getPhases().get(i).getActivityies().size(); j++) {

                for (int k = 0; k < webQuest.getPhases().get(i).getActivityies().get(j).getActors().size(); k++) {

                    if (webQuest.getPhases().get(i).getActivityies().get(j).getActors().get(k).getType().equalsIgnoreCase("Aluno")) {
                        //cria learner
                        Learner learner = new Learner();
                        //preenche learner
                        learner.setIdentifier("LE-" + new Random().nextInt(1000) + "-" + new Random().nextInt(1000));
                        learner.setTitle(webQuest.getPhases().get(i).getActivityies().get(j).getActors().get(k).getTitle());

                        //adiciona roles à lista de roles
                        roles.getLearnerList().add(learner);

                    } else if (webQuest.getPhases().get(i).getActivityies().get(j).getActors().get(k).getType().equalsIgnoreCase("Professor")) {
                        //cria staff
                        Staff staff = new Staff();
                        //preenche staff
                        staff.setIdentifier("ST-" + new Random().nextInt(1000) + "-" + new Random().nextInt(1000));
                        staff.setTitle(webQuest.getPhases().get(i).getActivityies().get(j).getActors().get(k).getTitle());

                        //adiciona roles à lista de roles
                        roles.getStaffList().add(staff);
                    }
                }
            }
        }

        //guarda lista de roles em componentes
        components.setRoles(roles);

        //cria environments
        Environments environments = new Environments();

        //cria activities
        Activities activities = new Activities();

        for (int i = 0; i < webQuest.getActivities().size(); i++) {

            //cria environment
            Environment environment = new Environment();
            //preenche environment
            environment.setTitle(webQuest.getActivities().get(i).getTitle() + "-Environment");
            environment.setIdentifier("EN-" + new Random().nextInt(1000) + "-" + new Random().nextInt(1000));

            for (int j = 0; j < webQuest.getActivities().get(i).getResources().size(); j++) {

                if (webQuest.getActivities().get(i).getResources().get(j).getType().equalsIgnoreCase("Objeto de aprendizagem")) {

                    //cria learningObject
                    LearningObject learningObject = new LearningObject();
                    //preenche learningObject
                    learningObject.setIdentifier("LO-" + new Random().nextInt(1000) + "-" + new Random().nextInt(1000));

                    //cria item
                    Item item = new Item();
                    //preenche item
                    item.setTitle(webQuest.getActivities().get(i).getResources().get(j).getTitle() + "-" + (j + 1));
                    item.setIdentifier("IT-" + new Random().nextInt(1000) + "-" + new Random().nextInt(1000));

                    //cria resource
                    Resource resource = new Resource();
                    //preenche resource
                    resource.setIdentifier("RE-" + new Random().nextInt(1000) + "-" + new Random().nextInt(1000));
                    resource.setType("webcontent");
                    resource.setHref(webQuest.getActivities().get(i).getResources().get(j).getHref());

                    //adiciona recurso à item
                    item.setIdentifierref(resource);

                    //adiciona resource à resources
                    resources.getResourceList().add(resource);

                    //adiciona item à learningObject
                    learningObject.getItemList().add(item);

                    //adiciona learningObject ao environment
                    environment.getLearningObjectOrServiceOrEnvironmentRef().add(learningObject);

                } else if (webQuest.getActivities().get(i).getResources().get(j).getType().equalsIgnoreCase("Email")) {

                    //cria service
                    Service service = new Service();
                    //preenche service
                    SendMail sendMail = new SendMail();
                    sendMail.setSelect("s");

                    EmailData emailData = new EmailData();

                    RoleRef roleRef = new RoleRef();
                    roleRef.setRef(roles);

                    emailData.setRoleRef(roleRef);
                    emailData.setEmailPropertyRef("email@email.com");

                    sendMail.getEmailDataList().add(emailData);

                    service.setSendMail(sendMail);
                    service.setIdentifier("m" + new Random().nextInt(1000));

                    //adiciona learningObject ao environment
                    environment.getLearningObjectOrServiceOrEnvironmentRef().add(service);

                } else if (webQuest.getActivities().get(i).getResources().get(j).getType().equalsIgnoreCase("Chat")) {
                    //
                } else if (webQuest.getActivities().get(i).getResources().get(j).getType().equalsIgnoreCase("Monitor")) {
                    //
                }
            }

            //adiciona environment à environments
            environments.getEnvironmentList().add(environment);

            //cria learningActivity
            LearningActivity learningActivity = new LearningActivity();
            learningActivity.setTitle(webQuest.getActivities().get(i).getTitle());
            learningActivity.setIdentifier("LA-" + new Random().nextInt(1000) + "-" + new Random().nextInt(1000));

            //cria environmentRef
            EnvironmentRef environmentRef = new EnvironmentRef();

            //preenche environmentRef
            for (int j = 0; j < environments.getEnvironmentList().size(); j++) {

                if (environments.getEnvironmentList().get(j).getTitle().equalsIgnoreCase(webQuest.getActivities().get(i).getTitle() + "-Environment")) {

                    environmentRef.setRef(environments.getEnvironmentList().get(j));
                }
            }

            //adiciona environment à learningActivity Environments
            learningActivity.getEnvironmentRefList().add(environmentRef);

            if (!webQuest.getActivities().get(i).getActivityDescription().equals("")) {

                //cria itemModel
                ItemModel itemModel = new ItemModel();

                //preenche itemModel
                itemModel.setTitle(webQuest.getActivities().get(i).getTitle() + "-Description");

                //cria item
                Item item = new Item();
                //preenche item
                item.setTitle(webQuest.getActivities().get(i).getTitle() + "-Description-" + (i + 1));
                item.setIdentifier("IT-Description-" + new Random().nextInt(1000) + "-" + new Random().nextInt(1000));

                //cria resource
                Resource resource = new Resource();
                //preenche resource
                resource.setIdentifier("RE-Description-" + new Random().nextInt(1000) + "-" + new Random().nextInt(1000));
                resource.setType("localcontent");

                //cria html com a descrição da atividade
                File activityDescription;

                String fileActivity = new Random().nextInt(1000) + "-Description-" + new Random().nextInt(1000);
                try {
                    // Gravando no arquivo  
                    activityDescription = new File(scontext.getRealPath("/export/" + fileActivity + ".html"));
                    FileOutputStream fos = new FileOutputStream(activityDescription);

                    fos.write(webQuest.getActivities().get(i).getActivityDescription().getBytes());

                    fos.close();

                    listFiles.add(activityDescription.toString());
                } catch (IOException ee) {
                }

                resource.setHref(fileActivity + ".html");

                //cria arquivo para o xml
                org.imsglobal.jaxb.content.File f = new org.imsglobal.jaxb.content.File();
                f.setHref(fileActivity + ".html");

                //adiciona file à resource
                resource.getFileList().add(f);

                //adiciona resource à item
                item.setIdentifierref(resource);

                //adiciona resource à resources
                resources.getResourceList().add(resource);

                //adiciona item à itemModel
                itemModel.getItemList().add(item);

                //adiciona itemModel à learningActivity Description
                learningActivity.setActivityDescription(itemModel);
            }

            if (!webQuest.getActivities().get(i).getLearningObjectives().equals("")) {

                //cria itemModel
                ItemModel itemModel = new ItemModel();

                //preenche itemModel
                itemModel.setTitle(webQuest.getActivities().get(i).getTitle() + "-Learnning-Objectives");

                //cria item
                Item item = new Item();
                //preenche item
                item.setTitle(webQuest.getActivities().get(i).getTitle() + "-Learnning-Objectives-" + (i + 1));
                item.setIdentifier("IT-Learnning-Objectives-" + new Random().nextInt(1000) + "-" + new Random().nextInt(1000));

                //cria resource
                Resource resource = new Resource();
                //preenche resource
                resource.setIdentifier("RE-Learnning-Objectives-" + new Random().nextInt(1000) + "-" + new Random().nextInt(1000));
                resource.setType("localcontent");

                //cria html com a descrição da atividade
                File learningObjectivesDescription;

                String fileLearningObjectives = new Random().nextInt(1000) + "-Learnning-Objectives-" + new Random().nextInt(1000);
                try {
                    // Gravando no arquivo  
                    learningObjectivesDescription = new File(scontext.getRealPath("/export/" + fileLearningObjectives + ".html"));
                    FileOutputStream fos = new FileOutputStream(learningObjectivesDescription);

                    fos.write(webQuest.getActivities().get(i).getLearningObjectives().getBytes());

                    fos.close();

                    listFiles.add(learningObjectivesDescription.toString());
                } catch (IOException ee) {
                }

                resource.setHref(fileLearningObjectives + ".html");

                //cria arquivo para o xml
                org.imsglobal.jaxb.content.File f = new org.imsglobal.jaxb.content.File();
                f.setHref(fileLearningObjectives + ".html");

                //adiciona file à resource
                resource.getFileList().add(f);

                //adiciona resource à item
                item.setIdentifierref(resource);

                //adiciona resource à resources
                resources.getResourceList().add(resource);

                //adiciona item à itemModel
                itemModel.getItemList().add(item);

                //adiciona itemModel à learningActivity LearningObjectives
                learningActivity.setLearningObjectives(itemModel);
            }

            if (!webQuest.getActivities().get(i).getPrerequisites().equals("")) {

                //cria itemModel
                ItemModel itemModel = new ItemModel();

                //preenche itemModel
                itemModel.setTitle(webQuest.getActivities().get(i).getTitle() + "-Prerequisites");

                //cria item
                Item item = new Item();
                //preenche item
                item.setTitle(webQuest.getActivities().get(i).getTitle() + "-Prerequisites-" + (i + 1));
                item.setIdentifier("IT-Prerequisites-" + new Random().nextInt(1000) + "-" + new Random().nextInt(1000));

                //cria resource
                Resource resource = new Resource();
                //preenche resource
                resource.setIdentifier("RE-Prerequisites-" + new Random().nextInt(1000) + "-" + new Random().nextInt(1000));
                resource.setType("localcontent");

                //cria html com a descrição da atividade
                File Prerequisites;

                String filePrerequisites = new Random().nextInt(1000) + "-Prerequisites-" + new Random().nextInt(1000);
                try {
                    // Gravando no arquivo  
                    Prerequisites = new File(scontext.getRealPath("/export/" + filePrerequisites + ".html"));
                    FileOutputStream fos = new FileOutputStream(Prerequisites);

                    fos.write(webQuest.getActivities().get(i).getPrerequisites().getBytes());

                    fos.close();

                    listFiles.add(Prerequisites.toString());
                } catch (IOException ee) {
                }

                resource.setHref(filePrerequisites + ".html");

                //cria arquivo para o xml
                org.imsglobal.jaxb.content.File f = new org.imsglobal.jaxb.content.File();
                f.setHref(filePrerequisites + ".html");

                //adiciona file à resource
                resource.getFileList().add(f);

                //adiciona resource à item
                item.setIdentifierref(resource);

                //adiciona resource à resources
                resources.getResourceList().add(resource);

                //adiciona item à itemModel
                itemModel.getItemList().add(item);

                //adiciona itemModel à learningActivity Prerequisites
                learningActivity.setPrerequisites(itemModel);
            }

            //cria completeActivity
            CompleteActivity completeActivity = new CompleteActivity();

            //preenche completeActivity
            if (webQuest.getActivities().get(i).getCompleteActivity().equalsIgnoreCase("1")) {

                //adiciona userChoice à completeActivity
                completeActivity.setUserChoice(new UserChoice());

            } else if (webQuest.getActivities().get(i).getCompleteActivity().equalsIgnoreCase("2")) {

                //cria timeLimit
                TimeLimit timeLimit = new TimeLimit();

                Date date = webQuest.getActivities().get(i).getDate();
                
                //duração anos meses dias horas minutus segundos                A  M  D  h  m  s
                Duration duration = new DatatypeFactoryImpl().newDuration(true, (date.getYear()+1900), (date.getMonth()+1), date.getDate(), 23, 59, 59);

                //preenche timeLimit
                timeLimit.setValue(duration);

                //adiciona timeLimit à completeActivity
                completeActivity.setTimeLimit(timeLimit);
            }

            //adiciona à learningActivity CompleteActivity
            learningActivity.setCompleteActivity(completeActivity);

            //adiciona learningActivity1 à activities
            activities.getLearningActivityOrSupportActivityOrActivityStructure().add(learningActivity);
        }

        //adiciona environments à components Environments
        components.setEnvironments(environments);

        for (int i = 0; i < webQuest.getPhases().size(); i++) {

            //cria activityStructure
            ActivityStructure activityStructure = new ActivityStructure();
            //preenche activityStructure
            activityStructure.setTitle(webQuest.getPhases().get(i).getTitle());
            activityStructure.setIdentifier("AS-" + new Random().nextInt(1000) + "-" + new Random().nextInt(1000));

            activityStructure.setStructureType("sequence");

            List<Activity> listActivities = new ActivityDaoImpl().listActivities(webQuest.getPhases().get(i).getId());

            if (listActivities != null) {

                for (int j = 0; j < listActivities.size(); j++) {

                    //cria learningActivityRef
                    LearningActivityRef learningActivityRef = new LearningActivityRef();

                    if (activities.getLearningActivityOrSupportActivityOrActivityStructure() != null) {

                        for (int k = 0; k < activities.getLearningActivityOrSupportActivityOrActivityStructure().size(); k++) {

                            if (activities.getLearningActivityOrSupportActivityOrActivityStructure().get(k) instanceof LearningActivity) {

                                LearningActivity la = (LearningActivity) activities.getLearningActivityOrSupportActivityOrActivityStructure().get(k);

                                if (la.getTitle().equalsIgnoreCase(listActivities.get(j).getTitle())) {

                                    //preenche learningActivityRef
                                    learningActivityRef.setRef(la);
                                }
                            }
                        }
                    }

                    //adiciona learningActivityRef à activityStructure
                    activityStructure.getLearningActivityRefOrSupportActivityRefOrUnitOfLearningHref().add(learningActivityRef);
                }
            }

            //adiciona activityStructure à activities
            activities.getLearningActivityOrSupportActivityOrActivityStructure().add(activityStructure);
        }

        //adiciona activities à components Activities
        components.setActivities(activities);

        //adiciona components à learningDesign
        learningDesign.setComponents(components);

        //cria method
        Method method = new Method();

        //cria play
        Play play = new Play();
        //preenche play
        play.setTitle("Play-" + webQuest.getTitle());
        play.setIdentifier("Play-" + new Random().nextInt(1000) + "-" + new Random().nextInt(1000));

        for (int i = 0; i < webQuest.getPhases().size(); i++) {

            //cria act
            Act act = new Act();
            //preenche act
            act.setTitle("Act-" + webQuest.getPhases().get(i).getTitle());
            act.setIdentifier("Act-" + new Random().nextInt(1000) + "-" + new Random().nextInt(1000));

            List<Activity> listActivities = new ActivityDaoImpl().listActivities(webQuest.getPhases().get(i).getId());

            if (listActivities != null) {

                //cria role part
                RolePart rolePart = null;
                for (int j = 0; j < listActivities.size(); j++) {

                    //cria role part
                    rolePart = new RolePart();
                    //preenche rolePart
                    rolePart.setTitle("Role-Part-" + listActivities.get(j).getTitle());
                    rolePart.setIdentifier("Role-Part-" + new Random().nextInt(1000) + "-" + new Random().nextInt(1000));

                    if (listActivities.get(j).getActors() != null) {

                        for (int k = 0; k < listActivities.get(j).getActors().size(); k++) {

                            if (roles.getLearnerList() != null) {

                                for (int l = 0; l < roles.getLearnerList().size(); l++) {

                                    if (roles.getLearnerList().get(l).getTitle().equalsIgnoreCase(listActivities.get(j).getActors().get(k).getTitle())) {

                                        //cria roleRef
                                        RoleRef roleRef = new RoleRef();
                                        //preenche roleRef
                                        roleRef.setRef(roles.getLearnerList().get(l));

                                        //adiciona roleRef à rolePart
                                        rolePart.setRoleRef(roleRef);
                                    }
                                }
                            }
                        }
                    }

                    //cria activityStructureRef
                    ActivityStructureRef activityStructureRef = new ActivityStructureRef();

                    if (activities.getLearningActivityOrSupportActivityOrActivityStructure() != null) {

                        for (int k = 0; k < activities.getLearningActivityOrSupportActivityOrActivityStructure().size(); k++) {

                            if (activities.getLearningActivityOrSupportActivityOrActivityStructure().get(k) instanceof ActivityStructure) {

                                ActivityStructure as = (ActivityStructure) activities.getLearningActivityOrSupportActivityOrActivityStructure().get(k);

                                if (as.getTitle().equalsIgnoreCase(webQuest.getPhases().get(i).getTitle())) {

                                    //preenche activityStructureRef
                                    activityStructureRef.setRef(as);
                                }
                            }
                        }
                    }

                    //adiciona activityStructureRef à rolePart
                    rolePart.setActivityStructureRef(activityStructureRef);

                    //adiciona rolePart à act
                    act.getRolePartList().add(rolePart);
                }
                
                //cria when act completed
                WhenRolePartCompleted whenRolePartCompleted = new WhenRolePartCompleted();
                whenRolePartCompleted.setRef(rolePart);

                //cria CompleteAct
                CompleteAct completeAct = new CompleteAct();
                completeAct.getWhenRolePartCompletedList().add(whenRolePartCompleted);

                act.setCompleteAct(completeAct);
            }

            //adiciona act à play
            play.getActList().add(act);
        }

        //cria completeActivity
        CompletePlay completePlay = new CompletePlay();
        completePlay.setWhenLastActCompleted(new WhenLastActCompleted());

        play.setCompletePlay(completePlay);

        ///adiciona play à method
        method.getPlayList().add(play);

        //cria when play completed
        WhenPlayCompleted whenPlayCompleted = new WhenPlayCompleted();
        whenPlayCompleted.setRef(play);

        //cria completeActivity
        CompleteUnitOfLearning completeUnitOfLearning = new CompleteUnitOfLearning();
        completeUnitOfLearning.getWhenPlayCompletedList().add(whenPlayCompleted);

        method.setCompleteUnitOfLearning(completeUnitOfLearning);

        //adiciona method à learningDesign
        learningDesign.setMethod(method);

        //adiciona learningDesign à organizations
        organizations.getAny().add(imsldFactory.createLearningDesign(learningDesign));

        //adiciona organizations à manifest
        manifest.setOrganizations(organizations);

        //adiciona resources à manifest
        manifest.setResources(resources);

        //inicia exportação
        JAXBContext jaxbContext = JAXBContext.newInstance("org.imsglobal.jaxb.content:org.imsglobal.jaxb.ld");

        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");

        String file = scontext.getRealPath("/export/imsmanifest.xml");

        marshaller.marshal(imscpFactory.createManifest(manifest), new File(file));

        listFiles.add(file);

        zipar(listFiles);

        InputStream stream = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getResourceAsStream("/export/webQuest.zip");
        setDownloadContentProperty(new DefaultStreamedContent(stream, "application/zip", "webQuest.zip"));

        for (int i = 0; i < listFiles.size(); i++) {
            new File(listFiles.get(i)).delete();
        }
        
        return "webquest-manager?faces-redirect=true";
    }

    public void setDownloadContentProperty(StreamedContent downloadContentProperty) {
        this.downloadContentProperty = downloadContentProperty;
    }

    public StreamedContent getDownloadContentProperty() {
        return downloadContentProperty;
    }

    // These are the files to include in the ZIP file
    public void zipar(ArrayList<String> listFiles) {

        int cont;

        // Create a buffer for reading the files
        byte[] dados = new byte[1024];

        BufferedInputStream origem;
        FileInputStream streamDeEntrada;
        FileOutputStream destino;
        ZipOutputStream saida;
        ZipEntry entry;

        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ServletContext scontext = (ServletContext) facesContext.getExternalContext().getContext();
            String file2 = scontext.getRealPath("/export/webQuest.zip");
            // Create the ZIP file
            destino = new FileOutputStream(new File(file2));
            saida = new ZipOutputStream(new BufferedOutputStream(destino));

            for (String filename : listFiles) {

                File file = new File(filename);
                streamDeEntrada = new FileInputStream(file);
                origem = new BufferedInputStream(streamDeEntrada, 1024);
                entry = new ZipEntry(file.getName());
                // Add ZIP entry to output stream.
                saida.putNextEntry(entry);
                // Transfer bytes from the file to the ZIP file
                while ((cont = origem.read(dados, 0, 1024)) > 0) {
                    saida.write(dados, 0, cont);
                }
                // Complete the entry
//                saida.closeEntry();
                origem.close();
            }

            // Complete the ZIP file
            saida.close();
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public static void main(String[] args) throws JAXBException {

        FacesContext facesContext = FacesContext.getCurrentInstance();
//        ServletContext scontext = (ServletContext) facesContext.getExternalContext().getContext();
        ArrayList<String> listFiles = new ArrayList<String>();

        Long webquest_id = Long.valueOf("2");

        WebQuest webQuest = new WebQuestDaoImpl().getWebQuest(webquest_id);

        org.imsglobal.jaxb.content.ObjectFactory imscpFactory = new org.imsglobal.jaxb.content.ObjectFactory();
        org.imsglobal.jaxb.ld.ObjectFactory imsldFactory = new org.imsglobal.jaxb.ld.ObjectFactory();

        //cria manifest
        Manifest manifest = new Manifest();
        manifest.setIdentifier(webQuest.getTitle());

        //cria metadata
        ManifestMetadata metadata = new ManifestMetadata();
        //preenche metadata
        metadata.setSchema("IMS Metadata");
        metadata.setSchemaversion("1.2");

        //adiciona metadata à manifest
        manifest.setMetadata(metadata);

        //cria organizations
        Organizations organizations = new Organizations();

        //cria learningDesign
        LearningDesign learningDesign = new LearningDesign();
        //preenche learningDesign
        learningDesign.setTitle(webQuest.getTitle());
        learningDesign.setIdentifier("LD-" + webQuest.getTitle() + "-" + new Random().nextInt(1000));
        learningDesign.setLevel("A");
        learningDesign.setVersion("1.0");
        learningDesign.setUri("http://webquest-ld.com.br");

        //cria resources
        Resources resources = new Resources();

        if (!webQuest.getLearningObjectives().equals("")) {

            //cria itemModel
            ItemModel itemModel = new ItemModel();

            //preenche itemModel
            itemModel.setTitle("Webquest learning objectives");

            //cria item
            Item item = new Item();

            //preenche item
            item.setTitle(webQuest.getTitle() + "-Learning-Objectives");
            item.setIdentifier("IT-LearningObjectives-" + new Random().nextInt(1000) + "-" + new Random().nextInt(1000));

            //cria resource
            Resource resource = new Resource();
            //preenche resource
            resource.setIdentifier("RE-Learning-Objectives-" + new Random().nextInt(1000) + "-" + new Random().nextInt(1000));
            resource.setType("localcontent");

            //cria html com LearningObjectives
            File fLearningObjectives;

            String sLearningObjectives = new Random().nextInt(1000) + "-Learning-Objectives-" + new Random().nextInt(1000);
            try {
                // Gravando no arquivo  
                fLearningObjectives = new File(sLearningObjectives + ".html");
                FileOutputStream fos = new FileOutputStream(fLearningObjectives);

                fos.write(webQuest.getLearningObjectives().getBytes());

                fos.close();

                listFiles.add(fLearningObjectives.toString());
            } catch (IOException ee) {
            }

            resource.setHref(sLearningObjectives + ".html");

            //cria arquivo para o xml
            org.imsglobal.jaxb.content.File f = new org.imsglobal.jaxb.content.File();
            f.setHref(sLearningObjectives + ".html");

            //adiciona file à resource
            resource.getFileList().add(f);

            //adiciona resource à item
            item.setIdentifierref(resource);

            //adiciona resource à resources
            resources.getResourceList().add(resource);

            //adiciona item à itemModel
            itemModel.getItemList().add(item);

            //adiciona itemModel à learningDesign LearningObjectives
            learningDesign.setLearningObjectives(itemModel);
        }

        if (!webQuest.getPrerequisites().equals("")) {

            //cria itemModel
            ItemModel itemModel = new ItemModel();

            //preenche itemModel
            itemModel.setTitle("Webquest prerequisites");

            //cria item
            Item item = new Item();
            //preenche item
            item.setTitle(webQuest.getTitle() + "-Prerequisites");
            item.setIdentifier("IT-Prerequisites-" + new Random().nextInt(1000) + "-" + new Random().nextInt(1000));

            //cria resource
            Resource resource = new Resource();
            //preenche resource
            resource.setIdentifier("RE-Prerequisites-" + new Random().nextInt(1000) + "-" + new Random().nextInt(1000));
            resource.setType("localcontent");

            //cria html com Prerequisites
            File fPrerequisites;

            String sPrerequisites = new Random().nextInt(1000) + "-Prerequisites-" + new Random().nextInt(1000);
            try {
                // Gravando no arquivo  
                fPrerequisites = new File(sPrerequisites + ".html");
                FileOutputStream fos = new FileOutputStream(fPrerequisites);

                fos.write(webQuest.getPrerequisites().getBytes());

                fos.close();

                listFiles.add(fPrerequisites.toString());
            } catch (IOException ee) {
            }

            resource.setHref(sPrerequisites + ".html");

            //cria arquivo para o xml
            org.imsglobal.jaxb.content.File f = new org.imsglobal.jaxb.content.File();
            f.setHref(sPrerequisites + ".html");

            //adiciona file à resource
            resource.getFileList().add(f);

            //adiciona resource à item
            item.setIdentifierref(resource);

            //adiciona resource à resources
            resources.getResourceList().add(resource);

            //adiciona item à itemModel
            itemModel.getItemList().add(item);

            //adiciona itemModel à learningDesign Prerequisites
            learningDesign.setPrerequisites(itemModel);
        }

        //cria components
        Components components = new Components();

        //cria roles
        Roles roles = new Roles();

        for (int i = 0; i < webQuest.getPhases().size(); i++) {

            for (int j = 0; j < webQuest.getPhases().get(i).getActivityies().size(); j++) {

                for (int k = 0; k < webQuest.getPhases().get(i).getActivityies().get(j).getActors().size(); k++) {

                    if (webQuest.getPhases().get(i).getActivityies().get(j).getActors().get(k).getType().equalsIgnoreCase("Aluno")) {
                        //cria learner
                        Learner learner = new Learner();
                        //preenche learner
                        learner.setIdentifier("LE-" + new Random().nextInt(1000) + "-" + new Random().nextInt(1000));
                        learner.setTitle(webQuest.getPhases().get(i).getActivityies().get(j).getActors().get(k).getTitle());

                        //adiciona roles à lista de roles
                        roles.getLearnerList().add(learner);

                    } else if (webQuest.getPhases().get(i).getActivityies().get(j).getActors().get(k).getType().equalsIgnoreCase("Professor")) {
                        //cria staff
                        Staff staff = new Staff();
                        //preenche staff
                        staff.setIdentifier("ST-" + new Random().nextInt(1000) + "-" + new Random().nextInt(1000));
                        staff.setTitle(webQuest.getPhases().get(i).getActivityies().get(j).getActors().get(k).getTitle());

                        //adiciona roles à lista de roles
                        roles.getStaffList().add(staff);
                    }
                }
            }
        }

        //guarda lista de roles em componentes
        components.setRoles(roles);

        //cria environments
        Environments environments = new Environments();

        //cria activities
        Activities activities = new Activities();

        for (int i = 0; i < webQuest.getActivities().size(); i++) {

            //cria environment
            Environment environment = new Environment();
            //preenche environment
            environment.setTitle(webQuest.getActivities().get(i).getTitle() + "-Environment");
            environment.setIdentifier("EN-" + new Random().nextInt(1000) + "-" + new Random().nextInt(1000));

            for (int j = 0; j < webQuest.getActivities().get(i).getResources().size(); j++) {

                if (webQuest.getActivities().get(i).getResources().get(j).getType().equalsIgnoreCase("Objeto de aprendizagem")) {

                    //cria learningObject
                    LearningObject learningObject = new LearningObject();
                    //preenche learningObject
                    learningObject.setIdentifier("LO-" + new Random().nextInt(1000) + "-" + new Random().nextInt(1000));

                    //cria item
                    Item item = new Item();
                    //preenche item
                    item.setTitle(webQuest.getActivities().get(i).getResources().get(j).getTitle() + "-" + (j + 1));
                    item.setIdentifier("IT-" + new Random().nextInt(1000) + "-" + new Random().nextInt(1000));

                    //cria resource
                    Resource resource = new Resource();
                    //preenche resource
                    resource.setIdentifier("RE-" + new Random().nextInt(1000) + "-" + new Random().nextInt(1000));
                    resource.setType("webcontent");
                    resource.setHref(webQuest.getActivities().get(i).getResources().get(j).getHref());

                    //adiciona recurso à item
                    item.setIdentifierref(resource);

                    //adiciona resource à resources
                    resources.getResourceList().add(resource);

                    //adiciona item à learningObject
                    learningObject.getItemList().add(item);

                    //adiciona learningObject ao environment
                    environment.getLearningObjectOrServiceOrEnvironmentRef().add(learningObject);

                } else if (webQuest.getActivities().get(i).getResources().get(j).getType().equalsIgnoreCase("Email")) {

                    //cria service
                    Service service = new Service();
                    //preenche service
                    SendMail sendMail = new SendMail();
                    sendMail.setSelect("s");

                    EmailData emailData = new EmailData();

                    RoleRef roleRef = new RoleRef();
                    roleRef.setRef(roles);

                    emailData.setRoleRef(roleRef);
                    emailData.setEmailPropertyRef("email@email.com");

                    sendMail.getEmailDataList().add(emailData);

                    service.setSendMail(sendMail);
                    service.setIdentifier("m" + new Random().nextInt(1000));

                    //adiciona learningObject ao environment
                    environment.getLearningObjectOrServiceOrEnvironmentRef().add(service);

                } else if (webQuest.getActivities().get(i).getResources().get(j).getType().equalsIgnoreCase("Chat")) {
                    //
                } else if (webQuest.getActivities().get(i).getResources().get(j).getType().equalsIgnoreCase("Monitor")) {
                    //
                }
            }

            //adiciona environment à environments
            environments.getEnvironmentList().add(environment);

            //cria learningActivity
            LearningActivity learningActivity = new LearningActivity();
            learningActivity.setTitle(webQuest.getActivities().get(i).getTitle());
            learningActivity.setIdentifier("LA-" + new Random().nextInt(1000) + "-" + new Random().nextInt(1000));

            //cria environmentRef
            EnvironmentRef environmentRef = new EnvironmentRef();

            //preenche environmentRef
            for (int j = 0; j < environments.getEnvironmentList().size(); j++) {

                if (environments.getEnvironmentList().get(j).getTitle().equalsIgnoreCase(webQuest.getActivities().get(i).getTitle() + "-Environment")) {

                    environmentRef.setRef(environments.getEnvironmentList().get(j));
                }
            }

            //adiciona environment à learningActivity Environments
            learningActivity.getEnvironmentRefList().add(environmentRef);

            if (!webQuest.getActivities().get(i).getActivityDescription().equals("")) {

                //cria itemModel
                ItemModel itemModel = new ItemModel();

                //preenche itemModel
                itemModel.setTitle(webQuest.getActivities().get(i).getTitle() + "-Description");

                //cria item
                Item item = new Item();
                //preenche item
                item.setTitle(webQuest.getActivities().get(i).getTitle() + "-Description-" + (i + 1));
                item.setIdentifier("IT-Description-" + new Random().nextInt(1000) + "-" + new Random().nextInt(1000));

                //cria resource
                Resource resource = new Resource();
                //preenche resource
                resource.setIdentifier("RE-Description-" + new Random().nextInt(1000) + "-" + new Random().nextInt(1000));
                resource.setType("localcontent");

                //cria html com a descrição da atividade
                File activityDescription;

                String fileActivity = new Random().nextInt(1000) + "-Description-" + new Random().nextInt(1000);
                try {
                    // Gravando no arquivo  
                    activityDescription = new File(fileActivity + ".html");
                    FileOutputStream fos = new FileOutputStream(activityDescription);

                    fos.write(webQuest.getActivities().get(i).getActivityDescription().getBytes());

                    fos.close();

                    listFiles.add(activityDescription.toString());
                } catch (IOException ee) {
                }

                resource.setHref(fileActivity + ".html");

                //cria arquivo para o xml
                org.imsglobal.jaxb.content.File f = new org.imsglobal.jaxb.content.File();
                f.setHref(fileActivity + ".html");

                //adiciona file à resource
                resource.getFileList().add(f);

                //adiciona resource à item
                item.setIdentifierref(resource);

                //adiciona resource à resources
                resources.getResourceList().add(resource);

                //adiciona item à itemModel
                itemModel.getItemList().add(item);

                //adiciona itemModel à learningActivity Description
                learningActivity.setActivityDescription(itemModel);
            }

            if (!webQuest.getActivities().get(i).getLearningObjectives().equals("")) {

                //cria itemModel
                ItemModel itemModel = new ItemModel();

                //preenche itemModel
                itemModel.setTitle(webQuest.getActivities().get(i).getTitle() + "-Learnning-Objectives");

                //cria item
                Item item = new Item();
                //preenche item
                item.setTitle(webQuest.getActivities().get(i).getTitle() + "-Learnning-Objectives-" + (i + 1));
                item.setIdentifier("IT-Learnning-Objectives-" + new Random().nextInt(1000) + "-" + new Random().nextInt(1000));

                //cria resource
                Resource resource = new Resource();
                //preenche resource
                resource.setIdentifier("RE-Learnning-Objectives-" + new Random().nextInt(1000) + "-" + new Random().nextInt(1000));
                resource.setType("localcontent");

                //cria html com a descrição da atividade
                File learningObjectivesDescription;

                String fileLearningObjectives = new Random().nextInt(1000) + "-Learnning-Objectives-" + new Random().nextInt(1000);
                try {
                    // Gravando no arquivo  
                    learningObjectivesDescription = new File(fileLearningObjectives + ".html");
                    FileOutputStream fos = new FileOutputStream(learningObjectivesDescription);

                    fos.write(webQuest.getActivities().get(i).getLearningObjectives().getBytes());

                    fos.close();

                    listFiles.add(learningObjectivesDescription.toString());
                } catch (IOException ee) {
                }

                resource.setHref(fileLearningObjectives + ".html");

                //cria arquivo para o xml
                org.imsglobal.jaxb.content.File f = new org.imsglobal.jaxb.content.File();
                f.setHref(fileLearningObjectives + ".html");

                //adiciona file à resource
                resource.getFileList().add(f);

                //adiciona resource à item
                item.setIdentifierref(resource);

                //adiciona resource à resources
                resources.getResourceList().add(resource);

                //adiciona item à itemModel
                itemModel.getItemList().add(item);

                //adiciona itemModel à learningActivity LearningObjectives
                learningActivity.setLearningObjectives(itemModel);
            }

            if (!webQuest.getActivities().get(i).getPrerequisites().equals("")) {

                //cria itemModel
                ItemModel itemModel = new ItemModel();

                //preenche itemModel
                itemModel.setTitle(webQuest.getActivities().get(i).getTitle() + "-Prerequisites");

                //cria item
                Item item = new Item();
                //preenche item
                item.setTitle(webQuest.getActivities().get(i).getTitle() + "-Prerequisites-" + (i + 1));
                item.setIdentifier("IT-Prerequisites-" + new Random().nextInt(1000) + "-" + new Random().nextInt(1000));

                //cria resource
                Resource resource = new Resource();
                //preenche resource
                resource.setIdentifier("RE-Prerequisites-" + new Random().nextInt(1000) + "-" + new Random().nextInt(1000));
                resource.setType("localcontent");

                //cria html com a descrição da atividade
                File Prerequisites;

                String filePrerequisites = new Random().nextInt(1000) + "-Prerequisites-" + new Random().nextInt(1000);
                try {
                    // Gravando no arquivo  
                    Prerequisites = new File(filePrerequisites + ".html");
                    FileOutputStream fos = new FileOutputStream(Prerequisites);

                    fos.write(webQuest.getActivities().get(i).getPrerequisites().getBytes());

                    fos.close();

                    listFiles.add(Prerequisites.toString());
                } catch (IOException ee) {
                }

                resource.setHref(filePrerequisites + ".html");

                //cria arquivo para o xml
                org.imsglobal.jaxb.content.File f = new org.imsglobal.jaxb.content.File();
                f.setHref(filePrerequisites + ".html");

                //adiciona file à resource
                resource.getFileList().add(f);

                //adiciona resource à item
                item.setIdentifierref(resource);

                //adiciona resource à resources
                resources.getResourceList().add(resource);

                //adiciona item à itemModel
                itemModel.getItemList().add(item);

                //adiciona itemModel à learningActivity Prerequisites
                learningActivity.setPrerequisites(itemModel);
            }

            //cria completeActivity
            CompleteActivity completeActivity = new CompleteActivity();

            //preenche completeActivity
            if (webQuest.getActivities().get(i).getCompleteActivity().equalsIgnoreCase("1")) {

                //adiciona userChoice à completeActivity
                completeActivity.setUserChoice(new UserChoice());

            } else if (webQuest.getActivities().get(i).getCompleteActivity().equalsIgnoreCase("2")) {

                //cria timeLimit
                TimeLimit timeLimit = new TimeLimit();

                Date date = webQuest.getActivities().get(i).getDate();
                
                //duração anos meses dias horas minutus segundos                A  M  D  h  m  s
                Duration duration = new DatatypeFactoryImpl().newDuration(true, (date.getYear()+1900), (date.getMonth()+1), date.getDate(), 23, 59, 59);

                //preenche timeLimit
                timeLimit.setValue(duration);

                //adiciona timeLimit à completeActivity
                completeActivity.setTimeLimit(timeLimit);
            }

            //adiciona à learningActivity CompleteActivity
            learningActivity.setCompleteActivity(completeActivity);

            //adiciona learningActivity1 à activities
            activities.getLearningActivityOrSupportActivityOrActivityStructure().add(learningActivity);
        }

        //adiciona environments à components Environments
        components.setEnvironments(environments);

        for (int i = 0; i < webQuest.getPhases().size(); i++) {

            //cria activityStructure
            ActivityStructure activityStructure = new ActivityStructure();
            //preenche activityStructure
            activityStructure.setTitle(webQuest.getPhases().get(i).getTitle());
            activityStructure.setIdentifier("AS-" + new Random().nextInt(1000) + "-" + new Random().nextInt(1000));

            activityStructure.setStructureType("sequence");

            List<Activity> listActivities = new ActivityDaoImpl().listActivities(webQuest.getPhases().get(i).getId());

            if (listActivities != null) {

                for (int j = 0; j < listActivities.size(); j++) {

                    //cria learningActivityRef
                    LearningActivityRef learningActivityRef = new LearningActivityRef();

                    if (activities.getLearningActivityOrSupportActivityOrActivityStructure() != null) {

                        for (int k = 0; k < activities.getLearningActivityOrSupportActivityOrActivityStructure().size(); k++) {

                            if (activities.getLearningActivityOrSupportActivityOrActivityStructure().get(k) instanceof LearningActivity) {

                                LearningActivity la = (LearningActivity) activities.getLearningActivityOrSupportActivityOrActivityStructure().get(k);

                                if (la.getTitle().equalsIgnoreCase(listActivities.get(j).getTitle())) {

                                    //preenche learningActivityRef
                                    learningActivityRef.setRef(la);
                                }
                            }
                        }
                    }

                    //adiciona learningActivityRef à activityStructure
                    activityStructure.getLearningActivityRefOrSupportActivityRefOrUnitOfLearningHref().add(learningActivityRef);
                }
            }

            //adiciona activityStructure à activities
            activities.getLearningActivityOrSupportActivityOrActivityStructure().add(activityStructure);
        }

        //adiciona activities à components Activities
        components.setActivities(activities);

        //adiciona components à learningDesign
        learningDesign.setComponents(components);

        //cria method
        Method method = new Method();

        //cria play
        Play play = new Play();
        //preenche play
        play.setTitle("Play-" + webQuest.getTitle());
        play.setIdentifier("Play-" + new Random().nextInt(1000) + "-" + new Random().nextInt(1000));

        for (int i = 0; i < webQuest.getPhases().size(); i++) {

            //cria act
            Act act = new Act();
            //preenche act
            act.setTitle("Act-" + webQuest.getPhases().get(i).getTitle());
            act.setIdentifier("Act-" + new Random().nextInt(1000) + "-" + new Random().nextInt(1000));

            List<Activity> listActivities = new ActivityDaoImpl().listActivities(webQuest.getPhases().get(i).getId());

            if (listActivities != null) {

                //cria role part
                RolePart rolePart = null;
                for (int j = 0; j < listActivities.size(); j++) {

                    //cria role part
                    rolePart = new RolePart();
                    //preenche rolePart
                    rolePart.setTitle("Role-Part-" + listActivities.get(j).getTitle());
                    rolePart.setIdentifier("Role-Part-" + new Random().nextInt(1000) + "-" + new Random().nextInt(1000));

                    if (listActivities.get(j).getActors() != null) {

                        for (int k = 0; k < listActivities.get(j).getActors().size(); k++) {

                            if (roles.getLearnerList() != null) {

                                for (int l = 0; l < roles.getLearnerList().size(); l++) {

                                    if (roles.getLearnerList().get(l).getTitle().equalsIgnoreCase(listActivities.get(j).getActors().get(k).getTitle())) {

                                        //cria roleRef
                                        RoleRef roleRef = new RoleRef();
                                        //preenche roleRef
                                        roleRef.setRef(roles.getLearnerList().get(l));

                                        //adiciona roleRef à rolePart
                                        rolePart.setRoleRef(roleRef);
                                    }
                                }
                            }
                        }
                    }

                    //cria activityStructureRef
                    ActivityStructureRef activityStructureRef = new ActivityStructureRef();

                    if (activities.getLearningActivityOrSupportActivityOrActivityStructure() != null) {

                        for (int k = 0; k < activities.getLearningActivityOrSupportActivityOrActivityStructure().size(); k++) {

                            if (activities.getLearningActivityOrSupportActivityOrActivityStructure().get(k) instanceof ActivityStructure) {

                                ActivityStructure as = (ActivityStructure) activities.getLearningActivityOrSupportActivityOrActivityStructure().get(k);

                                if (as.getTitle().equalsIgnoreCase(webQuest.getPhases().get(i).getTitle())) {

                                    //preenche activityStructureRef
                                    activityStructureRef.setRef(as);
                                }
                            }
                        }
                    }

                    //adiciona activityStructureRef à rolePart
                    rolePart.setActivityStructureRef(activityStructureRef);

                    //adiciona rolePart à act
                    act.getRolePartList().add(rolePart);
                }
                
                //cria when act completed
                WhenRolePartCompleted whenRolePartCompleted = new WhenRolePartCompleted();
                whenRolePartCompleted.setRef(rolePart);

                //cria CompleteAct
                CompleteAct completeAct = new CompleteAct();
                completeAct.getWhenRolePartCompletedList().add(whenRolePartCompleted);

                act.setCompleteAct(completeAct);
            }

            //adiciona act à play
            play.getActList().add(act);
        }

        //cria completeActivity
        CompletePlay completePlay = new CompletePlay();
        completePlay.setWhenLastActCompleted(new WhenLastActCompleted());

        play.setCompletePlay(completePlay);

        ///adiciona play à method
        method.getPlayList().add(play);

        //cria when play completed
        WhenPlayCompleted whenPlayCompleted = new WhenPlayCompleted();
        whenPlayCompleted.setRef(play);

        //cria completeActivity
        CompleteUnitOfLearning completeUnitOfLearning = new CompleteUnitOfLearning();
        completeUnitOfLearning.getWhenPlayCompletedList().add(whenPlayCompleted);

        method.setCompleteUnitOfLearning(completeUnitOfLearning);

        //adiciona method à learningDesign
        learningDesign.setMethod(method);

        //adiciona learningDesign à organizations
        organizations.getAny().add(imsldFactory.createLearningDesign(learningDesign));

        //adiciona organizations à manifest
        manifest.setOrganizations(organizations);

        //adiciona resources à manifest
        manifest.setResources(resources);

        //inicia exportação
        JAXBContext jaxbContext = JAXBContext.newInstance("org.imsglobal.jaxb.content:org.imsglobal.jaxb.ld");

        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");

        String file = "imsmanifest.xml";

        marshaller.marshal(imscpFactory.createManifest(manifest), new File(file));

        listFiles.add(file);

//        for (int i = 0; i < listFiles.size(); i++) {
//            new File(listFiles.get(i)).delete();
//        }
        //zipar(listFiles);

    }

    public void rrrr() throws JAXBException {

        //
        LearningActivity learningActivity;
        ItemModel itemModel;
        Item item;
        CompleteActivity completeActivity;
        Resource resource;
        Resources resources;

        org.imsglobal.jaxb.content.ObjectFactory imscpFactory = new org.imsglobal.jaxb.content.ObjectFactory();
        org.imsglobal.jaxb.ld.ObjectFactory imsldFactory = new org.imsglobal.jaxb.ld.ObjectFactory();

        //cria manifest
        Manifest manifest = new Manifest();
        manifest.setIdentifier("WebQuest-manifest");

        //cria metadata
        ManifestMetadata metadata = new ManifestMetadata();
        //preenche metadata
        metadata.setSchema("IMS Metadata");
        metadata.setSchemaversion("1.2");

        //adiciona metadata à manifest
        manifest.setMetadata(metadata);

        //cria organizations
        Organizations organizations = new Organizations();

        //cria learningDesign
        LearningDesign learningDesign = new LearningDesign();
        //preenche learningDesign
        learningDesign.setTitle("WebQuest-Tile");
        learningDesign.setIdentifier("WebQuest-Itenfifier");
        learningDesign.setLevel("A");
        learningDesign.setVersion("1.0");
        learningDesign.setUri("http://site.com");

        resources = new Resources();

        //cria itemModel Learning-Objectives
        itemModel = new ItemModel();

        //cria item
        item = new Item();
        item.setIdentifier("IT-Learning-Objectives");

        //cria resource
        resource = new Resource();
        resource.setIdentifier("RE-Learning-Objectives");
        resource.setType("webcontent");
        resource.setHref("Learning-Objectives.html");

        //adiciona recurso à item
        item.setIdentifierref(resource);

        resources.getResourceList().add(resource);

        //adiciona item à itemMode
        itemModel.getItemList().add(item);

        learningDesign.setLearningObjectives(itemModel);

        //cria itemModel Prerequisites
        itemModel = new ItemModel();

        //cria item
        item = new Item();
        item.setIdentifier("IT-Prerequisites");

        //cria resource
        resource = new Resource();
        resource.setIdentifier("RE-Prerequisites");
        resource.setType("webcontent");
        resource.setHref("Prerequisites.html");

        resources.getResourceList().add(resource);

        //adiciona recurso à item
        item.setIdentifierref(resource);

        //adiciona item à itemMode
        itemModel.getItemList().add(item);

        learningDesign.setPrerequisites(itemModel);

        //cria components
        Components components = new Components();

        //cria roles
        Roles roles = new Roles();

        //cria learner
        Learner learner = new Learner();
        //preenche learner
        learner.setIdentifier("RO-Learner");
        learner.setTitle("Learner Role");

        //adiciona roles à lista de roles
        roles.getLearnerList().add(learner);

        //guarda lista de roles em componentes
        components.setRoles(roles);

        //cria activities
        Activities activities = new Activities();

        //cria environment
        Environment environment = new Environment();
        //preenche environment
        environment.setTitle("Web");
        environment.setIdentifier("E-Web");

        //cria environments
        Environments environments = new Environments();
        environments.getEnvironmentList().add(environment);

        EnvironmentRef environmentRef = new EnvironmentRef();
        environmentRef.setRef(environment);

        //cria activityStructure
        ActivityStructure activityStructure = new ActivityStructure();
        //preenche activityStructure
        activityStructure.setTitle("WebQuest Structure");
        activityStructure.setIdentifier("AS-Structure");
        activityStructure.setStructureType("sequence");

        //cria learningActivity introduction
        learningActivity = new LearningActivity();
        learningActivity.setTitle("WebQuest Introduction");
        learningActivity.setIdentifier("LA-Introduction");
        learningActivity.getEnvironmentRefList().add(environmentRef);

        //cria itemModel
        itemModel = new ItemModel();

        //cria item
        item = new Item();
        item.setIdentifier("IT-Introduction");

        //cria resource
        resource = new Resource();
        resource.setIdentifier("RE-Introduction");
        resource.setType("webcontent");
        resource.setHref("Introduction.html");

        resources.getResourceList().add(resource);

        //adiciona recurso à item
        item.setIdentifierref(resource);

        //adiciona item à itemMode
        itemModel.getItemList().add(item);

        //adiciona environment à learningActivity description
        learningActivity.setActivityDescription(itemModel);

        //cria completeActivity
        completeActivity = new CompleteActivity();
        completeActivity.setUserChoice(new UserChoice());

        //adiciona à learningActivity CompleteActivity
        learningActivity.setCompleteActivity(completeActivity);

        //cria learningActivityRef
        LearningActivityRef learningActivityRef = new LearningActivityRef();
        learningActivityRef.setRef(learningActivity);

        //adiciona learningActivityRef à activityStructure
        activityStructure.getLearningActivityRefOrSupportActivityRefOrUnitOfLearningHref().add(learningActivityRef);

        //adiciona learningActivity à activities
        activities.getLearningActivityOrSupportActivityOrActivityStructure().add(learningActivity);

        //cria learningActivity Task
        learningActivity = new LearningActivity();
        learningActivity.setTitle("WebQuest Task");
        learningActivity.setIdentifier("LA-Task");
        learningActivity.getEnvironmentRefList().add(environmentRef);

        //cria itemModel
        itemModel = new ItemModel();

        //cria item
        item = new Item();
        item.setIdentifier("IT-Task");

        //cria resource
        resource = new Resource();
        resource.setIdentifier("RE-Task");
        resource.setType("webcontent");
        resource.setHref("Task.html");

        resources.getResourceList().add(resource);

        //adiciona recurso à item
        item.setIdentifierref(resource);

        //adiciona item à itemMode
        itemModel.getItemList().add(item);

        //adiciona environment à learningActivity description
        learningActivity.setActivityDescription(itemModel);

        //cria completeActivity
        completeActivity = new CompleteActivity();
        completeActivity.setUserChoice(new UserChoice());

        //adiciona à learningActivity CompleteActivity
        learningActivity.setCompleteActivity(completeActivity);

        //cria learningActivityRef
        learningActivityRef = new LearningActivityRef();
        learningActivityRef.setRef(learningActivity);

        //adiciona learningActivityRef à activityStructure
        activityStructure.getLearningActivityRefOrSupportActivityRefOrUnitOfLearningHref().add(learningActivityRef);

        //adiciona learningActivity1 à activities
        activities.getLearningActivityOrSupportActivityOrActivityStructure().add(learningActivity);

        //cria learningActivity Process
        learningActivity = new LearningActivity();
        learningActivity.setTitle("WebQuest Process");
        learningActivity.setIdentifier("LA-Process");
        learningActivity.getEnvironmentRefList().add(environmentRef);

        //cria itemModel
        itemModel = new ItemModel();

        //cria item
        item = new Item();
        item.setIdentifier("IT-Process");

        //cria resource
        resource = new Resource();
        resource.setIdentifier("RE-Process");
        resource.setType("webcontent");
        resource.setHref("Process.html");

        resources.getResourceList().add(resource);

        //adiciona recurso à item
        item.setIdentifierref(resource);

        //adiciona item à itemMode
        itemModel.getItemList().add(item);

        //adiciona environment à learningActivity description
        learningActivity.setActivityDescription(itemModel);

        //cria completeActivity
        completeActivity = new CompleteActivity();
        completeActivity.setUserChoice(new UserChoice());

        //adiciona à learningActivity CompleteActivity
        learningActivity.setCompleteActivity(completeActivity);

        //cria learningActivityRef
        learningActivityRef = new LearningActivityRef();
        learningActivityRef.setRef(learningActivity);

        //adiciona learningActivityRef à activityStructure
        activityStructure.getLearningActivityRefOrSupportActivityRefOrUnitOfLearningHref().add(learningActivityRef);

        //adiciona learningActivity1 à activities
        activities.getLearningActivityOrSupportActivityOrActivityStructure().add(learningActivity);

        //cria learningActivity Evaluation
        learningActivity = new LearningActivity();
        learningActivity.setTitle("WebQuest Evaluation");
        learningActivity.setIdentifier("LA-Evaluation");
        learningActivity.getEnvironmentRefList().add(environmentRef);

        //cria itemModel
        itemModel = new ItemModel();

        //cria item
        item = new Item();
        item.setIdentifier("IT-Evaluation");

        //cria resource
        resource = new Resource();
        resource.setIdentifier("RE-Evaluation");
        resource.setType("webcontent");
        resource.setHref("Evaluation.html");

        resources.getResourceList().add(resource);

        //adiciona recurso à item
        item.setIdentifierref(resource);

        //adiciona item à itemMode
        itemModel.getItemList().add(item);

        //adiciona environment à learningActivity description
        learningActivity.setActivityDescription(itemModel);

        //cria completeActivity
        completeActivity = new CompleteActivity();
        completeActivity.setUserChoice(new UserChoice());

        //adiciona à learningActivity CompleteActivity
        learningActivity.setCompleteActivity(completeActivity);

        //cria learningActivityRef
        learningActivityRef = new LearningActivityRef();
        learningActivityRef.setRef(learningActivity);

        //adiciona learningActivityRef à activityStructure
        activityStructure.getLearningActivityRefOrSupportActivityRefOrUnitOfLearningHref().add(learningActivityRef);

        //adiciona learningActivity1 à activities
        activities.getLearningActivityOrSupportActivityOrActivityStructure().add(learningActivity);

        //cria learningActivity Conclusion
        learningActivity = new LearningActivity();
        learningActivity.setTitle("WebQuest Conclusion");
        learningActivity.setIdentifier("LA-Conclusion");
        learningActivity.getEnvironmentRefList().add(environmentRef);

        //cria itemModel
        itemModel = new ItemModel();

        //cria item
        item = new Item();
        item.setIdentifier("IT-Conclusion");

        //cria resource
        resource = new Resource();
        resource.setIdentifier("RE-Conclusion");
        resource.setType("webcontent");
        resource.setHref("Conclusion.html");

        resources.getResourceList().add(resource);

        //adiciona recurso à item
        item.setIdentifierref(resource);

        //adiciona item à itemMode
        itemModel.getItemList().add(item);

        //adiciona environment à learningActivity description
        learningActivity.setActivityDescription(itemModel);

        //cria completeActivity
        completeActivity = new CompleteActivity();
        completeActivity.setUserChoice(new UserChoice());

        //adiciona à learningActivity CompleteActivity
        learningActivity.setCompleteActivity(completeActivity);

        //cria learningActivityRef
        learningActivityRef = new LearningActivityRef();
        learningActivityRef.setRef(learningActivity);

        //adiciona learningActivityRef à activityStructure
        activityStructure.getLearningActivityRefOrSupportActivityRefOrUnitOfLearningHref().add(learningActivityRef);

        //adiciona learningActivity1 à activities
        activities.getLearningActivityOrSupportActivityOrActivityStructure().add(learningActivity);

        //adiciona learningActivity1 à activities
        activities.getLearningActivityOrSupportActivityOrActivityStructure().add(activityStructure);

        //adiciona activities à components Activities
        components.setActivities(activities);

        //adiciona environments à components Environments
        components.setEnvironments(environments);

        //adiciona components à learningDesign
        learningDesign.setComponents(components);

        //cria method
        Method method = new Method();

        //cria play
        Play play = new Play();

        //cria act
        Act act = new Act();

        //cria role part
        RolePart rolePart = new RolePart();

        //cria roleRef
        RoleRef roleRef = new RoleRef();
        roleRef.setRef(learner);

        //adiciona roleRef à rolePart
        rolePart.setRoleRef(roleRef);

        //cria activityStructureRef
        ActivityStructureRef activityStructureRef = new ActivityStructureRef();
        activityStructureRef.setRef(activityStructure);

        //adiciona activityStructureRef à rolePart
        rolePart.setActivityStructureRef(activityStructureRef);

        //adiciona rolePart à act
        act.getRolePartList().add(rolePart);

        //adiciona act à play
        play.getActList().add(act);

        ///adiciona play à method
        method.getPlayList().add(play);

        //adiciona method à learningDesign
        learningDesign.setMethod(method);

        //adiciona learningDesign à organizations
        organizations.getAny().add(imsldFactory.createLearningDesign(learningDesign));

        //adiciona organizations à manifest
        manifest.setOrganizations(organizations);

        //adiciona resources à manifest
        manifest.setResources(resources);

        //inicia exportação
        JAXBContext jaxbContext = JAXBContext.newInstance("org.imsglobal.jaxb.content:org.imsglobal.jaxb.ld");

        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty("jaxb.formatted.output", true);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
        marshaller.marshal(imscpFactory.createManifest(manifest), new File("imsmanifest.xml"));

    }
}
