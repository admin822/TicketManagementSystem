package com.zccmyticketmaster.MyTicketMaster_CLI.API;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.zccmyticketmaster.MyTicketMaster_CLI.App;
import com.zccmyticketmaster.MyTicketMaster_CLI.Exceptions.IncorrectLoginsException;
import com.zccmyticketmaster.MyTicketMaster_CLI.Util.Util;


public class GetAllTickets {
	private static String buildListTicktetsUrl() throws Exception {
		Map<String, String> logins=Util.getLoginProperties();
		String getString="curl https://"+logins.get("subdomain")+".zendesk.com/api/v2/requests.json" + 
				" -v -u "+logins.get("email")+"/token:"+logins.get("token");
		return getString;
	}
	private static String buildSubsequentRequestUrl(String nextPageUrl) throws Exception{
		Map<String, String> logins=Util.getLoginProperties();
		String getString="curl "+nextPageUrl + 
				" -v -u "+logins.get("email")+"/token:"+logins.get("token");
		return getString;
	}
	private static JsonObject sendRequest(String getString) throws Exception {
		ProcessBuilder pb = new ProcessBuilder(getString.split(" "));
		File requestLogs=new File("logs/request_logs.log");
		pb.redirectError(requestLogs);
		Process p = pb.start();
		String response = new BufferedReader(
			      new InputStreamReader(p.getInputStream(), StandardCharsets.UTF_8))
			        .lines()
			        .collect(Collectors.joining("\n"));
		p.waitFor();
		p.destroy();
		JsonElement je = JsonParser.parseString(response);
		JsonObject jObject=je.getAsJsonObject();
		if(jObject.get("error")!=null) {
			throw new IncorrectLoginsException(); // made it here means request sent/response received,
			// there is something wrong with the logins
		}
		return jObject;
	}
	public static JsonObject getInitialResponse() throws Exception {
		return sendRequest(buildListTicktetsUrl());
	}
	public static JsonObject getSubsequentResponse(String nextPageUrl) throws Exception {
		return sendRequest(buildSubsequentRequestUrl(nextPageUrl));
	}
}
