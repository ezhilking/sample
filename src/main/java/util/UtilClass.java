package functions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Level;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.io.Files;
import com.googlecode.fightinglayoutbugs.AnimationDetectedException;
import com.opera.core.systems.OperaDriver;
import com.opera.core.systems.OperaProfile;
import com.opera.core.systems.scope.exceptions.CommunicationException;

//Created By : Johnson Solomon D
//Created On : 08-Oct-2012

public class UtilClass {

	// Instance Variables
	protected static WebDriver browser;
	protected static WebDriverWait wait;
	protected static Actions UserAction;
	protected Reporter Report = new Reporter();
	// private FightingLayoutBugs FLB = null;
	protected WebDriver Secondbrowser = null;
	protected int ActiveBrowser = 1; // 1 is first browser and 2 is second
	// browser
	protected static String CountryFromDriver = null;
	protected static String LanguageFromDriver = null;

	// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-For
	// Browsers*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

	public WebDriver OpenBrowser(String url) {
		boolean FreshBrowser = false;
		Thread PopUpHandler = null;
		try {
			// Start the Browser
			if (Env.BrowserObj == null) { // Code to launch the Browser only
				// once
				Env.BrowserObj = GetBrowser(Env.GetBrowserUsed().toUpperCase());
				FreshBrowser = true;
				browser = Env.BrowserObj;
				if (ActiveBrowser == 1) {
					Env.BrowserName = GetBrowserInfo();
					Env.MachineName = GetMachineInfo();
					PopUp.GetScreen();
					Env.MainHandle = browser.getWindowHandle(); // save the
					// current
					// window
					// handle.
				}
				wait = new WebDriverWait(browser, Env.SyncTimeOut);
				UserAction = new Actions(browser);
			} else {
				browser.manage().deleteAllCookies();
				// browser.manage().timeouts().setScriptTimeout(30,
				// TimeUnit.SECONDS);
			}

			if (FreshBrowser || Env.GetBrowserUsed().startsWith("OP")) {
				// Start the PopUp Handing script for Handle any Alert or Win
				// Authentication PopUp
				PopUpHandler = new Thread(new PopUp("BrowserStartUp"),"PopUpHandler");
				PopUpHandler.start();
			}

			// Navigate to the URL
			try {
				// if (FreshBrowser ||
				// !browser.getTitle().startsWith(LangData("Login")))
				if (Env.GetBrowserUsed().startsWith("OP") && FreshBrowser) {
					browser.get("http://www.google.co.in");
					while (browser.getTitle().startsWith("Connect to Debugger")|| browser.getTitle().startsWith("opera:debug"));{
						Wait(2);
					}
				}
				browser.get(url);
			} catch (WebDriverException e) {
				if (!e.getMessage().startsWith("Navigation failed with error code=2.")) // Known
					// Exception
					// in
					// chrome,
					// when
					// authentication
					// popup
					// appears
					Env.logger.log(Level.ERROR, "", e);
			}

			// Override link for IE
			if (Env.GetBrowserUsed().startsWith("IE")&& browser.getTitle().startsWith("Certificate Error")) {
				browser.get("javascript:document.getElementById('overridelink').click();");
			}

			// Wait only if it is IE or SA or OP
			if (FreshBrowser) {
				if (Env.GetBrowserUsed().startsWith("IE")|| Env.GetBrowserUsed().startsWith("SA")|| Env.GetBrowserUsed().startsWith("OP")) {
					try {
						PopUpHandler.join();
					} catch (InterruptedException e) {
						Env.logger.log(Level.ERROR, "", e);
					}
				}
			}

			// For Debugging (occurs only in local machine)
			if (Env.RuninLocalMachine && (Env.GetBrowserUsed() == "OP" || Env.GetBrowserUsed() == "SA")&& Env.RunEnvironment.compareToIgnoreCase("LocalTestServer") != 0) {
				Wait(Env.SyncTimeOut / 2);
			} else {
				WaitForPageload();
			}

			// Maximize the browser
			if (Env.GetBrowserUsed().compareToIgnoreCase("OP") != 0) { // Opera
				// browser
				// maximize
				// is
				// addressed
				// by
				// custom
				// profile
				// settings
				browser.manage().window().maximize(); // For all browsers,
				// except Opera
			} else {
				// For Opera, wait until the title is not the value
				// "Connect to Debugger"
				while (browser.getTitle().startsWith("Google")) {
					Wait(2);
				}
				;
			}

		} catch (MalformedURLException e) {
			Env.logger.log(Level.ERROR, "", e);
		} catch (WebDriverException e) {
			if (e.getCause().toString().contains("exception.GridException")
					|| e.getMessage().toString()
					.contains("FORWARDING_TO_NODE_FAILED")
					|| e.getCause().toString()
					.contains("was terminated due to TIMEOUT")
					|| e.getCause().toString().contains("It may have died.")) {
				Env.logger
				.log(Level.ERROR,
						"Error while launching browser, maybe popuphandler took lot of time",
						e);
				if (Env.BrowserObj != null) {
					Env.BrowserObj = null; // Make the BrowserObj as null, so to
					// start a new browser on next run.
					Wait(60);// Wait for one minute
				}
			} else {
				Env.logger.log(Level.ERROR, "Error while launching browser."
						+ e.getMessage(), e);
			}
			throw e;
		}

		return browser;
	}

	// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

	protected WebDriver GetBrowser(String browserName)
			throws MalformedURLException {
		WebDriver LaunchingDriver = null;
		SupportedBrowserType browserType = null;
		try {
			browserType = SupportedBrowserType.valueOf(browserName);
		} catch (IllegalArgumentException e) {
			Env.logger.log(Level.FATAL, "'" + browserName
					+ "' is a invalid browser type. Stopping TestRun");
			System.exit(1);
		}
		DesiredCapabilities capability = null;
		switch (browserType) {
		case FF:
			if (Env.RuninLocalMachine == true) {
				File FirefoxProfilePath = new File(Env.LibPath
						+ "/FirefoxProfile/h13jojom.selenium");
				FirefoxProfile profile = new FirefoxProfile(FirefoxProfilePath);
				LaunchingDriver = new FirefoxDriver(profile);
				FirefoxProfilePath = null;
				profile = null;
			} else {
				capability = SetProxy(DesiredCapabilities.firefox());
			}
			break;
		case IE32:
			if (Env.RuninLocalMachine == true) {
				String IE_DRIVER_PATH = Env.LibPath + "\\IEDriverServer32.exe";
				System.setProperty("webdriver.ie.driver", IE_DRIVER_PATH);
				LaunchingDriver = new InternetExplorerDriver();
			} else {
				capability = SetProxy(DesiredCapabilities.internetExplorer());
			}
			break;
		case IE64:
			if (Env.RuninLocalMachine == true) {
				String IE_DRIVER_PATH = Env.LibPath + "\\IEDriverServer64.exe";
				System.setProperty("webdriver.ie.driver", IE_DRIVER_PATH);
				LaunchingDriver = new InternetExplorerDriver();
			} else {
				capability = SetProxy(DesiredCapabilities.internetExplorer());
			}
			break;
		case GC:
			if (Env.RuninLocalMachine == true) {
				String CHROME_DRIVER_PATH = Env.LibPath
						+ "\\chromedriverWIN.exe";
				System.setProperty("webdriver.chrome.driver",
						CHROME_DRIVER_PATH);
				try {
					LaunchingDriver = new ChromeDriver();
				} catch (WebDriverException e) {
					if (e.getMessage().contains(
							"Chrome version must be >= 38.0.2125.0")) {
						CHROME_DRIVER_PATH = Env.LibPath
								+ "\\chromedriverWIN212.exe";
						System.setProperty("webdriver.chrome.driver",
								CHROME_DRIVER_PATH);
						LaunchingDriver = new ChromeDriver();
					} else {
						throw e;
					}
				}
			} else {
				capability = SetProxy(DesiredCapabilities.chrome());
			}
			break;
		case OP:
			OperaProfile NewOperaProfile = new OperaProfile();
			File sourceFile = new File(Env.LibPath
					+ "/OperaProfile/operaprefs.ini");
			File destinationFile = new File(NewOperaProfile.getDirectory()
					+ "/" + "operaprefs.ini");
			try {
				FileUtils.copyFile(sourceFile, destinationFile);
			} catch (IOException e) {
				Env.logger.log(Level.ERROR, "", e);
			} finally {
				sourceFile = null;
				destinationFile = null;
			}
			capability = DesiredCapabilities.operaBlink();
			capability.setCapability("opera.profile", NewOperaProfile);
			capability.setCapability("opera.idle", true); // Added newly
			capability.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
			NewOperaProfile.preferences().set("Extensions", "Scripting", true);
			NewOperaProfile.preferences().set("User Prefs",
					"Ignore Unrequested Popups", true);
			if (Env.RuninLocalMachine == true) {
				LaunchingDriver = new OperaDriver(capability);
			} else {
				capability = SetProxy(capability);
			}

			break;

		case SA:
			if (Env.RuninLocalMachine == true) {
				LaunchingDriver = new SafariDriver();
			} else {
				capability = SetProxy(DesiredCapabilities.safari());
				Wait(2);
			}
			break;
		default:
			throw new RuntimeException("Browser type unsupported");
		}

		if (Env.RuninLocalMachine != true) {
			if (!Env.OSName.startsWith("MAC")) {
				capability.setVersion(Env.OSName + "-" + Env.MachineName); // For
				// windows
				// machine
				// it
				// OSName
				// and
				// MachineName
			} else {
				capability.setVersion(Env.OSName); // For MAC it is just Host
				// Name
			}
			try {
				LaunchingDriver = new RemoteWebDriver(new URL("http://"
						+ Env.HUBIP + ":4444/wd/hub"), capability); // Example:
				// http://localhost:4444/wd/hub
				// or
				// http://10.100.148.900:4444/wd/hub
				Env.logger.log(Level.INFO, "Not running on local machine");
			} catch (UnreachableBrowserException e) {
				Env.logger.log(Level.FATAL, "HUB (" + Env.HUBIP
						+ ") is down. Stopping the Test Suite");
				System.exit(1);
			} catch (WebDriverException e) {
				Env.logger.log(Level.FATAL, e.getCause().toString());// +
				// " Browser="+capability.getBrowserName()+" and OSversion="+capability.getVersion());

				// Safari Version is incorrect
				if (e.getCause().toString()
						.contains("Executor has not been started yet")
						&& Env.GetBrowserUsed().startsWith("SA"))
					Env.logger
					.log(Level.FATAL,
							"Check safari version. It should be 5 or more for selenium to work...");

				// Safari did not start properly
				if (e.getCause().toString()
						.contains("Failed to connect to SafariDriver after")) {
					try {
						LaunchingDriver = new RemoteWebDriver(new URL("http://"
								+ Env.HUBIP + ":4444/wd/hub"), capability); // Start
						// the
						// browser
						// again
					} catch (WebDriverException e1) {
						Env.logger
						.log(Level.FATAL,
								"Safari communication failed. Error while communicating to Node. Stopping the Test Suite");
						System.exit(1);
					}
				} else if (e.getCause().toString()
						.contains("unable to discover open pages")
						&& Env.GetBrowserUsed().startsWith("GC")) {
					// Handling "unable to discover open pages" error in chrome
					try {
						LaunchingDriver = new RemoteWebDriver(new URL("http://"
								+ Env.HUBIP + ":4444/wd/hub"), capability); // Start
						// the
						// browser
						// again
					} catch (WebDriverException e1) {
						Env.logger
						.log(Level.FATAL,
								"Unable to discover pages on the second time for chrome.");
					}
				} else {
					Env.logger
					.log(Level.FATAL,
							"Error while communicating to Node. Stopping the Test Suite");
					System.exit(1);
				}

			}
		}

		return LaunchingDriver;
	}

	public static String CheckPlatform(String OSTorun) {
		SupportedOSType PlatformType = null;
		try {
			PlatformType = SupportedOSType.valueOf(OSTorun);
		} catch (IllegalArgumentException e) {
			logger.info("'" + OSTorun
					+ "' is invalid OS type. Stopping TestRun");
			System.exit(1);
		}
		Env.OSName = PlatformType.toString();

		return PlatformType.toString();
	}

	public static String CheckBrowser(String BrowserName) {
		if (BrowserName.startsWith("IE32") && Env.OSUsed.endsWith("64")) {
			Env.logger
			.log(Level.INFO,
					"Browser is Set IE32 for a 64Bit machine, continuing to use IE32 itself");
			// Env.SetBrowserUsed("IE64");
		}
		SupportedBrowserType BrowserType = null;
		try {
			BrowserType = SupportedBrowserType.valueOf(BrowserName);
		} catch (IllegalArgumentException e) {
			logger.info("'" + BrowserName
					+ "' is invalid Browser type. Stopping TestRun");
			System.exit(1);
		}
		return BrowserType.toString();
	}

	// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

	public static void CloseBrowser() {
		if (browser != null) {
			try {
				browser.quit();
			} catch (Exception ignore) {
			}
		}
		browser = null;
		Env.BrowserObj = null;
		// If the Second browser is present.
		if (Env.GetBrowserUsed().startsWith("OP")) {
			try {
				Thread.sleep(3000);
			} catch (Exception ignore) {
			} // If Opera, then wait for the OperaPopupcheck to stop
		}
	}

	public void CloseOnlyThisBrowser() {
		if (browser != null) {
			try {
				browser.close();
			} catch (Exception ignore) {
			}
		}
	}

	public void DeleteCookies() {
		browser.manage().deleteAllCookies();
	}

	// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

	public boolean IsAlertPresent() {
		boolean ReturnValue = false;
		WebDriverWait WaitForAlert = new WebDriverWait(browser,
				Env.ResponseTimeOut * 5);
		try {
			if (WaitForAlert.until(ExpectedConditions.alertIsPresent()) != null)
				ReturnValue = true;
		} catch (TimeoutException e) {
			// Env.logger.log(Level.INFO, "Alert is not present");
			ReturnValue = false;
		}
		WaitForAlert = null;
		return ReturnValue;
	}

	// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

	public String ORXPath(String ExcelID) {

		if (!(ExcelID.startsWith("/") || ExcelID.startsWith("("))) {
			if (ExcelID.startsWith("#")) {
				return Env.ECOS_OR.GetXPath(ExcelID);
			} else {
				return Env.ECKO_OR.GetXPath(ExcelID);
			}
		} else {
			return ExcelID;
		}
	}

	// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

	public String ORData(String ExcelID) {
		String LangID;
		if (ExcelID.startsWith("#")) {
			LangID = Env.ECOS_OR.GetData(ExcelID);
		} else {
			LangID = Env.ECKO_OR.GetData(ExcelID);
		}

		if (!LangID.startsWith("Data is not found for TextID")) {
			if (LangID.contains("|")) {
				final List<String> DataColl = Arrays
						.asList(LangID.split("\\|"));
				StringBuffer DataBuffer = new StringBuffer();
				for (String DataElement : DataColl) {
					DataElement = DataElement.trim();
					if (DataElement.length() != 0) {
						DataBuffer.append(LangData(DataElement));
						DataBuffer.append("|");
					} else {
						DataBuffer.append("|");
					}
				}
				LangID = DataBuffer.toString();
				LangID = LangID.substring(0, LangID.length() - 1); // Truncate
				// the last
				// delimiter
				// (pipe
				// symbol)
				DataBuffer = null;
			} else {
				LangID = LangData(LangID);
			}
		}
		return LangID;
	}

	// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

	public String TestData(String ExcelID) {
		return Env.TestData.GetData(ExcelID);
	}

	public String GetTextIDForString(String LanguageColomn,
			String StringToSearch) {
		return Env.LangData.GetTextID(LanguageColomn, StringToSearch);
	}

	public String LangData(String ExcelID) {

		if (ExcelID.trim().length() == 0) {
			return "TextID is blank";
		}

		String StringID = null;
		String ReturnValue = null;
		int rowindex = 0;
		if (Env.IsRunningFromDriver) { // Update ExcelLogger for RTM, only if it
			// is running for driver.
			ExcelUtil.Excellogger.log(Level.INFO, Reporter.ResultStamp + "\t"
					+ Env.CurrentTestName + "\t" + "LangData" + "\t" + ExcelID);
		}

		rowindex = Env.LangData.GetRowIndex(0, ExcelID);
		if (rowindex != 0) {
			StringID = Env.LangData.GetData(1, rowindex);
			if (StringID.startsWith("XML")) {
				if (!(Env.CurrentTestName.startsWith("eCRF"))) {
					if (!(Env.CurrentTestName.startsWith("REG")
							|| Env.CurrentTestName.startsWith("CTQ") || "SUP_000_DataRecordsPrerequisite;ECOSAdmin_010_AuxologicalReports;ECOSHCP_006_CombinationGraph;ECOSAdmin_017_CombinationGraph;ZZZ_000_TestBaselineAdd;ZZZ_000_TestVisitsAdd;AAA_000_PreRequiste;ECOSHCP_013_AuxologicalReports;"
							.contains(Env.CurrentTestName))) {
						return "XML Data should be used only for DataRecords - '"
								+ ExcelID + "'";
					}
				}
			}
			ReturnValue = TextCleanUp((Env.LangData.GetData(
					Env.LangData.LangaugeColNo, rowindex)).trim());

			if (StringID.contains(".") && ReturnValue.contains(StringID)) {
				ReturnValue = "String ID: '"
						+ StringID
						+ "' is found in the translated string for the textID: '"
						+ ExcelID + "'";
			}
		} else {
			ReturnValue = "Data is not found for TextID - '" + ExcelID + "'";
		}
		return ReturnValue;
	}

