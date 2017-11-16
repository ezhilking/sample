package functions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Level;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import eCRF.ArgentinaECRF;
import eCRF.AustraliaECRF;
import eCRF.AustriaECRF;
import eCRF.CanadaECRF;
import eCRF.ColombiaECRF;
import eCRF.CzechRepublicECRF;
import eCRF.FinlandECRF;
import eCRF.FranceECRF;
import eCRF.GreeceECRF;
import eCRF.HungaryECRF;
import eCRF.IcelandECRF;
import eCRF.IndonesiaECRF;
import eCRF.ItalyECRF;
import eCRF.KoreaECRF;
import eCRF.MexicoECRF;
import eCRF.NorwayECRF;
import eCRF.SaudiArabiaECRF;
import eCRF.SingaporeECRF;
import eCRF.SlovakiaECRF;
import eCRF.SpainECRF;
import eCRF.SwedenECRF;
import eCRF.TaiwanECRF;
import eCRF.UnitedArabEmiratesECRF;
import eCRF.UnitedKingdomECRF;
import functions.Easypod.Country;

public class ECOSStudyBaseline {
	Reporter Report = new Reporter();
	protected static String MaxValue = "9876543210";
	protected static String MinValue = "0";
	protected static String IncorrectFormat = "a#b";
	protected int Random;
	protected Country switchCountryName = Country.valueOf(Env.CountryUsed
			.replaceAll(" ", "").toUpperCase());
	protected Easypod EP;

	public void LoadBaseline(Easypod RunningUtil) {
		this.EP = RunningUtil;
	}

	// ================================================General ECOS methods
	// Starts here================================================

	public String GetChapterIndex(String ListOfChapters, String ChapterToFind) {
		List<String> SearchList = Arrays.asList(ListOfChapters.split("\\|"));
		for (int CurrentIndex = 0; CurrentIndex <= SearchList.size() - 1; CurrentIndex++) {
			if (EP.CompareText(SearchList.get(CurrentIndex), ChapterToFind)) {
				return String.valueOf(CurrentIndex + 1);
			}
		}
		return String.valueOf(-1);
	}

	public static String AppendHash(String TabName) {
		if (TabName.compareToIgnoreCase("VisitsRecord") == 0) {
			TabName = "AddVisit";
		} else if (TabName.compareToIgnoreCase("PatientDetails") == 0) {
			TabName = "RecordOverview";
		}
		if (!TabName.startsWith("#")) {
			return "#" + TabName + "_";
		} else {
			return TabName;
		}

	}

	public boolean IsFieldDisabled(String ObjectName) {

		String Actual = EP.GetAttributeValue(ObjectName, "class").toUpperCase();
		String Expected = "readonly".toUpperCase();

		if (Actual.contains(Expected)) {
			Report.Write(ObjectName, ObjectName + " should be disabled",
					ObjectName + " is disabled", "Pass", true);
			return true;
		} else {
			Report.Write(ObjectName, ObjectName + " should be disabled",
					ObjectName + " is not disabled", "Fail", true);
			return false;
		}
	}

	public void DeleteRecord(String ObjectName, boolean TextVerification) {
		boolean AlertFound = false;

		EP.Btn_ClickAndProceed(ObjectName); // Btn_ClickAndProceed
		if (TextVerification) {
			EP.TakeStaticScreenshot("DeletePopup");
			EP.VerifyText("#General_DeleteHeader_ST");
			EP.VerifyText("#General_DeleteText_ST");
			EP.VerifyText("#General_DeleteYes_ST");
			EP.VerifyText("#General_DeleteNo_ST");
			EP.VerifyPageLayout();
			if (!EP.ObjectExists("#AddVisit_SelectVisitRecord_BN")) { // Code
																		// added
																		// to
																		// navigate
																		// to
																		// the
																		// visits
																		// page
																		// if
																		// the
																		// pagelayout
																		// navigates
																		// to
																		// some
																		// other
																		// page
				EP.NavigateTo(Env.ECOSURL);
				EP.SearchForPatient(EP.TestData("ECOSPatient"));// John Doe
				EP.Rbtn_SetOption("#MyPatients_FirstPatient_RB", "ON");
				EP.Btn_Click("#MyPatients_VisitsRecord_BN");
				EP.WbTbl_SortColumn("#AddVisit_VisitDate_TE", 2, "DESC");
				EP.Rbtn_SetOption("#AddVisit_FirstVisit_RB", "ON");// Set
																	// prerequisite
																	// for
																	// Latest
																	// Second
																	// Visit
				EP.Btn_Click("#AddVisit_Continue_BN");
			}
			EP.Btn_ClickAndProceed(ObjectName);
		}

		if (Env.GetBrowserUsed().startsWith("IE")) {
			EP.Wait(2);
		}
		AlertFound = EP.Btn_Click("#General_DeleteYes_ST");
		EP.Wait(2);
		// SupportedBrowserType BrowserType =
		// SupportedBrowserType.valueOf(Env.GetBrowserUsed());
		// switch(BrowserType){
		// case IE32:
		// case IE64:
		// case GC :
		// case FF :
		// EP.Btn_ClickAndProceed(ObjectName); //Btn_ClickAndProceed
		// if(EP.IsAlertPresent()){
		// String ActualAlertText = EP.GetAlertText();
		// String ExpectedAlertText =EP.LangData("DeleteConfirm");
		// EP.HandleAlerts();
		// if (TextVerification){
		// EP.WaitForPageload();
		// if (EP.CompareText(ExpectedAlertText, ActualAlertText)){
		// EP.Report.Write("ConfirmDeleteAlert_DT",ExpectedAlertText,ActualAlertText,"Pass",false);
		// }else{
		// EP.Report.Write("ConfirmDeleteAlert_DT",ActualAlertText,ActualAlertText,"Fail",false);
		// }
		// }
		// AlertFound = true;
		// }
		// break;
		//
		// case OP :
		// Thread DeleteRecordOpera = new Thread(new
		// PopUp("DeleteRecordForOpera"),"DeleteRecordForOpera");
		// DeleteRecordOpera.start();
		// AlertFound = EP.Btn_ClickAndProceed(ObjectName);
		// try {
		// DeleteRecordOpera.join();
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		// break;
		// case SA :
		// EP.RunJavaScript("window.confirm = function(message){return true;};");
		// AlertFound = EP.Btn_ClickAndProceed(ObjectName);
		// //Btn_ClickAndProceed
		// EP.WaitForPageload();
		// }

		if (!AlertFound) {
			Env.logger
					.log(Level.ERROR,
							"Confirmation PopUp is not displayed while deleting the record.");
		}
	}

	public void VerifyChapterName(String ActionDone, String TabNumber,
			String ExpectedText) {
		final String ObjectXpath;
		// if (ActionDone.compareToIgnoreCase("PatientDetails")==0){
		// ObjectXpath =
		// "(//ul[@class='baseRecord overview']/li//div[@class='libg'])["+TabNumber+"]/h2";
		//
		// }else
		if (ActionDone.compareToIgnoreCase("PatientDetails") == 0
				&& Env.CurrentTestName.contains("eCRFAdmin")
				&& Env.CurrentTestName.contains("OverviewBaseline")) {
			ObjectXpath = "((//ul[@class='baseRecord overview'])[2]/li//div[@class='libg'])["
					+ TabNumber + "]/h2";

		} else if (ActionDone.compareToIgnoreCase("PatientDetails") == 0
				&& Env.CurrentTestName.contains("eCRFAdmin")
				&& Env.CurrentTestName.contains("OverviewVisits")) {
			ObjectXpath = "((//ul[@class='baseRecord overview'])[4]/li//div[@class='libg'])["
					+ TabNumber + "]/h2";

		} else if (ActionDone.compareToIgnoreCase("RecordOverview") == 0
				&& Env.CurrentTestName.contains("eCRFAdmin")
				&& Env.CurrentTestName.contains("OverviewBaseline")) {
			ObjectXpath = "(//ul[2]//div[@class='container show']//div[@class='libg'])["
					+ TabNumber + "]/h2";

		} else if (ActionDone.compareToIgnoreCase("RecordOverview") == 0
				&& Env.CurrentTestName.contains("eCRFAdmin")
				&& Env.CurrentTestName.contains("OverviewVisits")) {
			ObjectXpath = "(//ul[4]//div[@class='container']//div[@class='libg'])["
					+ TabNumber + "]/h2";

		} else if (ActionDone.compareToIgnoreCase("RecordOverview") == 0) {
			ObjectXpath = "(//div[@class='container show']//div[@class='libg clearfix'])["
					+ TabNumber + "]/h2";
		} else {
			ObjectXpath = "(//ul/li/div[@class='libg'])[" + TabNumber + "]/h2";

		}

		boolean ObjectPresent = EP.WaitForObject(ObjectXpath);
		if (ObjectPresent
				&& (ActionDone.startsWith("RecordOverview")
						|| ActionDone.startsWith("PatientDetails") || ActionDone
							.startsWith("DataRecord"))) {
			EP.ScrollElementIntoScreen(ObjectXpath);
		}

		EP.CompareText("ChapterName_ST", ExpectedText, EP.GetText(ObjectXpath));
	}

	public String GetDataRecordChapterName(String ActionDone, String TabNumber) {
		final String ObjectXpath;

		if (ActionDone.compareToIgnoreCase("PatientDetails") == 0
				&& Env.CurrentTestName.contains("eCRFAdmin")
				&& Env.CurrentTestName.contains("OverviewBaseline")) {
			ObjectXpath = "((//ul[@class='baseRecord overview'])[2]/li//div[@class='libg'])["
					+ TabNumber + "]/h2";

		} else if (ActionDone.compareToIgnoreCase("PatientDetails") == 0
				&& Env.CurrentTestName.contains("eCRFAdmin")
				&& Env.CurrentTestName.contains("OverviewVisits")) {
			ObjectXpath = "((//ul[@class='baseRecord overview'])[4]/li//div[@class='libg'])["
					+ TabNumber + "]/h2";

		} else if ((ActionDone.compareToIgnoreCase("RecordOverview") == 0
				&& Env.CurrentTestName.contains("eCRFAdmin")
				&& Env.CurrentTestName.contains("OverviewBaseline")
				|| ActionDone.compareToIgnoreCase("BaselineRecordOverview") == 0
				&& Env.CurrentTestName.startsWith("REG_") || ActionDone
				.compareToIgnoreCase("BaselineRecordOverview") == 0
				&& Env.CurrentTestName.startsWith("CTQ_"))) {
			ObjectXpath = "(//ul[2]//div[@class='container show']//div[@class='libg'])["
					+ TabNumber + "]/h2";

		} else if ((ActionDone.compareToIgnoreCase("RecordOverview") == 0
				&& Env.CurrentTestName.contains("eCRFAdmin")
				&& Env.CurrentTestName.contains("OverviewVisits")
				|| ActionDone.compareToIgnoreCase("VisitRecordOverview") == 0
				&& Env.CurrentTestName.startsWith("REG_") || ActionDone
				.compareToIgnoreCase("VisitRecordOverview") == 0
				&& Env.CurrentTestName.startsWith("CTQ_"))) {
			ObjectXpath = "(//ul[4]//div[@class='container']//div[@class='libg'])["
					+ TabNumber + "]/h2";

		} else if (ActionDone.compareToIgnoreCase("RecordOverview") == 0) {
			ObjectXpath = "(//div[@class='container show']//div[@class='libg clearfix'])["
					+ TabNumber + "]/h2";
		} else {
			ObjectXpath = "(//ul/li/div[@class='libg'])[" + TabNumber + "]/h2";

		}

		boolean ObjectPresent = EP.WaitForObject(ObjectXpath);
		if (ObjectPresent
				&& (ActionDone.startsWith("RecordOverview")
						|| ActionDone.startsWith("PatientDetails") || ActionDone
							.startsWith("DataRecord"))) {
			EP.ScrollElementIntoScreen(ObjectXpath);
		}

		return EP.GetText(ObjectXpath);
	}

	public void VerifyChapterStatus(String ActionDone, String TabNumber,
			String ExpectedText) {

		final String ObjectXpath;
		String ChapterName = GetDataRecordChapterName(ActionDone, TabNumber);
		if (ActionDone.compareToIgnoreCase("PatientDetails") == 0
				&& Env.CurrentTestName.contains("eCRFAdmin")
				&& Env.CurrentTestName.contains("OverviewBaseline")) {
			ObjectXpath = "((//ul[@class='baseRecord overview'])[2]/li//div[@class='libg'])["
					+ TabNumber + "]/span";
		} else if (ActionDone.compareToIgnoreCase("PatientDetails") == 0
				&& Env.CurrentTestName.contains("eCRFAdmin")
				&& Env.CurrentTestName.contains("OverviewVisits")) {
			ObjectXpath = "((//ul[@class='baseRecord overview'])[4]/li//div[@class='libg'])["
					+ TabNumber + "]/span";
		} else if ((ActionDone.compareToIgnoreCase("RecordOverview") == 0
				&& Env.CurrentTestName.contains("eCRFAdmin")
				&& Env.CurrentTestName.contains("OverviewBaseline")
				|| ActionDone.compareToIgnoreCase("BaselineRecordOverview") == 0
				&& Env.CurrentTestName.startsWith("REG_") || ActionDone
				.compareToIgnoreCase("BaselineRecordOverview") == 0
				&& Env.CurrentTestName.startsWith("CTQ_"))) {
			ObjectXpath = "(//ul[2]//div[@class='container show']//div[@class='libg'])["
					+ TabNumber + "]/span";
		} else if ((ActionDone.compareToIgnoreCase("RecordOverview") == 0
				&& Env.CurrentTestName.contains("eCRFAdmin")
				&& Env.CurrentTestName.contains("OverviewVisits")
				|| ActionDone.compareToIgnoreCase("VisitRecordOverview") == 0
				&& Env.CurrentTestName.startsWith("REG_") || ActionDone
				.compareToIgnoreCase("VisitRecordOverview") == 0
				&& Env.CurrentTestName.startsWith("CTQ_"))) {
			ObjectXpath = "(//ul[4]//div[@class='container']//div[@class='libg'])["
					+ TabNumber + "]/span";
		} else if (ActionDone.compareToIgnoreCase("RecordOverview") == 0) {
			ObjectXpath = "(//div[@class='container show']//div[@class='libg clearfix'])["
					+ TabNumber + "]/span";
		} else {
			ObjectXpath = "(//ul/li/div[@class='libg'])[" + TabNumber
					+ "]/span[2]";
		}

		// Wait for the Navigation bar to be present
		// (RecordOverview,Visits,Baseline)
		if (ActionDone.compareTo("BaselineRecord") == 0) {
			EP.Wait(3);
		}
		EP.WaitForPageload();
		EP.WaitForObject(ObjectXpath);

		if (!ExpectedText.contains("|")) {
			EP.CompareText(ChapterName + "(" + ActionDone + ")Status_ST",
					ExpectedText, EP.GetText(ObjectXpath));
		} else {
			EP.CompareWithList(ChapterName + "(" + ActionDone + ")Status_ST",
					ExpectedText, EP.GetText(ObjectXpath));
		}
	}

	public String GetChapterStatus(String ActionDone, String TabNumber) {

		final String ObjectXpath;
		String ChapterStatus = "";
		if (ActionDone.compareToIgnoreCase("PatientDetails") == 0
				&& Env.CurrentTestName.contains("eCRFAdmin")
				&& Env.CurrentTestName.contains("OverviewBaseline")) {
			ObjectXpath = "((//ul[@class='baseRecord overview'])[2]/li//div[@class='libg'])["
					+ TabNumber + "]/span";
		} else if (ActionDone.compareToIgnoreCase("PatientDetails") == 0
				&& Env.CurrentTestName.contains("eCRFAdmin")
				&& Env.CurrentTestName.contains("OverviewVisits")) {
			ObjectXpath = "((//ul[@class='baseRecord overview'])[4]/li//div[@class='libg'])["
					+ TabNumber + "]/span";
		} else if ((ActionDone.compareToIgnoreCase("RecordOverview") == 0
				&& Env.CurrentTestName.contains("eCRFAdmin")
				&& Env.CurrentTestName.contains("OverviewBaseline")
				|| ActionDone.compareToIgnoreCase("BaselineRecordOverview") == 0
				&& Env.CurrentTestName.startsWith("REG_") || ActionDone
				.compareToIgnoreCase("BaselineRecordOverview") == 0
				&& Env.CurrentTestName.startsWith("CTQ_"))) {
			ObjectXpath = "(//ul[2]//div[@class='container show']//div[@class='libg'])["
					+ TabNumber + "]/span";
		} else if ((ActionDone.compareToIgnoreCase("RecordOverview") == 0
				&& Env.CurrentTestName.contains("eCRFAdmin")
				&& Env.CurrentTestName.contains("OverviewVisits")
				|| ActionDone.compareToIgnoreCase("VisitRecordOverview") == 0
				&& Env.CurrentTestName.startsWith("REG_") || ActionDone
				.compareToIgnoreCase("VisitRecordOverview") == 0
				&& Env.CurrentTestName.startsWith("CTQ_"))) {
			ObjectXpath = "(//ul[4]//div[@class='container']//div[@class='libg'])["
					+ TabNumber + "]/span";
		} else if (ActionDone.compareToIgnoreCase("RecordOverview") == 0) {
			ObjectXpath = "(//div[@class='container show']//div[@class='libg clearfix'])["
					+ TabNumber + "]/span";
		} else {
			ObjectXpath = "(//ul/li/div[@class='libg'])[" + TabNumber
					+ "]/span[2]";
		}

		// Wait for the Navigation bar to be present
		// (RecordOverview,Visits,Baseline)
		if (ActionDone.compareTo("BaselineRecord") == 0) {
			EP.Wait(3);
		}
		EP.WaitForPageload();
		EP.WaitForObject(ObjectXpath);

		ChapterStatus = EP.GetText(ObjectXpath);
		return ChapterStatus;

	}

	public void VerifyChapterHeading(String ActionDone, String TabNumber,
			int HeadingIndex, String ExpectedText) {
		final String ObjectXpath;
		if (ActionDone.compareToIgnoreCase("PatientDetails") == 0) {
			ObjectXpath = "(//ul[@class='baseRecord overview']/li//div[@class='libg'])["
					+ TabNumber
					+ "]/following-sibling::div[1]/span[@class='blockTitle']["
					+ HeadingIndex + "]";
		} else if (ActionDone.compareToIgnoreCase("RecordOverview") == 0
				&& Env.CurrentTestName.contains("eCRFAdmin")
				&& Env.CurrentTestName.contains("OverviewBaseline")) {
			ObjectXpath = "(//ul[2]//div[@class='container show' or @class='container']//div[@class='libg'])["
					+ TabNumber
					+ "]/following-sibling::div[1]/span[@class='blockTitle']["
					+ HeadingIndex + "]";
		} else if (ActionDone.compareToIgnoreCase("RecordOverview") == 0
				&& Env.CurrentTestName.contains("eCRFAdmin")
				&& Env.CurrentTestName.contains("OverviewVisits")) {
			ObjectXpath = "(//ul[4]//div[@class='container show' or @class='container']//div[@class='libg'])["
					+ TabNumber
					+ "]/following-sibling::div[1]/span[@class='blockTitle']["
					+ HeadingIndex + "]";
		} else if (ActionDone.compareToIgnoreCase("RecordOverview") == 0) {
			ObjectXpath = "(//div[@class='container show']//div[@class='libg clearfix'])["
					+ TabNumber
					+ "]/following-sibling::div[1]/span[@class='blockTitle']["
					+ HeadingIndex + "]";
		} else {
			// ObjectXpath =
			// "(//div[@class='container show']//div[@class='chapterHeading'])["+HeadingIndex+"]";
			ObjectXpath = "(//div[@class[contains(.,'show')] and @class[contains(.,'container')]]//div[@class='chapterHeading'])["
					+ HeadingIndex + "]";

		}

		if (ActionDone.startsWith("RecordOverview")) {
			EP.ScrollElementIntoScreen(ObjectXpath);
		}

		EP.WaitForObject(ObjectXpath);
		EP.CompareText("ChapterHeading" + HeadingIndex + "_ST", ExpectedText,
				EP.GetText(ObjectXpath));
	}

	public void VerifySuccessMessage(String TabName) {
		EP.CompareText("#General_RecordsSuccessMsg_DT",
				EP.LangData("DataUpdated").replace("{0}", TabName),
				EP.GetText("#General_RecordsSuccessMsg_DT"));
	}

	public void VerifyDeleteMessage(String TabName) {
		EP.CompareText("#General_RecordsDeleteMsg_DT",
				EP.LangData("DataDeletedSuccess").replace("{0}", TabName),
				EP.GetText("#General_RecordsDeleteMsg_DT"));
	}

	public void RemoveScrollBar(String ObjectXPath) {
		try {
			WebElement ScrollElement = EP.GetObject(ObjectXPath); //
			EP.RunJavaScript(
					ScrollElement,
					"arguments[0].setAttribute('style', 'margin: 10px ! important; border: 1px solid rgb(134, 177, 134); display: block;');");
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, ObjectXPath, e);
		}
	}

	public void CheckTableText(String ObjectName, int Row, int Col,
			int ObjectIndex, String QuestionText, String ExpectedAnswer) {
		String Xpath = "((" + EP.ORXPath(ObjectName) + ")/tbody/tr[" + Row
				+ "]/td[" + Col
				+ "]//div[@class='toolTipDiv' or @id='toolTipDiv'])["
				+ ObjectIndex + "]";
		String StepName = ObjectName.substring(0, ObjectName.lastIndexOf("_"));

		// Verify the Answer
		if (ExpectedAnswer.length() != 0) {
			// String TextPath = "("+Xpath+"//div[@class='answerViewType'])[1]";
			EP.CompareWithList(StepName + "Col" + Col + "_DT", ExpectedAnswer,
					EP.GetText(Xpath));
		}

		// Verify the Question
		EP.CompareText(StepName + "Col" + Col + "_TT", QuestionText,
				EP.GetToolTip(Xpath));
	}

	public String GetErrorMessage(int ErrorIndex) {
		String Text = "(//div[@class='errors']//ul/li)" + "[" + ErrorIndex
				+ "]";
		return EP.GetText(Text);
	}

	// ================================================General ECOS methods Ends
	// here================================================

	public void DemographicDataStatic(String ActionDone, String TabIndex) {
		String TabName = AppendHash(ActionDone);

		VerifyChapterName(ActionDone, TabIndex, EP.LangData("DemographicData"));// Verify
																				// chapter
																				// name
		VerifyChapterStatus(ActionDone, TabIndex,
				EP.ORData("#General_ChapterStatus_ST"));// Verify chapter status

		switch (switchCountryName) {

		case AUSTRALIA:
			AustraliaECRF AustraliaDR = new AustraliaECRF(EP);
			AustraliaDR.DemographicDataStatic(ActionDone, TabIndex);
			AustraliaDR = null;
			break;

		case ARGENTINA:
			ArgentinaECRF ArgentinaDR = new ArgentinaECRF(EP);
			ArgentinaDR.DemographicDataStatic(ActionDone, TabIndex);
			ArgentinaDR = null;
			break;

		case AUSTRIA:
			AustriaECRF AustriaDR = new AustriaECRF(EP);
			AustriaDR.DemographicDataStatic(ActionDone, TabIndex);
			AustriaDR = null;
			break;

		case TAIWAN:
			TaiwanECRF TaiwanDR = new TaiwanECRF(EP);
			TaiwanDR.DemographicDataStatic(ActionDone, TabIndex);
			TaiwanDR = null;
			break;

		case COLOMBIA:
			ColombiaECRF ColombiaDR = new ColombiaECRF(EP);
			ColombiaDR.DemographicDataStatic(ActionDone, TabIndex);
			ColombiaDR = null;
			break;

		case INDONESIA:
			IndonesiaECRF IndonesiaDR = new IndonesiaECRF(EP);
			IndonesiaDR.DemographicDataStatic(ActionDone, TabIndex);
			IndonesiaDR = null;
			break;

		case CANADA:
			CanadaECRF CanadaDR = new CanadaECRF(EP);
			CanadaDR.DemographicDataStatic(ActionDone, TabIndex);
			CanadaDR = null;
			break;

		case CZECHREPUBLIC:
			CzechRepublicECRF CzechDR = new CzechRepublicECRF(EP);
			CzechDR.DemographicDataStatic(ActionDone, TabIndex);
			CzechDR = null;
			break;

		case FINLAND:
			FinlandECRF FinlandDR = new FinlandECRF(EP);
			FinlandDR.DemographicDataStatic(ActionDone, TabIndex);
			FinlandDR = null;
			break;

		case FRANCE:
			FranceECRF FranceDR = new FranceECRF(EP);
			FranceDR.DemographicDataStatic(ActionDone, TabIndex);
			FranceDR = null;
			break;

		case GREECE:
			GreeceECRF GreeceDR = new GreeceECRF(EP);
			GreeceDR.DemographicDataStatic(ActionDone, TabIndex);
			GreeceDR = null;
			break;

		case HUNGARY:
			HungaryECRF HungaryDR = new HungaryECRF(EP);
			HungaryDR.DemographicDataStatic(ActionDone, TabIndex);
			HungaryDR = null;
			break;

		case ICELAND:
			IcelandECRF IcelandDR = new IcelandECRF(EP);
			IcelandDR.DemographicDataStatic(ActionDone, TabIndex);
			IcelandDR = null;
			break;

		case ITALY:
			ItalyECRF ItalyDR = new ItalyECRF(EP);
			ItalyDR.DemographicDataStatic(ActionDone, TabIndex);
			ItalyDR = null;
			break;

		case KOREA:
			KoreaECRF KoreaDR = new KoreaECRF(EP);
			KoreaDR.DemographicDataStatic(ActionDone, TabIndex);
			KoreaDR = null;
			break;

		case NORWAY:
			NorwayECRF NorwayDR = new NorwayECRF(EP);
			NorwayDR.DemographicDataStatic(ActionDone, TabIndex);
			NorwayDR = null;
			break;

		case SLOVAKIA:
			SlovakiaECRF SlovakiaDR = new SlovakiaECRF(EP);
			SlovakiaDR.DemographicDataStatic(ActionDone, TabIndex);
			SlovakiaDR = null;
			break;

		case SPAIN:
			SpainECRF SpainDR = new SpainECRF(EP);
			SpainDR.DemographicDataStatic(ActionDone, TabIndex);
			SpainDR = null;
			break;

		case SWEDEN:
			SwedenECRF SwedenDR = new SwedenECRF(EP);
			SwedenDR.DemographicDataStatic(ActionDone, TabIndex);
			SwedenDR = null;
			break;

		case UNITEDKINGDOM:
			UnitedKingdomECRF UnitedKingdomDR = new UnitedKingdomECRF(EP);
			UnitedKingdomDR.DemographicDataStatic(ActionDone, TabIndex);
			UnitedKingdomDR = null;
			break;

		case MEXICO:
			MexicoECRF MexicoDR = new MexicoECRF(EP);
			MexicoDR.DemographicDataStatic(ActionDone, TabIndex);
			MexicoDR = null;
			break;
		// case CHINA:
		// case ARGENTINA:

		case GERMANY:

			// Static strings Verification
			EP.VerifyText(TabName + "DateOfBirth_ST");
			EP.VerifyText(TabName + "Gender_ST");

			EP.VerifyText(TabName + "Ethnicity_ST");
			// options for ethnicity
			EP.VerifyText(TabName + "Caucasian_ST");
			EP.VerifyText(TabName + "African_ST");
			EP.VerifyText(TabName + "Asian_ST");
			EP.VerifyText(TabName + "Other_ST");
			EP.VerifyText(TabName + "EthnicityNotKnown_ST");
			EP.VerifyText(TabName + "EthnicityNotAvailable_ST");

			EP.VerifyText(TabName + "GrowthHormoneIndication_ST");
			// options for growth hormone indication
			EP.VerifyText(TabName + "GrowthHormoneDefeciency_ST");
			EP.VerifyText(TabName + "TurnerSyndrome_ST");
			EP.VerifyText(TabName + "SmallforGestationalAge_ST");
			EP.VerifyText(TabName + "ChronicRenalFailure_ST");
			break;

		case SINGAPORE:
			SingaporeECRF SingaporeDR = new SingaporeECRF(EP);
			SingaporeDR.DemographicDataStatic(ActionDone, TabIndex);
			SingaporeDR = null;
			break;

		case UNITEDARABEMIRATES:
			UnitedArabEmiratesECRF UnitedArabEmiratesDR = new UnitedArabEmiratesECRF(
					EP);
			UnitedArabEmiratesDR.DemographicDataStatic(ActionDone, TabIndex);
			UnitedArabEmiratesDR = null;
			break;

		case SAUDIARABIA:
			SaudiArabiaECRF SaudiArabiaDR = new SaudiArabiaECRF(EP);
			SaudiArabiaDR.DemographicDataStatic(ActionDone, TabIndex);
			SaudiArabiaDR = null;
			break;

		default:
		}

	}

	public void DemographicDataDynamic(String ActionDone, String TabIndex,
			String RecoveringURL) {

		String TabName = AppendHash(ActionDone);
		switch (switchCountryName) {

		case AUSTRALIA:
			AustraliaECRF AustraliaDR = new AustraliaECRF(EP);
			AustraliaDR.DemographicDataDynamic(ActionDone, TabIndex);
			AustraliaDR = null;
			break;

		case AUSTRIA:
			AustriaECRF AustriaDR = new AustriaECRF(EP);
			AustriaDR.DemographicDataDynamic(ActionDone, TabIndex,
					RecoveringURL);
			AustriaDR = null;
			break;

		case ARGENTINA:
			ArgentinaECRF ArgentinaDR = new ArgentinaECRF(EP);
			ArgentinaDR.DemographicDataDynamic(ActionDone, TabIndex,
					RecoveringURL);
			ArgentinaDR = null;
			break;

		case CANADA:
			CanadaECRF CanadaDR = new CanadaECRF(EP);
			CanadaDR.DemographicDataDynamic(ActionDone, TabIndex);
			CanadaDR = null;
			break;

		case TAIWAN:
			TaiwanECRF TaiwanDR = new TaiwanECRF(EP);
			TaiwanDR.DemographicDataDynamic(ActionDone, TabIndex, RecoveringURL);
			TaiwanDR = null;
			break;

		case COLOMBIA:
			ColombiaECRF ColombiaDR = new ColombiaECRF(EP);
			ColombiaDR.DemographicDataDynamic(ActionDone, TabIndex,
					RecoveringURL);
			ColombiaDR = null;
			break;

		case INDONESIA:
			IndonesiaECRF IndonesiaDR = new IndonesiaECRF(EP);
			IndonesiaDR.DemographicDataDynamic(ActionDone, TabIndex,
					RecoveringURL);
			IndonesiaDR = null;
			break;

		case CZECHREPUBLIC:
			CzechRepublicECRF CzechDR = new CzechRepublicECRF(EP);
			CzechDR.DemographicDataDynamic(ActionDone, TabIndex);
			CzechDR = null;
			break;

		case FINLAND:
			FinlandECRF FinlandDR = new FinlandECRF(EP);
			FinlandDR.DemographicDataDynamic(ActionDone, TabIndex);
			FinlandDR = null;
			break;

		case FRANCE:
			FranceECRF FranceDR = new FranceECRF(EP);
			FranceDR.DemographicDataDynamic(ActionDone, TabIndex);
			FranceDR = null;
			break;

		case GREECE:
			GreeceECRF GreeceDR = new GreeceECRF(EP);
			GreeceDR.DemographicDataDynamic(ActionDone, TabIndex);
			GreeceDR = null;
			break;

		case HUNGARY:
			HungaryECRF HungaryDR = new HungaryECRF(EP);
			HungaryDR.DemographicDataDynamic(ActionDone, TabIndex);
			HungaryDR = null;
			break;

		case ICELAND:
			IcelandECRF IcelandDR = new IcelandECRF(EP);
			IcelandDR.DemographicDataDynamic(ActionDone, TabIndex);
			IcelandDR = null;
			break;

		case ITALY:
			ItalyECRF ItalyDR = new ItalyECRF(EP);
			ItalyDR.DemographicDataDynamic(ActionDone, TabIndex);
			ItalyDR = null;
			break;

		case KOREA:
			KoreaECRF KoreaDR = new KoreaECRF(EP);
			KoreaDR.DemographicDataDynamic(ActionDone, TabIndex);
			KoreaDR = null;
			break;

		case NORWAY:
			NorwayECRF NorwayDR = new NorwayECRF(EP);
			NorwayDR.DemographicDataDynamic(ActionDone, TabIndex, RecoveringURL);
			NorwayDR = null;
			break;

		case SLOVAKIA:
			SlovakiaECRF SlovakiaDR = new SlovakiaECRF(EP);
			SlovakiaDR.DemographicDataDynamic(ActionDone, TabIndex);
			SlovakiaDR = null;
			break;

		case SPAIN:
			SpainECRF SpainDR = new SpainECRF(EP);
			SpainDR.DemographicDataDynamic(ActionDone, TabIndex);
			SpainDR = null;
			break;

		case SWEDEN:
			SwedenECRF SwedenDR = new SwedenECRF(EP);
			SwedenDR.DemographicDataDynamic(ActionDone, TabIndex);
			SwedenDR = null;
			break;

		case UNITEDKINGDOM:
			UnitedKingdomECRF UnitedKingdomDR = new UnitedKingdomECRF(EP);
			UnitedKingdomDR.DemographicDataDynamic(ActionDone, TabIndex);
			UnitedKingdomDR = null;
			break;

		case SINGAPORE:
			SingaporeECRF SingaporeDR = new SingaporeECRF(EP);
			SingaporeDR.DemographicDataDynamic(ActionDone, TabIndex,
					RecoveringURL);
			SingaporeDR = null;
			break;

		case UNITEDARABEMIRATES:
			UnitedArabEmiratesECRF UnitedArabEmiratesDR = new UnitedArabEmiratesECRF(
					EP);
			UnitedArabEmiratesDR.DemographicDataDynamic(ActionDone, TabIndex,
					RecoveringURL);
			UnitedArabEmiratesDR = null;
			break;

		case SAUDIARABIA:
			SaudiArabiaECRF SaudiArabiaDR = new SaudiArabiaECRF(EP);
			SaudiArabiaDR.DemographicDataDynamic(ActionDone, TabIndex,
					RecoveringURL);
			SaudiArabiaDR = null;
			break;
		// case CHINA:
		// case ARGENTINA:
		case MEXICO:
			MexicoECRF MexicoDR = new MexicoECRF(EP);
			MexicoDR.DemographicDataDynamic(ActionDone, TabIndex, RecoveringURL);
			MexicoDR = null;
			break;

		case GERMANY:

			EP.CompareText("#General_Save_BN", EP.LangData("Save"),
					EP.GetText("#General_First_BN"));
			EP.CompareText("#General_Cancel_BN", EP.LangData("Cancel"),
					EP.GetText("#General_Second_BN"));

			// Ethnicity - Please specify
			EP.Rbtn_SetOption(TabName + "Other_RB", "ON");
			EP.VerifyText(TabName + "PleaseSpecify_ST");
			EP.Btn_Click("#General_First_BN");
			EP.VerifyText(TabName + "ValueOutOfRangeError_DT");
			EP.CompareText("DemographicFieldMandatoryError_DT",
					EP.LangData("FieldIsMandatory"), GetErrorMessage(1));

			// Status-Partial
			EP.Btn_Click("#General_First_BN");
			VerifyChapterStatus(ActionDone, TabIndex, EP.LangData("Partial"));

			// Status-Complete
			EP.Rbtn_SetOption(TabName + "Caucasian_RB", "ON");
			EP.Rbtn_SetOption(TabName + "TurnerSyndrome_RB", "ON");
			EP.Btn_Click("#General_First_BN");
			VerifyChapterStatus(ActionDone, TabIndex, EP.LangData("Complete"));

			// Success message
			VerifySuccessMessage(EP.LangData("DemographicData"));
			break;

		default:
		}
	}

	public void AuxologicalDataStatic(String ActionDone, String TabIndex) {

		String TabName = AppendHash(ActionDone);
		VerifyChapterName(ActionDone, TabIndex,
				EP.LangData("AuxologicalDataBaseline"));// Verify chapter name
		VerifyChapterStatus(ActionDone, TabIndex,
				EP.ORData("#General_ChapterStatus_ST"));// Verify chapter status
		switch (switchCountryName) {

		case AUSTRALIA:
			AustraliaECRF AustraliaDR = new AustraliaECRF(EP);
			AustraliaDR.AuxologicalDataStatic(ActionDone, TabIndex);
			AustraliaDR = null;
			break;

		case AUSTRIA:
			AustriaECRF AustriaDR = new AustriaECRF(EP);
			AustriaDR.AuxologicalDataStatic(ActionDone, TabIndex);
			AustriaDR = null;
			break;

		case ARGENTINA:
			ArgentinaECRF ArgentinaDR = new ArgentinaECRF(EP);
			ArgentinaDR.AuxologicalDataStatic(ActionDone, TabIndex);
			ArgentinaDR = null;
			break;

		case COLOMBIA:
			ColombiaECRF ColombiaDR = new ColombiaECRF(EP);
			ColombiaDR.AuxologicalDataStatic(ActionDone, TabIndex);
			ColombiaDR = null;
			break;

		case INDONESIA:
			IndonesiaECRF IndonesiaDR = new IndonesiaECRF(EP);
			IndonesiaDR.AuxologicalDataStatic(ActionDone, TabIndex);
			IndonesiaDR = null;
			break;

		case TAIWAN:
			TaiwanECRF TaiwanDR = new TaiwanECRF(EP);
			TaiwanDR.AuxologicalDataStatic(ActionDone, TabIndex);
			TaiwanDR = null;
			break;

		case CANADA:
			CanadaECRF CanadaDR = new CanadaECRF(EP);
			CanadaDR.AuxologicalDataStatic(ActionDone, TabIndex);
			CanadaDR = null;
			break;

		case CZECHREPUBLIC:
			CzechRepublicECRF CzechDR = new CzechRepublicECRF(EP);
			CzechDR.AuxologicalDataStatic(ActionDone, TabIndex);
			CzechDR = null;
			break;

		case FINLAND:
			FinlandECRF FinlandDR = new FinlandECRF(EP);
			FinlandDR.AuxologicalDataStatic(ActionDone, TabIndex);
			FinlandDR = null;
			break;

		case FRANCE:
			FranceECRF FranceDR = new FranceECRF(EP);
			FranceDR.AuxologicalDataStatic(ActionDone, TabIndex);
			FranceDR = null;
			break;

		case GREECE:
			GreeceECRF GreeceDR = new GreeceECRF(EP);
			GreeceDR.AuxologicalDataStatic(ActionDone, TabIndex);
			GreeceDR = null;
			break;

		case HUNGARY:
			HungaryECRF HungaryDR = new HungaryECRF(EP);
			HungaryDR.AuxologicalDataStatic(ActionDone, TabIndex);
			HungaryDR = null;
			break;

		case ICELAND:
			IcelandECRF IcelandDR = new IcelandECRF(EP);
			IcelandDR.AuxologicalDataStatic(ActionDone, TabIndex);
			IcelandDR = null;
			break;

		case ITALY:
			ItalyECRF ItalyDR = new ItalyECRF(EP);
			ItalyDR.AuxologicalDataStatic(ActionDone, TabIndex);
			ItalyDR = null;
			break;

		case KOREA:
			KoreaECRF KoreaDR = new KoreaECRF(EP);
			KoreaDR.AuxologicalDataStatic(ActionDone, TabIndex);
			KoreaDR = null;
			break;

		case NORWAY:
			NorwayECRF NorwayDR = new NorwayECRF(EP);
			NorwayDR.AuxologicalDataStatic(ActionDone, TabIndex);
			NorwayDR = null;
			break;

		case SLOVAKIA:
			SlovakiaECRF SlovakiaDR = new SlovakiaECRF(EP);
			SlovakiaDR.AuxologicalDataStatic(ActionDone, TabIndex);
			SlovakiaDR = null;
			break;

		case SPAIN:
			SpainECRF SpainDR = new SpainECRF(EP);
			SpainDR.AuxologicalDataStatic(ActionDone, TabIndex);
			SpainDR = null;
			break;

		case SWEDEN:
			SwedenECRF SwedenDR = new SwedenECRF(EP);
			SwedenDR.AuxologicalDataStatic(ActionDone, TabIndex);
			SwedenDR = null;
			break;

		case UNITEDKINGDOM:
			UnitedKingdomECRF UnitedKingdomDR = new UnitedKingdomECRF(EP);
			UnitedKingdomDR.AuxologicalDataStatic(ActionDone, TabIndex);
			UnitedKingdomDR = null;
			break;

		case SINGAPORE:
			SingaporeECRF SingaporeDR = new SingaporeECRF(EP);
			SingaporeDR.AuxologicalDataStatic(ActionDone, TabIndex);
			SingaporeDR = null;
			break;

		case UNITEDARABEMIRATES:
			UnitedArabEmiratesECRF UnitedArabEmiratesDR = new UnitedArabEmiratesECRF(
					EP);
			UnitedArabEmiratesDR.AuxologicalDataStatic(ActionDone, TabIndex);
			UnitedArabEmiratesDR = null;
			break;

		case SAUDIARABIA:
			SaudiArabiaECRF SaudiArabiaDR = new SaudiArabiaECRF(EP);
			SaudiArabiaDR.AuxologicalDataStatic(ActionDone, TabIndex);
			SaudiArabiaDR = null;
			break;

		case MEXICO:
			MexicoECRF MexicoDR = new MexicoECRF(EP);
			MexicoDR.AuxologicalDataStatic(ActionDone, TabIndex);
			MexicoDR = null;
			break;

		/*
		 * case CHINA: case ARGENTINA:
		 * 
		 * VerifyChapterHeading(ActionDone,TabIndex, 1,
		 * EP.LangData("AuxologicalInformationParents"));//Verify Chapter
		 * heading1 EP.VerifyText(TabName+"MothersHeightAT_ST");
		 * EP.VerifyText(TabName+"MothersHeightReported_ST");
		 * EP.VerifyText(TabName+"MothersHeightMeasured_ST");
		 * EP.VerifyText(TabName+"FathersHeight_ST");
		 * EP.VerifyText(TabName+"FathersHeightReported_ST");
		 * EP.VerifyText(TabName+"FathersHeightMeasured_ST");
		 * EP.VerifyText(TabName+"AdjustedMidParentalHeightAT_ST");
		 * 
		 * VerifyChapterHeading(ActionDone,TabIndex, 2,
		 * EP.LangData("AuxologicalInformationBaseline"));//Verify chapter
		 * heading2 EP.VerifyText(TabName+"GestationalAgeAT_ST");
		 * EP.VerifyText(TabName+"BirthLengthAT_ST");
		 * EP.VerifyText(TabName+"BirthWeightAT_ST");
		 * 
		 * if (!TabName.startsWith("#RecordOverview")){
		 * EP.VerifyTextContained(TabName+"MothersHeightUnit_ST");
		 * EP.VerifyTextContained(TabName+"FathersHeightUnit_ST");
		 * EP.VerifyTextContained(TabName+"AdjustedMidParentalHeightUnit_ST");
		 * EP.VerifyTextContained(TabName+"GestationalAgeUnitFR_ST");
		 * EP.VerifyTextContained(TabName+"BirthLengthUnit_ST");
		 * EP.VerifyTextContained(TabName+"BirthWeightUnitFR_ST");
		 * EP.VerifyText(TabName+"HeightBeforeGHTreatmentStartAT_ST");
		 * EP.VerifyTextContained
		 * (TabName+"HeightBeforeGHTreatmentStartunitAT_ST");
		 * EP.VerifyText(TabName+"GrowthVelocityBeforeGHTreatmentAT_ST");
		 * EP.VerifyTextContained
		 * (TabName+"GrowthVelocityBeforeGHTreatmentUnitAT_ST"); }else{
		 * EP.VerifyTextContained(TabName+"MothersHeightUnitAT_ST");
		 * EP.VerifyTextContained(TabName+"FathersHeightUnitAT_ST");
		 * EP.VerifyTextContained(TabName+"AdjustedMidParentalHeightUnitAT_ST");
		 * EP.VerifyTextContained(TabName+"GestationalAgeUnitAT_ST");
		 * EP.VerifyTextContained(TabName+"BirthLengthUnitAT_ST");
		 * EP.VerifyTextContained(TabName+"BirthWeightUnitAT_ST");
		 * EP.VerifyText(TabName+"HeightBeforeGHTreatmentStartAT_ST");
		 * EP.VerifyTextContained
		 * (TabName+"HeightBeforeGHTreatmentStartunitAT_ST");
		 * EP.VerifyText(TabName+"GrowthVelocityBeforeGHTreatmentAT_ST");
		 * EP.VerifyTextContained
		 * (TabName+"GrowthVelocityBeforeGHTreatmentUnitAT_ST");; } break;
		 */

		case GERMANY:

			VerifyChapterHeading(ActionDone, TabIndex, 1,
					EP.LangData("AuxologicalInformationParents"));// Verify
																	// Chapter
																	// heading1
			EP.VerifyText(TabName + "MothersHeightAT_ST");
			EP.VerifyText(TabName + "MothersHeightReported_ST");
			EP.VerifyText(TabName + "MothersHeightMeasured_ST");
			EP.VerifyText(TabName + "FathersHeight_ST");
			EP.VerifyText(TabName + "FathersHeightReported_ST");
			EP.VerifyText(TabName + "FathersHeightMeasured_ST");
			EP.VerifyText(TabName + "AdjustedMidParentalHeightAT_ST");

			VerifyChapterHeading(ActionDone, TabIndex, 2,
					EP.LangData("AuxologicalInformationBaseline"));// Verify
																	// chapter
																	// heading2
			EP.VerifyText(TabName + "GestationalAgeAT_ST");
			EP.VerifyText(TabName + "BirthLengthAT_ST");
			EP.VerifyText(TabName + "BirthWeightAT_ST");

			if (!TabName.startsWith("#RecordOverview")) {
				EP.VerifyTextContained(TabName + "MothersHeightUnit_ST");
				EP.VerifyTextContained(TabName + "FathersHeightUnit_ST");
				EP.VerifyTextContained(TabName
						+ "AdjustedMidParentalHeightUnit_ST");
				EP.VerifyTextContained(TabName + "GestationalAgeUnitFR_ST");
				EP.VerifyTextContained(TabName + "BirthLengthUnit_ST");
				EP.VerifyTextContained(TabName + "BirthWeightUnitFR_ST");
				EP.VerifyText(TabName + "HeightBeforeGHTreatmentStartAT_ST");
				EP.VerifyTextContained(TabName
						+ "HeightBeforeGHTreatmentStartunitAT_ST");
				EP.VerifyText(TabName + "GrowthVelocityBeforeGHTreatmentAT_ST");
				EP.VerifyTextContained(TabName
						+ "GrowthVelocityBeforeGHTreatmentUnitAT_ST");
			} else {
				EP.VerifyTextContained(TabName + "MothersHeightUnitAT_ST");
				EP.VerifyTextContained(TabName + "FathersHeightUnitAT_ST");
				EP.VerifyTextContained(TabName
						+ "AdjustedMidParentalHeightUnitAT_ST");
				EP.VerifyTextContained(TabName + "GestationalAgeUnitAT_ST");
				EP.VerifyTextContained(TabName + "BirthLengthUnitAT_ST");
				EP.VerifyTextContained(TabName + "BirthWeightUnitAT_ST");
				EP.VerifyText(TabName + "HeightBeforeGHTreatmentStartAT_ST");
				EP.VerifyTextContained(TabName
						+ "HeightBeforeGHTreatmentStartunitAT_ST");
				EP.VerifyText(TabName + "GrowthVelocityBeforeGHTreatmentAT_ST");
				EP.VerifyTextContained(TabName
						+ "GrowthVelocityBeforeGHTreatmentUnitAT_ST");
				;
			}
			break;

		default:

		}
	}

	public void AuxologicalDataDynamic(String ActionDone, String TabIndex,
			String RecoveringURL) {

		String TabName = AppendHash(ActionDone);

		switch (switchCountryName) {

		case AUSTRALIA:
			AustraliaECRF AustraliaDR = new AustraliaECRF(EP);
			AustraliaDR.AuxologicalDataDynamic(ActionDone, TabIndex);
			AustraliaDR = null;
			break;

		case AUSTRIA:
			AustriaECRF AustriaDR = new AustriaECRF(EP);
			AustriaDR.AuxologicalDataDynamic(ActionDone, TabIndex);
			AustriaDR = null;
			break;

		case ARGENTINA:
			ArgentinaECRF ArgentinaDR = new ArgentinaECRF(EP);
			ArgentinaDR.AuxologicalDataDynamic(ActionDone, TabIndex);
			ArgentinaDR = null;
			break;

		case INDONESIA:
			IndonesiaECRF IndonesiaDR = new IndonesiaECRF(EP);
			IndonesiaDR.AuxologicalDataDynamic(ActionDone, TabIndex);
			IndonesiaDR = null;
			break;

		case COLOMBIA:
			ColombiaECRF ColombiaDR = new ColombiaECRF(EP);
			ColombiaDR.AuxologicalDataDynamic(ActionDone, TabIndex);
			ColombiaDR = null;
			break;

		case CANADA:
			CanadaECRF CanadaDR = new CanadaECRF(EP);
			CanadaDR.AuxologicalDataDynamic(ActionDone, TabIndex);
			CanadaDR = null;
			break;

		case TAIWAN:
			TaiwanECRF TaiwanDR = new TaiwanECRF(EP);
			TaiwanDR.AuxologicalDataDynamic(ActionDone, TabIndex);
			TaiwanDR = null;
			break;

		case CZECHREPUBLIC:
			CzechRepublicECRF CzechDR = new CzechRepublicECRF(EP);
			CzechDR.AuxologicalDataDynamic(ActionDone, TabIndex);
			CzechDR = null;
			break;

		case FRANCE:
			FranceECRF FranceDR = new FranceECRF(EP);
			FranceDR.AuxologicalDataDynamic(ActionDone, TabIndex);
			FranceDR = null;
			break;

		case FINLAND:
			FinlandECRF FinlandDR = new FinlandECRF(EP);
			FinlandDR.AuxologicalDataDynamic(ActionDone, TabIndex);
			FinlandDR = null;
			break;

		case GREECE:
			GreeceECRF GreeceDR = new GreeceECRF(EP);
			GreeceDR.AuxologicalDataDynamic(ActionDone, TabIndex);
			GreeceDR = null;
			break;

		case HUNGARY:
			HungaryECRF HungaryDR = new HungaryECRF(EP);
			HungaryDR.AuxologicalDataDynamic(ActionDone, TabIndex);
			HungaryDR = null;
			break;

		case ICELAND:
			IcelandECRF IcelandDR = new IcelandECRF(EP);
			IcelandDR.AuxologicalDataDynamic(ActionDone, TabIndex);
			IcelandDR = null;
			break;

		case ITALY:
			ItalyECRF ItalyDR = new ItalyECRF(EP);
			ItalyDR.AuxologicalDataDynamic(ActionDone, TabIndex);
			ItalyDR = null;
			break;

		case KOREA:
			KoreaECRF KoreaDR = new KoreaECRF(EP);
			KoreaDR.AuxologicalDataDynamic(ActionDone, TabIndex);
			KoreaDR = null;
			break;

		case NORWAY:
			NorwayECRF NorwayDR = new NorwayECRF(EP);
			NorwayDR.AuxologicalDataDynamic(ActionDone, TabIndex);
			NorwayDR = null;
			break;

		case SLOVAKIA:
			SlovakiaECRF SlovakiaDR = new SlovakiaECRF(EP);
			SlovakiaDR.AuxologicalDataDynamic(ActionDone, TabIndex);
			SlovakiaDR = null;
			break;

		case SPAIN:
			SpainECRF SpainDR = new SpainECRF(EP);
			SpainDR.AuxologicalDataDynamic(ActionDone, TabIndex);
			SpainDR = null;
			break;

		case SWEDEN:
			SwedenECRF SwedenDR = new SwedenECRF(EP);
			SwedenDR.AuxologicalDataDynamic(ActionDone, TabIndex);
			SwedenDR = null;
			break;

		case UNITEDKINGDOM:
			UnitedKingdomECRF UnitedKingdomDR = new UnitedKingdomECRF(EP);
			UnitedKingdomDR.AuxologicalDataDynamic(ActionDone, TabIndex);
			UnitedKingdomDR = null;
			break;

		case MEXICO:
			MexicoECRF MexicoDR = new MexicoECRF(EP);
			MexicoDR.AuxologicalDataDynamic(ActionDone, TabIndex);
			MexicoDR = null;
			break;
		// case CHINA:
		// case ARGENTINA:
		case GERMANY:

			EP.CompareText("#General_ParentSave_BN", EP.LangData("Save"),
					EP.GetText("#General_First_BN"));
			EP.CompareText("#General_ParentCancel_BN", EP.LangData("Cancel"),
					EP.GetText("#General_Second_BN"));

			// Minimum Range Error
			EP.Edit_ClearValue(TabName + "MothersHeight_EB");
			EP.Wait(2);
			EP.Edit_SetValue(TabName + "MothersHeight_EB", MinValue);
			EP.Edit_ClearValue(TabName + "FathersHeight_EB");
			EP.Wait(2);
			EP.Edit_SetValueByJavaScript(TabName + "FathersHeight_EB", MinValue);
			EP.HideCursor();
			EP.WaitForPageload();
			EP.Edit_SetValue(TabName + "GestationalAge_EB", MinValue);
			EP.Edit_SetValue(TabName + "BirthLength_EB", MinValue);
			EP.Edit_SetValue(TabName + "BirthWeight_EB", MinValue);
			EP.Edit_SetValue(TabName + "HeightBeforeGH_EB", MinValue);
			EP.Edit_SetValue(TabName + "GrowthVelocityBeforeGH_EB", MinValue);
			EP.Btn_Click("#General_First_BN");
			EP.WaitForPageload();
			EP.VerifyText(TabName + "ValueOutOfRangeError_DT");
			EP.VerifyRegExp(TabName + "MothersHeightErrorMessage_DT",
					"\\d{1,3}");
			EP.VerifyRegExp(TabName + "FathersHeightErrorMessage_DT",
					"\\d{1,3}");
			EP.VerifyRegExp(TabName + "GestationalAgeErrorMessage_DT",
					"\\d{1,3}");
			EP.VerifyRegExp(TabName + "BirthLengthErrorMessage_DT", "\\d{1,3}");
			EP.VerifyRegExp(TabName + "BirthWeightErrorMessage_DT", "\\d{1,3}");
			EP.VerifyRegExp(TabName + "HeightBeforeGHErrorMsgAT_DT", "\\d{1,3}");
			EP.VerifyRegExp(TabName + "GrowthVelocityBeforGHErrorAT_DT");
			;

			// Incorrect Format Error- First set
			EP.Edit_ClearValue(TabName + "MothersHeight_EB");
			EP.Wait(2);
			EP.Edit_SetValue(TabName + "MothersHeight_EB", IncorrectFormat);
			EP.Edit_ClearValue(TabName + "FathersHeight_EB");
			EP.Wait(2);
			EP.Edit_SetValueByJavaScript(TabName + "FathersHeight_EB",
					IncorrectFormat);
			EP.Wait(2);
			EP.Edit_SetValue(TabName + "GestationalAge_EB", IncorrectFormat);
			EP.Edit_SetValue(TabName + "BirthLength_EB", IncorrectFormat);
			EP.Edit_SetValue(TabName + "BirthWeight_EB", IncorrectFormat);
			EP.Edit_SetValue(TabName + "HeightBeforeGH_EB", IncorrectFormat);
			EP.Edit_SetValue(TabName + "GrowthVelocityBeforeGH_EB",
					IncorrectFormat);
			EP.WaitForPageload();
			EP.Btn_Click("#General_First_BN");
			EP.VerifyText(TabName + "IncorrectFormatError_DT");
			EP.VerifyText(TabName + "MothersHeightFormatError_DT");
			EP.VerifyText(TabName + "FathersHeightFormatError_DT");
			EP.VerifyText(TabName + "GestationalAgeFormatError_DT");
			EP.VerifyText(TabName + "BirthLengthFormatError_DT");
			EP.VerifyText(TabName + "BirthWEightFormatErrorMessage_DT");
			EP.VerifyText(TabName + "HeightFormatErrorAT_DT");
			EP.VerifyText(TabName + "GrowthVelocityFormatErrorAT_DT");

			// Status - Complete
			EP.Edit_SetValue(TabName + "MothersHeight_EB", "165");
			EP.Edit_SetValueByJavaScript(TabName + "FathersHeight_EB", "165");
			EP.HideCursor();
			EP.Edit_SetValue(TabName + "GestationalAge_EB", "26");
			EP.Edit_SetValue(TabName + "BirthLength_EB", "20");
			EP.Edit_SetValue(TabName + "BirthWeight_EB", "600");
			EP.Wait(2);
			EP.Edit_SetValue(TabName + "HeightBeforeGH_EB", "60.1");
			EP.Edit_SetValue(TabName + "GrowthVelocityBeforeGH_EB", "1");
			EP.Btn_Click("#General_First_BN");
			if (EP.CompareText(Env.GetBrowserUsed(), "OP")) {
				EP.Wait(Env.ResponseTimeOut * 5); // Slow in Opera
				EP.WaitForPageload();
			}
			EP.WaitForPageload();
			VerifyChapterStatus(ActionDone, TabIndex, EP.LangData("Complete"));// Complete
																				// Legend

			// Verify Success Message
			VerifySuccessMessage(EP.LangData("AuxologicalDataBaseline"));
			break;

		case SINGAPORE:
			SingaporeECRF SingaporeDR = new SingaporeECRF(EP);
			SingaporeDR.AuxologicalDataDynamic(ActionDone, TabIndex);
			SingaporeDR = null;
			break;

		case UNITEDARABEMIRATES:
			UnitedArabEmiratesECRF UnitedArabEmiratesDR = new UnitedArabEmiratesECRF(
					EP);
			UnitedArabEmiratesDR.AuxologicalDataDynamic(ActionDone, TabIndex);
			UnitedArabEmiratesDR = null;
			break;

		case SAUDIARABIA:
			SaudiArabiaECRF SaudiArabiaDR = new SaudiArabiaECRF(EP);
			SaudiArabiaDR.AuxologicalDataDynamic(ActionDone, TabIndex);
			SaudiArabiaDR = null;
			break;

		default:
		}
	}

	public void PatientSupportProgramStatic(String ActionDone, String TabIndex) {
		VerifyChapterName(ActionDone, TabIndex,
				EP.LangData("PatientSupportProgram"));// Verify chapter name
		VerifyChapterStatus(ActionDone, TabIndex,
				EP.ORData("#General_ChapterStatus_ST"));// Verify chapter status
		switch (switchCountryName) {
		case UNITEDKINGDOM:
			UnitedKingdomECRF UnitedKingdomDR = new UnitedKingdomECRF(EP);
			UnitedKingdomDR.PatientSupportProgramStatic(ActionDone, TabIndex);
			UnitedKingdomDR = null;
			break;

		default:
		}
	}

	public void PatientSupportProgramDynamic(String ActionDone,
			String TabIndex, String RecoveringURL) {

		switch (switchCountryName) {

		case UNITEDKINGDOM:
			UnitedKingdomECRF UnitedKingdomDR = new UnitedKingdomECRF(EP);
			UnitedKingdomDR.PatientSupportProgramDynamic(ActionDone, TabIndex,
					RecoveringURL);
			UnitedKingdomDR = null;
			break;

		default:

		}
	}

	public void BaselineConcomitantMedicationStatic(String ActionDone,
			String TabIndex) {
		VerifyChapterName(ActionDone, TabIndex,
				EP.LangData("ConcomitantMedication"));// Verify chapter name
		VerifyChapterStatus(ActionDone, TabIndex,
				EP.ORData("#General_ChapterStatus_ST"));// Verify chapter status
		switch (switchCountryName) {
		case SWEDEN:
			SwedenECRF SwedenDR = new SwedenECRF(EP);
			SwedenDR.BaselineConcomitantMedicationStatic(ActionDone, TabIndex);
			SwedenDR = null;
			break;

		default:
		}
	}

	public void BaselineConcomitantMedicationDynamic(String ActionDone,
			String TabIndex, String RecoveringURL) {
		// Delete Record
		if (EP.ObjectExists("#General_Edit_IM")) {
			DeleteRecord("#General_Delete_IM", false);
			VerifyDeleteMessage(EP.LangData("ConcomitantMedication"));
		}
		switch (switchCountryName) {

		case SWEDEN:
			SwedenECRF SwedenDR = new SwedenECRF(EP);
			SwedenDR.BaselineConcomitantMedicationDynamic(ActionDone, TabIndex,
					RecoveringURL);
			SwedenDR = null;
			break;

		default:
		}
	}

	public void MedicalHistoryStatic(String ActionDone, String TabIndex) {
		String TabName = AppendHash(ActionDone);
		VerifyChapterName(ActionDone, TabIndex, EP.LangData("MedicalHistory"));// Verify
																				// chapter
																				// name
		VerifyChapterStatus(ActionDone, TabIndex,
				EP.ORData("#General_ChapterStatus_ST"));// Verify chapter status

		switch (switchCountryName) {

		case AUSTRALIA:
			AustraliaECRF AustraliaDR = new AustraliaECRF(EP);
			AustraliaDR.MedicalHistoryStatic(ActionDone, TabIndex);
			AustraliaDR = null;
			break;

		case AUSTRIA:
			AustriaECRF AustriaDR = new AustriaECRF(EP);
			AustriaDR.MedicalHistoryStatic(ActionDone, TabIndex);
			AustriaDR = null;
			break;

		case ARGENTINA:
			ArgentinaECRF ArgentinaDR = new ArgentinaECRF(EP);
			ArgentinaDR.MedicalHistoryStatic(ActionDone, TabIndex);
			ArgentinaDR = null;
			break;

		case TAIWAN:
			TaiwanECRF TaiwanDR = new TaiwanECRF(EP);
			TaiwanDR.MedicalHistoryStatic(ActionDone, TabIndex);
			TaiwanDR = null;
			break;

		case INDONESIA:
			IndonesiaECRF IndonesiaDR = new IndonesiaECRF(EP);
			IndonesiaDR.MedicalHistoryStatic(ActionDone, TabIndex);
			IndonesiaDR = null;
			break;

		case COLOMBIA:
			ColombiaECRF ColombiaDR = new ColombiaECRF(EP);
			ColombiaDR.MedicalHistoryStatic(ActionDone, TabIndex);
			ColombiaDR = null;
			break;

		case CANADA:
			CanadaECRF CanadaDR = new CanadaECRF(EP);
			CanadaDR.MedicalHistoryStatic(ActionDone, TabIndex);
			CanadaDR = null;
			break;

		case CZECHREPUBLIC:
			CzechRepublicECRF CzechDR = new CzechRepublicECRF(EP);
			CzechDR.MedicalHistoryStatic(ActionDone, TabIndex);
			CzechDR = null;
			break;

		case FINLAND:
			FinlandECRF FinlandDR = new FinlandECRF(EP);
			FinlandDR.MedicalHistoryStatic(ActionDone, TabIndex);
			FinlandDR = null;
			break;

		case FRANCE:
			FranceECRF FranceDR = new FranceECRF(EP);
			FranceDR.MedicalHistoryStatic(ActionDone, TabIndex);
			FranceDR = null;
			break;

		case GREECE:
			GreeceECRF GreeceDR = new GreeceECRF(EP);
			GreeceDR.MedicalHistoryStatic(ActionDone, TabIndex);
			GreeceDR = null;
			break;

		case HUNGARY:
			HungaryECRF HungaryDR = new HungaryECRF(EP);
			HungaryDR.MedicalHistoryStatic(ActionDone, TabIndex);
			HungaryDR = null;
			break;

		case ICELAND:
			IcelandECRF IcelandDR = new IcelandECRF(EP);
			IcelandDR.MedicalHistoryStatic(ActionDone, TabIndex);
			IcelandDR = null;
			break;

		case ITALY:
			ItalyECRF ItalyDR = new ItalyECRF(EP);
			ItalyDR.MedicalHistoryStatic(ActionDone, TabIndex);
			ItalyDR = null;
			break;

		case KOREA:
			KoreaECRF KoreaDR = new KoreaECRF(EP);
			KoreaDR.MedicalHistoryStatic(ActionDone, TabIndex);
			KoreaDR = null;
			break;

		case NORWAY:
			NorwayECRF NorwayDR = new NorwayECRF(EP);
			NorwayDR.MedicalHistoryStatic(ActionDone, TabIndex);
			NorwayDR = null;
			break;

		case SLOVAKIA:
			SlovakiaECRF SlovakiaDR = new SlovakiaECRF(EP);
			SlovakiaDR.MedicalHistoryStatic(ActionDone, TabIndex);
			SlovakiaDR = null;
			break;

		case SPAIN:
			SpainECRF SpainDR = new SpainECRF(EP);
			SpainDR.MedicalHistoryStatic(ActionDone, TabIndex);
			SpainDR = null;
			break;

		case SWEDEN:
			SwedenECRF SwedenDR = new SwedenECRF(EP);
			SwedenDR.MedicalHistoryStatic(ActionDone, TabIndex);
			SwedenDR = null;
			break;

		case UNITEDKINGDOM:
			UnitedKingdomECRF UnitedKingdomDR = new UnitedKingdomECRF(EP);
			UnitedKingdomDR.MedicalHistoryStatic(ActionDone, TabIndex);
			UnitedKingdomDR = null;
			break;

		case SINGAPORE:
			SingaporeECRF SingaporeDR = new SingaporeECRF(EP);
			SingaporeDR.MedicalHistoryStatic(ActionDone, TabIndex);
			SingaporeDR = null;
			break;

		case UNITEDARABEMIRATES:
			UnitedArabEmiratesECRF UnitedArabEmiratesDR = new UnitedArabEmiratesECRF(
					EP);
			UnitedArabEmiratesDR.MedicalHistoryStatic(ActionDone, TabIndex);
			UnitedArabEmiratesDR = null;
			break;

		case SAUDIARABIA:
			SaudiArabiaECRF SaudiArabiaDR = new SaudiArabiaECRF(EP);
			SaudiArabiaDR.MedicalHistoryStatic(ActionDone, TabIndex);
			SaudiArabiaDR = null;
			break;

		// case CHINA:
		case MEXICO:
			MexicoECRF MexicoDR = new MexicoECRF(EP);
			MexicoDR.MedicalHistoryStatic(ActionDone, TabIndex);
			MexicoDR = null;
			break;

		case GERMANY:

			VerifyChapterHeading(ActionDone, TabIndex, 1,
					EP.LangData("MedicalHistory"));

			// Medical History
			EP.VerifyText(TabName + "MedicalHistorySubTitleAT_ST");
			EP.VerifyText(TabName + "PrimaryCNSTumorAT_ST");
			EP.VerifyText(TabName + "SecondaryCNSTumorAT_ST");
			EP.VerifyText(TabName + "LeaukemiaAT_ST");
			EP.VerifyText(TabName + "LymphomaAT_ST");
			EP.VerifyText(TabName + "ThyroidStimulatingAT_ST");
			EP.VerifyText(TabName + "ACTHDefeciencyAT_ST");
			EP.VerifyText(TabName + "ADHDefeciencyAT_ST");
			EP.VerifyText(TabName + "GonadotropinDefeciencyAT_ST");
			EP.VerifyText(TabName + "OtherPleaseSpecifyMedicalHistoryAT_ST");
			EP.VerifyText(TabName + "NoneMedicalHistoryAT_ST");
			EP.VerifyText(TabName + "MedicalHistoryNotKnownCA_ST");
			EP.VerifyText(TabName + "MedicalHistoryNotAvailableCA_ST");

			// Is the subject receiving concomitant treatments?
			EP.VerifyText(TabName + "RadiationTherapyAT_ST");
			EP.VerifyText(TabName + "BoneMarrowTransplantationAT_ST");
			EP.VerifyText(TabName + "ImmunosuppresantUseAT_ST");
			EP.VerifyText(TabName + "ChemotherapyAT_ST");
			EP.VerifyText(TabName + "SurgeryNeurosurgeryAT_ST");
			EP.VerifyText(TabName + "OtherSurgeryAT_ST");
			EP.VerifyText(TabName + "SolidOrganTransplantAT_ST");
			EP.VerifyText(TabName + "DialysisAT_ST");
			EP.VerifyText(TabName + "GlucocorticoidsAT_ST");
			EP.VerifyText(TabName + "AndrogensAT_ST");
			EP.VerifyText(TabName + "EstrogensAT_ST");
			EP.VerifyText(TabName
					+ "OtherPleaseSpecifyConcomitantTreatmentAT_ST");
			EP.VerifyText(TabName + "NoneConcomitantTreatmentAT_ST");
			EP.VerifyText(TabName + "RecievingconcomitantNotKnownCA_ST");
			EP.VerifyText(TabName + "RecievingconcomitantNotAvailableCA_ST");
			if (!TabName.startsWith("#RecordOverview")) {
				EP.VerifyText(TabName + "ReceivingConcomitantTreatment_ST");
			} else {
				EP.VerifyText(TabName + "ReceivingConcomitantTreatmentAT_ST");
			}
			break;

		/*
		 * case ARGENTINA:
		 * 
		 * VerifyChapterHeading(ActionDone,TabIndex, 1,
		 * EP.LangData("MedicalHistory"));
		 * 
		 * EP.VerifyText(TabName+"MedicalHistorySubTitleAT_ST");
		 * EP.VerifyText(TabName+"PrimaryCNSTumorAT_ST");
		 * EP.VerifyText(TabName+"SecondaryCNSTumorAT_ST");
		 * EP.VerifyText(TabName+"LeaukemiaAT_ST");
		 * EP.VerifyText(TabName+"LymphomaAT_ST");
		 * EP.VerifyText(TabName+"ThyroidStimulatingAT_ST");
		 * EP.VerifyText(TabName+"ACTHDefeciencyAT_ST");
		 * EP.VerifyText(TabName+"ADHDefeciencyAT_ST");
		 * EP.VerifyText(TabName+"GonadotropinDefeciencyAT_ST");
		 * EP.VerifyText(TabName+"OtherPleaseSpecifyMedicalHistoryAT_ST");
		 * EP.VerifyText(TabName+"NoneMedicalHistoryAT_ST");
		 * 
		 * EP.VerifyText(TabName+"RadiationTherapyAT_ST");
		 * EP.VerifyText(TabName+"BoneMarrowTransplantationAT_ST");
		 * EP.VerifyText(TabName+"ImmunosuppresantUseAT_ST");
		 * EP.VerifyText(TabName+"ChemotherapyAT_ST");
		 * EP.VerifyText(TabName+"SurgeryNeurosurgeryAT_ST");
		 * EP.VerifyText(TabName+"OtherSurgeryAT_ST");
		 * EP.VerifyText(TabName+"SolidOrganTransplantAT_ST");
		 * EP.VerifyText(TabName+"DialysisAT_ST");
		 * EP.VerifyText(TabName+"GlucocorticoidsAT_ST");
		 * EP.VerifyText(TabName+"AndrogensAT_ST");
		 * EP.VerifyText(TabName+"EstrogensAT_ST");
		 * EP.VerifyText(TabName+"OtherPleaseSpecifyConcomitantTreatmentAT_ST");
		 * EP.VerifyText(TabName+"NoneConcomitantTreatmentAT_ST");
		 * 
		 * if (!TabName.startsWith("#RecordOverview")){
		 * EP.VerifyText(TabName+"ReceivingConcomitantTreatment_ST"); }else{
		 * EP.VerifyText(TabName+"ReceivingConcomitantTreatmentAT_ST"); } break;
		 */
		default:
		}
	}

	public void MedicalHistoryDynamic(String ActionDone, String TabIndex,
			String RecoveringURL) {
		String TabName = AppendHash(ActionDone);
		switch (switchCountryName) {

		case AUSTRALIA:
			AustraliaECRF AustraliaDR = new AustraliaECRF(EP);
			AustraliaDR.MedicalHistoryDynamic(ActionDone, TabIndex);
			AustraliaDR = null;
			break;

		case AUSTRIA:
			AustriaECRF AustriaDR = new AustriaECRF(EP);
			AustriaDR.MedicalHistoryDynamic(ActionDone, TabIndex);
			AustriaDR = null;
			break;

		case CANADA:
			CanadaECRF CanadaDR = new CanadaECRF(EP);
			CanadaDR.MedicalHistoryDynamic(ActionDone, TabIndex);
			CanadaDR = null;
			break;

		case ARGENTINA:
			ArgentinaECRF ArgentinaDR = new ArgentinaECRF(EP);
			ArgentinaDR.MedicalHistoryDynamic(ActionDone, TabIndex);
			ArgentinaDR = null;
			break;

		case INDONESIA:
			IndonesiaECRF IndonesiaDR = new IndonesiaECRF(EP);
			IndonesiaDR.MedicalHistoryDynamic(ActionDone, TabIndex);
			IndonesiaDR = null;
			break;

		case TAIWAN:
			TaiwanECRF TaiwanDR = new TaiwanECRF(EP);
			TaiwanDR.MedicalHistoryDynamic(ActionDone, TabIndex);
			TaiwanDR = null;
			break;

		case COLOMBIA:
			ColombiaECRF ColombiaDR = new ColombiaECRF(EP);
			ColombiaDR.MedicalHistoryDynamic(ActionDone, TabIndex);
			ColombiaDR = null;
			break;

		case CZECHREPUBLIC:
			CzechRepublicECRF CzechDR = new CzechRepublicECRF(EP);
			CzechDR.MedicalHistoryDynamic(ActionDone, TabIndex);
			CzechDR = null;
			break;

		case FINLAND:
			FinlandECRF FinlandDR = new FinlandECRF(EP);
			FinlandDR.MedicalHistoryDynamic(ActionDone, TabIndex);
			FinlandDR = null;
			break;

		case FRANCE:
			FranceECRF FranceDR = new FranceECRF(EP);
			FranceDR.MedicalHistoryDynamic(ActionDone, TabIndex);
			FranceDR = null;
			break;

		case GREECE:
			GreeceECRF GreeceDR = new GreeceECRF(EP);
			GreeceDR.MedicalHistoryDynamic(ActionDone, TabIndex);
			GreeceDR = null;
			break;

		case HUNGARY:
			HungaryECRF HungaryDR = new HungaryECRF(EP);
			HungaryDR.MedicalHistoryDynamic(ActionDone, TabIndex);
			HungaryDR = null;
			break;

		case ICELAND:
			IcelandECRF IcelandDR = new IcelandECRF(EP);
			IcelandDR.MedicalHistoryDynamic(ActionDone, TabIndex);
			IcelandDR = null;
			break;

		case ITALY:
			ItalyECRF ItalyDR = new ItalyECRF(EP);
			ItalyDR.MedicalHistoryDynamic(ActionDone, TabIndex);
			ItalyDR = null;
			break;

		case KOREA:
			KoreaECRF KoreaDR = new KoreaECRF(EP);
			KoreaDR.MedicalHistoryDynamic(ActionDone, TabIndex);
			KoreaDR = null;
			break;

		case NORWAY:
			NorwayECRF NorwayDR = new NorwayECRF(EP);
			NorwayDR.MedicalHistoryDynamic(ActionDone, TabIndex);
			NorwayDR = null;
			break;

		case SLOVAKIA:
			SlovakiaECRF SlovakiaDR = new SlovakiaECRF(EP);
			SlovakiaDR.MedicalHistoryDynamic(ActionDone, TabIndex);
			SlovakiaDR = null;
			break;

		case SPAIN:
			SpainECRF SpainDR = new SpainECRF(EP);
			SpainDR.MedicalHistoryDynamic(ActionDone, TabIndex);
			SpainDR = null;
			break;

		case SWEDEN:
			SwedenECRF SwedenDR = new SwedenECRF(EP);
			SwedenDR.MedicalHistoryDynamic(ActionDone, TabIndex);
			SwedenDR = null;
			break;

		case UNITEDKINGDOM:
			UnitedKingdomECRF UnitedKingdomDR = new UnitedKingdomECRF(EP);
			UnitedKingdomDR.MedicalHistoryDynamic(ActionDone, TabIndex);
			UnitedKingdomDR = null;
			break;

		case SINGAPORE:
			SingaporeECRF SingaporeDR = new SingaporeECRF(EP);
			SingaporeDR.MedicalHistoryDynamic(ActionDone, TabIndex);
			SingaporeDR = null;
			break;

		case UNITEDARABEMIRATES:
			UnitedArabEmiratesECRF UnitedArabEmiratesDR = new UnitedArabEmiratesECRF(
					EP);
			UnitedArabEmiratesDR.MedicalHistoryDynamic(ActionDone, TabIndex);
			UnitedArabEmiratesDR = null;
			break;

		case SAUDIARABIA:
			SaudiArabiaECRF SaudiArabiaDR = new SaudiArabiaECRF(EP);
			SaudiArabiaDR.MedicalHistoryDynamic(ActionDone, TabIndex);
			SaudiArabiaDR = null;
			break;

		// case CHINA:
		// case ARGENTINA:
		case MEXICO:
			MexicoECRF MexicoDR = new MexicoECRF(EP);
			MexicoDR.MedicalHistoryDynamic(ActionDone, TabIndex);
			MexicoDR = null;
			break;

		case GERMANY:

			EP.CompareText("#General_ParentSave_BN", EP.LangData("Save"),
					EP.GetText("#General_First_BN"));
			EP.CompareText("#General_ParentCancel_BN", EP.LangData("Cancel"),
					EP.GetText("#General_Second_BN"));

			// Mandatory field error
			EP.ChkBx_SetOption(TabName + "OtherSpecify_CB", "ON");
			EP.ChkBx_SetOption(TabName + "OtherSurgery_CB", "ON");
			EP.ChkBx_SetOption(TabName + "OtherPleaseSpecify_CB", "ON");
			EP.Btn_Click("#General_First_BN");
			EP.VerifyText(TabName + "ValueOutOfRangeError_DT");// value out of
																// range error
			EP.VerifyText(TabName + "FieldMandatoryError_DT");
			EP.VerifyText(TabName + "OtherSurgeryMandatory_DT");
			EP.VerifyText(TabName + "SurgerySpecifyMandatory_DT");

			// Status - Partial
			EP.ChkBx_SetOption(TabName + "RadiationTherapy_CB", "OFF");
			EP.Btn_Click("#General_First_BN");
			VerifyChapterStatus(ActionDone, TabIndex, EP.LangData("Partial"));// Status
																				// Legend
																				// -
																				// Partial

			// Success Message
			EP.ChkBx_SetOption(TabName + "OtherSpecify_CB", "OFF");
			EP.ChkBx_SetOption(TabName + "OtherSurgery_CB", "OFF");
			EP.ChkBx_SetOption(TabName + "OtherPleaseSpecify_CB", "OFF");
			EP.ChkBx_SetOption(TabName + "RadiationTherapy_CB", "ON");
			EP.ChkBx_SetOption(TabName + "PrimaryCNSTumorAT_CB", "ON");
			EP.Btn_Click("#General_First_BN");
			VerifySuccessMessage(EP.LangData("MedicalHistory"));

			// Status - Complete
			VerifyChapterStatus(ActionDone, TabIndex, EP.LangData("Complete"));// Status
																				// Legend
																				// -
																				// Complete
			break;

		default:
		}
	}

	public void GrowthHormoneStatic(String ActionDone, String TabIndex) {
		String TabName = AppendHash(ActionDone);
		VerifyChapterName(ActionDone, TabIndex,
				EP.LangData("GrowthHormoneHistory"));// Verify chapter name
		VerifyChapterStatus(ActionDone, TabIndex,
				EP.ORData("#General_ChapterStatus_ST"));// Verify chapter status

		switch (switchCountryName) {

		case AUSTRIA:
			AustriaECRF AustriaDR = new AustriaECRF(EP);
			AustriaDR.GrowthHormoneStatic(ActionDone, TabIndex);
			AustriaDR = null;
			break;

		case CANADA:
			CanadaECRF CanadaDR = new CanadaECRF(EP);
			CanadaDR.GrowthHormoneStatic(ActionDone, TabIndex);
			CanadaDR = null;
			break;

		case ARGENTINA:
			ArgentinaECRF ArgentinaDR = new ArgentinaECRF(EP);
			ArgentinaDR.GrowthHormoneStatic(ActionDone, TabIndex);
			ArgentinaDR = null;
			break;
		case CZECHREPUBLIC:
			CzechRepublicECRF CzechDR = new CzechRepublicECRF(EP);
			CzechDR.GrowthHormoneStatic(ActionDone, TabIndex);
			CzechDR = null;
			break;

		case TAIWAN:
			TaiwanECRF TaiwanDR = new TaiwanECRF(EP);
			TaiwanDR.GrowthHormoneStatic(ActionDone, TabIndex);
			TaiwanDR = null;
			break;

		case INDONESIA:
			IndonesiaECRF IndonesiaDR = new IndonesiaECRF(EP);
			IndonesiaDR.GrowthHormoneStatic(ActionDone, TabIndex);
			IndonesiaDR = null;
			break;

		case COLOMBIA:
			ColombiaECRF ColombiaDR = new ColombiaECRF(EP);
			ColombiaDR.GrowthHormoneStatic(ActionDone, TabIndex);
			ColombiaDR = null;
			break;

		case FINLAND:
			FinlandECRF FinlandDR = new FinlandECRF(EP);
			FinlandDR.GrowthHormoneStatic(ActionDone, TabIndex);
			FinlandDR = null;
			break;

		case FRANCE:
			FranceECRF FranceDR = new FranceECRF(EP);
			FranceDR.GrowthHormoneStatic(ActionDone, TabIndex);
			FranceDR = null;
			break;

		case GREECE:
			GreeceECRF GreeceDR = new GreeceECRF(EP);
			GreeceDR.GrowthHormoneStatic(ActionDone, TabIndex);
			GreeceDR = null;
			break;

		case HUNGARY:
			HungaryECRF HungaryDR = new HungaryECRF(EP);
			HungaryDR.GrowthHormoneStatic(ActionDone, TabIndex);
			HungaryDR = null;
			break;

		case ICELAND:
			IcelandECRF IcelandDR = new IcelandECRF(EP);
			IcelandDR.GrowthHormoneStatic(ActionDone, TabIndex);
			IcelandDR = null;
			break;

		case ITALY:
			ItalyECRF ItalyDR = new ItalyECRF(EP);
			ItalyDR.GrowthHormoneStatic(ActionDone, TabIndex);
			ItalyDR = null;
			break;

		case KOREA:
			KoreaECRF KoreaDR = new KoreaECRF(EP);
			KoreaDR.GrowthHormoneStatic(ActionDone, TabIndex);
			KoreaDR = null;
			break;

		case NORWAY:
			NorwayECRF NorwayDR = new NorwayECRF(EP);
			NorwayDR.GrowthHormoneStatic(ActionDone, TabIndex);
			NorwayDR = null;
			break;

		case SLOVAKIA:
			SlovakiaECRF SlovakiaDR = new SlovakiaECRF(EP);
			SlovakiaDR.GrowthHormoneStatic(ActionDone, TabIndex);
			SlovakiaDR = null;
			break;

		case SPAIN:
			SpainECRF SpainDR = new SpainECRF(EP);
			SpainDR.GrowthHormoneStatic(ActionDone, TabIndex);
			SpainDR = null;
			break;

		case SWEDEN:
			SwedenECRF SwedenDR = new SwedenECRF(EP);
			SwedenDR.GrowthHormoneStatic(ActionDone, TabIndex);
			SwedenDR = null;
			break;

		case UNITEDKINGDOM:
			UnitedKingdomECRF UnitedKingdomDR = new UnitedKingdomECRF(EP);
			UnitedKingdomDR.GrowthHormoneStatic(ActionDone, TabIndex);
			UnitedKingdomDR = null;
			break;

		case SINGAPORE:
			SingaporeECRF SingaporeDR = new SingaporeECRF(EP);
			SingaporeDR.GrowthHormoneStatic(ActionDone, TabIndex);
			SingaporeDR = null;
			break;

		case UNITEDARABEMIRATES:
			UnitedArabEmiratesECRF UnitedArabEmiratesDR = new UnitedArabEmiratesECRF(
					EP);
			UnitedArabEmiratesDR.GrowthHormoneStatic(ActionDone, TabIndex);
			UnitedArabEmiratesDR = null;
			break;

		case SAUDIARABIA:
			SaudiArabiaECRF SaudiArabiaDR = new SaudiArabiaECRF(EP);
			SaudiArabiaDR.GrowthHormoneStatic(ActionDone, TabIndex);
			SaudiArabiaDR = null;
			break;

		// case ARGENTINA:
		case MEXICO:
			MexicoECRF MexicoDR = new MexicoECRF(EP);
			MexicoDR.GrowthHormoneStatic(ActionDone, TabIndex);
			MexicoDR = null;
			break;

		case GERMANY:

			VerifyChapterHeading(ActionDone, TabIndex, 1,
					EP.LangData("XMLHistory"));// Verify chapter heading
			EP.VerifyTableHeader(TabName + "GrowthHormoneHistory_TE");

			if (TabName.startsWith("#RecordOverview")) {
				// Verify tooltip values in the table
				CheckTableText(TabName + "GrowthHormoneHistory_TE", 1, 1, 1,
						EP.LangData("SaizenUnitWithEasypod"),
						EP.ORData("#BaselineRecord_GrowthHormone_TE"));
				CheckTableText(TabName + "GrowthHormoneHistory_TE", 1, 1, 2,
						EP.LangData("GHName"),
						EP.ORData(TabName + "AddGrowthHormoneGHNameAT_DD"));
				CheckTableText(TabName + "GrowthHormoneHistory_TE", 1, 1, 3,
						EP.LangData("DailyDose"),
						EP.LangData("UnitsMgPerKgPerDay"));
				CheckTableText(TabName + "GrowthHormoneHistory_TE", 1, 1, 4,
						EP.LangData("StartOfTreatment"), EP.MonthShortAll());
				CheckTableText(TabName + "GrowthHormoneHistory_TE", 1, 1, 5,
						EP.LangData("FinishOfTreatmentSmall"),
						EP.MonthShortAll());
			}
			break;

		default:
		}
	}

	public void GrowthHormoneDynamic(String ActionDone, String TabIndex,
			String RecoveringURL) {

		String TabName = AppendHash(ActionDone);
		String PatientDOB = EP.TestData("ECOSPatientDOB");

		// Delete Record
		if (EP.ObjectExists("#General_Edit_IM")) {
			DeleteRecord("#General_Delete_IM", false);
			VerifyDeleteMessage(EP.LangData("GrowthHormoneHistory"));
		}
		switch (switchCountryName) {

		case UNITEDKINGDOM:
			UnitedKingdomECRF UnitedKingdomDR = new UnitedKingdomECRF(EP);
			UnitedKingdomDR.GrowthHormoneDynamic(ActionDone, TabIndex,
					RecoveringURL);
			UnitedKingdomDR = null;
			break;

		case CANADA:
			CanadaECRF CanadaDR = new CanadaECRF(EP);
			CanadaDR.GrowthHormoneDynamic(ActionDone, TabIndex, RecoveringURL);
			CanadaDR = null;
			break;

		case ARGENTINA:
			ArgentinaECRF ArgentinaDR = new ArgentinaECRF(EP);
			ArgentinaDR.GrowthHormoneDynamic(ActionDone, TabIndex,
					RecoveringURL);
			ArgentinaDR = null;
			break;

		case AUSTRIA:
			AustriaECRF AustriaDR = new AustriaECRF(EP);
			AustriaDR.GrowthHormoneDynamic(ActionDone, TabIndex, RecoveringURL);
			AustriaDR = null;
			break;

		case INDONESIA:
			IndonesiaECRF IndonesiaDR = new IndonesiaECRF(EP);
			IndonesiaDR.GrowthHormoneDynamic(ActionDone, TabIndex,
					RecoveringURL);
			IndonesiaDR = null;
			break;

		case COLOMBIA:
			ColombiaECRF ColombiaDR = new ColombiaECRF(EP);
			ColombiaDR
					.GrowthHormoneDynamic(ActionDone, TabIndex, RecoveringURL);
			ColombiaDR = null;
			break;

		case TAIWAN:
			TaiwanECRF TaiwanDR = new TaiwanECRF(EP);
			TaiwanDR.GrowthHormoneDynamic(ActionDone, TabIndex, RecoveringURL);
			TaiwanDR = null;
			break;

		case SPAIN:
			SpainECRF SpainDR = new SpainECRF(EP);
			SpainDR.GrowthHormoneDynamic(ActionDone, TabIndex, RecoveringURL);
			SpainDR = null;
			break;

		case SLOVAKIA:
			SlovakiaECRF SlovakiaDR = new SlovakiaECRF(EP);
			SlovakiaDR
					.GrowthHormoneDynamic(ActionDone, TabIndex, RecoveringURL);
			SlovakiaDR = null;
			break;

		case FRANCE:
			FranceECRF FranceDR = new FranceECRF(EP);
			FranceDR.GrowthHormoneDynamic(ActionDone, TabIndex, RecoveringURL);
			FranceDR = null;
			break;

		case CZECHREPUBLIC:
			CzechRepublicECRF CzechDR = new CzechRepublicECRF(EP);
			CzechDR.GrowthHormoneDynamic(ActionDone, TabIndex, RecoveringURL);
			CzechDR = null;
			break;

		case NORWAY:
			NorwayECRF NorwayDR = new NorwayECRF(EP);
			NorwayDR.GrowthHormoneDynamic(ActionDone, TabIndex, RecoveringURL);
			NorwayDR = null;
			break;

		case ITALY:
			ItalyECRF ItalyDR = new ItalyECRF(EP);
			ItalyDR.GrowthHormoneDynamic(ActionDone, TabIndex, RecoveringURL);
			ItalyDR = null;
			break;

		case GREECE:
			GreeceECRF GreeceDR = new GreeceECRF(EP);
			GreeceDR.GrowthHormoneDynamic(ActionDone, TabIndex, RecoveringURL);
			GreeceDR = null;
			break;

		case SWEDEN:
			SwedenECRF SwedenDR = new SwedenECRF(EP);
			SwedenDR.GrowthHormoneDynamic(ActionDone, TabIndex, RecoveringURL);
			SwedenDR = null;
			break;

		case SINGAPORE:
			SingaporeECRF SingaporeDR = new SingaporeECRF(EP);
			SingaporeDR.GrowthHormoneDynamic(ActionDone, TabIndex,
					RecoveringURL);
			SingaporeDR = null;
			break;

		case UNITEDARABEMIRATES:
			UnitedArabEmiratesECRF UnitedArabEmiratesDR = new UnitedArabEmiratesECRF(
					EP);
			UnitedArabEmiratesDR.GrowthHormoneDynamic(ActionDone, TabIndex,
					RecoveringURL);
			UnitedArabEmiratesDR = null;
			break;

		case SAUDIARABIA:
			SaudiArabiaECRF SaudiArabiaDR = new SaudiArabiaECRF(EP);
			SaudiArabiaDR.GrowthHormoneDynamic(ActionDone, TabIndex,
					RecoveringURL);
			SaudiArabiaDR = null;
			break;

		case MEXICO:
			MexicoECRF MexicoDR = new MexicoECRF(EP);
			MexicoDR.GrowthHormoneDynamic(ActionDone, TabIndex, RecoveringURL);
			MexicoDR = null;
			break;

		case GERMANY:

			EP.CompareText("Add_BN", EP.LangData("Add"),
					EP.GetText("#General_First_BN"));
			// Click Add button
			EP.Btn_Click("#General_First_BN");

			// Static verification
			EP.VerifyText(TabName + "GrowthHormoneHistory_LK");
			EP.VerifyText(TabName + "GrowthHormoneAddAT_ST");
			EP.VerifyText(TabName + "SaizenUnitWithEasypod_ST");
			EP.VerifyText(TabName + "Yes_ST");
			EP.VerifyText(TabName + "No_ST");
			EP.VerifyText(TabName + "AddGrowthHormoneGHNameAT_ST");
			EP.CompareDropDownValues(TabName + "AddGrowthHormoneGHNameAT_DD");
			EP.VerifyText(TabName + "AddGrowthHormoneDailyDoseAT_ST");
			EP.VerifyTextContained(TabName + "DailyDoseUnit_ST");
			EP.VerifyText(TabName + "AddGrowthHormoneStartOfTreatmentAT_ST");
			EP.VerifyToolTip(TabName + "StartOfTreatment_TT");// GrowthHormoneCalendar_TT
			EP.VerifyText(TabName + "AddGrowthHormoneFinishOfTreatmentAT_ST");
			EP.VerifyToolTip(TabName + "FinishOfTreatmentCalendar_TT");
			EP.CompareText("Save_BN", EP.LangData("Save"),
					EP.GetText("#General_AddRecordSave_BN"));
			EP.CompareText("Cancel_BN", EP.LangData("Cancel"),
					EP.GetText("#General_AddRecordCancel_BN"));

			EP.VerifyPageLayout();
			if (Env.GetBrowserUsed().startsWith("SA")
					|| Env.GetBrowserUsed().startsWith("OP")) {
				EP.NavigateTo(RecoveringURL);
				EP.WaitForPageload();
				EP.ShowTab(TabIndex);
				EP.Btn_Click("#General_First_BN");
			}

			// Verify Partial Legend
			if (Env.LanguageUsed.toUpperCase().startsWith("GERMAN")
					|| Env.LanguageUsed.toUpperCase().startsWith("SWEDISH")) {
				EP.Edit_SetValue(TabName + "AddGrowthHormoneDailyDoseAT_EB",
						"0,01");
			} else {
				EP.Edit_SetValue(TabName + "AddGrowthHormoneDailyDoseAT_EB",
						"0.01");
			}

			EP.DrpDwn_SelectIndex(TabName + "AddGrowthHormoneGHNameAT_DD",
					EP.RandomValue(1, 4));
			EP.Btn_Click("#General_AddRecordSave_BN");
			EP.Btn_Click("#General_AddRecordSave_BN");
			VerifyChapterStatus(ActionDone, TabIndex, EP.LangData("Partial"));

			EP.Lnk_Click("#General_Edit_IM");
			EP.VerifyText("#General_Edit_ST");

			// Mandatory field error and Greater Than and TFD < TSD
			EP.DrpDwn_SelectIndex(TabName + "AddGrowthHormoneGHNameAT_DD", 5);
			EP.Edit_SetValueByJavaScript(TabName
					+ "AddGrowthHormoneDailyDoseAT_EB", "9841");
			String TreatmentStartDate = EP.Date_AddDays(
					EP.TestData("ECOSPatientDOB"), 2, Calendar.MONTH);
			EP.SelectDate(TabName + "StartOfTreatment_TT", TreatmentStartDate);
			EP.SelectDate(TabName + "FinishOfTreatmentCalendar_TT",
					EP.TestData("ECOSPatientDOB"));
			EP.Btn_Click("#General_AddRecordSave_BN");
			EP.VerifyText(TabName + "ValueOutOfRangeError_DT");// Verify other,
																// Specify error
			EP.VerifyText(TabName + "GrowthHormoneFieldMandatoryErrorSE_DT");// Other
																				// GH
																				// Name
			EP.VerifyRegExp(TabName + "DailyDoseError_DT");// Daily Dose Error
			EP.VerifyRegExp(TabName + "StartTreatmentDateError_DT");
			EP.VerifyRegExp(TabName + "FinishTreamentDateErrorAT_DT");

			// Incorrect Format
			EP.DrpDwn_SelectIndex(TabName + "AddGrowthHormoneGHNameAT_DD",
					EP.RandomValue(1, 4));
			EP.Edit_SetValue(TabName + "DailyDose_EB", IncorrectFormat);
			EP.SelectDate(TabName + "StartOfTreatment_TT",
					EP.Date_AddDays(PatientDOB, -1, Calendar.DATE));
			EP.SelectDate(TabName + "FinishOfTreatmentCalendar_TT",
					EP.Date_AddDays(PatientDOB, 2, Calendar.MONTH));
			EP.Btn_Click("#General_AddRecordSave_BN");
			EP.VerifyText(TabName + "IncorrectFormatError_DT"); // changed to UK
			EP.VerifyText(TabName + "InvalidError_DT");
			EP.Btn_Click("#General_AddRecordSave_BN");
			EP.VerifyText(TabName + "TreatmentStartDateBirthDateErrorAT_DT");
			EP.VerifyText(TabName + "TreatmentFinishDateConsentDateErrorAT_DT");
			EP.Btn_Click("#General_AddRecordSave_BN");

			// Validating Error message "Date must be before the last visit"
			EP.Lnk_Click("#General_Edit_IM");
			EP.Edit_SetValue(TabName + "DailyDose_EB", "0.01");
			EP.SelectDate(TabName + "StartOfTreatment_TT", EP.Date_AddDays(
					EP.GetTimeStamp("dd-MMM-yyyy"), -1, Calendar.DATE));
			EP.SelectDate(TabName + "FinishOfTreatmentCalendar_TT",
					EP.GetTimeStamp("dd-MMM-yyyy"));
			EP.Btn_Click("#General_AddRecordSave_BN");
			EP.VerifyText(TabName + "TSDAfterLastVisitDateErrorCZ_DT");

			// Less than shows same error as greater than
			// Not required

			// Success Message
			if (Env.LanguageUsed.toUpperCase().startsWith("GERMAN")
					|| Env.LanguageUsed.toUpperCase().startsWith("SWEDISH")) {
				EP.Edit_SetValue(TabName + "DailyDose_EB", "0,01");
			} else {
				EP.Edit_SetValue(TabName + "DailyDose_EB", "0.01");
			}

			EP.SelectDate(TabName + "StartOfTreatment_TT",
					EP.Date_AddDays(PatientDOB, 2, Calendar.DATE));
			EP.SelectDate(TabName + "FinishOfTreatmentCalendar_TT",
					EP.Date_AddDays(PatientDOB, 15, Calendar.DATE));
			EP.Btn_Click("#General_AddRecordSave_BN");
			VerifySuccessMessage(EP.LangData("GrowthHormoneHistory"));

			// ToolTip
			EP.VerifyToolTip("#General_Edit_IM");
			EP.VerifyToolTip("#General_Delete_IM");

			VerifyChapterStatus(ActionDone, TabIndex, EP.LangData("Complete"));// Verify
																				// Complete
																				// Legend

			// Verify tooltip values in the table
			CheckTableText(TabName + "GrowthHormoneHistory_TE", 1, 1, 1,
					EP.LangData("SaizenUnitWithEasypod"),
					EP.ORData("#BaselineRecord_GrowthHormone_TE"));
			CheckTableText(TabName + "GrowthHormoneHistory_TE", 1, 1, 2,
					EP.LangData("GHName"),
					EP.ORData(TabName + "AddGrowthHormoneGHNameAT_DD"));
			CheckTableText(TabName + "GrowthHormoneHistory_TE", 1, 1, 3,
					EP.LangData("DailyDose"), EP.LangData("UnitsMgPerKgPerDay"));
			CheckTableText(TabName + "GrowthHormoneHistory_TE", 1, 1, 4,
					EP.LangData("StartOfTreatment"), EP.MonthShortAll());
			CheckTableText(TabName + "GrowthHormoneHistory_TE", 1, 1, 5,
					EP.LangData("FinishOfTreatmentSmall"), EP.MonthShortAll());
			break;

		/*
		 * case ARGENTINA:
		 * 
		 * EP.CompareText("Add_BN", EP.LangData("Add"),
		 * EP.GetText("#General_First_BN")); //Click Add button
		 * EP.Btn_Click("#General_First_BN");
		 * 
		 * //Static verification
		 * EP.VerifyText(TabName+"GrowthHormoneHistory_LK");
		 * EP.VerifyText(TabName+"GrowthHormoneAddAT_ST");
		 * EP.VerifyText(TabName+"SaizenUnitWithEasypod_ST");
		 * EP.VerifyText(TabName+"Yes_ST"); EP.VerifyText(TabName+"No_ST");
		 * EP.VerifyText(TabName+"AddGrowthHormoneGHNameAT_ST");
		 * EP.CompareDropDownValues(TabName+"AddGrowthHormoneGHNameAT_DD");
		 * EP.VerifyText(TabName+"AddGrowthHormoneDailyDoseAT_ST");
		 * EP.VerifyTextContained(TabName+"DailyDoseUnit_ST");
		 * EP.VerifyText(TabName+"AddGrowthHormoneStartOfTreatmentAT_ST");
		 * EP.VerifyToolTip
		 * (TabName+"StartOfTreatment_TT");//GrowthHormoneCalendar_TT
		 * EP.VerifyText(TabName+"AddGrowthHormoneFinishOfTreatmentAT_ST");
		 * EP.VerifyToolTip(TabName+"FinishOfTreatmentCalendar_TT");
		 * EP.CompareText("Save_BN", EP.LangData("Save"),
		 * EP.GetText("#General_AddRecordSave_BN")); EP.CompareText("Cancel_BN",
		 * EP.LangData("Cancel"), EP.GetText("#General_AddRecordCancel_BN"));
		 * 
		 * EP.VerifyPageLayout(); if (Env.GetBrowserUsed().startsWith("SA") ||
		 * Env.GetBrowserUsed().startsWith("OP")){ EP.NavigateTo(RecoveringURL);
		 * EP.WaitForPageload(); EP.ShowTab(TabIndex);
		 * EP.Btn_Click("#General_First_BN"); }
		 * 
		 * //Verify Partial Legend
		 * if(Env.LanguageUsed.toUpperCase().startsWith("GERMAN") ||
		 * Env.LanguageUsed.toUpperCase().startsWith("SWEDISH")){
		 * EP.Edit_SetValue(TabName+"AddGrowthHormoneDailyDoseAT_EB","0,01"); }
		 * else {
		 * EP.Edit_SetValue(TabName+"AddGrowthHormoneDailyDoseAT_EB","0.01"); }
		 * 
		 * EP.DrpDwn_SelectIndex(TabName+"AddGrowthHormoneGHNameAT_DD",
		 * EP.RandomValue(1, 4)); EP.Btn_Click("#General_AddRecordSave_BN");
		 * EP.Btn_Click("#General_AddRecordSave_BN");
		 * VerifyChapterStatus(ActionDone,TabIndex, EP.LangData("Partial"));
		 * 
		 * EP.Lnk_Click("#General_Edit_IM"); EP.VerifyText("#General_Edit_ST");
		 * //Mandatory field error and Greater Than and TFD < TSD
		 * EP.DrpDwn_SelectIndex(TabName+"AddGrowthHormoneGHNameAT_DD", 5);
		 * EP.Edit_SetValueByJavaScript
		 * (TabName+"AddGrowthHormoneDailyDoseAT_EB", "9841"); String
		 * TreatmentStartDateIS=EP.Date_AddDays(EP.TestData("ECOSPatientDOB"),
		 * 2, Calendar.MONTH);
		 * EP.SelectDate(TabName+"StartOfTreatment_TT",TreatmentStartDateIS);
		 * EP.SelectDate(TabName+"FinishOfTreatmentCalendar_TT",EP.TestData(
		 * "ECOSPatientDOB")); EP.Btn_Click("#General_AddRecordSave_BN");
		 * EP.VerifyText(TabName+"ValueOutOfRangeError_DT");//Verify other,
		 * Specify error
		 * EP.VerifyText(TabName+"GrowthHormoneFieldMandatoryErrorSE_DT"
		 * );//Other GH Name
		 * EP.VerifyRegExp(TabName+"DailyDoseError_DT");//Daily Dose Error
		 * EP.VerifyRegExp(TabName+"StartTreatmentDateError_DT");
		 * EP.VerifyRegExp(TabName+"FinishTreamentDateErrorSE_DT");
		 * 
		 * //Incorrect Format
		 * EP.DrpDwn_SelectIndex(TabName+"AddGrowthHormoneGHNameAT_DD",
		 * EP.RandomValue(1, 4)); EP.Edit_SetValue(TabName+"DailyDose_EB",
		 * IncorrectFormat);
		 * EP.SelectDate(TabName+"StartOfTreatment_TT",EP.Date_AddDays
		 * (PatientDOB, -1, Calendar.DATE));
		 * EP.SelectDate(TabName+"FinishOfTreatmentCalendar_TT"
		 * ,EP.Date_AddDays(PatientDOB, 2, Calendar.MONTH));
		 * EP.Btn_Click("#General_AddRecordSave_BN");
		 * EP.VerifyText(TabName+"IncorrectFormatError_DT"); //changed to UK
		 * EP.VerifyText(TabName+"InvalidError_DT");
		 * EP.Btn_Click("#General_AddRecordSave_BN");
		 * EP.VerifyText(TabName+"TreatmentStartDateBirthDateErrorAT_DT");
		 * EP.VerifyText(TabName+"TreatmentFinishDateConsentDateErrorAT_DT");
		 * EP.Btn_Click("#General_AddRecordSave_BN");
		 * 
		 * //Validating Error message "Date must be before the last visit"
		 * EP.Lnk_Click("#General_Edit_IM");
		 * EP.Edit_SetValue(TabName+"DailyDose_EB", "0.01");
		 * EP.SelectDate(TabName
		 * +"StartOfTreatment_TT",EP.Date_AddDays(EP.GetTimeStamp
		 * ("dd-MMM-yyyy"), -1, Calendar.DATE));
		 * EP.SelectDate(TabName+"FinishOfTreatmentCalendar_TT"
		 * ,EP.GetTimeStamp("dd-MMM-yyyy"));
		 * EP.Btn_Click("#General_AddRecordSave_BN");
		 * EP.VerifyText(TabName+"TSDAfterLastVisitDateErrorIS_DT");
		 * 
		 * //Less than shows same error as greater than //Not required
		 * 
		 * //Success Message
		 * if(Env.LanguageUsed.toUpperCase().startsWith("GERMAN") ||
		 * Env.LanguageUsed.toUpperCase().startsWith("SWEDISH")){
		 * EP.Edit_SetValue(TabName+"DailyDose_EB", "0,01"); } else {
		 * EP.Edit_SetValue(TabName+"DailyDose_EB", "0.01"); }
		 * EP.SelectDate(TabName
		 * +"StartOfTreatment_TT",EP.Date_AddDays(PatientDOB, 2,
		 * Calendar.DATE));
		 * EP.SelectDate(TabName+"FinishOfTreatmentCalendar_TT",
		 * EP.Date_AddDays(PatientDOB, 15, Calendar.DATE));
		 * EP.Btn_Click("#General_AddRecordSave_BN");
		 * VerifySuccessMessage(EP.LangData("GrowthHormoneHistory"));
		 * 
		 * //ToolTip EP.VerifyToolTip("#General_Edit_IM");
		 * EP.VerifyToolTip("#General_Delete_IM");
		 * 
		 * VerifyChapterStatus(ActionDone,TabIndex,
		 * EP.LangData("Complete"));//Verify Complete Legend
		 * 
		 * //Verify table values EP.CompareWithList(TabName+"GrowthHormone_TE",
		 * EP.ORData("#General_YesorNo_DT"),
		 * EP.GetText(TabName+"GrowthHormone_TE"));
		 * EP.VerifyMonth(TabName+"GrowthHormoneMonth_TE");
		 * EP.VerifyTextContained(TabName+"GrowthHormone_TE",
		 * EP.ORData(TabName+"DailyDoseUnit_ST"),
		 * EP.GetText(TabName+"GrowthHormone_TE"));
		 * EP.CompareWithList(TabName+"GrowthHormone_TE",
		 * EP.ORData(TabName+"AddGrowthHormoneGHNameAT_DD"),
		 * EP.GetText(TabName+"GrowthHormone_TE")); break;
		 */

		case AUSTRALIA:
			AustraliaECRF AustraliaDR = new AustraliaECRF(EP);
			AustraliaDR.GrowthHormoneDynamic(ActionDone, TabIndex,
					RecoveringURL);
			AustraliaDR = null;
			break;

		case ICELAND:
			IcelandECRF IcelandDR = new IcelandECRF(EP);
			IcelandDR.GrowthHormoneDynamic(ActionDone, TabIndex, RecoveringURL);
			IcelandDR = null;
			break;

		case FINLAND:
			FinlandECRF FinlandDR = new FinlandECRF(EP);
			FinlandDR.GrowthHormoneDynamic(ActionDone, TabIndex, RecoveringURL);
			FinlandDR = null;
			break;

		// case CANADA:
		// EP.CompareText("Add_BN", EP.LangData("Add"),
		// EP.GetText("#General_First_BN"));
		// EP.Btn_Click("#General_First_BN"); //Click Add button
		//
		// //Static verification
		// EP.VerifyText(TabName+"GrowthHormoneHistory_LK");
		// EP.VerifyText(TabName+"GrowthHormoneAddAT_ST");
		// EP.VerifyText(TabName+"SaizenUnitWithEasypod_ST");
		// EP.VerifyText(TabName+"Yes_ST");
		// EP.VerifyText(TabName+"No_ST");
		// EP.VerifyText(TabName+"AddGrowthHormoneGHNameAT_ST");
		// EP.CompareDropDownValues(TabName+"AddGrowthHormoneGHNameCA_DD");
		// EP.VerifyText(TabName+"AddGrowthHormoneDailyDoseAT_ST");
		// EP.VerifyTextContained(TabName+"DailyDoseUnit_ST");
		// EP.VerifyText(TabName+"AddGrowthHormoneStartOfTreatmentAT_ST");
		// EP.VerifyToolTip(TabName+"StartOfTreatment_TT");//GrowthHormoneCalendar_TT
		// EP.VerifyText(TabName+"AddGrowthHormoneFinishOfTreatmentAT_ST");
		// EP.VerifyToolTip(TabName+"FinishOfTreatmentCalendar_TT");
		// EP.CompareText("Save_BN", EP.LangData("Save"),
		// EP.GetText("#General_AddRecordSave_BN"));
		// EP.CompareText("Cancel_BN", EP.LangData("Cancel"),
		// EP.GetText("#General_AddRecordCancel_BN"));
		//
		// EP.VerifyPageLayout();
		// if (Env.GetBrowserUsed().startsWith("SA") ||
		// Env.GetBrowserUsed().startsWith("OP")){
		// EP.NavigateTo(RecoveringURL);
		// EP.WaitForPageload();
		// EP.ShowTab(TabIndex);
		// EP.Btn_Click("#General_First_BN");
		// }
		//
		// //Click save when all fields are blank
		// EP.Btn_Click("#General_AddRecordSave_BN");
		// EP.VerifyText("#BaselineRecord_ValueOutOfRangeError_DT");
		// EP.CompareText("GHName_MandatoryError",
		// EP.LangData("FieldIsMandatory"), GetErrorMessage(1));
		// EP.CompareText("DailyDose_MandatoryError",
		// EP.LangData("FieldIsMandatory"), GetErrorMessage(2));
		// EP.CompareText("StartofTreatment_MandatoryError",
		// EP.LangData("FieldIsMandatory"), GetErrorMessage(3));
		// EP.CompareText("FinishofTreatment_MandatoryError",
		// EP.LangData("FieldIsMandatory"), GetErrorMessage(4));
		//
		// //Verify Partial Legend
		// EP.Edit_SetValue(TabName+"AddGrowthHormoneDailyDoseAT_EB",EP.Converter("0.01"));
		// EP.DrpDwn_SelectIndex(TabName+"AddGrowthHormoneGHNameAT_DD", 5);
		// EP.Btn_Click("#General_AddRecordSave_BN");
		// VerifyChapterStatus(ActionDone,TabIndex, EP.LangData("Partial"));
		//
		// EP.Lnk_Click("#General_Edit_IM");
		// EP.VerifyText("#General_Edit_ST");
		//
		// //Mandatory field error and Greater Than and TFD < TSD
		// EP.DrpDwn_SelectIndex(TabName+"AddGrowthHormoneGHNameAT_DD", 5);
		// EP.Edit_SetValueByJavaScript(TabName+"AddGrowthHormoneDailyDoseAT_EB",
		// "9841");
		// String
		// TreatmentStartDat=EP.Date_AddDays(EP.TestData("ECOSPatientDOB"), 2,
		// Calendar.MONTH);
		// EP.SelectDate(TabName+"StartOfTreatment_TT",TreatmentStartDat );
		// EP.SelectDate(TabName+"FinishOfTreatmentCalendar_TT",EP.TestData("ECOSPatientDOB"));
		// EP.Btn_Click("#General_AddRecordSave_BN");
		// EP.VerifyText(TabName+"ValueOutOfRangeError_DT");//Verify other,
		// Specify error
		// EP.CompareText("GHNameOtherSpecify_MandatoryError",
		// EP.LangData("FieldIsMandatory"), GetErrorMessage(1));
		// EP.VerifyRegExpression("DailyDoseErrorRangeError",
		// EP.LangData("RangeValue"), GetErrorMessage(2));
		// EP.VerifyRegExpression("StartofTreatmentError_DT",
		// EP.LangData("ValueLessThan"), GetErrorMessage(3));
		// EP.VerifyRegExpression("FinishofTreatmentError_DT",
		// EP.LangData("ValueGreaterThan"), GetErrorMessage(4));
		//
		// //Incorrect Format
		// EP.DrpDwn_SelectIndex(TabName+"AddGrowthHormoneGHNameAT_DD",
		// EP.RandomValue(1, 4));
		// EP.Edit_SetValue(TabName+"DailyDose_EB", IncorrectFormat);
		// EP.SelectDate(TabName+"StartOfTreatment_TT",EP.Date_AddDays(PatientDOB,
		// -1, Calendar.DATE));
		// EP.SelectDate(TabName+"FinishOfTreatmentCalendar_TT",EP.Date_AddDays(PatientDOB,
		// 2, Calendar.MONTH));
		// EP.Btn_Click("#General_AddRecordSave_BN");
		// EP.VerifyText(TabName+"IncorrectFormatError_DT");
		// EP.CompareText("DailyDose_MandatoryError",
		// EP.LangData("IncorrectFormat"), GetErrorMessage(1));
		// EP.Btn_Click("#General_AddRecordSave_BN");
		// EP.VerifyText(TabName+"ValueOutOfRangeError_DT");
		// EP.CompareText("StartofTreatment_AfterDOBError",
		// EP.LangData("DateError"), GetErrorMessage(1));
		// EP.CompareText("FinishofTreatment_BeforeCSDError",
		// EP.LangData("TFDGreaterThanConsentDate"), GetErrorMessage(2));
		// EP.ClearStaticScreenshot();
		//
		// //Less than shows same error as greater than
		// //Not required
		//
		// //Success Message
		// EP.Edit_SetValue(TabName+"DailyDose_EB", EP.Converter("0.01"));
		// EP.SelectDate(TabName+"StartOfTreatment_TT",EP.Date_AddDays(PatientDOB,
		// 2, Calendar.DATE));
		// EP.SelectDate(TabName+"FinishOfTreatmentCalendar_TT",EP.Date_AddDays(PatientDOB,
		// 15, Calendar.DATE));
		// EP.Btn_Click("#General_AddRecordSave_BN");
		// VerifySuccessMessage(EP.LangData("GrowthHormoneHistory"));
		//
		// //ToolTip
		// EP.VerifyToolTip("#General_Edit_IM");
		// EP.VerifyToolTip("#General_Delete_IM");
		//
		// VerifyChapterStatus(ActionDone,TabIndex,
		// EP.LangData("Complete"));//Verify Complete Legend
		//
		// //Verify tooltip values in the table
		// CheckTableText(TabName+"GrowthHormoneHistory_TE", 1, 1, 1,
		// EP.LangData("SaizenUnitWithEasypod"),
		// EP.ORData("#BaselineRecord_GrowthHormone_TE"));
		// CheckTableText(TabName+"GrowthHormoneHistory_TE", 1, 1, 2,
		// EP.LangData("GHName"),
		// EP.ORData(TabName+"AddGrowthHormoneGHNameAT_DD"));
		// CheckTableText(TabName+"GrowthHormoneHistory_TE", 1, 1, 3,
		// EP.LangData("DailyDose"), EP.LangData("UnitsMgPerKgPerDay"));
		// CheckTableText(TabName+"GrowthHormoneHistory_TE", 1, 1, 4,
		// EP.LangData("StartOfTreatment"), EP.MonthShortAll());
		// CheckTableText(TabName+"GrowthHormoneHistory_TE", 1, 1, 5,
		// EP.LangData("FinishOfTreatmentCAPS"), EP.MonthShortAll());
		//
		// break;

		case KOREA:
			KoreaECRF KoreaDR = new KoreaECRF(EP);
			KoreaDR.GrowthHormoneDynamic(ActionDone, TabIndex, RecoveringURL);
			KoreaDR = null;
			break;

		case HUNGARY:
			HungaryECRF HungaryDR = new HungaryECRF(EP);
			HungaryDR.GrowthHormoneDynamic(ActionDone, TabIndex, RecoveringURL);
			HungaryDR = null;
			break;

		default:

		}
	}

	public void SocioeconomicStatic(String ActionDone, String TabIndex) {
		String TabName = AppendHash(ActionDone);
		VerifyChapterName(ActionDone, TabIndex,
				EP.LangData("SocioEconomicInformation"));// Verify chapter name
		VerifyChapterStatus(ActionDone, TabIndex,
				EP.ORData("#General_ChapterStatus_ST"));// Verify chapter status
		switch (switchCountryName) {
		case UNITEDKINGDOM:
			UnitedKingdomECRF UnitedKingdomDR = new UnitedKingdomECRF(EP);
			UnitedKingdomDR.SocioeconomicStatic(ActionDone, TabIndex);
			UnitedKingdomDR = null;
			break;

		case CANADA:
			CanadaECRF CanadaDR = new CanadaECRF(EP);
			CanadaDR.SocioeconomicStatic(ActionDone, TabIndex);
			CanadaDR = null;
			break;

		case ARGENTINA:
			ArgentinaECRF ArgentinaDR = new ArgentinaECRF(EP);
			ArgentinaDR.SocioeconomicStatic(ActionDone, TabIndex);
			ArgentinaDR = null;
			break;

		case AUSTRIA:
			AustriaECRF AustriaDR = new AustriaECRF(EP);
			AustriaDR.SocioeconomicStatic(ActionDone, TabIndex);
			AustriaDR = null;
			break;

		case INDONESIA:
			IndonesiaECRF IndonesiaDR = new IndonesiaECRF(EP);
			IndonesiaDR.SocioeconomicStatic(ActionDone, TabIndex);
			IndonesiaDR = null;
			break;

		case SPAIN:
			SpainECRF SpainDR = new SpainECRF(EP);
			SpainDR.SocioeconomicStatic(ActionDone, TabIndex);
			SpainDR = null;
			break;

		case COLOMBIA:
			ColombiaECRF ColombiaDR = new ColombiaECRF(EP);
			ColombiaDR.SocioeconomicStatic(ActionDone, TabIndex);
			ColombiaDR = null;
			break;

		case TAIWAN:
			TaiwanECRF TaiwanDR = new TaiwanECRF(EP);
			TaiwanDR.SocioeconomicStatic(ActionDone, TabIndex);
			TaiwanDR = null;
			break;

		case SLOVAKIA:
			SlovakiaECRF SlovakiaDR = new SlovakiaECRF(EP);
			SlovakiaDR.SocioeconomicStatic(ActionDone, TabIndex);
			SlovakiaDR = null;
			break;

		case FRANCE:
			FranceECRF FranceDR = new FranceECRF(EP);
			FranceDR.SocioeconomicStatic(ActionDone, TabIndex);
			FranceDR = null;
			break;

		case CZECHREPUBLIC:
			CzechRepublicECRF CzechDR = new CzechRepublicECRF(EP);
			CzechDR.SocioeconomicStatic(ActionDone, TabIndex);
			CzechDR = null;
			break;

		case NORWAY:
			NorwayECRF NorwayDR = new NorwayECRF(EP);
			NorwayDR.SocioeconomicStatic(ActionDone, TabIndex);
			NorwayDR = null;
			break;

		case ITALY:
			ItalyECRF ItalyDR = new ItalyECRF(EP);
			ItalyDR.SocioeconomicStatic(ActionDone, TabIndex);
			ItalyDR = null;
			break;

		case GREECE:
			GreeceECRF GreeceDR = new GreeceECRF(EP);
			GreeceDR.SocioeconomicStatic(ActionDone, TabIndex);
			GreeceDR = null;
			break;

		case SINGAPORE:
			SingaporeECRF SingaporeDR = new SingaporeECRF(EP);
			SingaporeDR.SocioeconomicStatic(ActionDone, TabIndex);
			SingaporeDR = null;
			break;

		case UNITEDARABEMIRATES:
			UnitedArabEmiratesECRF UnitedArabEmiratesDR = new UnitedArabEmiratesECRF(
					EP);
			UnitedArabEmiratesDR.SocioeconomicStatic(ActionDone, TabIndex);
			UnitedArabEmiratesDR = null;

		case SAUDIARABIA:
			SaudiArabiaECRF SaudiArabiaDR = new SaudiArabiaECRF(EP);
			SaudiArabiaDR.SocioeconomicStatic(ActionDone, TabIndex);
			SaudiArabiaDR = null;
			break;
		// case CHINA:
		// case ARGENTINA:
		case MEXICO:
			MexicoECRF MexicoDR = new MexicoECRF(EP);
			MexicoDR.SocioeconomicStatic(ActionDone, TabIndex);
			MexicoDR = null;
			break;

		case GERMANY:

			VerifyChapterHeading(ActionDone, TabIndex, 1,
					EP.LangData("SocioEconomicInformation"));// Verify chapter
																// heading
			EP.VerifyTableHeader(TabName + "FamilyContext_TE");// Verify table
																// header
			if (!TabName.startsWith("#RecordOverview")) {

			} else {
				// Verify tooltip values in the table
				CheckTableText(TabName + "FamilyContextFR_TE", 1, 1, 1,
						EP.LangData("Date"), EP.MonthShortAll());
				CheckTableText(
						TabName + "FamilyContextFR_TE",
						1,
						2,
						1,
						EP.LangData("Parent"),
						EP.ORData("#BaselineRecord_AddSocioeconomicParentAT_DD"));
				CheckTableText(
						TabName + "FamilyContextFR_TE",
						1,
						3,
						1,
						EP.LangData("ParentMaritalStatus"),
						EP.ORData("#BaselineRecord_ParentMaritalStatusColumnAT_ST"));
				CheckTableText(
						TabName + "FamilyContextFR_TE",
						1,
						4,
						1,
						EP.LangData("ParentEmploymentStatus"),
						EP.ORData("#RecordOverview_ParentEmploymentStatusColumn_ST"));
				CheckTableText(TabName + "FamilyContextFR_TE", 1, 5, 1,
						EP.LangData("EducationStatusOfPerson"),
						EP.ORData("#RecordOverview_EducationStatusColumn_ST"));

			}
			break;

		case SWEDEN:
			SwedenECRF SwedenDR = new SwedenECRF(EP);
			SwedenDR.SocioeconomicStatic(ActionDone, TabIndex);
			SwedenDR = null;
			break;

		case KOREA:
			KoreaECRF KoreaDR = new KoreaECRF(EP);
			KoreaDR.SocioeconomicStatic(ActionDone, TabIndex);
			KoreaDR = null;
			break;

		case AUSTRALIA:
			AustraliaECRF AustraliaDR = new AustraliaECRF(EP);
			AustraliaDR.SocioeconomicStatic(ActionDone, TabIndex);
			AustraliaDR = null;
			break;

		case ICELAND:
			IcelandECRF IcelandDR = new IcelandECRF(EP);
			IcelandDR.SocioeconomicStatic(ActionDone, TabIndex);
			IcelandDR = null;
			break;

		case FINLAND:
			FinlandECRF FinlandDR = new FinlandECRF(EP);
			FinlandDR.SocioeconomicStatic(ActionDone, TabIndex);
			FinlandDR = null;
			break;

		case HUNGARY:
			HungaryECRF HungaryDR = new HungaryECRF(EP);
			HungaryDR.SocioeconomicStatic(ActionDone, TabIndex);
			HungaryDR = null;
			break;

		default:
		}
	}

	public void SocioeconomicDynamic(String ActionDone, String TabIndex,
			String RecoveringURL) {
		String TabName = AppendHash(ActionDone);
		// Delete the Record
		if (EP.ObjectExists("#General_Edit_IM")) {
			DeleteRecord("#General_Delete_IM", false);
			VerifyDeleteMessage(EP.LangData("SocioEconomicInformation"));
		}

		switch (switchCountryName) {

		case UNITEDKINGDOM:
			UnitedKingdomECRF UnitedKingdomDR = new UnitedKingdomECRF(EP);
			UnitedKingdomDR.SocioeconomicDynamic(ActionDone, TabIndex,
					RecoveringURL);
			UnitedKingdomDR = null;
			break;

		case AUSTRIA:
			AustriaECRF AustriaDR = new AustriaECRF(EP);
			AustriaDR.SocioeconomicDynamic(ActionDone, TabIndex, RecoveringURL);
			AustriaDR = null;
			break;

		case COLOMBIA:
			ColombiaECRF ColombiaDR = new ColombiaECRF(EP);
			ColombiaDR
					.SocioeconomicDynamic(ActionDone, TabIndex, RecoveringURL);
			ColombiaDR = null;
			break;

		case ARGENTINA:
			ArgentinaECRF ArgentinaDR = new ArgentinaECRF(EP);
			ArgentinaDR.SocioeconomicDynamic(ActionDone, TabIndex,
					RecoveringURL);
			ArgentinaDR = null;
			break;

		case INDONESIA:
			IndonesiaECRF IndonesiaDR = new IndonesiaECRF(EP);
			IndonesiaDR.SocioeconomicDynamic(ActionDone, TabIndex,
					RecoveringURL);
			IndonesiaDR = null;
			break;

		case SPAIN:
			SpainECRF SpainDR = new SpainECRF(EP);
			SpainDR.SocioeconomicDynamic(ActionDone, TabIndex, RecoveringURL);
			SpainDR = null;
			break;

		case TAIWAN:
			TaiwanECRF TaiwanDR = new TaiwanECRF(EP);
			TaiwanDR.SocioeconomicDynamic(ActionDone, TabIndex, RecoveringURL);
			TaiwanDR = null;
			break;

		case FRANCE:
			FranceECRF FranceDR = new FranceECRF(EP);
			FranceDR.SocioeconomicDynamic(ActionDone, TabIndex, RecoveringURL);
			FranceDR = null;
			break;

		case CZECHREPUBLIC:
			CzechRepublicECRF CzechDR = new CzechRepublicECRF(EP);
			CzechDR.SocioeconomicDynamic(ActionDone, TabIndex, RecoveringURL);
			CzechDR = null;
			break;

		case NORWAY:
			NorwayECRF NorwayDR = new NorwayECRF(EP);
			NorwayDR.SocioeconomicDynamic(ActionDone, TabIndex, RecoveringURL);
			NorwayDR = null;
			break;

		case ITALY:
			ItalyECRF ItalyDR = new ItalyECRF(EP);
			ItalyDR.SocioeconomicDynamic(ActionDone, TabIndex, RecoveringURL);
			ItalyDR = null;
			break;

		case GREECE:
			GreeceECRF GreeceDR = new GreeceECRF(EP);
			GreeceDR.SocioeconomicDynamic(ActionDone, TabIndex, RecoveringURL);
			GreeceDR = null;
			break;

		case SLOVAKIA:
			SlovakiaECRF SlovakiaDR = new SlovakiaECRF(EP);
			SlovakiaDR
					.SocioeconomicDynamic(ActionDone, TabIndex, RecoveringURL);
			SlovakiaDR = null;
			break;

		case SWEDEN:
			SwedenECRF SwedenDR = new SwedenECRF(EP);
			SwedenDR.SocioeconomicDynamic(ActionDone, TabIndex, RecoveringURL);
			SwedenDR = null;
			break;

		case SINGAPORE:
			SingaporeECRF SingaporeDR = new SingaporeECRF(EP);
			SingaporeDR.SocioeconomicDynamic(ActionDone, TabIndex,
					RecoveringURL);
			SingaporeDR = null;
			break;

		case UNITEDARABEMIRATES:
			UnitedArabEmiratesECRF UnitedArabEmiratesDR = new UnitedArabEmiratesECRF(
					EP);
			UnitedArabEmiratesDR.SocioeconomicDynamic(ActionDone, TabIndex,
					RecoveringURL);
			UnitedArabEmiratesDR = null;
			break;

		case SAUDIARABIA:
			SaudiArabiaECRF SaudiArabiaDR = new SaudiArabiaECRF(EP);
			SaudiArabiaDR.SocioeconomicDynamic(ActionDone, TabIndex,
					RecoveringURL);
			SaudiArabiaDR = null;
			break;

		// case CHINA:
		// case ARGENTINA:
		case MEXICO:
			MexicoECRF MexicoDR = new MexicoECRF(EP);
			MexicoDR.SocioeconomicDynamic(ActionDone, TabIndex, RecoveringURL);
			MexicoDR = null;
			break;

		case GERMANY:

			EP.CompareText("SocioEconomic_Add_BN", EP.LangData("Add"),
					EP.GetText("#General_First_BN"));
			EP.Btn_Click("#General_First_BN");

			// Static Verification
			EP.VerifyText(TabName + "SocioEconomicInformation_LK");
			EP.VerifyText(TabName + "TitleAdd_ST");
			EP.VerifyText(TabName + "Date_ST");
			EP.VerifyToolTip(TabName + "VisitDate_IM");
			EP.VerifyText(TabName + "Parent_ST");
			EP.CompareDropDownValues(TabName + "AddSocioeconomicParentAT_DD");

			// Parent MArital Status
			EP.VerifyText(TabName + "ParentMaritalStatusAT_ST");
			EP.VerifyText(TabName + "MarriedCohabiting_ST");
			EP.VerifyText(TabName + "SeparatedDivorced_ST");
			EP.VerifyText(TabName + "SingleWidowed_ST");
			EP.VerifyText(TabName + "ParentMaritalNotKnown_ST");
			EP.VerifyText(TabName + "ParentMaritalNotAvailable_ST");
			// EP.VerifyText(TabName+"NotGiven_ST"); //not appearing for sweden

			// Parent Employment status
			EP.VerifyText(TabName + "ParentsEmploymentStatusAT_ST");
			EP.VerifyText(TabName + "Employed_ST");
			EP.VerifyText(TabName + "HomeMaker_ST");
			EP.VerifyText(TabName + "Student_ST");
			EP.VerifyText(TabName + "UnemployedRetired_ST");
			EP.VerifyText(TabName + "OtherPleaseSpecify_ST");
			EP.VerifyText(TabName + "ParentEmploymentNotKnown_ST");
			EP.VerifyText(TabName + "ParentEmploymentNotAvailable_ST");

			// Education status of [erson performing the majority of injections
			EP.VerifyText(TabName + "EducationStatusOfPerson_ST");
			EP.CompareDropDownValues(TabName + "EducationStatusOfPerson_DD");
			EP.CompareText("Save_BN", EP.LangData("Save"),
					EP.GetText("#General_AddRecordSave_BN"));
			EP.CompareText("Cancel_BN", EP.LangData("Cancel"),
					EP.GetText("#General_AddRecordCancel_BN"));

			EP.VerifyPageLayout();
			if (Env.GetBrowserUsed().startsWith("SA")
					|| Env.GetBrowserUsed().startsWith("OP")) {
				EP.NavigateTo(RecoveringURL);
				EP.WaitForPageload();
			}
			if (!EP.ObjectExists(TabName + "EducationStatusOfPerson_DD")) {
				EP.ShowTab(TabIndex);
				EP.Btn_Click("#General_First_BN");// Click on Add Button
			}

			// Verify Partial Legend
			EP.SelectDate(TabName + "VisitDate_IM", EP.Date_AddDays(
					EP.TestData("ECOSPatientDOB"), 2, Calendar.YEAR));
			EP.DrpDwn_SelectIndex(TabName + "EducationStatusOfPerson_DD", 3);
			EP.Btn_Click("#General_AddRecordSave_BN");
			EP.Btn_Click("#General_AddRecordSave_BN");
			VerifyChapterStatus(ActionDone, TabIndex, EP.LangData("Partial"));// Verify
																				// Partial
																				// Legend

			// Verify Success Message
			VerifySuccessMessage(EP.LangData("SocioEconomicInformation"));

			// Verify "edit" and "delete" icon tooltips in the table
			EP.VerifyToolTip("#General_Edit_IM");
			EP.VerifyToolTip("#General_Delete_IM");

			// Verify Mandatory field error
			EP.Lnk_Click("#General_Edit_IM");
			EP.VerifyText("#General_Edit_ST");
			EP.ChkBx_SetOption(TabName + "ParentEmploymentOtherSpecify_CB",
					"ON");
			EP.DrpDwn_SelectIndex(TabName + "EducationStatusOfPerson_DD", 3);
			EP.Btn_Click("#General_AddRecordSave_BN");// ERROR
			EP.VerifyText(TabName + "ValueOutOfRangeError_DT");
			EP.VerifyText(TabName + "EmploymentStatusMandatoryError_DT");
			EP.VerifyText(TabName + "EducationStatusMandatoryError_DT");

			// Status - Complete
			EP.DrpDwn_SelectIndex(TabName + "AddSocioeconomicParentAT_DD",
					EP.RandomValue(1, 3));
			EP.ChkBx_SetOption(TabName + "MarriedCohabiting_CB", "ON");
			EP.ChkBx_SetOption(TabName + "ParentEmploymentOtherSpecify_CB",
					"OFF");
			EP.ChkBx_SetOption(TabName + "Employed_CB", "ON");
			EP.DrpDwn_SelectIndex(TabName + "EducationStatusOfPerson_DD",
					EP.RandomValue(1, 2));
			EP.Btn_Click("#General_AddRecordSave_BN");
			VerifyChapterStatus(ActionDone, TabIndex, EP.LangData("Complete"));// Verify
																				// Complete
																				// Legend

			// Verify tooltip values in the table
			CheckTableText(TabName + "FamilyContextFR_TE", 1, 1, 1,
					EP.LangData("Date"), EP.MonthShortAll());
			CheckTableText(TabName + "FamilyContextFR_TE", 1, 2, 1,
					EP.LangData("Parent"),
					EP.ORData("#BaselineRecord_AddSocioeconomicParentAT_DD"));
			CheckTableText(TabName + "FamilyContextFR_TE", 1, 3, 1,
					EP.LangData("ParentMaritalStatus"),
					EP.ORData("#BaselineRecord_ParentMaritalStatusColumnAT_ST"));
			CheckTableText(
					TabName + "FamilyContextFR_TE",
					1,
					4,
					1,
					EP.LangData("ParentEmploymentStatus"),
					EP.ORData("#RecordOverview_ParentEmploymentStatusColumn_ST"));
			CheckTableText(TabName + "FamilyContextFR_TE", 1, 5, 1,
					EP.LangData("EducationStatusOfPerson"),
					EP.ORData("#RecordOverview_EducationStatusColumn_ST"));

			break;

		case CANADA:
			CanadaECRF CanadaDR = new CanadaECRF(EP);
			CanadaDR.SocioeconomicDynamic(ActionDone, TabIndex, RecoveringURL);
			CanadaDR = null;
			break;

		case KOREA:
			KoreaECRF KoreaDR = new KoreaECRF(EP);
			KoreaDR.SocioeconomicDynamic(ActionDone, TabIndex, RecoveringURL);
			KoreaDR = null;
			break;

		case AUSTRALIA:
			AustraliaECRF AustraliaDR = new AustraliaECRF(EP);
			AustraliaDR.SocioeconomicDynamic(ActionDone, TabIndex,
					RecoveringURL);
			AustraliaDR = null;
			break;

		case ICELAND:
			IcelandECRF IcelandDR = new IcelandECRF(EP);
			IcelandDR.SocioeconomicDynamic(ActionDone, TabIndex, RecoveringURL);
			IcelandDR = null;
			break;

		case FINLAND:
			FinlandECRF FinlandDR = new FinlandECRF(EP);
			FinlandDR.SocioeconomicDynamic(ActionDone, TabIndex, RecoveringURL);
			FinlandDR = null;
			break;

		case HUNGARY:
			HungaryECRF HungaryDR = new HungaryECRF(EP);
			HungaryDR.SocioeconomicDynamic(ActionDone, TabIndex, RecoveringURL);
			HungaryDR = null;
			break;

		default:
		}
	}

	public void CurrentGHBaselineStatic(String ActionDone, String TabIndex) {

		VerifyChapterName(ActionDone, TabIndex,
				EP.LangData("CurrentGHTreatment"));// Verify chapter name
		VerifyChapterStatus(ActionDone, TabIndex,
				EP.ORData("#General_ChapterStatus_ST"));// Verify chapter status

		switch (switchCountryName) {
		case UNITEDKINGDOM:
			UnitedKingdomECRF UnitedKingdomDR = new UnitedKingdomECRF(EP);
			UnitedKingdomDR.CurrentGHBaselineStatic(ActionDone, TabIndex);
			UnitedKingdomDR = null;
			break;

		case SWEDEN:
			SwedenECRF SwedenDR = new SwedenECRF(EP);
			SwedenDR.CurrentGHBaselineStatic(ActionDone, TabIndex);
			SwedenDR = null;
			break;

		case ARGENTINA:
			ArgentinaECRF ArgentinaDR = new ArgentinaECRF(EP);
			ArgentinaDR.CurrentGHBaselineStatic(ActionDone, TabIndex);
			ArgentinaDR = null;
			break;

		case COLOMBIA:
			ColombiaECRF ColombiaDR = new ColombiaECRF(EP);
			ColombiaDR.CurrentGHBaselineStatic(ActionDone, TabIndex);
			ColombiaDR = null;
			break;

		case ITALY:
			ItalyECRF ItalyDR = new ItalyECRF(EP);
			ItalyDR.CurrentGHBaselineStatic(ActionDone, TabIndex);
			ItalyDR = null;
			break;

		case INDONESIA:
			IndonesiaECRF IndonesiaDR = new IndonesiaECRF(EP);
			IndonesiaDR.CurrentGHBaselineStatic(ActionDone, TabIndex);
			IndonesiaDR = null;
			break;

		case TAIWAN:
			TaiwanECRF TaiwanDR = new TaiwanECRF(EP);
			TaiwanDR.CurrentGHBaselineStatic(ActionDone, TabIndex);
			TaiwanDR = null;
			break;

		case SLOVAKIA:
			SlovakiaECRF SlovakiaDR = new SlovakiaECRF(EP);
			SlovakiaDR.CurrentGHBaselineStatic(ActionDone, TabIndex);
			SlovakiaDR = null;
			break;

		case GREECE:
			GreeceECRF GreeceDR = new GreeceECRF(EP);
			GreeceDR.CurrentGHBaselineStatic(ActionDone, TabIndex);
			GreeceDR = null;
			break;

		case CANADA:
			CanadaECRF CanadaDR = new CanadaECRF(EP);
			CanadaDR.CurrentGHBaselineStatic(ActionDone, TabIndex);
			CanadaDR = null;
			break;

		case NORWAY:
			NorwayECRF NorwayDR = new NorwayECRF(EP);
			NorwayDR.CurrentGHBaselineStatic(ActionDone, TabIndex);
			NorwayDR = null;
			break;

		case CZECHREPUBLIC:
			CzechRepublicECRF CzechDR = new CzechRepublicECRF(EP);
			CzechDR.CurrentGHBaselineStatic(ActionDone, TabIndex);
			CzechDR = null;
			break;

		case HUNGARY:
			HungaryECRF HungaryDR = new HungaryECRF(EP);
			HungaryDR.CurrentGHBaselineStatic(ActionDone, TabIndex);
			HungaryDR = null;
			break;

		case AUSTRIA:
			VerifyChapterHeading(ActionDone, TabIndex, 1,
					EP.LangData("CurrentGHTreatmenteasypod"));// Verify chapter
																// heading
			AustriaECRF AustriaDR = new AustriaECRF(EP);
			AustriaDR.CurrentGHBaselineStatic(ActionDone, TabIndex);
			AustriaDR = null;
			break;

		case SPAIN:
			SpainECRF SpainDR = new SpainECRF(EP);
			SpainDR.CurrentGHBaselineStatic(ActionDone, TabIndex);
			SpainDR = null;
			break;

		case FRANCE:
			FranceECRF FranceDR = new FranceECRF(EP);
			FranceDR.CurrentGHBaselineStatic(ActionDone, TabIndex);
			FranceDR = null;
			break;

		case KOREA:
			KoreaECRF KoreaDR = new KoreaECRF(EP);
			KoreaDR.CurrentGHBaselineStatic(ActionDone, TabIndex);
			KoreaDR = null;
			break;

		case AUSTRALIA:
			AustraliaECRF AustraliaDR = new AustraliaECRF(EP);
			AustraliaDR.CurrentGHBaselineStatic(ActionDone, TabIndex);
			AustraliaDR = null;
			break;

		case ICELAND:
			IcelandECRF IcelandDR = new IcelandECRF(EP);
			IcelandDR.CurrentGHBaselineStatic(ActionDone, TabIndex);
			IcelandDR = null;
			break;

		case FINLAND:
			FinlandECRF FinlandDR = new FinlandECRF(EP);
			FinlandDR.CurrentGHBaselineStatic(ActionDone, TabIndex);
			FinlandDR = null;
			break;

		case SINGAPORE:
			SingaporeECRF SingaporeDR = new SingaporeECRF(EP);
			SingaporeDR.CurrentGHBaselineStatic(ActionDone, TabIndex);
			SingaporeDR = null;
			break;

		case UNITEDARABEMIRATES:
			UnitedArabEmiratesECRF UnitedArabEmiratesDR = new UnitedArabEmiratesECRF(
					EP);
			UnitedArabEmiratesDR.CurrentGHBaselineStatic(ActionDone, TabIndex);
			UnitedArabEmiratesDR = null;
			break;

		case SAUDIARABIA:
			SaudiArabiaECRF SaudiArabiaDR = new SaudiArabiaECRF(EP);
			SaudiArabiaDR.CurrentGHBaselineStatic(ActionDone, TabIndex);
			SaudiArabiaDR = null;
			break;

		case MEXICO:
			MexicoECRF MexicoDR = new MexicoECRF(EP);
			MexicoDR.CurrentGHBaselineStatic(ActionDone, TabIndex);
			MexicoDR = null;
			break;

		default:

		}
	}

	public void CurrentGHBaselineDynamic(String ActionDone, String TabIndex,
			String RecoveringURL) {

		switch (switchCountryName) {
		case UNITEDKINGDOM:
			UnitedKingdomECRF UnitedKingdomDR = new UnitedKingdomECRF(EP);
			UnitedKingdomDR.CurrentGHBaselineDynamic(ActionDone, TabIndex);
			UnitedKingdomDR = null;
			break;

		case SWEDEN:
			SwedenECRF SwedenDR = new SwedenECRF(EP);
			SwedenDR.CurrentGHBaselineDynamic(ActionDone, TabIndex);
			SwedenDR = null;
			break;

		case ITALY:
			ItalyECRF ItalyDR = new ItalyECRF(EP);
			ItalyDR.CurrentGHBaselineDynamic(ActionDone, TabIndex);
			ItalyDR = null;
			break;

		case ARGENTINA:
			ArgentinaECRF ArgentinaDR = new ArgentinaECRF(EP);
			ArgentinaDR.CurrentGHBaselineDynamic(ActionDone, TabIndex);
			ArgentinaDR = null;
			break;

		case COLOMBIA:
			ColombiaECRF ColombiaDR = new ColombiaECRF(EP);
			ColombiaDR.CurrentGHBaselineDynamic(ActionDone, TabIndex);
			ColombiaDR = null;
			break;

		case TAIWAN:
			TaiwanECRF TaiwanDR = new TaiwanECRF(EP);
			TaiwanDR.CurrentGHBaselineDynamic(ActionDone, TabIndex);
			TaiwanDR = null;
			break;

		case INDONESIA:
			IndonesiaECRF IndonesiaDR = new IndonesiaECRF(EP);
			IndonesiaDR.CurrentGHBaselineDynamic(ActionDone, TabIndex);
			IndonesiaDR = null;
			break;

		case SLOVAKIA:
			SlovakiaECRF SlovakiaDR = new SlovakiaECRF(EP);
			SlovakiaDR.CurrentGHBaselineDynamic(ActionDone, TabIndex);
			SlovakiaDR = null;
			break;

		case GREECE:
			GreeceECRF GreeceDR = new GreeceECRF(EP);
			GreeceDR.CurrentGHBaselineDynamic(ActionDone, TabIndex);
			GreeceDR = null;
			break;

		case CANADA:
			CanadaECRF CanadaDR = new CanadaECRF(EP);
			CanadaDR.CurrentGHBaselineDynamic(ActionDone, TabIndex);
			CanadaDR = null;
			break;

		case NORWAY:
			NorwayECRF NorwayDR = new NorwayECRF(EP);
			NorwayDR.CurrentGHBaselineDynamic(ActionDone, TabIndex);
			NorwayDR = null;
			break;

		case CZECHREPUBLIC:
			CzechRepublicECRF CzechDR = new CzechRepublicECRF(EP);
			CzechDR.CurrentGHBaselineDynamic(ActionDone, TabIndex);
			CzechDR = null;
			break;
		case AUSTRIA:
			AustriaECRF AustriaDR = new AustriaECRF(EP);
			AustriaDR.CurrentGHBaselineDynamic(ActionDone, TabIndex);
			AustriaDR = null;
			break;

		case KOREA:
			KoreaECRF KoreaDR = new KoreaECRF(EP);
			KoreaDR.CurrentGHBaselineDynamic(ActionDone, TabIndex);
			KoreaDR = null;
			break;

		case SPAIN:
			SpainECRF SpainDR = new SpainECRF(EP);
			SpainDR.CurrentGHBaselineDynamic(ActionDone, TabIndex);
			SpainDR = null;
			break;

		case AUSTRALIA:

			AustraliaECRF AustraliaDR = new AustraliaECRF(EP);
			AustraliaDR.CurrentGHBaselineDynamic(ActionDone, TabIndex);
			AustraliaDR = null;
			break;

		case ICELAND:
			IcelandECRF IcelandDR = new IcelandECRF(EP);
			IcelandDR.CurrentGHBaselineDynamic(ActionDone, TabIndex);
			IcelandDR = null;
			break;

		case FINLAND:
			FinlandECRF FinlandDR = new FinlandECRF(EP);
			FinlandDR.CurrentGHBaselineDynamic(ActionDone, TabIndex);
			FinlandDR = null;
			break;

		case FRANCE:
			FranceECRF FranceDR = new FranceECRF(EP);
			FranceDR.CurrentGHBaselineDynamic(ActionDone, TabIndex);
			FranceDR = null;
			break;

		case HUNGARY:
			HungaryECRF HungaryDR = new HungaryECRF(EP);
			HungaryDR.CurrentGHBaselineDynamic(ActionDone, TabIndex);
			HungaryDR = null;
			break;

		case SINGAPORE:
			SingaporeECRF SingaporeDR = new SingaporeECRF(EP);
			SingaporeDR.CurrentGHBaselineDynamic(ActionDone, TabIndex);
			SingaporeDR = null;
			break;

		case UNITEDARABEMIRATES:
			UnitedArabEmiratesECRF UnitedArabEmiratesDR = new UnitedArabEmiratesECRF(
					EP);
			UnitedArabEmiratesDR.CurrentGHBaselineDynamic(ActionDone, TabIndex);
			UnitedArabEmiratesDR = null;
			break;

		case SAUDIARABIA:
			SaudiArabiaECRF SaudiArabiaDR = new SaudiArabiaECRF(EP);
			SaudiArabiaDR.CurrentGHBaselineDynamic(ActionDone, TabIndex);
			SaudiArabiaDR = null;
			break;

		case MEXICO:
			MexicoECRF MexicoDR = new MexicoECRF(EP);
			MexicoDR.CurrentGHBaselineDynamic(ActionDone, TabIndex);
			MexicoDR = null;
			break;

		default:
		}
	}

	public void IGF1BaselineStatic(String ActionDone, String TabIndex) {
		if (("France;Sweden;Korea").contains(Env.CountryUsed)) {
			VerifyChapterName(ActionDone, TabIndex,
					EP.LangData("BiochemistryIGF1"));// Verify chapter name
		} else {
			VerifyChapterName(ActionDone, TabIndex, EP.LangData("IGF1"));// Verify
																			// chapter
																			// name
		}
		VerifyChapterStatus(ActionDone, TabIndex,
				EP.ORData("#General_ChapterStatus_ST"));// Verify chapter status

		switch (switchCountryName) {
		case UNITEDKINGDOM:
			UnitedKingdomECRF UnitedKingdomDR = new UnitedKingdomECRF(EP);
			UnitedKingdomDR.IGF1BaselineStatic(ActionDone, TabIndex);
			UnitedKingdomDR = null;
			break;

		case NORWAY:
			NorwayECRF NorwayDR = new NorwayECRF(EP);
			NorwayDR.IGF1BaselineStatic(ActionDone, TabIndex);
			NorwayDR = null;
			break;

		case ARGENTINA:
			ArgentinaECRF ArgentinaDR = new ArgentinaECRF(EP);
			ArgentinaDR.IGF1BaselineStatic(ActionDone, TabIndex);
			ArgentinaDR = null;
			break;

		case INDONESIA:
			IndonesiaECRF IndonesiaDR = new IndonesiaECRF(EP);
			IndonesiaDR.IGF1BaselineStatic(ActionDone, TabIndex);
			IndonesiaDR = null;
			break;

		case COLOMBIA:
			ColombiaECRF ColombiaDR = new ColombiaECRF(EP);
			ColombiaDR.IGF1BaselineStatic(ActionDone, TabIndex);
			ColombiaDR = null;
			break;

		case CZECHREPUBLIC:
			CzechRepublicECRF CzechDR = new CzechRepublicECRF(EP);
			CzechDR.IGF1BaselineStatic(ActionDone, TabIndex);
			CzechDR = null;
			break;

		case TAIWAN:
			TaiwanECRF TaiwanDR = new TaiwanECRF(EP);
			TaiwanDR.IGF1BaselineStatic(ActionDone, TabIndex);
			TaiwanDR = null;
			break;

		case CANADA:
			CanadaECRF CanadaDR = new CanadaECRF(EP);
			CanadaDR.IGF1BaselineStatic(ActionDone, TabIndex);
			CanadaDR = null;
			break;
		case ITALY:
			ItalyECRF ItalyDR = new ItalyECRF(EP);
			ItalyDR.IGF1BaselineStatic(ActionDone, TabIndex);
			ItalyDR = null;
			break;

		case SLOVAKIA:
			SlovakiaECRF SlovakiaDR = new SlovakiaECRF(EP);
			SlovakiaDR.IGF1BaselineStatic(ActionDone, TabIndex);
			SlovakiaDR = null;
			break;

		case GREECE:
			GreeceECRF GreeceDR = new GreeceECRF(EP);
			GreeceDR.IGF1BaselineStatic(ActionDone, TabIndex);
			GreeceDR = null;
			break;

		case AUSTRIA:
			AustriaECRF AustriaDR = new AustriaECRF(EP);
			AustriaDR.IGF1BaselineStatic(ActionDone, TabIndex);
			AustriaDR = null;
			break;

		case SPAIN:
			SpainECRF SpainDR = new SpainECRF(EP);
			SpainDR.IGF1BaselineStatic(ActionDone, TabIndex);
			SpainDR = null;
			break;

		case AUSTRALIA:
			AustraliaECRF AustraliaDR = new AustraliaECRF(EP);
			AustraliaDR.IGF1BaselineStatic(ActionDone, TabIndex);
			AustraliaDR = null;
			break;

		case KOREA:
			KoreaECRF KoreaDR = new KoreaECRF(EP);
			KoreaDR.IGF1BaselineStatic(ActionDone, TabIndex);
			KoreaDR = null;
			break;

		case ICELAND:
			IcelandECRF IcelandDR = new IcelandECRF(EP);
			IcelandDR.IGF1BaselineStatic(ActionDone, TabIndex);
			IcelandDR = null;
			break;

		case FINLAND:
			FinlandECRF FinlandDR = new FinlandECRF(EP);
			FinlandDR.IGF1BaselineStatic(ActionDone, TabIndex);
			FinlandDR = null;
			break;

		case SWEDEN:
			SwedenECRF SwedenDR = new SwedenECRF(EP);
			SwedenDR.IGF1BaselineStatic(ActionDone, TabIndex);
			SwedenDR = null;
			break;

		case FRANCE:
			FranceECRF FranceDR = new FranceECRF(EP);
			FranceDR.IGF1BaselineStatic(ActionDone, TabIndex);
			FranceDR = null;
			break;

		case HUNGARY:
			HungaryECRF HungaryDR = new HungaryECRF(EP);
			HungaryDR.IGF1BaselineStatic(ActionDone, TabIndex);
			HungaryDR = null;
			break;

		case SINGAPORE:
			SingaporeECRF SingaporeDR = new SingaporeECRF(EP);
			SingaporeDR.IGF1BaselineStatic(ActionDone, TabIndex);
			SingaporeDR = null;
			break;

		case UNITEDARABEMIRATES:
			UnitedArabEmiratesECRF UnitedArabEmiratesDR = new UnitedArabEmiratesECRF(
					EP);
			UnitedArabEmiratesDR.IGF1BaselineStatic(ActionDone, TabIndex);
			UnitedArabEmiratesDR = null;
			break;

		case SAUDIARABIA:
			SaudiArabiaECRF SaudiArabiaDR = new SaudiArabiaECRF(EP);
			SaudiArabiaDR.IGF1BaselineStatic(ActionDone, TabIndex);
			SaudiArabiaDR = null;
			break;

		case MEXICO:
			MexicoECRF MexicoDR = new MexicoECRF(EP);
			MexicoDR.IGF1BaselineStatic(ActionDone, TabIndex);
			MexicoDR = null;
			break;

		default:

		}
	}

	public void IGF1BaselineDynamic(String ActionDone, String TabIndex,
			String RecoveringURL) {

		switch (switchCountryName) {
		case UNITEDKINGDOM:
			UnitedKingdomECRF UnitedKingdomDR = new UnitedKingdomECRF(EP);
			UnitedKingdomDR.IGF1BaselineDynamic(ActionDone, TabIndex,
					RecoveringURL);
			UnitedKingdomDR = null;
			break;

		case NORWAY:
			NorwayECRF NorwayDR = new NorwayECRF(EP);
			NorwayDR.IGF1BaselineDynamic(ActionDone, TabIndex, RecoveringURL);
			NorwayDR = null;
			break;

		case ARGENTINA:
			ArgentinaECRF ArgentinaDR = new ArgentinaECRF(EP);
			ArgentinaDR
					.IGF1BaselineDynamic(ActionDone, TabIndex, RecoveringURL);
			ArgentinaDR = null;
			break;

		case COLOMBIA:
			ColombiaECRF ColombiaDR = new ColombiaECRF(EP);
			ColombiaDR.IGF1BaselineDynamic(ActionDone, TabIndex, RecoveringURL);
			ColombiaDR = null;
			break;

		case CZECHREPUBLIC:
			CzechRepublicECRF CzechDR = new CzechRepublicECRF(EP);
			CzechDR.IGF1BaselineDynamic(ActionDone, TabIndex, RecoveringURL);
			CzechDR = null;
			break;

		case INDONESIA:
			IndonesiaECRF IndonesiaDR = new IndonesiaECRF(EP);
			IndonesiaDR
					.IGF1BaselineDynamic(ActionDone, TabIndex, RecoveringURL);
			IndonesiaDR = null;
			break;

		case CANADA:
			CanadaECRF CanadaDR = new CanadaECRF(EP);
			CanadaDR.IGF1BaselineDynamic(ActionDone, TabIndex, RecoveringURL);
			CanadaDR = null;
			break;

		case TAIWAN:
			TaiwanECRF TaiwanDR = new TaiwanECRF(EP);
			TaiwanDR.IGF1BaselineDynamic(ActionDone, TabIndex, RecoveringURL);
			TaiwanDR = null;
			break;

		case ITALY:
			ItalyECRF ItalyDR = new ItalyECRF(EP);
			ItalyDR.IGF1BaselineDynamic(ActionDone, TabIndex, RecoveringURL);
			ItalyDR = null;
			break;

		case SLOVAKIA:
			SlovakiaECRF SlovakiaDR = new SlovakiaECRF(EP);
			SlovakiaDR.IGF1BaselineDynamic(ActionDone, TabIndex, RecoveringURL);
			SlovakiaDR = null;
			break;

		case GREECE:
			GreeceECRF GreeceDR = new GreeceECRF(EP);
			GreeceDR.IGF1BaselineDynamic(ActionDone, TabIndex, RecoveringURL);
			GreeceDR = null;
			break;

		case AUSTRIA:
			AustriaECRF AustriaDR = new AustriaECRF(EP);
			AustriaDR.IGF1BaselineDynamic(ActionDone, TabIndex, RecoveringURL);
			AustriaDR = null;
			break;

		case SPAIN:
			SpainECRF SpainDR = new SpainECRF(EP);
			SpainDR.IGF1BaselineDynamic(ActionDone, TabIndex, RecoveringURL);
			SpainDR = null;
			break;

		case AUSTRALIA:
			AustraliaECRF AustraliaDR = new AustraliaECRF(EP);
			AustraliaDR
					.IGF1BaselineDynamic(ActionDone, TabIndex, RecoveringURL);
			AustraliaDR = null;
			break;

		case KOREA:
			KoreaECRF KoreaDR = new KoreaECRF(EP);
			KoreaDR.IGF1BaselineDynamic(ActionDone, TabIndex, RecoveringURL);
			KoreaDR = null;
			break;

		case ICELAND:
			IcelandECRF IcelandDR = new IcelandECRF(EP);
			IcelandDR.IGF1BaselineDynamic(ActionDone, TabIndex, RecoveringURL);
			IcelandDR = null;
			break;

		case FINLAND:
			FinlandECRF FinlandDR = new FinlandECRF(EP);
			FinlandDR.IGF1BaselineDynamic(ActionDone, TabIndex, RecoveringURL);
			FinlandDR = null;
			break;

		case SWEDEN:
			SwedenECRF SwedenDR = new SwedenECRF(EP);
			SwedenDR.IGF1BaselineDynamic(ActionDone, TabIndex, RecoveringURL);
			SwedenDR = null;
			break;

		case FRANCE:
			FranceECRF FranceDR = new FranceECRF(EP);
			FranceDR.IGF1BaselineDynamic(ActionDone, TabIndex, RecoveringURL);
			FranceDR = null;
			break;

		case HUNGARY:
			HungaryECRF HungaryDR = new HungaryECRF(EP);
			HungaryDR.IGF1BaselineDynamic(ActionDone, TabIndex, RecoveringURL);
			HungaryDR = null;
			break;

		case SINGAPORE:
			SingaporeECRF SingaporeDR = new SingaporeECRF(EP);
			SingaporeDR
					.IGF1BaselineDynamic(ActionDone, TabIndex, RecoveringURL);
			SingaporeDR = null;
			break;

		case UNITEDARABEMIRATES:
			UnitedArabEmiratesECRF UnitedArabEmiratesDR = new UnitedArabEmiratesECRF(
					EP);
			UnitedArabEmiratesDR.IGF1BaselineDynamic(ActionDone, TabIndex,
					RecoveringURL);
			UnitedArabEmiratesDR = null;
			break;

		case SAUDIARABIA:
			SaudiArabiaECRF SaudiArabiaDR = new SaudiArabiaECRF(EP);
			SaudiArabiaDR.IGF1BaselineDynamic(ActionDone, TabIndex,
					RecoveringURL);
			SaudiArabiaDR = null;
			break;

		case MEXICO:
			MexicoECRF MexicoDR = new MexicoECRF(EP);
			MexicoDR.IGF1BaselineDynamic(ActionDone, TabIndex, RecoveringURL);
			MexicoDR = null;
			break;

		default:
		}

	}

	public void TannerStageBaselineStatic(String ActionDone, String TabIndex) {

		VerifyChapterName(ActionDone, TabIndex, EP.LangData("TannerStage"));// Verify
																			// chapter
																			// name
		VerifyChapterStatus(ActionDone, TabIndex,
				EP.ORData("#General_ChapterStatus_ST"));// Verify chapter status

		switch (switchCountryName) {
		case UNITEDKINGDOM:
			UnitedKingdomECRF UnitedKingdomDR = new UnitedKingdomECRF(EP);
			UnitedKingdomDR.TannerStageBaselineStatic(ActionDone, TabIndex);
			UnitedKingdomDR = null;
			break;

		case ITALY:
			ItalyECRF ItalyDR = new ItalyECRF(EP);
			ItalyDR.TannerStageBaselineStatic(ActionDone, TabIndex);
			ItalyDR = null;
			break;

		case COLOMBIA:
			ColombiaECRF ColombiaDR = new ColombiaECRF(EP);
			ColombiaDR.TannerStageBaselineStatic(ActionDone, TabIndex);
			ColombiaDR = null;
			break;

		case ARGENTINA:
			ArgentinaECRF ArgentinaDR = new ArgentinaECRF(EP);
			ArgentinaDR.TannerStageBaselineStatic(ActionDone, TabIndex);
			ArgentinaDR = null;
			break;

		case INDONESIA:
			IndonesiaECRF IndonesiaDR = new IndonesiaECRF(EP);
			IndonesiaDR.TannerStageBaselineStatic(ActionDone, TabIndex);
			IndonesiaDR = null;
			break;

		case GREECE:
			GreeceECRF GreeceDR = new GreeceECRF(EP);
			GreeceDR.TannerStageBaselineStatic(ActionDone, TabIndex);
			GreeceDR = null;
			break;

		case TAIWAN:
			TaiwanECRF TaiwanDR = new TaiwanECRF(EP);
			TaiwanDR.TannerStageBaselineStatic(ActionDone, TabIndex);
			TaiwanDR = null;
			break;

		case SLOVAKIA:
			SlovakiaECRF SlovakiaDR = new SlovakiaECRF(EP);
			SlovakiaDR.TannerStageBaselineStatic(ActionDone, TabIndex);
			SlovakiaDR = null;
			break;

		case CZECHREPUBLIC:
			CzechRepublicECRF CzechDR = new CzechRepublicECRF(EP);
			CzechDR.TannerStageBaselineStatic(ActionDone, TabIndex);
			CzechDR = null;
			break;

		case HUNGARY:
			HungaryECRF HungaryDR = new HungaryECRF(EP);
			HungaryDR.TannerStageBaselineStatic(ActionDone, TabIndex);
			HungaryDR = null;
			break;

		case NORWAY:
			NorwayECRF NorwayDR = new NorwayECRF(EP);
			NorwayDR.TannerStageBaselineStatic(ActionDone, TabIndex);
			NorwayDR = null;
			break;

		case CANADA:
			CanadaECRF CanadaDR = new CanadaECRF(EP);
			CanadaDR.TannerStageBaselineStatic(ActionDone, TabIndex);
			CanadaDR = null;
			break;

		case AUSTRIA:
			AustriaECRF AustriaDR = new AustriaECRF(EP);
			AustriaDR.TannerStageBaselineStatic(ActionDone, TabIndex);
			AustriaDR = null;
			break;

		case SPAIN:
			SpainECRF SpainDR = new SpainECRF(EP);
			SpainDR.TannerStageBaselineStatic(ActionDone, TabIndex);
			SpainDR = null;
			break;

		case KOREA:
			KoreaECRF KoreaDR = new KoreaECRF(EP);
			KoreaDR.TannerStageBaselineStatic(ActionDone, TabIndex);
			KoreaDR = null;
			break;

		case AUSTRALIA:
			AustraliaECRF AustraliaDR = new AustraliaECRF(EP);
			AustraliaDR.TannerStageBaselineStatic(ActionDone, TabIndex);
			AustraliaDR = null;
			break;

		case ICELAND:
			IcelandECRF IcelandDR = new IcelandECRF(EP);
			IcelandDR.TannerStageBaselineStatic(ActionDone, TabIndex);
			IcelandDR = null;
			break;

		case FINLAND:
			FinlandECRF FinlandDR = new FinlandECRF(EP);
			FinlandDR.TannerStageBaselineStatic(ActionDone, TabIndex);
			FinlandDR = null;
			break;

		case FRANCE:
			FranceECRF FranceDR = new FranceECRF(EP);
			FranceDR.TannerStageBaselineStatic(ActionDone, TabIndex);
			FranceDR = null;
			break;
		case SWEDEN:
			SwedenECRF SwedenDR = new SwedenECRF(EP);
			SwedenDR.TannerStageBaselineStatic(ActionDone, TabIndex);
			SwedenDR = null;
			break;

		case SINGAPORE:
			SingaporeECRF SingaporeDR = new SingaporeECRF(EP);
			SingaporeDR.TannerStageBaselineStatic(ActionDone, TabIndex);
			SingaporeDR = null;
			break;

		case UNITEDARABEMIRATES:
			UnitedArabEmiratesECRF UnitedArabEmiratesDR = new UnitedArabEmiratesECRF(
					EP);
			UnitedArabEmiratesDR
					.TannerStageBaselineStatic(ActionDone, TabIndex);
			UnitedArabEmiratesDR = null;
			break;

		case SAUDIARABIA:
			SaudiArabiaECRF SaudiArabiaDR = new SaudiArabiaECRF(EP);
			SaudiArabiaDR.TannerStageBaselineStatic(ActionDone, TabIndex);
			SaudiArabiaDR = null;
			break;

		case MEXICO:
			MexicoECRF MexicoDR = new MexicoECRF(EP);
			MexicoDR.TannerStageBaselineStatic(ActionDone, TabIndex);
			MexicoDR = null;
			break;

		default:
		}

	}

	public void TannerStageBaselineFemaleDynamic(String ActionDone,
			String TabIndex, String RecoveringURL) {

		switch (switchCountryName) {
		case UNITEDKINGDOM:
			UnitedKingdomECRF UnitedKingdomDR = new UnitedKingdomECRF(EP);
			UnitedKingdomDR.TannerStageBaselineFemaleDynamic(ActionDone,
					TabIndex, RecoveringURL);
			UnitedKingdomDR = null;
			break;

		case ITALY:
			ItalyECRF ItalyDR = new ItalyECRF(EP);
			ItalyDR.TannerStageBaselineFemaleDynamic(ActionDone, TabIndex,
					RecoveringURL);
			ItalyDR = null;
			break;

		case GREECE:
			GreeceECRF GreeceDR = new GreeceECRF(EP);
			GreeceDR.TannerStageBaselineFemaleDynamic(ActionDone, TabIndex,
					RecoveringURL);
			GreeceDR = null;
			break;

		case CANADA:
			CanadaECRF CanadaDR = new CanadaECRF(EP);
			CanadaDR.TannerStageBaselineFemaleDynamic(ActionDone, TabIndex,
					RecoveringURL);
			CanadaDR = null;
			break;

		case COLOMBIA:
			ColombiaECRF ColombiaDR = new ColombiaECRF(EP);
			ColombiaDR.TannerStageBaselineFemaleDynamic(ActionDone, TabIndex,
					RecoveringURL);
			ColombiaDR = null;
			break;

		case ARGENTINA:
			ArgentinaECRF ArgentinaDR = new ArgentinaECRF(EP);
			ArgentinaDR.TannerStageBaselineFemaleDynamic(ActionDone, TabIndex,
					RecoveringURL);
			ArgentinaDR = null;
			break;

		case INDONESIA:
			IndonesiaECRF IndonesiaDR = new IndonesiaECRF(EP);
			IndonesiaDR.TannerStageBaselineFemaleDynamic(ActionDone, TabIndex,
					RecoveringURL);
			IndonesiaDR = null;
			break;

		case TAIWAN:
			TaiwanECRF TaiwanDR = new TaiwanECRF(EP);
			TaiwanDR.TannerStageBaselineFemaleDynamic(ActionDone, TabIndex,
					RecoveringURL);
			TaiwanDR = null;
			break;

		case SWEDEN:
			SwedenECRF SwedenDR = new SwedenECRF(EP);
			SwedenDR.TannerStageBaselineFemaleDynamic(ActionDone, TabIndex,
					RecoveringURL);
			SwedenDR = null;
			break;
		case AUSTRIA:
			AustriaECRF AustriaDR = new AustriaECRF(EP);
			AustriaDR.TannerStageBaselineFemaleDynamic(ActionDone, TabIndex,
					RecoveringURL);
			AustriaDR = null;
			break;

		case SPAIN:
			SpainECRF SpainDR = new SpainECRF(EP);
			SpainDR.TannerStageBaselineFemaleDynamic(ActionDone, TabIndex,
					RecoveringURL);
			SpainDR = null;
			break;

		case KOREA:
			KoreaECRF KoreaDR = new KoreaECRF(EP);
			KoreaDR.TannerStageBaselineFemaleDynamic(ActionDone, TabIndex,
					RecoveringURL);
			KoreaDR = null;
			break;

		case AUSTRALIA:
			AustraliaECRF AustraliaDR = new AustraliaECRF(EP);
			AustraliaDR.TannerStageBaselineFemaleDynamic(ActionDone, TabIndex,
					RecoveringURL);
			AustraliaDR = null;
			break;

		case ICELAND:
			IcelandECRF IcelandDR = new IcelandECRF(EP);
			IcelandDR.TannerStageBaselineFemaleDynamic(ActionDone, TabIndex,
					RecoveringURL);
			IcelandDR = null;
			break;

		case FINLAND:
			FinlandECRF FinlandDR = new FinlandECRF(EP);
			FinlandDR.TannerStageBaselineFemaleDynamic(ActionDone, TabIndex,
					RecoveringURL);
			FinlandDR = null;
			break;

		case FRANCE:
			FranceECRF FranceDR = new FranceECRF(EP);
			FranceDR.TannerStageBaselineFemaleDynamic(ActionDone, TabIndex,
					RecoveringURL);
			FranceDR = null;
			break;

		case CZECHREPUBLIC:
			CzechRepublicECRF CzechDR = new CzechRepublicECRF(EP);
			CzechDR.TannerStageBaselineFemaleDynamic(ActionDone, TabIndex,
					RecoveringURL);
			CzechDR = null;
			break;

		case NORWAY:
			NorwayECRF NorwayDR = new NorwayECRF(EP);
			NorwayDR.TannerStageBaselineFemaleDynamic(ActionDone, TabIndex,
					RecoveringURL);
			NorwayDR = null;
			break;

		case SLOVAKIA:
			SlovakiaECRF SlovakiaDR = new SlovakiaECRF(EP);
			SlovakiaDR.TannerStageBaselineFemaleDynamic(ActionDone, TabIndex,
					RecoveringURL);
			SlovakiaDR = null;
			break;

		case HUNGARY:
			HungaryECRF HungaryDR = new HungaryECRF(EP);
			HungaryDR.TannerStageBaselineFemaleDynamic(ActionDone, TabIndex,
					RecoveringURL);
			HungaryDR = null;
			break;

		case SINGAPORE:
			SingaporeECRF SingaporeDR = new SingaporeECRF(EP);
			SingaporeDR.TannerStageBaselineFemaleDynamic(ActionDone, TabIndex,
					RecoveringURL);
			SingaporeDR = null;
			break;

		case UNITEDARABEMIRATES:
			UnitedArabEmiratesECRF UnitedArabEmiratesDR = new UnitedArabEmiratesECRF(
					EP);
			UnitedArabEmiratesDR.TannerStageBaselineFemaleDynamic(ActionDone,
					TabIndex, RecoveringURL);
			UnitedArabEmiratesDR = null;
			break;

		case SAUDIARABIA:
			SaudiArabiaECRF SaudiArabiaDR = new SaudiArabiaECRF(EP);
			SaudiArabiaDR.TannerStageBaselineFemaleDynamic(ActionDone,
					TabIndex, RecoveringURL);
			SaudiArabiaDR = null;
			break;

		case MEXICO:
			MexicoECRF MexicoDR = new MexicoECRF(EP);
			MexicoDR.TannerStageBaselineFemaleDynamic(ActionDone, TabIndex,
					RecoveringURL);
			MexicoDR = null;
			break;

		default:
		}

	}

	public void TannerStageBaselineDynamic(String ActionDone, String TabIndex,
			String RecoveringURL) {

		// Delete Record
		if (EP.ObjectExists("#General_Edit_IM")) {
			DeleteRecord("#General_Delete_IM", false);
			VerifyDeleteMessage(EP.LangData("TannerStage"));
		}

		switch (switchCountryName) {
		case UNITEDKINGDOM:
			UnitedKingdomECRF UnitedKingdomDR = new UnitedKingdomECRF(EP);
			UnitedKingdomDR.TannerStageBaselineDynamic(ActionDone, TabIndex,
					RecoveringURL);
			UnitedKingdomDR = null;
			break;

		case SWEDEN:
			SwedenECRF SwedenDR = new SwedenECRF(EP);
			SwedenDR.TannerStageBaselineDynamic(ActionDone, TabIndex,
					RecoveringURL);
			SwedenDR = null;
			break;

		case ARGENTINA:
			ArgentinaECRF ArgentinaDR = new ArgentinaECRF(EP);
			ArgentinaDR.TannerStageBaselineDynamic(ActionDone, TabIndex,
					RecoveringURL);
			ArgentinaDR = null;
			break;

		case INDONESIA:
			IndonesiaECRF IndonesiaDR = new IndonesiaECRF(EP);
			IndonesiaDR.TannerStageBaselineDynamic(ActionDone, TabIndex,
					RecoveringURL);
			IndonesiaDR = null;
			break;

		case TAIWAN:
			TaiwanECRF TaiwanDR = new TaiwanECRF(EP);
			TaiwanDR.TannerStageBaselineDynamic(ActionDone, TabIndex,
					RecoveringURL);
			TaiwanDR = null;
			break;

		case COLOMBIA:
			ColombiaECRF ColombiaDR = new ColombiaECRF(EP);
			ColombiaDR.TannerStageBaselineDynamic(ActionDone, TabIndex,
					RecoveringURL);
			ColombiaDR = null;
			break;

		case ITALY:
			ItalyECRF ItalyDR = new ItalyECRF(EP);
			ItalyDR.TannerStageBaselineDynamic(ActionDone, TabIndex,
					RecoveringURL);
			ItalyDR = null;
			break;

		case GREECE:
			GreeceECRF GreeceDR = new GreeceECRF(EP);
			GreeceDR.TannerStageBaselineDynamic(ActionDone, TabIndex,
					RecoveringURL);
			GreeceDR = null;
			break;

		case SLOVAKIA:
			SlovakiaECRF SlovakiaDR = new SlovakiaECRF(EP);
			SlovakiaDR.TannerStageBaselineDynamic(ActionDone, TabIndex,
					RecoveringURL);
			SlovakiaDR = null;
			break;

		case CANADA:
			CanadaECRF CanadaDR = new CanadaECRF(EP);
			CanadaDR.TannerStageBaselineDynamic(ActionDone, TabIndex,
					RecoveringURL);
			CanadaDR = null;
			break;

		case AUSTRIA:
			AustriaECRF AustriaDR = new AustriaECRF(EP);
			AustriaDR.TannerStageBaselineDynamic(ActionDone, TabIndex,
					RecoveringURL);
			AustriaDR = null;
			break;

		case NORWAY:
			NorwayECRF NorwayDR = new NorwayECRF(EP);
			NorwayDR.TannerStageBaselineDynamic(ActionDone, TabIndex,
					RecoveringURL);
			NorwayDR = null;
			break;

		case CZECHREPUBLIC:
			CzechRepublicECRF CzechDR = new CzechRepublicECRF(EP);
			CzechDR.TannerStageBaselineDynamic(ActionDone, TabIndex,
					RecoveringURL);
			CzechDR = null;
			break;

		case SPAIN:
			SpainECRF SpainDR = new SpainECRF(EP);
			SpainDR.TannerStageBaselineDynamic(ActionDone, TabIndex,
					RecoveringURL);
			SpainDR = null;
			break;

		case KOREA:
			KoreaECRF KoreaDR = new KoreaECRF(EP);
			KoreaDR.TannerStageBaselineDynamic(ActionDone, TabIndex,
					RecoveringURL);
			KoreaDR = null;
			break;

		case AUSTRALIA:
			AustraliaECRF AustraliaDR = new AustraliaECRF(EP);
			AustraliaDR.TannerStageBaselineDynamic(ActionDone, TabIndex,
					RecoveringURL);
			AustraliaDR = null;
			break;

		case ICELAND:
			IcelandECRF IcelandDR = new IcelandECRF(EP);
			IcelandDR.TannerStageBaselineDynamic(ActionDone, TabIndex,
					RecoveringURL);
			IcelandDR = null;
			break;

		case FINLAND:
			FinlandECRF FinlandDR = new FinlandECRF(EP);
			FinlandDR.TannerStageBaselineDynamic(ActionDone, TabIndex,
					RecoveringURL);
			FinlandDR = null;
			break;

		case FRANCE:
			FranceECRF FranceDR = new FranceECRF(EP);
			FranceDR.TannerStageBaselineDynamic(ActionDone, TabIndex,
					RecoveringURL);
			FranceDR = null;
			break;

		case HUNGARY:
			HungaryECRF HungaryDR = new HungaryECRF(EP);
			HungaryDR.TannerStageBaselineDynamic(ActionDone, TabIndex,
					RecoveringURL);
			HungaryDR = null;
			break;

		case SINGAPORE:
			SingaporeECRF SingaporeDR = new SingaporeECRF(EP);
			SingaporeDR.TannerStageBaselineDynamic(ActionDone, TabIndex,
					RecoveringURL);
			SingaporeDR = null;
			break;

		case UNITEDARABEMIRATES:
			UnitedArabEmiratesECRF UnitedArabEmiratesDR = new UnitedArabEmiratesECRF(
					EP);
			UnitedArabEmiratesDR.TannerStageBaselineDynamic(ActionDone,
					TabIndex, RecoveringURL);
			UnitedArabEmiratesDR = null;
			break;

		case SAUDIARABIA:
			SaudiArabiaECRF SaudiArabiaDR = new SaudiArabiaECRF(EP);
			SaudiArabiaDR.TannerStageBaselineDynamic(ActionDone, TabIndex,
					RecoveringURL);
			SaudiArabiaDR = null;
			break;

		case MEXICO:
			MexicoECRF MexicoDR = new MexicoECRF(EP);
			MexicoDR.TannerStageBaselineDynamic(ActionDone, TabIndex,
					RecoveringURL);
			MexicoDR = null;
			break;

		default:
		}

	}

	public void BoneAgeBaselineStatic(String ActionDone, String TabIndex) {

		VerifyChapterName(ActionDone, TabIndex, EP.LangData("BoneAge"));// Verify
																		// chapter
																		// name
		VerifyChapterStatus(ActionDone, TabIndex,
				EP.ORData("#General_ChapterStatus_ST"));// Verify chapter status

		switch (switchCountryName) {
		case UNITEDKINGDOM:
			UnitedKingdomECRF UnitedKingdomDR = new UnitedKingdomECRF(EP);
			UnitedKingdomDR.BoneAgeBaselineStatic(ActionDone, TabIndex);
			UnitedKingdomDR = null;
			break;

		case ITALY:
			ItalyECRF ItalyDR = new ItalyECRF(EP);
			ItalyDR.BoneAgeBaselineStatic(ActionDone, TabIndex);
			ItalyDR = null;
			break;

		case COLOMBIA:
			ColombiaECRF ColombiaDR = new ColombiaECRF(EP);
			ColombiaDR.BoneAgeBaselineStatic(ActionDone, TabIndex);
			ColombiaDR = null;
			break;

		case ARGENTINA:
			ArgentinaECRF ArgentinaDR = new ArgentinaECRF(EP);
			ArgentinaDR.BoneAgeBaselineStatic(ActionDone, TabIndex);
			ArgentinaDR = null;
			break;

		case INDONESIA:
			IndonesiaECRF IndonesiaDR = new IndonesiaECRF(EP);
			IndonesiaDR.BoneAgeBaselineStatic(ActionDone, TabIndex);
			IndonesiaDR = null;
			break;

		case TAIWAN:
			TaiwanECRF TaiwanDR = new TaiwanECRF(EP);
			TaiwanDR.BoneAgeBaselineStatic(ActionDone, TabIndex);
			TaiwanDR = null;
			break;

		case SWEDEN:
			SwedenECRF SwedenDR = new SwedenECRF(EP);
			SwedenDR.BoneAgeBaselineStatic(ActionDone, TabIndex);
			SwedenDR = null;
			break;

		case GREECE:
			GreeceECRF GreeceDR = new GreeceECRF(EP);
			GreeceDR.BoneAgeBaselineStatic(ActionDone, TabIndex);
			GreeceDR = null;
			break;

		case SLOVAKIA:
			SlovakiaECRF SlovakiaDR = new SlovakiaECRF(EP);
			SlovakiaDR.BoneAgeBaselineStatic(ActionDone, TabIndex);
			SlovakiaDR = null;
			break;

		case NORWAY:
			NorwayECRF NorwayDR = new NorwayECRF(EP);
			NorwayDR.BoneAgeBaselineStatic(ActionDone, TabIndex);
			NorwayDR = null;
			break;

		case CZECHREPUBLIC:
			CzechRepublicECRF CzechDR = new CzechRepublicECRF(EP);
			CzechDR.BoneAgeBaselineStatic(ActionDone, TabIndex);
			CzechDR = null;
			break;

		case CANADA:
			CanadaECRF CanadaDR = new CanadaECRF(EP);
			CanadaDR.BoneAgeBaselineStatic(ActionDone, TabIndex);
			CanadaDR = null;
			break;

		case AUSTRIA:
			AustriaECRF AustriaDR = new AustriaECRF(EP);
			AustriaDR.BoneAgeBaselineStatic(ActionDone, TabIndex);
			AustriaDR = null;
			break;

		case SPAIN:
			SpainECRF SpainDR = new SpainECRF(EP);
			SpainDR.BoneAgeBaselineStatic(ActionDone, TabIndex);
			SpainDR = null;
			break;

		case KOREA:
			KoreaECRF KoreaDR = new KoreaECRF(EP);
			KoreaDR.BoneAgeBaselineStatic(ActionDone, TabIndex);
			KoreaDR = null;
			break;

		case AUSTRALIA:
			AustraliaECRF AustraliaDR = new AustraliaECRF(EP);
			AustraliaDR.BoneAgeBaselineStatic(ActionDone, TabIndex);
			AustraliaDR = null;
			break;

		case ICELAND:
			IcelandECRF IcelandDR = new IcelandECRF(EP);
			IcelandDR.BoneAgeBaselineStatic(ActionDone, TabIndex);
			IcelandDR = null;
			break;

		case FINLAND:
			FinlandECRF FinlandDR = new FinlandECRF(EP);
			FinlandDR.BoneAgeBaselineStatic(ActionDone, TabIndex);
			FinlandDR = null;
			break;
		case FRANCE:
			FranceECRF FranceDR = new FranceECRF(EP);
			FranceDR.BoneAgeBaselineStatic(ActionDone, TabIndex);
			FranceDR = null;
			break;

		case HUNGARY:
			HungaryECRF HungaryDR = new HungaryECRF(EP);
			HungaryDR.BoneAgeBaselineStatic(ActionDone, TabIndex);
			HungaryDR = null;
			break;

		case SINGAPORE:
			SingaporeECRF SingaporeDR = new SingaporeECRF(EP);
			SingaporeDR.BoneAgeBaselineStatic(ActionDone, TabIndex);
			SingaporeDR = null;
			break;

		case UNITEDARABEMIRATES:
			UnitedArabEmiratesECRF UnitedArabEmiratesDR = new UnitedArabEmiratesECRF(
					EP);
			UnitedArabEmiratesDR.BoneAgeBaselineStatic(ActionDone, TabIndex);
			UnitedArabEmiratesDR = null;
			break;

		case SAUDIARABIA:
			SaudiArabiaECRF SaudiArabiaDR = new SaudiArabiaECRF(EP);
			SaudiArabiaDR.BoneAgeBaselineStatic(ActionDone, TabIndex);
			SaudiArabiaDR = null;
			break;

		case MEXICO:
			MexicoECRF MexicoDR = new MexicoECRF(EP);
			MexicoDR.BoneAgeBaselineStatic(ActionDone, TabIndex);
			MexicoDR = null;
			break;

		default:
		}
	}

	public void BoneAgeBaselineDynamic(String ActionDone, String TabIndex,
			String RecoveringURL) {

		String TabName = AppendHash(ActionDone);

		// Delete Record
		if (EP.ObjectExists("#General_Edit_IM")) {
			DeleteRecord("#General_Delete_IM", false);
			VerifyDeleteMessage(EP.LangData("BoneAge"));
		}

		switch (switchCountryName) {
		case UNITEDKINGDOM:
			UnitedKingdomECRF UnitedKingdomDR = new UnitedKingdomECRF(EP);
			UnitedKingdomDR.BoneAgeBaselineDynamic(TabName, TabIndex,
					RecoveringURL);
			UnitedKingdomDR = null;
			break;

		case ITALY:
			ItalyECRF ItalyDR = new ItalyECRF(EP);
			ItalyDR.BoneAgeBaselineDynamic(ActionDone, TabIndex, RecoveringURL);
			ItalyDR = null;

			break;

		case COLOMBIA:
			ColombiaECRF ColombiaDR = new ColombiaECRF(EP);
			ColombiaDR.BoneAgeBaselineDynamic(ActionDone, TabIndex,
					RecoveringURL);
			ColombiaDR = null;
			break;

		case INDONESIA:
			IndonesiaECRF IndoneisaDR = new IndonesiaECRF(EP);
			IndoneisaDR.BoneAgeBaselineDynamic(ActionDone, TabIndex,
					RecoveringURL);
			IndoneisaDR = null;
			break;

		case ARGENTINA:
			ArgentinaECRF ArgentinaDR = new ArgentinaECRF(EP);
			ArgentinaDR.BoneAgeBaselineDynamic(ActionDone, TabIndex,
					RecoveringURL);
			ArgentinaDR = null;
			break;

		case SLOVAKIA:
			SlovakiaECRF SlovakiaDR = new SlovakiaECRF(EP);
			SlovakiaDR.BoneAgeBaselineDynamic(ActionDone, TabIndex,
					RecoveringURL);
			SlovakiaDR = null;
			break;

		case TAIWAN:
			TaiwanECRF TaiwanDR = new TaiwanECRF(EP);
			TaiwanDR.BoneAgeBaselineDynamic(ActionDone, TabIndex, RecoveringURL);
			TaiwanDR = null;
			break;

		case SWEDEN:
			SwedenECRF SwedenDR = new SwedenECRF(EP);
			SwedenDR.BoneAgeBaselineDynamic(ActionDone, TabIndex, RecoveringURL);
			SwedenDR = null;
			break;

		case GREECE:
			GreeceECRF GreeceDR = new GreeceECRF(EP);
			GreeceDR.BoneAgeBaselineDynamic(ActionDone, TabIndex, RecoveringURL);
			GreeceDR = null;
			break;

		case CANADA:
			CanadaECRF CanadaDR = new CanadaECRF(EP);
			CanadaDR.BoneAgeBaselineDynamic(ActionDone, TabIndex, RecoveringURL);
			CanadaDR = null;
			break;

		case NORWAY:
			NorwayECRF NorwayDR = new NorwayECRF(EP);
			NorwayDR.BoneAgeBaselineDynamic(ActionDone, TabIndex, RecoveringURL);
			NorwayDR = null;
			break;

		case CZECHREPUBLIC:
			CzechRepublicECRF CzechDR = new CzechRepublicECRF(EP);
			CzechDR.BoneAgeBaselineDynamic(ActionDone, TabIndex, RecoveringURL);
			CzechDR = null;
			break;

		case AUSTRIA:
			AustriaECRF AustriaDR = new AustriaECRF(EP);
			AustriaDR.BoneAgeBaselineDynamic(ActionDone, TabIndex,
					RecoveringURL);
			AustriaDR = null;
			break;

		case SPAIN:
			SpainECRF SpainDR = new SpainECRF(EP);
			SpainDR.BoneAgeBaselineDynamic(ActionDone, TabIndex, RecoveringURL);
			SpainDR = null;
			break;

		case KOREA:
			KoreaECRF KoreaDR = new KoreaECRF(EP);
			KoreaDR.BoneAgeBaselineDynamic(ActionDone, TabIndex, RecoveringURL);
			KoreaDR = null;
			break;

		case ICELAND:
			IcelandECRF IcelandDR = new IcelandECRF(EP);
			IcelandDR.BoneAgeBaselineDynamic(ActionDone, TabIndex,
					RecoveringURL);
			IcelandDR = null;
			break;

		case FINLAND:
			FinlandECRF FinlandDR = new FinlandECRF(EP);
			FinlandDR.BoneAgeBaselineDynamic(ActionDone, TabIndex,
					RecoveringURL);
			FinlandDR = null;
			break;

		case AUSTRALIA:
			AustraliaECRF AustraliaDR = new AustraliaECRF(EP);
			AustraliaDR.BoneAgeBaselineDynamic(ActionDone, TabIndex,
					RecoveringURL);
			AustraliaDR = null;
			break;

		case FRANCE:
			FranceECRF FranceDR = new FranceECRF(EP);
			FranceDR.BoneAgeBaselineDynamic(ActionDone, TabIndex, RecoveringURL);
			FranceDR = null;
			break;

		case HUNGARY:
			HungaryECRF HungaryDR = new HungaryECRF(EP);
			HungaryDR.BoneAgeBaselineDynamic(ActionDone, TabIndex,
					RecoveringURL);
			HungaryDR = null;
			break;

		case SINGAPORE:
			SingaporeECRF SingaporeDR = new SingaporeECRF(EP);
			SingaporeDR.BoneAgeBaselineDynamic(ActionDone, TabIndex,
					RecoveringURL);
			SingaporeDR = null;
			break;

		case UNITEDARABEMIRATES:
			UnitedArabEmiratesECRF UnitedArabEmiratesDR = new UnitedArabEmiratesECRF(
					EP);
			UnitedArabEmiratesDR.BoneAgeBaselineDynamic(ActionDone, TabIndex,
					RecoveringURL);
			UnitedArabEmiratesDR = null;
			break;

		case SAUDIARABIA:
			SaudiArabiaECRF SaudiArabiaDR = new SaudiArabiaECRF(EP);
			SaudiArabiaDR.BoneAgeBaselineDynamic(ActionDone, TabIndex,
					RecoveringURL);
			SaudiArabiaDR = null;
			break;

		case MEXICO:
			MexicoECRF MexicoDR = new MexicoECRF(EP);
			MexicoDR.BoneAgeBaselineDynamic(ActionDone, TabIndex, RecoveringURL);
			MexicoDR = null;
			break;

		default:
		}
	}

	// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*ADD DATA IN ALL CHAPTERS OF BASELINE
	// RECORD (VALID AND INVALID
	// DATA)*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-**-*-*-*-*-*-*-*-*-*-*-*-*-*

	public void DemographicDataAdd(boolean IsValid, String TabIndex) {
		// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*DEMOGRAPHIC
		// DATA*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-**-*-*-*-*-*-*-*-*-*-*-*-*-*
		String EthnicityValue = "";
		String HormoneIndicationValue = "";

		EP.ShowTab(TabIndex);
		switch (switchCountryName) {
		case UNITEDKINGDOM:
			EP.DrpDwn_SelectIndex("#BaselineRecord_EthnicityGB_DD",
					EP.RandomValue(1, 3));
			EP.DrpDwn_SelectIndex(
					"#BaselineRecord_GrowthHormoneIndicationGB_DD",
					EP.RandomValue(1, 4));
			EP.Btn_Click("#General_First_BN");
			break;

		case GREECE:
			EP.DrpDwn_SelectIndex("#BaselineRecord_Ethnicity_DD",
					EP.RandomValue(1, 3));
			EP.DrpDwn_SelectIndex(
					"#BaselineRecord_GrowthHormoneIndicationES_DD",
					EP.RandomValue(1, 6));
			EP.Btn_Click("#General_First_BN");
			break;

		case PORTUGAL:

			EP.DrpDwn_SelectIndex("#BaselineRecord_Ethnicity_DD",
					EP.RandomValue(1, 3));
			EP.DrpDwn_SelectIndex("#BaselineRecord_GrowthHormoneIndication_DD",
					EP.RandomValue(1, 4));
			EP.Btn_Click("#General_First_BN");// Click save button
			break;

		case ITALY:
			EP.DrpDwn_SelectIndex(
					"#BaselineRecord_GrowthHormoneIndicationSK_DD",
					EP.RandomValue(1, 4));
			EP.DrpDwn_SelectIndex("#BaselineRecord_EthnicitySK_DD",
					EP.RandomValue(1, 3));
			EP.Btn_Click("#General_First_BN");
			break;
		case SLOVAKIA:
			EP.DrpDwn_SelectIndex(
					"#BaselineRecord_GrowthHormoneIndicationIT_DD",
					EP.RandomValue(1, 4));
			EP.DrpDwn_SelectIndex("#BaselineRecord_EthnicityIT_DD",
					EP.RandomValue(1, 3));
			EP.Btn_Click("#General_First_BN");
			break;

		case AUSTRIA:
			EP.DrpDwn_SelectIndex("#BaselineRecord_Ethnicity_DD",
					EP.RandomValue(1, 3));
			EP.DrpDwn_SelectIndex(
					"#BaselineRecord_GrowthHormoneIndicationAT_DD",
					EP.RandomValue(1, 4));
			EP.Btn_Click("#General_First_BN");
			break;

		case SINGAPORE:
			EP.DrpDwn_SelectIndex("#BaselineRecord_Ethnicity_DD",
					EP.RandomValue(1, 3));
			EP.DrpDwn_SelectIndex(
					"#BaselineRecord_GrowthHormoneIndicationAT_DD",
					EP.RandomValue(1, 4));
			EP.Btn_Click("#General_First_BN");
			break;
		case MEXICO:
			EP.DrpDwn_SelectIndex("#BaselineRecord_Ethnicity_DD",
					EP.RandomValue(1, 3));
			EP.DrpDwn_SelectIndex(
					"#BaselineRecord_GrowthHormoneIndicationAT_DD",
					EP.RandomValue(1, 4));
			EP.Btn_Click("#General_First_BN");
			break;
		case UNITEDARABEMIRATES:
			EP.DrpDwn_SelectIndex("#BaselineRecord_Ethnicity_DD",
					EP.RandomValue(1, 3));
			EP.DrpDwn_SelectIndex(
					"#BaselineRecord_GrowthHormoneIndicationAT_DD",
					EP.RandomValue(1, 4));
			EP.Btn_Click("#General_First_BN");
			break;
		case SAUDIARABIA:
			EP.DrpDwn_SelectIndex("#BaselineRecord_Ethnicity_DD",
					EP.RandomValue(1, 3));
			EP.DrpDwn_SelectIndex(
					"#BaselineRecord_GrowthHormoneIndicationAT_DD",
					EP.RandomValue(1, 4));
			EP.Btn_Click("#General_First_BN");
			break;

		case SPAIN:
			EP.DrpDwn_SelectIndex(
					"#BaselineRecord_GrowthHormoneIndicationAT_DD",
					EP.RandomValue(1, 4));
			EP.DrpDwn_SelectIndex("#BaselineRecord_EthnicityES_DD",
					EP.RandomValue(1, 3));
			EP.Btn_Click("#General_First_BN");
			break;

		case FRANCE:
			EP.DrpDwn_SelectIndex("#BaselineRecord_EthnicityFR_DD",
					EP.RandomValue(1, 3));
			EP.DrpDwn_SelectIndex(
					"#BaselineRecord_GrowthHormoneIndicationFR_DD",
					EP.RandomValue(1, 4));
			EP.Btn_Click("#General_First_BN");
			break;

		case CZECHREPUBLIC:
			EthnicityValue = "("
					+ EP.ORXPath("#BaselineRecord_EthnicityOptions_RB") + "["
					+ EP.RandomValue(1, 3) + "]//input)";
			HormoneIndicationValue = "("
					+ EP.ORXPath("#BaselineRecord_GrowthHormoneIndicationOptions_RB")
					+ "[" + EP.RandomValue(1, 4) + "]//input)";

			EP.Rbtn_SetOption(EthnicityValue, "ON");
			EP.Rbtn_SetOption(HormoneIndicationValue, "ON");
			EP.Btn_Click("#General_First_BN");
			break;

		case HUNGARY:
			EthnicityValue = "("
					+ EP.ORXPath("#BaselineRecord_EthnicityOptions_RB") + "["
					+ EP.RandomValue(1, 3) + "]//input)";
			HormoneIndicationValue = "("
					+ EP.ORXPath("#BaselineRecord_GrowthHormoneIndicationOptions_RB")
					+ "[" + EP.RandomValue(1, 4) + "]//input)";

			EP.Rbtn_SetOption(EthnicityValue, "ON");
			EP.Rbtn_SetOption(HormoneIndicationValue, "ON");
			EP.Btn_Click("#General_First_BN");
			break;

		case NORWAY:
			EthnicityValue = "("
					+ EP.ORXPath("#BaselineRecord_EthnicityOptions_RB") + "["
					+ EP.RandomValue(1, 3) + "]//input)";
			HormoneIndicationValue = "("
					+ EP.ORXPath("#BaselineRecord_GrowthHormoneIndicationOptions_RB")
					+ "[" + EP.RandomValue(1, 4) + "]//input)";

			EP.Rbtn_SetOption(EthnicityValue, "ON");
			EP.Rbtn_SetOption(HormoneIndicationValue, "ON");
			EP.Btn_Click("#General_First_BN");

			break;

		case CANADA:
			EP.DrpDwn_SelectIndex("#BaselineRecord_EthnicityCA_DD",
					EP.RandomValue(1, 5));
			EP.DrpDwn_SelectIndex(
					"#BaselineRecord_GrowthHormoneIndicationCA_DD",
					EP.RandomValue(1, 4));
			EP.Btn_Click("#General_First_BN");
			break;

		// case CHINA:
		case GERMANY:
		case KOREA:
			EP.Rbtn_SetOption("#BaselineRecord_Caucasian_RB", "ON");
			EP.Rbtn_SetOption("#BaselineRecord_TurnerSyndrome_RB", "ON");
			EP.Btn_Click("#General_First_BN");
			break;

		case AUSTRALIA:
			EthnicityValue = "("
					+ EP.ORXPath("#BaselineRecord_EthnicityOptions_RB") + "["
					+ EP.RandomValue(1, 3) + "]//input)";
			HormoneIndicationValue = "("
					+ EP.ORXPath("#BaselineRecord_GrowthHormoneIndicationOptions_RB")
					+ "[" + EP.RandomValue(1, 2) + "]//input)";

			EP.Rbtn_SetOption(EthnicityValue, "ON");
			EP.Rbtn_SetOption(HormoneIndicationValue, "ON");
			EP.Btn_Click("#General_First_BN");
			break;

		case ICELAND:
			EthnicityValue = "("
					+ EP.ORXPath("#BaselineRecord_EthnicityOptions_RB") + "["
					+ EP.RandomValue(1, 3) + "]//input)";
			HormoneIndicationValue = "("
					+ EP.ORXPath("#BaselineRecord_GrowthHormoneIndicationOptions_RB")
					+ "[" + EP.RandomValue(1, 4) + "]//input)";

			EP.Rbtn_SetOption(EthnicityValue, "ON");
			EP.Rbtn_SetOption(HormoneIndicationValue, "ON");
			EP.Btn_Click("#General_First_BN");
			break;

		case FINLAND:
			EthnicityValue = "("
					+ EP.ORXPath("#BaselineRecord_EthnicityOptions_RB") + "["
					+ EP.RandomValue(1, 3) + "]//input)";
			HormoneIndicationValue = "("
					+ EP.ORXPath("#BaselineRecord_GrowthHormoneIndicationOptions_RB")
					+ "[" + EP.RandomValue(1, 4) + "]//input)";

			EP.Rbtn_SetOption(EthnicityValue, "ON");
			EP.Rbtn_SetOption(HormoneIndicationValue, "ON");
			EP.Btn_Click("#General_First_BN");
			break;

		case ARGENTINA:
		case COLOMBIA:
		case TAIWAN:
		case INDONESIA:

			EP.DrpDwn_SelectIndex("#BaselineRecord_EthnicityGB_DD",
					EP.RandomValue(1, 3));
			EP.DrpDwn_SelectIndex(
					"#BaselineRecord_GrowthHormoneIndicationGB_DD",
					EP.RandomValue(1, 4));
			EP.Btn_Click("#General_First_BN");
			// EthnicityValue =
			// "("+EP.ORXPath("#BaselineRecord_EthnicityOptions_RB")+"["+EP.RandomValue(1,
			// 3)+"]//input)";
			// HormoneIndicationValue =
			// "("+EP.ORXPath("#BaselineRecord_GrowthHormoneIndicationOptions_RB")+"["+EP.RandomValue(1,
			// 4)+"]//input)";
			//
			// EP.Rbtn_SetOption(EthnicityValue, "ON");
			// EP.Rbtn_SetOption(HormoneIndicationValue, "ON");
			// EP.Btn_Click("#General_First_BN");
			break;

		case SWEDEN:
			EP.DrpDwn_SelectIndex("#BaselineRecord_EthnicityCA_DD",
					EP.RandomValue(1, 5));
			EP.DrpDwn_SelectIndex("#BaselineRecord_GrowthHormoneIndication_DD",
					EP.RandomValue(1, 4));
			EP.Btn_Click("#General_First_BN");// Click save button
			break;

		default:
		}

		VerifySuccessMessage(EP.LangData("DemographicData"));

	}

	public void AuxologicalDataAdd(boolean IsValid, String TabIndex) {
		// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*AUXOLOGICAL
		// DATA(BASELINE)*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
		EP.ShowTab(TabIndex);
		String[] InputValues;

		switch (switchCountryName) {
		case UNITEDKINGDOM:

			if (IsValid) {
				InputValues = new String[] { "165", "165", "30", "2", "30",
						"4", "26", "26", "600", "1", "60.5", "30", "40", "30",
						"30" };
			} else {
				InputValues = new String[] { "180", "0", "0", "13", "0", "13",
						"0", "0", "0", "0", "0", "0", "0", "131", "131" };
			}
			EP.DrpDwn_SelectIndex("#BaselineRecord_MothersHeightGB_DD", 1);
			EP.DrpDwn_SelectIndex("#BaselineRecord_FathersHeightGB_DD", 1);
			EP.DrpDwn_SelectIndex("#BaselineRecord_MotherAgeAtDeliveryGB_DD", 1);
			EP.DrpDwn_SelectIndex("#BaselineRecord_FathersAgeAtDeliveryGB_DD",
					1);
			EP.DrpDwn_SelectIndex("#BaselineRecord_GestationalAgeGB_DD", 1);
			EP.DrpDwn_SelectIndex("#BaselineRecord_BirthLengthGB_DD", 1);
			EP.DrpDwn_SelectIndex("#BaselineRecord_BirthWeightGB_DD", 1);
			EP.DrpDwn_SelectIndex(
					"#BaselineRecord_GrowthVelocityBeforeGHStartGB_DD", 1);
			EP.DrpDwn_SelectIndex("#BaselineRecord_InitialSittingGB_DD", 1);
			EP.DrpDwn_SelectIndex("#BaselineRecord_InitialWaistGB_DD", 1);
			EP.DrpDwn_SelectIndex("#BaselineRecord_InitiaHipGB_DD", 1);
			EP.DrpDwn_SelectIndex("#BaselineRecord_HeightBeforeGHStartCA_DD", 1);
			EP.DrpDwn_SelectIndex("#BaselineRecord_WeightBeforeGHStartCA_DD", 1);

			EP.Edit_SetValue("#BaselineRecord_MothersHeightGB_EB",
					InputValues[0]);
			EP.HideCursor();
			EP.Wait(2);
			EP.Edit_SetValue("#BaselineRecord_FathersHeightHeightGB_EB",
					InputValues[1]);
			EP.Wait(2);
			EP.HideCursor();
			EP.WaitForPageload();
			EP.Edit_SetValue("#BaselineRecord_MothersAgeDeliveryYearsGB_EB",
					InputValues[2]);
			EP.Edit_SetValue("#BaselineRecord_MothersAgeDeliverMonthsGB_EB",
					InputValues[3]);
			EP.Edit_SetValue("#BaselineRecord_FathersAgeDeliveryYearsGB_EB",
					InputValues[4]);
			EP.Edit_SetValue("#BaselineRecord_FathersAgeDeliveryMonthsGB_EB",
					InputValues[5]);
			EP.Edit_SetValue("#BaselineRecord_GestationalAgeGB_EB",
					InputValues[6]);
			EP.Edit_SetValue("#BaselineRecord_BirthLengthGB_EB", InputValues[7]);
			EP.Edit_SetValue("#BaselineRecord_BirthWeightGB_EB", InputValues[8]);
			EP.Edit_SetValue("#BaselineRecord_GrowthVelocityBeforeGHGB_EB",
					InputValues[9]);
			EP.Edit_SetValue("#BaselineRecord_HeightBeforeGHGB_EB",
					EP.Converter(InputValues[10]));
			EP.Edit_SetValue("#BaselineRecord_WeightBeforeGHGB_EB",
					InputValues[11]);
			EP.Edit_SetValue("#BaselineRecord_InitialSittingGB_EB",
					InputValues[12]);
			EP.Edit_SetValueByJavaScript("#BaselineRecord_InitialWaistGB_EB",
					InputValues[13]);
			EP.Wait(2);
			EP.HideCursor();
			EP.WaitForPageload();
			EP.Edit_SetValueByJavaScript("#BaselineRecord_InitialHipGB_EB",
					InputValues[14]);
			EP.Wait(2);
			EP.HideCursor();
			EP.WaitForPageload();
			EP.Btn_Click("#General_First_BN");
			EP.WaitForPageload();
			if (!IsValid) {
				EP.Btn_Click("#General_First_BN");
			}
			break;

		case GREECE:
			if (IsValid) {
				InputValues = new String[] { "165", "165", "26", "20", "600",
						"60.1", "60", "1.1" };
			} else {
				InputValues = new String[] { "140", "140", "1", "1", "1", "1",
						"1", "0" };
			}
			EP.DrpDwn_SelectIndex("#BaselineRecord_MothersHeightIT_DD", 1);
			EP.DrpDwn_SelectIndex("#BaselineRecord_FatherssHeightIT_DD", 1);
			EP.Edit_SetValue("#BaselineRecord_MotherHeightReportedIT_EB",
					InputValues[0]);
			EP.HideCursor();
			EP.WaitForPageload();
			EP.Edit_SetValue("#BaselineRecord_FatherHeightReportedIT_EB",
					InputValues[1]);
			EP.HideCursor();
			EP.WaitForPageload();
			EP.Edit_ClearWaitAndSet(
					"#BaselineRecord_FatherHeightReportedIT_EB", InputValues[1]);
			EP.HideCursor();
			EP.WaitForPageload();
			EP.Edit_ClearWaitAndSet("#BaselineRecord_GestationalAgeIT_EB",
					InputValues[2]);
			EP.Wait(1);
			EP.Edit_ClearWaitAndSet("#BaselineRecord_BirthLengthIT_EB",
					InputValues[3]);
			EP.Wait(1);
			EP.Edit_ClearWaitAndSet("#BaselineRecord_BirthWeightIT_EB",
					InputValues[4]);
			EP.Wait(1);
			EP.Edit_ClearWaitAndSet("#BaselineRecord_HeightBeforeGHIT_EB",
					InputValues[5]);
			EP.Wait(1);
			EP.Edit_ClearWaitAndSet("#BaselineRecord_WeightBeforeGHIT_EB",
					InputValues[6]);
			EP.Wait(1);
			EP.Edit_ClearWaitAndSet(
					"#BaselineRecord_GrowthVelocityBeforeGHIT_EB",
					InputValues[7]);
			EP.Wait(1);
			EP.Btn_Click("#General_First_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_First_BN");
			}
			break;

		case SLOVAKIA:
			if (IsValid) {
				InputValues = new String[] { "165", "165", "26", "20", "600",
						"60.1", "60", "1.1" };
			} else {
				InputValues = new String[] { "140", "140", "1", "1", "1", "1",
						"1", "0" };
			}
			EP.DrpDwn_SelectIndex("#BaselineRecord_MothersHeightIT_DD", 1);
			EP.DrpDwn_SelectIndex("#BaselineRecord_FatherssHeightIT_DD", 1);
			EP.Edit_SetValue("#BaselineRecord_MotherHeightReportedIT_EB",
					InputValues[0]);
			EP.HideCursor();
			EP.WaitForPageload();
			EP.Edit_SetValue("#BaselineRecord_FatherHeightReportedIT_EB",
					InputValues[1]);
			EP.HideCursor();
			EP.WaitForPageload();
			EP.Edit_ClearWaitAndSet(
					"#BaselineRecord_FatherHeightReportedIT_EB", InputValues[1]);
			EP.HideCursor();
			EP.WaitForPageload();
			EP.Edit_ClearWaitAndSet("#BaselineRecord_GestationalAgeIT_EB",
					InputValues[2]);
			EP.Wait(1);
			EP.Edit_ClearWaitAndSet("#BaselineRecord_BirthLengthIT_EB",
					InputValues[3]);
			EP.Wait(1);
			EP.Edit_ClearWaitAndSet("#BaselineRecord_BirthWeightIT_EB",
					InputValues[4]);
			EP.Wait(1);
			EP.Edit_ClearWaitAndSet("#BaselineRecord_HeightBeforeGHIT_EB",
					InputValues[5]);
			EP.Wait(1);
			EP.Edit_ClearWaitAndSet("#BaselineRecord_WeightBeforeGHIT_EB",
					InputValues[6]);
			EP.Wait(1);
			EP.Edit_ClearWaitAndSet(
					"#BaselineRecord_GrowthVelocityBeforeGHIT_EB",
					InputValues[7]);
			EP.Wait(1);
			EP.Btn_Click("#General_First_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_First_BN");
			}
			break;

		case AUSTRIA:
			if (IsValid) {
				InputValues = new String[] { "165", "165", "26", "20", "600",
						"60.1", "60", "1.1" };
			} else {
				InputValues = new String[] { "140", "140", "1", "1", "1", "1",
						"1", "0" };
			}
			EP.DrpDwn_SelectIndex("#BaselineRecord_MothersHeightAT_DD", 1);
			EP.DrpDwn_SelectIndex("#BaselineRecord_FathersHeightAT_DD", 1);
			EP.DrpDwn_SelectIndex("#BaselineRecord_HeightBeforeGHAT_DD", 1);
			EP.Edit_SetValue("#BaselineRecord_MothersHeightAT_EB",
					InputValues[0]);
			EP.HideCursor();
			EP.Wait(2);
			EP.Edit_SetValue("#BaselineRecord_FathersHeightAT_EB",
					InputValues[1]);
			EP.HideCursor();
			EP.Wait(2);
			EP.Edit_SetValue("#BaselineRecord_GestationalAgeAT_EB",
					InputValues[2]);
			EP.Edit_SetValue("#BaselineRecord_BirthLengthAT_EB", InputValues[3]);
			EP.Edit_SetValue("#BaselineRecord_BirthWeightAT_EB", InputValues[4]);
			EP.Edit_SetValue("#BaselineRecord_HeightBeforeGHAT_EB",
					InputValues[5]);
			EP.Edit_SetValue("#BaselineRecord_WeightBeforeGHAT_EB",
					InputValues[6]);
			EP.Edit_SetValue("#BaselineRecord_GrowthVelocityBeforeGHAT_EB",
					InputValues[7]);
			EP.Btn_Click("#General_First_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_First_BN");
			}

			break;

		case UNITEDARABEMIRATES:
		case SAUDIARABIA:
			if (IsValid) {
				InputValues = new String[] { "165", "165", "26", "20", "600",
						"60.1", "60", "1.1" };
			} else {
				InputValues = new String[] { "140", "140", "1", "1", "1", "1",
						"1", "0" };
			}
			EP.DrpDwn_SelectIndex("#BaselineRecord_MothersHeightAT_DD", 1);
			EP.DrpDwn_SelectIndex("#BaselineRecord_FathersHeightAT_DD", 1);
			EP.Edit_ClearWaitAndSet("#BaselineRecord_MothersHeightAT_EB",
					InputValues[0]);
			EP.Wait(2);
			EP.HideCursor();
			EP.Edit_ClearWaitAndSet("#BaselineRecord_FathersHeightAT_EB",
					InputValues[1]);
			EP.Wait(2);
			EP.HideCursor();
			EP.WaitForPageload();
			EP.Edit_SetValue("#BaselineRecord_GestationalAgeAT_EB",
					InputValues[2]);
			EP.Edit_SetValue("#BaselineRecord_BirthLengthAT_EB", InputValues[3]);
			EP.Edit_SetValue("#BaselineRecord_BirthWeightAT_EB", InputValues[4]);
			EP.Edit_SetValue("#BaselineRecord_HeightBeforeGHAT_EB",
					EP.Converter(InputValues[5]));
			EP.Edit_SetValue("#BaselineRecord_WeightBeforeGHAT_EB",
					InputValues[6]);
			EP.Edit_SetValue("#BaselineRecord_GrowthVelocityBeforeGHAT_EB",
					EP.Converter(InputValues[7]));
			EP.Btn_Click("#General_First_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_First_BN");
			}

			break;

		case SINGAPORE:
		case MEXICO:
			if (IsValid) {
				InputValues = new String[] { "165", "165", "26", "20", "600",
						"60.1", "60", "1.1" };
			} else {
				InputValues = new String[] { "140", "140", "1", "1", "1", "1",
						"1", "0" };
			}
			EP.DrpDwn_SelectIndex("#BaselineRecord_MothersHeightAT_DD", 1);
			EP.DrpDwn_SelectIndex("#BaselineRecord_FathersHeightAT_DD", 1);
			EP.DrpDwn_SelectIndex("#BaselineRecord_HeightBeforeGHAT_DD", 1);
			EP.Edit_ClearWaitAndSet("#BaselineRecord_MothersHeightAT_EB",
					InputValues[0]);
			EP.Wait(2);
			EP.HideCursor();
			EP.Edit_ClearWaitAndSet("#BaselineRecord_FathersHeightAT_EB",
					InputValues[1]);
			EP.Wait(2);
			EP.HideCursor();
			EP.WaitForPageload();
			EP.Edit_SetValue("#BaselineRecord_GestationalAgeAT_EB",
					InputValues[2]);
			EP.Edit_SetValue("#BaselineRecord_BirthLengthAT_EB", InputValues[3]);
			EP.Edit_SetValue("#BaselineRecord_BirthWeightAT_EB", InputValues[4]);
			EP.Edit_SetValue("#BaselineRecord_HeightBeforeGHAT_EB",
					EP.Converter(InputValues[5]));
			EP.Edit_SetValue("#BaselineRecord_WeightBeforeGHAT_EB",
					InputValues[6]);
			EP.Edit_SetValue("#BaselineRecord_GrowthVelocityBeforeGHAT_EB",
					EP.Converter(InputValues[7]));
			EP.Btn_Click("#General_First_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_First_BN");
			}

			break;
		case SPAIN:
			if (IsValid) {
				InputValues = new String[] { "165", "165", "26", "20", "600",
						"60.1", "60", "1.1" };
			} else {
				InputValues = new String[] { "140", "140", "1", "1", "1", "1",
						"1", "0" };
			}
			EP.Edit_SetValue("#BaselineRecord_MothersHeightES_EB",
					InputValues[0]);
			EP.HideCursor();
			EP.Wait(2);
			EP.Edit_SetValue("#BaselineRecord_FathersHeightES_EB",
					InputValues[1]);
			EP.HideCursor();
			EP.WaitForPageload();
			EP.Edit_SetValue("#BaselineRecord_GestationalAgeES_EB",
					InputValues[2]);
			EP.Edit_SetValue("#BaselineRecord_BirthLengthES_EB", InputValues[3]);
			EP.Edit_SetValue("#BaselineRecord_BirthWeightES_EB", InputValues[4]);
			EP.Edit_SetValue("#BaselineRecord_HeightBeforeGHES_EB",
					EP.Converter(InputValues[5]));
			EP.Edit_SetValue("#BaselineRecord_WeightBeeforeGHES_EB",
					InputValues[6]);
			EP.Edit_SetValue("#BaselineRecord_GrowthVelocityBeforeGHES_EB",
					EP.Converter(InputValues[7]));
			EP.Btn_Click("#General_First_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_First_BN");
			}
			break;
		case ARGENTINA:
			if (IsValid) {
				InputValues = new String[] { "165", "165", "26", "20", "600",
						"60.1", "60", "1.1" };
			} else {
				InputValues = new String[] { "140", "140", "1", "1", "1", "1",
						"1", "0" };
			}
			EP.DrpDwn_SelectIndex("#BaselineRecord_MothersHeightAT_DD", 1);
			EP.DrpDwn_SelectIndex("#BaselineRecord_FathersHeightAT_DD", 2);
			EP.Edit_SetValue("#BaselineRecord_MothersHeightES_EB",
					InputValues[0]);
			EP.HideCursor();
			EP.Wait(2);
			EP.Edit_SetValue("#BaselineRecord_FathersHeightMeasuredAT_EB",
					InputValues[1]);
			EP.HideCursor();
			EP.WaitForPageload();
			EP.Edit_SetValue("#BaselineRecord_GestationalAgeES_EB",
					InputValues[2]);
			EP.Edit_SetValue("#BaselineRecord_BirthLengthES_EB", InputValues[3]);
			EP.Edit_SetValue("#BaselineRecord_BirthWeightES_EB", InputValues[4]);
			EP.Edit_SetValue("#BaselineRecord_HeightBeforeGHES_EB",
					EP.Converter(InputValues[5]));
			EP.Edit_SetValue("#BaselineRecord_WeightBeeforeGHES_EB",
					InputValues[6]);
			EP.Edit_SetValue("#BaselineRecord_GrowthVelocityBeforeGHES_EB",
					EP.Converter(InputValues[7]));
			EP.Btn_Click("#General_First_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_First_BN");
			}
			break;
		case COLOMBIA:
		case TAIWAN:
		case INDONESIA:

			if (IsValid) {
				InputValues = new String[] { "165", "165", "26", "20", "600",
						"60.1", "60", "1.1" };
			} else {
				InputValues = new String[] { "140", "140", "1", "1", "1", "1",
						"1", "0" };
			}
			EP.DrpDwn_SelectIndex("#BaselineRecord_MothersHeightSE_DD", 1);
			EP.DrpDwn_SelectIndex("#BaselineRecord_FathersHeightSE_DD", 2);
			EP.Edit_SetValue("#BaselineRecord_MothersHeightES_EB",
					InputValues[0]);
			EP.HideCursor();
			EP.Wait(2);
			EP.Edit_SetValue("#BaselineRecord_FathersHeightAT_EB",
					InputValues[1]);
			EP.HideCursor();
			EP.WaitForPageload();
			EP.Edit_SetValue("#BaselineRecord_GestationalAgeES_EB",
					InputValues[2]);
			EP.Edit_SetValue("#BaselineRecord_BirthLengthES_EB", InputValues[3]);
			EP.Edit_SetValue("#BaselineRecord_BirthWeightES_EB", InputValues[4]);
			EP.Edit_SetValue("#BaselineRecord_HeightBeforeGHES_EB",
					EP.Converter(InputValues[5]));
			EP.Edit_SetValue("#BaselineRecord_WeightBeeforeGHES_EB",
					InputValues[6]);
			EP.Edit_SetValue("#BaselineRecord_GrowthVelocityBeforeGHES_EB",
					EP.Converter(InputValues[7]));
			EP.Btn_Click("#General_First_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_First_BN");
			}
			break;
		case FRANCE:
			if (IsValid) {
				InputValues = new String[] { "165", "165", "26", "20", "600",
						"60.1", "60", "1.1" };
			} else {
				InputValues = new String[] { "200", "200", "1", "1", "1", "1",
						"1", "0" };
			}
			EP.DrpDwn_SelectIndex("#BaselineRecord_MothersHeightFR_DD", 1);
			EP.DrpDwn_SelectIndex("#BaselineRecord_FathersHeightFR_DD", 1);
			EP.Edit_SetValue("#BaselineRecord_MothersHeightFR_EB",
					InputValues[0]);
			EP.HideCursor();
			EP.Wait(2);
			EP.Edit_SetValue("#BaselineRecord_FathersHeightFR_EB",
					InputValues[1]);
			EP.HideCursor();
			EP.WaitForPageload();
			EP.Edit_SetValue("#BaselineRecord_GestationalAgeFR_EB",
					InputValues[2]);
			EP.Edit_SetValue("#BaselineRecord_BirthLengthFR_EB", InputValues[3]);
			EP.Edit_SetValue("#BaselineRecord_BirthWeightFR_EB", InputValues[4]);
			EP.Edit_SetValue("#BaselineRecord_HeightBeforeGHFR_EB",
					EP.Converter(InputValues[5]));
			EP.Edit_SetValue("#BaselineRecord_WeightBeforeGHFR_EB",
					InputValues[6]);
			EP.Edit_SetValue("#BaselineRecord_GrowthVelocityBeforeGHFR_EB",
					EP.Converter(InputValues[7]));
			EP.Btn_Click("#General_First_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_First_BN");
			}
			break;

		// case CHINA:
		case AUSTRALIA:
			if (IsValid) {
				InputValues = new String[] { "165", "165", "26", "20.1", "600",
						"60.1", "60", "1.1" };
			} else {
				InputValues = new String[] { "140", "140", "1", "1", "1", "1",
						"1", "0" };
			}
			EP.Edit_SetValue("#BaselineRecord_MothersHeight_EB", InputValues[0]);
			EP.HideCursor();
			EP.WaitForPageload();
			EP.Edit_SetValueByJavaScript("#BaselineRecord_FathersHeight_EB",
					InputValues[1]);
			EP.HideCursor();

			if (EP.CompareText(Env.GetBrowserUsed(), "OP")) {
				EP.Wait(Env.ResponseTimeOut * 5); // Slow in Opera
				EP.WaitForPageload();
			}
			EP.Wait(2);
			EP.Edit_SetValue("#BaselineRecord_GestationalAge_EB",
					InputValues[2]);
			EP.Edit_SetValue("#BaselineRecord_BirthLength_EB",
					EP.Converter(InputValues[3]));
			EP.Edit_SetValue("#BaselineRecord_BirthWeight_EB", InputValues[4]);
			EP.Edit_SetValue("#BaselineRecord_HeightBeforeGH_EB",
					EP.Converter(InputValues[5]));
			EP.Edit_SetValue("#BaselineRecord_WeightBeeforeGHES_EB",
					InputValues[6]);
			EP.Edit_SetValue("#BaselineRecord_GrowthVelocityBeforeGH_EB",
					EP.Converter(InputValues[7]));
			EP.Btn_Click("#General_First_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_First_BN");// Click first save button
			}
			break;

		case KOREA:
			if (IsValid) {
				InputValues = new String[] { "165", "165", "26", "20", "600",
						"60", "60", "1" };
			} else {
				InputValues = new String[] { "140", "140", "1", "1", "1", "1",
						"1", "0.1" };
			}
			EP.Edit_SetValue("#BaselineRecord_MothersHeight_EB", InputValues[0]);
			EP.HideCursor();
			EP.WaitForPageload();
			EP.Edit_SetValueByJavaScript("#BaselineRecord_FathersHeight_EB",
					InputValues[1]);
			EP.HideCursor();

			if (EP.CompareText(Env.GetBrowserUsed(), "OP")) {
				EP.Wait(Env.ResponseTimeOut * 5); // Slow in Opera
				EP.WaitForPageload();
			}
			EP.Wait(2);
			EP.Edit_SetValue("#BaselineRecord_GestationalAge_EB",
					InputValues[2]);
			EP.Edit_SetValue("#BaselineRecord_BirthLength_EB", InputValues[3]);
			EP.Edit_SetValue("#BaselineRecord_BirthWeight_EB", InputValues[4]);
			EP.Edit_SetValue("#BaselineRecord_HeightBeforeGH_EB",
					InputValues[5]);
			EP.Edit_SetValue("#BaselineRecord_WeightBeeforeGHES_EB",
					InputValues[6]);
			EP.Edit_SetValue("#BaselineRecord_GrowthVelocityBeforeGH_EB",
					InputValues[7]);
			if (IsValid) {
				EP.Btn_Click("#General_First_BN");// Click first save button
													// //click save button
			} else {
				EP.Btn_Click("#General_First_BN");// Click first save button
				EP.Btn_Click("#General_First_BN");// Click first save button
			}
			break;

		case ICELAND:
			if (IsValid) {
				InputValues = new String[] { "165", "165", "26", "20.1", "600",
						"60.1", "60", "1.1" };
			} else {
				InputValues = new String[] { "140", "140", "1", "1", "1", "1",
						"1", "0" };
			}
			EP.Edit_SetValue("#BaselineRecord_MothersHeight_EB", InputValues[0]);
			EP.HideCursor();
			EP.WaitForPageload();
			EP.Edit_SetValueByJavaScript("#BaselineRecord_FathersHeight_EB",
					InputValues[1]);
			EP.HideCursor();

			if (EP.CompareText(Env.GetBrowserUsed(), "OP")) {
				EP.Wait(Env.ResponseTimeOut * 5); // Slow in Opera
				EP.WaitForPageload();
			}
			EP.Wait(2);
			EP.Edit_SetValue("#BaselineRecord_GestationalAge_EB",
					InputValues[2]);
			EP.Edit_SetValue("#BaselineRecord_BirthLength_EB",
					EP.Converter(InputValues[3]));
			EP.Edit_SetValue("#BaselineRecord_BirthWeight_EB", InputValues[4]);
			EP.Edit_SetValue("#BaselineRecord_HeightBeforeGH_EB",
					EP.Converter(InputValues[5]));
			EP.Edit_SetValue("#BaselineRecord_WeightBeeforeGHES_EB",
					InputValues[6]);
			EP.Edit_SetValue("#BaselineRecord_GrowthVelocityBeforeGH_EB",
					InputValues[7]);
			EP.Btn_Click("#General_First_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_First_BN");// Click first save button
			}
			break;

		case FINLAND:
			if (IsValid) {
				InputValues = new String[] { "165", "165", "26", "20.1", "600",
						"60.1", "60", "1.1" };
			} else {
				InputValues = new String[] { "140", "140", "1", "1", "1", "1",
						"1", "0" };
			}
			EP.Edit_SetValue("#BaselineRecord_MothersHeight_EB", InputValues[0]);
			EP.HideCursor();
			EP.WaitForPageload();
			EP.Edit_SetValueByJavaScript("#BaselineRecord_FathersHeight_EB",
					InputValues[1]);
			EP.HideCursor();
			EP.Wait(3);

			if (EP.CompareText(Env.GetBrowserUsed(), "OP")) {
				EP.Wait(Env.ResponseTimeOut * 5); // Slow in Opera
				EP.WaitForPageload();
			}
			EP.Edit_SetValue("#BaselineRecord_GestationalAge_EB",
					InputValues[2]);
			EP.Edit_SetValue("#BaselineRecord_BirthLength_EB",
					EP.Converter(InputValues[3]));
			EP.Edit_SetValue("#BaselineRecord_BirthWeight_EB", InputValues[4]);
			EP.Edit_SetValue("#BaselineRecord_HeightBeforeGH_EB",
					EP.Converter(InputValues[5]));
			EP.Edit_SetValue("#BaselineRecord_WeightBeeforeGHES_EB",
					InputValues[6]);
			EP.Edit_SetValue("#BaselineRecord_GrowthVelocityBeforeGH_EB",
					InputValues[7]);
			EP.Btn_Click("#General_First_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_First_BN");// Click first save button
			}
			break;

		// case ARGENTINA:
		case GERMANY:

			if (IsValid) {
				InputValues = new String[] { "165", "165", "26", "20", "600",
						"60", "1" };
			} else {
				InputValues = new String[] { "140", "140", "1", "1", "1", "1",
						"4" };
			}
			EP.Edit_SetValue("#BaselineRecord_MothersHeight_EB", InputValues[0]);
			EP.HideCursor();
			EP.WaitForPageload();
			EP.Edit_SetValueByJavaScript("#BaselineRecord_FathersHeight_EB",
					InputValues[1]);
			EP.HideCursor();

			if (EP.CompareText(Env.GetBrowserUsed(), "OP")) {
				EP.Wait(Env.ResponseTimeOut * 5); // Slow in Opera
				EP.WaitForPageload();
			}
			EP.Wait(2);
			EP.Edit_SetValue("#BaselineRecord_GestationalAge_EB",
					InputValues[2]);
			EP.Edit_SetValue("#BaselineRecord_BirthLength_EB", InputValues[3]);
			EP.Edit_SetValue("#BaselineRecord_BirthWeight_EB", InputValues[4]);
			EP.Edit_SetValue("#BaselineRecord_HeightBeforeGH_EB",
					InputValues[5]);
			EP.Edit_SetValue("#BaselineRecord_GrowthVelocityBeforeGH_EB",
					InputValues[6]);
			if (IsValid) {
				EP.Btn_Click("#General_First_BN");// Click first save button
													// //click save button
			} else {
				EP.Btn_Click("#General_First_BN");// Click first save button
				EP.Btn_Click("#General_First_BN");// Click first save button
			}
			break;

		case CANADA:
			if (IsValid) {
				InputValues = new String[] { "165", "165", "26", "20.1", "600",
						"60.1", "60", "1.1" };
			} else {
				InputValues = new String[] { "140", "140", "1", "1", "1", "1",
						"1", "0" };
			}
			EP.DrpDwn_SelectIndex("#BaselineRecord_MothersHeightCA_DD", 1);
			EP.DrpDwn_SelectIndex("#BaselineRecord_FathersHeightCA_DD", 1);
			EP.DrpDwn_SelectIndex("#BaselineRecord_HeightBeforeGHStartCA_DD", 1);
			EP.DrpDwn_SelectIndex("#BaselineRecord_WeightBeforeGHStartCA_DD", 1);
			EP.DrpDwn_SelectIndex(
					"#BaselineRecord_GrowthVelocityBeforeGHStartCA_DD", 1);
			EP.Edit_ClearWaitAndSet("#BaselineRecord_MothersHeight_EB",
					InputValues[0]);
			EP.HideCursor();
			EP.Wait(2);
			EP.Edit_ClearWaitAndSet("#BaselineRecord_FathersHeight_EB",
					InputValues[1]);
			EP.HideCursor();
			EP.Wait(2);
			EP.WaitForPageload();
			EP.Edit_SetValue("#BaselineRecord_GestationalAge_EB",
					InputValues[2]);
			EP.Edit_SetValue("#BaselineRecord_BirthLength_EB",
					EP.Converter(InputValues[3]));
			EP.Edit_SetValue("#BaselineRecord_BirthWeight_EB", InputValues[4]);
			EP.Wait(2);
			EP.Edit_SetValue("#BaselineRecord_HeightBeforeGH_EB",
					EP.Converter(InputValues[5]));
			EP.Edit_SetValue("#BaselineRecord_WeightBeforeGHCA_EB",
					InputValues[6]);
			EP.Edit_SetValue("#BaselineRecord_GrowthVelocityBeforeGH_EB",
					EP.Converter(InputValues[7]));
			EP.Btn_Click("#General_First_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_First_BN");
			}
			break;

		case CZECHREPUBLIC:
			if (IsValid) {
				InputValues = new String[] { "165", "165", "26", "20.1", "600",
						"60.1", "60", "1.1" };
			} else {
				InputValues = new String[] { "140", "140", "1", "1", "1", "1",
						"1", "0" };
			}
			EP.Edit_SetValue("#BaselineRecord_MothersHeight_EB", InputValues[0]);
			EP.Wait(2);
			EP.HideCursor();
			EP.Edit_SetValueByJavaScript("#BaselineRecord_FathersHeight_EB",
					InputValues[1]);
			EP.HideCursor();
			EP.WaitForPageload();
			EP.Edit_SetValue("#BaselineRecord_GestationalAge_EB",
					InputValues[2]);
			EP.Edit_SetValue("#BaselineRecord_BirthLength_EB", InputValues[3]);
			EP.Edit_SetValue("#BaselineRecord_BirthWeight_EB", InputValues[4]);
			EP.Edit_SetValue("#BaselineRecord_HeightBeforeGH_EB",
					InputValues[5]);
			EP.Edit_SetValue("#BaselineRecord_WeightBeforeGHCA_EB",
					InputValues[6]);
			EP.Edit_SetValue("#BaselineRecord_GrowthVelocityBeforeGH_EB",
					InputValues[7]);
			EP.Btn_Click("#General_First_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_First_BN");
			}

			break;

		case HUNGARY:
			if (IsValid) {
				InputValues = new String[] { "165", "165", "26", "20.1", "600",
						"60.1", "60", "1.1" };
			} else {
				InputValues = new String[] { "140", "140", "1", "1", "1", "1",
						"1", "0" };
			}
			EP.Edit_SetValue("#BaselineRecord_MothersHeight_EB", InputValues[0]);
			EP.HideCursor();
			EP.WaitForPageload();
			EP.Edit_SetValueByJavaScript("#BaselineRecord_FathersHeight_EB",
					InputValues[1]);
			EP.HideCursor();
			EP.WaitForPageload();
			EP.Edit_SetValue("#BaselineRecord_GestationalAge_EB",
					InputValues[2]);
			EP.Edit_SetValue("#BaselineRecord_BirthLength_EB", InputValues[3]);
			EP.Edit_SetValue("#BaselineRecord_BirthWeight_EB", InputValues[4]);
			EP.Edit_SetValue("#BaselineRecord_HeightBeforeGH_EB",
					InputValues[5]);
			EP.Edit_SetValue("#BaselineRecord_WeightBeforeGHNO_EB",
					InputValues[6]);
			EP.Edit_SetValue("#BaselineRecord_GrowthVelocityBeforeGH_EB",
					InputValues[7]);
			EP.Btn_Click("#General_First_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_First_BN");
			}

			break;

		case NORWAY:
			if (IsValid) {
				InputValues = new String[] { "165", "165", "26", "20.1", "600",
						"60.1", "60", "1.1" };
			} else {
				InputValues = new String[] { "140", "140", "1", "1", "1", "1",
						"1", "0" };
			}
			EP.Edit_SetValue("#BaselineRecord_MothersHeight_EB", InputValues[0]);
			EP.HideCursor();
			EP.WaitForPageload();
			EP.Edit_SetValue("#BaselineRecord_FathersHeight_EB", InputValues[1]);
			EP.HideCursor();
			EP.WaitForPageload();
			EP.Edit_SetValue("#BaselineRecord_GestationalAge_EB",
					InputValues[2]);
			EP.Edit_SetValue("#BaselineRecord_BirthLength_EB", InputValues[3]);
			EP.Edit_SetValue("#BaselineRecord_BirthWeight_EB", InputValues[4]);
			EP.Edit_SetValue("#BaselineRecord_HeightBeforeGH_EB",
					InputValues[5]);
			EP.Edit_SetValue("#BaselineRecord_WeightBeforeGHNO_EB",
					InputValues[6]);
			EP.Edit_SetValue("#BaselineRecord_GrowthVelocityBeforeGH_EB",
					InputValues[7]);
			EP.Btn_Click("#General_First_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_First_BN");
			}
			break;

		case SWEDEN:
			if (IsValid) {
				InputValues = new String[] { "165", "165", "26", "20", "600",
						"60", "1", "60", "40", "20", "21" };
			} else {
				InputValues = new String[] { "140", "140", "1", "1", "1", "1",
						"4", "1", "1", "1", "1" };
			}
			EP.DrpDwn_SelectIndex("#BaselineRecord_MothersHeightSE_DD", 1);
			EP.DrpDwn_SelectIndex("#BaselineRecord_FathersHeightSE_DD", 1);
			EP.DrpDwn_SelectIndex("#BaselineRecord_GestationalAgeGB_DD", 1);
			EP.DrpDwn_SelectIndex("#BaselineRecord_BirthLengthGB_DD", 1);
			EP.DrpDwn_SelectIndex("#BaselineRecord_BirthWeightGB_DD", 1);
			EP.DrpDwn_SelectIndex(
					"#BaselineRecord_GrowthVelocityBeforeGHStartGB_DD", 1);
			EP.DrpDwn_SelectIndex("#BaselineRecord_InitialSittingGB_DD", 1);
			EP.DrpDwn_SelectIndex("#BaselineRecord_InitialWaistGB_DD", 1);
			EP.DrpDwn_SelectIndex("#BaselineRecord_InitiaHipGB_DD", 1);
			EP.DrpDwn_SelectIndex("#BaselineRecord_HeightBeforeGHStartCA_DD", 1);
			EP.DrpDwn_SelectIndex("#BaselineRecord_WeightBeforeGHStartCA_DD", 1);
			EP.Edit_SetValue("#BaselineRecord_MothersHeight_EB", InputValues[0]);
			EP.HideCursor();
			EP.Wait(2);
			EP.Edit_SetValueByJavaScript("#BaselineRecord_FathersHeight_EB",
					InputValues[1]);
			EP.HideCursor();
			EP.WaitForPageload();
			EP.Edit_SetValue("#BaselineRecord_GestationalAge_EB",
					InputValues[2]);
			EP.Edit_SetValue("#BaselineRecord_BirthLength_EB", InputValues[3]);
			EP.Edit_SetValue("#BaselineRecord_BirthWeight_EB", InputValues[4]);
			EP.Edit_SetValue("#BaselineRecord_HeightBeforeGH_EB",
					InputValues[5]);
			EP.Edit_SetValue("#BaselineRecord_GrowthVelocityBeforeGH_EB",
					InputValues[6]);
			EP.Edit_SetValue("#BaselineRecord_WeightBeforeGHGB_EB",
					InputValues[7]);
			EP.Edit_SetValue("#BaselineRecord_InitialSittingSeatedHeight_EB",
					InputValues[8]);
			EP.Edit_ClearValue("#BaselineRecord_InitialWaistMeasurement_EB");
			EP.WaitForPageload();
			EP.Edit_SetValue("#BaselineRecord_InitialWaistMeasurement_EB",
					InputValues[9]);
			EP.Edit_ClearValue("#BaselineRecord_InitialHipMeasurement_EB");
			EP.WaitForPageload();
			EP.Edit_SetValue("#BaselineRecord_InitialHipMeasurement_EB",
					InputValues[10]);
			EP.HideCursor();
			EP.WaitForPageload();
			EP.Btn_Click("#General_First_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_First_BN");
			}

			break;

		case ITALY:
			if (IsValid) {
				InputValues = new String[] { "165", "165", "26", "20", "600",
						"60", "60", "1" };
			} else {
				InputValues = new String[] { "140", "140", "1", "1", "1", "1",
						"0", "0" };
			}
			EP.DrpDwn_SelectIndex("#BaselineRecord_MothersHeightIT_DD", 1);
			EP.DrpDwn_SelectIndex("#BaselineRecord_FatherssHeightIT_DD", 1);
			EP.Edit_SetValue("#BaselineRecord_MotherHeightReportedIT_EB",
					InputValues[0]);
			EP.HideCursor();
			EP.Wait(3);
			EP.Edit_SetValue("#BaselineRecord_FatherHeightReportedIT_EB",
					InputValues[1]);
			EP.HideCursor();
			EP.WaitForPageload();
			EP.Edit_SetValue("#BaselineRecord_GestationalAgeIT_EB",
					InputValues[2]);
			EP.Edit_SetValue("#BaselineRecord_BirthLengthIT_EB", InputValues[3]);
			EP.Edit_SetValue("#BaselineRecord_BirthWeightIT_EB", InputValues[4]);
			EP.Edit_SetValue("#BaselineRecord_HeightBeforeGHIT_EB",
					InputValues[5]);
			EP.Edit_SetValue("#BaselineRecord_WeightBeforeGHIT_EB",
					InputValues[6]);
			EP.Edit_SetValue("#BaselineRecord_GrowthVelocityBeforeGHIT_EB",
					InputValues[7]);
			EP.Btn_Click("#General_First_BN");// Click save
			if (!IsValid) {
				EP.Btn_Click("#General_First_BN");// Click save
			}
			break;

		default:
		}
		VerifySuccessMessage(EP.LangData("AuxologicalDataBaseline"));
	}

	public void PatientSupportProgramAdd(boolean IsValid, String TabIndex,
			String PatientDOB) {
		// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*PATIENT SUPPPORT
		// PROGRAM*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
		EP.ShowTab(TabIndex);
		switch (switchCountryName) {
		case UNITEDKINGDOM:
			EP.DrpDwn_SelectIndex("#BaselineRecord_ParticipationGB_DB", 1);
			EP.ChkBx_SetOption("#BaselineRecord_OnGoing_CB", "ON");
			EP.SelectDate("#BaselineRecord_DateInGB_IM", EP.Date_AddDays(
					EP.TestData("ECOSPatientDOB"), 2, Calendar.YEAR));
			EP.HideCursor();
			EP.Wait(2);
			EP.Btn_Click("#General_First_BN");
			break;

		default:
		}
		VerifySuccessMessage(EP.LangData("PatientSupportProgram"));

		// EP.CompareText("#BaselineRecord_DataUpdateSuccess_DT",EP.LangData("PatientSupportProgram")
		// +" " +
		// EP.LangData("DataUpdated"),EP.GetText("#BaselineRecord_DataUpdateSuccess_DT"));
	}

	public void MedicalHistoryAdd(boolean IsValid, String TabIndex) {
		// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*MEDICAL
		// HISTORY*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
		String MedicalHistoryOption = "";
		String ConcomitantTreatmentOption = "";
		int Random = 0;

		EP.ShowTab(TabIndex);
		switch (switchCountryName) {
		case UNITEDKINGDOM:
			EP.ChkBx_SetOptionForAll(
					EP.ORXPath("#BaselineRecord_MedicalHistoryOptions_RB"),
					"OFF");
			EP.ChkBx_SetOptionForAll(
					EP.ORXPath("#BaselineRecord_ConcomitantTreatmentsOptions_RB"),
					"OFF");
			Random = EP.RandomValue(1, 5);
			MedicalHistoryOption = EP
					.ORXPath("#BaselineRecord_MedicalHistoryOptions_RB")
					+ "["
					+ Random + "]";
			Random = EP.RandomValue(1, 10);
			ConcomitantTreatmentOption = EP
					.ORXPath("#BaselineRecord_ConcomitantTreatmentsOptions_RB")
					+ "[" + Random + "]";
			EP.ChkBx_SetOption(MedicalHistoryOption, "ON");
			EP.ChkBx_SetOption(ConcomitantTreatmentOption, "ON");
			EP.Btn_Click("#General_First_BN");
			break;

		case AUSTRIA:
			EP.ChkBx_SetOptionForAll(
					EP.ORXPath("#BaselineRecord_MedicalHistoryOptions_RB"),
					"OFF");
			EP.ChkBx_SetOptionForAll(
					EP.ORXPath("#BaselineRecord_ConcomitantTreatmentsOptions_RB"),
					"OFF");
			Random = EP.RandomValue(1, 10);
			if (Random == 9)
				++Random;
			MedicalHistoryOption = EP
					.ORXPath("#BaselineRecord_MedicalHistoryOptions_RB")
					+ "["
					+ Random + "]";
			Random = EP.RandomValue(1, 13);
			if (Random == 6 || Random == 12)
				--Random;
			ConcomitantTreatmentOption = EP
					.ORXPath("#BaselineRecord_ConcomitantTreatmentsOptions_RB")
					+ "[" + Random + "]";
			EP.ChkBx_SetOption(MedicalHistoryOption, "ON");
			EP.ChkBx_SetOption(ConcomitantTreatmentOption, "ON");
			EP.Btn_Click("#General_First_BN");
			break;

		case MEXICO:
		case SINGAPORE:
		case UNITEDARABEMIRATES:
		case SAUDIARABIA:
			EP.ChkBx_SetOptionForAll(
					EP.ORXPath("#BaselineRecord_MedicalHistoryOptions_RB"),
					"OFF");
			EP.ChkBx_SetOptionForAll(
					EP.ORXPath("#BaselineRecord_ConcomitantTreatmentsOptions_RB"),
					"OFF");
			Random = EP.RandomValue(1, 10);
			if (Random == 9)
				++Random;
			MedicalHistoryOption = EP
					.ORXPath("#BaselineRecord_MedicalHistoryOptions_RB")
					+ "["
					+ Random + "]";
			Random = EP.RandomValue(1, 13);
			if (Random == 6 || Random == 12)
				--Random;
			ConcomitantTreatmentOption = EP
					.ORXPath("#BaselineRecord_ConcomitantTreatmentsOptions_RB")
					+ "[" + Random + "]";
			EP.ChkBx_SetOption(MedicalHistoryOption, "ON");
			EP.ChkBx_SetOption(ConcomitantTreatmentOption, "ON");
			EP.Btn_Click("#General_First_BN");
			break;

		case SPAIN:
			EP.ChkBx_SetOptionForAll(
					EP.ORXPath("#BaselineRecord_MedicalHistoryOptions_RB"),
					"OFF");
			EP.ChkBx_SetOptionForAll(
					EP.ORXPath("#BaselineRecord_ConcomitantTreatmentsOptions_RB"),
					"OFF");
			Random = EP.RandomValue(1, 10);
			if (Random == 9)
				++Random;
			MedicalHistoryOption = EP
					.ORXPath("#BaselineRecord_MedicalHistoryOptions_RB")
					+ "["
					+ Random + "]";
			Random = EP.RandomValue(1, 13);
			if (Random == 6 || Random == 12)
				--Random;
			ConcomitantTreatmentOption = EP
					.ORXPath("#BaselineRecord_ConcomitantTreatmentsOptions_RB")
					+ "[" + Random + "]";
			EP.ChkBx_SetOption(MedicalHistoryOption, "ON");
			EP.ChkBx_SetOption(ConcomitantTreatmentOption, "ON");
			EP.Btn_Click("#General_First_BN");
			break;

		case SLOVAKIA:
			EP.ChkBx_SetOptionForAll(
					EP.ORXPath("#BaselineRecord_MedicalHistoryOptions_RB"),
					"OFF");
			EP.ChkBx_SetOptionForAll(
					EP.ORXPath("#BaselineRecord_ConcomitantTreatmentsOptions_RB"),
					"OFF");
			Random = EP.RandomValue(1, 10);
			if (Random == 9)
				++Random;
			MedicalHistoryOption = EP
					.ORXPath("#BaselineRecord_MedicalHistoryOptions_RB")
					+ "["
					+ Random + "]";
			Random = EP.RandomValue(1, 13);
			if (Random == 6 || Random == 12)
				--Random;
			ConcomitantTreatmentOption = EP
					.ORXPath("#BaselineRecord_ConcomitantTreatmentsOptions_RB")
					+ "[" + Random + "]";
			EP.ChkBx_SetOption(MedicalHistoryOption, "ON");
			EP.ChkBx_SetOption(ConcomitantTreatmentOption, "ON");
			EP.Btn_Click("#General_First_BN");

			break;

		case FRANCE:
			EP.ChkBx_SetOptionForAll(
					EP.ORXPath("#BaselineRecord_MedicalHistoryOptions_RB"),
					"OFF");
			EP.ChkBx_SetOptionForAll(
					EP.ORXPath("#BaselineRecord_ConcomitantTreatmentsOptions_RB"),
					"OFF");
			Random = EP.RandomValue(1, 10);
			if (Random == 9)
				++Random;
			MedicalHistoryOption = EP
					.ORXPath("#BaselineRecord_MedicalHistoryOptions_RB")
					+ "["
					+ Random + "]";
			Random = EP.RandomValue(1, 13);
			if (Random == 6 || Random == 12)
				--Random;
			ConcomitantTreatmentOption = EP
					.ORXPath("#BaselineRecord_ConcomitantTreatmentsOptions_RB")
					+ "[" + Random + "]";
			EP.ChkBx_SetOption(MedicalHistoryOption, "ON");
			EP.ChkBx_SetOption(ConcomitantTreatmentOption, "ON");
			EP.Btn_Click("#General_First_BN");
			break;

		case CZECHREPUBLIC:
			EP.ChkBx_SetOptionForAll(
					EP.ORXPath("#BaselineRecord_MedicalHistoryOptions_RB"),
					"OFF");
			EP.ChkBx_SetOptionForAll(
					EP.ORXPath("#BaselineRecord_ConcomitantTreatmentsOptions_RB"),
					"OFF");
			Random = EP.RandomValue(1, 10);
			if (Random == 9)
				++Random;
			MedicalHistoryOption = EP
					.ORXPath("#BaselineRecord_MedicalHistoryOptions_RB")
					+ "["
					+ Random + "]";
			Random = EP.RandomValue(1, 13);
			if (Random == 6 || Random == 12)
				--Random;
			ConcomitantTreatmentOption = EP
					.ORXPath("#BaselineRecord_ConcomitantTreatmentsOptions_RB")
					+ "[" + Random + "]";
			EP.ChkBx_SetOption(MedicalHistoryOption, "ON");
			EP.ChkBx_SetOption(ConcomitantTreatmentOption, "ON");
			EP.Btn_Click("#General_First_BN");
			break;

		case HUNGARY:
			EP.ChkBx_SetOptionForAll(
					EP.ORXPath("#BaselineRecord_MedicalHistoryOptions_RB"),
					"OFF");
			EP.ChkBx_SetOptionForAll(
					EP.ORXPath("#BaselineRecord_ConcomitantTreatmentsOptions_RB"),
					"OFF");
			Random = EP.RandomValue(1, 10);
			if (Random == 9)
				++Random;
			MedicalHistoryOption = EP
					.ORXPath("#BaselineRecord_MedicalHistoryOptions_RB")
					+ "["
					+ Random + "]";
			Random = EP.RandomValue(1, 13);
			if (Random == 6 || Random == 12)
				--Random;
			ConcomitantTreatmentOption = EP
					.ORXPath("#BaselineRecord_ConcomitantTreatmentsOptions_RB")
					+ "[" + Random + "]";
			EP.ChkBx_SetOption(MedicalHistoryOption, "ON");
			EP.ChkBx_SetOption(ConcomitantTreatmentOption, "ON");
			EP.Btn_Click("#General_First_BN");
			break;

		case NORWAY:
			EP.ChkBx_SetOptionForAll(
					EP.ORXPath("#BaselineRecord_MedicalHistoryOptions_RB"),
					"OFF");
			EP.ChkBx_SetOptionForAll(
					EP.ORXPath("#BaselineRecord_ConcomitantTreatmentsOptions_RB"),
					"OFF");
			Random = EP.RandomValue(1, 10);
			if (Random == 9)
				++Random;
			MedicalHistoryOption = EP
					.ORXPath("#BaselineRecord_MedicalHistoryOptions_RB")
					+ "["
					+ Random + "]";
			Random = EP.RandomValue(1, 13);
			if (Random == 6 || Random == 12)
				--Random;
			ConcomitantTreatmentOption = EP
					.ORXPath("#BaselineRecord_ConcomitantTreatmentsOptions_RB")
					+ "[" + Random + "]";
			EP.ChkBx_SetOption(MedicalHistoryOption, "ON");
			EP.ChkBx_SetOption(ConcomitantTreatmentOption, "ON");
			EP.Btn_Click("#General_First_BN");
			break;

		case ITALY:
			EP.ChkBx_SetOptionForAll(
					EP.ORXPath("#BaselineRecord_MedicalHistoryOptions_RB"),
					"OFF");
			EP.ChkBx_SetOptionForAll(
					EP.ORXPath("#BaselineRecord_ConcomitantTreatmentsOptions_RB"),
					"OFF");
			Random = EP.RandomValue(1, 10);
			if (Random == 9)
				++Random;
			MedicalHistoryOption = EP
					.ORXPath("#BaselineRecord_MedicalHistoryOptions_RB")
					+ "["
					+ Random + "]";
			Random = EP.RandomValue(1, 13);
			if (Random == 6 || Random == 12)
				--Random;
			ConcomitantTreatmentOption = EP
					.ORXPath("#BaselineRecord_ConcomitantTreatmentsOptions_RB")
					+ "[" + Random + "]";
			EP.ChkBx_SetOption(MedicalHistoryOption, "ON");
			EP.ChkBx_SetOption(ConcomitantTreatmentOption, "ON");
			EP.Btn_Click("#General_First_BN");
			break;
		case GREECE:
			EP.ChkBx_SetOptionForAll(
					EP.ORXPath("#BaselineRecord_MedicalHistoryOptions_RB"),
					"OFF");
			EP.ChkBx_SetOptionForAll(
					EP.ORXPath("#BaselineRecord_ConcomitantTreatmentsOptions_RB"),
					"OFF");
			Random = EP.RandomValue(1, 10);
			if (Random == 9)
				++Random;
			MedicalHistoryOption = EP
					.ORXPath("#BaselineRecord_MedicalHistoryOptions_RB")
					+ "["
					+ Random + "]";
			Random = EP.RandomValue(1, 13);
			if (Random == 6 || Random == 12)
				--Random;
			ConcomitantTreatmentOption = EP
					.ORXPath("#BaselineRecord_ConcomitantTreatmentsOptions_RB")
					+ "[" + Random + "]";
			EP.ChkBx_SetOption(MedicalHistoryOption, "ON");
			EP.ChkBx_SetOption(ConcomitantTreatmentOption, "ON");
			EP.Btn_Click("#General_First_BN");
			break;

		case CANADA:
			EP.ChkBx_SetOptionForAll(
					EP.ORXPath("#BaselineRecord_MedicalHistoryOptions_RB"),
					"OFF");
			EP.ChkBx_SetOptionForAll(
					EP.ORXPath("#BaselineRecord_ConcomitantTreatmentsOptions_RB"),
					"OFF");
			Random = EP.RandomValue(1, 12);
			if (Random == 9)
				++Random;
			MedicalHistoryOption = EP
					.ORXPath("#BaselineRecord_MedicalHistoryOptions_RB")
					+ "["
					+ Random + "]";
			Random = EP.RandomValue(1, 15);
			if (Random == 6 || Random == 12)
				--Random;
			ConcomitantTreatmentOption = EP
					.ORXPath("#BaselineRecord_ConcomitantTreatmentsOptions_RB")
					+ "[" + Random + "]";
			EP.ChkBx_SetOption(MedicalHistoryOption, "ON");
			EP.ChkBx_SetOption(ConcomitantTreatmentOption, "ON");
			EP.Btn_Click("#General_First_BN");
			break;

		// case CHINA:
		case KOREA:
			EP.ChkBx_SetOptionForAll(
					EP.ORXPath("#BaselineRecord_MedicalHistoryOptions_RB"),
					"OFF");
			EP.ChkBx_SetOptionForAll(
					EP.ORXPath("#BaselineRecord_ConcomitantTreatmentsOptions_RB"),
					"OFF");
			Random = EP.RandomValue(1, 10);
			if (Random == 9)
				++Random;
			MedicalHistoryOption = EP
					.ORXPath("#BaselineRecord_MedicalHistoryOptions_RB")
					+ "["
					+ Random + "]";
			Random = EP.RandomValue(1, 13);
			if (Random == 6 || Random == 12)
				--Random;
			ConcomitantTreatmentOption = EP
					.ORXPath("#BaselineRecord_ConcomitantTreatmentsOptions_RB")
					+ "[" + Random + "]";
			EP.ChkBx_SetOption(MedicalHistoryOption, "ON");
			EP.ChkBx_SetOption(ConcomitantTreatmentOption, "ON");
			EP.Btn_Click("#General_First_BN");

		case AUSTRALIA:
			EP.ChkBx_SetOptionForAll(
					EP.ORXPath("#BaselineRecord_MedicalHistoryOptions_RB"),
					"OFF");
			EP.ChkBx_SetOptionForAll(
					EP.ORXPath("#BaselineRecord_ConcomitantTreatmentsOptions_RB"),
					"OFF");
			Random = 0;
			Random = EP.RandomValue(1, 10);
			if (Random == 9)
				++Random;
			MedicalHistoryOption = EP
					.ORXPath("#BaselineRecord_MedicalHistoryOptions_RB")
					+ "["
					+ Random + "]";
			Random = EP.RandomValue(1, 13);
			if (Random == 6 || Random == 12)
				--Random;
			ConcomitantTreatmentOption = EP
					.ORXPath("#BaselineRecord_ConcomitantTreatmentsOptions_RB")
					+ "[" + Random + "]";
			EP.ChkBx_SetOption(MedicalHistoryOption, "ON");
			EP.ChkBx_SetOption(ConcomitantTreatmentOption, "ON");
			EP.Btn_Click("#General_First_BN");

			break;

		case ICELAND:

			EP.ChkBx_SetOptionForAll(
					EP.ORXPath("#BaselineRecord_MedicalHistoryOptions_RB"),
					"OFF");
			EP.ChkBx_SetOptionForAll(
					EP.ORXPath("#BaselineRecord_ConcomitantTreatmentsOptions_RB"),
					"OFF");
			Random = 0;
			Random = EP.RandomValue(1, 10);
			if (Random == 9)
				++Random;
			MedicalHistoryOption = EP
					.ORXPath("#BaselineRecord_MedicalHistoryOptions_RB")
					+ "["
					+ Random + "]";
			Random = EP.RandomValue(1, 13);
			if (Random == 6 || Random == 12)
				--Random;
			ConcomitantTreatmentOption = EP
					.ORXPath("#BaselineRecord_ConcomitantTreatmentsOptions_RB")
					+ "[" + Random + "]";

			EP.ChkBx_SetOption(MedicalHistoryOption, "ON");
			EP.ChkBx_SetOption(ConcomitantTreatmentOption, "ON");
			EP.Btn_Click("#General_First_BN");
			break;

		case FINLAND:
			EP.ChkBx_SetOptionForAll(
					EP.ORXPath("#BaselineRecord_MedicalHistoryOptions_RB"),
					"OFF");
			EP.ChkBx_SetOptionForAll(
					EP.ORXPath("#BaselineRecord_ConcomitantTreatmentsOptions_RB"),
					"OFF");
			Random = 0;
			Random = EP.RandomValue(1, 10);
			if (Random == 9)
				++Random;
			MedicalHistoryOption = EP
					.ORXPath("#BaselineRecord_MedicalHistoryOptions_RB")
					+ "["
					+ Random + "]";
			Random = EP.RandomValue(1, 13);
			if (Random == 6 || Random == 12)
				--Random;
			ConcomitantTreatmentOption = EP
					.ORXPath("#BaselineRecord_ConcomitantTreatmentsOptions_RB")
					+ "[" + Random + "]";

			EP.ChkBx_SetOption(MedicalHistoryOption, "ON");
			EP.ChkBx_SetOption(ConcomitantTreatmentOption, "ON");
			EP.Btn_Click("#General_First_BN");
			break;

		case ARGENTINA:
		case COLOMBIA:
		case GERMANY:
		case TAIWAN:
		case INDONESIA:

			EP.ChkBx_SetOptionForAll(
					EP.ORXPath("#BaselineRecord_MedicalHistoryOptions_RB"),
					"OFF");
			EP.ChkBx_SetOptionForAll(
					EP.ORXPath("#BaselineRecord_ConcomitantTreatmentsOptions_RB"),
					"OFF");
			Random = EP.RandomValue(1, 10);
			if (Random == 9)
				++Random;
			MedicalHistoryOption = EP
					.ORXPath("#BaselineRecord_MedicalHistoryOptions_RB")
					+ "["
					+ Random + "]";
			Random = EP.RandomValue(1, 13);
			if (Random == 6 || Random == 12)
				--Random;
			ConcomitantTreatmentOption = EP
					.ORXPath("#BaselineRecord_ConcomitantTreatmentsOptions_RB")
					+ "[" + Random + "]";
			EP.ChkBx_SetOption(MedicalHistoryOption, "ON");
			EP.ChkBx_SetOption(ConcomitantTreatmentOption, "ON");
			EP.Btn_Click("#General_First_BN");
			break;

		case SWEDEN:
			EP.Edit_SetValue("#BaselineRecord_OtherConcomitantTreatment_EB",
					"none");
			EP.ChkBx_SetOptionForAll(
					EP.ORXPath("#BaselineRecord_MedicalHistoryOptions_RB"),
					"OFF");
			EP.ChkBx_SetOptionForAll(
					EP.ORXPath("#BaselineRecord_ConcomitantTreatmentsOptions_RB"),
					"OFF");
			Random = 0;
			Random = EP.RandomValue(1, 10);
			if (Random == 9)
				++Random;
			MedicalHistoryOption = EP
					.ORXPath("#BaselineRecord_MedicalHistoryOptions_RB")
					+ "["
					+ Random + "]";
			Random = EP.RandomValue(1, 13);
			if (Random == 6 || Random == 12)
				--Random;
			ConcomitantTreatmentOption = EP
					.ORXPath("#BaselineRecord_ConcomitantTreatmentsOptions_RB")
					+ "[" + Random + "]";

			EP.ChkBx_SetOption(MedicalHistoryOption, "ON");
			EP.ChkBx_SetOption(ConcomitantTreatmentOption, "ON");
			EP.Btn_Click("#General_First_BN");
			break;

		default:
		}
		VerifySuccessMessage(EP.LangData("MedicalHistory"));
	}

	public void GrowthHormoneAdd(boolean IsValid, String TabIndex,
			String PatientDOB) {
		// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*GROWTH
		// HORMONE*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
		EP.ShowTab(TabIndex);
		String[] InputValues;
		if (EP.ObjectExists("#General_Delete_IM")) {
			DeleteRecord("#General_Delete_IM", false);
		}

		switch (switchCountryName) {
		case UNITEDKINGDOM:
			if (IsValid) {
				InputValues = new String[] { EP.Converter("0.01"),
						EP.Date_AddDays(PatientDOB, 2, Calendar.DATE),
						EP.Date_AddDays(PatientDOB, 3, Calendar.DATE) };
			} else {
				InputValues = new String[] { EP.Converter("0"),
						EP.Date_AddDays(PatientDOB, -1, Calendar.DATE),
						EP.Date_AddDays(PatientDOB, 2, Calendar.MONTH) };
			}

			EP.Btn_Click("#General_First_BN");// Click Add Button
			EP.Wait(2);
			EP.DrpDwn_SelectIndex("#BaselineRecord_GHNameGB_DD",
					EP.RandomValue(1, 9));
			EP.Edit_SetValue("#BaselineRecord_DailyDoseGB_EB", InputValues[0]);
			EP.Rbtn_SetOption("#BaselineRecord_OngoingNo_RB", "ON");
			EP.SelectDate("#BaselineRecord_FinishOfTreatmentCalendarGB_TT",
					InputValues[2]);
			EP.HideCursor();
			EP.Wait(2);
			EP.SelectDate("#BaselineRecord_StartOfTreatmentGB_TT",
					InputValues[1]);
			EP.HideCursor();
			EP.Wait(2);
			EP.Btn_Click("#General_AddRecordSave_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN");
			}
			break;

		case AUSTRIA:
			if (IsValid) {
				InputValues = new String[] { "0.01",
						EP.Date_AddDays(PatientDOB, 2, Calendar.DATE),
						EP.Date_AddDays(PatientDOB, 5, Calendar.DATE) };
			} else {
				InputValues = new String[] { "1",
						EP.Date_AddDays(PatientDOB, 2, Calendar.MONTH),
						PatientDOB };
			}
			EP.Btn_Click("#General_First_BN");// Click Add Button
			EP.DrpDwn_SelectIndex(
					"#BaselineRecord_AddGrowthHormoneGHNameAT_DD",
					EP.RandomValue(1, 4));
			EP.DrpDwn_SelectIndex("#BaselineRecord_DailyDoseAT_DD", 1);
			EP.Edit_SetValue("#BaselineRecord_AddGrowthHormoneDailyDoseAT_EB",
					EP.Converter(InputValues[0]));
			EP.DrpDwn_SelectIndex("#BaselineRecord_StartOfTreatmentAT_DD", 1);
			EP.Wait(2);
			EP.SelectDate("#BaselineRecord_FinishOfTreatmentCalendarAT_TT",
					InputValues[2]);
			EP.HideCursor();
			EP.Wait(2);
			EP.SelectDate("#BaselineRecord_StartOfTreatmentAT_TT",
					InputValues[1]);
			EP.HideCursor();
			EP.Wait(2);
			EP.Btn_Click("#General_AddRecordSave_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN");
			}
			break;

		case SINGAPORE:
			if (IsValid) {
				InputValues = new String[] { "0.01",
						EP.Date_AddDays(PatientDOB, 2, Calendar.DATE),
						EP.Date_AddDays(PatientDOB, 5, Calendar.DATE) };
			} else {
				InputValues = new String[] { "1",
						EP.Date_AddDays(PatientDOB, -1, Calendar.DATE),
						EP.Date_AddDays(PatientDOB, 2, Calendar.MONTH) };
			}
			EP.Btn_Click("#General_First_BN");// Click Add Button
			EP.DrpDwn_SelectIndex(
					"#BaselineRecord_AddGrowthHormoneGHNameSG_DD",
					EP.RandomValue(1, 4));
			EP.DrpDwn_SelectIndex("#BaselineRecord_DailyDoseAT_DD", 1);
			EP.Edit_SetValue("#BaselineRecord_AddGrowthHormoneDailyDoseAT_EB",
					EP.Converter(InputValues[0]));
			EP.DrpDwn_SelectIndex("#BaselineRecord_StartOfTreatmentAT_DD", 1);
			EP.Wait(2);
			EP.SelectDate("#BaselineRecord_FinishOfTreatmentCalendarAT_TT",
					InputValues[2]);
			EP.HideCursor();
			EP.Wait(2);
			EP.SelectDate("#BaselineRecord_StartOfTreatmentAT_TT",
					InputValues[1]);
			EP.HideCursor();
			EP.Wait(2);
			EP.Btn_Click("#General_AddRecordSave_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN");
			}
			break;

		case UNITEDARABEMIRATES:
		case SAUDIARABIA:
			if (IsValid) {
				InputValues = new String[] { "0.01",
						EP.Date_AddDays(PatientDOB, 2, Calendar.DATE),
						EP.Date_AddDays(PatientDOB, 5, Calendar.DATE) };
			} else {
				InputValues = new String[] { "1",
						EP.Date_AddDays(PatientDOB, -1, Calendar.DATE),
						EP.Date_AddDays(PatientDOB, 2, Calendar.MONTH) };
			}
			EP.Btn_Click("#General_First_BN");// Click Add Button
			EP.DrpDwn_SelectIndex(
					"#BaselineRecord_AddGrowthHormoneGHNameAT_DD",
					EP.RandomValue(1, 4));
			EP.Edit_SetValue("#BaselineRecord_AddGrowthHormoneDailyDoseAT_EB",
					EP.Converter(InputValues[0]));
			EP.SelectDate("#BaselineRecord_FinishOfTreatmentCalendarAT_TT",
					InputValues[2]);
			EP.HideCursor();
			EP.Wait(2);
			EP.SelectDate("#BaselineRecord_StartOfTreatmentAT_TT",
					InputValues[1]);
			EP.HideCursor();
			EP.Wait(2);
			EP.Btn_Click("#General_AddRecordSave_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN");
			}
			break;

		case MEXICO:
			if (IsValid) {
				InputValues = new String[] { "0.01",
						EP.Date_AddDays(PatientDOB, 2, Calendar.DATE),
						EP.Date_AddDays(PatientDOB, 5, Calendar.DATE) };
			} else {
				InputValues = new String[] { "1",
						EP.Date_AddDays(PatientDOB, -1, Calendar.DATE),
						EP.Date_AddDays(PatientDOB, 2, Calendar.MONTH) };
			}
			EP.Btn_Click("#General_First_BN");// Click Add Button
			EP.DrpDwn_SelectIndex(
					"#BaselineRecord_AddGrowthHormoneGHNameAT_DD",
					EP.RandomValue(1, 4));
			EP.DrpDwn_SelectIndex("#BaselineRecord_DailyDoseAT_DD", 1);
			EP.Edit_SetValue("#BaselineRecord_AddGrowthHormoneDailyDoseAT_EB",
					EP.Converter(InputValues[0]));
			EP.DrpDwn_SelectIndex("#BaselineRecord_StartOfTreatmentAT_DD", 1);
			EP.Wait(2);
			EP.SelectDate("#BaselineRecord_FinishOfTreatmentCalendarAT_TT",
					InputValues[2]);
			EP.HideCursor();
			EP.Wait(2);
			EP.SelectDate("#BaselineRecord_StartOfTreatmentAT_TT",
					InputValues[1]);
			EP.HideCursor();
			EP.Wait(2);
			EP.Btn_Click("#General_AddRecordSave_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN");
			}
			break;

		case SPAIN:

			if (IsValid) {
				InputValues = new String[] { "0.01",
						EP.Date_AddDays(PatientDOB, 2, Calendar.DATE),
						EP.Date_AddDays(PatientDOB, 5, Calendar.DATE) };
			} else {
				InputValues = new String[] { "1",
						EP.Date_AddDays(PatientDOB, -1, Calendar.DATE),
						EP.Date_AddDays(PatientDOB, 2, Calendar.MONTH) };
			}

			EP.Btn_Click("#General_First_BN");// Click Add Button
			EP.Wait(2);
			EP.DrpDwn_SelectIndex(
					"#BaselineRecord_AddGrowthHormoneGHNameES_DD",
					EP.RandomValue(1, 4));
			EP.Edit_SetValue("#BaselineRecord_AddGrowthHormoneDailyDoseES_EB",
					EP.Converter(InputValues[0]));
			EP.SelectDate("#BaselineRecord_FinishOfTreatmentCalendarES_TT",
					InputValues[2]);
			EP.HideCursor();
			EP.Wait(2);
			EP.SelectDate("#BaselineRecord_StartOfTreatmentCalendarES_TT",
					InputValues[1]);
			EP.HideCursor();
			EP.Wait(2);
			EP.Btn_Click("#General_AddRecordSave_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN");
			}
			break;

		case SLOVAKIA:
			if (IsValid) {
				InputValues = new String[] { "0.001",
						EP.Date_AddDays(PatientDOB, 2, Calendar.DATE),
						EP.Date_AddDays(PatientDOB, 5, Calendar.DATE) };
			} else {
				InputValues = new String[] { "1",
						EP.Date_AddDays(PatientDOB, -1, Calendar.DATE),
						EP.Date_AddDays(PatientDOB, 2, Calendar.MONTH) };
			}
			EP.Btn_Click("#General_First_BN");// Click Add Button
			EP.DrpDwn_SelectIndex(
					"#BaselineRecord_AddGrowthHormoneGHNameSK_DD",
					EP.RandomValue(1, 4));
			EP.Edit_SetValue("#BaselineRecord_AddGrowthHormoneDailyDoseES_EB",
					EP.Converter(InputValues[0]));
			EP.SelectDate("#BaselineRecord_FinishOfTreatmentCalendarES_TT",
					InputValues[2]);
			EP.HideCursor();
			EP.Wait(2);
			EP.SelectDate("#BaselineRecord_StartOfTreatmentCalendarES_TT",
					InputValues[1]);
			EP.HideCursor();
			EP.Wait(2);
			EP.Btn_Click("#General_AddRecordSave_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN");
			}
			break;

		case SWEDEN:
			if (IsValid) {
				InputValues = new String[] { "0.01",
						EP.Date_AddDays(PatientDOB, 2, Calendar.DATE),
						EP.Date_AddDays(PatientDOB, 5, Calendar.DATE) };
			} else {
				InputValues = new String[] { "1",
						EP.Date_AddDays(PatientDOB, 2, Calendar.MONTH),
						PatientDOB };
			}
			EP.Btn_Click("#General_First_BN");// Click Add Button
			EP.DrpDwn_SelectIndex(
					"#BaselineRecord_AddGrowthHormoneGHNameAT_DD",
					EP.RandomValue(1, 4));
			EP.DrpDwn_SelectIndex("#BaselineRecord_DailyDoseAT_DD", 1);
			EP.Edit_SetValue("#BaselineRecord_AddGrowthHormoneDailyDoseAT_EB",
					EP.Converter(InputValues[0]));
			EP.SelectDate("#BaselineRecord_FinishOfTreatmentCalendarAT_TT",
					InputValues[2]);
			EP.HideCursor();
			EP.Wait(2);
			EP.SelectDate("#BaselineRecord_StartOfTreatmentAT_TT",
					InputValues[1]);
			EP.HideCursor();
			EP.Wait(2);
			EP.Btn_Click("#General_AddRecordSave_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN");
			}
			break;
		case KOREA:

			if (IsValid) {
				InputValues = new String[] { "0.01",
						EP.Date_AddDays(PatientDOB, 2, Calendar.DATE),
						EP.Date_AddDays(PatientDOB, 5, Calendar.DATE) };
			} else {
				InputValues = new String[] { "1",
						EP.Date_AddDays(PatientDOB, -1, Calendar.YEAR),
						EP.Date_AddDays(PatientDOB, 2, Calendar.MONTH) };
			}

			EP.Btn_Click("#General_First_BN");// Click Add Button
			EP.Edit_SetValue("#BaselineRecord_AddGrowthHormoneDailyDoseES_EB",
					EP.Converter(InputValues[0]));
			EP.SelectDate("#BaselineRecord_FinishOfTreatmentCalendarES_TT",
					InputValues[2]);
			EP.HideCursor();
			EP.Wait(2);
			EP.SelectDate("#BaselineRecord_StartOfTreatmentCalendarES_TT",
					InputValues[1]);
			EP.HideCursor();
			EP.Wait(2);
			EP.DrpDwn_SelectIndex(
					"#BaselineRecord_AddGrowthHormoneGHNameKR_DD",
					EP.RandomValue(1, 4));
			EP.Btn_Click("#General_AddRecordSave_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN");
			}
			break;
		case AUSTRALIA:
			if (IsValid) {
				InputValues = new String[] { "0.01",
						EP.Date_AddDays(PatientDOB, 2, Calendar.DATE),
						EP.Date_AddDays(PatientDOB, 5, Calendar.DATE) };
			} else {
				InputValues = new String[] { "1",
						EP.Date_AddDays(PatientDOB, -1, Calendar.YEAR),
						EP.Date_AddDays(PatientDOB, 2, Calendar.MONTH) };
			}

			EP.Btn_Click("#General_First_BN");// Click Add Button
			EP.Edit_SetValue("#BaselineRecord_AddGrowthHormoneDailyDoseES_EB",
					EP.Converter(InputValues[0]));
			EP.SelectDate("#BaselineRecord_FinishOfTreatmentCalendarES_TT",
					InputValues[2]);
			EP.HideCursor();
			EP.Wait(2);
			EP.SelectDate("#BaselineRecord_StartOfTreatmentCalendarES_TT",
					InputValues[1]);
			EP.HideCursor();
			EP.Wait(2);
			EP.DrpDwn_SelectIndex(
					"#BaselineRecord_AddGrowthHormoneGHNameAU_DD",
					EP.RandomValue(1, 7));
			EP.Btn_Click("#General_AddRecordSave_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN");
			}

			break;

		case ICELAND:
			if (IsValid) {
				InputValues = new String[] { "0.01",
						EP.Date_AddDays(PatientDOB, 2, Calendar.DATE),
						EP.Date_AddDays(PatientDOB, 5, Calendar.DATE) };
			} else {
				InputValues = new String[] { "1",
						EP.Date_AddDays(PatientDOB, -1, Calendar.YEAR),
						EP.Date_AddDays(PatientDOB, 2, Calendar.MONTH) };
			}

			EP.Btn_Click("#General_First_BN");// Click Add Button
			EP.DrpDwn_SelectIndex(
					"#BaselineRecord_AddGrowthHormoneGHNameES_DD",
					EP.RandomValue(1, 4));
			EP.Edit_SetValue("#BaselineRecord_AddGrowthHormoneDailyDoseES_EB",
					EP.Converter(InputValues[0]));
			EP.SelectDate("#BaselineRecord_FinishOfTreatmentCalendarES_TT",
					InputValues[2]);
			EP.HideCursor();
			EP.Wait(2);
			EP.SelectDate("#BaselineRecord_StartOfTreatmentCalendarES_TT",
					InputValues[1]);
			EP.HideCursor();
			EP.Wait(2);
			EP.Btn_Click("#General_AddRecordSave_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN");
			}
			break;

		case FINLAND:
		case ARGENTINA:
		case COLOMBIA:
		case TAIWAN:
		case INDONESIA:
			if (IsValid) {
				InputValues = new String[] { "0.01",
						EP.Date_AddDays(PatientDOB, 2, Calendar.DATE),
						EP.Date_AddDays(PatientDOB, 5, Calendar.DATE) };
			} else {
				InputValues = new String[] { "1",
						EP.Date_AddDays(PatientDOB, -1, Calendar.YEAR),
						EP.Date_AddDays(PatientDOB, 2, Calendar.MONTH) };
			}

			EP.Btn_Click("#General_First_BN");// Click Add Button
			EP.DrpDwn_SelectIndex(
					"#BaselineRecord_AddGrowthHormoneGHNameES_DD",
					EP.RandomValue(1, 4));
			EP.Edit_SetValue("#BaselineRecord_AddGrowthHormoneDailyDoseES_EB",
					EP.Converter(InputValues[0]));
			EP.SelectDate("#BaselineRecord_FinishOfTreatmentCalendarES_TT",
					InputValues[2]);
			EP.HideCursor();
			EP.Wait(2);
			EP.SelectDate("#BaselineRecord_StartOfTreatmentCalendarES_TT",
					InputValues[1]);
			EP.HideCursor();
			EP.Wait(2);
			EP.Btn_Click("#General_AddRecordSave_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN");
			}
			break;

		case GERMANY:

			if (IsValid) {
				InputValues = new String[] { "0.01",
						EP.Date_AddDays(PatientDOB, 2, Calendar.DATE),
						EP.Date_AddDays(PatientDOB, 5, Calendar.DATE) };
			} else {
				InputValues = new String[] { "1",
						EP.Date_AddDays(PatientDOB, -1, Calendar.YEAR),
						EP.Date_AddDays(PatientDOB, 2, Calendar.MONTH) };
			}

			EP.Btn_Click("#General_First_BN");// Click Add Button
			EP.Edit_SetValue("#BaselineRecord_DailyDose_EB",
					EP.Converter(InputValues[0]));
			EP.SelectDate("#BaselineRecord_FinishOfTreatmentCalendar_TT",
					InputValues[2]);
			EP.HideCursor();
			EP.Wait(2);
			EP.SelectDate("#BaselineRecord_StartOfTreatment_TT", InputValues[1]);
			EP.HideCursor();
			EP.Wait(2);
			EP.DrpDwn_SelectIndex("#BaselineRecord_GHName_DD",
					EP.RandomValue(1, 4));
			EP.Btn_Click("#General_AddRecordSave_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN");
			}
			break;

		case ITALY:
			if (IsValid) {
				InputValues = new String[] { EP.Converter("0.01"),
						EP.Date_AddDays(PatientDOB, 2, Calendar.DATE),
						EP.Date_AddDays(PatientDOB, 5, Calendar.DATE) };
			} else {
				InputValues = new String[] { "1",
						EP.Date_AddDays(PatientDOB, -1, Calendar.YEAR),
						EP.Date_AddDays(PatientDOB, 2, Calendar.MONTH) };
			}

			EP.Btn_Click("#General_First_BN");// Click Add Button
			EP.Wait(1);
			EP.DrpDwn_SelectIndex(
					"#BaselineRecord_AddGrowthHormoneGHNameIT_DD",
					EP.RandomValue(1, 4));
			EP.Edit_SetValue("#BaselineRecord_AddGrowthHormoneDailyDoseIT_EB",
					InputValues[0]);
			EP.SelectDate("#BaselineRecord_FinishOfTreatmentCalendarIT_TT",
					InputValues[2]);
			EP.HideCursor();
			EP.Wait(2);
			EP.SelectDate("#BaselineRecord_StartOfTreatmentIT_TT",
					InputValues[1]);
			EP.HideCursor();
			EP.Wait(2);
			EP.Btn_Click("#General_AddRecordSave_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN");
			}
			break;
		case GREECE:
			if (IsValid) {
				InputValues = new String[] { EP.Converter("0.01"),
						EP.Date_AddDays(PatientDOB, 2, Calendar.DATE),
						EP.Date_AddDays(PatientDOB, 5, Calendar.DATE) };
			} else {
				InputValues = new String[] { "1",
						EP.Date_AddDays(PatientDOB, -1, Calendar.YEAR),
						EP.Date_AddDays(PatientDOB, 2, Calendar.MONTH) };
			}

			EP.Btn_Click("#General_First_BN");// Click Add Button
			EP.Wait(1);
			EP.DrpDwn_SelectIndex(
					"#BaselineRecord_AddGrowthHormoneGHNameIT_DD",
					EP.RandomValue(1, 4));
			EP.Edit_SetValue("#BaselineRecord_AddGrowthHormoneDailyDoseIT_EB",
					InputValues[0]);
			EP.SelectDate("#BaselineRecord_FinishOfTreatmentCalendarIT_TT",
					InputValues[2]);
			EP.HideCursor();
			EP.Wait(2);
			EP.SelectDate("#BaselineRecord_StartOfTreatmentIT_TT",
					InputValues[1]);
			EP.HideCursor();
			EP.Wait(2);
			EP.Btn_Click("#General_AddRecordSave_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN");
			}
			break;

		case FRANCE:
			if (IsValid) {
				InputValues = new String[] { "0.01",
						EP.Date_AddDays(PatientDOB, 2, Calendar.DATE),
						EP.Date_AddDays(PatientDOB, 5, Calendar.DATE) };
			} else {
				InputValues = new String[] { "1",
						EP.Date_AddDays(PatientDOB, -1, Calendar.DATE),
						EP.Date_AddDays(PatientDOB, 2, Calendar.MONTH) };
			}

			EP.Btn_Click("#General_First_BN");// Click Add Button
			EP.Wait(2);
			EP.DrpDwn_SelectIndex(
					"#BaselineRecord_AddGrowthHormoneGHNameFR_DD",
					EP.RandomValue(1, 13));
			EP.Edit_SetValue("#BaselineRecord_AddGrowthHormoneDailyDoseFR_EB",
					EP.Converter(InputValues[0]));
			EP.SelectDate("#BaselineRecord_FinishOfTreatmentCalendarFR_TT",
					InputValues[2]);
			EP.HideCursor();
			EP.Wait(2);
			EP.SelectDate("#BaselineRecord_StartOfTreatmentCalendarFR_TT",
					InputValues[1]);
			EP.HideCursor();
			EP.Wait(2);
			EP.Btn_Click("#General_AddRecordSave_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN");
			}
			break;

		case CZECHREPUBLIC:
		case HUNGARY:
			if (IsValid) {
				InputValues = new String[] { "0.01",
						EP.Date_AddDays(PatientDOB, 2, Calendar.DATE),
						EP.Date_AddDays(PatientDOB, 5, Calendar.DATE) };
			} else {
				InputValues = new String[] { "1",
						EP.Date_AddDays(PatientDOB, -1, Calendar.DATE),
						EP.Date_AddDays(PatientDOB, 2, Calendar.MONTH) };
			}

			EP.Btn_Click("#General_First_BN");// Click Add Button
			EP.Wait(1);
			EP.DrpDwn_SelectIndex(
					"#BaselineRecord_AddGrowthHormoneGHNameCZ_DD",
					EP.RandomValue(1, 4));
			EP.Edit_SetValue("#BaselineRecord_AddGrowthHormoneDailyDoseFR_EB",
					EP.Converter(InputValues[0]));
			EP.SelectDate("#BaselineRecord_FinishOfTreatmentCalendarFR_TT",
					InputValues[2]);
			EP.HideCursor();
			EP.Wait(2);
			EP.SelectDate("#BaselineRecord_StartOfTreatmentCalendarFR_TT",
					InputValues[1]);
			EP.HideCursor();
			EP.Wait(2);
			EP.Btn_Click("#General_AddRecordSave_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN");
			}

			break;

		case NORWAY:
			if (IsValid) {
				InputValues = new String[] { "0.01",
						EP.Date_AddDays(PatientDOB, 2, Calendar.DATE),
						EP.Date_AddDays(PatientDOB, 5, Calendar.DATE) };
			} else {
				InputValues = new String[] { "1",
						EP.Date_AddDays(PatientDOB, -1, Calendar.YEAR),
						EP.Date_AddDays(PatientDOB, 2, Calendar.MONTH) };
			}
			EP.Btn_Click("#General_First_BN");// Click Add Button
			EP.DrpDwn_SelectIndex(
					"#BaselineRecord_AddGrowthHormoneGHNameCZ_DD",
					EP.RandomValue(1, 4));
			EP.Edit_SetValue("#BaselineRecord_AddGrowthHormoneDailyDoseFR_EB",
					InputValues[0]);
			EP.SelectDate("#BaselineRecord_FinishOfTreatmentCalendarFR_TT",
					InputValues[2]);
			EP.HideCursor();
			EP.Wait(2);
			EP.SelectDate("#BaselineRecord_StartOfTreatmentCalendarFR_TT",
					InputValues[1]);
			EP.HideCursor();
			EP.Wait(2);
			EP.Btn_Click("#General_AddRecordSave_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN");
			}
			break;

		case CANADA:
			if (IsValid) {
				InputValues = new String[] { "0.01",
						EP.Date_AddDays(PatientDOB, 5, Calendar.DATE),
						EP.Date_AddDays(PatientDOB, 10, Calendar.DATE) };
			} else {
				InputValues = new String[] { "1",
						EP.Date_AddDays(PatientDOB, -1, Calendar.YEAR),
						EP.Date_AddDays(PatientDOB, 2, Calendar.MONTH) };
			}
			if (EP.ObjectExists("#General_Delete_IM")) {
				DeleteRecord("#General_Delete_IM", false);
			}
			EP.Btn_Click("#General_First_BN");// Click Add Button
			EP.DrpDwn_SelectIndex("#BaselineRecord_GHNameCA_DD",
					EP.RandomValue(1, 4));
			EP.Edit_SetValue("#BaselineRecord_DailyDoseGB_EB",
					EP.Converter(InputValues[0]));
			EP.SelectDate("#BaselineRecord_FinishOfTreatmentCalendarGB_TT",
					InputValues[2]);
			EP.HideCursor();
			EP.Wait(2);
			EP.SelectDate("#BaselineRecord_StartOfTreatmentGB_TT",
					InputValues[1]);
			EP.HideCursor();
			EP.Wait(2);
			EP.Btn_Click("#General_AddRecordSave_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN");
			}
			break;

		default:
		}
		VerifySuccessMessage(EP.LangData("GrowthHormoneHistory"));
	}

	public void SocioeconomicAdd(boolean IsValid, String TabIndex) {
		// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*SOCIOECONOMIC*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
		EP.ShowTab(TabIndex);

		if (EP.ObjectExists("#General_Delete_IM")) {
			DeleteRecord("#General_Delete_IM", false);
		}

		switch (switchCountryName) {
		case UNITEDKINGDOM:
			EP.Btn_Click("#General_First_BN");// Click Add Button
			EP.SelectDate("#BaselineRecord_VisitDateGB_IM", EP.Date_AddDays(
					EP.TestData("ECOSPatientDOB"), 2, Calendar.YEAR));
			EP.HideCursor();
			EP.Wait(2);
			EP.DrpDwn_SelectIndex("#BaselineRecord_ParentGB_DD",
					EP.RandomValue(1, 3));
			EP.DrpDwn_SelectIndex("#BaselineRecord_MaritalStatusGB_DD",
					EP.RandomValue(1, 6));
			EP.DrpDwn_SelectIndex("#BaselineRecord_EmploymentStatusGB_DD",
					EP.RandomValue(1, 5));
			EP.DrpDwn_SelectIndex(
					"#BaselineRecord_EducationStatusOfPersonGB_DD",
					EP.RandomValue(1, 2));
			EP.Btn_Click("#General_AddRecordSave_BN");
			break;

		case AUSTRIA:
			EP.Btn_Click("#General_First_BN");// Click Add Button
			EP.DrpDwn_SelectIndex("#BaselineRecord_DateAT_DD", 1);
			EP.SelectDate("#BaselineRecord_AddSocioeconomicDateAT_IM", EP
					.Date_AddDays(EP.TestData("ECOSPatientDOB"), -2,
							Calendar.YEAR));
			EP.HideCursor();
			EP.Wait(2);
			EP.ChkBx_SetOption(
					"#BaselineRecord_ParentEmploymentStatusPleaseSpecifyAT_CB",
					"OFF");
			EP.DrpDwn_SelectIndex(
					"#BaselineRecord_AddSocioeconomicParentAT_DD",
					EP.RandomValue(1, 5));
			EP.ChkBx_SetOption(
					EP.ORXPath("#BaselineRecord_ParentMaritalStatusOptions_CB")
							+ "[" + EP.RandomValue(1, 5) + "]", "ON");
			EP.ChkBx_SetOption(
					EP.ORXPath("#BaselineRecord_ParentEmploymentStatusOptions_CB")
							+ "[" + EP.RandomValue(1, 4) + "]", "ON");
			EP.DrpDwn_SelectIndex(
					"#BaselineRecord_EducationStatusOfPersonAT_DD",
					EP.RandomValue(1, 2));
			EP.Btn_Click("#General_AddRecordSave_BN");
			break;

		case SINGAPORE:
			EP.Btn_Click("#General_First_BN");// Click Add Button
			EP.SelectDate("#BaselineRecord_AddSocioeconomicDateAT_IM", EP
					.Date_AddDays(EP.TestData("ECOSPatientDOB"), -2,
							Calendar.YEAR));
			EP.HideCursor();
			EP.Wait(2);
			EP.ChkBx_SetOption(
					"#BaselineRecord_ParentEmploymentStatusPleaseSpecifyAT_CB",
					"OFF");
			EP.DrpDwn_SelectIndex(
					"#BaselineRecord_AddSocioeconomicParentAT_DD",
					EP.RandomValue(1, 3));
			EP.ChkBx_SetOption(
					EP.ORXPath("#BaselineRecord_ParentMaritalStatusOptions_CB")
							+ "[" + EP.RandomValue(1, 5) + "]", "ON");
			EP.ChkBx_SetOption(
					EP.ORXPath("#BaselineRecord_ParentEmploymentStatusOptions_CB")
							+ "[" + EP.RandomValue(1, 4) + "]", "ON");
			EP.DrpDwn_SelectIndex(
					"#BaselineRecord_EducationStatusOfPersonAT_DD",
					EP.RandomValue(1, 2));
			EP.Btn_Click("#General_AddRecordSave_BN");
			break;
		case UNITEDARABEMIRATES:
		case SAUDIARABIA:
			EP.Btn_Click("#General_First_BN");// Click Add Button
			EP.SelectDate("#BaselineRecord_AddSocioeconomicDateAT_IM", EP
					.Date_AddDays(EP.TestData("ECOSPatientDOB"), -2,
							Calendar.YEAR));
			EP.HideCursor();
			EP.Wait(2);
			EP.ChkBx_SetOption(
					"#BaselineRecord_ParentEmploymentStatusPleaseSpecifyAT_CB",
					"OFF");
			EP.DrpDwn_SelectIndex(
					"#BaselineRecord_AddSocioeconomicParentAT_DD",
					EP.RandomValue(1, 3));
			EP.ChkBx_SetOption(
					EP.ORXPath("#BaselineRecord_ParentMaritalStatusOptions_CB")
							+ "[" + EP.RandomValue(1, 3) + "]", "ON");
			EP.ChkBx_SetOption(
					EP.ORXPath("#BaselineRecord_ParentEmploymentStatusOptions_CB")
							+ "[" + EP.RandomValue(1, 4) + "]", "ON");
			EP.DrpDwn_SelectIndex(
					"#BaselineRecord_EducationStatusOfPersonAT_DD",
					EP.RandomValue(1, 2));
			EP.Btn_Click("#General_AddRecordSave_BN");
			break;
		case ARGENTINA:
		case COLOMBIA:
		case TAIWAN:
		case INDONESIA:
			EP.Btn_Click("#General_First_BN");// Click Add Button
			EP.SelectDate("#BaselineRecord_AddSocioeconomicDateAT_IM", EP
					.Date_AddDays(EP.TestData("ECOSPatientDOB"), -2,
							Calendar.YEAR));
			EP.HideCursor();
			EP.Wait(2);
			EP.ChkBx_SetOption(
					"#BaselineRecord_ParentEmploymentStatusPleaseSpecifyAT_CB",
					"OFF");
			EP.DrpDwn_SelectIndex(
					"#BaselineRecord_AddSocioeconomicParentAT_DD",
					EP.RandomValue(1, 3));
			EP.ChkBx_SetOption(
					EP.ORXPath("#BaselineRecord_ParentMaritalStatusOptions_CB")
							+ "[" + EP.RandomValue(1, 3) + "]", "ON");
			EP.ChkBx_SetOption(
					EP.ORXPath("#BaselineRecord_ParentEmploymentStatusOptions_CB")
							+ "[" + EP.RandomValue(1, 4) + "]", "ON");
			EP.DrpDwn_SelectIndex(
					"#BaselineRecord_EducationStatusOfPersonTW_DD",
					EP.RandomValue(1, 2));
			EP.Btn_Click("#General_AddRecordSave_BN");
			break;
		case MEXICO:
			EP.Btn_Click("#General_First_BN");// Click Add Button
			EP.DrpDwn_SelectIndex("#BaselineRecord_DateAT_DD", 1);
			EP.SelectDate("#BaselineRecord_AddSocioeconomicDateAT_IM", EP
					.Date_AddDays(EP.TestData("ECOSPatientDOB"), -2,
							Calendar.YEAR));
			EP.HideCursor();
			EP.Wait(2);
			EP.ChkBx_SetOption(
					"#BaselineRecord_ParentEmploymentStatusPleaseSpecifyAT_CB",
					"OFF");
			EP.DrpDwn_SelectIndex(
					"#BaselineRecord_AddSocioeconomicParentAT_DD",
					EP.RandomValue(1, 5));
			EP.ChkBx_SetOption(
					EP.ORXPath("#BaselineRecord_ParentMaritalStatusOptions_CB")
							+ "[" + EP.RandomValue(1, 5) + "]", "ON");
			EP.ChkBx_SetOption(
					EP.ORXPath("#BaselineRecord_ParentEmploymentStatusOptions_CB")
							+ "[" + EP.RandomValue(1, 4) + "]", "ON");
			EP.DrpDwn_SelectIndex(
					"#BaselineRecord_EducationStatusOfPersonAT_DD",
					EP.RandomValue(1, 2));
			EP.Btn_Click("#General_AddRecordSave_BN");
			break;

		case SPAIN:
			EP.Btn_Click("#General_First_BN");// Click Add Button
			EP.SelectDate("#BaselineRecord_AddSocioeconomicDateES_IM", EP
					.Date_AddDays(EP.TestData("ECOSPatientDOB"), -2,
							Calendar.YEAR));
			EP.HideCursor();
			EP.Wait(2);
			EP.DrpDwn_SelectIndex(
					"#BaselineRecord_AddSocioeconomicParentES_DD",
					EP.RandomValue(1, 3));
			EP.ChkBx_SetOption(
					EP.ORXPath("#BaselineRecord_ParentMaritalStatusOptions_CB")
							+ "[" + EP.RandomValue(1, 4) + "]", "ON");
			EP.Wait(1);
			EP.ChkBx_SetOption(
					EP.ORXPath("#BaselineRecord_ParentEmploymentStatusOptions_CB")
							+ "[" + EP.RandomValue(1, 4) + "]", "ON");
			EP.Wait(1);
			EP.DrpDwn_SelectIndex(
					"#BaselineRecord_EducationStatusOfPersonES_DD",
					EP.RandomValue(1, 2));
			EP.Btn_Click("#General_AddRecordSave_BN");
			break;

		case SWEDEN:
			EP.Btn_Click("#General_First_BN");// Click Add Button
			EP.SelectDate("#BaselineRecord_AddSocioeconomicDateAT_IM", EP
					.Date_AddDays(EP.TestData("ECOSPatientDOB"), -2,
							Calendar.YEAR));
			EP.HideCursor();
			EP.Wait(2);
			EP.ChkBx_SetOption(
					"#BaselineRecord_ParentEmploymentStatusPleaseSpecifyAT_CB",
					"OFF");
			EP.DrpDwn_SelectIndex(
					"#BaselineRecord_AddSocioeconomicParentSE_DD",
					EP.RandomValue(1, 3));
			EP.ChkBx_SetOption(
					EP.ORXPath("#BaselineRecord_ParentMaritalStatusOptions_CB")
							+ "[" + EP.RandomValue(1, 5) + "]", "ON");
			EP.ChkBx_SetOption(
					EP.ORXPath("#BaselineRecord_ParentEmploymentStatusOptions_CB")
							+ "[" + EP.RandomValue(1, 4) + "]", "ON");
			EP.DrpDwn_SelectIndex(
					"#BaselineRecord_EducationStatusOfPersonAT_DD",
					EP.RandomValue(1, 2));
			EP.Btn_Click("#General_AddRecordSave_BN");
			break;
		// case CHINA:
		case KOREA:
			EP.Btn_Click("#General_First_BN");// Click Add Button
			EP.SelectDate("#BaselineRecord_VisitDate_IM", EP.Date_AddDays(
					EP.TestData("ECOSPatientDOB"), 2, Calendar.YEAR));
			EP.HideCursor();
			EP.Wait(2);
			EP.DrpDwn_SelectIndex(
					"#BaselineRecord_AddSocioeconomicParentES_DD",
					EP.RandomValue(1, 3));
			EP.ChkBx_SetOption("#BaselineRecord_MarriedCohabitingES_CB", "ON");
			EP.ChkBx_SetOption(
					"#BaselineRecord_ParentEmploymentStatusPleaseSpecifyES_CB",
					"OFF");
			EP.ChkBx_SetOption("#BaselineRecord_HomeMakerES_CB", "ON");
			EP.DrpDwn_SelectIndex(
					"#BaselineRecord_EducationStatusOfPersonES_DD",
					EP.RandomValue(1, 2));
			EP.Btn_Click("#General_AddRecordSave_BN");
			break;

		case AUSTRALIA:
			EP.Btn_Click("#General_First_BN");// Click Add Button
			EP.SelectDate("#BaselineRecord_AddSocioeconomicDateES_IM", EP
					.Date_AddDays(EP.TestData("ECOSPatientDOB"), 2,
							Calendar.YEAR));
			EP.HideCursor();
			EP.Wait(2);
			EP.DrpDwn_SelectIndex(
					"#BaselineRecord_AddSocioeconomicParentES_DD",
					EP.RandomValue(1, 3));
			EP.ChkBx_SetOption(
					EP.ORXPath("#BaselineRecord_ParentMaritalStatusOptions_CB")
							+ "[" + EP.RandomValue(1, 4) + "]", "ON");
			EP.ChkBx_SetOption(
					"#BaselineRecord_ParentEmploymentStatusPleaseSpecifyES_CB",
					"OFF");
			EP.ChkBx_SetOption(
					EP.ORXPath("#BaselineRecord_ParentEmploymentStatusOptions_CB")
							+ "[" + EP.RandomValue(1, 4) + "]", "ON");
			EP.DrpDwn_SelectIndex(
					"#BaselineRecord_EducationStatusOfPersonES_DD",
					EP.RandomValue(1, 2));
			EP.Btn_Click("#General_AddRecordSave_BN");
			break;

		case ICELAND:
			EP.Btn_Click("#General_First_BN");// Click Add Button
			EP.SelectDate("#BaselineRecord_AddSocioeconomicDateES_IM", EP
					.Date_AddDays(EP.TestData("ECOSPatientDOB"), 2,
							Calendar.YEAR));
			EP.HideCursor();
			EP.Wait(2);
			EP.DrpDwn_SelectIndex(
					"#BaselineRecord_AddSocioeconomicParentES_DD",
					EP.RandomValue(1, 3));
			EP.ChkBx_SetOption(
					EP.ORXPath("#BaselineRecord_ParentMaritalStatusOptions_CB")
							+ "[" + EP.RandomValue(1, 4) + "]", "ON");
			EP.ChkBx_SetOption(
					"#BaselineRecord_ParentEmploymentStatusPleaseSpecifyES_CB",
					"OFF");
			EP.ChkBx_SetOption(
					EP.ORXPath("#BaselineRecord_ParentEmploymentStatusOptions_CB")
							+ "[" + EP.RandomValue(1, 4) + "]", "ON");
			EP.DrpDwn_SelectIndex(
					"#BaselineRecord_EducationStatusOfPersonES_DD",
					EP.RandomValue(1, 2));
			EP.Btn_Click("#General_AddRecordSave_BN");
			break;

		case FINLAND:
			EP.Btn_Click("#General_First_BN");// Click Add Button
			EP.SelectDate("#BaselineRecord_AddSocioeconomicDateES_IM", EP
					.Date_AddDays(EP.TestData("ECOSPatientDOB"), 2,
							Calendar.YEAR));
			EP.HideCursor();
			EP.Wait(2);
			EP.DrpDwn_SelectIndex(
					"#BaselineRecord_AddSocioeconomicParentES_DD",
					EP.RandomValue(1, 3));
			EP.ChkBx_SetOption(
					EP.ORXPath("#BaselineRecord_ParentMaritalStatusOptions_CB")
							+ "[" + EP.RandomValue(1, 4) + "]", "ON");
			EP.ChkBx_SetOption(
					"#BaselineRecord_ParentEmploymentStatusPleaseSpecifyES_CB",
					"OFF");
			EP.ChkBx_SetOption(
					EP.ORXPath("#BaselineRecord_ParentEmploymentStatusOptions_CB")
							+ "[" + EP.RandomValue(1, 4) + "]", "ON");
			EP.DrpDwn_SelectIndex(
					"#BaselineRecord_EducationStatusOfPersonES_DD",
					EP.RandomValue(1, 2));
			EP.Btn_Click("#General_AddRecordSave_BN");
			break;

		// case ARGENTINA:
		case GERMANY:

			EP.Btn_Click("#General_First_BN");// Click Add Button
			EP.SelectDate("#BaselineRecord_VisitDate_IM", EP.Date_AddDays(
					EP.TestData("ECOSPatientDOB"), 2, Calendar.YEAR));
			EP.DrpDwn_SelectIndex(
					"#BaselineRecord_AddSocioeconomicParentAT_DD",
					EP.RandomValue(1, 3));
			EP.ChkBx_SetOption("#BaselineRecord_MarriedCohabiting_CB", "ON");
			EP.ChkBx_SetOption(
					"#BaselineRecord_ParentEmploymentOtherSpecify_CB", "OFF");
			EP.ChkBx_SetOption("#BaselineRecord_Employed_CB", "ON");

			if (IsValid) {
				EP.ChkBx_SetOption(
						"#BaselineRecord_ParentEmploymentOtherSpecify_CB",
						"OFF");
				EP.DrpDwn_SelectIndex(
						"#BaselineRecord_EducationStatusOfPerson_DD",
						EP.RandomValue(1, 2));
				EP.Btn_Click("#General_AddRecordSave_BN");
			} else {
				EP.ChkBx_SetOption(
						"#BaselineRecord_ParentEmploymentOtherSpecify_CB", "ON");
				EP.DrpDwn_SelectIndex(
						"#BaselineRecord_EducationStatusOfPerson_DD", 3);
				EP.Btn_Click("#General_AddRecordSave_BN");
				EP.Btn_Click("#General_AddRecordSave_BN");
			}
			break;

		case ITALY:
			EP.Btn_Click("#General_First_BN");// Click Add Button
			EP.SelectDate("#BaselineRecord_VisitDateIT_IM", EP.Date_AddDays(
					EP.TestData("ECOSPatientDOB"), 2, Calendar.YEAR));
			EP.HideCursor();
			EP.Wait(2);
			EP.DrpDwn_SelectIndex(
					"#BaselineRecord_AddSocioeconomicParentIT_DD",
					EP.RandomValue(1, 3));
			EP.ChkBx_SetOption(
					EP.ORXPath("#BaselineRecord_ParentMaritalStatusOptions_CB")
							+ "[" + EP.RandomValue(1, 3) + "]", "ON");
			EP.Wait(1);
			EP.ChkBx_SetOption(
					EP.ORXPath("#BaselineRecord_ParentEmploymentStatusOptions_CB")
							+ "[" + EP.RandomValue(1, 4) + "]", "ON");
			EP.Wait(1);
			EP.DrpDwn_SelectIndex(
					"#BaselineRecord_EducationStatusOfPersonGR_DD",
					EP.RandomValue(1, 2));
			EP.Btn_Click("#General_AddRecordSave_BN");
			break;

		case SLOVAKIA:
			EP.Btn_Click("#General_First_BN");// Click Add Button
			EP.SelectDate("#BaselineRecord_VisitDateIT_IM", EP.Date_AddDays(
					EP.TestData("ECOSPatientDOB"), 2, Calendar.YEAR));
			EP.HideCursor();
			EP.Wait(2);
			EP.DrpDwn_SelectIndex(
					"#BaselineRecord_AddSocioeconomicParentIT_DD",
					EP.RandomValue(1, 3));
			EP.ChkBx_SetOption(
					EP.ORXPath("#BaselineRecord_ParentMaritalStatusOptions_CB")
							+ "[" + EP.RandomValue(1, 4) + "]", "ON");
			EP.Wait(1);
			EP.ChkBx_SetOption(
					EP.ORXPath("#BaselineRecord_ParentEmploymentStatusOptions_CB")
							+ "[" + EP.RandomValue(1, 4) + "]", "ON");
			EP.Wait(1);
			EP.DrpDwn_SelectIndex(
					"#BaselineRecord_EducationStatusOfPersonGR_DD",
					EP.RandomValue(1, 2));
			EP.Btn_Click("#General_AddRecordSave_BN");

			break;
		case GREECE:
			EP.Btn_Click("#General_First_BN");// Click Add Button
			EP.SelectDate("#BaselineRecord_VisitDateIT_IM", EP.Date_AddDays(
					EP.TestData("ECOSPatientDOB"), 2, Calendar.YEAR));
			EP.HideCursor();
			EP.Wait(2);
			EP.DrpDwn_SelectIndex(
					"#BaselineRecord_AddSocioeconomicParentIT_DD",
					EP.RandomValue(1, 3));
			EP.ChkBx_SetOption(
					"#BaselineRecord_ParentEmploymentOtherSpecifyIT_CB", "OFF");
			EP.ChkBx_SetOption(
					EP.ORXPath("#BaselineRecord_ParentMaritalStatusOptions_CB")
							+ "[" + EP.RandomValue(1, 4) + "]", "ON");
			EP.Wait(1);
			EP.ChkBx_SetOption(
					EP.ORXPath("#BaselineRecord_ParentEmploymentStatusOptions_CB")
							+ "[" + EP.RandomValue(1, 4) + "]", "ON");
			EP.Wait(1);
			EP.DrpDwn_SelectIndex(
					"#BaselineRecord_EducationStatusOfPersonGR_DD",
					EP.RandomValue(1, 2));
			EP.Btn_Click("#General_AddRecordSave_BN");

			break;

		case FRANCE:
			EP.Btn_Click("#General_First_BN");
			EP.SelectDate("#BaselineRecord_AddSocioEconomicDateFR_IM", EP
					.Date_AddDays(EP.TestData("ECOSPatientDOB"), 2,
							Calendar.YEAR));
			EP.HideCursor();
			EP.Wait(2);
			EP.DrpDwn_SelectIndex(
					"#BaselineRecord_AddSocioeconomicParentES_DD",
					EP.RandomValue(1, 3));
			EP.ChkBx_SetOption(
					EP.ORXPath("#BaselineRecord_ParentMaritalStatusOptions_CB")
							+ "[" + EP.RandomValue(1, 3) + "]", "ON");
			EP.Wait(1);
			EP.ChkBx_SetOption(
					EP.ORXPath("#BaselineRecord_ParentEmploymentStatusOptions_CB")
							+ "[" + EP.RandomValue(1, 4) + "]", "ON");
			EP.Wait(1);
			EP.Edit_SetValue("#BaselineRecord_NumberOfYearsRpeatedFR_EB",
					EP.Converter("12.4"));
			EP.Btn_Click("#General_AddRecordSave_BN");

			break;

		case CZECHREPUBLIC:
			EP.Btn_Click("#General_First_BN");// Click Add Button
			EP.SelectDate("#BaselineRecord_AddSocioEconomicDateFR_IM", EP
					.Date_AddDays(EP.TestData("ECOSPatientDOB"), 2,
							Calendar.YEAR));
			EP.HideCursor();
			EP.Wait(1);
			EP.DrpDwn_SelectIndex(
					"#BaselineRecord_AddSocioeconomicParentCZ_DD",
					EP.RandomValue(1, 3));
			EP.ChkBx_SetOption(
					EP.ORXPath("#BaselineRecord_ParentMaritalStatusOptions_CB")
							+ "[" + EP.RandomValue(1, 4) + "]", "ON");
			EP.ChkBx_SetOption(
					"#BaselineRecord_ParentEmploymentStatusPleaseSpecifyES_CB",
					"OFF");
			EP.ChkBx_SetOption(
					EP.ORXPath("#BaselineRecord_ParentEmploymentStatusOptions_CB")
							+ "[" + EP.RandomValue(1, 4) + "]", "ON");
			EP.DrpDwn_SelectIndex(
					"#BaselineRecord_EducationStatusOfPersonCZ_DD",
					EP.RandomValue(1, 2));
			EP.Btn_Click("#General_AddRecordSave_BN");
			break;

		case HUNGARY:
			EP.Btn_Click("#General_First_BN");// Click Add Button
			EP.SelectDate("#BaselineRecord_AddSocioeconomicDateES_IM", EP
					.Date_AddDays(EP.TestData("ECOSPatientDOB"), 2,
							Calendar.YEAR));
			EP.HideCursor();
			EP.Wait(1);
			EP.DrpDwn_SelectIndex(
					"#BaselineRecord_AddSocioeconomicParentES_DD",
					EP.RandomValue(1, 3));
			EP.ChkBx_SetOption(
					EP.ORXPath("#BaselineRecord_ParentMaritalStatusOptions_CB")
							+ "[" + EP.RandomValue(1, 4) + "]", "ON");
			EP.ChkBx_SetOption(
					"#BaselineRecord_ParentEmploymentStatusPleaseSpecifyES_CB",
					"OFF");
			EP.ChkBx_SetOption(
					EP.ORXPath("#BaselineRecord_ParentEmploymentStatusOptions_CB")
							+ "[" + EP.RandomValue(1, 4) + "]", "ON");
			EP.DrpDwn_SelectIndex(
					"#BaselineRecord_EducationStatusOfPersonES_DD",
					EP.RandomValue(1, 2));
			EP.Btn_Click("#General_AddRecordSave_BN");

			break;

		case NORWAY:
			EP.Btn_Click("#General_First_BN");// Click Add Button
			EP.SelectDate("#BaselineRecord_AddSocioeconomicDateES_IM", EP
					.Date_AddDays(EP.TestData("ECOSPatientDOB"), 2,
							Calendar.YEAR));
			EP.DrpDwn_SelectIndex(
					"#BaselineRecord_AddSocioeconomicParentES_DD",
					EP.RandomValue(1, 3));
			EP.ChkBx_SetOption(
					EP.ORXPath("#BaselineRecord_ParentMaritalStatusOptions_CB")
							+ "[" + EP.RandomValue(1, 4) + "]", "ON");
			EP.ChkBx_SetOption(
					"#BaselineRecord_ParentEmploymentOtherSpecifyFR_CB", "OFF");
			EP.ChkBx_SetOption(
					EP.ORXPath("#BaselineRecord_ParentsEmploymentStatusOptions_CB")
							+ "[" + EP.RandomValue(1, 4) + "]", "ON");
			EP.DrpDwn_SelectIndex(
					"#BaselineRecord_EducationStatusOfPersonES_DD",
					EP.RandomValue(1, 2));
			EP.Btn_Click("#General_AddRecordSave_BN");
			break;

		case CANADA:
			EP.Btn_Click("#General_First_BN");// Click Add Button
			EP.Wait(1);
			EP.ChkBx_SetOption(
					"#BaselineRecord_ParentEmploymentOtherSpecify_CB", "OFF");
			EP.SelectDate("#BaselineRecord_VisitDate_IM", EP.Date_AddDays(
					EP.TestData("ECOSPatientDOB"), 2, Calendar.YEAR));
			EP.HideCursor();
			EP.Wait(2);
			EP.DrpDwn_SelectIndex(
					"#BaselineRecord_AddSocioeconomicParentCA_DD",
					EP.RandomValue(1, 3));
			EP.ChkBx_SetOption(
					EP.ORXPath("#BaselineRecord_ParentMaritalStatusOptions_CB")
							+ "[" + EP.RandomValue(1, 3) + "]", "ON");
			EP.Wait(1);
			EP.ChkBx_SetOption(
					EP.ORXPath("#BaselineRecord_ParentEmploymentStatusOptions_CB")
							+ "[" + EP.RandomValue(1, 4) + "]", "ON");
			EP.Wait(1);
			EP.Btn_Click("#General_AddRecordSave_BN");
			break;

		default:
		}
		VerifySuccessMessage(EP.LangData("SocioEconomicInformation"));
	}

	public void CurrentGHBaselineAdd(boolean IsValid, String TabIndex,
			String PatientDOB) {
		// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*CURRENTGHBASELINE*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
		EP.ShowTab(TabIndex);
		String[] InputValues;
		// Delete data row from table if it exists
		if (EP.ObjectExists("#General_Delete_IM")) {
			DeleteRecord("#General_Delete_IM", false);
		}

		switch (switchCountryName) {
		case UNITEDKINGDOM:
			if (IsValid) {
				InputValues = new String[] { "0.01",
						EP.Date_AddDays(PatientDOB, 2, Calendar.DATE) };
			} else {
				InputValues = new String[] { "1",
						EP.Date_AddDays(PatientDOB, -1, Calendar.YEAR) };
			}
			EP.SelectDate("#BaselineRecord_TreatmentStartDateGB_IM",
					InputValues[1]);
			EP.HideCursor();
			EP.Wait(2);
			EP.Edit_SetValue("#BaselineRecord_CurrentGHDailyDoseGB_EB",
					EP.Converter(InputValues[0]));
			EP.Rbtn_SetOption(
					EP.ORXPath("#BaselineRecord_FormulationOptions_RB") + "["
							+ EP.RandomValue(1, 2) + "]", "ON");
			EP.Btn_Click("#General_First_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_First_BN");
			}
			break;

		case SWEDEN:
			if (IsValid) {
				InputValues = new String[] { "0.01",
						EP.Date_AddDays(PatientDOB, 2, Calendar.DATE) };
			} else {
				InputValues = new String[] { "1",
						EP.Date_AddDays(PatientDOB, -1, Calendar.YEAR) };
			}
			EP.DrpDwn_SelectIndex(
					"#BaselineRecord_CurrentGHTreatmentStartSE_DB", 1);
			EP.DrpDwn_SelectIndex("#BaselineRecord_CurrentGHDailyDoseSE_DB", 1);
			EP.DrpDwn_SelectIndex("#BaselineRecord_CurrentGHFormulationSE_DB",
					EP.RandomValue(1, 3));
			EP.SelectDate("#BaselineRecord_CurrentGHTreatmentStartSE_IM",
					InputValues[1]);
			EP.HideCursor();
			EP.Wait(2);
			EP.Edit_SetValue("#BaselineRecord_CurrentGHDailyDoseSE_EB",
					EP.Converter(InputValues[0]));
			EP.Btn_Click("#General_First_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_First_BN");
			}
			break;

		case ITALY:
			if (IsValid) {
				InputValues = new String[] { EP.Converter("0.01"),
						EP.Date_AddDays(PatientDOB, 2, Calendar.DATE) };
			} else {
				InputValues = new String[] { "1",
						EP.Date_AddDays(PatientDOB, -1, Calendar.YEAR) };
			}
			EP.SelectDate("#BaselineRecord_TSDCalendar_IM", InputValues[1]);
			EP.HideCursor();
			EP.Wait(2);
			EP.Edit_SetValue("#BaselineRecord_DailyDoseIT_EB", InputValues[0]);
			EP.ChkBx_SetOption("#BaselineRecord_SaizenFreezeDriedIT_CB", "ON");
			EP.Btn_Click("#General_First_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_First_BN");
			}
			break;

		case SLOVAKIA:
			if (IsValid) {
				InputValues = new String[] { EP.Converter("0.01"),
						EP.Date_AddDays(PatientDOB, 2, Calendar.DATE) };
			} else {
				InputValues = new String[] { "1",
						EP.Date_AddDays(PatientDOB, -1, Calendar.YEAR) };
			}
			EP.SelectDate("#BaselineRecord_TSDCalendar_IM", InputValues[1]);
			EP.HideCursor();
			EP.Wait(2);
			EP.Edit_SetValue("#BaselineRecord_DailyDoseIT_EB", InputValues[0]);
			EP.ChkBx_SetOption("#BaselineRecord_SaizenFreezeDriedIT_CB", "ON");
			EP.Btn_Click("#General_First_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_First_BN");
			}
			break;

		case GREECE:
			if (IsValid) {
				InputValues = new String[] { EP.Converter("0.01"),
						EP.Date_AddDays(PatientDOB, 2, Calendar.DATE) };
			} else {
				InputValues = new String[] { "1",
						EP.Date_AddDays(PatientDOB, -1, Calendar.YEAR) };
			}
			EP.DrpDwn_SelectIndex("#BaselineRecord_FormulationGR_DD",
					EP.RandomValue(1, 3));
			EP.SelectDate("#BaselineRecord_TSDCalendar_IM", InputValues[1]);
			EP.HideCursor();
			EP.Wait(2);
			EP.Edit_SetValue("#BaselineRecord_DailyDoseIT_EB", InputValues[0]);
			EP.Btn_Click("#General_First_BN");
			if (!IsValid)
				EP.Btn_Click("#General_First_BN");
			break;

		case CANADA:
			if (IsValid) {
				InputValues = new String[] { EP.Converter("0.01"),
						EP.Date_AddDays(PatientDOB, 2, Calendar.DATE) };
			} else {
				InputValues = new String[] { "1",
						EP.Date_AddDays(PatientDOB, -1, Calendar.YEAR) };
			}
			EP.SelectDate("#BaselineRecord_TSDCalendar_IM", InputValues[1]);
			EP.HideCursor();
			EP.Wait(2);
			EP.Edit_SetValue("#BaselineRecord_DailyDoseIT_EB", InputValues[0]);
			EP.DrpDwn_SelectIndex("#BaselineRecord_FormulationGR_DD",
					EP.RandomValue(1, 2));
			EP.Btn_Click("#General_First_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_First_BN");
			}
			break;

		case CZECHREPUBLIC:
			if (IsValid) {
				InputValues = new String[] { "20-JAN-2000",
						EP.Converter("0.001") };
			} else {
				InputValues = new String[] { "20-JAN-2000", "1" };
			}
			EP.SelectDate("#BaselineRecord_TSDCalendar_IM", InputValues[0]);
			EP.HideCursor();
			EP.Wait(2);
			EP.Edit_SetValue("#BaselineRecord_CurrentGHDailyDoseCZ_EB",
					EP.Converter(InputValues[1]));
			EP.Rbtn_SetOption(
					EP.ORXPath("#BaselineRecord_FormulationOptions_RB") + "["
							+ EP.RandomValue(1, 2) + "]", "ON");
			EP.Btn_Click("#General_First_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_First_BN");
			}
			break;
		case HUNGARY:
			if (IsValid) {
				InputValues = new String[] { "20-JAN-2000",
						EP.Converter("0.001") };
			} else {
				InputValues = new String[] { "20-JAN-2000", "1" };
			}
			EP.SelectDate("#BaselineRecord_TSDCalendar_IM", InputValues[0]);
			EP.HideCursor();
			EP.Wait(2);
			EP.Edit_SetValue("#BaselineRecord_CurrentGHDailyDoseCZ_EB",
					EP.Converter(InputValues[1]));
			EP.Rbtn_SetOption(
					EP.ORXPath("#BaselineRecord_FormulationOptions_RB") + "["
							+ EP.RandomValue(1, 2) + "]", "ON");
			EP.Btn_Click("#General_First_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_First_BN");
			}
			break;

		case NORWAY:
			if (IsValid) {
				InputValues = new String[] { "20-JAN-2000", "0.001" };
			} else {
				InputValues = new String[] { "20-JAN-2000", "1" };
			}
			EP.SelectDate("#BaselineRecord_TSDCalendar_IM", InputValues[0]);
			EP.HideCursor();
			EP.Wait(2);
			EP.Edit_SetValue("#BaselineRecord_CurrentGHDailyDoseCZ_EB",
					EP.Converter(InputValues[1]));
			EP.Rbtn_SetOption(
					EP.ORXPath("#BaselineRecord_FormulationOptions_RB") + "["
							+ EP.RandomValue(1, 2) + "]", "ON");
			EP.Btn_Click("#General_First_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_First_BN");
			}
			break;

		case AUSTRIA:
			if (IsValid) {
				InputValues = new String[] { "20-JAN-2000", "0.001" };
			} else {
				InputValues = new String[] { "20-JAN-2000", "1" };
			}
			EP.SelectDate("#BaselineRecord_TSDCalendar_IM", InputValues[0]);
			EP.HideCursor();
			EP.Wait(2);
			EP.Edit_SetValue("#BaselineRecord_DailyDoseAT_EB",
					EP.Converter(InputValues[1]));
			EP.ChkBx_SetOption("#BaselineRecord_CurrentGHSaizenFreezeAT_CB",
					"ON");
			EP.Btn_Click("#General_First_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_First_BN");
			}
			break;

		case UNITEDARABEMIRATES:
		case SAUDIARABIA:
			if (IsValid) {
				InputValues = new String[] { "20-JAN-2000", "0.001" };
			} else {
				InputValues = new String[] { "20-JAN-2000", "1" };
			}
			EP.SelectDate("#BaselineRecord_TSDCalendar_IM", InputValues[0]);
			EP.HideCursor();
			EP.Wait(2);
			EP.Edit_SetValue("#BaselineRecord_DailyDoseAT_EB",
					EP.Converter(InputValues[1]));
			EP.ChkBx_SetOption("#BaselineRecord_CurrentGHSaizenFreezeAT_CB",
					"ON");
			EP.Btn_Click("#General_First_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_First_BN");
			}
			break;

		case SINGAPORE:
			if (IsValid) {
				InputValues = new String[] { "20-JAN-2000", "0.001" };
			} else {
				InputValues = new String[] { "20-JAN-2000", "1" };
			}
			EP.SelectDate("#BaselineRecord_TSDCalendar_IM", InputValues[0]);
			EP.HideCursor();
			EP.Wait(2);
			EP.Edit_SetValue("#BaselineRecord_DailyDoseAT_EB",
					EP.Converter(InputValues[1]));
			EP.ChkBx_SetOption("#BaselineRecord_CurrentGHSaizenFreezeAT_CB",
					"ON");
			EP.Btn_Click("#General_First_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_First_BN");
			}
			break;

		case MEXICO:
			if (IsValid) {
				InputValues = new String[] { "20-JAN-2000", "0.001" };
			} else {
				InputValues = new String[] { "20-JAN-2000", "1" };
			}
			EP.SelectDate("#BaselineRecord_TSDCalendar_IM", InputValues[0]);
			EP.HideCursor();
			EP.Wait(2);
			EP.Edit_SetValue("#BaselineRecord_DailyDoseAT_EB",
					EP.Converter(InputValues[1]));
			EP.ChkBx_SetOption("#BaselineRecord_CurrentGHSaizenFreezeAT_CB",
					"ON");
			EP.Btn_Click("#General_First_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_First_BN");
			}
			break;

		case SPAIN:
			if (IsValid) {
				InputValues = new String[] { "0.001", "20-JAN-2000" };
			} else {
				InputValues = new String[] { "1", "20-JAN-2000" };
			}
			EP.Edit_SetValue("#BaselineRecord_DailyDoseES_EB", InputValues[0]);
			EP.DrpDwn_SelectIndex("#BaselineRecord_FormulationES_DD", 2);
			EP.SelectDate("#BaselineRecord_TreatmentStartDateES_IM",
					InputValues[1]);
			EP.HideCursor();
			EP.Wait(2);
			EP.Btn_Click("#General_First_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_First_BN");
			}
			break;

		case KOREA:
			if (IsValid) {
				InputValues = new String[] { "0.001", "20-JAN-2000" };
			} else {
				InputValues = new String[] { "1", "20-JAN-2000" };
			}
			EP.Edit_SetValue("#BaselineRecord_DailyDoseAT_EB", InputValues[0]);
			EP.ChkBx_SetOption("#BaselineRecord_CurrentGHSaizenFreezeAT_CB",
					"ON");
			EP.SelectDate("#BaselineRecord_TreatmentStartDateAT_IM",
					InputValues[1]);
			EP.HideCursor();
			EP.Wait(2);
			EP.Btn_Click("#General_First_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_First_BN");
			}
			break;

		case AUSTRALIA:
			if (IsValid) {
				InputValues = new String[] { "20-JAN-2000", "0.001" };
			} else {
				InputValues = new String[] { "20-JAN-2000", "1" };
			}
			EP.SelectDate("#BaselineRecord_TreatmentStartDateAT_IM",
					InputValues[0]);
			EP.HideCursor();
			EP.Wait(2);
			EP.Edit_SetValue("#BaselineRecord_DailyDoseAT_EB", InputValues[1]);
			EP.Rbtn_SetOption(
					EP.ORXPath("#BaselineRecord_FormulationOptions_RB") + "["
							+ EP.RandomValue(1, 2) + "]", "ON");
			EP.Btn_Click("#General_First_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_First_BN");
			}
			break;

		case FINLAND:
			if (IsValid) {
				InputValues = new String[] { "20-JAN-2000", "0.001" };
			} else {
				InputValues = new String[] { "20-JAN-2000", "1" };
			}
			EP.SelectDate("#BaselineRecord_TSDCalendar_IM", InputValues[0]);
			EP.HideCursor();
			EP.Wait(2);
			EP.HideCursor();
			EP.Wait(2);
			EP.Edit_SetValue("#BaselineRecord_CurrentGHDailyDoseCZ_EB",
					InputValues[1]);
			EP.Rbtn_SetOption(
					EP.ORXPath("#BaselineRecord_FormulationOptions_RB") + "["
							+ EP.RandomValue(1, 2) + "]", "ON");

			EP.Btn_Click("#General_First_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_First_BN");
			}
			break;

		case ARGENTINA:
			if (IsValid) {
				InputValues = new String[] { EP.Converter("0.01"),
						EP.Date_AddDays(PatientDOB, 2, Calendar.DATE) };
			} else {
				InputValues = new String[] { "1",
						EP.Date_AddDays(PatientDOB, -1, Calendar.YEAR) };
			}
			EP.SelectDate("#BaselineRecord_TSDCalendar_IM", InputValues[1]);
			EP.HideCursor();
			EP.Wait(2);
			EP.Edit_SetValue("#BaselineRecord_CurrentGHDailyDoseCZ_EB",
					InputValues[0]);
			EP.ChkBx_SetOption("#BaselineRecord_CurrentGHSaizenFreezeIS_CB",
					"ON");
			// EP.SelectDate("#BaselineRecord_CurrentGHTreatmentStopDateAR_IM",EP.GetTimeStamp("dd MMM yyyy"));
			EP.HideCursor();
			EP.Wait(2);
			EP.Btn_Click("#General_First_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_First_BN");
			}
			break;
		case COLOMBIA:
		case TAIWAN:
		case INDONESIA:
			if (IsValid) {
				InputValues = new String[] { EP.Converter("0.01"),
						EP.Date_AddDays(PatientDOB, 2, Calendar.DATE) };
			} else {
				InputValues = new String[] { "1",
						EP.Date_AddDays(PatientDOB, -1, Calendar.YEAR) };
			}
			EP.SelectDate("#BaselineRecord_TSDCalendar_IM", InputValues[1]);
			EP.HideCursor();
			EP.Wait(2);
			EP.Edit_SetValue("#BaselineRecord_CurrentGHDailyDoseCZ_EB",
					InputValues[0]);
			EP.ChkBx_SetOption("#BaselineRecord_CurrentGHSaizenFreezeIS_CB",
					"ON");
			EP.HideCursor();
			EP.Wait(2);
			EP.Btn_Click("#General_First_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_First_BN");
			}
			break;

		case ICELAND:
			if (IsValid) {
				InputValues = new String[] { "20-JAN-2000", "0.001" };
			} else {
				InputValues = new String[] { "20-JAN-2000", "1" };
			}
			EP.SelectDate("#BaselineRecord_TSDCalendar_IM", InputValues[0]);
			EP.HideCursor();
			EP.Wait(2);
			EP.Edit_SetValue("#BaselineRecord_CurrentGHDailyDoseCZ_EB",
					InputValues[1]);
			EP.ChkBx_SetOption("#BaselineRecord_CurrentGHSaizenFreezeIS_CB",
					"ON");
			EP.Btn_Click("#General_First_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_First_BN");
			}
			break;

		case FRANCE:
			if (IsValid) {
				InputValues = new String[] { "20-JAN-2000", "0.001" };
			} else {
				InputValues = new String[] { "20-JAN-2000", "1" };
			}
			EP.SelectDate("#BaselineRecord_CurrentGHTreatmentStartDateES_IM",
					InputValues[0]);
			EP.HideCursor();
			EP.Wait(2);
			EP.Edit_SetValue("#BaselineRecord_CurrentGHDailyDoseFR_EB",
					EP.Converter(InputValues[1]));
			EP.DrpDwn_SelectIndex("#BaselineRecord_CurrentGHFormulationFR_DD",
					EP.RandomValue(1, 2));
			EP.Btn_Click("#General_First_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_First_BN");
			}

			break;

		default:
		}
		VerifySuccessMessage(EP.LangData("CurrentGHTreatment"));
	}

	public void TannerStageBaselineAdd(boolean IsValid, String TabIndex,
			String PatientDOB, boolean Menarche, String Gender, String Puberty) {
		// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*TANNER
		// STAGE(BASELINE)*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-**-*-*-*-*-*-*-*-*-*-*-*-*-*
		EP.ShowTab(TabIndex);
		String[] InputValues;
		if (EP.ObjectExists("#General_Delete_IM")) {
			DeleteRecord("#General_Delete_IM", false);
		}

		switch (switchCountryName) {
		case SWEDEN:
			if (IsValid) {
				InputValues = new String[] {
						EP.GetTimeStamp("dd MMM yyyy"),
						"12",
						"12",
						EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"), -5,
								Calendar.YEAR), "5", "2" };
			} else {
				InputValues = new String[] {
						EP.GetTimeStamp("dd MMM yyyy"),
						"0",
						"0",
						EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"), -15,
								Calendar.YEAR), "13", "13" };
			}
			EP.Btn_Click("#General_First_BN");
			EP.Wait(2);
			EP.SelectDate("#BaselineRecord_AssessmentDateTannerFemaleES_IM",
					InputValues[0]);
			EP.HideCursor();
			EP.Wait(2);
			EP.DrpDwn_SelectIndex(
					"#BaselineRecord_PubertalBreastStageTannerFemaleES_DD", 1);
			if (Gender.compareToIgnoreCase("Female") == 0) {
				EP.DrpDwn_SelectIndex(
						"#BaselineRecord_AddTannerMenarcheFemaieES_DD", 1);
				EP.Edit_SetValue("#BaselineRecord_AddTannerMenarcheYearAU_EB",
						InputValues[4]);
				EP.Edit_SetValue(
						"#BaselineRecord_AddTannerMenarcheMonthSIT_EB",
						InputValues[5]);
			} else {
				EP.DrpDwn_SelectIndex(
						"#BaselineRecord_TannerAddPubertalAssessmentES_DD", 1);
				EP.Edit_SetValue(
						"#BaselineRecord_TannerAddTestisVolumeLeftES_EB",
						InputValues[1]);
				EP.Edit_SetValue(
						"#BaselineRecord_TannerAddTestisVolumeRightES_EB",
						InputValues[2]);
			}
			EP.Rbtn_SetOption("#BaselineRecord_PatientEnteredPubertyYesES_RB",
					"ON");
			EP.DrpDwn_SelectIndex(
					"#BaselineRecord_TannerAddPatientEnteredPubertySE_DB", 1);
			EP.SelectDate("#BaselineRecord_TannerAddDateEnteredPubertyES_IM",
					InputValues[3]);
			EP.HideCursor();
			EP.Wait(2);
			EP.Btn_Click("#General_AddRecordSave_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN");
			}
			break;

		case UNITEDKINGDOM:
			if (IsValid) {
				InputValues = new String[] {
						EP.GetTimeStamp("dd MMM yyyy"),
						"12",
						"12",
						EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"), -5,
								Calendar.YEAR),
						EP.Date_AddDays(EP.TestData("ECOSPatientDOBFemale"), 1,
								Calendar.YEAR) };
			} else {
				InputValues = new String[] {
						EP.Date_AddDays(PatientDOB, -2, Calendar.YEAR),
						"0",
						"0",
						EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"), -20,
								Calendar.YEAR),
						EP.Date_AddDays(EP.TestData("ECOSPatientDOBFemale"), 1,
								Calendar.YEAR) };
			}
			EP.Btn_Click("#General_First_BN");
			EP.SelectDate("#BaselineRecord_TannerAddAssessmentDateGB_IM",
					InputValues[0]);
			EP.HideCursor();
			EP.Wait(2);
			EP.ChkBx_SetOption("#BaselineRecord_PatientEnteredPubertyGB_CB",
					"ON");
			EP.DrpDwn_SelectIndex(
					"#BaselineRecord_TannerAddPubertalAssessmentGB_DD", 1);
			EP.SelectDate("#BaselineRecord_TannerAddDateEnteredPubertyGB_IM",
					InputValues[3]);
			EP.HideCursor();
			EP.Wait(2);
			if (Gender.compareToIgnoreCase("Female") == 0) {
				EP.DrpDwn_SelectIndex(
						"#BaselineRecord_AddTannerStageFemaleMenarcheGB_DD", 1);
				EP.DrpDwn_SelectIndex(
						"#BaselineRecord_FemaleMenarcheDateGB_DD", 1);
				EP.SelectDate("#BaselineRecord_MenarcheDateGB_IM",
						InputValues[4]);
				EP.HideCursor();
				EP.Wait(2);
			} else {
				EP.DrpDwn_SelectIndex(
						"#BaselineRecord_TannerAddTestisVolumeLeftGB_DD", 1);
				EP.DrpDwn_SelectIndex(
						"#BaselineRecord_TannerAddTestisVolumeRightGB_DD", 1);
				EP.Edit_SetValue(
						"#BaselineRecord_TannerAddTestisVolumeLeftGB_EB",
						InputValues[1]);
				EP.Edit_SetValue(
						"#BaselineRecord_TannerAddTestisVolumeRightGB_EB",
						InputValues[2]);
			}
			EP.Btn_Click("#General_AddRecordSave_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN");
			}
			break;

		case ITALY:

			if (IsValid) {
				InputValues = new String[] {
						EP.GetTimeStamp("dd MMM yyyy"),
						"12",
						"12",
						EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"), -5,
								Calendar.YEAR), "12", "12" };
			} else {
				InputValues = new String[] {
						EP.Date_AddDays(PatientDOB, -2, Calendar.YEAR),
						"13",
						"13",
						EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"), -20,
								Calendar.YEAR), "89", "89" };
			}
			EP.Btn_Click("#General_First_BN");
			EP.SelectDate("#BaselineRecord_TannerAddAssessmentDateIT_IM",
					InputValues[0]);
			EP.HideCursor();
			EP.Wait(2);
			if (Gender.compareToIgnoreCase("Female") == 0) {
				if (Menarche) {
					EP.DrpDwn_SelectIndex(
							"#BaselineRecord_TannerAddPubertalAssessmentIT_DD",
							1);
					EP.DrpDwn_SelectIndex(
							"#BaselineRecord_AddTannerStageFemaleMenarcheIT_DD",
							1);
					EP.Edit_SetValue(
							"#BaselineRecord_AddTannerMenarcheYearIT_EB",
							InputValues[4]);
					EP.Edit_SetValue(
							"#BaselineRecord_AddTannerMenarcheMonthSIT_EB",
							InputValues[4]);

				} else {
					EP.DrpDwn_SelectIndex(
							"#BaselineRecord_TannerAddPubertalAssessmentIT_DD",
							1);
					EP.DrpDwn_SelectIndex(
							"#BaselineRecord_AddTannerStageFemaleMenarcheIT_DD",
							2);
				}
			} else {
				EP.DrpDwn_SelectIndex(
						"#BaselineRecord_TannerAddPubertalAssessmentIT_DD", 1);
				EP.Edit_SetValue(
						"#BaselineRecord_TannerAddTestisVolumeLeftIT_EB",
						InputValues[4]);
				EP.Edit_SetValue(
						"#BaselineRecord_TannerAddTestisVolumeRightIT_EB",
						InputValues[5]);
			}
			EP.Rbtn_SetOption("#BaselineRecord_AddTannerYesIT_RB", "ON");
			EP.SelectDate("#BaselineRecord_TannerAddDateEnteredPubertyIT_IM",
					InputValues[3]);
			EP.HideCursor();
			EP.Wait(2);
			EP.Btn_Click("#General_AddRecordSave_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN");
			}

			break;
		case GREECE:

			if (IsValid) {
				InputValues = new String[] {
						EP.GetTimeStamp("dd MMM yyyy"),
						"12",
						"12",
						EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"), -5,
								Calendar.YEAR), "12", "12" };
			} else {
				InputValues = new String[] {
						EP.Date_AddDays(PatientDOB, -2, Calendar.YEAR),
						"13",
						"13",
						EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"), -20,
								Calendar.YEAR), "89", "89" };
			}
			EP.Btn_Click("#General_First_BN");
			EP.SelectDate("#BaselineRecord_TannerAddAssessmentDateIT_IM",
					InputValues[0]);
			EP.HideCursor();
			EP.Wait(2);
			if (Gender.compareToIgnoreCase("Female") == 0) {
				if (Menarche) {
					EP.DrpDwn_SelectIndex(
							"#BaselineRecord_TannerAddPubertalAssessmentIT_DD",
							1);
					EP.DrpDwn_SelectIndex(
							"#BaselineRecord_AddTannerStageFemaleMenarcheIT_DD",
							1);
					EP.Edit_SetValue(
							"#BaselineRecord_AddTannerMenarcheYearIT_EB",
							InputValues[4]);
					EP.Edit_SetValue(
							"#BaselineRecord_AddTannerMenarcheMonthSIT_EB",
							InputValues[4]);

				} else {
					EP.DrpDwn_SelectIndex(
							"#BaselineRecord_TannerAddPubertalAssessmentIT_DD",
							1);
					EP.DrpDwn_SelectIndex(
							"#BaselineRecord_AddTannerStageFemaleMenarcheIT_DD",
							2);
				}
			} else {
				EP.DrpDwn_SelectIndex(
						"#BaselineRecord_TannerAddPubertalAssessmentIT_DD", 1);
				EP.Edit_SetValue(
						"#BaselineRecord_TannerAddTestisVolumeLeftIT_EB",
						InputValues[4]);
				EP.Edit_SetValue(
						"#BaselineRecord_TannerAddTestisVolumeRightIT_EB",
						InputValues[5]);
			}
			EP.Rbtn_SetOption("#BaselineRecord_AddTannerYesIT_RB", "ON");
			EP.SelectDate("#BaselineRecord_TannerAddDateEnteredPubertyIT_IM",
					InputValues[3]);
			EP.HideCursor();
			EP.Wait(2);
			EP.Btn_Click("#General_AddRecordSave_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN");
			}

			break;

		case CANADA:
		case TAIWAN:
		case INDONESIA:
			String ConsentDateValid = EP.Date_AddDays(PatientDOB, 1,
					Calendar.MONTH);
			String AssessmentDateValid = EP.Date_AddDays(ConsentDateValid, -3,
					Calendar.MONTH);

			if (IsValid) {
				InputValues = new String[] {
						AssessmentDateValid,
						"12",
						"12",
						EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"), -5,
								Calendar.YEAR), "5", "2" };
			} else {
				InputValues = new String[] {
						EP.Date_AddDays(AssessmentDateValid, -1, Calendar.DATE),
						"0",
						"0",
						EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"), -15,
								Calendar.YEAR), "13", "13" };
			}
			EP.Btn_Click("#General_First_BN");
			EP.Wait(1);
			EP.DrpDwn_SelectIndex(
					"#BaselineRecord_TannerStageAddAssessmentDateCA_DD", 1);
			if (Gender.compareToIgnoreCase("Female") == 0) {
				int RandomMenarche;
				if (!IsValid)
					RandomMenarche = 1;
				else
					RandomMenarche = EP.RandomValue(1, 2);
				EP.DrpDwn_SelectIndex(
						"#BaselineRecord_TannerAddPubertalAssessmentAT_DD", 1);
				EP.DrpDwn_SelectIndex(
						"#BaselineRecord_AddTannerStageMenarcheAT_DD",
						RandomMenarche);

				if (RandomMenarche == 1) {
					EP.Edit_SetValue(
							"#BaselineRecord_AddTannerMenarcheYearsAT_EB",
							InputValues[4]);
					EP.Edit_SetValue(
							"#BaselineRecord_AddTannerMenarcheMonthsAT_EB",
							InputValues[5]);
				}
				EP.SelectDate("#BaselineRecord_TannerAddAssessmentDateAT_IM",
						InputValues[0]);
				EP.Rbtn_SetOption("#BaselineRecord_AddTannerYesAT_RB", "ON");
				EP.SelectDate(
						"#BaselineRecord_TannerAddDateEnteredPubertyAT_IM",
						InputValues[3]);

			} else {
				EP.DrpDwn_SelectIndex("Baseline_TannerAssessmentDate_DD", 1);
				EP.SelectDate("#BaselineRecord_TannerAddAssessmentDateAT_IM",
						InputValues[0]);
				EP.HideCursor();
				EP.Wait(2);
				EP.DrpDwn_SelectIndex(
						"#BaselineRecord_TannerAddPubertalAssessmentIT_DD", 1);
				EP.Edit_SetValue(
						"#BaselineRecord_TannerAddTestisVolumeLeftAT_EB",
						InputValues[1]);
				EP.Edit_SetValue(
						"#BaselineRecord_TannerAddTestisVolumeRightAT_EB",
						InputValues[2]);
				EP.Rbtn_SetOption("#BaselineRecord_AddTannerYesAT_RB", "ON");
				EP.SelectDate(
						"#BaselineRecord_TannerAddDateEnteredPubertyAT_IM",
						InputValues[3]);
				EP.HideCursor();
				EP.Wait(2);
			}
			EP.Btn_Click("#General_AddRecordSave_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN");
			}

			break;

		/*
		 * if (IsValid){ InputValues = new
		 * String[]{EP.GetTimeStamp("dd MMM yyyy"
		 * ),"12","12",EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"), -5,
		 * Calendar.YEAR),"5","2"}; }else{ InputValues = new
		 * String[]{EP.Date_AddDays(PatientDOB, -2,
		 * Calendar.YEAR),"0","0",EP.Date_AddDays
		 * (EP.GetTimeStamp("dd MMM yyyy"), -20, Calendar.YEAR),"13","13"};
		 * }EP.Btn_Click("#General_First_BN");
		 * EP.DrpDwn_SelectIndex("#BaselineRecord_TannerStageAddAssessmentDateCA_DD"
		 * ,1); EP.SelectDate("#BaselineRecord_TannerAddAssessmentDateIT_IM",
		 * InputValues[0]); EP.HideCursor(); EP.Wait(2);
		 * EP.DrpDwn_SelectIndex("#BaselineRecord_TannerAddPubertalAssessmentIT_DD"
		 * , 1); if(Gender.compareToIgnoreCase("Female")==0){ if(Menarche){
		 * EP.DrpDwn_SelectIndex("#BaselineRecord_AddTannerStageMenarcheCA_DD",
		 * 1); EP.Edit_SetValue("#BaselineRecord_AddTannerMenarcheYearCA_EB",
		 * InputValues[4]);
		 * EP.Edit_SetValue("#BaselineRecord_AddTannerMenarcheMonthSCA_EB"
		 * ,InputValues[4]); }else{
		 * EP.DrpDwn_SelectIndex("#BaselineRecord_AddTannerStageMenarcheCA_DD",
		 * 2); } }else{
		 * EP.Edit_SetValue("#BaselineRecord_TannerAddTestisVolumeLeftIT_EB",
		 * InputValues[1]);
		 * EP.Edit_SetValue("#BaselineRecord_TannerAddTestisVolumeRightIT_EB",
		 * InputValues[2]); }
		 * 
		 * EP.Rbtn_SetOption("#BaselineRecord_AddTannerYesIT_RB", "ON");
		 * EP.SelectDate("#BaselineRecord_TannerAddDateEnteredPubertyIT_IM",
		 * InputValues[3]); EP.HideCursor(); EP.Wait(2);
		 * EP.Btn_Click("#General_AddRecordSave_BN"); if(!IsValid){
		 * EP.Btn_Click("#General_AddRecordSave_BN"); } break;
		 */

		case AUSTRIA:
			if (IsValid) {
				InputValues = new String[] {
						EP.GetTimeStamp("dd MMM yyyy"),
						"12",
						"12",
						EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"), -5,
								Calendar.YEAR), "5", "2" };
			} else {
				InputValues = new String[] {
						EP.GetTimeStamp("dd MMM yyyy"),
						"0",
						"0",
						EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"), -15,
								Calendar.YEAR), "13", "13" };
			}
			EP.Btn_Click("#General_First_BN");
			if (Gender.compareToIgnoreCase("Female") == 0) {
				int RandomMenarche;
				if (!IsValid)
					RandomMenarche = 1;
				else
					RandomMenarche = EP.RandomValue(1, 2);
				EP.DrpDwn_SelectIndex(
						"#BaselineRecord_TannerAddPubertalAssessmentAT_DD",
						EP.RandomValue(1, 5));
				EP.DrpDwn_SelectIndex(
						"#BaselineRecord_AddTannerStageMenarcheAT_DD",
						RandomMenarche);

				if (RandomMenarche == 1) {
					EP.Edit_SetValue(
							"#BaselineRecord_AddTannerMenarcheYearsAT_EB",
							InputValues[4]);
					EP.Edit_SetValue(
							"#BaselineRecord_AddTannerMenarcheMonthsAT_EB",
							InputValues[5]);
				}
				EP.SelectDate("#BaselineRecord_TannerAddAssessmentDateAT_IM",
						InputValues[0]);
				EP.Rbtn_SetOption("#BaselineRecord_AddTannerYesAT_RB", "ON");
				EP.SelectDate(
						"#BaselineRecord_TannerAddDateEnteredPubertyAT_IM",
						InputValues[3]);

			} else {
				EP.SelectDate("#BaselineRecord_TannerAddAssessmentDateAT_IM",
						InputValues[0]);
				EP.HideCursor();
				EP.Wait(2);
				EP.DrpDwn_SelectIndex(
						"#BaselineRecord_TannerAddPubertalAssessmentIT_DD",
						EP.RandomValue(1, 5));
				EP.Edit_SetValue(
						"#BaselineRecord_TannerAddTestisVolumeLeftAT_EB",
						InputValues[1]);
				EP.Edit_SetValue(
						"#BaselineRecord_TannerAddTestisVolumeRightAT_EB",
						InputValues[2]);
				EP.Rbtn_SetOption("#BaselineRecord_AddTannerYesAT_RB", "ON");
				EP.SelectDate(
						"#BaselineRecord_TannerAddDateEnteredPubertyAT_IM",
						InputValues[3]);
				EP.HideCursor();
				EP.Wait(2);
			}
			EP.Btn_Click("#General_AddRecordSave_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN");
			}

			break;
		case ARGENTINA:
		case COLOMBIA:
			ConsentDateValid = EP.Date_AddDays(PatientDOB, 1, Calendar.MONTH);
			AssessmentDateValid = EP.Date_AddDays(ConsentDateValid, -3,
					Calendar.MONTH);

			if (IsValid) {
				InputValues = new String[] {
						AssessmentDateValid,
						"12",
						"12",
						EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"), -5,
								Calendar.YEAR), "5", "2" };
			} else {
				InputValues = new String[] {
						EP.Date_AddDays(AssessmentDateValid, -1, Calendar.DATE),
						"0",
						"0",
						EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"), -15,
								Calendar.YEAR), "13", "13" };
			}
			EP.Btn_Click("#General_First_BN");
			EP.Wait(1);
			if (Gender.compareToIgnoreCase("Female") == 0) {

				EP.DrpDwn_SelectIndex(
						"#BaselineRecord_PubertalBreastStageTannerFemaleSG_DD",
						1);
				EP.Rbtn_SetOption("#BaselineRecord_TannerMenarcheYesAR_RB",
						"ON");
				EP.Rbtn_SetOption(
						"#BaselineRecord_PatientEnteredPubertyYesAT_RB", "ON");

				EP.Edit_SetValue("#BaselineRecord_AddTannerMenarcheYearCA_EB",
						InputValues[4]);
				EP.Edit_SetValue(
						"#BaselineRecord_AddTannerMenarcheMonthSCA_EB",
						InputValues[5]);

				EP.SelectDate(
						"#BaselineRecord_AssessmentDateTannerFemaleAT_IM",
						InputValues[0]);
				EP.Rbtn_SetOption(
						"#BaselineRecord_PatientEnteredPubertyYesAT_RB", "ON");
				EP.SelectDate(
						"#BaselineRecord_TannerAddDateEnteredPubertyAT_IM",
						InputValues[3]);

			} else {
				EP.SelectDate("#BaselineRecord_TannerAddAssessmentDateAT_IM",
						InputValues[0]);
				EP.HideCursor();
				EP.Wait(2);
				EP.DrpDwn_SelectIndex(
						"#BaselineRecord_TannerAddPubertalAssessmentSG_DD", 1);
				EP.Edit_SetValue(
						"#BaselineRecord_TannerAddTestisVolumeLeftAT_EB",
						InputValues[1]);
				EP.Edit_SetValue(
						"#BaselineRecord_TannerAddTestisVolumeRightAT_EB",
						InputValues[2]);
				EP.Rbtn_SetOption("#BaselineRecord_AddTannerYesAT_RB", "ON");
				EP.SelectDate(
						"#BaselineRecord_TannerAddDateEnteredPubertyAT_IM",
						InputValues[3]);
				EP.HideCursor();
				EP.Wait(2);
			}
			EP.Btn_Click("#General_AddRecordSave_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN");
			}

			break;

		case SINGAPORE:
		case UNITEDARABEMIRATES:
		case SAUDIARABIA:
		case MEXICO:
			ConsentDateValid = EP.Date_AddDays(PatientDOB, 1, Calendar.MONTH);
			AssessmentDateValid = EP.Date_AddDays(ConsentDateValid, -3,
					Calendar.MONTH);

			if (IsValid) {
				InputValues = new String[] {
						AssessmentDateValid,
						"12",
						"12",
						EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"), -5,
								Calendar.YEAR), "5", "2" };
			} else {
				InputValues = new String[] {
						EP.Date_AddDays(AssessmentDateValid, -1, Calendar.DATE),
						"0",
						"0",
						EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"), -15,
								Calendar.YEAR), "13", "13" };
			}
			EP.Btn_Click("#General_First_BN");
			if (Gender.compareToIgnoreCase("Female") == 0) {
				int RandomMenarche;
				if (!IsValid)
					RandomMenarche = 1;
				else
					RandomMenarche = EP.RandomValue(1, 2);
				EP.DrpDwn_SelectIndex(
						"#BaselineRecord_TannerAddPubertalAssessmentAT_DD", 1);
				EP.DrpDwn_SelectIndex(
						"#BaselineRecord_AddTannerStageMenarcheAT_DD",
						RandomMenarche);

				if (RandomMenarche == 1) {
					EP.Edit_SetValue(
							"#BaselineRecord_AddTannerMenarcheYearsAT_EB",
							InputValues[4]);
					EP.Edit_SetValue(
							"#BaselineRecord_AddTannerMenarcheMonthsAT_EB",
							InputValues[5]);
				}
				EP.SelectDate("#BaselineRecord_TannerAddAssessmentDateAT_IM",
						InputValues[0]);
				EP.Rbtn_SetOption("#BaselineRecord_AddTannerYesAT_RB", "ON");
				EP.SelectDate(
						"#BaselineRecord_TannerAddDateEnteredPubertyAT_IM",
						InputValues[3]);

			} else {
				EP.SelectDate("#BaselineRecord_TannerAddAssessmentDateAT_IM",
						InputValues[0]);
				EP.HideCursor();
				EP.Wait(2);
				EP.DrpDwn_SelectIndex(
						"#BaselineRecord_TannerAddPubertalAssessmentIT_DD",
						EP.RandomValue(1, 5));
				EP.Edit_SetValue(
						"#BaselineRecord_TannerAddTestisVolumeLeftAT_EB",
						InputValues[1]);
				EP.Edit_SetValue(
						"#BaselineRecord_TannerAddTestisVolumeRightAT_EB",
						InputValues[2]);
				EP.Rbtn_SetOption("#BaselineRecord_AddTannerYesAT_RB", "ON");
				EP.SelectDate(
						"#BaselineRecord_TannerAddDateEnteredPubertyAT_IM",
						InputValues[3]);
				EP.HideCursor();
				EP.Wait(2);
			}
			EP.Btn_Click("#General_AddRecordSave_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN");
			}

			break;

		case CZECHREPUBLIC:
			if (IsValid) {
				InputValues = new String[] {
						EP.GetTimeStamp("dd MMM yyyy"),
						"12",
						"12",
						EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"), -5,
								Calendar.YEAR), "5", "2" };
			} else {
				InputValues = new String[] {
						EP.GetTimeStamp("dd MMM yyyy"),
						"0",
						"0",
						EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"), -15,
								Calendar.YEAR), "13", "13" };
			}

			EP.Btn_Click("#General_First_BN");
			if (Gender.compareToIgnoreCase("Female") == 0) {
				int RandomMenarche = 0;
				if (!IsValid)
					RandomMenarche = 1;
				else
					RandomMenarche = EP.RandomValue(1, 3);

				EP.Rbtn_SetOption(
						EP.ORXPath("#BaselineRecord_TannerMenarcheOptions_RB")
								+ "[" + RandomMenarche + "]", "ON");
				EP.DrpDwn_SelectIndex(
						"#BaselineRecord_PubertalBreastStageTannerFemaleHU_DD",
						1);
				EP.SelectDate("#BaselineRecord_TannerAddAssessmentDateAT_IM",
						InputValues[0]);
				EP.HideCursor();
				EP.Wait(2);
				EP.Rbtn_SetOption("#BaselineRecord_AddTannerYesAT_RB", "ON");
				EP.Wait(1);
				EP.SelectDate(
						"#BaselineRecord_TannerAddDateEnteredPubertyAT_IM", EP
								.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"),
										-5, Calendar.YEAR));
				EP.HideCursor();
				EP.Wait(2);
				if (RandomMenarche == 1) {
					EP.Edit_SetValue(
							"#BaselineRecord_AddTannerMenarcheYearFR_EB",
							InputValues[4]);
					EP.Edit_SetValue(
							"#BaselineRecord_AddTannerMenarcheMonthFR_EB",
							InputValues[5]);
				}
			} else {
				EP.DrpDwn_SelectIndex(
						"#BaselineRecord_TannerAddPubertalAssessmentCZ_DD", 1);
				EP.Rbtn_SetOption("#BaselineRecord_AddTannerYesAT_RB", "ON");
				EP.Wait(2);
				EP.SelectDate("#BaselineRecord_TannerAddAssessmentDateAT_IM",
						EP.GetTimeStamp("dd MMM yyyy"));
				EP.HideCursor();
				EP.Wait(2);
				EP.Edit_SetValue(
						"#BaselineRecord_TannerAddTestisVolumeLeftIT_EB",
						InputValues[1]);
				EP.Edit_SetValue(
						"#BaselineRecord_TannerAddTestisVolumeRightIT_EB",
						InputValues[2]);
			}

			EP.Btn_Click("#General_AddRecordSave_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN");

			}
			break;
		case HUNGARY:
			if (IsValid) {
				InputValues = new String[] {
						EP.GetTimeStamp("dd MMM yyyy"),
						"12",
						"12",
						EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"), -5,
								Calendar.YEAR), "5", "2" };
			} else {
				InputValues = new String[] {
						EP.GetTimeStamp("dd MMM yyyy"),
						"0",
						"0",
						EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"), -15,
								Calendar.YEAR), "13", "13" };
			}

			EP.Btn_Click("#General_First_BN");
			if (Gender.compareToIgnoreCase("Female") == 0) {
				int RandomMenarche = 0;
				if (!IsValid)
					RandomMenarche = 1;
				else
					RandomMenarche = EP.RandomValue(1, 3);

				EP.Rbtn_SetOption(
						EP.ORXPath("#BaselineRecord_TannerMenarcheOptions_RB")
								+ "[" + RandomMenarche + "]", "ON");
				EP.DrpDwn_SelectIndex(
						"#BaselineRecord_PubertalBreastStageTannerFemaleHU_DD",
						1);
				EP.SelectDate("#BaselineRecord_TannerAddAssessmentDateAT_IM",
						InputValues[0]);
				EP.HideCursor();
				EP.Wait(2);
				EP.Rbtn_SetOption("#BaselineRecord_AddTannerYesAT_RB", "ON");
				EP.SelectDate(
						"#BaselineRecord_TannerAddDateEnteredPubertyAT_IM", EP
								.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"),
										-5, Calendar.YEAR));
				EP.HideCursor();
				EP.Wait(2);
				if (RandomMenarche == 1) {
					EP.Edit_SetValue(
							"#BaselineRecord_AddTannerMenarcheYearFR_EB",
							InputValues[4]);
					EP.Edit_SetValue(
							"#BaselineRecord_AddTannerMenarcheMonthFR_EB",
							InputValues[5]);
				}
			} else {
				EP.DrpDwn_SelectIndex(
						"#BaselineRecord_TannerAddPubertalAssessmentCZ_DD", 1);
				EP.Rbtn_SetOption("#BaselineRecord_AddTannerYesAT_RB", "ON");
				EP.SelectDate("#BaselineRecord_TannerAddAssessmentDateAT_IM",
						EP.GetTimeStamp("dd MMM yyyy"));
				EP.HideCursor();
				EP.Wait(2);
				EP.Edit_SetValue(
						"#BaselineRecord_TannerAddTestisVolumeLeftIT_EB",
						InputValues[1]);
				EP.Edit_SetValue(
						"#BaselineRecord_TannerAddTestisVolumeRightIT_EB",
						InputValues[2]);
			}

			EP.Btn_Click("#General_AddRecordSave_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN");

			}
			break;

		case NORWAY:
			if (IsValid) {
				InputValues = new String[] {
						EP.GetTimeStamp("dd MMM yyyy"),
						"12",
						"12",
						EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"), -5,
								Calendar.YEAR), "5", "2" };
			} else {
				InputValues = new String[] {
						EP.GetTimeStamp("dd MMM yyyy"),
						"0",
						"0",
						EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"), -15,
								Calendar.YEAR), "13", "13" };
			}
			EP.Btn_Click("#General_First_BN");
			if (Gender.compareToIgnoreCase("Female") == 0) {

				EP.SelectDate(
						"#BaselineRecord_AssessmentDateTannerFemaleES_IM",
						InputValues[0]);
				EP.DrpDwn_SelectIndex(
						"#BaselineRecord_PubertalBreastStageTannerFemaleES_DD",
						1);
				int RandomMenarche = 0;
				if (!IsValid)
					RandomMenarche = 2;
				else
					RandomMenarche = EP.RandomValue(1, 3);

				EP.Rbtn_SetOption(
						EP.ORXPath("#BaselineRecord_TannerMenarcheOptions_RB")
								+ "[" + RandomMenarche + "]", "ON");
				if (RandomMenarche == 2) {
					EP.Edit_SetValue(
							"#BaselineRecord_AddTannerMenarcheYearFR_EB",
							InputValues[4]);
					EP.Edit_SetValue(
							"#BaselineRecord_AddTannerMenarcheMonthFR_EB",
							InputValues[5]);
				}
				EP.Rbtn_SetOption(
						"#BaselineRecord_PatientEnteredPubertyYesFR_RB", "ON");
				EP.SelectDate(
						"#BaselineRecord_TannerAddDateEnteredPubertyAT_IM",
						InputValues[3]);
				EP.HideCursor();
				EP.Wait(2);
			} else {
				EP.SelectDate("#BaselineRecord_TannerAddAssessmentDateES_IM",
						InputValues[0]);
				EP.DrpDwn_SelectIndex(
						"#BaselineRecord_TannerAddPubertalAssessmentES_DD", 1);
				EP.Edit_SetValue(
						"#BaselineRecord_TannerAddTestisVolumeLeftIT_EB",
						InputValues[1]);
				EP.Edit_SetValue(
						"#BaselineRecord_TannerAddTestisVolumeRightIT_EB",
						InputValues[2]);
				EP.Rbtn_SetOption(
						"#BaselineRecord_PatientEnteredPubertyYesFR_RB", "ON");
				EP.SelectDate(
						"#BaselineRecord_TannerAddDateEnteredPubertyAT_IM",
						InputValues[3]);
				EP.HideCursor();
				EP.Wait(2);
			}
			EP.Btn_Click("#General_AddRecordSave_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN");
			}
			break;

		case SPAIN:
			if (IsValid) {
				InputValues = new String[] {
						EP.GetTimeStamp("dd MMM yyyy"),
						"12",
						"12",
						EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"), -5,
								Calendar.YEAR), "5", "2" };
			} else {
				InputValues = new String[] {
						EP.GetTimeStamp("dd MMM yyyy"),
						"0",
						"0",
						EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"), -15,
								Calendar.YEAR), "13", "13" };
			}
			EP.Btn_Click("#General_First_BN");
			EP.Wait(2);
			EP.SelectDate("#BaselineRecord_AssessmentDateTannerFemaleES_IM",
					InputValues[0]);
			EP.HideCursor();
			EP.Wait(2);
			if (Gender.compareToIgnoreCase("Female") == 0) {
				int RandomMenarche;
				if (!IsValid)
					RandomMenarche = 1;
				else
					RandomMenarche = EP.RandomValue(1, 2);
				EP.DrpDwn_SelectIndex(
						"#BaselineRecord_PubertalBreastStageTannerFemaleES_DD",
						1);
				EP.DrpDwn_SelectIndex(
						"#BaselineRecord_AddTannerMenarcheFemaieES_DD",
						RandomMenarche);
				if (RandomMenarche == 1) {
					EP.Edit_SetValue(
							"#BaselineRecord_AddTannerMenarcheYearES_EB",
							InputValues[4]);
					EP.Edit_SetValue(
							"#BaselineRecord_AddTannerMenarcheMonthES_EB",
							InputValues[5]);
				}
			} else {
				EP.DrpDwn_SelectIndex(
						"#BaselineRecord_TannerAddPubertalAssessmentES_DD", 1);
				EP.Edit_SetValue(
						"#BaselineRecord_TannerAddTestisVolumeLeftES_EB",
						InputValues[1]);
				EP.Edit_SetValue(
						"#BaselineRecord_TannerAddTestisVolumeRightES_EB",
						InputValues[2]);
			}
			EP.Rbtn_SetOption("#BaselineRecord_PatientEnteredPubertyYesES_RB",
					"ON");
			EP.SelectDate("#BaselineRecord_TannerAddDateEnteredPubertyES_IM",
					InputValues[3]);
			EP.HideCursor();
			EP.Wait(2);
			EP.Btn_Click("#General_AddRecordSave_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN");
			}
			break;

		case KOREA:
			if (IsValid) {
				InputValues = new String[] {
						EP.GetTimeStamp("dd MMM yyyy"),
						"12",
						"12",
						EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"), -5,
								Calendar.YEAR), "5", "2" };
			} else {
				InputValues = new String[] {
						EP.GetTimeStamp("dd MMM yyyy"),
						"0",
						"0",
						EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"), -15,
								Calendar.YEAR), "13", "13" };
			}
			EP.Btn_Click("#General_First_BN");
			if (Gender.compareToIgnoreCase("Female") == 0) {

				EP.SelectDate(
						"#BaselineRecord_AssessmentDateTannerFemaleES_IM",
						InputValues[0]);
				EP.HideCursor();
				EP.Wait(2);
				EP.DrpDwn_SelectIndex(
						"#BaselineRecord_PubertalBreastStageTannerFemaleES_DD",
						1);
				EP.Rbtn_SetOption("#BaselineRecord_TannerMenarcheYesKR_RB",
						"ON");
				EP.Edit_SetValue("#BaselineRecord_AddTannerMenarcheYearES_EB",
						InputValues[4]);
				EP.Edit_SetValue("#BaselineRecord_AddTannerMenarcheMonthES_EB",
						InputValues[5]);
				EP.Rbtn_SetOption(
						"#BaselineRecord_PatientEnteredPubertyYesES_RB", "ON");
				EP.SelectDate(
						"#BaselineRecord_TannerAddDateEnteredPubertyES_IM",
						InputValues[3]);
			} else {
				EP.SelectDate("#BaselineRecord_TannerAddAssessmentDateES_IM",
						InputValues[0]);
				EP.HideCursor();
				EP.Wait(2);
				EP.DrpDwn_SelectIndex(
						"#BaselineRecord_TannerAddPubertalAssessmentES_DD", 1);
				EP.Rbtn_SetOption("#BaselineRecord_AddTannerYesES_RB", "ON");
				EP.SelectDate(
						"#BaselineRecord_TannerAddDateEnteredPubertyES_IM",
						InputValues[3]);
				EP.Edit_SetValue(
						"#BaselineRecord_TannerAddTestisVolumeLeftES_EB",
						InputValues[1]);
				EP.Edit_SetValue(
						"#BaselineRecord_TannerAddTestisVolumeRightES_EB",
						InputValues[2]);
			}
			EP.Btn_Click("#General_AddRecordSave_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN");
			}
			break;

		case AUSTRALIA:
			if (IsValid) {
				InputValues = new String[] {
						EP.GetTimeStamp("dd MMM yyyy"),
						"12",
						"12",
						EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"), -5,
								Calendar.YEAR), "5", "2" };
			} else {
				InputValues = new String[] {
						EP.GetTimeStamp("dd MMM yyyy"),
						"0",
						"0",
						EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"), -15,
								Calendar.YEAR), "13", "13" };
			}
			EP.Btn_Click("#General_First_BN");
			if (Gender.compareToIgnoreCase("Female") == 0) {

				EP.SelectDate(
						"#BaselineRecord_AssessmentDateTannerFemaleES_IM",
						InputValues[0]);
				EP.DrpDwn_SelectIndex(
						"#BaselineRecord_PubertalBreastStageTannerFemaleES_DD",
						1);
				EP.Rbtn_SetOption("#BaselineRecord_TannerMenarcheYesKR_RB",
						"ON");
				EP.Edit_SetValue("#BaselineRecord_AddTannerMenarcheYearAU_EB",
						InputValues[4]);
				EP.Edit_SetValue("#BaselineRecord_AddTannerMenarcheMonthES_EB",
						InputValues[5]);
				EP.Rbtn_SetOption(
						"#BaselineRecord_PatientEnteredPubertyYesAU_RB", "ON");
				EP.SelectDate(
						"#BaselineRecord_TannerAddDateEnteredPubertyES_IM",
						InputValues[3]);
				EP.HideCursor();
				EP.Wait(2);
			} else {
				EP.SelectDate("#BaselineRecord_TannerAddAssessmentDateES_IM",
						InputValues[0]);
				EP.DrpDwn_SelectIndex(
						"#BaselineRecord_TannerAddPubertalAssessmentES_DD", 1);
				EP.Rbtn_SetOption("#BaselineRecord_AddTannerYesAU_RB", "ON");
				EP.SelectDate(
						"#BaselineRecord_TannerAddDateEnteredPubertyES_IM",
						InputValues[3]);
				EP.HideCursor();
				EP.Wait(2);
				EP.Edit_SetValue(
						"#BaselineRecord_TannerAddTestisVolumeLeftES_EB",
						InputValues[1]);
				EP.Edit_SetValue(
						"#BaselineRecord_TannerAddTestisVolumeRightES_EB",
						InputValues[2]);
			}
			EP.Btn_Click("#General_AddRecordSave_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN");
			}
			break;

		case ICELAND:
			if (IsValid) {
				InputValues = new String[] {
						EP.GetTimeStamp("dd MMM yyyy"),
						"12",
						"12",
						EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"), -5,
								Calendar.YEAR), "5", "2" };
			} else {
				InputValues = new String[] {
						EP.GetTimeStamp("dd MMM yyyy"),
						"0",
						"0",
						EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"), -15,
								Calendar.YEAR), "13", "13" };
			}
			EP.Btn_Click("#General_First_BN");
			if (Gender.compareToIgnoreCase("Female") == 0) {

				EP.SelectDate(
						"#BaselineRecord_AssessmentDateTannerFemaleES_IM",
						InputValues[0]);
				EP.DrpDwn_SelectIndex(
						"#BaselineRecord_PubertalBreastStageTannerFemaleIS_DD",
						1);
				int RandomMenarche;
				if (!IsValid)
					RandomMenarche = 1;
				else
					RandomMenarche = EP.RandomValue(1, 3);

				EP.Rbtn_SetOption(
						EP.ORXPath("#BaselineRecord_TannerMenarcheOptions_RB")
								+ "[" + RandomMenarche + "]", "ON");
				// EP.Rbtn_SetOption("#BaselineRecord_TannerMenarcheYesIS_RB",
				// "ON");
				if (RandomMenarche == 1) {
					EP.Edit_SetValue(
							"#BaselineRecord_AddTannerMenarcheYearES_EB",
							InputValues[4]);
					EP.Edit_SetValue(
							"#BaselineRecord_AddTannerMenarcheMonthES_EB",
							InputValues[5]);
				}
				EP.Rbtn_SetOption(
						"#BaselineRecord_PatientEnteredPubertyYesES_RB", "ON");
				EP.SelectDate(
						"#BaselineRecord_TannerAddDateEnteredPubertyES_IM",
						InputValues[3]);
			}

			else {
				EP.SelectDate("#BaselineRecord_TannerAddAssessmentDateES_IM",
						InputValues[0]);
				EP.DrpDwn_SelectIndex(
						"#BaselineRecord_TannerAddPubertalAssessmentIS_DD", 1);
				EP.Edit_SetValue(
						"#BaselineRecord_TannerAddTestisVolumeLeftES_EB",
						InputValues[1]);
				EP.Edit_SetValue(
						"#BaselineRecord_TannerAddTestisVolumeRightES_EB",
						InputValues[2]);
				EP.Rbtn_SetOption("#BaselineRecord_AddTannerYesES_RB", "ON");
				EP.SelectDate(
						"#BaselineRecord_TannerAddDateEnteredPubertyES_IM",
						InputValues[3]);

			}
			EP.Btn_Click("#General_AddRecordSave_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN");
			}
			break;

		case FINLAND:
			if (IsValid) {
				InputValues = new String[] {
						EP.GetTimeStamp("dd MMM yyyy"),
						"12",
						"12",
						EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"), -5,
								Calendar.YEAR), "5", "2" };
			} else {
				InputValues = new String[] {
						EP.GetTimeStamp("dd MMM yyyy"),
						"0",
						"0",
						EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"), -15,
								Calendar.YEAR), "13", "13" };
			}
			EP.Btn_Click("#General_First_BN");
			if (Gender.compareToIgnoreCase("Female") == 0) {

				EP.SelectDate(
						"#BaselineRecord_AssessmentDateTannerFemaleES_IM",
						InputValues[0]);
				EP.HideCursor();
				EP.Wait(2);
				EP.DrpDwn_SelectIndex(
						"#BaselineRecord_PubertalBreastStageTannerFemaleES_DD",
						1);
				int RandomMenarche;
				if (!IsValid)
					RandomMenarche = 1;
				else
					RandomMenarche = EP.RandomValue(1, 3);

				EP.Rbtn_SetOption(
						EP.ORXPath("#BaselineRecord_TannerMenarcheOptions_RB")
								+ "[" + RandomMenarche + "]", "ON");
				// EP.Rbtn_SetOption("#BaselineRecord_TannerMenarcheYesIS_RB",
				// "ON");
				if (RandomMenarche == 1) {
					EP.Edit_SetValue(
							"#BaselineRecord_AddTannerMenarcheYearFI_EB",
							InputValues[4]);
					EP.Edit_SetValue(
							"#BaselineRecord_AddTannerMenarcheMonthES_EB",
							InputValues[5]);
				}
				EP.Rbtn_SetOption("#BaselineRecord_AddTannerYesFI_RB", "ON");
				EP.SelectDate(
						"#BaselineRecord_TannerAddDateEnteredPubertyES_IM",
						InputValues[3]);
				EP.HideCursor();
				EP.Wait(2);
			} else {
				EP.SelectDate("#BaselineRecord_TannerAddAssessmentDateES_IM",
						InputValues[0]);
				EP.HideCursor();
				EP.Wait(2);
				EP.DrpDwn_SelectIndex(
						"#BaselineRecord_TannerAddPubertalAssessmentES_DD", 1);
				EP.Edit_SetValue(
						"#BaselineRecord_TannerAddTestisVolumeLeftES_EB",
						InputValues[1]);
				EP.Edit_SetValue(
						"#BaselineRecord_TannerAddTestisVolumeRightES_EB",
						InputValues[2]);
				EP.Rbtn_SetOption("#BaselineRecord_AddTannerYesFI_RB", "ON");
				EP.SelectDate(
						"#BaselineRecord_TannerAddDateEnteredPubertyES_IM",
						InputValues[3]);

			}
			EP.Btn_Click("#General_AddRecordSave_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN");
			}
			break;

		case FRANCE:
			if (IsValid) {
				InputValues = new String[] {
						EP.GetTimeStamp("dd MMM yyyy"),
						"12",
						"12",
						EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"), -5,
								Calendar.YEAR), "5", "2" };
			} else {
				InputValues = new String[] {
						EP.GetTimeStamp("dd MMM yyyy"),
						"0",
						"0",
						EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"), -15,
								Calendar.YEAR), "13", "13" };
			}
			EP.Btn_Click("#General_First_BN");
			EP.Wait(2);
			EP.SelectDate("#BaselineRecord_AssessmentDateTannerFemaleFR_IM",
					InputValues[0]);
			EP.HideCursor();
			EP.Wait(2);
			EP.DrpDwn_SelectIndex(
					"#BaselineRecord_PubertalBreastStageTannerFemaleSK_DD", 1);
			if (Gender.compareToIgnoreCase("Female") == 0) {
				int RandomMenarche;
				if (!IsValid)
					RandomMenarche = 1;
				else
					RandomMenarche = EP.RandomValue(1, 2);
				EP.Rbtn_SetOption(
						EP.ORXPath("#BaselineRecord_TannerMenarcheOptions_RB")
								+ "[" + RandomMenarche + "]", "ON");
				if (RandomMenarche == 1) {
					EP.Edit_SetValue(
							"#BaselineRecord_AddTannerMenarcheYearFR_EB",
							InputValues[4]);
					EP.Edit_SetValue(
							"#BaselineRecord_AddTannerMenarcheMonthFR_EB",
							InputValues[5]);
				}
			} else {
				EP.Edit_SetValue(
						"#BaselineRecord_TannerAddTestisVolumeLeftFR_EB",
						InputValues[1]);
				EP.Edit_SetValue(
						"#BaselineRecord_TannerAddTestisVolumeRightFR_EB",
						InputValues[2]);
			}
			EP.Rbtn_SetOption("#BaselineRecord_PatientEnteredPubertyYesFR_RB",
					"ON");
			EP.SelectDate("#BaselineRecord_TannerAddDateEnteredPubertyFR_IM",
					InputValues[3]);
			EP.HideCursor();
			EP.Wait(2);
			EP.Btn_Click("#General_AddRecordSave_BN");

			if (!IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN");
			}

			break;
		case SLOVAKIA:
			if (IsValid) {
				InputValues = new String[] {
						EP.GetTimeStamp("dd MMM yyyy"),
						"12",
						"12",
						EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"), -5,
								Calendar.YEAR), "5", "2" };
			} else {
				InputValues = new String[] {
						EP.GetTimeStamp("dd MMM yyyy"),
						"0",
						"0",
						EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"), -15,
								Calendar.YEAR), "13", "13" };
			}
			EP.Btn_Click("#General_First_BN");
			if (Gender.compareToIgnoreCase("Female") == 0) {
				EP.SelectDate(
						"#BaselineRecord_AssessmentDateTannerFemaleFR_IM",
						InputValues[0]);
				EP.HideCursor();
				EP.Wait(2);
				EP.DrpDwn_SelectIndex(
						"#BaselineRecord_PubertalBreastStageTannerFemaleSK_DD",
						1);
				int RandomMenarche;
				if (!IsValid)
					RandomMenarche = 1;
				else
					RandomMenarche = EP.RandomValue(1, 2);
				EP.Rbtn_SetOption(
						EP.ORXPath("#BaselineRecord_TannerMenarcheOptions_RB")
								+ "[" + RandomMenarche + "]", "ON");
				if (RandomMenarche == 1) {
					EP.Edit_SetValue(
							"#BaselineRecord_AddTannerMenarcheYearFR_EB",
							InputValues[4]);
					EP.Edit_SetValue(
							"#BaselineRecord_AddTannerMenarcheMonthFR_EB",
							InputValues[5]);
				}
				EP.Rbtn_SetOption(
						"#BaselineRecord_PatientEnteredPubertyYesFR_RB", "ON");
				EP.SelectDate(
						"#BaselineRecord_TannerAddDateEnteredPubertyFR_IM",
						InputValues[3]);
				EP.HideCursor();
				EP.Wait(2);
			} else {
				EP.SelectDate("#BaselineRecord_TannerAddAssessmentDateFR_IM",
						InputValues[0]);
				EP.HideCursor();
				EP.Wait(2);
				EP.DrpDwn_SelectIndex(
						"#BaselineRecord_TannerAddPubertalAssessmentFR_DD", 1);
				EP.Edit_SetValue(
						"#BaselineRecord_TannerAddTestisVolumeLeftFR_EB",
						InputValues[1]);
				EP.Edit_SetValue(
						"#BaselineRecord_TannerAddTestisVolumeRightFR_EB",
						InputValues[2]);
				EP.Rbtn_SetOption("#BaselineRecord_AddTannerYesES_RB", "ON");
				EP.SelectDate(
						"#BaselineRecord_TannerAddDateEnteredPubertyFR_IM",
						InputValues[3]);
				EP.HideCursor();
				EP.Wait(2);
			}
			EP.Btn_Click("#General_AddRecordSave_BN");

			if (!IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN");
			}
			break;
		default:
		}
		VerifySuccessMessage(EP.LangData("TannerStage"));
	}

	public void BoneAgeBasleineAdd(boolean IsValid, String TabIndex,
			String PatientDOB) {
		// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*BONE AGE
		// (BASELINE)*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-**-*-*-*-*-*-*-*-*-*-*-*-*-*
		EP.ShowTab(TabIndex);
		String[] InputValues;
		// Delete Record
		if (EP.ObjectExists("#General_Edit_IM")) {
			DeleteRecord("#General_Delete_IM", false);
		}

		switch (switchCountryName) {

		case SWEDEN:
			if (IsValid) {
				InputValues = new String[] {
						EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"), -4,
								Calendar.MONTH), "5,3" };
			} else {
				InputValues = new String[] {
						EP.Date_AddDays(PatientDOB, -2, Calendar.YEAR),
						MaxValue };
			}
			EP.Btn_Click("#General_First_BN");
			EP.DrpDwn_SelectIndex("#BaselineRecord_BoneAgeAssesmentDateCA_DD",
					1);
			EP.SelectDate("#AddVisit_BoneAssessmentDate_IM", InputValues[0]);
			EP.HideCursor();
			EP.Wait(2);
			EP.DrpDwn_SelectIndex("#BaselineRecord_BoneAgeAddMethodIT_DD", 2);
			EP.Edit_SetValue("#BaselineRecord_BoneAgeAddYearsMonthsIT_EB",
					InputValues[1]);
			EP.Btn_Click("#General_AddRecordSave_BN");
			if (!IsValid)
				EP.Btn_Click("#General_AddRecordSave_BN");
			break;

		case UNITEDKINGDOM:
			if (IsValid) {
				InputValues = new String[] {
						EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"), -4,
								Calendar.MONTH), "5,3" };
			} else {
				InputValues = new String[] {
						EP.Date_AddDays(PatientDOB, -2, Calendar.YEAR),
						MaxValue };
			}
			EP.Btn_Click("#General_First_BN");
			EP.SelectDate("#BaselineRecord_BoneAgeAddAssessmentDateGB_IM",
					InputValues[0]);
			EP.HideCursor();
			EP.Wait(2);
			EP.DrpDwn_SelectIndex("#BaselineRecord_BoneAgeAddMethodGB_DD", 2);
			EP.Edit_SetValue("#BaselineRecord_BoneAgeAddYearsMonthsGB_EB",
					InputValues[1]);
			EP.Btn_Click("#General_AddRecordSave_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN");
			}
			break;

		case ITALY:
			if (IsValid) {
				InputValues = new String[] {
						EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"), -4,
								Calendar.MONTH), "5,3" };
			} else {
				InputValues = new String[] {
						EP.Date_AddDays(PatientDOB, -2, Calendar.YEAR),
						MaxValue };
			}
			EP.Btn_Click("#General_First_BN");
			EP.SelectDate("#AddVisit_BoneAssessmentDate_IM", InputValues[0]);
			EP.HideCursor();
			EP.Wait(2);
			EP.DrpDwn_SelectIndex("#BaselineRecord_BoneAgeAddMethodIT_DD", 2);
			EP.Edit_SetValue("#BaselineRecord_BoneAgeAddYearsMonthsIT_EB",
					InputValues[1]);
			EP.Btn_Click("#General_AddRecordSave_BN");
			if (!IsValid)
				EP.Btn_Click("#General_AddRecordSave_BN");
			break;

		case GREECE:
			if (IsValid) {
				InputValues = new String[] {
						EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"), -4,
								Calendar.MONTH), "5,3" };
			} else {
				InputValues = new String[] {
						EP.Date_AddDays(PatientDOB, -2, Calendar.YEAR),
						MaxValue };
			}
			EP.Btn_Click("#General_First_BN");
			EP.SelectDate("#AddVisit_BoneAssessmentDate_IM", InputValues[0]);
			EP.HideCursor();
			EP.Wait(2);
			EP.DrpDwn_SelectIndex("#BaselineRecord_BoneAgeAddMethodGR_DD", 2);
			EP.Edit_SetValue("#BaselineRecord_BoneAgeAddYearsMonthsIT_EB",
					InputValues[1]);
			EP.Btn_Click("#General_AddRecordSave_BN");
			if (!IsValid)
				EP.Btn_Click("#General_AddRecordSave_BN");
			break;

		case SLOVAKIA:
			if (IsValid) {
				InputValues = new String[] {
						EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"), -4,
								Calendar.MONTH), "5,3" };
			} else {
				InputValues = new String[] {
						EP.Date_AddDays(PatientDOB, -2, Calendar.YEAR),
						MaxValue };
			}

			EP.Btn_Click("#General_First_BN");
			EP.SelectDate("#AddVisit_BoneAssessmentDate_IM", InputValues[0]);
			EP.HideCursor();
			EP.Wait(2);
			EP.DrpDwn_SelectIndex("#BaselineRecord_BoneAgeAddMethodSK_DD", 2);
			EP.Edit_SetValue("#BaselineRecord_BoneAgeAddYearsMonthsIT_EB",
					InputValues[1]);
			EP.Btn_Click("#General_AddRecordSave_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN");
			}
			break;

		case CANADA:
			if (IsValid) {
				InputValues = new String[] {
						EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"), -4,
								Calendar.MONTH), "5,3" };
			} else {
				InputValues = new String[] {
						EP.Date_AddDays(PatientDOB, -2, Calendar.YEAR), "5, 13" };
			}
			EP.Btn_Click("#General_First_BN");
			EP.Wait(2);
			EP.DrpDwn_SelectIndex("#BaselineRecord_BoneAgeAssesmentDateCA_DD",
					1);
			EP.SelectDate("#AddVisit_BoneAssessmentDate_IM", InputValues[0]);
			EP.HideCursor();
			EP.Wait(2);
			EP.DrpDwn_SelectIndex("#BaselineRecord_BoneAgeAddMethodCA_DD", 2);
			EP.Edit_SetValue("#BaselineRecord_BoneAgeAddYearsMonthsIT_EB",
					InputValues[1]);
			EP.Btn_Click("#General_AddRecordSave_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN");
			}
			break;

		case ARGENTINA:
		case COLOMBIA:
		case TAIWAN:
		case INDONESIA:
			if (IsValid) {
				InputValues = new String[] {
						EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"), -4,
								Calendar.MONTH), "5,3" };
			} else {
				InputValues = new String[] {
						EP.Date_AddDays(PatientDOB, -2, Calendar.YEAR),
						MaxValue };
			}
			EP.Btn_Click("#General_First_BN");
			EP.Wait(2);
			EP.DrpDwn_SelectIndex("#BaselineRecord_BoneAgeAddMethodSG_DD", 1);
			EP.SelectDate("#BaselineRecord_BoneAgeAddAssessmentDateAT_IM",
					InputValues[0]);
			EP.HideCursor();
			EP.Wait(2);
			EP.DrpDwn_SelectIndex("#BaselineRecord_BoneAgeAddMethodSG_DD", 2);
			EP.Edit_SetValue("#BaselineRecord_BoneAgeAddYearsMonthsIT_EB",
					InputValues[1]);
			EP.Btn_Click("#General_AddRecordSave_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN");
				EP.Edit_SetValue("#BaselineRecord_BoneAgeAddYearsMonthsIT_EB",
						"");
			}
			break;

		case CZECHREPUBLIC:
			if (IsValid) {
				InputValues = new String[] {
						EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"), -4,
								Calendar.MONTH), "5,3" };
			} else {
				InputValues = new String[] {
						EP.Date_AddDays(PatientDOB, -2, Calendar.YEAR),
						MaxValue };
			}
			EP.Btn_Click("#General_First_BN");
			EP.DrpDwn_SelectIndex("#BaselineRecord_BoneAgeAddMethodCZ_DD", 2);
			EP.SelectDate("#AddVisit_BoneAssessmentDate_IM", InputValues[0]);
			EP.HideCursor();
			EP.Wait(2);
			EP.Edit_SetValue("#BaselineRecord_BoneAgeAddYearsMonthsIT_EB",
					InputValues[1]);
			EP.Btn_Click("#General_AddRecordSave_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN");
			}
			break;

		case HUNGARY:
			if (IsValid) {
				InputValues = new String[] {
						EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"), -4,
								Calendar.MONTH), "5,3" };
			} else {
				InputValues = new String[] {
						EP.Date_AddDays(PatientDOB, -2, Calendar.YEAR),
						MaxValue };
			}
			EP.Btn_Click("#General_First_BN");
			EP.SelectDate("#AddVisit_BoneAssessmentDate_IM", InputValues[0]);
			EP.HideCursor();
			EP.Wait(2);
			EP.DrpDwn_SelectIndex("#BaselineRecord_BoneAgeAddMethodIT_DD", 2);
			EP.Edit_SetValue("#BaselineRecord_BoneAgeAddYearsMonthsIT_EB",
					InputValues[1]);
			EP.Btn_Click("#General_AddRecordSave_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN");
			}
			break;

		case NORWAY:
			if (IsValid) {
				InputValues = new String[] {
						EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"), -4,
								Calendar.MONTH), "5,3" };
			} else {
				InputValues = new String[] {
						EP.Date_AddDays(PatientDOB, -2, Calendar.YEAR),
						MaxValue };
			}
			EP.Btn_Click("#General_First_BN");
			EP.SelectDate("#BaselineRecord_BoneAgeAddAssessmentDateES_IM",
					InputValues[0]);
			EP.HideCursor();
			EP.Wait(2);
			EP.DrpDwn_SelectIndex("#BaselineRecord_BoneAgeAddMethodES_DD", 2);
			EP.Edit_SetValue("#BaselineRecord_BoneAgeYearsMonthsES_EB",
					InputValues[1]);
			EP.Btn_Click("#General_AddRecordSave_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN");
			}
			break;

		case AUSTRIA:
			if (IsValid) {
				InputValues = new String[] {
						EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"), -4,
								Calendar.MONTH), "5,3" };
			} else {
				InputValues = new String[] {
						EP.Date_AddDays(PatientDOB, -2, Calendar.YEAR),
						MaxValue };
			}

			EP.Btn_Click("#General_First_BN");// Click Add Button
			EP.SelectDate("#AddVisit_BoneAssessmentDate_IM", InputValues[0]);
			EP.HideCursor();
			EP.Wait(2);
			EP.DrpDwn_SelectIndex("#BaselineRecord_BoneAgeAddMethodAT_DD", 2);
			EP.Edit_SetValue("#BaselineRecord_BoneAgeAddYearsMonthsAT_EB",
					InputValues[1]);
			EP.Btn_Click("#General_AddRecordSave_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN");
			}

			break;

		case SINGAPORE:
		case UNITEDARABEMIRATES:
		case SAUDIARABIA:
		case MEXICO:
			if (IsValid) {
				InputValues = new String[] {
						EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"), -4,
								Calendar.MONTH), "5,3" };
			} else {
				InputValues = new String[] {
						EP.Date_AddDays(PatientDOB, -2, Calendar.YEAR),
						MaxValue };
			}

			EP.Btn_Click("#General_First_BN");// Click Add Button
			EP.SelectDate("#AddVisit_BoneAssessmentDate_IM", InputValues[0]);
			EP.HideCursor();
			EP.Wait(2);
			EP.DrpDwn_SelectIndex("#BaselineRecord_BoneAgeAddMethodAT_DD", 2);
			EP.Edit_SetValue("#BaselineRecord_BoneAgeAddYearsMonthsAT_EB",
					EP.Converter(InputValues[1]));
			EP.Btn_Click("#General_AddRecordSave_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN");
			}

			break;

		case SPAIN:
			if (IsValid) {
				InputValues = new String[] {
						EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"), -4,
								Calendar.MONTH), "5,3" };
			} else {
				InputValues = new String[] {
						EP.Date_AddDays(PatientDOB, -2, Calendar.YEAR),
						MaxValue };
			}

			EP.Btn_Click("#General_First_BN");
			EP.SelectDate("#BaselineRecord_BoneAgeAddAssessmentDateES_IM",
					InputValues[0]);
			EP.HideCursor();
			EP.Wait(2);
			EP.DrpDwn_SelectIndex("#BaselineRecord_BoneAgeAddMethodES_DD", 2);
			EP.Edit_SetValue("#BaselineRecord_BoneAgeYearsMonthsES_EB",
					InputValues[1]);
			EP.Btn_Click("#General_AddRecordSave_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN");
			}
			break;

		case KOREA:
			if (IsValid) {
				InputValues = new String[] {
						EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"), -4,
								Calendar.MONTH), "5,3" };
			} else {
				InputValues = new String[] {
						EP.Date_AddDays(PatientDOB, -2, Calendar.YEAR),
						MaxValue };
			}

			EP.Btn_Click("#General_First_BN");
			EP.SelectDate("#BaselineRecord_BoneAgeAddAssessmentDateES_IM",
					InputValues[0]);
			EP.HideCursor();
			EP.Wait(2);
			EP.DrpDwn_SelectIndex("#BaselineRecord_BoneAgeAddMethodES_DD", 2);
			EP.Edit_SetValue("#BaselineRecord_BoneAgeYearsMonthsES_EB",
					InputValues[1]);
			EP.Btn_Click("#General_AddRecordSave_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN");
			}
			break;

		case AUSTRALIA:
			if (IsValid) {
				InputValues = new String[] {
						EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"), -4,
								Calendar.MONTH), "5,3" };
			} else {
				InputValues = new String[] {
						EP.Date_AddDays(PatientDOB, -2, Calendar.YEAR),
						MaxValue };
			}

			EP.Btn_Click("#General_First_BN");
			EP.SelectDate("#BaselineRecord_BoneAgeAddAssessmentDateES_IM",
					InputValues[0]);
			EP.HideCursor();
			EP.Wait(2);
			EP.DrpDwn_SelectIndex("#BaselineRecord_BoneAgeAddMethodES_DD", 2);
			EP.Edit_SetValue("#BaselineRecord_BoneAgeYearsMonthsES_EB",
					InputValues[1]);
			EP.Btn_Click("#General_AddRecordSave_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN");
			}
			break;

		case ICELAND:

			if (IsValid) {
				InputValues = new String[] {
						EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"), -4,
								Calendar.MONTH), "5,3" };
			} else {
				InputValues = new String[] {
						EP.Date_AddDays(PatientDOB, -2, Calendar.YEAR),
						MaxValue };
			}

			EP.Btn_Click("#General_First_BN");
			EP.SelectDate("#BaselineRecord_BoneAgeAddAssessmentDateES_IM",
					InputValues[0]);
			EP.DrpDwn_SelectIndex("#BaselineRecord_BoneAgeAddMethodES_DD", 2);
			EP.Edit_SetValue("#BaselineRecord_BoneAgeYearsMonthsES_EB",
					InputValues[1]);
			EP.Btn_Click("#General_AddRecordSave_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN");
			}
			break;

		case FINLAND:
			if (IsValid) {
				InputValues = new String[] {
						EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"), -4,
								Calendar.MONTH), "5,3" };
			} else {
				InputValues = new String[] {
						EP.Date_AddDays(PatientDOB, -2, Calendar.YEAR),
						MaxValue };
			}
			EP.Btn_Click("#General_First_BN");
			EP.SelectDate("#BaselineRecord_BoneAgeAddAssessmentDateES_IM",
					InputValues[0]);
			EP.HideCursor();
			EP.Wait(2);
			EP.DrpDwn_SelectIndex("#BaselineRecord_BoneAgeAddMethodES_DD", 2);
			EP.Edit_SetValue("#BaselineRecord_BoneAgeYearsMonthsES_EB",
					InputValues[1]);
			EP.Btn_Click("#General_AddRecordSave_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN");
			}
			break;

		case FRANCE:
			if (IsValid) {
				InputValues = new String[] {
						EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"), -4,
								Calendar.MONTH), "5,3" };
			} else {
				InputValues = new String[] {
						EP.Date_AddDays(PatientDOB, -2, Calendar.YEAR),
						MaxValue };
			}
			EP.Btn_Click("#General_First_BN");
			EP.Wait(1);
			EP.SelectDate("#BaselineRecord_BoneAgeAddAssessmentDateFR_IM",
					InputValues[0]);
			EP.HideCursor();
			EP.Wait(2);
			EP.DrpDwn_SelectIndex("#BaselineRecord_BoneAgeAddMethodFR_DD", 2);
			EP.Wait(2);
			EP.Edit_SetValue("#BaselineRecord_BoneAgeYearsMonthsES_EB",
					InputValues[1]);
			EP.Btn_Click("#General_AddRecordSave_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN");
			}
			break;
		default:
		}
		VerifySuccessMessage(EP.LangData("BoneAgeCaps"));
	}

	public void IGF1BaselineAdd(boolean IsValid, String TabIndex,
			String PatientDOB) {
		// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*IGF-1
		// (BASELINE)*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
		EP.ShowTab(TabIndex);
		String[] InputValues;

		switch (switchCountryName) {
		case UNITEDKINGDOM:
			if (IsValid) {
				InputValues = new String[] {
						EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"), -5,
								Calendar.DATE), "20", "10", "50" };
			} else {
				InputValues = new String[] {
						EP.Date_AddDays(PatientDOB, -2, Calendar.YEAR),
						MaxValue, MaxValue, MaxValue };
			}
			EP.SelectDate("#BaselineRecord_IGF1StatusDateBeforeGHGB_IM",
					InputValues[0]);
			EP.HideCursor();
			EP.Wait(2);
			EP.DrpDwn_SelectIndex("#BaselineRecord_IGF1StatusBeforeGHGB_DD",
					EP.RandomValue(1, 3));
			EP.DrpDwn_SelectIndex("#BaselineRecord_IGF1StatusUnitGB_DD",
					EP.RandomValue(1, 5));
			EP.Edit_SetValue("#BaselineRecord_IGF1LevelGB_EB", InputValues[1]);
			EP.Edit_SetValue(
					"#BaselineRecord_IGF1LaboratoryValueRangeMinGB_EB",
					InputValues[2]);
			EP.Edit_SetValue(
					"#BaselineRecord_IGF1LaboratoryValueRangeMaxGB_EB",
					InputValues[3]);
			EP.Btn_Click("#General_First_BN");
			break;

		case CZECHREPUBLIC:
			if (IsValid) {
				InputValues = new String[] {
						EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"), -5,
								Calendar.DATE), "20", "10", "50" };
			} else {
				InputValues = new String[] {
						EP.Date_AddDays(PatientDOB, -2, Calendar.YEAR),
						MaxValue, MaxValue, MaxValue };
			}
			EP.SelectDate("#BaselineRecord_IGF1StatusDateBeforeGHCZ_IM",
					InputValues[0]);
			EP.HideCursor();
			EP.Wait(2);
			EP.DrpDwn_SelectIndex("#BaselineRecord_IGF1StatusBeforeGHCZ_DD",
					EP.RandomValue(1, 4));
			EP.DrpDwn_SelectIndex("#BaselineRecord_IGF1StatusUnitGB_DD",
					EP.RandomValue(1, 5));
			EP.Edit_SetValue("#BaselineRecord_IGF1LevelGB_EB", InputValues[1]);
			EP.Edit_SetValue(
					"#BaselineRecord_IGF1LaboratoryValueRangeMinGB_EB",
					InputValues[2]);
			EP.Edit_SetValue(
					"#BaselineRecord_IGF1LaboratoryValueRangeMaxGB_EB",
					InputValues[3]);
			EP.Btn_Click("#General_First_BN");
			break;

		case HUNGARY:
			if (IsValid) {
				InputValues = new String[] {
						EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"), -5,
								Calendar.DATE), "20", "10", "50" };
			} else {
				InputValues = new String[] {
						EP.Date_AddDays(PatientDOB, -2, Calendar.YEAR),
						MaxValue, MaxValue, MaxValue };
			}
			EP.SelectDate("#BaselineRecord_IGF1StatusDateBeforeGHCZ_IM",
					InputValues[0]);
			EP.HideCursor();
			EP.Wait(1);
			EP.DrpDwn_SelectIndex("#BaselineRecord_IGF1StatusBeforeGHGB_DD",
					EP.RandomValue(1, 4));
			EP.DrpDwn_SelectIndex("#BaselineRecord_IGF1StatusUnitGB_DD",
					EP.RandomValue(1, 5));
			EP.Edit_SetValue("#BaselineRecord_IGF1LevelGB_EB", InputValues[1]);
			EP.Edit_SetValue(
					"#BaselineRecord_IGF1LaboratoryValueRangeMinGB_EB",
					InputValues[2]);
			EP.Edit_SetValue(
					"#BaselineRecord_IGF1LaboratoryValueRangeMaxGB_EB",
					InputValues[3]);
			EP.Btn_Click("#General_First_BN");
			break;

		case NORWAY:
			if (IsValid) {
				InputValues = new String[] {
						EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"), -5,
								Calendar.DATE), "20", "10", "50" };
			} else {
				InputValues = new String[] {
						EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"), -9,
								Calendar.DATE), MaxValue, MaxValue, MaxValue };
			}

			EP.SelectDate("#BaselineRecord_IGF1StatusDateBeforeGHES_IM",
					InputValues[0]);
			EP.HideCursor();
			EP.Wait(2);
			EP.DrpDwn_SelectIndex("#BaselineRecord_IGF1StatusBeforeGHNO_DD",
					EP.RandomValue(1, 4));
			EP.Edit_SetValue("#BaselineRecord_IGF1LevelAT_EB", InputValues[1]);
			EP.DrpDwn_SelectIndex("#BaselineRecord_IGF1StatusUnitNO_DD",
					EP.RandomValue(1, 5));
			EP.Edit_SetValue(
					"#BaselineRecord_IGF1LaboratoryValueRangeMinES_EB",
					InputValues[2]);
			EP.Edit_SetValue(
					"#BaselineRecord_IGF1LaboratoryValueRangeMaxES_EB",
					InputValues[3]);
			EP.Btn_Click("#General_First_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_First_BN");
			}

			break;
		case CANADA:
			if (IsValid) {
				InputValues = new String[] {
						EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"), -5,
								Calendar.DATE), "20", "10", "50" };
			} else {
				InputValues = new String[] {
						EP.Date_AddDays(PatientDOB, -2, Calendar.YEAR),
						MaxValue, MaxValue, MaxValue };
			}
			EP.DrpDwn_SelectIndex(
					"#BaselineRecord_IGF1StatusDateBeforeGHCA_DD", 1);
			EP.SelectDate("#BaselineRecord_IGF1StatusDateBeforeGHIT_IM",
					InputValues[0]);
			EP.HideCursor();
			EP.Wait(2);
			EP.DrpDwn_SelectIndex("#BaselineRecord_IGF1StatusBeforeGHCA_DD",
					EP.RandomValue(1, 3));
			EP.DrpDwn_SelectIndex("#BaselineRecord_IGF1StatusUnitIT_DD",
					EP.RandomValue(1, 5));
			EP.Edit_SetValue("#AddVisit_IGF1Level_EB", InputValues[1]);
			EP.Edit_SetValue("#AddVisit_IGF1LaboratoryValueRangeMin_EB",
					InputValues[2]);
			EP.Edit_SetValue("#AddVisit_IGF1LaboratoryValueRangeMax_EB",
					InputValues[3]);
			EP.Btn_Click("#General_First_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_First_BN");
			}
			break;

		case ITALY:
			if (IsValid) {
				InputValues = new String[] {
						EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"), -5,
								Calendar.DATE), "20", "10", "50" };
			} else {
				InputValues = new String[] {
						EP.Date_AddDays(PatientDOB, -2, Calendar.YEAR),
						MaxValue, MaxValue, MaxValue };
			}

			// Submit valid values for all fields
			EP.SelectDate("#BaselineRecord_IGF1StatusDateBeforeGHIT_IM",
					InputValues[0]);
			EP.HideCursor();
			EP.Wait(2);
			EP.DrpDwn_SelectIndex("#BaselineRecord_IGF1StatusBeforeGHIT_DD",
					EP.RandomValue(1, 3));
			EP.Edit_SetValue("#AddVisit_IGF1Level_EB", InputValues[1]);
			EP.DrpDwn_SelectIndex("#BaselineRecord_IGF1StatusUnitIT_DD",
					EP.RandomValue(1, 5));
			EP.Edit_SetValue(
					"#BaselineRecord_IGF1LaboratoryValueRangeMinIT_EB",
					InputValues[2]);
			EP.Edit_SetValue(
					"#BaselineRecord_IGF1LaboratoryValueRangeMaxIT_EB",
					InputValues[3]);
			EP.Btn_Click("#General_First_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_First_BN");
			}
			break;

		case ARGENTINA:
		case COLOMBIA:
		case TAIWAN:
		case INDONESIA:
			if (IsValid) {
				InputValues = new String[] {
						EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"), -5,
								Calendar.DATE), "20", "10", "50" };
			} else {
				InputValues = new String[] {
						EP.Date_AddDays(PatientDOB, -2, Calendar.YEAR),
						MaxValue, MaxValue, MaxValue };
			}

			// Submit valid values for all fields
			EP.SelectDate("#BaselineRecord_IGF1StatusDateBeforeGHTW_IM",
					InputValues[0]);
			EP.HideCursor();
			EP.Wait(2);
			EP.DrpDwn_SelectIndex("#BaselineRecord_IGF1StatusBeforeGHTW_DD",
					EP.RandomValue(1, 3));
			EP.Edit_SetValue("#AddVisit_IGF1Level_EB", InputValues[1]);
			EP.DrpDwn_SelectIndex("#BaselineRecord_IGF1StatusUnitTW_DD",
					EP.RandomValue(1, 5));
			EP.Edit_SetValue(
					"#BaselineRecord_IGF1LaboratoryValueRangeMinIT_EB",
					InputValues[2]);
			EP.Edit_SetValue(
					"#BaselineRecord_IGF1LaboratoryValueRangeMaxIT_EB",
					InputValues[3]);
			EP.Btn_Click("#General_First_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_First_BN");
			}
			break;

		case GREECE:
			if (IsValid) {
				InputValues = new String[] {
						EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"), -5,
								Calendar.DATE), "20", "10", "50" };
			} else {
				InputValues = new String[] {
						EP.Date_AddDays(PatientDOB, -2, Calendar.YEAR),
						MaxValue, MaxValue, MaxValue };
			}

			// Submit valid values for all fields
			EP.SelectDate("#BaselineRecord_IGF1StatusDateBeforeGHIT_IM",
					InputValues[0]);
			EP.HideCursor();
			EP.Wait(2);
			EP.DrpDwn_SelectIndex("#BaselineRecord_IGF1StatusBeforeGHGR_DD",
					EP.RandomValue(1, 5));
			EP.Edit_SetValue("#BaselineRecord_IGF1Level_EB", InputValues[1]);
			EP.DrpDwn_SelectIndex("#BaselineRecord_IGF1StatusUnitGR_DD",
					EP.RandomValue(1, 7));
			EP.Edit_SetValue(
					"#BaselineRecord_IGF1LaboratoryValueRangeMinIT_EB",
					InputValues[2]);
			EP.Edit_SetValue(
					"#BaselineRecord_IGF1LaboratoryValueRangeMaxIT_EB",
					InputValues[3]);
			EP.Btn_Click("#General_First_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_First_BN");
			}
			break;

		case SLOVAKIA:
			if (IsValid) {
				InputValues = new String[] {
						EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"), -5,
								Calendar.DATE), "20", "10", "50" };
			} else {
				InputValues = new String[] {
						EP.Date_AddDays(PatientDOB, -2, Calendar.YEAR),
						MaxValue, MaxValue, MaxValue };
			}

			// Submit valid values for all fields
			EP.SelectDate("#BaselineRecord_IGF1StatusDateBeforeGHIT_IM",
					InputValues[0]);
			EP.HideCursor();
			EP.Wait(2);
			EP.DrpDwn_SelectIndex("#BaselineRecord_IGF1StatusBeforeGHGR_DD",
					EP.RandomValue(1, 3));
			EP.Edit_SetValue("#AddVisit_IGF1Level_EB", InputValues[1]);
			EP.DrpDwn_SelectIndex("#BaselineRecord_IGF1StatusUnitGR_DD",
					EP.RandomValue(1, 5));
			EP.Edit_SetValue(
					"#BaselineRecord_IGF1LaboratoryValueRangeMinIT_EB",
					InputValues[2]);
			EP.Edit_SetValue(
					"#BaselineRecord_IGF1LaboratoryValueRangeMaxIT_EB",
					InputValues[3]);
			EP.Btn_Click("#General_First_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_First_BN");
			}
			break;

		case AUSTRIA:
			if (IsValid) {
				InputValues = new String[] {
						EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"), -5,
								Calendar.DATE), "20", "10", "50" };
			} else {
				InputValues = new String[] {
						EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"), -9,
								Calendar.DATE), MaxValue, MaxValue, MaxValue };
			}
			EP.SelectDate("#BaselineRecord_IGF1StatusDateBeforeGHAT_IM",
					InputValues[0]);
			EP.HideCursor();
			EP.Wait(2);
			EP.DrpDwn_SelectIndex("#BaselineRecord_IGF1StatusBeforeGHAT_DD",
					EP.RandomValue(1, 3));
			EP.DrpDwn_SelectIndex("#BaselineRecord_IGF1StatusUnitAT_DD",
					EP.RandomValue(1, 5));
			EP.Edit_SetValue("#BaselineRecord_IGF1LevelAT_EB", InputValues[1]);
			EP.Edit_SetValue(
					"#BaselineRecord_IGF1LaboratoryValueRangeMinAT_EB",
					InputValues[2]);
			EP.Edit_SetValue(
					"#BaselineRecord_IGF1LaboratoryValueRangeMaxAT_EB",
					InputValues[3]);
			EP.Btn_Click("#General_First_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_First_BN");
			}
			break;

		case SINGAPORE:
		case UNITEDARABEMIRATES:
		case SAUDIARABIA:
		case MEXICO:
			if (IsValid) {
				InputValues = new String[] {
						EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"), -5,
								Calendar.DATE), "20.2", "10", "50" };
			} else {
				InputValues = new String[] {
						EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"), -9,
								Calendar.DATE), MaxValue, MaxValue, MaxValue };
			}
			EP.SelectDate("#BaselineRecord_IGF1StatusDateBeforeGHAT_IM",
					InputValues[0]);
			EP.HideCursor();
			EP.Wait(2);
			EP.DrpDwn_SelectIndex("#BaselineRecord_IGF1StatusBeforeGHAT_DD",
					EP.RandomValue(1, 3));
			EP.DrpDwn_SelectIndex("#BaselineRecord_IGF1StatusUnitAT_DD",
					EP.RandomValue(1, 5));
			EP.Edit_SetValue("#BaselineRecord_IGF1LevelAT_EB",
					EP.Converter(InputValues[1]));
			EP.Edit_SetValue(
					"#BaselineRecord_IGF1LaboratoryValueRangeMinAT_EB",
					InputValues[2]);
			EP.Edit_SetValue(
					"#BaselineRecord_IGF1LaboratoryValueRangeMaxAT_EB",
					InputValues[3]);
			EP.Btn_Click("#General_First_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_First_BN");
			}
			break;

		case SPAIN:
			if (IsValid) {
				InputValues = new String[] {
						EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"), -5,
								Calendar.DATE), "20", "10", "50" };
			} else {
				InputValues = new String[] {
						EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"), -9,
								Calendar.DATE), MaxValue, MaxValue, MaxValue };
			}

			EP.SelectDate("#BaselineRecord_IGF1StatusDateBeforeGHES_IM",
					InputValues[0]);
			EP.HideCursor();
			EP.Wait(2);
			EP.DrpDwn_SelectIndex("#BaselineRecord_IGF1StatusBeforeGHES_DD",
					EP.RandomValue(1, 3));
			EP.DrpDwn_SelectIndex("#BaselineRecord_IGF1StatusUnitES_DD",
					EP.RandomValue(1, 5));
			EP.Edit_SetValue("#BaselineRecord_IGF1LevelAT_EB", InputValues[1]);
			EP.Edit_SetValue(
					"#BaselineRecord_IGF1LaboratoryValueRangeMinES_EB",
					InputValues[2]);
			EP.Edit_SetValue(
					"#BaselineRecord_IGF1LaboratoryValueRangeMaxES_EB",
					InputValues[3]);
			EP.Btn_Click("#General_First_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_First_BN");
			}
			break;

		case ICELAND:
			if (IsValid) {
				InputValues = new String[] {
						EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"), -5,
								Calendar.DATE), "20", "10", "50" };
			} else {
				InputValues = new String[] {
						EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"), -9,
								Calendar.DATE), MaxValue, MaxValue, MaxValue };
			}

			EP.SelectDate("#BaselineRecord_IGF1StatusDateBeforeGHES_IM",
					InputValues[0]);
			EP.HideCursor();
			EP.Wait(2);
			EP.DrpDwn_SelectIndex("#BaselineRecord_IGF1StatusBeforeGHIS_DD",
					EP.RandomValue(1, 3));
			EP.Edit_SetValue("#BaselineRecord_IGF1LevelAT_EB", InputValues[1]);
			EP.DrpDwn_SelectIndex("#BaselineRecord_IGF1StatusUnitIS_DD",
					EP.RandomValue(1, 5));
			EP.Edit_SetValue(
					"#BaselineRecord_IGF1LaboratoryValueRangeMinES_EB",
					InputValues[2]);
			EP.Edit_SetValue(
					"#BaselineRecord_IGF1LaboratoryValueRangeMaxES_EB",
					InputValues[3]);
			EP.Btn_Click("#General_First_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_First_BN");
			}
			break;

		case FINLAND:
			if (IsValid) {
				InputValues = new String[] {
						EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"), -5,
								Calendar.DATE), "20", "10", "50" };
			} else {
				InputValues = new String[] {
						EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"), -9,
								Calendar.DATE), MaxValue, MaxValue, MaxValue };
			}

			EP.SelectDate("#BaselineRecord_IGF1StatusDateBeforeGHES_IM",
					InputValues[0]);
			EP.HideCursor();
			EP.Wait(2);
			EP.DrpDwn_SelectIndex("#BaselineRecord_IGF1StatusBeforeGHFI_DD",
					EP.RandomValue(1, 3));
			EP.Edit_SetValue("#BaselineRecord_IGF1LevelAT_EB", InputValues[1]);
			EP.DrpDwn_SelectIndex("#BaselineRecord_IGF1StatusUnitFI_DD",
					EP.RandomValue(1, 5));
			EP.Edit_SetValue(
					"#BaselineRecord_IGF1LaboratoryValueRangeMinES_EB",
					InputValues[2]);
			EP.Edit_SetValue(
					"#BaselineRecord_IGF1LaboratoryValueRangeMaxES_EB",
					InputValues[3]);
			EP.Btn_Click("#General_First_BN");
			if (!IsValid)
				EP.Btn_Click("#General_First_BN");
			break;

		case AUSTRALIA:
			InputValues = new String[] { EP.Date_AddDays(
					EP.GetTimeStamp("dd MMM yyyy"), -5, Calendar.DATE) };
			EP.SelectDate("#BaselineRecord_IGF1StatusDateBeforeGHES_IM",
					InputValues[0]);
			EP.HideCursor();
			EP.Wait(2);
			EP.DrpDwn_SelectIndex("#BaselineRecord_IGF1StatusBeforeGHAU_DD",
					EP.RandomValue(1, 3));
			EP.Btn_Click("#General_First_BN");
			break;

		case SWEDEN:
			if (IsValid) {
				InputValues = new String[] {
						EP.Date_AddDays(PatientDOB, 2, Calendar.DATE),
						EP.Date_AddDays(PatientDOB, 3, Calendar.DATE), "56",
						"56", "56", "30" };
			} else {
				InputValues = new String[] {
						EP.Date_AddDays(PatientDOB, -1, Calendar.YEAR),
						EP.Date_AddDays(PatientDOB, -2, Calendar.YEAR),
						MaxValue, MaxValue, MaxValue, MaxValue };
			}

			EP.DrpDwn_SelectIndex(
					"#BaselineRecord_BiochemistryIGF1StatusBeforeGHSE_DD",
					EP.RandomValue(1, 5));
			EP.DrpDwn_SelectIndex(
					"#BaselineRecord_BiochemistryIGF1LevelDeviationSE_DD",
					EP.RandomValue(1, 3));
			EP.DrpDwn_SelectIndex(
					"#BaselineRecord_BiochemistryIGF1StatusUnitSE_DD",
					EP.RandomValue(1, 5));
			EP.DrpDwn_SelectIndex(
					"#BaselineRecord_BiochemistryIGF1PriorHbA1cDateSE_DB", 1);
			EP.DrpDwn_SelectIndex(
					"#BaselineRecord_BiochemistryIGF1HbA1cStatusSE_DB", 1);
			// Verify Success Message
			EP.SelectDate("#BaselineRecord_BiochemistryIGF1StatusDateSE_IM",
					InputValues[0]);
			EP.HideCursor();
			EP.Wait(2);
			EP.SelectDate(
					"#BaselineRecord_BiochemistryIGF1PriorHbA1cDateSE_IM",
					InputValues[1]);
			EP.HideCursor();
			EP.Wait(2);
			EP.Edit_SetValue("#BaselineRecord_BiochemistryIGF1LevelSE_EB",
					InputValues[2]);
			EP.Edit_SetValue(
					"#BaselineRecord_BiochemistryIGF1ValueRangeMinSE_EB",
					InputValues[3]);
			EP.Edit_SetValue(
					"#BaselineRecord_BiochemistryIGF1ValueRangeMaxSE_EB",
					InputValues[4]);
			EP.Edit_SetValue(
					"#BaselineRecord_BiochemistryIGF1HbA1cStatusSE_EB",
					InputValues[5]);
			EP.Btn_Click("#General_First_BN");
			if (!IsValid)
				EP.Btn_Click("#General_First_BN");
			break;

		case FRANCE:
			if (IsValid) {
				InputValues = new String[] {
						EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"), -5,
								Calendar.DATE), "30", "40" };
			} else {
				InputValues = new String[] {
						EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"), -5,
								Calendar.DATE), MaxValue, MaxValue };
			}
			EP.SelectDate("#BaselineRecord_BiochemistryAssessmentDate_IM",
					InputValues[0]);
			EP.HideCursor();
			EP.Wait(2);
			EP.DrpDwn_SelectIndex(
					"#BaselineRecord_BiochemistryIGFLevelDeviationFR_DD", 1);
			EP.Edit_SetValue("#BaselineRecord_BiochemistryIGFValueFR_EB", "12");
			EP.DrpDwn_SelectIndex("#BaselineRecord_BiochemistryIGFUnitFR_DD", 2);
			EP.Edit_SetValue(
					"#BaselineRecord_BiochemistryIGFLaboratoryValueRangeMinFR_EB",
					InputValues[1]);
			EP.Edit_SetValue(
					"#BaselineRecord_BiochemistryIGFLaboratoryValueRangeMaxFR_EB",
					InputValues[2]);
			EP.DrpDwn_SelectIndex(
					"#BaselineRecord_BiochemistryIGFStatusBeforeGHFR_DD", 3);
			EP.ChkBx_SetOption("#BaselineRecord_KitNameMedignostFR_CB", "ON");
			EP.ChkBx_SetOption("#BaselineRecord_AssayTypeElisaFR_CB", "ON");
			EP.ChkBx_SetOption("#BaselineRecord_ReferencePrep86522FR_CB", "ON");
			EP.ChkBx_SetOption("#BaselineRecord_KitNameOtherSpecifyFR_CB",
					"OFF");
			EP.ChkBx_SetOption(
					"#BaselineRecord_ReferencePrepOtherSpecifyFR_CB", "OFF");
			EP.Btn_Click("#General_First_BN");
			if (!IsValid)
				EP.Btn_Click("#General_First_BN");
			break;

		case KOREA:

			InputValues = new String[] { EP.GetTimeStamp("dd MMM yyyy"),
					EP.GetTimeStamp("dd MMM yyyy"),
					EP.GetTimeStamp("dd MMM yyyy"), "20", "20", "20", "20",
					"20", "20" };

			// Un-check all the others checkbox

			EP.ChkBx_SetOption("#BaselineRecord_IGF1GHUnitSpecifyKR_CB", "OFF");
			EP.ChkBx_SetOption("#BaselineRecord_IGF1GHKitNameSpecifyKR_CB",
					"OFF");
			EP.ChkBx_SetOption(
					"#BaselineRecord_IGF1GHNormalRangesUsedLowKR_CB", "OFF");
			EP.ChkBx_SetOption(
					"#BaselineRecord_IGF1GHNormalRangesUsedHighKR_CB", "OFF");
			EP.ChkBx_SetOption("#BaselineRecord_IGF1UnitSpecifyKR_CB", "OFF");
			EP.ChkBx_SetOption("#BaselineRecord_IGF1KitNameSpecifyKR_CB", "OFF");
			EP.ChkBx_SetOption(
					"#BaselineRecord_IGF1ReferencePreparationSpecifyKR_CB",
					"OFF");
			EP.ChkBx_SetOption("#BaselineRecord_IGF1NormalRangesUsedLowKR_CB",
					"OFF");
			EP.ChkBx_SetOption("#BaselineRecord_IGF1NormalRangesUsedHighKR_CB",
					"OFF");
			EP.ChkBx_SetOption("#BaselineRecord_IGFBP3UnitSpecifyKR_CB", "OFF");
			EP.ChkBx_SetOption(
					"#BaselineRecord_IGF1GHReferencePreparationSpecifyKR_CB",
					"OFF");
			EP.ChkBx_SetOption(
					"#BaselineRecord_IGFBP3NormalRangesUsedLowKR_CB", "OFF");
			EP.ChkBx_SetOption(
					"#BaselineRecord_IGFBP3NormalRangesUsedHighKR_CB", "OFF");

			EP.SelectDate("#BaselineRecord_IGF1GHAssessmentDateKR_IM",
					InputValues[0]);
			EP.DrpDwn_SelectIndex("#BaselineRecord_IGF1GHLevelDeviationKR_DD",
					EP.RandomValue(1, 3));
			EP.Edit_SetValue("#BaselineRecord_IGF1GHValueKR_EB", InputValues[3]);
			EP.ChkBx_SetOption("#BaselineRecord_IGF1GHUnitNmolLKR_CB", "ON");
			EP.DrpDwn_SelectIndex(
					"#BaselineRecord_IGF1GHStatusbeforeGHtreatmentKR_DD",
					EP.RandomValue(1, 4));
			EP.ChkBx_SetOption("#BaselineRecord_IGF1GHKitNameMediagnostKR_CB",
					"ON");
			EP.ChkBx_SetOption("#BaselineRecord_IGF1GHAssayTypeTrIcmaKR_CB",
					"ON");
			EP.ChkBx_SetOption(
					"#BaselineRecord_IGF1GHReferencePreparation86522KR_CB",
					"ON");
			EP.ChkBx_SetOption(
					"#BaselineRecord_IGF1GHNormalRangesUsedLowKR_CB", "ON");
			EP.Edit_SetValue("#BaselineRecord_IGF1GHNormalRangesUsedLowKR_EB",
					InputValues[4]);

			EP.SelectDate("#BaselineRecord_IGF1AssessmentDateKR_IM",
					InputValues[1]);
			EP.HideCursor();
			EP.Wait(2);
			EP.Edit_SetValue("#BaselineRecord_IGF1ValueKR_EB", InputValues[5]);
			EP.ChkBx_SetOption("#BaselineRecord_IGF1UnitNmolLKR_CB", "ON");
			EP.DrpDwn_SelectIndex("#BaselineRecord_IGF1StatusKR_DD",
					EP.RandomValue(1, 4));
			EP.ChkBx_SetOption("#BaselineRecord_IGF1KitNameMediagnostKR_CB",
					"ON");
			EP.ChkBx_SetOption("#BaselineRecord_IGF1AssayTypeTrIcmaKR_CB", "ON");
			EP.ChkBx_SetOption(
					"#BaselineRecord_IGF1ReferencePreparation86522KR_CB", "ON");
			EP.ChkBx_SetOption("#BaselineRecord_IGF1NormalRangesUsedLowKR_CB",
					"ON");
			EP.Edit_SetValue("#BaselineRecord_IGF1NormalRangesUsedLowKR_EB",
					InputValues[6]);

			EP.SelectDate("#BaselineRecord_IGFBP3AssessmentDateKR_IM",
					InputValues[2]);
			EP.HideCursor();
			EP.Wait(2);
			EP.Edit_SetValue("#BaselineRecord_IGFBP3ValueKR_EB", InputValues[7]);
			EP.ChkBx_SetOption("#BaselineRecord_IGFBP3UnitNmolLKR_CB", "ON");
			EP.ChkBx_SetOption(
					"#BaselineRecord_IGFBP3NormalRangesUsedLowKR_CB", "ON");
			EP.Edit_SetValue("#BaselineRecord_IGFBP3NormalRangesUsedLowKR_EB",
					InputValues[8]);
			EP.Btn_Click("#General_First_BN");
			break;

		default:
		}
		if (("France;Sweden;Korea").contains(Env.CountryUsed)) {
			VerifySuccessMessage(EP.LangData("BiochemistryIGF1"));
		} else {
			VerifySuccessMessage(EP.LangData("IGF1"));
		}
	}

	// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*COMPLETE/TERMINATION*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-**-*-*-*-*-*-*-*-*-*-*-*-*-*
	public void CompletionTerminationStatic(String ActionDone, String TabIndex) {

		Easypod EP = new Easypod();
		ECOSStudy ECOS = new ECOSStudy(EP);

		String TabName = AppendHash(ActionDone);
		switch (ECOS.switchCountryName) {

		case AUSTRIA:
		case SPAIN:
		case FRANCE:
		case UNITEDKINGDOM:
		case GREECE:
		case SLOVAKIA:
		case CZECHREPUBLIC:
		case HUNGARY:
		case NORWAY:
		case CANADA:
		case ITALY:
		case SWEDEN:
			// case CHINA:
		case FINLAND:
		case KOREA:
		case AUSTRALIA:
		case MEXICO:
		case ICELAND:
			// case ARGENTINA:

			EP.DrpDwn_SelectIndex(TabName + "StatusOfStudy_DD", 0);
			EP.VerifyText(TabName + "CompleteORTerminationTitle_ST"); // Form
																		// header
			EP.VerifyText(TabName + "ChapterTitle_ST");
			EP.VerifyText(TabName + "StatusOfStudy_ST");
			EP.CompareDynamicDropDownValues(TabName + "StatusOfStudy_DD",
					EP.ORData(TabName + "StatusOfStudy_DD"), false);
			EP.VerifyText(TabName + "Save_BN");
			EP.VerifyText(TabName + "Cancel_BN");
			break;

		default:
		}
	}

	public void ConcomitantBaselineAdd(boolean IsValid, String TabIndex) {
		// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*SOCIOECONOMIC*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
		String TabXpath = "(" + EP.ORXPath("#NavigationTab_ShowTab_IM") + ")["
				+ TabIndex + "]";
		EP.Lnk_Click(TabXpath);
		while (!EP.ObjectExists("#General_First_BN"))
			EP.Lnk_Click(TabXpath);

		if (EP.ObjectExists("#General_Delete_IM")) {
			DeleteRecord("#General_Delete_IM", false);
		}
		switch (switchCountryName) {

		case SWEDEN:
			// EP.ShowTab(TabIndex);
			while (!EP.ObjectExists("#General_First_BN"))
				EP.Lnk_Click(TabXpath);
			EP.Btn_Click("#General_First_BN");
			EP.DrpDwn_SelectIndex(
					"#BaselineRecord_OtherConcomitantMedication_DB", 1);
			EP.DrpDwn_SelectIndex("#BaselineRecord_ConcomitantVisitDate_DB", 1);
			EP.SelectDate("#BaselineRecord_VisitDateCalendar_IM", EP
					.Date_AddDays(EP.TestData("ECOSPatientDOB"), 2,
							Calendar.YEAR));
			EP.HideCursor();
			EP.Wait(2);
			EP.Edit_SetValue("#BaselineRecord_ConcomitantOtherConcomitant_EB",
					"TestMedicine123");
			EP.Btn_Click("#General_AddRecordSave_BN");

		}
	}

	public void CompletionTerminationDynamic(String ActionDone,
			String TabIndex, String RecoveringURL) {

		Easypod EP = new Easypod();
		ECOSStudy ECOS = new ECOSStudy(EP);

		String MinValue = "0";
		String IncorrectFormat = "a#b";

		String TabName = AppendHash(ActionDone);
		switch (ECOS.switchCountryName) {

		case ITALY:

			// Completed Section

			EP.DrpDwn_SelectIndex(TabName + "StatusOfStudy_DD", 1);
			EP.TakeStaticScreenshot("StudyCompleted");
			EP.VerifyText(TabName + "StudyCompletionAT_ST");
			EP.CompareDropDownValues(TabName + "StudyCompletionAT_DD");
			EP.VerifyText(TabName + "PatientHeightAtEndOfStudyAT_ST");
			EP.CompareDropDownValues(TabName + "SubjectHeightAT_DD");
			EP.VerifyText(TabName + "DateOfMeasurementAT_ST");
			EP.VerifyToolTip(TabName + "DateOFMeasurementCalendarAT_TT");
			EP.VerifyText(TabName + "MeasurementAT_ST");
			EP.VerifyTextContained(TabName + "MeasurementCmScaleAT_ST");
			EP.VerifyText(TabName + "LastStudyDayAT_ST");
			EP.VerifyToolTip(TabName + "LastStudyDayCalendarAT_TT");
			EP.VerifyText(TabName + "ContinuedTreatmentStopDateAT_ST");
			EP.VerifyToolTip(TabName + "ContinuedTreatmentStopDateAT_IM");
			EP.VerifyPageLayout();

			// Dynamic Contents with Status = Completed
			// Verifying the mandatory fields of Completed
			EP.DrpDwn_SelectIndex(TabName + "StatusOfStudy_DD", 1);
			EP.DrpDwn_SelectIndex(TabName + "StudyCompletionAT_DD", 0); // Study
																		// Completion
			EP.DrpDwn_SelectIndex(TabName + "SubjectHeightAT_DD", 0);
			EP.Edit_SetValueByJavaScript(TabName + "DateOfMeasurementAT_EB", "");
			EP.Edit_SetValueByJavaScript(TabName + "LastStudyDayAT_EB", "");
			EP.Edit_SetValueByJavaScript(TabName + "TreatmentStopDateAT_EB", "");
			EP.Edit_SetValue(TabName + "MeasurementCompletedAT_EB", "");
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("FieldMandatory");
			EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
					EP.GetText(TabName + "ValueOutOfRangeErrorIT_DT"));
			EP.CompareText("StudyCompletion", EP.LangData("FieldIsMandatory"),
					GetErrorMessage(1));
			EP.CompareText("PatientHeightAtEndOfStudy",
					EP.LangData("FieldIsMandatory"), GetErrorMessage(2));
			EP.CompareText("DateOfMeasurement",
					EP.LangData("FieldIsMandatory"), GetErrorMessage(3));
			// EP.CompareText("TreatmentStopDate",
			// EP.LangData("FieldIsMandatory"), GetErrorMessage(6));
			EP.CompareText("Ignoreandsave", EP.LangData("IgnoreAndSave"),
					EP.GetText(TabName + "Save_BN"));
			EP.ClearStaticScreenshot();
			EP.Btn_Click(TabName + "Cancel_BN");
			EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");

			// Date and Measurement Range Error Message Validations
			EP.DrpDwn_SelectIndex(TabName + "StatusOfStudy_DD", 1);
			EP.DrpDwn_SelectIndex(TabName + "StudyCompletionAT_DD", 2); // Study
																		// Completion
			EP.DrpDwn_SelectIndex(TabName + "SubjectHeightAT_DD", 3);
			EP.Edit_SetValue(TabName + "MeasurementCompletedAT_EB", MinValue);
			EP.SelectDate(TabName + "LastStudyDayCalendarAT_TT",
					EP.TestData("ECOSPatientDOB"));
			EP.SelectDate(TabName + "ContinuedTreatmentStopDateAT_IM",
					EP.TestData("ECOSPatientDOB"));
			EP.SelectDate(TabName + "DateOFMeasurementCalendarAT_TT",
					EP.TestData("ECOSPatientDOB"));
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("IncorrectFormatError1");
			EP.VerifyText(TabName + "IncorrecrFormatErrorIT_DT");// Header error
																	// msg
			EP.CompareText("StudyCompletionErrMsg_DT",
					EP.LangData("AccordanceWithAnother"), GetErrorMessage(1));
			EP.CompareText("PatientHeightErrMsg_DT",
					EP.LangData("AccordanceWithAnother"), GetErrorMessage(2));
			EP.VerifyRegExpression("MothersHeight_RangeError",
					EP.LangData("RangeValue"), GetErrorMessage(3), "\\d{1,3}");
			EP.CompareText("LastStudyDayErrMsg_DT",
					EP.LangData("LastStudyDateError"), GetErrorMessage(4));
			EP.CompareText("TreatmentStopDate",
					EP.LangData("LastStudyDateError"), GetErrorMessage(5));
			EP.ClearStaticScreenshot();

			// Final height is smaller than the latest visit height
			EP.DrpDwn_SelectIndex(TabName + "SubjectHeightAT_DD", 1);
			EP.Edit_ClearValue(TabName + "MeasurementCompleted_EB");
			EP.SelectDate(TabName + "LastStudyDayCalendarAT_TT", EP
					.Date_AddDays(EP.TestData("ECOSPatientDOB"), 2,
							Calendar.MONTH));
			EP.SelectDate(TabName + "DateOFMeasurementCalendarAT_TT", EP
					.Date_AddDays(EP.TestData("ECOSPatientDOB"), 2,
							Calendar.MONTH));
			EP.Edit_SetValue(TabName + "MeasurementCompletedAT_EB", 55);
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("IncorrectFormatError2");
			EP.CompareText("IncorrecrFormatErrorIT_DT",
					EP.LangData("VerifyIncorrectFormats"),
					EP.GetText(TabName + "IncorrecrFormatErrorIT_DT"));
			EP.CompareText("PatientHeightErrMsg_DT",
					EP.LangData("FinalHeightSmaller"), GetErrorMessage(2));
			EP.ClearStaticScreenshot();

			// Patient is female and final height recorded and age should be
			// greater than 14 years old
			EP.DrpDwn_SelectIndex(TabName + "StatusOfStudy_DD", 1);
			EP.DrpDwn_SelectIndex(TabName + "StudyCompletionAT_DD", 1);
			EP.DrpDwn_SelectIndex(TabName + "SubjectHeightAT_DD", 1);
			EP.Edit_SetValue(TabName + "MeasurementCompleted_EB", "100");
			EP.SelectDate(TabName + "DateOFMeasurementCalendarAT_TT", EP
					.Date_AddDays(EP.TestData("ECOSPatientDOB"), 2,
							Calendar.MONTH));
			EP.SelectDate(TabName + "ContinuedTreatmentStopDateAT_IM", EP
					.Date_AddDays(EP.TestData("ECOSPatientDOB"), 2,
							Calendar.MONTH));
			EP.Wait(2);
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("AgeMustBeGreaterThan14");
			EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
					EP.GetText(TabName + "ValueOutOfRangeErrorIT_DT"));
			EP.CompareText("PatientHeightErrMsg_DT",
					EP.LangData("AgeMustBeGreaterThan14"),
					EP.GetText(TabName + "PatientHeightErrMsgAT_DT"));
			EP.ClearStaticScreenshot();

			// Incorrect format for measurement
			EP.DrpDwn_SelectIndex(TabName + "SubjectHeightAT_DD", 2);
			EP.DrpDwn_SelectIndex(TabName + "StudyCompletionAT_DD", 2);
			EP.Edit_SetValue(TabName + "MeasurementCompleted_EB",
					IncorrectFormat);
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("incorrectFormatMeasurement");
			EP.CompareText("IncorrecrFormatError_DT",
					EP.LangData("VerifyIncorrectFormats"),
					EP.GetText(TabName + "IncorrecrFormatErrorIT_DT"));
			EP.CompareText("MeasurementErrMsg_DT",
					EP.LangData("IncorrectFormat"), GetErrorMessage(1));
			EP.ClearStaticScreenshot();
			EP.Edit_SetValue(TabName + "MeasurementCompleted_EB", 55);
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("nearfinalSmaller");
			EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
					EP.GetText(TabName + "ValueOutOfRangeErrorIT_DT"));
			EP.CompareText("PatientHeightErrMsg_DT",
					EP.LangData("NearFinalSmaller"), GetErrorMessage(1));
			EP.CompareText("Ignoreandsave", EP.LangData("IgnoreAndSave"),
					EP.GetText(TabName + "Save_BN"));
			EP.ClearStaticScreenshot();
			EP.Btn_Click(TabName + "Cancel_BN");
			EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");

			// Error for Treatment Stop Date lesser than treatment Start Date

			String TreatmentStartDate = EP.Date_AddDays(EP.Date_AddDays(
					EP.TestData("ECOSPatientDOB"), 2, Calendar.MONTH), -1,
					Calendar.DATE);
			EP.DrpDwn_SelectIndex(TabName + "StudyCompletionAT_DD", 3);// 1
			EP.DrpDwn_SelectIndex(TabName + "SubjectHeightAT_DD", 3);// 1
			EP.Edit_SetValue(TabName + "MeasurementCompleted_EB", 60);
			EP.SelectDate(TabName + "DateOFMeasurementCalendarAT_TT", EP
					.Date_AddDays(EP.TestData("ECOSPatientDOB"), 2,
							Calendar.MONTH));
			EP.SelectDate(TabName + "LastStudyDayCalendarAT_TT", EP
					.Date_AddDays(EP.TestData("ECOSPatientDOB"), 2,
							Calendar.MONTH));
			EP.SelectDate(TabName + "ContinuedTreatmentStopDateAT_IM",
					TreatmentStartDate);
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("TSDLessThanCurrentGH");
			EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
					EP.GetText(TabName + "ValueOutOfRangeErrorIT_DT"));
			EP.VerifyRegExpression("TSDLessThanCurrentGH",
					EP.LangData("TSDLessThanCurrentGH"), GetErrorMessage(1),
					EP.TranslateDate(TreatmentStartDate, "dd MMM yyyy", false));
			EP.ClearStaticScreenshot();
			EP.Btn_Click(TabName + "Cancel_BN");
			EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");

			// Success message for Completion
			EP.DrpDwn_SelectIndex(TabName + "StudyCompletionAT_DD", 3);// 1
			EP.DrpDwn_SelectIndex(TabName + "SubjectHeightAT_DD", 3);// 1
			EP.Edit_SetValue(TabName + "MeasurementCompleted_EB", 60);
			EP.SelectDate(TabName + "DateOFMeasurementCalendarAT_TT",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate(TabName + "ContinuedTreatmentStopDateAT_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate(TabName + "LastStudyDayCalendarAT_TT",
					EP.GetTimeStamp("dd MMM yyyy"));

			// EP.SelectDate(TabName+"ContinuedTreatmentStopDateAT_IM",
			// EP.Date_AddDays(EP.TestData("ECOSPatientDOB"), 2,
			// Calendar.MONTH));
			EP.VerifyMonth(TabName + "TreatmentStopDateAT_EB");
			EP.VerifyMonth(TabName + "DateOfMeasurementAT_EB");
			EP.VerifyMonth(TabName + "LastStudyDayAT_EB");
			EP.Btn_Click(TabName + "Save_BN");
			ECOS.VerifySuccessMessage(EP.LangData("CompletionOrTermination"));

			// To check error messgage for male patient
			EP.SearchForPatient(EP.TestData("ECOSPatient"));
			EP.Rbtn_SetOption("#MyPatients_FirstPatient_RB", "ON");
			EP.Btn_Click("#MyPatients_BaselineRecord_BN");
			EP.Lnk_Click("#NavigationTab_RecordOverview_LK");
			EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");
			EP.DrpDwn_SelectIndex(TabName + "StatusOfStudy_DD", 1);
			EP.DrpDwn_SelectIndex(TabName + "StudyCompletion_DD", 1);
			EP.DrpDwn_SelectIndex(TabName + "SubjectHeight_DD", 1);
			EP.Edit_SetValue(TabName + "MeasurementCompleted_EB", "100");
			EP.Wait(2);
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("AgeMustBeGreaterThan15Error");
			EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
					EP.GetText(TabName + "ValueOutOfRangeErrorIT_DT"));
			EP.CompareText("PatientHeightErrMsg_DT",
					EP.LangData("AgeMustBeGreaterThan15"), GetErrorMessage(1));
			EP.ClearStaticScreenshot();

			// ****************DIS CONTINUED
			// SECTION*********************************
			if (EP.ObjectExists("#NavigationTab_MyPatientsList_LK"))
				;
			EP.Lnk_Click("#NavigationTab_MyPatientsList_LK");
			EP.SearchForPatient(EP.TestData("PatientName") + " 04");
			EP.Rbtn_SetOption("#MyPatients_FirstPatient_RB", "ON");
			EP.Btn_Click("#MyPatients_BaselineRecord_BN");
			EP.Lnk_Click("#NavigationTab_RecordOverview_LK");
			EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");
			EP.DrpDwn_SelectIndex(TabName + "StatusOfStudy_DD", 2);
			EP.TakeStaticScreenshot("StudyDiscontinued");
			EP.VerifyText(TabName + "PrematureStudyDiscontinuationReasonAT_ST");
			EP.CompareDynamicDropDownValues(TabName
					+ "DiscontinuationReasonAT_DD",
					EP.ORData(TabName + "DiscontinuationReasonAT_DD"), false);
			EP.DrpDwn_SelectIndex(TabName + "DiscontinuationReasonAT_DD", 2);// To
																				// make
																				// visible
																				// Remove
																				// data
																				// from
																				// Study?

			EP.VerifyText(TabName + "RemoveDataFromStudyAT_ST");
			EP.VerifyText(TabName + "RemoveDataYesAT_ST");
			EP.VerifyText(TabName + "RemoveDataNoAT_ST");

			EP.VerifyText(TabName + "InitiatorOfWithdrawalAT_ST");
			EP.CompareDynamicDropDownValues(TabName
					+ "InitiatorOfWithdrawalAT_DD",
					EP.ORData(TabName + "InitiatorOfWithdrawalAT_DD"), false);
			EP.VerifyText(TabName + "PatientHeightAtPrematureAT_ST");
			EP.CompareDynamicDropDownValues(TabName
					+ "PatientHeightAtDiscontinuationAT_DD",
					EP.ORData(TabName + "PatientHeightAtDiscontinuation_DD"),
					false);
			EP.VerifyText(TabName + "DisContLastStudyDayAT_ST");
			EP.VerifyToolTip(TabName + "DiscontinuedLastStudyDayCalendarAT_TT");
			EP.VerifyToolTip(TabName + "DiscontinuedTreatmentStopDateAT_IM");
			EP.VerifyText(TabName + "DiscontinuedMeasurementAT_ST");
			EP.VerifyText(TabName + "DiscontinuedTreatmentStopDateAT_ST");
			EP.VerifyTextContained(TabName + "MeasurementCmAT_ST");
			EP.ClearStaticScreenshot();

			// Mandatory fields of discontinued section
			EP.DrpDwn_SelectIndex(TabName + "StatusOfStudy_DD", 2);
			EP.DrpDwn_SelectIndex(TabName + "DiscontinuationReasonAT_DD", 0);
			EP.DrpDwn_SelectIndex(TabName + "InitiatorOfWithdrawalAT_DD", 0);
			EP.Edit_SetValueByJavaScript(TabName
					+ "DiscontinuedLastStudyDayAT_EB", "");
			EP.Edit_SetValueByJavaScript(TabName
					+ "DiscontinuedTreatmentStopDateAT_EB", "");
			EP.Edit_ClearValue(TabName + "Measurement_EB");
			EP.DrpDwn_SelectIndex(TabName
					+ "PatientHeightAtDiscontinuationAT_DD", 0);
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("Mandatory");
			EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
					EP.GetText(TabName + "ValueOutOfRangeErrorIT_DT"));
			EP.CompareText("PrematureStudyDiscontinuationReasonMandatory",
					EP.LangData("FieldIsMandatory"), GetErrorMessage(1));
			EP.CompareText("InitiatorofwithdrawalMandatory",
					EP.LangData("FieldIsMandatory"), GetErrorMessage(2));
			EP.CompareText("StudyDiscontinuationMandatory",
					EP.LangData("FieldIsMandatory"), GetErrorMessage(3));
			EP.CompareText("Ignoreandsave", EP.LangData("IgnoreAndSave"),
					EP.GetText(TabName + "Save_BN"));
			EP.ClearStaticScreenshot();
			EP.Btn_Click(TabName + "Cancel_BN");
			EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");

			// Measurement range value and Date related error messages/Treatment
			// Discontinuation Details madatory field
			EP.DrpDwn_SelectIndex(TabName + "StatusOfStudy_DD", 2);
			EP.DrpDwn_SelectIndex(TabName + "DiscontinuationReasonAT_DD", 1);
			EP.DrpDwn_SelectIndex(TabName + "InitiatorOfWithdrawalAT_DD", 1);
			EP.DrpDwn_SelectIndex(TabName
					+ "PatientHeightAtDiscontinuationAT_DD", 3);
			EP.DrpDwn_SelectIndex(TabName
					+ "TreatmentDiscontinuationDetailsAT_DD", 0);
			EP.SelectDate(TabName + "DiscontinuedLastStudyDayCalendarAT_TT",
					EP.TestData("ECOSPatientDOB"));
			EP.SelectDate(TabName + "DiscontinuedTreatmentStopDateAT_IM",
					EP.TestData("ECOSPatientDOB"));
			EP.Edit_SetValue(TabName + "Measurement_EB", MinValue);
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("OutOfRangeError");
			EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
					EP.GetText(TabName + "ValueOutOfRangeErrorIT_DT"));
			EP.CompareText("TreatmentDiscontinuationDetailsMandatory",
					EP.LangData("FieldIsMandatory"), GetErrorMessage(1));
			EP.CompareText("LastStudyDateError",
					EP.LangData("LastStudyDateError"), GetErrorMessage(2));
			EP.CompareText("TreatmentStopDate ",
					EP.LangData("LastStudyDateError"), GetErrorMessage(3));
			EP.VerifyRegExpression("Height", EP.LangData("RangeValue"),
					GetErrorMessage(4), "\\d{2,3}");
			EP.CompareText("Ignoreandsave", EP.LangData("IgnoreAndSave"),
					EP.GetText(TabName + "Save_BN"));
			EP.ClearStaticScreenshot();
			EP.Btn_Click(TabName + "Cancel_BN");

			// Validation of "Other, Specify" error messages

			EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");
			EP.DrpDwn_SelectIndex(TabName + "StatusOfStudy_DD", 2);
			EP.DrpDwn_SelectIndex(TabName + "DiscontinuationReasonAT_DD", 6);
			EP.Edit_ClearValue(TabName + "DiscontinuationReasonAT_EB");
			EP.DrpDwn_SelectIndex(TabName + "InitiatorOfWithdrawalAT_DD", 5);
			EP.Edit_ClearValue(TabName + "InitiatorOfWithdrawalAT_EB");
			EP.SelectDate(TabName + "DiscontinuedLastStudyDayCalendarAT_TT", EP
					.Date_AddDays(EP.TestData("ECOSPatientDOB"), 2,
							Calendar.MONTH));
			EP.SelectDate(TabName + "DiscontinuedTreatmentStopDateAT_IM", EP
					.Date_AddDays(EP.TestData("ECOSPatientDOB"), 2,
							Calendar.MONTH));
			EP.DrpDwn_SelectIndex(TabName
					+ "PatientHeightAtDiscontinuationAT_DD", 3);
			EP.Edit_SetValue(TabName + "Measurement_EB", 56);
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("OutOfRangeError");
			EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
					EP.GetText(TabName + "ValueOutOfRangeErrorIT_DT"));
			EP.CompareText("FieldIsMandatory", EP.LangData("FieldIsMandatory"),
					GetErrorMessage(1));
			EP.CompareText("FieldIsMandatory", EP.LangData("FieldIsMandatory"),
					GetErrorMessage(2));
			EP.CompareText("Ignoreandsave", EP.LangData("IgnoreAndSave"),
					EP.GetText(TabName + "Save_BN"));
			EP.ClearStaticScreenshot();
			EP.Btn_Click(TabName + "Cancel_BN");

			// Incorrect Formats
			EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");
			EP.Edit_ClearValue(TabName + "MeasurementAT_EB");
			EP.Edit_SetValue(TabName + "MeasurementAT_EB", IncorrectFormat);
			EP.DrpDwn_SelectIndex(TabName + "DiscontinuationReasonAT_DD", 5);
			EP.DrpDwn_SelectIndex(TabName
					+ "PatientHeightAtDiscontinuationAT_DD", 3);
			EP.DrpDwn_SelectIndex(TabName + "InitiatorOfWithdrawalAT_DD", 4);
			EP.SelectDate(TabName + "DiscontinuedLastStudyDayCalendarAT_TT",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate(TabName + "DiscontinuedTreatmentStopDateAT_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("MeasurementFormatError");
			EP.VerifyText(TabName + "IncorrecrFormatErrorIT_DT");
			EP.CompareText("IncorrectFormat", EP.LangData("IncorrectFormat"),
					GetErrorMessage(1));
			EP.ClearStaticScreenshot();

			EP.Btn_Click(TabName + "Cancel_BN");
			EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");

			// Error for Treatment Stop Date lesser than treatment Start Date

			EP.Edit_SetValue(TabName + "MeasurementAT_EB", 60);
			EP.DrpDwn_SelectIndex(TabName + "DiscontinuationReasonAT_DD", 5);
			EP.DrpDwn_SelectIndex(TabName
					+ "PatientHeightAtDiscontinuationAT_DD", 3);
			EP.DrpDwn_SelectIndex(TabName + "InitiatorOfWithdrawalAT_DD", 4);
			EP.SelectDate(TabName + "DiscontinuedLastStudyDayAT_EB", EP
					.Date_AddDays(EP.TestData("ECOSPatientDOB"), 2,
							Calendar.MONTH));
			EP.SelectDate(TabName + "DiscontinuedTreatmentStopDateAT_IM",
					TreatmentStartDate);
			EP.SelectDate(TabName + "DateOFMeasurementCalendarAT_TT", EP
					.Date_AddDays(EP.TestData("ECOSPatientDOB"), 2,
							Calendar.MONTH));
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("TSDLessThanCurrentGH");
			EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
					EP.GetText(TabName + "ValueOutOfRangeErrorIT_DT"));
			EP.VerifyRegExpression("TSDLessThanCurrentGH",
					EP.LangData("TSDLessThanCurrentGH"), GetErrorMessage(1),
					EP.TranslateDate(TreatmentStartDate, "dd MMM yyyy", false));
			EP.ClearStaticScreenshot();
			EP.Btn_Click(TabName + "Cancel_BN");
			EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");

			// To verify success message for Discontinued
			EP.DrpDwn_SelectIndex(TabName + "DiscontinuationReasonAT_DD",
					EP.RandomValue(2, 5));
			EP.DrpDwn_SelectIndex(TabName + "InitiatorOfWithdrawalAT_DD",
					EP.RandomValue(1, 4));
			EP.Edit_SetValue(TabName + "Measurement_EB", "56");
			EP.SelectDate(TabName + "DiscontinuedLastStudyDayCalendarAT_TT",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate(TabName + "DiscontinuedTreatmentStopDateAT_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.TakeStaticScreenshot("MonthVerification");
			EP.VerifyMonth(TabName + "DiscontinuedTreatmentStopDateAT_EB");
			EP.VerifyMonth(TabName + "DiscontinuedLastStudyDayAT_EB");
			EP.ClearStaticScreenshot();
			EP.Btn_Click(TabName + "Save_BN");
			ECOS.VerifySuccessMessage(EP.LangData("CompletionOrTermination"));

			break;
		case CANADA:

			EP.DrpDwn_SelectIndex(TabName + "StatusOfStudy_DD", 1);
			EP.TakeStaticScreenshot("StudyCompleted");
			EP.VerifyText(TabName + "StudyCompletionAT_ST");
			EP.CompareDropDownValues(TabName + "StudyCompletionCA_DD");
			EP.VerifyText(TabName + "PatientHeightAtEndOfStudyAT_ST");
			EP.CompareDropDownValues(TabName + "SubjectHeightCA_DD");
			EP.VerifyText(TabName + "DateOfMeasurementAT_ST");
			EP.VerifyToolTip(TabName + "DateOFMeasurementCalendarAT_TT");
			EP.VerifyText(TabName + "MeasurementAT_ST");
			EP.VerifyTextContained(TabName + "MeasurementCmScaleAT_ST");
			EP.VerifyText(TabName + "LastStudyDayAT_ST");
			EP.VerifyToolTip(TabName + "LastStudyDayCalendarAT_TT");
			EP.VerifyText(TabName + "ContinuedTreatmentStopDateCA_ST");
			EP.VerifyToolTip(TabName + "ContinuedTreatmentStopDateCA_IM");
			EP.VerifyPageLayout();

			// Error message when Please Select is selected in the first
			// dropdown
			EP.DrpDwn_SelectIndex(TabName + "StatusOfStudy_DD", 0);
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("FieldMandatory");
			EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
					EP.GetText(TabName + "ValueOutOfRangeErrorES_DT"));
			EP.CompareText("StatusOfStudy_DD_Mandatory",
					EP.LangData("FieldIsMandatory"), GetErrorMessage(1));
			EP.CompareText("Ignoreandsave", EP.LangData("IgnoreAndSave"),
					EP.GetText(TabName + "Save_BN"));
			EP.ClearStaticScreenshot();
			EP.Btn_Click(TabName + "Cancel_BN");
			EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");

			// Dynamic Contents with Status = Completed
			// Verifying the mandatory fields of Completed
			EP.DrpDwn_SelectIndex(TabName + "StatusOfStudy_DD", 1);
			EP.DrpDwn_SelectIndex(TabName + "StudyCompletionCA_DD", 0); // Study
																		// Completion
			EP.DrpDwn_SelectIndex(TabName + "SubjectHeightCA_DD", 0);
			EP.Edit_SetValueByJavaScript(TabName + "DateOfMeasurementAT_EB", "");
			EP.Edit_SetValueByJavaScript(TabName + "LastStudyDayAT_EB", "");
			EP.Edit_SetValueByJavaScript(TabName + "TreatmentStopDateAT_EB", "");
			EP.Edit_SetValue(TabName + "MeasurementCompletedAT_EB", "");
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("FieldMandatory");
			EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
					EP.GetText(TabName + "ValueOutOfRangeErrorIT_DT"));
			EP.CompareText("StudyCompletion", EP.LangData("FieldIsMandatory"),
					GetErrorMessage(1));
			EP.CompareText("PatientHeightAtEndOfStudy",
					EP.LangData("FieldIsMandatory"), GetErrorMessage(2));
			EP.CompareText("DateOfMeasurement",
					EP.LangData("FieldIsMandatory"), GetErrorMessage(3));
			EP.CompareText("Measurement", EP.LangData("FieldIsMandatory"),
					GetErrorMessage(4));
			EP.CompareText("LastStudyDate", EP.LangData("FieldIsMandatory"),
					GetErrorMessage(5));
			EP.CompareText("Ignoreandsave", EP.LangData("IgnoreAndSave"),
					EP.GetText(TabName + "Save_BN"));
			EP.ClearStaticScreenshot();
			EP.Btn_Click(TabName + "Cancel_BN");
			EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");

			// Date and Measurement Range Error Message Validations
			EP.DrpDwn_SelectIndex(TabName + "StatusOfStudy_DD", 1);
			EP.DrpDwn_SelectIndex(TabName + "StudyCompletionCA_DD", 2); // Study
																		// Completion
			EP.DrpDwn_SelectIndex(TabName + "SubjectHeightCA_DD", 3);
			EP.Edit_SetValue(TabName + "MeasurementCompletedAT_EB", MinValue);
			EP.SelectDate(TabName + "ContinuedTreatmentStopDateCA_IM",
					EP.TestData("ECOSPatientDOB"));
			EP.SelectDate(TabName + "LastStudyDayCalendarAT_TT",
					EP.TestData("ECOSPatientDOB"));
			EP.SelectDate(TabName + "DateOFMeasurementCalendarAT_TT",
					EP.TestData("ECOSPatientDOB"));
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("IncorrectFormatError1");
			EP.VerifyText(TabName + "IncorrecrFormatErrorIT_DT");// Header error
																	// msg
			EP.CompareText("StudyCompletionErrMsg_DT",
					EP.LangData("AccordanceWithAnother"), GetErrorMessage(1));
			EP.CompareText("PatientHeightErrMsg_DT",
					EP.LangData("AccordanceWithAnother"), GetErrorMessage(2));
			EP.VerifyRegExpression("MothersHeight_RangeError",
					EP.LangData("RangeValue"), GetErrorMessage(3), "\\d{1,3}");
			EP.CompareText("LastStudyDayErrMsg_DT",
					EP.LangData("LastStudyDateError"), GetErrorMessage(4));
			EP.CompareText("TreatmentStopDate",
					EP.LangData("LastStudyDateError"), GetErrorMessage(5));
			EP.ClearStaticScreenshot();

			// Final height is smaller than the latest visit height
			EP.DrpDwn_SelectIndex(TabName + "SubjectHeightCA_DD", 1);
			EP.Edit_ClearValue(TabName + "MeasurementCompleted_EB");
			EP.SelectDate(TabName + "LastStudyDayCalendarAT_TT", EP
					.Date_AddDays(EP.TestData("ECOSPatientDOB"), 2,
							Calendar.MONTH));
			EP.SelectDate(TabName + "DateOFMeasurementCalendarAT_TT", EP
					.Date_AddDays(EP.TestData("ECOSPatientDOB"), 2,
							Calendar.MONTH));
			EP.Edit_SetValue(TabName + "MeasurementCompletedAT_EB", 55);
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("IncorrectFormatError2");
			EP.CompareText("IncorrecrFormatErrorIT_DT",
					EP.LangData("VerifyIncorrectFormats"),
					EP.GetText(TabName + "IncorrecrFormatErrorIT_DT"));
			EP.CompareText("PatientHeightErrMsg_DT",
					EP.LangData("FinalHeightSmaller"), GetErrorMessage(2));
			EP.ClearStaticScreenshot();

			// Patient is female and final height recorded and age should be
			// greater than 14 years old
			EP.DrpDwn_SelectIndex(TabName + "StatusOfStudy_DD", 1);
			EP.DrpDwn_SelectIndex(TabName + "StudyCompletionCA_DD", 1);
			EP.DrpDwn_SelectIndex(TabName + "SubjectHeightCA_DD", 1);
			EP.Edit_SetValue(TabName + "MeasurementCompleted_EB", "100");
			EP.SelectDate(TabName + "ContinuedTreatmentStopDateCA_IM", EP
					.Date_AddDays(EP.TestData("ECOSPatientDOB"), 2,
							Calendar.MONTH));
			EP.SelectDate(TabName + "DateOFMeasurementCalendarAT_TT", EP
					.Date_AddDays(EP.TestData("ECOSPatientDOB"), 2,
							Calendar.MONTH));
			EP.Wait(2);
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("AgeMustBeGreaterThan14");
			EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
					EP.GetText(TabName + "ValueOutOfRangeErrorIT_DT"));
			EP.CompareText("PatientHeightErrMsg_DT",
					EP.LangData("AgeMustBeGreaterThan14"),
					EP.GetText(TabName + "PatientHeightErrMsgAT_DT"));
			EP.ClearStaticScreenshot();

			// Incorrect format for measurement
			EP.DrpDwn_SelectIndex(TabName + "SubjectHeightCA_DD", 2);
			EP.DrpDwn_SelectIndex(TabName + "StudyCompletionCA_DD", 2);
			EP.Edit_SetValue(TabName + "MeasurementCompleted_EB",
					IncorrectFormat);
			EP.SelectDate(TabName + "ContinuedTreatmentStopDateCA_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate(TabName + "LastStudyDayCalendarAT_TT",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate(TabName + "DateOFMeasurementCalendarAT_TT",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("incorrectFormatMeasurement");
			EP.CompareText("IncorrecrFormatError_DT",
					EP.LangData("VerifyIncorrectFormats"),
					EP.GetText(TabName + "IncorrecrFormatErrorIT_DT"));
			EP.CompareText("MeasurementErrMsg_DT",
					EP.LangData("IncorrectFormat"), GetErrorMessage(1));
			EP.ClearStaticScreenshot();
			EP.Edit_SetValue(TabName + "MeasurementCompleted_EB", 55);
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("nearfinalSmaller");
			EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
					EP.GetText(TabName + "ValueOutOfRangeErrorIT_DT"));
			EP.CompareText("PatientHeightErrMsg_DT",
					EP.LangData("NearFinalSmaller"), GetErrorMessage(1));
			EP.CompareText("Ignoreandsave", EP.LangData("IgnoreAndSave"),
					EP.GetText(TabName + "Save_BN"));
			EP.ClearStaticScreenshot();

			// Error for Treatment Stop Date lesser than treatment Start Date
			EP.NavigateTo(RecoveringURL);
			TreatmentStartDate = EP.Date_AddDays(
					EP.GetTimeStamp("dd MMM yyyy"), -1, Calendar.DATE);
			EP.DrpDwn_SelectIndex(TabName + "StudyCompletionCA_DD", 3);// 1
			EP.DrpDwn_SelectIndex(TabName + "SubjectHeightCA_DD", 3);// 1
			EP.Edit_SetValue(TabName + "MeasurementCompleted_EB", 60);
			EP.SelectDate(TabName + "ContinuedTreatmentStopDateAT_IM",
					TreatmentStartDate);
			EP.SelectDate(TabName + "LastStudyDayCalendarAT_TT",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate(TabName + "DateOFMeasurementCalendarAT_TT", EP
					.Date_AddDays(EP.TestData("ECOSPatientDOB"), 2,
							Calendar.MONTH));
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("TSDLessThanCurrentGH");
			EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
					EP.GetText(TabName + "IncorrecrFormatErrorES_DT"));
			EP.VerifyRegExpression("TSDLessThanCurrentGH",
					EP.LangData("TSDLessThanCurrentGH"), GetErrorMessage(1),
					EP.TranslateDate(TreatmentStartDate, "dd MMM yyyy", false));
			EP.CompareText("Ignoreandsave", EP.LangData("IgnoreAndSave"),
					EP.GetText(TabName + "Save_BN"));
			EP.ClearStaticScreenshot();
			EP.Btn_Click(TabName + "Cancel_BN");
			EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");
			EP.NavigateTo(Env.ECOSURL);
			// -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

			// To get the Visit Date (Last Visit)
			EP.SearchForPatient(EP.TestData("PatientName") + " 01");
			EP.Rbtn_SetOption("#MyPatients_FirstPatient_RB", "ON");
			EP.Btn_Click("#MyPatients_VisitsRecord_BN");
			EP.WbTbl_SortColumn("#AddVisit_VisitDate_TE", 2, "DESC");// Bring
																		// the
																		// latest
																		// record
																		// on
																		// top
			String Date = EP.WbTbl_GetText("#AddVisit_VisitDate_TE", 1, 2);

			// Error for LAst Visit
			EP.NavigateTo(RecoveringURL);
			EP.DrpDwn_SelectIndex(TabName + "StudyCompletionCA_DD", 3);// 1
			EP.DrpDwn_SelectIndex(TabName + "SubjectHeightCA_DD", 3);// 1
			EP.Edit_SetValue(TabName + "MeasurementCompleted_EB", 60);
			EP.SelectDate(TabName + "ContinuedTreatmentStopDateAT_IM", EP
					.Date_AddDays(EP.TranslateDate(Date, "dd MMM yyyy", true),
							-1, Calendar.DATE));
			EP.SelectDate(TabName + "LastStudyDayCalendarAT_TT", EP
					.Date_AddDays(EP.TranslateDate(Date, "dd MMM yyyy", true),
							-1, Calendar.DATE));
			EP.SelectDate(TabName + "DateOFMeasurementCalendarAT_TT", EP
					.Date_AddDays(EP.TestData("ECOSPatientDOB"), 2,
							Calendar.MONTH));
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("TSDLessThanCurrentGH");
			EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
					EP.GetText(TabName + "IncorrecrFormatErrorES_DT"));
			EP.CompareText("DateAfterLastVisit",
					EP.LangData("DateAfterLastVisit"), GetErrorMessage(1));
			EP.CompareText("DateAfterLastVisit",
					EP.LangData("DateAfterLastVisit"), GetErrorMessage(2));
			EP.CompareText("Ignoreandsave", EP.LangData("IgnoreAndSave"),
					EP.GetText(TabName + "Save_BN"));
			EP.ClearStaticScreenshot();
			EP.Btn_Click(TabName + "Cancel_BN");
			EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");

			// Success message for Completion
			EP.DrpDwn_SelectIndex(TabName + "StudyCompletionCA_DD", 3);// 1
			EP.DrpDwn_SelectIndex(TabName + "SubjectHeightCA_DD", 3);// 1
			EP.Edit_SetValue(TabName + "MeasurementCompleted_EB", 60);
			EP.SelectDate(TabName + "ContinuedTreatmentStopDateCA_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate(TabName + "LastStudyDayCalendarAT_TT",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate(TabName + "DateOFMeasurementCalendarAT_TT",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.VerifyMonth(TabName + "TreatmentStopDateAT_EB");
			EP.VerifyMonth(TabName + "DateOfMeasurementAT_EB");
			EP.VerifyMonth(TabName + "LastStudyDayAT_EB");
			EP.Btn_Click(TabName + "Save_BN");
			ECOS.VerifySuccessMessage(EP.LangData("CompletionOrTermination"));

			// To check error messgage for male patient - FinalHeightDifference
			EP.SearchForPatient(EP.TestData("ECOSPatient"));
			EP.Rbtn_SetOption("#MyPatients_FirstPatient_RB", "ON");
			EP.Btn_Click("#MyPatients_BaselineRecord_BN");
			EP.Lnk_Click("#NavigationTab_RecordOverview_LK");
			EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");
			EP.DrpDwn_SelectIndex(TabName + "StatusOfStudy_DD", 1);
			EP.DrpDwn_SelectIndex(TabName + "StudyCompletionCA_DD", 1);
			EP.DrpDwn_SelectIndex(TabName + "SubjectHeightCA_DD", 1);
			EP.Edit_SetValue(TabName + "MeasurementCompleted_EB", "100");
			EP.Wait(2);
			EP.SelectDate(TabName + "ContinuedTreatmentStopDateCA_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate(TabName + "LastStudyDayCalendarAT_TT",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate(TabName + "DateOFMeasurementCalendarAT_TT",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("FinalHeightDifference");
			EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
					EP.GetText(TabName + "ValueOutOfRangeErrorES_DT"));
			EP.CompareText("FinalHeightDifference",
					EP.LangData("FinalHeightDifference"), GetErrorMessage(1));
			EP.CompareText("Ignoreandsave", EP.LangData("IgnoreAndSave"),
					EP.GetText(TabName + "Save_BN"));
			EP.ClearStaticScreenshot();

			EP.Btn_Click(TabName + "Cancel_BN");
			EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");

			// To check error messgage for male patient -
			// FinalHeightDifference1CM

			EP.DrpDwn_SelectIndex(TabName + "StatusOfStudy_DD", 1);
			EP.DrpDwn_SelectIndex(TabName + "StudyCompletionCA_DD", 1);
			EP.DrpDwn_SelectIndex(TabName + "SubjectHeightCA_DD", 2);
			EP.Edit_SetValue(TabName + "MeasurementCompleted_EB", "100");
			EP.Wait(2);
			EP.SelectDate(TabName + "ContinuedTreatmentStopDateCA_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate(TabName + "LastStudyDayCalendarAT_TT",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate(TabName + "DateOFMeasurementCalendarAT_TT",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("FinalHeightDifference1CM");
			EP.CompareText("IncorrecrFormatError_DT",
					EP.LangData("VerifyIncorrectFormats"),
					EP.GetText(TabName + "IncorrecrFormatErrorES_DT"));
			EP.CompareText("AccordanceWithAnother",
					EP.LangData("AccordanceWithAnother"), GetErrorMessage(1));
			EP.CompareText("FinalHeightDifference1CM",
					EP.LangData("FinalHeightDifference1CM"), GetErrorMessage(2));
			EP.CompareText("Save", EP.LangData("Save"),
					EP.GetText(TabName + "Save_BN"));
			EP.ClearStaticScreenshot();

			// Delete the Visit for John Doe
			EP.Btn_Click(TabName + "Cancel_BN");
			EP.Lnk_Click("#NavigationTab_VisitsRecord_LK");
			EP.WbTbl_SortColumn("#AddVisit_VisitDate_TE", 2, "DESC");
			EP.WbTbl_SelectRbtn("#AddVisit_VisitDate_TE", 2);
			EP.Btn_Click("#AddVisit_Continue_BN");
			if (EP.ObjectExists("#General_Delete_IM")) {
				DeleteRecord("#General_Delete_IM", false);
			}

			// To check error messgage for male patient - AgeMustBeGreaterThan15

			EP.Lnk_Click("#NavigationTab_RecordOverview_LK");
			EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");
			EP.DrpDwn_SelectIndex(TabName + "StatusOfStudy_DD", 1);
			EP.DrpDwn_SelectIndex(TabName + "StudyCompletionCA_DD", 1);
			EP.DrpDwn_SelectIndex(TabName + "SubjectHeightCA_DD", 1);
			EP.Edit_SetValue(TabName + "MeasurementCompleted_EB", "100");
			EP.Wait(2);
			EP.SelectDate(TabName + "ContinuedTreatmentStopDateCA_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate(TabName + "LastStudyDayCalendarAT_TT",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate(TabName + "DateOFMeasurementCalendarAT_TT",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("AgeMustBeGreaterThan15Error");
			EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
					EP.GetText(TabName + "ValueOutOfRangeErrorES_DT"));
			EP.CompareText("PatientHeightErrMsg_DT",
					EP.LangData("AgeMustBeGreaterThan15"), GetErrorMessage(1));
			EP.CompareText("Ignoreandsave", EP.LangData("IgnoreAndSave"),
					EP.GetText(TabName + "Save_BN"));
			EP.ClearStaticScreenshot();

			// ****************DIS CONTINUED
			// SECTION*********************************
			if (EP.ObjectExists("#NavigationTab_MyPatientsList_LK"))
				;
			EP.Lnk_Click("#NavigationTab_MyPatientsList_LK");
			EP.SearchForPatient(EP.TestData("PatientName") + " 04");
			EP.Rbtn_SetOption("#MyPatients_FirstPatient_RB", "ON");
			EP.Btn_Click("#MyPatients_BaselineRecord_BN");
			EP.Lnk_Click("#NavigationTab_RecordOverview_LK");
			EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");
			EP.DrpDwn_SelectIndex(TabName + "StatusOfStudy_DD", 2);
			EP.TakeStaticScreenshot("StudyDiscontinued");
			EP.VerifyText(TabName + "PrematureStudyDiscontinuationReasonAT_ST");
			EP.CompareDynamicDropDownValues(TabName
					+ "DiscontinuationReasonAT_DD",
					EP.ORData(TabName + "DiscontinuationReasonAT_DD"), false);
			EP.DrpDwn_SelectIndex(TabName + "DiscontinuationReasonAT_DD", 2);// To
																				// make
																				// visible
																				// Remove
																				// data
																				// from
																				// Study?

			EP.VerifyText(TabName + "RemoveDataFromStudyAT_ST");
			EP.VerifyText(TabName + "RemoveDataYesAT_ST");
			EP.VerifyText(TabName + "RemoveDataNoAT_ST");

			EP.VerifyText(TabName + "InitiatorOfWithdrawalAT_ST");
			EP.CompareDynamicDropDownValues(TabName
					+ "InitiatorOfWithdrawalAT_DD",
					EP.ORData(TabName + "InitiatorOfWithdrawalAT_DD"), false);
			EP.VerifyText(TabName + "PatientHeightAtPrematureAT_ST");
			EP.CompareDynamicDropDownValues(TabName
					+ "PatientHeightAtDiscontinuationAT_DD",
					EP.ORData(TabName + "PatientHeightAtDiscontinuation_DD"),
					false);
			EP.VerifyText(TabName + "DisContLastStudyDayAT_ST");
			EP.VerifyToolTip(TabName + "DiscontinuedLastStudyDayCalendarAT_TT");
			EP.VerifyToolTip(TabName + "DiscontinuedTreatmentStopDateCA_IM");
			EP.VerifyText(TabName + "DiscontinuedMeasurementAT_ST");
			EP.VerifyText(TabName + "DiscontinuedTreatmentStopDateAT_ST");
			EP.VerifyTextContained(TabName + "MeasurementCmAT_ST");
			EP.ClearStaticScreenshot();

			// Mandatory fields of discontinued section
			EP.DrpDwn_SelectIndex(TabName + "StatusOfStudy_DD", 2);
			EP.DrpDwn_SelectIndex(TabName + "DiscontinuationReasonAT_DD", 0);
			EP.DrpDwn_SelectIndex(TabName + "InitiatorOfWithdrawalAT_DD", 0);
			EP.Edit_SetValueByJavaScript(TabName
					+ "DiscontinuedLastStudyDayAT_EB", "");
			EP.Edit_SetValueByJavaScript(TabName
					+ "DiscontinuedTreatmentStopDateAT_EB", "");
			EP.Edit_ClearValue(TabName + "Measurement_EB");
			EP.DrpDwn_SelectIndex(TabName
					+ "PatientHeightAtDiscontinuationAT_DD", 0);
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("Mandatory");
			EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
					EP.GetText(TabName + "ValueOutOfRangeErrorIT_DT"));
			EP.CompareText("PrematureStudyDiscontinuationReasonMandatory",
					EP.LangData("FieldIsMandatory"), GetErrorMessage(1));
			EP.CompareText("InitiatorofwithdrawalMandatory",
					EP.LangData("FieldIsMandatory"), GetErrorMessage(2));
			EP.CompareText("StudyDiscontinuationMandatory",
					EP.LangData("FieldIsMandatory"), GetErrorMessage(3));
			EP.CompareText("LastStudyMandatory",
					EP.LangData("FieldIsMandatory"), GetErrorMessage(4));
			EP.CompareText("MeasurementMandatory",
					EP.LangData("FieldIsMandatory"), GetErrorMessage(5));
			EP.CompareText("Ignoreandsave", EP.LangData("IgnoreAndSave"),
					EP.GetText(TabName + "Save_BN"));
			EP.ClearStaticScreenshot();
			EP.Btn_Click(TabName + "Cancel_BN");
			EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");

			// Measurement range value and Date related error messages/Treatment
			// Discontinuation Details madatory field
			EP.DrpDwn_SelectIndex(TabName + "StatusOfStudy_DD", 2);
			EP.DrpDwn_SelectIndex(TabName + "DiscontinuationReasonAT_DD", 1);
			EP.DrpDwn_SelectIndex(TabName + "InitiatorOfWithdrawalAT_DD", 1);
			EP.DrpDwn_SelectIndex(TabName
					+ "PatientHeightAtDiscontinuationAT_DD", 3);
			EP.DrpDwn_SelectIndex(TabName
					+ "TreatmentDiscontinuationDetailsAT_DD", 0);
			EP.SelectDate(TabName + "DiscontinuedTreatmentStopDateCA_IM",
					EP.TestData("ECOSPatientDOB"));
			EP.SelectDate(TabName + "DiscontinuedLastStudyDayCalendarAT_TT",
					EP.TestData("ECOSPatientDOB"));
			EP.Edit_SetValue(TabName + "Measurement_EB", MinValue);
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("OutOfRangeError");
			EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
					EP.GetText(TabName + "ValueOutOfRangeErrorIT_DT"));
			EP.CompareText("TreatmentDiscontinuationDetailsMandatory",
					EP.LangData("FieldIsMandatory"), GetErrorMessage(1));
			EP.CompareText("LastStudyDateError",
					EP.LangData("LastStudyDateError"), GetErrorMessage(2));
			EP.CompareText("TreatmentStopDate ",
					EP.LangData("LastStudyDateError"), GetErrorMessage(3));
			EP.VerifyRegExpression("Height", EP.LangData("RangeValue"),
					GetErrorMessage(4), "\\d{2,3}");
			EP.CompareText("Ignoreandsave", EP.LangData("IgnoreAndSave"),
					EP.GetText(TabName + "Save_BN"));
			EP.ClearStaticScreenshot();
			EP.Btn_Click(TabName + "Cancel_BN");

			// Validation of "Other, Specify" error messages

			EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");
			EP.DrpDwn_SelectIndex(TabName + "StatusOfStudy_DD", 2);
			EP.DrpDwn_SelectIndex(TabName + "DiscontinuationReasonAT_DD", 6);
			EP.Edit_ClearValue(TabName + "DiscontinuationReasonAT_EB");
			EP.DrpDwn_SelectIndex(TabName + "InitiatorOfWithdrawalAT_DD", 5);
			EP.Edit_ClearValue(TabName + "InitiatorOfWithdrawalAT_EB");
			EP.SelectDate(TabName + "DiscontinuedTreatmentStopDateCA_IM", EP
					.Date_AddDays(EP.TestData("ECOSPatientDOB"), 2,
							Calendar.MONTH));
			EP.SelectDate(TabName + "DiscontinuedLastStudyDayCalendarAT_TT", EP
					.Date_AddDays(EP.TestData("ECOSPatientDOB"), 2,
							Calendar.MONTH));
			EP.DrpDwn_SelectIndex(TabName
					+ "PatientHeightAtDiscontinuationAT_DD", 3);
			EP.Edit_SetValue(TabName + "Measurement_EB", 56);
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("OutOfRangeError");
			EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
					EP.GetText(TabName + "ValueOutOfRangeErrorIT_DT"));
			EP.CompareText("FieldIsMandatory", EP.LangData("FieldIsMandatory"),
					GetErrorMessage(1));
			EP.CompareText("FieldIsMandatory", EP.LangData("FieldIsMandatory"),
					GetErrorMessage(2));
			EP.CompareText("Ignoreandsave", EP.LangData("IgnoreAndSave"),
					EP.GetText(TabName + "Save_BN"));
			EP.ClearStaticScreenshot();
			EP.Btn_Click(TabName + "Cancel_BN");

			// Incorrect Formats
			EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");
			EP.Edit_ClearValue(TabName + "MeasurementAT_EB");
			EP.Edit_SetValue(TabName + "MeasurementAT_EB", IncorrectFormat);
			EP.DrpDwn_SelectIndex(TabName + "DiscontinuationReasonAT_DD", 5);
			EP.DrpDwn_SelectIndex(TabName
					+ "PatientHeightAtDiscontinuationAT_DD", 3);
			EP.DrpDwn_SelectIndex(TabName + "InitiatorOfWithdrawalAT_DD", 4);
			EP.SelectDate(TabName + "DiscontinuedTreatmentStopDateCA_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate(TabName + "DiscontinuedLastStudyDayCalendarAT_TT",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("MeasurementFormatError");
			EP.VerifyText(TabName + "IncorrecrFormatErrorIT_DT");
			EP.CompareText("IncorrectFormat", EP.LangData("IncorrectFormat"),
					GetErrorMessage(1));
			EP.ClearStaticScreenshot();

			EP.Btn_Click(TabName + "Cancel_BN");
			EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");

			// Error for Treatment Stop Date lesser than treatment Start Date

			EP.Edit_SetValue(TabName + "MeasurementAT_EB", 60);
			EP.DrpDwn_SelectIndex(TabName + "DiscontinuationReasonAT_DD", 5);
			EP.DrpDwn_SelectIndex(TabName
					+ "PatientHeightAtDiscontinuationAT_DD", 3);
			EP.DrpDwn_SelectIndex(TabName + "InitiatorOfWithdrawalAT_DD", 4);
			EP.SelectDate(TabName + "DiscontinuedTreatmentStopDateCA_IM",
					TreatmentStartDate);
			EP.SelectDate(TabName + "DiscontinuedLastStudyDayAT_EB", EP
					.Date_AddDays(EP.TestData("ECOSPatientDOB"), 2,
							Calendar.MONTH));
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("TSDLessThanCurrentGH");
			EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
					EP.GetText(TabName + "ValueOutOfRangeErrorIT_DT"));
			EP.VerifyRegExpression("TSDLessThanCurrentGH",
					EP.LangData("TSDLessThanCurrentGH"), GetErrorMessage(1),
					EP.TranslateDate(TreatmentStartDate, "dd MMM yyyy", false));
			EP.ClearStaticScreenshot();
			EP.Btn_Click(TabName + "Cancel_BN");
			EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");

			// To verify success message for Discontinued
			EP.DrpDwn_SelectIndex(TabName + "DiscontinuationReasonAT_DD",
					EP.RandomValue(2, 5));
			EP.DrpDwn_SelectIndex(TabName + "InitiatorOfWithdrawalAT_DD",
					EP.RandomValue(1, 4));
			EP.Edit_SetValue(TabName + "Measurement_EB", "56");
			EP.SelectDate(TabName + "DiscontinuedTreatmentStopDateCA_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate(TabName + "DiscontinuedLastStudyDayCalendarAT_TT",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.TakeStaticScreenshot("MonthVerification");
			EP.VerifyMonth(TabName + "DiscontinuedTreatmentStopDateAT_EB");
			EP.VerifyMonth(TabName + "DiscontinuedLastStudyDayAT_EB");
			EP.ClearStaticScreenshot();
			EP.Btn_Click(TabName + "Save_BN");
			ECOS.VerifySuccessMessage(EP.LangData("CompletionOrTermination"));
			break;

		case UNITEDKINGDOM:
			// Completed Section

			EP.DrpDwn_SelectIndex(TabName + "StatusOfStudy_DD", 1);
			EP.TakeStaticScreenshot("StudyCompleted");
			EP.VerifyText(TabName + "StudyCompletionAT_ST");
			EP.CompareDropDownValues(TabName + "StudyCompletionAT_DD");
			EP.VerifyText(TabName + "SubjectHeightAtEndOfStudyUK_ST");
			EP.CompareDropDownValues(TabName + "SubjectHeightAT_DD");
			EP.VerifyText(TabName + "DateOfMeasurementAT_ST");
			EP.VerifyToolTip(TabName + "DateOFMeasurementCalendarAT_TT");
			EP.VerifyText(TabName + "MeasurementAT_ST");
			EP.VerifyTextContained(TabName + "MeasurementCmScaleAT_ST");
			EP.VerifyText(TabName + "LastStudyDayAT_ST");
			EP.VerifyToolTip(TabName + "LastStudyDayCalendarAT_TT");
			EP.VerifyText(TabName + "ContinuedTreatmentStopDateAT_ST");
			EP.VerifyToolTip(TabName + "ContinuedTreatmentStopDateAT_IM");
			EP.VerifyPageLayout();

			// Date and Measurement Range Error Message Validations
			EP.DrpDwn_SelectIndex(TabName + "StatusOfStudy_DD", 1);
			EP.DrpDwn_SelectIndex(TabName + "StudyCompletionAT_DD", 2); // Study
																		// Completion
			EP.DrpDwn_SelectIndex(TabName + "SubjectHeightAT_DD", 3);
			EP.Edit_SetValue(TabName + "MeasurementCompletedAT_EB", MinValue);
			EP.SelectDate(TabName + "LastStudyDayCalendarAT_TT",
					EP.TestData("ECOSPatientDOB"));
			EP.SelectDate(TabName + "ContinuedTreatmentStopDateAT_IM",
					EP.TestData("ECOSPatientDOB"));
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("IncorrectFormatError1");
			EP.VerifyText(TabName + "IncorrecrFormatErrorES_DT");// Header error
																	// msg
			EP.CompareText("StudyCompletionErrMsg_DT",
					EP.LangData("AccordanceWithAnother"), GetErrorMessage(1));
			EP.CompareText("PatientHeightErrMsg_DT",
					EP.LangData("AccordanceWithAnother"), GetErrorMessage(2));
			EP.VerifyRegExpression("MothersHeight_RangeError",
					EP.LangData("RangeValue"), GetErrorMessage(3), "\\d{1,3}");
			EP.CompareText("LastStudyDayErrMsg_DT",
					EP.LangData("LastStudyDateError"), GetErrorMessage(4));
			EP.CompareText("TreatmentStopDate",
					EP.LangData("LastStudyDateError"), GetErrorMessage(5));
			EP.ClearStaticScreenshot();

			// Final height is smaller than the latest visit height
			EP.DrpDwn_SelectIndex(TabName + "SubjectHeightAT_DD", 1);
			EP.Edit_ClearValue(TabName + "MeasurementCompleted_EB");
			EP.SelectDate(TabName + "LastStudyDayCalendarAT_TT", EP
					.Date_AddDays(EP.TestData("ECOSPatientDOB"), 2,
							Calendar.MONTH));
			EP.SelectDate(TabName + "DateOFMeasurementCalendarAT_TT", EP
					.Date_AddDays(EP.TestData("ECOSPatientDOB"), 2,
							Calendar.MONTH));
			EP.Edit_SetValue(TabName + "MeasurementCompletedAT_EB", 55);
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("IncorrectFormatError2");
			EP.CompareText("IncorrecrFormatError_DT",
					EP.LangData("VerifyIncorrectFormats"),
					EP.GetText(TabName + "IncorrecrFormatErrorES_DT"));
			EP.CompareText("PatientHeightErrMsg_DT",
					EP.LangData("FinalHeightSmaller"), GetErrorMessage(2));
			EP.ClearStaticScreenshot();

			// Patient is female and final height recorded and age should be
			// greater than 14 years old
			EP.DrpDwn_SelectIndex(TabName + "StatusOfStudy_DD", 1);
			EP.DrpDwn_SelectIndex(TabName + "StudyCompletionAT_DD", 1);
			EP.DrpDwn_SelectIndex(TabName + "SubjectHeightAT_DD", 1);
			EP.Edit_SetValue(TabName + "MeasurementCompleted_EB", "100");
			EP.SelectDate(TabName + "DateOFMeasurementCalendarAT_TT", EP
					.Date_AddDays(EP.TestData("ECOSPatientDOB"), 2,
							Calendar.MONTH));
			EP.SelectDate(TabName + "ContinuedTreatmentStopDateAT_IM", EP
					.Date_AddDays(EP.TestData("ECOSPatientDOB"), 2,
							Calendar.MONTH));
			EP.Wait(2);
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("AgeMustBeGreaterThan14");
			EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
					EP.GetText(TabName + "ValueOutOfRangeErrorES_DT"));
			EP.CompareText("PatientHeightErrMsg_DT",
					EP.LangData("AgeMustBeGreaterThan14"), GetErrorMessage(1));
			EP.CompareText("Ignoreandsave", EP.LangData("IgnoreAndSave"),
					EP.GetText(TabName + "Save_BN"));
			EP.ClearStaticScreenshot();

			// Incorrect format for measurement
			EP.DrpDwn_SelectIndex(TabName + "SubjectHeightAT_DD", 2);
			EP.DrpDwn_SelectIndex(TabName + "StudyCompletionAT_DD", 2);
			EP.Edit_SetValue(TabName + "MeasurementCompleted_EB",
					IncorrectFormat);
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("incorrectFormatMeasurement");
			EP.CompareText("IncorrecrFormatError_DT",
					EP.LangData("VerifyIncorrectFormats"),
					EP.GetText(TabName + "IncorrecrFormatErrorES_DT"));
			EP.CompareText("MeasurementErrMsg_DT",
					EP.LangData("IncorrectFormat"), GetErrorMessage(1));
			EP.ClearStaticScreenshot();
			EP.Edit_SetValue(TabName + "MeasurementCompleted_EB", 55);
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("nearfinalSmaller");
			EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
					EP.GetText(TabName + "ValueOutOfRangeErrorES_DT"));
			EP.CompareText("PatientHeightErrMsg_DT",
					EP.LangData("NearFinalSmaller"), GetErrorMessage(1));
			EP.CompareText("Ignoreandsave", EP.LangData("IgnoreAndSave"),
					EP.GetText(TabName + "Save_BN"));
			EP.ClearStaticScreenshot();
			EP.Btn_Click(TabName + "Cancel_BN");
			EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");

			// Error for Treatment Stop Date lesser than treatment Start Date

			TreatmentStartDate = EP.Date_AddDays(EP.Date_AddDays(
					EP.TestData("ECOSPatientDOB"), 2, Calendar.MONTH), -1,
					Calendar.DATE);
			EP.DrpDwn_SelectIndex(TabName + "StudyCompletionAT_DD", 3);// 1
			EP.DrpDwn_SelectIndex(TabName + "SubjectHeightAT_DD", 3);// 1
			EP.Edit_SetValue(TabName + "MeasurementCompleted_EB", 60);
			EP.SelectDate(TabName + "DateOFMeasurementCalendarAT_TT", EP
					.Date_AddDays(EP.TestData("ECOSPatientDOB"), 2,
							Calendar.MONTH));
			EP.SelectDate(TabName + "LastStudyDayCalendarAT_TT", EP
					.Date_AddDays(EP.TestData("ECOSPatientDOB"), 2,
							Calendar.MONTH));
			EP.SelectDate(TabName + "ContinuedTreatmentStopDateAT_IM",
					TreatmentStartDate);
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("TSDLessThanCurrentGH");
			EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
					EP.GetText(TabName + "IncorrecrFormatErrorES_DT"));
			EP.VerifyRegExpression("TSDLessThanCurrentGH",
					EP.LangData("TSDLessThanCurrentGH"), GetErrorMessage(1),
					EP.TranslateDate(TreatmentStartDate, "dd MMM yyyy", false));
			EP.CompareText("Ignoreandsave", EP.LangData("IgnoreAndSave"),
					EP.GetText(TabName + "Save_BN"));
			EP.ClearStaticScreenshot();
			EP.Btn_Click(TabName + "Cancel_BN");
			EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");

			// Success message for Completion
			EP.DrpDwn_SelectIndex(TabName + "StatusOfStudy_DD", 1);
			EP.DrpDwn_SelectIndex(TabName + "StudyCompletionAT_DD", 3);// 1
			EP.DrpDwn_SelectIndex(TabName + "SubjectHeightAT_DD", 3);// 1
			EP.Edit_SetValue(TabName + "MeasurementCompleted_EB", 65);
			EP.SelectDate(TabName + "DateOFMeasurementCalendarAT_TT",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate(TabName + "ContinuedTreatmentStopDateAT_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate(TabName + "LastStudyDayCalendarAT_TT",
					EP.GetTimeStamp("dd MMM yyyy"));

			// EP.SelectDate(TabName+"ContinuedTreatmentStopDateAT_IM",
			// EP.Date_AddDays(EP.TestData("ECOSPatientDOB"), 2,
			// Calendar.MONTH));
			EP.VerifyMonth(TabName + "TreatmentStopDateAT_EB");
			EP.VerifyMonth(TabName + "DateOfMeasurementAT_EB");
			EP.VerifyMonth(TabName + "LastStudyDayAT_EB");
			EP.Btn_Click(TabName + "Save_BN");
			ECOS.VerifySuccessMessage(EP.LangData("CompletionOrTermination"));

			// To check error messgage for male patient-- new error
			EP.SearchForPatient(EP.TestData("ECOSPatient"));
			EP.Rbtn_SetOption("#MyPatients_FirstPatient_RB", "ON");
			EP.Btn_Click("#MyPatients_BaselineRecord_BN");
			EP.Lnk_Click("#NavigationTab_RecordOverview_LK");
			EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");
			EP.DrpDwn_SelectIndex(TabName + "StatusOfStudy_DD", 1);
			EP.DrpDwn_SelectIndex(TabName + "StudyCompletion_DD", 1);
			EP.DrpDwn_SelectIndex(TabName + "SubjectHeight_DD", 1);
			EP.Edit_SetValue(TabName + "MeasurementCompleted_EB", "100");
			EP.Wait(2);
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("AgeMustBeGreaterThan15Error");
			EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
					EP.GetText(TabName + "ValueOutOfRangeErrorES_DT"));
			EP.CompareText("FinalHeightDifference",
					EP.LangData("FinalHeightDifference"), GetErrorMessage(1));
			EP.CompareText("Ignoreandsave", EP.LangData("IgnoreAndSave"),
					EP.GetText(TabName + "Save_BN"));
			EP.ClearStaticScreenshot();

			// delet visit
			EP.Btn_Click(TabName + "Cancel_BN");
			// take vist xpath

			EP.Btn_Click("#NavigationTab_VisitsRecord_LK");
			EP.WbTbl_SortColumn("#AddVisit_VisitDate_TE", 2, "DESC");
			EP.Rbtn_SetOption("#AddVisit_FirstVisit_RB", "ON");// Set
																// prerequisite
																// for Latest
																// first visit
			EP.Btn_Click("#AddVisit_Continue_BN");
			if (EP.ObjectExists("#General_Delete_IM")) {
				DeleteRecord("#General_Delete_IM", false);
			}

			// select record overview button
			EP.Lnk_Click("#NavigationTab_RecordOverview_LK");
			EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");
			// To check error messgage for male patient

			EP.DrpDwn_SelectIndex(TabName + "StatusOfStudy_DD", 1);
			EP.DrpDwn_SelectIndex(TabName + "StudyCompletion_DD", 1);
			EP.DrpDwn_SelectIndex(TabName + "SubjectHeight_DD", 1);
			EP.Edit_SetValue(TabName + "MeasurementCompleted_EB", "100");
			EP.Wait(2);
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("AgeMustBeGreaterThan15Error");
			EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
					EP.GetText(TabName + "ValueOutOfRangeErrorES_DT"));
			EP.CompareText("PatientHeightErrMsg_DT",
					EP.LangData("AgeMustBeGreaterThan15"), GetErrorMessage(1));
			EP.CompareText("Ignoreandsave", EP.LangData("IgnoreAndSave"),
					EP.GetText(TabName + "Save_BN"));
			EP.ClearStaticScreenshot();

			// ****************DIS CONTINUED
			// SECTION*********************************

			if (EP.ObjectExists("#NavigationTab_PatientsTab_LK"))
				;
			EP.Lnk_Click("#NavigationTab_PatientsTab_LK");
			if (EP.ObjectExists("#MyPatients_SearchPatient_EB"))
				EP.SearchForPatient(EP.TestData("PatientName") + " 04");
			EP.Rbtn_SetOption("#MyPatients_FirstPatient_RB", "ON");
			EP.Btn_Click("#MyPatients_BaselineRecord_BN");
			EP.Lnk_Click("#NavigationTab_RecordOverview_LK");
			EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");
			EP.DrpDwn_SelectIndex(TabName + "StatusOfStudy_DD", 2);
			EP.TakeStaticScreenshot("StudyDiscontinued");
			EP.VerifyText(TabName + "PrematureStudyDiscontinuationReasonUK_ST");
			EP.CompareDynamicDropDownValues(TabName
					+ "DiscontinuationReasonUK_DD",
					EP.ORData(TabName + "DiscontinuationReasonUK_DD"), false);
			EP.DrpDwn_SelectIndex(TabName + "DiscontinuationReasonUK_DD", 2);// To
																				// make
																				// visible
																				// Remove
																				// data
																				// from
																				// Study?

			EP.VerifyText(TabName + "RemoveDataFromStudyUK_ST");
			EP.VerifyText(TabName + "InitiatorOfWithdrawalUK_ST");
			EP.CompareDynamicDropDownValues(TabName
					+ "InitiatorOfWithdrawalUK_DD",
					EP.ORData(TabName + "InitiatorOfWithdrawalUK_DD"), false);
			EP.VerifyText(TabName + "PatientHeightAtPrematureUK_ST");
			EP.CompareDynamicDropDownValues(TabName
					+ "PatientHeightAtDiscontinuationAT_DD",
					EP.ORData(TabName + "PatientHeightAtDiscontinuation_DD"),
					false);
			EP.VerifyText(TabName + "DisContLastStudyDayUK_ST");
			EP.VerifyToolTip(TabName + "DiscontinuedLastStudyDayCalendarAT_TT");
			EP.VerifyToolTip(TabName + "DiscontinuedTreatmentStopDateAT_IM");
			EP.VerifyText(TabName + "DiscontinuedMeasurementUK_ST");
			EP.VerifyText(TabName + "DiscontinuedTreatmentStopDateAT_ST");
			EP.VerifyTextContained(TabName + "MeasurementCmAT_ST");
			EP.ClearStaticScreenshot();

			// Mandatory fields of discontinued section
			EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");
			EP.DrpDwn_SelectIndex(TabName + "StatusOfStudy_DD", 2);
			EP.DrpDwn_SelectIndex(TabName + "DiscontinuationReasonUK_DD", 9);
			EP.Edit_ClearValue(TabName + "DiscontinuationReasonAT_EB");
			EP.DrpDwn_SelectIndex(TabName + "InitiatorOfWithdrawalUK_DD", 5);
			EP.Edit_ClearValue(TabName + "InitiatorOfWithdrawalAT_EB");
			EP.SelectDate(TabName + "DiscontinuedLastStudyDayCalendarAT_TT", EP
					.Date_AddDays(EP.TestData("ECOSPatientDOB"), 2,
							Calendar.MONTH));
			EP.SelectDate(TabName + "DiscontinuedTreatmentStopDateAT_IM", EP
					.Date_AddDays(EP.TestData("ECOSPatientDOB"), 2,
							Calendar.MONTH));
			EP.DrpDwn_SelectIndex(TabName
					+ "PatientHeightAtDiscontinuationAT_DD", 3);
			EP.Edit_SetValue(TabName + "Measurement_EB", 56);
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("OutOfRangeError");
			EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
					EP.GetText(TabName + "ValueOutOfRangeErrorES_DT"));
			EP.CompareText("FieldIsMandatory", EP.LangData("FieldIsMandatory"),
					GetErrorMessage(1));
			EP.CompareText("FieldIsMandatory", EP.LangData("FieldIsMandatory"),
					GetErrorMessage(2));
			EP.CompareText("Ignoreandsave", EP.LangData("IgnoreAndSave"),
					EP.GetText(TabName + "Save_BN"));
			EP.ClearStaticScreenshot();
			EP.Btn_Click(TabName + "Cancel_BN");

			// Measurement range value and Date related error messages/Treatment
			// Discontinuation Details madatory field
			EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");
			EP.DrpDwn_SelectIndex(TabName + "StatusOfStudy_DD", 2);
			EP.DrpDwn_SelectIndex(TabName + "DiscontinuationReasonUK_DD", 1);
			EP.DrpDwn_SelectIndex(TabName + "InitiatorOfWithdrawalUK_DD", 1);
			EP.DrpDwn_SelectIndex(TabName
					+ "PatientHeightAtDiscontinuationAT_DD", 3);
			EP.SelectDate(TabName + "DiscontinuedLastStudyDayCalendarAT_TT",
					EP.TestData("ECOSPatientDOB"));
			EP.SelectDate(TabName + "DiscontinuedTreatmentStopDateAT_IM",
					EP.TestData("ECOSPatientDOB"));
			EP.Edit_SetValue(TabName + "Measurement_EB", MinValue);
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("OutOfRangeError");
			EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
					EP.GetText(TabName + "ValueOutOfRangeErrorES_DT"));
			EP.CompareText("LastStudyDateError",
					EP.LangData("LastStudyDateError"), GetErrorMessage(1));
			EP.CompareText("TreatmentStopDate ",
					EP.LangData("LastStudyDateError"), GetErrorMessage(2));
			EP.VerifyRegExpression("Height", EP.LangData("RangeValue"),
					GetErrorMessage(3), "\\d{2,3}");
			EP.CompareText("Ignoreandsave", EP.LangData("IgnoreAndSave"),
					EP.GetText(TabName + "Save_BN"));
			EP.ClearStaticScreenshot();
			EP.Btn_Click(TabName + "Cancel_BN");

			// Incorrect Formats
			EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");
			EP.Edit_ClearValue(TabName + "MeasurementAT_EB");
			EP.Edit_SetValue(TabName + "MeasurementAT_EB", IncorrectFormat);
			EP.DrpDwn_SelectIndex(TabName + "DiscontinuationReasonUK_DD", 5);
			EP.DrpDwn_SelectIndex(TabName
					+ "PatientHeightAtDiscontinuationAT_DD", 3);
			EP.DrpDwn_SelectIndex(TabName + "InitiatorOfWithdrawalUK_DD", 4);
			EP.SelectDate(TabName + "DiscontinuedLastStudyDayCalendarAT_TT",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate(TabName + "DiscontinuedTreatmentStopDateAT_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("MeasurementFormatError");
			EP.VerifyText(TabName + "IncorrecrFormatErrorES_DT");
			EP.CompareText("IncorrectFormat", EP.LangData("IncorrectFormat"),
					GetErrorMessage(1));
			EP.ClearStaticScreenshot();

			EP.Btn_Click(TabName + "Cancel_BN");
			EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");

			// Error for Treatment Stop Date lesser than treatment Start Date

			EP.Edit_SetValue(TabName + "MeasurementAT_EB", 60);
			EP.DrpDwn_SelectIndex(TabName + "DiscontinuationReasonUK_DD", 5);
			EP.DrpDwn_SelectIndex(TabName
					+ "PatientHeightAtDiscontinuationAT_DD", 3);
			EP.DrpDwn_SelectIndex(TabName + "InitiatorOfWithdrawalUK_DD", 4);
			EP.SelectDate(TabName + "DiscontinuedLastStudyDayAT_EB", EP
					.Date_AddDays(EP.TestData("ECOSPatientDOB"), 2,
							Calendar.MONTH));
			EP.SelectDate(TabName + "DiscontinuedTreatmentStopDateAT_IM",
					TreatmentStartDate);
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("TSDLessThanCurrentGH");
			EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
					EP.GetText(TabName + "ValueOutOfRangeErrorES_DT"));
			EP.VerifyRegExpression("TSDLessThanCurrentGH",
					EP.LangData("TSDLessThanCurrentGH"), GetErrorMessage(1),
					EP.TranslateDate(TreatmentStartDate, "dd MMM yyyy", false));
			EP.CompareText("Ignoreandsave", EP.LangData("IgnoreAndSave"),
					EP.GetText(TabName + "Save_BN"));
			EP.ClearStaticScreenshot();
			EP.Btn_Click(TabName + "Cancel_BN");
			EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");

			// To verify success message for Discontinued
			EP.DrpDwn_SelectIndex(TabName + "DiscontinuationReasonUK_DD",
					EP.RandomValue(2, 5));
			EP.DrpDwn_SelectIndex(TabName + "InitiatorOfWithdrawalUK_DD",
					EP.RandomValue(1, 4));
			EP.Edit_SetValue(TabName + "Measurement_EB", "56");
			EP.SelectDate(TabName + "DiscontinuedLastStudyDayCalendarAT_TT",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate(TabName + "DiscontinuedTreatmentStopDateAT_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.TakeStaticScreenshot("MonthVerification");
			EP.VerifyMonth(TabName + "DiscontinuedTreatmentStopDateAT_EB");
			EP.VerifyMonth(TabName + "DiscontinuedLastStudyDayAT_EB");
			EP.ClearStaticScreenshot();
			EP.Btn_Click(TabName + "Save_BN");
			ECOS.VerifySuccessMessage(EP.LangData("CompletionOrTermination"));

			break;

		/*
		 * case AUSTRIA:
		 * 
		 * //Completed Section EP.DrpDwn_SelectIndex(TabName+"StatusOfStudy_DD",
		 * 1); EP.TakeStaticScreenshot("StudyCompleted");
		 * EP.VerifyText(TabName+"StudyCompletionAT_ST");
		 * EP.CompareDropDownValues(TabName+"StudyCompletionAT_DD");
		 * EP.CompareDropDownValues(TabName+"SubjectHeightAT_DD");
		 * EP.VerifyText(TabName+"DateOfMeasurementAT_ST");
		 * EP.VerifyToolTip(TabName+"DateOFMeasurementCalendarAT_TT");
		 * EP.VerifyText(TabName+"MeasurementAT_ST");
		 * EP.VerifyTextContained(TabName+"MeasurementCmScaleAT_ST");
		 * EP.VerifyText(TabName+"LastStudyDayAT_ST");
		 * EP.VerifyToolTip(TabName+"LastStudyDayCalendarAT_TT");
		 * EP.VerifyText(TabName+"ContinuedTreatmentStopDateAT_ST");
		 * EP.VerifyToolTip(TabName+"ContinuedTreatmentStopDateAT_IM");
		 * EP.VerifyPageLayout();
		 * 
		 * //Dynamic Contents with Status = Completed
		 * EP.DrpDwn_SelectIndex(TabName+"StatusOfStudy_DD", 1);
		 * EP.DrpDwn_SelectIndex(TabName+"StudyCompletionAT_DD",3); //Study
		 * Completion EP.DrpDwn_SelectIndex(TabName+"SubjectHeightAT_DD", 3);
		 * 
		 * //Verifying Mandatory fields
		 * EP.Edit_SetValueByJavaScript(TabName+"LastStudyDayAT_EB", "");
		 * EP.Edit_ClearValue(TabName+"MeasurementCompletedAT_EB");
		 * EP.Btn_Click(TabName+"Save_BN");
		 * EP.TakeStaticScreenshot("Mandatory");
		 * EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
		 * EP.GetText(TabName+"ValueOutOfRangeError_DT"));
		 * EP.CompareText("Measurement",EP.LangData("FieldIsMandatory"),
		 * GetErrorMessage(1));
		 * EP.CompareText("LastStudyDate",EP.LangData("FieldIsMandatory"),
		 * GetErrorMessage(2)); EP.ClearStaticScreenshot(); //change navigation
		 * when defect is fixed
		 * if(EP.ObjectExists("#MyPatients_SearchPatient_BN"))
		 * EP.NavigateTo(RecoveringURL); else {
		 * EP.Btn_Click(TabName+"Cancel_BN");
		 * EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN"); } //Date
		 * and Measurement Range Error Message Validations
		 * EP.DrpDwn_SelectIndex(TabName+"StudyCompletionAT_DD",2); //Study
		 * Completion EP.DrpDwn_SelectIndex(TabName+"SubjectHeightAT_DD", 3);
		 * EP.Edit_SetValue(TabName+"MeasurementCompletedAT_EB", MinValue);
		 * EP.SelectDate
		 * (TabName+"LastStudyDayCalendarAT_TT",EP.TestData("ECOSPatientDOB"));
		 * EP.SelectDate(TabName+"ContinuedTreatmentStopDateAT_IM",EP.TestData(
		 * "ECOSPatientDOB")); EP.Btn_Click(TabName+"Save_BN");
		 * EP.TakeStaticScreenshot("IncorrectFormatError1");
		 * EP.VerifyText(TabName+"IncorrecrFormatError_DT");//Header error msg
		 * EP.CompareText("StudyCompletionErrMsg_DT",
		 * EP.LangData("AccordanceWithAnother"), GetErrorMessage(1));
		 * EP.CompareText("PatientHeightErrMsg_DT",
		 * EP.LangData("AccordanceWithAnother"),GetErrorMessage(2));
		 * EP.VerifyRegExpression("MothersHeight_RangeError",
		 * EP.LangData("RangeValue"), GetErrorMessage(3), "\\d{1,3}");
		 * EP.CompareText("LastStudyDayErrMsg_DT",
		 * EP.LangData("LastStudyDateError"),GetErrorMessage(4));
		 * EP.CompareText("TreatmentStopDate",
		 * EP.LangData("LastStudyDateError"),GetErrorMessage(5));
		 * EP.ClearStaticScreenshot();
		 * 
		 * //Final height is smaller than the latest visit height
		 * EP.DrpDwn_SelectIndex(TabName+"SubjectHeightAT_DD", 1);
		 * EP.Edit_ClearValue(TabName+"MeasurementCompleted_EB");
		 * EP.SelectDate(TabName
		 * +"LastStudyDayCalendarAT_TT",EP.Date_AddDays(EP.TestData
		 * ("ECOSPatientDOB"), 2, Calendar.MONTH));
		 * EP.SelectDate(TabName+"DateOFMeasurementCalendarAT_TT"
		 * ,EP.Date_AddDays(EP.TestData("ECOSPatientDOB"), 2, Calendar.MONTH));
		 * EP.Edit_SetValue(TabName+"MeasurementCompletedAT_EB", 55);
		 * EP.Btn_Click(TabName+"Save_BN");
		 * EP.TakeStaticScreenshot("IncorrectFormatError2");
		 * EP.CompareText("IncorrecrFormatError_DT",
		 * EP.LangData("VerifyIncorrectFormats"),
		 * EP.GetText(TabName+"IncorrecrFormatError_DT"));
		 * EP.CompareText("PatientHeightErrMsg_DT",
		 * EP.LangData("FinalHeightSmaller"),GetErrorMessage(2));
		 * EP.ClearStaticScreenshot();
		 * 
		 * //Patient is female and final height recorded and age should be
		 * greater than 14 years old
		 * EP.DrpDwn_SelectIndex(TabName+"StatusOfStudy_DD",1);
		 * EP.DrpDwn_SelectIndex(TabName+"StudyCompletionAT_DD",1);
		 * EP.DrpDwn_SelectIndex(TabName+"SubjectHeightAT_DD", 1);
		 * EP.Edit_SetValue(TabName+"MeasurementCompleted_EB", "100");
		 * EP.SelectDate
		 * (TabName+"DateOFMeasurementCalendarAT_TT",EP.Date_AddDays
		 * (EP.TestData("ECOSPatientDOB"), 2, Calendar.MONTH));
		 * EP.SelectDate(TabName
		 * +"ContinuedTreatmentStopDateAT_IM",EP.Date_AddDays
		 * (EP.TestData("ECOSPatientDOB"), 2, Calendar.MONTH)); EP.Wait(2);
		 * EP.Btn_Click(TabName+"Save_BN");
		 * EP.TakeStaticScreenshot("AgeMustBeGreaterThan14");
		 * EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
		 * EP.GetText(TabName+"ValueOutOfRangeError_DT"));
		 * EP.CompareText("PatientHeightErrMsg_DT",
		 * EP.LangData("AgeMustBeGreaterThan14"),
		 * EP.GetText(TabName+"PatientHeightErrMsgAT_DT"));
		 * EP.ClearStaticScreenshot();
		 * 
		 * //Incorrect format for measurement EP.Btn_Click(TabName+"Cancel_BN");
		 * EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");
		 * EP.DrpDwn_SelectIndex(TabName+"SubjectHeightAT_DD", 2);
		 * EP.DrpDwn_SelectIndex(TabName+"StudyCompletionAT_DD",2);
		 * EP.Edit_SetValue(TabName+"MeasurementCompleted_EB", IncorrectFormat);
		 * EP.Btn_Click(TabName+"Save_BN");
		 * EP.TakeStaticScreenshot("incorrectFormatMeasurement");
		 * EP.CompareText("IncorrecrFormatError_DT",
		 * EP.LangData("VerifyIncorrectFormats"),
		 * EP.GetText(TabName+"IncorrecrFormatError_DT"));
		 * EP.CompareText("MeasurementErrMsg_DT",
		 * EP.LangData("IncorrectFormat"), GetErrorMessage(1));
		 * EP.ClearStaticScreenshot();
		 * EP.Edit_SetValue(TabName+"MeasurementCompleted_EB", 55);
		 * EP.Btn_Click(TabName+"Save_BN");
		 * EP.TakeStaticScreenshot("nearfinalSmaller");
		 * EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
		 * EP.GetText(TabName+"ValueOutOfRangeError_DT"));
		 * EP.CompareText("PatientHeightErrMsg_DT",
		 * EP.LangData("NearFinalSmaller"), GetErrorMessage(1));
		 * EP.CompareText("Ignoreandsave",EP.LangData("IgnoreAndSave"),
		 * EP.GetText(TabName+"Save_BN")); EP.ClearStaticScreenshot();
		 * EP.NavigateTo(RecoveringURL);
		 * 
		 * //Error for Treatment Stop Date lesser than treatment Start Date
		 * 
		 * TreatmentStartDate=EP.Date_AddDays(EP.Date_AddDays(EP.TestData(
		 * "ECOSPatientDOB"), 2, Calendar.MONTH),-1, Calendar.DATE);
		 * 
		 * EP.DrpDwn_SelectIndex(TabName+"StudyCompletionAT_DD",3);//1
		 * EP.DrpDwn_SelectIndex(TabName+"SubjectHeightAT_DD", 3);//1
		 * EP.Edit_SetValue(TabName+"MeasurementCompleted_EB", 60);
		 * EP.SelectDate(TabName+"DateOFMeasurementCalendarAT_TT",
		 * EP.Date_AddDays(EP.TestData("ECOSPatientDOB"), 2, Calendar.MONTH));
		 * EP.SelectDate(TabName+"LastStudyDayCalendarAT_TT",
		 * EP.Date_AddDays(EP.TestData("ECOSPatientDOB"), 2, Calendar.MONTH));
		 * EP
		 * .SelectDate(TabName+"ContinuedTreatmentStopDateAT_IM",TreatmentStartDate
		 * ); EP.Btn_Click(TabName+"Save_BN");
		 * EP.TakeStaticScreenshot("TSDLessThanCurrentGH");
		 * EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
		 * EP.GetText(TabName+"IncorrecrFormatError_DT"));
		 * EP.VerifyRegExpression("TSDLessThanCurrentGH",
		 * EP.LangData("TSDLessThanCurrentGH"
		 * ),GetErrorMessage(1),EP.TranslateDate(TreatmentStartDate,
		 * "dd MMM yyyy", false)); EP.ClearStaticScreenshot();
		 * EP.Btn_Click(TabName+"Cancel_BN");
		 * EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");
		 * 
		 * EP.NavigateTo(RecoveringURL); //Success message for Completion
		 * EP.DrpDwn_SelectIndex(TabName+"StudyCompletionAT_DD",3);//1
		 * EP.DrpDwn_SelectIndex(TabName+"SubjectHeightAT_DD", 3);//1
		 * EP.Edit_SetValue(TabName+"MeasurementCompleted_EB", 60);
		 * EP.SelectDate(TabName+"DateOFMeasurementCalendarAT_TT",
		 * EP.GetTimeStamp("dd MMM yyyy"));
		 * EP.SelectDate(TabName+"ContinuedTreatmentStopDateAT_IM"
		 * ,EP.GetTimeStamp("dd MMM yyyy"));
		 * EP.VerifyMonth(TabName+"TreatmentStopDateAT_EB");
		 * EP.VerifyMonth(TabName+"DateOfMeasurementAT_EB");
		 * EP.VerifyMonth(TabName+"LastStudyDayAT_EB");
		 * EP.Btn_Click(TabName+"Save_BN");
		 * ECOS.VerifySuccessMessage(EP.LangData("CompletionOrTermination"));
		 * 
		 * //To check error messgage for male patient
		 * EP.SearchForPatient(EP.TestData("ECOSPatient"));
		 * EP.Rbtn_SetOption("#MyPatients_FirstPatient_RB", "ON");
		 * EP.Btn_Click("#MyPatients_BaselineRecord_BN");
		 * EP.Lnk_Click("#NavigationTab_RecordOverview_LK");
		 * EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");
		 * EP.DrpDwn_SelectIndex(TabName+"StatusOfStudy_DD", 1);
		 * EP.DrpDwn_SelectIndex(TabName+"StudyCompletion_DD",1);
		 * EP.DrpDwn_SelectIndex(TabName+"SubjectHeight_DD", 1);
		 * EP.Edit_SetValue(TabName+"MeasurementCompleted_EB", "100");
		 * EP.Wait(2); EP.Btn_Click(TabName+"Save_BN");
		 * EP.TakeStaticScreenshot("AgeMustBeGreaterThan15Error");
		 * EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
		 * EP.GetText(TabName+"IncorrecrFormatError_DT"));
		 * EP.CompareText("PatientHeightErrMsg_DT",
		 * EP.LangData("AgeMustBeGreaterThan15"), GetErrorMessage(1));
		 * EP.ClearStaticScreenshot();
		 * 
		 * //****************DIS CONTINUED
		 * SECTION*********************************
		 * if(EP.ObjectExists("#NavigationTab_MyPatientsList_LK"));
		 * EP.Lnk_Click("#NavigationTab_MyPatientsList_LK");
		 * EP.SearchForPatient(EP.TestData("PatientName") + " 04");
		 * EP.Rbtn_SetOption("#MyPatients_FirstPatient_RB", "ON");
		 * EP.Btn_Click("#MyPatients_BaselineRecord_BN");
		 * EP.Lnk_Click("#NavigationTab_RecordOverview_LK");
		 * EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");
		 * EP.DrpDwn_SelectIndex(TabName+"StatusOfStudy_DD", 2);
		 * EP.TakeStaticScreenshot("StudyDiscontinued");
		 * EP.VerifyText(TabName+"PrematureStudyDiscontinuationReasonAT_ST");
		 * EP.CompareDynamicDropDownValues(TabName+"DiscontinuationReasonAT_DD",
		 * EP.ORData(TabName+"DiscontinuationReasonAT_DD"), false);
		 * EP.DrpDwn_SelectIndex(TabName+"DiscontinuationReasonAT_DD", 2);//To
		 * make visible Remove data from Study?
		 * 
		 * EP.VerifyText(TabName+"RemoveDataFromStudyAT_ST");
		 * EP.VerifyText(TabName+"RemoveDataYesAT_ST");
		 * EP.VerifyText(TabName+"RemoveDataNoAT_ST");
		 * 
		 * EP.VerifyText(TabName+"InitiatorOfWithdrawalAT_ST");
		 * EP.CompareDynamicDropDownValues(TabName+"InitiatorOfWithdrawalAT_DD",
		 * EP.ORData(TabName+"InitiatorOfWithdrawalAT_DD"), false);
		 * EP.VerifyText(TabName+"PatientHeightAtPrematureAT_ST");
		 * EP.CompareDynamicDropDownValues
		 * (TabName+"PatientHeightAtDiscontinuationAT_DD",
		 * EP.ORData(TabName+"PatientHeightAtDiscontinuation_DD"), false);
		 * EP.VerifyText(TabName+"DisContLastStudyDayAT_ST");
		 * EP.VerifyToolTip(TabName+"DiscontinuedLastStudyDayCalendarAT_TT");
		 * EP.VerifyToolTip(TabName+"DiscontinuedTreatmentStopDateAT_IM");
		 * EP.VerifyText(TabName+"DiscontinuedMeasurementAT_ST");
		 * EP.VerifyText(TabName+"DiscontinuedTreatmentStopDateAT_ST");
		 * EP.VerifyTextContained(TabName+"MeasurementCmAT_ST");
		 * EP.ClearStaticScreenshot();
		 * 
		 * //Mandatory fields of discontinued section
		 * EP.DrpDwn_SelectIndex(TabName+"StatusOfStudy_DD", 2);
		 * EP.DrpDwn_SelectIndex(TabName+"DiscontinuationReasonAT_DD", 0);
		 * EP.DrpDwn_SelectIndex(TabName+"InitiatorOfWithdrawalAT_DD", 0);
		 * EP.Edit_SetValueByJavaScript(TabName+"DiscontinuedLastStudyDayAT_EB",
		 * "");
		 * EP.Edit_SetValueByJavaScript(TabName+"DiscontinuedTreatmentStopDateAT_EB"
		 * ,""); EP.Edit_ClearValue(TabName+"Measurement_EB");
		 * EP.DrpDwn_SelectIndex
		 * (TabName+"PatientHeightAtDiscontinuationAT_DD",0);
		 * EP.Btn_Click(TabName+"Save_BN");
		 * EP.TakeStaticScreenshot("Mandatory");
		 * EP.CompareText("PrematureStudyDiscontinuationReasonMandatory",
		 * EP.LangData("FieldIsMandatory"), GetErrorMessage(1));
		 * EP.CompareText("InitiatorofwithdrawalMandatory",
		 * EP.LangData("FieldIsMandatory"), GetErrorMessage(2));
		 * EP.CompareText("StudyDiscontinuationMandatory",
		 * EP.LangData("FieldIsMandatory"), GetErrorMessage(3));
		 * EP.CompareText("LastStudyDateMandatory",
		 * EP.LangData("FieldIsMandatory"), GetErrorMessage(4));
		 * EP.CompareText("MeasurementMandatory",
		 * EP.LangData("FieldIsMandatory"), GetErrorMessage(5));
		 * EP.ClearStaticScreenshot(); EP.Btn_Click(TabName+"Cancel_BN");
		 * EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");
		 * 
		 * 
		 * //Measurement range value and Date related error messages/Treatment
		 * Discontinuation Details madatory field
		 * EP.DrpDwn_SelectIndex(TabName+"StatusOfStudy_DD", 2);
		 * EP.DrpDwn_SelectIndex(TabName+"DiscontinuationReasonAT_DD", 1);
		 * EP.DrpDwn_SelectIndex(TabName+"InitiatorOfWithdrawalAT_DD", 1);
		 * EP.DrpDwn_SelectIndex
		 * (TabName+"PatientHeightAtDiscontinuationAT_DD",3);
		 * EP.DrpDwn_SelectIndex
		 * (TabName+"TreatmentDiscontinuationDetailsAT_DD",0);
		 * EP.SelectDate(TabName+"DiscontinuedLastStudyDayCalendarAT_TT",
		 * EP.TestData("ECOSPatientDOB"));
		 * EP.SelectDate(TabName+"DiscontinuedTreatmentStopDateAT_IM",
		 * EP.TestData("ECOSPatientDOB"));
		 * EP.Edit_SetValue(TabName+"Measurement_EB", MinValue);
		 * EP.Btn_Click(TabName+"Save_BN");
		 * EP.TakeStaticScreenshot("OutOfRangeError");
		 * EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
		 * EP.GetText(TabName+"ValueOutOfRangeError_DT"));
		 * EP.CompareText("TreatmentDiscontinuationDetailsMandatory",
		 * EP.LangData("FieldIsMandatory"), GetErrorMessage(1));
		 * EP.CompareText("LastStudyDateError",
		 * EP.LangData("LastStudyDateError"), GetErrorMessage(2));
		 * EP.CompareText("TreatmentStopDate ",
		 * EP.LangData("LastStudyDateError"), GetErrorMessage(3));
		 * EP.VerifyRegExpression("Height",
		 * EP.LangData("RangeValue"),GetErrorMessage(4),"\\d{2,3}");
		 * EP.CompareText("Ignoreandsave",EP.LangData("IgnoreAndSave"),
		 * EP.GetText(TabName+"Save_BN")); EP.ClearStaticScreenshot();
		 * EP.Btn_Click(TabName+"Cancel_BN");
		 * 
		 * //Validation of "Other, Specify" error messages
		 * 
		 * EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");
		 * EP.DrpDwn_SelectIndex(TabName+"StatusOfStudy_DD", 2);
		 * EP.DrpDwn_SelectIndex(TabName+"DiscontinuationReasonAT_DD", 6);
		 * EP.Edit_ClearValue(TabName+"DiscontinuationReasonAT_EB");
		 * EP.DrpDwn_SelectIndex(TabName+"InitiatorOfWithdrawalAT_DD", 5);
		 * EP.Edit_ClearValue(TabName+"InitiatorOfWithdrawalAT_EB");
		 * EP.SelectDate(TabName+"DiscontinuedLastStudyDayCalendarAT_TT",
		 * EP.Date_AddDays(EP.TestData("ECOSPatientDOB"), 2, Calendar.MONTH));
		 * EP.SelectDate(TabName+"DiscontinuedTreatmentStopDateAT_IM",
		 * EP.Date_AddDays(EP.TestData("ECOSPatientDOB"), 2, Calendar.MONTH));
		 * EP
		 * .DrpDwn_SelectIndex(TabName+"PatientHeightAtDiscontinuationAT_DD",3);
		 * EP.Edit_SetValue(TabName+"Measurement_EB", 56);
		 * EP.Btn_Click(TabName+"Save_BN");
		 * EP.TakeStaticScreenshot("OutOfRangeError");
		 * EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
		 * EP.GetText(TabName+"IncorrecrFormatError_DT"));
		 * EP.CompareText("PrematureStudyDiscontinuationReason",
		 * EP.LangData("FieldIsMandatory"), GetErrorMessage(1));
		 * EP.CompareText("InitiatorOfWithdrawal",
		 * EP.LangData("FieldIsMandatory"), GetErrorMessage(2));
		 * EP.CompareText("Ignoreandsave",EP.LangData("IgnoreAndSave"),
		 * EP.GetText(TabName+"Save_BN")); EP.ClearStaticScreenshot();
		 * EP.Btn_Click(TabName+"Cancel_BN");
		 * 
		 * //Incorrect Formats
		 * EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");
		 * EP.Edit_ClearValue(TabName+"MeasurementAT_EB");
		 * EP.Edit_SetValue(TabName+"MeasurementAT_EB", IncorrectFormat);
		 * EP.DrpDwn_SelectIndex(TabName+"DiscontinuationReasonAT_DD", 5);
		 * EP.DrpDwn_SelectIndex
		 * (TabName+"PatientHeightAtDiscontinuationAT_DD",3);
		 * EP.DrpDwn_SelectIndex(TabName+"InitiatorOfWithdrawalAT_DD", 4);
		 * EP.SelectDate(TabName+"DiscontinuedLastStudyDayCalendarAT_TT",
		 * EP.GetTimeStamp("dd MMM yyyy"));
		 * EP.SelectDate(TabName+"DiscontinuedTreatmentStopDateAT_IM"
		 * ,EP.GetTimeStamp("dd MMM yyyy")); EP.Btn_Click(TabName+"Save_BN");
		 * EP.TakeStaticScreenshot("MeasurementFormatError");
		 * EP.VerifyText(TabName+"IncorrecrFormatError_DT");
		 * EP.CompareText("IncorrectFormat", EP.LangData("IncorrectFormat"),
		 * GetErrorMessage(1)); EP.ClearStaticScreenshot();
		 * 
		 * //Error for Treatment Stop Date lesser than treatment Start Date
		 * EP.Btn_Click(TabName+"Cancel_BN");
		 * EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");
		 * EP.Edit_SetValue(TabName+"MeasurementAT_EB", 60);
		 * EP.DrpDwn_SelectIndex(TabName+"DiscontinuationReasonAT_DD", 5);
		 * EP.DrpDwn_SelectIndex
		 * (TabName+"PatientHeightAtDiscontinuationAT_DD",3);
		 * EP.DrpDwn_SelectIndex(TabName+"InitiatorOfWithdrawalAT_DD", 4);
		 * EP.SelectDate(TabName+"DiscontinuedLastStudyDayAT_EB",
		 * EP.Date_AddDays(EP.TestData("ECOSPatientDOB"), 2, Calendar.MONTH));
		 * EP.SelectDate(TabName+"DiscontinuedTreatmentStopDateAT_IM",
		 * TreatmentStartDate); EP.Btn_Click(TabName+"Save_BN");
		 * EP.TakeStaticScreenshot("TSDLessThanCurrentGH");
		 * EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
		 * EP.GetText(TabName+"ValueOutOfRangeError_DT"));
		 * EP.VerifyRegExpression("TSDLessThanCurrentGH",
		 * EP.LangData("TSDLessThanCurrentGH"
		 * ),GetErrorMessage(1),EP.TranslateDate(TreatmentStartDate,
		 * "dd MMM yyyy", false)); EP.ClearStaticScreenshot();
		 * EP.Btn_Click(TabName+"Cancel_BN");
		 * EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");
		 * 
		 * 
		 * 
		 * //To verify success message for Discontinued
		 * EP.DrpDwn_SelectIndex(TabName+"DiscontinuationReasonAT_DD",
		 * EP.RandomValue(2, 5));
		 * EP.DrpDwn_SelectIndex(TabName+"InitiatorOfWithdrawalAT_DD",
		 * EP.RandomValue(1, 4)); EP.Edit_SetValue(TabName+"Measurement_EB",
		 * "56");
		 * EP.SelectDate(TabName+"DiscontinuedLastStudyDayCalendarAT_TT",EP
		 * .GetTimeStamp("dd MMM yyyy"));
		 * EP.SelectDate(TabName+"DiscontinuedTreatmentStopDateAT_IM"
		 * ,EP.GetTimeStamp("dd MMM yyyy"));
		 * EP.TakeStaticScreenshot("MonthVerification");
		 * EP.VerifyMonth(TabName+"DiscontinuedTreatmentStopDateAT_EB");
		 * EP.VerifyMonth(TabName+"DiscontinuedLastStudyDayAT_EB");
		 * EP.ClearStaticScreenshot(); EP.Btn_Click(TabName+"Save_BN");
		 * ECOS.VerifySuccessMessage(EP.LangData("CompletionOrTermination"));
		 * 
		 * break;
		 * 
		 * 
		 * case FRANCE: EP.DrpDwn_SelectIndex(TabName+"StatusOfStudy_DD", 1);
		 * EP.TakeStaticScreenshot("StudyCompleted");
		 * EP.VerifyText(TabName+"StudyCompletionAT_ST");
		 * EP.CompareDropDownValues(TabName+"StudyCompletionAT_DD");
		 * EP.VerifyText(TabName+"PatientHeightAtEndOfStudyAT_ST");
		 * EP.CompareDropDownValues(TabName+"SubjectHeightAT_DD");
		 * EP.VerifyText(TabName+"DateOfMeasurementAT_ST");
		 * EP.VerifyToolTip(TabName+"DateOFMeasurementCalendarAT_TT");
		 * EP.VerifyText(TabName+"MeasurementAT_ST");
		 * EP.VerifyTextContained(TabName+"MeasurementCmScaleAT_ST");
		 * EP.VerifyText(TabName+"LastStudyDayAT_ST");
		 * EP.VerifyToolTip(TabName+"LastStudyDayCalendarAT_TT");
		 * EP.VerifyText(TabName+"ContinuedTreatmentStopDateAT_ST");
		 * EP.VerifyToolTip(TabName+"ContinuedTreatmentStopDateAT_IM");
		 * EP.VerifyPageLayout();
		 * 
		 * //Dynamic Contents with Status = Completed //Verifying the mandatory
		 * fields of Completed EP.DrpDwn_SelectIndex(TabName+"StatusOfStudy_DD",
		 * 1); EP.DrpDwn_SelectIndex(TabName+"StudyCompletionAT_DD",0); //Study
		 * Completion EP.DrpDwn_SelectIndex(TabName+"SubjectHeightAT_DD", 0);
		 * EP.Edit_SetValueByJavaScript(TabName+"DateOfMeasurementAT_EB", "");
		 * EP.Edit_SetValueByJavaScript(TabName+"LastStudyDayAT_EB", "");
		 * EP.Edit_SetValueByJavaScript(TabName+"TreatmentStopDateAT_EB", "");
		 * EP.Edit_SetValue(TabName+"MeasurementCompletedAT_EB", "");
		 * EP.Btn_Click(TabName+"Save_BN");
		 * EP.TakeStaticScreenshot("FieldMandatory");
		 * EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
		 * EP.GetText(TabName+"ValueOutOfRangeError_DT"));
		 * EP.CompareText("StudyCompletion", EP.LangData("FieldIsMandatory"),
		 * GetErrorMessage(1)); EP.CompareText("PatientHeightAtEndOfStudy",
		 * EP.LangData("FieldIsMandatory"), GetErrorMessage(2));
		 * EP.CompareText("DateOfMeasurement", EP.LangData("FieldIsMandatory"),
		 * GetErrorMessage(3)); EP.CompareText("Measurement",
		 * EP.LangData("FieldIsMandatory"), GetErrorMessage(4));
		 * EP.CompareText("LastStudyDay", EP.LangData("FieldIsMandatory"),
		 * GetErrorMessage(5)); EP.CompareText("TreatmentStopDate",
		 * EP.LangData("FieldIsMandatory"), GetErrorMessage(6));
		 * EP.CompareText("Ignoreandsave",EP.LangData("IgnoreAndSave"),
		 * EP.GetText(TabName+"Save_BN")); EP.ClearStaticScreenshot();
		 * EP.Btn_Click(TabName+"Cancel_BN");
		 * EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");
		 * 
		 * 
		 * //Date and Measurement Range Error Message Validations
		 * EP.DrpDwn_SelectIndex(TabName+"StatusOfStudy_DD", 1);
		 * EP.DrpDwn_SelectIndex(TabName+"StudyCompletionAT_DD",2); //Study
		 * Completion EP.DrpDwn_SelectIndex(TabName+"SubjectHeightAT_DD", 3);
		 * EP.Edit_SetValue(TabName+"MeasurementCompletedAT_EB", MinValue);
		 * EP.SelectDate
		 * (TabName+"LastStudyDayCalendarAT_TT",EP.TestData("ECOSPatientDOB"));
		 * EP.SelectDate(TabName+"ContinuedTreatmentStopDateAT_IM",EP.TestData(
		 * "ECOSPatientDOB"));
		 * EP.SelectDate(TabName+"DateOFMeasurementCalendarAT_TT"
		 * ,EP.Date_AddDays(EP.TestData("ECOSPatientDOB"), 2, Calendar.MONTH));
		 * EP.Btn_Click(TabName+"Save_BN");
		 * EP.TakeStaticScreenshot("IncorrectFormatError1");
		 * EP.VerifyText(TabName+"IncorrecrFormatError_DT");//Header error msg
		 * EP.CompareText("StudyCompletionErrMsg_DT",
		 * EP.LangData("AccordanceWithAnother"), GetErrorMessage(1));
		 * EP.CompareText("PatientHeightErrMsg_DT",
		 * EP.LangData("AccordanceWithAnother"),GetErrorMessage(2));
		 * EP.VerifyRegExpression("MothersHeight_RangeError",
		 * EP.LangData("RangeValue"), GetErrorMessage(3), "\\d{1,3}");
		 * EP.CompareText("LastStudyDayErrMsg_DT",
		 * EP.LangData("LastStudyDateError"),GetErrorMessage(4));
		 * EP.CompareText("TreatmentStopDate",
		 * EP.LangData("LastStudyDateError"),GetErrorMessage(5));
		 * EP.ClearStaticScreenshot();
		 * 
		 * //Final height is smaller than the latest visit height
		 * EP.DrpDwn_SelectIndex(TabName+"SubjectHeightAT_DD", 1);
		 * EP.Edit_ClearValue(TabName+"MeasurementCompleted_EB");
		 * EP.SelectDate(TabName
		 * +"LastStudyDayCalendarAT_TT",EP.Date_AddDays(EP.TestData
		 * ("ECOSPatientDOB"), 2, Calendar.MONTH));
		 * EP.SelectDate(TabName+"DateOFMeasurementCalendarAT_TT"
		 * ,EP.Date_AddDays(EP.TestData("ECOSPatientDOB"), 2, Calendar.MONTH));
		 * EP.Edit_SetValue(TabName+"MeasurementCompletedAT_EB", 55);
		 * EP.Btn_Click(TabName+"Save_BN");
		 * EP.TakeStaticScreenshot("IncorrectFormatError2");
		 * EP.CompareText("IncorrecrFormatError_DT",
		 * EP.LangData("VerifyIncorrectFormats"),
		 * EP.GetText(TabName+"IncorrecrFormatError_DT"));
		 * EP.CompareText("PatientHeightErrMsg_DT",
		 * EP.LangData("FinalHeightSmaller"),GetErrorMessage(2));
		 * EP.ClearStaticScreenshot();
		 * 
		 * //Patient is female and final height recorded and age should be
		 * greater than 14 years old
		 * EP.DrpDwn_SelectIndex(TabName+"StatusOfStudy_DD",1);
		 * EP.DrpDwn_SelectIndex(TabName+"StudyCompletionAT_DD",1);
		 * EP.DrpDwn_SelectIndex(TabName+"SubjectHeightAT_DD", 1);
		 * EP.Edit_SetValue(TabName+"MeasurementCompleted_EB", "100");
		 * EP.SelectDate
		 * (TabName+"DateOFMeasurementCalendarAT_TT",EP.Date_AddDays
		 * (EP.TestData("ECOSPatientDOB"), 2, Calendar.MONTH));
		 * EP.SelectDate(TabName
		 * +"ContinuedTreatmentStopDateAT_IM",EP.Date_AddDays
		 * (EP.TestData("ECOSPatientDOB"), 2, Calendar.MONTH)); EP.Wait(2);
		 * EP.Btn_Click(TabName+"Save_BN");
		 * EP.TakeStaticScreenshot("AgeMustBeGreaterThan14");
		 * EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
		 * EP.GetText(TabName+"ValueOutOfRangeError_DT"));
		 * EP.CompareText("PatientHeightErrMsg_DT",
		 * EP.LangData("AgeMustBeGreaterThan14"),
		 * EP.GetText(TabName+"PatientHeightErrMsgAT_DT"));
		 * EP.ClearStaticScreenshot();
		 * 
		 * //Incorrect format for measurement
		 * EP.DrpDwn_SelectIndex(TabName+"SubjectHeightAT_DD", 2);
		 * EP.DrpDwn_SelectIndex(TabName+"StudyCompletionAT_DD",2);
		 * EP.Edit_SetValue(TabName+"MeasurementCompleted_EB", IncorrectFormat);
		 * EP.Btn_Click(TabName+"Save_BN");
		 * EP.TakeStaticScreenshot("incorrectFormatMeasurement");
		 * EP.CompareText("IncorrecrFormatError_DT",
		 * EP.LangData("VerifyIncorrectFormats"),
		 * EP.GetText(TabName+"IncorrecrFormatError_DT"));
		 * EP.CompareText("MeasurementErrMsg_DT",
		 * EP.LangData("IncorrectFormat"), GetErrorMessage(1));
		 * EP.ClearStaticScreenshot();
		 * EP.Edit_SetValue(TabName+"MeasurementCompleted_EB", 55);
		 * EP.Btn_Click(TabName+"Save_BN");
		 * EP.TakeStaticScreenshot("nearfinalSmaller");
		 * EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
		 * EP.GetText(TabName+"ValueOutOfRangeError_DT"));
		 * EP.CompareText("PatientHeightErrMsg_DT",
		 * EP.LangData("NearFinalSmaller"), GetErrorMessage(1));
		 * EP.CompareText("Ignoreandsave",EP.LangData("IgnoreAndSave"),
		 * EP.GetText(TabName+"Save_BN")); EP.ClearStaticScreenshot();
		 * EP.Btn_Click(TabName+"Cancel_BN");
		 * EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");
		 * 
		 * //Error for Treatment Stop Date lesser than treatment Start Date
		 * 
		 * TreatmentStartDate=EP.Date_AddDays(EP.Date_AddDays(EP.TestData(
		 * "ECOSPatientDOB"), 2, Calendar.MONTH),-1, Calendar.DATE);
		 * EP.DrpDwn_SelectIndex(TabName+"StudyCompletionAT_DD",3);//1
		 * EP.DrpDwn_SelectIndex(TabName+"SubjectHeightAT_DD", 3);//1
		 * EP.Edit_SetValue(TabName+"MeasurementCompleted_EB", 60);
		 * EP.SelectDate(TabName+"DateOFMeasurementCalendarAT_TT",
		 * EP.Date_AddDays(EP.TestData("ECOSPatientDOB"), 2, Calendar.MONTH));
		 * EP.SelectDate(TabName+"LastStudyDayCalendarAT_TT",
		 * EP.Date_AddDays(EP.TestData("ECOSPatientDOB"), 2, Calendar.MONTH));
		 * EP
		 * .SelectDate(TabName+"ContinuedTreatmentStopDateAT_IM",TreatmentStartDate
		 * ); EP.Btn_Click(TabName+"Save_BN");
		 * EP.TakeStaticScreenshot("TSDLessThanCurrentGH");
		 * EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
		 * EP.GetText(TabName+"IncorrecrFormatError_DT"));
		 * EP.VerifyRegExpression("TSDLessThanCurrentGH",
		 * EP.LangData("TSDLessThanCurrentGH"
		 * ),GetErrorMessage(1),EP.TranslateDate(TreatmentStartDate,
		 * "dd MMM yyyy", false)); EP.ClearStaticScreenshot();
		 * EP.Btn_Click(TabName+"Cancel_BN");
		 * EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");
		 * 
		 * 
		 * //Success message for Completion
		 * EP.DrpDwn_SelectIndex(TabName+"StudyCompletionAT_DD",3);//1
		 * EP.DrpDwn_SelectIndex(TabName+"SubjectHeightAT_DD", 3);//1
		 * EP.Edit_SetValue(TabName+"MeasurementCompleted_EB", 60);
		 * EP.SelectDate(TabName+"DateOFMeasurementCalendarAT_TT",
		 * EP.GetTimeStamp("dd MMM yyyy"));
		 * EP.SelectDate(TabName+"ContinuedTreatmentStopDateAT_IM",
		 * EP.GetTimeStamp("dd MMM yyyy"));
		 * EP.SelectDate(TabName+"LastStudyDayCalendarAT_TT",
		 * EP.GetTimeStamp("dd MMM yyyy"));
		 * 
		 * //EP.SelectDate(TabName+"ContinuedTreatmentStopDateAT_IM",
		 * EP.Date_AddDays(EP.TestData("ECOSPatientDOB"), 2, Calendar.MONTH));
		 * EP.VerifyMonth(TabName+"TreatmentStopDateAT_EB");
		 * EP.VerifyMonth(TabName+"DateOfMeasurementAT_EB");
		 * EP.VerifyMonth(TabName+"LastStudyDayAT_EB");
		 * EP.Btn_Click(TabName+"Save_BN");
		 * ECOS.VerifySuccessMessage(EP.LangData("CompletionOrTermination"));
		 * 
		 * //To check error messgage for male patient
		 * EP.SearchForPatient(EP.TestData("ECOSPatient"));
		 * EP.Rbtn_SetOption("#MyPatients_FirstPatient_RB", "ON");
		 * EP.Btn_Click("#MyPatients_BaselineRecord_BN");
		 * EP.Lnk_Click("#NavigationTab_RecordOverview_LK");
		 * EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");
		 * EP.DrpDwn_SelectIndex(TabName+"StatusOfStudy_DD", 1);
		 * EP.DrpDwn_SelectIndex(TabName+"StudyCompletion_DD",1);
		 * EP.DrpDwn_SelectIndex(TabName+"SubjectHeight_DD", 1);
		 * EP.Edit_SetValue(TabName+"MeasurementCompleted_EB", "100");
		 * EP.Wait(2); EP.Btn_Click(TabName+"Save_BN");
		 * EP.TakeStaticScreenshot("AgeMustBeGreaterThan15Error");
		 * EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
		 * EP.GetText(TabName+"ValueOutOfRangeError_DT"));
		 * EP.CompareText("PatientHeightErrMsg_DT",
		 * EP.LangData("AgeMustBeGreaterThan15"), GetErrorMessage(1));
		 * EP.ClearStaticScreenshot();
		 * 
		 * //****************DIS CONTINUED
		 * SECTION*********************************
		 * if(EP.ObjectExists("#NavigationTab_MyPatientsList_LK"));
		 * EP.Lnk_Click("#NavigationTab_MyPatientsList_LK");
		 * EP.SearchForPatient(EP.TestData("PatientName") + " 04");
		 * EP.Rbtn_SetOption("#MyPatients_FirstPatient_RB", "ON");
		 * EP.Btn_Click("#MyPatients_BaselineRecord_BN");
		 * EP.Lnk_Click("#NavigationTab_RecordOverview_LK");
		 * EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");
		 * EP.DrpDwn_SelectIndex(TabName+"StatusOfStudy_DD", 2);
		 * EP.TakeStaticScreenshot("StudyDiscontinued");
		 * EP.VerifyText(TabName+"PrematureStudyDiscontinuationReasonAT_ST");
		 * EP.CompareDynamicDropDownValues(TabName+"DiscontinuationReasonAT_DD",
		 * EP.ORData(TabName+"DiscontinuationReasonAT_DD"), false);
		 * EP.DrpDwn_SelectIndex(TabName+"DiscontinuationReasonAT_DD", 2);//To
		 * make visible Remove data from Study?
		 * 
		 * EP.VerifyText(TabName+"RemoveDataFromStudyAT_ST");
		 * EP.VerifyText(TabName+"RemoveDataYesAT_ST");
		 * EP.VerifyText(TabName+"RemoveDataNoAT_ST");
		 * 
		 * EP.VerifyText(TabName+"InitiatorOfWithdrawalAT_ST");
		 * EP.CompareDynamicDropDownValues(TabName+"InitiatorOfWithdrawalAT_DD",
		 * EP.ORData(TabName+"InitiatorOfWithdrawalAT_DD"), false);
		 * EP.VerifyText(TabName+"PatientHeightAtPrematureAT_ST");
		 * EP.CompareDynamicDropDownValues
		 * (TabName+"PatientHeightAtDiscontinuationAT_DD",
		 * EP.ORData(TabName+"PatientHeightAtDiscontinuation_DD"), false);
		 * EP.VerifyText(TabName+"DisContLastStudyDayAT_ST");
		 * EP.VerifyToolTip(TabName+"DiscontinuedLastStudyDayCalendarAT_TT");
		 * EP.VerifyToolTip(TabName+"DiscontinuedTreatmentStopDateAT_IM");
		 * EP.VerifyText(TabName+"DiscontinuedMeasurementAT_ST");
		 * EP.VerifyText(TabName+"DiscontinuedTreatmentStopDateAT_ST");
		 * EP.VerifyTextContained(TabName+"MeasurementCmAT_ST");
		 * EP.ClearStaticScreenshot();
		 * 
		 * //Mandatory fields of discontinued section
		 * EP.DrpDwn_SelectIndex(TabName+"StatusOfStudy_DD", 2);
		 * EP.DrpDwn_SelectIndex(TabName+"DiscontinuationReasonAT_DD", 0);
		 * EP.DrpDwn_SelectIndex(TabName+"InitiatorOfWithdrawalAT_DD", 0);
		 * EP.Edit_SetValueByJavaScript(TabName+"DiscontinuedLastStudyDayAT_EB",
		 * "");
		 * EP.Edit_SetValueByJavaScript(TabName+"DiscontinuedTreatmentStopDateAT_EB"
		 * ,""); EP.Edit_ClearValue(TabName+"Measurement_EB");
		 * EP.DrpDwn_SelectIndex
		 * (TabName+"PatientHeightAtDiscontinuationAT_DD",0);
		 * EP.Btn_Click(TabName+"Save_BN");
		 * EP.TakeStaticScreenshot("Mandatory");
		 * EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
		 * EP.GetText(TabName+"ValueOutOfRangeError_DT"));
		 * EP.CompareText("PrematureStudyDiscontinuationReasonMandatory",
		 * EP.LangData("FieldIsMandatory"), GetErrorMessage(1));
		 * EP.CompareText("InitiatorofwithdrawalMandatory",
		 * EP.LangData("FieldIsMandatory"), GetErrorMessage(2));
		 * EP.CompareText("StudyDiscontinuationMandatory",
		 * EP.LangData("FieldIsMandatory"), GetErrorMessage(3));
		 * EP.CompareText("LastStudyDateMandatory",
		 * EP.LangData("FieldIsMandatory"), GetErrorMessage(4));
		 * EP.CompareText("MeasurementMandatory",
		 * EP.LangData("FieldIsMandatory"), GetErrorMessage(5));
		 * EP.ClearStaticScreenshot(); EP.Btn_Click(TabName+"Cancel_BN");
		 * EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");
		 * 
		 * 
		 * //Measurement range value and Date related error messages/Treatment
		 * Discontinuation Details madatory field
		 * EP.DrpDwn_SelectIndex(TabName+"StatusOfStudy_DD", 2);
		 * EP.DrpDwn_SelectIndex(TabName+"DiscontinuationReasonAT_DD", 1);
		 * EP.DrpDwn_SelectIndex(TabName+"InitiatorOfWithdrawalAT_DD", 1);
		 * EP.DrpDwn_SelectIndex
		 * (TabName+"PatientHeightAtDiscontinuationAT_DD",3);
		 * EP.DrpDwn_SelectIndex
		 * (TabName+"TreatmentDiscontinuationDetailsAT_DD",0);
		 * EP.SelectDate(TabName+"DiscontinuedLastStudyDayCalendarAT_TT",
		 * EP.TestData("ECOSPatientDOB"));
		 * EP.SelectDate(TabName+"DiscontinuedTreatmentStopDateAT_IM",
		 * EP.TestData("ECOSPatientDOB"));
		 * EP.Edit_SetValue(TabName+"Measurement_EB", MinValue);
		 * EP.Btn_Click(TabName+"Save_BN");
		 * EP.TakeStaticScreenshot("OutOfRangeError");
		 * EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
		 * EP.GetText(TabName+"ValueOutOfRangeError_DT"));
		 * EP.CompareText("TreatmentDiscontinuationDetailsMandatory",
		 * EP.LangData("FieldIsMandatory"), GetErrorMessage(1));
		 * EP.CompareText("LastStudyDateError",
		 * EP.LangData("LastStudyDateError"), GetErrorMessage(2));
		 * EP.CompareText("TreatmentStopDate ",
		 * EP.LangData("LastStudyDateError"), GetErrorMessage(3));
		 * EP.VerifyRegExpression("Height",
		 * EP.LangData("RangeValue"),GetErrorMessage(4),"\\d{2,3}");
		 * EP.CompareText("Ignoreandsave",EP.LangData("IgnoreAndSave"),
		 * EP.GetText(TabName+"Save_BN")); EP.ClearStaticScreenshot();
		 * EP.Btn_Click(TabName+"Cancel_BN");
		 * 
		 * //Validation of "Other, Specify" error messages
		 * 
		 * EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");
		 * EP.DrpDwn_SelectIndex(TabName+"StatusOfStudy_DD", 2);
		 * EP.DrpDwn_SelectIndex(TabName+"DiscontinuationReasonAT_DD", 6);
		 * EP.Edit_ClearValue(TabName+"DiscontinuationReasonAT_EB");
		 * EP.DrpDwn_SelectIndex(TabName+"InitiatorOfWithdrawalAT_DD", 5);
		 * EP.Edit_ClearValue(TabName+"InitiatorOfWithdrawalAT_EB");
		 * EP.SelectDate(TabName+"DiscontinuedLastStudyDayCalendarAT_TT",
		 * EP.Date_AddDays(EP.TestData("ECOSPatientDOB"), 2, Calendar.MONTH));
		 * EP.SelectDate(TabName+"DiscontinuedTreatmentStopDateAT_IM",
		 * EP.Date_AddDays(EP.TestData("ECOSPatientDOB"), 2, Calendar.MONTH));
		 * EP
		 * .DrpDwn_SelectIndex(TabName+"PatientHeightAtDiscontinuationAT_DD",3);
		 * EP.Edit_SetValue(TabName+"Measurement_EB", 56);
		 * EP.Btn_Click(TabName+"Save_BN");
		 * EP.TakeStaticScreenshot("OutOfRangeError");
		 * EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
		 * EP.GetText(TabName+"ValueOutOfRangeError_DT"));
		 * EP.CompareText("FieldIsMandatory", EP.LangData("FieldIsMandatory"),
		 * GetErrorMessage(1)); EP.CompareText("FieldIsMandatory",
		 * EP.LangData("FieldIsMandatory"), GetErrorMessage(2));
		 * EP.CompareText("Ignoreandsave",EP.LangData("IgnoreAndSave"),
		 * EP.GetText(TabName+"Save_BN")); EP.ClearStaticScreenshot();
		 * EP.Btn_Click(TabName+"Cancel_BN");
		 * 
		 * //Incorrect Formats
		 * EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");
		 * EP.Edit_ClearValue(TabName+"MeasurementAT_EB");
		 * EP.Edit_SetValue(TabName+"MeasurementAT_EB", IncorrectFormat);
		 * EP.DrpDwn_SelectIndex(TabName+"DiscontinuationReasonAT_DD", 5);
		 * EP.DrpDwn_SelectIndex
		 * (TabName+"PatientHeightAtDiscontinuationAT_DD",3);
		 * EP.DrpDwn_SelectIndex(TabName+"InitiatorOfWithdrawalAT_DD", 4);
		 * EP.SelectDate(TabName+"DiscontinuedLastStudyDayCalendarAT_TT",
		 * EP.GetTimeStamp("dd MMM yyyy"));
		 * EP.SelectDate(TabName+"DiscontinuedTreatmentStopDateAT_IM"
		 * ,EP.GetTimeStamp("dd MMM yyyy")); EP.Btn_Click(TabName+"Save_BN");
		 * EP.TakeStaticScreenshot("MeasurementFormatError");
		 * EP.VerifyText(TabName+"IncorrecrFormatError_DT");
		 * EP.CompareText("IncorrectFormat", EP.LangData("IncorrectFormat"),
		 * GetErrorMessage(1)); EP.ClearStaticScreenshot();
		 * 
		 * 
		 * EP.Btn_Click(TabName+"Cancel_BN");
		 * EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");
		 * 
		 * //Error for Treatment Stop Date lesser than treatment Start Date
		 * 
		 * EP.Edit_SetValue(TabName+"MeasurementAT_EB", 60);
		 * EP.DrpDwn_SelectIndex(TabName+"DiscontinuationReasonAT_DD", 5);
		 * EP.DrpDwn_SelectIndex
		 * (TabName+"PatientHeightAtDiscontinuationAT_DD",3);
		 * EP.DrpDwn_SelectIndex(TabName+"InitiatorOfWithdrawalAT_DD", 4);
		 * EP.SelectDate(TabName+"DiscontinuedLastStudyDayAT_EB",
		 * EP.Date_AddDays(EP.TestData("ECOSPatientDOB"), 2, Calendar.MONTH));
		 * EP.SelectDate(TabName+"DiscontinuedTreatmentStopDateAT_IM",
		 * TreatmentStartDate); EP.Btn_Click(TabName+"Save_BN");
		 * EP.TakeStaticScreenshot("TSDLessThanCurrentGH");
		 * EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
		 * EP.GetText(TabName+"ValueOutOfRangeError_DT"));
		 * EP.VerifyRegExpression("TSDLessThanCurrentGH",
		 * EP.LangData("TSDLessThanCurrentGH"
		 * ),GetErrorMessage(1),EP.TranslateDate(TreatmentStartDate,
		 * "dd MMM yyyy", false)); EP.ClearStaticScreenshot();
		 * EP.Btn_Click(TabName+"Cancel_BN");
		 * EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");
		 * 
		 * 
		 * //To verify success message for Discontinued
		 * EP.DrpDwn_SelectIndex(TabName+"DiscontinuationReasonAT_DD",
		 * EP.RandomValue(2, 5));
		 * EP.DrpDwn_SelectIndex(TabName+"InitiatorOfWithdrawalAT_DD",
		 * EP.RandomValue(1, 4)); EP.Edit_SetValue(TabName+"Measurement_EB",
		 * "56");
		 * EP.SelectDate(TabName+"DiscontinuedLastStudyDayCalendarAT_TT",EP
		 * .GetTimeStamp("dd MMM yyyy"));
		 * EP.SelectDate(TabName+"DiscontinuedTreatmentStopDateAT_IM"
		 * ,EP.GetTimeStamp("dd MMM yyyy"));
		 * EP.TakeStaticScreenshot("MonthVerification");
		 * EP.VerifyMonth(TabName+"DiscontinuedTreatmentStopDateAT_EB");
		 * EP.VerifyMonth(TabName+"DiscontinuedLastStudyDayAT_EB");
		 * EP.ClearStaticScreenshot(); EP.Btn_Click(TabName+"Save_BN");
		 * ECOS.VerifySuccessMessage(EP.LangData("CompletionOrTermination"));
		 * 
		 * 
		 * break;
		 */
		case SWEDEN:
		case CZECHREPUBLIC:

			// Completed Section
			EP.DrpDwn_SelectIndex(TabName + "StatusOfStudy_DD", 1);
			EP.TakeStaticScreenshot("StudyCompleted");
			EP.VerifyText(TabName + "StudyCompletionAT_ST");
			EP.CompareDropDownValues(TabName + "StudyCompletionCZ_DD");
			EP.VerifyText(TabName + "PatientHeightAtEndOfStudyAT_ST");
			EP.CompareDropDownValues(TabName + "SubjectHeightCZ_DD");
			EP.VerifyText(TabName + "DateOfMeasurementAT_ST");
			EP.VerifyToolTip(TabName + "DateOFMeasurementCalendarAT_TT");
			EP.VerifyText(TabName + "MeasurementAT_ST");
			EP.VerifyTextContained(TabName + "MeasurementCmScaleAT_ST");
			EP.VerifyText(TabName + "LastStudyDayAT_ST");
			EP.VerifyToolTip(TabName + "LastStudyDayCalendarAT_TT");
			EP.VerifyText(TabName + "ContinuedTreatmentStopDateAT_ST");
			EP.VerifyToolTip(TabName + "ContinuedTreatmentStopDateAT_IM");
			EP.VerifyPageLayout();

			String TreatmentStartDateCZ = EP.Date_AddDays(
					EP.GetTimeStamp("dd MMM yyyy"), -2, Calendar.DATE);
			// Date and Measurement Range Error Message Validations
			EP.DrpDwn_SelectIndex(TabName + "StatusOfStudy_DD", 1);
			EP.DrpDwn_SelectIndex(TabName + "StudyCompletionCZ_DD", 2); // Study
																		// Completion
			EP.DrpDwn_SelectIndex(TabName + "SubjectHeightCZ_DD", 3);
			EP.Edit_SetValue(TabName + "MeasurementCompletedAT_EB", MinValue);
			EP.SelectDate(TabName + "ContinuedTreatmentStopDateAT_IM",
					EP.TestData("ECOSPatientDOB"));
			EP.SelectDate(TabName + "LastStudyDayCalendarAT_TT",
					EP.TestData("ECOSPatientDOB"));
			EP.SelectDate(TabName + "DateOFMeasurementCalendarAT_TT",
					EP.TestData("ECOSPatientDOB"));

			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("IncorrectFormatError1");
			EP.VerifyText(TabName + "IncorrecrFormatErrorES_DT");// Header error
																	// msg
			EP.CompareText("StudyCompletionErrMsg_DT",
					EP.LangData("AccordanceWithAnother"), GetErrorMessage(1));
			EP.CompareText("PatientHeightErrMsg_DT",
					EP.LangData("AccordanceWithAnother"), GetErrorMessage(2));
			EP.VerifyRegExpression("MothersHeight_RangeError",
					EP.LangData("RangeValue"), GetErrorMessage(3), "\\d{1,3}");
			EP.CompareText("LastStudyDayErrMsg_DT",
					EP.LangData("LastStudyDateError"), GetErrorMessage(4));
			EP.CompareText("TreatmentStopDate",
					EP.LangData("LastStudyDateError"), GetErrorMessage(5));
			EP.ClearStaticScreenshot();

			// Final height is smaller than the latest visit height
			EP.DrpDwn_SelectIndex(TabName + "StudyCompletionCZ_DD", 1);
			EP.DrpDwn_SelectIndex(TabName + "SubjectHeightCZ_DD", 1);
			EP.Edit_ClearValue(TabName + "MeasurementCompleted_EB");
			EP.SelectDate(TabName + "LastStudyDayCalendarAT_TT",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate(TabName + "DateOFMeasurementCalendarAT_TT", EP
					.Date_AddDays(EP.TestData("ECOSPatientDOB"), 2,
							Calendar.MONTH));
			EP.SelectDate(TabName + "ContinuedTreatmentStopDateAT_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.Edit_SetValue(TabName + "MeasurementCompletedAT_EB", 55);
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("PatientHeightErrMsg_DT");
			EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
					EP.GetText(TabName + "ValueOutOfRangeErrorES_DT"));
			EP.CompareText("PatientHeightErrMsg_DT",
					EP.LangData("FinalHeightSmaller"), GetErrorMessage(1));
			EP.CompareText("Ignoreandsave", EP.LangData("IgnoreAndSave"),
					EP.GetText(TabName + "Save_BN"));
			EP.ClearStaticScreenshot();
			EP.Btn_Click(TabName + "Cancel_BN");
			EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");

			// Patient is female and final height recorded and age should be
			// greater than 14 years old
			EP.DrpDwn_SelectIndex(TabName + "StatusOfStudy_DD", 1);
			EP.DrpDwn_SelectIndex(TabName + "StudyCompletionCZ_DD", 1);
			EP.DrpDwn_SelectIndex(TabName + "SubjectHeightCZ_DD", 1);
			EP.Edit_SetValue(TabName + "MeasurementCompleted_EB", "100");
			EP.SelectDate(TabName + "ContinuedTreatmentStopDateAT_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate(TabName + "LastStudyDayCalendarAT_TT",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate(TabName + "DateOFMeasurementCalendarAT_TT", EP
					.Date_AddDays(EP.TestData("ECOSPatientDOB"), 2,
							Calendar.MONTH));
			EP.Wait(2);
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("AgeMustBeGreaterThan14");
			EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
					EP.GetText(TabName + "ValueOutOfRangeErrorES_DT"));
			EP.CompareText("PatientHeightErrMsg_DT",
					EP.LangData("AgeMustBeGreaterThan14"),
					EP.GetText(TabName + "PatientHeightErrMsgAT_DT"));
			EP.CompareText("Ignoreandsave", EP.LangData("IgnoreAndSave"),
					EP.GetText(TabName + "Save_BN"));
			EP.ClearStaticScreenshot();
			EP.Btn_Click(TabName + "Cancel_BN");
			EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");

			// Incorrect format for measurement
			EP.DrpDwn_SelectIndex(TabName + "StudyCompletionCZ_DD", 3);
			EP.DrpDwn_SelectIndex(TabName + "SubjectHeightCZ_DD", 3);
			EP.Edit_SetValue(TabName + "MeasurementCompleted_EB",
					IncorrectFormat);
			EP.SelectDate(TabName + "ContinuedTreatmentStopDateAT_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate(TabName + "LastStudyDayCalendarAT_TT",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate(TabName + "DateOFMeasurementCalendarAT_TT",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("incorrectFormatMeasurement");
			EP.CompareText("IncorrecrFormatError_DT",
					EP.LangData("VerifyIncorrectFormats"),
					EP.GetText(TabName + "IncorrecrFormatErrorES_DT"));
			EP.CompareText("MeasurementErrMsg_DT",
					EP.LangData("IncorrectFormat"), GetErrorMessage(1));
			EP.ClearStaticScreenshot();
			EP.DrpDwn_SelectIndex(TabName + "StudyCompletionCZ_DD", 2);
			EP.DrpDwn_SelectIndex(TabName + "SubjectHeightCZ_DD", 2);
			EP.Edit_SetValue(TabName + "MeasurementCompleted_EB", 55);
			EP.SelectDate(TabName + "ContinuedTreatmentStopDateAT_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate(TabName + "LastStudyDayCalendarAT_TT",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate(TabName + "DateOFMeasurementCalendarAT_TT",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("nearfinalSmaller");
			EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
					EP.GetText(TabName + "ValueOutOfRangeErrorES_DT"));
			EP.CompareText("PatientHeightErrMsg_DT",
					EP.LangData("NearFinalSmaller"), GetErrorMessage(1));
			EP.CompareText("Ignoreandsave", EP.LangData("IgnoreAndSave"),
					EP.GetText(TabName + "Save_BN"));
			EP.ClearStaticScreenshot();
			EP.Btn_Click(TabName + "Cancel_BN");
			EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");

			// Error for LAst Visit
			EP.NavigateTo(RecoveringURL);
			TreatmentStartDate = EP.Date_AddDays(EP.Date_AddDays(
					EP.TestData("ECOSPatientDOB"), 2, Calendar.MONTH), -1,
					Calendar.DATE);
			EP.DrpDwn_SelectIndex(TabName + "StudyCompletionCZ_DD", 3);// 1
			EP.DrpDwn_SelectIndex(TabName + "SubjectHeightCZ_DD", 3);// 1
			EP.Edit_SetValue(TabName + "MeasurementCompleted_EB", 60);
			EP.SelectDate(TabName + "ContinuedTreatmentStopDateAT_IM",
					TreatmentStartDateCZ);
			EP.SelectDate(TabName + "LastStudyDayCalendarAT_TT",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate(TabName + "DateOFMeasurementCalendarAT_TT", EP
					.Date_AddDays(EP.TestData("ECOSPatientDOB"), 2,
							Calendar.MONTH));
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("TSDLessThanCurrentGH");
			EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
					EP.GetText(TabName + "IncorrecrFormatErrorES_DT"));
			EP.VerifyRegExpression("TSDLessThanCurrentGH",
					EP.LangData("TSDLessThanCurrentGH"), GetErrorMessage(1),
					EP.TranslateDate(TreatmentStartDate, "dd MMM yyyy", false));
			EP.CompareText("Ignoreandsave", EP.LangData("IgnoreAndSave"),
					EP.GetText(TabName + "Save_BN"));
			EP.ClearStaticScreenshot();
			EP.Btn_Click(TabName + "Cancel_BN");
			EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");
			EP.NavigateTo(Env.ECOSURL);
			// -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

			// Verify the text in populate previous values popup (Last visit)
			EP.SearchForPatient(EP.TestData("PatientName") + " 01");
			EP.Rbtn_SetOption("#MyPatients_FirstPatient_RB", "ON");
			EP.Btn_Click("#MyPatients_VisitsRecord_BN");
			EP.WbTbl_SortColumn("#AddVisit_VisitDate_TE", 2, "DESC");// Bring
																		// the
																		// latest
																		// record
																		// on
																		// top
			Date = EP.WbTbl_GetText("#AddVisit_VisitDate_TE", 1, 2);

			// Error for Treatment Stop Date lesser than treatment Start Date
			EP.NavigateTo(RecoveringURL);
			EP.DrpDwn_SelectIndex(TabName + "StudyCompletionCZ_DD", 3);// 1
			EP.DrpDwn_SelectIndex(TabName + "SubjectHeightCZ_DD", 3);// 1
			EP.Edit_SetValue(TabName + "MeasurementCompleted_EB", 60);
			EP.SelectDate(TabName + "ContinuedTreatmentStopDateAT_IM", EP
					.Date_AddDays(EP.TranslateDate(Date, "dd MMM yyyy", true),
							-1, Calendar.DATE));
			EP.SelectDate(TabName + "LastStudyDayCalendarAT_TT", EP
					.Date_AddDays(EP.TranslateDate(Date, "dd MMM yyyy", true),
							-1, Calendar.DATE));
			EP.SelectDate(TabName + "DateOFMeasurementCalendarAT_TT", EP
					.Date_AddDays(EP.TestData("ECOSPatientDOB"), 2,
							Calendar.MONTH));
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("TSDLessThanCurrentGH");
			EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
					EP.GetText(TabName + "IncorrecrFormatErrorES_DT"));
			EP.CompareText("DateAfterLastVisit",
					EP.LangData("DateAfterLastVisit"), GetErrorMessage(1));
			EP.CompareText("DateAfterLastVisit",
					EP.LangData("DateAfterLastVisit"), GetErrorMessage(2));
			EP.CompareText("Ignoreandsave", EP.LangData("IgnoreAndSave"),
					EP.GetText(TabName + "Save_BN"));
			EP.ClearStaticScreenshot();
			EP.Btn_Click(TabName + "Cancel_BN");
			EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");

			// Success message for Completion
			EP.DrpDwn_SelectIndex(TabName + "StudyCompletionCZ_DD", 3);// 1
			EP.DrpDwn_SelectIndex(TabName + "SubjectHeightCZ_DD", 3);// 1
			EP.Edit_SetValue(TabName + "MeasurementCompleted_EB", 60);
			EP.SelectDate(TabName + "ContinuedTreatmentStopDateAT_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate(TabName + "LastStudyDayCalendarAT_TT",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate(TabName + "DateOFMeasurementCalendarAT_TT",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.VerifyMonth(TabName + "TreatmentStopDateAT_EB");
			EP.VerifyMonth(TabName + "DateOfMeasurementAT_EB");
			EP.VerifyMonth(TabName + "LastStudyDayAT_EB");
			EP.Btn_Click(TabName + "Save_BN");
			ECOS.VerifySuccessMessage(EP.LangData("CompletionOrTermination"));

			// To check error messgage for male patient - FinalHeightDifference
			EP.SearchForPatient(EP.TestData("ECOSPatient"));
			EP.Rbtn_SetOption("#MyPatients_FirstPatient_RB", "ON");
			EP.Btn_Click("#MyPatients_BaselineRecord_BN");
			EP.Lnk_Click("#NavigationTab_RecordOverview_LK");
			EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");
			EP.DrpDwn_SelectIndex(TabName + "StatusOfStudy_DD", 1);
			EP.DrpDwn_SelectIndex(TabName + "StudyCompletionCZ_DD", 1);
			EP.DrpDwn_SelectIndex(TabName + "SubjectHeightCZ_DD", 1);
			EP.Edit_SetValue(TabName + "MeasurementCompleted_EB", "100");
			EP.Wait(2);
			EP.SelectDate(TabName + "ContinuedTreatmentStopDateAT_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate(TabName + "LastStudyDayCalendarAT_TT",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate(TabName + "DateOFMeasurementCalendarAT_TT",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("FinalHeightDifference");
			EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
					EP.GetText(TabName + "ValueOutOfRangeErrorES_DT"));
			EP.CompareText("FinalHeightDifference",
					EP.LangData("FinalHeightDifference"), GetErrorMessage(1));
			EP.CompareText("Ignoreandsave", EP.LangData("IgnoreAndSave"),
					EP.GetText(TabName + "Save_BN"));
			EP.ClearStaticScreenshot();

			EP.Btn_Click(TabName + "Cancel_BN");
			EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");

			// To check error messgage for male patient -
			// FinalHeightDifference1CM

			EP.DrpDwn_SelectIndex(TabName + "StatusOfStudy_DD", 1);
			EP.DrpDwn_SelectIndex(TabName + "StudyCompletionCZ_DD", 1);
			EP.DrpDwn_SelectIndex(TabName + "SubjectHeightCZ_DD", 2);
			EP.Edit_SetValue(TabName + "MeasurementCompleted_EB", "100");
			EP.Wait(2);
			EP.SelectDate(TabName + "ContinuedTreatmentStopDateAT_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate(TabName + "LastStudyDayCalendarAT_TT",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate(TabName + "DateOFMeasurementCalendarAT_TT",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("FinalHeightDifference1CM");
			EP.CompareText("IncorrecrFormatError_DT",
					EP.LangData("VerifyIncorrectFormats"),
					EP.GetText(TabName + "IncorrecrFormatErrorES_DT"));
			EP.CompareText("AccordanceWithAnother",
					EP.LangData("AccordanceWithAnother"), GetErrorMessage(1));
			EP.CompareText("FinalHeightDifference1CM",
					EP.LangData("FinalHeightDifference1CM"), GetErrorMessage(2));
			EP.CompareText("Save", EP.LangData("Save"),
					EP.GetText(TabName + "Save_BN"));
			EP.ClearStaticScreenshot();

			// Delete the Visit for John Doe
			EP.Btn_Click(TabName + "Cancel_BN");
			EP.Lnk_Click("#NavigationTab_VisitsRecord_LK");
			EP.WbTbl_SortColumn("#AddVisit_VisitDate_TE", 2, "DESC");
			EP.WbTbl_SelectRbtn("#AddVisit_VisitDate_TE", 2);
			EP.Btn_Click("#AddVisit_Continue_BN");
			if (EP.ObjectExists("#General_Delete_IM")) {
				DeleteRecord("#General_Delete_IM", false);
			}

			// To check error messgage for male patient - AgeMustBeGreaterThan15

			EP.Lnk_Click("#NavigationTab_RecordOverview_LK");
			EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");
			EP.DrpDwn_SelectIndex(TabName + "StatusOfStudy_DD", 1);
			EP.DrpDwn_SelectIndex(TabName + "StudyCompletionCZ_DD", 1);
			EP.DrpDwn_SelectIndex(TabName + "SubjectHeightCZ_DD", 1);
			EP.Edit_SetValue(TabName + "MeasurementCompleted_EB", "100");
			EP.Wait(2);
			EP.SelectDate(TabName + "ContinuedTreatmentStopDateAT_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate(TabName + "LastStudyDayCalendarAT_TT",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate(TabName + "DateOFMeasurementCalendarAT_TT",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("AgeMustBeGreaterThan15Error");
			EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
					EP.GetText(TabName + "ValueOutOfRangeErrorES_DT"));
			EP.CompareText("PatientHeightErrMsg_DT",
					EP.LangData("AgeMustBeGreaterThan15"), GetErrorMessage(1));
			EP.CompareText("Ignoreandsave", EP.LangData("IgnoreAndSave"),
					EP.GetText(TabName + "Save_BN"));
			EP.ClearStaticScreenshot();

			// ****************DIS CONTINUED
			// SECTION*********************************
			if (EP.ObjectExists("#NavigationTab_MyPatientsList_LK"))
				;
			EP.Lnk_Click("#NavigationTab_MyPatientsList_LK");
			EP.SearchForPatient(EP.TestData("PatientName") + " 04");
			EP.Rbtn_SetOption("#MyPatients_FirstPatient_RB", "ON");
			EP.Btn_Click("#MyPatients_BaselineRecord_BN");
			EP.Lnk_Click("#NavigationTab_RecordOverview_LK");
			EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");
			EP.DrpDwn_SelectIndex(TabName + "StatusOfStudy_DD", 2);
			EP.TakeStaticScreenshot("StudyDiscontinued");
			EP.VerifyText(TabName + "PrematureStudyDiscontinuationReasonAT_ST");
			EP.CompareDynamicDropDownValues(TabName
					+ "DiscontinuationReasonCZ_DD",
					EP.ORData(TabName + "DiscontinuationReasonCZ_DD"), false);
			EP.DrpDwn_SelectIndex(TabName + "DiscontinuationReasonCZ_DD", 2);// To
																				// make
																				// visible
																				// Remove
																				// data
																				// from
																				// Study?

			EP.VerifyText(TabName + "RemoveDataFromStudyAT_ST");
			EP.VerifyText(TabName + "RemoveDataYesAT_ST");
			EP.VerifyText(TabName + "RemoveDataNoAT_ST");

			EP.VerifyText(TabName + "InitiatorOfWithdrawalAT_ST");
			EP.CompareDynamicDropDownValues(TabName
					+ "InitiatorOfWithdrawalCZ_DD",
					EP.ORData(TabName + "InitiatorOfWithdrawalCZ_DD"), false);
			EP.VerifyText(TabName + "PatientHeightAtPrematureAT_ST");
			EP.CompareDynamicDropDownValues(TabName
					+ "PatientHeightAtDiscontinuationCZ_DD",
					EP.ORData(TabName + "PatientHeightAtDiscontinuationCZ_DD"),
					false);
			EP.VerifyText(TabName + "DisContLastStudyDayAT_ST");
			EP.VerifyToolTip(TabName + "DiscontinuedLastStudyDayCalendarAT_TT");
			EP.VerifyToolTip(TabName + "DiscontinuedTreatmentStopDateAT_IM");
			EP.VerifyText(TabName + "DiscontinuedMeasurementAT_ST");
			EP.VerifyText(TabName + "DiscontinuedTreatmentStopDateAT_ST");
			EP.VerifyTextContained(TabName + "MeasurementCmAT_ST");
			EP.ClearStaticScreenshot();

			// Validation of "Other, Specify" error messages

			EP.DrpDwn_SelectIndex(TabName + "StatusOfStudy_DD", 2);
			EP.DrpDwn_SelectIndex(TabName + "DiscontinuationReasonCZ_DD", 6);
			EP.Edit_ClearValue(TabName + "DiscontinuationReasonAT_EB");
			EP.DrpDwn_SelectIndex(TabName + "InitiatorOfWithdrawalCZ_DD", 5);
			EP.Edit_ClearValue(TabName + "InitiatorOfWithdrawalAT_EB");
			EP.SelectDate(TabName + "DiscontinuedTreatmentStopDateAT_IM", EP
					.Date_AddDays(EP.TestData("ECOSPatientDOB"), 2,
							Calendar.MONTH));
			EP.SelectDate(TabName + "DiscontinuedLastStudyDayCalendarAT_TT", EP
					.Date_AddDays(EP.TestData("ECOSPatientDOB"), 2,
							Calendar.MONTH));
			EP.DrpDwn_SelectIndex(TabName
					+ "PatientHeightAtDiscontinuationCZ_DD", 3);
			EP.Edit_SetValue(TabName + "Measurement_EB", 56);
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("MandatoryDiscontinued");
			EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
					EP.GetText(TabName + "ValueOutOfRangeErrorES_DT"));
			EP.CompareText("FieldIsMandatory", EP.LangData("FieldIsMandatory"),
					GetErrorMessage(1));
			EP.CompareText("FieldIsMandatory", EP.LangData("FieldIsMandatory"),
					GetErrorMessage(2));
			EP.CompareText("Ignoreandsave", EP.LangData("IgnoreAndSave"),
					EP.GetText(TabName + "Save_BN"));
			EP.ClearStaticScreenshot();
			EP.Btn_Click(TabName + "Cancel_BN");
			EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");

			// Measurement range value and Date related error messages/Treatment
			// Discontinuation Details madatory field
			EP.DrpDwn_SelectIndex(TabName + "StatusOfStudy_DD", 2);
			EP.DrpDwn_SelectIndex(TabName + "DiscontinuationReasonCZ_DD", 1);
			EP.DrpDwn_SelectIndex(TabName + "InitiatorOfWithdrawalCZ_DD", 1);
			EP.DrpDwn_SelectIndex(TabName
					+ "PatientHeightAtDiscontinuationCZ_DD", 3);
			EP.DrpDwn_SelectIndex(TabName
					+ "TreatmentDiscontinuationDetailsCZ_DD", 0);
			EP.SelectDate(TabName + "DiscontinuedTreatmentStopDateAT_IM",
					EP.TestData("ECOSPatientDOB"));
			EP.SelectDate(TabName + "DiscontinuedLastStudyDayCalendarAT_TT",
					EP.TestData("ECOSPatientDOB"));
			EP.Edit_SetValue(TabName + "Measurement_EB", MinValue);
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("OutOfRangeError");
			EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
					EP.GetText(TabName + "ValueOutOfRangeErrorES_DT"));
			EP.CompareText("LastStudyDateError",
					EP.LangData("LastStudyDateError"), GetErrorMessage(1));
			EP.CompareText("TreatmentStopDate ",
					EP.LangData("LastStudyDateError"), GetErrorMessage(2));
			EP.VerifyRegExpression("Height", EP.LangData("RangeValue"),
					GetErrorMessage(3), "\\d{2,3}");
			EP.CompareText("Ignoreandsave", EP.LangData("IgnoreAndSave"),
					EP.GetText(TabName + "Save_BN"));
			EP.ClearStaticScreenshot();
			EP.Btn_Click(TabName + "Cancel_BN");

			// Incorrect Formats
			EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");
			EP.Edit_ClearValue(TabName + "MeasurementAT_EB");
			EP.Edit_SetValue(TabName + "MeasurementAT_EB", IncorrectFormat);
			EP.DrpDwn_SelectIndex(TabName + "DiscontinuationReasonCZ_DD", 5);
			EP.DrpDwn_SelectIndex(TabName
					+ "PatientHeightAtDiscontinuationCZ_DD", 3);
			EP.DrpDwn_SelectIndex(TabName + "InitiatorOfWithdrawalCZ_DD", 4);
			EP.SelectDate(TabName + "DiscontinuedTreatmentStopDateAT_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate(TabName + "DiscontinuedLastStudyDayCalendarAT_TT",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("MeasurementFormatError");
			EP.VerifyText(TabName + "IncorrecrFormatErrorES_DT");
			EP.CompareText("IncorrectFormat", EP.LangData("IncorrectFormat"),
					GetErrorMessage(1));
			EP.ClearStaticScreenshot();
			EP.Btn_Click(TabName + "Cancel_BN");
			EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");

			// Error for Treatment Stop Date lesser than treatment Start Date

			EP.DrpDwn_SelectIndex(TabName + "DiscontinuationReasonCZ_DD", 5);
			EP.DrpDwn_SelectIndex(TabName
					+ "PatientHeightAtDiscontinuationCZ_DD", 3);
			EP.DrpDwn_SelectIndex(TabName + "InitiatorOfWithdrawalCZ_DD", 4);
			EP.SelectDate(TabName + "DiscontinuedTreatmentStopDateAT_IM",
					TreatmentStartDateCZ);
			EP.SelectDate(TabName + "DiscontinuedLastStudyDayCalendarAT_TT",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.Edit_SetValue(TabName + "MeasurementAT_EB", 60);
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("TSDLessThanCurrentGH");
			EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
					EP.GetText(TabName + "ValueOutOfRangeErrorES_DT"));
			EP.VerifyRegExpression("TSDLessThanCurrentGH",
					EP.LangData("TSDLessThanCurrentGH"), GetErrorMessage(1),
					EP.TranslateDate(TreatmentStartDate, "dd MMM yyyy", false));
			EP.CompareText("Ignoreandsave", EP.LangData("IgnoreAndSave"),
					EP.GetText(TabName + "Save_BN"));
			EP.ClearStaticScreenshot();
			EP.Btn_Click(TabName + "Cancel_BN");
			EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");

			// To verify success message for Discontinued
			EP.DrpDwn_SelectIndex(TabName + "DiscontinuationReasonCZ_DD",
					EP.RandomValue(2, 5));
			EP.DrpDwn_SelectIndex(TabName + "InitiatorOfWithdrawalCZ_DD",
					EP.RandomValue(1, 4));
			EP.Edit_SetValue(TabName + "Measurement_EB", "56");
			EP.SelectDate(TabName + "DiscontinuedTreatmentStopDateAT_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate(TabName + "DiscontinuedLastStudyDayCalendarAT_TT",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.TakeStaticScreenshot("MonthVerification");
			EP.VerifyMonth(TabName + "DiscontinuedTreatmentStopDateAT_EB");
			EP.VerifyMonth(TabName + "DiscontinuedLastStudyDayAT_EB");
			EP.ClearStaticScreenshot();
			EP.Btn_Click(TabName + "Save_BN");
			ECOS.VerifySuccessMessage(EP.LangData("CompletionOrTermination"));

			break;

		case HUNGARY:
		case NORWAY:

			// Completed Section
			EP.DrpDwn_SelectIndex(TabName + "StatusOfStudy_DD", 1);
			EP.TakeStaticScreenshot("StudyCompleted");
			EP.VerifyText(TabName + "StudyCompletionAT_ST");
			EP.CompareDropDownValues(TabName + "StudyCompletionCZ_DD");
			EP.VerifyText(TabName + "PatientHeightAtEndOfStudyAT_ST");
			EP.CompareDropDownValues(TabName + "SubjectHeightCZ_DD");
			EP.VerifyText(TabName + "DateOfMeasurementAT_ST");
			EP.VerifyToolTip(TabName + "DateOFMeasurementCalendarAT_TT");
			EP.VerifyText(TabName + "MeasurementAT_ST");
			EP.VerifyTextContained(TabName + "MeasurementCmScaleAT_ST");
			EP.VerifyText(TabName + "LastStudyDayAT_ST");
			EP.VerifyToolTip(TabName + "LastStudyDayCalendarAT_TT");
			EP.VerifyText(TabName + "ContinuedTreatmentStopDateAT_ST");
			EP.VerifyToolTip(TabName + "ContinuedTreatmentStopDateAT_IM");
			EP.VerifyPageLayout();

			String TreatmentStartDateHU = EP.Date_AddDays(
					EP.GetTimeStamp("dd MMM yyyy"), -2, Calendar.DATE);
			// Date and Measurement Range Error Message Validations
			EP.DrpDwn_SelectIndex(TabName + "StatusOfStudy_DD", 1);
			EP.DrpDwn_SelectIndex(TabName + "StudyCompletionCZ_DD", 2); // Study
																		// Completion
			EP.DrpDwn_SelectIndex(TabName + "SubjectHeightCZ_DD", 3);
			EP.Edit_SetValue(TabName + "MeasurementCompletedAT_EB", MinValue);
			EP.SelectDate(TabName + "ContinuedTreatmentStopDateAT_IM",
					EP.TestData("ECOSPatientDOB"));
			EP.SelectDate(TabName + "LastStudyDayCalendarAT_TT",
					EP.TestData("ECOSPatientDOB"));
			EP.SelectDate(TabName + "DateOFMeasurementCalendarAT_TT",
					EP.TestData("ECOSPatientDOB"));

			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("IncorrectFormatError1");
			EP.VerifyText(TabName + "IncorrecrFormatErrorES_DT");// Header error
																	// msg
			EP.CompareText("StudyCompletionErrMsg_DT",
					EP.LangData("AccordanceWithAnother"), GetErrorMessage(1));
			EP.CompareText("PatientHeightErrMsg_DT",
					EP.LangData("AccordanceWithAnother"), GetErrorMessage(2));
			EP.VerifyRegExpression("MothersHeight_RangeError",
					EP.LangData("RangeValue"), GetErrorMessage(3), "\\d{1,3}");
			EP.CompareText("LastStudyDayErrMsg_DT",
					EP.LangData("LastStudyDateError"), GetErrorMessage(4));
			EP.CompareText("TreatmentStopDate",
					EP.LangData("LastStudyDateError"), GetErrorMessage(5));
			EP.ClearStaticScreenshot();

			// Final height is smaller than the latest visit height
			EP.DrpDwn_SelectIndex(TabName + "StudyCompletionCZ_DD", 1);
			EP.DrpDwn_SelectIndex(TabName + "SubjectHeightCZ_DD", 1);
			EP.Edit_ClearValue(TabName + "MeasurementCompleted_EB");
			EP.SelectDate(TabName + "LastStudyDayCalendarAT_TT",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate(TabName + "DateOFMeasurementCalendarAT_TT", EP
					.Date_AddDays(EP.TestData("ECOSPatientDOB"), 2,
							Calendar.MONTH));
			EP.SelectDate(TabName + "ContinuedTreatmentStopDateAT_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.Edit_SetValue(TabName + "MeasurementCompletedAT_EB", 55);
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("PatientHeightErrMsg_DT");
			EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
					EP.GetText(TabName + "ValueOutOfRangeErrorES_DT"));
			EP.CompareText("PatientHeightErrMsg_DT",
					EP.LangData("FinalHeightSmaller"), GetErrorMessage(1));
			EP.CompareText("Ignoreandsave", EP.LangData("IgnoreAndSave"),
					EP.GetText(TabName + "Save_BN"));
			EP.ClearStaticScreenshot();
			EP.Btn_Click(TabName + "Cancel_BN");
			EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");

			// Patient is female and final height recorded and age should be
			// greater than 14 years old
			EP.DrpDwn_SelectIndex(TabName + "StatusOfStudy_DD", 1);
			EP.DrpDwn_SelectIndex(TabName + "StudyCompletionCZ_DD", 1);
			EP.DrpDwn_SelectIndex(TabName + "SubjectHeightCZ_DD", 1);
			EP.Edit_SetValue(TabName + "MeasurementCompleted_EB", "100");
			EP.SelectDate(TabName + "ContinuedTreatmentStopDateAT_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate(TabName + "LastStudyDayCalendarAT_TT",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate(TabName + "DateOFMeasurementCalendarAT_TT", EP
					.Date_AddDays(EP.TestData("ECOSPatientDOB"), 2,
							Calendar.MONTH));
			EP.Wait(2);
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("AgeMustBeGreaterThan14");
			EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
					EP.GetText(TabName + "ValueOutOfRangeErrorES_DT"));
			EP.CompareText("PatientHeightErrMsg_DT",
					EP.LangData("AgeMustBeGreaterThan14"),
					EP.GetText(TabName + "PatientHeightErrMsgAT_DT"));
			EP.CompareText("Ignoreandsave", EP.LangData("IgnoreAndSave"),
					EP.GetText(TabName + "Save_BN"));
			EP.ClearStaticScreenshot();
			EP.Btn_Click(TabName + "Cancel_BN");
			EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");

			// Incorrect format for measurement
			EP.DrpDwn_SelectIndex(TabName + "StudyCompletionCZ_DD", 3);
			EP.DrpDwn_SelectIndex(TabName + "SubjectHeightCZ_DD", 3);
			EP.Edit_SetValue(TabName + "MeasurementCompleted_EB",
					IncorrectFormat);
			EP.SelectDate(TabName + "ContinuedTreatmentStopDateAT_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate(TabName + "LastStudyDayCalendarAT_TT",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate(TabName + "DateOFMeasurementCalendarAT_TT",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("incorrectFormatMeasurement");
			EP.CompareText("IncorrecrFormatError_DT",
					EP.LangData("VerifyIncorrectFormats"),
					EP.GetText(TabName + "IncorrecrFormatErrorES_DT"));
			EP.CompareText("MeasurementErrMsg_DT",
					EP.LangData("IncorrectFormat"), GetErrorMessage(1));
			EP.ClearStaticScreenshot();
			EP.DrpDwn_SelectIndex(TabName + "StudyCompletionCZ_DD", 2);
			EP.DrpDwn_SelectIndex(TabName + "SubjectHeightCZ_DD", 2);
			EP.Edit_SetValue(TabName + "MeasurementCompleted_EB", 55);
			EP.SelectDate(TabName + "ContinuedTreatmentStopDateAT_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate(TabName + "LastStudyDayCalendarAT_TT",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate(TabName + "DateOFMeasurementCalendarAT_TT",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("nearfinalSmaller");
			EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
					EP.GetText(TabName + "ValueOutOfRangeErrorES_DT"));
			EP.CompareText("PatientHeightErrMsg_DT",
					EP.LangData("NearFinalSmaller"), GetErrorMessage(1));
			EP.CompareText("Ignoreandsave", EP.LangData("IgnoreAndSave"),
					EP.GetText(TabName + "Save_BN"));
			EP.ClearStaticScreenshot();
			EP.Btn_Click(TabName + "Cancel_BN");
			EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");

			// Error for LAst Visit
			EP.NavigateTo(RecoveringURL);
			TreatmentStartDate = EP.Date_AddDays(EP.Date_AddDays(
					EP.TestData("ECOSPatientDOB"), 2, Calendar.MONTH), -1,
					Calendar.DATE);
			EP.DrpDwn_SelectIndex(TabName + "StudyCompletionCZ_DD", 3);// 1
			EP.DrpDwn_SelectIndex(TabName + "SubjectHeightCZ_DD", 3);// 1
			EP.Edit_SetValue(TabName + "MeasurementCompleted_EB", 60);
			EP.SelectDate(TabName + "ContinuedTreatmentStopDateAT_IM",
					TreatmentStartDateHU);
			EP.SelectDate(TabName + "LastStudyDayCalendarAT_TT",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate(TabName + "DateOFMeasurementCalendarAT_TT", EP
					.Date_AddDays(EP.TestData("ECOSPatientDOB"), 2,
							Calendar.MONTH));
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("TSDLessThanCurrentGH");
			EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
					EP.GetText(TabName + "IncorrecrFormatErrorES_DT"));
			EP.VerifyRegExpression("TSDLessThanCurrentGH",
					EP.LangData("TSDLessThanCurrentGH"), GetErrorMessage(1),
					EP.TranslateDate(TreatmentStartDate, "dd MMM yyyy", false));
			EP.CompareText("Ignoreandsave", EP.LangData("IgnoreAndSave"),
					EP.GetText(TabName + "Save_BN"));
			EP.ClearStaticScreenshot();
			EP.Btn_Click(TabName + "Cancel_BN");
			EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");
			EP.NavigateTo(Env.ECOSURL);
			// -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

			// Verify the text in populate previous values popup (Last visit)
			EP.SearchForPatient(EP.TestData("PatientName") + " 01");
			EP.Rbtn_SetOption("#MyPatients_FirstPatient_RB", "ON");
			EP.Btn_Click("#MyPatients_VisitsRecord_BN");
			EP.WbTbl_SortColumn("#AddVisit_VisitDate_TE", 2, "DESC");// Bring
																		// the
																		// latest
																		// record
																		// on
																		// top
			Date = EP.WbTbl_GetText("#AddVisit_VisitDate_TE", 1, 2);

			// Error for Treatment Stop Date lesser than treatment Start Date
			EP.NavigateTo(RecoveringURL);
			EP.DrpDwn_SelectIndex(TabName + "StudyCompletionCZ_DD", 3);// 1
			EP.DrpDwn_SelectIndex(TabName + "SubjectHeightCZ_DD", 3);// 1
			EP.Edit_SetValue(TabName + "MeasurementCompleted_EB", 60);
			EP.SelectDate(TabName + "ContinuedTreatmentStopDateAT_IM", EP
					.Date_AddDays(EP.TranslateDate(Date, "dd MMM yyyy", true),
							-1, Calendar.DATE));
			EP.SelectDate(TabName + "LastStudyDayCalendarAT_TT", EP
					.Date_AddDays(EP.TranslateDate(Date, "dd MMM yyyy", true),
							-1, Calendar.DATE));
			EP.SelectDate(TabName + "DateOFMeasurementCalendarAT_TT", EP
					.Date_AddDays(EP.TestData("ECOSPatientDOB"), 2,
							Calendar.MONTH));
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("TSDLessThanCurrentGH");
			EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
					EP.GetText(TabName + "IncorrecrFormatErrorES_DT"));
			EP.CompareText("DateAfterLastVisit",
					EP.LangData("DateAfterLastVisit"), GetErrorMessage(1));
			EP.CompareText("DateAfterLastVisit",
					EP.LangData("DateAfterLastVisit"), GetErrorMessage(2));
			EP.CompareText("Ignoreandsave", EP.LangData("IgnoreAndSave"),
					EP.GetText(TabName + "Save_BN"));
			EP.ClearStaticScreenshot();
			EP.Btn_Click(TabName + "Cancel_BN");
			EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");

			// Success message for Completion
			EP.DrpDwn_SelectIndex(TabName + "StudyCompletionCZ_DD", 3);// 1
			EP.DrpDwn_SelectIndex(TabName + "SubjectHeightCZ_DD", 3);// 1
			EP.Edit_SetValue(TabName + "MeasurementCompleted_EB", 60);
			EP.SelectDate(TabName + "ContinuedTreatmentStopDateAT_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate(TabName + "LastStudyDayCalendarAT_TT",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate(TabName + "DateOFMeasurementCalendarAT_TT",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.VerifyMonth(TabName + "TreatmentStopDateAT_EB");
			EP.VerifyMonth(TabName + "DateOfMeasurementAT_EB");
			EP.VerifyMonth(TabName + "LastStudyDayAT_EB");
			EP.Btn_Click(TabName + "Save_BN");
			ECOS.VerifySuccessMessage(EP.LangData("CompletionOrTermination"));

			// To check error messgage for male patient - FinalHeightDifference
			EP.SearchForPatient(EP.TestData("ECOSPatient"));
			EP.Rbtn_SetOption("#MyPatients_FirstPatient_RB", "ON");
			EP.Btn_Click("#MyPatients_BaselineRecord_BN");
			EP.Lnk_Click("#NavigationTab_RecordOverview_LK");
			EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");
			EP.DrpDwn_SelectIndex(TabName + "StatusOfStudy_DD", 1);
			EP.DrpDwn_SelectIndex(TabName + "StudyCompletionCZ_DD", 1);
			EP.DrpDwn_SelectIndex(TabName + "SubjectHeightCZ_DD", 1);
			EP.Edit_SetValue(TabName + "MeasurementCompleted_EB", "100");
			EP.Wait(2);
			EP.SelectDate(TabName + "ContinuedTreatmentStopDateAT_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate(TabName + "LastStudyDayCalendarAT_TT",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate(TabName + "DateOFMeasurementCalendarAT_TT",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("FinalHeightDifference");
			EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
					EP.GetText(TabName + "ValueOutOfRangeErrorES_DT"));
			EP.CompareText("FinalHeightDifference",
					EP.LangData("FinalHeightDifference"), GetErrorMessage(1));
			EP.CompareText("Ignoreandsave", EP.LangData("IgnoreAndSave"),
					EP.GetText(TabName + "Save_BN"));
			EP.ClearStaticScreenshot();

			EP.Btn_Click(TabName + "Cancel_BN");
			EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");

			// To check error messgage for male patient -
			// FinalHeightDifference1CM

			EP.DrpDwn_SelectIndex(TabName + "StatusOfStudy_DD", 1);
			EP.DrpDwn_SelectIndex(TabName + "StudyCompletionCZ_DD", 1);
			EP.DrpDwn_SelectIndex(TabName + "SubjectHeightCZ_DD", 2);
			EP.Edit_SetValue(TabName + "MeasurementCompleted_EB", "100");
			EP.Wait(2);
			EP.SelectDate(TabName + "ContinuedTreatmentStopDateAT_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate(TabName + "LastStudyDayCalendarAT_TT",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate(TabName + "DateOFMeasurementCalendarAT_TT",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("FinalHeightDifference1CM");
			EP.CompareText("IncorrecrFormatError_DT",
					EP.LangData("VerifyIncorrectFormats"),
					EP.GetText(TabName + "IncorrecrFormatErrorES_DT"));
			EP.CompareText("AccordanceWithAnother",
					EP.LangData("AccordanceWithAnother"), GetErrorMessage(1));
			EP.CompareText("FinalHeightDifference1CM",
					EP.LangData("FinalHeightDifference1CM"), GetErrorMessage(2));
			EP.CompareText("Save", EP.LangData("Save"),
					EP.GetText(TabName + "Save_BN"));
			EP.ClearStaticScreenshot();

			// Delete the Visit for John Doe
			EP.Btn_Click(TabName + "Cancel_BN");
			EP.Lnk_Click("#NavigationTab_VisitsRecord_LK");
			EP.WbTbl_SortColumn("#AddVisit_VisitDate_TE", 2, "DESC");
			EP.WbTbl_SelectRbtn("#AddVisit_VisitDate_TE", 2);
			EP.Btn_Click("#AddVisit_Continue_BN");
			if (EP.ObjectExists("#General_Delete_IM")) {
				DeleteRecord("#General_Delete_IM", false);
			}

			// To check error messgage for male patient - AgeMustBeGreaterThan15

			EP.Lnk_Click("#NavigationTab_RecordOverview_LK");
			EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");
			EP.DrpDwn_SelectIndex(TabName + "StatusOfStudy_DD", 1);
			EP.DrpDwn_SelectIndex(TabName + "StudyCompletionCZ_DD", 1);
			EP.DrpDwn_SelectIndex(TabName + "SubjectHeightCZ_DD", 1);
			EP.Edit_SetValue(TabName + "MeasurementCompleted_EB", "100");
			EP.Wait(2);
			EP.SelectDate(TabName + "ContinuedTreatmentStopDateAT_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate(TabName + "LastStudyDayCalendarAT_TT",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate(TabName + "DateOFMeasurementCalendarAT_TT",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("AgeMustBeGreaterThan15Error");
			EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
					EP.GetText(TabName + "ValueOutOfRangeErrorES_DT"));
			EP.CompareText("PatientHeightErrMsg_DT",
					EP.LangData("AgeMustBeGreaterThan15"), GetErrorMessage(1));
			EP.CompareText("Ignoreandsave", EP.LangData("IgnoreAndSave"),
					EP.GetText(TabName + "Save_BN"));
			EP.ClearStaticScreenshot();

			// ****************DIS CONTINUED
			// SECTION*********************************
			if (EP.ObjectExists("#NavigationTab_MyPatientsList_LK"))
				;
			EP.Lnk_Click("#NavigationTab_MyPatientsList_LK");
			EP.SearchForPatient(EP.TestData("PatientName") + " 04");
			EP.Rbtn_SetOption("#MyPatients_FirstPatient_RB", "ON");
			EP.Btn_Click("#MyPatients_BaselineRecord_BN");
			EP.Lnk_Click("#NavigationTab_RecordOverview_LK");
			EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");
			EP.DrpDwn_SelectIndex(TabName + "StatusOfStudy_DD", 2);
			EP.TakeStaticScreenshot("StudyDiscontinued");
			EP.VerifyText(TabName + "PrematureStudyDiscontinuationReasonAT_ST");
			EP.CompareDynamicDropDownValues(TabName
					+ "DiscontinuationReasonCZ_DD",
					EP.ORData(TabName + "DiscontinuationReasonCZ_DD"), false);
			EP.DrpDwn_SelectIndex(TabName + "DiscontinuationReasonCZ_DD", 2);// To
																				// make
																				// visible
																				// Remove
																				// data
																				// from
																				// Study?

			EP.VerifyText(TabName + "RemoveDataFromStudyAT_ST");
			EP.VerifyText(TabName + "RemoveDataYesAT_ST");
			EP.VerifyText(TabName + "RemoveDataNoAT_ST");

			EP.VerifyText(TabName + "InitiatorOfWithdrawalAT_ST");
			EP.CompareDynamicDropDownValues(TabName
					+ "InitiatorOfWithdrawalCZ_DD",
					EP.ORData(TabName + "InitiatorOfWithdrawalCZ_DD"), false);
			EP.VerifyText(TabName + "PatientHeightAtPrematureAT_ST");
			EP.CompareDynamicDropDownValues(TabName
					+ "PatientHeightAtDiscontinuationCZ_DD",
					EP.ORData(TabName + "PatientHeightAtDiscontinuationCZ_DD"),
					false);
			EP.VerifyText(TabName + "DisContLastStudyDayAT_ST");
			EP.VerifyToolTip(TabName + "DiscontinuedLastStudyDayCalendarAT_TT");
			EP.VerifyToolTip(TabName + "DiscontinuedTreatmentStopDateAT_IM");
			EP.VerifyText(TabName + "DiscontinuedMeasurementAT_ST");
			EP.VerifyText(TabName + "DiscontinuedTreatmentStopDateAT_ST");
			EP.VerifyTextContained(TabName + "MeasurementCmAT_ST");
			EP.ClearStaticScreenshot();

			// Validation of "Other, Specify" error messages

			EP.DrpDwn_SelectIndex(TabName + "StatusOfStudy_DD", 2);
			EP.DrpDwn_SelectIndex(TabName + "DiscontinuationReasonCZ_DD", 6);
			EP.Edit_ClearValue(TabName + "DiscontinuationReasonAT_EB");
			EP.DrpDwn_SelectIndex(TabName + "InitiatorOfWithdrawalCZ_DD", 5);
			EP.Edit_ClearValue(TabName + "InitiatorOfWithdrawalAT_EB");
			EP.SelectDate(TabName + "DiscontinuedTreatmentStopDateAT_IM", EP
					.Date_AddDays(EP.TestData("ECOSPatientDOB"), 2,
							Calendar.MONTH));
			EP.SelectDate(TabName + "DiscontinuedLastStudyDayCalendarAT_TT", EP
					.Date_AddDays(EP.TestData("ECOSPatientDOB"), 2,
							Calendar.MONTH));
			EP.DrpDwn_SelectIndex(TabName
					+ "PatientHeightAtDiscontinuationCZ_DD", 3);
			EP.Edit_SetValue(TabName + "Measurement_EB", 56);
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("MandatoryDiscontinued");
			EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
					EP.GetText(TabName + "ValueOutOfRangeErrorES_DT"));
			EP.CompareText("FieldIsMandatory", EP.LangData("FieldIsMandatory"),
					GetErrorMessage(1));
			EP.CompareText("FieldIsMandatory", EP.LangData("FieldIsMandatory"),
					GetErrorMessage(2));
			EP.CompareText("Ignoreandsave", EP.LangData("IgnoreAndSave"),
					EP.GetText(TabName + "Save_BN"));
			EP.ClearStaticScreenshot();
			EP.Btn_Click(TabName + "Cancel_BN");
			EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");

			// Measurement range value and Date related error messages/Treatment
			// Discontinuation Details madatory field
			EP.DrpDwn_SelectIndex(TabName + "StatusOfStudy_DD", 2);
			EP.DrpDwn_SelectIndex(TabName + "DiscontinuationReasonCZ_DD", 1);
			EP.DrpDwn_SelectIndex(TabName + "InitiatorOfWithdrawalCZ_DD", 1);
			EP.DrpDwn_SelectIndex(TabName
					+ "PatientHeightAtDiscontinuationCZ_DD", 3);
			EP.DrpDwn_SelectIndex(TabName
					+ "TreatmentDiscontinuationDetailsCZ_DD", 0);
			EP.SelectDate(TabName + "DiscontinuedTreatmentStopDateAT_IM",
					EP.TestData("ECOSPatientDOB"));
			EP.SelectDate(TabName + "DiscontinuedLastStudyDayCalendarAT_TT",
					EP.TestData("ECOSPatientDOB"));
			EP.Edit_SetValue(TabName + "Measurement_EB", MinValue);
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("OutOfRangeError");
			EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
					EP.GetText(TabName + "ValueOutOfRangeErrorES_DT"));
			EP.CompareText("LastStudyDateError",
					EP.LangData("LastStudyDateError"), GetErrorMessage(1));
			EP.CompareText("TreatmentStopDate ",
					EP.LangData("LastStudyDateError"), GetErrorMessage(2));
			EP.VerifyRegExpression("Height", EP.LangData("RangeValue"),
					GetErrorMessage(3), "\\d{2,3}");
			EP.CompareText("Ignoreandsave", EP.LangData("IgnoreAndSave"),
					EP.GetText(TabName + "Save_BN"));
			EP.ClearStaticScreenshot();
			EP.Btn_Click(TabName + "Cancel_BN");

			// Incorrect Formats
			EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");
			EP.Edit_ClearValue(TabName + "MeasurementAT_EB");
			EP.Edit_SetValue(TabName + "MeasurementAT_EB", IncorrectFormat);
			EP.DrpDwn_SelectIndex(TabName + "DiscontinuationReasonCZ_DD", 5);
			EP.DrpDwn_SelectIndex(TabName
					+ "PatientHeightAtDiscontinuationCZ_DD", 3);
			EP.DrpDwn_SelectIndex(TabName + "InitiatorOfWithdrawalCZ_DD", 4);
			EP.SelectDate(TabName + "DiscontinuedTreatmentStopDateAT_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate(TabName + "DiscontinuedLastStudyDayCalendarAT_TT",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("MeasurementFormatError");
			EP.VerifyText(TabName + "IncorrecrFormatErrorES_DT");
			EP.CompareText("IncorrectFormat", EP.LangData("IncorrectFormat"),
					GetErrorMessage(1));
			EP.ClearStaticScreenshot();
			EP.Btn_Click(TabName + "Cancel_BN");
			EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");

			// Error for Treatment Stop Date lesser than treatment Start Date

			EP.DrpDwn_SelectIndex(TabName + "DiscontinuationReasonCZ_DD", 5);
			EP.DrpDwn_SelectIndex(TabName
					+ "PatientHeightAtDiscontinuationCZ_DD", 3);
			EP.DrpDwn_SelectIndex(TabName + "InitiatorOfWithdrawalCZ_DD", 4);
			EP.SelectDate(TabName + "DiscontinuedTreatmentStopDateAT_IM",
					TreatmentStartDateHU);
			EP.SelectDate(TabName + "DiscontinuedLastStudyDayCalendarAT_TT",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.Edit_SetValue(TabName + "MeasurementAT_EB", 60);
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("TSDLessThanCurrentGH");
			EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
					EP.GetText(TabName + "ValueOutOfRangeErrorES_DT"));
			EP.VerifyRegExpression("TSDLessThanCurrentGH", EP
					.LangData("TSDLessThanCurrentGH"), GetErrorMessage(1), EP
					.TranslateDate(TreatmentStartDateHU, "dd MMM yyyy", false));
			EP.CompareText("Ignoreandsave", EP.LangData("IgnoreAndSave"),
					EP.GetText(TabName + "Save_BN"));
			EP.ClearStaticScreenshot();
			EP.Btn_Click(TabName + "Cancel_BN");
			EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");

			// To verify success message for Discontinued
			EP.DrpDwn_SelectIndex(TabName + "DiscontinuationReasonCZ_DD",
					EP.RandomValue(2, 5));
			EP.DrpDwn_SelectIndex(TabName + "InitiatorOfWithdrawalCZ_DD",
					EP.RandomValue(1, 4));
			EP.Edit_SetValue(TabName + "Measurement_EB", "56");
			EP.SelectDate(TabName + "DiscontinuedTreatmentStopDateAT_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate(TabName + "DiscontinuedLastStudyDayCalendarAT_TT",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.TakeStaticScreenshot("MonthVerification");
			EP.VerifyMonth(TabName + "DiscontinuedTreatmentStopDateAT_EB");
			EP.VerifyMonth(TabName + "DiscontinuedLastStudyDayAT_EB");
			EP.ClearStaticScreenshot();
			EP.Btn_Click(TabName + "Save_BN");
			ECOS.VerifySuccessMessage(EP.LangData("CompletionOrTermination"));

			break;

		case SLOVAKIA:

			// Completed Section

			EP.DrpDwn_SelectIndex(TabName + "StatusOfStudy_DD", 1);
			EP.TakeStaticScreenshot("StudyCompleted");
			EP.VerifyText(TabName + "StudyCompletionAT_ST");
			EP.CompareDropDownValues(TabName + "StudyCompletionGR_DD");
			EP.VerifyText(TabName + "PatientHeightAtEndOfStudyAT_ST");
			EP.CompareDropDownValues(TabName + "SubjectHeightSK_DD");
			EP.VerifyText(TabName + "DateOfMeasurementAT_ST");
			EP.VerifyToolTip(TabName + "DateOFMeasurementCalendarAT_TT");
			EP.VerifyText(TabName + "MeasurementAT_ST");
			EP.VerifyTextContained(TabName + "MeasurementCmScaleAT_ST");
			EP.VerifyText(TabName + "LastStudyDayAT_ST");
			EP.VerifyToolTip(TabName + "LastStudyDayCalendarAT_TT");
			EP.VerifyText(TabName + "ContinuedTreatmentStopDateAT_ST");
			EP.VerifyToolTip(TabName + "ContinuedTreatmentStopDateAT_IM");
			EP.VerifyPageLayout();

			// Error message when Please Select is selected in the first
			// dropdown
			EP.DrpDwn_SelectIndex(TabName + "StatusOfStudy_DD", 0);
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("FieldMandatory");
			EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
					EP.GetText(TabName + "ValueOutOfRangeErrorES_DT"));
			EP.CompareText("StatusOfStudy_DD_Mandatory",
					EP.LangData("FieldIsMandatory"), GetErrorMessage(1));
			EP.CompareText("Ignoreandsave", EP.LangData("IgnoreAndSave"),
					EP.GetText(TabName + "Save_BN"));
			EP.ClearStaticScreenshot();
			EP.Btn_Click(TabName + "Cancel_BN");
			EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");

			// Dynamic Contents with Status = Completed
			// Verifying the mandatory fields of Completed
			EP.DrpDwn_SelectIndex(TabName + "StatusOfStudy_DD", 1);
			EP.DrpDwn_SelectIndex(TabName + "StudyCompletionSK_DD", 0); // Study
																		// Completion
			EP.DrpDwn_SelectIndex(TabName + "SubjectHeightSK_DD", 0);
			EP.Edit_SetValueByJavaScript(TabName + "DateOfMeasurementAT_EB", "");
			EP.Edit_SetValueByJavaScript(TabName + "LastStudyDayAT_EB", "");
			EP.Edit_SetValueByJavaScript(TabName + "TreatmentStopDateAT_EB", "");
			EP.Edit_SetValue(TabName + "MeasurementCompletedAT_EB", "");
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("FieldMandatory");
			EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
					EP.GetText(TabName + "ValueOutOfRangeErrorES_DT"));
			EP.CompareText("StudyCompletion", EP.LangData("FieldIsMandatory"),
					GetErrorMessage(1));
			EP.CompareText("PatientHeightAtEndOfStudy",
					EP.LangData("FieldIsMandatory"), GetErrorMessage(2));
			EP.CompareText("DateOfMeasurement",
					EP.LangData("FieldIsMandatory"), GetErrorMessage(3));
			EP.CompareText("Measurement", EP.LangData("FieldIsMandatory"),
					GetErrorMessage(4));
			EP.CompareText("LastStudyDay", EP.LangData("FieldIsMandatory"),
					GetErrorMessage(5));
			// EP.CompareText("TreatmentStopDate",
			// EP.LangData("FieldIsMandatory"), GetErrorMessage(6));
			EP.CompareText("Ignoreandsave", EP.LangData("IgnoreAndSave"),
					EP.GetText(TabName + "Save_BN"));
			EP.ClearStaticScreenshot();
			EP.Btn_Click(TabName + "Cancel_BN");
			EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");

			// Date and Measurement Range Error Message Validations
			EP.DrpDwn_SelectIndex(TabName + "StatusOfStudy_DD", 1);
			EP.DrpDwn_SelectIndex(TabName + "StudyCompletionSK_DD", 2); // Study
																		// Completion
			EP.DrpDwn_SelectIndex(TabName + "SubjectHeightSK_DD", 3);
			EP.Edit_SetValue(TabName + "MeasurementCompletedAT_EB", MinValue);
			EP.SelectDate(TabName + "ContinuedTreatmentStopDateAT_IM",
					EP.TestData("ECOSPatientDOB"));
			EP.SelectDate(TabName + "LastStudyDayCalendarAT_TT",
					EP.TestData("ECOSPatientDOB"));
			EP.SelectDate(TabName + "DateOFMeasurementCalendarAT_TT",
					EP.TestData("ECOSPatientDOB"));

			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("IncorrectFormatError1");
			EP.VerifyText(TabName + "IncorrecrFormatErrorES_DT");// Header error
																	// msg
			EP.CompareText("StudyCompletionErrMsg_DT",
					EP.LangData("AccordanceWithAnother"), GetErrorMessage(1));
			EP.CompareText("PatientHeightErrMsg_DT",
					EP.LangData("AccordanceWithAnother"), GetErrorMessage(2));
			EP.VerifyRegExpression("MothersHeight_RangeError",
					EP.LangData("RangeValue"), GetErrorMessage(3), "\\d{1,3}");
			EP.CompareText("LastStudyDayErrMsg_DT",
					EP.LangData("LastStudyDateError"), GetErrorMessage(4));
			EP.CompareText("TreatmentStopDate",
					EP.LangData("LastStudyDateError"), GetErrorMessage(5));
			EP.ClearStaticScreenshot();

			// Final height is smaller than the latest visit height
			EP.DrpDwn_SelectIndex(TabName + "SubjectHeightSK_DD", 1);
			EP.Edit_ClearValue(TabName + "MeasurementCompleted_EB");
			EP.SelectDate(TabName + "LastStudyDayCalendarAT_TT", EP
					.Date_AddDays(EP.TestData("ECOSPatientDOB"), 2,
							Calendar.MONTH));
			EP.SelectDate(TabName + "DateOFMeasurementCalendarAT_TT", EP
					.Date_AddDays(EP.TestData("ECOSPatientDOB"), 2,
							Calendar.MONTH));
			EP.Edit_SetValue(TabName + "MeasurementCompletedAT_EB", 55);
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("IncorrectFormatError2");
			EP.CompareText("IncorrecrFormatError_DT",
					EP.LangData("VerifyIncorrectFormats"),
					EP.GetText(TabName + "IncorrecrFormatErrorES_DT"));
			EP.CompareText("PatientHeightErrMsg_DT",
					EP.LangData("FinalHeightSmaller"), GetErrorMessage(2));
			EP.ClearStaticScreenshot();

			// Patient is female and final height recorded and age should be
			// greater than 14 years old
			EP.DrpDwn_SelectIndex(TabName + "StatusOfStudy_DD", 1);
			EP.DrpDwn_SelectIndex(TabName + "StudyCompletionSK_DD", 1);
			EP.DrpDwn_SelectIndex(TabName + "SubjectHeightSK_DD", 1);
			EP.Edit_SetValue(TabName + "MeasurementCompleted_EB", "100");
			EP.SelectDate(TabName + "ContinuedTreatmentStopDateAT_IM", EP
					.Date_AddDays(EP.TestData("ECOSPatientDOB"), 2,
							Calendar.MONTH));
			EP.SelectDate(TabName + "DateOFMeasurementCalendarAT_TT", EP
					.Date_AddDays(EP.TestData("ECOSPatientDOB"), 2,
							Calendar.MONTH));
			EP.Wait(2);
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("AgeMustBeGreaterThan14");
			EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
					EP.GetText(TabName + "ValueOutOfRangeErrorES_DT"));
			EP.CompareText("PatientHeightErrMsg_DT",
					EP.LangData("AgeMustBeGreaterThan14"),
					EP.GetText(TabName + "PatientHeightErrMsgAT_DT"));
			EP.CompareText("Ignoreandsave", EP.LangData("IgnoreAndSave"),
					EP.GetText(TabName + "Save_BN"));
			EP.ClearStaticScreenshot();
			EP.Btn_Click(TabName + "Cancel_BN");
			EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");

			// Incorrect format for measurement
			EP.DrpDwn_SelectIndex(TabName + "StudyCompletionSK_DD", 2);
			EP.DrpDwn_SelectIndex(TabName + "SubjectHeightSK_DD", 2);
			EP.Edit_SetValue(TabName + "MeasurementCompleted_EB",
					IncorrectFormat);
			EP.SelectDate(TabName + "ContinuedTreatmentStopDateAT_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate(TabName + "LastStudyDayCalendarAT_TT",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate(TabName + "DateOFMeasurementCalendarAT_TT",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("incorrectFormatMeasurement");
			EP.CompareText("IncorrecrFormatError_DT",
					EP.LangData("VerifyIncorrectFormats"),
					EP.GetText(TabName + "IncorrecrFormatErrorES_DT"));
			EP.CompareText("MeasurementErrMsg_DT",
					EP.LangData("IncorrectFormat"), GetErrorMessage(2));
			EP.ClearStaticScreenshot();
			EP.DrpDwn_SelectIndex(TabName + "StudyCompletionSK_DD", 2);
			EP.DrpDwn_SelectIndex(TabName + "SubjectHeightSK_DD", 2);
			EP.Edit_SetValue(TabName + "MeasurementCompleted_EB", 55);
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("nearfinalSmaller");
			EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
					EP.GetText(TabName + "ValueOutOfRangeErrorES_DT"));
			EP.CompareText("PatientHeightErrMsg_DT",
					EP.LangData("NearFinalSmaller"), GetErrorMessage(1));
			EP.CompareText("Ignoreandsave", EP.LangData("IgnoreAndSave"),
					EP.GetText(TabName + "Save_BN"));
			EP.ClearStaticScreenshot();
			EP.Btn_Click(TabName + "Cancel_BN");
			EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");

			// Error for Treatment Stop Date lesser than treatment Start Date

			TreatmentStartDate = EP.Date_AddDays(EP.Date_AddDays(
					EP.TestData("ECOSPatientDOB"), 2, Calendar.MONTH), -1,
					Calendar.DATE);
			EP.DrpDwn_SelectIndex(TabName + "StudyCompletionSK_DD", 3);// 1
			EP.DrpDwn_SelectIndex(TabName + "SubjectHeightSK_DD", 3);// 1
			EP.Edit_SetValue(TabName + "MeasurementCompleted_EB", 60);
			EP.SelectDate(TabName + "ContinuedTreatmentStopDateAT_IM",
					TreatmentStartDate);
			EP.SelectDate(TabName + "LastStudyDayCalendarAT_TT", EP
					.Date_AddDays(EP.TestData("ECOSPatientDOB"), 2,
							Calendar.MONTH));
			EP.SelectDate(TabName + "DateOFMeasurementCalendarAT_TT", EP
					.Date_AddDays(EP.TestData("ECOSPatientDOB"), 2,
							Calendar.MONTH));
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("TSDLessThanCurrentGH");
			EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
					EP.GetText(TabName + "IncorrecrFormatErrorES_DT"));
			EP.VerifyRegExpression("TSDLessThanCurrentGH",
					EP.LangData("TSDLessThanCurrentGH"), GetErrorMessage(1),
					EP.TranslateDate(TreatmentStartDate, "dd MMM yyyy", false));
			EP.CompareText("Ignoreandsave", EP.LangData("IgnoreAndSave"),
					EP.GetText(TabName + "Save_BN"));
			EP.ClearStaticScreenshot();
			EP.Btn_Click(TabName + "Cancel_BN");
			EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");

			// Success message for Completion
			EP.DrpDwn_SelectIndex(TabName + "StudyCompletionSK_DD", 3);// 1
			EP.DrpDwn_SelectIndex(TabName + "SubjectHeightSK_DD", 3);// 1
			EP.Edit_SetValue(TabName + "MeasurementCompleted_EB", 60);
			EP.SelectDate(TabName + "ContinuedTreatmentStopDateAT_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate(TabName + "LastStudyDayCalendarAT_TT",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate(TabName + "DateOFMeasurementCalendarAT_TT",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.VerifyMonth(TabName + "TreatmentStopDateAT_EB");
			EP.VerifyMonth(TabName + "DateOfMeasurementAT_EB");
			EP.VerifyMonth(TabName + "LastStudyDayAT_EB");
			EP.Btn_Click(TabName + "Save_BN");
			ECOS.VerifySuccessMessage(EP.LangData("CompletionOrTermination"));

			// Success message when "Not Known" and "Not Available" are selected
			EP.NavigateTo(RecoveringURL);
			EP.DrpDwn_SelectIndex(TabName + "StudyCompletionSK_DD",
					EP.RandomValue(4, 5));// 1
			EP.DrpDwn_SelectIndex(TabName + "SubjectHeightSK_DD",
					EP.RandomValue(4, 5));// 1
			EP.Edit_SetValue(TabName + "MeasurementCompleted_EB", 60);
			EP.SelectDate(TabName + "ContinuedTreatmentStopDateAT_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate(TabName + "LastStudyDayCalendarAT_TT",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate(TabName + "DateOFMeasurementCalendarAT_TT",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.VerifyMonth(TabName + "TreatmentStopDateAT_EB");
			EP.VerifyMonth(TabName + "DateOfMeasurementAT_EB");
			EP.VerifyMonth(TabName + "LastStudyDayAT_EB");
			EP.Btn_Click(TabName + "Save_BN");
			ECOS.VerifySuccessMessage(EP.LangData("CompletionOrTermination"));

			// To check error messgage for male patient - FinalHeightDifference
			EP.SearchForPatient(EP.TestData("ECOSPatient"));
			EP.Rbtn_SetOption("#MyPatients_FirstPatient_RB", "ON");
			EP.Btn_Click("#MyPatients_BaselineRecord_BN");
			EP.Lnk_Click("#NavigationTab_RecordOverview_LK");
			EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");
			EP.DrpDwn_SelectIndex(TabName + "StatusOfStudy_DD", 1);
			EP.DrpDwn_SelectIndex(TabName + "StudyCompletionSK_DD", 1);
			EP.DrpDwn_SelectIndex(TabName + "SubjectHeightSK_DD", 1);
			EP.Edit_SetValue(TabName + "MeasurementCompleted_EB", "100");
			EP.Wait(2);
			EP.SelectDate(TabName + "ContinuedTreatmentStopDateAT_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate(TabName + "LastStudyDayCalendarAT_TT",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate(TabName + "DateOFMeasurementCalendarAT_TT",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("FinalHeightDifference");
			EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
					EP.GetText(TabName + "ValueOutOfRangeErrorES_DT"));
			EP.CompareText("FinalHeightDifference",
					EP.LangData("FinalHeightDifference"), GetErrorMessage(1));
			EP.CompareText("Ignoreandsave", EP.LangData("IgnoreAndSave"),
					EP.GetText(TabName + "Save_BN"));
			EP.ClearStaticScreenshot();

			EP.Btn_Click(TabName + "Cancel_BN");
			EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");

			// To check error messgage for male patient -
			// FinalHeightDifference1CM

			EP.DrpDwn_SelectIndex(TabName + "StatusOfStudy_DD", 1);
			EP.DrpDwn_SelectIndex(TabName + "StudyCompletionSK_DD", 1);
			EP.DrpDwn_SelectIndex(TabName + "SubjectHeightSK_DD", 2);
			EP.Edit_SetValue(TabName + "MeasurementCompleted_EB", "100");
			EP.Wait(2);
			EP.SelectDate(TabName + "ContinuedTreatmentStopDateAT_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate(TabName + "LastStudyDayCalendarAT_TT",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate(TabName + "DateOFMeasurementCalendarAT_TT",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("FinalHeightDifference1CM");
			EP.CompareText("IncorrecrFormatError_DT",
					EP.LangData("VerifyIncorrectFormats"),
					EP.GetText(TabName + "IncorrecrFormatErrorES_DT"));
			EP.CompareText("AccordanceWithAnother",
					EP.LangData("AccordanceWithAnother"), GetErrorMessage(1));
			EP.CompareText("FinalHeightDifference1CM",
					EP.LangData("FinalHeightDifference1CM"), GetErrorMessage(2));
			EP.CompareText("Save", EP.LangData("Save"),
					EP.GetText(TabName + "Save_BN"));
			EP.ClearStaticScreenshot();

			// Delete the Visit for John Doe
			EP.Btn_Click(TabName + "Cancel_BN");
			EP.Lnk_Click("#NavigationTab_VisitsRecord_LK");
			EP.WbTbl_SortColumn("#AddVisit_VisitDate_TE", 2, "DESC");
			EP.WbTbl_SelectRbtn("#AddVisit_VisitDate_TE", 2);
			EP.Btn_Click("#AddVisit_Continue_BN");
			if (EP.ObjectExists("#General_Delete_IM")) {
				DeleteRecord("#General_Delete_IM", false);
			}

			// To check error messgage for male patient - AgeMustBeGreaterThan15

			EP.Lnk_Click("#NavigationTab_RecordOverview_LK");
			EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");
			EP.DrpDwn_SelectIndex(TabName + "StatusOfStudy_DD", 1);
			EP.DrpDwn_SelectIndex(TabName + "StudyCompletionSK_DD", 1);
			EP.DrpDwn_SelectIndex(TabName + "SubjectHeightSK_DD", 1);
			EP.Edit_SetValue(TabName + "MeasurementCompleted_EB", "100");
			EP.Wait(2);
			EP.SelectDate(TabName + "ContinuedTreatmentStopDateAT_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate(TabName + "LastStudyDayCalendarAT_TT",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate(TabName + "DateOFMeasurementCalendarAT_TT",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("AgeMustBeGreaterThan15Error");
			EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
					EP.GetText(TabName + "ValueOutOfRangeErrorES_DT"));
			EP.CompareText("PatientHeightErrMsg_DT",
					EP.LangData("AgeMustBeGreaterThan15"), GetErrorMessage(1));
			EP.CompareText("Ignoreandsave", EP.LangData("IgnoreAndSave"),
					EP.GetText(TabName + "Save_BN"));
			EP.ClearStaticScreenshot();

			// ****************DIS CONTINUED
			// SECTION*********************************
			if (EP.ObjectExists("#NavigationTab_MyPatientsList_LK"))
				;
			EP.Lnk_Click("#NavigationTab_MyPatientsList_LK");
			EP.SearchForPatient(EP.TestData("PatientName") + " 04");
			EP.Rbtn_SetOption("#MyPatients_FirstPatient_RB", "ON");
			EP.Btn_Click("#MyPatients_BaselineRecord_BN");
			EP.Lnk_Click("#NavigationTab_RecordOverview_LK");
			EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");
			EP.DrpDwn_SelectIndex(TabName + "StatusOfStudy_DD", 2);
			EP.TakeStaticScreenshot("StudyDiscontinued");
			EP.VerifyText(TabName + "PrematureStudyDiscontinuationReasonAT_ST");
			EP.CompareDynamicDropDownValues(TabName
					+ "DiscontinuationReasonSK_DD",
					EP.ORData(TabName + "DiscontinuationReasonSK_DD"), false);
			EP.DrpDwn_SelectIndex(TabName + "DiscontinuationReasonSK_DD", 2);// To
																				// make
																				// visible
																				// Remove
																				// data
																				// from
																				// Study?

			EP.VerifyText(TabName + "RemoveDataFromStudyAT_ST");
			EP.VerifyText(TabName + "RemoveDataYesAT_ST");
			EP.VerifyText(TabName + "RemoveDataNoAT_ST");

			EP.VerifyText(TabName + "InitiatorOfWithdrawalAT_ST");
			EP.CompareDynamicDropDownValues(TabName
					+ "InitiatorOfWithdrawalSK_DD",
					EP.ORData(TabName + "InitiatorOfWithdrawalSK_DD"), false);
			EP.VerifyText(TabName + "PatientHeightAtPrematureAT_ST");
			EP.CompareDynamicDropDownValues(TabName
					+ "PatientHeightAtDiscontinuationSK_DD",
					EP.ORData(TabName + "PatientHeightAtDiscontinuationSK_DD"),
					false);
			EP.VerifyText(TabName + "DisContLastStudyDayAT_ST");
			EP.VerifyToolTip(TabName + "DiscontinuedLastStudyDayCalendarAT_TT");
			EP.VerifyToolTip(TabName + "DiscontinuedTreatmentStopDateAT_IM");
			EP.VerifyText(TabName + "DiscontinuedMeasurementAT_ST");
			EP.VerifyText(TabName + "DiscontinuedTreatmentStopDateAT_ST");
			EP.VerifyTextContained(TabName + "MeasurementCmAT_ST");
			EP.ClearStaticScreenshot();

			// Mandatory fields of discontinued section
			EP.DrpDwn_SelectIndex(TabName + "StatusOfStudy_DD", 2);
			EP.DrpDwn_SelectIndex(TabName + "DiscontinuationReasonSK_DD", 0);
			EP.DrpDwn_SelectIndex(TabName + "InitiatorOfWithdrawalSK_DD", 0);
			EP.Edit_SetValueByJavaScript(TabName
					+ "DiscontinuedLastStudyDayAT_EB", "");
			EP.Edit_SetValueByJavaScript(TabName
					+ "DiscontinuedTreatmentStopDateAT_EB", "");
			EP.Edit_ClearValue(TabName + "Measurement_EB");
			EP.DrpDwn_SelectIndex(TabName
					+ "PatientHeightAtDiscontinuationSK_DD", 0);
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("Mandatory");
			EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
					EP.GetText(TabName + "ValueOutOfRangeErrorES_DT"));
			EP.CompareText("PrematureStudyDiscontinuationReasonMandatory",
					EP.LangData("FieldIsMandatory"), GetErrorMessage(1));
			EP.CompareText("InitiatorofwithdrawalMandatory",
					EP.LangData("FieldIsMandatory"), GetErrorMessage(2));
			EP.CompareText("StudyDiscontinuationMandatory",
					EP.LangData("FieldIsMandatory"), GetErrorMessage(3));
			EP.CompareText("LastStudyDateMandatory",
					EP.LangData("FieldIsMandatory"), GetErrorMessage(4));
			EP.CompareText("MeasurementMandatory",
					EP.LangData("FieldIsMandatory"), GetErrorMessage(5));
			EP.CompareText("Ignoreandsave", EP.LangData("IgnoreAndSave"),
					EP.GetText(TabName + "Save_BN"));
			EP.ClearStaticScreenshot();
			EP.Btn_Click(TabName + "Cancel_BN");
			EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");

			// Measurement range value and Date related error messages/Treatment
			// Discontinuation Details madatory field
			EP.DrpDwn_SelectIndex(TabName + "StatusOfStudy_DD", 2);
			EP.DrpDwn_SelectIndex(TabName + "DiscontinuationReasonSK_DD", 1);
			EP.DrpDwn_SelectIndex(TabName + "InitiatorOfWithdrawalSK_DD", 1);
			EP.DrpDwn_SelectIndex(TabName
					+ "PatientHeightAtDiscontinuationSK_DD", 3);
			EP.DrpDwn_SelectIndex(TabName
					+ "TreatmentDiscontinuationDetailsSK_DD", 0);
			EP.SelectDate(TabName + "DiscontinuedTreatmentStopDateAT_IM",
					EP.TestData("ECOSPatientDOB"));
			EP.SelectDate(TabName + "DiscontinuedLastStudyDayCalendarAT_TT",
					EP.TestData("ECOSPatientDOB"));
			EP.Edit_SetValue(TabName + "Measurement_EB", MinValue);
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("OutOfRangeError");
			EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
					EP.GetText(TabName + "ValueOutOfRangeErrorES_DT"));
			EP.CompareText("TreatmentDiscontinuationDetailsMandatory",
					EP.LangData("FieldIsMandatory"), GetErrorMessage(1));
			EP.CompareText("LastStudyDateError",
					EP.LangData("LastStudyDateError"), GetErrorMessage(2));
			EP.CompareText("TreatmentStopDate ",
					EP.LangData("LastStudyDateError"), GetErrorMessage(3));
			EP.VerifyRegExpression("Height", EP.LangData("RangeValue"),
					GetErrorMessage(4), "\\d{2,3}");
			EP.CompareText("Ignoreandsave", EP.LangData("IgnoreAndSave"),
					EP.GetText(TabName + "Save_BN"));
			EP.ClearStaticScreenshot();
			EP.Btn_Click(TabName + "Cancel_BN");

			// Validation of "Other, Specify" error messages

			EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");
			EP.DrpDwn_SelectIndex(TabName + "StatusOfStudy_DD", 2);
			EP.DrpDwn_SelectIndex(TabName + "DiscontinuationReasonSK_DD", 6);
			EP.Edit_ClearValue(TabName + "DiscontinuationReasonAT_EB");
			EP.DrpDwn_SelectIndex(TabName + "InitiatorOfWithdrawalSK_DD", 5);
			EP.Edit_ClearValue(TabName + "InitiatorOfWithdrawalAT_EB");
			EP.SelectDate(TabName + "DiscontinuedTreatmentStopDateAT_IM", EP
					.Date_AddDays(EP.TestData("ECOSPatientDOB"), 2,
							Calendar.MONTH));
			EP.SelectDate(TabName + "DiscontinuedLastStudyDayCalendarAT_TT", EP
					.Date_AddDays(EP.TestData("ECOSPatientDOB"), 2,
							Calendar.MONTH));
			EP.DrpDwn_SelectIndex(TabName
					+ "PatientHeightAtDiscontinuationAT_DD", 3);
			EP.Edit_SetValue(TabName + "Measurement_EB", 56);
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("OutOfRangeError");
			EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
					EP.GetText(TabName + "ValueOutOfRangeErrorES_DT"));
			EP.CompareText("FieldIsMandatory", EP.LangData("FieldIsMandatory"),
					GetErrorMessage(1));
			EP.CompareText("FieldIsMandatory", EP.LangData("FieldIsMandatory"),
					GetErrorMessage(2));
			EP.CompareText("Ignoreandsave", EP.LangData("IgnoreAndSave"),
					EP.GetText(TabName + "Save_BN"));
			EP.ClearStaticScreenshot();
			EP.Btn_Click(TabName + "Cancel_BN");

			// Incorrect Formats
			EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");
			EP.Edit_ClearValue(TabName + "MeasurementAT_EB");
			EP.Edit_SetValue(TabName + "MeasurementAT_EB", IncorrectFormat);
			EP.DrpDwn_SelectIndex(TabName + "DiscontinuationReasonSK_DD", 5);
			EP.DrpDwn_SelectIndex(TabName
					+ "PatientHeightAtDiscontinuationAT_DD", 3);
			EP.DrpDwn_SelectIndex(TabName + "InitiatorOfWithdrawalSK_DD", 4);
			EP.SelectDate(TabName + "DiscontinuedTreatmentStopDateAT_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate(TabName + "DiscontinuedLastStudyDayCalendarAT_TT",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("MeasurementFormatError");
			EP.VerifyText(TabName + "IncorrecrFormatErrorES_DT");
			EP.CompareText("IncorrectFormat", EP.LangData("IncorrectFormat"),
					GetErrorMessage(1));
			EP.ClearStaticScreenshot();
			EP.Btn_Click(TabName + "Cancel_BN");
			EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");

			// Error for Treatment Stop Date lesser than treatment Start Date

			EP.DrpDwn_SelectIndex(TabName + "DiscontinuationReasonSK_DD", 5);
			EP.DrpDwn_SelectIndex(TabName
					+ "PatientHeightAtDiscontinuationAT_DD", 3);
			EP.DrpDwn_SelectIndex(TabName + "InitiatorOfWithdrawalSK_DD", 4);
			EP.SelectDate(TabName + "DiscontinuedTreatmentStopDateAT_IM",
					TreatmentStartDate);
			EP.SelectDate(TabName + "DiscontinuedLastStudyDayCalendarAT_TT",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.Edit_SetValue(TabName + "MeasurementAT_EB", 60);
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("TSDLessThanCurrentGH");
			EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
					EP.GetText(TabName + "ValueOutOfRangeErrorES_DT"));
			EP.VerifyRegExpression("TSDLessThanCurrentGH",
					EP.LangData("TSDLessThanCurrentGH"), GetErrorMessage(1),
					EP.TranslateDate(TreatmentStartDate, "dd MMM yyyy", false));
			EP.CompareText("Ignoreandsave", EP.LangData("IgnoreAndSave"),
					EP.GetText(TabName + "Save_BN"));
			EP.ClearStaticScreenshot();
			EP.Btn_Click(TabName + "Cancel_BN");
			EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");

			// To verify success message for Discontinued
			EP.DrpDwn_SelectIndex(TabName + "DiscontinuationReasonSK_DD",
					EP.RandomValue(2, 5));
			EP.DrpDwn_SelectIndex(TabName + "InitiatorOfWithdrawalSK_DD",
					EP.RandomValue(1, 4));
			EP.Edit_SetValue(TabName + "Measurement_EB", "56");
			EP.SelectDate(TabName + "DiscontinuedTreatmentStopDateAT_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate(TabName + "DiscontinuedLastStudyDayCalendarAT_TT",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.TakeStaticScreenshot("MonthVerification");
			EP.VerifyMonth(TabName + "DiscontinuedTreatmentStopDateAT_EB");
			EP.VerifyMonth(TabName + "DiscontinuedLastStudyDayAT_EB");
			EP.ClearStaticScreenshot();
			EP.Btn_Click(TabName + "Save_BN");
			ECOS.VerifySuccessMessage(EP.LangData("CompletionOrTermination"));

			// To verify success message for Discontinued with "Not Known" and
			// "Not Available" Option
			EP.SearchForPatient(EP.TestData("PatientName") + " 04");
			EP.Rbtn_SetOption("#MyPatients_FirstPatient_RB", "ON");
			EP.Btn_Click("#MyPatients_BaselineRecord_BN");
			EP.Lnk_Click("#NavigationTab_RecordOverview_LK");
			EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");
			EP.DrpDwn_SelectIndex(TabName + "DiscontinuationReasonSK_DD",
					EP.RandomValue(7, 8));
			EP.DrpDwn_SelectIndex(TabName + "InitiatorOfWithdrawalSK_DD",
					EP.RandomValue(6, 7));
			EP.Edit_SetValue(TabName + "Measurement_EB", "56");
			EP.SelectDate(TabName + "DiscontinuedTreatmentStopDateAT_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate(TabName + "DiscontinuedLastStudyDayCalendarAT_TT",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.TakeStaticScreenshot("MonthVerification");
			EP.VerifyMonth(TabName + "DiscontinuedTreatmentStopDateAT_EB");
			EP.VerifyMonth(TabName + "DiscontinuedLastStudyDayAT_EB");
			EP.ClearStaticScreenshot();
			EP.Btn_Click(TabName + "Save_BN");
			ECOS.VerifySuccessMessage(EP.LangData("CompletionOrTermination"));

			break;

		/*
		 * case SPAIN:
		 * 
		 * //Completed Section
		 * 
		 * EP.DrpDwn_SelectIndex(TabName+"StatusOfStudy_DD", 1);
		 * EP.TakeStaticScreenshot("StudyCompleted");
		 * EP.VerifyText(TabName+"StudyCompletionAT_ST");
		 * EP.CompareDropDownValues(TabName+"StudyCompletionAT_DD");
		 * EP.VerifyText(TabName+"PatientHeightAtEndOfStudyAT_ST");
		 * EP.CompareDropDownValues(TabName+"SubjectHeightAT_DD");
		 * EP.VerifyText(TabName+"DateOfMeasurementAT_ST");
		 * EP.VerifyToolTip(TabName+"DateOFMeasurementCalendarAT_TT");
		 * EP.VerifyText(TabName+"MeasurementAT_ST");
		 * EP.VerifyTextContained(TabName+"MeasurementCmScaleAT_ST");
		 * EP.VerifyText(TabName+"LastStudyDayAT_ST");
		 * EP.VerifyToolTip(TabName+"LastStudyDayCalendarAT_TT");
		 * EP.VerifyText(TabName+"ContinuedTreatmentStopDateAT_ST");
		 * EP.VerifyToolTip(TabName+"ContinuedTreatmentStopDateAT_IM");
		 * EP.VerifyPageLayout();
		 * 
		 * //Dynamic Contents with Status = Completed //Verifying the mandatory
		 * fields of Completed EP.DrpDwn_SelectIndex(TabName+"StatusOfStudy_DD",
		 * 1); EP.DrpDwn_SelectIndex(TabName+"StudyCompletionAT_DD",0); //Study
		 * Completion EP.DrpDwn_SelectIndex(TabName+"SubjectHeightAT_DD", 0);
		 * EP.Edit_SetValueByJavaScript(TabName+"DateOfMeasurementAT_EB", "");
		 * EP.Edit_SetValueByJavaScript(TabName+"LastStudyDayAT_EB", "");
		 * EP.Edit_SetValueByJavaScript(TabName+"TreatmentStopDateAT_EB", "");
		 * EP.Edit_SetValue(TabName+"MeasurementCompletedAT_EB", "");
		 * EP.Btn_Click(TabName+"Save_BN");
		 * EP.TakeStaticScreenshot("FieldMandatory");
		 * EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
		 * EP.GetText(TabName+"ValueOutOfRangeErrorES_DT"));
		 * EP.CompareText("StudyCompletion", EP.LangData("FieldIsMandatory"),
		 * GetErrorMessage(1)); EP.CompareText("PatientHeightAtEndOfStudy",
		 * EP.LangData("FieldIsMandatory"), GetErrorMessage(2));
		 * EP.CompareText("DateOfMeasurement", EP.LangData("FieldIsMandatory"),
		 * GetErrorMessage(3)); EP.CompareText("Measurement",
		 * EP.LangData("FieldIsMandatory"), GetErrorMessage(4));
		 * EP.CompareText("LastStudyDay", EP.LangData("FieldIsMandatory"),
		 * GetErrorMessage(5)); EP.CompareText("TreatmentStopDate",
		 * EP.LangData("FieldIsMandatory"), GetErrorMessage(6));
		 * EP.CompareText("Ignoreandsave",EP.LangData("IgnoreAndSave"),
		 * EP.GetText(TabName+"Save_BN")); EP.ClearStaticScreenshot();
		 * EP.Btn_Click(TabName+"Cancel_BN");
		 * EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");
		 * 
		 * 
		 * //Date and Measurement Range Error Message Validations
		 * EP.DrpDwn_SelectIndex(TabName+"StatusOfStudy_DD", 1);
		 * EP.DrpDwn_SelectIndex(TabName+"StudyCompletionAT_DD",2); //Study
		 * Completion EP.DrpDwn_SelectIndex(TabName+"SubjectHeightAT_DD", 3);
		 * EP.Edit_SetValue(TabName+"MeasurementCompletedAT_EB", MinValue);
		 * EP.SelectDate
		 * (TabName+"LastStudyDayCalendarAT_TT",EP.TestData("ECOSPatientDOB"));
		 * EP.SelectDate(TabName+"ContinuedTreatmentStopDateAT_IM",EP.TestData(
		 * "ECOSPatientDOB")); EP.Btn_Click(TabName+"Save_BN");
		 * EP.TakeStaticScreenshot("IncorrectFormatError1");
		 * EP.VerifyText(TabName+"IncorrecrFormatErrorES_DT");//Header error msg
		 * EP.CompareText("StudyCompletionErrMsg_DT",
		 * EP.LangData("AccordanceWithAnother"), GetErrorMessage(1));
		 * EP.CompareText("PatientHeightErrMsg_DT",
		 * EP.LangData("AccordanceWithAnother"),GetErrorMessage(2));
		 * EP.VerifyRegExpression("MothersHeight_RangeError",
		 * EP.LangData("RangeValue"), GetErrorMessage(3), "\\d{1,3}");
		 * EP.CompareText("LastStudyDayErrMsg_DT",
		 * EP.LangData("LastStudyDateError"),GetErrorMessage(4));
		 * EP.CompareText("TreatmentStopDate",
		 * EP.LangData("LastStudyDateError"),GetErrorMessage(5));
		 * EP.ClearStaticScreenshot();
		 * 
		 * //Final height is smaller than the latest visit height
		 * EP.DrpDwn_SelectIndex(TabName+"SubjectHeightAT_DD", 1);
		 * EP.Edit_ClearValue(TabName+"MeasurementCompleted_EB");
		 * EP.SelectDate(TabName
		 * +"LastStudyDayCalendarAT_TT",EP.Date_AddDays(EP.TestData
		 * ("ECOSPatientDOB"), 2, Calendar.MONTH));
		 * EP.SelectDate(TabName+"DateOFMeasurementCalendarAT_TT"
		 * ,EP.Date_AddDays(EP.TestData("ECOSPatientDOB"), 2, Calendar.MONTH));
		 * EP.Edit_SetValue(TabName+"MeasurementCompletedAT_EB", 55);
		 * EP.Btn_Click(TabName+"Save_BN");
		 * EP.TakeStaticScreenshot("IncorrectFormatError2");
		 * EP.CompareText("IncorrecrFormatError_DT",
		 * EP.LangData("VerifyIncorrectFormats"),
		 * EP.GetText(TabName+"IncorrecrFormatErrorES_DT"));
		 * EP.CompareText("PatientHeightErrMsg_DT",
		 * EP.LangData("FinalHeightSmaller"),GetErrorMessage(2));
		 * EP.ClearStaticScreenshot();
		 * 
		 * //Patient is female and final height recorded and age should be
		 * greater than 14 years old
		 * EP.DrpDwn_SelectIndex(TabName+"StatusOfStudy_DD",1);
		 * EP.DrpDwn_SelectIndex(TabName+"StudyCompletionAT_DD",1);
		 * EP.DrpDwn_SelectIndex(TabName+"SubjectHeightAT_DD", 1);
		 * EP.Edit_SetValue(TabName+"MeasurementCompleted_EB", "100");
		 * EP.SelectDate
		 * (TabName+"DateOFMeasurementCalendarAT_TT",EP.Date_AddDays
		 * (EP.TestData("ECOSPatientDOB"), 2, Calendar.MONTH));
		 * EP.SelectDate(TabName
		 * +"ContinuedTreatmentStopDateAT_IM",EP.Date_AddDays
		 * (EP.TestData("ECOSPatientDOB"), 2, Calendar.MONTH)); EP.Wait(2);
		 * EP.Btn_Click(TabName+"Save_BN");
		 * EP.TakeStaticScreenshot("AgeMustBeGreaterThan14");
		 * EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
		 * EP.GetText(TabName+"ValueOutOfRangeErrorES_DT"));
		 * EP.CompareText("PatientHeightErrMsg_DT",
		 * EP.LangData("AgeMustBeGreaterThan14"),
		 * EP.GetText(TabName+"PatientHeightErrMsgAT_DT"));
		 * EP.CompareText("Ignoreandsave",EP.LangData("IgnoreAndSave"),
		 * EP.GetText(TabName+"Save_BN")); EP.ClearStaticScreenshot();
		 * 
		 * //Incorrect format for measurement
		 * EP.DrpDwn_SelectIndex(TabName+"SubjectHeightAT_DD", 2);
		 * EP.DrpDwn_SelectIndex(TabName+"StudyCompletionAT_DD",2);
		 * EP.Edit_SetValue(TabName+"MeasurementCompleted_EB", IncorrectFormat);
		 * EP.Btn_Click(TabName+"Save_BN");
		 * EP.TakeStaticScreenshot("incorrectFormatMeasurement");
		 * EP.CompareText("IncorrecrFormatError_DT",
		 * EP.LangData("VerifyIncorrectFormats"),
		 * EP.GetText(TabName+"IncorrecrFormatErrorES_DT"));
		 * EP.CompareText("MeasurementErrMsg_DT",
		 * EP.LangData("IncorrectFormat"), GetErrorMessage(1));
		 * EP.ClearStaticScreenshot();
		 * EP.Edit_SetValue(TabName+"MeasurementCompleted_EB", 55);
		 * EP.Btn_Click(TabName+"Save_BN");
		 * EP.TakeStaticScreenshot("nearfinalSmaller");
		 * EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
		 * EP.GetText(TabName+"ValueOutOfRangeErrorES_DT"));
		 * EP.CompareText("PatientHeightErrMsg_DT",
		 * EP.LangData("NearFinalSmaller"), GetErrorMessage(1));
		 * EP.CompareText("Ignoreandsave",EP.LangData("IgnoreAndSave"),
		 * EP.GetText(TabName+"Save_BN")); EP.ClearStaticScreenshot();
		 * EP.Btn_Click(TabName+"Cancel_BN");
		 * EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");
		 * 
		 * //Error for Treatment Stop Date lesser than treatment Start Date
		 * 
		 * TreatmentStartDate=EP.Date_AddDays(EP.Date_AddDays(EP.TestData(
		 * "ECOSPatientDOB"), 2, Calendar.MONTH),-1, Calendar.DATE);
		 * EP.DrpDwn_SelectIndex(TabName+"StudyCompletionAT_DD",3);//1
		 * EP.DrpDwn_SelectIndex(TabName+"SubjectHeightAT_DD", 3);//1
		 * EP.Edit_SetValue(TabName+"MeasurementCompleted_EB", 60);
		 * EP.SelectDate(TabName+"DateOFMeasurementCalendarAT_TT",
		 * EP.Date_AddDays(EP.TestData("ECOSPatientDOB"), 2, Calendar.MONTH));
		 * EP.SelectDate(TabName+"LastStudyDayCalendarAT_TT",
		 * EP.Date_AddDays(EP.TestData("ECOSPatientDOB"), 2, Calendar.MONTH));
		 * EP
		 * .SelectDate(TabName+"ContinuedTreatmentStopDateAT_IM",TreatmentStartDate
		 * ); EP.Btn_Click(TabName+"Save_BN");
		 * EP.TakeStaticScreenshot("TSDLessThanCurrentGH");
		 * EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
		 * EP.GetText(TabName+"IncorrecrFormatErrorES_DT"));
		 * EP.VerifyRegExpression("TSDLessThanCurrentGH",
		 * EP.LangData("TSDLessThanCurrentGH"
		 * ),GetErrorMessage(1),EP.TranslateDate(TreatmentStartDate,
		 * "dd MMM yyyy", false));
		 * EP.CompareText("Ignoreandsave",EP.LangData("IgnoreAndSave"),
		 * EP.GetText(TabName+"Save_BN")); EP.ClearStaticScreenshot();
		 * EP.Btn_Click(TabName+"Cancel_BN");
		 * EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");
		 * 
		 * 
		 * //Success message for Completion
		 * EP.DrpDwn_SelectIndex(TabName+"StudyCompletionAT_DD",3);//1
		 * EP.DrpDwn_SelectIndex(TabName+"SubjectHeightAT_DD", 3);//1
		 * EP.Edit_SetValue(TabName+"MeasurementCompleted_EB", 60);
		 * EP.SelectDate(TabName+"DateOFMeasurementCalendarAT_TT",
		 * EP.GetTimeStamp("dd MMM yyyy"));
		 * EP.SelectDate(TabName+"ContinuedTreatmentStopDateAT_IM",
		 * EP.GetTimeStamp("dd MMM yyyy"));
		 * EP.SelectDate(TabName+"LastStudyDayCalendarAT_TT",
		 * EP.GetTimeStamp("dd MMM yyyy"));
		 * 
		 * //EP.SelectDate(TabName+"ContinuedTreatmentStopDateAT_IM",
		 * EP.Date_AddDays(EP.TestData("ECOSPatientDOB"), 2, Calendar.MONTH));
		 * EP.VerifyMonth(TabName+"TreatmentStopDateAT_EB");
		 * EP.VerifyMonth(TabName+"DateOfMeasurementAT_EB");
		 * EP.VerifyMonth(TabName+"LastStudyDayAT_EB");
		 * EP.Btn_Click(TabName+"Save_BN");
		 * ECOS.VerifySuccessMessage(EP.LangData("CompletionOrTermination"));
		 * 
		 * //To check error messgage for male patient
		 * EP.SearchForPatient(EP.TestData("ECOSPatient"));
		 * EP.Rbtn_SetOption("#MyPatients_FirstPatient_RB", "ON");
		 * EP.Btn_Click("#MyPatients_BaselineRecord_BN");
		 * EP.Lnk_Click("#NavigationTab_RecordOverview_LK");
		 * EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");
		 * EP.DrpDwn_SelectIndex(TabName+"StatusOfStudy_DD", 1);
		 * EP.DrpDwn_SelectIndex(TabName+"StudyCompletion_DD",1);
		 * EP.DrpDwn_SelectIndex(TabName+"SubjectHeight_DD", 1);
		 * EP.Edit_SetValue(TabName+"MeasurementCompleted_EB", "100");
		 * EP.Wait(2); EP.Btn_Click(TabName+"Save_BN");
		 * EP.TakeStaticScreenshot("AgeMustBeGreaterThan15Error");
		 * EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
		 * EP.GetText(TabName+"ValueOutOfRangeErrorES_DT"));
		 * EP.CompareText("PatientHeightErrMsg_DT",
		 * EP.LangData("AgeMustBeGreaterThan15"), GetErrorMessage(1));
		 * EP.CompareText("Ignoreandsave",EP.LangData("IgnoreAndSave"),
		 * EP.GetText(TabName+"Save_BN")); EP.ClearStaticScreenshot();
		 * 
		 * //****************DIS CONTINUED
		 * SECTION*********************************
		 * if(EP.ObjectExists("#NavigationTab_MyPatientsList_LK"));
		 * EP.Lnk_Click("#NavigationTab_MyPatientsList_LK");
		 * EP.SearchForPatient(EP.TestData("PatientName") + " 04");
		 * EP.Rbtn_SetOption("#MyPatients_FirstPatient_RB", "ON");
		 * EP.Btn_Click("#MyPatients_BaselineRecord_BN");
		 * EP.Lnk_Click("#NavigationTab_RecordOverview_LK");
		 * EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");
		 * EP.DrpDwn_SelectIndex(TabName+"StatusOfStudy_DD", 2);
		 * EP.TakeStaticScreenshot("StudyDiscontinued");
		 * EP.VerifyText(TabName+"PrematureStudyDiscontinuationReasonAT_ST");
		 * EP.CompareDynamicDropDownValues(TabName+"DiscontinuationReasonAT_DD",
		 * EP.ORData(TabName+"DiscontinuationReasonAT_DD"), false);
		 * EP.DrpDwn_SelectIndex(TabName+"DiscontinuationReasonAT_DD", 2);//To
		 * make visible Remove data from Study?
		 * 
		 * EP.VerifyText(TabName+"RemoveDataFromStudyAT_ST");
		 * EP.VerifyText(TabName+"RemoveDataYesAT_ST");
		 * EP.VerifyText(TabName+"RemoveDataNoAT_ST");
		 * 
		 * EP.VerifyText(TabName+"InitiatorOfWithdrawalAT_ST");
		 * EP.CompareDynamicDropDownValues(TabName+"InitiatorOfWithdrawalAT_DD",
		 * EP.ORData(TabName+"InitiatorOfWithdrawalAT_DD"), false);
		 * EP.VerifyText(TabName+"PatientHeightAtPrematureAT_ST");
		 * EP.CompareDynamicDropDownValues
		 * (TabName+"PatientHeightAtDiscontinuationAT_DD",
		 * EP.ORData(TabName+"PatientHeightAtDiscontinuation_DD"), false);
		 * EP.VerifyText(TabName+"DisContLastStudyDayAT_ST");
		 * EP.VerifyToolTip(TabName+"DiscontinuedLastStudyDayCalendarAT_TT");
		 * EP.VerifyToolTip(TabName+"DiscontinuedTreatmentStopDateAT_IM");
		 * EP.VerifyText(TabName+"DiscontinuedMeasurementAT_ST");
		 * EP.VerifyText(TabName+"DiscontinuedTreatmentStopDateAT_ST");
		 * EP.VerifyTextContained(TabName+"MeasurementCmAT_ST");
		 * EP.ClearStaticScreenshot();
		 * 
		 * //Mandatory fields of discontinued section
		 * EP.DrpDwn_SelectIndex(TabName+"StatusOfStudy_DD", 2);
		 * EP.DrpDwn_SelectIndex(TabName+"DiscontinuationReasonAT_DD", 0);
		 * EP.DrpDwn_SelectIndex(TabName+"InitiatorOfWithdrawalAT_DD", 0);
		 * EP.Edit_SetValueByJavaScript(TabName+"DiscontinuedLastStudyDayAT_EB",
		 * "");
		 * EP.Edit_SetValueByJavaScript(TabName+"DiscontinuedTreatmentStopDateAT_EB"
		 * ,""); EP.Edit_ClearValue(TabName+"Measurement_EB");
		 * EP.DrpDwn_SelectIndex
		 * (TabName+"PatientHeightAtDiscontinuationAT_DD",0);
		 * EP.Btn_Click(TabName+"Save_BN");
		 * EP.TakeStaticScreenshot("Mandatory");
		 * EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
		 * EP.GetText(TabName+"ValueOutOfRangeErrorES_DT"));
		 * EP.CompareText("PrematureStudyDiscontinuationReasonMandatory",
		 * EP.LangData("FieldIsMandatory"), GetErrorMessage(1));
		 * EP.CompareText("InitiatorofwithdrawalMandatory",
		 * EP.LangData("FieldIsMandatory"), GetErrorMessage(2));
		 * EP.CompareText("StudyDiscontinuationMandatory",
		 * EP.LangData("FieldIsMandatory"), GetErrorMessage(3));
		 * EP.CompareText("LastStudyDateMandatory",
		 * EP.LangData("FieldIsMandatory"), GetErrorMessage(4));
		 * EP.CompareText("MeasurementMandatory",
		 * EP.LangData("FieldIsMandatory"), GetErrorMessage(5));
		 * EP.CompareText("Ignoreandsave",EP.LangData("IgnoreAndSave"),
		 * EP.GetText(TabName+"Save_BN")); EP.ClearStaticScreenshot();
		 * EP.Btn_Click(TabName+"Cancel_BN");
		 * EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");
		 * 
		 * 
		 * //Measurement range value and Date related error messages/Treatment
		 * Discontinuation Details madatory field
		 * EP.DrpDwn_SelectIndex(TabName+"StatusOfStudy_DD", 2);
		 * EP.DrpDwn_SelectIndex(TabName+"DiscontinuationReasonAT_DD", 1);
		 * EP.DrpDwn_SelectIndex(TabName+"InitiatorOfWithdrawalAT_DD", 1);
		 * EP.DrpDwn_SelectIndex
		 * (TabName+"PatientHeightAtDiscontinuationAT_DD",3);
		 * EP.DrpDwn_SelectIndex
		 * (TabName+"TreatmentDiscontinuationDetailsAT_DD",0);
		 * EP.SelectDate(TabName+"DiscontinuedLastStudyDayCalendarAT_TT",
		 * EP.TestData("ECOSPatientDOB"));
		 * EP.SelectDate(TabName+"DiscontinuedTreatmentStopDateAT_IM",
		 * EP.TestData("ECOSPatientDOB"));
		 * EP.Edit_SetValue(TabName+"Measurement_EB", MinValue);
		 * EP.Btn_Click(TabName+"Save_BN");
		 * EP.TakeStaticScreenshot("OutOfRangeError");
		 * EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
		 * EP.GetText(TabName+"ValueOutOfRangeErrorES_DT"));
		 * EP.CompareText("TreatmentDiscontinuationDetailsMandatory",
		 * EP.LangData("FieldIsMandatory"), GetErrorMessage(1));
		 * EP.CompareText("LastStudyDateError",
		 * EP.LangData("LastStudyDateError"), GetErrorMessage(2));
		 * EP.CompareText("TreatmentStopDate ",
		 * EP.LangData("LastStudyDateError"), GetErrorMessage(3));
		 * EP.VerifyRegExpression("Height",
		 * EP.LangData("RangeValue"),GetErrorMessage(4),"\\d{2,3}");
		 * EP.CompareText("Ignoreandsave",EP.LangData("IgnoreAndSave"),
		 * EP.GetText(TabName+"Save_BN")); EP.ClearStaticScreenshot();
		 * EP.Btn_Click(TabName+"Cancel_BN");
		 * 
		 * //Validation of "Other, Specify" error messages
		 * 
		 * EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");
		 * EP.DrpDwn_SelectIndex(TabName+"StatusOfStudy_DD", 2);
		 * EP.DrpDwn_SelectIndex(TabName+"DiscontinuationReasonAT_DD", 6);
		 * EP.Edit_ClearValue(TabName+"DiscontinuationReasonAT_EB");
		 * EP.DrpDwn_SelectIndex(TabName+"InitiatorOfWithdrawalAT_DD", 5);
		 * EP.Edit_ClearValue(TabName+"InitiatorOfWithdrawalAT_EB");
		 * EP.SelectDate(TabName+"DiscontinuedLastStudyDayCalendarAT_TT",
		 * EP.Date_AddDays(EP.TestData("ECOSPatientDOB"), 2, Calendar.MONTH));
		 * EP.SelectDate(TabName+"DiscontinuedTreatmentStopDateAT_IM",
		 * EP.Date_AddDays(EP.TestData("ECOSPatientDOB"), 2, Calendar.MONTH));
		 * EP
		 * .DrpDwn_SelectIndex(TabName+"PatientHeightAtDiscontinuationAT_DD",3);
		 * EP.Edit_SetValue(TabName+"Measurement_EB", 56);
		 * EP.Btn_Click(TabName+"Save_BN");
		 * EP.TakeStaticScreenshot("OutOfRangeError");
		 * EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
		 * EP.GetText(TabName+"ValueOutOfRangeErrorES_DT"));
		 * EP.CompareText("FieldIsMandatory", EP.LangData("FieldIsMandatory"),
		 * GetErrorMessage(1)); EP.CompareText("FieldIsMandatory",
		 * EP.LangData("FieldIsMandatory"), GetErrorMessage(2));
		 * EP.CompareText("Ignoreandsave",EP.LangData("IgnoreAndSave"),
		 * EP.GetText(TabName+"Save_BN")); EP.ClearStaticScreenshot();
		 * EP.Btn_Click(TabName+"Cancel_BN");
		 * 
		 * //Incorrect Formats
		 * EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");
		 * EP.Edit_ClearValue(TabName+"MeasurementAT_EB");
		 * EP.Edit_SetValue(TabName+"MeasurementAT_EB", IncorrectFormat);
		 * EP.DrpDwn_SelectIndex(TabName+"DiscontinuationReasonAT_DD", 5);
		 * EP.DrpDwn_SelectIndex
		 * (TabName+"PatientHeightAtDiscontinuationAT_DD",3);
		 * EP.DrpDwn_SelectIndex(TabName+"InitiatorOfWithdrawalAT_DD", 4);
		 * EP.SelectDate(TabName+"DiscontinuedLastStudyDayCalendarAT_TT",
		 * EP.GetTimeStamp("dd MMM yyyy"));
		 * EP.SelectDate(TabName+"DiscontinuedTreatmentStopDateAT_IM"
		 * ,EP.GetTimeStamp("dd MMM yyyy")); EP.Btn_Click(TabName+"Save_BN");
		 * EP.TakeStaticScreenshot("MeasurementFormatError");
		 * EP.VerifyText(TabName+"IncorrecrFormatErrorES_DT");
		 * EP.CompareText("IncorrectFormat", EP.LangData("IncorrectFormat"),
		 * GetErrorMessage(1)); EP.ClearStaticScreenshot();
		 * 
		 * 
		 * EP.Btn_Click(TabName+"Cancel_BN");
		 * EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");
		 * 
		 * //Error for Treatment Stop Date lesser than treatment Start Date
		 * 
		 * EP.Edit_SetValue(TabName+"MeasurementAT_EB", 60);
		 * EP.DrpDwn_SelectIndex(TabName+"DiscontinuationReasonAT_DD", 5);
		 * EP.DrpDwn_SelectIndex
		 * (TabName+"PatientHeightAtDiscontinuationAT_DD",3);
		 * EP.DrpDwn_SelectIndex(TabName+"InitiatorOfWithdrawalAT_DD", 4);
		 * EP.SelectDate(TabName+"DiscontinuedLastStudyDayAT_EB",
		 * EP.Date_AddDays(EP.TestData("ECOSPatientDOB"), 2, Calendar.MONTH));
		 * EP.SelectDate(TabName+"DiscontinuedTreatmentStopDateAT_IM",
		 * TreatmentStartDate); EP.Btn_Click(TabName+"Save_BN");
		 * EP.TakeStaticScreenshot("TSDLessThanCurrentGH");
		 * EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
		 * EP.GetText(TabName+"ValueOutOfRangeErrorES_DT"));
		 * EP.VerifyRegExpression("TSDLessThanCurrentGH",
		 * EP.LangData("TSDLessThanCurrentGH"
		 * ),GetErrorMessage(1),EP.TranslateDate(TreatmentStartDate,
		 * "dd MMM yyyy", false));
		 * EP.CompareText("Ignoreandsave",EP.LangData("IgnoreAndSave"),
		 * EP.GetText(TabName+"Save_BN")); EP.ClearStaticScreenshot();
		 * EP.Btn_Click(TabName+"Cancel_BN");
		 * EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");
		 * 
		 * 
		 * //To verify success message for Discontinued
		 * EP.DrpDwn_SelectIndex(TabName+"DiscontinuationReasonAT_DD",
		 * EP.RandomValue(2, 5));
		 * EP.DrpDwn_SelectIndex(TabName+"InitiatorOfWithdrawalAT_DD",
		 * EP.RandomValue(1, 4)); EP.Edit_SetValue(TabName+"Measurement_EB",
		 * "56");
		 * EP.SelectDate(TabName+"DiscontinuedLastStudyDayCalendarAT_TT",EP
		 * .GetTimeStamp("dd MMM yyyy"));
		 * EP.SelectDate(TabName+"DiscontinuedTreatmentStopDateAT_IM"
		 * ,EP.GetTimeStamp("dd MMM yyyy"));
		 * EP.TakeStaticScreenshot("MonthVerification");
		 * EP.VerifyMonth(TabName+"DiscontinuedTreatmentStopDateAT_EB");
		 * EP.VerifyMonth(TabName+"DiscontinuedLastStudyDayAT_EB");
		 * EP.ClearStaticScreenshot(); EP.Btn_Click(TabName+"Save_BN");
		 * ECOS.VerifySuccessMessage(EP.LangData("CompletionOrTermination"));
		 * 
		 * break;
		 */

		case GREECE:
			// Completed Section

			EP.DrpDwn_SelectIndex(TabName + "StatusOfStudy_DD", 1);
			EP.TakeStaticScreenshot("StudyCompleted");
			EP.VerifyText(TabName + "StudyCompletionAT_ST");
			EP.CompareDropDownValues(TabName + "StudyCompletionGR_DD");
			EP.VerifyText(TabName + "PatientHeightAtEndOfStudyAT_ST");
			EP.CompareDropDownValues(TabName + "SubjectHeightGR_DD");
			EP.VerifyText(TabName + "DateOfMeasurementAT_ST");
			EP.VerifyToolTip(TabName + "DateOFMeasurementCalendarAT_TT");
			EP.VerifyText(TabName + "MeasurementAT_ST");
			EP.VerifyTextContained(TabName + "MeasurementCmScaleAT_ST");
			EP.VerifyText(TabName + "LastStudyDayAT_ST");
			EP.VerifyToolTip(TabName + "LastStudyDayCalendarAT_TT");
			EP.VerifyText(TabName + "ContinuedTreatmentStopDateAT_ST");
			EP.VerifyToolTip(TabName + "ContinuedTreatmentStopDateAT_IM");
			EP.VerifyPageLayout();

			// Error message when Please Select is selected in the first
			// dropdown
			EP.DrpDwn_SelectIndex(TabName + "StatusOfStudy_DD", 0);
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("FieldMandatory");
			EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
					EP.GetText(TabName + "ValueOutOfRangeErrorES_DT"));
			EP.CompareText("StatusOfStudy_DD_Mandatory",
					EP.LangData("FieldIsMandatory"), GetErrorMessage(1));
			EP.CompareText("Ignoreandsave", EP.LangData("IgnoreAndSave"),
					EP.GetText(TabName + "Save_BN"));
			EP.ClearStaticScreenshot();
			EP.Btn_Click(TabName + "Cancel_BN");
			EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");

			// Dynamic Contents with Status = Completed
			// Verifying the mandatory fields of Completed
			EP.DrpDwn_SelectIndex(TabName + "StatusOfStudy_DD", 1);
			EP.DrpDwn_SelectIndex(TabName + "StudyCompletionGR_DD", 0); // Study
																		// Completion
			EP.DrpDwn_SelectIndex(TabName + "SubjectHeightGR_DD", 0);
			EP.Edit_SetValueByJavaScript(TabName + "DateOfMeasurementAT_EB", "");
			EP.Edit_SetValueByJavaScript(TabName + "LastStudyDayAT_EB", "");
			EP.Edit_SetValueByJavaScript(TabName + "TreatmentStopDateAT_EB", "");
			EP.Edit_SetValue(TabName + "MeasurementCompletedAT_EB", "");
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("FieldMandatory");
			EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
					EP.GetText(TabName + "ValueOutOfRangeErrorES_DT"));
			EP.CompareText("StudyCompletion", EP.LangData("FieldIsMandatory"),
					GetErrorMessage(1));
			EP.CompareText("PatientHeightAtEndOfStudy",
					EP.LangData("FieldIsMandatory"), GetErrorMessage(2));
			EP.CompareText("DateOfMeasurement",
					EP.LangData("FieldIsMandatory"), GetErrorMessage(3));
			EP.CompareText("Measurement", EP.LangData("FieldIsMandatory"),
					GetErrorMessage(4));
			EP.CompareText("LastStudyDay", EP.LangData("FieldIsMandatory"),
					GetErrorMessage(5));
			EP.CompareText("TreatmentStopDate",
					EP.LangData("FieldIsMandatory"), GetErrorMessage(6));
			EP.CompareText("Ignoreandsave", EP.LangData("IgnoreAndSave"),
					EP.GetText(TabName + "Save_BN"));
			EP.ClearStaticScreenshot();
			EP.Btn_Click(TabName + "Cancel_BN");
			EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");

			// Date and Measurement Range Error Message Validations
			EP.DrpDwn_SelectIndex(TabName + "StatusOfStudy_DD", 1);
			EP.DrpDwn_SelectIndex(TabName + "StudyCompletionGR_DD", 2); // Study
																		// Completion
			EP.DrpDwn_SelectIndex(TabName + "SubjectHeightGR_DD", 3);
			EP.Edit_SetValue(TabName + "MeasurementCompletedAT_EB", MinValue);
			EP.SelectDate(TabName + "ContinuedTreatmentStopDateAT_IM",
					EP.TestData("ECOSPatientDOB"));
			EP.SelectDate(TabName + "LastStudyDayCalendarAT_TT",
					EP.TestData("ECOSPatientDOB"));
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("IncorrectFormatError1");
			EP.VerifyText(TabName + "IncorrecrFormatErrorES_DT");// Header error
																	// msg
			EP.CompareText("StudyCompletionErrMsg_DT",
					EP.LangData("AccordanceWithAnother"), GetErrorMessage(1));
			EP.CompareText("PatientHeightErrMsg_DT",
					EP.LangData("AccordanceWithAnother"), GetErrorMessage(2));
			EP.VerifyRegExpression("MothersHeight_RangeError",
					EP.LangData("RangeValue"), GetErrorMessage(3), "\\d{1,3}");
			EP.CompareText("LastStudyDayErrMsg_DT",
					EP.LangData("LastStudyDateError"), GetErrorMessage(4));
			EP.CompareText("TreatmentStopDate",
					EP.LangData("LastStudyDateError"), GetErrorMessage(5));
			EP.ClearStaticScreenshot();

			// Final height is smaller than the latest visit height
			EP.DrpDwn_SelectIndex(TabName + "SubjectHeightGR_DD", 1);
			EP.Edit_ClearValue(TabName + "MeasurementCompleted_EB");
			EP.SelectDate(TabName + "LastStudyDayCalendarAT_TT", EP
					.Date_AddDays(EP.TestData("ECOSPatientDOB"), 2,
							Calendar.MONTH));
			EP.SelectDate(TabName + "DateOFMeasurementCalendarAT_TT", EP
					.Date_AddDays(EP.TestData("ECOSPatientDOB"), 2,
							Calendar.MONTH));
			EP.Edit_SetValue(TabName + "MeasurementCompletedAT_EB", 55);
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("IncorrectFormatError2");
			EP.CompareText("IncorrecrFormatError_DT",
					EP.LangData("VerifyIncorrectFormats"),
					EP.GetText(TabName + "IncorrecrFormatErrorES_DT"));
			EP.CompareText("PatientHeightErrMsg_DT",
					EP.LangData("FinalHeightSmaller"), GetErrorMessage(2));
			EP.ClearStaticScreenshot();

			// Patient is female and final height recorded and age should be
			// greater than 14 years old
			EP.DrpDwn_SelectIndex(TabName + "StatusOfStudy_DD", 1);
			EP.DrpDwn_SelectIndex(TabName + "StudyCompletionGR_DD", 1);
			EP.DrpDwn_SelectIndex(TabName + "SubjectHeightGR_DD", 1);
			EP.Edit_SetValue(TabName + "MeasurementCompleted_EB", "100");
			EP.SelectDate(TabName + "ContinuedTreatmentStopDateAT_IM", EP
					.Date_AddDays(EP.TestData("ECOSPatientDOB"), 2,
							Calendar.MONTH));
			EP.SelectDate(TabName + "DateOFMeasurementCalendarAT_TT", EP
					.Date_AddDays(EP.TestData("ECOSPatientDOB"), 2,
							Calendar.MONTH));
			EP.Wait(2);
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("AgeMustBeGreaterThan14");
			EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
					EP.GetText(TabName + "ValueOutOfRangeErrorES_DT"));
			EP.CompareText("PatientHeightErrMsg_DT",
					EP.LangData("AgeMustBeGreaterThan14"),
					EP.GetText(TabName + "PatientHeightErrMsgAT_DT"));
			EP.CompareText("Ignoreandsave", EP.LangData("IgnoreAndSave"),
					EP.GetText(TabName + "Save_BN"));
			EP.ClearStaticScreenshot();
			EP.Btn_Click(TabName + "Cancel_BN");
			EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");

			// Incorrect format for measurement
			EP.DrpDwn_SelectIndex(TabName + "StudyCompletionGR_DD", 2);
			EP.DrpDwn_SelectIndex(TabName + "SubjectHeightGR_DD", 2);
			EP.Edit_SetValue(TabName + "MeasurementCompleted_EB",
					IncorrectFormat);
			EP.SelectDate(TabName + "ContinuedTreatmentStopDateAT_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate(TabName + "LastStudyDayCalendarAT_TT",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate(TabName + "DateOFMeasurementCalendarAT_TT",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("incorrectFormatMeasurement");
			EP.CompareText("IncorrecrFormatError_DT",
					EP.LangData("VerifyIncorrectFormats"),
					EP.GetText(TabName + "IncorrecrFormatErrorES_DT"));
			EP.CompareText("MeasurementErrMsg_DT",
					EP.LangData("IncorrectFormat"), GetErrorMessage(2));
			EP.ClearStaticScreenshot();
			EP.DrpDwn_SelectIndex(TabName + "StudyCompletionGR_DD", 2);
			EP.DrpDwn_SelectIndex(TabName + "SubjectHeightGR_DD", 2);
			EP.Edit_SetValue(TabName + "MeasurementCompleted_EB", 55);
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("nearfinalSmaller");
			EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
					EP.GetText(TabName + "ValueOutOfRangeErrorES_DT"));
			EP.CompareText("PatientHeightErrMsg_DT",
					EP.LangData("NearFinalSmaller"), GetErrorMessage(1));
			EP.CompareText("Ignoreandsave", EP.LangData("IgnoreAndSave"),
					EP.GetText(TabName + "Save_BN"));
			EP.ClearStaticScreenshot();
			EP.Btn_Click(TabName + "Cancel_BN");
			EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");

			// Error for Treatment Stop Date lesser than treatment Start Date

			TreatmentStartDate = EP.Date_AddDays(EP.Date_AddDays(
					EP.TestData("ECOSPatientDOB"), 2, Calendar.MONTH), -1,
					Calendar.DATE);
			EP.DrpDwn_SelectIndex(TabName + "StudyCompletionGR_DD", 3);// 1
			EP.DrpDwn_SelectIndex(TabName + "SubjectHeightGR_DD", 3);// 1
			EP.Edit_SetValue(TabName + "MeasurementCompleted_EB", 60);
			EP.SelectDate(TabName + "ContinuedTreatmentStopDateAT_IM",
					TreatmentStartDate);
			EP.SelectDate(TabName + "LastStudyDayCalendarAT_TT", EP
					.Date_AddDays(EP.TestData("ECOSPatientDOB"), 2,
							Calendar.MONTH));
			EP.SelectDate(TabName + "DateOFMeasurementCalendarAT_TT", EP
					.Date_AddDays(EP.TestData("ECOSPatientDOB"), 2,
							Calendar.MONTH));
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("TSDLessThanCurrentGH");
			EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
					EP.GetText(TabName + "IncorrecrFormatErrorES_DT"));
			EP.VerifyRegExpression("TSDLessThanCurrentGH",
					EP.LangData("TSDLessThanCurrentGH"), GetErrorMessage(1),
					EP.TranslateDate(TreatmentStartDate, "dd MMM yyyy", false));
			EP.CompareText("Ignoreandsave", EP.LangData("IgnoreAndSave"),
					EP.GetText(TabName + "Save_BN"));
			EP.ClearStaticScreenshot();
			EP.Btn_Click(TabName + "Cancel_BN");
			EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");

			// Success message for Completion
			EP.DrpDwn_SelectIndex(TabName + "StudyCompletionGR_DD", 3);// 1
			EP.DrpDwn_SelectIndex(TabName + "SubjectHeightGR_DD", 3);// 1
			EP.Edit_SetValue(TabName + "MeasurementCompleted_EB", 60);
			EP.SelectDate(TabName + "ContinuedTreatmentStopDateAT_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate(TabName + "LastStudyDayCalendarAT_TT",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate(TabName + "DateOFMeasurementCalendarAT_TT",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.VerifyMonth(TabName + "TreatmentStopDateAT_EB");
			EP.VerifyMonth(TabName + "DateOfMeasurementAT_EB");
			EP.VerifyMonth(TabName + "LastStudyDayAT_EB");
			EP.Btn_Click(TabName + "Save_BN");
			ECOS.VerifySuccessMessage(EP.LangData("CompletionOrTermination"));

			// Success message when "Not Known" and "Not Available" are selected
			EP.NavigateTo(RecoveringURL);
			EP.DrpDwn_SelectIndex(TabName + "StudyCompletionGR_DD",
					EP.RandomValue(4, 5));// 1
			EP.DrpDwn_SelectIndex(TabName + "SubjectHeightGR_DD",
					EP.RandomValue(4, 5));// 1
			EP.Edit_SetValue(TabName + "MeasurementCompleted_EB", 60);
			EP.SelectDate(TabName + "ContinuedTreatmentStopDateAT_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate(TabName + "LastStudyDayCalendarAT_TT",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate(TabName + "DateOFMeasurementCalendarAT_TT",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.VerifyMonth(TabName + "TreatmentStopDateAT_EB");
			EP.VerifyMonth(TabName + "DateOfMeasurementAT_EB");
			EP.VerifyMonth(TabName + "LastStudyDayAT_EB");
			EP.Btn_Click(TabName + "Save_BN");
			ECOS.VerifySuccessMessage(EP.LangData("CompletionOrTermination"));

			// To check error messgage for male patient - FinalHeightDifference
			EP.SearchForPatient(EP.TestData("ECOSPatient"));
			EP.Rbtn_SetOption("#MyPatients_FirstPatient_RB", "ON");
			EP.Btn_Click("#MyPatients_BaselineRecord_BN");
			EP.Lnk_Click("#NavigationTab_RecordOverview_LK");
			EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");
			EP.DrpDwn_SelectIndex(TabName + "StatusOfStudy_DD", 1);
			EP.DrpDwn_SelectIndex(TabName + "StudyCompletionGR_DD", 1);
			EP.DrpDwn_SelectIndex(TabName + "SubjectHeightGR_DD", 1);
			EP.Edit_SetValue(TabName + "MeasurementCompleted_EB", "100");
			EP.Wait(2);
			EP.SelectDate(TabName + "ContinuedTreatmentStopDateAT_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate(TabName + "LastStudyDayCalendarAT_TT",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate(TabName + "DateOFMeasurementCalendarAT_TT",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("FinalHeightDifference");
			EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
					EP.GetText(TabName + "ValueOutOfRangeErrorES_DT"));
			EP.CompareText("FinalHeightDifference",
					EP.LangData("FinalHeightDifference"), GetErrorMessage(1));
			EP.CompareText("Ignoreandsave", EP.LangData("IgnoreAndSave"),
					EP.GetText(TabName + "Save_BN"));
			EP.ClearStaticScreenshot();

			EP.Btn_Click(TabName + "Cancel_BN");
			EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");

			// To check error messgage for male patient -
			// FinalHeightDifference1CM

			EP.DrpDwn_SelectIndex(TabName + "StatusOfStudy_DD", 1);
			EP.DrpDwn_SelectIndex(TabName + "StudyCompletionGR_DD", 1);
			EP.DrpDwn_SelectIndex(TabName + "SubjectHeightGR_DD", 2);
			EP.Edit_SetValue(TabName + "MeasurementCompleted_EB", "100");
			EP.Wait(2);
			EP.SelectDate(TabName + "ContinuedTreatmentStopDateAT_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate(TabName + "LastStudyDayCalendarAT_TT",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate(TabName + "DateOFMeasurementCalendarAT_TT",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("FinalHeightDifference1CM");
			EP.CompareText("IncorrecrFormatError_DT",
					EP.LangData("VerifyIncorrectFormats"),
					EP.GetText(TabName + "IncorrecrFormatErrorES_DT"));
			EP.CompareText("AccordanceWithAnother",
					EP.LangData("AccordanceWithAnother"), GetErrorMessage(1));
			EP.CompareText("FinalHeightDifference1CM",
					EP.LangData("FinalHeightDifference1CM"), GetErrorMessage(2));
			EP.CompareText("Save", EP.LangData("Save"),
					EP.GetText(TabName + "Save_BN"));
			EP.ClearStaticScreenshot();

			// Delete the Visit for John Doe
			EP.Btn_Click(TabName + "Cancel_BN");
			EP.Lnk_Click("#NavigationTab_VisitsRecord_LK");
			EP.WbTbl_SortColumn("#AddVisit_VisitDate_TE", 2, "DESC");
			EP.WbTbl_SelectRbtn("#AddVisit_VisitDate_TE", 2);
			EP.Btn_Click("#AddVisit_Continue_BN");
			if (EP.ObjectExists("#General_Delete_IM")) {
				DeleteRecord("#General_Delete_IM", false);
			}

			// To check error messgage for male patient - AgeMustBeGreaterThan15

			EP.Lnk_Click("#NavigationTab_RecordOverview_LK");
			EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");
			EP.DrpDwn_SelectIndex(TabName + "StatusOfStudy_DD", 1);
			EP.DrpDwn_SelectIndex(TabName + "StudyCompletionGR_DD", 1);
			EP.DrpDwn_SelectIndex(TabName + "SubjectHeightGR_DD", 1);
			EP.Edit_SetValue(TabName + "MeasurementCompleted_EB", "100");
			EP.Wait(2);
			EP.SelectDate(TabName + "ContinuedTreatmentStopDateAT_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate(TabName + "LastStudyDayCalendarAT_TT",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate(TabName + "DateOFMeasurementCalendarAT_TT",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("AgeMustBeGreaterThan15Error");
			EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
					EP.GetText(TabName + "ValueOutOfRangeErrorES_DT"));
			EP.CompareText("PatientHeightErrMsg_DT",
					EP.LangData("AgeMustBeGreaterThan15"), GetErrorMessage(1));
			EP.CompareText("Ignoreandsave", EP.LangData("IgnoreAndSave"),
					EP.GetText(TabName + "Save_BN"));
			EP.ClearStaticScreenshot();

			// ****************DIS CONTINUED
			// SECTION*********************************
			if (EP.ObjectExists("#NavigationTab_MyPatientsList_LK"))
				;
			EP.Lnk_Click("#NavigationTab_MyPatientsList_LK");
			EP.SearchForPatient(EP.TestData("PatientName") + " 04");
			EP.Rbtn_SetOption("#MyPatients_FirstPatient_RB", "ON");
			EP.Btn_Click("#MyPatients_BaselineRecord_BN");
			EP.Lnk_Click("#NavigationTab_RecordOverview_LK");
			EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");
			EP.DrpDwn_SelectIndex(TabName + "StatusOfStudy_DD", 2);
			EP.TakeStaticScreenshot("StudyDiscontinued");
			EP.VerifyText(TabName + "PrematureStudyDiscontinuationReasonAT_ST");
			EP.CompareDynamicDropDownValues(TabName
					+ "DiscontinuationReasonGR_DD",
					EP.ORData(TabName + "DiscontinuationReasonGR_DD"), false);
			EP.DrpDwn_SelectIndex(TabName + "DiscontinuationReasonGR_DD", 2);// To
																				// make
																				// visible
																				// Remove
																				// data
																				// from
																				// Study?

			EP.VerifyText(TabName + "RemoveDataFromStudyAT_ST");
			EP.VerifyText(TabName + "RemoveDataYesAT_ST");
			EP.VerifyText(TabName + "RemoveDataNoAT_ST");

			EP.VerifyText(TabName + "InitiatorOfWithdrawalAT_ST");
			EP.CompareDynamicDropDownValues(TabName
					+ "InitiatorOfWithdrawalGR_DD",
					EP.ORData(TabName + "InitiatorOfWithdrawalGR_DD"), false);
			EP.VerifyText(TabName + "PatientHeightAtPrematureAT_ST");
			EP.CompareDynamicDropDownValues(TabName
					+ "PatientHeightAtDiscontinuationGR_DD",
					EP.ORData(TabName + "PatientHeightAtDiscontinuationGR_DD"),
					false);
			EP.VerifyText(TabName + "DisContLastStudyDayAT_ST");
			EP.VerifyToolTip(TabName + "DiscontinuedLastStudyDayCalendarAT_TT");
			EP.VerifyToolTip(TabName + "DiscontinuedTreatmentStopDateAT_IM");
			EP.VerifyText(TabName + "DiscontinuedMeasurementAT_ST");
			EP.VerifyText(TabName + "DiscontinuedTreatmentStopDateAT_ST");
			EP.VerifyTextContained(TabName + "MeasurementCmAT_ST");
			EP.ClearStaticScreenshot();

			// Mandatory fields of discontinued section
			EP.DrpDwn_SelectIndex(TabName + "StatusOfStudy_DD", 2);
			EP.DrpDwn_SelectIndex(TabName + "DiscontinuationReasonGR_DD", 0);
			EP.DrpDwn_SelectIndex(TabName + "InitiatorOfWithdrawalGR_DD", 0);
			EP.Edit_SetValueByJavaScript(TabName
					+ "DiscontinuedLastStudyDayAT_EB", "");
			EP.Edit_SetValueByJavaScript(TabName
					+ "DiscontinuedTreatmentStopDateAT_EB", "");
			EP.Edit_ClearValue(TabName + "Measurement_EB");
			EP.DrpDwn_SelectIndex(TabName
					+ "PatientHeightAtDiscontinuationGR_DD", 0);
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("Mandatory");
			EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
					EP.GetText(TabName + "ValueOutOfRangeErrorES_DT"));
			EP.CompareText("PrematureStudyDiscontinuationReasonMandatory",
					EP.LangData("FieldIsMandatory"), GetErrorMessage(1));
			EP.CompareText("InitiatorofwithdrawalMandatory",
					EP.LangData("FieldIsMandatory"), GetErrorMessage(2));
			EP.CompareText("StudyDiscontinuationMandatory",
					EP.LangData("FieldIsMandatory"), GetErrorMessage(3));
			EP.CompareText("LastStudyDateMandatory",
					EP.LangData("FieldIsMandatory"), GetErrorMessage(4));
			EP.CompareText("MeasurementMandatory",
					EP.LangData("FieldIsMandatory"), GetErrorMessage(5));
			EP.CompareText("Ignoreandsave", EP.LangData("IgnoreAndSave"),
					EP.GetText(TabName + "Save_BN"));
			EP.ClearStaticScreenshot();
			EP.Btn_Click(TabName + "Cancel_BN");
			EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");

			// Measurement range value and Date related error messages/Treatment
			// Discontinuation Details madatory field
			EP.DrpDwn_SelectIndex(TabName + "StatusOfStudy_DD", 2);
			EP.DrpDwn_SelectIndex(TabName + "DiscontinuationReasonGR_DD", 1);
			EP.DrpDwn_SelectIndex(TabName + "InitiatorOfWithdrawalGR_DD", 1);
			EP.DrpDwn_SelectIndex(TabName
					+ "PatientHeightAtDiscontinuationGR_DD", 3);
			EP.DrpDwn_SelectIndex(TabName
					+ "TreatmentDiscontinuationDetailsGR_DD", 0);
			EP.SelectDate(TabName + "DiscontinuedTreatmentStopDateAT_IM",
					EP.TestData("ECOSPatientDOB"));
			EP.SelectDate(TabName + "DiscontinuedLastStudyDayCalendarAT_TT",
					EP.TestData("ECOSPatientDOB"));
			EP.Edit_SetValue(TabName + "Measurement_EB", MinValue);
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("OutOfRangeError");
			EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
					EP.GetText(TabName + "ValueOutOfRangeErrorES_DT"));
			EP.CompareText("TreatmentDiscontinuationDetailsMandatory",
					EP.LangData("FieldIsMandatory"), GetErrorMessage(1));
			EP.CompareText("LastStudyDateError",
					EP.LangData("LastStudyDateError"), GetErrorMessage(2));
			EP.CompareText("TreatmentStopDate ",
					EP.LangData("LastStudyDateError"), GetErrorMessage(3));
			EP.VerifyRegExpression("Height", EP.LangData("RangeValue"),
					GetErrorMessage(4), "\\d{2,3}");
			EP.CompareText("Ignoreandsave", EP.LangData("IgnoreAndSave"),
					EP.GetText(TabName + "Save_BN"));
			EP.ClearStaticScreenshot();
			EP.Btn_Click(TabName + "Cancel_BN");

			// Validation of "Other, Specify" error messages

			EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");
			EP.DrpDwn_SelectIndex(TabName + "StatusOfStudy_DD", 2);
			EP.DrpDwn_SelectIndex(TabName + "DiscontinuationReasonAT_DD", 6);
			EP.Edit_ClearValue(TabName + "DiscontinuationReasonAT_EB");
			EP.DrpDwn_SelectIndex(TabName + "InitiatorOfWithdrawalAT_DD", 5);
			EP.Edit_ClearValue(TabName + "InitiatorOfWithdrawalAT_EB");
			EP.SelectDate(TabName + "DiscontinuedTreatmentStopDateAT_IM", EP
					.Date_AddDays(EP.TestData("ECOSPatientDOB"), 2,
							Calendar.MONTH));
			EP.SelectDate(TabName + "DiscontinuedLastStudyDayCalendarAT_TT", EP
					.Date_AddDays(EP.TestData("ECOSPatientDOB"), 2,
							Calendar.MONTH));
			EP.DrpDwn_SelectIndex(TabName
					+ "PatientHeightAtDiscontinuationAT_DD", 3);
			EP.Edit_SetValue(TabName + "Measurement_EB", 56);
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("OutOfRangeError");
			EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
					EP.GetText(TabName + "ValueOutOfRangeErrorES_DT"));
			EP.CompareText("FieldIsMandatory", EP.LangData("FieldIsMandatory"),
					GetErrorMessage(1));
			EP.CompareText("FieldIsMandatory", EP.LangData("FieldIsMandatory"),
					GetErrorMessage(2));
			EP.CompareText("Ignoreandsave", EP.LangData("IgnoreAndSave"),
					EP.GetText(TabName + "Save_BN"));
			EP.ClearStaticScreenshot();
			EP.Btn_Click(TabName + "Cancel_BN");

			// Incorrect Formats
			EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");
			EP.Edit_ClearValue(TabName + "MeasurementAT_EB");
			EP.Edit_SetValue(TabName + "MeasurementAT_EB", IncorrectFormat);
			EP.DrpDwn_SelectIndex(TabName + "DiscontinuationReasonAT_DD", 5);
			EP.DrpDwn_SelectIndex(TabName
					+ "PatientHeightAtDiscontinuationAT_DD", 3);
			EP.DrpDwn_SelectIndex(TabName + "InitiatorOfWithdrawalAT_DD", 4);
			EP.SelectDate(TabName + "DiscontinuedTreatmentStopDateAT_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate(TabName + "DiscontinuedLastStudyDayCalendarAT_TT",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("MeasurementFormatError");
			EP.VerifyText(TabName + "IncorrecrFormatErrorES_DT");
			EP.CompareText("IncorrectFormat", EP.LangData("IncorrectFormat"),
					GetErrorMessage(1));
			EP.ClearStaticScreenshot();
			EP.Btn_Click(TabName + "Cancel_BN");
			EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");

			// Error for Treatment Stop Date lesser than treatment Start Date

			EP.DrpDwn_SelectIndex(TabName + "DiscontinuationReasonAT_DD", 5);
			EP.DrpDwn_SelectIndex(TabName
					+ "PatientHeightAtDiscontinuationAT_DD", 3);
			EP.DrpDwn_SelectIndex(TabName + "InitiatorOfWithdrawalAT_DD", 4);
			EP.SelectDate(TabName + "DiscontinuedTreatmentStopDateAT_IM",
					TreatmentStartDate);
			EP.SelectDate(TabName + "DiscontinuedLastStudyDayCalendarAT_TT",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.Edit_SetValue(TabName + "MeasurementAT_EB", 60);
			EP.Btn_Click(TabName + "Save_BN");
			EP.TakeStaticScreenshot("TSDLessThanCurrentGH");
			EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
					EP.GetText(TabName + "ValueOutOfRangeErrorES_DT"));
			EP.VerifyRegExpression("TSDLessThanCurrentGH",
					EP.LangData("TSDLessThanCurrentGH"), GetErrorMessage(1),
					EP.TranslateDate(TreatmentStartDate, "dd MMM yyyy", false));
			EP.CompareText("Ignoreandsave", EP.LangData("IgnoreAndSave"),
					EP.GetText(TabName + "Save_BN"));
			EP.ClearStaticScreenshot();
			EP.Btn_Click(TabName + "Cancel_BN");
			EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");

			// To verify success message for Discontinued
			EP.DrpDwn_SelectIndex(TabName + "DiscontinuationReasonAT_DD",
					EP.RandomValue(2, 5));
			EP.DrpDwn_SelectIndex(TabName + "InitiatorOfWithdrawalAT_DD",
					EP.RandomValue(1, 4));
			EP.Edit_SetValue(TabName + "Measurement_EB", "56");
			EP.SelectDate(TabName + "DiscontinuedTreatmentStopDateAT_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate(TabName + "DiscontinuedLastStudyDayCalendarAT_TT",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.TakeStaticScreenshot("MonthVerification");
			EP.VerifyMonth(TabName + "DiscontinuedTreatmentStopDateAT_EB");
			EP.VerifyMonth(TabName + "DiscontinuedLastStudyDayAT_EB");
			EP.ClearStaticScreenshot();
			EP.Btn_Click(TabName + "Save_BN");
			ECOS.VerifySuccessMessage(EP.LangData("CompletionOrTermination"));

			// To verify success message for Discontinued with "Not Known" and
			// "Not Available" Option
			EP.SearchForPatient(EP.TestData("PatientName") + " 04");
			EP.Rbtn_SetOption("#MyPatients_FirstPatient_RB", "ON");
			EP.Btn_Click("#MyPatients_BaselineRecord_BN");
			EP.Lnk_Click("#NavigationTab_RecordOverview_LK");
			EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");
			EP.DrpDwn_SelectIndex(TabName + "DiscontinuationReasonAT_DD",
					EP.RandomValue(7, 8));
			EP.DrpDwn_SelectIndex(TabName + "InitiatorOfWithdrawalAT_DD",
					EP.RandomValue(6, 7));
			EP.Edit_SetValue(TabName + "Measurement_EB", "56");
			EP.SelectDate(TabName + "DiscontinuedTreatmentStopDateAT_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate(TabName + "DiscontinuedLastStudyDayCalendarAT_TT",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.TakeStaticScreenshot("MonthVerification");
			EP.VerifyMonth(TabName + "DiscontinuedTreatmentStopDateAT_EB");
			EP.VerifyMonth(TabName + "DiscontinuedLastStudyDayAT_EB");
			EP.ClearStaticScreenshot();
			EP.Btn_Click(TabName + "Save_BN");
			ECOS.VerifySuccessMessage(EP.LangData("CompletionOrTermination"));

			break;

		// case CHINA:
		// case ARGENTINA:
		case FINLAND:
		case KOREA:
		case AUSTRALIA:
		case MEXICO:

			// *-*-*-*-*-*-*-*-*-*-*Static Contents with Status =
			// Completed*-*-*-*-*-*-*-*-*-*-*
			EP.DrpDwn_SelectIndex(TabName + "StatusOfStudy_DD", 1);
			EP.TakeStaticScreenshot("StudyCompleted");

			EP.VerifyText(TabName + "StudyCompletion_ST");
			EP.CompareDropDownValues(TabName + "StudyCompletion_DD");
			EP.VerifyText(TabName + "PatientHeightAtEndOfStudyAT_ST");
			EP.CompareDropDownValues(TabName + "SubjectHeight_DD");
			EP.VerifyText(TabName + "DateOfMeasurement_ST");
			EP.VerifyToolTip(TabName + "DateOFMeasurementCalendar_TT");
			EP.VerifyText(TabName + "Measurement_ST");
			EP.VerifyTextContained(TabName + "MeasurementCmScale_ST");
			EP.VerifyText(TabName + "LastStudyDay_ST");
			EP.VerifyToolTip(TabName + "LastStudyDayCalendar_TT");

			EP.VerifyPageLayout();
			// if (Env.GetBrowserUsed().startsWith("SA")){
			// EP.NavigateTo(RecoveringURL);
			// }

			// Dynamic Contents with Status = Completed
			EP.DrpDwn_SelectIndex(TabName + "StatusOfStudy_DD", 1);

			// Error Mesaage Validations
			EP.DrpDwn_SelectIndex(TabName + "StudyCompletion_DD", 2); // Study
																		// Completion
			EP.DrpDwn_SelectIndex(TabName + "SubjectHeight_DD", 3);
			EP.Edit_SetValue(TabName + "DateOfMeasurement_EB", "0");
			EP.SelectDate(TabName + "LastStudyDayCalendar_TT",
					EP.TestData("ECOSPatientDOB"));
			EP.Btn_Click(TabName + "Save_BN");
			EP.VerifyText(TabName + "IncorrecrFormatError_DT");// Header error
																// msg
			EP.CompareText("StudyCompletionErrMsg_DT",
					EP.LangData("AccordanceWithAnother"),
					EP.GetText(TabName + "StudyCompletionErrMsg_DT"));
			EP.CompareText("PatientHeightErrMsg_DT",
					EP.LangData("AccordanceWithAnother"),
					EP.GetText(TabName + "PatientHeightErrMsgAT_DT"));
			EP.VerifyRegExp(TabName + "MeasurementErrMsg_DT");
			EP.CompareText("LastStudyDayErrMsg_DT",
					EP.LangData("LastStudyDateError"),
					EP.GetText(TabName + "LastStudyDayErrMsg_DT"));

			EP.DrpDwn_SelectIndex(TabName + "SubjectHeight_DD", 1);
			EP.Edit_ClearValue(TabName + "DateOfMeasurement_EB");
			EP.SelectDate(TabName + "LastStudyDayCalendar_TT", EP.Date_AddDays(
					EP.TestData("ECOSPatientDOB"), 2, Calendar.MONTH));
			EP.Btn_Click(TabName + "Save_BN");
			EP.CompareText("OutOfRangeErr_DT",
					EP.LangData("VerifyIncorrectFormats"),
					EP.GetText(TabName + "IncorrecrFormatError_DT"));
			EP.CompareText("PatientHeightErrMsg_DT",
					EP.LangData("FinalHeightSmaller"),
					EP.GetText(TabName + "PatientHeightErrMsgAT_DT"));
			EP.CompareText("MeasurementErrMsg_DT",
					EP.LangData("FieldIsMandatory"),
					EP.GetText(TabName + "MeasurementErrMsg_DT"));

			EP.DrpDwn_SelectIndex(TabName + "SubjectHeight_DD", 2);
			EP.Edit_SetValue(TabName + "DateOfMeasurement_EB", IncorrectFormat);
			EP.Btn_Click(TabName + "Save_BN");
			EP.CompareText("MeasurementErrMsg_DT",
					EP.LangData("IncorrectFormat"),
					EP.GetText(TabName + "MeasurementErrMsg_DT"));
			EP.CompareText("PatientHeightErrMsg_DT",
					EP.LangData("NearFinalSmaller"),
					EP.GetText(TabName + "PatientHeightErrMsgAT_DT"));

			EP.DrpDwn_SelectIndex(TabName + "StudyCompletion_DD", 1);
			EP.DrpDwn_SelectIndex(TabName + "SubjectHeight_DD", 1);
			EP.Edit_SetValue(TabName + "DateOfMeasurement_EB", "100");
			EP.Btn_Click(TabName + "Save_BN");
			EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
					EP.GetText(TabName + "IncorrecrFormatError_DT"));
			EP.CompareText("PatientHeightErrMsg_DT",
					EP.LangData("AgeMustBeGreaterThan15"),
					EP.GetText(TabName + "PatientHeightErrMsgAT_DT"));

			// For success message
			EP.DrpDwn_SelectIndex(TabName + "StudyCompletion_DD", 3);// 1
			EP.DrpDwn_SelectIndex(TabName + "SubjectHeight_DD", 3);// 1
			EP.Btn_Click(TabName + "Save_BN");
			ECOS.VerifySuccessMessage(EP.LangData("CompletionOrTermination"));
			EP.SearchForPatient(EP.TestData("ECOSPatient"));
			EP.WbTbl_VerifyTableColumn("#MyPatients_PatientsInfoTable_TH",
					"StudyID", 3, 1, EP.LangData("Completed"), false);

			// *-*-*-*-*-*-*-*Static Contents with Status =
			// "Discontinued"*-*-*-*-*-*-*-*-*
			EP.NavigateTo(RecoveringURL);
			EP.DrpDwn_SelectIndex(TabName + "StatusOfStudy_DD", 2);
			EP.TakeStaticScreenshot("StudyDiscontinued");
			EP.VerifyText(TabName + "PrematureStudyDiscontinuationReason_ST");
			EP.CompareDynamicDropDownValues(TabName
					+ "DiscontinuationReasonAT_DD",
					EP.ORData(TabName + "DiscontinuationReasonAT_DD"), false);
			EP.DrpDwn_SelectIndex(TabName + "DiscontinuationReasonAT_DD", 2);// To
																				// make
																				// visible
																				// Remove
																				// data
																				// from
																				// Study?

			EP.VerifyText(TabName + "RemoveDataFromStudyAT_ST");
			EP.VerifyText(TabName + "RemoveDataYes_ST");
			EP.VerifyText(TabName + "RemoveDataNo_ST");

			EP.VerifyText(TabName + "InitiatorOfWithdrawalAT_ST");
			EP.CompareDynamicDropDownValues(TabName
					+ "InitiatorOfWithdrawalAT_DD",
					EP.ORData(TabName + "InitiatorOfWithdrawalAT_DD"), false);
			EP.VerifyText(TabName + "PatientHeightAtPrematureAT_ST");
			EP.CompareDynamicDropDownValues(TabName
					+ "PatientHeightAtDiscontinuation_DD",
					EP.ORData(TabName + "PatientHeightAtDiscontinuation_DD"),
					false);
			EP.VerifyText(TabName + "DisContLastStudyDayAT_ST");
			EP.VerifyToolTip(TabName + "DiscontinuedLastStudyDayCalendar_TT");
			EP.VerifyText(TabName + "DiscontinuedMeasurementAT_ST");
			EP.VerifyTextContained(TabName + "MeasurementCm_ST");

			EP.VerifyPageLayout();
			// if (Env.GetBrowserUsed().startsWith("SA")){
			// EP.NavigateTo(RecoveringURL);
			// }

			// Validation of "Other, Specify" error messages
			EP.DrpDwn_SelectIndex(TabName + "StatusOfStudy_DD", 2);
			EP.DrpDwn_SelectIndex(TabName + "DiscontinuationReasonAT_DD", 6);
			EP.Edit_ClearValue(TabName + "DiscontinuationReason_EB");
			EP.DrpDwn_SelectIndex(TabName + "InitiatorOfWithdrawalAT_DD", 5);
			EP.Edit_ClearValue(TabName + "InitiatorOfWithdrawal_EB");
			EP.SelectDate(TabName + "DiscontinuedLastStudyDayCalendar_TT",
					EP.TestData("ECOSPatientDOB"));
			EP.Edit_SetValue(TabName + "Measurement_EB", MinValue);
			EP.Btn_Click(TabName + "Save_BN");
			EP.VerifyText(TabName + "ValueOutOfRangeError_DT");// Header
			EP.VerifyText(TabName + "StudyDiscontinuationReasonError_DT");
			EP.VerifyText(TabName + "InitiatorOfWithdrawalError_DT");
			EP.VerifyText(TabName + "StudyDateError_DT");// Date must be after
															// the consents date
			EP.VerifyRegExp(TabName + "MeasurementRangeError_DT");

			// Incorrect Format
			EP.Edit_SetValue(TabName + "Measurement_EB", IncorrectFormat);
			EP.Btn_Click(TabName + "Save_BN");
			EP.VerifyText(TabName + "IncorrecrFormatError_DT");
			EP.VerifyText(TabName + "MeasurementFormatError_DT");

			EP.DrpDwn_SelectIndex(TabName + "DiscontinuationReasonAT_DD",
					EP.RandomValue(2, 5));
			EP.DrpDwn_SelectIndex(TabName + "InitiatorOfWithdrawalAT_DD",
					EP.RandomValue(1, 4));
			EP.Edit_SetValue(TabName + "Measurement_EB", "56");
			EP.SelectDate(TabName + "DiscontinuedLastStudyDayCalendar_TT", EP
					.Date_AddDays(EP.TestData("ECOSPatientDOB"), 2,
							Calendar.MONTH));
			EP.Btn_Click(TabName + "Save_BN");
			ECOS.VerifySuccessMessage(EP.LangData("CompletionOrTermination"));
			EP.SearchForPatient(EP.TestData("ECOSPatient"));
			EP.WbTbl_VerifyTableColumn("#MyPatients_PatientsInfoTable_TH",
					"StudyID", 3, 1, EP.LangData("Discontinued"), false);

			// Error message if the difference in height between the last two
			// visits is >0.5
			EP.Rbtn_SetOption("#MyPatients_FirstPatient_RB", "ON");
			EP.Btn_Click("#MyPatients_VisitsRecord_BN");
			EP.WbTbl_SortColumn("#AddVisit_VisitDate_TE", 2, "DESC");
			EP.Rbtn_SetOption("#AddVisit_FirstVisit_RB", "ON");// Set
																// prerequisite
																// for Latest
																// first visit
			EP.Btn_Click("#AddVisit_Continue_BN");
			if (!EP.ObjectExists("#General_Edit_IM")) { // If the Record is
														// present then Edit it
														// or else create a new
														// record
				EP.Btn_Click("#General_First_BN"); // Click Add button
				EP.WaitForPageload();
			} else {
				EP.Btn_Click("#General_Edit_IM"); // Click Edit button
				EP.WaitForPageload();
			}
			EP.Edit_SetValue("#AddVisit_AuxologicalHeight_EB", "56");
			EP.HideCursor();
			EP.WaitForPageload();
			EP.Btn_Click("#General_AddRecordSave_BN"); // click save button

			if (!EP.ObjectExists("#General_Edit_IM")) {
				EP.Lnk_Click("#General_AddRecordSave_BN"); // Until getting
															// saved.
				EP.WaitForPageload();
			}
			EP.Lnk_Click("#NavigationTab_RecordOverview_LK");
			EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");

			EP.DrpDwn_SelectIndex(TabName + "StatusOfStudy_DD", 1);
			EP.TakeStaticScreenshot("StudyCompleted");
			EP.DrpDwn_SelectIndex(TabName + "StudyCompletion_DD", 1);
			EP.DrpDwn_SelectIndex(TabName + "SubjectHeight_DD", 1);
			EP.Edit_SetValue(TabName + "DateOfMeasurement_EB", "57");
			EP.Btn_Click(TabName + "Save_BN");
			EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
					EP.GetText(TabName + "IncorrecrFormatError_DT"));
			EP.CompareText("PatientHeightErrMsg_DT",
					EP.LangData("FinalHeightDifference"),
					EP.GetText(TabName + "PatientHeightErrMsgAT_DT"));

			// Revert the changes made to height field in visits record
			EP.Lnk_Click("#NavigationTab_MyPatientsList_LK");
			EP.SearchForPatient(EP.TestData("ECOSPatient"));
			EP.Rbtn_SetOption("#MyPatients_FirstPatient_RB", "ON");
			EP.Btn_Click("#MyPatients_VisitsRecord_BN");
			EP.WbTbl_SortColumn("#AddVisit_VisitDate_TE", 2, "DESC");
			EP.Rbtn_SetOption("#AddVisit_FirstVisit_RB", "ON");// Set
																// prerequisite
																// for Latest
																// first visit
			EP.Btn_Click("#AddVisit_Continue_BN");
			if (!EP.ObjectExists("#General_Edit_IM")) { // If the Record is
														// present then Edit it
														// or else create a new
														// record
				EP.Btn_Click("#General_First_BN"); // Click Add button
				EP.WaitForPageload();
			} else {
				EP.Btn_Click("#General_Edit_IM"); // Click Edit button
				EP.WaitForPageload();
			}
			EP.Edit_SetValue("#AddVisit_AuxologicalHeight_EB", "55.4");
			EP.HideCursor();
			EP.WaitForPageload();
			EP.Btn_Click("#General_AddRecordSave_BN"); // click save button

			if (!EP.ObjectExists("#General_Edit_IM")) {
				EP.Lnk_Click("#General_AddRecordSave_BN"); // Until getting
															// saved.
				EP.WaitForPageload();
			}

			break;

		case ICELAND:
			// *-*-*-*-*-*-*-*-*-*-*Static Contents with Status =
			// Completed*-*-*-*-*-*-*-*-*-*-*
			EP.DrpDwn_SelectIndex(TabName + "StatusOfStudy_DD", 1);
			EP.TakeStaticScreenshot("StudyCompleted");

			EP.VerifyText(TabName + "StudyCompletion_ST");
			EP.CompareDropDownValues(TabName + "StudyCompletion_DD");
			EP.VerifyText(TabName + "PatientHeightAtEndOfStudyAT_ST");
			EP.CompareDropDownValues(TabName + "SubjectHeight_DD");
			EP.VerifyText(TabName + "DateOfMeasurement_ST");
			EP.VerifyToolTip(TabName + "DateOFMeasurementCalendar_TT");
			EP.VerifyText(TabName + "Measurement_ST");
			EP.VerifyTextContained(TabName + "MeasurementCmScale_ST");
			EP.VerifyText(TabName + "LastStudyDay_ST");
			EP.VerifyToolTip(TabName + "LastStudyDayCalendar_TT");

			EP.VerifyPageLayout();
			// if (Env.GetBrowserUsed().startsWith("SA")){
			// EP.NavigateTo(RecoveringURL);
			// }

			// Dynamic Contents with Status = Completed
			EP.DrpDwn_SelectIndex(TabName + "StatusOfStudy_DD", 1);

			// Error Mesaage Validations
			EP.DrpDwn_SelectIndex(TabName + "StudyCompletion_DD", 2); // Study
																		// Completion
			EP.DrpDwn_SelectIndex(TabName + "SubjectHeight_DD", 3);
			EP.Edit_SetValue(TabName + "DateOfMeasurement_EB", "0");
			EP.SelectDate(TabName + "LastStudyDayCalendar_TT",
					EP.TestData("ECOSPatientDOB"));
			EP.Btn_Click(TabName + "Save_BN");
			EP.VerifyText(TabName + "IncorrecrFormatError_DT");// Header error
																// msg
			EP.CompareText("StudyCompletionErrMsg_DT",
					EP.LangData("AccordanceWithAnother"),
					EP.GetText(TabName + "StudyCompletionErrMsg_DT"));
			EP.CompareText("PatientHeightErrMsg_DT",
					EP.LangData("AccordanceWithAnother"),
					EP.GetText(TabName + "PatientHeightErrMsgAT_DT"));
			EP.VerifyRegExp(TabName + "MeasurementErrMsg_DT");
			EP.CompareText("LastStudyDayErrMsg_DT",
					EP.LangData("LastStudyDateError"),
					EP.GetText(TabName + "LastStudyDayErrMsg_DT"));

			EP.DrpDwn_SelectIndex(TabName + "SubjectHeight_DD", 1);
			EP.Edit_ClearValue(TabName + "DateOfMeasurement_EB");
			EP.SelectDate(TabName + "LastStudyDayCalendar_TT", EP.Date_AddDays(
					EP.TestData("ECOSPatientDOB"), 2, Calendar.MONTH));
			EP.Btn_Click(TabName + "Save_BN");
			EP.CompareText("OutOfRangeErr_DT",
					EP.LangData("VerifyIncorrectFormats"),
					EP.GetText(TabName + "IncorrecrFormatError_DT"));
			// EP.CompareText("PatientHeightErrMsg_DT",
			// EP.LangData("FinalHeightSmaller"),
			// EP.GetText(TabName+"PatientHeightErrMsgAT_DT"));
			EP.CompareText("MeasurementErrMsg_DT",
					EP.LangData("FieldIsMandatory"),
					EP.GetText(TabName + "MeasurementErrMsg_DT"));

			EP.DrpDwn_SelectIndex(TabName + "SubjectHeight_DD", 2);
			EP.Edit_SetValue(TabName + "DateOfMeasurement_EB", IncorrectFormat);
			EP.Btn_Click(TabName + "Save_BN");
			EP.CompareText("MeasurementErrMsg_DT",
					EP.LangData("IncorrectFormat"),
					EP.GetText(TabName + "MeasurementErrMsg_DT"));
			// EP.CompareText("PatientHeightErrMsg_DT",
			// EP.LangData("NearFinalSmaller"),
			// EP.GetText(TabName+"PatientHeightErrMsgAT_DT"));

			EP.DrpDwn_SelectIndex(TabName + "StudyCompletion_DD", 1);
			EP.DrpDwn_SelectIndex(TabName + "SubjectHeight_DD", 1);
			EP.Edit_SetValue(TabName + "DateOfMeasurement_EB", "100");
			EP.Btn_Click(TabName + "Save_BN");
			EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
					EP.GetText(TabName + "IncorrecrFormatError_DT"));
			EP.CompareText("PatientHeightErrMsg_DT",
					EP.LangData("AgeMustBeGreaterThan15"),
					EP.GetText(TabName + "PatientHeightErrMsgAT_DT"));

			// For success message
			EP.DrpDwn_SelectIndex(TabName + "StudyCompletion_DD", 3);// 1
			EP.DrpDwn_SelectIndex(TabName + "SubjectHeight_DD", 3);// 1
			EP.Btn_Click(TabName + "Save_BN");
			ECOS.VerifySuccessMessage(EP.LangData("CompletionOrTermination"));
			EP.SearchForPatient(EP.TestData("ECOSPatient"));
			EP.WbTbl_VerifyTableColumn("#MyPatients_PatientsInfoTable_TH",
					"StudyID", 3, 1, EP.LangData("Completed"), false);

			// *-*-*-*-*-*-*-*Static Contents with Status =
			// "Discontinued"*-*-*-*-*-*-*-*-*
			EP.NavigateTo(RecoveringURL);
			EP.DrpDwn_SelectIndex(TabName + "StatusOfStudy_DD", 2);
			EP.TakeStaticScreenshot("StudyDiscontinued");
			EP.VerifyText(TabName + "PrematureStudyDiscontinuationReason_ST");
			EP.CompareDynamicDropDownValues(TabName
					+ "DiscontinuationReasonIS_DD",
					EP.ORData(TabName + "DiscontinuationReasonIS_DD"), false);
			EP.DrpDwn_SelectIndex(TabName + "DiscontinuationReasonIS_DD", 2);// To
																				// make
																				// visible
																				// Remove
																				// data
																				// from
																				// Study?

			EP.VerifyText(TabName + "RemoveDataFromStudyAT_ST");
			EP.VerifyText(TabName + "RemoveDataYes_ST");
			EP.VerifyText(TabName + "RemoveDataNo_ST");

			EP.VerifyText(TabName + "InitiatorOfWithdrawalAT_ST");
			EP.CompareDynamicDropDownValues(TabName
					+ "InitiatorOfWithdrawalAT_DD",
					EP.ORData(TabName + "InitiatorOfWithdrawalAT_DD"), false);
			EP.VerifyText(TabName + "PatientHeightAtPrematureAT_ST");
			EP.CompareDynamicDropDownValues(TabName
					+ "PatientHeightAtDiscontinuation_DD",
					EP.ORData(TabName + "PatientHeightAtDiscontinuation_DD"),
					false);
			EP.VerifyText(TabName + "DisContLastStudyDayAT_ST");
			EP.VerifyToolTip(TabName + "DiscontinuedLastStudyDayCalendar_TT");
			EP.VerifyText(TabName + "DiscontinuedMeasurementAT_ST");
			EP.VerifyTextContained(TabName + "MeasurementCm_ST");

			EP.VerifyPageLayout();
			// if (Env.GetBrowserUsed().startsWith("SA")){
			// EP.NavigateTo(RecoveringURL);
			// }

			// Validation of "Other, Specify" error messages
			EP.DrpDwn_SelectIndex(TabName + "StatusOfStudy_DD", 2);
			EP.DrpDwn_SelectIndex(TabName + "DiscontinuationReasonIS_DD", 9);
			EP.Edit_ClearValue(TabName + "DiscontinuationReason_EB");
			EP.DrpDwn_SelectIndex(TabName + "InitiatorOfWithdrawalAT_DD", 5);
			EP.Edit_ClearValue(TabName + "InitiatorOfWithdrawal_EB");
			EP.SelectDate(TabName + "DiscontinuedLastStudyDayCalendar_TT",
					EP.TestData("ECOSPatientDOB"));
			EP.Edit_SetValue(TabName + "Measurement_EB", MinValue);
			EP.Btn_Click(TabName + "Save_BN");
			EP.VerifyText(TabName + "ValueOutOfRangeError_DT");// Header
			EP.VerifyText(TabName + "StudyDiscontinuationReasonError_DT");
			EP.VerifyText(TabName + "InitiatorOfWithdrawalError_DT");
			EP.VerifyText(TabName + "StudyDateError_DT");// Date must be after
															// the consents date
			EP.VerifyRegExp(TabName + "MeasurementRangeError_DT");

			// Incorrect Format
			EP.Edit_SetValue(TabName + "Measurement_EB", IncorrectFormat);
			EP.Btn_Click(TabName + "Save_BN");
			EP.VerifyText(TabName + "IncorrecrFormatError_DT");
			EP.VerifyText(TabName + "MeasurementFormatError_DT");

			EP.DrpDwn_SelectIndex(TabName + "DiscontinuationReasonIS_DD",
					EP.RandomValue(2, 5));
			EP.DrpDwn_SelectIndex(TabName + "InitiatorOfWithdrawalAT_DD",
					EP.RandomValue(1, 4));
			EP.Edit_SetValue(TabName + "Measurement_EB", "56");
			EP.SelectDate(TabName + "DiscontinuedLastStudyDayCalendar_TT", EP
					.Date_AddDays(EP.TestData("ECOSPatientDOB"), 2,
							Calendar.MONTH));
			EP.Btn_Click(TabName + "Save_BN");
			ECOS.VerifySuccessMessage(EP.LangData("CompletionOrTermination"));
			EP.SearchForPatient(EP.TestData("ECOSPatient"));
			EP.WbTbl_VerifyTableColumn("#MyPatients_PatientsInfoTable_TH",
					"StudyID", 3, 1, EP.LangData("Discontinued"), false);

			// Error message if the difference in height between the last two
			// visits is >0.5
			EP.Rbtn_SetOption("#MyPatients_FirstPatient_RB", "ON");
			EP.Btn_Click("#MyPatients_VisitsRecord_BN");
			EP.WbTbl_SortColumn("#AddVisit_VisitDate_TE", 2, "DESC");
			EP.Rbtn_SetOption("#AddVisit_FirstVisit_RB", "ON");// Set
																// prerequisite
																// for Latest
																// first visit
			EP.Btn_Click("#AddVisit_Continue_BN");
			if (!EP.ObjectExists("#General_Edit_IM")) { // If the Record is
														// present then Edit it
														// or else create a new
														// record
				EP.Btn_Click("#General_First_BN"); // Click Add button
				EP.WaitForPageload();
			} else {
				EP.Btn_Click("#General_Edit_IM"); // Click Edit button
				EP.WaitForPageload();
			}
			EP.Edit_SetValue("#AddVisit_AuxologicalHeight_EB", "56");
			EP.HideCursor();
			EP.WaitForPageload();
			EP.Btn_Click("#General_AddRecordSave_BN"); // click save button

			if (!EP.ObjectExists("#General_Edit_IM")) {
				EP.Lnk_Click("#General_AddRecordSave_BN"); // Until getting
															// saved.
				EP.WaitForPageload();
			}
			EP.Lnk_Click("#NavigationTab_RecordOverview_LK");
			EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");

			EP.DrpDwn_SelectIndex(TabName + "StatusOfStudy_DD", 1);
			EP.TakeStaticScreenshot("StudyCompleted");
			EP.DrpDwn_SelectIndex(TabName + "StudyCompletion_DD", 1);
			EP.DrpDwn_SelectIndex(TabName + "SubjectHeight_DD", 1);
			EP.Edit_SetValue(TabName + "DateOfMeasurement_EB", "57");
			EP.Btn_Click(TabName + "Save_BN");
			EP.CompareText("OutOfRangeErr_DT", EP.LangData("OutOfRangeError"),
					EP.GetText(TabName + "IncorrecrFormatError_DT"));
			// EP.CompareText("PatientHeightErrMsg_DT",
			// EP.LangData("FinalHeightDifference"),
			// EP.GetText(TabName+"PatientHeightErrMsgAT_DT"));

			// Revert the changes made to height field in visits record
			EP.Lnk_Click("#NavigationTab_MyPatientsList_LK");
			EP.SearchForPatient(EP.TestData("ECOSPatient"));
			EP.Rbtn_SetOption("#MyPatients_FirstPatient_RB", "ON");
			EP.Btn_Click("#MyPatients_VisitsRecord_BN");
			EP.WbTbl_SortColumn("#AddVisit_VisitDate_TE", 2, "DESC");
			EP.Rbtn_SetOption("#AddVisit_FirstVisit_RB", "ON");// Set
																// prerequisite
																// for Latest
																// first visit
			EP.Btn_Click("#AddVisit_Continue_BN");
			if (!EP.ObjectExists("#General_Edit_IM")) { // If the Record is
														// present then Edit it
														// or else create a new
														// record
				EP.Btn_Click("#General_First_BN"); // Click Add button
				EP.WaitForPageload();
			} else {
				EP.Btn_Click("#General_Edit_IM"); // Click Edit button
				EP.WaitForPageload();
			}
			EP.Edit_SetValue("#AddVisit_AuxologicalHeight_EB", "55.4");
			EP.HideCursor();
			EP.WaitForPageload();
			EP.Btn_Click("#General_AddRecordSave_BN"); // click save button

			if (!EP.ObjectExists("#General_Edit_IM")) {
				EP.Lnk_Click("#General_AddRecordSave_BN"); // Until getting
															// saved.
				EP.WaitForPageload();
			}

			break;

		}

	}

	public void DiscontinuedHCPDynamic(String ActionDone, String TabIndex,
			String RecoveringURL, String TreatmentStartDate) {

		{

			Easypod EP = new Easypod();
			ECOSStudy ECOS = new ECOSStudy(EP);

			TreatmentStartDate = EP.Date_AddDays(TreatmentStartDate, -1,
					Calendar.DATE);
			switch (ECOS.switchCountryName) {

			case ITALY:
				ItalyECRF ItalyDR = new ItalyECRF(EP);
				ItalyDR.DiscontinuedHCPDynamic(ActionDone, TabIndex,
						RecoveringURL, TreatmentStartDate);
				ItalyDR = null;
				break;

			case CANADA:
				CanadaECRF CanadaDR = new CanadaECRF(EP);
				CanadaDR.DiscontinuedHCPDynamic(ActionDone, TabIndex,
						RecoveringURL, TreatmentStartDate);
				CanadaDR = null;
				break;

			case COLOMBIA:
				ColombiaECRF ColombiaDR = new ColombiaECRF(EP);
				ColombiaDR.DiscontinuedHCPDynamic(ActionDone, TabIndex,
						RecoveringURL, TreatmentStartDate);
				ColombiaDR = null;
				break;

			case INDONESIA:
				IndonesiaECRF IndonesiaDR = new IndonesiaECRF(EP);
				IndonesiaDR.DiscontinuedHCPDynamic(ActionDone, TabIndex,
						RecoveringURL, TreatmentStartDate);
				IndonesiaDR = null;
				break;

			case UNITEDKINGDOM:

				UnitedKingdomECRF UntiedKingdomDR = new UnitedKingdomECRF(EP);
				UntiedKingdomDR.DiscontinuedHCPDynamic(ActionDone, TabIndex,
						RecoveringURL, TreatmentStartDate);
				UntiedKingdomDR = null;
				break;

			case TAIWAN:
				TaiwanECRF TaiwanDR = new TaiwanECRF(EP);
				TaiwanDR.DiscontinuedHCPDynamic(ActionDone, TabIndex,
						RecoveringURL, TreatmentStartDate);
				TaiwanDR = null;
				break;

			case AUSTRIA:
				AustriaECRF AustriaDR = new AustriaECRF(EP);
				AustriaDR.DiscontinuedHCPDynamic(ActionDone, TabIndex,
						RecoveringURL, TreatmentStartDate);
				AustriaDR = null;
				break;

			case ARGENTINA:
				ArgentinaECRF ArgentinaDR = new ArgentinaECRF(EP);
				ArgentinaDR.DiscontinuedHCPDynamic(ActionDone, TabIndex,
						RecoveringURL, TreatmentStartDate);
				ArgentinaDR = null;
				break;

			case SINGAPORE:
				SingaporeECRF SingaporeDR = new SingaporeECRF(EP);
				SingaporeDR.DiscontinuedHCPDynamic(ActionDone, TabIndex,
						RecoveringURL, TreatmentStartDate);
				SingaporeDR = null;
				break;

			case UNITEDARABEMIRATES:
				UnitedArabEmiratesECRF UnitedArabEmiratesDR = new UnitedArabEmiratesECRF(
						EP);
				UnitedArabEmiratesDR.DiscontinuedHCPDynamic(ActionDone,
						TabIndex, RecoveringURL, TreatmentStartDate);
				UnitedArabEmiratesDR = null;
				break;

			case SAUDIARABIA:
				SaudiArabiaECRF SaudiArabiaDR = new SaudiArabiaECRF(EP);
				SaudiArabiaDR.DiscontinuedHCPDynamic(ActionDone, TabIndex,
						RecoveringURL, TreatmentStartDate);
				SingaporeDR = null;
				break;

			case FRANCE:
				FranceECRF FranceDR = new FranceECRF(EP);
				FranceDR.DiscontinuedHCPDynamic(ActionDone, TabIndex,
						RecoveringURL, TreatmentStartDate);
				FranceDR = null;
				break;

			case SWEDEN:
				SwedenECRF SwedenDR = new SwedenECRF(EP);
				SwedenDR.DiscontinuedHCPDynamic(ActionDone, TabIndex,
						RecoveringURL, TreatmentStartDate);
				SwedenDR = null;
				break;

			case CZECHREPUBLIC:
				CzechRepublicECRF CzechDR = new CzechRepublicECRF(EP);
				CzechDR.DiscontinuedHCPDynamic(ActionDone, TabIndex,
						RecoveringURL, TreatmentStartDate);
				CzechDR = null;
				break;

			case NORWAY:
				NorwayECRF NorwayDR = new NorwayECRF(EP);
				NorwayDR.DiscontinuedHCPDynamic(ActionDone, TabIndex,
						RecoveringURL, TreatmentStartDate);
				NorwayDR = null;
				break;

			case HUNGARY:
				HungaryECRF HungaryDR = new HungaryECRF(EP);
				HungaryDR.DiscontinuedHCPDynamic(ActionDone, TabIndex,
						RecoveringURL, TreatmentStartDate);
				HungaryDR = null;
				break;

			case SLOVAKIA:
				SlovakiaECRF SlovakiaDR = new SlovakiaECRF(EP);
				SlovakiaDR.DiscontinuedHCPDynamic(ActionDone, TabIndex,
						RecoveringURL, TreatmentStartDate);
				SlovakiaDR = null;
				break;

			case SPAIN:
				SpainECRF SpainDR = new SpainECRF(EP);
				SpainDR.DiscontinuedHCPDynamic(ActionDone, TabIndex,
						RecoveringURL, TreatmentStartDate);
				SpainDR = null;
				break;

			case GREECE:
				GreeceECRF GreeceDR = new GreeceECRF(EP);
				GreeceDR.DiscontinuedHCPDynamic(ActionDone, TabIndex,
						RecoveringURL, TreatmentStartDate);
				GreeceDR = null;
				break;

			case FINLAND:
				FinlandECRF FinlandDR = new FinlandECRF(EP);
				FinlandDR.DiscontinuedHCPDynamic("CompleteTermination",
						TabIndex, RecoveringURL, TreatmentStartDate);
				FinlandDR = null;
				break;

			case KOREA:

				KoreaECRF KoreaDR = new KoreaECRF(EP);
				KoreaDR.DiscontinuedHCPDynamic("CompleteTermination", TabIndex,
						RecoveringURL, TreatmentStartDate);
				KoreaDR = null;
				break;

			case AUSTRALIA:

				AustraliaECRF AustraliaDR = new AustraliaECRF(EP);
				AustraliaDR.DiscontinuedHCPDynamic(ActionDone, TabIndex,
						RecoveringURL, TreatmentStartDate);
				AustraliaDR = null;
				break;

			case MEXICO:
				MexicoECRF MexicoDR = new MexicoECRF(EP);
				MexicoDR.DiscontinuedHCPDynamic(ActionDone, TabIndex,
						RecoveringURL, TreatmentStartDate);
				MexicoDR = null;
				break;

			/*
			 * //case CHINA: //case ARGENTINA:
			 * 
			 * 
			 * // *-*-*-*-*-*-*-*Static Contents with Status =
			 * "Discontinued"*-*-*-*-*-*-*-*-*
			 * EP.DrpDwn_SelectIndex(TabName+"StatusOfStudy_DD", 2);
			 * EP.TakeStaticScreenshot("StudyDiscontinued");
			 * EP.VerifyText(TabName+"PrematureStudyDiscontinuationReason_ST");
			 * EP
			 * .CompareDynamicDropDownValues(TabName+"DiscontinuationReasonAT_DD"
			 * , EP.ORData(TabName+"DiscontinuationReasonAT_DD"), false);
			 * EP.DrpDwn_SelectIndex(TabName+"DiscontinuationReasonAT_DD",
			 * 2);//To make visible Remove data from Study?
			 * 
			 * EP.VerifyText(TabName+"RemoveDataFromStudyAT_ST");
			 * EP.VerifyText(TabName+"RemoveDataYes_ST");
			 * EP.VerifyText(TabName+"RemoveDataNo_ST");
			 * 
			 * EP.VerifyText(TabName+"InitiatorOfWithdrawalAT_ST");
			 * EP.CompareDynamicDropDownValues
			 * (TabName+"InitiatorOfWithdrawalAT_DD",
			 * EP.ORData(TabName+"InitiatorOfWithdrawalAT_DD"), false);
			 * EP.VerifyText(TabName+"PatientHeightAtPrematureAT_ST");
			 * EP.CompareDynamicDropDownValues
			 * (TabName+"PatientHeightAtDiscontinuation_DD",
			 * EP.ORData(TabName+"PatientHeightAtDiscontinuation_DD"), false);
			 * EP.VerifyText(TabName+"DisContLastStudyDayAT_ST");
			 * EP.VerifyToolTip(TabName+"DiscontinuedLastStudyDayCalendar_TT");
			 * EP.VerifyText(TabName+"DiscontinuedMeasurementAT_ST");
			 * EP.VerifyTextContained(TabName+"MeasurementCm_ST");
			 * 
			 * EP.VerifyPageLayout(); // if
			 * (Env.GetBrowserUsed().startsWith("SA")){ //
			 * EP.NavigateTo(RecoveringURL); // }
			 * 
			 * //Validation of "Other, Specify" error messages
			 * EP.DrpDwn_SelectIndex(TabName+"StatusOfStudy_DD", 2);
			 * EP.DrpDwn_SelectIndex(TabName+"DiscontinuationReasonAT_DD", 6);
			 * EP.Edit_ClearValue(TabName+"DiscontinuationReason_EB");
			 * EP.DrpDwn_SelectIndex(TabName+"InitiatorOfWithdrawalAT_DD", 5);
			 * EP.Edit_ClearValue(TabName+"InitiatorOfWithdrawal_EB");
			 * EP.SelectDate(TabName+"DiscontinuedLastStudyDayCalendar_TT",
			 * EP.TestData("ECOSPatientDOB"));
			 * EP.Edit_SetValue(TabName+"Measurement_EB", MinValue);
			 * EP.Btn_Click(TabName+"Save_BN");
			 * EP.VerifyText(TabName+"ValueOutOfRangeError_DT");//Header
			 * EP.VerifyText(TabName+"StudyDiscontinuationReasonError_DT");
			 * EP.VerifyText(TabName+"InitiatorOfWithdrawalError_DT");
			 * EP.VerifyText(TabName+"StudyDateError_DT");//Date must be after
			 * the consents date
			 * EP.VerifyRegExp(TabName+"MeasurementRangeError_DT");
			 * 
			 * //Incorrect Format EP.Edit_SetValue(TabName+"Measurement_EB",
			 * IncorrectFormat); EP.Btn_Click(TabName+"Save_BN");
			 * EP.VerifyText(TabName+"IncorrecrFormatError_DT");
			 * EP.VerifyText(TabName+"MeasurementFormatError_DT");
			 * 
			 * EP.DrpDwn_SelectIndex(TabName+"DiscontinuationReasonAT_DD",
			 * EP.RandomValue(2, 5));
			 * EP.DrpDwn_SelectIndex(TabName+"InitiatorOfWithdrawalAT_DD",
			 * EP.RandomValue(1, 4)); EP.Edit_SetValue(TabName+"Measurement_EB",
			 * "56");
			 * EP.SelectDate(TabName+"DiscontinuedLastStudyDayCalendar_TT"
			 * ,EP.Date_AddDays(EP.TestData("ECOSPatientDOB"), 2,
			 * Calendar.MONTH)); EP.Btn_Click(TabName+"Save_BN");
			 * ECOS.VerifySuccessMessage
			 * (EP.LangData("CompletionOrTermination"));
			 * EP.SearchForPatient(EP.TestData("ECOSPatient"));
			 * EP.WbTbl_VerifyTableColumn("#MyPatients_PatientsInfoTable_TH",
			 * "StudyID", 3, 1, EP.LangData("Discontinued"), false);
			 * 
			 * //Error message if the difference in height between the last two
			 * visits is >0.5 EP.Rbtn_SetOption("#MyPatients_FirstPatient_RB",
			 * "ON"); EP.Btn_Click("#MyPatients_VisitsRecord_BN");
			 * EP.WbTbl_SortColumn("#AddVisit_VisitDate_TE", 2, "DESC");
			 * EP.Rbtn_SetOption("#AddVisit_FirstVisit_RB", "ON");//Set
			 * prerequisite for Latest first visit
			 * EP.Btn_Click("#AddVisit_Continue_BN"); if
			 * (!EP.ObjectExists("#General_Edit_IM")){ //If the Record is
			 * present then Edit it or else create a new record
			 * EP.Btn_Click("#General_First_BN"); //Click Add button
			 * EP.WaitForPageload(); }else{ EP.Btn_Click("#General_Edit_IM");
			 * //Click Edit button EP.WaitForPageload(); }
			 * EP.Edit_SetValue("#AddVisit_AuxologicalHeight_EB", "56");
			 * EP.HideCursor(); EP.WaitForPageload();
			 * EP.Btn_Click("#General_AddRecordSave_BN"); //click save button
			 * 
			 * if (!EP.ObjectExists("#General_Edit_IM")){
			 * EP.Lnk_Click("#General_AddRecordSave_BN"); //Until getting saved.
			 * EP.WaitForPageload(); }
			 * EP.Lnk_Click("#NavigationTab_RecordOverview_LK");
			 * EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");
			 * 
			 * EP.DrpDwn_SelectIndex(TabName+"StatusOfStudy_DD", 1);
			 * EP.TakeStaticScreenshot("StudyCompleted");
			 * EP.DrpDwn_SelectIndex(TabName+"StudyCompletion_DD",1);
			 * EP.DrpDwn_SelectIndex(TabName+"SubjectHeight_DD", 1);
			 * EP.Edit_SetValue(TabName+"DateOfMeasurement_EB", "57");
			 * EP.Btn_Click(TabName+"Save_BN");
			 * EP.CompareText("OutOfRangeErr_DT",
			 * EP.LangData("OutOfRangeError"),
			 * EP.GetText(TabName+"IncorrecrFormatError_DT"));
			 * EP.CompareText("PatientHeightErrMsg_DT",
			 * EP.LangData("FinalHeightDifference"),
			 * EP.GetText(TabName+"PatientHeightErrMsgAT_DT"));
			 * 
			 * //Revert the changes made to height field in visits record
			 * EP.Lnk_Click("#NavigationTab_MyPatientsList_LK");
			 * EP.SearchForPatient(EP.TestData("ECOSPatient"));
			 * EP.Rbtn_SetOption("#MyPatients_FirstPatient_RB", "ON");
			 * EP.Btn_Click("#MyPatients_VisitsRecord_BN");
			 * EP.WbTbl_SortColumn("#AddVisit_VisitDate_TE", 2, "DESC");
			 * EP.Rbtn_SetOption("#AddVisit_FirstVisit_RB", "ON");//Set
			 * prerequisite for Latest first visit
			 * EP.Btn_Click("#AddVisit_Continue_BN"); if
			 * (!EP.ObjectExists("#General_Edit_IM")){ //If the Record is
			 * present then Edit it or else create a new record
			 * EP.Btn_Click("#General_First_BN"); //Click Add button
			 * EP.WaitForPageload(); }else{ EP.Btn_Click("#General_Edit_IM");
			 * //Click Edit button EP.WaitForPageload(); }
			 * EP.Edit_SetValue("#AddVisit_AuxologicalHeight_EB", "55.4");
			 * EP.HideCursor(); EP.WaitForPageload();
			 * EP.Btn_Click("#General_AddRecordSave_BN"); //click save button
			 * 
			 * if (!EP.ObjectExists("#General_Edit_IM")){
			 * EP.Lnk_Click("#General_AddRecordSave_BN"); //Until getting saved.
			 * EP.WaitForPageload(); }
			 * 
			 * break;
			 */

			case ICELAND:
				IcelandECRF IcelandDR = new IcelandECRF(EP);
				IcelandDR.DiscontinuedHCPDynamic(ActionDone, TabIndex,
						RecoveringURL, TreatmentStartDate);
				IcelandDR = null;
				break;

			}
		}

	}

	public void CompletionHCPDynamic(String ActionDone, String TabIndex,
			String RecoveringURL, String TreatmentStartDate) {
		Easypod EP = new Easypod();
		ECOSStudy ECOS = new ECOSStudy(EP);

		TreatmentStartDate = EP.Date_AddDays(TreatmentStartDate, -1,
				Calendar.DATE);

		switch (ECOS.switchCountryName) {

		case ITALY:
			ItalyECRF ItalyDR = new ItalyECRF(EP);
			ItalyDR.CompletionHCPDynamic(ActionDone, TabIndex, RecoveringURL,
					TreatmentStartDate);
			ItalyDR = null;

			break;

		case CANADA:
			CanadaECRF CanadaDR = new CanadaECRF(EP);
			CanadaDR.CompletionHCPDynamic(ActionDone, TabIndex, RecoveringURL,
					TreatmentStartDate);
			CanadaDR = null;
			break;

		case UNITEDKINGDOM:
			// Completed Section
			UnitedKingdomECRF UnitedKingdomDR = new UnitedKingdomECRF(EP);
			UnitedKingdomDR.CompletionHCPDynamic(ActionDone, TabIndex,
					RecoveringURL, TreatmentStartDate);
			UnitedKingdomDR = null;
			break;

		case AUSTRIA:
			AustriaECRF AustriaDR = new AustriaECRF(EP);
			AustriaDR.CompletionHCPDynamic(ActionDone, TabIndex, RecoveringURL,
					TreatmentStartDate);
			AustriaDR = null;
			break;

		case INDONESIA:
			IndonesiaECRF IndonesiaDR = new IndonesiaECRF(EP);
			IndonesiaDR.CompletionHCPDynamic(ActionDone, TabIndex,
					RecoveringURL, TreatmentStartDate);
			IndonesiaDR = null;
			break;

		case COLOMBIA:
			ColombiaECRF ColombiaDR = new ColombiaECRF(EP);
			ColombiaDR.CompletionHCPDynamic(ActionDone, TabIndex,
					RecoveringURL, TreatmentStartDate);
			ColombiaDR = null;
			break;

		case ARGENTINA:
			ArgentinaECRF ArgentinaDR = new ArgentinaECRF(EP);
			ArgentinaDR.CompletionHCPDynamic(ActionDone, TabIndex,
					RecoveringURL, TreatmentStartDate);
			ArgentinaDR = null;
			break;

		case SINGAPORE:
			SingaporeECRF SingaporeDR = new SingaporeECRF(EP);
			SingaporeDR.CompletionHCPDynamic(ActionDone, TabIndex,
					RecoveringURL, TreatmentStartDate);
			SingaporeDR = null;
			break;

		case TAIWAN:
			TaiwanECRF TaiwanDR = new TaiwanECRF(EP);
			TaiwanDR.CompletionHCPDynamic(ActionDone, TabIndex, RecoveringURL,
					TreatmentStartDate);
			TaiwanDR = null;
			break;

		case UNITEDARABEMIRATES:
			UnitedArabEmiratesECRF UnitedArabEmiratesDR = new UnitedArabEmiratesECRF(
					EP);
			UnitedArabEmiratesDR.CompletionHCPDynamic(ActionDone, TabIndex,
					RecoveringURL, TreatmentStartDate);
			UnitedArabEmiratesDR = null;
			break;

		case SAUDIARABIA:
			SaudiArabiaECRF SaudiArabiaDR = new SaudiArabiaECRF(EP);
			SaudiArabiaDR.CompletionHCPDynamic(ActionDone, TabIndex,
					RecoveringURL, TreatmentStartDate);
			SingaporeDR = null;
			break;

		case FRANCE:
			FranceECRF FranceDR = new FranceECRF(EP);
			FranceDR.CompletionHCPDynamic(ActionDone, TabIndex, RecoveringURL,
					TreatmentStartDate);
			FranceDR = null;
			break;

		case SWEDEN:
			SwedenECRF SwedenDR = new SwedenECRF(EP);
			SwedenDR.CompletionHCPDynamic(ActionDone, TabIndex, RecoveringURL,
					TreatmentStartDate);
			SwedenDR = null;
			break;

		case CZECHREPUBLIC:
			CzechRepublicECRF CzechDR = new CzechRepublicECRF(EP);
			CzechDR.CompletionHCPDynamic(ActionDone, TabIndex, RecoveringURL,
					TreatmentStartDate);
			CzechDR = null;
			break;

		case NORWAY:
			NorwayECRF NorwayDR = new NorwayECRF(EP);
			NorwayDR.CompletionHCPDynamic(ActionDone, TabIndex, RecoveringURL,
					TreatmentStartDate);
			NorwayDR = null;
			break;

		case HUNGARY:
			HungaryECRF HungaryDR = new HungaryECRF(EP);
			HungaryDR.CompletionHCPDynamic(ActionDone, TabIndex, RecoveringURL,
					TreatmentStartDate);
			HungaryDR = null;
			break;

		case SLOVAKIA:
			SlovakiaECRF SlovakiaDR = new SlovakiaECRF(EP);
			SlovakiaDR.CompletionHCPDynamic(ActionDone, TabIndex,
					RecoveringURL, TreatmentStartDate);
			SlovakiaDR = null;
			break;

		case SPAIN:
			SpainECRF SpainDR = new SpainECRF(EP);
			SpainDR.CompletionHCPDynamic(ActionDone, TabIndex, RecoveringURL,
					TreatmentStartDate);
			SpainDR = null;
			break;

		case GREECE:
			GreeceECRF GreeceDR = new GreeceECRF(EP);
			GreeceDR.CompletionHCPDynamic(ActionDone, TabIndex, RecoveringURL,
					TreatmentStartDate);
			GreeceDR = null;
			break;

		case FINLAND:
			FinlandECRF FinlandDR = new FinlandECRF(EP);
			FinlandDR.CompletionHCPDynamic("CompleteTermination", TabIndex,
					RecoveringURL, TreatmentStartDate);
			FinlandDR = null;
			break;

		case KOREA:
			KoreaECRF KoreaDR = new KoreaECRF(EP);
			KoreaDR.CompletionHCPDynamic("CompleteTermination", TabIndex,
					RecoveringURL, TreatmentStartDate);
			KoreaDR = null;
			break;

		case AUSTRALIA:
			AustraliaECRF AustraliaDR = new AustraliaECRF(EP);
			AustraliaDR.CompletionHCPDynamic(ActionDone, TabIndex,
					RecoveringURL, TreatmentStartDate);
			AustraliaDR = null;
			break;

		case MEXICO:
			MexicoECRF MexicoDR = new MexicoECRF(EP);
			MexicoDR.CompletionHCPDynamic(ActionDone, TabIndex, RecoveringURL,
					TreatmentStartDate);
			MexicoDR = null;
			break;

		/*
		 * case CHINA: case ARGENTINA: // *-*-*-*-*-*-*-*-*-*-*Static Contents
		 * with Status = Completed*-*-*-*-*-*-*-*-*-*-*
		 * EP.DrpDwn_SelectIndex(TabName+"StatusOfStudy_DD", 1);
		 * EP.TakeStaticScreenshot("StudyCompleted");
		 * 
		 * EP.VerifyText(TabName+"StudyCompletion_ST");
		 * EP.CompareDropDownValues(TabName+"StudyCompletion_DD");
		 * EP.VerifyText(TabName+"PatientHeightAtEndOfStudyAT_ST");
		 * EP.CompareDropDownValues(TabName+"SubjectHeight_DD");
		 * EP.VerifyText(TabName+"DateOfMeasurement_ST");
		 * EP.VerifyToolTip(TabName+"DateOFMeasurementCalendar_TT");
		 * EP.VerifyText(TabName+"Measurement_ST");
		 * EP.VerifyTextContained(TabName+"MeasurementCmScale_ST");
		 * EP.VerifyText(TabName+"LastStudyDay_ST");
		 * EP.VerifyToolTip(TabName+"LastStudyDayCalendar_TT");
		 * 
		 * EP.VerifyPageLayout(); // if (Env.GetBrowserUsed().startsWith("SA")){
		 * // EP.NavigateTo(RecoveringURL); // }
		 * 
		 * //Dynamic Contents with Status = Completed
		 * EP.DrpDwn_SelectIndex(TabName+"StatusOfStudy_DD", 1);
		 * 
		 * //Error Mesaage Validations
		 * EP.DrpDwn_SelectIndex(TabName+"StudyCompletion_DD",2); //Study
		 * Completion EP.DrpDwn_SelectIndex(TabName+"SubjectHeight_DD", 3);
		 * EP.Edit_SetValue(TabName+"DateOfMeasurement_EB", "0");
		 * EP.SelectDate(TabName
		 * +"LastStudyDayCalendar_TT",EP.TestData("ECOSPatientDOB"));
		 * EP.Btn_Click(TabName+"Save_BN");
		 * EP.VerifyText(TabName+"IncorrecrFormatError_DT");//Header error msg
		 * EP.CompareText("StudyCompletionErrMsg_DT",
		 * EP.LangData("AccordanceWithAnother"),
		 * EP.GetText(TabName+"StudyCompletionErrMsg_DT"));
		 * EP.CompareText("PatientHeightErrMsg_DT",
		 * EP.LangData("AccordanceWithAnother"),
		 * EP.GetText(TabName+"PatientHeightErrMsgAT_DT"));
		 * EP.VerifyRegExp(TabName+"MeasurementErrMsg_DT");
		 * EP.CompareText("LastStudyDayErrMsg_DT",
		 * EP.LangData("LastStudyDateError"),
		 * EP.GetText(TabName+"LastStudyDayErrMsg_DT"));
		 * 
		 * EP.DrpDwn_SelectIndex(TabName+"SubjectHeight_DD", 1);
		 * EP.Edit_ClearValue(TabName+"DateOfMeasurement_EB");
		 * EP.SelectDate(TabName
		 * +"LastStudyDayCalendar_TT",EP.Date_AddDays(EP.TestData
		 * ("ECOSPatientDOB"), 2, Calendar.MONTH));
		 * EP.Btn_Click(TabName+"Save_BN"); EP.CompareText("OutOfRangeErr_DT",
		 * EP.LangData("VerifyIncorrectFormats"),
		 * EP.GetText(TabName+"IncorrecrFormatError_DT"));
		 * EP.CompareText("PatientHeightErrMsg_DT",
		 * EP.LangData("FinalHeightSmaller"),
		 * EP.GetText(TabName+"PatientHeightErrMsgAT_DT"));
		 * EP.CompareText("MeasurementErrMsg_DT",
		 * EP.LangData("FieldIsMandatory"),
		 * EP.GetText(TabName+"MeasurementErrMsg_DT"));
		 * 
		 * EP.DrpDwn_SelectIndex(TabName+"SubjectHeight_DD", 2);
		 * EP.Edit_SetValue(TabName+"DateOfMeasurement_EB", IncorrectFormat);
		 * EP.Btn_Click(TabName+"Save_BN");
		 * EP.CompareText("MeasurementErrMsg_DT",
		 * EP.LangData("IncorrectFormat"),
		 * EP.GetText(TabName+"MeasurementErrMsg_DT"));
		 * EP.CompareText("PatientHeightErrMsg_DT",
		 * EP.LangData("NearFinalSmaller"),
		 * EP.GetText(TabName+"PatientHeightErrMsgAT_DT"));
		 * 
		 * EP.DrpDwn_SelectIndex(TabName+"StudyCompletion_DD",1);
		 * EP.DrpDwn_SelectIndex(TabName+"SubjectHeight_DD", 1);
		 * EP.Edit_SetValue(TabName+"DateOfMeasurement_EB", "100");
		 * EP.Btn_Click(TabName+"Save_BN"); EP.CompareText("OutOfRangeErr_DT",
		 * EP.LangData("OutOfRangeError"),
		 * EP.GetText(TabName+"IncorrecrFormatError_DT"));
		 * EP.CompareText("PatientHeightErrMsg_DT",
		 * EP.LangData("AgeMustBeGreaterThan15"),
		 * EP.GetText(TabName+"PatientHeightErrMsgAT_DT"));
		 * 
		 * //For success message
		 * EP.DrpDwn_SelectIndex(TabName+"StudyCompletion_DD",3);//1
		 * EP.DrpDwn_SelectIndex(TabName+"SubjectHeight_DD", 3);//1
		 * EP.Btn_Click(TabName+"Save_BN");
		 * ECOS.VerifySuccessMessage(EP.LangData("CompletionOrTermination"));
		 * EP.SearchForPatient(EP.TestData("ECOSPatient"));
		 * EP.WbTbl_VerifyTableColumn("#MyPatients_PatientsInfoTable_TH",
		 * "StudyID", 3, 1, EP.LangData("Completed"), false);
		 * 
		 * // *-*-*-*-*-*-*-*Static Contents with Status =
		 * "Discontinued"*-*-*-*-*-*-*-*-*
		 * 
		 * //Error message if the difference in height between the last two
		 * visits is >0.5 EP.Rbtn_SetOption("#MyPatients_FirstPatient_RB",
		 * "ON"); EP.Btn_Click("#MyPatients_VisitsRecord_BN");
		 * EP.WbTbl_SortColumn("#AddVisit_VisitDate_TE", 2, "DESC");
		 * EP.Rbtn_SetOption("#AddVisit_FirstVisit_RB", "ON");//Set prerequisite
		 * for Latest first visit EP.Btn_Click("#AddVisit_Continue_BN"); if
		 * (!EP.ObjectExists("#General_Edit_IM")){ //If the Record is present
		 * then Edit it or else create a new record
		 * EP.Btn_Click("#General_First_BN"); //Click Add button
		 * EP.WaitForPageload(); }else{ EP.Btn_Click("#General_Edit_IM");
		 * //Click Edit button EP.WaitForPageload(); }
		 * EP.Edit_SetValue("#AddVisit_AuxologicalHeight_EB", "56");
		 * EP.HideCursor(); EP.WaitForPageload();
		 * EP.Btn_Click("#General_AddRecordSave_BN"); //click save button
		 * 
		 * if (!EP.ObjectExists("#General_Edit_IM")){
		 * EP.Lnk_Click("#General_AddRecordSave_BN"); //Until getting saved.
		 * EP.WaitForPageload(); }
		 * EP.Lnk_Click("#NavigationTab_RecordOverview_LK");
		 * EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");
		 * 
		 * EP.DrpDwn_SelectIndex(TabName+"StatusOfStudy_DD", 1);
		 * EP.TakeStaticScreenshot("StudyCompleted");
		 * EP.DrpDwn_SelectIndex(TabName+"StudyCompletion_DD",1);
		 * EP.DrpDwn_SelectIndex(TabName+"SubjectHeight_DD", 1);
		 * EP.Edit_SetValue(TabName+"DateOfMeasurement_EB", "57");
		 * EP.Btn_Click(TabName+"Save_BN"); EP.CompareText("OutOfRangeErr_DT",
		 * EP.LangData("OutOfRangeError"),
		 * EP.GetText(TabName+"IncorrecrFormatError_DT"));
		 * EP.CompareText("PatientHeightErrMsg_DT",
		 * EP.LangData("FinalHeightDifference"),
		 * EP.GetText(TabName+"PatientHeightErrMsgAT_DT"));
		 * 
		 * //Revert the changes made to height field in visits record
		 * EP.Lnk_Click("#NavigationTab_MyPatientsList_LK");
		 * EP.SearchForPatient(EP.TestData("ECOSPatient"));
		 * EP.Rbtn_SetOption("#MyPatients_FirstPatient_RB", "ON");
		 * EP.Btn_Click("#MyPatients_VisitsRecord_BN");
		 * EP.WbTbl_SortColumn("#AddVisit_VisitDate_TE", 2, "DESC");
		 * EP.Rbtn_SetOption("#AddVisit_FirstVisit_RB", "ON");//Set prerequisite
		 * for Latest first visit EP.Btn_Click("#AddVisit_Continue_BN"); if
		 * (!EP.ObjectExists("#General_Edit_IM")){ //If the Record is present
		 * then Edit it or else create a new record
		 * EP.Btn_Click("#General_First_BN"); //Click Add button
		 * EP.WaitForPageload(); }else{ EP.Btn_Click("#General_Edit_IM");
		 * //Click Edit button EP.WaitForPageload(); }
		 * EP.Edit_SetValue("#AddVisit_AuxologicalHeight_EB", "55.4");
		 * EP.HideCursor(); EP.WaitForPageload();
		 * EP.Btn_Click("#General_AddRecordSave_BN"); //click save button
		 * 
		 * if (!EP.ObjectExists("#General_Edit_IM")){
		 * EP.Lnk_Click("#General_AddRecordSave_BN"); //Until getting saved.
		 * EP.WaitForPageload(); }
		 * 
		 * break;
		 */

		case ICELAND:
			IcelandECRF IcelandDR = new IcelandECRF(EP);
			IcelandDR.CompletionHCPDynamic(ActionDone, TabIndex, RecoveringURL,
					TreatmentStartDate);
			IcelandDR = null;
			break;

		}
	}

	public void InculsionExclusionStatic() {

		/*
		 * ===String ID's for Inclusion and Exclusion text======
		 * InclusionTreatmentStart = "hcp.inclexcl.treatmentStart";
		 * InclusionGuardianConsent = "hcp.inclexcl.legalGuardian";
		 * InclusionBetween2and18 = "hcp.inclexcl.between2and18";
		 * InclusionGrowthHormone = "hcp.inclexcl.growthHormone";
		 * ExclusionInvestigationalDrug = "hcp.inclexcl.investigationalDrug";
		 * ExclusionContraIndications = "hcp.inclexcl.contraIndications";
		 * ExclusionTakingSaizen = "hcp.inclexcl.takingSaizen";
		 */

		String InclusionTranslatedText = "";
		String ExclusionTranslatedText = "";
		String InclusionXPath = EP.ORXPath("#EnrollPatient_InclusionObject_ST");
		String ExclusionXPath = EP.ORXPath("#EnrollPatient_ExclusionObject_ST");

		Country switchCountryName = Country.valueOf(Env.CountryUsed.replaceAll(
				" ", "").toUpperCase());
		String Inclusion[] = switchCountryName.GetInclusionText().split("\\|");
		String Exclusion[] = switchCountryName.GetExclusionText().split("\\|");

		for (int i = 0; i < Inclusion.length; i++) {
			InclusionTranslatedText = InclusionTranslatedText + "|"
					+ EP.LangData(Inclusion[i]);
		}

		for (int i = 0; i < Exclusion.length; i++) {
			ExclusionTranslatedText = ExclusionTranslatedText + "|"
					+ EP.LangData(Exclusion[i]);
		}

		// Verify both the headings
		EP.VerifyText("#EnrollPatient_InclusionText_ST");
		EP.VerifyText("#EnrollPatient_ExclusionText_ST");

		// Verify Text in Inclusions
		String TextObject = "";
		String CurrentText = "";
		for (int i = 0; i < Inclusion.length; i++) {
			TextObject = InclusionXPath + "[" + String.valueOf(i + 1) + "]";
			CurrentText = EP.GetText(TextObject + "/div[@class='fl']");
			EP.CompareWithList("InclusionText" + (i + 1) + "_ST",
					InclusionTranslatedText, CurrentText);
			InclusionTranslatedText = InclusionTranslatedText.replace("|"
					+ CurrentText, "");
			EP.CompareText("InclusionYes" + (i + 1) + "_ST",
					EP.LangData("Yes"), EP.GetText(TextObject + "//label[1]"));
			EP.CompareText("InclusionNo" + (i + 1) + "_ST", EP.LangData("No"),
					EP.GetText(TextObject + "//label[2]"));
		}

		// Verify Text in Exclusions
		for (int i = 0; i < Exclusion.length; i++) {
			TextObject = ExclusionXPath + "[" + String.valueOf(i + 1) + "]";
			CurrentText = EP.GetText(TextObject + "/div[@class='fl']");
			EP.CompareWithList("ExclusionText" + (i + 1) + "_ST",
					ExclusionTranslatedText, CurrentText);
			ExclusionTranslatedText = ExclusionTranslatedText.replace("|"
					+ CurrentText, "");
			EP.CompareText("ExclusionYes" + (i + 1) + "_ST",
					EP.LangData("Yes"), EP.GetText(TextObject + "//label[1]"));
			EP.CompareText("ExclusionNo" + (i + 1) + "_ST", EP.LangData("No"),
					EP.GetText(TextObject + "//label[2]"));
		}

	}

	public void InculsionExclusionAdd() {

		// Select Yes for all Inclusions
		String ObjectXPath = EP.ORXPath("#EnrollPatient_InclusionObject_ST")
				+ "//input[@type='radio' and @value='true']";
		List<WebElement> ObjectFields = UtilClass.browser.findElements(By
				.xpath(ObjectXPath));
		for (WebElement ObjectField : ObjectFields) {
			ObjectField.click(); // Yes Radio button
		}

		// Select No for all Exclusions
		ObjectXPath = EP.ORXPath("#EnrollPatient_ExclusionObject_ST")
				+ "//input[@type='radio' and @value='false']";
		ObjectFields = UtilClass.browser.findElements(By.xpath(ObjectXPath));
		for (WebElement ObjectField : ObjectFields) {
			ObjectField.click(); // No Radio button
		}

	}

	public void CompletionAdminStatic(String ActionDone,
			ArrayList<String> values) {
		Easypod EP = new Easypod();
		ECOSStudy ECOS = new ECOSStudy(EP);
		switch (ECOS.switchCountryName) {

		case CANADA:
			CanadaECRF CanadaDR = new CanadaECRF(EP);
			CanadaDR.CompletionAdminStatic("CompleteTermination", values);
			CanadaDR = null;
			break;

		case SWEDEN:
			SwedenECRF SwedenDR = new SwedenECRF(EP);
			SwedenDR.CompletionAdminStatic(ActionDone, values);
			SwedenDR = null;

		case KOREA:
			KoreaECRF KoreaDR = new KoreaECRF(EP);
			KoreaDR.CompletionAdminStatic("CompleteTermination", values);
			KoreaDR = null;
			break;

		case AUSTRALIA:
			AustraliaECRF AustraliaDR = new AustraliaECRF(EP);
			AustraliaDR.CompletionAdminStatic("CompleteTermination", values);
			AustraliaDR = null;
			break;

		case ARGENTINA:
			ArgentinaECRF ArgentinaDR = new ArgentinaECRF(EP);
			ArgentinaDR.CompletionAdminStatic("CompleteTermination", values);
			ArgentinaDR = null;
			break;

		case TAIWAN:
			TaiwanECRF TaiwanDR = new TaiwanECRF(EP);
			TaiwanDR.CompletionAdminStatic("CompleteTermination", values);
			TaiwanDR = null;
			break;

		case ICELAND:
			IcelandECRF IcelandDR = new IcelandECRF(EP);
			IcelandDR.CompletionAdminStatic(ActionDone, values);
			IcelandDR = null;
			break;

		case FINLAND:
			FinlandECRF FinlandDR = new FinlandECRF(EP);
			FinlandDR.CompletionAdminStatic(ActionDone, values);
			FinlandDR = null;
			break;

		case ITALY:
			ItalyECRF ItalyDR = new ItalyECRF(EP);
			ItalyDR.CompletionAdminStatic(ActionDone, values);
			ItalyDR = null;
			break;

		case HUNGARY:
			HungaryECRF HungaryDR = new HungaryECRF(EP);
			HungaryDR.CompletionAdminStatic(ActionDone, values);
			HungaryDR = null;
			break;

		case CZECHREPUBLIC:
			CzechRepublicECRF CzechDR = new CzechRepublicECRF(EP);
			CzechDR.CompletionAdminStatic(ActionDone, values);
			CzechDR = null;
			break;

		case GREECE:
			GreeceECRF GreeceDR = new GreeceECRF(EP);
			GreeceDR.CompletionAdminStatic(ActionDone, values);
			GreeceDR = null;
			break;

		case SLOVAKIA:
			SlovakiaECRF SlovakiaDR = new SlovakiaECRF(EP);
			SlovakiaDR.CompletionAdminStatic(ActionDone, values);
			SlovakiaDR = null;
			break;

		case FRANCE:
			FranceECRF FranceDR = new FranceECRF(EP);
			FranceDR.CompletionAdminStatic(ActionDone, values);
			FranceDR = null;
			break;

		case AUSTRIA:
			AustriaECRF AustriaDR = new AustriaECRF(EP);
			AustriaDR.CompletionAdminStatic(ActionDone, values);
			AustriaDR = null;
			break;

		case SINGAPORE:
			SingaporeECRF SingaporeDR = new SingaporeECRF(EP);
			SingaporeDR.CompletionAdminStatic(ActionDone, values);
			SingaporeDR = null;
			break;

		case UNITEDARABEMIRATES:
			UnitedArabEmiratesECRF UnitedArabEmiratesDR = new UnitedArabEmiratesECRF(
					EP);
			UnitedArabEmiratesDR.CompletionAdminStatic(ActionDone, values);
			UnitedArabEmiratesDR = null;
			break;

		case SAUDIARABIA:
			SaudiArabiaECRF SaudiArabiaDR = new SaudiArabiaECRF(EP);
			SaudiArabiaDR.CompletionAdminStatic(ActionDone, values);
			SingaporeDR = null;
			break;

		case MEXICO:
			MexicoECRF MexicoDR = new MexicoECRF(EP);
			MexicoDR.CompletionAdminStatic(ActionDone, values);
			MexicoDR = null;
			break;

		case NORWAY:
			NorwayECRF NorwayDR = new NorwayECRF(EP);
			NorwayDR.CompletionAdminStatic(ActionDone, values);
			NorwayDR = null;
			break;

		case SPAIN:
			SpainECRF SpainDR = new SpainECRF(EP);
			SpainDR.CompletionAdminStatic(ActionDone, values);
			SpainDR = null;
			break;

		case UNITEDKINGDOM:
			UnitedKingdomECRF UnitedKingdomDR = new UnitedKingdomECRF(EP);
			UnitedKingdomDR.CompletionAdminStatic(ActionDone, values);
			UnitedKingdomDR = null;
			break;
		}
	}

	public void DiscontinuedAdminStatic(String ActionDone,
			ArrayList<String> values) {
		Easypod EP = new Easypod();
		ECOSStudy ECOS = new ECOSStudy(EP);

		switch (ECOS.switchCountryName) {

		case CANADA:
			CanadaECRF CanadaDR = new CanadaECRF(EP);
			CanadaDR.DiscontinuedAdminStatic("CompleteTermination", values);
			CanadaDR = null;
			break;

		case KOREA:
			KoreaECRF KoreaDR = new KoreaECRF(EP);
			KoreaDR.DiscontinuedAdminStatic("CompleteTermination", values);
			KoreaDR = null;
			break;

		case AUSTRALIA:
			AustraliaECRF AustraliaDR = new AustraliaECRF(EP);
			AustraliaDR.DiscontinuedAdminStatic("CompleteTermination", values);
			AustraliaDR = null;
			break;

		case ARGENTINA:
			ArgentinaECRF ArgentinaDR = new ArgentinaECRF(EP);
			ArgentinaDR.DiscontinuedAdminStatic("CompleteTermination", values);
			ArgentinaDR = null;
			break;

		case TAIWAN:
			TaiwanECRF TaiwanDR = new TaiwanECRF(EP);
			TaiwanDR.DiscontinuedAdminStatic("CompleteTermination", values);
			TaiwanDR = null;
			break;

		case COLOMBIA:
			ColombiaECRF ColombiaDR = new ColombiaECRF(EP);
			ColombiaDR.DiscontinuedAdminStatic("CompleteTermination", values);
			ColombiaDR = null;
			break;

		case INDONESIA:
			IndonesiaECRF IndonesiaDR = new IndonesiaECRF(EP);
			IndonesiaDR.DiscontinuedAdminStatic("CompleteTermination", values);
			IndonesiaDR = null;
			break;

		case ICELAND:
			IcelandECRF IcelandDR = new IcelandECRF(EP);
			IcelandDR.DiscontinuedAdminStatic(ActionDone, values);
			IcelandDR = null;
			break;

		case FINLAND:
			FinlandECRF FinlandDR = new FinlandECRF(EP);
			FinlandDR.DiscontinuedAdminStatic(ActionDone, values);
			FinlandDR = null;
			break;

		case ITALY:
			ItalyECRF ItalyDR = new ItalyECRF(EP);
			ItalyDR.DiscontinuedAdminStatic(ActionDone, values);
			ItalyDR = null;
			break;

		case HUNGARY:
			HungaryECRF HungaryDR = new HungaryECRF(EP);
			HungaryDR.DiscontinuedAdminStatic(ActionDone, values);
			HungaryDR = null;
			break;

		case CZECHREPUBLIC:
			CzechRepublicECRF CzechDR = new CzechRepublicECRF(EP);
			CzechDR.DiscontinuedAdminStatic(ActionDone, values);
			CzechDR = null;
			break;

		case GREECE:
			GreeceECRF GreeceDR = new GreeceECRF(EP);
			GreeceDR.DiscontinuedAdminStatic(ActionDone, values);
			GreeceDR = null;
			break;

		case SLOVAKIA:
			SlovakiaECRF SlovakiaDR = new SlovakiaECRF(EP);
			SlovakiaDR.DiscontinuedAdminStatic(ActionDone, values);
			SlovakiaDR = null;
			break;

		case AUSTRIA:
			AustriaECRF AustriaDR = new AustriaECRF(EP);
			AustriaDR.DiscontinuedAdminStatic(ActionDone, values);
			AustriaDR = null;
			break;

		case SINGAPORE:
			SingaporeECRF SingaporeDR = new SingaporeECRF(EP);
			SingaporeDR.DiscontinuedAdminStatic(ActionDone, values);
			SingaporeDR = null;
			break;

		case UNITEDARABEMIRATES:
			UnitedArabEmiratesECRF UnitedArabEmiratesDR = new UnitedArabEmiratesECRF(
					EP);
			UnitedArabEmiratesDR.DiscontinuedAdminStatic(ActionDone, values);
			UnitedArabEmiratesDR = null;
			break;

		case SAUDIARABIA:
			SaudiArabiaECRF SaudiArabiaDR = new SaudiArabiaECRF(EP);
			SaudiArabiaDR.DiscontinuedAdminStatic(ActionDone, values);
			SingaporeDR = null;
			break;

		case FRANCE:
			FranceECRF FranceDR = new FranceECRF(EP);
			FranceDR.DiscontinuedAdminStatic(ActionDone, values);
			FranceDR = null;
			break;

		case NORWAY:
			NorwayECRF NorwayDR = new NorwayECRF(EP);
			NorwayDR.DiscontinuedAdminStatic(ActionDone, values);
			NorwayDR = null;
			break;

		case SPAIN:
			SpainECRF SpainDR = new SpainECRF(EP);
			SpainDR.DiscontinuedAdminStatic(ActionDone, values);
			SpainDR = null;
			break;

		case UNITEDKINGDOM:
			UnitedKingdomECRF UnitedKingdomDR = new UnitedKingdomECRF(EP);
			UnitedKingdomDR.DiscontinuedAdminStatic(ActionDone, values);
			UnitedKingdomDR = null;
			break;

		case SWEDEN:
			SwedenECRF SwedenDR = new SwedenECRF(EP);
			SwedenDR.DiscontinuedAdminStatic(ActionDone, values);
			SwedenDR = null;

		case MEXICO:
			MexicoECRF MexicoDR = new MexicoECRF(EP);
			MexicoDR.DiscontinuedAdminStatic(ActionDone, values);
			MexicoDR = null;
			break;

		default:

		}
	}

}
