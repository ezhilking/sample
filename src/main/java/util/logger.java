package com.automation;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

public class logger {

	// static Logger logger=null ;
	public static Logger mainlogger = null;
	public static Logger logger = null;

	public static void main(String[] args) {

		String Logfilepath = "C:/Users/e.saravanamuthu/Desktop/"+ System.getProperty("user.name").toLowerCase() + ".log";

		logger = Logger.getLogger(logger.class.getName());
		FileAppender FA = null;
		try {
			FA = new FileAppender(new PatternLayout("%d{dd-MMM-yyyy hh:mm:ss:SSS a} %-10C{1} %-20M %-4L %-5p: %m%n"),Logfilepath, true);
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}

		ConsoleAppender CA = new ConsoleAppender(new PatternLayout("%d{ISO8601} %-20C{1} %-20M %-4L %-5p: %m%n"), "System.out");

		logger.setAdditivity(false);
		logger.setLevel(Level.DEBUG);
		logger.addAppender(FA);
		logger.addAppender(CA);
		mainlogger = logger;
		// logger = null;
		ab();
	}

	public static void ab() {
		logger = Logger.getLogger(loggerSample.class.getName());
		SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss:SSS a");
		Calendar calobj = Calendar.getInstance();
		logger.log(Level.FATAL,"cdssd started at " + df.format(calobj.getTime()));
		mainlogger.log(Level.DEBUG,"fgh started at " + df.format(calobj.getTime()));
		mainlogger.log(Level.FATAL,"wrwe started at " + df.format(calobj.getTime()));

	}
}
