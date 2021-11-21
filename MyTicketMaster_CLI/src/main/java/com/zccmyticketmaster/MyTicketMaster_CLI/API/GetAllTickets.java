package com.zccmyticketmaster.MyTicketMaster_CLI.API;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
		pb.directory(new File(System.getProperty("user.dir")));
		pb.redirectError(new File("request_logs.log"));
		Process p = pb.start();
		String response = new BufferedReader(
			      new InputStreamReader(p.getInputStream(), StandardCharsets.UTF_8))
			        .lines()
			        .collect(Collectors.joining("\n"));
		p.waitFor();
		JsonElement je = JsonParser.parseString(response);
		JsonObject jObject=je.getAsJsonObject();
		p.destroy();
		return jObject;
	}
	public static JsonObject getInitialResponse() {
		try {
			return sendRequest(buildListTicktetsUrl());
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public static JsonObject getSubsequentResponse(String nextPageUrl) {
		try {
			return sendRequest(buildSubsequentRequestUrl(nextPageUrl));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
