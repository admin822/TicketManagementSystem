package com.zccmyticketmaster.MyTicketMaster_CLI.APITest;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.zccmyticketmaster.MyTicketMaster_CLI.API.GetAllTickets;
import com.zccmyticketmaster.MyTicketMaster_CLI.Exceptions.IncorrectLoginsException;
import com.zccmyticketmaster.MyTicketMaster_CLI.Util.Util;

public class GetAllTicketsTest {
	// NO NEED TO TEST PROPERTY FILE NOT FOUND SCENARIO, UtilTest COVERS IT
	// MANUALLY TEST WHEN LOGINS EXIST BUT ARE INCORRECT.
	protected String correctRequest;
	protected String secondPage;
	protected String correctSecondPageQuest;
	protected String invalidSecondPage;
	@Before
	public void setUp() throws Exception {
		Map<String, String> correctInfo=Util.getLoginProperties();
		correctRequest="curl https://"+correctInfo.get("subdomain")+".zendesk.com/api/v2/requests.json" + 
				" -v -u "+correctInfo.get("email")+"/token:"+correctInfo.get("token");
		secondPage="https://zccmyticketmaster.zendesk.com/api/v2/requests.json?page=2"; 
		invalidSecondPage="https://zccmyticketmaster.zendesk.com/api/v2/requests.json?page=-1";
		correctSecondPageQuest="curl "+secondPage + " -v -u "+correctInfo.get("email")+"/token:"+correctInfo.get("token");
	}

	@Test
	public void getInitialResponseWithCorrectRequest() throws Exception{ // takes a while to finish
		ProcessBuilder pb = new ProcessBuilder(correctRequest.split(" "));
		File requestLogs=new File("logs/test_request_logs.log");
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
		assertEquals(jObject, GetAllTickets.getInitialResponse());
	}
	@Test
	public void getSubsequentResponseWithCorrectRequest() throws Exception{
		ProcessBuilder pb = new ProcessBuilder(correctSecondPageQuest.split(" "));
		File requestLogs=new File("logs/test_request_logs.log");
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
		assertEquals(jObject, GetAllTickets.getSubsequentResponse(secondPage));
	}
	//THIS TEST COVERS A BRANCH OF CODE(INPUT INVALID), BUT IS PROBABLY NOT NECESSARY SINCE SECOND PAGE IS AUTO-PARSED FROM 
	// THE API RESPONSE, IT IS VERY UNLIKELY TO BE INVALID.
	@Test(expected = IncorrectLoginsException.class)
	public void getSubsequentResponseWithIncorrectRequest() throws Exception {
		GetAllTickets.getSubsequentResponse(invalidSecondPage);
	}
}
