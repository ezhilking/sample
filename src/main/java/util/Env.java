package com.automation;

import java.net.InetAddress;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

public class Env {

	// Environment Configuration
	public static String CountryUsed = "France"; // Application Country
	public static String LanguageUsed = "English"; // Application Language
	protected static String BrowserUsed = "FF"; // IE32,IE64,GC,OP,SA,FF
	public static String RunEnvironment = "Integ"; // LocalTestServer or JUWIMM
													// or Integ or LoadServer

	// Other Environment Configurations
	public static String OSUsed = "WIN732"; // WINXP32,WinXP64,WinVista32,WinVista64,Win732,Win764,MAC
	public static boolean RuninLocalMachine = true; // true or false
	public static String HUBIP = "ds-54bef757d2a7.hclt.corp.hcl.in"; // IP or
																		// hostname
																		// of
																		// the
																		// HUB
	public static int HUBPort = 4444; // Port of the HUB

	// Application Configuration
	protected static String RunName = "Release3 - Formal Wave2";
	protected static String AppName = "easypod\u2122 connect Software"; // TradeMark
																		// sign
																		// is
																		// converted
																		// to
																		// Unicode.
	public static String ECKOURL, ECOSURL, WTTURL, AppUser; // This value will
															// be initiated in
															// LoadEnvironmentValues
	public static String AppModule = ""; // ECKO or ECOS

	// Automation Path Configuration
	protected static String FrameWorkPath = "T:/MS-Automation";
	protected static String DataPath = FrameWorkPath + "/EasyPod/data";
	protected static String TestDataPath = DataPath + "/TestData.xls";
	private static String LangDataPath = DataPath + "/LanguageData.xls";
	private static String ECKOORDataPath = DataPath + "/ECKO-ORData.xls";
	private static String ECOSORDataPath = DataPath + "/ECOS-ORData.xls";

	protected static String LocalProjectPath = "D:/AutoShared/MS-Automation";
	protected static String ResultPath = LocalProjectPath + "/EasyPod/results";
	protected static String LibPath = LocalProjectPath + "/Common/lib";
	protected static String ImagePath = LocalProjectPath + "/Common/img";

	public static String ScreenshotPath = null;
	protected static String StaticScreenshotName = null;
	protected static boolean isPageLayoutEnabled = true;

	// Automation RunTime Configuration
	public static String CurrentTestName = "";
	protected static String ProxyServer = "campus-proxy";
	protected static boolean isProxyRequired = false;
	protected static String BrowserName = null;
	protected static String OSName = null;
	public static String MachineName = null;
	public static String UserID = System.getProperty("user.name").toLowerCase();
	public static byte[] UserPhrase = null;
	protected static final String ServerLoginID = "epc";
	protected static final String ServerLoginPhrase = "1e1p1c";
	protected static String MainHandle = null;
	public static Logger logger = null;
	protected static boolean IsRunningFromDriver = false;
	public static Level LogLevel = Level.DEBUG; // TRACE,DEBUG,INFO,WARN,ERROR,FATAL
	protected static WebDriver BrowserObj = null;
	public static int ResponseTimeOut = 0; // Maximum Seconds to wait after
											// button click
	protected static int SyncTimeOut = 40; // Maximum Seconds to wait for
											// Element to appear
	protected static String BuildNumber = null;
	protected static String LanguageUsedWithDot = null; // CA.English will be
														// saved as CA.English
														// and it will not be
														// converted to
														// 'English'

	// Environment Excel Objects
	protected static ExcelUtil ECKO_OR;
	protected static ExcelUtil ECOS_OR;
	protected static ExcelUtil LangData;
	public static ExcelUtil TestData;

	// Patient Details for EasyPod
	public static String JsnId_ECOSPatient = null;
	public static String JsnId_NonECOSPatient = null;
	public static String JsnId_ECOSPatientFemale = null;
	public static String JsnId_Patient01 = null;
	public static String JsnId_Patient04 = null;
	public static String JsnId_Patient05 = null;

