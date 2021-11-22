package com.zccmyticketmaster.MyTicketMaster_CLI.CLI.TicketsList;

import java.util.List;
import java.util.Scanner;

import com.zccmyticketmaster.MyTicketMaster_CLI.App;
import com.zccmyticketmaster.MyTicketMaster_CLI.Util.Util;

public class TicketListPage {
	public void start(Scanner scanner) {
		String currentInput;
		int currentOption;
		try {
			Util.clearConsole();
			System.out.println("[Ticket List View Page]: Requesting for all the tickets, this should just take a second...");
			TicketsHandler ticketsHandler=new TicketsHandler();
			if(ticketsHandler.isEmpty()) {
				System.out.println("[Ticket List View Page]: No data available right now, return to main page");
				return;
			}
			boolean getNewPage=true;
			List<String> pageData=null;
			while(true) {
				if(getNewPage) {
					Util.clearConsole();
					pageData=ticketsHandler.getAPage();
				}
				if(pageData==null) {
					System.out.println("[Ticket List View Page]: No data available right now, return to main page");
					return;
				}
				if(pageData.isEmpty()) {
					System.out.println("[Ticket List View Page]: No more available data, return to main page");
					return;
				}
				System.out.println("No.\tID\tStatus\tSubject");
				for(String s:pageData) {
					System.out.println(s);
				}
				System.out.println("[Ticket List View Page]: What do you wish to do now: ");
				System.out.println("1: See another page of tickets");
				System.out.println("2: view details of a ticket");
				System.out.println("3: back");
				while(true) {
					currentInput=scanner.nextLine();
					try {
						currentOption=Integer.parseInt(currentInput);
						if(currentOption>=1&&currentOption<=3) {
							break;
						}
						System.out.println("Please type in a valid number");
					}catch (NumberFormatException e) {
						System.out.println("Please type in a valid number");
					}
				}
				if(currentOption==1) {
					getNewPage=true;
					continue;
				}
				if(currentOption==2) {
					System.out.println("[Ticket List View Page]: Please enter the No. of the ticket you want to see:");

					while(true) {
						currentInput=scanner.nextLine();
						try {
							currentOption=Integer.parseInt(currentInput);
							break;
						}catch (NumberFormatException e) {
							System.out.println("Please type in a valid number");
						}
					}
					String detail=ticketsHandler.detailTicket(currentOption);
					Util.clearConsole();
					if(detail==null) {
						System.out.println("[Ticket List View Page]: There is no information about this ticket");
					}
					else {
						System.out.println(detail);
					}
					System.out.println("Press 1 to go back to the previous page");
					while(true) {
						currentInput=scanner.nextLine();
						try {
							currentOption=Integer.parseInt(currentInput);
							if(currentOption==1) {
								getNewPage=false;
								Util.clearConsole();
								break;
							}
							System.out.println("Press 1 to go back to the previous page");
						}catch (NumberFormatException e) {
							System.out.println("Press 1 to go back to the previous page");
						}
					}
				}
				if(currentOption==3) {
					Util.clearConsole();
					break;
				}
			}
		} catch (Exception e) {
			System.out.println("[Ticket List View Page]: An error has occurred on this page, please refer to error.log for more details. Sorry for the inconnevience");
			App.logger.error("Error happens at TicketListPage",e);
		}
	}
}
