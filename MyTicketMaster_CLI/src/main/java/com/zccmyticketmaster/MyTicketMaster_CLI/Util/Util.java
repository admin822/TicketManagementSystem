package com.zccmyticketmaster.MyTicketMaster_CLI.Util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


public class Util {
	public static Map<String, String> getLoginProperties() throws Exception{
		Map<String, String> logins=new HashMap<>();
		InputStream input=new FileInputStream("login.propertiess");
		Properties props=new Properties();
		props.load(input);
		String email=props.getProperty("login.email");
        String token=props.getProperty("login.token");
        String subdomain=props.getProperty("login.subdomain");
        logins.put("email", email);
        logins.put("token", token);
        logins.put("subdomain", subdomain);
        return logins;
	}
	public static void clearConsole() throws Exception {
		new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
	}
	
}
