package com.tek.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;
import org.primefaces.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tek.rs.RestServiceInterface;

@ManagedBean(name="userRegisterController")
@ViewScoped 
public class UserRegisterController extends DefaultController implements Serializable {
	
	/**
	 * 
	 */
	public final Logger logger = LoggerFactory.getLogger(UserRegisterController.class);
	private static final long serialVersionUID = 1L;

public void sendEmail() {
    	
		try {
			logger.info("Class Name :: "+this.getClass().getSimpleName() + "-------->" + getMessageFromBundle("startOfSendMail"));
			HttpURLConnection con = (HttpURLConnection) new URL(RestServiceInterface.REGISTRATION_SERVICE).openConnection();
			con.setDoOutput(true);
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
			JSONObject  json = new JSONObject();
			json.put("emailData", bean);
		
			OutputStream os = con.getOutputStream();
	        os.write(json.toString().getBytes());
	        os.flush();
	        
	        if (con.getResponseCode() != HttpURLConnection.HTTP_OK) {
	        	setError(con.getResponseCode() + "::" + con.getResponseMessage());
	        }
	        else {
	        	setMessage(con.getResponseCode() + "::" + "Successfully Email Sent");
	        }
			logger.info(getMessageFromBundle("endOfSendMail"));
			bean = new HashMap<String,Object>();
	        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("Class Name :: "+this.getClass().getSimpleName() + "   " + getMessageFromBundle("error"));
			e.printStackTrace();
		}
		
    }

	public void showPopup(){
	    RequestContext.getCurrentInstance().execute("PF('register').show();");
	 }

}