	public void ReadOnlyEnglish(boolean IsOnlyEnglish) {
		Env.LangData.SwitchToEnglishLanguage(IsOnlyEnglish);
	}

	public void SwitchCountryAndLanguage(String CountryToSwitch,
			String LanguageToSwitch) {
		CountryFromDriver = Env.CountryUsed;
		LanguageFromDriver = Env.LanguageUsedWithDot;
		Env.TestData.SwitchToTestData(CountryToSwitch);
		Env.LangData.SwitchToLangData(LanguageToSwitch);
		Env.CountryUsed = CountryToSwitch;
		Env.LanguageUsedWithDot = LanguageToSwitch;
		Env.LanguageUsed = Env.TruncateDotInLanguageUsed(LanguageToSwitch);
		Env.logger
		.log(Level.INFO, "Country is switched to :" + CountryToSwitch);
		Env.logger.log(Level.INFO, "Language is switched to :"
				+ LanguageToSwitch);
	}

	public static void RevertCountryAndLanguageToDefault() {
		// Since running from Driver, revert to the orginal Country and Language
		// for the next script.
		if (UtilClass.CountryFromDriver != null) {
			Env.TestData.SwitchToTestData(UtilClass.CountryFromDriver);
			Env.LangData.SwitchToLangData(UtilClass.LanguageFromDriver);
			Env.CountryUsed = UtilClass.CountryFromDriver;
			Env.LanguageUsedWithDot = UtilClass.LanguageFromDriver;
			Env.LanguageUsed = Env
					.TruncateDotInLanguageUsed(UtilClass.LanguageFromDriver);
			UtilClass.LanguageFromDriver = null;
			UtilClass.CountryFromDriver = null;
		}
	}

	public void RevertCountryAndLanguage() {
		RevertCountryAndLanguageToDefault();
	}

	// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

