package com.zccmyticketmaster.MyTicketMaster_CLI.CLI.TicketsList;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.zccmyticketmaster.MyTicketMaster_CLI.App;
import com.zccmyticketmaster.MyTicketMaster_CLI.API.GetAllTickets;
import com.zccmyticketmaster.MyTicketMaster_CLI.Exceptions.IncorrectLoginsException;
import com.zccmyticketmaster.MyTicketMaster_CLI.Exceptions.PropertyFileNotFoundException;

public class TicketsHandler {
	private String nextPage;
	private Queue<JsonElement> currentTickets;
	private List<JsonElement> ticketsBuffer;
	private boolean isEmpty;
	final int PAGESIZE=25;
	public TicketsHandler() {
		try {
			JsonObject response=GetAllTickets.getInitialResponse();
			JsonElement allTicketsJson=response.get("requests");
			JsonElement nextPageJson=response.get("next_page");
			
			if(allTicketsJson.isJsonNull()) {
				currentTickets=null;
				nextPage=null;
			}
			else {
				currentTickets=new LinkedList<>();
				JsonArray allTickets=allTicketsJson.getAsJsonArray();
				for(int i=0;i<allTickets.size();i++) {
					currentTickets.offer(allTickets.get(i));
				}
				if(nextPageJson.isJsonNull()) {
					nextPage=null;
				}
				else {
					nextPage=nextPageJson.getAsString();
				}
			}
			isEmpty=false;
		} catch (PropertyFileNotFoundException pne) {
			System.out.println("[ERROR]: The property file cannot be found, please make sure login.properties is at the root directory before launching");
			App.logger.error("Error at creating tickets handler",pne);
			this.isEmpty=true;
		}
		catch (IncorrectLoginsException ile) {
			System.out.println("[ERROR]: Failed to load all tickets, please make sure your logins are correct");
			App.logger.error("Error at creating tickets handler",ile);
			this.isEmpty=true;
		}
		catch (Exception e) {
			System.out.println("[ERROR]: Failed to load all tickets, please refer to error.log for more details. Sorry for the inconvenience.");
			App.logger.error("Error at creating tickets handler",e);
			this.isEmpty=true;
		}
	}
	private void makeSubsequentRequest() throws Exception{
		System.out.println("Requesting another page of data, just will just take a few seconds...");
		JsonObject response=GetAllTickets.getSubsequentResponse(nextPage);
		JsonElement allTicketsJson=response.get("requests");
		JsonElement nextPageJson=response.get("next_page");
		if(allTicketsJson.isJsonNull()) {
			nextPage=null;
		}
		else {
			JsonArray allTickets=allTicketsJson.getAsJsonArray();
			for(int i=0;i<allTickets.size();i++) {
				currentTickets.offer(allTickets.get(i));
			}
			if(nextPageJson.isJsonNull()) {
				nextPage=null;
			}
			else {
				nextPage=nextPageJson.getAsString();
			}
		}
	}
	public List<String> getAPage(){
		try {
			if(currentTickets==null) {
				return null;
			}
			if((currentTickets.isEmpty()&&nextPage==null)) {
				return new LinkedList<String>();
			}
			while(currentTickets.size()<PAGESIZE&&nextPage!=null) {
				makeSubsequentRequest();
			}
			ticketsBuffer=new ArrayList<>();
			List<String> pageData=new LinkedList<String>();
			int counter=1;
			while(counter<=PAGESIZE&&currentTickets.isEmpty()==false) {
				JsonObject currentTicketJson=currentTickets.peek().getAsJsonObject();
				String ticketListView="("+counter+"):  "+currentTicketJson.get("id").getAsString()+"  "+
						currentTicketJson.get("status")+"  "+currentTicketJson.get("subject");
				ticketsBuffer.add(currentTickets.poll());
				pageData.add(ticketListView);
				counter++;
			}
			return pageData;
		} catch (PropertyFileNotFoundException pne) {
			System.out.println("[ERROR]: The property file cannot be found, please make sure login.properties is at the root directory before launching");
			App.logger.error("Error at loading page",pne);
			return null;
		}
		catch (IncorrectLoginsException ile) {
			System.out.println("[ERROR]: Failed to load all tickets, please make sure your logins are correct");
			App.logger.error("Error at loading page",ile);
			return null;
		}
		catch (Exception e) {
			System.out.println("[ERROR]: Failed to load page content, please refer to error.log for more details. Sorry for the inconvenience.");
			App.logger.error("Error at loading page",e);
			return null;
		}
	}
	public String detailTicket(int index) {
		try {
			if((index-1)>=ticketsBuffer.size()||index<=0) {
				return null;
			}
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			return gson.toJson(ticketsBuffer.get(index-1));
		} catch (Exception e) {
			System.out.println("[ERROR]: Failed to load details, please refer to error.log for more details. Sorry for the inconvenience.");
			App.logger.error("Error at loading ticket details",e);
			return null;
		}
	}
	public boolean isEmpty() {
		return this.isEmpty;
	}
}
