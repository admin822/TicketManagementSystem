package com.zccmyticketmaster.MyTicketMaster_CLI.CLI;

import java.util.Scanner;

import com.zccmyticketmaster.MyTicketMaster_CLI.App;
import com.zccmyticketmaster.MyTicketMaster_CLI.CLI.GetOneTicket.OneTicketPage;
import com.zccmyticketmaster.MyTicketMaster_CLI.CLI.TicketsList.TicketListPage;
import com.zccmyticketmaster.MyTicketMaster_CLI.Util.Util;

public class Index {
	public void start() {
		System.out.println("=======================Welcome to My Ticket Master=========================");
		System.out.println("\n\n");
		try {
			Scanner scanner=new Scanner(System.in);
			String currentInput;
			int currentOption;
			while(true) {
				System.out.println("[Main Page]: Choose view options: ");
				System.out.println("1: View all tickets");
				System.out.println("2: View one ticket by ID");
				System.out.println("3: quit My Ticket Master");
				// TAKE VIEW OPTION
				while(true) {
					currentInput=scanner.nextLine();
					try {
						currentOption=Integer.parseInt(currentInput);
						if(currentOption>=1&&currentOption<=4) {
							break;
						}
						System.out.println("Please type in a valid number");
					}catch (NumberFormatException e) {
						System.out.println("Please type in a valid number");
					}
				}
				// USER QUIT
				if(currentOption==3) {
					System.out.println("Thank you for using My Ticket Master!");
					scanner.close();
					break;
				}
				// USER VIEW ALL TICKETS
				if(currentOption==1) {
					new TicketListPage().start(scanner);
				}
				// USER VIEW ONE TICKET SPECIFIED BY ID
				if(currentOption==2) {
					new OneTicketPage().start(scanner);
				}
		}
		} catch (Exception e) {
			System.out.println("[Main Page]: An error has occurred on this page, please refer to error.log for more details. Sorry for the inconnevience");
			App.logger.error("Error happens at IndexPage",e);
		}
}
}
