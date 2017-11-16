package com.automation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.sun.jna.platform.win32.Kernel32;

public class Reporter {

	private static Document doc = null;
	protected static String ResultStamp = null;
	private static String Logfilepath = null;
	private static String XMLfilepath = "";
	private static String HTMLfilepath = "";
	private static String DriverResultFolder = "";
	protected static String DriverScreenshotsFolder = "";
	public static boolean IsReportON;
	public static String ManualStepID = null;

	public static void OpenDriver() {

		if (ResultStamp == null) {
			SetupReportFolder();
		}
		Env.LoadEnvironmentValues();
		OpenXML();
		Env.logger.log(Level.INFO,
				"Execution Started for " + Env.GetBrowserUsed() + " - "
						+ HTMLfilepath);
		Env.logger.log(Level.INFO,
				"PID :  " + Kernel32.INSTANCE.GetCurrentProcessId());
		IsReportON = true;

		// Save the original language used, in a variable before updating
		Env.LanguageUsedWithDot = Env.LanguageUsed;
		Env.LanguageUsed = Env.TruncateDotInLanguageUsed(Env.LanguageUsed); // Remove
																			// the
																			// country
																			// code
																			// in
																			// the
																			// LanguageUsed
	}

	private static void SetupReportFolder() {

		Env.OSName = UtilClass.CheckPlatform(Env.OSUsed);
		UtilClass.CheckBrowser(Env.GetBrowserUsed());

		String XMLFileName = "";
		boolean success = true;
		File CreateFolderPath;
		File sourceFile;
		File destinationFile;
		try {
			if (ResultStamp == null) {
				ResultStamp = GetTimeStamp("yyyyMMMdd-hhmmssa");
			}
			if (Env.ScreenshotPath == null) {
				String CountryUsed = Env.CountryUsed.replaceAll(" ", "");
				DriverResultFolder = Env.ResultPath + "/" + ResultStamp + "-"
						+ Env.OSUsed + "-" + CountryUsed + "-"
						+ Env.LanguageUsed;
				DriverScreenshotsFolder = DriverResultFolder + "/Screenshots";
				CreateFolderPath = new File(DriverScreenshotsFolder);
				success = CreateFolderPath.mkdirs();
				if (success) {
					Env.ScreenshotPath = DriverScreenshotsFolder;
					CreateFolderPath = new File(DriverResultFolder + "/Data");
					CreateFolderPath.mkdir();
				} else {
					Env.logger.log(Level.FATAL,
							"Unable to create Report folder");
					System.exit(1);// Stop the Run
				}
			}

			if (success) {
				XMLFileName = Env.GetBrowserUsed();
				if (XMLFileName.startsWith("IE")) {
					XMLFileName = "IE";
				}
				XMLfilepath = DriverResultFolder + "/Data/" + XMLFileName
						+ ".xml";

				// Copy the template file
				sourceFile = new File(Env.LibPath + "/Template.html");
				destinationFile = new File(DriverResultFolder + "/"
						+ XMLFileName + ".html");
				FileUtils.copyFile(sourceFile, destinationFile);

				// Copy the Logo file
				sourceFile = new File(Env.LibPath + "/LogoHCL.png");
				destinationFile = new File(DriverScreenshotsFolder
						+ "/LogoHCL.png");
				FileUtils.copyFile(sourceFile, destinationFile);

				// Print the message
				HTMLfilepath = DriverResultFolder + "/" + XMLFileName + ".html";
				// Env.logger.log(Level.INFO, "Reports - " + HTMLfilepath);
			}
		} catch (Exception e) {
			Env.logger.log(Level.ERROR,
					"Unable to create Report folder" + e.getMessage());
		} finally {
			sourceFile = null;
			CreateFolderPath = null;
			destinationFile = null;
		}
	}

