package functions;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.Collator;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Level;
import org.mortbay.log.Log;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.ibm.icu.text.DecimalFormat;

//******************This file contains the reusable code for EasyPod Application******************
public class Easypod extends UtilClass {

	private PopUp OperaCheck = null;

	public final String NHSNumber1 = "5010232138";
	public final String NHSNumber2 = "6010232139";

	public final int AdherenceCalculationDuration = 52;
	public final int IATBelow = 56;
	public final int IATAbove = 110;
	public final int IATReminder = 1;
	public final int IDUT = 10;
	public final int IDUTReminder = 9;

	public WebDriver OpenBrowser(String url) { // OverRiding

		boolean IsHandlerRunningOP = false;

		if (Env.BrowserObj == null && Env.GetBrowserUsed().startsWith("OP")) {
			IsHandlerRunningOP = true;
		}

		WebDriver browser = super.OpenBrowser(url);
		if (url.toUpperCase().contains("/ECKO/")) {
			Env.AppModule = "ECKO";
		} else {
			Env.AppModule = "ECOS";
		}

		if (IsHandlerRunningOP) {
			OperaPopUpCheck();
		}

		return browser;
	}

	public void WaitForPageload() {
		// Declare the Start, End and Difference
		long startTime, endTime, diffTime;

		// Overriding to perform the Processing Image validation
		super.WaitForPageload();

		// Get the starting Time
		startTime = System.currentTimeMillis();
		int MaxTimeOut = 1; // This value should be less than the grid session
							// timeout value. Currently it is 3 minutes.
		String BrowserStatus = "";
		do {

			// check for max timeout for the busy image
			endTime = System.currentTimeMillis();
			diffTime = TimeUnit.MILLISECONDS.toMinutes(endTime - startTime);
			if (diffTime > MaxTimeOut) {
				Env.logger
						.log(Level.DEBUG,
								"Processing image was displayed for 2 minutes, so proceeding without waiting.");
				RunJavaScript("return window.stop");// Stop loading the page
				break; // Stop waiting, when the busy image is displayed for the
						// minutes specified in MaxTimeOut
			}

			try { // Check for the "Please Wait..." processing image
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By
						.xpath("//img[@id='busyImage']")));
				BrowserStatus = "complete";
			} catch (TimeoutException e) {
				Env.logger.log(Level.DEBUG,
						"Still Loading...Processing image is appearing.");
				BrowserStatus = "Still loading..";
			} catch (UnhandledAlertException e) {
				Env.logger
						.log(Level.DEBUG,
								"This code was helpful to process the UnhandledAlertExcption.");
				BrowserStatus = "Still loading..";
			} catch (Exception e) {
				Env.logger.log(Level.DEBUG, "Error occured in WaitForPageload",
						e);
				BrowserStatus = "complete";
			}
		} while (!(BrowserStatus.compareToIgnoreCase("complete") == 0));

		if (Env.GetBrowserUsed().startsWith("OP") && OperaCheck != null) {
			OperaCheck.OperaStatus("CheckStatus");
		}

	}

	public String GetText(WebElement ObjectName) {
		return super.GetText(ObjectName);
	}

	public String GetText(String ObjectName) {
		String ReturnString = TextCleanUp(super.GetText(ObjectName));
		if (ReturnString.endsWith("...")) { // Handling Ellipsis
			ReturnString = GetToolTip(ObjectName);
		}
		return ReturnString;
	}

	public String GetOnlyMyText(String ObjectName) {
		String ReturnString = TextCleanUp(super.GetOnlyMyText(ObjectName));
		if (ReturnString.endsWith("...")) { // Handling Ellipsis
			ReturnString = GetToolTip(ObjectName);
		}
		return ReturnString;
	}

	public boolean Login(String Username, String Password,
			boolean ShouldCloseFeaturesList, boolean AcceptTermsOfUse) {

		// If not in the Login page, then logout and bring the application to
		// the login page.
		if (!ObjectExists("Login_Username_EB")) {
			Logout();
		}

		// Enter Login Credentials
		Edit_SetValue("Login_Username_EB", Username);
		Edit_SetValue("Login_Password_EB", Password);
		Btn_Click("Login_Login_BN");
		WaitForPageload();

		// Store the User Logged In
		if (Username.toUpperCase().startsWith("ADMIN")
				|| Username.startsWith(TestData("AdminName"))) {
			Env.AppUser = "ADMIN";
		} else {
			Env.AppUser = "HCP";
		}

		// Store the Module Logged In to
		if (GetCurrentURL().toUpperCase().contains("/ECKO/")) {
			Env.AppModule = "ECKO";
		} else {
			Env.AppModule = "ECOS";
		}

		// Verify whether Login is successful
		if (!(ObjectExists("Login_Username_EB") && ObjectExists("Login_Password_EB"))) {
			Env.logger.log(Level.INFO, "Login Success - " + Username);
			if (Env.AppUser.startsWith("ADMIN")) {
				if (AcceptTermsOfUse) {
					CloseTermsOfUse(true);
				}
				if (ShouldCloseFeaturesList) {
					CloseFeatureList(true);
				}
				if (ObjectExists("Login_Language_DD")) {
					ChangeLanguage(Env.LanguageUsed); // Change the language in
														// the footer, for admin
				}
			}
			return true;
		} else {
			String Errormessage = GetText(ORXPath("Login_AttemptLeftErr_DT"));
			if (CompareText(Errormessage, "OBJECT NOT FOUND")) {
				Errormessage = "";
			} else {
				Errormessage = " Error:" + Errormessage;
			}
			Env.logger.log(Level.ERROR, "Login Failed for UserName: "
					+ Username + Errormessage);
			throw new RuntimeException("Login Failed for UserName: " + Username
					+ Errormessage);
		}
	}

	public boolean Login(String Username, String Password) {
		return Login(Username, Password, true, true);
	}

	public boolean EmailLogin() {
		String UserName = Env.EmailUserName;
		Rbtn_SetOption("EmailSummary_DeclarePrivate_RB", "ON");
		Edit_SetValue("EmailSummary_Username_EB", UserName);
		Edit_SetValue("EmailSummary_Password_EB", Env.EmailPassword);
		Btn_Click("EmailSummary_Login_BN");
		Wait(1);
		WaitForPageload();
		if (!(ObjectExists("EmailSummary_Username_EB") && ObjectExists("EmailSummary_Password_EB"))) {
			return true;
		} else {
			Env.logger.log(Level.ERROR, "Login Failed for UserName: "
					+ UserName);
			throw new RuntimeException("Login Failed for UserName: " + UserName);
		}

	}

	// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
	public boolean Logout() {
		try {
			GetBuildNumber();

			if (ObjectExists("Header_Logout_LK")) {
				Btn_Click("Header_Logout_LK");
				Btn_Click("SignOut_Yes_BN");
			} else if (ObjectExists("General_BackOrLogout_ST")) {
				Lnk_Click("General_BackOrLogout_ST"); // In case of exception
														// and crash, click the
														// logout link present
														// in the message
			} else {
				NavigateTo(GetLogoutURL());
			}
			if (ObjectExists("Login_Username_EB")
					&& ObjectExists("Login_Password_EB")) {
				Env.logger.log(Level.INFO, "Logout Success");
				return true;
			} else {
				Env.logger.log(Level.ERROR,
						"Logout Success - Forced through URL");
				NavigateTo(GetLogoutURL());
				return false;
			}
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, "Logout - Error occured", e);
			return false;
		}
	}

	public String GetLogoutURL() {
		String LogoutURL = null;
		if (GetCurrentURL().contains("/ecko/")) {
			LogoutURL = Env.ECKOURL.replace("/ecko/", "/ecko/logout/index");
		} else {
			LogoutURL = Env.ECOSURL.replace("/ecos/", "/ecos/logout/index");
		}
		return LogoutURL;
	}

	// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

	public boolean ChangeCountry(String CountryName) {
		boolean ReturnValue = false;
		ReturnValue = DrpDwn_SelectValue("Login_Country_DD", "Mjgz");// United
																		// Kingdom,
																		// so
																		// all
																		// the
																		// text
																		// will
																		// be in
																		// English
		if (Env.GetBrowserUsed().startsWith("IE")
				|| Env.GetBrowserUsed().startsWith("SA")) {
			WaitForPageload();
			Wait(2);
		}
		ReturnValue = DrpDwn_SelectText("Login_Country_DD", CountryName);
		WaitForPageload();
		if (Env.GetBrowserUsed().startsWith("IE")) {
			WaitForPageload();
			Wait(2);
		}
		if (ReturnValue) {
			WaitForPageload();
			if (Env.OSUsed.startsWith("MAC")) {
				Wait(1);
				WaitForPageload();
			}
		} else {
			Env.logger.log(Level.ERROR, "Unable to change Country to "
					+ CountryName);
			throw new RuntimeException("Unable to change Country to "
					+ CountryName);
		}

		return ReturnValue;
	}

	// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

	public boolean ChangeLanguage(String LanguageName) {
		boolean ReturnValue;
		String ValueToSelect = Encode(GetLanguageID(LanguageName));
		ReturnValue = DrpDwn_SelectValue("Login_Language_DD", ValueToSelect);
		if (ReturnValue) {
			WaitForPageload();
		} else {
			Env.logger.log(Level.ERROR, "Unable to change language to "
					+ LanguageName);
			if (!"PREVAL1;VAL1".contains(Env.RunEnvironment.toUpperCase()
					.trim())) { // Do not throw exception in PREVAL1 and VAL1
				throw new RuntimeException("Unable to change language to "
						+ LanguageName);
			}
		}
		return ReturnValue;
	}

	// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

	public boolean EnterSecurePharse(String AppPharse) {

		// Enter the Security phrase
		if (ObjectExists("SecurityPhrase_SecurityPhrase_EB")) {
			Edit_SetValue("SecurityPhrase_SecurityPhrase_EB", AppPharse);
			Btn_Click("SecurityPhrase_ConfirmPhrase_BN");
		}

		// Handling the Reset Phrase
		if (GetBrowserTitle().startsWith(LangData("SecurityPhrase"))) {
			Env.logger.log(Level.INFO,
					"EnterPharse Failed. Retrying with the hint.");
			Lnk_Click("SecurityPhrase_ForgotPhrase_LK");
			Edit_SetValue("SecurityPhrase_SecurityPhrase_EB",
					GetOnlyMyText("ForgotPhrase_Hint_ST").trim());
			Btn_Click("SecurityPhrase_ConfirmPhrase_BN");
		}

		// Check whether the phrase is entered correctly
		if (!GetBrowserTitle().startsWith(LangData("SecurityPhrase"))) {
			CloseTermsOfUse(true);
			CloseFeatureList(true);
			Env.logger.log(Level.INFO, "EnterPharse Success - " + AppPharse);
			if (ObjectExists("Login_Language_DD")) {
				ChangeLanguage(Env.LanguageUsed); // Change the language in the
													// footer
			}
			return true;
		} else {
			String Errormessage = GetText("SecurityPhrase_WrongPhraseErr_DT");
			if (CompareText(Errormessage, "OBJECT NOT FOUND")) {
				Errormessage = "";
			} else {
				Errormessage = " Error: " + Errormessage;
			}
			Env.logger.log(Level.ERROR, "EnterPharse Failed for pharse: "
					+ AppPharse + Errormessage);
			throw new RuntimeException("EnterPharse Failed for pharse: "
					+ AppPharse + Errormessage);
		}

	}

	public boolean SkipSecurePharse() {

		// click skip Security phrase link
		Lnk_Click("SecurityPhrase_ContinueWithoutPhrase_LK");

		if (!GetBrowserTitle().startsWith(LangData("SecurityPhrase"))) {
			Env.logger.log(Level.INFO, "SkipSecurePharse Success");
			CloseTermsOfUse(true);
			CloseFeatureList(true);
			if (ObjectExists("Login_Language_DD")) {
				ChangeLanguage(Env.LanguageUsed); // Change the language in the
													// footer
			}
			return true;
		} else {
			String Errormessage = GetText("SecurityPhrase_WrongPhraseErr_DT");
			if (CompareText(Errormessage, "OBJECT NOT FOUND")) {
				Errormessage = "";
			} else {
				Errormessage = " Error: " + Errormessage;
			}
			Env.logger.log(Level.ERROR, "SkipSecurePharse Failed. "
					+ Errormessage);
			Report.Write(
					"SkipSecurePharse",
					"User should be able proceed to homepage, without entering secure phrase",
					"SkipSecurePharse Failed. " + Errormessage, "Fail", true);
			return false;
		}

	}

	public String GetCountryCode(String CountryName, boolean ReplaceCountry) {
		CountryName = CountryName.replace(" ", "").toUpperCase();
		if (ReplaceCountry) {
			return Country.valueOf(CountryName).CountryCode()
					.replace("Country.", "");
		} else {
			return Country.valueOf(CountryName).CountryCode();
		}
	}

	public String GetLanguageCode(String LanguageName, boolean ReplaceLanguage) {
		if (LanguageName.contains(".")) {
			LanguageName = LanguageName.substring(
					LanguageName.indexOf(".") + 1, LanguageName.length()); // Convert
																			// CA.French
																			// to
																			// French
		}
		LanguageName = LanguageName.replaceAll("\n", "");
		LanguageName = LanguageName.replace(" ", "").toUpperCase();
		if (ReplaceLanguage) {
			return Language.valueOf(LanguageName).LanguageCode()
					.replace("language.", "");
		} else {
			return Language.valueOf(LanguageName).LanguageCode();
		}
	}

	public String GetCountryID(String CountryName) {
		CountryName = CountryName.replace(" ", "").toUpperCase();
		return Country.valueOf(CountryName).CountryID();
	}

	public String GetLanguageID(String LanguageName) {
		if (LanguageName.contains(".")) {
			LanguageName = LanguageName.substring(
					LanguageName.indexOf(".") + 1, LanguageName.length()); // Convert
																			// CA.French
																			// to
																			// French
		}
		LanguageName = LanguageName.replace(" ", "").toUpperCase();
		return Language.valueOf(LanguageName).LanguageID();
	}

	public String GetLanguageList(String ObjectName) {
		List<String> ListOfValues = Arrays.asList(DrpDwn_GetValue(ObjectName)
				.split("\\|"));
		StringBuffer ReturnText = new StringBuffer();
		String LanguageFound = "";
		for (String ID : ListOfValues) {
			ID = Decode(ID);
			if (Language.SearchByID(ID) != null) {
				LanguageFound = WordUtils.capitalize(Language.SearchByID(ID)
						.name().toLowerCase());
			} else {
				LanguageFound = "uage not found for " + ID; // Lang gets added
															// in
															// TranslateToLocal,
															// so keep language
															// as uage
			}
			ReturnText.append(LanguageFound + "|");
		}
		ReturnText.deleteCharAt(ReturnText.lastIndexOf("|"));
		return TranslateToLocal(ReturnText.toString(), "Lang", true);

	}

	public String GetSupportedLanguages(String CountryName) {
		String SupportedLanguages = "";
		CountryName = CountryName.replace(" ", "").toUpperCase();
		if (CountryName.compareTo("ALL") == 0) {
			SupportedLanguages = "English|German|Greek|Spanish|French|Korean|Slovak|Swedish|Chinese|Italian|Finnish|Icelandic|Czech|Norwegian|Hungarian|Turkish|Latvian|Estonian|Russian|Ukrainian|Polish|Portuguese|Malay|Arabic|Indonesian|Serbian|Thai|Filipino|Vietnamese";
		} else {
			SupportedLanguages = Country.valueOf(CountryName)
					.LanguageSupported();
		}
		return TranslateToLocal(SupportedLanguages, "Lang", true);
	}

	public String GetSupportedBaselineChapters() {
		String CountryName = Env.CountryUsed.replace(" ", "").toUpperCase();
		String SupportedBaseline = Country.valueOf(CountryName)
				.BaselineSupported();
		return TranslateToLocal(SupportedBaseline, "", false);
	}

	public String GetSupportedVisitChapters() {
		String CountryName = Env.CountryUsed.replace(" ", "").toUpperCase();
		String SupportedVisits = Country.valueOf(CountryName)
				.VisitsSuppported();
		return TranslateToLocal(SupportedVisits, "", false);
	}

	public String TranslateToLocal(String StringToTranslate, String Keyword,
			boolean ToBeSorted) {
		List<String> LocalList = Arrays.asList(StringToTranslate.split("\\|"));
		List<String> TranslatedList = new ArrayList<String>();
		for (String LocalWord : LocalList) {
			TranslatedList.add(LangData(Keyword + LocalWord));
		}
		if (ToBeSorted) {
			Collections.sort(TranslatedList);
		}
		return StringUtils.join(TranslatedList, "|");
	}

	public enum Country {
		AUSTRALIA(
				97,
				"Country.AU",
				"English",
				"DemographicData|AuxologicalDataBaseline|MedicalHistory|GrowthHormoneHistory|SocioEconomicInformation|CurrentGHTreatment|TannerStage|BoneAge|IGF1",
				"AuxologicalDataVisits|SocioEconomicInformation|CurrentGHTreatment|TannerStage|BoneAgeCaps|IGF1",
				"MS0002004112",
				"2282600271000102",
				"2282600271000166",
				"InclusionGrowthHormone|InclusionBetween2and18|InclusionGuardianConsent",
				"ExclusionTakingSaizen|ExclusionContraIndications|ExclusionInvestigationalDrug",
				true), AUSTRIA(
				98,
				"Country.AT",
				"English|German",
				"DemographicData|AuxologicalDataBaseline|MedicalHistory|GrowthHormoneHistory|SocioEconomicInformation|CurrentGHTreatment|TannerStage|BoneAgeCaps|IGF1",
				"AuxologicalDataVisits|SocioEconomicInformation|CurrentGHTreatment|TannerStage|BoneAgeCaps|IGF1",
				"MS0002004113",
				"2282600271000103",
				"2282600271000133",
				"InclusionGrowthHormone|InclusionBetween2and18|InclusionGuardianConsent",
				"ExclusionTakingSaizen|ExclusionContraIndications|ExclusionInvestigationalDrug",
				true), CANADA(
				126,
				"Country.CA",
				"English|French",
				"DemographicData|AuxologicalDataBaseline|MedicalHistory|GrowthHormoneHistory|SocioEconomicInformation|CurrentGHTreatment|TannerStage|BoneAgeCaps|IGF1",
				"AuxologicalDataVisits|SocioEconomicInformation|CurrentGHTreatment|TannerStage|BoneAgeCaps|IGF1",
				"MS0002004114",
				"2282600271000104",
				"2282600271000134",
				"InclusionGrowthHormone|InclusionBetween2and18|InclusionGuardianConsent",
				"ExclusionTakingSaizen|ExclusionContraIndications|ExclusionInvestigationalDrug",
				true), CZECHREPUBLIC(
				133,
				"Country.CZ",
				"English|Czech",
				"DemographicData|AuxologicalDataBaseline|MedicalHistory|GrowthHormoneHistory|SocioEconomicInformation|CurrentGHTreatment|TannerStage|BoneAgeCaps|IGF1",
				"AuxologicalDataVisits|SocioEconomicInformation|CurrentGHTreatment|TannerStage|BoneAgeCaps|IGF1",
				"MS0002004116",
				"2282600271000106",
				"2282600271000136",
				"InclusionGrowthHormone|InclusionBetween2and18|InclusionGuardianConsent",
				"ExclusionTakingSaizen|ExclusionContraIndications|ExclusionInvestigationalDrug",
				true), FINLAND(
				140,
				"Country.FI",
				"English|Finnish",
				"DemographicData|AuxologicalDataBaseline|MedicalHistory|GrowthHormoneHistory|SocioEconomicInformation|CurrentGHTreatment|TannerStage|BoneAgeCaps|IGF1",
				"AuxologicalDataVisits|SocioEconomicInformation|CurrentGHTreatment|TannerStage|BoneAgeCaps|IGF1",
				"MS0002004140",
				"2282600271000107",
				"2282600271000137",
				"InclusionGrowthHormone|InclusionBetween2and18|InclusionGuardianConsent",
				"ExclusionTakingSaizen|ExclusionContraIndications|ExclusionInvestigationalDrug",
				true), FRANCE(
				161,
				"Country.FR",
				"English|French",
				"DemographicData|AuxologicalDataBaseline|MedicalHistory|GrowthHormoneHistory|SocioEconomicInformation|CurrentGHTreatment|TannerStage|BoneAgeCaps|BiochemistryIGF1",
				"AuxologicalDataVisits|SocioEconomicInformation|CurrentGHTreatment|TannerStage|BoneAgeCaps|BiochemistryIGF1",
				"MS0002004118",
				"2282600271000108",
				"2282600271000138",
				"InclusionGrowthHormone|InclusionBetween2and18|InclusionGuardianConsent|InclusionTreatmentStart",
				"ExclusionTakingSaizen|ExclusionContraIndications|ExclusionInvestigationalDrug",
				true), GERMANY(
				162,
				"Country.DE",
				"English|German",
				"DemographicData|AuxologicalDataBaseline|MedicalHistory|GrowthHormoneHistory|SocioEconomicInformation",
				"AuxologicalDataVisits|CurrentGHTreatment|TannerStage|BoneAgeCaps|IGF1",
				"MS0002004119", "2282600271000109", "2282600271000139", "", "",
				false), GREECE(
				163,
				"Country.GR",
				"English|Greek",
				"DemographicData|AuxologicalDataBaseline|MedicalHistory|GrowthHormoneHistory|SocioEconomicInformation|CurrentGHTreatment|TannerStage|BoneAgeCaps|IGF1",
				"AuxologicalDataVisits|SocioEconomicInformation|CurrentGHTreatment|TannerStage|BoneAgeCaps|IGF1",
				"MS0002004120",
				"2282600271000110",
				"2282600271000140",
				"InclusionGrowthHormone|InclusionBetween2and18|InclusionGuardianConsent",
				"ExclusionTakingSaizen|ExclusionContraIndications|ExclusionInvestigationalDrug",
				true), HUNGARY(
				167,
				"Country.HU",
				"English|Hungarian",
				"DemographicData|AuxologicalDataBaseline|MedicalHistory|GrowthHormoneHistory|SocioEconomicInformation|CurrentGHTreatment|TannerStage|BoneAgeCaps|IGF1",
				"AuxologicalDataVisits|SocioEconomicInformation|CurrentGHTreatment|TannerStage|BoneAgeCaps|IGF1",
				"MS0002004121",
				"2282600271000111",
				"2282600271000141",
				"InclusionGrowthHormone|InclusionBetween2and18|InclusionGuardianConsent",
				"ExclusionTakingSaizen|ExclusionContraIndications|ExclusionInvestigationalDrug",
				true), ICELAND(
				168,
				"Country.IS",
				"English|Icelandic",
				"DemographicData|AuxologicalDataBaseline|MedicalHistory|GrowthHormoneHistory|SocioEconomicInformation|CurrentGHTreatment|TannerStage|BoneAgeCaps|IGF1",
				"AuxologicalDataVisits|SocioEconomicInformation|CurrentGHTreatment|TannerStage|BoneAgeCaps|IGF1",
				"MS0002004115",
				"2282600271000112",
				"2282600271000142",
				"InclusionGrowthHormone|InclusionBetween2and18|InclusionGuardianConsent",
				"ExclusionTakingSaizen|ExclusionContraIndications|ExclusionInvestigationalDrug",
				false), IRELAND(
				172,
				"Country.IE",
				"English",
				"",
				"",
				"MS0002004123",
				"2282600271000113",
				"2282600271000143",
				"InclusionGrowthHormone|InclusionBetween2and18|InclusionGuardianConsent",
				"ExclusionTakingSaizen|ExclusionContraIndications|ExclusionInvestigationalDrug",
				false), INDIA(169, "Country.IN", "English", "", "", "", "", "",
				"", "", false), ITALY(
				174,
				"Country.IT",
				"English|Italian",
				"DemographicData|AuxologicalDataBaseline|MedicalHistory|GrowthHormoneHistory|SocioEconomicInformation|CurrentGHTreatment|TannerStage|BoneAgeCaps|IGF1",
				"AuxologicalDataVisits|SocioEconomicInformation|CurrentGHTreatment|TannerStage|BoneAgeCaps|IGF1",
				"MS0002004124",
				"2282600271000114",
				"2282600271000144",
				"InclusionGrowthHormone|InclusionBetween2and18|InclusionGuardianConsent",
				"ExclusionTakingSaizen|ExclusionContraIndications|ExclusionInvestigationalDrug",
				true), KOREA(
				251,
				"Country.KR",
				"English|Korean",
				"DemographicData|AuxologicalDataBaseline|MedicalHistory|GrowthHormoneHistory|SocioEconomicInformation|CurrentGHTreatment|TannerStage|BoneAgeCaps|BiochemistryIGF1",
				"AuxologicalDataVisits|SocioEconomicInformation|CurrentGHTreatment|TannerStage|BoneAgeCaps|BiochemistryIGF1",
				"MS0002004125",
				"2282600271000115",
				"2282600271000145",
				"InclusionGrowthHormone|InclusionBetween2and18|InclusionGuardianConsent",
				"ExclusionTakingSaizen|ExclusionContraIndications|ExclusionInvestigationalDrug",
				true), MEXICO(
				206,
				"Country.MX",
				"English|Spanish",
				"DemographicData|AuxologicalDataBaseline|MedicalHistory|GrowthHormoneHistory|SocioEconomicInformation|CurrentGHTreatment|TannerStage|BoneAgeCaps|IGF1",
				"AuxologicalDataVisits|SocioEconomicInformation|CurrentGHTreatment|TannerStage|BoneAgeCaps|IGF1",
				"MS0002004126",
				"2282600271000116",
				"2282600271000146",
				"InclusionGrowthHormone|InclusionBetween2and18|InclusionGuardianConsent",
				"ExclusionTakingSaizen|ExclusionContraIndications|ExclusionInvestigationalDrug",
				true), NORWAY(
				212,
				"Country.NO",
				"English|Norwegian",
				"DemographicData|AuxologicalDataBaseline|MedicalHistory|GrowthHormoneHistory|SocioEconomicInformation|CurrentGHTreatment|TannerStage|BoneAgeCaps|IGF1",
				"AuxologicalDataVisits|SocioEconomicInformation|CurrentGHTreatment|TannerStage|BoneAgeCaps|IGF1",
				"MS0002004141",
				"2282600271000117",
				"2282600271000147",
				"InclusionGrowthHormone|InclusionBetween2and18|InclusionGuardianConsent",
				"ExclusionTakingSaizen|ExclusionContraIndications|ExclusionInvestigationalDrug",
				true), PORTUGAL(219, "Country.PT", "English", "", "",
				"MS0002004111", "2282600271000118", "2282600271000148", "", "",
				false), SLOVAKIA(
				248,
				"Country.SK",
				"English|Slovak",
				"DemographicData|AuxologicalDataBaseline|MedicalHistory|GrowthHormoneHistory|SocioEconomicInformation|CurrentGHTreatment|TannerStage|BoneAgeCaps|IGF1",
				"AuxologicalDataVisits|SocioEconomicInformation|CurrentGHTreatment|TannerStage|BoneAgeCaps|IGF1",
				"MS0002004129",
				"2282600271000119",
				"2282600271000149",
				"InclusionGrowthHormone|InclusionBetween2and18|InclusionGuardianConsent",
				"ExclusionTakingSaizen|ExclusionContraIndications|ExclusionInvestigationalDrug",
				true), SPAIN(
				252,
				"Country.ES",
				"English|Spanish",
				"DemographicData|AuxologicalDataBaseline|MedicalHistory|GrowthHormoneHistory|SocioEconomicInformation|CurrentGHTreatment|TannerStage|BoneAgeCaps|IGF1",
				"AuxologicalDataVisits|SocioEconomicInformation|CurrentGHTreatment|TannerStage|BoneAgeCaps|IGF1",
				"MS0002004130",
				"2282600271000120",
				"2282600271000150",
				"InclusionGrowthHormone|InclusionBetween2and18|InclusionGuardianConsent|Inclusion3Month",
				"ExclusionTakingSaizen|ExclusionContraIndications|ExclusionInvestigationalDrug|InclusionSaizen",
				true), SWEDEN(
				254,
				"Country.SE",
				"English|Swedish",
				"DemographicData|AuxologicalDataBaseline|MedicalHistory|GrowthHormoneHistory|SocioEconomicInformation|ConcomitantMedication|CurrentGHTreatment|TannerStage|BoneAgeCaps|BiochemistryIGF1",
				"AuxologicalDataVisits|SocioEconomicInformation|ConcomitantMedication|CurrentGHTreatment|TannerStage|BoneAgeCaps|BiochemistryIGF1",
				"MS0002004131",
				"2282600271000121",
				"2282600271000151",
				"InclusionGrowthHormone|InclusionBetween2and18|InclusionGuardianConsent",
				"ExclusionTakingSaizen|ExclusionContraIndications|ExclusionInvestigationalDrug",
				true), SWITZERLAND(255, "Country.CH",
				"English|French|German|Italian", "", "", "MS0002004132",
				"2282600271000122", "2282600271000152", "", "", false), UNITEDKINGDOM(
				283,
				"Country.GB",
				"English",
				"DemographicData|AuxologicalDataBaseline|PatientSupportProgram|MedicalHistory|GrowthHormoneHistory|SocioEconomicInformation|CurrentGHTreatment|TannerStage|BoneAgeCaps|IGF1",
				"AuxologicalDataVisits|SocioEconomicInformation|CurrentGHTreatment|TannerStage|BoneAgeCaps|IGF1",
				"MS0002004133",
				"2282600271000123",
				"2282600271000153",
				"InclusionGrowthHormone|InclusionBetween2and18|InclusionGuardianConsent",
				"ExclusionTakingSaizen|ExclusionContraIndications|ExclusionInvestigationalDrug",
				true), LATVIA(178, "Country.LV", "English|Latvian", "", "",
				"MS0002004142", "2282600271000124", "2282600271000154", "", "",
				false), ESTONIA(139, "Country.EE", "English|Estonian", "", "",
				"MS0002004143", "2282600271000125", "2282600271000155", "", "",
				false), CHILE(127, "Country.CL", "English|Spanish", "", "",
				"MS0002004136", "2282600271000126", "2282600271000156", "", "",
				false), BRAZIL(124, "Country.BR", "English|Portuguese", "", "",
				"MS0002004137", "2282600271000127", "2282600271000157", "", "",
				false), ECUADOR(136, "Country.EC", "English|Spanish", "", "",
				"MS0002004138", "2282600271000128", "2282600271000158", "", "",
				false), SINGAPORE(
				247,
				"Country.SG",
				"English|Chinese|Malay",
				"DemographicData|AuxologicalDataBaseline|MedicalHistory|GrowthHormoneHistory|SocioEconomicInformation|CurrentGHTreatment|TannerStage|BoneAgeCaps|IGF1",
				"AuxologicalDataVisits|SocioEconomicInformation|CurrentGHTreatment|TannerStage|BoneAgeCaps|IGF1",
				"MS0002004139", "2282600271000129", "2282600271000159", "", "",
				true), HONGKONG(166, "Country.HK", "English|Chinese", "", "",
				"MS0002004134", "2282600271000130", "2282600271000160", "", "",
				false), MALAYSIA(204, "Country.MY", "English|Malay|Chinese",
				"", "", "MS0002004135", "2282600271000131", "2282600271000161",
				"", "", false), SAUDIARABIA(
				244,
				"Country.SA",
				"English|Arabic",
				"DemographicData|AuxologicalDataBaseline|MedicalHistory|GrowthHormoneHistory|SocioEconomicInformation|CurrentGHTreatment|TannerStage|BoneAgeCaps|IGF1",
				"AuxologicalDataVisits|SocioEconomicInformation|CurrentGHTreatment|TannerStage|BoneAgeCaps|IGF1",
				"MS0002004117", "2282600271000105", "2282600271000162", "", "",
				true), UNITEDARABEMIRATES(
				282,
				"Country.AE",
				"English|Arabic",
				"DemographicData|AuxologicalDataBaseline|MedicalHistory|GrowthHormoneHistory|SocioEconomicInformation|CurrentGHTreatment|TannerStage|BoneAgeCaps|IGF1",
				"AuxologicalDataVisits|SocioEconomicInformation|CurrentGHTreatment|TannerStage|BoneAgeCaps|IGF1",
				"MS0002004122", "2282600271000132", "2282600271000163", "", "",
				true), VENEZUELA(286, "Country.VE", "English|Spanish", "", "",
				"MS0002004128", "2282600271000101", "2282600271000164", "", "",
				false), GUATEMALA(164, "Country.GT", "English|Spanish", "", "",
				"MS0002004127", "2282600271000135", "2282600271000165", "", "",
				false), ARGENTINA(
				765183,
				"Country.AR",
				"English|Spanish",
				"DemographicData|AuxologicalDataBaseline|MedicalHistory|GrowthHormoneHistory|SocioEconomicInformation|CurrentGHTreatment|TannerStage|BoneAgeCaps|IGF1",
				"AuxologicalDataVisits|SocioEconomicInformation|CurrentGHTreatment|TannerStage|BoneAgeCaps|IGF1",
				"MS0002004144", "2282600271000167", "2282600271000168", "", "",
				true), COLOMBIA(
				765184,
				"Country.CO",
				"English|Spanish",
				"DemographicData|AuxologicalDataBaseline|MedicalHistory|GrowthHormoneHistory|SocioEconomicInformation|CurrentGHTreatment|TannerStage|BoneAgeCaps|IGF1",
				"AuxologicalDataVisits|SocioEconomicInformation|CurrentGHTreatment|TannerStage|BoneAgeCaps|IGF1",
				"MS0002004145", "2282600271000169", "2282600271000170", "", "",
				true), INDONESIA(
				765185,
				"Country.ID",
				"English|Indonesian",
				"DemographicData|AuxologicalDataBaseline|MedicalHistory|GrowthHormoneHistory|SocioEconomicInformation|CurrentGHTreatment|TannerStage|BoneAgeCaps|IGF1",
				"AuxologicalDataVisits|SocioEconomicInformation|CurrentGHTreatment|TannerStage|BoneAgeCaps|IGF1",
				"MS0002004146", "2282600271000171", "2282600271000172", "", "",
				true), TAIWAN(
				765185,
				"Country.TW",
				"English|Chinese",
				"DemographicData|AuxologicalDataBaseline|MedicalHistory|GrowthHormoneHistory|SocioEconomicInformation|CurrentGHTreatment|TannerStage|BoneAgeCaps|IGF1",
				"AuxologicalDataVisits|SocioEconomicInformation|CurrentGHTreatment|TannerStage|BoneAgeCaps|IGF1",
				"MS0002004147", "2282600271000173", "2282600271000174", "", "",
				true), PERU(765186, "Country.PE", "English|Spanish", "", "",
				"MS0002004148", "2282600271000175", "2282600271000176", "", "",
				false), SERBIA(883101, "Country.RS", "English|Serbian", "", "",
				"MS0002004149", "2282600271000177", "2282600271000178", "", "",
				false), MONTENEGRO(765188, "Country.ME", "English|Serbian", "",
				"", "MS0002004150", "2282600271000179", "2282600271000180", "",
				"", false), THAILAND(765190, "Country.TH", "English|Thai", "",
				"", "MS0002004151", "2282600271000181", "2282600271000182", "",
				"", false), ELSALVADOR(883121, "Country.SV", "English|Spanish",
				"", "", "MS0002004152", "2282600271000183", "2282600271000184",
				"", "", false), HONDURAS(883120, "Country.HN",
				"English|Spanish", "", "", "MS0002004153", "2282600271000185",
				"2282600271000186", "", "", false), NICARAGUA(883119,
				"Country.NI", "English|Spanish", "", "", "MS0002004154",
				"2282600271000187", "2282600271000188", "", "", false), TRINIDADANDTOBAGO(
				883122, "Country.TT", "English|Spanish", "", "",
				"MS0002004155", "2282600271000189", "2282600271000190", "", "",
				false), DOMINICANREPUBLIC(883116, "Country.DO",
				"English|Spanish", "", "", "MS0002004156", "2282600271000191",
				"2282600271000192", "", "", false), PHILIPPINES(883115,
				"Country.PH", "English|Filipino", "", "", "MS0002004157",
				"2282600271000193", "2282600271000194", "", "", false), VIETNAM(
				883114, "Country.VN", "English|Vietnamese", "", "",
				"MS0002004158", "2282600271000195", "2282600271000196", "", "",
				false);

		// Constructor
		Country(int ID, String Code, String LanguageSupported,
				String BaselineSupported, String VisitsSuppported,
				String EP1Device, String EP2Device, String NewDevice,
				String InclusionText, String ExclusionText,
				boolean SupportForECOS) {
			this.CountryID = String.valueOf(ID);
			this.CountryCode = Code;
			this.LanguageSupported = LanguageSupported;
			this.BaselineSupported = BaselineSupported;
			this.VisitsSuppported = VisitsSuppported;
			this.EP1Device = EP1Device;
			this.EP2Device = EP2Device;
			this.NewDevice = NewDevice;
			this.InclusionText = InclusionText;
			this.ExclusionText = ExclusionText;
			this.SupportForECOS = SupportForECOS;
		}

		private final String CountryID;
		private final String CountryCode;
		private final String LanguageSupported;
		private final String BaselineSupported;
		private final String VisitsSuppported;
		private final String EP1Device;
		private final String EP2Device;
		private final String NewDevice;
		private final String InclusionText;
		private final String ExclusionText;
		private final boolean SupportForECOS;

		public String CountryID() {
			return CountryID;
		}

		public String CountryCode() {
			return CountryCode;
		}

		public String LanguageSupported() {
			return LanguageSupported;
		}

		public String BaselineSupported() {
			return BaselineSupported;
		}

		public String VisitsSuppported() {
			return VisitsSuppported;
		}

		public String GetEP1Device() {
			return EP1Device;
		}

		public String GetEP2Device() {
			return EP2Device;
		}

		public String GetNewDevice() {
			return NewDevice;
		}

		public String GetInclusionText() {
			if (InclusionText.trim().isEmpty()) { // Default Values from UK
				return "InclusionGrowthHormone|InclusionBetween2and18|InclusionGuardianConsent|InclusionTreatmentStart";
			} else {
				return InclusionText;
			}
		}

		public String GetExclusionText() {
			if (ExclusionText.trim().isEmpty()) {
				return "ExclusionTakingSaizen|ExclusionContraIndications|ExclusionInvestigationalDrug";
			} else {
				return ExclusionText;
			}
		}

		public boolean GetSupportForECOS() {
			return SupportForECOS;
		}

	}

	public enum Language {
		CZECH(3761585, "language.cs"), ENGLISH(172179, "language.en"), FINNISH(
				1594648, "language.fi"), FRENCH(175542, "language.fr"), GERMAN(
				172180, "language.de"), GREEK(1173495, "language.el"), HUNGARIAN(
				3761588, "language.hu"), ICELANDIC(2605805, "language.is"), ITALIAN(
				2068437, "language.it"), LATVIAN(3761586, "language.lv"), KOREAN(
				175543, "language.ko"), NORWEGIAN(3761587, "language.no"), CHINESE(
				2068438, "language.zh"), SLOVAK(7, "language.sk"), SPANISH(
				175541, "language.es"), SWEDISH(175544, "language.sv"), TURKISH(
				3761584, "language.tr"), ESTONIAN(17, "language.et"), POLISH(
				21, "language.pl"), UKRAINIAN(19, "language.uk"), RUSSIAN(18,
				"language.ru"), PORTUGUESE(22, "language.pt"), SLOVENIAN(0,
				"language.sl"), DANISH(0, "language.da"), DUTCH(0,
				"language.nl"), LITHUANIAN(0, "language.lt"), MALAY(410185401,
				"language.ms"), ARABIC(410185402, "language.ar"), INDONESIAN(
				410695718, "language.id"), SERBIAN(410695719, "language.sr"), THAI(
				410695720, "language.th"), FILIPINO(410695721, "language.tl"), VIETNAMESE(
				410695722, "language.vi");

		private String LanguageID;
		private String LanguageCode;

		// Constructor
		Language(int ID, String Code) {
			this.LanguageID = String.valueOf(ID);
			this.LanguageCode = Code;

		}

		public String LanguageID() {
			return LanguageID;
		}

		public String LanguageCode() {
			return LanguageCode;
		}

		public static Language SearchByID(String SearchString) {
			for (Language p : Language.values()) {
				if (p.LanguageID().compareTo(SearchString) == 0) {
					return p;
				}
			}
			return null;
		}

	}

	public boolean SelectDate(String ObjectName, String DateToSet) {
		boolean ReturnValue = false;
		int MyYear = 0;
		int MyMonth = 0;
		int MyDay = 0;
		int UpperBound = 0;
		int LowerBound = 0;
		boolean IsBounded = true;

		try {
			// Parse the Date
			DateToSet = DateToSet.trim().replaceAll("-", " ");
			Date MyDate = (new SimpleDateFormat("dd MMM yyyy"))
					.parse(DateToSet);
			Calendar cal = Calendar.getInstance();
			cal.setTime(MyDate);
			MyYear = cal.get(Calendar.YEAR);
			MyMonth = cal.get(Calendar.MONTH);
			MyDay = cal.get(Calendar.DAY_OF_MONTH);
			String CalendarXpath = "//div[@id='ui-datepicker-div']";
			String MonthXpath = "//div[@id='ui-datepicker-div']//select[@class='ui-datepicker-month']";
			String YearXpath = "//div[@id='ui-datepicker-div']//select[@class='ui-datepicker-year']";
			String DayXpath = "//div[@id='ui-datepicker-div']//a[text()='"
					+ MyDay + "']";
			Select DropDownObject = null;

			// Click on the Calendar Icon
			Img_Click(ObjectName);
			Wait(1);
			// If calendar is displayed, then select the date
			WebElement Calendar = GetObject(CalendarXpath);
			if (Calendar.isDisplayed()) {
				do { // Loop until the requied date, is displayed in the Year
						// Drop Down
					DropDownObject = new Select(GetObject(YearXpath));
					try {
						UpperBound = Integer.parseInt(GetText(YearXpath
								+ "/option["
								+ (DropDownObject.getOptions().size()) + "]"));
					} catch (NumberFormatException e) {
						// If the exception, occurs then the last value is Next,
						// so take one value less than Next
						UpperBound = Integer.parseInt(GetText(YearXpath
								+ "/option["
								+ (DropDownObject.getOptions().size() - 1)
								+ "]"));
					}
					LowerBound = Integer.parseInt(GetText(YearXpath
							+ "/option[2]")); // Since 1 is Prev
					if (!(LowerBound <= MyYear)) {
						DropDownObject.selectByIndex(0); // DrpDwn_SelectIndex(YearXpath,
															// 0); //Previous
						IsBounded = true;
					} else if (!(MyYear <= UpperBound)) {
						DropDownObject.selectByVisibleText(String
								.valueOf(UpperBound)); // DrpDwn_SelectText(YearXpath,
														// String.valueOf(UpperBound));
						IsBounded = true;
					} else {
						IsBounded = false;
					}
				} while (IsBounded);
				DrpDwn_SelectValue(YearXpath, String.valueOf(MyYear)); // Select
																		// the
																		// Year
				DrpDwn_SelectValue(MonthXpath, String.valueOf(MyMonth)); // Select
																			// the
																			// Month
				Btn_ClickAndProceed(DayXpath); // Select the Day
				ReturnValue = true;
			} else {
				Env.logger.log(Level.ERROR, "Calendar is not displayed");
				ReturnValue = false;
			}
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, ObjectName, e);
			HideCursor();
		}
		return ReturnValue;
	}

	public boolean VerifyPaginationText(String ObjectName, String ExpectedString) {
		TakeStaticScreenshot(ObjectName);
		boolean IsPass = false;

		// Pagination dropdown verification
		if (ExpectedString.trim().length() != 0) {
			GetObject(ORXPath(ObjectName) + "//input[@id='pageLenText']")
					.click();
			IsPass = CompareText("DropDownValue_ST", LangData("All"),
					GetText("Pagination_DropDownText_ST"));
			ExpectedString = " " + ExpectedString + " " + LangData("PerPage")
					+ ":";
		}

		String Prefix = LangData("PreviousPage") + " " + LangData("Page") + " "
				+ LangData("Of") + " ";
		String Suffix = " " + LangData("NextPage") + ExpectedString;
		String actual = GetText(ObjectName);

		if (actual.startsWith(Prefix) && actual.endsWith(Suffix)) {
			Report.Write(ObjectName, Prefix + "xx" + Suffix, actual, "Pass",
					true);
			IsPass = true;
		} else {
			Report.Write(ObjectName, Prefix + "xx" + Suffix, actual, "Fail",
					true);
		}

		ClearStaticScreenshot();

		return IsPass;
	}

	public boolean VerifyRecordInfoText(String ObjectName) {

		String OfName = LangData("Of");
		String RecordName = LangData("Records");
		String actual = GetText(ObjectName);
		boolean Text1, Text2 = false;
		try {
			Text1 = actual.contains(OfName);
			Text2 = actual.contains(RecordName + ")");
			if (!Text2) {
				Text2 = actual.contains(LangData("Record") + ")");
				if (Text2) {
					RecordName = LangData("Record");
				}
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			Text1 = false;
			Text2 = false;
		}

		if (Text1 && Text2) {
			Report.Write(ObjectName, "(X-Y " + OfName + " Z " + RecordName
					+ ")", actual, "Pass", true);
			return false;
		} else {
			Report.Write(ObjectName, "(X-Y " + OfName + " Z " + RecordName
					+ ")", actual, "Fail", true);
			return false;
		}

	}

	public boolean VerifyHelpText(String HelpIconObjectName,
			String ExpectedHelpTitle, String ExpectedHelpText) {
		boolean IsPass = true;
		WebElement ActionObject;
		ActionObject = GetObject(HelpIconObjectName);
		String ScreenshotName = "";
		String StepID = HelpIconObjectName.substring(0,
				HelpIconObjectName.lastIndexOf("_"));
		String StepHelpTitle = StepID + "Title_DT";
		String StepHelpText = StepID + "Text_DT";
		ExpectedHelpTitle = LangData("Help") + "-" + ExpectedHelpTitle;
		ExpectedHelpText = ExpectedHelpText + " " + LangData("HelpTextFooter");
		try {
			if (!ActionObject.getAttribute("title").isEmpty()) {
				CompareText("HelpImageToolTip", LangData("Help"),
						GetToolTip(HelpIconObjectName));
			}
			ActionObject.click();
			Wait(1);
			WebElement VisilbleHelpBox = GetVisibleElement("//div[@class='toolTip' or @class='toolTip rightPos']");
			if (VisilbleHelpBox == null) {// if null for the first time, try
											// again after wait
				Wait(Env.ResponseTimeOut);// Wait for the Help PopUp to appear
				VisilbleHelpBox = GetVisibleElement("//div[@class='toolTip' or @class='toolTip rightPos']");
				if (VisilbleHelpBox == null
						&& Env.GetBrowserUsed().startsWith("OP")) {
					UserAction.moveToElement(ActionObject).build().perform();
					UserAction.click(ActionObject).build().perform();
					RunJavaScript("document.getElementById('"
							+ ActionObject.getAttribute("id") + "').click();");
					VisilbleHelpBox = GetVisibleElement("//div[@class='toolTip' or @class='toolTip rightPos']");
					Wait(1);
				}
			}
			ScreenshotName = UtilClass.GetScreenshot(HelpIconObjectName);

			if (VisilbleHelpBox != null) {
				String ActualHelpTitle = TextCleanUp(TextCleanUp(GetText(VisilbleHelpBox
						.findElement(By.xpath("div[@class='toolTipHeader']"))))); // Double
																					// TextCleanup
																					// is
																					// added
																					// since,
																					// few
																					// korean
																					// strings
																					// are
																					// ending
																					// with
																					// ::
				ActualHelpTitle = ActualHelpTitle.replaceAll(" - ", "-");
				ActualHelpTitle = ActualHelpTitle.replaceAll(" \u2013 ", "-");
				String ActualHelpText = TextCleanUp(TextCleanUp(GetText(VisilbleHelpBox
						.findElement(By.xpath("div[@class='tootlTipCont']")))));
				if (CompareText(ExpectedHelpTitle, ActualHelpTitle)) {
					Report.Write(StepHelpTitle, ExpectedHelpTitle,
							ActualHelpTitle, "Pass", ScreenshotName);
				} else {
					Report.Write(StepHelpTitle, ExpectedHelpTitle,
							ActualHelpTitle, "Fail", ScreenshotName);
				}
				if (CompareText(ExpectedHelpText, ActualHelpText)) {
					Report.Write(StepHelpText, ExpectedHelpText,
							ActualHelpText, "Pass", ScreenshotName);
				} else {
					Report.Write(StepHelpText, ExpectedHelpText,
							ActualHelpText, "Fail", ScreenshotName);
				}
			} else {
				Report.Write(StepHelpTitle, ExpectedHelpTitle,
						"OBJECT NOT FOUND", "Fail", ScreenshotName);
				Report.Write(StepHelpText, ExpectedHelpText,
						"OBJECT NOT FOUND", "Fail", ScreenshotName);
				IsPass = false;
			}
			HideCursor();
		} catch (Exception e) {
			ScreenshotName = UtilClass.GetScreenshot(HelpIconObjectName);
			Env.logger.log(Level.ERROR, HelpIconObjectName, e);
			Report.Write(StepHelpTitle, ExpectedHelpTitle, "ERROR", "Fail",
					ScreenshotName);
			Report.Write(StepHelpText, ExpectedHelpText, "ERROR", "Fail",
					ScreenshotName);
			IsPass = false;
		}

		return IsPass;

	}

	public String Date_AddDays(String InputDateString, int NoOfDays,
			int IntervalType) {
		// Example: Date_AddDays("01-JAN-2012",2,Calendar.DATE)
		try {
			InputDateString = InputDateString.trim().replaceAll("-", " ");
			SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
			Date InputDate = (Date) formatter.parse(InputDateString);
			Calendar cal = Calendar.getInstance();
			cal.setTime(InputDate);
			cal.add(IntervalType, NoOfDays);
			return formatter.format(cal.getTime());
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, "Date_AddDays:" + InputDateString, e);
			return null;
		}

	}

	public void HideCursor() {
		WebElement VisibleElement = null;
		try {
			VisibleElement = GetObject(ORXPath("General_HideCursor_ST")); // old
																			// -
																			// //p[@class='address']
			VisibleElement.click();
		} catch (Exception e) {
			try {
				VisibleElement = GetVisibleElement(ORXPath("General_HideCursorEditPanel_ST"));
				VisibleElement.click();
			} catch (Exception ignore) {
			}
		}
	}

	public String GetRangeValue(String InputString) {

		InputString = InputString.toUpperCase().trim();
		if (CompareText(InputString, "CURRENT MONTH")) {
			return "1";
		} else if (CompareText(InputString, "LAST 3 MONTHS")) {
			return "3";
		} else if (CompareText(InputString, "LAST 6 MONTHS")) {
			return "6";
		} else if (CompareText(InputString, "LAST 12 MONTHS")) {
			return "12";
		} else if (CompareText(InputString, "FULL RANGE")) {
			return "-1";
		} else if (CompareText(InputString, "CUSTOM DATE RANGE")
				|| CompareText(InputString, "CUSTOM MONTH RANGE")) {
			return "-2";
		} else if (CompareText(InputString, "LAST VISIT")) {
			return "-3";
		}
		return "";
	}

	public boolean SelectMonth(String ObjectName, String DateToSet) {
		boolean ReturnValue = false;
		int MyYear = 0;
		int MyMonth = 0;
		int UpperBound = 0;
		int LowerBound = 0;
		boolean IsBounded = true;

		try {
			// Parse the Date
			DateToSet = DateToSet.trim().replaceAll("-", " ");
			Date MyDate = (new SimpleDateFormat("dd MMM yyyy"))
					.parse(DateToSet);
			Calendar cal = Calendar.getInstance();
			cal.setTime(MyDate);
			MyYear = cal.get(Calendar.YEAR);
			MyMonth = cal.get(Calendar.MONTH);

			// Click on the Calendar Icon
			Img_Click(ObjectName);
			Wait(1);
			String CalendarXpath = "//div[@id='ui-datepicker-div']";
			String MonthXpath = "//div[@id='ui-datepicker-div']//select[@class='ui-datepicker-month']";
			String YearXpath = "//div[@id='ui-datepicker-div']//select[@class='ui-datepicker-year']";

			Select DropDownObject;
			// If calendar is displayed, then select the date
			WebElement Calendar = GetObject(CalendarXpath);
			if (Calendar.isDisplayed()) {
				do { // Loop until the requied date, is displayed in the Year
						// Drop Down
					DropDownObject = new Select(GetObject(YearXpath));
					try {
						UpperBound = Integer.parseInt(GetText(YearXpath
								+ "/option["
								+ (DropDownObject.getOptions().size()) + "]"));
					} catch (NumberFormatException e) {
						// If the exception, occurs then the last value is Next,
						// so take one value less than Next
						UpperBound = Integer.parseInt(GetText(YearXpath
								+ "/option["
								+ (DropDownObject.getOptions().size() - 1)
								+ "]"));
					}
					LowerBound = Integer.parseInt(GetText(YearXpath
							+ "/option[2]")); // Since 1 is Prev
					if (!(LowerBound <= MyYear)) {
						// DrpDwn_SelectText(YearXpath,
						// String.valueOf(LowerBound));
						DrpDwn_SelectIndex(YearXpath, 0); // Previous
						IsBounded = true;
					} else if (!(MyYear <= UpperBound)) {
						DrpDwn_SelectText(YearXpath, String.valueOf(UpperBound));
						IsBounded = true;
					} else {
						IsBounded = false;
					}
				} while (IsBounded);
				DrpDwn_SelectValue(YearXpath, String.valueOf(MyYear)); // Select
																		// the
																		// Year
				DrpDwn_SelectValue(MonthXpath, String.valueOf(MyMonth)); // Select
																			// the
																			// Month
				Btn_ClickAndProceed("AdherenceCalendar_Done_BN");
				ReturnValue = true;
			} else {
				Env.logger.log(Level.ERROR, "Calendar is not displayed");
				ReturnValue = false;
			}
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, ObjectName, e);
		}
		return ReturnValue;
	}

	public boolean SearchForCountry(String CountryName) {
		if (Env.AppModule.startsWith("ECKO")) {
			return ECKOSearchForCountry(CountryName);
		} else {
			return ECOSSearchForCountry(CountryName);
		}
	}

	public boolean ECKOSearchForCountry(String CountryName) {
		Edit_SetValue("MyPatients_SearchText_EB", CountryName);
		Btn_Click("MyPatients_Search_BN");
		if (GetText(ORXPath("DeviceSettings_PatientName_ST")).toUpperCase()
				.contains(CountryName.toUpperCase())) {
			return true;
		} else {
			throw new RuntimeException("Country not found: " + CountryName);
		}

	}

	public boolean ECOSSearchForCountry(String CountryName) {
		Edit_SetValue("MyPatients_SearchText_EB", CountryName);
		Btn_Click("MyPatients_Search_BN");
		if (GetText(ORXPath("DeviceSettings_PatientName_ST")).toUpperCase()
				.contains(CountryName.toUpperCase())) {
			return true;
		} else {
			throw new RuntimeException("Country not found: " + CountryName);
		}

	}

	public boolean SearchForPatient(String PatientName) {

		if (!PatientName.startsWith("jsn")) {
			PatientName = FormatPatientName(PatientName);
		}
		if (Env.AppModule.startsWith("ECKO")) {
			;
			return ECKOSearchForPatient(PatientName);
		} else {
			return ECOSSearchForPatient(PatientName);
		}
	}

	public boolean ECKOSearchForPatient(String PatientName) {
		Edit_SetValue("MyPatients_SearchText_EB", PatientName);
		Btn_Click("MyPatients_Search_BN");
		if (Env.GetBrowserUsed().startsWith("IE")) {
			if (ObjectExists("MyPatients_Search_BN")) {
				Btn_Click("MyPatients_Search_BN");
			}
		}
		if (GetText(ORXPath("DeviceSettings_PatientName_ST")).toUpperCase()
				.contains(PatientName.toUpperCase())) {
			if (ObjectExists("General_ExpandArrow_IM")) {
				Btn_Click("General_ExpandArrow_IM");
			}
			return true;
		} else {
			throw new RuntimeException("Patient not found: " + PatientName);
		}

	}

	public List<WebElement> EmailSearch(String subject, int ExpectedEmailCount) {
		Wait(2);
		String xpath = "//div[@id='divSubject' and starts-with(text(), '"
				+ subject + "')]";
		List<WebElement> Subjects = GetAllElements(xpath);
		int ActualEmailCount = Subjects.size();
		if (!Env.LanguageUsed.toUpperCase().startsWith("ITALIAN")
				|| !Env.CurrentTestName.contains("Reminder")) {
			CompareText("EmailCount", String.valueOf(ExpectedEmailCount),
					String.valueOf(ActualEmailCount));
		}
		return Subjects;
	}

	public void OpenEmail(String subject, int Index) {
		String xpath = "(//div[@id='divSubject' and starts-with(text(), '"
				+ subject + "')])[" + Index + "]";
		WebElement EmailSubject = GetObject(xpath);
		if (EmailSubject != null) {
			if (Env.GetBrowserUsed().startsWith("IE")) {
				MoveMouseTo(EmailSubject);
			}
			RunJavaScript(EmailSubject, "arguments[0].scrollIntoView(true);");
			Btn_ClickAndProceed(xpath);
			EmailSubject.sendKeys(Keys.ENTER);
			SwitchWindows(2);
		} else {
			throw new RuntimeException("Unable to open the email");
		}
	}

	public boolean ECOSSearchForPatient(String PatientName) {
		if (Env.AppUser.startsWith("HCP")) {
			Edit_SetValue("#MyPatients_SearchPatient_EB", PatientName);
			Btn_Click("#MyPatients_SearchPatient_BN");
			if (Env.GetBrowserUsed().startsWith("IE")) {
				if (!GetText(ORXPath("#General_SearchedPatient_DT"))
						.toUpperCase().contains(PatientName.toUpperCase())) {
					Edit_SetValue("#MyPatients_SearchPatient_EB", PatientName);
					Btn_Click("#MyPatients_SearchPatient_BN");
				}
			}
		} else if (Env.AppUser.startsWith("ADMIN")) {
			Edit_SetValue("#LocalReportAdmin_PatientID_EB", PatientName);
			Btn_Click("#LocalReportAdmin_SearchPatientID_BN");
			if (Env.GetBrowserUsed().startsWith("IE")) {
				if (!GetText(ORXPath("#General_SearchedPatient_DT"))
						.toUpperCase().contains(PatientName.toUpperCase())) {
					Edit_SetValue("#LocalReportAdmin_PatientID_EB", PatientName);
					Btn_Click("#LocalReportAdmin_SearchPatientID_BN");
				}
			}

		}

		if (GetText(ORXPath("#General_SearchedPatient_DT")).toUpperCase()
				.contains(PatientName.toUpperCase())) {
			return true;
		} else {
			throw new RuntimeException("Patient not found: " + PatientName);
		}

	}

	public void VerifyBrowserTitle(String PageName) {
		WaitForPageload();
		final String ActualTitle = TextCleanUp(GetBrowserTitle()
				.replaceAll(" - ", "-").replaceAll("- ", "-")
				.replaceAll(" -", "-").trim());
		String ExpectedTitle = "";
		if (Env.AppModule.startsWith("ECKO")) {
			;
			ExpectedTitle = (PageName + "-" + LangData("ApplicationName"))
					.replaceAll(" - ", "-").replaceAll("- ", "-")
					.replaceAll(" -", "-");
		} else {
			ExpectedTitle = (PageName + "-" + LangData("StudiesECOSCAPS"))
					.replaceAll(" - ", "-").replaceAll("- ", "-")
					.replaceAll(" -", "-");
		}
		if (CompareText(ExpectedTitle, ActualTitle)) {
			Report.Write("BrowserTitle_ST", ExpectedTitle, ActualTitle, "Pass",
					true);
		} else {
			Report.Write("BrowserTitle_ST", ExpectedTitle, ActualTitle, "FAIL",
					true);
		}
	}

	public void SanityBrowserTitle(String PageName, String SanityScreenshotName) {
		WaitForPageload();
		final String ActualTitle = TextCleanUp(GetBrowserTitle()
				.replaceAll(" - ", "-").replaceAll("- ", "-")
				.replaceAll(" -", "-").trim());
		String ExpectedTitle = "";
		if (Env.AppModule.startsWith("ECKO")) {
			;
			ExpectedTitle = (PageName + "-" + LangData("ApplicationName"))
					.replaceAll(" - ", "-").replaceAll("- ", "-")
					.replaceAll(" -", "-");
		} else {
			ExpectedTitle = (PageName + "-" + LangData("StudiesECOSCAPS"))
					.replaceAll(" - ", "-").replaceAll("- ", "-")
					.replaceAll(" -", "-");
		}

		if (CompareText(ExpectedTitle, ActualTitle)) {
			Report.Write("BrowserTitle_ST", ExpectedTitle, ActualTitle, "Pass",
					UtilClass.GetScreenshot(SanityScreenshotName));
		} else {
			Report.Write("BrowserTitle_ST", ExpectedTitle, ActualTitle, "FAIL",
					UtilClass.GetScreenshot(SanityScreenshotName));
		}
	}

	public void CheckCountryCode(String ObjectName, String Expected,
			boolean SkipFirstValue) {
		String Actual = DrpDwn_GetText(ObjectName);
		Actual = Actual.replaceAll("\\n", ""); // Replace newline character
		Actual = Actual.replaceAll(" {2,}", " "); // Replace 2 or more spaces
													// with one space
		List<String> ActualList = Arrays.asList(Actual.split("\\|"));
		int Start = 0;
		for (int i = Start; i < ActualList.size(); i++) {
			ActualList.set(i,
					(ActualList.get(i).replaceAll(" \\(\\+.*", "")).trim());
		}
		List<String> ExpectedList = Arrays.asList(Expected.split("\\|"));
		Collections.sort(ExpectedList);
		Collections.sort(ActualList);
		CompareList(ObjectName, ExpectedList, ActualList,
				UtilClass.GetScreenshot(ObjectName));
	}

	public boolean CheckAdherencePopup(String ObjectName, String EventDate,
			String RowToCheck) {
		String EventName = ObjectName.replace("AdherenceCalendar_", "")
				.replace("_LK", "");
		try {
			if (EventDate != "31-FEB-2000") {
				Lnk_Click(ObjectName);
				Env.StaticScreenshotName = null;
				Env.StaticScreenshotName = GetScreenshot(EventName);
				VerifyTextContained("AdherenceCalendar_LegendPopupTitleDate_ST");
				CompareWithList("AdherenceCalendar_LegendPopupTitleDate_ST",
						MonthLongAll(),
						GetText("AdherenceCalendar_LegendPopupTitleDate_ST"));
				VerifyTextContained("AdherenceCalendar_LegendPopupTitleRegime_ST");

				VerifyTableHeader("AdherenceCalendar_LegendTableHeader_ST");
				VerifyText("AdherenceCalendar_LegendClose_BN");
				VerifyTextContained("AdherenceCalendar_PrescribedDose_ST");
				String ExpectedEvent = "";
				ObjectName = ObjectName.toUpperCase();

				if (ObjectName.endsWith("PARTIALLEGEND_LK")) {
					ExpectedEvent = LangData("PartialInjection");
				} else if (ObjectName.endsWith("COMPLETELEGEND_LK")) {
					ExpectedEvent = LangData("CompleteInjection");
				} else if (ObjectName.endsWith("NOINJECTIONLEGEND_LK")) {
					ExpectedEvent = LangData("NoInjection");
				} else if (ObjectName.endsWith("NEWDOSAGESETLEGEND_LK")) {
					ExpectedEvent = LangData("NewDosageSet");
				} else if (ObjectName.endsWith("NEWCARTRIDGELEGEND_LK")) {
					ExpectedEvent = LangData("NewCartridge");
				}

				WebElement ActionObject = GetObject("AdherenceCalendar_Popup_TB");
				WaitForPageload();
				String sActualValue = GetText(ActionObject.findElement(By
						.xpath("tbody/tr[" + RowToCheck + "]/td[2]")));
				CompareText("AdherenceCalendar_Popup_TB", ExpectedEvent,
						sActualValue);

				// WbTbl_VerifyTableColumn("AdherenceCalendar_Popup_TB","Event",
				// 2, 1, ExpectedEvent, false);
				Btn_Click("AdherenceCalendar_LegendClose_BN");
				return true;
			} else {
				Env.logger.log(Level.ERROR, EventName + " event not present");
				return false;
			}
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, ObjectName, e);
			return false;
		} finally {
			Env.StaticScreenshotName = null;
			EventName = null;
		}

	}

	public boolean CheckAdherenceCalendarHeader(String ObjectName) // VerifyAdherenceCalendarHeader
	{
		boolean IsPass = true;
		try {
			String ActualText = "";
			String ScreenShotName = UtilClass.GetScreenshot(ObjectName);
			String ExpectedStrings = GetWeekdaysOrder(DayMaxAll()) + "|"
					+ LangData("Adherence");
			List<String> Expected = Arrays.asList(ExpectedStrings.split("\\|"));
			List<WebElement> CalendarFields = browser.findElements(By
					.xpath(ORXPath(ObjectName)));
			for (WebElement CalendarField : CalendarFields) {
				ActualText = ActualText + GetText(CalendarField) + "|";
			}
			String[] ActualValue = ActualText.split("\\|");

			for (int i = 0; i < Expected.size(); i++) {
				if (CompareText(Expected.get(i), ActualValue[i])) {
					Report.Write(ObjectName, Expected.get(i), ActualValue[i],
							"Pass", ScreenShotName);
				} else {
					IsPass = false;
					Report.Write(ObjectName, Expected.get(i), ActualValue[i],
							"Fail", ScreenShotName);
				}
			}
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, ObjectName, e);
			IsPass = false;
		}
		return IsPass;
	}

	public String GetAdherenceEventDate(String ObjectName, String EventType) {
		String Date = "";
		String Month = "";
		try {
			WebElement ActionObject = GetObject(ObjectName);
			String sActual = ActionObject.getAttribute("onclick");
			String a[] = sActual.split("'");
			DateFormatSymbols DFS = new DateFormatSymbols();
			Month = DFS.getShortMonths()[Integer.valueOf(a[3])];
			Date = a[1] + "-" + Month + "-" + a[5];
			return Date;
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, ObjectName, e);
			Report.Write(EventType, "Data should be present for event"
					+ EventType, "Data is not present for event" + EventType,
					"Fail", true);
			return "31-FEB-2000";
		}

	}

	public boolean CheckSettingsHistoryYAXIS(String SettingsValue) {
		boolean IsPass = true;
		SettingsValue = SettingsValue.trim();
		String GeneralXPath = ORXPath("DeviceSettings_AllElements_DT");
		String Axis = "YAxis_";
		int Count = 0;
		try {
			if (CompareText(LangData("FilterNeedleSpeed"), SettingsValue)) {
				CompareText(Axis + SettingsValue,
						LangData("FilterNeedleSpeed"), GetText(GeneralXPath
								+ "[1]"));
				CompareText(Axis + "NeedleSpeedSlow",
						LangData("NeedleSpeedSlow"), GetText(GeneralXPath
								+ "[3]"));
				CompareText(Axis + "NeedleSpeedMedium",
						LangData("NeedleSpeedMedium"), GetText(GeneralXPath
								+ "[4]"));
				CompareText(Axis + "NeedleSpeedFast",
						LangData("NeedleSpeedFast"), GetText(GeneralXPath
								+ "[5]"));
			} else if (CompareText(LangData("FilterInjectionTime"),
					SettingsValue)) {
				CompareText(Axis + SettingsValue,
						LangData("FilterInjectionTime") + " ("
								+ LangData("Seconds") + ")",
						GetText(GeneralXPath + "[1]"));
				for (int i = 0; i <= 9; i++) {
					Count = Count + 3;
					CompareText(Axis + (Count) + "Seconds",
							String.valueOf(Count), GetText(GeneralXPath + "["
									+ (i + 3) + "]"));
				}
			} else if (CompareText(LangData("FilterInjectionDepth"),
					SettingsValue)) {
				CompareText(Axis + SettingsValue,
						LangData("FilterInjectionDepth") + " "
								+ LangData("UnitMMBrackets"),
						GetText(GeneralXPath + "[1]"));
				Count = 2;
				for (int i = 0; i <= 3; i++) {
					Count = Count + 2;
					CompareText(Axis + Count + "Seconds",
							String.valueOf(Count), GetText(GeneralXPath + "["
									+ (i + 3) + "]"));
				}
			} else if (CompareText(LangData("FilterInjectionSpeed"),
					SettingsValue)) {
				CompareText(Axis + SettingsValue,
						LangData("FilterInjectionSpeed"), GetText(GeneralXPath
								+ "[1]"));
				CompareText(Axis + "NeedleSpeedSlow",
						LangData("NeedleSpeedSlow"), GetText(GeneralXPath
								+ "[3]"));
				CompareText(Axis + "NeedleSpeedMedium",
						LangData("NeedleSpeedMedium"), GetText(GeneralXPath
								+ "[4]"));
				CompareText(Axis + "NeedleSpeedFast",
						LangData("NeedleSpeedFast"), GetText(GeneralXPath
								+ "[5]"));
			} else if (CompareText(LangData("FilterDoseStrategy"),
					SettingsValue)) {
				CompareText(Axis + SettingsValue,
						LangData("FilterDoseStrategy"), GetText(GeneralXPath
								+ "[1]"));
				CompareText(Axis + "DoseStrategyByMilligram",
						LangData("DoseStrategyByMilligram"),
						GetText(GeneralXPath + "[3]"));
				CompareText(Axis + "DoseStrategyByWeight",
						LangData("DoseStrategyByWeight"), GetText(GeneralXPath
								+ "[4]"));
				CompareText(Axis + "DoseStrategyByBodySurface",
						LangData("DoseStrategyByBodySurface"),
						GetText(GeneralXPath + "[5]"));
			} else if (CompareText(LangData("DoseFrequency"), SettingsValue)) {
				CompareText(Axis + SettingsValue, LangData("DoseFrequency")
						+ " (" + LangData("DaysPerWeek") + ")",
						GetText(GeneralXPath + "[1]"));
				CompareText(Axis + "DaysPerWeek", String.valueOf("6"),
						GetText(GeneralXPath + "[3]"));
				CompareText(Axis + "DaysPerWeek", String.valueOf("7"),
						GetText(GeneralXPath + "[4]"));
			} else if (CompareText(LangData("FilterDoseAdjustment"),
					SettingsValue)) {
				CompareText(Axis + SettingsValue,
						LangData("FilterDoseAdjustment"), GetText(GeneralXPath
								+ "[1]"));
				CompareText(Axis + "DoseOFF", LangData("DoseOFF"),
						GetText(GeneralXPath + "[3]"));
				CompareText(Axis + "50%", "> 50%",
						GetText(GeneralXPath + "[4]"));
				CompareText(Axis + "DoseAutomatic", LangData("DoseAutomatic"),
						GetText(GeneralXPath + "[5]"));
			}

			else if (CompareText(LangData("DosePerDay"), SettingsValue)) {
				CompareText(Axis + SettingsValue, LangData("DosePerDay") + " ("
						+ LangData("UnitMg") + ")", GetText(GeneralXPath
						+ "[1]"));
				for (int i = 0; i <= 4; i++) {
					CompareText(Axis + i + "MG", String.valueOf(i),
							GetText(GeneralXPath + "[" + (i + 3) + "]"));
				}
			}

			else {
				Report.Write(SettingsValue,
						"Correct parameter Should be given",
						"Wrong parameter is given", "Fail", false);
				IsPass = false;
			}

		} catch (Exception e) {
			Env.logger.log(Level.ERROR, SettingsValue, e);
			IsPass = false;
		}
		return IsPass;
	}

	public boolean CheckSettingsHistoryXAXIS(String RangeValue,
			String SettingsValue) {
		boolean IsPass = true;
		RangeValue = RangeValue.trim();
		SettingsValue = SettingsValue.trim();
		String LogicalName = "XAxis_" + RangeValue + "_" + SettingsValue;
		String FromDate = "";
		String BaseDate = "";
		String NextMonthDateExpected = "";
		String NextMonthDateActual = "";
		int Count = 0;
		int Index = getXAxisIndex(SettingsValue);
		int Variation = 0;
		CompareText("XAxis_Date", LangData("Date"),
				GetText("DeviceSettings_XAxis_ST"));
		List<WebElement> Date = browser.findElements(By
				.xpath(ORXPath("DeviceSettings_AllElements_DT")));
		try {
			if (CompareText(RangeValue, LangData("RangeCustomDateRange"))) {
				FromDate = Edit_GetValue("SettingsHistory_FromMonthValue_ST"); // From
																				// Date
																				// for
																				// Custom
																				// Date
																				// Range
			} else
				FromDate = GetText("SettingsHistory_FromMonth_ST"); // For Other
																	// Cases

			// To get the Variation between the Dates
			if (ObjectExists(ORXPath("DeviceSettings_AllElements_DT") + "["
					+ (Index + 2) + "]")) {
				NextMonthDateActual = (GetText(ORXPath("DeviceSettings_AllElements_DT")
						+ "[" + (Index + 2) + "]")
						+ " " + (GetText(ORXPath("DeviceSettings_AllElements_DT")
						+ "[" + (Index + 3) + "]"))); // Check Date format
				Variation = DateDiff(FromDate, NextMonthDateActual, "DAYS");
			} else
				Variation = 0;

			if (Index != -1) {
				if (FromDate.startsWith("0")) {
					FromDate = FromDate.replaceFirst("0", "");
				}
				NextMonthDateExpected = FromDate;
				for (int i = Index; i <= Date.size(); i += 2) {
					Count = i + 1;
					NextMonthDateActual = (GetText(ORXPath("DeviceSettings_AllElements_DT")
							+ "[" + i + "]")
							+ " " + (GetText(ORXPath("DeviceSettings_AllElements_DT")
							+ "[" + Count + "]"))); // Check Date format
					CompareText(LogicalName, NextMonthDateExpected,
							NextMonthDateActual);
					BaseDate = NextMonthDateActual;
					NextMonthDateExpected = Date_AddDays(BaseDate, Variation,
							Calendar.DATE);
					String[] sDate = NextMonthDateExpected.split(" ");
					if (sDate[0].substring(0, 1).contains("0")) {
						sDate[0] = sDate[0].replaceFirst("0", "");
					}
					NextMonthDateExpected = sDate[0] + " " + sDate[1] + " "
							+ sDate[2];
				}
				return true;
			} else {
				Report.Write(LogicalName, "Correct parameters should be given",
						"Wrong parameter is given", "Fail", false);
				IsPass = false;
			}

		} catch (Exception e) {
			Env.logger.log(Level.ERROR, LogicalName, e);
			IsPass = false;
		}
		return IsPass;
	}

	public int getXAxisIndex(String SettingsValue) {
		if (CompareText(LangData("FilterNeedleSpeed"), SettingsValue)) {
			return 6;
		} else if (CompareText(LangData("FilterInjectionTime"), SettingsValue)) {
			return 13;
		} else if (CompareText(LangData("FilterInjectionDepth"), SettingsValue)) {
			return 7;
		} else if (CompareText(LangData("FilterInjectionSpeed"), SettingsValue)) {
			return 6;
		} else if (CompareText(LangData("FilterDoseStrategy"), SettingsValue)) {
			return 6;
		} else if (CompareText(LangData("DoseFrequency"), SettingsValue)) {
			return 5;
		} else if (CompareText(LangData("FilterDoseAdjustment"), SettingsValue)) {
			return 6;
		} else if (CompareText(LangData("DosePerDay"), SettingsValue)) {
			return 8;
		} else {
			return -1;
		}
	}

	public boolean SearchForDevice(String DeviceID) {
		Edit_SetValue("PatientAssignDevice_SearchBox_EB", DeviceID);
		Btn_Click("PatientAssignDevice_Search_BN");
		if (Env.GetBrowserUsed().startsWith("IE")) {
			if (ObjectExists("PatientAssignDevice_Search_BN")) {
				Btn_Click("PatientAssignDevice_Search_BN");
			}
		}
		if (GetText(ORXPath("DeviceSettings_DeviceID_ST")).contains(DeviceID)) {
			return true;
		} else {
			throw new RuntimeException("Device not found: " + DeviceID);
		}

	}

	public boolean AssignDevice(String DeviceID, String PatientName,
			String DeviceStatus, String DeviceUsageStartDate) {
		try {
			if (!GetBrowserTitle().startsWith(LangData("UnassignedDevices"))) {
				Btn_Click("NavigationTab_DevicesTab_ST"); // If not in
															// Unassinged
															// Devices tab, then
															// click the Devices
															// tab
				WaitForPageload();
			}
			SearchForDevice(DeviceID);
			Rbtn_SetOption("UnassignedDevices_SelectDevice_RB", "ON");
			Btn_Click("UnassignedDevices_AssignDevice_BN");
			SearchForPatient(PatientName);
			Rbtn_SetOption("DevicesAssignDevice_SelectPatient_RB", "ON");
			Btn_Click("DevicesAssignDevice_Continue_BN");
			WaitForObject("DevicesAssignDevice_ConfirmYes_BN"); // Wait for the
																// confirmation
																// PopUp
			Btn_Click("DevicesAssignDevice_ConfirmYes_BN"); // Click Yes on the
															// confirmation
															// Popup

			// Handling for EP1 Device - to maintain adherence data
			if (DeviceID.compareToIgnoreCase(GetDeviceID("EP1Device")) == 0
					&& PatientName.compareToIgnoreCase(TestData("ECOSPatient")) == 0) {
				DeviceUsageStartDate = "02-Mar-2010";
				DeviceStatus = LangData("DevicePrimary");
			}

			if (DeviceStatus.startsWith(LangData("DevicePrimary"))) {
				Rbtn_SetOption("AssignedDevices_Primary_RB", "ON");
			} else {
				Rbtn_SetOption("AssignedDevices_Secondary_RB", "ON");
			}

			SelectDate("PatientAssignDevice_Calendar_TT", DeviceUsageStartDate);
			Btn_Click("AssignDevice_Submit_BN");

			String ActualText = GetText("AssignDevice_AssignedtoSuccessMsg_DT");
			String ExpectedText = LangData("AssignedToSmall");
			ExpectedText = ExpectedText.replace("{0}", DeviceID);
			ExpectedText = ExpectedText.replace("{1}",
					FormatPatientName(PatientName));
			ExpectedText = ExpectedText.replace("{2}", DeviceStatus);
			ExpectedText = ExpectedText.replace("{3}",
					TranslateMonth(DeviceUsageStartDate));

			if (CompareText(ExpectedText, ActualText)) {
				Env.logger.log(Level.INFO, DeviceID
						+ " is Assigned successfully to " + PatientName);
				return true;
			} else {
				Env.logger.log(Level.WARN, "Unable to Assign the device to "
						+ PatientName);
				return false;
			}
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, DeviceID, e);

			return false;
		}
	}

	public boolean UnassignDevice(String DeviceID) {
		try {
			if (!GetBrowserTitle().startsWith(LangData("AssignedDevice"))) {
				Btn_Click("NavigationTab_DevicesTab_ST"); // If not in Assinged
															// Devices tab, then
															// click the Devices
															// tab > Assigned
															// Devices Tab
				Lnk_Click("NavigationTab_AssignedDevicesTab_ST");
			}

			// Search for Device
			SearchForDevice(DeviceID);
			Rbtn_SetOption("UnassignedDevices_SelectDevice_RB", "ON");

			// If the button is not enabled, then the device is primary. So make
			// to secondary and then unassign
			if (!Btn_IsEnabled("DevicesAssignedDevices_Unassign_BN")) {

				String AlternateDeviceID = GetDeviceID("NewDevice");
				if (DeviceID.endsWith(AlternateDeviceID)) {
					AlternateDeviceID = GetDeviceID("EP1Device");
				}
				SearchForDevice(AlternateDeviceID);

				WbTbl_SelectRbtn("DevicesAssignedDevices_Header_TE", 4,
						LangData("SecondaryDevice"));
				Btn_Click("AssignedDevices_EditDevice_BN");
				Rbtn_SetOption("AssignedDEvices_EditDevicePrimaryDevice_RB",
						"ON");
				Btn_Click("AssignedDevices_EditDeviceSubmit_BN");
				SearchForDevice(DeviceID);
				Rbtn_SetOption("UnassignedDevices_SelectDevice_RB", "ON");
			}

			if (Btn_Click("DevicesAssignedDevices_Unassign_BN")) {
				Btn_Click("UnassignDevice_Yes_BN");
				Env.logger.log(Level.INFO, DeviceID
						+ " is Unassigned successfully");
				return true;
			} else {
				Env.logger.log(Level.WARN, "Unable to Unassign the device");
				return false;
			}

		} catch (Exception e) {
			Env.logger.log(Level.ERROR, DeviceID, e);
			return false;
		}
	}

	public boolean EditDevice(String DeviceID, boolean ShouldBeSetAsPrimary,
			String DeviceUsageStartDate) {
		try {
			if (!GetBrowserTitle().startsWith(LangData("AssignedDevice"))) {
				Btn_Click("NavigationTab_DevicesTab_ST"); // If not in Assinged
															// Devices tab, then
															// click the Devices
															// tab > Assigned
															// Devices Tab
				Lnk_Click("NavigationTab_AssignedDevicesTab_ST");
			}

			// Search for Device
			SearchForDevice(DeviceID);
			Rbtn_SetOption("UnassignedDevices_SelectDevice_RB", "ON");

			// If the button is not enabled, then the device to be edited is
			// primary. If this device should be made as secondary, then make
			// newDevice as the primary device.
			if (!ShouldBeSetAsPrimary) {
				if (!Btn_IsEnabled("DevicesAssignedDevices_Unassign_BN")) {
					String AlternateDeviceID = GetDeviceID("NewDevice");
					SearchForDevice(AlternateDeviceID);
					WbTbl_SelectRbtn("DevicesAssignedDevices_Header_TE", 4,
							LangData("SecondaryDevice"));
					Btn_Click("AssignedDevices_EditDevice_BN");
					Rbtn_SetOption(
							"AssignedDEvices_EditDevicePrimaryDevice_RB", "ON");
					Btn_Click("AssignedDevices_EditDeviceSubmit_BN");
					SearchForDevice(DeviceID);
					Rbtn_SetOption("UnassignedDevices_SelectDevice_RB", "ON");
				}
			}

			Btn_Click("AssignedDevices_EditDevice_BN");

			if (ShouldBeSetAsPrimary) {
				Rbtn_SetOption("AssignedDEvices_EditDevicePrimaryDevice_RB",
						"ON");
			} else {
				Rbtn_SetOption("AssignedDevices_EditDeviceSecondaryDevice_RB",
						"ON");
			}
			SelectDate("AssignedDevices_EditDeviceDevcieUsageStartDate_TT",
					DeviceUsageStartDate);
			Wait(3);
			HideCursor();
			WaitForObject("AssignedDevices_EditDeviceSubmit_BN");
			Btn_Click("AssignedDevices_EditDeviceSubmit_BN");

			if (CompareText(
					ORData("AssignedDevices_DeviceDetailsUpdatedSuccessMsg_DT"),
					GetText("AssignedDevices_DeviceDetailsUpdatedSuccessMsg_DT"))) {
				Env.logger
						.log(Level.INFO, DeviceID + " is Edited successfully");
				return true;
			} else {
				Env.logger.log(Level.WARN, "Unable to edit the device "
						+ DeviceID + ". To status as " + ShouldBeSetAsPrimary
						+ " and DUS as " + DeviceUsageStartDate);
				return false;
			}
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, DeviceID, e);
			return false;
		}
	}

	public FileUtil DownloadFile(String... DownloadParams) {
		/*
		 * Format : DownloadFile(ObjectName, GetURLFrom, FileName,
		 * ValidorInvalidScenario, TimeRange, FromDate, ToDate) Example1:
		 * DownloadFile("", "ADHERENCEOVERVIEW", "AdherenceOverview.pdf",
		 * "INVALID",) or Example2: DownloadFile("", "ADHERENCEOVERVIEW",
		 * "AdherenceOverview.pdf", "INVALID", "6", "01 Aug 2013",
		 * "20 Jan 2014")
		 */
		String UserName = DownloadParams[0].trim();
		String ObjectName = DownloadParams[1].trim();
		String GetURLFrom = DownloadParams[2];
		String FileName = DownloadParams[3];
		String ValidorInvalidScenario = DownloadParams[4];
		String ExpectedFileName = null;
		String FromDate = GetFromDate();
		String ToDate = GetToDate();
		String TimeRange = "-2";
		String FromKey = "&from=";
		String ToKey = "&to=";

		if (DownloadParams.length >= 6
				&& !Env.CurrentTestName.startsWith("REG")) {
			ExpectedFileName = DownloadParams[5];
		}

		if (DownloadParams.length >= 8) {
			ExpectedFileName = null;
			TimeRange = DownloadParams[5];
			if (TimeRange.compareToIgnoreCase("-2") != 0) { // Apply Key based
															// on Time Range
				FromKey = "&selectFrom=";
				ToKey = "&selectTo=";
			}
			// Get the From Date
			FromDate = DownloadParams[6].replaceAll("-", " ");
			FromDate = TranslateDate(FromDate, "dd MMM yyyy", true);
			FromDate = Encode(FromDate);

			// Get the To Date
			ToDate = DownloadParams[7].replaceAll("-", " ");
			ToDate = TranslateDate(ToDate, "dd MMM yyyy", true);
			ToDate = Encode(ToDate);
		}

		FileUtil PDF = new FileUtil(this, UserName);
		PDF.ExpectedLocalizedFileName = ExpectedFileName;
		ValidorInvalidScenario = ValidorInvalidScenario.toUpperCase();
		String Date1 = "";
		String Date2 = "";
		try {
			String Source = "";
			GetURLFrom = GetURLFrom.toUpperCase();
			PDF.DestinationPath = Env.ScreenshotPath + "/"
					+ GetTimeStamp("hhmmss") + "-" + Env.GetBrowserUsed() + "-"
					+ FileName;
			if (GetURLFrom.compareToIgnoreCase("HREF") == 0) {
				WebElement ActionObject = GetObject(ObjectName);
				UserAction.contextClick(ActionObject).build().perform();
				Source = ActionObject.getAttribute("href");
			} else if (CompareText(GetURLFrom, "ADHERENCEBEHAVIOUR")) {
				String GraphName = "&injectionAdherence=" + Encode("true");// Default
																			// :
																			// Adherence
																			// Injection
																			// vs
																			// Patients
																			// in
																			// Clinic
				String GraphTypeValue = Encode("INJECTION");
				String ComparisonValue = Encode("IN_CLINIC");
				if (ObjectName.toUpperCase().startsWith("DOSE")) {
					GraphName = "&doseDensity=" + Encode("true");
					GraphTypeValue = Encode("DOSE");
				}
				if (ObjectName.toUpperCase().endsWith("COUNTRY")) {
					ComparisonValue = Encode("IN_COUNTRY");
				}
				if (FileName.endsWith("pdf")) { // For PDF
					Source = "/ecko/patientReport/generatePDF?id="
							+ GetPatientIDFromURL() + "&print="
							+ Encode("true") + "&ecosid=" + GraphName
							+ "&graphType=" + GraphTypeValue
							+ "&comparisonType=" + ComparisonValue
							+ "&comparisonCheck=&monthRange="
							+ Encode(TimeRange) + "&selectFrom=" + FromDate
							+ "&selectTo=" + ToDate;
				} else { // For CSV
					Source = "/ecko/behaviourGraph/export?id="
							+ GetPatientIDFromURL() + "&ecosid=&graphType="
							+ GraphTypeValue + "&comparisonType="
							+ ComparisonValue + "&comparisonCheck=&monthRange="
							+ Encode(TimeRange) + FromKey + FromDate + ToKey
							+ ToDate + "&prevLocale="
							+ Encode(GetLocale().toString());
				}
			} else if (CompareText(GetURLFrom, "ADHERENCEOVERVIEW")) {
				Source = "/ecko/reportList/generatePdf?id="
						+ GetPatientIDFromURL() + "&monthRange="
						+ Encode(TimeRange) + "&overview=" + Encode("overview")
						+ "&inline=" + Encode("1") + FromKey + FromDate + ToKey
						+ ToDate;
			} else if (CompareText(GetURLFrom, "ADHERENCEGRAPH")) {
				Source = "/ecko/reportList/generatePdf?id="
						+ GetPatientIDFromURL() + "&monthRange="
						+ Encode(TimeRange) + "&Graph=" + Encode("Graph")
						+ "&inline=" + Encode("1") + FromKey + FromDate + ToKey
						+ ToDate;
			} else if (CompareText(GetURLFrom, "ADHERENCECALENDAR")) {
				Source = "/ecko/reportList/generatePdf?id="
						+ GetPatientIDFromURL() + "&monthRange="
						+ Encode(TimeRange) + "&Calendar=" + Encode("Calendar")
						+ "&inline=" + Encode("1") + FromKey + FromDate + ToKey
						+ ToDate;
			} else if (CompareText(GetURLFrom, "ADHERENCELIST")
					&& FileName.endsWith("pdf")) {
				Source = "/ecko/reportList/generatePdf?id="
						+ GetPatientIDFromURL()
						+ "&monthRange="
						+ Encode(TimeRange)
						+ "&List="
						+ Encode("List")
						+ "&inline="
						+ Encode("1")
						+ FromKey
						+ FromDate
						+ ToKey
						+ ToDate
						+ "&hn=dHJ1ZQ==&hc=dHJ1ZQ==&hp=dHJ1ZQ==&hm=ZmFsc2U=&hnc=dHJ1ZQ==&hnd=dHJ1ZQ==";
			} else if (CompareText(GetURLFrom, "ADHERENCELIST")
					&& FileName.endsWith("csv")) {
				Source = "/ecko/reportList/historyListExport?id="
						+ GetPatientIDFromURL() + "&monthRange="
						+ Encode(TimeRange) + FromKey + FromDate + ToKey
						+ ToDate;
			} else if (CompareText(GetURLFrom, "PATIENTREPORT")) {
				if (!CompareText(ValidorInvalidScenario, "INVALID")) {
					Source = "/ecko/patientReport/generatePdf?id="
							+ GetPatientIDFromURL() + "&monthRange="
							+ Encode(TimeRange) + "&overview="
							+ Encode("overview") + "&Graph=" + Encode("Graph")
							+ "&injection=" + Encode("injection") + "&Remarks="
							+ Encode("Remarks") + "&List=" + Encode("List")
							+ "&Calendar=" + Encode("Calendar") + FromKey
							+ FromDate + ToKey + ToDate;
				} else {
					Date1 = Encode(Date_AddDays(GetTimeStamp("dd MMM yyyy"), 1,
							Calendar.DATE).replaceAll("-", " "));
					Source = "/ecko/patientReport/generatePdf?id="
							+ GetPatientIDFromURL() + "&monthRange="
							+ Encode(TimeRange) + "&overview="
							+ Encode("overview") + "&Graph=" + Encode("Graph")
							+ "&injection=" + Encode("injection") + "&Remarks="
							+ Encode("Remarks") + "&List=" + Encode("List")
							+ "&Calendar=" + Encode("Calendar") + FromKey
							+ Date1 + ToKey + Date1;
				}
			} else if (CompareText(GetURLFrom, "PAYERREPORT")) {
				Source = "/ecko/payerReport/generatePdf?id="
						+ GetPayerIDFromURL() + "&monthRange="
						+ Encode(TimeRange) + "&overview=" + Encode("overview")
						+ "&drugWastage=" + Encode("drugWastage") + FromKey
						+ FromDate + ToKey + ToDate + "&noOfPatients="
						+ Encode(ObjectName);
			} else if (CompareText(GetURLFrom, "AGGREGATEREPORT")) {
				String ReportFromDate = "01 Jan 2005"; // Full Range value
														// hardcoded.
				String ReportToDate = GetTimeStamp("dd MMM yyyy");
				Source = "/ecko/patientReport/aggregatedHcpReport?id="
						+ GetPayerIDFromURL() + "&monthRange=" + Encode("-1")
						+ "&selectFrom=" + Encode(ReportFromDate)
						+ "&selectTo=" + Encode(ReportToDate);
			} else if (CompareText(GetURLFrom, "GROWTHCURVE")
					&& FileName.endsWith("pdf")) {// MTE2Mw==
				Source = "/ecko/patientReport/generatePDF?id="
						+ GetPatientIDFromURL() + "&ecosid=&statisticsFilter="
						+ GetStaticFilertID(0) + "&percentileList="
						+ Encode("P3,P50,P97") + "&sdList=" + Encode("-3,0,3")
						+ "&growthCurve=" + Encode("true") + "&print="
						+ Encode("true");
			} else if (CompareText(GetURLFrom, "GROWTHCURVE")
					&& FileName.endsWith("xls")) {
				Source = "/ecko/patientGrowth/export?id="
						+ GetPatientIDFromURL() + "&ecosid=&statisticsFilter="
						+ GetStaticFilertID(0);
			} else if (CompareText(GetURLFrom, "GROWTHVELOCITYCURVE")
					&& FileName.endsWith("pdf")) {
				Source = "/ecko/patientReport/generatePDF?id="
						+ GetPatientIDFromURL() + "&ecosid=&percentileList="
						+ Encode("P3,P50") + "&sdList=" + Encode("undefined")
						+ "&print=" + Encode("true") + "&growthVelocityCurve="
						+ Encode("true") + "&statisticsFilter="
						+ GetStaticFilertID(0);
			} else if (CompareText(GetURLFrom, "GROWTHVELOCITYCURVE")
					&& FileName.endsWith("xls")) {
				Source = "/ecko/patientGrowthVelocity/export?id="
						+ GetPatientIDFromURL() + "&ecosid=&statisticsFilter="
						+ GetStaticFilertID(1);
			} else if (CompareText(GetURLFrom, "BODYMASSINDEX")
					&& FileName.endsWith("pdf")) {
				Source = "/ecko/patientReport/generatePDF?id="
						+ GetPatientIDFromURL() + "&ecosid=&percentileList="
						+ Encode("P3,P50,P97") + "&sdList="
						+ Encode("undefined") + "&bodyMassIndex="
						+ Encode("true") + "&print=" + Encode("true")
						+ "&statisticsFilter=" + GetStaticFilertID(0);
			} else if (CompareText(GetURLFrom, "BODYMASSINDEX")
					&& FileName.endsWith("xls")) {
				Source = "/ecko/patientBodyMassIndex/export?id="
						+ GetPatientIDFromURL() + "&ecosid=&statisticsFilter="
						+ GetStaticFilertID(1);
			} else if (CompareText(GetURLFrom, "WEIGHTCURVE")
					&& FileName.endsWith("pdf")) {
				Source = "/ecko/patientReport/generatePDF?id="
						+ GetPatientIDFromURL() + "&ecosid=&percentileList="
						+ Encode("P3,P50,P97") + "&sdList="
						+ Encode("undefined") + "&weightCurve="
						+ Encode("true") + "&print=" + Encode("true")
						+ "&statisticsFilter=" + GetStaticFilertID(0);
			} else if (CompareText(GetURLFrom, "WEIGHTCURVE")
					&& FileName.endsWith("xls")) {
				Source = "/ecko/patientGrowth/exportWeight?id="
						+ GetPatientIDFromURL() + "&ecosid=&statisticsFilter="
						+ GetStaticFilertID(1);
			} else if (CompareText(GetURLFrom, "COMBINATIONGRAPH")) {
				if (ObjectName.toUpperCase().startsWith("GROWTHVELOCITY")) {
					Source = "/ecko/patientReport/generatePDF?id="
							+ GetPatientIDFromURL() + "&ecosid=&print="
							+ Encode("true") + "&doseAdherenceGrowthVelocity="
							+ Encode("true");
				} else {
					Source = "/ecko/patientReport/generatePDF?id="
							+ GetPatientIDFromURL() + "&ecosid=&print="
							+ Encode("true") + "&doseAdherenceGrowthCurve="
							+ Encode("true");
				}
			} else if (CompareText(GetURLFrom, "LOCALCOMPONENT")) {
				Source = "/eck/webstart/master.jnlp";

				// ECOS - PDF, EXCEL and CSV
			} else if (CompareText(GetURLFrom, "ECOS-ADHERENCEBEHAVIOUR")) {
				String GraphName = "&injectionAdherence=" + Encode("true");// Default
																			// :
																			// Adherence
																			// Injection
																			// vs
																			// Patients
																			// in
																			// Clinic
				String GraphTypeValue = Encode("INJECTION");
				String ComparisonValue = Encode("IN_CLINIC");
				if (ObjectName.toUpperCase().startsWith("DOSE")) {
					GraphName = "&doseDensity=" + Encode("true");
					GraphTypeValue = Encode("DOSE");
				}
				if (ObjectName.toUpperCase().endsWith("COUNTRY")) {
					ComparisonValue = Encode("IN_COUNTRY");
				}
				if (FileName.endsWith("pdf")) {// For PDF
					Source = "/ecos/patientReport/generatePDF?id="
							+ GetPatientIDFromURL() + "&print="
							+ Encode("true") + "&ecosid=" + GetECOSIDFromURL()
							+ GraphName + "&graphType=" + GraphTypeValue
							+ "&comparisonType=" + ComparisonValue
							+ "&monthRange=" + Encode(TimeRange)
							+ "&selectFrom=" + FromDate + "&selectTo=" + ToDate;
				} else {// For CSV
					Source = "/ecos/behaviourGraph/export?id="
							+ GetPatientIDFromURL() + "&ecosid="
							+ GetECOSIDFromURL() + "&graphType="
							+ GraphTypeValue + "&comparisonType="
							+ ComparisonValue + "&monthRange="
							+ Encode(TimeRange) + FromKey + FromDate + ToKey
							+ ToDate + "&prevLocale="
							+ Encode(GetLocale().toString());
				}
			} else if (CompareText(GetURLFrom, "ECOS-ADHERENCEOVERVIEW")) {
				Source = "/ecos/reportList/generatePdf?id="
						+ GetPatientIDFromURL() + "&monthRange="
						+ Encode(TimeRange) + "&overview=" + Encode("overview")
						+ "&inline=" + Encode("1") + FromKey + FromDate + ToKey
						+ ToDate;
			} else if (CompareText(GetURLFrom, "ECOS-ADHERENCEGRAPH")) {
				Source = "/ecos/reportList/generatePdf?id="
						+ GetPatientIDFromURL() + "&monthRange="
						+ Encode(TimeRange) + "&Graph=" + Encode("Graph")
						+ "&inline=" + Encode("1") + FromKey + FromDate + ToKey
						+ ToDate;
			} else if (CompareText(GetURLFrom, "ECOS-ADHERENCECALENDAR")) { // Changed
				Source = "/ecos/reportList/generatePdf?id="
						+ GetPatientIDFromURL() + "&monthRange="
						+ Encode(TimeRange) + "&Calendar=" + Encode("Calendar")
						+ "&inline=" + Encode("1") + FromKey + FromDate + ToKey
						+ ToDate + "&hn=" + Encode("true") + "&hc="
						+ Encode("true") + "&hp=" + Encode("true") + "&hm="
						+ Encode("true") + "&hnc=" + Encode("true") + "&hnd="
						+ Encode("true");
			} else if (CompareText(GetURLFrom, "ECOS-ADHERENCELIST")
					&& FileName.endsWith("pdf")) {
				Source = "/ecos/reportList/generatePdf?id="
						+ GetPatientIDFromURL()
						+ "&monthRange="
						+ Encode(TimeRange)
						+ "&List="
						+ Encode("List")
						+ "&inline="
						+ Encode("1")
						+ FromKey
						+ FromDate
						+ ToKey
						+ ToDate
						+ "&hn=dHJ1ZQ==&hc=dHJ1ZQ==&hp=dHJ1ZQ==&hm=ZmFsc2U=&hnc=dHJ1ZQ==&hnd=dHJ1ZQ==";
			} else if (CompareText(GetURLFrom, "ECOS-ADHERENCELIST")
					&& FileName.endsWith("csv")) {
				Source = "/ecos/reportList/historyListExport?id="
						+ GetPatientIDFromURL() + "&monthRange="
						+ Encode(TimeRange) + FromKey + FromDate + ToKey
						+ ToDate;
			} else if (CompareText(GetURLFrom, "ECOS-GROWTHCURVE")
					&& FileName.endsWith("pdf")) {
				Source = "/ecos/patientReport/generatePDF?id="
						+ GetPatientIDFromURL() + "&ecosid="
						+ GetECOSIDFromURL() + "&percentileList="
						+ Encode("P3,P50,P97") + "&sdList=" + Encode("-3,0,3")
						+ "&growthCurve=" + Encode("true") + "&print="
						+ Encode("true") + "&statisticsFilter="
						+ GetStaticFilertID(4); // Growth (Height) - China,
												// 2-18yrs
			} else if (CompareText(GetURLFrom, "ECOS-GROWTHCURVE")
					&& FileName.endsWith("xls")) {
				Source = "/ecos/patientGrowth/export?id="
						+ GetPatientIDFromURL() + "&ecosid="
						+ GetECOSIDFromURL() + "&statisticsFilter="
						+ GetStaticFilertID(4); // Growth (Height) - China,
												// 2-18yrs
			} else if (CompareText(GetURLFrom, "ECOS-GROWTHVELOCITYCURVE")
					&& FileName.endsWith("pdf")) {
				Source = "/ecos/patientReport/generatePDF?id="
						+ GetPatientIDFromURL() + "&ecosid="
						+ GetECOSIDFromURL() + "&percentileList="
						+ Encode("P3,P50") + "&sdList=" + Encode("undefined")
						+ "&print=" + Encode("true") + "&growthVelocityCurve="
						+ Encode("true") + "&statisticsFilter="
						+ GetStaticFilertID(0); // Growth Velocity 2.5-18 yrs
			} else if (CompareText(GetURLFrom, "ECOS-GROWTHVELOCITYCURVE")
					&& FileName.endsWith("xls")) {
				Source = "/ecos/patientGrowthVelocity/export?id="
						+ GetPatientIDFromURL() + "&ecosid="
						+ GetECOSIDFromURL() + "&statisticsFilter="
						+ GetStaticFilertID(1); // Growth Velocity 2.5-18 yrs
			} else if (CompareText(GetURLFrom, "ECOS-BODYMASSINDEX")
					&& FileName.endsWith("pdf")) {
				Source = "/ecos/patientReport/generatePDF?id="
						+ GetPatientIDFromURL() + "&ecosid="
						+ GetECOSIDFromURL() + "&percentileList="
						+ Encode("P50") + "&sdList=" + Encode("-3,0,3")
						+ "&bodyMassIndex=" + Encode("true") + "&print="
						+ Encode("true") + "&statisticsFilter="
						+ GetStaticFilertID(4);// BMI - Spain, 0.25-18yrs
			} else if (CompareText(GetURLFrom, "ECOS-BODYMASSINDEX")
					&& FileName.endsWith("xls")) {
				Source = "/ecos/patientBodyMassIndex/export?id="
						+ GetPatientIDFromURL() + "&ecosid="
						+ GetECOSIDFromURL() + "&statisticsFilter="
						+ GetStaticFilertID(4); // BMI - Spain, 0.25-18yrs
			} else if (CompareText(GetURLFrom, "ECOS-WEIGHTCURVE")
					&& FileName.endsWith("pdf")) {
				Source = "/ecos/patientReport/generatePDF?id="
						+ GetPatientIDFromURL() + "&ecosid="
						+ GetECOSIDFromURL() + "&percentileList="
						+ Encode("P3,P50,P97") + "&sdList="
						+ Encode("undefined") + "&print=" + Encode("true")
						+ "&weightCurve=" + Encode("true")
						+ "&statisticsFilter=" + GetStaticFilertID(4); // Weight
																		// -
																		// China,
																		// 2-18yrs
			} else if (CompareText(GetURLFrom, "ECOS-WEIGHTCURVE")
					&& FileName.endsWith("xls")) {
				Source = "/ecos/patientGrowth/exportWeight?id="
						+ GetPatientIDFromURL() + "&ecosid="
						+ GetECOSIDFromURL() + "&statisticsFilter="
						+ GetStaticFilertID(4); // Weight - China, 2-18yrs
			} else if (CompareText(GetURLFrom, "ECOS-COMBINATIONGRAPH")) {
				if (ObjectName.toUpperCase().startsWith("GROWTHVELOCITY")) {
					Source = "/ecos/patientReport/generatePDF?id="
							+ GetPatientIDFromURL() + "&ecosid="
							+ GetECOSIDFromURL() + "&print=" + Encode("true")
							+ "&doseAdherenceGrowthVelocity=" + Encode("true");
				} else {
					Source = "/ecos/patientReport/generatePDF?id="
							+ GetPatientIDFromURL() + "&ecosid="
							+ GetECOSIDFromURL() + "&print=" + Encode("true")
							+ "&doseAdherenceGrowthCurve=" + Encode("true");
				}

			} else if (CompareText(GetURLFrom, "ECOS-NURSEGROWTHDATA")
					&& FileName.endsWith("pdf")) {
				Date1 = Encode("01 Nov 2007");
				Date2 = Encode("01 Jan 2013");
				Source = "/ecos/patientReport/generatePDF?id="
						+ GetPatientIDFromURL() + "&ecosid="
						+ GetECOSIDFromURL() + "&monthRange="
						+ Encode(TimeRange) + "&nurseGrowth="
						+ Encode("nurseGrowth") + "&print=" + Encode("print")
						+ "&selectFrom=" + Date1 + "&selectTo=" + Date2;
			} else if (CompareText(GetURLFrom, "ECOS-NURSEGROWTHDATA")
					&& FileName.endsWith("csv")) {
				Date1 = Encode("01 Nov 2007");
				Date2 = Encode("01 Jan 2013");
				Source = "/ecos/auxologicalReport/nurseGrowthData?id="
						+ GetPatientIDFromURL() + "&ecosid="
						+ GetECOSIDFromURL() + "&monthRange="
						+ Encode(TimeRange) + "&nurseGrowth="
						+ Encode("nurseGrowth") + "&export=" + Encode("export")
						+ FromKey + Date1 + ToKey + Date2;
			} else if (CompareText(GetURLFrom, "ECOS-PATIENTREPORT1")) {
				if (!CompareText(ValidorInvalidScenario, "INVALID")) {
					Source = "/ecos/patientReport/generatePDF/"
							+ GetPatientIDFromURL() + "?ecosid="
							+ GetECOSIDFromURL() + "&monthRange="
							+ Encode(TimeRange) + "&overview=" + Encode("true")
							+ "&injectionHistory=" + Encode("true") + "&list="
							+ Encode("true") + "&graph=" + Encode("true")
							+ "&calendar=" + Encode("true")
							+ "&injectionAdherence=" + Encode("true")
							+ "&doseDensity=" + Encode("true")
							+ "&generateReport=" + Encode("true")
							+ "&selectFrom=" + FromDate + "&selectTo=" + ToDate;
				} else {
					Date1 = Encode(Date_AddDays(GetTimeStamp("dd MMM yyyy"), 1,
							Calendar.DATE).replaceAll("-", " "));
					Source = "/ecos/patientReport/generatePDF/"
							+ GetPatientIDFromURL() + "?ecosid="
							+ GetECOSIDFromURL() + "&monthRange="
							+ Encode(TimeRange) + "&injectionHistory="
							+ Encode("true") + "&list=" + Encode("true")
							+ "&generateReport=" + Encode("true")
							+ "&selectFrom=" + Date1 + "&selectTo=" + Date1;
				}
			} else if (CompareText(GetURLFrom, "ECOS-PATIENTREPORT2")) {
				Source = "/ecos/patientReport/generatePDF/"
						+ GetPatientIDFromURL() + "?ecosid="
						+ GetECOSIDFromURL() + "&monthRange="
						+ Encode(TimeRange) + "&generateReport="
						+ Encode("true") + "&growthCurve=" + Encode("true")
						+ "&growthVelocityCurve=" + Encode("true")
						+ "&bodyMassIndex=" + Encode("true") + "&weightCurve="
						+ Encode("true") + "&doseAdherenceGrowthVelocity="
						+ Encode("true") + "&doseAdherenceGrowthCurve="
						+ Encode("true") + "&selectFrom=" + FromDate
						+ "&selectTo=" + ToDate;

				// ECKO ADMIN - PDF, EXCEL and CSV
			} else if (CompareText(GetURLFrom, "ECKO-EXPORTAUDITLOG")
					&& FileName.endsWith("pdf")) {
				Source = "/ecko/auditLog/exportToPdf?monthRange="
						+ Encode(TimeRange) + FromKey + FromDate + ToKey
						+ ToDate + "&exportAuditFilename=" + Encode("ecko");
			} else if (CompareText(GetURLFrom, "ECKO-EXPORTAUDITLOG")
					&& FileName.endsWith("csv")) {
				Source = "/ecko/auditLog/exportToCsv?monthRange="
						+ Encode(TimeRange) + FromKey + FromDate + ToKey
						+ ToDate + "&exportAuditFilename=" + Encode("ecko");
			} else if (CompareText(GetURLFrom, "ECKO-EXPORTAUDITLOG")
					&& FileName.endsWith("zip")) {
				Source = "/ecko/auditLog/exportToPdfAndCsv?monthRange="
						+ Encode(TimeRange) + FromKey + FromDate + ToKey
						+ ToDate + "&exportAuditFilename=" + Encode("ecko");

				// ECOS ADMIN - PDF, EXCEL and CSV

			} else if (CompareText(GetURLFrom, "ECOS-ADMIN-VIEWINVALIDDATA")) {
				Source = "/ecos/adminReport/invalidDataRecord?patientId="
						+ ObjectName + "&print=" + Encode("1");
			} else if (CompareText(GetURLFrom, "ECOS-ADMIN-SITESSTUDY")
					&& FileName.endsWith("pdf")) {
				Date1 = Encode(GetTimeStamp("dd MMM yyyy"));
				Source = "/ecos/adminReport/sitesInTheStudy?countryId="
						+ Encode("null") + "&monthRange=" + Encode(TimeRange)
						+ "&print=" + Encode("print") + ToKey + Date1 + FromKey
						+ GetFromDataForReports() + "&enrollmentReportType="
						+ Encode("1");
			} else if (CompareText(GetURLFrom, "ECOS-ADMIN-SITESSTUDY")
					&& FileName.endsWith("xls")) {
				Date1 = Encode(GetTimeStamp("dd MMM yyyy"));
				Source = "/ecos/adminReport/sitesInTheStudy?enrollmentReportType="
						+ Encode("1")
						+ "&monthRange="
						+ Encode(TimeRange)
						+ FromKey
						+ GetFromDataForReports()
						+ ToKey
						+ Date1
						+ "&countryId=77"
						+ Encode("+")
						+ "977"
						+ Encode("+")
						+ "9&range=&max=&print=&offset=&currentPage=&sortElement=&orderByValue=&handler=&pageReq=&isExport="
						+ Decode("dHJ1ZQ==");
			} else if (CompareText(GetURLFrom, "ECOS-ADMIN-PATIENTSENROLLED")
					&& FileName.endsWith("pdf")
					&& ValidorInvalidScenario.startsWith("VALID")) {
				Date1 = Encode(GetTimeStamp("dd MMM yyyy"));
				Source = "/ecos/adminReport/enrolledPatients?countryId="
						+ Encode("null") + ToKey + Date1 + "&monthRange="
						+ Encode(TimeRange) + FromKey + GetFromDataForReports()
						+ "&print=" + Encode("print") + "&clinicId="
						+ Encode("all") + "&enrollmentReportType="
						+ Encode("2");
			} else if (CompareText(GetURLFrom, "ECOS-ADMIN-PATIENTSENROLLED")
					&& FileName.endsWith("pdf")
					&& ValidorInvalidScenario.startsWith("INVALID")) {
				Source = "/ecos/adminReport/enrolledPatients?to="
						+ Encode("01 Jan 1993") + "&countryId="
						+ Encode("null") + "&monthRange=" + Encode(TimeRange)
						+ FromKey + Encode("01 Jan 1993") + "&clinicId="
						+ Encode("all") + "&print=" + Encode("print")
						+ "&enrollmentReportType=" + Encode("2");
			} else if (CompareText(GetURLFrom, "ECOS-ADMIN-PATIENTSENROLLED")
					&& FileName.endsWith("xls")) {
				Date1 = Encode(GetTimeStamp("dd MMM yyyy"));
				// Source =
				// "/ecos/adminReport/enrolledPatients?enrollmentReportType="+Encode("1")+"&monthRange="+Encode(TimeRange)+FromKey+GetFromDataForReports()+ToKey+Date1+"&countryId=77"+Encode("+")+"977"+Encode("+")+"9&range=&max=&print=&offset=&currentPage=&sortElement=&orderByValue=&handler=&pageReq=&isExport="+Decode("dHJ1ZQ==");
				Source = "/ecos/adminReport/enrolledPatients?enrollmentReportType="
						+ Encode("1")
						+ "&monthRange="
						+ Encode(TimeRange)
						+ FromKey
						+ GetFromDataForReports()
						+ ToKey
						+ Date1
						+ "&countryId=77"
						+ Encode("+")
						+ "977"
						+ Encode("+")
						+ "9&clinicId="
						+ Decode("dHJ1ZQ==")
						+ "&range=&max=&print=&offset=&currentPage=&sortElement=&orderByValue=&handler=&pageReq=&isExport="
						+ Decode("dHJ1ZQ==");
			} else if (CompareText(GetURLFrom, "ECOS-ADMIN-COMPLETEDPATIENTS")
					&& FileName.endsWith("pdf")
					&& ValidorInvalidScenario.startsWith("VALID")) {
				Date1 = Encode(GetTimeStamp("dd MMM yyyy"));
				Source = "/ecos/adminReport/enrolledPatients?countryId="
						+ Encode("null") + ToKey + Date1 + "&monthRange="
						+ Encode(TimeRange) + FromKey + GetFromDataForReports()
						+ "&print=" + Encode("print") + "&clinicId="
						+ Encode("all") + "&enrollmentReportType="
						+ Encode("3");
			} else if (CompareText(GetURLFrom, "ECOS-ADMIN-COMPLETEDPATIENTS")
					&& FileName.endsWith("pdf")
					&& ValidorInvalidScenario.startsWith("INVALID")) {
				Source = "/ecos/adminReport/enrolledPatients?countryId="
						+ Encode("null") + ToKey + Encode("01 Jan 1993")
						+ "&monthRange=" + Encode(TimeRange) + FromKey
						+ Encode("01 Jan 1993") + "&print=" + Encode("print")
						+ "&clinicId=" + Encode("all")
						+ "&enrollmentReportType=" + Encode("3");
			} else if (CompareText(GetURLFrom, "ECOS-ADMIN-COMPLETEDPATIENTS")
					&& FileName.endsWith("xls")) {
				Date1 = Encode(GetTimeStamp("dd MMM yyyy"));
				// Source =
				// "/ecos/adminReport/enrolledPatients?enrollmentReportType="+Encode("1")+"&monthRange="+Encode(TimeRange)+FromKey+GetFromDataForReports()+ToKey+Date1+"&countryId=77"+Encode("+")+"977"+Encode("+")+"9&range=&max=&print=&offset=&currentPage=&sortElement=&orderByValue=&handler=&pageReq=&isExport="+Decode("dHJ1ZQ==");
				Source = "/ecos/adminReport/enrolledPatients?enrollmentReportType="
						+ Encode("1")
						+ "&monthRange="
						+ Encode(TimeRange)
						+ FromKey
						+ GetFromDataForReports()
						+ ToKey
						+ Date1
						+ "&countryId=77"
						+ Encode("+")
						+ "977"
						+ Encode("+")
						+ "9&clinicId="
						+ Decode("dHJ1ZQ==")
						+ "&range=&max=&print=&offset=&currentPage=&sortElement=&orderByValue=&handler=&pageReq=&isExport="
						+ Decode("dHJ1ZQ==");
			} else if (CompareText(GetURLFrom,
					"ECOS-ADMIN-DISCONTINUEDPATIENTS")
					&& FileName.endsWith("pdf")
					&& ValidorInvalidScenario.startsWith("VALID")) {
				Date1 = Encode(GetTimeStamp("dd MMM yyyy"));
				Source = "/ecos/adminReport/enrolledPatients?countryId="
						+ Encode("null") + ToKey + Date1 + "&monthRange="
						+ Encode(TimeRange) + FromKey + GetFromDataForReports()
						+ "&print=" + Encode("print") + "&clinicId="
						+ Encode("all") + "&enrollmentReportType="
						+ Encode("4");
			} else if (CompareText(GetURLFrom,
					"ECOS-ADMIN-DISCONTINUEDPATIENTS")
					&& FileName.endsWith("pdf")
					&& ValidorInvalidScenario.startsWith("INVALID")) {
				Source = "/ecos/adminReport/enrolledPatients?countryId="
						+ Encode("null") + ToKey + Encode("01 Jan 1993")
						+ "&monthRange=" + Encode(TimeRange) + FromKey
						+ Encode("01 Jan 1993") + "&print=" + Encode("print")
						+ "&clinicId=" + Encode("all")
						+ "&enrollmentReportType=" + Encode("4");
			} else if (CompareText(GetURLFrom,
					"ECOS-ADMIN-DISCONTINUEDPATIENTS")
					&& FileName.endsWith("xls")) {
				Date1 = Encode(GetTimeStamp("dd MMM yyyy"));
				// Source =
				// "/ecos/adminReport/enrolledPatients?enrollmentReportType="+Encode("1")+"&monthRange="+Encode(TimeRange)+FromKey+GetFromDataForReports()+ToKey+Date1+"&countryId=77"+Encode("+")+"977"+Encode("+")+"9&range=&max=&print=&offset=&currentPage=&sortElement=&orderByValue=&handler=&pageReq=&isExport="+Decode("dHJ1ZQ==");
				Source = "/ecos/adminReport/enrolledPatients?enrollmentReportType="
						+ Encode("1")
						+ "&monthRange="
						+ Encode(TimeRange)
						+ FromKey
						+ GetFromDataForReports()
						+ ToKey
						+ Date1
						+ "&countryId=77"
						+ Encode("+")
						+ "977"
						+ Encode("+")
						+ "9&clinicId="
						+ Decode("dHJ1ZQ==")
						+ "&range=&max=&print=&offset=&currentPage=&sortElement=&orderByValue=&handler=&pageReq=&isExport="
						+ Decode("dHJ1ZQ==");
			} else if (CompareText(GetURLFrom, "ECOS-ADMIN-WORTHDATA")
					&& FileName.endsWith("pdf")
					&& ValidorInvalidScenario.startsWith("VALID")) {
				Source = "/ecos/adminReport/worthData?countryId="
						+ Encode("null") + "&print=" + Encode("print")
						+ "&clinicId=" + Encode("all")
						+ "&enrollmentReportType=" + Encode("5");
			} else if (CompareText(GetURLFrom, "ECOS-ADMIN-WORTHDATA")
					&& FileName.endsWith("pdf")
					&& ValidorInvalidScenario.startsWith("INVALID")) {
				Source = "/ecos/adminReport/worthData?countryId="
						+ Encode("null") + "&print=" + Encode("print")
						+ "&clinicId=" + ObjectName + "&enrollmentReportType="
						+ Encode("5");
			} else if (CompareText(GetURLFrom, "ECOS-ADMIN-WORTHDATA")
					&& FileName.endsWith("xls")) {
				Source = "/ecos/adminReport/worthData?enrollmentReportType="
						+ Encode("1")
						+ "&monthRange="
						+ Encode(TimeRange)
						+ FromKey
						+ GetFromDataForReports()
						+ ToKey
						+ Date1
						+ "&countryId=77"
						+ Encode("+")
						+ "977"
						+ Encode("+")
						+ "9&range=&max=&print=&offset=&currentPage=&sortElement=&orderByValue=&handler=&pageReq=&isExport="
						+ Decode("dHJ1ZQ==");

				// ECKO ADMIN - PDF, EXCEL and CSV
			} else if (CompareText(GetURLFrom, "ECKO-ADMIN-PAYERREPORT")
					&& FileName.endsWith("pdf")) {
				String PayerID = ObjectName.split("-")[0];
				String PatientCount = ObjectName.split("-")[1];
				String ClinicName = ObjectName.split("-")[2];
				Source = "/ecko/payer/generatePdf?payerId=" + PayerID
						+ "&overview=" + Encode("on") + "&drugWastage="
						+ Encode("on") + "&monthRange=" + Encode(TimeRange)
						+ ToKey + ToDate + FromKey + FromDate
						+ "&clinicId=&countryId="
						+ Encode(GetCountryID(Env.CountryUsed))
						+ "&clinicName=" + Encode(ClinicName)
						+ "&noOfPatients=" + Encode(PatientCount);
			}

			if (!Source.startsWith("http")) {
				URL AppURL = new URL(browser.getCurrentUrl());
				PDF.SourcePath = AppURL.getProtocol() + "://"
						+ AppURL.getAuthority() + Source;
			} else {
				PDF.SourcePath = Source;
			}
			if (PDF.DispatchFile()) {
				return PDF;
			} else {
				Env.logger.log(Level.ERROR, "Unable to download the file to "
						+ PDF.DestinationPath);
				Env.logger.log(Level.ERROR, "Unable to download the file "
						+ PDF.SourcePath);
				Report.Write("DownloadFile", "File should be downloaded",
						"Error occured while downloading " + FileName, "Fail",
						false);
				return null;
			}
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, "Unable to download the file to "
					+ PDF.DestinationPath);
			Env.logger.log(Level.ERROR, "Unable to download the file "
					+ PDF.SourcePath);
			Report.Write("DownloadFile", "File should be downloaded",
					"Error occured while downloading " + FileName, "Fail",
					false);
			return null;
		}
	}

	public boolean ShowTab(String tabnumber) {
		WaitForPageload();
		String TabXpath = "(" + ORXPath("#NavigationTab_ShowTab_IM") + ")["
				+ tabnumber + "]";
		if (GetAttributeValue(TabXpath, "class").contains("arrowRight")) {
			Lnk_Click(TabXpath);
			return true;
		} else {
			return false;
		}
	}

	public boolean CloseTab(String tabnumber) {
		WaitForPageload();
		String TabXpath = "(" + ORXPath("#NavigationTab_ShowTab_IM") + ")["
				+ tabnumber + "]";
		if (GetAttributeValue(TabXpath, "class").contains("arrowDown")) {
			Lnk_Click(TabXpath);
			return true;
		} else {
			return false;
		}
	}

	private String GetPatientIDFromURL() {
		String CurrentURL = GetCurrentURL();
		int indexofQuestionMark = CurrentURL.indexOf("?");
		if (indexofQuestionMark != -1) {
			int indexofSlash = CurrentURL.lastIndexOf("/", indexofQuestionMark);
			if (indexofSlash != -1) {
				return CurrentURL.substring(indexofSlash + 1,
						indexofQuestionMark);
			}
		}
		Env.logger.log(Level.ERROR, "PatientID is not found in " + CurrentURL);
		throw new RuntimeException("PatientID is not found in " + CurrentURL);

	}

	public String GetECOSIDFromURL() {
		String CurrentURL = GetCurrentURL();
		String SearchWord = "ecosid=";
		String ECOSID = "";
		int indexofEqualTo = CurrentURL.indexOf(SearchWord);
		if (indexofEqualTo != -1) {
			int indexofAmpersand = CurrentURL.indexOf("&", indexofEqualTo);
			if (indexofAmpersand != -1) {
				ECOSID = CurrentURL.substring(
						indexofEqualTo + SearchWord.length(), indexofAmpersand); // Lenght
																					// of
																					// ecosid=
																					// is
																					// 7
				return ECOSID;
			} else {
				ECOSID = CurrentURL.substring(
						indexofEqualTo + SearchWord.length(),
						CurrentURL.length()).trim(); // Lenght of ecosid= is 7
				if (!ECOSID.matches("[0-9]+")) {
					Env.logger.log(Level.INFO,
							"Check whether ECOSID is correct");
				}
				return ECOSID;
			}
		}
		Env.logger.log(Level.ERROR, "ECOSID is not found in " + CurrentURL);
		throw new RuntimeException("ECOSID is not found in " + CurrentURL);
	}

	private String GetPayerIDFromURL() {
		String CurrentURL = GetCurrentURL();
		int indexofID = CurrentURL.lastIndexOf("/");
		if (indexofID != -1) {
			return CurrentURL.substring(indexofID + 1, CurrentURL.length());
		}
		throw new RuntimeException("Payer ID is not found in " + CurrentURL);

	}

	public String GetStaticFilertID(int OptionIndex) {
		Select SelectObject = null;
		try {
			if (Env.AppModule.startsWith("ECKO")) {
				SelectObject = new Select(
						GetObject("AuxologicalReports_RangeGrowthCurve_DD"));
			} else {
				SelectObject = new Select(
						GetObject("#AuxologicalReports_RangeGrowthCurve_DD"));
			}
			List<WebElement> OptionList = SelectObject.getOptions();
			return OptionList.get(OptionIndex).getAttribute("value");
		} catch (Exception e) {
			throw new RuntimeException("Unable to get Static Filter ID", e);
		}
	}

	public String GetFromDate() {
		DateFormatSymbols DFS = new DateFormatSymbols();
		String Date = "01 " + DFS.getShortMonths()[RandomValue(0, 11)]
				+ " 2010";
		DFS = null;
		return Encode(Date);
	}

	public String GetToDate() {
		DateFormatSymbols DFS = new DateFormatSymbols();
		String Date = "01 " + DFS.getShortMonths()[RandomValue(3, 11)]
				+ " 2012"; // Partial is present only in 02-Mar-2012
		DFS = null;
		return Encode(Date);
	}

	private String GetFromDataForReports() {
		return Encode("01 Jan "
				+ String.valueOf(Integer.valueOf(GetTimeStamp("yyyy")) - 1));
	}

	public boolean AddPatient(boolean RenameExistingPatient,
			String PatientName, String Gender, String DOB,
			boolean ConsetnYesorNo, String ListOfStudies,
			boolean ContactDetails, boolean CarerDetails) {
		String FirstName = PatientName.split(" ")[1];
		String LastName = PatientName.split(" ")[0];
		String RandomValue = GetTimeStamp("hhmmss");
		ListOfStudies = ListOfStudies.toUpperCase();
		Gender = Gender.toUpperCase();
		int MaxCount;

		if (RenameExistingPatient) {
			try {
				SearchForPatient(PatientName);
				Wait(2);
				Btn_Click("MyPatients_ViewProfile_LK");
				if (!ObjectExists("ViewProfile_PDEdit_LK")) {
					Btn_Click("MyPatients_ViewProfile_LK");
				}
				Lnk_Click("ViewProfile_PDEdit_LK");
				Edit_SetValue("EditPatient_FirstName_EB", RandomValue);
				Edit_SetValue("EditPatient_PDLastName_EB", RandomString()
						.replaceAll("x", "y").replaceAll("X", "Y")); // Renamed
																		// patients
																		// will
																		// not
																		// have
																		// x
				if (ObjectExists("AddPatient_NHS_EB")) {
					if (Env.CountryUsed.trim().toUpperCase() != "UNITED KINGDOM") {
						Edit_SetValueByJavaScript("AddPatient_NHS_EB", "");
					} else {
						Edit_SetValueByJavaScript("AddPatient_NHS_EB",
								GenerateUniqueID());
					}
				}
				Btn_Click("EditPatient_Submit_BN");

				// Update payer name for patient 04
				if (PatientName.startsWith(TestData("PatientName") + " 04")) {
					Lnk_Click("ViewProfile_PayerDetailsEdit_LK");
					DrpDwn_SelectText(
							"EditPatient_TDPayername_DD",
							TestData("PayerName")
									+ GetCountryCode(Env.CountryUsed, true)); // Update
																				// PayerName
					Btn_Click("EditPatient_Submit_BN");
				}

				NavigateTo(Env.ECKOURL);
			} catch (Exception e) {
				Env.logger.log(Level.INFO, FormatPatientName(PatientName)
						+ " is not present, creating new patient");
				// Patient is not there, so will create a new one.
			}
		}

		// Click Add Patient Button, only when there is no patient with the
		// given name
		Btn_Click("MyPatients_AddPatientButton_BN");

		// Patient Details
		if (Gender.compareTo("MALE") == 0) { // Select Gender and Title
			DrpDwn_SelectIndex("AddPatient_Title_DD", 1);
			Rbtn_SetOption("AddPatient_GenderMale_RB", "ON"); // Male
		} else {
			DrpDwn_SelectIndex("AddPatient_Title_DD", RandomValue(2, 3));
			Rbtn_SetOption("AddPatient_GenderFemale_RB", "ON"); // Female
		}
		Edit_SetValue("AddPatient_Firstname_EB", FirstName); // Enter FirstName
		Edit_SetValue("AddPatient_Lastname_EB", LastName); // Enter LastName
		SelectDate("AddPatient_DOB_IM", DOB); // Select Date Of Birth

		if (ConsetnYesorNo) { // Give Consent Date if true
			Rbtn_SetOption("AddPatient_ConsentYes_RB", "ON");// Say "YES" for
																// Consent
			SelectDate(ORXPath("AddPatient_ConsentDate_IM"),
					Date_AddDays(DOB, 1, Calendar.DATE)); // click on the Date
															// picker for
															// consent
		} else {
			Rbtn_SetOption("AddPatient_ConsentNo_RB", "ON");
		}

		// Select Native Language
		DrpDwn_SelectIndex("AddPatient_NativeLanguage_DD", RandomValue(1, 17));
		if (ObjectExists("AddPatient_NHS_EB")) {
			if (Env.CountryUsed.trim().toUpperCase()
					.startsWith("UNITED KINGDOM")) {
				Edit_SetValue("AddPatient_NHS_EB", "");
			} else {
				Edit_SetValue("AddPatient_NHS_EB", GenerateUniqueID());
			}
		}

		// Select Ethnicity and Growth Hormone values.
		if (ObjectExists("AddPatient_Ethnicity_ST")) {
			MaxCount = DrpDwn_GetCount("AddPatient_Ethnicity_DD");
			DrpDwn_SelectIndex("AddPatient_Ethnicity_DD",
					RandomValue(1, MaxCount - 1));
		}
		if (ObjectExists("AddPatient_GrowthHormoneIndication_ST")) {
			MaxCount = DrpDwn_GetCount("AddPatient_GrowthHormoneIndication_DD");
			DrpDwn_SelectIndex("AddPatient_GrowthHormoneIndication_DD",
					RandomValue(1, MaxCount - 1));
		}

		// Treatment Details
		SelectDate("AddPatient_TreatmentStartDate_IM",
				Date_AddDays(DOB, 1, Calendar.DATE)); // Select TSD

		// //select HCP name
		// String[] LoggedInUser = GetText("Header_LoggedInUser_ST").split(" ");
		// DrpDwn_SelectText("AddPatient_HCPName_DD", LoggedInUser[1] + " " +
		// LoggedInUser[0]);

		// Set the Study for ECOS
		List<String> StudiesList = Arrays.asList(ListOfStudies.split("\\|"));
		for (int i = 0; i <= StudiesList.size() - 1; i++) {
			if (StudiesList.get(i).compareToIgnoreCase("ECOS") == 0) {
				// ChkBx_SetOption("AddPatient_StudiesECOS_CB", "ON");
				continue;
			}
			if (StudiesList.get(i).compareToIgnoreCase("PREDICT") == 0) {
				ChkBx_SetOption("AddPatient_StudiesPREDICT_CB", "ON");
				continue;
			}
			if (StudiesList.get(i).compareToIgnoreCase("IMMUNOGENICITY") == 0) {
				ChkBx_SetOption("AddPatient_StudiesIMMUNOGENCITY_CB", "ON");
				continue;
			}
		}

		if (ObjectExists("AddPatient_PayerName_DD")) {
			if (ObjectExists("AddPatient_PayerRegion_DD")) { // For Payer
																// Hierarchy
				DrpDwn_SelectIndex("AddPatient_PayerRegion_DD", 1);
				WaitForPageload();
				DrpDwn_SelectIndex("AddPatient_PayerProvince_DD", 1);
				WaitForPageload();
				DrpDwn_SelectIndex("AddPatient_PayerTown_DD", 1);
				WaitForPageload();
			}
			String PayerName = TestData("PayerName")
					+ GetCountryCode(Env.CountryUsed, true);
			if (PatientName.startsWith(TestData("PatientName") + " 04")) {
				PayerName = TestData("PayerName") + "Invalid"
						+ GetCountryCode(Env.CountryUsed, true);
			}
			DrpDwn_SelectText("AddPatient_PayerName_DD", PayerName); // Select
																		// the
																		// Payer
			Edit_SetValue("AddPatient_InsuranceID_EB",
					GetTimeStamp("ddMMMyyyy") + "INS" + RandomValue);// set
																		// insurance
																		// id
		}

		// Contact Details
		MaxCount = DrpDwn_GetCount("AddPatient_ContactCountryCode_DD");
		if (ContactDetails) {
			DrpDwn_SelectIndex("AddPatient_ContactCountryCode_DD",
					RandomValue(1, MaxCount - 1));
			Edit_SetValue("AddPatient_ContactMobileNumber_EB", RandomValue
					+ "5555");
			Edit_SetValue("AddPatient_ContactEmailAddress_EB", "Patient"
					+ RandomValue + "@S.com");
		}

		// Carer Details
		if (CarerDetails) {
			DrpDwn_SelectIndex("AddPatient_CarerCountryCode_DD",
					RandomValue(1, MaxCount - 1));
			Edit_SetValue("AddPatient_CarerMobileNumber_EB", RandomValue
					+ "5555");
			Edit_SetValue("AddPatient_CareerEmailAddress_EB", "Patient"
					+ RandomValue + "@S.com");
		}

		Report.Write("BeforeSaving", "Details before saving.",
				"Details before saving.", "Pass", true);

		// click submit
		Btn_Click("AddPatient_Submit_BN");

		// Validate for the Success message
		return CompareText("AddPatient_Successmessage_DT",
				LangData("PatientAdded") + " : "
						+ FormatPatientName(PatientName) + " "
						+ LangData("ViewProfile") + " | "
						+ LangData("AssignDevice"),
				GetText("AddPatient_Successmessage_DT"));

	}

	public void AddECKOvisit(String VisitDate, double Height, double Weight) {
		// Check whether the count is incremented
		String RecordText = GetText("Visits_VisitOfRecords_ST");
		List<String> RecordList = Arrays.asList(RecordText.split(" "));
		String InitialValue = RecordList.get(2);
		String NewValue = "0";

		// Click on Add Visit/Remarks Button
		Btn_Click("Visits_AddVisitsRemarks_BN");
		WaitForObject("EditPatient_PDCalendar_IM");
		SelectDate("EditPatient_PDCalendar_IM", VisitDate);
		Edit_SetValue("Visits_AddHeight_EB", String.valueOf(Height));
		Edit_SetValue("Visits_AddWeight_EB", String.valueOf(Weight));
		Report.Write("BeforeSaving", "Details before saving.",
				"Details before saving.", "Pass", true);
		Btn_Click("Visits_Save_BN");
		if (ObjectExists("Visits_Save_BN")) {
			Btn_Click("Visits_Save_BN");
		}

		try {
			RecordText = GetText("Visits_VisitOfRecords_ST");
			RecordList = Arrays.asList(RecordText.split(" "));
			NewValue = RecordList.get(2);
		} catch (Exception ignore) {
		}

		if (Integer.valueOf(InitialValue) + 1 == Integer.valueOf(NewValue)) {
			Report.Write("AddVisit", "Visit should be Added", "Visit is Added",
					"Pass", true);
		} else {
			Report.Write("AddVisit", "Visit should be Added",
					"Visit is not  Added", "Fail", true);
		}
	}

	public void EnrollPatient(String PatientName, String ConsentDate,
			boolean IsConsentOnly) {

		if (IsSupportForECOSAvailable(Env.CountryUsed)) {
			ECOSStudy ECOS = null;
			try {
				NavigateTo(Env.ECKOURL);
				SearchForPatient(PatientName);
				String Studies = WbTbl_GetText(
						"MyPatients_MyPatientsColumn_TH", 1, 9);
				if (!Studies.contains("ECOS")) {

					String AttributeValue = GetAttributeValue(
							"General_FirstRadioButton_RB", "value");
					String ConsentPageURL = Env.ECOSURL
							+ "ecosConsent/enrol?id=" + AttributeValue; // Env.ECOSURL+"ecosConsent/enrol/ODgzNjIy
					ECOS = new ECOSStudy(this);
					NavigateTo(ConsentPageURL);

					if (ObjectExists("#EnrollPatient_AssentYes_RB")) {

						Rbtn_SetOption("#EnrollPatient_AssentYes_RB", "ON");
						SelectDate("#EnrollPatient_ConsentDate_IM", ConsentDate);

						Rbtn_SetOption("#EnrollPatient_ConsentRoleYes_RB", "ON");
						SelectDate("#EnrollPatient_ConsentDateEdit_IM",
								ConsentDate);
						Btn_Click("#EnrollPatient_ConsentSave_BN");

						Rbtn_SetOption("#EnrollPatient_ConsentRoleYes1_RB",
								"ON");
						SelectDate("#EnrollPatient_ConsentDateEdit1_IM",
								ConsentDate);
						Btn_Click("#EnrollPatient_ConsentSave_BN");

						Rbtn_SetOption("#EnrollPatient_ConsentRoleYes2_RB",
								"ON");
						SelectDate("#EnrollPatient_ConsentDateEdit2_IM",
								ConsentDate);
						Btn_Click("#EnrollPatient_ConsentSave_BN");

						Rbtn_SetOption("#EnrollPatient_ConsentRoleYes3_RB",
								"ON");
						SelectDate("#EnrollPatient_ConsentDateEdit3_IM",
								ConsentDate);
						Btn_Click("#EnrollPatient_ConsentSave_BN");

						if (!IsConsentOnly) {
							ShowTab("2");

							// Selecting Yes radio button for inclusion fields
							// and No Radio button foe exclusion fields
							ECOS.InculsionExclusionAdd();

							Btn_Click("#EnrollPatient_InclusionSave_BN");

							// Check for success message that added to study
							VerifyText("#EnrollPatient_ParticipationStudyCriteria_DT");
						}
					} else {
						Env.logger
								.log(Level.ERROR,
										"ECOS Patient Enrollment page is not displayed");
					}
				} else {
					Env.logger.log(Level.ERROR,
							"Patient is already in the enrolled list.");
				}
			} catch (Exception e) {
				Env.logger.log(Level.ERROR, "Enroll Patient Failed");
			}
		} else {
			Env.logger.log(Level.ERROR,
					"Patient is not enrolled because it is a non-ECOS country");
		}
	}

	public void UpdateHCPAccountSettings() {
		try {
			// Navigate to MyAccount Page
			Lnk_Click("Header_MyAccount_LK");

			// Set Default HCP Subscriptions
			Btn_Click("MyAccount_DefaultSubscriptionsEdit_LK");
			WaitForObject("MyAccount_SendSmsReminder_CB");
			ChkBx_SetOption("MyAccount_SendSmsReminder_CB", "ON");
			ChkBx_SetOption("MyAccount_EmailReminder_CB", "ON");
			Btn_Click("MyAccount_CalculationDurationEditSave_ST");
			CompareText("MyAccount_ChangesSaved_DT",
					LangData("SubscriptionsDetailsUpdated"),
					GetText("MyAccount_ChangesSaved_DT"));
			Lnk_Click("NavigationTab_PatientsTab_ST");
		} catch (Exception ignore) {
			Report.Write("UpdateMyAccount", "My Account should be updated",
					"My Account is not Updated", "Fail", true);
		}
	}

	public void ReminderSettings() {
		try {

			// Reminders > Sent Reminders
			Lnk_Click("NavigationTab_RemindersTab_LK");
			Lnk_Click("ReminderSettings_History_LK");

			// Click on Send Reminder button
			Btn_Click("ReminderSettings_SendAlertNow_BN"); // For the value
															// Upload
			WaitForPageload();

			// Check for Message
			CompareText("ReminderSetting_SuccessMsg_DT",
					LangData("ReminderMessageSent"),
					GetText("ReminderSetting_SuccessMsg_DT"));

		} catch (Exception e) {
			Report.Write("ReminderSettings",
					"Reminder settings should be successful",
					"Reminder settings is not Updated", "Fail", true);
		}
	}

	public void AddECOSVisitRecord(boolean IsValid, String PatientName,
			String PatientDOB, String VisitDate, boolean AuxologicalDataVisits,
			boolean SocioEconomicVisits, boolean CurrentGH,
			boolean TannerStage, boolean BoneAgeCaps, boolean IGF1,
			boolean ConcomitantMedication, boolean BiochemistryIGF1,
			String ParamForTannerStage) {

		if (ObjectExists("#AddVisit_SelectVisitRecord_BN")) {
			Btn_Click("#AddVisit_SelectVisitRecord_BN");
		} else {
			NavigateTo(Env.ECOSURL);
			// Navigate to Add Visit Page
			SearchForPatient(PatientName);
			Rbtn_SetOption("#MyPatients_ECOSPatient_RB", "ON");
			Btn_Click("#MyPatients_VisitsRecord_BN");
		}

		if (WbTbl_SelectRbtn("#AddVisit_VisitDate_TE", 2,
				VisitDate.replaceAll("-", " "))) {
			Btn_Click("#AddVisit_Continue_BN"); // If the visit is already
												// present, then edit the
												// record.
		} else {
			SelectDate("#AddVisit_VisitDate_IM", VisitDate);// If the visit is
															// not present, then
															// Add a visit with
															// the given visit
															// date
			Btn_Click("#AddVisit_AddVisitDate_BN");
			CompareText("#AddVisit_AddVisitSuccess_DT",
					LangData("VisitDateSuccessUpdate"),
					GetText("#AddVisit_AddVisitSuccess_DT"));
		}

		if (CompareText(Env.CurrentTestName, "AAA_000_PreRequiste")) {
			return; // As of now, just add visit and add auxological data
		}

		// Create ECOS Object
		ECOSStudy ECOS = new ECOSStudy(this);
		String VisitsSupportedChaptersList = GetSupportedVisitChapters();
		String TabIndex = "";
		try {
			// Add the records for each tab
			TabIndex = ECOS.GetChapterIndex(VisitsSupportedChaptersList,
					LangData("AuxologicalDataVisits"));
			if (AuxologicalDataVisits && !CompareText(TabIndex, "-1"))
				ECOS.AuxologicalDataVisitsAdd(IsValid, TabIndex);

			TabIndex = ECOS.GetChapterIndex(VisitsSupportedChaptersList,
					LangData("SocioEconomicInformation"));
			if (SocioEconomicVisits && !CompareText(TabIndex, "-1"))
				ECOS.SocioEconomicVisitsAdd(IsValid, TabIndex);

			TabIndex = ECOS.GetChapterIndex(VisitsSupportedChaptersList,
					LangData("CurrentGHTreatment"));
			if (CurrentGH && !CompareText(TabIndex, "-1"))
				ECOS.CurrentGHAdd(IsValid, TabIndex, PatientDOB);

			TabIndex = ECOS.GetChapterIndex(VisitsSupportedChaptersList,
					LangData("BoneAgeCaps"));
			if (BoneAgeCaps && !CompareText(TabIndex, "-1"))
				ECOS.BoneAgeAdd(IsValid, TabIndex);

			TabIndex = ECOS.GetChapterIndex(VisitsSupportedChaptersList,
					LangData("IGF1"));
			if (IGF1 && !CompareText(TabIndex, "-1"))
				ECOS.IGF1Add(IsValid, TabIndex);

			TabIndex = ECOS.GetChapterIndex(VisitsSupportedChaptersList,
					LangData("BiochemistryIGF1"));
			if (BiochemistryIGF1 && !CompareText(TabIndex, "-1"))
				ECOS.IGF1Add(IsValid, TabIndex);

			TabIndex = ECOS.GetChapterIndex(VisitsSupportedChaptersList,
					LangData("ConcomitantMedication"));
			if (ConcomitantMedication && !CompareText(TabIndex, "-1"))
				ECOS.ConcomitantMedicationAdd(IsValid, TabIndex);

			TabIndex = ECOS.GetChapterIndex(VisitsSupportedChaptersList,
					LangData("TannerStage"));// Param1,2 and 3 are for
												// IsNotavailable,Gender and
												// TannerMenarche given in
												// method AddECOSVisitRecord
			if (TannerStage && !CompareText(TabIndex, "-1")) {
				boolean Param1;
				String Param2;
				String Param3;
				try {
					List<String> TannerStageList = Arrays
							.asList(ParamForTannerStage.split("\\|"));
					Param1 = Boolean.parseBoolean(TannerStageList.get(0));
					Param2 = TannerStageList.get(1);
					Param3 = TannerStageList.get(2);

				} catch (Exception e) {
					Param1 = true;
					Param2 = "MALE";
					Param3 = "";
					Env.logger
							.log(Level.ERROR,
									"Invalid Parameters for TannerStage. Loading default values");
				}
				ECOS.TannerStageAdd(IsValid, TabIndex, PatientDOB, Param1,
						Param2, Param3, VisitDate);
			}

		} catch (Exception e) {
			Env.logger.log(Level.ERROR, "AddECOSVisitRecord", e);
		} finally {
			ECOS = null;
		}

	}

	public void AddECOSBaselineRecord(boolean IsValid, String PatientName,
			String PatientDOB, boolean DemographicData,
			boolean AuxologicalData, boolean PatientSupportProgram,
			boolean MedicalHistory, boolean GrowthHormone,
			boolean Socioeconomic, boolean Concomitant, boolean CurrentGH,
			boolean BiochemistryIGF1, boolean IGF1, boolean TannerStage,
			boolean BoneAgeCaps, String ParamForTannerStage) {
		if (!ObjectExists("#MyPatients_PatientsInfoTable_TH")) {
			Btn_Click("#NavigationTab_PatientsTab_LK");
		}
		// Select a patient and Navigate to Baseline Record
		SearchForPatient(PatientName);
		Rbtn_SetOption("#MyPatients_FirstPatient_RB", "ON");
		Btn_Click("#MyPatients_BaselineRecord_BN");

		if (CompareText(Env.CurrentTestName, "AAA_000_PreRequiste")) {
			return; // PreRequiste is updated for all countries.
		}

		// Create ECOS Object
		String SupportedBaselineChapters = GetSupportedBaselineChapters();
		ECOSStudy ECOS = new ECOSStudy(this);
		String TabIndex = "";

		try {
			TabIndex = ECOS.GetChapterIndex(SupportedBaselineChapters,
					LangData("DemographicData"));
			if (DemographicData && !CompareText(TabIndex, "-1")) {
				ECOS.DemographicDataAdd(IsValid, TabIndex);
			}

			TabIndex = ECOS.GetChapterIndex(SupportedBaselineChapters,
					LangData("AuxologicalDataBaseline"));
			if (AuxologicalData && !CompareText(TabIndex, "-1")) {
				ECOS.AuxologicalDataAdd(IsValid, TabIndex);
			}

			TabIndex = ECOS.GetChapterIndex(SupportedBaselineChapters,
					LangData("PatientSupportProgram"));
			if (PatientSupportProgram && !CompareText(TabIndex, "-1")) {
				ECOS.PatientSupportProgramAdd(IsValid, TabIndex, PatientDOB);
			}

			TabIndex = ECOS.GetChapterIndex(SupportedBaselineChapters,
					LangData("MedicalHistory"));
			if (MedicalHistory && !CompareText(TabIndex, "-1")) {
				ECOS.MedicalHistoryAdd(IsValid, TabIndex);
			}

			TabIndex = ECOS.GetChapterIndex(SupportedBaselineChapters,
					LangData("GrowthHormoneHistory"));
			if (GrowthHormone && !CompareText(TabIndex, "-1")) {
				ECOS.GrowthHormoneAdd(IsValid, TabIndex, PatientDOB);
			}

			TabIndex = ECOS.GetChapterIndex(SupportedBaselineChapters,
					LangData("SocioEconomicInformation"));
			if (Socioeconomic && !CompareText(TabIndex, "-1")) {
				ECOS.SocioeconomicAdd(IsValid, TabIndex);
			}
			TabIndex = ECOS.GetChapterIndex(SupportedBaselineChapters,
					LangData("ConcomitantMedication"));
			if (Socioeconomic && !CompareText(TabIndex, "-1")) {
				ECOS.ConcomitantBaselineAdd(IsValid, TabIndex);
			}
			TabIndex = ECOS.GetChapterIndex(SupportedBaselineChapters,
					LangData("CurrentGHTreatment"));
			if (CurrentGH && !CompareText(TabIndex, "-1")) {
				ECOS.CurrentGHBaselineAdd(IsValid, TabIndex, PatientDOB);
			}
			TabIndex = ECOS.GetChapterIndex(SupportedBaselineChapters,
					LangData("BiochemistryIGF1"));
			if (GrowthHormone && !CompareText(TabIndex, "-1")) {
				ECOS.IGF1BaselineAdd(IsValid, TabIndex, PatientDOB);
			}

			TabIndex = ECOS.GetChapterIndex(SupportedBaselineChapters,
					LangData("TannerStage"));
			if (TannerStage && !CompareText(TabIndex, "-1")) {

				boolean Param1;
				String Param2;
				String Param3;
				try {
					List<String> TannerStageList = Arrays
							.asList(ParamForTannerStage.split("\\|"));
					Param1 = Boolean.parseBoolean(TannerStageList.get(0));
					Param2 = TannerStageList.get(1);
					Param3 = TannerStageList.get(2);

				} catch (Exception e) {
					Param1 = true;
					Param2 = "MALE";
					Param3 = "";
					Env.logger
							.log(Level.ERROR,
									"Invalid Parameters for TannerStage. Loading default values");
				}
				ECOS.TannerStageBaselineAdd(IsValid, TabIndex, PatientDOB,
						Param1, Param2, Param3);
			}

			TabIndex = ECOS.GetChapterIndex(SupportedBaselineChapters,
					LangData("BoneAgeCaps"));
			if (BoneAgeCaps && !CompareText(TabIndex, "-1")) {
				ECOS.BoneAgeBasleineAdd(IsValid, TabIndex, PatientDOB);

				if (ObjectExists("#General_AddRecordSave_BN")) {
					SearchForPatient(PatientName);
					Rbtn_SetOption("#MyPatients_FirstPatient_RB", "ON");
					Btn_Click("#MyPatients_BaselineRecord_BN");
				}

			}
			TabIndex = ECOS.GetChapterIndex(SupportedBaselineChapters,
					LangData("IGF1"));
			if (IGF1 && !CompareText(TabIndex, "-1")) {
				ECOS.IGF1BaselineAdd(IsValid, TabIndex, PatientDOB);
			}

		} catch (Exception e) {
			Env.logger.log(Level.ERROR, "AddECOSBaselineRecord", e);
		} finally {
			ECOS = null;
		}

	}

	public boolean CreateClinic(String CountryName, String ClinicName,
			boolean IsActiveClinic, boolean IsPhraseRequired) {

		boolean ReturnValue = false;

		if (!ObjectExists("CountryList_Clinic_BN")) {
			browser.navigate().to(Env.ECKOURL);
		}

		// Search Country and click on Clinics button
		SearchForCountry(Env.CountryUsed);
		Rbtn_SetOption("CountryList_FirstRadio_RB", "ON");
		Btn_Click("CountryList_Clinic_BN");

		try {
			// Click add clinic button
			Btn_Click("ClinicList_AddClinic_BN");
			WaitForPageload();

			// Enter Clinic Details to be created
			Edit_SetValue("CreateClinic_ClinicName_EB", ClinicName);
			if (IsActiveClinic) {
				Rbtn_SetOption("CreateClinic_StatusActive_RB", "ON");
			} else {
				Rbtn_SetOption("CreateClinic_StatusInactive_RB", "ON");
			}
			if (IsActiveClinic && IsPhraseRequired) {
				Rbtn_SetOption("CreateClinic_PhraseStatusRequired_RB", "ON");
			} else {
				Rbtn_SetOption("CreateClinic_PhraseStatusNotRequired_RB", "ON");
			}
			Edit_SetValue("CreateClinic_ClinicStateName_EB",
					GetCountryCode(Env.CountryUsed, true));
			DrpDwn_SelectIndex("CreateClinic_TimeZone_DD", 1);// Always select
																// first
																// timezone
			Report.Write("BeforeSaving", "Details before saving.",
					"Details before saving.", "Pass", true);
			Btn_Click("CreateClinic_ClinicSubmit_BN");

			// Verify Success message
			ReturnValue = CompareText("CreateClinic_SuccessMessage_DT",
					LangData("Clinic") + " " + ClinicName + " created",
					GetText("CreateClinic_SuccessMessage_DT"));

		} catch (Exception e) {
			Env.logger.log(Level.ERROR,
					"Unable to create clinic " + ClinicName, e);
		}

		if (!ReturnValue) {
			Report.Write("CreateClinic", "Clinic: " + ClinicName
					+ " should be created", "Clinic " + ClinicName
					+ " is not created", "Fail", true);
		}

		return ReturnValue;
	}

	public boolean CreateHCP(String CountryName, String ClinicToSearch,
			String FirstName, String LastName, String Gender,
			boolean IsActiveHCP, boolean IsECOSAccessible, String EmailAddress,
			String NativeLanguage) {
		boolean ReturnValue = false;

		if (FirstName.endsWith(TestData("Index"))) {
			FirstName = FirstName.substring(0, FirstName.length() - 4);
		}

		// If the page is in Country List page
		if (ObjectExists("CountryList_Clinic_BN")) {
			SearchForCountry(CountryName);
			Rbtn_SetOption("CountryList_FirstRadio_RB", "ON");
			Btn_Click("CountryList_Clinic_BN");
		}

		// If the page is in Clinic List page
		if (ObjectExists("ClinicList_ViewHCP_BN")) {
			// Search For Clinic
			Edit_SetValue("ClinicList_SearchText_EB", ClinicToSearch);
			Btn_Click("ClinicList_Search_BN");
			Rbtn_SetOption("ClinicDetails_ClinicDetail_RB", "ON");
			Btn_Click("ClinicList_ViewHCP_BN");
		}

		String HCPUsername = FirstName + LastName;
		try {
			// Add HCP page
			Btn_Click("HCPList_AddHCP_BN");

			// Enter HCP Details to be created
			Edit_SetValue("CreateHCP_UserName_EB", HCPUsername);
			DrpDwn_SelectIndex("EditPatient_Title_DD", RandomValue(1, 3));
			Edit_SetValue("CreateHCP_Password_EB", HCPUsername);
			Edit_SetValue("CreateHCP_ConfirmPassword_EB", HCPUsername);
			Edit_SetValue("CreateHCP_FirstName_EB", FirstName);
			Edit_SetValue("CreateHCP_LastName_EB", LastName);
			DrpDwn_SelectIndex("CreateHCP_Role_DD", RandomValue(1, 3));
			if (IsActiveHCP) { // Active or Inactive
				Rbtn_SetOption("CreateHCP_StatusActive_RB", "ON");
				;
			} else {
				Rbtn_SetOption("CreateHCP_StatusInActive_RB", "ON");
			}
			if (IsECOSAccessible) { // ECOS allowed or NOT
				Rbtn_SetOption("CreateHCP_AccessECOSActive_RB", "ON");
			} else {
				Rbtn_SetOption("CreateHCP_AccessECOSInActive_RB", "ON");
			}
			if (NativeLanguage.trim().length() == 0) {
				DrpDwn_SelectIndex(
						"GlobalAdmin_NativeLanguage_DD",
						RandomValue(
								1,
								DrpDwn_GetCount("GlobalAdmin_NativeLanguage_DD") - 1));
			} else {
				DrpDwn_SelectText("GlobalAdmin_NativeLanguage_DD",
						NativeLanguage);
			}

			DrpDwn_SelectIndex(
					"CreateHCP_Countrycode_DD",
					RandomValue(1,
							DrpDwn_GetCount("CreateHCP_Countrycode_DD") - 1));
			Edit_SetValue("CreateHCP_Contactnumber_EB", GetTimeStamp("hhmmss")
					+ "5555");
			if (EmailAddress.trim().length() == 0) {
				Edit_SetValue("CreateHCP_Email_EB",
						("HCP" + LastName + "@s.com").toLowerCase());
			} else {
				Edit_SetValue("CreateHCP_Email_EB", EmailAddress);
			}
			Report.Write("BeforeSaving", "Details before saving.",
					"Details before saving.", "Pass", true);
			Btn_Click("CreateHCP_Submit_BN");

			// Verify success Message
			ReturnValue = CompareText("CreateHCP_SuccessMessage_DT",
					LangData("HCP") + " " + HCPUsername
							+ " successfully created",
					GetText("CreateClinic_SuccessMessage_DT"));

		} catch (Exception e) {
			Env.logger.log(Level.ERROR, "Unable to create HCP  " + HCPUsername,
					e);
		}

		if (!ReturnValue) {
			Report.Write("CreateHCP", "HCP: " + HCPUsername
					+ "should be created", "HCP " + HCPUsername
					+ " is not created", "Fail", true);
		}

		return ReturnValue;
	}

	public boolean ForcePasswordChange(String CurrentPassword,
			String NewPassword) {

		boolean ReturnValue = false;
		// Create new password
		if (ObjectExists("CreatePassword_Password_EB")) {
			Edit_SetValue("CreatePassword_Password_EB", CurrentPassword);
			Edit_SetValue("CreatePassword_NewPassword_EB", NewPassword);
			Edit_SetValue("CreatePassword_ConfirmPassword_EB", NewPassword);
			Btn_Click("CreatePassword_Submit_BN");
			ReturnValue = true;
		} else {
			Env.logger.log(Level.DEBUG,
					"Force Change Password Page, is not displayed.");
			ReturnValue = false;
		}
		return ReturnValue;
	}

	public boolean CreateSecurityPhrase() {
		String PhraseToSet = TestData("HCPSecurityPharse");
		boolean ReturnValue = false;
		// Create Security Phrase
		if (ObjectExists("CreatePhrase_ConfirmNewSecurityphrase_EB")) {
			Edit_SetValue("CreatePhrase_SecurityPhrase_EB", PhraseToSet);
			Edit_SetValue("CreatePhrase_ConfirmNewSecurityphrase_EB",
					PhraseToSet);
			Edit_SetValue("CreatePhrase_SecurityPhraseHint_EB", PhraseToSet);
			Btn_Click("CreatePhrase_Confirm_BN");
			ReturnValue = true;
		} else {
			Env.logger
					.log(Level.DEBUG,
							"Create Security Page, is not displayed. So trying with EnterSecurePharse method");
			try {
				EnterSecurePharse(PhraseToSet);
			} catch (RuntimeException e) {
				Lnk_Click("SecurityPhrase_ForgotPhrase_LK");
				EnterSecurePharse(GetOnlyMyText("ForgotPhrase_Hint_ST").trim());
			}
		}

		// Licence Agreement Page
		if (ObjectExists("LicenseAgreement_Accept_BN")) {
			ChkBx_SetOption("LicenseAgreement_IAccept_CB", "ON");
			Btn_Click("LicenseAgreement_Accept_BN");
			ReturnValue = true;
		}

		// Terms Of Use Page
		CloseTermsOfUse(true);

		// Features List Page
		CloseFeatureList(true);

		// Adherence Calculation Page
		if (ObjectExists("AdherenceCalculation_Submit_BN")) {
			if (Edit_GetValue("AdherenceCalculation_Duration_EB").trim()
					.isEmpty()) {
				Spin_SetValue("AdherenceCalculation_Duration_EB", 52);
			}
			Btn_Click("AdherenceCalculation_Submit_BN");
			ReturnValue = true;
		}

		if (ObjectExists("Login_Language_DD")) {
			Wait(2);
			ChangeLanguage(Env.LanguageUsed); // Change the language in the
												// footer
		}

		return ReturnValue;
	}

	public void GetPatientIDFromProfile(String ListOfPatientType) {

		boolean SearchingForFirstTime = true;
		String PatientJSNID = null;
		String PatientUID = null;
		String EnvValueForPatient = null;
		String PatientName = null;

		final List<String> PatientList = Arrays.asList(ListOfPatientType
				.split("\\|"));
		for (String PatientType : PatientList) {
			PatientName = TestData(PatientType);
			PatientType = PatientType.toUpperCase().trim();

			EnvValueForPatient = null;
			// Get the SearchValue and PatientName
			if (PatientType.startsWith("ECOSPATIENTFEMALE")) {
				EnvValueForPatient = Env.JsnId_ECOSPatientFemale;
			} else if (PatientType.startsWith("NONECOSPATIENT")) {
				EnvValueForPatient = Env.JsnId_NonECOSPatient;
			} else if (PatientType.startsWith("ECOSPATIENT")) {
				EnvValueForPatient = Env.JsnId_ECOSPatient;
			} else if (PatientType.startsWith("PATIENT01")) {
				EnvValueForPatient = Env.JsnId_Patient01;
				PatientName = TestData("PatientName") + " 01";
			} else if (PatientType.startsWith("PATIENT04")) {
				EnvValueForPatient = Env.JsnId_Patient04;
				PatientName = TestData("PatientName") + " 04";
			}

			if (EnvValueForPatient == null) {

				if (SearchingForFirstTime) {
					OpenBrowser(Env.ECKOURL);

					// Enter User name and password
					Login(TestData("HCPUsername"), TestData("HCPUsername"));

					// Enter Security Pharse
					EnterSecurePharse(TestData("HCPSecurityPharse"));

					SearchingForFirstTime = false;
				} else {
					NavigateTo(Env.ECKOURL);
				}

				// Search for the Patient and click view profile
				SearchForPatient(PatientName);

				// Select the patient
				Btn_Click("MyPatients_ViewProfile_LK");

				// Get the Patient ID Example: jsn:12345678
				PatientJSNID = GetText("EditPatient_PatientIDValue_ST");
				PatientUID = GetText("EditPatient_UniqueIDValue_ST");

				// Set the PatientID Found to the appropriate field
				if (PatientType.startsWith("ECOSPATIENTFEMALE")) {
					Env.JsnId_ECOSPatientFemale = PatientJSNID;
					Env.UniqueID_ECOSPatientFemale = PatientUID;
				} else if (PatientType.startsWith("NONECOSPATIENT")) {
					Env.JsnId_NonECOSPatient = PatientJSNID;
					Env.UniqueID_NonECOSPatient = PatientUID;
				} else if (PatientType.startsWith("ECOSPATIENT")) {
					Env.JsnId_ECOSPatient = PatientJSNID;
					Env.UniqueID_ECOSPatient = PatientUID;
				} else if (PatientType.startsWith("PATIENT01")) {
					Env.JsnId_Patient01 = PatientJSNID;
					Env.UniqueID_Patient01 = PatientUID;
				} else if (PatientType.startsWith("PATIENT04")) {
					Env.JsnId_Patient04 = PatientJSNID;
					Env.UniqueID_Patient04 = PatientUID;
				} else if (PatientType.startsWith("PATIENT05")) {
					Env.JsnId_Patient05 = PatientJSNID;
					Env.UniqueID_Patient05 = PatientUID;
				}

			}

		}

		if (!SearchingForFirstTime) {
			Logout();
			NavigateTo(Env.ECOSURL);
			if (ObjectExists("Header_Logout_LK")) {
				Logout();
			}
		}
	}

	public void OperaPopUpCheck() {
		OperaCheck = new PopUp("OperaPopUpHandling");
		Thread OperaPopUpHandler = new Thread(OperaCheck, "PopUpHandler");
		OperaPopUpHandler.start();
		try {
			// OperaPopUpHandler.join();
		} catch (Exception ignore) {
		}
	}

	@Override
	public void TakeStaticScreenshot(String ScreenShotName) {
		HideCursor();
		Env.StaticScreenshotName = UtilClass.GetScreenshot(ScreenShotName);
		// Usage: EP.TakeStaticScreenshot("test");
	}

	public String TranslateMonth(String DateToTranslate) {
		int MyMonth = 0;
		try {
			String DateSeperator = DateToTranslate.substring(2, 3);
			String d[] = DateToTranslate.split(DateSeperator);
			Date MyDate = (new SimpleDateFormat("dd" + DateSeperator + "MMM"
					+ DateSeperator + "yyyy")).parse(DateToTranslate);
			Calendar cal = Calendar.getInstance();
			cal.setTime(MyDate);
			MyMonth = cal.get(Calendar.MONTH) + 1;
			String DateToCompare = d[0] + " "
					+ LangData("MonthShort" + MyMonth) + " " + d[2];
			return DateToCompare;
		} catch (Exception e) {
			Env.logger
					.log(Level.ERROR, "DateToTranslate:" + DateToTranslate, e);
			return null;
		}

	}

	public void SwitchTo(String ApplicationModuleToSwitch) {
		ApplicationModuleToSwitch = ApplicationModuleToSwitch.toUpperCase();
		String CurrentURL = GetCurrentURL();
		if (ApplicationModuleToSwitch.startsWith("ECOS")
				&& CurrentURL.contains("/ecko/")) {
			Lnk_Click("Header_ECOS_LK");
		} else {
			if (CurrentURL.contains("/ecos/")) {
				Lnk_Click("#Header_ApplicationName_ST");
			}
		}
		Env.AppModule = ApplicationModuleToSwitch;
		CloseFeatureList(true);
	}

	public boolean VerifyMandatoryText(String ObjectName) {
		return CompareText(ObjectName, LangData("FieldsMarked") + " * "
				+ LangData("AreMandatory"), GetText(GetObject(ObjectName)));// Using
																			// GetText
																			// on
																			// an
																			// WebElement,
																			// to
																			// avoid
																			// TextCleanup
																			// as
																			// few
																			// strings
																			// are
																			// ending
																			// with
																			// *
	}

	public boolean CreateAdmin(String UserName, String AdminType, boolean Status) {
		AdminType = AdminType.toUpperCase();
		if (AdminType.startsWith("LOCAL")) {
			NavigateTo(Env.ECKOURL);
			Edit_SetValue("CountryList_Search_EB", Env.CountryUsed);
			Btn_Click("CountryList_Search_BN");
			Rbtn_SetOption("CountryList_SelectFirstRadio_RB", "ON");
			Btn_Click("CountryList_Admin_BN");
			Btn_Click("AdminList_AddAdmin_BN");
		} else {
			if (ObjectExists("CreateAdmin_AddAdmin_BN")) {
				Btn_Click("CreateAdmin_AddAdmin_BN");
			} else {
				NavigateTo(Env.ECKOURL);
				Btn_Click("CreateAdmin_GlobalAdmin_BN");
				Btn_Click("CreateAdmin_AddAdmin_BN");
			}
		}

		// title
		DrpDwn_SelectIndex("CreateAdmin_Title_DD", RandomValue(1, 3));

		// For Global Admin Types
		if (AdminType.toUpperCase().startsWith("GLOBALADMIN")) {
			DrpDwn_SelectIndex("CreateAdmin_AdminType_DD", 0);
		} else if (AdminType.toUpperCase().startsWith("DEVICEADMIN")) {
			DrpDwn_SelectIndex("CreateAdmin_AdminType_DD", 1);
		} else if (AdminType.toUpperCase().startsWith("GLOBALREPORTADMIN")) {
			DrpDwn_SelectIndex("CreateAdmin_AdminType_DD", 2);
		} else if (AdminType.toUpperCase().startsWith("SUPPORTADMIN")) {
			DrpDwn_SelectIndex("CreateAdmin_AdminType_DD", 3);
			// For Local Admin Types
		} else if (AdminType.toUpperCase().startsWith("LOCALADMIN")) {
			DrpDwn_SelectIndex("CreateAdmin_AdminType_DD", 0);
		} else if (AdminType.toUpperCase().startsWith("LOCALREPORTADMIN")) {
			DrpDwn_SelectIndex("CreateAdmin_AdminType_DD", 1);
		} else {
			DrpDwn_SelectIndex("CreateAdmin_AdminType_DD", 0);
		}

		Edit_SetValue("CreateAdmin_FirstName_EB",
				UserName.substring(0, UserName.length() - 4));
		Edit_SetValue("CreateAdmin_LastName_EB",
				UserName.substring(UserName.length() - 4));

		Edit_SetValue("CreateAdmin_UserName_EB", UserName);
		Edit_SetValue("CreateAdmin_Password_EB", UserName);
		Edit_SetValue("CreateAdmin_ConfirmPassword_EB", UserName);

		if (!Status) {
			Rbtn_SetOption("CreateAdmin_InActive_RB", "ON");
		} else {
			Rbtn_SetOption("CreateAdmin_Active_RB", "ON");
		}
		DrpDwn_SelectIndex(
				"CreateAdmin_CountryCode_DD",
				RandomValue(1,
						DrpDwn_GetCount("CreateAdmin_CountryCode_DD") - 1));

		Edit_SetValue("CreateAdmin_MobileNumber_EB", GetTimeStamp("hhmmss")
				+ "5555");
		Edit_SetValue("CreateAdmin_Email_EB", AdminType.toLowerCase()
				+ "@s.com");
		Report.Write("BeforeSaving", "Details before saving.",
				"Details before saving.", "Pass", true);
		Btn_Click("CreateAdmin_Submit_BN");
		return CompareText("AddAdmin_SuccessMsg_DT", LangData("Admin") + " "
				+ UserName + " " + LangData("SuccessfullyCreated"),
				GetText("AddAdmin_AddAdminSuccessMsg_DT"));
	}

	public String GetTannerValues() {
		String TannerValues = "";
		String CountryName = Env.CountryUsed.replace(" ", "").toUpperCase();
		Country switchCountryName = Country.valueOf(CountryName);
		switch (switchCountryName) {
		case UNITEDKINGDOM:
			TannerValues = LangData("PubertalAssessmentOption1") + "|"
					+ LangData("PubertalAssessmentOption2") + "|"
					+ LangData("PubertalAssessmentOption3") + "|"
					+ LangData("PubertalAssessmentOption4") + "|"
					+ LangData("PubertalAssessmentOption5") + "|"
					+ LangData("NotAvailable");
			break;
		// "T1;T2;T3;T4;T5;Not available"
		default:
			TannerValues = LangData("TannerFirst") + "|"
					+ LangData("TannerSecond") + "|" + LangData("TannerThird")
					+ "|" + LangData("TannerFourth") + "|"
					+ LangData("TannerFifth") + "|" + LangData("NotAvailable");
			break;
		// "Tanner 1;Tanner 2;Tanner 3;Tanner 4;Tanner 5;Not available";
		}

		return TannerValues;
	}

	public String GetDeviceID(String DeviceType) {

		Country switchCountryName = Country.valueOf(Env.CountryUsed.replaceAll(
				" ", "").toUpperCase());
		DeviceType = DeviceType.toUpperCase().trim();

		// String EP1Device = "There is no device ID for this OS";
		// //MS0002004423
		// String EP2Device = "There is no device ID for this OS";
		// //2282600271000197
		// String NewDevice = "There is no device ID for this OS";
		// //2114600271000180
		//
		if (DeviceType.startsWith("EP1DEVICE")) {
			return switchCountryName.GetEP1Device();
		}

		if (DeviceType.startsWith("EP2DEVICE")) {
			return switchCountryName.GetEP2Device();
		}

		if (DeviceType.startsWith("NEWDEVICE")) {
			return switchCountryName.GetNewDevice();
		}

		if (DeviceType.startsWith("EXTRA1EP2DEVICE")) {

			if (Env.CountryUsed.toUpperCase()
					.compareToIgnoreCase("SWITZERLAND") == 0) {// ECKO Admin
				return "2282600271000096";
			}

			if (Env.CountryUsed.toUpperCase().compareToIgnoreCase("SINGAPORE") == 0) {// ECOS
																						// Admin
				return "2282600271000097";
			}

			if (Env.CountryUsed.toUpperCase().compareToIgnoreCase("CANADA") == 0) {// ECKO
																					// HCP
				return "2282600271000098";
			}

			if (Env.CountryUsed.toUpperCase().compareToIgnoreCase("FRANCE") == 0) {// ECOS
																					// HCP
				return "2282600271000099";
			} else {
				return null;
			}

		}

		return "Invalid Device Type";
	}

	public boolean IsSupportForECOSAvailable(String CountryName) {
		Country switchCountryName = Country.valueOf(CountryName.replaceAll(" ",
				"").toUpperCase());
		return switchCountryName.SupportForECOS;
	}

	public void CloseFeatureList(boolean DoNotShowAgain) {
		if (ObjectExists("Login_FeatureList_CB")) {
			if (DoNotShowAgain) {
				ChkBx_SetOption("Login_FeatureList_CB", "ON");
			}
			Btn_Click("Login_FeatureListConinue_BN");
		}
	}

	public void CloseTermsOfUse(boolean AcceptTermsOfUse) {
		if (ObjectExists("TermsOfUse_IAccept_CB")) {
			if (AcceptTermsOfUse) {
				ChkBx_SetOption("TermsOfUse_IAccept_CB", "ON");
				Btn_Click("TermsOfUse_Accept_BN");
			} else {
				Btn_Click("TermsOfUse_Decline_BN");
			}
		}
	}

	public boolean UpdateStudyStatus(String PatientName, String StatusStudy) {
		NavigateTo(Env.ECOSURL);
		SearchForPatient(PatientName);
		Rbtn_SetOption("#MyPatients_FirstPatient_RB", "ON");
		Btn_Click("#MyPatients_BaselineRecord_BN");
		Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");
		StatusStudy = StatusStudy.toUpperCase();

		if (CompareText(StatusStudy, "COMPLETED")) {
			DrpDwn_SelectValue("#CompleteTermination_StatusOfStudy_DD",
					"Completed"); // Previously Index 1 was used
			DrpDwn_SelectIndex("#CompleteTermination_StudyCompletion_DD", 3); // Study
																				// Completion
																				// -End
																				// of
																				// study
																				// period
			DrpDwn_SelectIndex("#CompleteTermination_SubjectHeight_DD", 3); // Patient
																			// Height
																			// at
																			// End
																			// of
																			// Study
																			// -Not
																			// final
			SelectDate(
					"#CompleteTermination_ContinuedTreatmentStopDateCA_IM",
					Date_AddDays(GetTimeStamp("dd MMM yyyy"), -1, Calendar.DATE));
			HideCursor();
			Wait(2);
			SelectDate(
					"#CompleteTermination_LastStudyDayCalendar_TT",
					Date_AddDays(GetTimeStamp("dd MMM yyyy"), -1, Calendar.DATE));// Last
																					// visit
																					// date
																					// of
																					// Lucy
																					// Sue
																					// +
																					// 1.
			HideCursor();
			Wait(2);
			SelectDate(
					"#CompleteTermination_DateOFMeasurementCalendar_TT",
					Date_AddDays(GetTimeStamp("dd MMM yyyy"), -1, Calendar.DATE));
			HideCursor();
			Wait(2);
			Edit_SetValue("#CompleteTermination_MeasurementCompleted_EB", "100");
		} else {
			DrpDwn_SelectValue("#CompleteTermination_StatusOfStudy_DD",
					"Discontinued"); // Previously Index 2 was used
			DrpDwn_SelectIndex("#CompleteTermination_DiscontinuationReason_DD",
					5);// Premature Study Discontinuation Reason-Select Others
			DrpDwn_SelectIndex("#CompleteTermination_InitiatorOfWithdrawal_DD",
					5);// Initiator of withdrawal --Select Others
			Edit_SetValue("#CompleteTermination_InitiatorOfWithdrawal_EB",
					"Others");
			DrpDwn_SelectIndex(
					"#CompleteTermination_PatientHeightAtDiscontinuation_DD", 3);// Study
																					// Discontinuation
																					// -Not
																					// final
			SelectDate(
					"#CompleteTermination_DiscontinuedTreatmentStopDateCA_IM",
					Date_AddDays(GetTimeStamp("dd MMM yyyy"), -1, Calendar.DATE));
			HideCursor();
			Wait(2);
			Edit_SetValue("#CompleteTermination_Measurement_EB", "100");
			SelectDate(
					"#CompleteTermination_DiscontinuedLastStudyDayCalendar_TT",
					Date_AddDays(GetTimeStamp("dd MMM yyyy"), -1, Calendar.DATE));
			HideCursor();
			Wait(2);
		}
		Report.Write("BeforeSaving", "Details before saving.",
				"Details before saving.", "Pass", true);
		Btn_Click("#CompleteTermination_Save_BN");
		return CompareText(
				"#General_RecordsSuccessMsg_DT",
				LangData("DataUpdated").replace("{0}",
						LangData("CompletionOrTermination")),
				GetText("#General_RecordsSuccessMsg_DT"));
	}

	public boolean EmailMoveToVerified() {
		boolean flag = false;
		try {
			SwitchFrames("");
			Btn_ClickAndProceed("Email_Move_LK");
			GetObject("Email_MoveToVerifiedFolder_ST").click();
			SwitchWindows(1);
			RefreshPage();
			Env.logger.log(Level.INFO, "Email is Moved to Verified Folder");
			flag = true;
		} catch (Exception e) {
			Env.logger
					.log(Level.ERROR, "Email is not Moved to Verified Folder");
			flag = false;
		}
		return flag;
	}

	public boolean AddPayer(String PayerName) {
		String RandomValue = GetTimeStamp("hhmmss");
		boolean ReturnValue = false;

		if (!ObjectExists("PayerList_AddPayer_BN")) {
			NavigateTo(Env.ECKOURL);
			SearchForCountry(Env.CountryUsed);
			Rbtn_SetOption("CountryList_SelectFirstRadio_RB", "ON");
			Btn_Click("CountryList_Payer_BN");
		}

		// Search for the payer before adding
		Edit_SetValue("PayerList_Search_EB", PayerName);
		Btn_Click("PayerList_Search_BN");
		if (ObjectExists("CountryList_SelectFirstRadio_RB")) {
			Env.logger.log(Level.ERROR, "Payer: " + PayerName
					+ " already exists.");
			return false;
		}

		try {
			Btn_Click("PayerList_AddPayer_BN");
			Edit_SetValue("AddPayer_PayerID_EB", PayerName);
			Edit_SetValue("AddPayer_PayerName_EB", PayerName);
			Edit_SetValue("AddPayer_Address1_EB", LangData("AddressLine1")
					.replaceAll("-", "").trim() + RandomValue);
			Edit_SetValue("AddPayer_Address2_EB",
					RandomValue
							+ LangData("AddressLine2").replaceAll("-", "")
									.trim());
			Edit_SetValue("AddPayer_Address3_EB", LangData("AddressLine3")
					.replaceAll("-", "").trim() + RandomValue);
			DrpDwn_SelectIndex("AddPayer_Language_DD", 1);
			if (ObjectExists("AddPatient_PayerRegion_DD")) {
				DrpDwn_SelectIndex("AddPatient_PayerRegion_DD", 1);
				WaitForPageload();
				DrpDwn_SelectIndex("AddPatient_PayerProvince_DD", 1);
				WaitForPageload();
				DrpDwn_SelectIndex("AddPatient_PayerTown_DD", 1);
				WaitForPageload();
			}
			int MaxCount = DrpDwn_GetCount("AddPayer_CountryCode_DD");
			DrpDwn_SelectIndex("AddPayer_CountryCode_DD",
					RandomValue(1, MaxCount - 1));
			Edit_SetValue("AddPayer_ContactNumber_EB", RandomValue + "5555");
			Edit_SetValue("AddPayer_Email_EB",
					"Payer" + RandomValue + "@S.com".toLowerCase());
			Report.Write("BeforeSaving", "Details before saving.",
					"Details before saving.", "Pass", true);
			Btn_Click("AddPayer_Submit_BN");
			if (VerifyRegExp("AddPayer_SucessMessage_DT", PayerName)) {
				ReturnValue = true;
			}

		} catch (Exception e) {
			Env.logger.log(Level.ERROR, "Unable to create Payer  " + PayerName,
					e);
		}

		if (!ReturnValue) {
			Report.Write("CreatePayer", "Payer: " + PayerName
					+ "should be created", "HCP " + PayerName
					+ " is not created", "Fail", true);
		}

		return ReturnValue;
	}

	public void UpdateRepresentativeDetails() {
		String RandomValue = GetTimeStamp("hhmmss");
		String CountryCode = GetCountryCode(Env.CountryUsed, true);
		// Update the Representative Details
		Lnk_Click("CountrySettings_RepEdit_LK");
		WaitForObject("CountrySettings_RepFirstName_EB");
		while (ObjectExists("CountrySettings_RepContactRemove_LK")) {
			Btn_ClickAndProceed("CountrySettings_RepContactRemove_LK");
		}
		Edit_SetValue("CountrySettings_RepFirstName_EB", RandomString());
		Edit_SetValue("CountrySettings_RepLastName_EB", TestData("Index"));
		int MaxCount = DrpDwn_GetCount("CountrySettings_RepCountryCode_DD");
		DrpDwn_SelectIndex("CountrySettings_RepCountryCode_DD",
				RandomValue(1, MaxCount - 1));
		Edit_SetValue("CountrySettings_RepMobileNumber_EB", RandomValue
				+ "5555");
		Edit_SetValue("CountrySettings_RepEmail_EB", "Rep" + CountryCode
				+ RandomValue + "@S.com".toLowerCase());
		Edit_SetValue("CountrySettings_RepAddressLine1_EB",
				LangData("AddressLine1").replaceAll("-", "").trim()
						+ RandomValue);
		Edit_SetValue("CountrySettings_RepAddressLine2_EB", RandomValue
				+ LangData("AddressLine2").replaceAll("-", "").trim());
		Edit_SetValue("CountrySettings_RepAddressLine3_EB",
				LangData("AddressLine3").replaceAll("-", "").trim()
						+ RandomValue);
		Btn_Click("CountrySettings_RepEditSave_BN");
		CompareText("RepresentativeDetailsEdited",
				LangData("RepresentativeEditedSuccessfully"),
				GetText("CountrySettings_SuccessMessage_DT"));// Verify success
																// message

		// Add another Contact detail
		RandomValue = GetTimeStamp("hhmmss");
		Lnk_Click("CountrySettings_RepAddContact_LK");
		DrpDwn_SelectIndex("CountrySettings_RepAddContactCountryCode_DD",
				RandomValue(1, MaxCount - 1));
		Edit_SetValue("CountrySettings_RepAddContactMobileNumber_EB",
				RandomValue + "5555");
		Edit_SetValue("CountrySettings_RepAddContactEmail_EB", "Rep"
				+ CountryCode + RandomValue + "@S.com".toLowerCase());
		Btn_Click("CountrySettings_RepSave_BN");
		CompareText("RepresentativeDetailsAdded",
				LangData("RepresentativeAddedSuccessfully"),
				GetText("CountrySettings_SuccessMessage_DT"));// Verify success
																// message
	}

	public boolean UploadFooterHTMLs(String ForWhichLanguage) {
		String CountryCode = GetCountryCode(Env.CountryUsed, true);
		String LanguageCode = GetLanguageCode(Env.LanguageUsed, true);
		int MaxCount = 1;
		if (ForWhichLanguage.compareToIgnoreCase("ALL") == 0) {
			MaxCount = DrpDwn_GetCount("CountrySettings_ViewSections_DD");
		}

		// Upload the files for all languages in the drop down

		try {
			for (int i = 0; i < MaxCount; i++) {

				// Select the language in drop down
				if (ForWhichLanguage.compareToIgnoreCase("ALL") == 0) {
					DrpDwn_SelectIndex("CountrySettings_ViewSections_DD", i);
					LanguageCode = GetLanguageCode(
							DrpDwn_GetSelectedText("CountrySettings_ViewSections_DD"),
							true);
				} else {
					DrpDwn_SelectText("CountrySettings_ViewSections_DD",
							ForWhichLanguage);
					LanguageCode = GetLanguageCode(ForWhichLanguage, true);
				}
				WaitForPageload();

				// Upload Terms of Use
				UploadFooterFile(1, "HTML/TermsOfUse_" + LanguageCode + "_"
						+ CountryCode + ".html");

				// Upload License Agreement
				UploadFooterFile(2, "HTML/LicenseAgreement_" + LanguageCode
						+ "_" + CountryCode + ".html");

				// Study User Guide
				UploadFooterFile(3, "HTML/ECOS-IFU.pdf");

				// Upload IFU
				UploadFooterFile(4, "HTML/ECKO-IFU.pdf");

				// Upload Distributor
				UploadFooterFile(5, "HTML/Distributor.pdf");

				// Upload Left footer
				UploadFooterFile(6, "IMG/Footer_Logo_Left_" + CountryCode
						+ ".gif");

				// Upload right footer
				UploadFooterFile(7, "IMG/Footer_Logo_Right_" + CountryCode
						+ ".gif");

				// Upload ECKO FAQ
				UploadFooterFile(8, "HTML/ECKO-FAQ.pdf");

				// Upload ECOS FAQ
				UploadFooterFile(9, "HTML/ECKO-FAQ.pdf");

				// Upload Admin FAQ
				UploadFooterFile(10, "HTML/ECKO-FAQ.pdf");

				// Upload Admin IFU
				UploadFooterFile(11, "HTML/ECKO-IFU.pdf");

				// Upload Features List
				UploadFooterFile(12, "HTML/FeaturesHCP_" + LanguageCode + "_"
						+ CountryCode + ".html");

			} // End the For loop
			Env.logger.log(Level.INFO, "Footer files  uploaded successfully");
			return true;
		} catch (Exception e) {
			Env.logger.log(Level.ERROR,
					"Footer files not uploaded successfully", e);
			return false;
		}
	}

	public void UpdateDefaultLanguage() {
		// Click the Language & Footer link
		Lnk_Click("Navigation_LangFooter_LK");
		WaitForPageload();
		// click Edit link for Footer Details
		Lnk_Click("CountrySettings_FooterEdit_LK");

		// Select the Participate value to yes
		Rbtn_SetOption("CreateAdmin_Active_RB", "ON");

		while (ObjectExists("CountrySettings_LanguageRemove_LK")) {
			Btn_ClickAndProceed("CountrySettings_LanguageRemove_LK");
		}

		DrpDwn_SelectText("CountrySettings_DefaultLanguage_DD", "English");
		Btn_Click("CountrySettings_FooterSave_BN");

		String SupportedLanguages = GetSupportedLanguages(Env.CountryUsed);
		List<String> LangList = Arrays.asList(SupportedLanguages.split("\\|"));
		String DefaultLanguage = "English";
		for (int i = 0; i < LangList.size(); i++) {

			if (!LangList.get(i).startsWith("English")) {
				Lnk_Click("CountrySettings_AddLanguage_LK");
				DrpDwn_SelectText("CountrySettings_AddLanguage_DD",
						LangList.get(i));
				Btn_Click("CountrySettings_SaveAddLanguage_BN");
			}

			if (i == 1) {
				DefaultLanguage = LangList.get(i);
			}

		}

		if (!DefaultLanguage.startsWith("English")) {
			Lnk_Click("CountrySettings_FooterEdit_LK");
			DrpDwn_SelectText("CountrySettings_DefaultLanguage_DD",
					DefaultLanguage);
			DrpDwn_SelectText("//option[@selected and contains(text(),'"
					+ DefaultLanguage + "')]/..", "English");
			Btn_Click("CountrySettings_FooterSave_BN");
		}

	}

	public boolean FileUpload_SetFile(String ObjectName, String FilePath) {
		String CountryCode = GetCountryCode(Env.CountryUsed, true);
		File UploadFile;
		try {
			UploadFile = new File(FilePath);
			if (!UploadFile.exists()) {
				if (FilePath.endsWith(".xml")) {
					FilePath = FilePath.replaceAll(CountryCode.toLowerCase()
							+ ".xml", "uk.xml");
				} else if (FilePath.endsWith(".gif")) {
					FilePath = FilePath.replaceAll(CountryCode + ".gif",
							"GB.gif");
				} else if (FilePath.endsWith(".html")) {
					FilePath = FilePath.replaceAll("_[a-z]{2}_[A-Z]{2}.html",
							"_en_GB.html"); // Replace _gr_DE.html to
											// _en_GB.html
				}
			}
			UploadFile = null;
			return super.FileUpload_SetFile(ObjectName, FilePath);
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, ObjectName, e);
			return false;
		} finally {
			UploadFile = null;
		}
	}

	public Locale GetLocale() {
		String CountryCode = GetCountryCode(Env.CountryUsed, true)
				.toUpperCase();
		String LanguageCode = GetLanguageCode(Env.LanguageUsed, true);
		return new Locale(LanguageCode, CountryCode);
	}

	public void GetBuildNumber() {
		if (Env.BuildNumber == null) {

			if (!Env.IsRunningFromDriver) {
				Env.BuildNumber = "N/A";
				return;
			}

			String BuilNumberXPath = "//div[@class='middleFooter']/span";
			// Log the message
			Env.logger.log(Level.INFO, "Getting Build Number");

			// if the Object does not Exists, then sign out and login as admin
			if (!ObjectExists(BuilNumberXPath)) {
				// Logout
				NavigateTo(GetLogoutURL());
				// Login as Global Admin
				if (!Env.CurrentTestName.startsWith("MIG")) {
					Edit_SetValue("Login_Username_EB", TestData("GlobalAdmin"));
					Edit_SetValue("Login_Password_EB", TestData("GlobalAdmin"));
				} else {
					Edit_SetValue("Login_Username_EB", Env.MIGAdminUser);
					Edit_SetValue("Login_Password_EB", Env.MIGAdminPassword);
				}
				Btn_Click("Login_Login_BN");
				Wait(2);
				WaitForPageload();

				// Close the feature list if it exists
				CloseFeatureList(true);
			}

			// Get the Build Number
			String BuildNo = GetText(BuilNumberXPath);
			BuildNo = BuildNo.replace("Build: ", "");
			Env.BuildNumber = BuildNo;

			// //Logout
			// Btn_Click("Header_Logout_LK");
			// Btn_Click("SignOut_Yes_BN");
		}
	}

	public String GetWeekdaysOrder(String ObjectName) {
		List<String> Expected = Arrays.asList(ObjectName.split("\\|"));
		if (IsWeekStartingMonday()) { // If monday, then swap the elements.
			// Order of the Text changes for United Kingdom (Convert
			// "Sun;Mon;Tue;Wed;Thu;Fri;Sat;Adherence" to
			// "Mon;Tue;Wed;Thu;Fri;Sat;Sun;Adherence")
			Collections.rotate(Expected, -1); // Move every element by one place
		}
		return StringUtils.join(Expected, "|");
	}

	public boolean IsWeekStartingMonday() {
		boolean ReturnValue = false;
		Locale CurrentLocale = GetLocale();
		Calendar Currentcalendar = Calendar.getInstance(CurrentLocale);
		if (Currentcalendar.getFirstDayOfWeek() == 2) { // If monday, then swap
														// the elements.
			ReturnValue = true;
		}
		CurrentLocale = null;
		Currentcalendar = null;
		return ReturnValue;
	}

	@Deprecated
	public void SetHCPActive(String CountryName, String ClinicName,
			String HCPName) {

		Login(TestData("GlobalAdmin"), TestData("GlobalAdmin"));
		// Select Country and Navigate to Clinic List
		Edit_SetValue("CountryList_SearchCountry_EB", CountryName);
		Btn_Click("CountryList_Search_BN");
		Rbtn_SetOption("CountryList_SelectFirstRadio_RB", "ON");
		Btn_Click("CountryList_Clinic_BN");

		// Navigate to HCP List
		Edit_SetValue("ClinicList_SearchText_EB", ClinicName);
		Btn_Click("ClinicList_Search_BN");
		Rbtn_SetOption("ClinicDetails_ClinicDetail_RB", "ON");
		Btn_Click("ClinicList_ViewHCP_BN");

		// HCP Details page
		Edit_SetValue("MyPatients_SearchText_EB", HCPName);
		Btn_Click("MyPatients_Search_BN");
		Rbtn_SetOption("ClinicDetails_ClinicDetail_RB", "ON");
		Btn_Click("HCPList_HCPDetails_BN");

		// HCP Details edit
		Lnk_Click("HCPDetails_HCPDetailsEdit_LK");
		WaitForObject("HCPDetails_EditPopupTitle_ST");
		Rbtn_SetOption("HCPDetails_EditStatusActive_RB", "ON");
		Btn_Click("HCPDetails_Save_BN");

		// Logout
		Logout();

	}

	public void SetDefaultLanguage() {

		String DDxpath = null;
		String DDText = null;
		String DefaultText = null;
		String DefaultXpath = null;
		int i = 0;
		boolean bflag = false;

		String CurrentRunningLanguage = Env.LanguageUsed;
		if (CurrentRunningLanguage.contains(".")) {
			CurrentRunningLanguage = CurrentRunningLanguage.substring(
					CurrentRunningLanguage.indexOf(".") + 1,
					CurrentRunningLanguage.length()); // Convert CA.French to
														// French
		}

		Lnk_Click("Navigation_LangFooter_LK");// Click Language & Footer link
		Lnk_Click("GlobalAdmin_FooterDetailsEdit_LK");
		if (CompareText(CurrentRunningLanguage,
				DrpDwn_GetSelectedText("GlobalAdmin_DefaultLanguage_DD"))) {
			Btn_Click("GlobalAdmin_Cancel_BN");
		} else { // Some other language is set as default
			String Sxpath = ORXPath("GlobalAdmin_SelectDefaultLanguage_DD");
			List<WebElement> ActionObject = browser.findElements(By
					.xpath(Sxpath));
			int GetSize = ActionObject.size();

			DefaultXpath = "(" + Sxpath + "//select)[1]";
			DefaultText = DrpDwn_GetSelectedText(DefaultXpath);
			DrpDwn_SelectText(DefaultXpath, CurrentRunningLanguage);

			for (i = 2; i <= GetSize; i++) {
				DDxpath = "(" + Sxpath + "//select)[" + i + "]";
				DDText = DrpDwn_GetSelectedText(DDxpath);
				if (CompareText(DDText, CurrentRunningLanguage)) {
					DrpDwn_SelectText("(" + Sxpath + "//select)[" + i + "]",
							DefaultText);
					bflag = true;
					break;
				}

			}
			Btn_Click("GlobalAdmin_Save_BN");

			if (bflag == false) {
				Lnk_Click("GlobalAdmin_AddAdditionalLangugae_LK");
				DrpDwn_SelectText("GlobalAdmin_Langugae_DD", DefaultText);
				Btn_Click("GlobalAdmin_AddAdditionalLangSave_BN");
				UploadFooterHTMLs(CurrentRunningLanguage);
				Logout();
			}
		}
	}

	public void UpdateAssessmentQuestions() {

		// Remove all the questions, if they are present
		if (ObjectExists("#Assessment_Object_LK")) {
			Lnk_Click("#Assessment_IncExcCriteriaEdit_LK");
			for (int i = 1; i <= browser.findElements(
					By.xpath(ORXPath("#GlobalAdmin_NA_ST"))).size(); i++) {
				DrpDwn_SelectIndex(ORXPath("#GlobalAdmin_NA_ST") + "[" + i
						+ "]", 2); // Make all fields as NA
			}
			Btn_Click("#Assessment_IncExcSave_BN");
		}

		Country switchCountryName = Country.valueOf(Env.CountryUsed.replaceAll(
				" ", "").toUpperCase());

		String InclusionText = switchCountryName.GetInclusionText();
		String ExclusionText = switchCountryName.GetExclusionText();

		if (InclusionText.trim().isEmpty()) { // Default Values from UK
			InclusionText = "InclusionGrowthHormone|InclusionBetween2and18|InclusionGuardianConsent";
			ExclusionText = "ExclusionTakingSaizen|ExclusionContraIndications|ExclusionInvestigationalDrug";
		}

		String Inclusion[] = InclusionText.split("\\|");
		String Exclusion[] = ExclusionText.split("\\|");

		int QuestionRow;
		String QuestionID;

		for (int i = 0; i < Inclusion.length; i++) {// Add Inclusion criteria
			Lnk_Click("#Assessment_AddInclExcl_LK");
			QuestionRow = Env.LangData.GetRowIndex(0, Inclusion[i]);
			QuestionID = Env.LangData.GetData(1, QuestionRow);
			Edit_SetValue("#Assessment_Message_EB", QuestionID);
			// Inclusione is selected by default, so need to change the value in
			// drop down
			Btn_Click("#Assessment_AddAssessmentSave_BN");
			CompareText(Inclusion[i],
					"Assessment Incl/Excl Config created successfully",
					GetText("#Assessment_AssessmentUpdate_DT"));
		}

		for (int i = 0; i < Exclusion.length; i++) {// Add Exclusion criteria
			Lnk_Click("#Assessment_AddInclExcl_LK");
			QuestionRow = Env.LangData.GetRowIndex(0, Exclusion[i]);
			QuestionID = Env.LangData.GetData(1, QuestionRow);
			Edit_SetValue("#Assessment_Message_EB", QuestionID);
			DrpDwn_SelectIndex("#Assessment_AddAssessmentInclusionCriteria_DD",
					1);
			Btn_Click("#Assessment_AddAssessmentSave_BN");
			CompareText(Exclusion[i],
					"Assessment Incl/Excl Config created successfully",
					GetText("#Assessment_AssessmentUpdate_DT"));
		}
	}

	public void OpenSecondBrowser(String url) { // , String HCPorAdmin
		// HCPorAdmin = HCPorAdmin.trim().toUpperCase();
		// String Username = null;
		try {

			String BrowserName = "FF"; // Second browser is defaulted to FF in
										// Windows machine
			if (Env.OSUsed.startsWith("MAC")) {
				BrowserName = "GC"; // Second browser is defaulted to GC in MAC
									// machine
			}

			this.Secondbrowser = GetBrowser(BrowserName);
			this.Secondbrowser.get(url);
			this.Secondbrowser.manage().window().maximize();

			// Switch browser
			SwitchActiveBrowser();

			// if (HCPorAdmin.startsWith("ADMIN")){
			// //Login as Admin
			// Username = TestData("GlobalAdmin");
			// Edit_ClearValue("Login_Username_EB");
			// Edit_SetValue("Login_Username_EB",Username);
			// Edit_SetValue("Login_Password_EB",Username);
			// Btn_Click("Login_Login_BN");
			// }else{
			// //Login as HCP
			// Username = TestData("HCPUsername");
			// Edit_ClearValue("Login_Username_EB");
			// Edit_SetValue("Login_Username_EB",Username);
			// Edit_SetValue("Login_Password_EB",Username);
			// Btn_Click("Login_Login_BN");
			// //Enter Phrase
			// Edit_SetValue("SecurityPhrase_SecurityPhrase_EB",TestData("HCPSecurityPharse"));
			// Btn_Click("SecurityPhrase_ConfirmPhrase_BN");
			// }
			//
			// CloseFeatureList(true);
			// Wait(2);
			// WaitForPageload();
			//
			// //Verify whether Login is successful
			// if((ObjectExists("Login_Username_EB") &&
			// ObjectExists("Login_Password_EB"))){
			// Env.logger.log(Level.ERROR, "Login Failed for UserName: " +
			// Username);
			// throw new RuntimeException("Login Failed for UserName: " +
			// Username);
			// }

		} catch (MalformedURLException e) {
			Env.logger.log(Level.ERROR, "", e);
		} catch (WebDriverException e) {
			Env.logger.log(Level.ERROR, "", e);
		}
	}

	public void CloseSecondBrowser() {

		if (ActiveBrowser == 2) {// Switch the active browser to the first one
									// before closing, if it is not done.
			SwitchActiveBrowser();
		}

		if (this.Secondbrowser != null) {
			try {
				this.Secondbrowser.quit();
			} catch (Exception Ignore) {
				// Timeout webdriverexcetion may occur.
			} finally {
				this.Secondbrowser = null;
			}
		}
	}

	public void VerifyTimeRange(int Months, String ActualFromDate,
			String ActualToDate) {
		int Month = Months - 1;
		String ExpectedToDate = GetServerTimeStamp("dd MMM yyyy");
		String ExpectedFromDate = ExpectedToDate.replaceFirst(
				ExpectedToDate.substring(0, 2), "01");
		ExpectedFromDate = Date_AddDays(ExpectedFromDate, Month * (-1),
				Calendar.MONTH);
		CompareText("FromDateFor" + Months + "Months",
				TranslateMonth(ExpectedFromDate), ActualFromDate);
		CompareText("ToDateFor" + Months + "Months",
				TranslateMonth(ExpectedToDate), ActualToDate);
	}

	public String DeviceSettingsConverter(String SettingsName,
			String ValueInXML, String DeviceType) {
		DeviceType = DeviceType.trim().toUpperCase();
		DeviceSettings SettingType = DeviceSettings.valueOf(SettingsName
				.toUpperCase());
		String ReturnValue = null;
		DecimalFormat df = null;
		switch (SettingType) {
		case PINCODE:
		case EDEVICEID:
		case SOFTWAREVERSION:
		case HARDWAREVERSION:
		case SERIALNUMBER:
		case SIMCARDNUMBER:
		case IMEINUMBER:
		case DOSE:
			ReturnValue = ValueInXML; // Return the same value.
			break;
		case INJECTIONDEPTH:
		case INJECTIONTIME:
		case CARTRIDGEEXPIRATION:
		case DOSEFREQUENCY:
		case LASTDOSEMAXSPREAD:
		case WEIGHT:
		case HEIGHT:
		case WELCOMESCREEN:
			ReturnValue = String.valueOf(Integer.valueOf(ValueInXML)); // Truncate
																		// the
																		// leading
																		// zeros
																		// ReturnValue
																		// =
																		// ValueInXML.replaceFirst("^0+(?!$)",
																		// "");
			break;
		case POSOLOGYBYWEIGHT:
			df = new DecimalFormat("0.00");
			ReturnValue = (df.format(Float.parseFloat(ValueInXML))).toString();
			break;
		case POSOLOGYBYSURFACE:
			df = new DecimalFormat("0.0");
			ReturnValue = (df.format(Float.parseFloat(ValueInXML))).toString();
			break;
		case NEEDLESPEED:
		case INJECTIONSPEED:
			if (ValueInXML.endsWith("00000")) {
				ReturnValue = LangData("NeedleSpeedSlow");
			} else if (ValueInXML.endsWith("00001")) {
				ReturnValue = LangData("NeedleSpeedMedium");
			} else if (ValueInXML.endsWith("00002")) {
				ReturnValue = LangData("NeedleSpeedFast");
			}
			break;
		case NEEDLETYPE:
			if (ValueInXML.endsWith("00000")) {
				ReturnValue = LangData("NeedleType29G");
			} else if (ValueInXML.endsWith("00001")) {
				ReturnValue = LangData("NeedleType30G");
			} else if (ValueInXML.endsWith("00002")) {
				ReturnValue = LangData("NeedleType31G");
			}
			break;
		case DOSELOGON:
		case SOUNDON:
			if (ValueInXML.endsWith("00000")) {
				ReturnValue = LangData("SoundOFF");
			} else if (ValueInXML.endsWith("00001")) {
				ReturnValue = LangData("SoundON");
			}
			break;
		case CARTRIDGETYPE:
			if (ValueInXML.endsWith("00000")) {
				ReturnValue = LangData("CartridgeTypeA");
			} else if (ValueInXML.endsWith("00001")) {
				ReturnValue = LangData("CartridgeTypeB");
			}
			break;
		case REGIONALSETTINGS:
			if (ValueInXML.endsWith("00000")) {
				ReturnValue = LangData("RegionalSetting0");
			} else if (ValueInXML.endsWith("00001")) {
				ReturnValue = LangData("RegionalSetting1");
			} else if (ValueInXML.endsWith("00002")) {
				ReturnValue = LangData("RegionalSetting2");
			}
			break;
		case FORMULATION:
			if (DeviceType.endsWith("EP2")) {
				ReturnValue = "-"; // Forumlation is not used for EP2
			} else if (ValueInXML.endsWith("00000")) {
				ReturnValue = LangData("FormulationValueLiquid");
			} else if (ValueInXML.endsWith("00001")) {
				ReturnValue = LangData("FormulationValueFreezedried");
			}
			break;
		case DOSECALCULATIONSTRATEGY:
			if (ValueInXML.endsWith("00000")) {
				ReturnValue = LangData("DoseStrategyByMilligram");
			} else if (ValueInXML.endsWith("00001")) {
				ReturnValue = LangData("DoseStrategyByWeight");
			} else if (ValueInXML.endsWith("00002")) {
				ReturnValue = LangData("DoseStrategyByBodySurface");
			}
			break;
		case LASTDOSESTRATEGY:
			if (ValueInXML.endsWith("00000")) {
				ReturnValue = LangData("DoseOFF");
			} else if (ValueInXML.endsWith("00001")) {
				ReturnValue = LangData("FiftyPercent");
			} else if (ValueInXML.endsWith("00002")) {
				ReturnValue = LangData("DoseAutomatic");
			}
			break;
		case DATETIMEFORMAT:
			if (ValueInXML.endsWith("00000")) {
				ReturnValue = LangData("International");
			} else if (ValueInXML.endsWith("00001")) {
				ReturnValue = LangData("UnitedStatesShort");
			} else if (ValueInXML.endsWith("00002")) {
				ReturnValue = LangData("Nordic");
			}
			break;
		case TIMEZONE:
			ValueInXML = String.valueOf(Integer.valueOf(ValueInXML));
			if (ValueInXML.compareToIgnoreCase("0") == 0) {
				ReturnValue = LangData("TimeZone0");
			} else if (ValueInXML.compareToIgnoreCase("1") == 0) {
				ReturnValue = LangData("TimeZone1");
			} else if (ValueInXML.compareToIgnoreCase("2") == 0) {
				ReturnValue = LangData("TimeZone2");
			} else if (ValueInXML.compareToIgnoreCase("3") == 0) {
				ReturnValue = LangData("TimeZone3");
			} else if (ValueInXML.compareToIgnoreCase("4") == 0) {
				ReturnValue = LangData("TimeZone4");
			} else if (ValueInXML.compareToIgnoreCase("5") == 0) {
				ReturnValue = LangData("TimeZone5");
			} else if (ValueInXML.compareToIgnoreCase("6") == 0) {
				ReturnValue = LangData("TimeZone6");
			} else if (ValueInXML.compareToIgnoreCase("7") == 0) {
				ReturnValue = LangData("TimeZone7");
			} else if (ValueInXML.compareToIgnoreCase("8") == 0) {
				ReturnValue = LangData("TimeZone8");
			} else if (ValueInXML.compareToIgnoreCase("9") == 0) {
				ReturnValue = LangData("TimeZone9");
			} else if (ValueInXML.compareToIgnoreCase("10") == 0) {
				ReturnValue = LangData("TimeZone10");
			} else if (ValueInXML.compareToIgnoreCase("11") == 0) {
				ReturnValue = LangData("TimeZone11");
			} else if (ValueInXML.compareToIgnoreCase("12") == 0) {
				ReturnValue = LangData("TimeZone12");
			} else if (ValueInXML.compareToIgnoreCase("13") == 0) {
				ReturnValue = LangData("TimeZone13");
			} else if (ValueInXML.compareToIgnoreCase("14") == 0) {
				ReturnValue = LangData("TimeZone14");
			} else if (ValueInXML.compareToIgnoreCase("15") == 0) {
				ReturnValue = LangData("TimeZone15");
			} else if (ValueInXML.compareToIgnoreCase("16") == 0) {
				ReturnValue = LangData("TimeZone16");
			} else if (ValueInXML.compareToIgnoreCase("17") == 0) {
				ReturnValue = LangData("TimeZone17");
			} else if (ValueInXML.compareToIgnoreCase("18") == 0) {
				ReturnValue = LangData("TimeZone18");
			} else if (ValueInXML.compareToIgnoreCase("19") == 0) {
				ReturnValue = LangData("TimeZone19");
			} else if (ValueInXML.compareToIgnoreCase("20") == 0) {
				ReturnValue = LangData("TimeZone20");
			} else if (ValueInXML.compareToIgnoreCase("21") == 0) {
				ReturnValue = LangData("TimeZone21");
			} else if (ValueInXML.compareToIgnoreCase("22") == 0) {
				ReturnValue = LangData("TimeZone22");
			} else if (ValueInXML.compareToIgnoreCase("23") == 0) {
				ReturnValue = LangData("TimeZone23");
			} else if (ValueInXML.compareToIgnoreCase("24") == 0) {
				ReturnValue = LangData("TimeZone24");
			} else if (ValueInXML.compareToIgnoreCase("25") == 0) {
				ReturnValue = LangData("TimeZone25");
			} else if (ValueInXML.compareToIgnoreCase("26") == 0) {
				ReturnValue = LangData("TimeZone26");
			} else if (ValueInXML.compareToIgnoreCase("27") == 0) {
				ReturnValue = LangData("TimeZone27");
			} else if (ValueInXML.compareToIgnoreCase("28") == 0) {
				ReturnValue = LangData("TimeZone28");
			} else if (ValueInXML.compareToIgnoreCase("29") == 0) {
				ReturnValue = LangData("TimeZone29");
			} else if (ValueInXML.compareToIgnoreCase("30") == 0) {
				ReturnValue = LangData("TimeZone30");
			} else if (ValueInXML.compareToIgnoreCase("31") == 0) {
				ReturnValue = LangData("TimeZone31");
			} else if (ValueInXML.compareToIgnoreCase("32") == 0) {
				ReturnValue = LangData("TimeZone32");
			} else if (ValueInXML.compareToIgnoreCase("33") == 0) {
				ReturnValue = LangData("TimeZone33");
			} else if (ValueInXML.compareToIgnoreCase("34") == 0) {
				ReturnValue = LangData("TimeZone34");
			} else if (ValueInXML.compareToIgnoreCase("35") == 0) {
				ReturnValue = LangData("TimeZone35");
			} else if (ValueInXML.compareToIgnoreCase("36") == 0) {
				ReturnValue = LangData("TimeZone36");
			} else if (ValueInXML.compareToIgnoreCase("37") == 0) {
				ReturnValue = LangData("TimeZone37");
			} else if (ValueInXML.compareToIgnoreCase("38") == 0) {
				ReturnValue = LangData("TimeZone38");
			}
			break;
		case LANGUAGE:
			ValueInXML = String.valueOf(Integer.valueOf(ValueInXML));
			if (ValueInXML.compareToIgnoreCase("0") == 0) {
				ReturnValue = LangData("DeviceLangCzech");
			} else if (ValueInXML.compareToIgnoreCase("1") == 0) {
				ReturnValue = LangData("DeviceLangDanish");
			} else if (ValueInXML.compareToIgnoreCase("2") == 0) {
				ReturnValue = LangData("DeviceLangGerman");
			} else if (ValueInXML.compareToIgnoreCase("3") == 0) {
				ReturnValue = LangData("DeviceLangEstonian");
			} else if (ValueInXML.compareToIgnoreCase("4") == 0) {
				ReturnValue = LangData("DeviceLangEnglish");
			} else if (ValueInXML.compareToIgnoreCase("5") == 0) {
				ReturnValue = LangData("DeviceLangSpanish");
			} else if (ValueInXML.compareToIgnoreCase("6") == 0) {
				ReturnValue = LangData("DeviceLangFrench");
			} else if (ValueInXML.compareToIgnoreCase("7") == 0) {
				ReturnValue = LangData("DeviceLangItalian");
			} else if (ValueInXML.compareToIgnoreCase("8") == 0) {
				ReturnValue = LangData("DeviceLangLatvian");
			} else if (ValueInXML.compareToIgnoreCase("9") == 0) {
				ReturnValue = LangData("DeviceLangLithuanian");
			} else if (ValueInXML.compareToIgnoreCase("10") == 0) {
				ReturnValue = LangData("DeviceLangHungarian");
			} else if (ValueInXML.compareToIgnoreCase("11") == 0) {
				ReturnValue = LangData("DeviceLangDutch");
			} else if (ValueInXML.compareToIgnoreCase("12") == 0) {
				ReturnValue = LangData("DeviceLangNorwegian");
			} else if (ValueInXML.compareToIgnoreCase("13") == 0) {
				ReturnValue = LangData("DeviceLangPolish");
			} else if (ValueInXML.compareToIgnoreCase("14") == 0) {
				ReturnValue = LangData("DeviceLangPortuguese");
			} else if (ValueInXML.compareToIgnoreCase("15") == 0) {
				ReturnValue = LangData("DeviceLangSlovak");
			} else if (ValueInXML.compareToIgnoreCase("16") == 0) {
				ReturnValue = LangData("DeviceLangFinnish");
			} else if (ValueInXML.compareToIgnoreCase("17") == 0) {
				ReturnValue = LangData("DeviceLangSwedish");
			} else if (ValueInXML.compareToIgnoreCase("18") == 0) {
				ReturnValue = LangData("DeviceLangTurkish");
			} else if (ValueInXML.compareToIgnoreCase("19") == 0) {
				ReturnValue = LangData("DeviceLangGreek");
			} else if (ValueInXML.compareToIgnoreCase("20") == 0) {
				ReturnValue = LangData("DeviceLangRussian");
			} else if (ValueInXML.compareToIgnoreCase("21") == 0) {
				ReturnValue = LangData("DeviceLangUkrainian");
			} else if (ValueInXML.compareToIgnoreCase("22") == 0) {
				ReturnValue = LangData("DeviceLangChineseSimplified");
			} else if (ValueInXML.compareToIgnoreCase("23") == 0) {
				ReturnValue = LangData("DeviceLangChineseTraditional");
			} else if (ValueInXML.compareToIgnoreCase("24") == 0) {
				ReturnValue = LangData("DeviceLangJapanese");
			} else if (ValueInXML.compareToIgnoreCase("25") == 0) {
				ReturnValue = LangData("DeviceLangKorean");
			}
			ReturnValue = ValueInXML + " " + ReturnValue;
			break;
		}// Switch ends

		return ReturnValue;
	}

	private enum DeviceSettings {

		VERSION, SERIALNO, EDEVICEID, TIMESTAMP, HEIGHT, WEIGHT, NEEDLESPEED, INJECTIONSPEED, PATIENTID, PINCODE, DOSELOGON, DOSECALCULATIONSTRATEGY, POSOLOGYBYWEIGHT, POSOLOGYBYSURFACE, DOSEFREQUENCY, DOSE, LASTDOSESTRATEGY, LASTDOSEMAXSPREAD, CARTRIDGETYPE, INJECTIONDEPTH, NEEDLETYPE, SOUNDON, LANGUAGE, DATETIMEFORMAT, WELCOMESCREEN, INJECTIONTIME, CARTRIDGEEXPIRATION, FORMULATION, REGIONALSETTINGS, TIMEZONE, SOFTWAREVERSION, HARDWAREVERSION, SERIALNUMBER, SIMCARDNUMBER, IMEINUMBER
	}

	public int DateDiff(String Date1, String Date2, String returnType) { // return
																			// type
																			// =WEEKS
																			// or
																			// MONTHS
																			// or
																			// YEARS
		Date MyDate1 = null;
		Date MyDate2 = null;

		try {
			MyDate1 = (new SimpleDateFormat("dd MMM yyyy")).parse(Date1);
		} catch (ParseException e) {

			Env.logger.log(Level.ERROR, "Unable to parse the date");
			Report.Write(Date1 + "DateDifference", "Date is able to parse",
					"Unable to parse the date", "Fail", true);
		}

		try {
			MyDate2 = (new SimpleDateFormat("dd MMM yyyy")).parse(Date2);
		} catch (ParseException e) {
			Env.logger.log(Level.ERROR, "Unable to parse the date");
			Report.Write(Date1 + "DateDifference", "Date is able to parse",
					"Unable to parse the date", "Fail", true);
		}
		int Output = 0;
		Calendar dateOne = Calendar.getInstance();
		String DateSeperator = " ";
		String[] d1 = Date1.split(DateSeperator);
		dateOne.setTime(MyDate1);
		int month1 = dateOne.get(Calendar.MONTH);
		// set first date of calendar
		dateOne.set(Integer.parseInt(d1[2]), month1, Integer.parseInt(d1[0]));
		// logger.info("First date :" + dateOne.getTime());

		Calendar dateSecond = Calendar.getInstance();
		String[] d2 = Date2.split(DateSeperator);
		dateSecond.setTime(MyDate2);
		int month2 = dateSecond.get(Calendar.MONTH);
		// set second date of calendar
		dateSecond
				.set(Integer.parseInt(d2[2]), month2, Integer.parseInt(d2[0]));
		// logger.info("Second date :" + dateSecond.getTime());

		// convert date value into millisecond
		long t1 = dateOne.getTimeInMillis();
		long t2 = dateSecond.getTimeInMillis();
		// Calculate difference in millisecond
		long diffInMilliSecond = t2 - t1;
		// Convert milliseconds into minutes
		long minutes = (diffInMilliSecond / (1000 * 60));
		// Convert minutes value into hours
		long hours = minutes / 60;
		// convert hours into days
		long day = hours / 24;
		// logger.info(day);
		long weeks = day / 7;

		// convert days into months
		long month = day / 30;
		long years = month / 12;

		int totalmonths = (int) month;
		int totalweeks = (int) weeks;
		int totalyears = (int) years;

		if (returnType.compareToIgnoreCase("WEEKS") == 0) {
			Output = totalweeks;
		} else if (returnType.compareToIgnoreCase("MONTHS") == 0) {
			if (totalmonths == 0) {
				totalmonths = 1;
			}
			Output = totalmonths;
		} else if (returnType.compareToIgnoreCase("YEARS") == 0) {
			Output = totalyears;
		} else if (returnType.compareToIgnoreCase("DAYS") == 0) {
			Output = (int) day;
		}
		return Output;
	}

	public boolean VerifySelectedTab(int NavigationLevel,
			String TextOfSelectedTab) {
		String XPathOfSelectedTab = ORXPath("General_NavigationLevel"
				+ NavigationLevel + "_LK")
				+ ORXPath("General_NavigationLevel" + NavigationLevel
						+ "Selected_LK");
		return CompareText("Level" + NavigationLevel + "_SelectedTab",
				TextOfSelectedTab, GetText(XPathOfSelectedTab));
	}

	public String GetDeviceUsageStartDate(String PatientName) {

		if (!ObjectExists("NavigationTab_PatientsDevicesTab_LK")) {
			// Get Device Usgae start date
			NavigateTo(Env.ECKOURL);
			SearchForPatient(PatientName); // TestData("ECOSPatient")
			Btn_Click("MyPatients_PatientsDevices_BN");
		} else {
			Lnk_Click("NavigationTab_PatientsDevicesTab_LK");
		}

		if (WbTbl_GetRowcount("AssignedDevices_TableHeader_ST") != 0) {
			WbTbl_SortColumn("AssignedDevices_TableHeader_ST", 6, "ASC");
			return WbTbl_GetText("AssignedDevices_TableHeader_ST", 1, 6); // Least
																			// of
																			// all
																			// device
																			// usage
																			// start
																			// date
		} else {
			Env.logger.log(Level.ERROR, "Assigned Devices table is empty");
			return null;
		}
	}

	public String GetLastUploadDate(String PatientName) {
		if (!ObjectExists("NavigationTab_PatientsDevicesTab_LK")) {
			NavigateTo(Env.ECKOURL);
			SearchForPatient(PatientName);
			Btn_Click("MyPatients_PatientsDevices_BN");
		} else {
			Lnk_Click("NavigationTab_PatientsDevicesTab_LK");
		}
		// Get Device Usgae start date
		if (WbTbl_GetRowcount("AssignedDevices_TableHeader_ST") != 0) {
			WbTbl_SortColumn("AssignedDevices_TableHeader_ST", 5, "DESC");
		}
		String ReturnValue = WbTbl_GetText("AssignedDevices_TableHeader_ST", 1,
				5).trim();
		ReturnValue = ReturnValue.substring(0, ReturnValue.length() - 5).trim(); // Truncating
																					// the
																					// time
		return ReturnValue;

	}

	public String GetLastVisitDate(String PatientName) {
		if (!ObjectExists("NavigationTab_VisitsTab_LK")) {
			// Get Device Usgae start date
			NavigateTo(Env.ECKOURL);
			SearchForPatient(PatientName); // TestData("ECOSPatient")
			Btn_Click("MyPatients_VisitsRemarks_LK");
		} else {
			Lnk_Click("NavigationTab_VisitsTab_LK");
		}
		if (WbTbl_GetRowcount("Visits_VisitsTable_TE") != 0) {
			return WbTbl_GetText("Visits_VisitsTable_TE", 1, 2);
		} else {
			Env.logger.log(Level.ERROR, "Visits table is empty");
			return null;
		}
	}

	public String GetClinicName() {
		return GetText("Footer_ClinicValue_ST"); // Get Clinic Name
	}

	public String GetHCPName(boolean IsApplication) {
		String HCPName = " ";

		if (IsApplication) {
			HCPName = GetText("Header_LoggedInUser_ST");
		} else {
			HCPName = GetText("Header_LoggedInUser_ST");
			String HCPModified[] = HCPName.split(" ");
			HCPName = HCPModified[0] + " " + HCPModified[1];
		}
		return HCPName;
	}

	public void WbTbl_VerifySorting(String TableId, int iColIndex,
			String DataType, String ColumnName, String StringToIgnore) {
		ColumnName = ColumnName.replaceAll(" ", "");
		List<String> TableText = null;
		StringToIgnore = StringToIgnore.trim();
		// Initiating values for custom sorting
		String CustomStrings = null;
		String CustomTableText = null;
		List<String> CustomList = null;
		if (DataType.toUpperCase().trim().startsWith("CUSTOM")) {
			CustomStrings = DataType;
			DataType = "NUMBER";
			CustomList = Arrays.asList(CustomStrings.split("\\|"));
		}

		// Ascending
		WbTbl_SortColumn(TableId, iColIndex, "ASC");
		TableText = WbTbl_GetText(TableId, iColIndex);
		if (!StringToIgnore.isEmpty()) { // Handling Strings to Ignore
			for (int i = 0; i < TableText.size(); i++) {
				TableText.set(
						i,
						TableText.get(i)
								.replaceAll("[" + StringToIgnore + "]", "")
								.trim());
			}
		}
		if (CustomStrings != null) { // Handling Custom Strings
			CustomTableText = StringUtils.join(TableText, "|");
			for (int j = 1; j < CustomList.size(); j++) {
				CustomTableText = CustomTableText.replaceAll(CustomList.get(j),
						String.valueOf(j));
			}
			TableText = Arrays.asList(CustomTableText.split("\\|"));
		}
		CheckSorting(TableId, TableText, DataType, "Ascending", ColumnName,
				iColIndex);

		// Descending
		WbTbl_SortColumn(TableId, iColIndex, "DESC");
		TableText = WbTbl_GetText(TableId, iColIndex);
		if (!StringToIgnore.isEmpty()) {
			for (int i = 0; i < TableText.size(); i++) {
				TableText.set(
						i,
						TableText.get(i)
								.replaceAll("[" + StringToIgnore + "]", "")
								.trim());
			}
		}
		if (CustomStrings != null) { // Handling Custom Strings
			CustomTableText = StringUtils.join(TableText, "|");
			for (int j = 1; j < CustomList.size(); j++) {
				CustomTableText = CustomTableText.replaceAll(CustomList.get(j),
						String.valueOf(j));
			}
			TableText = Arrays.asList(CustomTableText.split("\\|"));
		}
		CheckSorting(TableId, TableText, DataType, "Descending", ColumnName,
				iColIndex);

	}

	public void CheckSorting(String TableId, List<String> ListPassedIn,
			String DataType, String SortingType, String ColumnName,
			int iColIndex) {
		boolean ReturnValue = true;
		Locale locale = GetLocale();
		WebElement ActionObject = null;
		WebElement SortingImage = null;
		String CurrentSort = null;
		Collator collator = Collator.getInstance(locale);

		ActionObject = GetObject(TableId);
		SortingImage = ActionObject.findElement(By.xpath("//thead/tr/th["
				+ iColIndex
				+ "]//img[contains(@class,'sort') or contains(@class,'srt')]"));// srtDisplayIcn
		CurrentSort = SortingImage.getAttribute("src");

		if (DataType.trim().toUpperCase().startsWith("STRING")) {
			List<String> ListToCheck = new ArrayList<String>();
			List<String> SorteddList = new ArrayList<String>();

			ListToCheck.addAll(ListPassedIn);
			SorteddList.addAll(ListToCheck);
			Collections.sort(SorteddList, collator);

			if (SortingType.toUpperCase().startsWith("ASCENDING")) {
				for (int i = 0; i < ListToCheck.size(); i++) {
					if (SorteddList.get(i).compareToIgnoreCase(
							ListToCheck.get(i)) != 0) {
						Report.Write(
								ColumnName + "_Sort" + SortingType,
								ColumnName + " should be sorted in "
										+ SortingType + " order.",
								ColumnName
										+ " column is not sorted in Ascending order for the value present in row "
										+ (i + 1), "Fail", true);
						ReturnValue = false;
						break;
					}
				}
				if (CurrentSort.endsWith("arrow_black_asc.png")) {
					Report.Write(ColumnName + "_SortIcon" + SortingType,
							"Ascending Sort icon should be displayed for "
									+ ColumnName,
							"Ascending Sort icon is displayed for "
									+ ColumnName, "Pass", true);
				} else {
					Report.Write(ColumnName + "_SortIcon" + SortingType,
							"Ascending Sort icon should be displayed for "
									+ ColumnName,
							"Ascending Sort icon is not displayed for "
									+ ColumnName, "Fail", true);
				}
			} else if (SortingType.toUpperCase().startsWith("DESCENDING")) {
				for (int i = 0, j = ListToCheck.size() - 1; j >= 0; i++, j--) {
					if (SorteddList.get(i).compareToIgnoreCase(
							ListToCheck.get(j)) != 0) {
						Report.Write(
								ColumnName + "_Sort" + SortingType,
								ColumnName + " should be sorted in "
										+ SortingType + " order.",
								ColumnName
										+ " column is not sorted in Descending order for the value present in row "
										+ (j + 1), "Fail", true);
						ReturnValue = false;
						break;
					}
				}
				if (CurrentSort.endsWith("arrow_black.png")) {
					Report.Write(ColumnName + "_SortIcon" + SortingType,
							"Descending Sort icon should be displayed for "
									+ ColumnName,
							"Descending Sort icon is displayed for "
									+ ColumnName, "Pass", true);
				} else {
					Report.Write(ColumnName + "_SortIcon" + SortingType,
							"Descending Sort icon should be displayed for "
									+ ColumnName,
							"Descending Sort icon is not displayed for "
									+ ColumnName, "Fail", true);
				}
			}

		} else if (DataType.trim().toUpperCase().startsWith("NUMBER")) {
			List<Integer> ListToCheck = new ArrayList<Integer>();
			List<Integer> SorteddList = new ArrayList<Integer>();

			for (String ini : ListPassedIn) {
				if (!(ini.startsWith("-") || ini.trim().isEmpty())) { // Hyphen
																		// and
																		// Empty
																		// values
																		// are
																		// treated
																		// as
																		// errror
																		// values
					ListToCheck.add(Integer.valueOf(ini));
				} else {
					ListToCheck.add(Integer.valueOf("99999"));
				}
			}
			SorteddList.addAll(ListToCheck);
			Collections.sort(SorteddList);

			if (SortingType.toUpperCase().startsWith("ASCENDING")) {
				for (int i = 0; i < ListToCheck.size(); i++) {
					if (SorteddList.get(i).compareTo(ListToCheck.get(i)) != 0) {
						Report.Write(
								ColumnName + "_Sort" + SortingType,
								ColumnName + " should be sorted in "
										+ SortingType + " order.",
								ColumnName
										+ " column is not sorted in Ascending order for the value present in row "
										+ (i + 1), "Fail", true);
						ReturnValue = false;
						break;
					}
				}
				if (CurrentSort.endsWith("arrow_black_asc.png")) {
					Report.Write(ColumnName + "_SortIcon" + SortingType,
							"Ascending Sort icon should be displayed for "
									+ ColumnName,
							"Ascending Sort icon is displayed for "
									+ ColumnName, "Pass", true);
				} else {
					Report.Write(ColumnName + "_SortIcon" + SortingType,
							"Ascending Sort icon should be displayed for "
									+ ColumnName,
							"Ascending Sort icon is not displayed for "
									+ ColumnName, "Fail", true);
				}
			} else if (SortingType.toUpperCase().startsWith("DESCENDING")) {
				for (int i = 0, j = ListToCheck.size() - 1; j >= 0; i++, j--) {
					if (SorteddList.get(i).compareTo(ListToCheck.get(j)) != 0) {
						Report.Write(
								ColumnName + "_Sort" + SortingType,
								ColumnName + " should be sorted in "
										+ SortingType + " order.",
								ColumnName
										+ " column is not sorted in Descending order for the value present in row "
										+ (j + 1), "Fail", true);
						ReturnValue = false;
						break;
					}
				}
				if (CurrentSort.endsWith("arrow_black.png")) {
					Report.Write(ColumnName + "_SortIcon" + SortingType,
							"Descending Sort icon should be displayed for "
									+ ColumnName,
							"Descending Sort icon is displayed for "
									+ ColumnName, "Pass", true);
				} else {
					Report.Write(ColumnName + "_SortIcon" + SortingType,
							"Descending Sort icon should be displayed for "
									+ ColumnName,
							"Descending Sort icon is not displayed for "
									+ ColumnName, "Fail", true);
				}
			}
		} else {
			// If time is present in the format, then choose the format for that
			// particulare language from LangData
			if (DataType.trim().toUpperCase().contains("HH:")) {
				DataType = DataType.replaceAll("HH:mm",
						LangData("JavaTimeFormater"));
			}

			DateFormat f = new SimpleDateFormat(DataType, locale);
			DateFormat ErrorFormat = new SimpleDateFormat("yyyy MM dd HH:mm:ss");
			String ErrorValue = "1500 01 01 00:00:00"; // A Lowest value Ex:
														// 01-Jan-1500. Fix for
														// EPCWAVEII-65, hypen
														// in date object will
														// take lowest value.

			List<Date> ListToCheck = new ArrayList<Date>();
			List<Date> SorteddList = new ArrayList<Date>();

			try {
				for (String ini : ListPassedIn) {
					if (!(ini.startsWith("-") || ini.trim().isEmpty() || !ini
							.trim().contains(":"))) { // Hyphen and Empty values
														// are treated as errror
														// values
						ListToCheck.add(f.parse(ini));
					} else {
						ListToCheck.add(ErrorFormat.parse(ErrorValue));
					}
				}
			} catch (ParseException e) {
				ReturnValue = false;
				Env.logger.log(Level.ERROR, "Unable to parse the date");
				Report.Write(ColumnName + "_Sort" + SortingType, ColumnName
						+ " should be sorted in " + SortingType + " order.",
						"Unable to parse the date", "Fail", true);
			}
			SorteddList.addAll(ListToCheck);
			Collections.sort(SorteddList);

			if (SortingType.toUpperCase().startsWith("ASCENDING")) {
				for (int i = 0; i < ListToCheck.size(); i++) {
					if (SorteddList.get(i).compareTo(ListToCheck.get(i)) != 0) {
						Report.Write(
								ColumnName + "_Sort" + SortingType,
								ColumnName + " should be sorted in "
										+ SortingType + " order.",
								ColumnName
										+ " column is not sorted in Ascending order for the value present in row "
										+ (i + 1), "Fail", true);
						ReturnValue = false;
						break;
					}
				}
				if (CurrentSort.endsWith("arrow_black_asc.png")) {
					Report.Write(ColumnName + "_SortIcon" + SortingType,
							"Ascending Sort icon should be displayed for "
									+ ColumnName,
							"Ascending Sort icon is displayed for "
									+ ColumnName, "Pass", true);
				} else {
					Report.Write(ColumnName + "_SortIcon" + SortingType,
							"Ascending Sort icon should be displayed for "
									+ ColumnName,
							"Ascending Sort icon is not displayed for "
									+ ColumnName, "Fail", true);
				}
			} else if (SortingType.toUpperCase().startsWith("DESCENDING")) {
				for (int i = 0, j = ListToCheck.size() - 1; j >= 0; i++, j--) {
					if (SorteddList.get(i).compareTo(ListToCheck.get(j)) != 0) {
						Report.Write(
								ColumnName + "_Sort" + SortingType,
								ColumnName + " should be sorted in "
										+ SortingType + " order.",
								ColumnName
										+ " column is not sorted in Descending order for the value present in row "
										+ (j + 1), "Fail", true);
						ReturnValue = false;
						break;
					}
				}
				if (CurrentSort.endsWith("arrow_black.png")) {
					Report.Write(ColumnName + "_SortIcon" + SortingType,
							"Descending Sort icon should be displayed for "
									+ ColumnName,
							"Descending Sort icon is displayed for "
									+ ColumnName, "Pass", true);
				} else {
					Report.Write(ColumnName + "_SortIcon" + SortingType,
							"Descending Sort icon should be displayed for "
									+ ColumnName,
							"Descending Sort icon is not displayed for "
									+ ColumnName, "Fail", true);
				}
			}
		}

		if (ReturnValue) {
			Report.Write(ColumnName + "_Sort" + SortingType, ColumnName
					+ " should be sorted in " + SortingType + " order.",
					ColumnName + " is sorted in " + SortingType + " order.",
					"Pass", true);
		}
	}

	public void CheckPaginationFunctioanlity(String TableObject,
			String RecordInfoObject, String PageInfoObject) {

		String XPathPrevPage = "(" + ORXPath(PageInfoObject)
				+ "//a[contains(@class,'prev')])[1]";
		String XpathNextPage = "(" + ORXPath(PageInfoObject)
				+ "//a[contains(@class,'next')])[1]";
		String PrevPageClass = null;
		String NextPageClass = null;
		String XpathCurrentPageEditBox = ORXPath(PageInfoObject) + "//input";
		String Expected = "";
		String To = "-"; // For ECKO
		if (Env.AppModule == "ECOS") {
			To = " - "; // For ECOS //LangData("To")
		}

		String GeneralPaginationXpath = "";

		final int RecordsPerPage = 10;
		final int TotalRecords;
		final int TotalPages;
		final int ExpectedPageCount;
		int CurrentPage = 0;
		int RecordEndingNumber = 0;
		int RecordStartingNumber = 0;
		int TotalRecordsInCurrentPage = 0;
		boolean isRecordInfoPresent = false;

		if (!RecordInfoObject.trim().isEmpty()) {
			isRecordInfoPresent = true;
		}

		if (Env.AppModule == "ECOS") {
			GeneralPaginationXpath = "//div[@class='pagination']";
		} else {
			GeneralPaginationXpath = "//div[@id='myTable_paginate']";
		}
		try {

			// Get the TotalPages and TotalRecords
			TotalPages = Integer.valueOf(GetText(PageInfoObject).split(" ")[4]);
			Edit_SetValue(XpathCurrentPageEditBox, TotalPages);// Get the number
																// of records in
																// the last page
			Wait(2);
			WaitForPageload();
			TotalRecordsInCurrentPage = WbTbl_GetRowcount(TableObject);
			if (TotalRecordsInCurrentPage < 10) {
				TotalRecords = (RecordsPerPage * (TotalPages - 1))
						+ TotalRecordsInCurrentPage;
			} else {
				TotalRecords = RecordsPerPage * TotalPages;
			}

			// Calculate the expected total pages
			if (TotalRecords == 0) {
				ExpectedPageCount = 1;
			} else if ((TotalRecords % 10) != 0) {
				ExpectedPageCount = (TotalRecords / 10) + 1;
			} else {
				ExpectedPageCount = TotalRecords / 10;
			}

			// ========================First Page Starts
			// ========================
			// Navigate to the first page
			CurrentPage = 1;
			Edit_SetValue(XpathCurrentPageEditBox, CurrentPage);
			Wait(2);
			WaitForPageload();
			TakeStaticScreenshot("FirstPage");
			if (isRecordInfoPresent) { // Verify RecordInfo In First page
				TotalRecordsInCurrentPage = WbTbl_GetRowcount(TableObject);
				if (TotalRecordsInCurrentPage != 0) {
					RecordStartingNumber = ((CurrentPage - 1) * 10) + 1;
				} else {
					RecordStartingNumber = 0;
				}
				if (TotalRecordsInCurrentPage < 10) {
					RecordEndingNumber = TotalRecordsInCurrentPage;
				} else {
					RecordEndingNumber = RecordStartingNumber + 9;
				}
				if (TotalRecordsInCurrentPage > 1) {
					Expected = "(" + RecordStartingNumber + To
							+ RecordEndingNumber + " " + LangData("Of") + " "
							+ TotalRecords + " " + LangData("Records") + ")";
					CompareText("RecordInfoPage" + CurrentPage, Expected,
							GetText(RecordInfoObject));
				} else {
					Expected = "(" + RecordStartingNumber + To
							+ RecordEndingNumber + " " + LangData("Of") + " "
							+ TotalRecords + " " + LangData("Record") + ")";
					CompareText("RecordInfoPage" + CurrentPage, Expected,
							GetText(RecordInfoObject));
				}
			}

			// Check if previous page link is disabled in first page
			PrevPageClass = GetAttributeValue(XPathPrevPage, "class")
					.toLowerCase();
			NextPageClass = GetAttributeValue(XpathNextPage, "class")
					.toLowerCase();
			if ((PrevPageClass.endsWith("disabled"))
					|| (PrevPageClass.endsWith("error"))) {
				Report.Update("PreviousLinkPage" + CurrentPage,
						"Previous page link should be disabled",
						"Previous page link is disabled", "Pass");
			} else {
				Report.Update("PreviousLinkPage" + CurrentPage,
						"Previous page link should be disabled",
						"Previous page link is not disabled", "Fail");
			}

			if (TotalPages > 1) { // If there is more than one page, verify that
									// next page is enabled
				if ((NextPageClass.endsWith("enabled"))
						|| (NextPageClass.endsWith("active"))) {
					Report.Update("NextLinkPage" + CurrentPage,
							"Next page link should be enabled",
							"Next page link is enabled", "Pass");
				} else {
					Report.Update("NextLinkPage" + CurrentPage,
							"Next page link should be enabled",
							"Next page link is not enabled", "Fail");
				}
			} else { // If there is only one page, Verify that next page is
						// disabled
				if (NextPageClass.endsWith("disabled")
						|| (PrevPageClass.endsWith("error"))
						|| (PrevPageClass.endsWith("Deactive"))) {
					Report.Update("NextLinkPage" + CurrentPage,
							"Next page link should be disabled",
							"Next page link is disabled", "Pass");
				} else {
					Report.Update("NextLinkPage" + CurrentPage,
							"Next page link should be disabled",
							"Next page link is not disabled", "Fail");
				}
			}

			CompareText("CurrentPage" + CurrentPage + "_EB",
					String.valueOf(CurrentPage),
					Edit_GetValue(XpathCurrentPageEditBox));

			if (GetText(GeneralPaginationXpath).equalsIgnoreCase(
					"OBJECT NOT FOUND")) {
				GeneralPaginationXpath = "//div[@id='myTable_paginate' or @class='pagination']";// for
																								// admin
			}
			CompareText("PageInfoPage" + CurrentPage, LangData("PreviousPage")
					+ " " + LangData("Page") + " " + LangData("Of") + " "
					+ ExpectedPageCount + " " + LangData("NextPage"),
					GetText(GeneralPaginationXpath));
			// ========================First Page Ends ========================

			// ========================Second Page Starts
			// ========================
			if (TotalPages >= 2) { // Only when there are 2 or more pages
				ClearStaticScreenshot();
				// Navigate to Second Page by clicking the next page link
				Lnk_Click(XpathNextPage);
				TakeStaticScreenshot("SecondPage");
				CurrentPage = 2;

				if (isRecordInfoPresent) {
					RecordStartingNumber = ((CurrentPage - 1) * 10) + 1;
					TotalRecordsInCurrentPage = WbTbl_GetRowcount(TableObject);
					if (TotalRecordsInCurrentPage < 10) {
						RecordEndingNumber = RecordStartingNumber
								+ (TotalRecordsInCurrentPage - 1);
					} else {
						RecordEndingNumber = RecordStartingNumber + 9;
					}
					Expected = "(" + RecordStartingNumber + To
							+ RecordEndingNumber + " " + LangData("Of") + " "
							+ TotalRecords + " " + LangData("Records") + ")";
					CompareText("RecordInfoPage" + CurrentPage + "_EB",
							Expected, GetText(RecordInfoObject));
				}

				// Check if previous page link is enabled in second page
				PrevPageClass = GetAttributeValue(XPathPrevPage, "class")
						.toLowerCase();
				;
				NextPageClass = GetAttributeValue(XpathNextPage, "class")
						.toLowerCase();
				;
				if ((PrevPageClass.endsWith("enabled"))
						|| (PrevPageClass.endsWith("active"))) {
					Report.Update("PreviousLinkPage" + CurrentPage,
							"Previous page link should be enabled",
							"Previous page link is enabled", "Pass");
				} else {
					Report.Update("PreviousLinkPage" + CurrentPage,
							"Previous page link should be enabled",
							"Previous page link is not enabled", "Fail");
				}

				if (TotalPages == 2) {// When only 2 pages are present, verify
										// that next page is disabled in second
										// page
					if ((NextPageClass.endsWith("disabled"))
							|| (NextPageClass.endsWith("error"))) {
						Report.Update("NextLinkPage" + CurrentPage,
								"Next page link should be disabled",
								"Next page link is disabled", "Pass");
					} else {
						Report.Update(
								"NextLinkPage" + CurrentPage,
								"Next page link should be disabled",
								"Next paghttps://integ1.easypodconnect2.com/ecko/admin/globalCountryListe link is not disabled",
								"Fail");
					}
				} else {
					if ((NextPageClass.endsWith("enabled"))
							| (NextPageClass.endsWith("active"))) {
						Report.Update("NextLinkPage" + CurrentPage,
								"Next page link should be enabled",
								"Next page link is enabled", "Pass");
					} else {
						Report.Update("NextLinkPage" + CurrentPage,
								"Next page link should be enabled",
								"Next page link is not enabled", "Fail");
					}
				}

				CompareText("CurrentPage" + CurrentPage + "_EB",
						String.valueOf(CurrentPage),
						Edit_GetValue(XpathCurrentPageEditBox));
				CompareText("PageInfoPage" + CurrentPage,
						LangData("PreviousPage") + " " + LangData("Page") + " "
								+ LangData("Of") + " " + ExpectedPageCount
								+ " " + LangData("NextPage"),
						GetText(GeneralPaginationXpath));

				// ========================Second Page Ends
				// ========================

				// ========================Last Page Starts
				// ========================
				if (TotalPages >= 3) { // Only when there are 3 or more pages
					ClearStaticScreenshot();

					// Navigate to the last page by entering the last page
					// number
					Edit_SetValue(XpathCurrentPageEditBox, TotalPages);
					Wait(2);
					WaitForPageload();
					TakeStaticScreenshot("LastPage");
					CurrentPage = TotalPages;

					if (isRecordInfoPresent) {
						RecordStartingNumber = ((CurrentPage - 1) * 10) + 1;
						TotalRecordsInCurrentPage = WbTbl_GetRowcount(TableObject);
						if (TotalRecordsInCurrentPage < 10) {
							RecordEndingNumber = RecordStartingNumber
									+ (TotalRecordsInCurrentPage - 1);
						} else {
							RecordEndingNumber = RecordStartingNumber + 9;
						}
						Expected = "(" + RecordStartingNumber + To
								+ RecordEndingNumber + " " + LangData("Of")
								+ " " + TotalRecords + " "
								+ LangData("Records") + ")";
						CompareText("RecordInfoPage" + CurrentPage + "_EB",
								Expected, GetText(RecordInfoObject));
					}

					// Check if previous page link is enabled in last page
					PrevPageClass = GetAttributeValue(XPathPrevPage, "class")
							.toLowerCase();
					;
					NextPageClass = GetAttributeValue(XpathNextPage, "class")
							.toLowerCase();
					;
					if ((PrevPageClass.endsWith("enabled"))
							|| (PrevPageClass.endsWith("active"))) {
						Report.Update("PreviousLinkPage" + CurrentPage,
								"Previous page link should be enabled",
								"Previous page link is enabled", "Pass");
					} else {
						Report.Update("PreviousLinkPage" + CurrentPage,
								"Previous page link should be enabled",
								"Previous page link is not enabled", "Fail");
					}

					// Verify that next page is disabled in last page
					if ((NextPageClass.endsWith("disabled"))
							|| (NextPageClass.endsWith("error"))) {
						Report.Update("NextLinkPage" + CurrentPage,
								"Next page link should be disabled",
								"Next page link is disabled", "Pass");
					} else {
						Report.Update("NextLinkPage" + CurrentPage,
								"Next page link should be disabled",
								"Next page link is not disabled", "Fail");
					}

					CompareText("CurrentPage" + CurrentPage + "_EB",
							String.valueOf(CurrentPage),
							Edit_GetValue(XpathCurrentPageEditBox));
					CompareText("PageInfoPage" + CurrentPage,
							LangData("PreviousPage") + " " + LangData("Page")
									+ " " + LangData("Of") + " "
									+ ExpectedPageCount + " "
									+ LangData("NextPage"),
							GetText(GeneralPaginationXpath));
				}
				// ========================Last Page Starts
				// ========================
				ClearStaticScreenshot();
				// Error validation in last page
				Edit_SetValue(XpathCurrentPageEditBox, TotalPages + 1);
				Wait(2);
				WaitForPageload();
				TakeStaticScreenshot("LastPageErr");
				CompareText("GreaterThanTotalPagesErr_EB",
						String.valueOf(TotalPages),
						Edit_GetValue(XpathCurrentPageEditBox));

			}

		} catch (Exception e) {
			Env.logger.log(Level.ERROR,
					"Error occured while CheckPaginationFunctioanlity", e);
			Report.Update("CheckPaginationFunctioanlity",
					"There should be no error",
					"Error occured while CheckPaginationFunctioanlity. ",
					"Fail");
		} finally {
			try {
				ClearStaticScreenshot();
				Edit_SetValue(XpathCurrentPageEditBox, 1); // Go Back to the
															// first page
			} catch (Exception ignore) {
			}
		}

	}

	public String GetAge(String dateOfBirth) {

		dateOfBirth = dateOfBirth.trim().replaceAll("-", " ");
		DateFormat df = new SimpleDateFormat("dd MMM yyyy");
		Date startDate = null;
		try {
			startDate = df.parse(dateOfBirth);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Calendar today = Calendar.getInstance();
		Calendar birthDate = Calendar.getInstance();

		int age = 0;

		birthDate.setTime(startDate);
		if (birthDate.after(today)) {
			throw new IllegalArgumentException("Can't be born in the future");
		}

		age = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);

		// If birth date is greater than todays date (after 2 days adjustment of
		// leap year) then decrement age one year
		if ((birthDate.get(Calendar.DAY_OF_YEAR)
				- today.get(Calendar.DAY_OF_YEAR) > 3)
				|| (birthDate.get(Calendar.MONTH) > today.get(Calendar.MONTH))) {
			age--;

			// If birth date and todays date are of same month and birth day of
			// month is greater than todays day of month then decrement age
		} else if ((birthDate.get(Calendar.MONTH) == today.get(Calendar.MONTH))
				&& (birthDate.get(Calendar.DAY_OF_MONTH) > today
						.get(Calendar.DAY_OF_MONTH))) {
			age--;
		}

		return Integer.toString(age);
	}

	public String GetUploadWeeks(String PatientName, String ReturnType) {
		String UploadOver;

		try {
			Lnk_Click("Header_MyAccount_LK");
			Lnk_Click("MyAccount_UploadThresholdEdit_LK");
			UploadOver = GetAttributeValue("MyAccount_DataUploadOverValue_SB",
					"value");
			Btn_Click("MyAccount_UploadDataSave_ST");

		} catch (Exception e) {
			Env.logger.log(Level.ERROR,
					"Error occured while getting upload weeks for patient "
							+ PatientName, e);
			UploadOver = null;

		}
		return UploadOver;
	}

	public String GetTreatmentStartDate(String PatientName) {
		String TreatmentStartDate = "";

		try {
			if (!ObjectExists("NavigationTab_ProfileTab_LK")) {
				NavigateTo(Env.ECKOURL);
				SearchForPatient(PatientName);
				Btn_Click("MyPatients_ViewProfile_LK");
				TreatmentStartDate = GetText("ViewProfile_TreatStartDateValue_ST");
			} else {
				Lnk_Click("NavigationTab_ProfileTab_LK");
				TreatmentStartDate = GetText("ViewProfile_TreatStartDateValue_ST");
			}
		} catch (Exception e) {
			Env.logger.log(Level.ERROR,
					"Error occured while getting Treatment Start date for patient "
							+ PatientName, e);
			TreatmentStartDate = null;
		}

		return TreatmentStartDate;
	}

	public String GetPatientDOB(String PatientName) {
		String PatientDOB = "";

		try {
			if (!ObjectExists("NavigationTab_ProfileTab_LK")) {
				NavigateTo(Env.ECKOURL);
				SearchForPatient(PatientName);
				Btn_Click("MyPatients_ViewProfile_LK");
				PatientDOB = GetText("ViewProfile_PDDOBValue_ST");
			} else {
				Lnk_Click("NavigationTab_ProfileTab_LK");
			}

		} catch (Exception e) {
			Env.logger.log(Level.ERROR,
					"Error occured while getting Patient DOB for "
							+ PatientName, e);
			PatientDOB = null;
		}

		return PatientDOB;
	}

	public String GetPatientConsentDate(String PatientName) {
		String ConsentDate = "";
		try {

			if (!ObjectExists("NavigationTab_ProfileTab_LK")) {
				NavigateTo(Env.ECKOURL);
				SearchForPatient(PatientName);
				Btn_Click("MyPatients_ViewProfile_LK");
				ConsentDate = GetText("ViewProfile_ConsentDateValue_ST");
			} else {
				Lnk_Click("NavigationTab_ProfileTab_LK");
			}
		} catch (Exception e) {
			Env.logger.log(Level.ERROR,
					"Error occured while getting Patient DOB for "
							+ PatientName, e);
			ConsentDate = null;
		}

		return ConsentDate;
	}

	public void VerifyLastUploadDate(String ObjectName,
			String ExpectedLastUploadDateInLocale,
			String ExpectedCriticalWeeks, boolean IsDevicePresent) {// ,String
																	// ExpectedCriticalWeeks){

		// icon verification
		String IconName = null;
		String ActualIcon = null;
		String ExpectedMessage = "";
		String ExpectedLastUploadDate = TranslateDate(
				ExpectedLastUploadDateInLocale, "dd MMM yyyy", true);
		int TotalWeeks = DateDiff(ExpectedLastUploadDate,
				GetTimeStamp("dd MMM yyyy"), "WEEKS");
		int CriticalWeek = Integer.parseInt(ExpectedCriticalWeeks);
		try {
			Lnk_Click(ObjectName);

			String Icon = GetAttributeValue(
					ORXPath("AdherenceOverview_DataLastUploadValue_DT")
							+ "/span", "class");
			if (Icon.startsWith("roundIcn")) {
				IconName = "WithinRange";
			} else if (Icon.startsWith("iconRed")) {
				IconName = "Critical";
			}

			if ((TotalWeeks == CriticalWeek) || (TotalWeeks < CriticalWeek)) {
				ActualIcon = "WithinRange";
				VerifyRegExpression("BelowWeeks", LangData("DataLastUpload")
						+ " : " + LangData("BelowWeeks"),
						GetText("AdherenceOverview_DataLastUpload_DT"),
						"\\d{1,2}");
			} else if (TotalWeeks > CriticalWeek) {// &&TotalWeeks<CriticalWeek
													// ||TotalWeeks==CriticalWeek
				ActualIcon = "Critical";
				VerifyRegExpression("OverWeeksAgo", LangData("DataLastUpload")
						+ " : " + LangData("OverWeeksAgo"),
						GetText("AdherenceOverview_DataLastUpload_DT"),
						"\\d{1,2}");

			}

			// Icon Verification
			CompareText("Icon", IconName, ActualIcon);

			if (IsDevicePresent) {
				ExpectedMessage = " (" + ExpectedLastUploadDateInLocale + ")";
			} else {
				ExpectedMessage = " -";
			}

			// Text Verification
			CompareText("LastUploadDate", ExpectedMessage,
					GetOnlyMyText("AdherenceOverview_DataLastUploadValue_DT"));
			HideCursor();
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, "Error ", e);

		}
	}

	public String TranslateDate(String DateToTranslate, String DateFormat,
			boolean ConvertToEnglish) {
		try {
			SimpleDateFormat OtherFormat = new SimpleDateFormat(DateFormat,
					GetLocale());
			SimpleDateFormat EnglishFormat = new SimpleDateFormat(DateFormat,
					new Locale("en", "US"));
			if (ConvertToEnglish) {
				Date MyDate = OtherFormat.parse(DateToTranslate);
				return EnglishFormat.format(MyDate);
			} else {
				Date MyDate = EnglishFormat.parse(DateToTranslate);
				return OtherFormat.format(MyDate);
			}
		} catch (Exception e) {
			return null;
		}

	}

	public void RemoveFooterFile(int RowIndex) {
		WbTbl_SelectRbtn("GlobalAdminList_AdminTable_TE", RowIndex);
		if (Btn_IsEnabled("GlobalAdminSettings_Remove_BN")) {
			Btn_Click("GlobalAdminSettings_Remove_BN");
			WaitForObject("GlobalAdminSettings_RemoveYes_BN");
			Btn_Click("GlobalAdminSettings_RemoveYes_BN");
		}
	}

	public boolean UploadFooterFile(int RowIndex, String FileName) {
		boolean ReturnValue = true;
		WbTbl_SelectRbtn("GlobalAdminList_AdminTable_TE", RowIndex);
		String FileStatus = WbTbl_GetText("GlobalAdminList_AdminTable_TE",
				RowIndex, 3);
		if (CompareText(FileStatus, "Not Available")
				|| "|1|2|12|".contains("|" + String.valueOf(RowIndex) + "|")) {// ignore
																				// if
																				// the
																				// file
																				// is
																				// already
																				// present
																				// except
																				// terms,
																				// license
																				// and
																				// Features
			Btn_Click("GlobalAdminSettings_Upload_BN");
			WaitForPageload();
			FileUpload_SetFile("CountrySettings_Browse_BN", GetDataPath()
					+ FileName);
			Btn_Click("CountrySettings_FileUploadSubmit_BN");
			WaitForPageload();
			if (browser.getTitle().startsWith("Error")) {
				browser.navigate().back();
				Env.logger.log(Level.ERROR,
						"Footer files was not uploaded correctly");
				ReturnValue = false;
			}
		}
		return ReturnValue;
	}

	public String Converter(String StringToFormat) {
		final Locale CurrentLocale;
		String ReturnValue = " ";
		String Number = "0.5";
		CurrentLocale = new Locale(GetLanguageCode(Env.LanguageUsed, true),
				GetCountryCode(Env.CountryUsed, true));
		NumberFormat nf = NumberFormat.getInstance(CurrentLocale);
		try {
			String NumberInLocale = nf.format(Float.parseFloat(Number))
					.toString();
			if (NumberInLocale.contains(",")) {
				ReturnValue = StringToFormat.replace(".", ",");
			} else {
				ReturnValue = StringToFormat;
			}
			return ReturnValue;
		} catch (Exception e) {
			return null;
		}
	}

	public void SwitchActiveBrowser() {
		if (Secondbrowser != null) {
			String CurrentURL = null;
			// Set the Currently active browser to temp
			WebDriver Temp = browser;

			// Switch the browser
			browser = Secondbrowser;
			Secondbrowser = Temp;

			// Update the instance variables
			wait = new WebDriverWait(browser, Env.SyncTimeOut);
			Env.BrowserObj = browser;
			UserAction = new Actions(browser);

			try {
				CurrentURL = GetCurrentURL();

				// Update the Active Browser
				if (ActiveBrowser == 1) {
					ActiveBrowser = 2;
				} else {
					ActiveBrowser = 1;
				}

				// Update AppModule
				if (CurrentURL.toUpperCase().contains("/ECKO/")) {
					Env.AppModule = "ECKO";
				} else {
					Env.AppModule = "ECOS";
				}
				Env.logger.log(Level.INFO, "Active Browser is switched to "
						+ ActiveBrowser);
			} catch (WebDriverException e) {
				if (e.getCause().getMessage()
						.endsWith("was terminated due to TIMEOUT")) {
					Env.logger.log(Level.FATAL, "Browser Index "
							+ ActiveBrowser + " was closed due to timeout.");
				} else {
					throw e;
				}
			}

		} else {
			Env.logger.log(Level.FATAL,
					"Second Browser is null. Unable to switch");
		}
	}

	public void CalendarPaginationStatus(String ObjectName, String LogicalName,
			String Status) {

		boolean LinkStatus = false;
		String LinkAttributeValue = GetAttributeValue(ObjectName, "class");
		if (Status.toUpperCase().trim().startsWith("DISABLED")) {
			LinkStatus = LinkAttributeValue.endsWith("disabled");
		} else {
			LinkStatus = !LinkAttributeValue.endsWith("disabled");
		}

		if (LinkStatus) {
			Report.Update(ObjectName, LogicalName + " 6 months link should be "
					+ Status, LogicalName + " 6 months link is " + Status,
					"Pass");
		} else {
			Report.Update(ObjectName, LogicalName + " 6 months link should be "
					+ Status, LogicalName + " 6 months link is not " + Status,
					"Fail");
		}
	}

	public String[] GetEP1WeeklyAdherenceValues() {
		String[] EP1WeeklyAdherence = { "32.50%", "69.23%", "0.00%", "0.00%",
				"0.00%" };
		String[] EP1WeeklyAdherenceMon = { "32.50%", "64.29%", "0.00%",
				"0.00%", "0.00%" }; // Weekly Adherence for all the weeks in
									// March 2012 starts with Monday
		if (IsWeekStartingMonday()) {
			EP1WeeklyAdherence = EP1WeeklyAdherenceMon;
			EP1WeeklyAdherenceMon = null;
		}
		return EP1WeeklyAdherence;
	}

	public String[] GetEP1MonthlyAdherenceValues() {
		String[] EP1MonthlyAdherence = { "100.00%", "100.00%", "100.00%",
				"100.00%", "100.00%", "100.00%", "100.00%", "100.00%",
				"41.38%", "0.00%", "0.00%", "3.23%", "3.23%", "0.00%", "17.67%" }; // Jan
																					// 2011
																					// to
																					// mar
																					// 2012
		return EP1MonthlyAdherence;
	}

	public String[] GetEP2MonthlyAdherenceValues() {

		final Country switchCountryName;
		String[] EP2MonthlyAdherence = new String[12];
		switchCountryName = Country.valueOf(Env.CountryUsed.replaceAll(" ", "")
				.toUpperCase());
		switch (switchCountryName) {
		case SWEDEN:
		case ITALY:
		case SPAIN:
		case CZECHREPUBLIC:
		case GREECE:
		case PORTUGAL:
		case FRANCE:
			EP2MonthlyAdherence = new String[] { "7.12%", "12.68%", "0.00%",
					"14.25%", "0.00%", "5.00%", "0.00%", "0.00%", "0.00%",
					"0.00%", "0.00%", "0.00%" };// jan 2013 to Dec 2013
			break;
		case CANADA:
			EP2MonthlyAdherence = new String[] { "7.12%", "13.41%", "0.00%",
					"14.25%", "0.00%", "5.00%", "0.00%", "0.00%", "0.00%",
					"0.00%", "0.00%", "0.00%" };// jan 2013 to Dec 2013
			break;
		case GERMANY:
			EP2MonthlyAdherence = new String[] { "25.00%", "12.68%", "0.00%",
					"14.25%", "0.00%", "5.00%", "0.00%", "0.00%", "0.00%",
					"0.00%", "0.00%", "0.00%" };// jan 2013 to Dec 2013
			break;
		default:
			EP2MonthlyAdherence = new String[] { "7.12%", "12.68%", "0.00%",
					"14.25%", "0.00%", "5.00%", "0.00%", "0.00%", "0.00%",
					"0.00%", "0.00%", "0.00%" };// jan 2013 to Dec 2013
			break;
		}
		return EP2MonthlyAdherence;
	}

	public boolean REGCheckAdherencePopup(String EventDate, String EventName,
			String ExpectedRegime, String ExpectedTime, String DeviceID,
			String ExpectedInjectedDose, String ExpectedPrescribedDose,
			String RowToCheck) {
		try {
			if (EventDate != "31-FEB-2000") {
				String DateValue = null;
				WebElement ActionObject = GetObject("#AdherenceCalendar_Popup_TB");
				String GetMonthFullName = GetMonthName(EventDate);
				String[] DateSplit = EventDate.split("-");
				DateValue = DateSplit[0];
				if (DateSplit[0].substring(0, 1).contains("0")) {
					DateValue = DateSplit[0].replaceFirst("0", "");
				}
				String ExpectedDate = LangData("Date") + " : " + DateValue
						+ "-" + GetMonthFullName + "-" + DateSplit[2];
				CompareText("AdherenceCalendar_LegendPopupTitleDate_ST",
						ExpectedDate,
						GetText("AdherenceCalendar_LegendPopupTitleDate_ST"));
				CompareText("AdherenceCalendar_LegendPopupTitleRegime_ST",
						LangData("Regime") + " : " + ExpectedRegime,
						GetText("AdherenceCalendar_LegendPopupTitleRegime_ST"));
				if (DeviceID.startsWith("MS")) {
					CompareText(
							"#AdherenceCalendar_PopupTime_TB",
							ExpectedTime,
							GetText(ActionObject.findElement(By
									.xpath("tbody/tr[" + RowToCheck + "]/td[1]"))));
				}
				CompareText(
						"#AdherenceCalendar_PopupEventName_TB",
						EventName,
						GetText(ActionObject.findElement(By.xpath("tbody/tr["
								+ RowToCheck + "]/td[2]"))));
				CompareText(
						"#AdherenceCalendar_PopupDeviceID_TB",
						DeviceID,
						GetText(ActionObject.findElement(By.xpath("tbody/tr["
								+ RowToCheck + "]/td[3]"))));
				CompareText(
						"#AdherenceCalendar_PopupInjectedDose_TB",
						ExpectedInjectedDose,
						GetText(ActionObject.findElement(By.xpath("tbody/tr["
								+ RowToCheck + "]/td[4]"))));
				CompareText("#AdherenceCalendar_PopupPrescribedDose_TB",
						LangData("PrescribedDoseMg") + " : "
								+ ExpectedPrescribedDose,
						GetText("AdherenceCalendar_PrescribedDose_ST"));
				return true;
			} else {
				Env.logger.log(Level.ERROR, EventName + " event not present");
				return false;
			}
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, EventDate, e);
			return false;
		}

	}

	public String GenerateUniqueID() {
		return GetTimeStamp("MMddHHmmss");
	}

	public String CountryAll() {
		String ValueList = "AU|AT|CA|CZ|EE|FI|FR|DE|GR|HU|IS|IE|IT|KR|LV|NO|PT|SK|ES|SE|CH|GB|CL|BR|EC|GT|HK|MY|MX|SA|SG|AE|VE";// Wave1
																																// to
																																// Wave4
																																// countries.
		ValueList = ValueList + "|AR|CO|ID|TW|PE|RS|ME|TH|SV|HN|NI|TT|DO|PH|VN"; // Adding
																					// wave5
																					// countries.
		if (Env.RunEnvironment.trim().toUpperCase().startsWith("LOCAL")) {
			ValueList = ValueList + "|IN"; // Allowing India only in the Local
											// Test Server
			ValueList = ValueList + "|CN|PL|RU|TR|UA";// Existing Country codes
														// which are in
														// production.
		}
		ValueList = TranslateToLocal(ValueList, "Country.", false);
		return ValueList;
	}

	public String CountryCodeAll() {
		String ValueList = "AU|AT|CA|CZ|EE|FI|FR|DE|GR|HU|IS|IN|IE|IT|KR|LV|NO|PT|SK|ES|SE|CH|GB|CL|BR|EC|GT|HK|MY|MX|SA|SG|AE|VE"; // Wave1
																																	// to
																																	// Wave4
																																	// countries.
		ValueList = ValueList + "|AR|CO|ID|TW|PE|RS|ME|TH|SV|HN|NI|TT|DO|PH|VN"; // Adding
																					// wave5
																					// countries.
		ValueList = ValueList + "|CN|PL|RU|TR|UA";// Existing Country codes
													// which are in production.
		ValueList = TranslateToLocal(ValueList, "Country.", false);
		return ValueList;
	}

	public String DayLongAll() {
		String ValueList = "1|2|3|4|5|6|7";
		ValueList = TranslateToLocal(ValueList, "DayLong", false);
		return ValueList;
	}

	public String DayMaxAll() {
		String ValueList = "1|2|3|4|5|6|7";
		ValueList = TranslateToLocal(ValueList, "DayMax", false);
		return ValueList;
	}

	public String DayMinAll() {
		String ValueList = "1|2|3|4|5|6|7";
		ValueList = TranslateToLocal(ValueList, "DayMin", false);
		return ValueList;
	}

	public String MonthLongAll() {
		String ValueList = "1|2|3|4|5|6|7|8|9|10|11|12";
		ValueList = TranslateToLocal(ValueList, "MonthLong", false);
		return ValueList;
	}

	public String MonthShortAll() {
		String ValueList = LangData("MonthShortAllRaw");
		ValueList = ValueList.replaceAll(", ]", "]");// for Iceland
		ValueList = ValueList.replaceAll(" ", "");
		ValueList = ValueList.replaceAll("','", ",");
		ValueList = ValueList.replaceAll(",", "|");
		ValueList = ValueList.replaceAll("']", "");
		ValueList = ValueList.replaceAll("\\['", "");
		ValueList = ValueList.replaceAll("\\[", "");// for latvia
		ValueList = ValueList.replaceAll("',]", ""); // For an Extra comma after
														// dec
		ValueList = ValueList.replaceAll("\\]", "");// for Latvia
		return ValueList;
	}

	public boolean VerifyMonth(String ObjectName, String ActualDate) {

		// Date Format: 01 Jan 2012 or 01-Jan-2012
		boolean IsPass;
		try {
			// Clean the Characters
			if (ActualDate.contains(":"))
				ActualDate.replaceAll(":", "");

			// Replace all double or more spaces with a single space
			ActualDate = ActualDate.replaceAll("  {2,}", " ");

			// Get the Expected List and Compare
			String Expected = MonthShortAll();
			IsPass = CompareWithList(ObjectName, Expected, ActualDate);
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, ObjectName, e);
			IsPass = false;
		}

		return IsPass;
	}

	public boolean VerifyMonth(String ObjectName) {
		String ActualDate = GetText(ObjectName);

		// Handling for EditBox
		WebElement ActionObject = GetObject(ObjectName);
		if (ActionObject.getTagName().toUpperCase().startsWith("INPUT")) {
			if (ActionObject.getAttribute("type").toUpperCase()
					.startsWith("TEXT")) {
				ActualDate = ActionObject.getAttribute("value");
			}
		}

		return VerifyMonth(ObjectName, ActualDate);
	}

	public void UploadDeviceData(String DeviceID, String ClinicName,
			String HCPLastFirstName, String FilePath) {

		try {

			File CommonHistoryFile = null;
			String EP1FilePath = FilePath
					+ "DeviceData/EP1Devices/CandHFormat/"; // Default path of
															// EP1 Devices
			String EP2FilePath = FilePath + "DeviceData/EP2Devices/" + DeviceID
					+ "/"; // Default path of EP2 Devices
			String FileNameforEDH = "CommonHistoryFile.edh";
			if (!CompareText(FilePath, GetDataPath())) {
				EP1FilePath = FilePath;
				EP2FilePath = FilePath;
				FileNameforEDH = DeviceID + ".edh";
			}

			// To navigate to Upload Device Data page if not in the right page
			if ((!GetBrowserTitle().startsWith(LangData("UploadDeviceData")))) {
				NavigateTo(Env.ECKOURL);
				SearchForCountry(Env.CountryUsed);
				Rbtn_SetOption("CountryList_FirstRadio_RB", "ON");
				Btn_Click("CountryList_Devices_BN");
				Lnk_Click("GlobalAdmin_UploadDeviceData_LK");
			}

			if (DeviceID.trim().toUpperCase().startsWith("MS")) {
				// Upload EP1 device
				Rbtn_SetOption("GlobalAdmin_UploadDeviceEP1_RB", "ON");
				FileUpload_SetFile("GlobalAdmin_ConfigurationFileBrowse_BN",
						GetDataPath() + "DeviceData/EP1Devices/CandHFormat/"
								+ DeviceID + ".edc"); // Always default path for
														// EDC file
				CommonHistoryFile = new File(EP1FilePath + FileNameforEDH);
				FileUpload_SetFile("GlobalAdmin_HistoryFileBrowse_BN",
						CommonHistoryFile.getAbsolutePath());
			} else {
				// Upload EP2device
				Rbtn_SetOption("GlobalAdmin_UploadDeviceEP2_RB", "ON");
				FileUpload_SetFile("GlobalAdmin_EDSFileBrowse_BN", EP2FilePath
						+ "SetLog_" + DeviceID + ".eds");
				FileUpload_SetFile("GlobalAdmin_EDBFileBrowse_BN", EP2FilePath
						+ "BinHistLog_" + DeviceID + ".edb");
				FileUpload_SetFile("GlobalAdmin_EDTFileBrowse_BN", EP2FilePath
						+ "TechLog_" + DeviceID + ".edt");
			}
			Btn_Click("GlobalAdmin_UploadDeviceDataSubmit_BN");

			if (ObjectExists("GlobalAdmin_AlreadyAssociatedSubmit_BN")) {
				Btn_Click("GlobalAdmin_AlreadyAssociatedSubmit_BN");
			} else {
				DrpDwn_SelectText("GlobalAdmin_Clinic_DD", ClinicName);
				Wait(3);
				if (HCPLastFirstName.startsWith(TestData("Index"))) {
					Edit_SetValue("HCPList_SearchText_EB", HCPLastFirstName);
				} else {
					Edit_SetValue("HCPList_SearchText_EB", TestData("Index")
							+ ", " + HCPLastFirstName);
				}
				Btn_Click("HCPList_Search_BN");
				Wait(2);
				Rbtn_SetOption("GlobalAdmin_SelectHCP_RB", "ON");
				Btn_Click("GlobalAdmin_HCPContinue_BN");
			}
			VerifyRegExpression("GlobalAdmin_DeviceDataSuccessMsg_DT",
					LangData("DeviceDataUploadedSuccessfully"),
					GetText("GlobalAdmin_DeviceDataSuccessMsg_DT"), DeviceID);
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, "Unable to Upload the Device : "
					+ DeviceID, e);
		}

	}

	public String FormatPatientName(String PatientName) {
		if (!PatientName.contains(", ") && PatientName.contains(" ")) {
			PatientName = PatientName.replace(" ", ", ");
		}
		return PatientName;
	}

	public String GetDialCodeFromText(String TextContainingDialCode,
			boolean ReplaceBrackets, boolean ReplaceSpaces) {
		String Output = TextContainingDialCode;

		if (ReplaceBrackets) {
			Output = TextContainingDialCode.split(" \\(")[1];
			Output = Output.substring(0, Output.length());
		} else {
			Output = "( " + TextContainingDialCode.split(" \\(")[1];
		}

		if (ReplaceSpaces) {
			Output = Output.replaceAll("\\n", ""); // Replace newline character
			Output = Output.replaceAll(" {2,}", " "); // Replace 2 or more
														// spaces with one space
			Output = Output.replaceAll(" ", "");
		}

		return Output.trim();
	}

	public boolean RegisterNewDevice(String DeviceID, String PatientName,
			String DeviceStatus, String DeviceUsageStartDate) {
		if (!GetBrowserTitle().startsWith(LangData("UnassignedDevices"))) {
			Btn_Click("NavigationTab_DevicesTab_ST"); // If not in Unassinged
														// Devices tab, then
														// click the Devices tab
			WaitForPageload();
		}
		Btn_Click("PatientAssignDevice_AddNewDevice_BN");
		SearchForPatient(PatientName);
		Rbtn_SetOption("DevicesAssignDevice_SelectPatient_RB", "ON");
		Btn_Click("DevicesAssignDevice_Continue_BN");

		Edit_SetValue("RegisterDevice_DeviceID1_EB", DeviceID.substring(0, 4));
		Edit_SetValue("RegisterDevice_DeviceID2_EB", DeviceID.substring(4, 7));
		Edit_SetValue("RegisterDevice_DeviceID3_EB", DeviceID.substring(7));

		if (DeviceStatus.startsWith(LangData("DevicePrimary"))) {
			Rbtn_SetOption("AssignedDevices_Primary_RB", "ON");
		} else {
			Rbtn_SetOption("AssignedDevices_Secondary_RB", "ON");
		}
		SelectDate("RegisterDevice_Calendar_LK", DeviceUsageStartDate); // Date_AddDays(TestData("ECOSPatientDOB"),
																		// 1,Calendar.YEAR)
		Btn_Click("DeviceRegisterDevice_Submit_BN");
		String ActualText = GetText("AssignDevice_AssignedtoSuccessMsg_DT");

		String ExpectedText = LangData("AssignedToSmall");
		ExpectedText = ExpectedText.replace("{0}", DeviceID);
		ExpectedText = ExpectedText.replace("{1}",
				FormatPatientName(PatientName));
		ExpectedText = ExpectedText.replace("{2}", DeviceStatus);
		ExpectedText = ExpectedText.replace("{3}",
				TranslateMonth(DeviceUsageStartDate));

		if (CompareText(ExpectedText, ActualText)) {
			Env.logger.log(Level.INFO, DeviceID
					+ " is Registered successfully to " + PatientName);
			return true;
		} else {
			Env.logger.log(Level.WARN, "Unable to Register the device to "
					+ PatientName);
			return false;
		}

	}

	public void VerifyEP2DeviceDataInAdherenceCalendar(String EP2DeviceID) {

		String CompleteInjection = LangData("CompleteInjection");
		String NewCartridge = LangData("NewCartridge");
		String NewDosageSet = LangData("NewDosageSet");

		String EP2FromDate = "01 Jan 2013";
		String EP2ToDate = "31 Dec 2013";
		String EP2CustomToDate = "31 Jan 2013";
		String EP2Multiple_Date = "22-JAN-2013";

		String Expected = "";
		String[] EP2MonthlyAdherence = GetEP2MonthlyAdherenceValues();

		if (Env.AppModule.startsWith("ECKO")) {

			// =========================================================
			// Verifying Injection Count and Adherence popup for EP2 Device
			// ============================================
			DrpDwn_SelectIndex("AdherenceOverview_Range_DD", 5);
			SelectMonth("Adherence_CustomDateFrom_TT", EP2FromDate);
			SelectMonth("Adherence_CustomDateTo_TT", EP2CustomToDate);
			Btn_Click("Adherence_Submit_BN");

			// Verify the values in each injection event popup
			int ExpectedCount = 1;
			int ActualCount = browser.findElements(
					By.xpath(ORXPath("AdherenceCalendar_MultipleLegends_LK")))
					.size();
			CompareText("MultipleInjectionCount",
					String.valueOf(ExpectedCount), String.valueOf(ActualCount));//
			Lnk_Click("AdherenceCalendar_MultipleLegends_LK");
			REGCheckAdherencePopup(EP2Multiple_Date, NewDosageSet, "6",
					"00:59", EP2DeviceID, "1.1", "1.1 mg", "1");
			REGCheckAdherencePopup(EP2Multiple_Date, NewDosageSet, "6",
					"00:59", EP2DeviceID, "1.1", "1.1 mg", "2");
			REGCheckAdherencePopup(EP2Multiple_Date, NewCartridge, "6",
					"01:09", EP2DeviceID, "8.65", "1.1 mg", "3");
			REGCheckAdherencePopup(EP2Multiple_Date, CompleteInjection, "6",
					"01:12", EP2DeviceID, "1.1", "1.1 mg", "4");
			REGCheckAdherencePopup(EP2Multiple_Date, CompleteInjection, "6",
					"01:12", EP2DeviceID, "1.1", "1.1 mg", "5");
			Btn_Click("AdherenceCalendar_LegendClose_BN");
			// =========================================================
			// Verifying Injection Count and Adherence popup ends for EP2 Device
			// ============================================

			// =========================================================
			// Verifying Monthly Adherence values starts for EP2Device
			// ===========================================
			// First 6 Months(Jan-Jun)
			SelectMonth("AdherenceCalendar_CustomDateFrom_TT", EP2FromDate);
			SelectMonth("AdherenceCalendar_CustomDateTo_TT", EP2ToDate);
			Btn_Click("Adherence_Submit_BN");
			VerifyTextContained(
					"AdherenceCalendar_InjectionReportFromMonth_ST",
					LangData("MonthLong1"),
					GetText("AdherenceCalendar_InjectionReportFromMonth_ST"));
			VerifyTextContained(
					"AdherenceCalendar_InjectionReportFromMonth_ST",
					LangData("MonthLong6"),
					GetText("AdherenceCalendar_InjectionReportFromMonth_ST"));

			// (Jan-Jun)
			for (int MonthCount = 1; MonthCount <= 6; MonthCount++) {
				String MonthNameXpath = "//div[@id='itemContainer']//div["
						+ MonthCount + "]/h4/span[1]";
				String AdherenceXpath = "//div[@id='itemContainer']//div["
						+ MonthCount + "]/h4//strong";
				Expected = LangData("MonthLong" + MonthCount);
				CompareText("EP2MonthName" + MonthCount, Expected + " 2013",
						GetText(MonthNameXpath));
				CompareText("EP2MonthlyAdherenceValue" + MonthCount,
						Converter(EP2MonthlyAdherence[MonthCount - 1]),
						GetText(AdherenceXpath));
			}

			// (Jul-Dec)
			Lnk_Click("AdherenceCalendar_Next_LK");
			for (int MonthCount = 7; MonthCount <= 12; MonthCount++) {
				String MonthNameXpath = "//div[@id='itemContainer']//div["
						+ MonthCount + "]/h4/span[1]";
				String AdherenceXpath = "//div[@id='itemContainer']//div["
						+ MonthCount + "]/h4//strong";
				Expected = LangData("MonthLong" + MonthCount);
				CompareText("EP2MonthName" + MonthCount, Expected + " 2013",
						GetText(MonthNameXpath));
				CompareText("EP2MonthlyAdherenceValue" + MonthCount,
						Converter(EP2MonthlyAdherence[MonthCount - 1]),
						GetText(AdherenceXpath));
			}

		} else {

			// =========================================================
			// Verifying Injection Count and Adherence popup for EP2 Device
			// ============================================
			DrpDwn_SelectIndex("#AdherenceCalendar_Range_DD", 5);
			SelectMonth("Adherence_CustomDateFrom_TT", EP2FromDate);
			SelectMonth("Adherence_CustomDateTo_TT", EP2CustomToDate);
			Btn_Click("Adherence_Submit_BN");

			// Multiple Count
			int ExpectedCount = 1;
			int ActualCount = browser.findElements(
					By.xpath(ORXPath("#AdherenceCalendar_MultipleLegends_LK")))
					.size();
			CompareText("MultipleInjectionCount",
					String.valueOf(ExpectedCount), String.valueOf(ActualCount));
			Lnk_Click("AdherenceCalendar_MultipleLegends_LK");
			REGCheckAdherencePopup(EP2Multiple_Date, NewDosageSet, "6",
					"00:59", EP2DeviceID, "1.1", "1.1 mg", "1");
			REGCheckAdherencePopup(EP2Multiple_Date, NewDosageSet, "6",
					"00:59", EP2DeviceID, "1.1", "1.1 mg", "2");
			REGCheckAdherencePopup(EP2Multiple_Date, NewCartridge, "6",
					"01:09", EP2DeviceID, "8.65", "1.1 mg", "3");
			REGCheckAdherencePopup(EP2Multiple_Date, CompleteInjection, "6",
					"01:12", EP2DeviceID, "1.1", "1.1 mg", "4");
			REGCheckAdherencePopup(EP2Multiple_Date, CompleteInjection, "6",
					"01:12", EP2DeviceID, "1.1", "1.1 mg", "5");
			Btn_Click("AdherenceCalendar_LegendClose_BN");
			// =========================================================
			// Verifying Injection Count and Adherence popup ends for EP2 Device
			// ============================================

			// =========================================================
			// Verifying Monthly Adherence values starts for EP2Device
			// ===========================================
			// First 6 Months(Jan-Jun)
			SelectMonth("AdherenceCalendar_CustomDateFrom_TT", EP2FromDate);
			SelectMonth("AdherenceCalendar_CustomDateTo_TT", EP2ToDate);
			Btn_Click("Adherence_Submit_BN");
			VerifyTextContained(
					"AdherenceCalendar_InjectionReportFromMonth_ST",
					LangData("MonthLong1"),
					GetText("#AdherenceCalendar_InjectionReportFromMonth_ST"));
			VerifyTextContained(
					"AdherenceCalendar_InjectionReportFromMonth_ST",
					LangData("MonthLong6"),
					GetText("#AdherenceCalendar_InjectionReportFromMonth_ST"));

			// (Jan-Jun)
			for (int MonthCount = 1; MonthCount <= 6; MonthCount++) {
				String MonthNameXpath = "//div[@id='itemContainer']//div["
						+ MonthCount + "]/h4/span[1]";
				String AdherenceXpath = "//div[@id='itemContainer']//div["
						+ MonthCount + "]/h4//strong";
				Expected = LangData("MonthLong" + MonthCount);
				CompareText("EP2MonthName" + MonthCount, Expected + " 2013",
						GetText(MonthNameXpath));
				CompareText("EP2MonthlyAdherenceValue" + MonthCount,
						Converter(EP2MonthlyAdherence[MonthCount - 1]),
						GetText(AdherenceXpath));
			}

			// (Jul-Dec)
			Lnk_Click("AdherenceCalendar_Next_LK");
			for (int MonthCount = 7; MonthCount <= 12; MonthCount++) {
				String MonthNameXpath = "//div[@id='itemContainer']//div["
						+ MonthCount + "]/h4/span[1]";
				String AdherenceXpath = "//div[@id='itemContainer']//div["
						+ MonthCount + "]/h4//strong";
				Expected = LangData("MonthLong" + MonthCount);
				CompareText("EP2MonthName" + MonthCount, Expected + " 2013",
						GetText(MonthNameXpath));
				CompareText("EP2MonthlyAdherenceValue" + MonthCount,
						Converter(EP2MonthlyAdherence[MonthCount - 1]),
						GetText(AdherenceXpath));
			}
			// =========================================================
			// Verifying Monthly Adherence values ends for EP2Device
			// ===========================================

		}
	}

	public void VerifyEP2DeviceDataInAdherenceList(String EP2DeviceID) {

		String EP2FromDate = "01 Jan 2013";
		String EP2ToDate = "31 Dec 2013";
		// Select the Date and click submit button
		DrpDwn_SelectIndex("#AdherenceOverview_Range_DD", 5);// custom Date
		SelectDate("#Adherence_CustomDateFrom_TT", EP2FromDate);
		SelectDate("#Adherence_CustomDateTo_TT", EP2ToDate);
		Btn_Click("Adherence_Submit_BN");

		// Ensure that all the Injection and other events are selected
		ChkBx_SetOption("AdherenceList_Complete_CB", "ON");
		ChkBx_SetOption("AdherenceList_Partial_CB", "ON");
		ChkBx_SetOption("AdherenceList_NoInjection_CB", "ON");
		ChkBx_SetOption("AdherenceList_NewCartridge_CB", "ON");
		ChkBx_SetOption("AdherenceList_NewDosageSet_CB", "ON");

		// Verify the total number of records
		int Expected = 385;// Complete - 12 ; No Injection 361 ; Partial - 0 ;
							// New Cartridge - 3 ; New Dosage Set - 9
		int ActualCount = WbTbl_GetRecordCount("AdherenceList_TableHeader_ST",
				"AdherenceList_Pagination_EB");
		if (Expected == ActualCount) {
			Report.Update("InjectionCount", String.valueOf(Expected),
					String.valueOf(ActualCount), "PASS");
		} else {
			Report.Update("InjectionCount", String.valueOf(Expected),
					String.valueOf(ActualCount), "FAIL");
		}

		// //Partial Count
		// ChkBx_SetOption("AdherenceList_Complete_CB", "OFF");
		// ChkBx_SetOption("AdherenceList_Partial_CB", "ON");
		// ActualCount =
		// WbTbl_GetRecordCount("AdherenceList_TableHeader_ST","AdherenceList_Pagination_EB");
		// if(Expected==ActualCount){
		// Report.Update("PartialInjectionCount", String.valueOf(Expected),
		// String.valueOf(ActualCount), "PASS");
		// }else{
		// Report.Update("PartialInjectionCount", String.valueOf(Expected),
		// String.valueOf(ActualCount), "FAIL");
		// }
		//
		// //New cartridge Count
		// ChkBx_SetOption("AdherenceList_Partial_CB", "OFF");
		// ChkBx_SetOption("AdherenceList_NewCartridge_CB", "ON");
		// ActualCount =
		// WbTbl_GetRecordCount("AdherenceList_TableHeader_ST","AdherenceList_Pagination_EB");
		// if(Expected==ActualCount){
		// Report.Update("NewcartridgeCount", String.valueOf(Expected),
		// String.valueOf(ActualCount), "PASS");
		// }else{
		// Report.Update("NewcartridgeCount", String.valueOf(Expected),
		// String.valueOf(ActualCount), "FAIL");
		// }
		//
		// //New Dosage Set Count
		// ChkBx_SetOption("AdherenceList_NewCartridge_CB", "OFF");
		// ChkBx_SetOption("AdherenceList_NewDosageSet_CB", "ON");
		// ActualCount =
		// WbTbl_GetRecordCount("AdherenceList_TableHeader_ST","AdherenceList_Pagination_EB");
		// if(Expected==ActualCount){
		// Report.Update("NewDosageSetCount",String.valueOf(Expected),
		// String.valueOf(ActualCount), "PASS");
		// }else{
		// Report.Update("NewDosageSetCount",String.valueOf(Expected),
		// String.valueOf(ActualCount), "FAIL");
		// }

	}

	public String CalculateFromDate(String ToDate,
			int AdherenceCalculationDuration) {
		// The value returned by this method will be the FromDate for the
		// adherence pages (except calendar) in custom date range.
		int NoOfDaysToGoBack = -1 * ((AdherenceCalculationDuration * 7) - 1);
		String MyDate = Date_AddDays(ToDate, NoOfDaysToGoBack, Calendar.DATE);
		return MyDate;
	}

	public boolean AssignDeviceAdmin_ByPatientID(String AdminRole,
			String PatientID, String DeviceID, String DeviceStatus,
			String DeviceUsageStartDate) {

		try {

			if (!ObjectExists("LocalAdmin_PatientId_RB")) {
				NavigateTo(Env.ECKOURL);

				if (AdminRole.contains("Global")) {
					SearchForCountry(Env.CountryUsed);
					Rbtn_SetOption("CountryList_FirstRadio_RB", "ON");
					Btn_Click("CountryList_Devices_BN");
				}
				// ADD NEW DEVICE.
				Btn_Click("GlobalAdmin_AddNewDevice_BN");
			}

			Rbtn_SetOption("LocalAdmin_PatientId_RB", "ON");
			Edit_SetValue("LocalAdmin_PatientId_EB",
					PatientID.replaceAll("jsn:", ""));
			Btn_Click("LocalAdmin_PatientIdSearch_BN");

			if (ObjectExists("LocalAdmin_PatientConfirmPopupYes_BN")) {
				Btn_Click("LocalAdmin_PatientConfirmPopupYes_BN");
				// Provide valid data in Step 2
				Edit_SetValue("LocalAdmin_NewDeviceSerialNo1_EB",
						DeviceID.substring(0, 4));
				Edit_SetValue("LocalAdmin_NewDeviceSerialNo2_EB",
						DeviceID.substring(4, 7));
				Edit_SetValue("LocalAdmin_NewDeviceSerialNo3_EB",
						DeviceID.substring(7));
				Edit_SetValue("LocalAdmin_ConfirmNewDeviceSerialNo1_EB",
						DeviceID.substring(0, 4));
				Edit_SetValue("LocalAdmin_ConfirmNewDeviceSerialNo2_EB",
						DeviceID.substring(4, 7));
				Edit_SetValue("LocalAdmin_ConfirmNewDeviceSerialNo3_EB",
						DeviceID.substring(7));
				SelectDate("LocalAdmin_DeviceUsageStartDateCalendar_LK",
						DeviceUsageStartDate);
				if (CompareText(LangData("DeviceSecondary"), DeviceStatus)) {
					Rbtn_SetOption("LocalAdmin_DeviceSecondary_RB", "ON"); // Select
																			// Secondary
																			// device
																			// Radio
																			// Button
				} else {
					Rbtn_SetOption("AssignedDevices_Primary_RB", "ON"); // Select
																		// Primary
																		// device
																		// Radio
																		// Button
				}
				Btn_Click("LocalAdmin_Submit_BN");
				Btn_Click("LocalAdmin_AssignDevicePopupYes_BN");

				String ActualText = GetText("AssignDevice_AssignedtoSuccessMsg_DT");
				String ExpectedText = LangData("AssignedToSmall");
				ExpectedText = ExpectedText.replace("{0}", DeviceID);
				ExpectedText = ExpectedText.replace("{1}", "Patient - "
						+ PatientID);
				ExpectedText = ExpectedText.replace("{2}", DeviceStatus);
				ExpectedText = ExpectedText.replace("{3}",
						TranslateMonth(DeviceUsageStartDate));

				if (CompareText(ExpectedText, ActualText)) {
					Env.logger.log(Level.INFO, DeviceID
							+ " is Assigned successfully to " + PatientID);
					return true;
				} else {
					Env.logger.log(Level.WARN, "Unable to Assign the device "
							+ DeviceID + " to the patient " + PatientID);
					return false;
				}
			} else {
				Env.logger.log(Level.WARN, "Unable to Assign the device "
						+ DeviceID + " to the patient " + PatientID);
				return false;
			}
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, DeviceID, e);
			return false;
		}
	}

	public boolean AssignDeviceAdmin_ByExistingDeviceSerial(String AdminRole,
			String PatientID, String ExistingDeviceSerial, String DeviceID,
			String DeviceStatus, String DeviceUsageStartDate) {

		try {

			if (!ObjectExists("LocalAdmin_DeviceSerialNumber_RB")) {
				NavigateTo(Env.ECKOURL);

				if (AdminRole.contains("Global")) {
					SearchForCountry(Env.CountryUsed);
					Rbtn_SetOption("CountryList_FirstRadio_RB", "ON");
					Btn_Click("CountryList_Devices_BN");
				}
				// ADD NEW DEVICE.
				Btn_Click("GlobalAdmin_AddNewDevice_BN");
			}

			Rbtn_SetOption("LocalAdmin_DeviceSerialNumber_RB", "ON");
			Edit_SetValue("LocalAdmin_ExistingDeviceSerialNo1_EB",
					ExistingDeviceSerial.substring(0, 4));
			Edit_SetValue("LocalAdmin_ExistingDeviceSerialNo2_EB",
					ExistingDeviceSerial.substring(4, 7));
			Edit_SetValue("LocalAdmin_ExistingDeviceSerialNo3_EB",
					ExistingDeviceSerial.substring(7));
			Btn_Click("LocalAdmin_DeviceSearch_BN");

			if (ObjectExists("LocalAdmin_PatientConfirmPopupYes_BN")) {
				Btn_Click("LocalAdmin_PatientConfirmPopupYes_BN");
				Edit_SetValue("LocalAdmin_NewDeviceSerialNo1_EB",
						DeviceID.substring(0, 4));
				Edit_SetValue("LocalAdmin_NewDeviceSerialNo2_EB",
						DeviceID.substring(4, 7));
				Edit_SetValue("LocalAdmin_NewDeviceSerialNo3_EB",
						DeviceID.substring(7));
				Edit_SetValue("LocalAdmin_ConfirmNewDeviceSerialNo1_EB",
						DeviceID.substring(0, 4));
				Edit_SetValue("LocalAdmin_ConfirmNewDeviceSerialNo2_EB",
						DeviceID.substring(4, 7));
				Edit_SetValue("LocalAdmin_ConfirmNewDeviceSerialNo3_EB",
						DeviceID.substring(7));
				if (CompareText(LangData("DeviceSecondary"), DeviceStatus)) {
					Rbtn_SetOption("LocalAdmin_DeviceSecondary_RB", "ON"); // Select
																			// Secondary
																			// device
																			// Radio
																			// Button
				} else {
					Rbtn_SetOption("AssignedDevices_Primary_RB", "ON"); // Select
																		// Primary
																		// device
																		// Radio
																		// Button
				}
				SelectDate("LocalAdmin_DeviceUsageStartDateCalendar_LK",
						DeviceUsageStartDate);
				Btn_Click("LocalAdmin_Submit_BN");
				Btn_Click("LocalAdmin_AssignDevicePopupYes_BN");

				String ActualText = GetText("AssignDevice_AssignedtoSuccessMsg_DT");
				String ExpectedText = LangData("AssignedToSmall");
				ExpectedText = ExpectedText.replace("{0}", DeviceID);
				ExpectedText = ExpectedText.replace("{1}", "Patient - "
						+ PatientID);
				ExpectedText = ExpectedText.replace("{2}", DeviceStatus);
				ExpectedText = ExpectedText.replace("{3}",
						TranslateMonth(DeviceUsageStartDate));

				if (CompareText(ExpectedText, ActualText)) {
					Env.logger.log(Level.INFO, DeviceID
							+ " is Assigned successfully to " + PatientID);
					return true;
				} else {
					Env.logger.log(Level.WARN, "Unable to Assign the device "
							+ DeviceID + " to the patient " + PatientID);
					return false;
				}
			} else {
				Env.logger.log(Level.WARN, "Unable to Assign the device "
						+ DeviceID + " to the patient " + PatientID);
				return false;
			}
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, DeviceID, e);
			return false;
		}
	}

	public boolean AssignDeviceAdmin_ByPatientName(String ClinicName,
			String PatientFirstname, String PatientLastname, String DeviceID,
			String DeviceStatus, String DeviceUsageStartDate) {

		try {

			if (!ObjectExists("LocalAdmin_PatientName_RB")) {
				NavigateTo(Env.ECKOURL);

				// ADD NEW DEVICE.
				Btn_Click("GlobalAdmin_AddNewDevice_BN");
			}

			Rbtn_SetOption("LocalAdmin_PatientName_RB", "ON");// Select patient
																// name option
			DrpDwn_SelectText("LocalAdmin_ClinicName_DD", ClinicName);
			Edit_SetValue("LocalAdmin_FirstName_EB", PatientFirstname);
			Edit_SetValue("LocalAdmin_LastName_EB", PatientLastname);
			Btn_Click("LocalAdmin_PatientSearch_BN");

			if (ObjectExists("LocalAdmin_PatientConfirmPopupYes_BN")) {
				Btn_Click("LocalAdmin_PatientConfirmPopupYes_BN");
				Edit_SetValue("LocalAdmin_NewDeviceSerialNo1_EB",
						DeviceID.substring(0, 4));
				Edit_SetValue("LocalAdmin_NewDeviceSerialNo2_EB",
						DeviceID.substring(4, 7));
				Edit_SetValue("LocalAdmin_NewDeviceSerialNo3_EB",
						DeviceID.substring(7));
				Edit_SetValue("LocalAdmin_ConfirmNewDeviceSerialNo1_EB",
						DeviceID.substring(0, 4));
				Edit_SetValue("LocalAdmin_ConfirmNewDeviceSerialNo2_EB",
						DeviceID.substring(4, 7));
				Edit_SetValue("LocalAdmin_ConfirmNewDeviceSerialNo3_EB",
						DeviceID.substring(7));
				SelectDate("LocalAdmin_DeviceUsageStartDateCalendar_LK",
						DeviceUsageStartDate);
				if (CompareText(LangData("DeviceSecondary"), DeviceStatus)) {
					Rbtn_SetOption("LocalAdmin_DeviceSecondary_RB", "ON"); // Select
																			// Secondary
																			// device
																			// Radio
																			// Button
				} else {
					Rbtn_SetOption("AssignedDevices_Primary_RB", "ON"); // Select
																		// Primary
																		// device
																		// Radio
																		// Button
				}
				Btn_Click("LocalAdmin_Submit_BN");
				Btn_Click("LocalAdmin_AssignDevicePopupYes_BN");

				String ActualText = GetText("AssignDevice_AssignedtoSuccessMsg_DT");
				String ExpectedText = LangData("AssignedToSmall");
				ExpectedText = ExpectedText.replace("{0}", DeviceID);
				ExpectedText = ExpectedText.replace("{1}", "Patient - "
						+ PatientLastname + ", " + PatientFirstname);
				ExpectedText = ExpectedText.replace("{2}", DeviceStatus);
				ExpectedText = ExpectedText.replace("{3}",
						TranslateMonth(DeviceUsageStartDate));

				if (CompareText(ExpectedText, ActualText)) {
					Env.logger.log(Level.INFO, DeviceID
							+ " is Assigned successfully to " + PatientLastname
							+ ", " + PatientFirstname);
					return true;
				} else {
					Env.logger.log(Level.WARN, "Unable to Assign the device "
							+ DeviceID + " to the patient " + PatientLastname
							+ ", " + PatientFirstname);
					return false;
				}
			} else {
				Env.logger.log(Level.WARN, "Unable to Assign the device "
						+ DeviceID + " to the patient " + PatientLastname
						+ ", " + PatientFirstname);
				return false;
			}
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, DeviceID, e);
			return false;
		}
	}

	public boolean UnassignDevice_Admin(String DeviceID) {

		try {
			if (!ObjectExists("GlobalAdmin_AssignedDevicesTab_ST")) {
				NavigateTo(Env.ECKOURL);
				SearchForCountry(Env.CountryUsed);
				Rbtn_SetOption("CountryList_FirstRadio_RB", "ON");
				Btn_Click("CountryList_Devices_BN");
			}
			Lnk_Click("GlobalAdmin_AssignedDevicesTab_ST");
			SearchForDevice(DeviceID);// Search for device

			Rbtn_SetOption("General_FirstRadioButton_RB", "ON");
			Btn_Click("GlobalAdmin_UnAssignDevice_BN");
			if (ObjectExists("GlobalAdmin_ChangeDeviceToSecondaryPopup_TE")) {
				String Xpath = ORXPath("GlobalAdmin_ChangeDeviceToSecondaryPopup_TE")
						+ "/tbody/tr[1]/td[1]/input";
				Rbtn_SetOption(Xpath, "ON");
				Btn_Click("GlobalAdmin_SetAsPrimary_BN");
			} else {
				Btn_Click("UnassignedDevices_DeleteYes_BN");// Unassign the
															// device
			}

			if (ObjectExists("GlobalAdmin_DeleteSuccess_DT")) {
				Env.logger.log(Level.INFO, DeviceID
						+ " is Unassigned successfully");
				return true;
			} else {
				Env.logger.log(Level.WARN, "Unable to Unassign the device "
						+ DeviceID);
				return false;
			}

		} catch (Exception e) {
			Env.logger.log(Level.ERROR, DeviceID, e);
			return false;
		}
	}

	public boolean DeleteDevice_Admin(String DeviceID) {

		try {
			Lnk_Click("NavigationTab_AdminUnAssignedDevices_LK");
			SearchForDevice(DeviceID);
			Rbtn_SetOption("General_FirstRadioButton_RB", "ON");
			Btn_Click("GlobalAdmin_UnassignDelete_BN");
			Btn_Click("GlobalAdmin_UnassignDeleteYes_BN");

			if (CompareText("Device : " + DeviceID + " deleted successfully",
					GetText("GlobalAdmin_DeleteSuccess_DT"))) {
				Env.logger.log(Level.INFO, DeviceID
						+ " is deleted successfully");
				return true;
			} else {
				Env.logger.log(Level.WARN, "Unable to delete the device "
						+ DeviceID);
				return false;
			}
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, DeviceID, e);
			return false;
		}

	}

	public boolean DeletePatientAdmin(String JSNID) {

		try {
			NavigateTo(Env.ECOSURL);
			SearchForCountry(Env.CountryUsed);
			Rbtn_SetOption("#CountryList_SelectFirstRadio_BN", "ON");
			Btn_Click("#CountryList_Patients_BN");
			Lnk_Click("#PatientList_EasypodconnectPatients_LK");
			Edit_SetValue("#GlobalAdmin_SearchPatientID_EB", JSNID);
			Btn_Click("#GlobalAdmin_SearchPatient_BN");
			Rbtn_SetOption("#General_FirstRadioButton_RB", "ON");
			Btn_Click("#GlobalAdmin_DeletePatient_BN");
			Btn_Click("#GlobalAdmin_DeletePatientYes_BN");
			if (CompareText("Patient - " + JSNID + " deleted successfully.",
					GetText("GlobalAdmin_DeleteSuccess_DT"))) {
				Env.logger.log(Level.INFO, "JSNID : " + JSNID
						+ " is deleted successfully");
				return true;
			} else {
				Env.logger.log(Level.WARN, "Unable to delete the JSNID : "
						+ JSNID);
				return false;
			}
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, JSNID, e);
			return false;
		}
	}

	public boolean LockThisUserAccount(String Username) {

		for (int i = 1; i <= 5; i++) {

			if (Env.AppModule.startsWith("ECOS")) {
				NavigateTo(Env.ECOSURL);
			} else {
				NavigateTo(Env.ECKOURL);
			}

			// Lock the HCP user account.
			Edit_SetValue("Login_Username_EB", Username);
			Edit_SetValue("Login_Password_EB", "WrongPassword");
			Btn_Click("Login_Login_BN");

			if (CompareText(ORData("Login_LockedAccount_DT"),
					GetText("Login_LockedAccount_DT"))) {
				break; // Account is locked, need not try again
			}
		}
		return CompareText(LangData("UserLocked"),
				GetText("Login_AttemptLeftErr_DT"));
	}

	public String GetColonCharacter() {
		String ReturnColon = ":"; // Normal Colon
		if (Env.LanguageUsed.contains("Chinese")) {
			ReturnColon = "：";// Chinese Colon
		}
		return ReturnColon;

	}

	public String AuxologicalRangeValue(String GraphType, String RangeType) {
		String ReturnText = null;
		GraphType = GraphType.toUpperCase().trim();
		RangeType = RangeType.toUpperCase().trim();

		// For Growth Curve
		if (GraphType.startsWith("GROWTHCURVE")) {
			if (RangeType.startsWith("CDC-036")) {
				if (Env.RunEnvironment.toUpperCase().startsWith("LOCAL")) {
					ReturnText = "MTI1MjAz";
				} else {
					ReturnText = "MTg0NTkz";
				}
			} else if (RangeType.startsWith("CDC-220")) {
				if (Env.RunEnvironment.toUpperCase().startsWith("LOCAL")) {
					ReturnText = "MTI1MjAx";
				} else {
					ReturnText = "MTIwMDAwODYz";
				}
			} else if (RangeType.startsWith("CGF-418")) {
				if (Env.RunEnvironment.toUpperCase().startsWith("LOCAL")) {
					ReturnText = "MTI1MjEw";
				} else {
					ReturnText = "MTg0NjAw";
				}
			} else if (RangeType.startsWith("CHINESE-024")) {
				if (Env.RunEnvironment.toUpperCase().startsWith("LOCAL")) {
					ReturnText = "MTI1MjI4";
				} else {
					ReturnText = "MTIwMDAwODgz";
				}
			} else if (RangeType.startsWith("CHINESE-218")) {
				if (Env.RunEnvironment.toUpperCase().startsWith("LOCAL")) {
					ReturnText = "MTI1MjMw";
				} else {
					ReturnText = "MTIwMDAwODg1";
				}
			} else if (RangeType.startsWith("GERMANY-018")) {
				if (Env.RunEnvironment.toUpperCase().startsWith("LOCAL")) {
					ReturnText = "MTA3NTcyMg==";
				} else {
					ReturnText = "NDEwODU2NjIz";
				}
			} else if (RangeType.startsWith("GERMANY-218")) {
				if (Env.RunEnvironment.toUpperCase().startsWith("LOCAL")) {
					ReturnText = "MTA3NTcyNg==";
				} else {
					ReturnText = "NDEwODU2NjI3";
				}
			} else if (RangeType.startsWith("GREEK-118")) {
				if (Env.RunEnvironment.toUpperCase().startsWith("LOCAL")) {
					ReturnText = "MTI1MjQ2";
				} else {
					ReturnText = "MTIwMDAwODk5";
				}
			} else if (RangeType.startsWith("KOREAN-024")) {
				if (Env.RunEnvironment.toUpperCase().startsWith("LOCAL")) {
					ReturnText = "MTI1MjE3";
				} else {
					ReturnText = "MTIwMDAwODcz";
				}
			} else if (RangeType.startsWith("KOREAN-220")) {
				if (Env.RunEnvironment.toUpperCase().startsWith("LOCAL")) {
					ReturnText = "MTI1MjE5";
				} else {
					ReturnText = "MTIwMDAwODc1";
				}
			} else if (RangeType.startsWith("NORWAY-019")) {
				if (Env.RunEnvironment.toUpperCase().startsWith("LOCAL")) {
					ReturnText = "MjI4NDM1";
				} else {
					ReturnText = "NDEwNDk5OTk5";
				}
			} else if (RangeType.startsWith("SPANISH-UPTOADULT")) {
				if (Env.RunEnvironment.toUpperCase().startsWith("LOCAL")) {
					ReturnText = "MjUwNzY5";
				} else {
					ReturnText = "NDEwNjU5NTU1";
				}
			} else if (RangeType.startsWith("SPANISH-03")) {
				if (Env.RunEnvironment.toUpperCase().startsWith("LOCAL")) {
					ReturnText = "MjUwNzcw";
				} else {
					ReturnText = "NDEwNjU5NTU2";
				}
			} else if (RangeType.startsWith("SPANISH-02518")) {
				if (Env.RunEnvironment.toUpperCase().startsWith("LOCAL")) {
					ReturnText = "MTI1MjM5";
				} else {
					ReturnText = "MTIwMDAwODkz";
				}
			} else if (RangeType.startsWith("SPANISH-NEWBORNS")) {
				if (Env.RunEnvironment.toUpperCase().startsWith("LOCAL")) {
					ReturnText = "MjUwNzcx";
				} else {
					ReturnText = "NDEwNjU5NTU3";
				}
			} else if (RangeType.startsWith("TAIWANESE-0185")) {
				if (Env.RunEnvironment.toUpperCase().startsWith("LOCAL")) {
					ReturnText = "NTcyNTky";
				} else {
					ReturnText = "MTIwMDAwOTA0";
				}
			} else if (RangeType.startsWith("TURNER-118")) {
				if (Env.RunEnvironment.toUpperCase().startsWith("LOCAL")) {
					ReturnText = "MTI1MjUz";
				} else {
					ReturnText = "MTIwMDAwOTAy";
				}
			} else if (RangeType.startsWith("WHO-04")) {
				if (Env.RunEnvironment.toUpperCase().startsWith("LOCAL")) {
					ReturnText = "MTI1MTg2";
				} else {
					ReturnText = "MTg0NTc2";
				}
			} else if (RangeType.startsWith("WHO-018")) {
				if (Env.RunEnvironment.toUpperCase().startsWith("LOCAL")) {
					ReturnText = "MTA3NTczMQ==";
				} else {
					ReturnText = "NDEwODU2NjMy";
				}
			} else if (RangeType.startsWith("WHO-418")) {
				if (Env.RunEnvironment.toUpperCase().startsWith("LOCAL")) {
					ReturnText = "MTI1MTg4";
				} else {
					ReturnText = "MTIwMDAwODU3";
				}
			}

		}
		// For Growth Velocity Curve
		if (GraphType.startsWith("GROWTHVELOCITYCURVE")) {
			if (RangeType.startsWith("NORWEGIAN-060")) {
				if (Env.RunEnvironment.toUpperCase().startsWith("LOCAL")) {
					ReturnText = "MjI4NDM5";
				} else {
					ReturnText = "NDEwNTAwMDAz";
				}
			} else if (RangeType.startsWith("TANNER-2518")) {
				if (Env.RunEnvironment.toUpperCase().startsWith("LOCAL")) {
					ReturnText = "MTI1MjUx";
				} else {
					ReturnText = "MTg0NjE2";
				}
			}
		}

		// For Body Mass Index
		if (GraphType.startsWith("BODYMASSINDEX")) {
			if (RangeType.startsWith("CDC-220")) {
				if (Env.RunEnvironment.toUpperCase().startsWith("LOCAL")) {
					ReturnText = "MTI1MTk5";
				} else {
					ReturnText = "MTIwMDAwODYx";
				}
			} else if (RangeType.startsWith("CGF-418")) {
				if (Env.RunEnvironment.toUpperCase().startsWith("LOCAL")) {
					ReturnText = "MTI1MjEy";
				} else {
					ReturnText = "MTg0NjAy";
				}
			} else if (RangeType.startsWith("CHINESE-218")) {
				if (Env.RunEnvironment.toUpperCase().startsWith("LOCAL")) {
					ReturnText = "MTI1MjI2";
				} else {
					ReturnText = "MTIwMDAwODgx";
				}
			} else if (RangeType.startsWith("GREEK-118")) {
				if (Env.RunEnvironment.toUpperCase().startsWith("LOCAL")) {
					ReturnText = "MTI1MjQ0";
				} else {
					ReturnText = "MTIwMDAwODk3";
				}
			} else if (RangeType.startsWith("NORWEGIAN-020")) {
				if (Env.RunEnvironment.toUpperCase().startsWith("LOCAL")) {
					ReturnText = "MjI4NDM3";
				} else {
					ReturnText = "NDEwNTAwMDAx";
				}
			} else if (RangeType.startsWith("SPANISH-02518")) {
				if (Env.RunEnvironment.toUpperCase().startsWith("LOCAL")) {
					ReturnText = "MTI1MjM3";
				} else {
					ReturnText = "MTIwMDAwODkx";
				}
			} else if (RangeType.startsWith("TAIWANESE-2185")) {
				if (Env.RunEnvironment.toUpperCase().startsWith("LOCAL")) {
					ReturnText = "NTcyNTk2";
				} else {
					ReturnText = "MTIwMDAwOTA4";
				}
			} else if (RangeType.startsWith("WHO-04")) {
				if (Env.RunEnvironment.toUpperCase().startsWith("LOCAL")) {
					ReturnText = "MTI1MTkw";
				} else {
					ReturnText = "MTg0NTgw";
				}
			} else if (RangeType.startsWith("WHO-018")) {
				if (Env.RunEnvironment.toUpperCase().startsWith("LOCAL")) {
					ReturnText = "MTA3NTczMA==";
				} else {
					ReturnText = "NDEwODU2NjMx";
				}
			} else if (RangeType.startsWith("WHO-418")) {
				if (Env.RunEnvironment.toUpperCase().startsWith("LOCAL")) {
					ReturnText = "MTI1MTky";
				} else {
					ReturnText = "MTIwMDAwMDAx";
				}
			}
		}

		// For Weight Curve
		if (GraphType.startsWith("WEIGHTCURVE")) {
			if (RangeType.startsWith("CDC-036")) {
				if (Env.RunEnvironment.toUpperCase().startsWith("LOCAL")) {
					ReturnText = "MTI1MjA3";
				} else {
					ReturnText = "MTg0NTk3";
				}
			} else if (RangeType.startsWith("CDC-220")) {
				if (Env.RunEnvironment.toUpperCase().startsWith("LOCAL")) {
					ReturnText = "MTI1MjA1";
				} else {
					ReturnText = "MTIwMDAwODY1";
				}
			} else if (RangeType.startsWith("CGF-020")) {
				if (Env.RunEnvironment.toUpperCase().startsWith("LOCAL")) {
					ReturnText = "MTI1MjE0";
				} else {
					ReturnText = "MTg0NjA0";
				}
			} else if (RangeType.startsWith("CHINESE-024")) {
				if (Env.RunEnvironment.toUpperCase().startsWith("LOCAL")) {
					ReturnText = "MTI1MjMy";
				} else {
					ReturnText = "MTIwMDAwODg3";
				}
			} else if (RangeType.startsWith("CHINESE-218")) {
				if (Env.RunEnvironment.toUpperCase().startsWith("LOCAL")) {
					ReturnText = "MTI1MjM0";
				} else {
					ReturnText = "MTIwMDAwODg5";
				}
			} else if (RangeType.startsWith("GERMANY-018")) {
				if (Env.RunEnvironment.toUpperCase().startsWith("LOCAL")) {
					ReturnText = "MTA3NTcyNA==";
				} else {
					ReturnText = "NDEwODU2NjI1";
				}
			} else if (RangeType.startsWith("GREEK-118")) {
				if (Env.RunEnvironment.toUpperCase().startsWith("LOCAL")) {
					ReturnText = "MTI1MjQ4";
				} else {
					ReturnText = "MTIwMDAwOTAx";
				}
			} else if (RangeType.startsWith("KOREAN-024")) {
				if (Env.RunEnvironment.toUpperCase().startsWith("LOCAL")) {
					ReturnText = "MTI1MjIx";
				} else {
					ReturnText = "MTIwMDAwODc3";
				}
			} else if (RangeType.startsWith("KOREAN-220")) {
				if (Env.RunEnvironment.toUpperCase().startsWith("LOCAL")) {
					ReturnText = "MTI1MjIz";
				} else {
					ReturnText = "MTIwMDAwODc5";
				}
			} else if (RangeType.startsWith("NORWEGIAN-019")) {
				if (Env.RunEnvironment.toUpperCase().startsWith("LOCAL")) {
					ReturnText = "MjI4NDMz";
				} else {
					ReturnText = "NDEwNDk5OTk3";
				}
			} else if (RangeType.startsWith("SPANISH-UPTOADULT")) {
				if (Env.RunEnvironment.toUpperCase().startsWith("LOCAL")) {
					ReturnText = "MjUwNzc2";
				} else {
					ReturnText = "NDEwNjU5NTYy";
				}
			} else if (RangeType.startsWith("SPANISH-03")) {
				if (Env.RunEnvironment.toUpperCase().startsWith("LOCAL")) {
					ReturnText = "MjUwNzc1";
				} else {
					ReturnText = "NDEwNjU5NTYx";
				}
			} else if (RangeType.startsWith("SPANISH-02518")) {
				if (Env.RunEnvironment.toUpperCase().startsWith("LOCAL")) {
					ReturnText = "MTI1MjQx";
				} else {
					ReturnText = "MTIwMDAwODk1";
				}
			} else if (RangeType.startsWith("SPANISH-NEWBORNS")) {
				if (Env.RunEnvironment.toUpperCase().startsWith("LOCAL")) {
					ReturnText = "MjUwNzc3";
				} else {
					ReturnText = "NDEwNjU5NTYz";
				}
			} else if (RangeType.startsWith("TAIWANESE-0185")) {
				if (Env.RunEnvironment.toUpperCase().startsWith("LOCAL")) {
					ReturnText = "NTcyNTk0";
				} else {
					ReturnText = "MTIwMDAwOTA2";
				}
			} else if (RangeType.startsWith("WHO-010")) {
				if (Env.RunEnvironment.toUpperCase().startsWith("LOCAL")) {
					ReturnText = "MTA3NTczMg==";
				} else {
					ReturnText = "NDEwODU2NjMz";
				}
			} else if (RangeType.startsWith("WHO-05")) {
				if (Env.RunEnvironment.toUpperCase().startsWith("LOCAL")) {
					ReturnText = "MTI1MTk0";
				} else {
					ReturnText = "MTg0NTg0";
				}
			} else if (RangeType.startsWith("WHO-510")) {
				if (Env.RunEnvironment.toUpperCase().startsWith("LOCAL")) {
					ReturnText = "MTI1MTk2";
				} else {
					ReturnText = "MTIwMDAwODU5";
				}
			}
		}

		return ReturnText;
	}

	public void VerifySettingsHistoryPopUP(String StepID, String CircleObject,
			String FirstValue, String SecondValue) {
		try {
			WaitForObject(CircleObject);
			Img_Click(CircleObject);
			WebElement ActionObject = GetObject("DeviceSettings_ToolTip_DT");
			if (!ActionObject.isDisplayed()) {
				String StyleValue = ActionObject.getAttribute("style");
				StyleValue = StyleValue.replace("none;", "block;");
				RunJavaScript(ActionObject,
						"arguments[0].setAttribute('style', '" + StyleValue
								+ "');");
			}
			String ToolTip = GetText("DeviceSettings_ToolTip_DT");
			String[] Actual = ToolTip.split(":");
			Env.StaticScreenshotName = UtilClass
					.GetScreenshot("SettingHistoryPopUp");
			CompareText(StepID + "_DT", FirstValue, Actual[0]);
			CompareText("SettingValue_DT", SecondValue.trim() + " "
					+ LangData("Date"), Actual[1]);
			VerifyMonth("SettingDate_DT", Actual[2]);
			ClearStaticScreenshot();
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, CircleObject, e);
		}
	}

	public String GenerateRegExpForDate() {
		String returnText = "\\b(0[1-9]|[12][0-9]|3[01]) (AllMonths) ((19|20)\\d\\d\\b)";
		returnText = returnText.replace("AllMonths", MonthShortAll());
		return returnText;
	}

	public boolean TransferDevice_Admin(String DeviceToTransfer,
			String FromCountryName, String HCPLastFirstName,
			boolean isFromUnAssigned, boolean isTransferwithinClinic) {

		String Index = "";

		try {
			if (isFromUnAssigned) {// Unassigned Devices List Screen
				NavigateTo(Env.ECKOURL);
				SearchForCountry(FromCountryName);
				Rbtn_SetOption("CountryList_SelectFirstRadio_RB", "ON");
				Btn_Click("CountryList_Devices_BN");
			} else {
				NavigateTo(Env.ECKOURL);// Assigned Devices List Screen
				SearchForCountry(Env.CountryUsed);
				Rbtn_SetOption("CountryList_SelectFirstRadio_RB", "ON");
				Btn_Click("CountryList_Devices_BN");
				Lnk_Click("NavigationTab_AdminAssignedDevices_LK");
			}

			SearchForDevice(DeviceToTransfer);
			Rbtn_SetOption("General_FirstRadioButton_RB", "ON");
			Btn_Click("GlobalAdmin_UnassignedDevicesTransferDevice_BN");// Click
																		// on
																		// Transfer
																		// button

			if (!ObjectExists("SupportAdmin_PrimaryDevicePopup")) {// Validation
																	// for
																	// Primary
																	// Device
				if (!isTransferwithinClinic) {
					if (FromCountryName.equalsIgnoreCase("Canada")) {// if
																		// device
																		// is to
																		// be
																		// transferred
																		// from
																		// Canada
						Index = "SE10";
						DrpDwn_SelectText("GlobalAdmin_Country_DD", "Sweden");
						DrpDwn_SelectText("GlobalAdmin_Clinic_DD",
								TestData("ClinicName") + "test" + Index);
					} else {
						Index = "CA10";
						DrpDwn_SelectText("GlobalAdmin_Country_DD", "Canada");
						DrpDwn_SelectText("GlobalAdmin_Clinic_DD",
								TestData("ClinicName") + "test" + Index);
					}
				} else {
					Index = TestData("Index");
				}
				Edit_SetValue("HCPList_SearchText_EB", HCPLastFirstName);// Search
																			// for
																			// HCP
				Btn_Click("GlobalAdminList_Search_BN");

				if (!CompareText(LangData("NoRecordsToDisplay"),
						GetText("MyPatients_SearchInvalidError_DT"))) {// Valid
																		// HCP
																		// name
					Rbtn_SetOption("General_FirstRadioButton_RB", "ON");
					Btn_Click("GlobalAdmin_HCPListTransferDevice_BN");
					Btn_Click("GlobalAdmin_TransferDeviceSubmit_BN");

					String ActualText = GetText("GlobalAdmin_TransferDeviceDataSuccessMsg_DT");
					String ExpectedText = LangData("DeviceAssignedSuccessMsg");
					ExpectedText = ExpectedText
							.replace("{0}", DeviceToTransfer);
					ExpectedText = ExpectedText.replace("{1}", Index + " "
							+ HCPLastFirstName.replace(",", ""));

					if (CompareText(ExpectedText, ActualText)) {
						Env.logger.log(Level.INFO, DeviceToTransfer
								+ " is Assigned successfully to "
								+ HCPLastFirstName.replace(",", ""));
						return true;
					} else {
						Env.logger.log(Level.WARN,
								"Unable to Assign the device "
										+ DeviceToTransfer + " to the patient "
										+ HCPLastFirstName.replace(",", ""));
						return false;
					}
				} else {// Invalid HCP name
					Env.logger.log(Level.WARN, "Unable to transfer the device "
							+ DeviceToTransfer + " to the invalid HCP "
							+ HCPLastFirstName);
					return false;
				}
			} else {
				if (CompareText(LangData("DeviceTransferPrimaryError"),
						GetText("SupportAdmin_PrimaryDevicePopup_ST"))) {
					Env.logger.log(Level.WARN,
							"Unable to transfer the primary device "
									+ DeviceToTransfer + " to the HCP "
									+ HCPLastFirstName);
				}
				return false;
			}
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, DeviceToTransfer, e);
			return false;
		}
	}

	public String GetPDFPasswordForPatient() {
		String Firstname = "";
		String Lastname = "";
		String DOB = "";
		String ReturnPassword = "";
		try {
			Firstname = GetOnlyMyText("ViewProfile_FirstNameValue_ST");
			Lastname = GetOnlyMyText("ViewProfile_LastnameValue_ST");
			DOB = GetOnlyMyText("ViewProfile_DOBValue_ST");
			ReturnPassword = Firstname.substring(0, 2)
					+ Lastname.substring(0, 2) + DOB.split(" ")[2];
		} catch (Exception e) {
			Env.logger
					.log(Level.ERROR,
							"Error Occured while constructing the password for patients.",
							e);
		}
		return ReturnPassword.toLowerCase();
	}

	public String GetPDFPasswordForHCP() {
		String ReturnPassword = "";

		try {
			// Navigate to MyAccounts page
			Lnk_Click("Header_MyAccount_LK");

			String Full = GetText("Header_LoggedInUser_ST");
			String LastName = Full.split(" ")[1];
			String Mobilenumber = GetOnlyMyText("MyAccountHcp_PhoneNumbervalue_ST");
			Mobilenumber = Mobilenumber.substring(Mobilenumber.length() - 4,
					Mobilenumber.length());
			ReturnPassword = Full.substring(0, 2) + LastName.substring(0, 2)
					+ Mobilenumber;

		} catch (Exception e) {
			Env.logger.log(Level.ERROR,
					"Unable to construct the HCP password for PDF", e);
		}
		return ReturnPassword.toLowerCase();

	}

	public void WTTUpload(String DeviceID) {

		NavigateTo(Env.WTTURL);

		String FilePath = "DeviceData" + "/" + "EP2Devices" + "/" + DeviceID;
		FileUpload_SetFile("//input[@id='eds']", GetDataPath() + FilePath + "/"
				+ "SetLog_" + DeviceID + ".eds");
		FileUpload_SetFile("//input[@id='edb']", GetDataPath() + FilePath + "/"
				+ "BinHistLog_" + DeviceID + ".edb");
		FileUpload_SetFile("//input[@id='edt']", GetDataPath() + FilePath + "/"
				+ "TechLog_" + DeviceID + ".edt");
		FileUpload_SetFile("//input[@id='trs']", GetDataPath() + FilePath + "/"
				+ "Settings_" + "2121064122000066" + ".trs");
		FileUpload_SetFile("//input[@id='trt']", GetDataPath() + FilePath + "/"
				+ "TechnicalLog_" + "2121064122000066" + ".trt");
		Edit_SetValue("//input[@name='cmdTest']", "1");
		Btn_Click("//input[@id='upload_btn']");
	}

	public String SetUpDeviceFiles(String DevicePath, String DeviceID) {

		String BasePath = GetDataPath() + "DeviceData/" + DevicePath + "/";
		String CommonDeviceID = "xxxx";
		String OutputPath = Reporter.DriverScreenshotsFolder + "/"
				+ GetTimeStamp("hhmmss") + "/";
		DeviceID = DeviceID.trim();
		File OriginalFile = null;
		File NewFile = null;

		try {
			if (DeviceID.startsWith("MS")) {
				// For EP1 Devices
				OriginalFile = new File(BasePath + CommonDeviceID + ".edh");
				NewFile = new File(OutputPath + DeviceID + ".edh");
				FileUtils.copyFile(OriginalFile, NewFile);
			} else {
				// EP2 Device
				OriginalFile = new File(BasePath + "SetLog_" + CommonDeviceID
						+ ".eds");
				NewFile = new File(OutputPath + "SetLog_" + DeviceID + ".eds");
				FileUtils.copyFile(OriginalFile, NewFile);

				OriginalFile = new File(BasePath + "BinHistLog_"
						+ CommonDeviceID + ".edb");
				NewFile = new File(OutputPath + "BinHistLog_" + DeviceID
						+ ".edb");
				FileUtils.copyFile(OriginalFile, NewFile);

				OriginalFile = new File(BasePath + "TechLog_" + CommonDeviceID
						+ ".edt");
				NewFile = new File(OutputPath + "TechLog_" + DeviceID + ".edt");
				FileUtils.copyFile(OriginalFile, NewFile);
			}
			Env.logger.log(Level.INFO, "Device files for :" + DeviceID
					+ " are copied successfully");
		} catch (Exception e) {
			Env.logger.log(Level.ERROR,
					"Error occured while copying the device files " + DeviceID,
					e);
		}
		return OutputPath;
	}

	public String EthnicityDropDownValues() {
		Country switchCountryName = Country.valueOf(Env.CountryUsed.replaceAll(
				" ", "").toUpperCase());
		String EthnicitySupported = null;
		if (switchCountryName.SupportForECOS) {
			switch (switchCountryName) {
			case AUSTRALIA:
			case UNITEDARABEMIRATES:
			case AUSTRIA:
			case CZECHREPUBLIC:
			case FINLAND:
			case FRANCE:
			case ICELAND:
			case ITALY:
			case KOREA:
			case MEXICO:
			case SAUDIARABIA:
			case SINGAPORE:
				EthnicitySupported = LangData("African") + "|"
						+ LangData("Asian") + "|" + LangData("Caucasian") + "|"
						+ LangData("Other");
				break;
			case CANADA:
			case GERMANY:
			case SPAIN:
			case GREECE:
			case HUNGARY:
			case NORWAY:
			case SWEDEN:
			case SLOVAKIA:
			case UNITEDKINGDOM:
				EthnicitySupported = LangData("African") + "|"
						+ LangData("Asian") + "|" + LangData("Caucasian") + "|"
						+ LangData("EthnicityNotAvailable") + "|"
						+ LangData("EthinicityNotKnown") + "|"
						+ LangData("Other");
				break;
			default:
				// Default values from Austria.
				EthnicitySupported = LangData("African") + "|"
						+ LangData("Asian") + "|" + LangData("Caucasian") + "|"
						+ LangData("Other");
				break;
			}
		} else {
			// For Non-ECOS Countries. Default values from Austria.
			EthnicitySupported = LangData("African") + "|" + LangData("Asian")
					+ "|" + LangData("Caucasian") + "|" + LangData("Other");
		}
		return EthnicitySupported;
	}

	public String GrowthHormoneDropDownValues() {
		Country switchCountryName = Country.valueOf(Env.CountryUsed.replaceAll(
				" ", "").toUpperCase());
		String GrowthHormonesSupported = null;
		if (switchCountryName.SupportForECOS) {
			switch (switchCountryName) {
			case AUSTRALIA:
				GrowthHormonesSupported = LangData("GrowthHormoneDeficiency")
						+ "|" + LangData("TurnerSyndrome");
				break;
			case GERMANY:
			case FRANCE:
				GrowthHormonesSupported = LangData("ChronicRenalFailure") + "|"
						+ LangData("GrowthHormoneDeficiency") + "|"
						+ LangData("SmallForGestationalAge") + "|"
						+ LangData("TurnerSyndrome");
				break;
			case NORWAY:
				GrowthHormonesSupported = LangData("ChronicRenalFailureKidneyDisease")
						+ "|"
						+ LangData("GrowthHormoneDeficiency")
						+ "|"
						+ LangData("GhiOther")
						+ "|"
						+ LangData("SmallForGestationalAge")
						+ "|"
						+ LangData("TurnerSyndrome");
			case GREECE:
			case HUNGARY:
			case SLOVAKIA:
				GrowthHormonesSupported = LangData("ChronicRenalFailureKidneyDisease")
						+ "|"
						+ LangData("GrowthHormoneDeficiency")
						+ "|"
						+ LangData("GhiNotAvailable")
						+ "|"
						+ LangData("SmallForGestationalAge")
						+ "|"
						+ LangData("TurnerSyndrome")
						+ "|"
						+ LangData("GhiNotKnown");
				break;
			case UNITEDKINGDOM:
				GrowthHormonesSupported = LangData("ChronicRenalFailureKidneyDisease")
						+ "|"
						+ LangData("GrowthHormoneDeficiency")
						+ "|"
						+ LangData("GhiNotAvailable")
						+ "|"
						+ LangData("SmallForGestationalAge")
						+ "|"
						+ LangData("TurnerSyndrome")
						+ "|"
						+ LangData("GhiNotKnown") + "|" + LangData("GhiOther");
				break;
			case CANADA:
			case AUSTRIA:
			case CZECHREPUBLIC:
			case SPAIN:
			case FINLAND:
			case ICELAND:
			case ITALY:
			case KOREA:
			case MEXICO:
			case SAUDIARABIA:
			case SWEDEN:
			case SINGAPORE:
			case UNITEDARABEMIRATES:
				GrowthHormonesSupported = LangData("ChronicRenalFailureKidneyDisease")
						+ "|"
						+ LangData("GrowthHormoneDeficiency")
						+ "|"
						+ LangData("SmallForGestationalAge")
						+ "|"
						+ LangData("TurnerSyndrome");
				break;
			default: // Default values from Austria.
				GrowthHormonesSupported = LangData("ChronicRenalFailureKidneyDisease")
						+ "|"
						+ LangData("GrowthHormoneDeficiency")
						+ "|"
						+ LangData("SmallForGestationalAge")
						+ "|"
						+ LangData("TurnerSyndrome");
				break;
			}
		} else {
			// For Non-ECOS Countries. Default values from Austria.
			GrowthHormonesSupported = LangData("ChronicRenalFailureKidneyDisease")
					+ "|"
					+ LangData("GrowthHormoneDeficiency")
					+ "|"
					+ LangData("SmallForGestationalAge")
					+ "|"
					+ LangData("TurnerSyndrome");
		}

		return GrowthHormonesSupported;
	}

	public void AdherenceGraphRaphaelVerification(String CircleObject) {
		try {
			WebElement ActionObject = GetObject(CircleObject);
			String Text[] = ClickAndGetText(ActionObject).split(":");
			CompareText("Dose", LangData("Dose"), Text[0]);
			VerifyTextContained("UnitMg", LangData("UnitMg"), Text[1]);
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, "Exception occured " + e);
		}
	}

	public void AuxologicalGraphTextVerification(String HeaderExpected,
			String YAxisExpected, String XAxisExpected, String Tooltip1,
			String Tooltip2, String DataReferenceExpected,
			boolean HveToCheckTooltip) {
		try {
			String[] Tooltip = null;
			String[] Visit1 = null;
			String[] Visit2 = null;
			// Header
			CompareText("#AuxologicalReports_Header_ST", HeaderExpected,
					GetText("#AuxologicalReports_Header_ST"));

			// Percentiles
			CompareText("Percentiles_ST", LangData("Percentiles"),
					GetText("#AuxologicalReport_Percentiles_ST"));
			CompareText("#AuxologicalReports_Patient_ST", LangData("Patient"),
					GetText("#AuxologicalReports_Patient_ST"));
			CompareText("LastVisit_ST", LangData("LastVisit"),
					GetText("#AuxologicalReports_LastVisit_ST"));
			CompareText("NextVisit_ST", LangData("NextVisit"),
					GetText("#AuxologicalReports_NextVisit_ST"));

			// Standard Deviation
			String StandardDeviationText = GetText(
					"#AuxologicalReport_StandardDeviation_ST").trim();
			if (!StandardDeviationText.isEmpty()) {
				CompareText("StandardDeviation", LangData("StandardDeviation"),
						StandardDeviationText);
			}

			VerifyDataReferenceHelpText(DataReferenceExpected);

			if (!(Env.GetBrowserUsed().startsWith("SA") || Env.GetBrowserUsed()
					.startsWith("IE"))) {

				List<WebElement> Elements = GetAllVisibleElements("#AuxologicalReport_AxisText_DT");
				CompareText("YAxis_ST", YAxisExpected, TextCleanUp(Elements
						.get(0).getText()));
				CompareText("XAxis_ST", XAxisExpected, TextCleanUp(Elements
						.get(1).getText()));

				List<WebElement> CircleObject = GetAllVisibleElements("//*[name()='circle']");

				if (HveToCheckTooltip) {
					if (CircleObject.size() > 0) {
						MoveMouseTo(CircleObject.get(0));
						WebElement TooltipObject = GetObject("#AuxologicalReports_ToolTip_DT");// Tooltip
						if (!TooltipObject.isDisplayed()) {
							String StyleValue = TooltipObject
									.getAttribute("style");
							StyleValue = StyleValue.replace("none;", "block;");
							RunJavaScript(TooltipObject,
									"arguments[0].setAttribute('style', '"
											+ StyleValue + "');");
						}
						if (!TooltipObject.getText().isEmpty()) {
							Tooltip = TooltipObject.getText().split(":");
						}
						CompareText("Measurement_ST", Tooltip1, Tooltip[0]);
						VerifyTextContained("Unit_ST", Tooltip2,
								TextCleanUp(Tooltip[1]));
						VerifyTextContained("On_ST",
								LangData("AuxologicalVisitOn"),
								TextCleanUp(Tooltip[1]));
						VerifyMonth("Date_ST", Tooltip[2]); // EPCV-74

						if (CircleObject.size() > 2) {
							CircleObject.get(1).click();
							WebElement First = GetObject("//*[name()='path'][last()]");
							MoveMouseTo(First);
							if (!GetObject("#AuxologicalReports_ToolTip_DT")
									.isDisplayed()) {
								String StyleValue = First.getAttribute("style");
								StyleValue = StyleValue.replace("none;",
										"block;");
								RunJavaScript(First,
										"arguments[0].setAttribute('style', '"
												+ StyleValue + "');");
							}
							if (!GetText("#AuxologicalReports_ToolTip_DT")
									.isEmpty()) {
								Visit1 = GetText(
										"#AuxologicalReports_ToolTip_DT")
										.split(":");
							}
							CompareText("NextVisit_DT", LangData("NextVisit"),
									Visit1[0]);
							VerifyMonth("Month_DT", Visit1[1]); // EPCV-74

							WebElement Second = GetObject("//*[name()='path'][last()-1]");
							MoveMouseTo(Second);
							if (!GetObject("#AuxologicalReports_ToolTip_DT")
									.isDisplayed()) {
								String StyleValue = Second
										.getAttribute("style");
								StyleValue = StyleValue.replace("none;",
										"block;");
								RunJavaScript(Second,
										"arguments[0].setAttribute('style', '"
												+ StyleValue + "');");
							}
							if (!GetText("#AuxologicalReports_ToolTip_DT")
									.isEmpty()) {
								Visit2 = GetText(
										"#AuxologicalReports_ToolTip_DT")
										.split(":");
							}
							CompareText("LastVisit_DT", LangData("LastVisit"),
									Visit2[0]);
							VerifyMonth("Month_DT", Visit2[1]); // EPCV-74
						}
					}
				}
				VerifyEnlargedAuxologicalGraph(HeaderExpected, YAxisExpected,
						XAxisExpected);
			}
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, "Exception occured", e);
		}
	}

	public void VerifyDataReferenceHelpText(String Expected) {
		CompareText("DataReference", LangData("DataReferences"),
				GetText("#AuxologicalReports_DataReferences_ST"));
		VerifyToolTip("#AuxologicalReports_DataReferences_IM");
		Btn_Click("#AuxologicalReports_DataReferences_ST");
		CompareText("#AuxologicalReports_DataReferencesTitle_ST",
				LangData("DataReferences"),
				GetText("#AuxologicalReports_DataReferencesTitle_ST"));
		CompareText("#AuxologicalReports_DataReferencesContent_DT", Expected,
				GetText("#AuxologicalReports_DataReferencesContent_DT"));
		HideCursor();
	}

	public void VerifyEnlargedAuxologicalGraph(String ExpectedHeader,
			String XAxis, String YAxis) {
		Btn_Click("#AuxologicalReports_Enlarge_LK");
		WaitForObject("//*[@id='canvaspop']//*[name()='tspan']");
		List<WebElement> Element = GetAllVisibleElements("//*[@id='canvaspop']//*[name()='tspan']");
		CompareText("Header_ST", ExpectedHeader, TextCleanUp(Element.get(0)
				.getText()));
		CompareText("XAxis_ST", XAxis, TextCleanUp(Element.get(1).getText()));
		CompareText("YAxis_ST", YAxis, TextCleanUp(Element.get(2).getText()));
		Btn_Click("#AuxologicalReports_EnlargeClose_BN");
	}

	public boolean VerifyMoreInfoText(String MoreInfoObjectName,
			String ExpectedMoreInfoTitle, String ExpectedMoreInfoText) {

		boolean IsPass = true;
		WebElement ActionObject = null;
		String ScreenshotName = "";

		ActionObject = GetObject(MoreInfoObjectName);
		String StepID = MoreInfoObjectName.substring(0,
				MoreInfoObjectName.lastIndexOf("_"));
		String StepMoreInfoTitle = StepID + "Title_DT";
		String StepMoreInfoText = StepID + "Text_DT";
		ExpectedMoreInfoTitle = LangData("MoreInfo") + " - "
				+ ExpectedMoreInfoTitle;

		try {
			CompareText(MoreInfoObjectName, LangData("MoreInfo"),
					GetText(MoreInfoObjectName));
			ActionObject.click();
			Wait(2);
			if (ObjectExists("General_MoreInfoPopupTitle_DT")) {
				ScreenshotName = UtilClass.GetScreenshot(MoreInfoObjectName);
				String ActualMoreInfoTitle = TextCleanUp(GetText("General_MoreInfoPopupTitle_DT"));
				ActualMoreInfoTitle = ActualMoreInfoTitle.replaceAll(" - ",
						" - ");
				ActualMoreInfoTitle = ActualMoreInfoTitle.replaceAll(
						" \u2013 ", " - ");
				String ActualMoreInfoText = TextCleanUp(GetText("General_MoreInfoPopupText_DT"));
				CompareText(StepMoreInfoTitle, ExpectedMoreInfoTitle,
						ActualMoreInfoTitle);
				CompareText(StepMoreInfoText, ExpectedMoreInfoText,
						ActualMoreInfoText);
			} else {
				Report.Write(StepMoreInfoTitle, ExpectedMoreInfoTitle,
						"OBJECT NOT FOUND", "Fail", ScreenshotName);
				Report.Write(StepMoreInfoText, ExpectedMoreInfoText,
						"OBJECT NOT FOUND", "Fail", ScreenshotName);
				IsPass = false;
			}
			HideCursor();

		} catch (Exception e) {
			ScreenshotName = UtilClass.GetScreenshot(MoreInfoObjectName);
			Report.Write(StepMoreInfoTitle, ExpectedMoreInfoTitle, "ERROR",
					"Fail", ScreenshotName);
			Report.Write(StepMoreInfoText, ExpectedMoreInfoText, "ERROR",
					"Fail", ScreenshotName);
			Env.logger.log(Level.ERROR, MoreInfoObjectName, e);
			IsPass = false;
		}
		return IsPass;
	}

	public void VerifyDeviceDetailsText(String ObjectName) {
		Lnk_Click(ObjectName);
		if ((Env.GetBrowserUsed().startsWith("IE") || Env.GetBrowserUsed()
				.startsWith("SA"))) {
			WaitForPageload();
			Wait(2);
		}
		CompareText("General_HelpTitle_DT", LangData("DeviceDetails"),
				GetText("General_MoreInfoPopupTitle_DT"));
		CompareText("General_DevicesUsedHelpText_DT",
				LangData("DevicesUsedinTimeRange"),
				GetText("AdherenceOverview_DevicesUsedHelpText_DT"));
		VerifyTextContained("General_DataLastUpload_DT",
				LangData("DataLastUpload") + " " + ":",
				GetText("AdherenceOverview_DataLastUpload_DT"));
		VerifyMonth("General_DataLastUpload_DT",
				GetOnlyMyText("AdherenceOverview_DataLastUploadValue_DT"));

		if (GetAttributeValue("AdherenceOverview_DeviceDetailsIcon_IM", "class")
				.compareTo("roundIcn") == 0) {
			VerifyRegExpression("BelowWeeks", LangData("DataLastUpload")
					+ " : " + LangData("BelowWeeks"),
					GetText("AdherenceOverview_DataLastUpload_DT"), "\\d{1,2}");
		} else {
			VerifyRegExpression("OverWeeksAgo", LangData("DataLastUpload")
					+ " : " + LangData("OverWeeksAgo"),
					GetText("AdherenceOverview_DataLastUpload_DT"), "\\d{1,2}");

		}
		CompareText("General_DevicesNoteText_DT", LangData("Note") + ":" + " "
				+ LangData("DeviceCaution"),
				GetText("AdherenceOverview_DevicesNoteText_DT"));
		HideCursor();
	}

	public void CombinationGraphVerification(String GrowthVelocityCircle,
			String DoseAdherenceCircle) {
		String ToolTipText = null;
		String[] Tooltip = null;

		// Growth Velocity
		MoveMouseTo(GrowthVelocityCircle);
		WebElement ActionObject = GetObject("CombinationGraph_ToolTip_DT");
		if (!ActionObject.isDisplayed()) {
			String StyleValue = ActionObject.getAttribute("style");
			StyleValue = StyleValue.replace("none;", "block;");
			RunJavaScript(ActionObject, "arguments[0].setAttribute('style', '"
					+ StyleValue + "');");
		}
		ToolTipText = GetText("CombinationGraph_ToolTip_DT");
		if (!ToolTipText.contains(":")) {
			ToolTipText = " : ";
		}
		Tooltip = ToolTipText.split(":");
		CompareText("GowthVelocity",
				LangData("CombinationGraphGrowthVelocity"), Tooltip[0]);
		VerifyTextContained("GowthVelocityValue", LangData("UnitCmPerYear"),
				Tooltip[1]);

		// Dose Adherence
		MoveMouseTo(DoseAdherenceCircle);
		ActionObject = GetObject("CombinationGraph_ToolTip_DT");
		if (!ActionObject.isDisplayed()) {
			String StyleValue = ActionObject.getAttribute("style");
			StyleValue = StyleValue.replace("none;", "block;");
			RunJavaScript(ActionObject, "arguments[0].setAttribute('style', '"
					+ StyleValue + "');");
		}
		ToolTipText = GetText("CombinationGraph_ToolTip_DT");
		if (!ToolTipText.contains(":")) {
			ToolTipText = " : ";
		}
		Tooltip = ToolTipText.split(":");
		CompareText("DoseAdherence", LangData("CombinationGraphDoseAdherence"),
				Tooltip[0]);
	}

	public void DeviceID_Setvalue(String LogicalName, String DeviceEdit1,
			String DeviceEdit2, String DeviceEdit3, String DeviceID,
			boolean VerifyEnteredDeviceID) {
		try {
			Edit_SetValue(DeviceEdit1, DeviceID.substring(0, 4));
			Edit_SetValue(DeviceEdit2, DeviceID.substring(4, 7));
			Edit_SetValue(DeviceEdit3, DeviceID.substring(7));

			if (VerifyEnteredDeviceID) {
				if (CompareText(DeviceID.substring(0, 4),
						Edit_GetValue(DeviceEdit1))
						&& CompareText(DeviceID.substring(4, 7),
								Edit_GetValue(DeviceEdit2))
						&& CompareText(DeviceID.substring(7),
								Edit_GetValue(DeviceEdit3))) {
					Report.Update(LogicalName + "_" + DeviceID,
							"Entered Device ID should be displayed",
							"Entered Device ID is displayed", "Pass");
				} else {
					Report.Update(LogicalName + "_" + DeviceID,
							"Entered Device ID should be displayed",
							"Entered Device ID is not displayed", "Fail");
				}
			}
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, "Exception occured", e);
		}
	}

	public void AdherenceListTableVerification(int row, String Date,
			String Time, String InjectionEvent, String DeviceID,
			String PrescribedDose, String ActualDose) { // To verify the values
														// in Adherence List

		if (!(GetBrowserTitle().contains("ECOS"))) {
			if (!(Edit_GetValue("Pagination_DropDownText_BN").equals("All"))) {
				Btn_Click("Pagination_DropDownText_BN");
				Btn_Click("//div[@class='esItem'][last()]/li"); // TO Select All
																// in the
																// dropdown
			}
		}
		String LogicalName = InjectionEvent + "_" + row + "_";
		CompareText(LogicalName + "Date",
				TranslateDate(Date, "dd MMM yyyy", false),
				WbTbl_GetText("AdherenceList_TableHeader_ST", row, 1));
		CompareText(LogicalName + "Time", Time,
				WbTbl_GetText("AdherenceList_TableHeader_ST", row, 2));
		CompareText(LogicalName + "Event", InjectionEvent,
				WbTbl_GetText("AdherenceList_TableHeader_ST", row, 3));
		CompareText(LogicalName + "DeviceID", DeviceID,
				WbTbl_GetText("AdherenceList_TableHeader_ST", row, 4));
		CompareText(LogicalName + "PrescribedDose", PrescribedDose,
				WbTbl_GetText("AdherenceList_TableHeader_ST", row, 5));
		CompareText(LogicalName + "ActualDose", ActualDose,
				WbTbl_GetText("AdherenceList_TableHeader_ST", row, 6));
	}

	public void CheckBoxSelect(String AdherenceScreen, String Event) {
		if (Event.compareTo("All") == 0) {
			ChkBx_SetOptionForAll("//input[@type='checkbox']", "ON");
		}

		ChkBx_SetOptionForAll("//input[@type='checkbox']", "OFF");
		if (AdherenceScreen.compareTo("List") == 0) { // To select the checkbox
														// in adherence List
			// To select the checkbox in adherence List
			if (Event.equals(LangData("Complete"))) {
				ChkBx_SetOption("AdherenceList_Complete_CB", "ON");

			} else if (Event.equals(LangData("Partial"))) {
				ChkBx_SetOption("AdherenceList_Partial_CB", "ON");
			} else if (Event.equals(LangData("NoInjection"))) {
				ChkBx_SetOption("AdherenceList_NoInjection_CB", "ON");
			} else if (Event.equals(LangData("NewCartridge"))) {
				ChkBx_SetOption("AdherenceList_NewCartridge_CB", "ON");
			} else if (Event.equals(LangData("NewDosageSet"))) {
				ChkBx_SetOption("AdherenceList_NewDosageSet_CB", "ON");
			}
		} else if (AdherenceScreen.compareTo("Graph") == 0) { // To select the
																// checkbox in
																// adherence
																// Graph
			if (Event.equals(LangData("Complete"))) {
				ChkBx_SetOption("AdherenceGraph_Complete_CB", "ON");

			} else if (Event.equals(LangData("Partial"))) {
				ChkBx_SetOption("AdherenceGraph_Partial_CB", "ON");
			} else if (Event.equals(LangData("NoInjection"))) {
				ChkBx_SetOption("AdherenceGraph_NoInjection_CB", "ON");
			} else if (Event.equals(LangData("Multiple"))) {
				ChkBx_SetOption("AdherenceGraph_Multiple_CB", "ON");
			} else if (Event.equals(LangData("PrescribedDose"))) {
				ChkBx_SetOption("AdherenceGraph_PrescribedDose_CB", "ON");
			} else if (Event.equals(LangData("NoData"))) {
				ChkBx_SetOption("AdherenceGraph_NoData_CB", "ON");
			}
		} else if (AdherenceScreen.compareTo("Calendar") == 0) { // To select
																	// the
																	// checkbox
																	// in
																	// adherence
																	// Calendar
			if (Event.equals(LangData("Complete"))) {
				ChkBx_SetOption("AdherenceCalendar_CompleteInjection_CB", "ON");

			} else if (Event.equals(LangData("Partial"))) {
				ChkBx_SetOption("AdherenceCalendar_PartialInjection_CB", "ON");
			} else if (Event.equals(LangData("NoInjection"))) {
				ChkBx_SetOption("AdherenceCalendar_NoInjection_CB", "ON");
			} else if (Event.equals(LangData("Multiple"))) {
				ChkBx_SetOption("AdherenceCalendar_MultipleInjection_CB", "ON");
			}
		} else {
			Log.info("Wrong argument passed");
		}
	}

	public void VerifyAdherenceInjectionOverview(String[] ArrayPercentage,
			int[] ArrayCount, boolean MoreInjectionText) {

		CompareText("Missed", LangData("Missed"),
				GetText("Adherence_InjectionOverviewMissed_ST"));
		CompareText("PercentageValue", ArrayPercentage[0],
				GetText("AdherenceOverview_MissedInjectionValue_DT"));
		CompareText("Count", "(" + ArrayCount[0] + " " + LangData("Injections")
				+ ")",
				GetText("Adherence_InjectionOverviewMissedInjections_ST"));

		CompareText("Partial", LangData("Partial"),
				GetText("Adherence_InjectionOverviewPartial_ST"));
		CompareText("PercentageValue", ArrayPercentage[1],
				GetText("AdherenceOverview_PartialInjectionValue_DT"));
		CompareText("Count", "(" + ArrayCount[1] + " " + LangData("Injections")
				+ ")",
				GetText("Adherence_InjectionOverviewPartialInjections_ST"));

		CompareText("Complete", LangData("Complete"),
				GetText("Adherence_InjectionOverviewComplete_ST"));
		CompareText("PercentageValue", ArrayPercentage[2],
				GetText("AdherenceOverview_CompleteInjectionValue_DT"));
		CompareText("Count", "(" + ArrayCount[2] + " " + LangData("Injections")
				+ ")",
				GetText("Adherence_InjectionOverviewCompleteInjections_ST"));

		CompareText("Multiple", "[ " + LangData("Multiple"),
				GetText("Adherence_InjectionOverviewMultiple_ST"));
		CompareText("PercentageValue", ArrayPercentage[3],
				GetText("AdherenceOverview_MutlipleInjectionValue_DT"));
		CompareText("Count", "(" + ArrayCount[3] + " " + LangData("Injections")
				+ ")",
				GetText("Adherence_InjectionOverviewMultipleInjections_ST"));

		CompareText("InjectionOverviewExpected", LangData("Expected"),
				GetText("Adherence_InjectionOverviewExpected_ST"));
		CompareText("PercentageValue", ArrayPercentage[4],
				GetText("AdherenceOverview_InjectionOverviewValue_DT"));
		CompareText("Count", "(" + ArrayCount[4] + " " + LangData("Injections")
				+ ")",
				GetText("Adherence_InjectionOverviewExpectedInjections_ST"));

		if (MoreInjectionText) {
			CompareText("MoreInjectionsTaken", LangData("MoreInjectionsTaken"),
					GetText("Adherence_InjectionOverviewMoreInjectionTaken_ST"));
		}
	}

	public void VerifyAdherenceDoseOverview(String[] ArrayPercentage,
			String[] ArrayCount, boolean OverdoseOccuredText) {
		CompareText("DoseOverviewMissed", LangData("Missed"),
				GetText("Adherence_DoseOverviewMissed_ST"));
		CompareText("Percentage", ArrayPercentage[0],
				GetText("AdherenceOverview_DoseMissedValue_DT"));
		CompareText("Value", "(" + ArrayCount[0] + " " + LangData("UnitMg")
				+ ")", GetText("Adherence_DoseOverviewMissedMg_ST"));

		CompareText("Administered", LangData("Administered"),
				GetText("Adherence_DoseOverviewAdministered_ST"));
		CompareText("Percentage", ArrayPercentage[1],
				GetText("AdherenceOverview_DoseAdministeredValue_DT"));
		CompareText("UnitMg", "(" + ArrayCount[1] + " " + LangData("UnitMg")
				+ ")", GetText("Adherence_DoseOverviewAdministeredMg_ST"));

		CompareText("DoseOverviewExpected", LangData("Expected"),
				GetText("Adherence_InjectionOverviewExpected_ST"));
		CompareText("Percentage", ArrayPercentage[2],
				GetText("AdherenceOverview_DoseOverviewValue_DT"));
		CompareText("Value", "(" + ArrayCount[2] + " " + LangData("UnitMg")
				+ ")", GetText("Adherence_DoseOverviewExpectedMg_ST"));

		if (OverdoseOccuredText) {
			CompareText("OverDoseOccurred", LangData("OverDoseOccurred"),
					GetText("Adherence_OverdoseOccurred_ST"));
		}
	}

	public String SelectAlternateCountry(String PresentCountry) {
		String AlternateCountry = "";
		if (PresentCountry.equalsIgnoreCase("Canada")) {// if device is to be
														// transferred from
														// Canada
			AlternateCountry = "Sweden";
		} else {
			AlternateCountry = "Canada";
		}
		return AlternateCountry;
	}

	public boolean IsDisplayed(String ObjectName, String LogicalName) { // To
																		// verify
																		// if an
																		// object
																		// is
																		// displayed
		boolean ReturnValue = false;
		WebElement ActionObject = GetObject(ORXPath(ObjectName));
		if (!(ActionObject == null)) {
			if (ActionObject.isDisplayed()) {
				Report.Write(LogicalName, LogicalName + " should be displayed",
						LogicalName + " is displayed", "Pass", true);
				ReturnValue = true;
			} else {
				Report.Write(LogicalName, LogicalName + " should be displayed",
						LogicalName + " is not displayed", "Fail", true);
				ReturnValue = false;
			}
		} else {
			Report.Write(LogicalName, LogicalName + " should be displayed",
					LogicalName + " is not displayed", "Fail", true);
			ReturnValue = false;
		}
		return ReturnValue;
	}

	public void AdherenceGraphInjectionCount(String InjectionType,
			String InjectionTypeXpath, int ExpectedCount) {
		CheckBoxSelect("Graph", InjectionType);
		int count = 0;
		List<WebElement> Elements = GetAllVisibleElements(InjectionTypeXpath);
		for (int i = 0; i < Elements.size(); i++) {
			if (InjectionType.equalsIgnoreCase("Partial")) {
				if (Elements.get(i).getAttribute("fill").equals("#6c3a00")) {
					count++;
				}
			} else if (InjectionType.equalsIgnoreCase("Complete")) {
				if (Elements.get(i).getAttribute("fill").equals("#04c059")) {
					count++;
				}
			} else if (InjectionType.equalsIgnoreCase("Multiple")) {
				if (Elements.get(i).getAttribute("fill").equals("#467cf7")) {
					count++;
				}
			} else if (InjectionType.equalsIgnoreCase("No Injection")) {
				if (Elements.get(i).getAttribute("fill").equals("#e24d04")) {
					count++;
				}
			}
		}

		if (InjectionType.equalsIgnoreCase("Partial") && (count != 1)) { // To
																			// handle
																			// Partial
																			// Cases
			count = count / 2;
		}
		if (ExpectedCount == count) {
			Report.Update(InjectionType, "Count should be " + ExpectedCount,
					"Count is " + count, "Pass");
		} else {
			Report.Update(InjectionType, "Count should be " + ExpectedCount,
					"Count is " + count, "Fail");
		}
	}

	public void AdherenceCalendarInjectionData(String InjectionType,
			String InjectionTypeXpath, String[] ExpectedValues) {

		try {
			int count = GetAllElements(InjectionTypeXpath).size();
			for (int i = 1; i <= count; i++) {
				CompareText(InjectionType + "Date", ExpectedValues[i - 1],
						GetText("(" + ORXPath(InjectionTypeXpath)
								+ "//preceding-sibling::span)[" + i + "]"));
				if (ObjectExists(ORXPath(InjectionTypeXpath) + "[" + i + "]")) {
					Report.Update(InjectionType + "_Icon", InjectionType
							+ " Icon should be present for the Date "
							+ ExpectedValues[i - 1], InjectionType
							+ " Icon is present for the Date "
							+ ExpectedValues[i - 1], "Pass");
				} else {
					Report.Update(InjectionType + "_Icon", InjectionType
							+ " Icon should be present for the Date "
							+ ExpectedValues[i - 1], InjectionType
							+ " Icon is not present for the Date "
							+ ExpectedValues[i - 1], "Fail");
				}

			}
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, "Exception occured", e);
		}
	}

	public void CheckPercentilesAndDevaition() {
		try {
			List<WebElement> CheckedCheckBoxCount = GetAllElements("//input[@type='checkbox']");// Checked
																								// checkboxes
																								// count
			for (int i = 0; i < CheckedCheckBoxCount.size(); i++) {
				if (CheckedCheckBoxCount.get(i).isSelected()) {
					String Text = GetText("(//input[@type='checkbox'])["
							+ (i + 1) + "]//following-sibling::*");
					Text = Text.replace("±", "");
					String Xpath = "(//*[name()='tspan'])[text()='" + Text
							+ "']";// Constructing Xpath for to check elements
									// in graph
					if (ObjectExists(Xpath)) {
						Report.Update("Percentiles_ST", Text
								+ " line should be displayed in the graph.",
								Text + " line is displayed in the graph.",
								"Pass");
					} else {
						Report.Update("Percentiles_ST", Text
								+ " line should be displayed in the graph.",
								Text + " line is not displayed in the graph.",
								"Fail");
					}
				}
			}// For loop ends
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, "Exception occured", e);
		}
	}

	public void VerifyVisitWithAuxological(String WhichModule, int StartFrom,
			List<String> VisitDate, List<String> Height, List<String> Weight,
			List<String> Velocity) {
		try {
			String CircleXpath = null;
			String[] Tooltip = null;
			String StyleValue = null;
			for (int i = 1; i <= GetAllVisibleElements("//*[name()='circle']")
					.size(); i++) {
				// boolean IsJSExecuted = false;
				CircleXpath = "//*[name()='circle']" + "[" + i + "]";

				WebElement CircleObject = GetObject(CircleXpath);
				MoveMouseTo(CircleObject);
				WebElement TooltipObject = GetObject("#AuxologicalReports_ToolTip_DT");// Tooltip
				// if(!TooltipObject.isDisplayed()){
				StyleValue = TooltipObject.getAttribute("style");
				StyleValue = StyleValue.replace("none;", "block;");
				RunJavaScript(TooltipObject,
						"arguments[0].setAttribute('style', '" + StyleValue
								+ "');");
				Wait(2);
				// IsJSExecuted = true;
				// }
				// if(!TooltipObject.getText().isEmpty()){
				Tooltip = GetText(TooltipObject).split(":");
				// }
				CompareText("VisitDate", VisitDate.get(StartFrom), Tooltip[2]);

				if (WhichModule.compareToIgnoreCase("Growth Velocity Curve") == 0) {
					CompareText("Velocity", Velocity.get(StartFrom) + " "
							+ LangData("UnitCmPerYear"),
							Tooltip[1].split("\n")[0]);
				} else if (WhichModule.compareToIgnoreCase("Growth Curve") == 0) {
					CompareText("Height", Height.get(StartFrom) + " "
							+ LangData("Unitcm"), Tooltip[1].split("\n")[0]);
				} else if (WhichModule.compareToIgnoreCase("Weight Curve") == 0) {
					CompareText("Weight", Weight.get(StartFrom) + " "
							+ LangData("UnitKg"), Tooltip[1].split("\n")[0]);
				}
				StartFrom++;

				// if(IsJSExecuted){
				StyleValue = StyleValue.replace("block;", "none;");
				RunJavaScript(TooltipObject,
						"arguments[0].setAttribute('style', '" + StyleValue
								+ "');");
				HideCursor();
				// }
			}
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, "Exception occured", e);
		}

	}

	public void VerifyBMIAuxological(int StartFrom, List<String> VisitDate,
			List<String> Height, List<String> Weight) {
		try {
			String CircleXpath = null;
			String[] Tooltip = null;
			String StyleValue = null;
			for (int i = 1; i <= GetAllVisibleElements("//*[name()='circle']")
					.size(); i++) {

				CircleXpath = "//*[name()='circle']" + "[" + i + "]";
				// Calculating BMI Value
				Double HeightBMI = (Double.parseDouble(Height.get(StartFrom)) * Double
						.parseDouble(Height.get(StartFrom)));
				Double WeightBMI = (Double.parseDouble(Weight.get(StartFrom)));

				double BMIAPP = (WeightBMI / HeightBMI) * (10000);
				String BMIValue = String.format("%.2f", BMIAPP);

				WebElement CircleObject = GetObject(CircleXpath);
				MoveMouseTo(CircleObject);
				WebElement TooltipObject = GetObject("#AuxologicalReports_ToolTip_DT");// Tooltip

				StyleValue = TooltipObject.getAttribute("style");
				StyleValue = StyleValue.replace("none;", "block;");
				RunJavaScript(TooltipObject,
						"arguments[0].setAttribute('style', '" + StyleValue
								+ "');");
				Wait(2);

				Tooltip = GetText(TooltipObject).split(":");

				CompareText("VisitDate", VisitDate.get(StartFrom), Tooltip[2]);
				CompareText("BMIValue", BMIValue,
						Tooltip[1].trim().split(" ")[0]);

				StartFrom++;

				StyleValue = StyleValue.replace("block;", "none;");
				RunJavaScript(TooltipObject,
						"arguments[0].setAttribute('style', '" + StyleValue
								+ "');");
				HideCursor();

			}
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, "Exception occured", e);
		}
	}

	public void UploadLegalDocument(String Language, String FileName,
			boolean RemoveExisting) {
		try {
			DrpDwn_SelectText("CountrySettings_LegalDocumentsLanguage_DD",
					Language); // Select the language
			Wait(1);

			if (RemoveExisting) { // To remove the Exisiting Document
				while (ObjectExists("CountrySettings_RemoveLegalDocuments_DT")) {
					Lnk_Click("CountrySettings_RemoveLegalDocuments_DT");
				}
			}

			int Count = GetAllVisibleElements(
					"CountrySettings_LegalDocumentsList_DT").size();
			DrpDwn_SelectText("CountrySettings_LegalDocumentsLanguage_DD",
					Language);
			Wait(2);
			Btn_Click("CountrySettings_LegalDocumentsUpload_BN");
			WaitForObject("CountrySettings_FileUploadSubmit_BN");
			FileUpload_SetFile("CountrySettings_Browse_BN", GetDataPath()
					+ FileName);
			Btn_Click("CountrySettings_FileUploadSubmit_BN");
			if (GetAllVisibleElements("CountrySettings_LegalDocumentsList_DT")
					.size() == Count + 1) {
				Report.Update(
						"FilePath_"
								+ FileName
										.substring(FileName.lastIndexOf("/") + 1),
						"File Should be uploaded", "File is uploaded", "Pass");
			} else {
				Report.Update(
						"FilePath_"
								+ FileName
										.substring(FileName.lastIndexOf("/") + 1),
						"File Should be uploaded", "File is not uploaded",
						"Fail");
			}

		} catch (Exception e) {
			Env.logger.log(Level.ERROR, "Exception occured", e);
		}
	}

	public void CheckEmailLanguageForHCP() {
		String LanguagesForWhichHCPisEnglish = "Indonesian|Serbian|Thai|Filipino|Vietnamese|Malay|Arabic";
		if (LanguagesForWhichHCPisEnglish.contains(Env.LanguageUsed)) {
			ReadOnlyEnglish(true);
		}
	}

	public void RevertEmailLanguageForHCP() {
		ReadOnlyEnglish(false);
	}

}
