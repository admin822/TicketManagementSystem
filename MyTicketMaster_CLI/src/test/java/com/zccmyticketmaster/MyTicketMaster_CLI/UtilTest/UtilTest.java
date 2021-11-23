package com.zccmyticketmaster.MyTicketMaster_CLI.UtilTest;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import com.zccmyticketmaster.MyTicketMaster_CLI.Util.Util;

public class UtilTest {
	protected String email;
	protected String subdomain;
	protected String token;
	protected final String PROPERTIESPATH="login.properties"; 
	@Before
	public void setUp() throws Exception {
		InputStream input=new FileInputStream(PROPERTIESPATH); // This test is referring to the same file
		// the Util function is, so no need to write separate test to check if the file exists or not; if not, this test won't pass.
		Properties props=new Properties();
		props.load(input);
		email=props.getProperty("login.email");
		subdomain=props.getProperty("login.subdomain");
		token=props.getProperty("login.token");
	}

	@Test
	public void testGetLoginProperties() throws Exception{ // test if function get right results
		Map<String, String> utilProps=Util.getLoginProperties();
		assertEquals(email, utilProps.get("email"));
		assertEquals(subdomain, utilProps.get("subdomain"));
		assertEquals(token, utilProps.get("token"));
	}

}
