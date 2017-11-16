package functions;

import java.io.File;
import java.io.IOException;

import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class ExcelUtil {
	int LangaugeColNo = 1; // Defaulted to One
	String FilePath;
	Workbook wrkbook;
	Sheet wrksheet;
	public int RowCount = 0;
	static Logger Excellogger;

	// Constructor with String filepath
	public ExcelUtil(String FileLocation, boolean ContainsLangCol) {

		// if (Excellogger == null){
		// Excellogger = Logger.getLogger(ExcelUtil.class.getName());
		// FileAppender FA = null;
		// try {
		// // String Logfilepath = Env.FrameWorkPath +
		// "/EasyPod/logs/ExcelLog.log";
		// String Logfilepath = new
		// File(Env.ScreenshotPath).getParentFile().getAbsolutePath() +
		// "/ExcelLog.log";
		// FA = new FileAppender(new PatternLayout("%m%n"), Logfilepath,true);
		// } catch (IOException e) {
		// System.err.println(e.getMessage());
		// }
		// Excellogger.setAdditivity(false);
		// Excellogger.addAppender(FA);
		// Excellogger.log(Level.INFO, "TimeStamp" + "\t" + "TestName" + "\t" +
		// "FileType"+ "\t" + "ObjectID");
		// }

		this.FilePath = FileLocation;
		try {
			WorkbookSettings ws = new WorkbookSettings();
			ws.setEncoding("Cp1252");
			this.wrkbook = Workbook.getWorkbook(new File(FileLocation), ws);
			this.wrksheet = this.wrkbook.getSheet(0); // By Default take the
														// First Sheet
														// (LocalTestServer)

			// Switch the Sheet for Test Data alone
			if (FileLocation.endsWith("/TestData.xls")
					&& Env.RunEnvironment
							.compareToIgnoreCase("LocalTestServer") != 0) {
				if (Env.RunEnvironment.toUpperCase().trim().startsWith("INTEG")) {
					this.wrksheet = this.wrkbook.getSheet(1); // Second Sheet
				} else if (Env.RunEnvironment.compareToIgnoreCase("Support") == 0) {
					this.wrksheet = this.wrkbook.getSheet(2); // Third Sheet
				} else {
					this.wrksheet = this.wrkbook.getSheet(0); // Take First
																// Sheet
																// (LocalServer)
																// Sheet, if
																// nothing
																// matches
				}
			}

			if (ContainsLangCol == true) {
				if (FileLocation.endsWith("/TestData.xls")) { // For Test Data
					this.LangaugeColNo = this.GetColIndex(Env.CountryUsed);
					if (this.LangaugeColNo == 0) {
						Env.logger.log(Level.FATAL,
								"There is no column in TestData file for Country: "
										+ Env.CountryUsed);
						System.exit(1);
					}
				} else { // For Other Files
					this.LangaugeColNo = this.GetColIndex(Env.LanguageUsed);

					if (this.LangaugeColNo == 0) {
						Env.logger.log(Level.FATAL,
								"There is no column in LangData file for Language: "
										+ Env.LanguageUsed);
						System.exit(1);
					}

					// Print the Country and Language used
					Env.logger.log(Level.INFO, Env.CountryUsed + " - "
							+ Env.LanguageUsed);
				}
			} else {
				this.LangaugeColNo = this.GetColIndex("TextID");
			}

		} catch (BiffException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Default Constructor
	ExcelUtil() {
		// Need to Implement
	}

	public int GetRowCount() {
		return wrksheet.getRows();
	}

	public int GetColCount() {
		return this.wrksheet.getColumns();
	}

	public void GetSheet(int SheetIndex) {
		this.wrksheet = wrkbook.getSheet(SheetIndex);
	}

	public String GetData(int column, int row) {
		try {
			return this.wrksheet.getCell(column, row).getContents();
		} catch (ArrayIndexOutOfBoundsException e) {
			return "No such cell in Excel";
		}
	}

	public String GetData(int row) {
		return this.wrksheet.getCell(this.LangaugeColNo, row).getContents();
	}

	public String GetData(String TextID) {
		int rowindex = this.GetRowIndex(0, TextID);
		if (rowindex != 0) {
			return (GetData(this.LangaugeColNo, rowindex)).trim();
		} else {
			return "Data is not found for TextID - '" + TextID + "'";
		}

	}

	public String GetXPath(String TextID) {
		int rowindex = this.GetRowIndex(0, TextID);
		if (rowindex != 0) {
			return (GetData(2, rowindex)).trim();
		} else {
			return "XPath is not found for TextID - " + TextID; // Write in
																// console
		}

	}

	public int GetColIndex(String ColumnName) {
		Sheet MySheet = this.wrksheet;
		ColumnName = ColumnName.trim();
		for (int col = 0; col < MySheet.getColumns(); col++) {
			if (MySheet.getCell(col, 0).getContents().trim()
					.compareTo(ColumnName) == 0) {
				return col;
			}
		}
		return 0;
	}

	public int GetRowIndex(int SearchinColumn, String ContenttoSearch) {
		Sheet MySheet = this.wrksheet;
		int TotalRows = MySheet.getRows();
		for (int row = 0; row < TotalRows; row++) {
			if (MySheet.getCell(SearchinColumn, row).getContents()
					.compareTo(ContenttoSearch) == 0) {
				return row;
			}
		}
		return 0;
	}

	public void SetSheet(String SheetName, boolean ShouldUpdateColNo) {
		if (this.FilePath.endsWith("/TestData.xls")) {// For Test Data
			this.wrksheet = this.wrkbook.getSheet(SheetName);
			this.RowCount = this.wrksheet.getRows();
			if (ShouldUpdateColNo) {
				this.LangaugeColNo = this.GetColIndex(Env.CountryUsed);
				if (this.LangaugeColNo == 0) {
					Env.logger.log(Level.FATAL,
							"There is no column in TestData file for Country: "
									+ Env.CountryUsed);
					System.exit(1);
				}
			}
		} else {
			Env.logger.log(Level.ERROR,
					"Set Sheet method will work only for test data");
		}
	}

	public String GetData(String ColName, String TextID) {
		int rowindex = this.GetRowIndex(0, TextID);
		int colindex = this.GetColIndex(ColName);
		if (rowindex != 0) {
			return (GetData(colindex, rowindex)).trim();
		} else {
			return "Data is not found for TextID - '" + TextID + "'";
		}

	}

	public String GetData(String ColName, int rowindex) {
		int colindex = this.GetColIndex(ColName);
		if (rowindex != 0) {
			return (GetData(colindex, rowindex)).trim();
		} else {
			return "Data is not found for ColumnName - '" + ColName
					+ "' in Row " + rowindex;
		}

	}

	public void SwitchToEnglishLanguage(boolean IsOnlyFromEnglish) {

		// Check whether it is langdata
		if (!this.FilePath.endsWith("/LanguageData.xls")) {
			Env.logger
					.log(Level.FATAL,
							"SwitchToEnglishLanguage method can be called only for LanguageData file.");
			System.exit(1);
		}

		// Set the Current Language
		String CurrentLanguage = null;
		if (IsOnlyFromEnglish) {
			CurrentLanguage = "English";
		} else {
			CurrentLanguage = Env.LanguageUsedWithDot;
		}

		// Update instance variable based on the current language
		this.LangaugeColNo = this.GetColIndex(CurrentLanguage);
		if (this.LangaugeColNo == 0) {
			Env.logger.log(Level.FATAL,
					"There is no column in LangData file for Language: "
							+ CurrentLanguage);
			System.exit(1);
		}
	}

	public void SwitchToLangData(String LanguageToSwitch) {
		// Check whether it is langdata
		if (!this.FilePath.endsWith("/LanguageData.xls")) {
			Env.logger
					.log(Level.FATAL,
							"SwitchToLangData method can be called only for LanguageData file.");
			System.exit(1);
		}
		// Update instance variable based on the current language
		this.LangaugeColNo = this.GetColIndex(LanguageToSwitch);
		if (this.LangaugeColNo == 0) {
			Env.logger.log(Level.FATAL,
					"There is no column in LangData file for Language: "
							+ LanguageToSwitch);
			System.exit(1);
		}
	}

	public void SwitchToTestData(String CountryToSwitch) {
		// Check whether it is langdata
		if (!this.FilePath.endsWith("/TestData.xls")) {
			Env.logger
					.log(Level.FATAL,
							"SwitchToTestData method can be called only for TestData file.");
			System.exit(1);
		}
		// Update instance variable based on the current language
		this.LangaugeColNo = this.GetColIndex(CountryToSwitch);
		if (this.LangaugeColNo == 0) {
			Env.logger.log(Level.FATAL,
					"There is no column in TestData file for Country: "
							+ CountryToSwitch);
			System.exit(1);
		}
	}

	public String GetTextID(String ColumnToSearch, String ValueToSearch) {
		int rowindex = GetRowIndex(GetColIndex(ColumnToSearch), ValueToSearch);
		if (rowindex != 0) {
			return GetData(0, rowindex);
		} else {
			return "Data is not found for TextID - '" + ValueToSearch + "'";
		}

	}

}
