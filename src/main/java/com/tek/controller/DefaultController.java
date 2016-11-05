/**
 * 
 */
package com.tek.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.event.FlowEvent;
import org.primefaces.json.JSONObject;
import org.primefaces.model.UploadedFile;

/**
 * @author nlaks
 *
 */
public abstract class DefaultController {
	
	
	public Map<String,Object> bean = new HashMap<String,Object>();
	

	/**
	 * @return the bean
	 */
	public Map<String, Object> getBean() {
		return bean;
	}

	/**
	 * @param bean the bean to set
	 */
	public void setBean(Map<String, Object> bean) {
		this.bean = bean;
	}

	public static String getMessageFromBundle(String key){
		FacesContext context = FacesContext.getCurrentInstance();
		Application app = context.getApplication();
		ResourceBundle backendText = app.getResourceBundle(context, "msg");
		return backendText.getString(key);
	}
	
	protected Map<String,Object> fetchSMTPSettings(){
		Map<String,Object> settings = new HashMap<String,Object>();
		settings.put("mail.smtp.host", getMessageFromBundle("mail.smtp.host"));
		settings.put("mail.smtp.port", getMessageFromBundle("mail.smtp.port"));
		settings.put("mail.smtp.auth", getMessageFromBundle("mail.smtp.auth"));
		settings.put("userName", getMessageFromBundle("userName"));
		settings.put("password", getMessageFromBundle("password"));
		return settings;
	}
	protected JSONObject convertMapToJsonObject(Map<String,Object> dataMap){
		JSONObject jsonObj = new JSONObject();
		Set<String> keys = dataMap.keySet();
		for(String key : keys){
			
		}
		return jsonObj;
	}

	
	protected void setMessage(String notificationMessage){
		FacesMessage msg = new FacesMessage(notificationMessage);
        FacesContext.getCurrentInstance().addMessage(null, msg);
	}
	
	protected void setError(String notificationMessage){
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", notificationMessage);
        FacesContext.getCurrentInstance().addMessage(null, msg);
	}
	
    public void setSessionData(String key,Object value){
  	  FacesContext context = FacesContext.getCurrentInstance();
  	  context.getExternalContext().getSessionMap().put(key, value);
    }
    
    public void redirect(String path) throws IOException{
    	String forwardTo = FacesContext.getCurrentInstance().getExternalContext().getApplicationContextPath() + path;
    	FacesContext.getCurrentInstance().getExternalContext().redirect(forwardTo);
    }
    
    public static void clearSession(){
  	  FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
    }
    
	protected String writeToFile(UploadedFile uploadedFile,String basePath){
		String absoluteFile = "";
			File profileFolder = new File( basePath );
			if(!profileFolder.exists()){
				profileFolder.mkdirs();
			}
			File profilePic = new File(profileFolder,uploadedFile.getFileName());
			
			FileOutputStream outStream = null;
			try {
				outStream = new FileOutputStream(profilePic);
				outStream.write(uploadedFile.getContents());
				absoluteFile = profilePic.getAbsolutePath();
			} catch (IOException e) {
				e.printStackTrace();
			}
			finally {
				
					try {
						if(outStream != null)
							outStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
			}
			
		
		return absoluteFile;
	}
	/***********StartCRUD Operation Methods*****************/
	/*public abstract void saveOrUpdate();
	public abstract void remove();
	public abstract void findAllActive();
	public abstract void findAll();*/
	/***********End CRUD Operation Methods******************/
	
	
	/***********Start Tab Navigation*****************/
	protected boolean skip;
	public boolean isSkip() {
        return skip;
    }
 
    public void setSkip(boolean skip) {
        this.skip = skip;
    }
     
    public String onFlowProcess(FlowEvent event) {
        if(skip) {
            skip = false;   //reset in case user goes back
            return "confirm";
        }
        else {
            return event.getNewStep();
        }
    }
	/**********End Tab Navigation********************/
	
}
