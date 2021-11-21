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
import com.google.gson.JsonParser;
import com.zccmyticketmaster.MyTicketMaster_CLI.Util.Util;

public class GetTicketByID {
	public static String getTicketByID(int ticketID) {
		try {
			Map<String, String> logins=Util.getLoginProperties();
			String getString="curl https://"+logins.get("subdomain")+".zendesk.com/api/v2/requests/"+ticketID+".json" + 
					" -v -u "+logins.get("email")+"/token:"+logins.get("token");
			ProcessBuilder pb = new ProcessBuilder(getString.split(" "));
			pb.directory(new File(System.getProperty("user.dir")));
			pb.redirectError(new File("request_logs.log"));
			Process p = pb.start();
			String response = new BufferedReader(
				      new InputStreamReader(p.getInputStream(), StandardCharsets.UTF_8))
				        .lines()
				        .collect(Collectors.joining("\n"));
			p.waitFor();
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			JsonElement je = JsonParser.parseString(response);
			return gson.toJson(je);
		} catch (Exception e) {
			System.out.println("Get ticket"+ticketID+" failed, due to ");
			e.printStackTrace();
			return null;
		}
	}
}
