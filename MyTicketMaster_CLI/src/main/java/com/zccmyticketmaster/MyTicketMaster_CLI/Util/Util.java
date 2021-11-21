package com.zccmyticketmaster.MyTicketMaster_CLI.Util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

public class Util {
	public static Map<String, String> getLoginProperties() throws Exception{
		Map<String, String> logins=new HashMap<>();
		try(InputStream input=new FileInputStream("login.properties")) {
			Properties props=new Properties();
			props.load(input);
			String email=props.getProperty("login.email");
            String token=props.getProperty("login.token");
            String subdomain=props.getProperty("login.subdomain");
            logins.put("email", email);
            logins.put("token", token);
            logins.put("subdomain", subdomain);
            return logins;
            }catch (Exception e) {
            	throw e;
            }
	}
	public static void clearConsole() {
			try {
				new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
			} catch (InterruptedException | IOException e) {
				e.printStackTrace();
			}
	}
	
}
