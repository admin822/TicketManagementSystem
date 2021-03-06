package com.zccmyticketmaster.TicketGenerator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.stream.Collectors;

public class Main {
	public static void main(String[] args) {
		try (InputStream input = new FileInputStream("myLogin.properties")) {
            Properties prop = new Properties();
            prop.load(input);
            String email=prop.getProperty("email");
            String password=prop.getProperty("password");
            String subdomain=prop.getProperty("subdomain");
            String postString="curl https://"+subdomain+".zendesk.com/api/v2/imports/tickets/create_many.json -v -u " + 
            		email+":"+password+" -X POST -d @tickets.json -H \"Content-Type:application/json\"";
            System.out.println("This is the CURL command:\n"+postString);
            ProcessBuilder pb=new ProcessBuilder(postString.split(" "));
            pb.directory(new File(System.getProperty("user.dir"))); // run from current directory
            pb.inheritIO();
            Process p=pb.start();
            p.waitFor();
            System.out.println("Successfully imported tickets");
        } catch (Exception e) {
            e.printStackTrace();
        }
		
	}
}
