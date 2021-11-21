package com.zccmyticketmaster.MyTicketMaster_CLI.CLI.TicketsList;

import java.io.IOException;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

import com.zccmyticketmaster.MyTicketMaster_CLI.Util.Util;

public class TicketListPage {
	public void start(Scanner scanner) {
		String currentInput;
		int currentOption;
		Util.clearConsole();
		System.out.println("Requesting for all the tickets, this should just take a second...");
		TicketsHandler ticketsHandler=new TicketsHandler();
		boolean getNewPage=true;
		List<String> pageData=null;
		while(true) {
			if(getNewPage) {
				Util.clearConsole();
				pageData=ticketsHandler.getAPage();
			}
			if(pageData==null) {
				System.out.println("No data available right now");
				return;
			}
			System.out.println("No.\tID\tStatus\tSubject");
			for(String s:pageData) {
				System.out.println(s);
			}
			System.out.println("What do you wish to do now: ");
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
				System.out.println("Please enter the No. of the ticket you want to see:");

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
				System.out.println(detail);
				System.out.println("Press 1 to go back to the previous page");
				while(true) {
					currentInput=scanner.nextLine();
					try {
						currentOption=Integer.parseInt(currentInput);
						if(currentOption==1) {
							break;
						}
						System.out.println("Press 1 to go back to the previous page");
					}catch (NumberFormatException e) {
						System.out.println("Press 1 to go back to the previous page");
					}
				}
				getNewPage=false;
			}
			if(currentOption==3) {
				Util.clearConsole();
				break;
			}
		}
	}
}