	public static void CloseDriver() {
		CloseXML();
		UtilClass.CloseBrowser();
		Env.logger.log(Level.INFO,
				"Execution Ended for " + Env.GetBrowserUsed() + " - "
						+ HTMLfilepath);
	}

	public void StartTest() {
		ManualStepID = "";
		if (Env.logger == null) {
			Reporter.OpenLOG();
			Reporter.OpenDriver(); // This will allow to run individual scripts
									// without the driver for debugging purpose
		}
		StartXML();
		Env.CurrentTestName = GetCurrentTestName();

		if (Env.RunEnvironment.startsWith("Local")
				&& Env.CurrentTestName.startsWith("REG")) {
			if (!Env.CurrentTestName.endsWith("GenerateScreenshots")) {
				// logger.info("Regre"+"ssion scr"+"ipts sh"+"ould n"+"ot b"+"e exe"+"cuted i"+"n Lo"+"cal Te"+"st Se"+"rver");
				// System.exit(1);
			}
		}

		InitializeExcelLogger();
		Env.logger.log(Level.INFO, "StartTest>" + Env.BrowserUsed + ">"
				+ Env.CurrentTestName);
	}

	public void StopTest() {
		ManualStepID = null;
		StopXML();
		// To Check in which script the browser is getting switched from IE to
		// FF.
		String CurrentBrowserName = "NULL";
		if (Env.BrowserObj != null) {
			CurrentBrowserName = Env.BrowserObj.toString();
			if (CurrentBrowserName.contains(":")) {
				CurrentBrowserName = CurrentBrowserName.split(":")[0];
			}
		}
		Env.logger.log(Level.INFO, "StopTest>" + CurrentBrowserName + ">"
				+ Env.CurrentTestName);
		Env.CurrentTestName = null;
		Env.StaticScreenshotName = null;
		if (!Env.IsRunningFromDriver) {
			CloseDriver();
		} else {
			UtilClass.RevertCountryAndLanguageToDefault();
		}
	}

	public void Update(String sStepName, String sExpected, String sActual,
			String sStatus) {
		Write(sStepName, sExpected, sActual, sStatus, true);
	}

	public void Write(String sStepName, String sExpected, String sActual,
			String sStatus, boolean TakeScreenshot) {
		String sScreenshotName = "";
		if (sStepName.trim().length() == 0) {
			sStepName = "StepNameIsNotGiven";
		}
		if (TakeScreenshot && IsReportON) {
			sScreenshotName = UtilClass.GetScreenshot(sStepName);
		}
		Write(sStepName, sExpected, sActual, sStatus, sScreenshotName);
		sScreenshotName = null;
		if (sStatus.compareToIgnoreCase("FAIL") == 0) {
			sScreenshotName = null;
		}

	}

	public void Write(String sStepName, String sExpected, String sActual,
			String sStatus, String sScreenshotName) {

		// Write the report in the HTML
		if (IsReportON) {
			sStatus = sStatus.toUpperCase();
			if (sStatus.startsWith("PASS") || sStatus.startsWith("FAIL")) {
				WriteXML(sStepName, sExpected, sActual, sStatus.substring(0, 1)
						.toUpperCase() + sStatus.substring(1).toLowerCase(),
						GetTimeStamp(), sScreenshotName);
			}
		}

		// Write the report in the Console in the format sStatus +">"+ sStepName
		// +">"+ sExpected +">"+ sActual;
		sStepName = AppendStepNameWithManualStepID(sStepName);

		String sDebugMessage = (new StringBuffer()).append(sStatus).append(">")
				.append(sStepName).append(">").append(sExpected).append(">")
				.append(sActual).toString();

		if (sStatus.startsWith("FAIL")) {
			WriteLOG(Level.ERROR, sDebugMessage);
		} else {
			WriteLOG(Level.DEBUG, sDebugMessage);
		}

		// CleanUp
		sStatus = null;
		sDebugMessage = null;
	}

	// -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-* -*-*-*-*-*-*-*-* Log
	// Reporting-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-* -*-*-*-*-*-*-*-* //