	public static String UniqueID_ECOSPatient = null;
	public static String UniqueID_NonECOSPatient = null;
	public static String UniqueID_ECOSPatientFemale = null;
	public static String UniqueID_Patient01 = null;
	public static String UniqueID_Patient04 = null;
	public static String UniqueID_Patient05 = null;

	// For Migration Admin User
	public static String MIGAdminUser = "GlobalAdminMigrated";
	public static String MIGAdminPassword = "xr2XzPuhmvVdRSDH";

	// Generic email configuration
	public static String EMAILURL = "https://chnmail.hcl.com/";
	public static String EmailUserName = "associates\\msrdsautomation";
	public static String EmailPassword = "Comnet@5678";
	public static String EmailID = "msrdsautomation@hcl.com";
	public static String EmailFromAddress = "system@easypodconnect2.com";
	public static String EmailToAddress = "Merck Serono Automation";

	public static String GetBrowserUsed() {
		return BrowserUsed;
	}

	public static void SetBrowserUsed(String BrowserName) {
		BrowserUsed = BrowserName.toUpperCase();
	}

	protected static String TruncateDotInLanguageUsed(String LanguageWithDot) {
		if (LanguageWithDot.contains(".")) {// Update Env.LanguageUsed
			return LanguageWithDot.substring(LanguageWithDot.indexOf(".") + 1,
					LanguageWithDot.length()); // Convert CA.French to French
		} else {
			return LanguageWithDot;
		}
	}

