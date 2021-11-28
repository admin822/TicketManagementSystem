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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.zccmyticketmaster.MyTicketMaster_CLI.API.GetTicketByID;
import com.zccmyticketmaster.MyTicketMaster_CLI.Util.Util;

public class GetTicketByIDTest {
	protected String requestWithValidID;
	// NO NEED TO TEST PROPERTY FILE NOT FOUND SCENARIO, UtilTest COVERS IT
	// MANUALLY TEST PROPERTY EXISTS BUT INVALID SCENARIOS
	@Before
	public void setUp() throws Exception {
		Map<String, String> logins=Util.getLoginProperties();
		requestWithValidID="curl https://"+logins.get("subdomain")+".zendesk.com/api/v2/requests/2.json" + 
				" -v -u "+logins.get("email")+"/token:"+logins.get("token");
	}

	@Test
	public void testgetTicketByIDWithValidID() throws Exception{
		ProcessBuilder pb = new ProcessBuilder(requestWithValidID.split(" "));
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
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String jString=gson.toJson(je);
		assertEquals(jString, GetTicketByID.getTicketByID(2));
	}
	@Test
	// THIS COVERS A BRANCH OF THE CODE, BUT NOT REALLY NECESSARY SINCE IN REAL-WORLD NEXT PAGE IS PARSED NOT INPUT BY USER
	// THIS WILL PRINT THE ERROR OUTPUT TO STDIO, SINCE APP'S LOGGER IS NOT SET UP YET
	public void testgetTicketByIDWithInvalidID() {
		String standardErrorVerbose="[Ticket Detail Page]: Ticket is not found, please make sure your logins and ticket ID are valid";
		assertEquals(standardErrorVerbose, GetTicketByID.getTicketByID(Integer.MAX_VALUE));
		assertEquals(standardErrorVerbose, GetTicketByID.getTicketByID(-1));
		assertEquals(standardErrorVerbose, GetTicketByID.getTicketByID(0));
	}
}
