package com.zccmyticketmaster.MyTicketMaster_CLI.Util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.zccmyticketmaster.MyTicketMaster_CLI.Exceptions.PropertyFileNotFoundException;


public class Util {
	public static Map<String, String> getLoginProperties() throws Exception{
		try {
			Map<String, String> logins=new HashMap<>();
			InputStream input=new FileInputStream("login.properties");
			Properties props=new Properties();
			props.load(input);
			String email=props.getProperty("login.email");
	        String token=props.getProperty("login.token");
	        String subdomain=props.getProperty("login.subdomain");
	        logins.put("email", email);
	        logins.put("token", token);
	        logins.put("subdomain", subdomain);
	        return logins;
		} catch (FileNotFoundException fne) {
			throw new PropertyFileNotFoundException();
		}
		catch (Exception e) {
			throw e;
		}
	}
	
}
