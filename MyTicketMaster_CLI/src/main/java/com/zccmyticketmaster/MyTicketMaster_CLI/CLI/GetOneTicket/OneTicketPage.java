package com.zccmyticketmaster.MyTicketMaster_CLI.CLI.GetOneTicket;

import java.util.Scanner;

import com.zccmyticketmaster.MyTicketMaster_CLI.App;
import com.zccmyticketmaster.MyTicketMaster_CLI.API.GetTicketByID;
import com.zccmyticketmaster.MyTicketMaster_CLI.Util.Util;

public class OneTicketPage {
	public void start(Scanner scanner) {
		String currentInput;
		int currentOption;
		try {
			while(true) {
				Util.clearConsole();
				System.out.println("[Ticket Detail Page]: Please enter the id of the ticket: ");
				while(true) {
					currentInput=scanner.nextLine();
					try {
						currentOption=Integer.parseInt(currentInput);
						break;
					}catch (NumberFormatException e) {
						System.out.println("Please type in a valid number");
					}
				}
				String details=GetTicketByID.getTicketByID(currentOption);
				if(details!=null) {
					System.out.println(details);
				}
				System.out.println("[Ticket Detail Page]: Press 1 to go back to the main page: ");
				while(true) {
					currentInput=scanner.nextLine();
					try {
						currentOption=Integer.parseInt(currentInput);
						if(currentOption==1) {
							break;
						}
						System.out.println("Press 1 to go back to the main page");
					}catch (NumberFormatException e) {
						System.out.println("Press 1 to go back to the main page");
					}
				}
				Util.clearConsole();
				break;
			}
		} catch (Exception e) {
			System.out.println("[Ticket Detail Page]: An error has occurred on this page, please refer to error.log for more details. Sorry for the inconnevience");
			App.logger.error("Error happens at OneTicketPage",e);
		}
	}
}
