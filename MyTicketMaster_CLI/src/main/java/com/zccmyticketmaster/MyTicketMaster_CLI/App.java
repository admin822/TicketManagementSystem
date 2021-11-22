package com.zccmyticketmaster.MyTicketMaster_CLI;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;

import com.zccmyticketmaster.MyTicketMaster_CLI.CLI.Index;

public class App 
{	
	public static final Logger logger = LogManager.getLogger(App.class);
    public static void main( String[] args )
    {	
    	try {
    		LoggerContext context = (LoggerContext) LogManager.getContext(false);
        	File file=new File(App.class.getClassLoader().getResource("Config/log4j2.xml").getPath());
        	context.setConfigLocation(file.toURI());
            new Index().start();
		} catch (Exception e) {
			System.out.println("The application failed to launch, please refer to error.log for more details. Sorry for the inconvenience.");
			logger.error("Error at launching",e);
		}
    }
}