	protected static void LoadEnvironmentValues() {

		if (RunEnvironment.compareToIgnoreCase("LocalTestServer") == 0) {
			// LocalTestServer Environment
			ECKOURL = "http://10.145.139.89:8082/ecko/"; // Old URL =
															// 10.145.197.82
			ECOSURL = "http://10.145.139.89:8082/ecos/";
			ResponseTimeOut = 1;
			SyncTimeOut = 10;
			WTTURL = "http://10.145.139.89:8082/GPRS/uploadTest.jsp";
			isProxyRequired = false;
		} else if (RunEnvironment.compareToIgnoreCase("juwimm") == 0) {
			// JUWIMM Environment
			ECKOURL = "https://epc20.hcl.dev.juwimm.net/ecko/";
			ECOSURL = "http://epc20.hcl.dev.juwimm.net/ecos/";
			ResponseTimeOut = 2;
			SyncTimeOut = 14;
			isProxyRequired = false;
		} else if (RunEnvironment.compareToIgnoreCase("integ") == 0) {
			// integ Environment
			ECKOURL = "https://integ1.easypodconnect2.com/ecko/";
			ECOSURL = "https://integ1.easypodconnect2.com/ecos/";
			ResponseTimeOut = 1;
			SyncTimeOut = 10;
			if (Env.GetBrowserUsed().startsWith("SA")) {
				ResponseTimeOut = 2;
				SyncTimeOut = 14;
			}
			WTTURL = "https://integ1.rebismartdownloadonline.com/GPRS/uploadTest.jsp";
			isProxyRequired = false;
		} else if (RunEnvironment.compareToIgnoreCase("integ2") == 0) {
			// integ Environment
			ECKOURL = "https://integ1.rebismartdownloadonline.com/ecko/";
			ECOSURL = "https://integ1.rebismartdownloadonline.com/ecos/";
			ResponseTimeOut = 2;
			SyncTimeOut = 14;
			WTTURL = "https://integ1.rebismartdownloadonline.com/GPRS/uploadTest.jsp";
			isProxyRequired = false;
		} else if (RunEnvironment.compareToIgnoreCase("LoadServer") == 0) {
			// LoadServer
			ECKOURL = "http://10.100.148.470:8082/ecko/";
			ECOSURL = "http://10.100.148.470:8082/ecos/";
			ResponseTimeOut = 1;
			SyncTimeOut = 14;
			isProxyRequired = false;
		} else if (RunEnvironment.compareToIgnoreCase("LocalDevServer") == 0) {
			// LocalDevServer Environment
			ECKOURL = "http://10.145.169.52:8082/ecko/";
			ECOSURL = "http://10.145.169.52:8082/ecos/";
			ResponseTimeOut = 1;
			SyncTimeOut = 10;
			WTTURL = "http://10.100.148.53:8080/GPRS/uploadTest.jsp";
			isProxyRequired = false;
		} else if (RunEnvironment.compareToIgnoreCase("VAL") == 0) {
			// VAL Environment
			ECKOURL = "https://val1.easypodconnect2.com/ecko/";
			ECOSURL = "https://val1.easypodconnect2.com/ecos/";
			ResponseTimeOut = 1;
			SyncTimeOut = 14;
			WTTURL = "http://10.100.148.53:8080/GPRS/uploadTest.jsp";
			isProxyRequired = true;
		} else if (RunEnvironment.compareToIgnoreCase("PREVAL") == 0) {
			// PREVAL Environment
			ECKOURL = "https://preval1.easypodconnect2.com/ecko/";
			ECOSURL = "https://preval1.easypodconnect2.com/ecos/";
			ResponseTimeOut = 1;
			SyncTimeOut = 14;
			WTTURL = "http://10.100.148.53:8080/GPRS/uploadTest.jsp";
			isProxyRequired = true;
		} else if (RunEnvironment.compareToIgnoreCase("PROD") == 0) {
			// PROD Environment
			ECKOURL = "https://www.easypodconnect2.com/ecko/";
			ECOSURL = "https://www.easypodconnect2.com/ecos/";
			ResponseTimeOut = 1;
			SyncTimeOut = 14;
			WTTURL = "http://10.100.148.53:8080/GPRS/uploadTest.jsp";
			isProxyRequired = true;
		} else if (RunEnvironment.compareToIgnoreCase("SUPPORT") == 0) {
			// Support Environment
			ECKOURL = "https://support.easypodconnect2.com/ecko/";
			ECOSURL = "https://support.easypodconnect2.com/ecos/";
			ResponseTimeOut = 1;
			SyncTimeOut = 14;
			WTTURL = "";
			isProxyRequired = true;
		} else {
			Env.logger
					.log(Level.FATAL,
							"RunEnvironment - "
									+ RunEnvironment
									+ " is invalid. It should be either 'LocalTestServer' or 'JUWIMM'");
			System.exit(1);
		}

		if (Env.UserID.toLowerCase().trim().startsWith("chandravinodh.r")) {
			FrameWorkPath = "D:/AutoShared/MS-Automation";
			DataPath = FrameWorkPath + "/EasyPod/data";
			TestDataPath = DataPath + "/TestData.xls";
			LangDataPath = DataPath + "/LanguageData.xls";
			ECKOORDataPath = DataPath + "/ECKO-ORData.xls";
			ECOSORDataPath = DataPath + "/ECOS-ORData.xls";
		}

		ECKO_OR = new ExcelUtil(ECKOORDataPath, false);
		ECOS_OR = new ExcelUtil(ECOSORDataPath, false);
		LangData = new ExcelUtil(LangDataPath, true);
		TestData = new ExcelUtil(TestDataPath, true);

		// Load HUB IP
		try {
			InetAddress thisIp = InetAddress.getLocalHost();
			String CurrentMachineIP = thisIp.getHostAddress();
			if (CurrentMachineIP.contains("10.145.197.")) {
				HUBIP = CurrentMachineIP; // HUB PC in ODC
			} else {
				HUBIP = CurrentMachineIP; // HUB PC in LAB6. IP of
											// ds-d4bed9bd47fb
			}
		} catch (Exception e) {
			System.out.println("Unable to Load HUB IP address.");
		}
	}
}
