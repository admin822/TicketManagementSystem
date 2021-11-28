package com.zccmyticketmaster.MyTicketMaster_CLI.API;


import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.zccmyticketmaster.MyTicketMaster_CLI.App;
import com.zccmyticketmaster.MyTicketMaster_CLI.Exceptions.IncorrectLoginsException;
import com.zccmyticketmaster.MyTicketMaster_CLI.Exceptions.PropertyFileNotFoundException;
import com.zccmyticketmaster.MyTicketMaster_CLI.Util.Util;

public class GetTicketByID {
	public static String getTicketByID(int ticketID) {

		try {
			Map<String, String> logins=Util.getLoginProperties();
			String getString="curl https://"+logins.get("subdomain")+".zendesk.com/api/v2/requests/"+ticketID+".json" + 
					" -v -u "+logins.get("email")+"/token:"+logins.get("token");
			ProcessBuilder pb = new ProcessBuilder(getString.split(" "));
			File requestLogs=new File("logs/request_logs.log");
			pb.redirectError(requestLogs);
			Process p = pb.start();
			String response = new BufferedReader(
				      new InputStreamReader(p.getInputStream(), StandardCharsets.UTF_8))
				        .lines()
				        .collect(Collectors.joining("\n"));
			p.waitFor();
			JsonElement je = JsonParser.parseString(response);
			JsonObject jo=je.getAsJsonObject();
			if(jo.get("error")!=null) { // reached here means the request is sent and response is received, but something wrong
				// with the logins or the provided ticket id
				App.logger.error("Error when trying to get ticket: "+ticketID,new IncorrectLoginsException());
				return "[Ticket Detail Page]: Ticket is not found, please make sure your logins and ticket ID are valid";
			}
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			return gson.toJson(je);
		}catch (PropertyFileNotFoundException pne) { // property not found
			System.out.println("[ERROR]: The property file cannot be found, please make sure login.properties is at the root directory before launching");
			App.logger.error("Error when trying to get ticket: "+ticketID,pne);
			return null;
		}
		catch (Exception e) { // program error
			System.out.println("[ERROR]: An error occurred, please refer to the error.log for more information. Sorry for the inconvenience");
			App.logger.error("Error when trying to get ticket: "+ticketID,e);
			return null;
		}
		
	}
}