	public static void OpenLOG() {

		if (ResultStamp == null) {
			SetupReportFolder();
			// ResultStamp = GetTimeStamp("yyyyMMMdd-hhmmssa");
		}

		if (Logfilepath == null) {
			Logfilepath = DriverResultFolder + "/" + ResultStamp + "-"+ Env.OSUsed + "-"+ System.getProperty("user.name").toLowerCase() + ".log";
			// Logfilepath = Env.FrameWorkPath + "/EasyPod//logs/" + ResultStamp
			// + "-" + Env.OSUsed + "-" +
			// System.getProperty("user.name").toLowerCase() + ".log";

			Logger logger = Logger.getLogger(Reporter.class.getName());
			FileAppender FA = null;
			try {
				FA = new FileAppender(
						new PatternLayout(
								"%d{dd-MMM-yyyy hh:mm:ss:SSS a} %-10C{1} %-20M %-4L %-5p: %m%n"),
						Logfilepath, true);
			} catch (IOException e) {
				System.err.println(e.getMessage());
			}

			ConsoleAppender CA = new ConsoleAppender(
					new PatternLayout(
							"%d{dd-MMM-yyyy hh:mm:ss:SSS a} %-10C{1} %-20M %-4L %-5p: %m%n"),
					"System.out");

			logger.setAdditivity(false);
			logger.setLevel(Env.LogLevel);
			logger.addAppender(FA);
			logger.addAppender(CA);
			Env.logger = logger;
			logger = null;

		}
	}

	public static void CloseLog() {

		// copy the Log file to result folder
		// CopyLogToResultsFolder();

		// Clear the static variables
		doc = null;
		ResultStamp = null;
		Logfilepath = null;
		XMLfilepath = null;
		HTMLfilepath = null;
		DriverResultFolder = null;
		DriverScreenshotsFolder = null;

		// Clear Environment Values
		Env.logger.removeAllAppenders();
		Env.logger = null;
		Env.CountryUsed = null;
		Env.LanguageUsed = null;
		Env.BrowserUsed = null;
		Env.BrowserName = null;
		Env.ScreenshotPath = null;
		Env.StaticScreenshotName = null;
		Env.CurrentTestName = null;
		Env.MainHandle = null;
		Env.JsnId_ECOSPatient = null;
		Env.JsnId_NonECOSPatient = null;
		Env.JsnId_ECOSPatientFemale = null;
		Env.JsnId_Patient01 = null;
		Env.JsnId_Patient04 = null;
		Env.JsnId_Patient05 = null;
		Env.BuildNumber = null;
	}

	private void WriteLOG(Level LogLevel, String sMessage) {
		Env.logger.log(LogLevel, sMessage);
	}

	// -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-* -*-*-*-*-*-*-*-* HTML
	// Reporting-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-* -*-*-*-*-*-*-*-* //