	public boolean VerifyPageLayout() {
		boolean isPass = true;
		Env.StaticScreenshotName = null;

		// Get the StepID from the PageName
		String StepID = "Layout";
		String ScreenShotForPass = UtilClass.GetScreenshot(StepID);

		try {
			// if (Env.isPageLayoutEnabled){
			// isPass = true;
			// WaitForPageload();
			// Wait(1);
			// WaitForPageload();
			//
			// //HideCursor
			// HideCursor();
			//
			// //Screenshot for Pass scenario
			// String BrowserVersion = Env.BrowserName.split("ver")[1];
			//
			// if (FLB == null){
			// //FLB = new FightingLayoutBugs(new
			// DetectTextNearOrOverlappingVerticalEdge());
			// FLB = new FightingLayoutBugs(new
			// DetectTextNearOrOverlappingVerticalEdge(), new
			// DetectTextNearOrOverlappingHorizontalEdge() );
			// if (Env.BrowserUsed.startsWith("IE")||
			// BrowserVersion.startsWith("7")){
			// FLB = new FightingLayoutBugs(new
			// DetectTextNearOrOverlappingVerticalEdge());
			// }
			// //FLB.enableDebugMode();
			// File ScreenshotPath = new File(Env.ScreenshotPath);
			// FLB.setScreenshotDir(ScreenshotPath);
			// ScreenshotPath = null;
			// }
			//
			// //Get Browser Version
			// SimpleEdgeDetector ed = new SimpleEdgeDetector();
			// //TextDetector textDetectorA = new AnimationAwareTextDetector();
			// TextDetector textDetector = new SimpleTextDetector();
			// ed.setEdgeMinContrast(1.2d);
			// ed.setMinHorizontalEdgeLength(19);
			// ed.setMinVerticalEdgeLength(13);
			//
			// //Special Handling for IE versions 7 and 8
			// if (Env.BrowserUsed.startsWith("IE") &&
			// (BrowserVersion.startsWith("8") ||
			// BrowserVersion.startsWith("7"))){
			// ed.setMinVerticalEdgeLength(19); //Older Versions of IE, FLB does
			// not recognize alphabets with vertical lines. So increase the min
			// length
			// textDetector.ignore("h2.headerLogin"); //<h2 class="headerLogin">
			//
			// if (BrowserVersion.startsWith("7")){//Ignore all links for IE7
			// textDetector.ignore("a");
			// textDetector.ignore("div.footer"); //To ignore the links in the
			// footer
			// }
			//
			// if (ObjectExists("Login_Username_EB") &&
			// BrowserVersion.startsWith("8")){ //Ignore links in IE8, only in
			// login page
			// textDetector.ignore("a");
			// }
			// }
			//
			// if (Env.CurrentTestName.startsWith("HCP_005_ClinicPatients")){
			// textDetector.ignore("table#myTable > tbody");
			// }
			//
			// FLB.setEdgeDetector(ed);
			//
			// /* Default Values used inside the class
			// *
			// http://fighting-layout-bugs.googlecode.com/svn-history/r150/trunk/src/main/java/com/googlecode/fightinglayoutbugs/SimpleEdgeDetector.java
			// private double _edgeMinContrast = 1.5;
			// private double _similarColorMaxContrast = 1.5;
			// private int _minHorizontalEdgeLength = 16;
			// private int _minVerticalEdgeLength = 10;
			//
			//
			// //Add regions to ignore for textDetector
			//
			// textDetector.ignore("select");
			// textDetector.ignore("img");
			// textDetector.ignore(".lb_overlay"); //For background overlay, on
			// edit popup
			// textDetector.ignore(".js_lb_overlay"); //For background overlay,
			// on edit popup
			// textDetector.ignore("div#fade"); //For Signout popup
			//
			// if (Env.GetBrowserUsed().startsWith("GC")){
			// textDetector.ignore("a"); //For links in Chrome
			// textDetector.ignore("span.fr"); //For edit links in Chrome
			// (MyAccount, clinic details)
			// }
			// if (browser.getTitle().startsWith(LangData("LicenseAgreement"))
			// || browser.getTitle().startsWith(LangData("FeaturesList")) ||
			// browser.getTitle().startsWith(LangData("TermsOfUse"))){
			// textDetector.ignore("div.content"); //For License Agreement,
			// Terms and Feature list bulk Text in fist time login
			// }
			// if (browser.getTitle().startsWith(LangData("LicenseAgreement"))){
			// textDetector.ignore("div.contentBgGreen"); //For Local component
			// bulk text
			// }
			// if
			// (browser.getTitle().startsWith(LangData("InstructionsForUse"))){
			// textDetector.ignore("div.blueBorder"); //For the PDF icon in
			// Instuction for Use page
			// }
			// if (browser.getTitle().startsWith(LangData("FeaturesList"))){
			// textDetector.ignore("div.cntntWrapper"); //For Features List
			// under the help tab
			// }
			// if
			// (Env.CurrentTestName.startsWith("ECOSAdmin_010_EnrollmentReports")){
			// textDetector.ignore("table.tableLeftFixed > tbody"); //For the
			// first column in the table which appears with scroll bar
			// }
			// FLB.setTextDetector(textDetector);
			//
			// WebPage webPage = new WebPage(browser);
			// final Collection<LayoutBug> layoutBugs =
			// FLB.findLayoutBugsIn(webPage);
			// String ScreenshotForFail = null;
			// File BugFile = null;
			// File NewFile = null;
			// for (LayoutBug bug : layoutBugs) {
			// BugFile = bug.getScreenshot();
			// NewFile = new File(Env.ScreenshotPath +"/" +
			// BugFile.getName().replace(".png", "") + "-" + Env.BrowserUsed +
			// ".png");
			// if (BugFile.renameTo(NewFile)){
			// ScreenshotForFail = NewFile.getName();
			// }else{
			// ScreenshotForFail = BugFile.getName();
			// }
			// Report.Write(StepID, "No Overlapping", bug.getDescription(),
			// "Fail", ScreenshotForFail);
			// isPass = false;
			// }
			// webPage = null;
			//
			// if(isPass){
			// Report.Write(StepID, "No Overlapping", "No Overlapping is found",
			// "Pass", ScreenShotForPass);
			// }
			//
			// }else{
			// isPass = false;
			// Report.Write("PageLayout", "Page Layout is disabled",
			// "Page Layout is disabled", "Pass", false);
			// }

		} catch (AnimationDetectedException e) {
			Env.logger.log(Level.ERROR, "", e);
			Report.Write(StepID, "No Overlapping", "No Overlapping is found",
					"Pass", ScreenShotForPass);
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, "", e);
			Report.Write(StepID, "No Overlapping", "No Overlapping is found",
					"Pass", ScreenShotForPass);
		} finally {
			if (Env.GetBrowserUsed().startsWith("SA")
					&& (Env.OSName.startsWith("WINXP32"))
					&& Env.CurrentTestName
					.startsWith("ECOSHCP_018_AuxologicalReports")) {
				Thread PopUpHandler = new Thread(new PopUp("DataPrevention"),
						"PopUpHandler");
				PopUpHandler.start();
			}
			this.RefreshPage();
			WaitForPageload();
			if (Env.GetBrowserUsed().startsWith("IE")
					|| Env.GetBrowserUsed().startsWith("OP")) { // Taking long
				// time in IE
				// and OP
				Wait(Env.ResponseTimeOut);
				WaitForPageload();
			}
		}
		return isPass;
	}

	// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

	public static String GetScreenshot(String sFileName) {
		FileOutputStream FOS = null;
		File ScreenshotFile = null;
		WebDriver screenshotdriver = browser;
		try {
			if (Env.StaticScreenshotName == null) {
				if (Env.BrowserObj != null) {
					sFileName = sFileName.replaceAll(" ", "");
					sFileName = sFileName.replaceAll("#", "");
					sFileName = Env.ScreenshotPath + "/" + sFileName + "-"
							+ Reporter.GetTimeStamp("hhmmssSSS") + "-"
							+ Env.GetBrowserUsed() + ".png";
					ScreenshotFile = new File(sFileName);
					sFileName = ScreenshotFile.getName();

					FOS = new FileOutputStream(ScreenshotFile);
					if (!Env.RuninLocalMachine) {
						screenshotdriver = new Augmenter().augment(browser);
					}
					FOS.write(((TakesScreenshot) screenshotdriver)
							.getScreenshotAs(OutputType.BYTES));

					FOS.flush();
					FOS.close();
				} else {
					Env.logger.log(Level.INFO,
							"Browser object is null, so not taking screenshot for - "
									+ sFileName);
				}
			} else {
				sFileName = Env.StaticScreenshotName;
			}
		} catch (WebDriverException e) {
			Env.logger.log(Level.ERROR, "While taking screenshot for - "
					+ sFileName, e);
		} catch (IOException e) {
			Env.logger.log(Level.ERROR, "While taking screenshot for - "
					+ sFileName, e);
		} finally {
			try {
				FOS = null;
				ScreenshotFile = null;
				screenshotdriver = null;
			} catch (Exception e) {
				Env.logger.log(Level.ERROR, "", e);
			}
		}
		return sFileName;
	}

	// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

	public String GetUniqueName(String sFileName) {
		return Env.ScreenshotPath + "/" + sFileName + "-"
				+ GetTimeStamp("hhmmssSSS") + Env.GetBrowserUsed();
	}

	// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

	public String GetTimeStamp(String DateFormat) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(DateFormat);
		String formattedDate = sdf.format(date);
		date = null;
		sdf = null;
		return formattedDate;
	}

	// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

	public String GetServerTimeStamp(String DateFormat) {

		TimeZone tz = null;
		if (Env.RunEnvironment.startsWith("LocalTestServer")) {
			tz = TimeZone.getTimeZone("Asia/Calcutta");
		} else {
			tz = TimeZone.getTimeZone("Europe/London");
		}

		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(DateFormat);
		sdf.setTimeZone(tz);
		String formattedDate = sdf.format(date);
		date = null;
		sdf = null;
		return formattedDate;
	}

	// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
	public enum SupportedBrowserType {
		IE32, IE64, GC, OP, SA, FF
		// FF - FireFox
		// IE - InternetExplorer
		// GC - Google Chrome
		// OP - Opera
		// SA - Safari

	}

	// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

	public enum SupportedOSType {
		WINXP32, WINXP64, WIN732, WIN764, WIN832, WIN864, MAC107, MAC108, MAC109, MAC1010
		// WINXP32 - windows XP 32Bit
		// WINXP64 - windows XP 64Bit
		// WINVISTA32 - windows Vista 32Bit
		// WINVISTA64 - windows Vista 64Bit
		// WIN732 - Windows 7 32bit
		// WIN764 - Windows 7 64bit
		// WIN864 - Windows 8 64bit
		// MAC107 - MAC 10.7 (Lion)
		// MAC108 - MAC 10.8 (Mountain Lion)
		// MAC109 - MAC 10.9 (Mavericks)
		// MAC1010 - MAC 10.10 (Yosemite)
	}

	// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

	public Object RunJavaScript(String Script) {
		try {
			return ((JavascriptExecutor) browser).executeScript(Script);
		} catch (UnhandledAlertException e) {
			if (!CompareText(Env.GetBrowserUsed(), "SA")) { // Do not log for
				// Safari
				Env.logger.log(Level.WARN,
						"UnhandledAlertException: " + e.getAlertText());
			}
			return "UnhandledAlertError";
		} catch (NullPointerException e) {
			throw new RuntimeException("Browser is null");
		} catch (Exception e) {
			Env.logger.log(Level.ERROR,
					"JavaScriptExecution failed. " + Script, e);
			return "complete";
		}
	}

	// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

	public Object RunJavaScript(WebElement ActionObject, String Script) {
		// arguments[0].style.border='3px solid red'
		try {
			return ((JavascriptExecutor) browser).executeScript(Script,
					ActionObject);
		} catch (UnhandledAlertException e) {
			if (!CompareText(Env.GetBrowserUsed(), "SA")) { // Do not log for
				// Safari
				Env.logger.log(Level.WARN,
						"UnhandledAlertException: " + e.getAlertText());
			}
			return "UnhandledAlertError";
		} catch (Exception e) {
			return "Error";
		}
	}

	// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

	public boolean HandleAlerts() {
		if (Env.GetBrowserUsed().compareToIgnoreCase("OP") == 0) { // Special
			// handling
			// for OP
			// Env.logger.log(Level.INFO,"Opera Alert handling Started...");
			// Keyboard keyboard = ((RemoteWebDriver) browser).getKeyboard();
			// keyboard.sendKeys(Keys.ENTER);
			// keyboard = null;
			// Env.logger.log(Level.INFO,"Opera Alert handling Ended...");
			return true;
		} else {
			if (IsAlertPresent()) {
				// Some alert is present
				Alert alert = browser.switchTo().alert();
				Env.logger.log(Level.WARN, "Alert: " + alert.getText()
				+ " is displayed"); // Get the text from the alert
				alert.accept(); // Accept the alert
				browser.switchTo().window(Env.MainHandle); // Switch back to the
				// application
				return true;
			} else {
				return false;
			}
		}

	}

	// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

	public String GetBrowserInfo() {

		// String test = (String) RunJavaScript("return document.readyState;");
		String browserAgent = (String) RunJavaScript("return navigator.userAgent;");
		String browserName = (String) RunJavaScript("return navigator.appName;");
		String browserBit = (String) RunJavaScript("return navigator.platform;");

		String fullVersion = "", returnValue = "";
		int verOffset, nameOffset, ix;

		// For Opera
		if ((verOffset = browserAgent.indexOf("Opera")) != -1) {
			browserName = "Opera";
			fullVersion = browserAgent.substring(verOffset + 6);
			if ((verOffset = browserAgent.indexOf("Version")) != -1)
				fullVersion = browserAgent.substring(verOffset + 8);
		}

		// For Internet Explorer
		else if ((verOffset = browserAgent.indexOf("MSIE")) != -1) {
			if (browserBit.compareToIgnoreCase("Win64") == 0) {
				browserName = "IE" + "-64Bit";
			} else {
				browserName = "IE" + "-32Bit";
			}
			fullVersion = browserAgent.substring(verOffset + 5);
		}

		// For Internet Explorer 11
		else if ((verOffset = browserAgent.indexOf("rv:11")) != -1) {
			if (browserBit.compareToIgnoreCase("Win64") == 0) {
				browserName = "IE" + "-64Bit";
			} else {
				browserName = "IE" + "-32Bit";
			}
			fullVersion = browserAgent.substring(verOffset + 3, verOffset + 7);
		}

		// For Chrome
		else if ((verOffset = browserAgent.indexOf("Chrome")) != -1) {
			browserName = "Chrome";
			fullVersion = browserAgent.substring(verOffset + 7);
		}

		// For Safari
		else if ((verOffset = browserAgent.indexOf("Safari")) != -1) {
			browserName = "Safari";
			fullVersion = browserAgent.substring(verOffset + 7);
			if ((verOffset = browserAgent.indexOf("Version")) != -1)
				fullVersion = browserAgent.substring(verOffset + 8);
		}

		// For Firefox
		else if ((verOffset = browserAgent.indexOf("Firefox")) != -1) {
			browserName = "Firefox";
			fullVersion = browserAgent.substring(verOffset + 8);
		}

		// For other browsers, "name/version" is at the end of userAgent
		else if ((nameOffset = browserAgent.lastIndexOf(' ') + 1) < (verOffset = browserAgent
				.lastIndexOf('/'))) {
			browserName = browserAgent.substring(nameOffset, verOffset);
			fullVersion = browserAgent.substring(verOffset + 1);
		}

		// trim the fullVersion string at semicolon/space/dot if present
		if ((ix = fullVersion.indexOf(";")) != -1)
			fullVersion = fullVersion.substring(0, ix);
		if ((ix = fullVersion.indexOf(" ")) != -1)
			fullVersion = fullVersion.substring(0, ix);
		// if (fullVersion.indexOf(".")!= -1)
		// fullVersion = fullVersion.substring(0, fullVersion.indexOf("."));

		// ((RemoteWebDriver)browser).getCapabilities().getCapability("browserName");
		// ((RemoteWebDriver)browser).getCapabilities().getCapability("version");
		// fullVersion =
		// ((RemoteWebDriver)browser).getCapabilities().getCapability("version").toString();
		returnValue = browserName + " ver" + fullVersion;

		browserAgent = null;
		browserName = null;
		browserBit = null;
		fullVersion = null;
		return returnValue; // Format: IE-32Bit ver7 (or) Safari ver5

	}

	// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

	public String GetMachineInfo() {

		String MachineUniqueID = "";
		// String yourHubIP = Env.HUBIP;
		// int yourHubPort = Env.HUBPort;
		// HttpHost host = null;
		// DefaultHttpClient client = null;
		// URL testSessionApi;
		// try {
		//
		// if (Env.RuninLocalMachine != true){
		// host = new HttpHost(yourHubIP, yourHubPort);
		// client = new DefaultHttpClient();
		// testSessionApi = new URL("http://" + yourHubIP + ":" + yourHubPort +
		// "/grid/api/testsession?session=" + ((RemoteWebDriver)
		// browser).getSessionId());
		// BasicHttpEntityEnclosingRequest r = new
		// BasicHttpEntityEnclosingRequest("POST",
		// testSessionApi.toExternalForm());
		// HttpResponse response = client.execute(host,r);
		// JsonObject object = (JsonObject)new
		// JsonParser().parse(EntityUtils.toString(response.getEntity()));
		// MachineUniqueID = object.get("proxyId").toString();
		// MachineUniqueID = MachineUniqueID.replaceAll("http://", "");
		// MachineUniqueID = MachineUniqueID.replace("\"", "");
		// MachineUniqueID = MachineUniqueID.substring(0,
		// MachineUniqueID.indexOf(":"));
		// r = null;
		// response = null;
		// object = null;
		// }else{
		// InetAddress thisIp =InetAddress.getLocalHost();
		// MachineUniqueID = thisIp.getHostAddress();
		// thisIp = null;
		// }
		//
		// } catch (UnknownHostException e) {
		// Env.logger.log(Level.ERROR,"",e);
		// } catch (MalformedURLException e) {
		// Env.logger.log(Level.ERROR,"",e);
		// } catch (ClientProtocolException e) {
		// Env.logger.log(Level.ERROR,"",e);
		// } catch (IOException e) {
		// Env.logger.log(Level.ERROR,"",e);
		// }finally{
		// yourHubIP = null;
		// host = null;
		// client = null;
		// testSessionApi = null;
		// }

		try {
			if (!Env.RuninLocalMachine) {
				MachineUniqueID = Env.MachineName;
			} else {
				InetAddress thisIp = InetAddress.getLocalHost();
				MachineUniqueID = thisIp.getHostName();
				thisIp = null;
			}

			if (Env.OSUsed.startsWith("MAC")) {
				if (Env.OSUsed.startsWith("MAC107")) {
					MachineUniqueID = "MAC 10.7";
				} else if (Env.OSUsed.startsWith("MAC108")) {
					MachineUniqueID = "MAC 10.8";
				} else if (Env.OSUsed.startsWith("MAC109")) {
					MachineUniqueID = "MAC 10.9";
				} else {
					MachineUniqueID = "MAC 10.10";
				}
			}
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, "", e);
			MachineUniqueID = null;
		}

		return MachineUniqueID;
	}

	// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

	public void RefreshPage() {
		try {
			browser.navigate().refresh();
			Wait(Env.ResponseTimeOut);
			HandleAlerts();
			WaitForPageload();
		} catch (WebDriverException e) {
			if (HandleAlerts() == false) { // some error occurred while
				// refreshing the page
				Env.logger.log(Level.WARN, e);
			}
		}
	}

	// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

	private DesiredCapabilities SetProxy(DesiredCapabilities Capability) {
		// String PROXY = "vpln-proxy:80";
		// Proxy proxy = new Proxy();
		// proxy.setHttpProxy(PROXY)
		// .setFtpProxy(PROXY)
		// .setSslProxy(PROXY);
		// Capability.setCapability(CapabilityType.PROXY, proxy);
		Capability.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		return Capability;
	}

	// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

	public void Wait(int TimeOutInSecs) {
		try {
			Thread.sleep((TimeOutInSecs * 1000));
		} catch (InterruptedException e) {
			Env.logger.log(Level.WARN, e);
		}
	}

	// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

	public void WaitForPageload() {
		String BrowserStatus;
		do {
			Wait(1); // this should be 1 always
			try {
				BrowserStatus = (String) RunJavaScript("return document.readyState;");
				// For Opera
				if (CompareText(Env.GetBrowserUsed(), "OP")
						&& BrowserStatus.startsWith("complete")) {
					if (WaitForObject(ORXPath("General_HideCursor_ST"))) {
						BrowserStatus = "complete";// Running JavaScript in
						// Opera is going to end up
						// in an error, so replaced
						// it with first div tag
					} else {
						BrowserStatus = "loading";
					}
				}
			} catch (Exception e) {
				Env.logger.log(Level.WARN, e);
				BrowserStatus = "complete";
			}
		} while (!(BrowserStatus.compareToIgnoreCase("complete") == 0));
		BrowserStatus = null;
	}

	// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

	public boolean WaitForObject(String ObjectName) {
		boolean ReturnValue = false;
		String sXPath = ObjectName;
		try {
			sXPath = ORXPath(ObjectName);
			if (!sXPath.startsWith("XPath is not found for TextID")) {
				// wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(sXPath)));
				wait.until(ExpectedConditions.elementToBeClickable(By
						.xpath(sXPath)));
				ReturnValue = true;
			} else {
				Env.logger.log(Level.ERROR, "XPath Error: " + sXPath);
			}
		} catch (TimeoutException e) {
			// Env.logger.log(Level.WARN, "Timeout for Object : "+sXPath);
			if (Env.GetBrowserUsed().startsWith("OP")) {
				// OperaPopUpCheck();
				OperaNetworkError();
			}
		} catch (CommunicationException e) {
			Env.logger.log(Level.ERROR, ObjectName, e);
			if (Env.BrowserObj != null) {
				Env.BrowserObj = null; // Make the BrowserObj as null, so to
				// start a new browser on next run.
				Wait(60);
			}
		} catch (UnreachableBrowserException e) {
			Env.logger.log(Level.ERROR, ObjectName, e);
			if (Env.BrowserObj != null) {
				Env.BrowserObj = null; // Make the BrowserObj as null, so to
				// start a new browser on next run.
				Wait(60);
			}
		} catch (Exception e) {
			if (e.getCause().toString().contains("exception.GridException")
					|| e.getMessage().toString()
					.contains("FORWARDING_TO_NODE_FAILED")
					|| e.getCause().toString()
					.contains("was terminated due to TIMEOUT")
					|| e.getCause().toString().contains("It may have died.")) {
				Env.logger.log(Level.ERROR, ObjectName, e);
				if (Env.BrowserObj != null) {
					Env.BrowserObj = null; // Make the BrowserObj as null, so to
					// start a new browser on next run.
					Wait(60);
				}
			} else {
				Env.logger.log(Level.ERROR, ObjectName, e);
			}

		} finally {
			sXPath = null;
		}
		return ReturnValue;
	}

	// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

	public WebElement GetObject(String ObjectName) {
		String sXPath = ObjectName;
		WebElement FoundObject = null;
		if (!(ObjectName.startsWith("/") || ObjectName.startsWith("("))) {
			sXPath = ORXPath(ObjectName);
			if (Env.GetBrowserUsed().startsWith("OP")) {
				WaitForPageload();
			}
			WaitForObject(sXPath);
		}
		try {
			// //If the session is terminated for some reason, then
			// WaitForObject would have set the Env.BrowserObj to null.
			// //So if the sesssion is alive, only then get the Object
			// if (Env.BrowserObj != null){
			FoundObject = browser.findElement(By.xpath(sXPath));
			// }else{
			// FoundObject = null;
			// }
			// if ((Env.GetBrowserUsed().startsWith("OP") ||
			// Env.GetBrowserUsed().startsWith("SA")) &&
			// FoundObject.getLocation().getY() >= 600){
			// MoveInToViewPort(sXPath);
			// }

		} catch (NoSuchElementException e) {
			Env.logger.log(Level.ERROR, "Object not found: " + sXPath);

			// Application Excetpion Handling Block - Begin
			if (browser.getTitle().contains(LangData("Denied"))
					|| browser.getTitle().contains("Exception")
					|| browser.getTitle().contains("Error")
					|| browser.getTitle().contains(
							LangData("ErrorBrowserTitle"))
					|| browser.getTitle().contains("Problem loading page")
					|| browser.getTitle().contains("cannot display")
					|| browser.getTitle().contains("is not available")
					|| browser.getTitle().contains("Failed to open page")) {
				AppExceptionHandler();
			}
		} catch (UnreachableBrowserException e) {
			throw e;
		} finally {
			sXPath = null;
		}
		return FoundObject;

	}

	protected void AppExceptionHandler() {

		// Clear the Static Screenshot
		Env.StaticScreenshotName = null;

		// Write the current page source into a text file
		File ExpectionFile = new File(GetUniqueName("ExceptionOccured")
				+ ".txt");
		try {
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(ExpectionFile),
					Charset.forName("UTF-8")));
			writer.write(browser.getPageSource());
			writer.flush();
			writer = null;
		} catch (Exception Ignore) {
		}

		// Write the failure in the report with text file as screenshot.
		Report.Write("ApplicationException",
				"There should be no Application Exception",
				ExpectionFile.getName(), "Fail", true);

		// Construct the Home page URL of the User
		String UsersHomePageURL = Env.ECOSURL;
		if (Env.AppModule.startsWith("ECKO")) { // Navigate to home page
			UsersHomePageURL = Env.ECKOURL;
		}

		// Recover from the error by navigating back to the page.
		try {
			if (Env.GetBrowserUsed().startsWith("SA")) {
				RunJavaScript("window.history.back();");
			} else {
				browser.navigate().back();
			}
			HandleAlerts();
			String BrowserTitleAfterRecovery = browser.getTitle();
			if (BrowserTitleAfterRecovery.contains(LangData("ApplicationName"))
					|| BrowserTitleAfterRecovery
					.contains(LangData("StudiesECOSCAPS"))) {
				// Title is containing the string ECKO or ECOS, so the recovery
				// is successful.
				Env.logger
				.log(Level.ERROR,
						"Application Exception occured and it was recovered by navigating back");
			} else {
				NavigateTo(UsersHomePageURL);
				Env.logger
				.log(Level.ERROR,
						"Application Exception occured and it was recovered by navigating to home page");
			}
		} catch (Exception Ignore) {
			logger.info(Ignore);
		}

		// Throw the exception, so that the current test is stopped.
		throw new RuntimeException("Application Exception occured");
	}

	// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*StaticText-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

	protected String GetText(WebElement ObjectName) {
		String ReturnValue = "";
		try {
			ReturnValue = ObjectName.getText();
		} catch (Exception e) {
			ReturnValue = "OBJECT NOT FOUND";
		}
		return ReturnValue;
	}

	// returns the text of a webElement
	public String GetText(String ObjectName) {
		String ReturnValue = "";
		try {
			WebElement ActionObject = GetObject(ObjectName);
			String sTagName = ActionObject.getTagName().toUpperCase();
			if (CompareText(sTagName, "INPUT")) {
				sTagName = ActionObject.getAttribute("type").toUpperCase();
				if (CompareText(sTagName, "TEXT")) { // EditBox
					HideCursor();
					ReturnValue = ActionObject.getAttribute("placeholder");
				} else if (CompareText(sTagName, "SUBMIT")
						|| CompareText(sTagName, "BUTTON")) {
					ReturnValue = ActionObject.getAttribute("value"); // Button
				} else {
					Env.logger.log(Level.ERROR, "Check for this control - "
							+ sTagName);
					ReturnValue = ActionObject.getText(); // Unknown type with
					// TagName as INPUT
				}
			} else if (CompareText(sTagName, "SELECT")) {
				ReturnValue = DrpDwn_GetText(ObjectName);
			} else if (CompareText(sTagName, "OPTGROUP")) {
				ReturnValue = ActionObject.getAttribute("label");
			} else {
				ReturnValue = ActionObject.getText();
				// Text - DIV,P,H1,H2,H3,UL,LI,LABEL
				// Link - A
				// BrowserTitle - TITLE
			}

		} catch (NullPointerException e) {
			ReturnValue = "OBJECT NOT FOUND";
		} catch (StaleElementReferenceException e) {
			ReturnValue = "STALE ELEMENT EXCEPTION";
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, ObjectName, e);
			ReturnValue = "ERROR OCCURED";
		}

		return ReturnValue;
	}

	public String GetOnlyMyText(String ObjectName) {
		String ReturnValue = "";
		String ChildText = "";
		try {
			WebElement ParentElement = GetObject(ObjectName);
			ReturnValue = ParentElement.getText();
			List<WebElement> ChildColl = ParentElement.findElements(By
					.xpath("descendant::*"));
			for (WebElement ChildElement : ChildColl) {
				ChildText = ReplaceSplCharacters(ChildElement.getText());
				if (!ChildText.isEmpty()) {
					ReturnValue = ReturnValue.replaceFirst(ChildText, "");
				}
			}
		} catch (NullPointerException e) {
			ReturnValue = "OBJECT NOT FOUND";
		} catch (StaleElementReferenceException e) {
			ReturnValue = "STALE ELEMENT EXCEPTION";
		} catch (Exception e) {
			ReturnValue = "ERROR OCCURED";
		}

		return ReturnValue.trim().replaceAll("^", "");
	}

	public String TextCleanUp(String InputString) {

		if (InputString == null) {
			return "null";
		}

		// Trim the spaces, before checking the starting and ending characters
		InputString = InputString.trim();

		// String CleanUp - Remove * and : it it appears at the end or beginning
		// of the String
		if (InputString.startsWith("*"))
			InputString = InputString.replaceAll("^(\\*)", "");

		if (InputString.endsWith("*"))
			InputString = InputString.replaceAll("(\\*)$", "");

		// Replace all newline with space
		InputString = InputString.replaceAll("\\n", " ");

		// Replace all double or more spaces with a single space
		InputString = InputString.replaceAll(" {2,}", " ");

		// Replace all unicode space (nbsp;) with normal space
		InputString = InputString.replaceAll("\u00A0", " ");

		// //Replace all hyphen with normal keyboard hyphen
		// InputString = InputString.replaceAll("\u2013", "-");

		return InputString.trim();
	}

	// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*WEBEDIT-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

	// sets the value in editbox
	public boolean Edit_SetValue(String ObjectName, String sSetValue) {
		try {
			WebElement ActionObject = GetObject(ObjectName);
			ActionObject.clear();
			ActionObject.sendKeys(sSetValue);
			// if (ActionObject.getAttribute("value").compareTo(sSetValue)!=0){
			// Env.logger.log(Level.ERROR,ObjectName + " value is not set to "
			// +sSetValue);
			// }
			return true;
		} catch (StaleElementReferenceException e) {
			if (Env.GetBrowserUsed().compareToIgnoreCase("SA") == 0) {
				Env.logger.log(Level.ERROR, "Setting by javascript", e);
				Edit_SetValueByJavaScript(ObjectName, sSetValue);
				return true;
			} else {
				Env.logger.log(Level.ERROR, ObjectName, e);
				return false;
			}
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, ObjectName, e);
			return false;
		}
	}

	// sets the value in editbox
	public boolean Edit_SetValue(String ObjectName, int sSetValue) {
		try {
			WebElement ActionObject = GetObject(ObjectName);
			ActionObject.clear();
			ActionObject.sendKeys(String.valueOf(sSetValue));
			// if (ActionObject.getAttribute("value").compareTo(sSetValue)!=0){
			// Env.logger.log(Level.ERROR,ObjectName + " value is not set to "
			// +sSetValue);
			// }
			return true;
		} catch (StaleElementReferenceException e) {
			if (Env.GetBrowserUsed().compareToIgnoreCase("SA") == 0) {
				Edit_SetValueByJavaScript(ObjectName, String.valueOf(sSetValue));
				return true;
			} else {
				Env.logger.log(Level.ERROR, ObjectName, e);
				return false;
			}
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, ObjectName, e);
			return false;
		}
	}

	// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

	// clears , waits and sets the value in editbox
	public boolean Edit_ClearWaitAndSet(String ObjectName, String sSetValue) {
		try {
			WebElement ActionObject = GetObject(ObjectName);
			ActionObject.clear();
			Wait(1);
			WaitForPageload();
			ActionObject.sendKeys(sSetValue);
			// if (ActionObject.getAttribute("value").compareTo(sSetValue)!=0){
			// Env.logger.log(Level.ERROR,ObjectName + " value is not set to "
			// +sSetValue);
			// }
			return true;
		} catch (StaleElementReferenceException e) {
			if (Env.GetBrowserUsed().compareToIgnoreCase("SA") == 0) {
				Edit_SetValueByJavaScript(ObjectName, sSetValue);
				return true;
			} else {
				Env.logger.log(Level.ERROR, ObjectName, e);
				return false;
			}
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, ObjectName, e);
			return false;
		}
	}

	// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

	// Clears the value in editbox
	public boolean Edit_ClearValue(String ObjectName) {
		try {
			WebElement ActionObject = GetObject(ObjectName);
			ActionObject.clear();
			return true;
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, ObjectName, e);
			return false;
		}
	}

	// Clears the value For all the editbox
	public boolean Edit_ClearValueForAll(String ObjectXPath) {
		try {
			List<WebElement> ListOfObjects = browser.findElements(By
					.xpath(ObjectXPath));
			for (WebElement ActionObject : ListOfObjects) {
				try {
					ActionObject.clear();
				} catch (Exception ignore) {
					// Exception may occur, while clearing some Editbox
				}
			}
			return true;
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, ObjectXPath, e);
			return false;
		}

	}

	// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

	// gets the value from editbox
	public String Edit_GetValue(String ObjectName) {
		try {
			WebElement ActionObject = GetObject(ObjectName);
			return ActionObject.getAttribute("value");
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, ObjectName, e);
			return "OBJECT NOT FOUND";
		}
	}

	// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*LINK-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
	// clicks the link
	public boolean Lnk_Click(String ObjectName) {
		try {
			WebElement ActionObject = GetObject(ObjectName);
			if (ActionObject.isEnabled()) {
				if (Env.GetBrowserUsed().startsWith("IE")) {
					MoveMouseTo(ActionObject);
				}
				ActionObject.click();
				Wait(Env.ResponseTimeOut / 2);
				WaitForPageload();
				return true;
			} else {
				Env.logger.log(Level.ERROR, ObjectName + " link is disabled");
				return false;
			}
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, ObjectName
					+ " Exception is captured in Screenshot : "
					+ GetScreenshot("Exception"), e);
			return false;
		}
	}

	// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*RADIOBUTTON-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
	// select the radiobutton
	public boolean Rbtn_SetOption(String ObjectName, String SetValue) {
		try {
			WebElement ActionObject = GetObject(ObjectName);
			if (SetValue.toUpperCase().trim().compareTo("ON") == 0) {
				// Select the radio button
				if (!ActionObject.isSelected())
					ActionObject.click();
				return true;
			} else {
				// UnSelect the radio button
				if (ActionObject.isSelected())
					ActionObject.click();
				return true;
			}
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, ObjectName, e);
			return false;
		}
	}

	// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

	// verifies and returns true or false if the particular element is selected
	public boolean Rbtn_IsSelected(String ObjectName) {
		try {
			WebElement ActionObject = GetObject(ObjectName);
			if (ActionObject.isSelected()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, ObjectName, e);
			return false;
		}
	}

	// verify the status of checkbox
	public void Rbtn_VerifyStatus(String StepName, String ObjectName,
			String ExpectedStatus)// ON or OFF
	{
		String ActualStatus;
		try {
			WebElement ActionObject = GetObject(ObjectName);
			if (ActionObject.isSelected()) {
				ActualStatus = "ON";
			} else {
				ActualStatus = "OFF";
			}
			if (CompareText(ExpectedStatus, ActualStatus)) {
				Report.Write(StepName, ExpectedStatus, ActualStatus, "Pass",
						true);
			} else {
				Report.Write(StepName, ExpectedStatus, ActualStatus, "Fail",
						true);
			}

		} catch (Exception e) {
			Env.logger.log(Level.ERROR, ObjectName, e);

		}
	}

	// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*BUTTON-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

	// click the button
	public boolean Btn_Click(String ObjectName) {
		try {
			WebElement ActionObject = GetObject(ObjectName);
			if (ActionObject.isEnabled()) {
				if (Env.GetBrowserUsed().startsWith("IE"))
					MoveMouseTo(ActionObject);
				ActionObject.click();
				Wait(Env.ResponseTimeOut / 2);
				WaitForPageload();
				return true;
			} else {
				Env.logger.log(Level.ERROR, ObjectName + " button is disabled");
				return false;
			}
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, ObjectName
					+ " Exception is captured in Screenshot : "
					+ GetScreenshot("Exception"), e);
			return false;
		}
	}

	public boolean Btn_ClickAndProceed(String ObjectName) {
		try {
			WebElement ActionObject = GetObject(ObjectName);
			if (Env.GetBrowserUsed().startsWith("IE"))
				MoveMouseTo(ActionObject);
			ActionObject.click();
			return true;
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, ObjectName, e);
			return false;
		}
	}

	// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

	// check the button is enabled or disabled
	public boolean Btn_IsEnabled(String ObjectName) {
		try {
			WebElement ActionObject = GetObject(ObjectName);
			return ActionObject.isEnabled();
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, ObjectName, e);
			return false;
		}
	}

	// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*Drop
	// Down-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

	public boolean DrpDwn_IsEnabled(String ObjectName) {
		try {
			WebElement ActionObject = GetObject(ObjectName);
			if (ActionObject.isEnabled()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, ObjectName, e);
			return false;
		}
	}

	// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

	public String DrpDwn_GetSelectedText(String ObjectName) {
		try {
			WebElement ActionObject = GetObject(ObjectName);
			Select DropDownObject = new Select(ActionObject);
			return GetText(DropDownObject.getFirstSelectedOption());
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, ObjectName, e);
			return "OBJECT NOT FOUND";
		}
	}

	// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

	public String DrpDwn_GetText(String ObjectName) {
		try {
			WebElement ActionObject = GetObject(ObjectName);
			Select DropDownObject = new Select(ActionObject);
			StringBuffer ReturnText = new StringBuffer();
			List<WebElement> listoptions = DropDownObject.getOptions();
			if (listoptions.size() > 0) {
				for (WebElement option : listoptions) {
					ReturnText.append(TextCleanUp(option.getText()) + "|");
				}
				ReturnText.deleteCharAt(ReturnText.lastIndexOf("|"));
				return ReturnText.toString();
			} else {
				return "";
			}
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, ObjectName, e);
			return "OBJECT NOT FOUND";
		}
	}

	public String DrpDwn_GetValue(String ObjectName) {
		try {
			WebElement ActionObject = GetObject(ObjectName);
			Select DropDownObject = new Select(ActionObject);
			StringBuffer ReturnText = new StringBuffer();
			List<WebElement> listoptions = DropDownObject.getOptions();
			for (WebElement option : listoptions) {
				ReturnText.append(option.getAttribute("value") + "|");
			}
			ReturnText.deleteCharAt(ReturnText.lastIndexOf("|"));
			return ReturnText.toString();
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, ObjectName, e);
			return "OBJECT NOT FOUND";
		}
	}

	// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

	public String DrpDwn_GetText(String ObjectName, String IndexValue) {
		int IndexToSearch;
		try {
			Select SelectObject = new Select(GetObject(ObjectName));
			int MaxIndex = SelectObject.getOptions().size();
			if (IndexValue.toUpperCase().compareTo("FIRST") == 0) {
				IndexToSearch = 1;
			} else if (IndexValue.toUpperCase().compareTo("LAST") == 0) {
				IndexToSearch = MaxIndex;
			} else {
				IndexToSearch = Integer.parseInt(IndexValue);
				if (IndexToSearch > MaxIndex) {
					IndexToSearch = MaxIndex;
				}
			}
			return GetText(SelectObject.getOptions().get(IndexToSearch - 1));
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, ObjectName, e);
			return "Error";
		}
	}

	public int DrpDwn_GetCount(String ObjectName) {
		try {
			Select SelectObject = new Select(GetObject(ObjectName));
			return SelectObject.getOptions().size();
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, ObjectName, e);
			return -1;
		}
	}

	// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

	public boolean DrpDwn_SelectText(String ObjectName, String TextToSelect) {
		boolean ReturnValue = false;
		try {
			WebElement ActionObject = GetObject(ObjectName);
			Select DropDownObject = new Select(ActionObject);
			try {
				DropDownObject.selectByVisibleText(TextToSelect);
				ReturnValue = true;
			} catch (NoSuchElementException e) {
				Env.logger.log(Level.ERROR, "There is no option with text: '"
						+ TextToSelect + "'");
				ReturnValue = false;
			}
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, ObjectName, e);
			ReturnValue = false;
		}

		return ReturnValue;
	}

	// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

	public boolean DrpDwn_SelectValue(String ObjectName, String ValueToSelect) {
		boolean ReturnValue = false;
		try {
			WebElement ActionObject = GetObject(ObjectName);
			Select DropDownObject = new Select(ActionObject);
			try {
				String BeforeValue = DropDownObject.getFirstSelectedOption()
						.getAttribute("value");
				if (!(BeforeValue.compareToIgnoreCase(ValueToSelect) == 0)) {
					DropDownObject.selectByValue(ValueToSelect);
				}
				ReturnValue = true;
			} catch (NoSuchElementException e) {
				Env.logger.log(Level.ERROR, "There is no option with value: '"
						+ ValueToSelect + "'" + "for the object " + ObjectName);
				ReturnValue = false;
			}
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, ObjectName, e);
			ReturnValue = false;
		}
		return ReturnValue;
	}

	// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

	public boolean DrpDwn_SelectIndex(String ObjectName, int IndexToSelect) {
		boolean ReturnValue = false;
		try {
			WebElement ActionObject = GetObject(ObjectName);
			Select DropDownObject = new Select(ActionObject);
			try {
				DropDownObject.selectByIndex(IndexToSelect);
				ReturnValue = true;
			} catch (NoSuchElementException e) {
				Env.logger.log(Level.ERROR, "There is no option with index: '"
						+ IndexToSelect + "' for the object " + ObjectName);
				ReturnValue = false;
			}
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, ObjectName, e);
			ReturnValue = false;
		}
		return ReturnValue;
	}

	public String DrpDwn_GetValue(String ObjectName, String StringToSearch) {
		try {
			WebElement ActionObject = GetObject(ObjectName);
			Select DropDownObject = new Select(ActionObject);
			List<WebElement> listoptions = DropDownObject.getOptions();
			for (WebElement option : listoptions) {
				if (option.getText().contains(StringToSearch)) {
					return option.getAttribute("value");
				}
			}
			return "NOT FOUND";
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, ObjectName, e);
			return "OBJECT NOT FOUND";
		}
	}

	// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*CHECKBOX-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

	// set the chkbox = ON or OFF
	public boolean ChkBx_SetOption(String ObjectName, String SetValue) {
		String sXPath;
		try {
			WebElement ActionObject = GetObject(ObjectName);
			String DisabledValue = ActionObject.getAttribute("disabled");
			if (DisabledValue == null) {// If not disabled, then set the value
				if (SetValue.toUpperCase().trim().compareTo("ON") == 0) {
					// Select the check box
					if (!ActionObject.isSelected())
						ActionObject.click();

					if ((ObjectName.startsWith("/") || ObjectName
							.startsWith("("))) {
						sXPath = ObjectName;
					} else {
						sXPath = ORXPath(ObjectName);
					}

					// Check if selected
					if (!GetObject(sXPath).isSelected()) {
						// Env.logger.log(Level.WARN,"Unable to turn 'ON' this checkbox "
						// + ObjectName + " .Retrying with JavaScript...");
						RunJavaScript("document.getElementById('"
								+ ActionObject.getAttribute("id")
								+ "').checked=true;");
						if (!GetObject(sXPath).isSelected()) {// Check for
							// second time
							Env.logger.log(Level.WARN,
									"Unable to turn 'ON' this checkbox "
											+ ObjectName + " No Retrying...");
							return false;
						}
					}
					return true;
				} else {
					// UnSelect the check box
					if (ActionObject.isSelected())
						ActionObject.click();

					// Check if selected
					if (GetObject(ORXPath(ObjectName)).isSelected()) {
						Env.logger.log(Level.WARN,
								"Unable to turn 'OFF' this checkbox "
										+ ObjectName
										+ " .Retrying with JavaScript...");
						RunJavaScript("document.getElementById('"
								+ ActionObject.getAttribute("id")
								+ "').checked=false;");
						if (GetObject(ORXPath(ObjectName)).isSelected()) {// Check
							// for
							// second
							// time
							Env.logger.log(Level.WARN,
									"Unable to turn 'OFF' this checkbox "
											+ ObjectName + "No Retrying...");
							return false;
						}
					}
					return true;
				}
			} else {
				Env.logger.log(Level.ERROR, ObjectName
						+ " Object is in disabled state");
				return false;
			}
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, ObjectName, e);
			return false;
		}
	}

	public boolean ChkBx_SetOptionForAll(String ObjectXPath, String SetValue) {
		try {
			List<WebElement> ListOfObjects = browser.findElements(By
					.xpath(ObjectXPath));
			for (WebElement ActionObject : ListOfObjects) {
				try {
					if (SetValue.toUpperCase().trim().compareTo("ON") == 0) {
						// Select the Check box
						if (!ActionObject.isSelected())
							ActionObject.click();
					} else {
						// UnSelect the Check box
						if (ActionObject.isSelected())
							ActionObject.click();
					}
				} catch (Exception ignore) {
					// Exception may occur, while checking some checkboxe's
				}
			}
			return true;
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, ObjectXPath, e);
			return false;
		}
	}

	// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

	// get the status of checkbox
	public boolean ChkBx_isSelected(String ObjectName) {
		try {
			WebElement ActionObject = GetObject(ObjectName);
			if (ActionObject.isSelected()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, ObjectName, e);
			return false;
		}
	}

	// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
	// verify the status of checkbox
	public void ChkBx_VerifyStatus(String StepName, String ObjectName,
			String ExpectedStatus)// ON or OFF
	{
		String ActualStatus;
		try {
			WebElement ActionObject = GetObject(ObjectName);
			if (ActionObject.isSelected()) {
				ActualStatus = "ON";
			} else {
				ActualStatus = "OFF";
			}
			if (CompareText(ExpectedStatus, ActualStatus)) {
				Report.Write(StepName, ExpectedStatus, ActualStatus, "Pass",
						true);
			} else {
				Report.Write(StepName, ExpectedStatus, ActualStatus, "Fail",
						true);
			}

		} catch (Exception e) {
			Env.logger.log(Level.ERROR, ObjectName, e);

		}
	}

	// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*Image-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

	// click the button
	public boolean Img_Click(String ObjectName) {
		try {
			WebElement ActionObject = GetObject(ObjectName);
			if (ActionObject.isEnabled()) {
				if (Env.GetBrowserUsed().startsWith("IE")) {
					MoveMouseTo(ActionObject);
				}
				ActionObject.click();
				return true;
			} else {
				Env.logger.log(Level.ERROR, ObjectName + " image is disabled");
				return false;
			}
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, ObjectName
					+ " Exception is captured in Screenshot : "
					+ GetScreenshot("Exception"), e);
			return false;
		}
	}

	// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*DATA
	// COMPARE-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

	// compares the two strings and report the failure
	public boolean CompareText(String StepID, String Expected, String Actual) {
		if (CompareText(Expected, Actual)) {
			Report.Write(StepID, Expected, Actual, "Pass", true);
			return true;
		} else {
			Report.Write(StepID, Expected, Actual, "FAIL", true);
			return false;
		}
	}

	// compares the two strings and report the failure
	public boolean CompareText(String StepID, String Expected, String Actual,
			boolean TakeScreenshot) {
		if (CompareText(Expected, Actual)) {
			Report.Write(StepID, Expected, Actual, "Pass", TakeScreenshot);
			return true;
		} else {
			Report.Write(StepID, Expected, Actual, "FAIL", TakeScreenshot);
			return false;
		}
	}

	// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

	// compares the two strings and returns true or false
	public boolean CompareText(String Expected, String Actual) {
		if (Expected.trim().compareTo(Actual.trim()) == 0) {
			return true;
		} else {
			return false;
		}
	}

	// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

	public boolean VerifyRegExpression(String ObjectName, String sExpected,
			String sActual, String... ParamsToReplace) {

		boolean ReturnValue;
		String sExpectedPattern = sExpected;
		String ExpectedString;

		// Replace the Parameters with the give values
		for (int i = 0; i < ParamsToReplace.length; i++) {
			ParamsToReplace[i] = ParamsToReplace[i].replace("\\", "\\\\");
			sExpectedPattern = sExpectedPattern.replaceFirst("\\{" + i + "\\}",
					ParamsToReplace[i]);
		}

		// If there are still values present, then replace with .*
		sExpectedPattern = sExpectedPattern.replaceAll("\\{\\d{1}\\}", ".*");

		// compare as Regular Expression
		sExpectedPattern = ReplaceSplCharacters(sExpectedPattern);
		Pattern CheckPattern = Pattern.compile(sExpectedPattern);
		Matcher m = CheckPattern.matcher(sActual);
		if (m.find()) {
			ExpectedString = m.group();
			if (ExpectedString.compareTo(sActual) == 0) {
				Report.Write(ObjectName, sExpected, sActual, "Pass", true);
				ReturnValue = true;
			} else {
				Report.Write(ObjectName, sExpected, sActual, "Fail", true);
				ReturnValue = false;
			}
		} else {
			ExpectedString = "";
			Report.Write(ObjectName, sExpected, sActual, "Fail", true);
			ReturnValue = false;
		}

		sExpectedPattern = null;
		sExpected = null;
		sActual = null;
		ExpectedString = null;
		CheckPattern = null;
		return ReturnValue;
	}

	public boolean VerifyRegExp(String ObjectName, String... ParamsToReplace) {

		boolean ReturnValue;
		String sExpected = ORData(ObjectName);
		String sExpectedPattern = sExpected;
		String sActual = GetText(ObjectName);
		String ExpectedString;

		// Replace the Parameters with the give values
		for (int i = 0; i < ParamsToReplace.length; i++) {
			ParamsToReplace[i] = ParamsToReplace[i].replace("\\", "\\\\");
			sExpectedPattern = sExpectedPattern.replaceFirst("\\{" + i + "\\}",
					ParamsToReplace[i]);
		}

		// If there are still values present, then replace with .*
		sExpectedPattern = sExpectedPattern.replaceAll("\\{\\d{1}\\}", ".*");

		// compare as Regular Expression
		sExpectedPattern = ReplaceSplCharacters(sExpectedPattern);
		Pattern CheckPattern = Pattern.compile(sExpectedPattern);
		Matcher m = CheckPattern.matcher(sActual);
		if (m.find()) {
			ExpectedString = m.group();
			if (ExpectedString.compareTo(sActual) == 0) {
				Report.Write(ObjectName, sExpected, sActual, "Pass", true);
				ReturnValue = true;
			} else {
				Report.Write(ObjectName, sExpected, sActual, "Fail", true);
				ReturnValue = false;

			}
		} else {
			ExpectedString = "";
			Report.Write(ObjectName, sExpected, sActual, "Fail", true);
			ReturnValue = false;
		}

		sExpectedPattern = null;
		sExpected = null;
		sActual = null;
		ExpectedString = null;
		CheckPattern = null;
		return ReturnValue;

	}

	// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

	@Deprecated
	public boolean TestRegExp12(String sExpected, String sActual) {
		// This method is for debugging purpose. to test RegEXP
		String ExpectedString;

		sExpected = ReplaceSplCharacters(sExpected);
		Pattern CheckPattern = Pattern.compile(sExpected);
		Matcher m = CheckPattern.matcher(sActual);
		if (m.find()) {
			ExpectedString = m.group();
			if (ExpectedString.compareTo(sActual) == 0) {
				logger.info("Match found");
				return true;
			} else {
				logger.info("Match found, but the text comparison failed");
				return false;
			}
		} else {
			ExpectedString = "";
			logger.info("No match found");
			return false;
		}
	}

	// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*WEBTABLE-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

	// pass table xpath and get the row count
	public int WbTbl_GetRowcount(String ObjectName) {
		try {
			int ReturnValue = 0;
			WebElement ActionObject = GetObject(ObjectName);
			String TableText = null;
			int ReturnValueWithTD = 0;
			int MinRow = 0;

			ReturnValueWithTD = ActionObject.findElements(
					By.xpath("tbody/tr/td/..")).size();
			ReturnValue = ActionObject.findElements(By.xpath("tbody/tr"))
					.size();
			if (ReturnValueWithTD < ReturnValue) {
				MinRow = 1;// Since when no record is found. Then table returns
				// 11 rows.
			}

			for (; ReturnValue > MinRow; ReturnValue--) {
				TableText = GetText(
						ActionObject.findElement(By.xpath("tbody/tr["
								+ ReturnValue + "]/td[2]"))).trim();
				if (TableText.length() != 0) {
					break;
				}
			}

			if (MinRow == 1) {
				ReturnValue--;
			}
			return ReturnValue;
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, ObjectName, e);
			return -1;
		}
	}

	// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

	// pass table xpath and get the column count
	public int WbTbl_GetColumncount(String ObjectName) {
		try {
			WebElement ActionObject = GetObject(ObjectName);
			return ActionObject.findElements(By.xpath("tbody/tr[1]/td")).size();
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, ObjectName, e);
			return -1;
		}
	}

	// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

	public int WbTbl_GetPagecount() {
		String PageNum = "";
		WebElement ActionObject = null;
		String PaginationText = null;
		try {

			if (ObjectExists("General_TablePagination1_ST")) {
				ActionObject = GetObject(ORXPath("General_TablePagination1_ST"));
				PaginationText = GetText(ActionObject);
			} else if (ObjectExists("General_TablePagination2_ST")) {
				ActionObject = GetObject(ORXPath("General_TablePagination2_ST"));
				PaginationText = GetText(ActionObject);
			} else {
				return 1; // There is no pagination for few tables
			}

			int length = PaginationText.length();

			if (length == 0) {
				return 1; // There is no pagination for few tables
			}

			for (int i = 0; i < length; i++) {
				Character character = PaginationText.charAt(i);
				if (Character.isDigit(character)) {
					// Max Page number will be 2 pages, 99 pages (99*10=990
					// records)
					PageNum += character;
					if (i + 1 < length
							&& Character.isDigit(PaginationText.charAt(i + 1))) {
						PageNum += PaginationText.charAt(i + 1);
					}
					if (i + 2 < length
							&& Character.isDigit(PaginationText.charAt(i + 2))) {
						PageNum += PaginationText.charAt(i + 2);
					}
					PageNum = PageNum.trim();
					break;
				}
			}

			return Integer.parseInt(PageNum);

		} catch (Exception e) {
			Env.logger.log(Level.ERROR, "", e);
			return -1; // Any error: PaginationObject not found, Array index
			// exception, replace exception
		}

	}

	// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

	// pass header name and it returns the column index
	public int WbTbl_GetColumnIndex(String ObjectName, String sHeaderName) {
		try {
			WebElement ActionObject = GetObject(ObjectName);
			String Headername;
			int ccnt = ActionObject.findElements(By.xpath("thead/tr[1]/th"))
					.size();
			for (int i = 1; i <= ccnt; i++) {
				Headername = GetText(ActionObject.findElement(By
						.xpath("thead/tr/th[" + i + "]")));
				if (Headername.compareTo(sHeaderName) == 0) // comparing the
					// header name
					return i;
			}
			return -1; // No match Found
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, ObjectName, e);
			return -1; // Element does not exists
		}
	}

	// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

	// pass the string, it returns the row count (any page). Control will be in
	// the same page where the string is available.
	public int WbTbl_GetRowIndexPagination(String ObjectName, int ColIndex,
			String sSearchString) {
		try {
			WebElement ActionObject = null;
			List<WebElement> Cells = null;
			String sActualString = null;
			boolean bStringFound = false;
			boolean bLastPage = false;
			int CurrentRow = 1;
			int CurrentPage = 1;
			int RecordsParsed = 0;
			int PageCount = WbTbl_GetPagecount();

			do { // start the do loop here till the sSearchString is found
				ActionObject = GetObject(ObjectName);
				Cells = ActionObject.findElements(By.xpath("tbody/tr/td["
						+ ColIndex + "]"));
				for (CurrentRow = 0; CurrentRow < Cells.size(); CurrentRow++) {
					RecordsParsed++; // Increment the records parsed
					sActualString = GetText(Cells.get(CurrentRow));
					if (sActualString.length() == 0 && CurrentPage == PageCount) { // indicates
						// first
						// blank
						// row
						// in
						// the
						// last
						// page
						bLastPage = true;
						break;
					}

					if (CompareText(sSearchString, sActualString)) { // String
						// Found
						bStringFound = true;
						return CurrentRow + 1;
					}
				}

				if ((CurrentPage < PageCount && !bStringFound && !bLastPage)) {
					Lnk_Click("General_NextPage_LK");
					CurrentPage++;
				} else {
					bLastPage = true;
				}

			} while (!(bStringFound || bLastPage)); // Stop while if the string
			// is found or the last page
			// is reached

			return -1;
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, ObjectName, e);
			return -1; // Element does not exists
		}
	}

	// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

	// get the page number
	public int WbTbl_GetPageNumber(String ObjectName, String sColName,
			String sSearchString) {
		try {
			WebElement ActionObject = GetObject(ObjectName);
			String sActualString = null;
			boolean bStringFound = false;
			boolean bLastPage = false;
			int CurrentRow = 1;
			int CurrentPage = 1;
			int RecordsParsed = 0;
			int RowCount = WbTbl_GetRowcount(ObjectName);
			int ColIndex = WbTbl_GetColumnIndex(ObjectName, sColName);
			int PageCount = WbTbl_GetPagecount();

			do { // start the do loop here till the sSearchString is found
				for (CurrentRow = 1; CurrentRow <= RowCount; CurrentRow++) { // increment
					// the
					// row
					// index
					// and
					// try
					// to
					// match
					// the
					// sActualString
					// with
					// sSearchString

					RecordsParsed++; // Increment the records parsed
					sActualString = GetText(ActionObject.findElement(By
							.xpath("tbody/tr[" + CurrentRow + "]/td["
									+ ColIndex + "]/a")));

					if (CompareText(sSearchString, sActualString)) { // String
						// Found
						bStringFound = true;
						break;
					}

					if (sActualString.length() == 0 && CurrentPage == PageCount) { // indicates
						// first
						// blank
						// row
						// in
						// the
						// last
						// page
						bLastPage = true;
						break;
					}
				}

				if ((CurrentPage < PageCount && !bStringFound && !bLastPage)) {
					Lnk_Click(ORXPath("General_TableNextPage_ST"));
					CurrentPage++;
				} else {
					bLastPage = true;
				}

			} while (!(bStringFound || bLastPage)); // Stop while if the string
			// is found or the last page
			// is reached

			return CurrentPage;
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, ObjectName, e);
			return -1; // Element does not exists
		}
	}

	// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

	// get the row index without any pagination
	public int WbTbl_GetRowIndex(String ObjectName, int ColIndex,
			String sSearchString) {
		try {
			WebElement ActionObject = GetObject(ObjectName);
			String actualStr = null;
			List<WebElement> Cells = ActionObject.findElements(By
					.xpath("tbody/tr/td[" + ColIndex + "]"));
			for (int CurrentRow = 0; CurrentRow < Cells.size(); CurrentRow++) {
				actualStr = GetText(Cells.get(CurrentRow));
				if (CompareText(sSearchString, actualStr)) {
					return CurrentRow + 1;
				}
			}
			return -1;
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, ObjectName, e);
			return -1; // Element does not exists
		}
	}

	// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
	// get the xpath value of any object in any page
	public String WbTbl_GetXpathVal(String ObjectName, int iColIndex,
			String SSearchString) {
		try {
			int rcnt = WbTbl_GetRowIndexPagination(ObjectName, iColIndex,
					SSearchString);
			int colval = iColIndex;
			if (rcnt == -1 || colval == -1) {
				return "STRING NOT FOUND IN TABLE";
			}
			String xpathval = ObjectName;
			xpathval = ORXPath(ObjectName);
			return xpathval + "/tbody/tr[" + rcnt + "]/td[" + colval + "]/a";
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, ObjectName, e);
			return "Error Occured"; // Element does not exists
		}

	}

	// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

	public String WbTbl_GetText(String TableId, int rowIndex, int colIndex) {

		try {
			String xpath = ORXPath(TableId) + "/tbody/tr[" + rowIndex + "]/td["
					+ colIndex + "]";
			WebElement ActionObject = GetObject(xpath);
			return GetText(ActionObject).trim();
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, e);
			return "Error Occured"; // Element does not exists
		}

	}

	public String WbTbl_GetXPath(String TableId) {
		String xpath = null;
		try {
			int rindex = 1;
			if (rindex != -1) {
				xpath = "(" + ORXPath(TableId) + "//input[@type='radio'])["
						+ rindex + "]";
			}
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, e); // Element does not exists
		}
		return xpath;
	}

	// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
	// locate the element in any page and click the link
	public void WbTbl_Lnk_Click(String ObjectName, int iColIndex,
			String SSearchString) {
		try {
			WbTbl_SelectRbtn(ObjectName, iColIndex, SSearchString);
			String sXpath = WbTbl_GetXpathVal(ObjectName, iColIndex,
					SSearchString);
			Lnk_Click(sXpath);
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, ObjectName, e); // Element does not
			// exists
		}

	}

	// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
	// locate the element in any page and select the radio button
	public boolean WbTbl_SelectRbtn(String TableId, int iColIndex,
			String SSearchString) {
		try {
			int rindex = WbTbl_GetRowIndexPagination(TableId, iColIndex,
					SSearchString);
			if (rindex != -1) {
				String xpath = "(" + ORXPath(TableId)
				+ "//input[@type='radio'])[" + rindex + "]";
				Rbtn_SetOption(xpath, "ON");
				return true;
			} else {
				Env.logger.log(Level.ERROR, "Search String '" + SSearchString
						+ "' is not found in the table");
				return false;
			}
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, TableId, e); // Element does not exists
			return false;
		}
	}

	// locate the element in any page and select the radio button
	public boolean WbTbl_SelectRbtn(String TableId, int iRowIndex) {
		try {
			int rindex = iRowIndex;
			String xpath = "(" + ORXPath(TableId) + "//input[@type='radio'])["
					+ rindex + "]";
			Rbtn_SetOption(xpath, "ON");
			return true;
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, TableId, e); // Element does not exists
			return false;
		}
	}

	// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
	// locate the element in any page and select the radio button
	public void WbTbl_VerifyTableColumn(String TableId, String sStepName,
			int iColIndex, int MaxRow, String ExpectedList, boolean IgnoreHypen) {
		int ReportCount = 0;
		try {
			WebElement ActionObject = GetObject(TableId);
			int InitialRowIndex = 1;

			// If the Table does not have Thead, then start the index from 2.
			int ColumnCount = WbTbl_GetColumncount(TableId);
			if (ColumnCount == 0) {
				InitialRowIndex = 2;
			}

			int RowCount = WbTbl_GetRowcount(TableId);
			if (MaxRow > RowCount) {
				MaxRow = RowCount;
			}

			String sActualValue = "";
			sStepName = "Col" + sStepName.replaceAll(" ", "") + "Row";
			for (int CurrentRowIndex = InitialRowIndex; CurrentRowIndex <= MaxRow; CurrentRowIndex++) {
				sActualValue = GetText(ActionObject.findElement(By
						.xpath("tbody/tr[" + CurrentRowIndex + "]/td["
								+ iColIndex + "]")));
				if (sActualValue.isEmpty()) {
					continue; // Ignore Blanks
				}
				if (IgnoreHypen && sActualValue.startsWith("-")
						&& sActualValue.endsWith("-")) {
					continue;
				}
				CompareWithList(sStepName + CurrentRowIndex + "_DT",
						ExpectedList, sActualValue);
				ReportCount++;
			}
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, TableId, e); // Element does not exists
		}

		if (ReportCount == 0) {
			Report.Write(sStepName, ExpectedList, "", "Fail", true);
		}

	}

	public void WbTbl_SortColumn(String TableId, int iColIndex,
			String SortingType) {
		// Example: WbTbl_SortColumn("Home_MyPatients_TE,2,"DESC")
		SortingType = SortingType.toUpperCase();
		WebElement ActionObject = null;
		WebElement SortingImage = null;
		String CurrentSort = null;
		try {

			// final WebElement SortingImage =
			// ActionObject.findElement(By.xpath("//thead/tr/th["+iColIndex+"]//img[@class='sortIcnPad srtDisplayIcn' or @class='sortIcnPad srtDisplayIcn']"));//srtDisplayIcn
			// final WebElement SortingImage =
			// ActionObject.findElement(By.xpath("//thead/tr/th["+iColIndex+"]//img[starts-with(@class,'sortIcn')]"));//srtDisplayIcn
			for (int i = 0; i < 2; i++) {
				ActionObject = GetObject(TableId);

				// thead/tr/th["+iColIndex+"]//img[@class='sortIcn
				// srtDisplayIcn' or @class='srtDisplayIcn' or
				// @class='sortIcnPad srtDisplayIcn']
				SortingImage = ActionObject
						.findElement(By
								.xpath("//thead/tr/th["
										+ iColIndex
										+ "]//img[contains(@class,'sort') or contains(@class,'srt')]"));// srtDisplayIcn
				CurrentSort = SortingImage.getAttribute("src");
				// Ascending
				if (CompareText(SortingType, "ASC")
						&& !(CurrentSort.endsWith("arrow_black_asc.png"))) {
					SortingImage.click();
					WaitForPageload();
				}

				// Descending
				if (CompareText(SortingType, "DESC")
						&& !(CurrentSort.endsWith("arrow_black.png"))) {
					SortingImage.click();
					WaitForPageload();
				}
				ActionObject = null;
				SortingImage = null;
				CurrentSort = null;
			}

		} catch (Exception e) {
			Env.logger.log(Level.ERROR, TableId, e);
		}
	}

	public List<String> WbTbl_GetText(String TableId, int colIndex) {
		try {

			WebElement ActionObject = GetObject(TableId);
			String CellText = "";
			List<String> TableText = new ArrayList<String>();
			List<WebElement> Cells = ActionObject.findElements(By
					.xpath("tbody/tr[text()!='']/td[" + colIndex + "]"));
			for (int CurrentRow = 0; CurrentRow < Cells.size(); CurrentRow++) {
				CellText = GetText(Cells.get(CurrentRow));
				TableText.add(CellText);
			}
			return TableText;
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, e);
			return null;
		}

	}

	public int WbTbl_GetRecordCount(String TableObject, String PaginationObject) {
		// Verify the Record info text
		int totalpages = WbTbl_GetPagecount();
		Edit_SetValue(ORXPath(PaginationObject) + "//input", totalpages);
		WaitForPageload();
		int RecordsInLastPage = WbTbl_GetRowcount(TableObject);
		int totalrecords = (totalpages - 1) * 10 + RecordsInLastPage;
		Edit_SetValue(ORXPath(PaginationObject) + "//input", 1);
		return totalrecords;
	}

	public void WbTbl_VerifyHighlightedRow(String ObjectName, String StepID,
			int RowToCheck) {
		try {
			String AttributeValue = "selectRow";
			if (GetAttributeValue(
					ORXPath(ObjectName) + "/tbody/tr[" + RowToCheck + "]",
					"class").contains(AttributeValue)) {
				Report.Update(StepID, "Selected Row should be highlighted",
						"Selected Row is highlighted", "Pass");
			} else {
				Report.Update(StepID, "Selected Row should be highlighted",
						"Selected Row is not highlighted", "Fail");
			}

		} catch (Exception e) {
			Env.logger.log(Level.ERROR, e);
		}
	}

	public void WbTbl_ImgIconExists(String TableObject, String ColumnName,
			int ColumnIndex, String HelporSort) {
		String AttributeValue = "";
		HelporSort = HelporSort.toUpperCase().trim();
		try {
			if (CompareText("HELP", HelporSort)) {
				AttributeValue = "helpIcon";
			} else {
				AttributeValue = GetAttributeValue(ORXPath(TableObject)
						+ "//thead//th[" + ColumnIndex
						+ "]//following-sibling::img", "class");
				if (AttributeValue.startsWith("srt")
						|| AttributeValue.startsWith("sort")) {
					AttributeValue = "s";
				}
			}
			String Xpath = ORXPath(TableObject) + "//thead//th[" + ColumnIndex
					+ "]//following-sibling::img[starts-with(@class,'"
					+ AttributeValue + "')]";
			if (ObjectExists(Xpath)) {
				Report.Update(ColumnName + "_" + HelporSort, HelporSort
						+ " Icon should be present for " + ColumnName,
						HelporSort + " icon is present for " + ColumnName,
						"Pass");
			} else {
				Report.Update(ColumnName + "_" + HelporSort, HelporSort
						+ " Icon should be present for " + ColumnName,
						HelporSort + " icon is not present for " + ColumnName,
						"Fail");
			}
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, e);
		}
	}

	public boolean CompareWithList(String ObjectName,
			String ExpectedListOfValues, String ActualValue) {
		final List<String> ExpectedList = Arrays.asList(ExpectedListOfValues
				.split("\\|"));
		for (String ExpectedValue : ExpectedList) {
			ExpectedValue = ExpectedValue.trim();
			if (CheckTextContained(ExpectedValue, ActualValue)
					&& !ExpectedValue.isEmpty()) {
				Report.Write(ObjectName, ExpectedValue, ActualValue, "Pass",
						true);
				return true;
			}
		}
		Report.Write(ObjectName, ExpectedListOfValues, ActualValue, "Fail",
				true);
		return false;
	}

	public boolean CompareWithExactList(String ObjectName,
			String ExpectedListOfValues, String ActualValue) {
		final List<String> ExpectedList = Arrays.asList(ExpectedListOfValues
				.split("\\|"));
		for (String ExpectedValue : ExpectedList) {
			ExpectedValue = ExpectedValue.trim();
			if (CompareText(ExpectedValue, ActualValue)
					&& !ExpectedValue.isEmpty()) {
				Report.Write(ObjectName, ExpectedValue, ActualValue, "Pass",
						true);
				return true;
			}
		}
		Report.Write(ObjectName, ExpectedListOfValues, ActualValue, "Fail",
				true);
		return false;
	}

	public void CompareDynamicDropDownValues(String ObjectName,
			String ExpectedString, boolean ToBeSorted) {
		try {
			String ScreenshotName = GetScreenshot(ObjectName);
			WebElement ActionObject = GetObject(ObjectName);
			if (ActionObject != null) {
				List<String> ExpectedList = Arrays.asList(ExpectedString
						.split("\\|"));
				String ActualValue = DrpDwn_GetText(ObjectName);
				List<String> ActualList = Arrays.asList(ActualValue
						.split("\\|"));

				// first trim the spaces in the ActualList
				for (int i = 0; i < ActualList.size(); i++) {
					ActualList.set(i, (ActualList.get(i)).trim());
				}

				if (ToBeSorted) {
					Collections.sort(ExpectedList);
					Collections.sort(ActualList);
				}
				CompareList(ObjectName, ExpectedList, ActualList,
						ScreenshotName);
			} else { // Object does not exists
				Env.logger.log(Level.ERROR, ObjectName + " is not present");
				Report.Write(ObjectName, ObjectName + " should be present",
						ObjectName + " is not present", "Fail", ScreenshotName);
			}
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, ObjectName, e); // Element does not
			// exists
		}
	}

	public void CompareDropDownValues(String ObjectName) {
		final String temp = Env.StaticScreenshotName;
		Env.StaticScreenshotName = null;
		try {
			WebElement ActionObject = GetObject(ObjectName);
			if (ActionObject != null) {
				String ExpectedValue = "";
				String ActualValue = "";
				Select ActionDropDown = new Select(ActionObject);
				String InitialValue = GetText(ActionDropDown
						.getFirstSelectedOption());
				List<WebElement> ActualList = ActionDropDown.getOptions();
				List<String> ExpectedList = Arrays.asList(ORData(ObjectName)
						.split("\\|"));
				int min = ExpectedList.size();
				if (ActualList.size() < ExpectedList.size()) {
					min = ActualList.size();
				}
				for (int i = 0; i < min; i++) {
					ExpectedValue = ExpectedList.get(i);
					ActualValue = TextCleanUp(GetText(ActualList.get(i)));
					if (CompareText(ExpectedValue, ActualValue)) {
						try {
							ActionDropDown.selectByVisibleText(ExpectedValue);
						} catch (Exception e) {
							Env.logger.log(Level.ERROR,
									"Unable to select the value - "
											+ ExpectedValue);
						}
						Report.Write(ObjectName + i, ExpectedValue,
								ActualValue, "Pass", true);
					} else {
						Report.Write(ObjectName + i, ExpectedValue,
								ActualValue, "Fail", true);
					}
				} // For Ends here

				for (int i = min; i < ActualList.size(); i++) { // When Actual
					// list is more
					// than the
					// Expected
					ActualValue = GetText(ActualList.get(i));
					try {
						ActionDropDown.selectByVisibleText(ActualValue);
					} catch (Exception e) {
						Env.logger.log(Level.ERROR,
								"Unable to select the value - " + ActualValue);
					}
					Report.Write(ObjectName + i, "", ActualValue, "Fail", true);
				}

				for (int i = min; i < ExpectedList.size(); i++) { // When
					// Expected
					// list is
					// more than
					// the
					// Actual
					ExpectedValue = ExpectedList.get(i);
					Report.Write(ObjectName + i, ExpectedValue, "", "Fail",
							true);
				}

				// Revert to the initial value
				ActionDropDown.selectByVisibleText(InitialValue);
			} else { // Object does not exists
				Env.logger.log(Level.ERROR, ObjectName + " is not present");
				Report.Write(ObjectName, ObjectName + " should be present",
						ObjectName + " is not present", "Fail", true);
			}
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, ObjectName, e); // Element does not
			// exists
		} finally {
			Env.StaticScreenshotName = temp;
		}
	}

	public boolean VerifyText(String ObjectName) {
		String sExpected = ORData(ObjectName);
		String sActual = GetText(ObjectName);
		return CompareText(ObjectName, sExpected, sActual);
	}

	public boolean VerifyTextContained(String ObjectName) {
		String sExpected = ORData(ObjectName);
		String sActual = GetText(ObjectName);
		return VerifyTextContained(ObjectName, sExpected, sActual);
	}

	public boolean VerifyTextContained(String ObjectName, String sExpected,
			String sActual) {

		if (CheckTextContained(sExpected, sActual)) {
			Report.Write(ObjectName, sExpected, sActual, "Pass", true);
			return true;
		} else {
			Report.Write(ObjectName, sExpected, sActual, "FAIL", true);
			return false;
		}

	}

	public boolean CheckTextContained(String sExpected, String sActual) {

		String CharactersToIgnore = "0123456789 |[]().?!;:‘’'–,=+%-（）：_";
		char DoubleQuotes = '"';
		CharactersToIgnore = CharactersToIgnore + DoubleQuotes;
		boolean isPass = false;
		sExpected = sExpected.trim();
		sActual = sActual.trim();
		String BiggerText = " " + sActual + " ";
		String SmallerText = sExpected;

		if (sActual.isEmpty() && !sExpected.isEmpty()) {
			isPass = false;
		} else if (sExpected.isEmpty() && !sActual.isEmpty()) {
			isPass = false;
		} else if (sActual.length() >= sExpected.length()) {
			isPass = BiggerText.contains(SmallerText);
		} else if (sExpected.length() > sActual.length()) {
			BiggerText = " " + sExpected + " ";
			SmallerText = sActual;
			isPass = BiggerText.contains(SmallerText);
		}

		if (isPass) {
			int PrevCharIndex = BiggerText.indexOf(SmallerText) - 1;
			String PrevChar = BiggerText.substring(PrevCharIndex,
					PrevCharIndex + 1);

			int NextCharIndex = BiggerText.indexOf(SmallerText)
					+ SmallerText.length();
			String NextChar = BiggerText.substring(NextCharIndex,
					NextCharIndex + 1);
			if (!CharactersToIgnore.contains(NextChar)) {
				isPass = false;
			}

			if (!CharactersToIgnore.contains(PrevChar)) {
				isPass = false;
			}
		}
		return isPass;
	}

	public boolean VerifyBulkText(String ObjectName, String FileName) {

		BufferedWriter writer = null;
		BufferedReader ExpectedBuffer = null;
		BufferedReader ActualBuffer = null;
		String Expected = "";
		String Actual = "";
		int Lineno = -1;
		boolean isPass = true;
		boolean EOFExpected = false;
		boolean EOFActual = false;
		char Expectedchars[];
		char Actualchars[];

		try {
			Charset charset = Charset.forName("UTF-8");

			// Expected File
			File sourceFile = new File(Env.DataPath + "/BulkText/" + FileName
					+ "-" + Env.LanguageUsed + ".txt");
			File ExpectedFile = new File(GetUniqueName(FileName + "Expected")
					+ ".txt");
			FileUtils.copyFile(sourceFile, ExpectedFile);
			ExpectedBuffer = Files.newReader(ExpectedFile, charset);
			// if (!Env.GetBrowserUsed().startsWith("IE")){
			// ExpectedBuffer.skip(1); //Ignore the opening line sequence in
			// UTF-8 file
			// }

			// Actual Text
			String ActualText = GetObject(ObjectName).getText(); // This should
			// be
			// getText
			// and not
			// GetText
			File ActualFile = new File(GetUniqueName(FileName + "Actual")
					+ ".txt");
			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(ActualFile), charset));
			writer.write(ActualText);
			writer.flush();
			writer = null;
			ActualBuffer = Files.newReader(ActualFile, charset);

			// Result File
			File ResultFile = new File(GetUniqueName(FileName + "Result")
					+ ".txt");
			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(ResultFile), charset));

			do {
				Lineno++;
				if (Expected.trim().compareTo(Actual.trim()) == 0) {

				} else {
					isPass = false;
					Expectedchars = Expected.toCharArray();
					Actualchars = Actual.toCharArray();
					for (int i = 0; i <= Actualchars.length - 1; i++) {
						if (i != Expectedchars.length) {
							if (Expectedchars[i] != Actualchars[i]) {
								writer.write("Mismatch - Line:" + Lineno
										+ " Char:" + (i + 1));
								writer.newLine();
								writer.write("Expected: " + Expected);
								writer.newLine();
								writer.write("Actual  : " + Actual);
								writer.newLine();
								writer.newLine();
								break;
							}
						} else {
							writer.write("Mismatch - Line:" + Lineno + " Char:"
									+ (i + 1));
							writer.newLine();
							writer.write("Expected: " + Expected);
							writer.newLine();
							writer.write("Actual  : " + Actual);
							writer.newLine();
							writer.newLine();
							break;
						}
						if ((i == Actualchars.length - 1)
								&& (i != Expectedchars.length - 1)) { // Check
							// for
							// Expectedchars
							// greater
							// than
							// the
							// Actualchars
							writer.write("Mismatch - Line:" + Lineno + " Char:"
									+ (i + 2));
							writer.newLine();
							writer.write("Expected: " + Expected);
							writer.newLine();
							writer.write("Actual  : " + Actual);
							writer.newLine();
							writer.newLine();
							break;
						}

					}
				}

				// Read the next set of lines
				if ((Expected = ExpectedBuffer.readLine()) == null) {
					EOFExpected = true;
				}
				if ((Actual = ActualBuffer.readLine()) == null) {
					EOFActual = true;
				}

			} while (!(EOFExpected || EOFActual)); // execute do-while till EOF
			// is reached atleast in one
			// file

			// Expected file is still having some lines
			if (!EOFExpected && EOFActual) {
				isPass = false;
				do {
					Lineno++;
					writer.write("Mismatch - Line:" + Lineno + " Char:1");
					writer.newLine();
					writer.write("Expected: " + Expected);
					writer.newLine();
					writer.write("Actual  : ");
					writer.newLine();
					writer.newLine();
					// writer.write("Following line is not displayed in the Application - Line:"+Lineno+
					// " Expected:" + Expected);
					// writer.newLine();
				} while ((Expected = ExpectedBuffer.readLine()) != null);
			}

			// Actual file is still some lines
			if (EOFExpected && !EOFActual) {
				isPass = false;
				do {
					Lineno++;
					writer.write("Mismatch - Line:" + Lineno + " Char:1");
					writer.newLine();
					writer.write("Expected: ");
					writer.newLine();
					writer.write("Actual  : " + Actual);
					writer.newLine();
					writer.newLine();
					// writer.write("Following line is not expected in the Application - Line:"+Lineno+
					// " Actual:" + Actual);
					// writer.newLine();
				} while ((Actual = ActualBuffer.readLine()) != null);
			}

			if (isPass) {
				writer.write("No mismatch is found");
				Report.Write(ObjectName, "No mismatch should be found",
						"No mismatch is found", "Pass", ActualFile.getName());
			} else {
				Report.Write(ObjectName, ExpectedFile.getName(),
						ActualFile.getName(), "Fail", ResultFile.getName());
			}

		} catch (Exception e) {
			Env.logger.log(Level.ERROR, ObjectName, e);
			Report.Write(
					ObjectName,
					"Bulk Text should be verified",
					"VerifyBulkText Stopped. Following error occured: "
							+ e.getMessage(), "Fail", true);
		} finally {
			if (writer != null)
				try {
					writer.close();
				} catch (IOException ignore) {
				}
		}

		return isPass;
	}

	public String ReplaceSplCharacters(String StringToClean) {
		if (StringToClean.contains("("))
			StringToClean = StringToClean.replace("(", "\\(");
		if (StringToClean.contains(")"))
			StringToClean = StringToClean.replace(")", "\\)");
		if (StringToClean.contains("?"))
			StringToClean = StringToClean.replace("?", "\\?");
		// if (StringToClean.contains("*")) //COMMENTING THESE TWO LINES BECAUSE
		// IT CAUSES ISSUES IN VerifyRegExp Method
		// StringToClean = StringToClean.replace("*", "\\*");
		return StringToClean;
	}

	public boolean VerifyToolTip(String ObjectName) {
		boolean ReturnValue = false;
		String sExpected = ORData(ObjectName);
		String sActual = "";
		try {
			sActual = GetToolTip(ObjectName);
			ReturnValue = true;
		} catch (Exception e) {
			sActual = "OBJECT NOT FOUND";
			Env.logger.log(Level.ERROR, ObjectName, e); // Element does not
			// exists
		}
		if (CompareText(sExpected, sActual)) {
			Report.Write(ObjectName, sExpected, sActual, "Pass", true);
		} else {
			Report.Write(ObjectName, sExpected, sActual, "Fail", true);
		}

		sExpected = null;
		sActual = null;
		return ReturnValue;
	}

	public String GetToolTip(String ObjectName) {
		String sToolTip = "";
		try {
			WebElement ActionObject = GetObject(ObjectName);
			sToolTip = ActionObject.getAttribute("title");
			if (sToolTip.isEmpty()) {
				sToolTip = ActionObject.getAttribute("alt");
				if (sToolTip == null || sToolTip.isEmpty()) {
					sToolTip = "No ToolTip Found";
				}
			}
		} catch (Exception e) {
			sToolTip = "OBJECT NOT FOUND";
		}
		return TextCleanUp(sToolTip);
	}

	public boolean VerifyTableHeader(String ObjectName, String ExpectedText) {

		String[] Expected = ExpectedText.split("\\|");
		WebElement ActionObject = null;
		String ActualValue = null;
		String ExpectedValue = null;
		boolean IsPass = true;
		String ChildXPath = "thead/tr/th";
		try {
			String ParentObject = ORXPath(ObjectName) + "/..";
			String ObjectAttribute = GetAttributeValue(ParentObject, "class");
			if (ObjectAttribute.contains("scrollBody")) {
				ActionObject = GetObject(ORXPath(ObjectName) + "/../..//table"); // For
				// changes
				// in
				// CR
				// 244
				// we
				// moving
				// two
				// steps
				// in
				// xpath
				// to
				// get
				// header
				// objects.
			} else {
				ActionObject = GetObject(ORXPath(ObjectName));
			}

			String ScreenShotName = UtilClass.GetScreenshot(ObjectName);
			int CurrentColumn;
			int ColumnCount = ActionObject.findElements(By.xpath(ChildXPath))
					.size();
			if (ColumnCount == 0) {
				ChildXPath = "tbody/tr/th";
				ColumnCount = ActionObject.findElements(By.xpath(ChildXPath))
						.size();
			}

			int min = Expected.length;
			if (ColumnCount < min) {
				min = ColumnCount;
			}

			for (CurrentColumn = 0; CurrentColumn < min; CurrentColumn++) {
				ActualValue = TextCleanUp(GetText(
						ActionObject.findElement(By.xpath(ChildXPath + "["
								+ (CurrentColumn + 1) + "]"))).trim());
				ExpectedValue = Expected[CurrentColumn].trim();
				if (CurrentColumn == 0 && ActualValue.isEmpty()
						&& ExpectedValue.isEmpty()) {
					continue; // To Ignore First column both blank
				}
				if (CompareText(ExpectedValue, ActualValue)) {
					Report.Write(ObjectName, ExpectedValue, ActualValue,
							"Pass", ScreenShotName);
				} else {
					IsPass = false;
					Report.Write(ObjectName, ExpectedValue, ActualValue,
							"Fail", ScreenShotName);
				}
			}

			for (CurrentColumn = min; CurrentColumn < ColumnCount; CurrentColumn++) {
				IsPass = false;
				ActualValue = GetText(ActionObject.findElement(By
						.xpath("thead/tr/th[" + (CurrentColumn + 1) + "]")));
				Report.Write(ObjectName, "", ActualValue, "Fail",
						ScreenShotName);
			}

			for (CurrentColumn = min; CurrentColumn < Expected.length; CurrentColumn++) {
				IsPass = false;
				Report.Write(ObjectName, Expected[CurrentColumn], "", "Fail",
						ScreenShotName);
			}

			ScreenShotName = null;
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, ObjectName, e);
			IsPass = false;
			Report.Write(ObjectName, ExpectedText, "OBJECT NOT FOUND", "Fail",
					true);
		}
		return IsPass;
	}

	public boolean VerifyTableHeader(String ObjectName) {
		return VerifyTableHeader(ObjectName, ORData(ObjectName));
	}

	public boolean CompareList(String ObjectName, List<String> ExpectedList,
			List<String> ActualList, String ScreenshotName) {
		String ExpectedValue = "";
		String ActualValue = "";
		boolean ReturnValue = true;
		try {
			int min = ExpectedList.size();
			if (ActualList.size() < ExpectedList.size()) {
				min = ActualList.size();
			}

			for (int i = 0; i < min; i++) {
				ExpectedValue = ExpectedList.get(i).trim();
				ActualValue = ActualList.get(i).trim();
				if (CompareText(ExpectedValue, ActualValue)) {
					Report.Write(ObjectName + i, ExpectedValue, ActualValue,
							"Pass", ScreenshotName);
				} else {
					Report.Write(ObjectName + i, ExpectedValue, ActualValue,
							"Fail", ScreenshotName);
					ReturnValue = false;
				}
			}// For Ends here

			for (int i = min; i < ActualList.size(); i++) { // When Actual list
				// is more than the
				// Expected
				ActualValue = ActualList.get(i);
				Report.Write(ObjectName + i, "", ActualValue, "Fail",
						ScreenshotName);
				ReturnValue = false;
			}

			for (int i = min; i < ExpectedList.size(); i++) { // When Expected
				// list is more
				// than the
				// Actual
				ExpectedValue = ExpectedList.get(i);
				Report.Write(ObjectName + i, ExpectedValue, "", "Fail",
						ScreenshotName);
				ReturnValue = false;
			}

		} catch (Exception e) {
			Env.logger.log(Level.ERROR, ObjectName, e);
			ReturnValue = false;
		}

		return ReturnValue;
	}

	public WebElement GetVisibleElement(String Xpath) { // //div[@class='toolTip']
		WebElement ReturnValue = null;
		try {
			List<WebElement> ElementCollection = browser.findElements(By
					.xpath(Xpath));
			int TotalElementCount = ElementCollection.size() - 1;
			WebElement ElementItem = null;
			for (int i = TotalElementCount; i >= 0; i--) { // Usually Visible
				// element is
				// displayed at the
				// last. Going in
				// the reverse
				// order.
				ElementItem = ElementCollection.get(i);
				if (ElementItem.isDisplayed()) {
					ReturnValue = ElementItem;
					break;
				}
			}
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, "", e);
		}
		return ReturnValue;
	}

	public List<WebElement> GetAllVisibleElements(String ObjectName) {
		String XPath = null;
		List<WebElement> ElementCollection = null;
		List<WebElement> WantsToRemove = new ArrayList<WebElement>();
		XPath = ORXPath(ObjectName);

		try {
			ElementCollection = browser.findElements(By.xpath(XPath));
			for (WebElement ElementItem : ElementCollection) {
				if (!ElementItem.isDisplayed()) {
					WantsToRemove.add(ElementItem);
				}
			}
			ElementCollection.removeAll(WantsToRemove);
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, "", e);
		}
		return ElementCollection;
	}

	public List<WebElement> GetAllElements(String ObjectName) {
		String XPath = null;
		List<WebElement> ElementCollection = null;
		XPath = ORXPath(ObjectName);

		try {
			ElementCollection = browser.findElements(By.xpath(XPath));
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, "", e);
			return null;
		}
		return ElementCollection;
	}

	public void HideCursor() {
		try {
			browser.findElement(By.xpath(ORXPath("General_HideCursor_ST")))
			.click();
			// GetObject(ORXPath("General_HideCursor_ST")).click();
		} catch (Exception e) {
			if (e.getCause().toString().contains("exception.GridException")) {
				Env.logger.log(Level.ERROR, e.getCause());
				if (Env.BrowserObj != null) {
					Env.BrowserObj = null; // Make the BrowserObj as null, so to
					// start a new browser on next run.
					Wait(60);
				}
			} else {
				try {
					GetVisibleElement(ORXPath("General_HideCursorEditPanel_ST"))
					.click();
				} catch (Exception ignore) {
				}
			}
		}
	}

	public String GetAttributeValue(String ObjectName, String AttributeName) {
		try {
			return GetObject(ObjectName).getAttribute(AttributeName);
		} catch (Exception e) {
			return "Error";
		}

	}

	public void Edit_SetValueByJavaScript(String ObjectName, String sSetValue) {

		WebElement MyObject = GetObject(ObjectName);
		String MyXpath = ORXPath(ObjectName);

		String SearchByName = "@name='";
		String SearchById = "@id='";
		String Script1 = "";
		String Script2 = "";
		String ClassName = MyObject.getAttribute("class") + " autocolor";
		int Start = 0;
		int End = MyXpath.lastIndexOf("'");

		if (MyXpath.contains(SearchByName)) {
			Start = MyXpath.lastIndexOf(SearchByName) + SearchByName.length();
			MyXpath = MyXpath.substring(Start, End);
			Script1 = "document.getElementsByName('" + MyXpath
					+ "')[0].value='" + sSetValue + "';";
			Script2 = "document.getElementsByName('" + MyXpath
					+ "')[0].setAttribute('class','" + ClassName + "');";
		} else if (MyXpath.contains(SearchById)) {
			Start = MyXpath.lastIndexOf(SearchById) + SearchById.length();
			MyXpath = MyXpath.substring(Start, End);
			Script1 = "document.getElementById('" + MyXpath + "').value='"
					+ sSetValue + "';";
			Script2 = "document.getElementById('" + MyXpath
					+ "').setAttribute('class','" + ClassName + "');";
		} else {
			Env.logger
			.log(Level.ERROR, "Cannot run Edit_SetValueByJavaScript.");
		}

		try {
			MyObject.click();
		} catch (Exception ignore) {
		}
		;
		((JavascriptExecutor) browser).executeScript(Script1);
		((JavascriptExecutor) browser).executeScript(Script2);
		HideCursor();

	}

	public int RandomValue(int min, int max) {
		max = max + 1;
		if (max > min) {
			Random r = new Random();
			return (r.nextInt(max - min) + min);
		} else {
			return min;
		}
	}

	public String GetAlertText() {
		try {
			Alert alert = browser.switchTo().alert();
			return alert.getText();
		} catch (Exception e) {
			return "Alert is not present";
		}
	}

	public void MoveMouseTo(WebElement HoverOnElement) {
		try {
			UserAction.moveToElement(HoverOnElement).build().perform();
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, "MoveMouseTo method failed", e);
		}
	}

	public void MoveMouseTo(String ObjectName) {
		WebElement ActionObject = GetObject(ObjectName);
		MoveMouseTo(ActionObject);
	}

	public boolean ObjectExists(String ObjectName) {
		try {
			String sXPath = ORXPath(ObjectName);
			if (sXPath.startsWith("XPath is not found")) {
				return true; // so exception will be thrown, when an action is
				// performed on that object.
			}
			browser.findElement(By.xpath(sXPath));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static byte[] TripleIn(char[] message) {
		try {
			final MessageDigest md = MessageDigest.getInstance("md5");
			final byte[] digestOfPassword = md.digest("HG58YZ3CR9"
					.getBytes("utf-8"));
			final byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
			for (int j = 0, k = 16; j < 8;) {
				keyBytes[k++] = keyBytes[j++];
			}

			final SecretKey key = new SecretKeySpec(keyBytes, "DESede");
			final IvParameterSpec iv = new IvParameterSpec(new byte[8]);
			final Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, key, iv);

			final byte[] plainTextBytes = (String.valueOf(message))
					.getBytes("utf-8");
			final byte[] cipherText = cipher.doFinal(plainTextBytes);
			return cipherText;
		} catch (Exception e) {
			return null;
		}
	}

	public static String TripleOut(byte[] message) throws Exception {

		try {
			if (message != null) {
				final MessageDigest md = MessageDigest.getInstance("md5");
				final byte[] digestOfPassword = md.digest("HG58YZ3CR9"
						.getBytes("utf-8"));
				final byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
				for (int j = 0, k = 16; j < 8;) {
					keyBytes[k++] = keyBytes[j++];
				}

				final SecretKey key = new SecretKeySpec(keyBytes, "DESede");
				final IvParameterSpec iv = new IvParameterSpec(new byte[8]);
				final Cipher decipher = Cipher
						.getInstance("DESede/CBC/PKCS5Padding");
				decipher.init(Cipher.DECRYPT_MODE, key, iv);
				final byte[] plainText = decipher.doFinal(message);
				return new String(plainText, "UTF-8");
			} else {

				return "No Password Set";
			}
		} catch (Exception e) {
			return null;
		}

	}

	@Deprecated
	public void OperaNetworkError() {
		if (browser.getTitle().contains("Error")) {
			Env.logger.log(Level.INFO,
					"Network Error found. Attempting to recover...");
			// String currentURL = EP.GetCurrentURL();
			RecoverFromError();
		}
	}

	@Deprecated
	public void RecoverFromError() {
		// int iLoop = 0;
		// do{
		browser.navigate().to("http://www.google.co.in");
		Wait(2);
		// iLoop++;
		// }while(browser.getTitle().contains("Error"));
		// http://www.google.co.in/imghp
		Env.logger.log(Level.INFO,
				"After navigatgion, the title is " + browser.getTitle());
		// for (int i=iLoop;i>=0;i--){
		browser.navigate().back();
		// }
		Env.logger.log(Level.INFO,
				"After recovery, the title is " + browser.getTitle());
	}

	public boolean Spin_SetValue(String ObjectName, String sSetValue) {
		try {
			WebElement ActionObject = GetObject(ObjectName);
			String ObjectID = ActionObject.getAttribute("name");
			RunJavaScript("document.getElementsByName('" + ObjectID
					+ "')[0].value='';");// Clear the existing values by
			// javascript
			ActionObject.sendKeys(sSetValue + Keys.TAB); // Enter the value and
			// hit tab button
			return true;
			// Code to use for ID.
			// RunJavaScript("document.getElementById('"+ObjectID+"').value='"+sSetValue+"';");
			// Previous code to set the value.
			// RunJavaScript("document.getElementsByName('"+ObjectID+"')[0].value='"+sSetValue+"';");
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, ObjectName, e);
			return false;
		}
	}

	public boolean Spin_SetValue(String ObjectName, int iSetValue) {
		String sSetValue = String.valueOf(iSetValue);
		try {
			WebElement ActionObject = GetObject(ObjectName);
			String ObjectID = ActionObject.getAttribute("name");
			RunJavaScript("document.getElementsByName('" + ObjectID
					+ "')[0].value='';");// Clear the existing values by
			// javascript
			ActionObject.sendKeys(sSetValue + Keys.TAB); // Enter the value and
			// hit tab button
			return true;
			// Code to use for ID.
			// RunJavaScript("document.getElementById('"+ObjectID+"').value='"+sSetValue+"';");
			// Previous code to set the value.
			// RunJavaScript("document.getElementsByName('"+ObjectID+"')[0].value='"+sSetValue+"';");
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, ObjectName, e);
			return false;
		}
	}

	public boolean Spin_ClearValue(String ObjectName) {
		try {
			WebElement ActionObject = GetObject(ObjectName);
			String ObjectID = ActionObject.getAttribute("name");
			RunJavaScript("document.getElementsByName('" + ObjectID
					+ "')[0].value='';");// Clear the value by javascript
			return true;
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, ObjectName, e);
			return false;
		}
	}

	public void Spin_VerifyUp(String ObjectName, int noofclicks) {
		String StepID = ObjectName.substring(0, ObjectName.lastIndexOf("_"));
		int CurrentValue = Integer.parseInt(Edit_GetValue(ObjectName));
		String UpArrow = ORXPath(ObjectName)
				+ "/following-sibling::span//div[contains(@class,'spinner-up')]";
		for (int j = 1; j <= noofclicks; j++) {
			Btn_ClickAndProceed(UpArrow);
		}
		CompareText(StepID + "Up_SB",
				String.valueOf(CurrentValue + noofclicks),
				Edit_GetValue(ObjectName));
	}

	public void Spin_VerifyDown(String ObjectName, int noofclicks) {
		String StepID = ObjectName.substring(0, ObjectName.lastIndexOf("_"));
		int CurrentValue = Integer.parseInt(Edit_GetValue(ObjectName));
		String DownArrow = ORXPath(ObjectName)
				+ "/following-sibling::span//div[contains(@class,'spinner-down')]";
		for (int j = 1; j <= noofclicks; j++) {
			Btn_ClickAndProceed(DownArrow);
		}
		CompareText(StepID + "Down_SB",
				String.valueOf(CurrentValue - noofclicks),
				Edit_GetValue(ObjectName));
	}

	public boolean Spin_SetValueByClick(String ObjectName,
			int ValueToBePresentInTheField) {
		int CurrentValue = 0;
		String TextInTheField = "";
		String ArrowToClick = null;
		int NumberOfClicks = 0;
		try {
			WebElement ActionObject = GetObject(ObjectName);
			TextInTheField = ActionObject.getAttribute("value");
			if (TextInTheField.trim().length() == 0) {// If the field is blank,
				// then mark the current
				// value as 0
				TextInTheField = "0";
			}

			CurrentValue = Integer.parseInt(TextInTheField);
			if (CurrentValue > ValueToBePresentInTheField) {
				ArrowToClick = ORXPath(ObjectName)
						+ "/following-sibling::span//div[contains(@class,'spinner-down')]/span"; // Down
				// Arrow
				NumberOfClicks = CurrentValue - ValueToBePresentInTheField;
			} else {
				ArrowToClick = ORXPath(ObjectName)
						+ "/following-sibling::span//div[contains(@class,'spinner-up')]/span"; // Up
				// Arrow
				NumberOfClicks = ValueToBePresentInTheField - CurrentValue;
			}

			for (int j = 1; j <= NumberOfClicks; j++) {
				Btn_ClickAndProceed(ArrowToClick);
			}

			// Check whether the value is correct
			CurrentValue = Integer.parseInt(Edit_GetValue(ObjectName));
			if (CurrentValue != ValueToBePresentInTheField) {
				Env.logger.log(Level.ERROR,
						"Intended value was not set in the Spin Box");
				return false;
			} else {
				return true;
			}
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, "Error while setting the value "
					+ ValueToBePresentInTheField + " in " + ObjectName, e);
			return false;
		}
	}

	public void TakeStaticScreenshot(String ScreenShotName) {
		Env.StaticScreenshotName = UtilClass.GetScreenshot(ScreenShotName);
	}

	public void ClearStaticScreenshot() {
		Env.StaticScreenshotName = null;
	}

	public void NavigateTo(String URLtoNavigate) {
		browser.get(URLtoNavigate);
		WaitForPageload();
		// Store the Module Logged In to
		if (URLtoNavigate.toUpperCase().contains("/ECKO/")) {
			Env.AppModule = "ECKO";
		} else {
			Env.AppModule = "ECOS";
		}
	}

	public boolean FileUpload_SetFile(String ObjectName, String FilePath) {
		File UploadFile;
		try {
			UploadFile = new File(FilePath);
			WebElement ActionObject = GetObject(ObjectName);
			ActionObject.sendKeys(UploadFile.getAbsolutePath());
			return true;
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, ObjectName, e);
			return false;
		} finally {
			UploadFile = null;
		}
	}

	public int GetXcoordinate(String ObjectName) {
		WebElement ActionObject = GetObject(ObjectName);
		int XPosition = ActionObject.getLocation().getX();
		return XPosition;
	}

	public int GetYcoordinate(String ObjectName) {
		WebElement ActionObject = GetObject(ObjectName);
		int XPosition = ActionObject.getLocation().getY();
		return XPosition;
	}

	public String Encode(String PlainString) {
		return Base64.encodeBase64String(PlainString.getBytes());
	}

	public String Decode(String EncryptedString) {
		return (new String(Base64.decodeBase64(EncryptedString)));
	}

	public String Encode(int PlainNumber) {
		return Encode(String.valueOf(PlainNumber));
	}

	public String GetCurrentURL() {
		return browser.getCurrentUrl();
	}

	public String RandomString() {

		String ReturnValue = GetTimeStamp("hhmmss");
		int MainRand = Integer.valueOf(GetTimeStamp("H"));
		int Day = Integer.valueOf(GetTimeStamp("DD"));
		String CurrentDay = GetTimeStamp("E");
		int CurrentDayInNumber = 0;
		if (CurrentDay.startsWith("Sun")) {
			CurrentDayInNumber = 0;
		} else if (CurrentDay.startsWith("Mon")) {
			CurrentDayInNumber = 1;
		} else if (CurrentDay.startsWith("Tue")) {
			CurrentDayInNumber = 2;
		} else if (CurrentDay.startsWith("Wed")) {
			CurrentDayInNumber = 3;
		} else if (CurrentDay.startsWith("Thu")) {
			CurrentDayInNumber = 4;
		} else if (CurrentDay.startsWith("Wed")) {
			CurrentDayInNumber = 5;
		} else if (CurrentDay.startsWith("Thu")) {
			CurrentDayInNumber = 6;
		} else if (CurrentDay.startsWith("Fri")) {
			CurrentDayInNumber = 7;
		} else if (CurrentDay.startsWith("Sat")) {
			CurrentDayInNumber = 8;
		}

		// replacing 1081 with 1025
		if (!Env.RunEnvironment.toUpperCase().startsWith("LOCALTESTSERVER")) {

			int StartSmallEnglish = 98;// Small English starting from c. Unicode
			// Range - Basic Latin
			int StartGreek = 945;// Unicode Range - Greek
			int StartCapitalEnglish = 65;// Capital English starting from A.
			// Unicode Range - Basic Latin
			int StartRussian = 1025; // Unicode Range - Cyrillic
			int StartLatin = 218;// Prev 232//Unicode Range - Latin-1 Supplement
			int StartAnotherRussian = 1162;// Unicode Range - Cyrillic

			ReturnValue = ReturnValue.replace('0',
					(char) (MainRand + StartSmallEnglish));
			ReturnValue = ReturnValue.replace('1', (char) (MainRand
					+ StartGreek + CurrentDayInNumber));
			ReturnValue = ReturnValue.replace('2',
					(char) (MainRand + StartCapitalEnglish));
			ReturnValue = ReturnValue.replace('3', (char) (MainRand
					+ StartRussian + CurrentDayInNumber));
			ReturnValue = ReturnValue.replace('4', (char) (MainRand
					+ StartLatin + CurrentDayInNumber));
			ReturnValue = ReturnValue.replace('5', (char) (MainRand
					+ StartAnotherRussian + CurrentDayInNumber));
			ReturnValue = ReturnValue.replace('6', (char) (MainRand
					+ StartGreek + CurrentDayInNumber + 1));
			ReturnValue = ReturnValue.replace('7', (char) (MainRand
					+ StartRussian + CurrentDayInNumber + 1));
			ReturnValue = ReturnValue.replace('8', (char) (MainRand
					+ StartLatin + CurrentDayInNumber + 1));
			ReturnValue = ReturnValue.replace('9', (char) (MainRand
					+ StartAnotherRussian + CurrentDayInNumber + 1));
		} else {
			ReturnValue = ReturnValue.replace('0',
					(char) (RandomValue(97, 119)));
			ReturnValue = ReturnValue.replace('1',
					(char) (RandomValue(97, 119)));
			ReturnValue = ReturnValue.replace('2',
					(char) (RandomValue(97, 119)));
			ReturnValue = ReturnValue.replace('3',
					(char) (RandomValue(97, 119)));
			ReturnValue = ReturnValue.replace('4',
					(char) (RandomValue(97, 119)));
			ReturnValue = ReturnValue.replace('5',
					(char) (RandomValue(97, 119)));
			ReturnValue = ReturnValue.replace('6',
					(char) (RandomValue(97, 119)));
			ReturnValue = ReturnValue.replace('7',
					(char) (RandomValue(97, 119)));
			ReturnValue = ReturnValue.replace('8',
					(char) (RandomValue(97, 119)));
			ReturnValue = ReturnValue.replace('9',
					(char) (RandomValue(97, 119)));
		}

		ReturnValue = ReturnValue + "X"; // X to identify the patients added
		// through script
		if (Day % 2 == 0) {
			ReturnValue = ReturnValue.substring(0, 1).toUpperCase()
					+ ReturnValue.substring(1);// Make the first letter as caps
		}

		return ReturnValue;
	}

	public boolean IsEnabled(String ObjectName, String LogicalName,String EnabledORDisabled) {
		WebElement ActionObject = GetObject(ORXPath(ObjectName));
		try {
			if (!ActionObject.isDisplayed()) {
				Report.Write(LogicalName, LogicalName + " should be displayed",
						LogicalName + " is not displayed", "Fail", true);
				return false;
			}

			EnabledORDisabled = EnabledORDisabled.toUpperCase();
			if (EnabledORDisabled.toUpperCase().startsWith("ENABLED")) {
				// Verify whether the Object is enabled
				if (ActionObject.isEnabled()) {
					Report.Write(LogicalName, LogicalName
							+ " should be enabled",
							LogicalName + " is enabled", "Pass", true);
					return true;
				} else {
					Report.Write(LogicalName, LogicalName
							+ " should be enabled", LogicalName
							+ " is not enabled", "Fail", true);
					return false;
				}
			} else {
				// Verify whether the Object is disabled
				if (!ActionObject.isEnabled()) {
					Report.Write(LogicalName, LogicalName
							+ " should be disabled", LogicalName
							+ " is disabled", "Pass", true);
					return true;
				} else {
					Report.Write(LogicalName, LogicalName
							+ " should be disabled", LogicalName
							+ " is not disabled", "Fail", true);
					return false;
				}
			}
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, ObjectName, e);
			return false;
		}
	}

	public boolean IsHidden(String ObjectName, String LogicalName) {
		WebElement ActionObject = GetObject(ORXPath(ObjectName));
		boolean ReturnValue = false;
		if (!ActionObject.isDisplayed()) {
			Report.Write(LogicalName, LogicalName + " should not be displayed",
					LogicalName + " is not displayed", "Pass", true);
			ReturnValue = true;
		} else {
			Report.Write(LogicalName, LogicalName + " should not be displayed",
					LogicalName + " is displayed", "Fail", true);
			ReturnValue = false;
		}
		return ReturnValue;
	}

	public void CheckMandatoryAsterik(String ObjectName) {
		WebElement ActionObject = GetObject(ObjectName);
		String ObjectText = ActionObject.getText().trim();

		// If the string ends with :, then replace:
		ObjectText = ObjectText.replaceAll(":$", "").trim();

		if (ObjectText.endsWith("*")) {
			Report.Write(ObjectName, "Asterik Symbol should be displayed.",
					"Asterik Symbol  is displayed", "Pass", true);
		} else {
			Report.Write(ObjectName, "Asterik Symbol  should be displayed.",
					"Asterik Symbol  is not displayed", "Fail", true);
		}
	}

	public String SearchCriteria(String InputString) {
		InputString = InputString.trim();
		try {
			if (InputString.length() <= 1) {
				return InputString.toUpperCase();
			} else if (InputString.length() == 2) {
				return InputString.substring(1).toUpperCase();
			} else {
				return (InputString.substring(1, InputString.length() - 1))
						.toUpperCase();
			}
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, "", e);
			return InputString;
		}
	}

	public void LogOnError(Exception e) {
		String ErrorDescription = null;
		if (e.getCause() != null) {
			ErrorDescription = e.getCause().toString();
			Report.Write("Error", "Test should run without error",
					"Test Stopped. Following error occured: "
							+ ErrorDescription, "Fail", false);
		} else {
			ErrorDescription = e.getMessage();
			Report.Write("Error", "Test should run without error",
					"Test Stopped. Following error occured: "
							+ ErrorDescription, "Fail", true);
		}
		Env.logger.log(Level.ERROR, Env.CurrentTestName, e);

	}

	public String GetMonthName(String DateToTranslate) {
		int MyMonth = 0;
		Date MyDate = null;
		try {
			DateToTranslate = DateToTranslate.trim();
			String DateSeperator = DateToTranslate.substring(2, 3);
			MyDate = (new SimpleDateFormat("dd" + DateSeperator + "MMM"
					+ DateSeperator + "yyyy")).parse(DateToTranslate);
			Calendar cal = Calendar.getInstance();
			cal.setTime(MyDate);
			MyMonth = cal.get(Calendar.MONTH) + 1;
			return LangData("MonthLong" + MyMonth);
		} catch (ParseException e) {
			Env.logger.log(Level.ERROR, Env.CurrentTestName, e);
			return "Error Occured in GetMonthName";
		}
	}

	public String GetBrowserTitle() {
		return browser.getTitle().trim();
	}

	public boolean SwitchFrames(String FrameObject) {
		try {
			if (FrameObject.trim().length() != 0) {
				browser.switchTo().frame(GetObject(FrameObject));
				return true;
			} else {
				browser.switchTo().defaultContent();
				return true;
			}
		} catch (Exception e) {
			return false;
		}
	}

	public boolean SwitchWindows(int WindowToControl) {
		Set<String> windowHandles = browser.getWindowHandles();
		boolean flag;
		try {
			ArrayList<String> winHandle = new ArrayList<String>(windowHandles);
			String CurrentWinHandle = null;
			CurrentWinHandle = winHandle.get(WindowToControl - 1);
			browser.switchTo().window(CurrentWinHandle);
			browser.manage().window().maximize();
			flag = true;
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}

	public void ScrollElementIntoScreen(String ObjectName) {
		WebElement ActionObject = null;
		try {
			ActionObject = GetObject(ObjectName);
			RunJavaScript(ActionObject, "arguments[0].scrollIntoView(true);");
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, "ScrollElementIntoScreen - "
					+ ActionObject.toString(), e);
		}

	}

	public String Date_RoundToHighest(String Date) {
		String RetunrString = Date;
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
			Date InputDate = (Date) formatter.parse(Date);
			Calendar cal = Calendar.getInstance();
			String CurrentDateString = GetServerTimeStamp("dd MMM yyyy");
			cal.setTime(InputDate);
			int RoundDateLargest = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
			String str = Integer.toString(RoundDateLargest);
			StringBuilder RoundDatetoLargest = new StringBuilder(Date);
			RoundDatetoLargest.setCharAt(0, str.charAt(0));
			RoundDatetoLargest.setCharAt(1, str.charAt(1));
			RetunrString = RoundDatetoLargest.toString();

			Date ReturnDate = formatter.parse(RetunrString);
			Date CurrentDate = formatter.parse(CurrentDateString);
			if (ReturnDate.after(CurrentDate)) {
				return CurrentDateString;
			} else {
				return RetunrString;
			}
		} catch (ParseException e) {
			Env.logger.log(Level.ERROR,
					"Exception occured while parsing the date " + Date, e);
			return RetunrString;
		} finally {

		}
	}

	public String Date_RoundToLowest(String Date) {
		StringBuilder RoundedDate = new StringBuilder(Date);
		RoundedDate.setCharAt(0, '0');
		RoundedDate.setCharAt(1, '1');
		return RoundedDate.toString();
	}

	public void CloseAllChildBrowser() {
		Set<String> list = browser.getWindowHandles();
		for (int i = 1; i < list.size(); i++) {
			browser.switchTo().window( (String) list.toArray()[i]);
			browser.close();
		}
		browser.switchTo().window((String) list.toArray()[0]);
	}

	public String GetParentPath(String FilePath) {
		File CurrentFile = new File(FilePath);
		return CurrentFile.getParentFile().getAbsolutePath();
	}

	public String GetDataPath() {
		return Env.DataPath + "/";
	}

	public String ClickAndGetText(WebElement HoverOnElement) {
		String a = null, Value = null;
		JavascriptExecutor js = (JavascriptExecutor) browser;
		try {
			HideCursor();
			HoverOnElement.click();
			UserAction.moveToElement(HoverOnElement).click().build().perform();
			js = (JavascriptExecutor) browser;
			Value = GetAttributeValue("//div[@id='adherence_tip']", "style");
			Value = Value.replace("none", "block");
			js.executeScript("document.getElementById('adherence_tip').setAttribute('style','"
					+ Value + "');");
			a = GetText("//div[@id='adherence_tip']");
		} catch (Exception e) {
			Env.logger.log(Level.INFO, HoverOnElement, e);
		} finally {
			Value = GetAttributeValue("//div[@id='adherence_tip']", "style");
			Value = Value.replace("block", "none");
			js.executeScript("document.getElementById('adherence_tip').setAttribute('style','"
					+ Value + "');");
			UserAction.release();
			HoverOnElement = null;
		}
		return a;
	}

	public void MoveToElement(WebElement a) {

		UserAction.moveToElement(a).click().build().perform();

	}

}
