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
import com.zccmyticketmaster.MyTicketMaster_CLI.API.GetAllTickets;

public class TicketsHandler {
	private String nextPage;
	private Queue<JsonElement> currentTickets;
	private List<JsonElement> ticketsBuffer;
	final int PAGESIZE=25;
	public TicketsHandler() {
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
	}
	private void makeSubsequentRequest() {
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
		if(currentTickets==null||(currentTickets.isEmpty()&&nextPage==null)) {
			return null;
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
	}
	public String detailTicket(int index) {
		if((index-1)>=ticketsBuffer.size()) {
			return null;
		}
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(ticketsBuffer.get(index-1));
	}
}