	private static void OpenXML() {

		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			doc = docBuilder.newDocument();

			// root elements
			Element rootElement = doc.createElement("TestRoot");
			doc.appendChild(rootElement);

			// staff elements
			Element Runargs = CreateChildNode(rootElement, "Runargs", "");

			// Set Attribute
			rootElement.setAttribute("AppName", Env.AppName);
			rootElement.setAttribute("RunName", Env.RunEnvironment);
			rootElement.setAttribute("RunURL", Env.ECKOURL);
			rootElement.setAttribute("BuildNumber", "Blank");
			// Converting to Format 07-Mar-2013 09:59:45 AM
			rootElement.setAttribute(
					"RunStartTime",
					ResultStamp.substring(7, 9) + "-"
							+ ResultStamp.substring(4, 7) + "-"
							+ ResultStamp.substring(0, 4) + " "
							+ ResultStamp.substring(10, 12) + ":"
							+ ResultStamp.substring(12, 14) + ":"
							+ ResultStamp.substring(14, 16) + " "
							+ ResultStamp.substring(16, 18));
			rootElement.setAttribute("RunEndTime", "Execution ended abruptly");

			// BrowserName elements
			CreateChildNode(Runargs, "BrowserName", "NULL");
			CreateChildNode(Runargs, "OSNameVersion", "NULL");
			CreateChildNode(Runargs, "MachineName", "NULL");
			CreateChildNode(Runargs, "LanguageName", Env.CountryUsed + " - "
					+ Env.LanguageUsed);
			CreateChildNode(Runargs, "TesterID", "NULL");

			SaveXML();

			rootElement = null;
			Runargs = null;
			docBuilder = null;
			docFactory = null;

		} catch (Exception e) {
			Env.logger
					.log(Level.ERROR,
							"Stopping Test Suite, since unable to create Report XML file",
							e);
			System.exit(1);
		}
	}

	private static void CloseXML() {
		try {
			Element rootElement = (Element) doc.getFirstChild();
			UpdateAttribute(rootElement, "RunEndTime", GetTimeStamp());
			UpdateAttribute(rootElement, "BuildNumber", Env.BuildNumber);
			SaveXML();
			rootElement = null;
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, "", e);
		}
	}

	private void StartXML() {
		try {
			// Get the root element
			Node rootElement = doc.getFirstChild();

			// append a new Test to Root
			String TestID = String.valueOf(doc.getElementsByTagName("Test")
					.getLength() + 1);
			Element Test = CreateChildNode((Element) rootElement, "Test", "");
			Test.setAttribute("TestID", TestID);
			Test.setAttribute("TestName", GetCurrentTestName());
			Element Testargs = CreateChildNode(Test, "Testargs", "");
			CreateChildNode(Testargs, "StartTime", GetTimeStamp());
			CreateChildNode(Testargs, "EndTime", "WIP");
			CreateChildNode(Testargs, "TestStatus", "WIP");
			CreateChildNode(Testargs, "ExpectedCount", "WIP");
			CreateChildNode(Testargs, "PassCount", "WIP");
			CreateChildNode(Testargs, "FailCount", "WIP");
			CreateChildNode(Test, "StepGroup", "");

			// CleanUp
			Testargs = null;
			Test = null;
			TestID = null;
			rootElement = null;
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, "", e);
		}
	}

	private void StopXML() {

		// Prepare for Xpath Evaluation
		XPathFactory xpathFactory = XPathFactory.newInstance();
		XPath xpath = xpathFactory.newXPath();
		XPathExpression expr = null;
		Double FailCount = null;
		Double PassCount = null;
		String TestStatus = "Pass";

		try {

			// Get the root element
			int ArgsList = doc.getElementsByTagName("Testargs").getLength();
			Node Testargs = doc.getElementsByTagName("Testargs").item(
					ArgsList - 1);

			// Update EndTime
			Element EndTime = (Element) Testargs.getChildNodes().item(1);
			EndTime.setTextContent(GetTimeStamp());

			// Get PassCount and FailCount
			expr = xpath.compile("count(//Test[@TestID='" + (ArgsList)
					+ "']/StepGroup/Step[StepStatus='Pass'])");
			PassCount = (Double) expr.evaluate(doc, XPathConstants.NUMBER);

			expr = xpath.compile("count(//Test[@TestID='" + (ArgsList)
					+ "']/StepGroup/Step[StepStatus='Fail'])");
			FailCount = (Double) expr.evaluate(doc, XPathConstants.NUMBER);

			if (FailCount != 0)
				TestStatus = "Fail";

			if (PassCount == 0 && FailCount == 0)
				TestStatus = "No Run";

			// Update the fields
			Testargs.getChildNodes().item(2).setTextContent(TestStatus);
			Testargs.getChildNodes().item(3).setTextContent(String.valueOf(0));
			Testargs.getChildNodes().item(4)
					.setTextContent(String.valueOf(PassCount.intValue()));
			Testargs.getChildNodes().item(5)
					.setTextContent(String.valueOf(FailCount.intValue()));

			// Check whether Browser Info is written
			Testargs = doc.getElementsByTagName("Runargs").item(0);
			Element TestargsChild = (Element) Testargs.getChildNodes().item(0);
			if (TestargsChild.getTextContent().compareToIgnoreCase("NULL") == 0) {
				// Update BrowserName
				TestargsChild.setTextContent(Env.BrowserName);

				// Update OSNameVersion
				TestargsChild = (Element) Testargs.getChildNodes().item(1);
				TestargsChild.setTextContent(Env.OSName);

				// Update MachineName
				TestargsChild = (Element) Testargs.getChildNodes().item(2);
				TestargsChild.setTextContent(Env.MachineName);

				// //Update LanguageName
				// TestargsChild = (Element) Testargs.getChildNodes().item(3);
				// TestargsChild.setTextContent(Env.CountryUsed + " - " +
				// Env.LanguageUsed);

				// Update Tester's ID
				TestargsChild = (Element) Testargs.getChildNodes().item(4);
				TestargsChild.setTextContent(Env.UserID);

			}

			SaveXML();

			// CleanUp
			Testargs = null;
			EndTime = null;
			TestargsChild = null;
		} catch (XPathExpressionException e) {
			Env.logger.log(Level.ERROR, "", e);
		} finally {
			xpathFactory = null;
			xpath = null;
			expr = null;
			FailCount = null;
			PassCount = null;
			TestStatus = null;
		}

	}

	private static void SaveXML() {
		TransformerFactory transformerFactory;
		Transformer transformer;
		DOMSource source;
		FileOutputStream fos;
		StreamResult result;
		File XMLFile;
		try {
			// write the content into xml file
			transformerFactory = TransformerFactory.newInstance();
			transformer = transformerFactory.newTransformer();
			source = new DOMSource(doc);
			XMLFile = new File(XMLfilepath);
			fos = new FileOutputStream(XMLFile);
			result = new StreamResult(fos);
			transformer.transform(source, result);
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, "", e);
		} finally {
			// cleanUp
			transformer = null;
			transformerFactory = null;
			result = null;
			fos = null;
			source = null;
			XMLFile = null;
		}
	}

	private void WriteXML(String sStepName, String sExpected, String sActual,
			String sStatus, String sTimeStamp, String sScreenshotName) {

		try {

			if (sExpected.trim().length() == 0)
				sExpected = "BLANK";

			if (sActual.trim().length() == 0)
				sActual = "BLANK";

			if (sScreenshotName == "")
				sScreenshotName = "NA";

			// truncate PageName (i.e. If 2 underscore is present, then truncate
			// all letters before the first underscore)
			if (sStepName.contains("_")) {
				int First = sStepName.indexOf("_") + 1;
				if ((sStepName.indexOf("_", First)) > First) {
					sStepName = sStepName.substring(First, sStepName.length());
				}
			}

			// Update the StepName with the Manual StepID
			sStepName = AppendStepNameWithManualStepID(sStepName);

			// Get the root element
			int TotalTest = doc.getElementsByTagName("StepGroup").getLength();
			Element StepGroup = (Element) doc.getElementsByTagName("StepGroup")
					.item(TotalTest - 1);
			String StepID = String.valueOf((StepGroup.getElementsByTagName(
					"Step").getLength() + 1));
			Element NewStep = CreateChildNode(StepGroup, "Step", "");
			NewStep.setAttribute("StepID", StepID);
			NewStep.setAttribute("StepName", sStepName);
			CreateChildNode(NewStep, "TimeStamp", sTimeStamp);
			CreateChildNode(NewStep, "StepStatus", sStatus);
			CreateChildNode(NewStep, "ExpectedResult", sExpected);
			CreateChildNode(NewStep, "ActualResult", sActual);
			CreateChildNode(NewStep, "Screenshots", sScreenshotName);

			StepGroup = null;
			StepID = null;
			NewStep = null;

		} catch (Exception e) {
			Env.logger.log(Level.ERROR, "", e);
		} finally {
			sStepName = null;
			sExpected = null;
			sActual = null;
			sStatus = null;
			sTimeStamp = null;
			sScreenshotName = null;
		}
	}

	private static void UpdateAttribute(Element Node, String AttributeName,
			String AttributeValue) {
		// update staff attribute
		NamedNodeMap attr = Node.getAttributes();
		Node nodeAttr = attr.getNamedItem(AttributeName);
		nodeAttr.setTextContent(AttributeValue);
		nodeAttr = null;
		attr = null;
	}

	private static Element CreateChildNode(Element ParentNode,
			String ChildNodeName, String ChildNodeValue) {
		Document myparam = ParentNode.getOwnerDocument();
		Element NewNode = myparam.createElement(ChildNodeName);
		if (ChildNodeValue != "") {
			NewNode.appendChild(myparam.createTextNode(ChildNodeValue));
		}
		ParentNode.appendChild(NewNode);
		myparam = null;
		return NewNode;

	}

	private static String GetTimeStamp() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
		String formattedDate = sdf.format(date);
		sdf = null;
		date = null;
		return formattedDate;
	}

	public static String GetTimeStamp(String DateFormat) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(DateFormat);
		String formattedDate = sdf.format(date);
		sdf = null;
		date = null;
		return formattedDate;
	}

	private static String GetCurrentTestName() {
		StackTraceElement[] TestList = Thread.currentThread().getStackTrace();
		String CurrentTestName = "";
		if (TestList.length >= 6) {
			CurrentTestName = TestList[TestList.length - 6].getFileName(); // Running
																			// from
																			// Driver
			Env.IsRunningFromDriver = true;
		} else {
			CurrentTestName = TestList[TestList.length - 1].getFileName(); // Running
																			// individual
																			// scripts
																			// for
																			// debugging
			Env.IsRunningFromDriver = false;
		}
		TestList = null;
		return CurrentTestName.replace(".java", "").trim();
	}

	protected static void CopyLogToResultsFolder() {
		try {
			if (Env.IsRunningFromDriver) {
				// Copy the template file
				File sourceFile = new File(Logfilepath);
				File destinationFile = new File(DriverResultFolder + "/"
						+ sourceFile.getName());
				FileUtils.copyFile(sourceFile, destinationFile);
			}
		} catch (Exception e) {
			logger.info("Exception occured while moving the logfile to result folder");
		}
	}

	public static void InitializeExcelLogger() {

		if (Env.IsRunningFromDriver) {
			if (ExcelUtil.Excellogger == null) {
				ExcelUtil.Excellogger = Logger.getLogger(ExcelUtil.class
						.getName());
				FileAppender FA = null;
				try {
					// String Logfilepath = Env.FrameWorkPath +
					// "/EasyPod/logs/ExcelLog.log";
					String ExcelLogfilepath = new File(Env.ScreenshotPath)
							.getParentFile().getAbsolutePath()
							+ "/RTM-"
							+ ResultStamp + ".log";
					FA = new FileAppender(new PatternLayout("%m%n"),
							ExcelLogfilepath, true);
				} catch (IOException e) {
					System.err.println(e.getMessage());
				}
				ExcelUtil.Excellogger.setAdditivity(false);
				ExcelUtil.Excellogger.addAppender(FA);
				ExcelUtil.Excellogger.log(Level.INFO, "TimeStamp" + "\t"
						+ "TestName" + "\t" + "FileType" + "\t" + "ObjectID");
			}
		}
	}

	public String AppendStepNameWithManualStepID(String sStepName) {
		if (ManualStepID.trim().length() != 0) {
			sStepName = ManualStepID + "_" + sStepName;
		}
		return sStepName;
	}

}