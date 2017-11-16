package functions;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.log4j.Level;

public class TestDriver {

	public static void main(String[] args) throws ClassNotFoundException,
			SecurityException, NoSuchMethodException, IllegalArgumentException,
			IllegalAccessException, InvocationTargetException, BiffException,
			IOException {

		// Driver Declaration
		String DriverPath = "";
		Env.UserID = System.getProperty("user.name").toLowerCase();

		// Take the Driver file, based on the arugment passed
		if (args.length == 0) {
			DriverPath = "C:/Users/" + Env.UserID + "/Desktop/TestDriver.xls";
		} else {
			DriverPath = "C:/Users/" + Env.UserID + "/Desktop/"
					+ args[0].trim() + ".xls";
		}

		Workbook DriverBook = Workbook.getWorkbook(new File(DriverPath));
		Sheet DriverSheet = DriverBook.getSheet(0);
		String TestName = null;
		boolean RunFlag = false;
		List<String> CountriesToRun = new ArrayList<String>();
		List<String> LanguagesToRun = new ArrayList<String>();
		List<String> BrowsersToRun = new ArrayList<String>();
		LinkedHashMap<String, Boolean> TestList = new LinkedHashMap<String, Boolean>();

		// Load the Environment Values from Excel
		Env.OSUsed = DriverSheet.getCell(5, 1).getContents().toUpperCase()
				.trim();
		Env.MachineName = DriverSheet.getCell(5, 2).getContents().trim();
		Env.RunEnvironment = DriverSheet.getCell(5, 3).getContents().trim();
		if (Env.MachineName.toUpperCase().trim().startsWith("LOCALHOST")) {
			Env.RuninLocalMachine = true;
		} else {
			Env.RuninLocalMachine = false;
		}
		Env.LogLevel = Level.DEBUG;

		// //Proxy Settings
		// JPasswordField passwordField = new JPasswordField();
		// Object[] obj = {"Please enter the password:\n\n", passwordField};
		// Object stringArray[] = {"OK"};
		// if (JOptionPane.showOptionDialog(null, obj,
		// "Need password",JOptionPane.YES_NO_OPTION,
		// JOptionPane.QUESTION_MESSAGE, null, stringArray, obj) ==
		// JOptionPane.YES_OPTION){
		// Env.UserPhrase = UtilClass.TripleIn(passwordField.getPassword());
		// }else{
		// //System.out.println("I am out");
		// }

		// Load all the Test Names from Excel into HashMap
		for (int RowNo = 1; RowNo < DriverSheet.getRows(); RowNo++) {
			TestName = DriverSheet.getCell(0, RowNo).getContents().trim();
			if (TestName.length() == 0) {
				continue;
			}
			if (DriverSheet.getCell(1, RowNo).getContents().trim()
					.compareToIgnoreCase("YES") == 0) {
				RunFlag = true;
			} else {
				RunFlag = false;
			}
			TestList.put(TestName, RunFlag);
		}

		// Load all the Countries and Languages
		String Country, Language, Browser;
		for (int RowNo = 6; RowNo < DriverSheet.getRows(); RowNo++) {
			Country = DriverSheet.getCell(4, RowNo).getContents().trim();
			Language = DriverSheet.getCell(5, RowNo).getContents().trim();
			Browser = DriverSheet.getCell(6, RowNo).getContents().trim();
			if (Country.length() == 0 || Language.length() == 0) {
				break;
			}
			CountriesToRun.add(Country);
			LanguagesToRun.add(Language);
			BrowsersToRun.add(Browser);
		}

		// Release the Excel, for next run
		DriverSheet = null;
		DriverBook.close();

		String PackageName = "testscripts.";

		for (int i = 0; i < CountriesToRun.size(); i++) {
			// System.out.println(CountriesToRun.get(i));
			// System.out.println(LanguagesToRun.get(i));

			Env.CountryUsed = CountriesToRun.get(i);
			Env.LanguageUsed = LanguagesToRun.get(i);
			// BrowserList = Arrays.asList(BrowsersToRun.get(i).split(";"));
			String CurrentBrowserName = BrowsersToRun.get(i).trim();

			// //For Each Browser
			// for(String CurrentBrowserName : BrowserList) {

			// Assign the Browser Name
			Env.SetBrowserUsed(CurrentBrowserName);

			Reporter.OpenLOG();

			// Open the Driver
			Reporter.OpenDriver();

			// Run each test, which has the RunFlag as 'Yes'
			for (String CurrentTestName : TestList.keySet()) {
				RunFlag = TestList.get(CurrentTestName);
				if (RunFlag) {

					// Get Package Name
					if (CurrentTestName.startsWith("MIG")) {
						PackageName = "migration.";
					} else if (CurrentTestName.startsWith("SUP")) {
						PackageName = "support.";
					} else if (CurrentTestName.startsWith("REG")) {
						PackageName = "regression.";
					} else if (CurrentTestName.startsWith("ZZZ")) {
						PackageName = "TBD.";
					} else if (CurrentTestName.startsWith("CTQ")) {
						PackageName = "regression.CTQ.";
					} else if (CurrentTestName.startsWith("eCRF")) {
						PackageName = "testscripts.datarecords.";
					} else if (CurrentTestName.startsWith("eMail")) {
						PackageName = "testscripts.eMail.";
					} else {
						PackageName = "testscripts.";
					}

					try {
						Class<?> cls = Class.forName(PackageName
								+ CurrentTestName);
						Method meth = cls.getMethod("main", String[].class);
						String[] params = null; // init params accordingly
						System.out.println("Running Test...  " + cls.getName());
						meth.invoke(null, (Object) params); // static method
															// doesn't have an
															// instance
					} catch (ClassNotFoundException e) {
						Env.logger.log(Level.ERROR, CurrentTestName, e);
					}

				} else {
					System.out.println("Skipping Test..." + CurrentTestName);
				}

			}// Goto to the next Test

			// Close the Driver
			Reporter.CloseDriver();

			// }//Goto to the next browser

			Reporter.CloseLog();

		}// Goto to the Next Country-Language
	}
}
