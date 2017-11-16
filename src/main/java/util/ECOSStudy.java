package functions;

import java.util.Calendar;

import org.openqa.selenium.Keys;

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

public class ECOSStudy extends ECOSStudyBaseline {

	// ================================================General ECOS methods
	// Starts here================================================
	public ECOSStudy(Easypod RunningUtil) {
		super.LoadBaseline(RunningUtil);
	}

	// =============================================================================================================================================
	// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-* Methods for DataRecord
	// Visits*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-**-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
	// =============================================================================================================================================

	public void AuxologicalDataVisitsStatic(String ActionDone, String TabIndex) {

		String TabName = AppendHash(ActionDone);
		VerifyChapterName(ActionDone, TabIndex,
				EP.LangData("AuxologicalDataVisits"));

		if (EP.CompareText(ActionDone, "AddVisit")) {
			VerifyChapterStatus(ActionDone, TabIndex, EP.LangData("NoData"));
		} else {
			VerifyChapterStatus(ActionDone, TabIndex,
					EP.ORData("#General_ChapterStatus_ST"));
		}

		switch (switchCountryName) {

		case AUSTRALIA:
			AustraliaECRF AustraliaDR = new AustraliaECRF(EP);
			AustraliaDR.AuxologicalDataVisitsStatic(ActionDone, TabIndex);
			AustraliaDR = null;
			break;

		case TAIWAN:
			TaiwanECRF TaiwanDR = new TaiwanECRF(EP);
			TaiwanDR.AuxologicalDataVisitsStatic(ActionDone, TabIndex);
			TaiwanDR = null;
			break;

		case COLOMBIA:
			ColombiaECRF ColombiaDR = new ColombiaECRF(EP);
			ColombiaDR.AuxologicalDataVisitsStatic(ActionDone, TabIndex);
			ColombiaDR = null;
			break;

		case INDONESIA:
			IndonesiaECRF IndonesiaDR = new IndonesiaECRF(EP);
			IndonesiaDR.AuxologicalDataVisitsStatic(ActionDone, TabIndex);
			IndonesiaDR = null;
			break;

		case AUSTRIA:
			AustriaECRF AustriaDR = new AustriaECRF(EP);
			AustriaDR.AuxologicalDataVisitsStatic(ActionDone, TabIndex);
			AustriaDR = null;
			break;

		case ARGENTINA:
			ArgentinaECRF ArgentinaDR = new ArgentinaECRF(EP);
			ArgentinaDR.AuxologicalDataVisitsStatic(ActionDone, TabIndex);
			ArgentinaDR = null;
			break;

		case CZECHREPUBLIC:
			CzechRepublicECRF CzechDR = new CzechRepublicECRF(EP);
			CzechDR.AuxologicalDataVisitsStatic(ActionDone, TabIndex);
			CzechDR = null;
			break;

		case FINLAND:
			FinlandECRF FinlandDR = new FinlandECRF(EP);
			FinlandDR.AuxologicalDataVisitsStatic(ActionDone, TabIndex);
			FinlandDR = null;
			break;

		case GREECE:
			GreeceECRF GreeceDR = new GreeceECRF(EP);
			GreeceDR.AuxologicalDataVisitsStatic(ActionDone, TabIndex);
			GreeceDR = null;
			break;

		case HUNGARY:
			HungaryECRF HungaryDR = new HungaryECRF(EP);
			HungaryDR.AuxologicalDataVisitsStatic(ActionDone, TabIndex);
			HungaryDR = null;
			break;

		case ICELAND:
			IcelandECRF IcelandDR = new IcelandECRF(EP);
			IcelandDR.AuxologicalDataVisitsStatic(ActionDone, TabIndex);
			IcelandDR = null;
			break;

		case ITALY:
			ItalyECRF ItalyDR = new ItalyECRF(EP);
			ItalyDR.AuxologicalDataVisitsStatic(ActionDone, TabIndex);
			ItalyDR = null;
			break;

		case KOREA:
			KoreaECRF KoreaDR = new KoreaECRF(EP);
			KoreaDR.AuxologicalDataVisitsStatic(ActionDone, TabIndex);
			KoreaDR = null;
			break;

		case NORWAY:
			NorwayECRF NorwayDR = new NorwayECRF(EP);
			NorwayDR.AuxologicalDataVisitsStatic(ActionDone, TabIndex);
			NorwayDR = null;
			break;

		case SLOVAKIA:
			SlovakiaECRF SlovakiaDR = new SlovakiaECRF(EP);
			SlovakiaDR.AuxologicalDataVisitsStatic(ActionDone, TabIndex);
			SlovakiaDR = null;
			break;

		case SPAIN:
			SpainECRF SpainDR = new SpainECRF(EP);
			SpainDR.AuxologicalDataVisitsStatic(ActionDone, TabIndex);
			SpainDR = null;
			break;

		case SWEDEN:
			SwedenECRF SwedenDR = new SwedenECRF(EP);
			SwedenDR.AuxologicalDataVisitsStatic(ActionDone, TabIndex);
			SwedenDR = null;
			break;
		case SAUDIARABIA:
			SaudiArabiaECRF SaudiArabiaDR = new SaudiArabiaECRF(EP);
			SaudiArabiaDR.AuxologicalDataVisitsStatic(ActionDone, TabIndex);
			SaudiArabiaDR = null;
			break;

		case UNITEDKINGDOM:
			UnitedKingdomECRF UnitedKingdomDR = new UnitedKingdomECRF(EP);
			UnitedKingdomDR.AuxologicalDataVisitsStatic(ActionDone, TabIndex);
			UnitedKingdomDR = null;
			break;

		case GERMANY:
			// case ARGENTINA:
			VerifyChapterHeading(ActionDone, TabIndex, 1,
					EP.LangData("AuxologicalInformationVisits"));

			// Retrospective Table
			EP.VerifyTableHeader(TabName + "RetrospectiveAT_TE");

			// Prospective Table
			EP.VerifyTableHeader(TabName + "ProspectiveAT_TE");

			if (!EP.CompareText(ActionDone, "RecordOverview")) {
				EP.VerifyText(TabName + "RetrospectiveTitle_ST");
				EP.VerifyText(TabName + "ProspectiveTitle_ST");
			} else {
				EP.VerifyText(TabName + "RetrospectiveTitleCA_ST");
				EP.VerifyText(TabName + "ProspectiveTitleCA_ST");

				// Verify tooltip and values in the table
				CheckTableText(TabName + "RetrospectiveAT_TE", 1, 1, 1,
						EP.LangData("XMLDate"), EP.MonthShortAll());
				CheckTableText(TabName + "RetrospectiveAT_TE", 1, 2, 1,
						EP.LangData("XMLHeight"), "");
				CheckTableText(TabName + "RetrospectiveAT_TE", 1, 3, 1,
						EP.LangData("XMLWeight"), "");
				CheckTableText(TabName + "RetrospectiveAT_TE", 2, 4, 1,
						EP.LangData("GrowthVelocityPatient"),
						EP.LangData("GrowthVelocityUnit"));

			}
			break;
		case MEXICO:
			MexicoECRF MexicoDR = new MexicoECRF(EP);
			MexicoDR.AuxologicalDataVisitsStatic(ActionDone, TabIndex);
			MexicoDR = null;
			break;
		case CANADA:
			CanadaECRF CanadaDR = new CanadaECRF(EP);
			CanadaDR.AuxologicalDataVisitsStatic(ActionDone, TabIndex);
			CanadaDR = null;
			break;

		case FRANCE:
			FranceECRF FranceDR = new FranceECRF(EP);
			FranceDR.AuxologicalDataVisitsStatic(ActionDone, TabIndex);
			FranceDR = null;
			break;

		/*
		 * case CHINA: VerifyChapterHeading(ActionDone,TabIndex,
		 * 1,EP.LangData("AuxologicalInformationVisits"));
		 * 
		 * //Retrospective Table
		 * EP.VerifyTableHeader(TabName+"RetrospectiveAT_TE");
		 * 
		 * //Prospective Table EP.VerifyTableHeader(TabName+"ProspectiveAT_TE");
		 * 
		 * if (!EP.CompareText(ActionDone, "RecordOverview")){
		 * EP.VerifyText(TabName+"RetrospectiveTitle_ST");
		 * EP.VerifyText(TabName+"ProspectiveTitle_ST"); } else {
		 * EP.VerifyText(TabName+"RetrospectiveTitleCN_ST");
		 * EP.VerifyText(TabName+"ProspectiveTitleCN_ST");
		 * 
		 * //Verify table columns
		 * EP.WbTbl_VerifyTableColumn(TabName+"RetrospectiveAT_TE", "Date", 1,
		 * 5, EP.MonthShortAll(), true);
		 * EP.WbTbl_VerifyTableColumn(TabName+"RetrospectiveAT_TE", "Height", 2,
		 * 5, EP.LangData("Centimetre"), true);
		 * EP.WbTbl_VerifyTableColumn(TabName+"RetrospectiveAT_TE", "Weight", 3,
		 * 5, EP.LangData("Kilogram"), true);
		 * EP.WbTbl_VerifyTableColumn(TabName+"RetrospectiveAT_TE",
		 * "GrowthVelocity", 4, 5, EP.LangData("GrowthVelocityUnit"), true);
		 * 
		 * 
		 * } break;
		 */
		case SINGAPORE:
			SingaporeECRF SingaporeDR = new SingaporeECRF(EP);
			SingaporeDR.AuxologicalDataVisitsStatic(ActionDone, TabIndex);
			SingaporeDR = null;
			break;

		case UNITEDARABEMIRATES:
			UnitedArabEmiratesECRF UnitedArabEmiratesDR = new UnitedArabEmiratesECRF(
					EP);
			UnitedArabEmiratesDR.AuxologicalDataVisitsStatic(ActionDone,
					TabIndex);
			UnitedArabEmiratesDR = null;
			break;

		default:

		}

	}

	public void AuxologicalDataVisitsDynamic(String ActionDone,
			String TabIndex, String RecoveringURL) {
		String TabName = AppendHash(ActionDone);
		String VisitDate = EP.Date_AddDays(EP.TestData("ECOSPatientDOB"), 3,
				Calendar.DATE);
		EP.CompareText("#General_First_BN", EP.LangData("Add"),
				EP.GetText("#General_First_BN")); // Add Button
		if (EP.ObjectExists("#General_Delete_IM")) {
			DeleteRecord("#General_Delete_IM", false);
			EP.Btn_Click("#General_First_BN");
		} else {
			EP.Btn_Click("#General_First_BN");
			EP.WaitForPageload();
		}
		switch (switchCountryName) {

		case AUSTRALIA:
			AustraliaECRF AustraliaDR = new AustraliaECRF(EP);
			AustraliaDR.AuxologicalDataVisitsDynamic(ActionDone, TabIndex,
					RecoveringURL);
			AustraliaDR = null;
			break;

		case AUSTRIA:
			AustriaECRF AustriaDR = new AustriaECRF(EP);
			AustriaDR.AuxologicalDataVisitsDynamic(ActionDone, TabIndex,
					RecoveringURL);
			AustriaDR = null;
			break;

		case COLOMBIA:
			ColombiaECRF ColombiaDR = new ColombiaECRF(EP);
			ColombiaDR.AuxologicalDataVisitsDynamic(ActionDone, TabIndex,
					RecoveringURL);
			ColombiaDR = null;
			break;

		case INDONESIA:
			IndonesiaECRF IndonesiaDR = new IndonesiaECRF(EP);
			IndonesiaDR.AuxologicalDataVisitsDynamic(ActionDone, TabIndex,
					RecoveringURL);
			IndonesiaDR = null;
			break;

		case ARGENTINA:
			ArgentinaECRF ArgentinaDR = new ArgentinaECRF(EP);
			ArgentinaDR.AuxologicalDataVisitsDynamic(ActionDone, TabIndex,
					RecoveringURL);
			ArgentinaDR = null;
			break;
		case SINGAPORE:
			SingaporeECRF SingaporeDR = new SingaporeECRF(EP);
			SingaporeDR.AuxologicalDataVisitsDynamic(ActionDone, TabIndex,
					RecoveringURL);
			SingaporeDR = null;
			break;

		case CZECHREPUBLIC:
			CzechRepublicECRF CzechDR = new CzechRepublicECRF(EP);
			CzechDR.AuxologicalDataVisitsDynamic(ActionDone, TabIndex,
					RecoveringURL);
			CzechDR = null;
			break;

		case FINLAND:
			FinlandECRF FinlandDR = new FinlandECRF(EP);
			FinlandDR.AuxologicalDataVisitsDynamic(ActionDone, TabIndex,
					RecoveringURL);
			FinlandDR = null;
			break;

		case GREECE:
			GreeceECRF GreeceDR = new GreeceECRF(EP);
			GreeceDR.AuxologicalDataVisitsDynamic(ActionDone, TabIndex,
					RecoveringURL);
			GreeceDR = null;
			break;

		case HUNGARY:
			HungaryECRF HungaryDR = new HungaryECRF(EP);
			HungaryDR.AuxologicalDataVisitsDynamic(ActionDone, TabIndex,
					RecoveringURL);
			HungaryDR = null;
			break;

		case ICELAND:
			IcelandECRF IcelandDR = new IcelandECRF(EP);
			IcelandDR.AuxologicalDataVisitsDynamic(ActionDone, TabIndex,
					RecoveringURL);
			IcelandDR = null;
			break;

		case ITALY:
			ItalyECRF ItalyDR = new ItalyECRF(EP);
			ItalyDR.AuxologicalDataVisitsDynamic(ActionDone, TabIndex,
					RecoveringURL);
			ItalyDR = null;
			break;

		case KOREA:
			KoreaECRF KoreaDR = new KoreaECRF(EP);
			KoreaDR.AuxologicalDataVisitsDynamic(ActionDone, TabIndex,
					RecoveringURL);
			KoreaDR = null;
			break;

		case TAIWAN:
			TaiwanECRF TaiwanDR = new TaiwanECRF(EP);
			TaiwanDR.AuxologicalDataVisitsDynamic(ActionDone, TabIndex,
					RecoveringURL);
			TaiwanDR = null;
			break;

		case NORWAY:
			NorwayECRF NorwayDR = new NorwayECRF(EP);
			NorwayDR.AuxologicalDataVisitsDynamic(ActionDone, TabIndex,
					RecoveringURL);
			NorwayDR = null;
			break;

		case SLOVAKIA:
			SlovakiaECRF SlovakiaDR = new SlovakiaECRF(EP);
			SlovakiaDR.AuxologicalDataVisitsDynamic(ActionDone, TabIndex,
					RecoveringURL);
			SlovakiaDR = null;
			break;

		case SPAIN:
			SpainECRF SpainDR = new SpainECRF(EP);
			SpainDR.AuxologicalDataVisitsDynamic(ActionDone, TabIndex,
					RecoveringURL);
			SpainDR = null;
			break;

		case SWEDEN:
			SwedenECRF SwedenDR = new SwedenECRF(EP);
			SwedenDR.AuxologicalDataVisitsDynamic(ActionDone, TabIndex,
					RecoveringURL);
			SwedenDR = null;
			break;

		case SAUDIARABIA:
			SaudiArabiaECRF SaudiArabiaDR = new SaudiArabiaECRF(EP);
			SaudiArabiaDR.AuxologicalDataVisitsDynamic(ActionDone, TabIndex,
					RecoveringURL);
			SaudiArabiaDR = null;
			break;

		case UNITEDKINGDOM:
			UnitedKingdomECRF UnitedKingdomDR = new UnitedKingdomECRF(EP);
			UnitedKingdomDR.AuxologicalDataVisitsDynamic(ActionDone, TabIndex,
					RecoveringURL);
			UnitedKingdomDR = null;
			break;

		case MEXICO:
			MexicoECRF MexicoDR = new MexicoECRF(EP);
			MexicoDR.AuxologicalDataVisitsDynamic(ActionDone, TabIndex,
					RecoveringURL);
			MexicoDR = null;
			break;

		case CANADA:
			CanadaECRF CanadaDR = new CanadaECRF(EP);
			CanadaDR.AuxologicalDataVisitsDynamic(ActionDone, TabIndex,
					RecoveringURL);
			CanadaDR = null;
			break;

		// case CHINA:

		case GERMANY:

			EP.TakeStaticScreenshot("VisitAuxologicalAddEdit");
			EP.CompareText("AuxologicalDate",
					EP.TranslateDate(VisitDate, "dd MMM yyyy", false),
					EP.Edit_GetValue("#AddVisit_AuxologicalDate_EB"));
			EP.VerifyText(TabName + "AuxologicalDate_ST"); // Date
			EP.VerifyMonth(TabName + "AuxologicalDate_EB");// DateValue Dec
			EP.VerifyText(TabName + "AuxologicalHeight_ST");// Height
			EP.VerifyTextContained(TabName + "AuxologicalHeightCm_ST");// Cm
			EP.VerifyText(TabName + "AuxologicalWeight_ST");// Weight
			EP.VerifyTextContained(TabName + "AuxologicalWeightKg_ST");// Kg
			EP.VerifyText(TabName + "AuxologicalGrowthVelocity_ST");// Growth
																	// Velocity
			EP.VerifyTextContained(TabName
					+ "AuxologicalGrowthVelocityCMYear_ST");// cm/year
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
			EP.Edit_ClearValue("#AddVisit_AuxologicalHeight_EB");
			EP.HideCursor();
			EP.WaitForPageload();
			EP.Edit_ClearValue("#AddVisit_AuxologicalWeight_EB");
			EP.Btn_Click("#General_AddRecordSave_BN");// Click save button
			EP.CompareText("Save_BN", EP.LangData("IgnoreAndSave"),
					EP.GetText("#General_AddRecordSave_BN"));
			EP.CompareText("#General_VisitsErrMsg_DT",
					EP.LangData("OutOfRangeError"),
					EP.GetText("#General_VisitsErrMsg_DT"));
			EP.CompareText("HeightMandatoryMessgae",
					EP.LangData("FieldIsMandatory"), GetErrorMessage(1));
			EP.CompareText("WeightMandatoryMessgae",
					EP.LangData("FieldIsMandatory"), GetErrorMessage(2));
			EP.Btn_Click("#General_AddRecordSave_BN");
			VerifyChapterStatus(ActionDone, TabIndex, EP.LangData("Partial"));
			EP.Btn_Click("#General_Edit_IM");

			// Greater than the Max Value - Value must be in the range between
			// 50 and 100
			EP.Edit_SetValue(TabName + "AuxologicalHeight_EB", "196" + Keys.TAB);
			EP.HideCursor();
			EP.WaitForPageload();
			EP.Edit_SetValue(TabName + "AuxologicalWeight_EB", "101");
			EP.Btn_Click("#General_AddRecordSave_BN");// Click save button
			EP.CompareText("#General_VisitsErrMsg_DT",
					EP.LangData("OutOfRangeError"),
					EP.GetText("#General_VisitsErrMsg_DT"));// outof range error
															// msg
			EP.VerifyRegExp(TabName + "AuxologicalHeightErrMsg_DT", "\\d{1,3}");
			EP.VerifyRegExp(TabName + "AuxologicalWeightErrMsg_DT", "\\d{1,3}");
			EP.CompareText("Save_BN", EP.LangData("IgnoreAndSave"),
					EP.GetText("#General_AddRecordSave_BN"));

			// Incorrect format validation
			EP.Edit_SetValue(TabName + "AuxologicalHeight_EB", IncorrectFormat
					+ Keys.TAB);
			EP.HideCursor();
			EP.WaitForPageload();
			EP.Edit_SetValue(TabName + "AuxologicalWeight_EB", IncorrectFormat);
			EP.Btn_Click("#General_AddRecordSave_BN"); // click save button
			EP.CompareText("#General_VisitsErrMsg_DT",
					EP.LangData("VerifyIncorrectFormats"),
					EP.GetText("#General_VisitsErrMsg_DT"));// Incorrect format
															// error msg
			EP.CompareText(TabName + "AuxologicalHeightErrMsg_DT",
					EP.LangData("IncorrectFormat"),
					EP.GetText(TabName + "AuxologicalHeightErrMsg_DT"));
			EP.CompareText(TabName + "AuxologicalWeightErrMsg_DT",
					EP.LangData("IncorrectFormat"),
					EP.GetText(TabName + "AuxologicalWeightErrMsg_DT"));
			EP.CompareText("Save_BN", EP.LangData("Save"),
					EP.GetText("#General_AddRecordSave_BN"));// For Incorrect
																// format Save
																// button ly
																// should get
																// displayed.

			// Lesser than the Min Value - Value must be <=5-
			EP.Edit_SetValue(TabName + "AuxologicalHeight_EB", MinValue
					+ Keys.TAB);
			EP.HideCursor();
			EP.WaitForPageload();
			EP.Edit_SetValue(TabName + "AuxologicalWeight_EB", MinValue);
			EP.Btn_Click("#General_AddRecordSave_BN"); // click save button
			EP.CompareText("#General_VisitsErrMsg_DT",
					EP.LangData("VerifyIncorrectFormats"),
					EP.GetText("#General_VisitsErrMsg_DT"));// outof range error
															// msg
			EP.VerifyRegExp(TabName + "AuxologicalHeightValueMustBeErrMsg_DT",
					"\\d{1,3}");
			EP.VerifyRegExp(TabName + "AuxologicalWeightValueMustBeErrMsg_DT",
					"\\d{1,3}");

			EP.Edit_SetValue(TabName + "AuxologicalHeight_EB", "195" + Keys.TAB);// 196
			EP.HideCursor();
			EP.WaitForPageload();
			EP.Edit_SetValue(TabName + "AuxologicalWeight_EB", "100");// 101
			EP.Btn_Click("#General_AddRecordSave_BN"); // click save button
			VerifySuccessMessage(EP.LangData("AuxologicalDataVisits"));
			VerifyChapterStatus(ActionDone, TabIndex, EP.LangData("Complete"));// Verify
																				// chapter
																				// status

			// Tooltip for Edit and Delete
			EP.VerifyToolTip("#General_Edit_IM");
			EP.VerifyToolTip("#General_Delete_IM");

			// Verify tooltip and values in the table
			CheckTableText(TabName + "Retrospective_TE", 1, 1, 1,
					EP.LangData("XMLDate"), EP.MonthShortAll());
			CheckTableText(TabName + "Retrospective_TE", 1, 2, 1,
					EP.LangData("XMLHeight"), EP.LangData("Centimetre"));
			CheckTableText(TabName + "Retrospective_TE", 1, 3, 1,
					EP.LangData("XMLWeight"), EP.LangData("Kilogram"));
			CheckTableText(TabName + "Retrospective_TE", 1, 4, 1,
					EP.LangData("GrowthVelocityPatient"),
					EP.LangData("GrowthVelocityUnit"));

			// Delete msg validation (Should be done only for VisitsRecords)
			if (EP.CompareText(ActionDone, "VisitsRecord")) {
				DeleteRecord("#General_Delete_IM", true);
				VerifyDeleteMessage(EP.LangData("AuxologicalDataVisits"));
			}
			break;

		/*
		 * case ARGENTINA:
		 * 
		 * EP.TakeStaticScreenshot("VisitAuxologicalAddEdit");
		 * EP.VerifyText(TabName+"AuxologicalDate_ST"); //Date
		 * EP.VerifyMonth(TabName+"AuxologicalDate_EB");//DateValue Dec
		 * EP.VerifyText(TabName+"AuxologicalHeight_ST");//Height
		 * EP.VerifyTextContained(TabName+"AuxologicalHeightCm_ST");//Cm
		 * EP.VerifyText(TabName+"AuxologicalWeight_ST");//Weight
		 * EP.VerifyTextContained(TabName+"AuxologicalWeightKg_ST");//Kg
		 * EP.VerifyText(TabName+"AuxologicalGrowthVelocity_ST");//Growth
		 * Velocity
		 * EP.VerifyTextContained(TabName+"AuxologicalGrowthVelocityCMYear_ST"
		 * );//cm/year EP.CompareText("Save_BN", EP.LangData("Save"),
		 * EP.GetText("#General_AddRecordSave_BN")); EP.CompareText("Cancel_BN",
		 * EP.LangData("Cancel"), EP.GetText("#General_AddRecordCancel_BN"));
		 * 
		 * 
		 * EP.VerifyPageLayout(); if (Env.GetBrowserUsed().startsWith("SA") ||
		 * Env.GetBrowserUsed().startsWith("OP")){ EP.NavigateTo(RecoveringURL);
		 * EP.WaitForPageload(); EP.ShowTab(TabIndex);
		 * EP.Btn_Click("#General_First_BN"); }
		 * 
		 * // //+-50 // EP.Edit_SetValue(TabName+"AuxologicalHeight_EB", "300");
		 * // EP.HideCursor(); // EP.WaitForPageload(); //
		 * EP.Edit_SetValue(TabName+"AuxologicalWeight_EB", "150"); //
		 * EP.Btn_Click("#General_AddRecordSave_BN");//Click save button //
		 * EP.CompareText("#General_VisitsErrMsg_DT",
		 * EP.LangData("OutOfRangeError"),
		 * EP.GetText("#General_VisitsErrMsg_DT"));//outof range error msg
		 * 
		 * // EP.CompareText(TabName+"AuxologicalHeightErrMsg_DT",
		 * EP.LangData("MinMaxChange"),
		 * EP.GetText(TabName+"AuxologicalHeightErrMsg_DT")); //
		 * EP.CompareText(TabName+"AuxologicalWeightErrMsg_DT",
		 * EP.LangData("MinMaxChange"),
		 * EP.GetText(TabName+"AuxologicalWeightErrMsg_DT"));
		 * 
		 * 
		 * //Greater than the Max Value - Value must be in the range between 50
		 * and 100 EP.Edit_SetValue(TabName+"AuxologicalHeight_EB", "196");
		 * EP.HideCursor(); EP.WaitForPageload();
		 * EP.Edit_SetValue(TabName+"AuxologicalWeight_EB", "101");
		 * EP.Btn_Click("#General_AddRecordSave_BN");//Click save button
		 * EP.CompareText("#General_VisitsErrMsg_DT",
		 * EP.LangData("OutOfRangeError"),
		 * EP.GetText("#General_VisitsErrMsg_DT"));//outof range error msg
		 * EP.VerifyRegExp(TabName+"AuxologicalHeightErrMsg_DT","\\d{1,3}");
		 * EP.VerifyRegExp(TabName+"AuxologicalWeightErrMsg_DT","\\d{1,3}");
		 * 
		 * //Incorrect format validation
		 * EP.Edit_SetValue(TabName+"AuxologicalHeight_EB", IncorrectFormat);
		 * EP.HideCursor(); EP.WaitForPageload();
		 * EP.Edit_SetValue(TabName+"AuxologicalWeight_EB", IncorrectFormat);
		 * EP.Btn_Click("#General_AddRecordSave_BN"); //click save button
		 * EP.CompareText("#General_VisitsErrMsg_DT",
		 * EP.LangData("VerifyIncorrectFormats"),
		 * EP.GetText("#General_VisitsErrMsg_DT"));//Incorrect format error msg
		 * EP.CompareText(TabName+"AuxologicalHeightErrMsg_DT",
		 * EP.LangData("IncorrectFormat"),
		 * EP.GetText(TabName+"AuxologicalHeightErrMsg_DT"));
		 * EP.CompareText(TabName+"AuxologicalWeightErrMsg_DT",
		 * EP.LangData("IncorrectFormat"),
		 * EP.GetText(TabName+"AuxologicalWeightErrMsg_DT"));
		 * 
		 * //Lesser than the Min Value - Value must be <=5-
		 * EP.Edit_SetValue(TabName+"AuxologicalHeight_EB", MinValue);
		 * EP.HideCursor(); EP.WaitForPageload();
		 * EP.Edit_SetValue(TabName+"AuxologicalWeight_EB", MinValue);
		 * EP.Btn_Click("#General_AddRecordSave_BN"); //click save button
		 * EP.CompareText("#General_VisitsErrMsg_DT",
		 * EP.LangData("OutOfRangeError"),
		 * EP.GetText("#General_VisitsErrMsg_DT"));//outof range error msg
		 * EP.VerifyRegExp(TabName+"AuxologicalHeightValueMustBeErrMsg_DT",
		 * "\\d{1,3}");
		 * EP.VerifyRegExp(TabName+"AuxologicalWeightValueMustBeErrMsg_DT",
		 * "\\d{1,3}");
		 * 
		 * // //Success Message validation //
		 * EP.Edit_ClearValue(TabName+"AuxologicalHeight_EB"); //
		 * EP.HideCursor(); // EP.WaitForPageload(); //
		 * EP.Btn_Click("#General_AddRecordSave_BN");//Click save button // //
		 * VerifyChapterStatus(ActionDone, TabIndex,
		 * EP.LangData("Partial"));//Verify chapter status //
		 * 
		 * EP.Edit_SetValue(TabName+"AuxologicalHeight_EB", "195");//196
		 * EP.HideCursor(); EP.WaitForPageload();
		 * EP.Edit_SetValue(TabName+"AuxologicalWeight_EB", "100");//101
		 * EP.Btn_Click("#General_AddRecordSave_BN"); //click save button
		 * VerifySuccessMessage(EP.LangData("AuxologicalDataVisits"));
		 * VerifyChapterStatus(ActionDone, TabIndex,
		 * EP.LangData("Complete"));//Verify chapter status
		 * 
		 * //Tooltip for Edit and Delete EP.VerifyToolTip("#General_Edit_IM");
		 * EP.VerifyToolTip("#General_Delete_IM");
		 * 
		 * EP.WbTbl_VerifyTableColumn(TabName+"Retrospective_TE", "Date", 1, 1,
		 * EP.MonthShortAll(), false);
		 * EP.WbTbl_VerifyTableColumn(TabName+"Retrospective_TE", "Height", 2,
		 * 1, EP.LangData("Centimetre"), false);
		 * EP.WbTbl_VerifyTableColumn(TabName+"Retrospective_TE", "Weight", 3,
		 * 1, EP.LangData("Kilogram"), false);
		 * EP.WbTbl_VerifyTableColumn(TabName+"Retrospective_TE",
		 * "GrowthVelocity", 4, 1, EP.LangData("GrowthVelocityUnit"), false);
		 * 
		 * //Delete msg validation (Should be done only for VisitsRecords) if
		 * (EP.CompareText(ActionDone,"VisitsRecord")){
		 * DeleteRecord("#General_Delete_IM", true);
		 * VerifyDeleteMessage(EP.LangData("AuxologicalDataVisits")); } break;
		 */

		case FRANCE:
			FranceECRF FranceDR = new FranceECRF(EP);
			FranceDR.AuxologicalDataVisitsDynamic(ActionDone, TabIndex,
					RecoveringURL);
			FranceDR = null;
			break;
		case UNITEDARABEMIRATES:
			UnitedArabEmiratesECRF UnitedArabEmiratesDR = new UnitedArabEmiratesECRF(
					EP);
			UnitedArabEmiratesDR.AuxologicalDataVisitsDynamic(ActionDone,
					TabIndex, RecoveringURL);
			UnitedArabEmiratesDR = null;
			break;

		default:

		}

	}

	public void SocioEconomicStaticVisits(String ActionDone, String TabIndex) {

		VerifyChapterName(ActionDone, TabIndex,
				EP.LangData("SocioEconomicInformation"));
		if (EP.CompareText(ActionDone, "AddVisit")) {
			VerifyChapterStatus(ActionDone, TabIndex, EP.LangData("NoData"));
		} else {
			VerifyChapterStatus(ActionDone, TabIndex,
					EP.ORData("#General_ChapterStatus_ST"));
		}
		switch (switchCountryName) {

		case AUSTRALIA:
			AustraliaECRF AustraliaDR = new AustraliaECRF(EP);
			AustraliaDR.SocioEconomicStaticVisits(ActionDone, TabIndex);
			AustraliaDR = null;
			break;

		case AUSTRIA:
			AustriaECRF AustriaDR = new AustriaECRF(EP);
			AustriaDR.SocioEconomicStaticVisits(ActionDone, TabIndex);
			AustriaDR = null;
			break;

		case CZECHREPUBLIC:
			CzechRepublicECRF CzechDR = new CzechRepublicECRF(EP);
			CzechDR.SocioEconomicStaticVisits(ActionDone, TabIndex);
			CzechDR = null;
			break;

		case TAIWAN:
			TaiwanECRF TaiwanDR = new TaiwanECRF(EP);
			TaiwanDR.SocioEconomicStaticVisits(ActionDone, TabIndex);
			TaiwanDR = null;
			break;

		case COLOMBIA:
			ColombiaECRF ColombiaDR = new ColombiaECRF(EP);
			ColombiaDR.SocioEconomicStaticVisits(ActionDone, TabIndex);
			ColombiaDR = null;
			break;

		case ARGENTINA:
			ArgentinaECRF ArgentinaDR = new ArgentinaECRF(EP);
			ArgentinaDR.SocioEconomicStaticVisits(ActionDone, TabIndex);
			ArgentinaDR = null;
			break;

		case INDONESIA:
			IndonesiaECRF IndonesiaDR = new IndonesiaECRF(EP);
			IndonesiaDR.SocioEconomicStaticVisits(ActionDone, TabIndex);
			IndonesiaDR = null;
			break;

		case FINLAND:
			FinlandECRF FinlandDR = new FinlandECRF(EP);
			FinlandDR.SocioEconomicStaticVisits(ActionDone, TabIndex);
			FinlandDR = null;
			break;

		case GREECE:
			GreeceECRF GreeceDR = new GreeceECRF(EP);
			GreeceDR.SocioEconomicStaticVisits(ActionDone, TabIndex);
			GreeceDR = null;
			break;

		case HUNGARY:
			HungaryECRF HungaryDR = new HungaryECRF(EP);
			HungaryDR.SocioEconomicStaticVisits(ActionDone, TabIndex);
			HungaryDR = null;
			break;

		case ICELAND:
			IcelandECRF IcelandDR = new IcelandECRF(EP);
			IcelandDR.SocioEconomicStaticVisits(ActionDone, TabIndex);
			IcelandDR = null;
			break;

		case ITALY:
			ItalyECRF ItalyDR = new ItalyECRF(EP);
			ItalyDR.SocioEconomicStaticVisits(ActionDone, TabIndex);
			ItalyDR = null;
			break;

		case KOREA:
			KoreaECRF KoreaDR = new KoreaECRF(EP);
			KoreaDR.SocioEconomicStaticVisits(ActionDone, TabIndex);
			KoreaDR = null;
			break;

		case NORWAY:
			NorwayECRF NorwayDR = new NorwayECRF(EP);
			NorwayDR.SocioEconomicStaticVisits(ActionDone, TabIndex);
			NorwayDR = null;
			break;

		case SLOVAKIA:
			SlovakiaECRF SlovakiaDR = new SlovakiaECRF(EP);
			SlovakiaDR.SocioEconomicStaticVisits(ActionDone, TabIndex);
			SlovakiaDR = null;
			break;

		case SPAIN:
			SpainECRF SpainDR = new SpainECRF(EP);
			SpainDR.SocioEconomicStaticVisits(ActionDone, TabIndex);
			SpainDR = null;
			break;

		case SINGAPORE:
			SingaporeECRF SingaporeDR = new SingaporeECRF(EP);
			SingaporeDR.SocioEconomicStaticVisits(ActionDone, TabIndex);
			SingaporeDR = null;
			break;

		case SAUDIARABIA:
			SaudiArabiaECRF SaudiArabiaDR = new SaudiArabiaECRF(EP);
			SaudiArabiaDR.SocioEconomicStaticVisits(ActionDone, TabIndex);
			SaudiArabiaDR = null;
			break;

		case SWEDEN:
			SwedenECRF SwedenDR = new SwedenECRF(EP);
			SwedenDR.SocioEconomicStaticVisits(ActionDone, TabIndex);
			SwedenDR = null;
			break;

		case UNITEDKINGDOM:
			UnitedKingdomECRF UnitedKingdomDR = new UnitedKingdomECRF(EP);
			UnitedKingdomDR.SocioEconomicStaticVisits(ActionDone, TabIndex);
			UnitedKingdomDR = null;
			break;

		case UNITEDARABEMIRATES:
			UnitedArabEmiratesECRF UnitedArabEmiratesDR = new UnitedArabEmiratesECRF(
					EP);
			UnitedArabEmiratesDR
					.SocioEconomicStaticVisits(ActionDone, TabIndex);
			UnitedArabEmiratesDR = null;
			break;

		case FRANCE:
			FranceECRF FranceDR = new FranceECRF(EP);
			FranceDR.SocioEconomicStaticVisits(ActionDone, TabIndex);
			FranceDR = null;
			break;

		case MEXICO:
			MexicoECRF MexicoDR = new MexicoECRF(EP);
			MexicoDR.SocioEconomicStaticVisits(ActionDone, TabIndex);
			MexicoDR = null;
			break;

		case CANADA:
			CanadaECRF CanadaDR = new CanadaECRF(EP);
			CanadaDR.SocioEconomicStaticVisits(ActionDone, TabIndex);
			CanadaDR = null;
			break;

		default:
		}
	}

	public void SocioEconomicDynamicVisits(String ActionDone, String TabIndex,
			String RecoveringURL) {
		EP.CompareText("#General_First_BN", EP.LangData("Add"),
				EP.GetText("#General_First_BN")); // Add Button

		EP.CompareText("#AddVisit_AssessmentNotDone_BN",
				EP.LangData("AssessmentNotDone"),
				EP.GetText("#AddVisit_AssessmentNotDone_BN")); // Assessment not
																// done Button

		if (EP.ObjectExists("#General_Delete_IM")) {
			DeleteRecord("#General_Delete_IM", false);
			EP.Btn_Click("#General_First_BN");
		} else {
			EP.Btn_Click("#General_First_BN");
			EP.WaitForPageload();

		}
		switch (switchCountryName) {

		case AUSTRALIA:
			AustraliaECRF AustraliaDR = new AustraliaECRF(EP);
			AustraliaDR.SocioEconomicDynamicVisits(ActionDone, TabIndex,
					RecoveringURL);
			AustraliaDR = null;
			break;

		case AUSTRIA:
			AustriaECRF AustriaDR = new AustriaECRF(EP);
			AustriaDR.SocioEconomicDynamicVisits(ActionDone, TabIndex,
					RecoveringURL);
			AustriaDR = null;
			break;

		case TAIWAN:
			TaiwanECRF TaiwanDR = new TaiwanECRF(EP);
			TaiwanDR.SocioEconomicDynamicVisits(ActionDone, TabIndex,
					RecoveringURL);
			TaiwanDR = null;
			break;

		case COLOMBIA:
			ColombiaECRF ColombiaDR = new ColombiaECRF(EP);
			ColombiaDR.SocioEconomicDynamicVisits(ActionDone, TabIndex,
					RecoveringURL);
			ColombiaDR = null;
			break;

		case ARGENTINA:
			ArgentinaECRF ArgentinaDR = new ArgentinaECRF(EP);
			ArgentinaDR.SocioEconomicDynamicVisits(ActionDone, TabIndex,
					RecoveringURL);
			ArgentinaDR = null;
			break;

		case INDONESIA:
			IndonesiaECRF IndonesiaDR = new IndonesiaECRF(EP);
			IndonesiaDR.SocioEconomicDynamicVisits(ActionDone, TabIndex,
					RecoveringURL);
			IndonesiaDR = null;
			break;

		case CZECHREPUBLIC:
			CzechRepublicECRF CzechDR = new CzechRepublicECRF(EP);
			CzechDR.SocioEconomicDynamicVisits(ActionDone, TabIndex,
					RecoveringURL);
			CzechDR = null;
			break;

		case FINLAND:
			FinlandECRF FinlandDR = new FinlandECRF(EP);
			FinlandDR.SocioEconomicDynamicVisits(ActionDone, TabIndex,
					RecoveringURL);
			FinlandDR = null;
			break;
		case GREECE:
			GreeceECRF GreeceDR = new GreeceECRF(EP);
			GreeceDR.SocioEconomicDynamicVisits(ActionDone, TabIndex,
					RecoveringURL);
			GreeceDR = null;
			break;

		case HUNGARY:
			HungaryECRF HungaryDR = new HungaryECRF(EP);
			HungaryDR.SocioEconomicDynamicVisits(ActionDone, TabIndex,
					RecoveringURL);
			HungaryDR = null;
			break;

		case ICELAND:
			IcelandECRF IcelandDR = new IcelandECRF(EP);
			IcelandDR.SocioEconomicDynamicVisits(ActionDone, TabIndex,
					RecoveringURL);
			IcelandDR = null;
			break;

		case ITALY:
			ItalyECRF ItalyDR = new ItalyECRF(EP);
			ItalyDR.SocioEconomicDynamicVisits(ActionDone, TabIndex,
					RecoveringURL);
			ItalyDR = null;
			break;

		case KOREA:
			KoreaECRF KoreaDR = new KoreaECRF(EP);
			KoreaDR.SocioEconomicDynamicVisits(ActionDone, TabIndex,
					RecoveringURL);
			KoreaDR = null;
			break;

		case NORWAY:
			NorwayECRF NorwayDR = new NorwayECRF(EP);
			NorwayDR.SocioEconomicDynamicVisits(ActionDone, TabIndex,
					RecoveringURL);
			NorwayDR = null;
			break;

		case SLOVAKIA:
			SlovakiaECRF SlovakiaDR = new SlovakiaECRF(EP);
			SlovakiaDR.SocioEconomicDynamicVisits(ActionDone, TabIndex,
					RecoveringURL);
			SlovakiaDR = null;
			break;

		case SPAIN:
			SpainECRF SpainDR = new SpainECRF(EP);
			SpainDR.SocioEconomicDynamicVisits(ActionDone, TabIndex,
					RecoveringURL);
			SpainDR = null;
			break;

		case SINGAPORE:
			SingaporeECRF SingaporeDR = new SingaporeECRF(EP);
			SingaporeDR.SocioEconomicDynamicVisits(ActionDone, TabIndex,
					RecoveringURL);
			SingaporeDR = null;
			break;

		case SAUDIARABIA:
			SaudiArabiaECRF SaudiArabiaDR = new SaudiArabiaECRF(EP);
			SaudiArabiaDR.SocioEconomicDynamicVisits(ActionDone, TabIndex,
					RecoveringURL);
			SaudiArabiaDR = null;
			break;

		case SWEDEN:
			SwedenECRF SwedenDR = new SwedenECRF(EP);
			SwedenDR.SocioEconomicDynamicVisits(ActionDone, TabIndex,
					RecoveringURL);
			SwedenDR = null;
			break;

		case UNITEDKINGDOM:
			UnitedKingdomECRF UnitedKingdomDR = new UnitedKingdomECRF(EP);
			UnitedKingdomDR.SocioEconomicDynamicVisits(ActionDone, TabIndex,
					RecoveringURL);
			UnitedKingdomDR = null;
			break;

		case UNITEDARABEMIRATES:
			UnitedArabEmiratesECRF UnitedArabEmiratesDR = new UnitedArabEmiratesECRF(
					EP);
			UnitedArabEmiratesDR.SocioEconomicDynamicVisits(ActionDone,
					TabIndex, RecoveringURL);
			UnitedArabEmiratesDR = null;
			break;

		case FRANCE:
			FranceECRF FranceDR = new FranceECRF(EP);
			FranceDR.SocioEconomicDynamicVisits(ActionDone, TabIndex,
					RecoveringURL);
			FranceDR = null;
			break;

		case CANADA:
			CanadaECRF CanadaDR = new CanadaECRF(EP);
			CanadaDR.SocioEconomicDynamicVisits(ActionDone, TabIndex,
					RecoveringURL);
			CanadaDR = null;
			break;
		case MEXICO:
			MexicoECRF MexicoDR = new MexicoECRF(EP);
			MexicoDR.SocioEconomicDynamicVisits(ActionDone, TabIndex,
					RecoveringURL);
			MexicoDR = null;
			break;
		default:

		}
	}

	public void CurrentGHStatic(String ActionDone, String TabIndex) {
		String TabName = AppendHash(ActionDone);
		VerifyChapterName(ActionDone, TabIndex,
				EP.LangData("CurrentGHTreatment"));
		if (EP.CompareText(ActionDone, "AddVisit")) {
			VerifyChapterStatus(ActionDone, TabIndex, EP.LangData("NoData"));
		} else {
			VerifyChapterStatus(ActionDone, TabIndex,
					EP.ORData("#General_ChapterStatus_ST"));
		}
		switch (switchCountryName) {

		case AUSTRALIA:
			AustraliaECRF AustraliaDR = new AustraliaECRF(EP);
			AustraliaDR.CurrentGHStatic(ActionDone, TabIndex);
			AustraliaDR = null;
			break;

		case AUSTRIA:
			AustriaECRF AustriaDR = new AustriaECRF(EP);
			AustriaDR.CurrentGHStatic(ActionDone, TabIndex);
			AustriaDR = null;
			break;

		case CZECHREPUBLIC:
			CzechRepublicECRF CzechDR = new CzechRepublicECRF(EP);
			CzechDR.CurrentGHStatic(ActionDone, TabIndex);
			CzechDR = null;
			break;

		case TAIWAN:
			TaiwanECRF TaiwanDR = new TaiwanECRF(EP);
			TaiwanDR.CurrentGHStatic(ActionDone, TabIndex);
			TaiwanDR = null;
			break;

		case COLOMBIA:
			ColombiaECRF ColombiaDR = new ColombiaECRF(EP);
			ColombiaDR.CurrentGHStatic(ActionDone, TabIndex);
			ColombiaDR = null;
			break;

		case INDONESIA:
			IndonesiaECRF IndonesiaDR = new IndonesiaECRF(EP);
			IndonesiaDR.CurrentGHStatic(ActionDone, TabIndex);
			IndonesiaDR = null;
			break;

		case ARGENTINA:
			ArgentinaECRF ArgentinaDR = new ArgentinaECRF(EP);
			ArgentinaDR.CurrentGHStatic(ActionDone, TabIndex);
			ArgentinaDR = null;
			break;

		case FINLAND:
			FinlandECRF FinlandDR = new FinlandECRF(EP);
			FinlandDR.CurrentGHStatic(ActionDone, TabIndex);
			FinlandDR = null;
			break;

		case GREECE:
			GreeceECRF GreeceDR = new GreeceECRF(EP);
			GreeceDR.CurrentGHStatic(ActionDone, TabIndex);
			GreeceDR = null;
			break;

		case HUNGARY:
			HungaryECRF HungaryDR = new HungaryECRF(EP);
			HungaryDR.CurrentGHStatic(ActionDone, TabIndex);
			HungaryDR = null;
			break;

		case ICELAND:
			IcelandECRF IcelandDR = new IcelandECRF(EP);
			IcelandDR.CurrentGHStatic(ActionDone, TabIndex);
			IcelandDR = null;
			break;

		case ITALY:
			ItalyECRF ItalyDR = new ItalyECRF(EP);
			ItalyDR.CurrentGHStatic(ActionDone, TabIndex);
			ItalyDR = null;
			break;

		case KOREA:
			KoreaECRF KoreaDR = new KoreaECRF(EP);
			KoreaDR.CurrentGHStatic(ActionDone, TabIndex);
			KoreaDR = null;
			break;

		case NORWAY:
			NorwayECRF NorwayDR = new NorwayECRF(EP);
			NorwayDR.CurrentGHStatic(ActionDone, TabIndex);
			NorwayDR = null;
			break;

		case SLOVAKIA:
			SlovakiaECRF SlovakiaDR = new SlovakiaECRF(EP);
			SlovakiaDR.CurrentGHStatic(ActionDone, TabIndex);
			SlovakiaDR = null;
			break;

		case SPAIN:
			SpainECRF SpainDR = new SpainECRF(EP);
			SpainDR.CurrentGHStatic(ActionDone, TabIndex);
			SpainDR = null;
			break;

		case SINGAPORE:
			SingaporeECRF SingaporeDR = new SingaporeECRF(EP);
			SingaporeDR.CurrentGHStatic(ActionDone, TabIndex);
			SingaporeDR = null;
			break;

		case SAUDIARABIA:
			SaudiArabiaECRF SaudiArabiaDR = new SaudiArabiaECRF(EP);
			SaudiArabiaDR.CurrentGHStatic(ActionDone, TabIndex);
			SaudiArabiaDR = null;
			break;

		case SWEDEN:
			SwedenECRF SwedenDR = new SwedenECRF(EP);
			SwedenDR.CurrentGHStatic(ActionDone, TabIndex);
			SwedenDR = null;
			break;

		case UNITEDARABEMIRATES:
			UnitedArabEmiratesECRF UnitedArabEmiratesDR = new UnitedArabEmiratesECRF(
					EP);
			UnitedArabEmiratesDR.CurrentGHStatic(ActionDone, TabIndex);
			UnitedArabEmiratesDR = null;
			break;

		case UNITEDKINGDOM:
			UnitedKingdomECRF UnitedKingdomDR = new UnitedKingdomECRF(EP);
			UnitedKingdomDR.CurrentGHStatic(ActionDone, TabIndex);
			UnitedKingdomDR = null;
			break;

		case FRANCE:
			FranceECRF FranceDR = new FranceECRF(EP);
			FranceDR.CurrentGHStatic(ActionDone, TabIndex);
			FranceDR = null;
			break;

		case CANADA:// Canada same as sweden but 1st chapter heading differed
			CanadaECRF CanadaDR = new CanadaECRF(EP);
			CanadaDR.CurrentGHStatic(ActionDone, TabIndex);
			CanadaDR = null;
			break;

		case MEXICO:
			MexicoECRF MexicoDR = new MexicoECRF(EP);
			MexicoDR.CurrentGHStatic(ActionDone, TabIndex);
			MexicoDR = null;
			break;

		/*
		 * case ARGENTINA:
		 * 
		 * VerifyChapterHeading(ActionDone,TabIndex,
		 * 1,EP.LangData("CurrentGHTreatmenteasypod"));
		 * VerifyChapterHeading(ActionDone,TabIndex,
		 * 2,EP.LangData("GHTreatmentWithEasypod"));
		 * EP.VerifyTextContained(TabName+"SaizenFreezeDriedFR_ST");
		 * 
		 * if(!EP.CompareText(ActionDone, "RecordOverview")) {
		 * 
		 * EP.VerifyTextContained(TabName+"CurrentGHTreatmentDailyDoseUnit_ST");
		 * EP.VerifyText(TabName+"CurrentGHTreatmentStartDate_ST");
		 * EP.VerifyText(TabName+"CurrentGHTreatmentDailyDose_ST");
		 * EP.VerifyText(TabName+"CurrentGHTreatmentFormulation_ST");
		 * EP.VerifyText(TabName+"CurrentGHTreatmentStopDate_ST");
		 * //EP.VerifyTableHeader(TabName+"GHtreatmentWithEasypod_TE"); Removed
		 * frm for france, check for austria
		 * EP.VerifyTableHeader(TabName+"GHtreatmentWithEasypodGR_TE"); } else {
		 * EP
		 * .VerifyTextContained(TabName+"CurrentGHTreatmentDailyDoseUnitDE_ST");
		 * EP.VerifyText(TabName+"CurrentGHTreatmentStartDateAT_ST");
		 * EP.VerifyText(TabName+"CurrentGHTreatmentDailyDoseAT_ST");
		 * EP.VerifyText(TabName+"CurrentGHTreatmentFormulationAT_ST");
		 * EP.VerifyText(TabName+"CurrentGHTreatmentStopDateAT_ST");
		 * EP.VerifyTableHeader(TabName+"GHtreatmentWithEasypodDE_TE");
		 * 
		 * 
		 * //Column verification for Current GH Treatment for record overview
		 * EP.WbTbl_VerifyTableColumn(TabName+"GHtreatmentWithEasypodDE_TE",
		 * "InjectionsMadeBy", 5, 1,
		 * EP.ORData("#RecordOverview_InjectionsMadeByColumnAT_ST"), true);
		 * EP.WbTbl_VerifyTableColumn(TabName+"GHtreatmentWithEasypodDE_TE",
		 * "Dosechanged", 6, 1,
		 * EP.ORData("#RecordOverview_CurrentTreatmentTableColumn_ST"), true);
		 * EP.VerifyTextContained(TabName+"GHtreatmentWithEasypodDE_TE",
		 * EP.LangData("UnitsMgPerKgBracketsPerDay"),
		 * EP.GetText("#RecordOverview_FormulationUnitTableColumnDE_ST"));
		 * EP.WbTbl_VerifyTableColumn(TabName+"GHtreatmentWithEasypodDE_TE",
		 * "MissedInjections", 7, 1,
		 * EP.ORData("#RecordOverview_CurrentTreatmentTableColumn_ST"), true);
		 * EP.WbTbl_VerifyTableColumn(TabName+"GHtreatmentWithEasypodDE_TE",
		 * "MissedInjectionsReason", 7, 1,
		 * EP.ORData("#RecordOverview_MissedInjectionReasonTableColumn_ST"),
		 * true);
		 * 
		 * } break;
		 */
		/*
		 * case CHINA: VerifyChapterHeading(ActionDone,TabIndex,
		 * 1,EP.LangData("CurrentGHTreatmenteasypod"));
		 * VerifyChapterHeading(ActionDone,TabIndex,
		 * 2,EP.LangData("GHTreatmentWithEasypod"));
		 * EP.VerifyTextContained(TabName+"SaizenFreezeDriedFR_ST");
		 * 
		 * if(!EP.CompareText(ActionDone, "RecordOverview")) {
		 * EP.VerifyTextContained(TabName+"CurrentGHTreatmentDailyDoseUnit_ST");
		 * EP.VerifyText(TabName+"CurrentGHTreatmentStartDate_ST");
		 * EP.VerifyText(TabName+"CurrentGHTreatmentDailyDose_ST");
		 * EP.VerifyText(TabName+"CurrentGHTreatmentFormulation_ST");
		 * EP.VerifyText(TabName+"CurrentGHTreatmentStopDate_ST");
		 * //EP.VerifyTableHeader(TabName+"GHtreatmentWithEasypod_TE"); Removed
		 * frm for france, check for austria
		 * EP.VerifyTableHeader(TabName+"GHtreatmentWithEasypodGR_TE"); } else {
		 * EP
		 * .VerifyTextContained(TabName+"CurrentGHTreatmentDailyDoseUnitCN_ST");
		 * EP.VerifyText(TabName+"CurrentGHTreatmentStartDateAT_ST");
		 * EP.VerifyText(TabName+"CurrentGHTreatmentDailyDoseAT_ST");
		 * EP.VerifyText(TabName+"CurrentGHTreatmentFormulationAT_ST");
		 * EP.VerifyText(TabName+"CurrentGHTreatmentStopDateAT_ST");
		 * EP.VerifyTableHeader(TabName+"GHtreatmentWithEasypodDE_TE");
		 * 
		 * 
		 * //Column verification for Current GH Treatment for record overview
		 * EP.WbTbl_VerifyTableColumn(TabName+"GHtreatmentWithEasypodDE_TE",
		 * "InjectionsMadeBy", 5, 1,
		 * EP.ORData("#RecordOverview_InjectionsMadeByColumnAT_ST"), true);
		 * EP.WbTbl_VerifyTableColumn(TabName+"GHtreatmentWithEasypodDE_TE",
		 * "Dosechanged", 6, 1,
		 * EP.ORData("#RecordOverview_CurrentTreatmentTableColumn_ST"), true);
		 * EP.VerifyTextContained(TabName+"GHtreatmentWithEasypodDE_TE",
		 * EP.LangData("UnitsMgPerKgBracketsPerDay"),
		 * EP.GetText("#RecordOverview_FormulationUnitTableColumnDE_ST"));
		 * EP.WbTbl_VerifyTableColumn(TabName+"GHtreatmentWithEasypodDE_TE",
		 * "MissedInjections", 7, 1,
		 * EP.ORData("#RecordOverview_CurrentTreatmentTableColumn_ST"), true);
		 * EP.WbTbl_VerifyTableColumn(TabName+"GHtreatmentWithEasypodDE_TE",
		 * "MissedInjectionsReason", 7, 1,
		 * EP.ORData("#RecordOverview_MissedInjectionReasonTableColumn_ST"),
		 * true);
		 * 
		 * } break;
		 */
		case GERMANY:
			VerifyChapterHeading(ActionDone, TabIndex, 1,
					EP.LangData("CurrentGHTreatmenteasypod"));
			VerifyChapterHeading(ActionDone, TabIndex, 2,
					EP.LangData("GHTreatmentWithEasypod"));

			EP.VerifyTextContained(TabName + "SaizenFreezeDriedFR_ST");
			EP.VerifyTextContained(TabName
					+ "CurrentGHTreatmentDailyDoseUnitDE_ST");
			// EP.VerifyTextContained(TabName+"SaizenSolutionForInjectionAT_ST");
			// Removed frm for france, check for austria

			if (!EP.CompareText(ActionDone, "RecordOverview")) {
				EP.VerifyText(TabName + "CurrentGHTreatmentStartDate_ST");
				EP.VerifyText(TabName + "CurrentGHTreatmentDailyDose_ST");
				EP.VerifyTextContained(TabName
						+ "CurrentGHTreatmentDailyDoseUnit_ST");
				EP.VerifyText(TabName + "CurrentGHTreatmentFormulation_ST");
				EP.VerifyText(TabName + "CurrentGHTreatmentStopDate_ST");
				EP.VerifyTableHeader(TabName + "GHtreatmentWithEasypodDE_TE");
			} else {
				EP.VerifyText(TabName + "CurrentGHTreatmentStartDateAT_ST");
				EP.VerifyText(TabName + "CurrentGHTreatmentDailyDoseAT_ST");
				EP.VerifyText(TabName + "CurrentGHTreatmentFormulationAT_ST");
				EP.VerifyText(TabName + "CurrentGHTreatmentStopDateAT_ST");
				EP.VerifyTableHeader(TabName + "GHtreatmentWithEasypodDE_TE");

				// Verify tooltip and values in the table
				// Fifth Column
				CheckTableText(
						TabName + "GHtreatmentWithEasypod_TE",
						1,
						5,
						1,
						EP.LangData("WhoPerformsMajorityInjections"),
						EP.ORData("#RecordOverview_InjectionsMadeByColumnAT_ST"));

				// Sixth Column
				CheckTableText(TabName + "GHtreatmentWithEasypod_TE", 1, 6, 1,
						EP.LangData("DoseChangedSincePreviousVisit"),
						EP.ORData("#AddVisit_DoseChangedColValue1_DT"));
				CheckTableText(TabName + "GHtreatmentWithEasypod_TE", 1, 6, 2,
						EP.LangData("DoseChangeReason"),
						EP.ORData("#AddVisit_DoseChangedColValue2_DT"));
				CheckTableText(TabName + "GHtreatmentWithEasypod_TE", 1, 6, 3,
						EP.LangData("NewDailyDose"),
						EP.LangData("UnitsMgPerKgBracketsPerDay"));
				CheckTableText(TabName + "GHtreatmentWithEasypod_TE", 1, 6, 4,
						EP.LangData("PreviousDailyDoseStopDate"),
						EP.MonthShortAll());
				CheckTableText(TabName + "GHtreatmentWithEasypod_TE", 1, 6, 5,
						EP.LangData("NewDailyDoseAtStartDate"),
						EP.MonthShortAll());

				// Seventh Column
				CheckTableText(TabName + "GHtreatmentWithEasypod_TE", 1, 7, 1,
						EP.LangData("MissedInjectionsSincePreviousVisit"),
						EP.ORData("#AddVisit_DoseChangedColValue1_DT"));
				CheckTableText(TabName + "GHtreatmentWithEasypod_TE", 1, 7, 2,
						EP.LangData("MissedInjectionsReason"),
						EP.ORData("#AddVisit_CurrentGHDoseChangedCol_DT"));
				CheckTableText(TabName + "GHtreatmentWithEasypod_TE", 1, 7, 3,
						EP.LangData("TemporaryCessationGreater"),
						EP.ORData("#AddVisit_DoseChangedColValue1_DT"));
				CheckTableText(TabName + "GHtreatmentWithEasypod_TE", 1, 7, 4,
						EP.LangData("StopFrom"), EP.MonthShortAll());
				CheckTableText(TabName + "GHtreatmentWithEasypod_TE", 1, 7, 5,
						EP.LangData("StopTo"), EP.MonthShortAll());
				CheckTableText(TabName + "GHtreatmentWithEasypod_TE", 1, 7, 6,
						EP.LangData("TreamentStopAccordancePhysician"),
						EP.ORData("#AddVisit_MissedInjectionColValue6_DT"));
				CheckTableText(TabName + "GHtreatmentWithEasypod_TE", 1, 7, 7,
						EP.LangData("TemporaryTreatmentStop"),
						EP.ORData("#AddVisit_MissedInjectionColValue6_DT"));
				CheckTableText(TabName + "GHtreatmentWithEasypod_TE", 1, 7, 8,
						EP.LangData("TemporaryCessationReason"),
						EP.ORData("#AddVisit_MissedInjectionColValue8_DT"));

			}
			break;

		default:

		}
	}

	public void CurrentGHDynamic(String ActionDone, String TabIndex,
			String RecoveringURL) {
		String ActionPerformed;
		String TabName = AppendHash(ActionDone);
		String Date = "";

		EP.TakeStaticScreenshot("CurrentGHStatic");
		EP.CompareText("#General_First_BN", EP.LangData("Add"),
				EP.GetText("#General_First_BN")); // Add Button
		EP.ClearStaticScreenshot();
		if (EP.ObjectExists("#General_Delete_IM")) {
			DeleteRecord("#General_Delete_IM", false);
			EP.Btn_Click("#General_First_BN");
		} else {
			EP.Btn_Click("#General_First_BN"); // Click Add button
			EP.WaitForPageload();
		}
		switch (switchCountryName) {

		case AUSTRALIA:
			AustraliaECRF AustraliaDR = new AustraliaECRF(EP);
			AustraliaDR.CurrentGHDynamic(ActionDone, TabIndex, RecoveringURL);
			AustraliaDR = null;
			break;

		case AUSTRIA:
			AustriaECRF AustriaDR = new AustriaECRF(EP);
			AustriaDR.CurrentGHDynamic(ActionDone, TabIndex, RecoveringURL);
			AustriaDR = null;
			break;

		case TAIWAN:
			TaiwanECRF TaiwanDR = new TaiwanECRF(EP);
			TaiwanDR.CurrentGHDynamic(ActionDone, TabIndex, RecoveringURL);
			TaiwanDR = null;
			break;

		case COLOMBIA:
			ColombiaECRF ColombiaDR = new ColombiaECRF(EP);
			ColombiaDR.CurrentGHDynamic(ActionDone, TabIndex, RecoveringURL);
			ColombiaDR = null;
			break;

		case INDONESIA:
			IndonesiaECRF IndonesiaDR = new IndonesiaECRF(EP);
			IndonesiaDR.CurrentGHDynamic(ActionDone, TabIndex, RecoveringURL);
			IndonesiaDR = null;
			break;

		case ARGENTINA:
			ArgentinaECRF ArgentinaDR = new ArgentinaECRF(EP);
			ArgentinaDR.CurrentGHDynamic(ActionDone, TabIndex, RecoveringURL);
			ArgentinaDR = null;
			break;

		case CZECHREPUBLIC:
			CzechRepublicECRF CzechDR = new CzechRepublicECRF(EP);
			CzechDR.CurrentGHDynamic(ActionDone, TabIndex, RecoveringURL);
			CzechDR = null;
			break;

		case FINLAND:
			FinlandECRF FinlandDR = new FinlandECRF(EP);
			FinlandDR.CurrentGHDynamic(ActionDone, TabIndex, RecoveringURL);
			FinlandDR = null;
			break;

		case GREECE:
			GreeceECRF GreeceDR = new GreeceECRF(EP);
			GreeceDR.CurrentGHDynamic(ActionDone, TabIndex, RecoveringURL);
			GreeceDR = null;
			break;

		case HUNGARY:
			HungaryECRF HungaryDR = new HungaryECRF(EP);
			HungaryDR.CurrentGHDynamic(ActionDone, TabIndex, RecoveringURL);
			HungaryDR = null;
			break;

		case ICELAND:
			IcelandECRF IcelandDR = new IcelandECRF(EP);
			IcelandDR.CurrentGHDynamic(ActionDone, TabIndex, RecoveringURL);
			IcelandDR = null;
			break;

		case ITALY:
			ItalyECRF ItalyDR = new ItalyECRF(EP);
			ItalyDR.CurrentGHDynamic(ActionDone, TabIndex, RecoveringURL);
			ItalyDR = null;
			break;

		case KOREA:
			KoreaECRF KoreaDR = new KoreaECRF(EP);
			KoreaDR.CurrentGHDynamic(ActionDone, TabIndex, RecoveringURL);
			KoreaDR = null;
			break;

		case NORWAY:
			NorwayECRF NorwayDR = new NorwayECRF(EP);
			NorwayDR.CurrentGHDynamic(ActionDone, TabIndex, RecoveringURL);
			NorwayDR = null;
			break;

		case SLOVAKIA:
			SlovakiaECRF SlovakiaDR = new SlovakiaECRF(EP);
			SlovakiaDR.CurrentGHDynamic(ActionDone, TabIndex, RecoveringURL);
			SlovakiaDR = null;
			break;

		case SPAIN:
			SpainECRF SpainDR = new SpainECRF(EP);
			SpainDR.CurrentGHDynamic(ActionDone, TabIndex, RecoveringURL);
			SpainDR = null;
			break;

		case SINGAPORE:
			SingaporeECRF SingaporeDR = new SingaporeECRF(EP);
			SingaporeDR.CurrentGHDynamic(ActionDone, TabIndex, RecoveringURL);
			SingaporeDR = null;
			break;

		case SAUDIARABIA:
			SaudiArabiaECRF SaudiArabiaDR = new SaudiArabiaECRF(EP);
			SaudiArabiaDR.CurrentGHDynamic(ActionDone, TabIndex, RecoveringURL);
			SaudiArabiaDR = null;
			break;

		case SWEDEN:
			SwedenECRF SwedenDR = new SwedenECRF(EP);
			SwedenDR.CurrentGHDynamic(ActionDone, TabIndex, RecoveringURL);
			SwedenDR = null;
			break;

		case UNITEDKINGDOM:
			UnitedKingdomECRF UnitedKingdomDR = new UnitedKingdomECRF(EP);
			UnitedKingdomDR.CurrentGHDynamic(ActionDone, TabIndex,
					RecoveringURL);
			UnitedKingdomDR = null;
			break;

		case UNITEDARABEMIRATES:
			UnitedArabEmiratesECRF UnitedArabEmiratesDR = new UnitedArabEmiratesECRF(
					EP);
			UnitedArabEmiratesDR.CurrentGHDynamic(ActionDone, TabIndex,
					RecoveringURL);
			UnitedArabEmiratesDR = null;
			break;

		case FRANCE:
			FranceECRF FranceDR = new FranceECRF(EP);
			FranceDR.CurrentGHDynamic(ActionDone, TabIndex, RecoveringURL);
			FranceDR = null;
			break;

		case CANADA:
			CanadaECRF CanadaDR = new CanadaECRF(EP);
			CanadaDR.CurrentGHDynamic(ActionDone, TabIndex, RecoveringURL);
			CanadaDR = null;
			break;
		case MEXICO:
			MexicoECRF MexicoDR = new MexicoECRF(EP);
			MexicoDR.CurrentGHDynamic(ActionDone, TabIndex, RecoveringURL);
			MexicoDR = null;
			break;

		// case CHINA:
		// //case ARGENTINA:
		// case MEXICO:
		//
		//
		// EP.VerifyToolTip(TabName+"CurrentGHTreatmentStartDate_IM");
		// EP.VerifyToolTip(TabName+"CurrentGHTreatmentStopDate_IM");
		//
		// //Incorrect Format
		// EP.Edit_SetValue(TabName+"CurrentGHTreatmentDailyDose_EB",
		// IncorrectFormat);
		// EP.Btn_Click("#General_Second_BN");
		// EP.CompareText("#General_VisitsErrMsg_DT",
		// EP.LangData("VerifyIncorrectFormats"),
		// EP.GetText("#General_VisitsErrMsg_DT"));
		// EP.CompareText(TabName+"CurrentGHTreatmentDailyDoseErrMsg_DT",
		// EP.LangData("IncorrectFormat"),
		// EP.GetText(TabName+"CurrentGHTreatmentDailyDoseErrMsg_DT"));
		//
		// //Lesser Than min value
		// EP.Edit_SetValue(TabName+"CurrentGHTreatmentDailyDose_EB", MinValue);
		// EP.Btn_Click("#General_Second_BN");
		// EP.CompareText("#General_VisitsErrMsg_DT",
		// EP.LangData("OutOfRangeError"),
		// EP.GetText("#General_VisitsErrMsg_DT"));
		// EP.VerifyRegExp(TabName+"CurrentGHTreatmentDailyDoseErrMsg_DT");
		//
		// //Save again to proceed with the add page
		// EP.Edit_SetValue(TabName+"CurrentGHTreatmentDailyDose_EB", "0.05");
		// EP.Btn_Click("#General_Second_BN");
		//
		// //Navigate to Add Page
		// if (!EP.ObjectExists("#General_Edit_IM")){ //If the Record is present
		// then Edit it or else create a new record
		// ActionPerformed = "#General_Fourth_BN";
		// EP.Btn_Click(ActionPerformed); //Click Add button
		// EP.WaitForPageload();
		// EP.CompareText("#General_FormHeader_ST",
		// EP.LangData("Add"),EP.GetText("#General_FormHeader_ST")); //Form
		// Header
		// }else{
		// ActionPerformed = "#General_Edit_IM";
		// EP.Btn_Click(ActionPerformed); //Click Edit button
		// EP.WaitForPageload();
		// EP.CompareText("#General_FormHeader_ST",EP.LangData("Edit"),
		// EP.GetText("#General_FormHeader_ST")); //Form Header
		// }
		//
		// //Make all the fields visible before taking the screenshot
		// EP.Rbtn_SetOption(TabName+"CurrentGHDoseChangedYes_RB", "ON");
		// //EP.Rbtn_SetOption(TabName+"CurrentGHFormulationChangeYes_RB",
		// "ON");
		// EP.Rbtn_SetOption(TabName+"CurrentGHMissedInjectionYes_RB", "ON");
		// EP.Rbtn_SetOption(TabName+"CurrentGHTemporaryCessationYes_RB", "ON");
		// EP.ChkBx_SetOption(TabName+"CurrentGHAdverseEvent_CB", "ON");
		// EP.ChkBx_SetOption(TabName+"CurrentGHOther_CB", "ON");
		// EP.ChkBx_SetOption(TabName+"CurrentGHOthers_CB", "ON");
		// EP.ChkBx_SetOption(TabName+"CurrentGHReasonsOthersPlsSpecify_CB",
		// "ON");
		//
		// EP.TakeStaticScreenshot("CurrentGHAddEdit");
		// EP.VerifyText("#NavigationTab_CurrentGHTreatment_LK");//Navigation
		// Tab
		//
		// //Who performs the majority of injections?
		// EP.VerifyText(TabName+"CurrentGHWhoPerforms_ST");
		// EP.VerifyText(TabName+"CurrentGHParents_ST");
		// EP.VerifyText(TabName+"CurrentGHUnderParents_ST");
		// EP.VerifyText(TabName+"CurrentGHPatientSelfInjectionAT_ST");
		//
		// //Dose changed since previous visit?
		// EP.VerifyText(TabName+"CurrentGHDoesChanged_ST");
		// EP.VerifyText(TabName+"CurrentGHDoesChangedYes_ST");
		// EP.VerifyText(TabName+"CurrentGHDoesChangedNo_ST");
		// EP.VerifyText(TabName+"CurrentGHReasonForDose_ST");
		// EP.VerifyText(TabName+"CurrentGHDoseAdjustment_ST");
		// EP.VerifyText(TabName+"CurrentGHPuberty_ST");
		// EP.VerifyText(TabName+"CurrentGHEfficacy_ST");
		// EP.VerifyText(TabName+"CurrentGHAdverseEvent_ST");
		// EP.VerifyText(TabName+"CurrentGHAdverseEventPlsSpecify_ST");
		// EP.VerifyText(TabName+"CurrentGHOther_ST");
		// EP.VerifyText(TabName+"CurrentGHOthersPlsSpecify_ST");
		// EP.VerifyText(TabName+"CurrentGHNewDailyDose_ST");
		// EP.VerifyTextContained(TabName+"CurrentGHNewDailyDoseScale_ST");
		// EP.VerifyText(TabName+"CurrentGHOthersStopDate_ST");
		// EP.VerifyToolTip(TabName+"CurrentGHOthersStopDate_IM");
		// EP.VerifyText(TabName+"CurrentGHOthersStartDate_ST");
		// EP.VerifyToolTip(TabName+"CurrentGHOthersStartDate_IM");
		//
		// //Missed injections since previous visit?
		// EP.VerifyText(TabName+"CurrentGHMissedInjectionFR_ST");
		// EP.VerifyText(TabName+"CurrentGHMissedInjectionYesFR_ST");
		// EP.VerifyText(TabName+"CurrentGHMissedInjectionNoFR_ST");
		// EP.VerifyText(TabName+"CurrentGHReasonForFR_ST");
		// EP.VerifyText(TabName+"CurrentGHFogotInjection_ST");
		// EP.VerifyText(TabName+"CurrentGHHolidaysLong_ST");
		// EP.VerifyText(TabName+"CurrentGHMedicalReason_ST");
		// EP.VerifyText(TabName+"CurrentGHTiredOfInjection_ST");
		// EP.VerifyText(TabName+"CurrentGHTechnicalProblems_ST");
		// EP.VerifyText(TabName+"CurrentGHForgotDrub_ST");
		// EP.VerifyText(TabName+"CurrentGHRanOut_ST");
		// EP.VerifyText(TabName+"CurrentGHOthers_ST");
		//
		// //Temporary cessation of treatment Week
		// EP.VerifyText(TabName+"CurrentGHTemporaryCessationFR_ST");
		// EP.VerifyText(TabName+"CurrentGHTemporaryCessationYes_ST");
		// EP.VerifyText(TabName+"CurrentGHTemporaryCessationNo_ST");
		// EP.VerifyText(TabName+"CurrentGHTemporaryCessationStopFromFR_ST");
		// EP.VerifyToolTip(TabName+"CurrentGHTemporaryCessationStopFrom_IM");
		// EP.VerifyText(TabName+"CurrentGHTemporaryCessationStopToFR_ST");
		// EP.VerifyToolTip(TabName+"CurrentGHTemporaryCessationStopTo_IM");
		//
		// //Treatment stop in accordance with physician?
		// EP.VerifyText(TabName+"CurrentGHTreatmentStopFR_ST");
		// EP.VerifyText(TabName+"CurrentGHTreatmentStopYes_ST");
		// EP.VerifyText(TabName+"CurrentGHTreatmentStopNo_ST");
		//
		// //Was the dose adjusted following this temporary treatment stop?
		// EP.VerifyText(TabName+"CurrentGHWasTheDoseFR_ST");
		// EP.VerifyText(TabName+"CurrentGHWasTheDoseYesFR_ST");
		// EP.VerifyText(TabName+"CurrentGHWasTheDoseNoFR_ST");
		//
		//
		// //Reasons for temporary cessation of treatment
		// EP.VerifyText(TabName+"CurrentGHReasonsForFR_ST");
		// EP.VerifyText(TabName+"CurrentGHReasonsForHolidays_ST");
		// EP.VerifyText(TabName+"CurrentGHReasonsForMedicalReason_ST");
		// EP.VerifyText(TabName+"CurrentGHReasonsOthersPlsSpecify_ST");
		//
		// //Verify save and cancel buttons on the Current GhTreatment
		// EP.CompareText("Save_BN", EP.LangData("Save"),
		// EP.GetText("#General_AddRecordSave_BN"));
		// EP.CompareText("Cancel_BN", EP.LangData("Cancel"),
		// EP.GetText("#General_AddRecordCancel_BN"));
		//
		//
		// EP.VerifyPageLayout();
		// if (Env.GetBrowserUsed().startsWith("SA") ||
		// Env.GetBrowserUsed().startsWith("OP")){
		// EP.NavigateTo(RecoveringURL);
		// EP.WaitForPageload();
		// EP.ShowTab(TabIndex);
		// EP.Btn_Click(ActionPerformed);
		// }
		//
		// //-*-*-*-*-**-*-*-*-*-*-*-*-*-*-*-*-*-*DYNAMIC Text in Current GH
		// add*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
		//
		// //Mandatory field error
		// //After pagelayout, all the fields will go back to last saved state.
		// So select the radio buttons again
		// EP.Rbtn_SetOption(TabName+"CurrentGHDoseChangedYes_RB", "ON");
		// //EP.Rbtn_SetOption(TabName+"CurrentGHFormulationChangeYes_RB",
		// "ON");
		// EP.Rbtn_SetOption(TabName+"CurrentGHMissedInjectionYes_RB", "ON");
		// EP.Rbtn_SetOption(TabName+"CurrentGHTemporaryCessationYes_RB", "ON");
		//
		// //Remove all the checkbox
		// EP.ChkBx_SetOptionForAll("//div[@class='RecordOverview']//input[@class='checkbox']",
		// "OFF");
		// //Select the Required Checkboxes
		// EP.ChkBx_SetOption(TabName+"CurrentGHAdverseEvent_CB", "ON");
		// EP.ChkBx_SetOption(TabName+"CurrentGHOther_CB", "ON");
		// EP.ChkBx_SetOption(TabName+"CurrentGHOthers_CB", "ON");
		// EP.ChkBx_SetOption(TabName+"CurrentGHReasonsOthersPlsSpecify_CB",
		// "ON");
		// //Clear all the Editbox
		// EP.Edit_ClearValueForAll("//div[@class='RecordOverview']//input[@type='text' and not(@readonly)]");
		// EP.Btn_Click("#General_AddRecordSave_BN");//Click save button
		// EP.CompareText("#General_VisitsErrMsg_DT",
		// EP.LangData("OutOfRangeError"),
		// EP.GetText("#General_VisitsErrMsg_DT"));
		//
		// EP.CompareText(TabName+"CurrentGHDoseChangedAdverseSpecify_DT",
		// EP.LangData("FieldIsMandatory"),
		// EP.GetText(TabName+"CurrentGHDoseChangeAdverseSpecify_DT"));
		// EP.CompareText(TabName+"CurrentGHDoseChangedAdverseOtherSpecify_DT",
		// EP.LangData("FieldIsMandatory"),
		// EP.GetText(TabName+"CurrentGHDoseChangeAdverseOtherSpecify_DT"));
		// EP.CompareText(TabName+"CurrentGHDoseChangedNewDailyDose_DT",
		// EP.LangData("FieldIsMandatory"),
		// EP.GetText(TabName+"CurrentGHDoseChangeNewDailyDose_DT"));
		//
		// //Second Set of fields
		// //No validation in second set
		// //Third Set of fields
		// EP.CompareText(TabName+"CurrentGHMissedInjectionSinceOther_DT",
		// EP.LangData("FieldIsMandatory"),
		// EP.GetText(TabName+"CurrentGHMissedInjectionSinceOtherFR_DT"));
		// EP.CompareText(TabName+"CurrentGHMissedInjectionSinceOtherPlsSpecify_DT",
		// EP.LangData("FieldIsMandatory"),
		// EP.GetText(TabName+"CurrentGHMissedInjectionSinceOtherPlsSpecifyFR_DT"));
		//
		// //Remove other fields
		// EP.ChkBx_SetOption(TabName+"CurrentGHAdverseEvent_CB", "OFF");
		// EP.ChkBx_SetOption(TabName+"CurrentGHOther_CB", "OFF");
		//
		// //Greater Than the Max Value
		// //- Shows incorrect format
		//
		// //Incorrect Format
		// EP.Edit_SetValue(TabName+"CurrentGHNewDailyDose_EB",
		// IncorrectFormat);
		// EP.Btn_Click("#General_AddRecordSave_BN");
		// EP.CompareText("#General_VisitsErrMsg_DT",
		// EP.LangData("VerifyIncorrectFormats"),
		// EP.GetText("#General_VisitsErrMsg_DT"));
		// EP.CompareText(TabName+"CurrentGHNewDailyDoseFormat_DT",
		// EP.LangData("IncorrectFormat"),
		// EP.GetText(TabName+"CurrentGHNewDailyDoseFormat_DT"));
		//
		// //Lesser Than the min Value
		// EP.Edit_SetValue(TabName+"CurrentGHNewDailyDose_EB", MinValue);
		// EP.Btn_Click("#General_AddRecordSave_BN");
		// EP.CompareText("#General_VisitsErrMsg_DT",
		// EP.LangData("OutOfRangeError"),
		// EP.GetText("#General_VisitsErrMsg_DT"));
		// EP.VerifyRegExp(TabName+"CurrentGHDoesChangedNoErrmsg_DT");//Using
		// amother Xpath
		//
		// //Success Message
		// EP.ChkBx_SetOptionForAll("//div[@class='RecordOverview']//input[@class='checkbox']",
		// "OFF"); //clear all the checkbox - to remove other, specify error
		// EP.Edit_ClearValue(TabName+"CurrentGHNewDailyDose_EB");
		// EP.Btn_Click("#General_AddRecordSave_BN"); //Save Again, to ignore
		// the message
		// VerifySuccessMessage(EP.LangData("CurrentGHTreatment"));
		//
		// //Check Chapter Status - Partial
		// VerifyChapterStatus(ActionDone, TabIndex, EP.LangData("Partial"));
		//
		// //Tooltip for Edit and Delete
		// EP.VerifyToolTip("#General_Edit_IM");
		// EP.VerifyToolTip("#General_Delete_IM");
		//
		// //Check Chapter Status - Complete
		// EP.Btn_Click("#General_Edit_IM");
		// EP.WaitForPageload();
		// EP.ChkBx_SetOption(EP.ORXPath(TabName+"GHtreatmentMajorityInjection_CB")
		// + "["+EP.RandomValue(1, 3)+"]", "ON"); //Select a random checkbox in
		// Majority Injections
		// EP.Edit_SetValue(TabName+"CurrentGHNewDailyDose_EB", "0.05"); //Enter
		// a value of Formulation Unit
		// //EP.SelectDate(TabName+"CurrentGHFormaultionStartDate_IM",
		// EP.TestData("ECOSPatientDOB")); //Enter Formulation Date
		// EP.SelectDate(TabName+"CurrentGHOthersStopDate_IM",
		// EP.TestData("ECOSPatientDOB"));
		//
		// EP.ChkBx_SetOption(EP.ORXPath(TabName+"GHtreatmentMissedInjection_CB")
		// + "["+EP.RandomValue(1, 7)+"]", "ON");
		// //EP.ChkBx_SetOption(TabName+"CurrentGHMissedReasonForgot_CB", "ON");
		// //Select the checkbox for missed injection, needed for table
		// verication
		// EP.Btn_Click("#General_AddRecordSave_BN"); //Click save
		//
		// //Statuc - Complete
		// VerifyChapterStatus(ActionDone, TabIndex, EP.LangData("Complete"));
		//
		// //Column verification for Current GH Treatment for record overview
		// EP.WbTbl_VerifyTableColumn(TabName+"GHtreatmentWithEasypod_TE",
		// "InjectionsMadeBy", 1, 1,
		// EP.ORData("#RecordOverview_InjectionsMadeByColumnFR_ST"), false);
		// EP.WbTbl_VerifyTableColumn(TabName+"GHtreatmentWithEasypod_TE",
		// "Dosechanged", 2, 1,
		// EP.ORData("#RecordOverview_CurrentTreatmentTableColumn_ST"), false);
		// ColumnText =
		// EP.GetText(EP.ORXPath(TabName+"GHtreatmentWithEasypod_TE")+"//tbody/tr['1']/td[2]");
		// if (ColumnText.startsWith(EP.LangData("YesCaps"))){
		// EP.VerifyTextContained("DosechangedCol_DT",EP.LangData("UnitsMgPerKgBracketsPerDay"),ColumnText);
		// }
		// // EP.WbTbl_VerifyTableColumn(TabName+"GHtreatmentWithEasypod_TE",
		// "FormulationChanged", 3, 1,
		// EP.ORData("#RecordOverview_CurrentTreatmentTableColumn_ST"), false);
		// // ColumnText =
		// EP.GetText(EP.ORXPath(TabName+"GHtreatmentWithEasypod_TE")+"//tbody/tr['1']/td[3]");
		// // if (ColumnText.startsWith(EP.LangData("YesCaps"))){
		// // EP.VerifyMonth("FormulationChangedCol_DT", ColumnText);
		// // }
		//
		// EP.WbTbl_VerifyTableColumn(TabName+"GHtreatmentWithEasypod_TE",
		// "MissedInjections", 3, 1,
		// EP.ORData("#RecordOverview_CurrentTreatmentTableColumn_ST"), false);
		// ColumnText =
		// EP.GetText(EP.ORXPath(TabName+"GHtreatmentWithEasypod_TE")+"//tbody/tr['1']/td[4]");
		// if (ColumnText.startsWith(EP.LangData("YesCaps"))){
		// EP.CompareWithList("MissedInjectionsCol_DT",
		// EP.ORData(TabName+"CurrentGHDoseChangedCol_DT"), ColumnText);
		// }
		//
		// //Delete msg validation (Should be done only for VisitsRecords)
		// if (EP.CompareText(ActionDone,"VisitsRecord")){
		// DeleteRecord("#General_Delete_IM", false);
		// VerifyDeleteMessage(EP.LangData("CurrentGHTreatment"));
		// }
		// break;
		//
		case GERMANY:
			EP.VerifyToolTip(TabName + "CurrentGHTreatmentStartDate_IM");
			EP.VerifyToolTip(TabName + "CurrentGHTreatmentStopDate_IM");

			// Incorrect Format
			EP.Edit_SetValue(TabName + "CurrentGHTreatmentDailyDose_EB",
					IncorrectFormat);
			EP.Btn_Click("#General_Second_BN");
			EP.CompareText("#General_VisitsErrMsg_DT",
					EP.LangData("VerifyIncorrectFormats"),
					EP.GetText("#General_VisitsErrMsg_DT"));
			EP.CompareText(TabName + "CurrentGHTreatmentDailyDoseErrMsg_DT", EP
					.LangData("IncorrectFormat"), EP.GetText(TabName
					+ "CurrentGHTreatmentDailyDoseErrMsg_DT"));

			// Lesser Than min value
			EP.Edit_SetValue(TabName + "CurrentGHTreatmentDailyDose_EB",
					MinValue);
			EP.Btn_Click("#General_Second_BN");
			EP.CompareText("#General_VisitsErrMsg_DT",
					EP.LangData("OutOfRangeError"),
					EP.GetText("#General_VisitsErrMsg_DT"));
			EP.VerifyRegExp(TabName + "CurrentGHTreatmentDailyDoseErrMsg_DT");

			// Save again to proceed with the add page
			if (Env.LanguageUsed.toUpperCase().startsWith("GERMAN")) {
				EP.Edit_SetValue(TabName + "CurrentGHTreatmentDailyDose_EB",
						"0,05");
			} else if (Env.LanguageUsed.toUpperCase().startsWith("ENGLISH")) {
				EP.Edit_SetValue(TabName + "CurrentGHTreatmentDailyDose_EB",
						"0.05");
			}
			EP.Btn_Click("#General_Second_BN");

			// Navigate to Add Page
			if (!EP.ObjectExists("#General_Edit_IM")) { // If the Record is
														// present then Edit it
														// or else create a new
														// record
				ActionPerformed = "#General_Fourth_BN";
				EP.Btn_Click(ActionPerformed); // Click Add button
				EP.WaitForPageload();
				EP.CompareText("#General_FormHeader_ST", EP.LangData("Add"),
						EP.GetText("#General_FormHeader_ST")); // Form Header
			} else {
				ActionPerformed = "#General_Edit_IM";
				EP.Btn_Click(ActionPerformed); // Click Edit button
				EP.WaitForPageload();
				EP.CompareText("#General_FormHeader_ST", EP.LangData("Edit"),
						EP.GetText("#General_FormHeader_ST")); // Form Header
			}

			// Make all the fields visible before taking the screenshot
			EP.Rbtn_SetOption(TabName + "CurrentGHDoseChangedYes_RB", "ON");
			// EP.Rbtn_SetOption(TabName+"CurrentGHFormulationChangeYes_RB",
			// "ON");
			EP.Rbtn_SetOption(TabName + "CurrentGHMissedInjectionYes_RB", "ON");
			EP.Rbtn_SetOption(TabName + "CurrentGHTemporaryCessationYes_RB",
					"ON");
			EP.ChkBx_SetOption(TabName + "CurrentGHAdverseEvent_CB", "ON");
			EP.ChkBx_SetOption(TabName + "CurrentGHOther_CB", "ON");
			EP.ChkBx_SetOption(TabName + "CurrentGHOthers_CB", "ON");
			EP.ChkBx_SetOption(TabName + "CurrentGHReasonsOthersPlsSpecify_CB",
					"ON");

			EP.TakeStaticScreenshot("CurrentGHAddEdit");
			EP.VerifyText("#NavigationTab_CurrentGHTreatment_LK");// Navigation
																	// Tab

			// Who performs the majority of injections?
			EP.VerifyText(TabName + "CurrentGHWhoPerforms_ST");
			EP.VerifyText(TabName + "CurrentGHParents_ST");
			EP.VerifyText(TabName + "CurrentGHUnderParents_ST");
			EP.VerifyText(TabName + "CurrentGHPatientSelfInjectionAT_ST");

			// Dose changed since previous visit?
			EP.VerifyText(TabName + "CurrentGHDoesChanged_ST");
			EP.VerifyText(TabName + "CurrentGHDoesChangedYes_ST");
			EP.VerifyText(TabName + "CurrentGHDoesChangedNo_ST");
			EP.VerifyText(TabName + "CurrentGHReasonForDose_ST");
			EP.VerifyText(TabName + "CurrentGHDoseAdjustment_ST");
			EP.VerifyText(TabName + "CurrentGHPuberty_ST");
			EP.VerifyText(TabName + "CurrentGHEfficacy_ST");
			EP.VerifyText(TabName + "CurrentGHAdverseEvent_ST");
			EP.VerifyText(TabName + "CurrentGHAdverseEventPlsSpecify_ST");
			EP.VerifyText(TabName + "CurrentGHOther_ST");
			EP.VerifyText(TabName + "CurrentGHOthersPlsSpecify_ST");
			EP.VerifyText(TabName + "CurrentGHNewDailyDose_ST");
			EP.VerifyTextContained(TabName + "CurrentGHNewDailyDoseScale_ST");
			EP.VerifyText(TabName + "CurrentGHOthersStopDate_ST");
			EP.VerifyToolTip(TabName + "CurrentGHOthersStopDate_IM");
			EP.VerifyText(TabName + "CurrentGHOthersStartDate_ST");
			EP.VerifyToolTip(TabName + "CurrentGHOthersStartDate_IM");

			// Missed injections since previous visit?
			EP.VerifyText(TabName + "CurrentGHMissedInjectionFR_ST");
			EP.VerifyText(TabName + "CurrentGHMissedInjectionYesFR_ST");
			EP.VerifyText(TabName + "CurrentGHMissedInjectionNoFR_ST");
			EP.VerifyText(TabName + "CurrentGHReasonForFR_ST");
			EP.VerifyText(TabName + "CurrentGHFogotInjection_ST");
			EP.VerifyText(TabName + "CurrentGHHolidaysLong_ST");
			EP.VerifyText(TabName + "CurrentGHMedicalReason_ST");
			EP.VerifyText(TabName + "CurrentGHTiredOfInjection_ST");
			EP.VerifyText(TabName + "CurrentGHTechnicalProblems_ST");
			EP.VerifyText(TabName + "CurrentGHForgotDrub_ST");
			EP.VerifyText(TabName + "CurrentGHRanOut_ST");
			EP.VerifyText(TabName + "CurrentGHOthers_ST");

			// Temporary cessation of treatment Week
			EP.VerifyText(TabName + "CurrentGHTemporaryCessationFR_ST");
			EP.VerifyText(TabName + "CurrentGHTemporaryCessationYes_ST");
			EP.VerifyText(TabName + "CurrentGHTemporaryCessationNo_ST");
			EP.VerifyText(TabName + "CurrentGHTemporaryCessationStopFromFR_ST");
			EP.VerifyToolTip(TabName + "CurrentGHTemporaryCessationStopFrom_IM");
			EP.VerifyText(TabName + "CurrentGHTemporaryCessationStopToFR_ST");
			EP.VerifyToolTip(TabName + "CurrentGHTemporaryCessationStopTo_IM");

			// Treatment stop in accordance with physician?
			EP.VerifyText(TabName + "CurrentGHTreatmentStopFR_ST");
			EP.VerifyText(TabName + "CurrentGHTreatmentStopYes_ST");
			EP.VerifyText(TabName + "CurrentGHTreatmentStopNo_ST");

			// Was the dose adjusted following this temporary treatment stop?
			EP.VerifyText(TabName + "CurrentGHWasTheDoseFR_ST");
			EP.VerifyText(TabName + "CurrentGHWasTheDoseYesFR_ST");
			EP.VerifyText(TabName + "CurrentGHWasTheDoseNoFR_ST");

			// Reasons for temporary cessation of treatment
			EP.VerifyText(TabName + "CurrentGHReasonsForFR_ST");
			EP.VerifyText(TabName + "CurrentGHReasonsForHolidays_ST");
			EP.VerifyText(TabName + "CurrentGHReasonsForMedicalReason_ST");
			EP.VerifyText(TabName + "CurrentGHReasonsOthersPlsSpecify_ST");

			// Verify save and cancel buttons on the Current GhTreatment
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
				EP.Btn_Click(ActionPerformed);
			}

			// -*-*-*-*-**-*-*-*-*-*-*-*-*-*-*-*-*-*DYNAMIC Text in Current GH
			// add*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

			// Mandatory field error
			// After pagelayout, all the fields will go back to last saved
			// state. So select the radio buttons again
			EP.Rbtn_SetOption(TabName + "CurrentGHDoseChangedYes_RB", "ON");
			// EP.Rbtn_SetOption(TabName+"CurrentGHFormulationChangeYes_RB",
			// "ON");
			EP.Rbtn_SetOption(TabName + "CurrentGHMissedInjectionYes_RB", "ON");
			EP.Rbtn_SetOption(TabName + "CurrentGHTemporaryCessationYes_RB",
					"ON");

			// Remove all the checkbox
			EP.ChkBx_SetOptionForAll(
					"//div[@class='RecordOverview']//input[@class='checkbox']",
					"OFF");
			// Select the Required Checkboxes
			EP.ChkBx_SetOption(TabName + "CurrentGHAdverseEvent_CB", "ON");
			EP.ChkBx_SetOption(TabName + "CurrentGHOther_CB", "ON");
			EP.ChkBx_SetOption(TabName + "CurrentGHOthers_CB", "ON");
			EP.ChkBx_SetOption(TabName + "CurrentGHReasonsOthersPlsSpecify_CB",
					"ON");
			// Clear all the Editbox
			EP.Edit_ClearValueForAll("//div[@class='RecordOverview']//input[@type='text' and not(@readonly)]");
			EP.Btn_Click("#General_AddRecordSave_BN");// Click save button
			EP.CompareText("#General_VisitsErrMsg_DT",
					EP.LangData("OutOfRangeError"),
					EP.GetText("#General_VisitsErrMsg_DT"));

			EP.CompareText(TabName + "CurrentGHDoseChangedAdverseSpecify_DT",
					EP.LangData("FieldIsMandatory"), EP.GetText(TabName
							+ "CurrentGHDoseChangeAdverseSpecify_DT"));
			EP.CompareText(
					TabName + "CurrentGHDoseChangedAdverseOtherSpecify_DT",
					EP.LangData("FieldIsMandatory"),
					EP.GetText(TabName
							+ "CurrentGHDoseChangeAdverseOtherSpecify_DT"));
			EP.CompareText(TabName + "CurrentGHDoseChangedNewDailyDose_DT",
					EP.LangData("FieldIsMandatory"),
					EP.GetText(TabName + "CurrentGHDoseChangeNewDailyDose_DT"));

			// Second Set of fields
			// No validation in second set
			// Third Set of fields
			EP.CompareText(
					TabName + "CurrentGHMissedInjectionSinceOther_DT",
					EP.LangData("FieldIsMandatory"),
					EP.GetText(TabName
							+ "CurrentGHMissedInjectionSinceOtherFR_DT"));
			EP.CompareText(TabName
					+ "CurrentGHMissedInjectionSinceOtherPlsSpecify_DT", EP
					.LangData("FieldIsMandatory"), EP.GetText(TabName
					+ "CurrentGHMissedInjectionSinceOtherPlsSpecifyFR_DT"));

			// Remove other fields
			EP.ChkBx_SetOption(TabName + "CurrentGHAdverseEvent_CB", "OFF");
			EP.ChkBx_SetOption(TabName + "CurrentGHOther_CB", "OFF");

			// Greater Than the Max Value
			// - Shows incorrect format

			// Incorrect Format
			EP.Edit_SetValue(TabName + "CurrentGHNewDailyDose_EB",
					IncorrectFormat);
			EP.Btn_Click("#General_AddRecordSave_BN");
			EP.CompareText("#General_VisitsErrMsg_DT",
					EP.LangData("VerifyIncorrectFormats"),
					EP.GetText("#General_VisitsErrMsg_DT"));
			EP.CompareText(TabName + "CurrentGHNewDailyDoseFormat_DT",
					EP.LangData("IncorrectFormat"),
					EP.GetText(TabName + "CurrentGHNewDailyDoseFormat_DT"));

			// Lesser Than the min Value
			EP.Edit_SetValue(TabName + "CurrentGHNewDailyDose_EB", MinValue);
			EP.Btn_Click("#General_AddRecordSave_BN");
			EP.CompareText("#General_VisitsErrMsg_DT",
					EP.LangData("OutOfRangeError"),
					EP.GetText("#General_VisitsErrMsg_DT"));
			EP.VerifyRegExp(TabName + "CurrentGHDoesChangedNoErrmsg_DT");// Using
																			// amother
																			// Xpath

			// Success Message
			EP.ChkBx_SetOptionForAll(
					"//div[@class='RecordOverview']//input[@class='checkbox']",
					"OFF"); // clear all the checkbox - to remove other, specify
							// error
			EP.Edit_ClearValue(TabName + "CurrentGHNewDailyDose_EB");
			EP.Btn_Click("#General_AddRecordSave_BN"); // Save Again, to ignore
														// the message
			VerifySuccessMessage(EP.LangData("CurrentGHTreatment"));

			// Check Chapter Status - Partial
			VerifyChapterStatus(ActionDone, TabIndex, EP.LangData("Partial"));

			// Tooltip for Edit and Delete
			EP.VerifyToolTip("#General_Edit_IM");
			EP.VerifyToolTip("#General_Delete_IM");

			// Check Chapter Status - Complete
			EP.Btn_Click("#General_Edit_IM");
			EP.WaitForPageload();
			EP.ChkBx_SetOption(
					EP.ORXPath(TabName + "GHtreatmentMajorityInjection_CB")
							+ "[" + EP.RandomValue(1, 3) + "]", "ON"); // Select
																		// a
																		// random
																		// checkbox
																		// in
																		// Majority
																		// Injections
			String dailyDose1 = EP.Converter("0.05");
			EP.Edit_SetValue(TabName + "CurrentGHNewDailyDose_EB", dailyDose1); // Enter
																				// a
																				// value
																				// of
																				// Formulation
																				// Unit
			EP.ChkBx_SetOption("#AddVisit_CurrentGHPuberty_CB", "ON");
			EP.SelectDate("#AddVisit_CurrentGHTreatmentStartDate_IM",
					EP.TestData("ECOSPatientDOB"));// Previous daily dose stop
													// date
			Date = EP.Date_AddDays(EP.TestData("ECOSPatientDOB"), 5,
					Calendar.MONTH);
			EP.SelectDate("#AddVisit_CurrentGHTreatmentStopDate_IM", Date);// New
																			// daily
																			// dose
																			// start
																			// date
			EP.ChkBx_SetOption(
					EP.ORXPath(TabName + "GHtreatmentMissedInjection_CB") + "["
							+ EP.RandomValue(1, 7) + "]", "ON");
			EP.Rbtn_SetOption("#AddVisit_CurrentGHTemporaryCessationYes_RB",
					"ON");
			EP.SelectDate(
					"#CompleteTermination_DiscontinuedLastStudyDayCalendar_TT",
					EP.TestData("ECOSPatientDOB"));// Cessation of treatment
													// stop from
			EP.SelectDate("#AddVisit_CurrentGHFormaultionStopDate_IM", Date);// Cessation
																				// of
																				// treatment
																				// stop
																				// to
			EP.ChkBx_SetOption("#AddVisit_MedicalReason_CB", "ON");
			EP.Btn_Click("#General_AddRecordSave_BN"); // Click save

			// Status - Complete
			VerifyChapterStatus(ActionDone, TabIndex, EP.LangData("Complete"));

			// Verify tooltip and values in the table
			// First Column
			CheckTableText(TabName + "GHtreatmentWithEasypod_TE", 1, 1, 1,
					EP.LangData("WhoPerformsMajorityInjections"),
					EP.ORData("#RecordOverview_InjectionsMadeByColumnAT_ST"));

			// Second Column
			CheckTableText(TabName + "GHtreatmentWithEasypod_TE", 1, 2, 1,
					EP.LangData("DoseChangedSincePreviousVisit"),
					EP.ORData("#AddVisit_DoseChangedColValue1_DT"));
			CheckTableText(TabName + "GHtreatmentWithEasypod_TE", 1, 2, 2,
					EP.LangData("DoseChangeReason"),
					EP.ORData("#AddVisit_DoseChangedColValue2_DT"));
			CheckTableText(TabName + "GHtreatmentWithEasypod_TE", 1, 2, 3,
					EP.LangData("NewDailyDose"),
					EP.LangData("UnitsMgPerKgBracketsPerDay"));
			CheckTableText(TabName + "GHtreatmentWithEasypod_TE", 1, 2, 4,
					EP.LangData("PreviousDailyDoseStopDate"),
					EP.MonthShortAll());
			CheckTableText(TabName + "GHtreatmentWithEasypod_TE", 1, 2, 5,
					EP.LangData("NewDailyDoseAtStartDate"), EP.MonthShortAll());

			// Fourth Column
			CheckTableText(TabName + "GHtreatmentWithEasypod_TE", 1, 3, 1,
					EP.LangData("MissedInjectionsSincePreviousVisit"),
					EP.ORData("#AddVisit_DoseChangedColValue1_DT"));
			CheckTableText(TabName + "GHtreatmentWithEasypod_TE", 1, 3, 2,
					EP.LangData("MissedInjectionsReason"),
					EP.ORData("#AddVisit_CurrentGHDoseChangedCol_DT"));
			CheckTableText(TabName + "GHtreatmentWithEasypod_TE", 1, 3, 3,
					EP.LangData("TemporaryCessationGreater"),
					EP.ORData("#AddVisit_DoseChangedColValue1_DT"));
			CheckTableText(TabName + "GHtreatmentWithEasypod_TE", 1, 3, 4,
					EP.LangData("StopFrom"), EP.MonthShortAll());
			CheckTableText(TabName + "GHtreatmentWithEasypod_TE", 1, 3, 5,
					EP.LangData("StopTo"), EP.MonthShortAll());
			CheckTableText(TabName + "GHtreatmentWithEasypod_TE", 1, 3, 6,
					EP.LangData("TreamentStopAccordancePhysician"),
					EP.ORData("#AddVisit_MissedInjectionColValue6_DT"));
			CheckTableText(TabName + "GHtreatmentWithEasypod_TE", 1, 3, 7,
					EP.LangData("TemporaryTreatmentStop"),
					EP.ORData("#AddVisit_MissedInjectionColValue6_DT"));
			CheckTableText(TabName + "GHtreatmentWithEasypod_TE", 1, 3, 8,
					EP.LangData("TemporaryCessationReason"),
					EP.ORData("#AddVisit_MissedInjectionColValue8_DT"));

			// Delete msg validation (Should be done only for VisitsRecords)
			if (EP.CompareText(ActionDone, "VisitsRecord")) {
				DeleteRecord("#General_Delete_IM", false);
				VerifyDeleteMessage(EP.LangData("CurrentGHTreatment"));
			}
			break;

		default:

		}

	}

	public void TannerStageStaticMale(String ActionDone, String TabIndex) {
		VerifyChapterName(ActionDone, TabIndex, EP.LangData("TannerStage"));
		if (EP.CompareText(ActionDone, "AddVisit")) {
			VerifyChapterStatus(ActionDone, TabIndex, EP.LangData("NoData"));
		} else {
			VerifyChapterStatus(ActionDone, TabIndex,
					EP.ORData("#General_ChapterStatus_ST"));
		}
		switch (switchCountryName) {

		case AUSTRALIA:
			AustraliaECRF AustraliaDR = new AustraliaECRF(EP);
			AustraliaDR.TannerStageStaticMale(ActionDone, TabIndex);
			AustraliaDR = null;
			break;

		case AUSTRIA:
			AustriaECRF AustriaDR = new AustriaECRF(EP);
			AustriaDR.TannerStageStaticMale(ActionDone, TabIndex);
			AustriaDR = null;
			break;

		case TAIWAN:
			TaiwanECRF TaiwanDR = new TaiwanECRF(EP);
			TaiwanDR.TannerStageStaticMale(ActionDone, TabIndex);
			TaiwanDR = null;
			break;

		case CZECHREPUBLIC:
			CzechRepublicECRF CzechDR = new CzechRepublicECRF(EP);
			CzechDR.TannerStageStaticMale(ActionDone, TabIndex);
			CzechDR = null;
			break;

		case FINLAND:
			FinlandECRF FinlandDR = new FinlandECRF(EP);
			FinlandDR.TannerStageStaticMale(ActionDone, TabIndex);
			FinlandDR = null;
			break;

		case GREECE:
			GreeceECRF GreeceDR = new GreeceECRF(EP);
			GreeceDR.TannerStageStaticMale(ActionDone, TabIndex);
			GreeceDR = null;
			break;

		case HUNGARY:
			HungaryECRF HungaryDR = new HungaryECRF(EP);
			HungaryDR.TannerStageStaticMale(ActionDone, TabIndex);
			HungaryDR = null;
			break;

		case ICELAND:
			IcelandECRF IcelandDR = new IcelandECRF(EP);
			IcelandDR.TannerStageStaticMale(ActionDone, TabIndex);
			IcelandDR = null;
			break;

		case ITALY:
			ItalyECRF ItalyDR = new ItalyECRF(EP);
			ItalyDR.TannerStageStaticMale(ActionDone, TabIndex);
			ItalyDR = null;
			break;

		case KOREA:
			KoreaECRF KoreaDR = new KoreaECRF(EP);
			KoreaDR.TannerStageStaticMale(ActionDone, TabIndex);
			KoreaDR = null;
			break;

		case NORWAY:
			NorwayECRF NorwayDR = new NorwayECRF(EP);
			NorwayDR.TannerStageStaticMale(ActionDone, TabIndex);
			NorwayDR = null;
			break;

		case SLOVAKIA:
			SlovakiaECRF SlovakiaDR = new SlovakiaECRF(EP);
			SlovakiaDR.TannerStageStaticMale(ActionDone, TabIndex);
			SlovakiaDR = null;
			break;

		case SPAIN:
			SpainECRF SpainDR = new SpainECRF(EP);
			SpainDR.TannerStageStaticMale(ActionDone, TabIndex);
			SpainDR = null;
			break;

		case SWEDEN:
			SwedenECRF SwedenDR = new SwedenECRF(EP);
			SwedenDR.TannerStageStaticMale(ActionDone, TabIndex);
			SwedenDR = null;
			break;

		case UNITEDKINGDOM:
			UnitedKingdomECRF UnitedKingdomDR = new UnitedKingdomECRF(EP);
			UnitedKingdomDR.TannerStageStaticMale(ActionDone, TabIndex);
			UnitedKingdomDR = null;
			break;

		case FRANCE:
			FranceECRF FranceDR = new FranceECRF(EP);
			FranceDR.TannerStageStaticMale(ActionDone, TabIndex);
			FranceDR = null;
			break;

		case CANADA:
			CanadaECRF CanadaDR = new CanadaECRF(EP);
			CanadaDR.TannerStageStaticMale(ActionDone, TabIndex);
			CanadaDR = null;
			break;

		default:

		}
	}

	public void TannerStageStatic(String ActionDone, String TabIndex) {
		String TabName = AppendHash(ActionDone);
		VerifyChapterName(ActionDone, TabIndex, EP.LangData("TannerStage"));
		if (EP.CompareText(ActionDone, "AddVisit")) {
			VerifyChapterStatus(ActionDone, TabIndex, EP.LangData("NoData"));
		} else {
			VerifyChapterStatus(ActionDone, TabIndex,
					EP.ORData("#General_ChapterStatus_ST"));
		}
		switch (switchCountryName) {

		case AUSTRALIA:
			AustraliaECRF AustraliaDR = new AustraliaECRF(EP);
			AustraliaDR.TannerStageStatic(ActionDone, TabIndex);
			AustraliaDR = null;
			break;

		case AUSTRIA:
			AustriaECRF AustriaDR = new AustriaECRF(EP);
			AustriaDR.TannerStageStatic(ActionDone, TabIndex);
			AustriaDR = null;
			break;

		case CZECHREPUBLIC:
			CzechRepublicECRF CzechDR = new CzechRepublicECRF(EP);
			CzechDR.TannerStageStatic(ActionDone, TabIndex);
			CzechDR = null;
			break;

		case INDONESIA:
			IndonesiaECRF IndonesiaDR = new IndonesiaECRF(EP);
			IndonesiaDR.TannerStageStatic(ActionDone, TabIndex);
			IndonesiaDR = null;
			break;

		case COLOMBIA:
			ColombiaECRF ColombiaDR = new ColombiaECRF(EP);
			ColombiaDR.TannerStageStatic(ActionDone, TabIndex);
			ColombiaDR = null;
			break;

		case ARGENTINA:
			ArgentinaECRF ArgentinaDR = new ArgentinaECRF(EP);
			ArgentinaDR.TannerStageStatic(ActionDone, TabIndex);
			ArgentinaDR = null;
			break;

		case TAIWAN:
			TaiwanECRF TaiwanDR = new TaiwanECRF(EP);
			TaiwanDR.TannerStageStatic(ActionDone, TabIndex);
			TaiwanDR = null;
			break;
		case FINLAND:
			FinlandECRF FinlandDR = new FinlandECRF(EP);
			FinlandDR.TannerStageStatic(ActionDone, TabIndex);
			FinlandDR = null;
			break;

		case GREECE:
			GreeceECRF GreeceDR = new GreeceECRF(EP);
			GreeceDR.TannerStageStatic(ActionDone, TabIndex);
			GreeceDR = null;
			break;

		case HUNGARY:
			HungaryECRF HungaryDR = new HungaryECRF(EP);
			HungaryDR.TannerStageStatic(ActionDone, TabIndex);
			HungaryDR = null;
			break;

		case ICELAND:
			IcelandECRF IcelandDR = new IcelandECRF(EP);
			IcelandDR.TannerStageStatic(ActionDone, TabIndex);
			IcelandDR = null;
			break;

		case ITALY:
			ItalyECRF ItalyDR = new ItalyECRF(EP);
			ItalyDR.TannerStageStatic(ActionDone, TabIndex);
			ItalyDR = null;
			break;

		case KOREA:
			KoreaECRF KoreaDR = new KoreaECRF(EP);
			KoreaDR.TannerStageStatic(ActionDone, TabIndex);
			KoreaDR = null;
			break;

		case NORWAY:
			NorwayECRF NorwayDR = new NorwayECRF(EP);
			NorwayDR.TannerStageStatic(ActionDone, TabIndex);
			NorwayDR = null;
			break;

		case SLOVAKIA:
			SlovakiaECRF SlovakiaDR = new SlovakiaECRF(EP);
			SlovakiaDR.TannerStageStatic(ActionDone, TabIndex);
			SlovakiaDR = null;
			break;

		case SPAIN:
			SpainECRF SpainDR = new SpainECRF(EP);
			SpainDR.TannerStageStatic(ActionDone, TabIndex);
			SpainDR = null;
			break;

		case SINGAPORE:
			SingaporeECRF SingaporeDR = new SingaporeECRF(EP);
			SingaporeDR.TannerStageStatic(ActionDone, TabIndex);
			SingaporeDR = null;
			break;

		case SAUDIARABIA:
			SaudiArabiaECRF SaudiArabiaDR = new SaudiArabiaECRF(EP);
			SaudiArabiaDR.TannerStageStatic(ActionDone, TabIndex);
			SaudiArabiaDR = null;
			break;

		case SWEDEN:
			SwedenECRF SwedenDR = new SwedenECRF(EP);
			SwedenDR.TannerStageStatic(ActionDone, TabIndex);
			SwedenDR = null;
			break;

		case UNITEDKINGDOM:
			UnitedKingdomECRF UnitedKingdomDR = new UnitedKingdomECRF(EP);
			UnitedKingdomDR.TannerStageStatic(ActionDone, TabIndex);
			UnitedKingdomDR = null;
			break;

		case UNITEDARABEMIRATES:
			UnitedArabEmiratesECRF UnitedArabEmiratesDR = new UnitedArabEmiratesECRF(
					EP);
			UnitedArabEmiratesDR.TannerStageStatic(ActionDone, TabIndex);
			UnitedArabEmiratesDR = null;
			break;

		case FRANCE:
			FranceECRF FranceDR = new FranceECRF(EP);
			FranceDR.TannerStageStatic(ActionDone, TabIndex);
			FranceDR = null;
			break;

		case CANADA:
			CanadaECRF CanadaDR = new CanadaECRF(EP);
			CanadaDR.TannerStageStatic(ActionDone, TabIndex);
			CanadaDR = null;
			break;
		case MEXICO:
			MexicoECRF MexicoDR = new MexicoECRF(EP);
			MexicoDR.TannerStageStatic(ActionDone, TabIndex);
			MexicoDR = null;
			break;
		// case CHINA:
		// case ARGENTINA:

		case GERMANY:

			VerifyChapterHeading(ActionDone, TabIndex, 1,
					EP.LangData("PubertalAssessmentResults"));
			EP.VerifyTableHeader(TabName + "Tanner_TE");

			if (!EP.CompareText(ActionDone, "RecordOverview")) {

			} else {

				// Verify tooltip and values in the table
				CheckTableText(TabName + "Tanner_TE", 1, 1, 1,
						EP.LangData("AssessmentDate"), EP.MonthShortAll());
				CheckTableText(TabName + "Tanner_TE", 1, 2, 1,
						EP.LangData("AxillaryHair"),
						EP.ORData("#AddVisit_TannerStageAxillaryHair_DD"));
				CheckTableText(TabName + "Tanner_TE", 1, 3, 1,
						EP.LangData("PubicHairStage"),
						EP.ORData("#AddVisit_TannerStagePubicHairStage_DD"));
				CheckTableText(
						TabName + "Tanner_TE",
						1,
						4,
						1,
						EP.LangData("PubertalAssessmentGenitalStage"),
						EP.ORData("#AddVisit_TannerStagePubertalAssessmentGenitalStageAT_DD"));
				CheckTableText(
						TabName + "Tanner_TE",
						1,
						5,
						2,
						EP.LangData("Menarche"),
						EP.LangData("NotAvailable") + "|"
								+ EP.ORData("#General_YesorNoNotKnown_DT"));
				String ColumnText1 = EP.GetText(EP.ORXPath(TabName
						+ "Tanner_TE")
						+ "//tbody/tr['1']/td[5]");
				if (ColumnText1.startsWith(EP.LangData("YesCaps"))) {
					CheckTableText(TabName + "Tanner_TE", 3, 5, 3,
							EP.LangData("AgeMenarcheDate"),
							EP.LangData("XMLYear"));
					CheckTableText(TabName + "Tanner_TE", 3, 5, 3,
							EP.LangData("AgeMenarcheDate"),
							EP.LangData("XMLMonth"));
				}

				CheckTableText(TabName + "Tanner_TE", 1, 6, 1,
						EP.LangData("PatientEnteredPuberty"),
						EP.ORData("#General_YesorNo_DT"));
				String ColumnText = EP.GetText(EP
						.ORXPath(TabName + "Tanner_TE")
						+ "//tbody/tr['1']/td[6]");
				if (ColumnText.startsWith(EP.LangData("YesCaps"))) {
					CheckTableText(TabName + "Tanner_TE", 1, 6, 2,
							EP.LangData("DataEnteredPuberty"),
							EP.MonthShortAll());
				}

			}
			break;

		default:
		}
	}

	public void TannerStageDynamic(String ActionDone, String TabIndex,
			String RecoveringURL) {
		String TabName = AppendHash(ActionDone);
		EP.CompareText("#General_First_BN", EP.LangData("Add"),
				EP.GetText("#General_First_BN")); // Add Button
		EP.CompareText("#AddVisit_AssessmentNotDone_BN",
				EP.LangData("AssessmentNotDone"),
				EP.GetText("#AddVisit_AssessmentNotDone_BN")); // Assessment not
																// done Button
		if (EP.ObjectExists("#General_Delete_IM")) {
			DeleteRecord("#General_Delete_IM", false);
			EP.Btn_Click("#General_First_BN");
		} else {
			EP.Btn_Click("#General_First_BN");
			EP.WaitForPageload();
		}
		switch (switchCountryName) {

		case AUSTRALIA:
			AustraliaECRF AustraliaDR = new AustraliaECRF(EP);
			AustraliaDR.TannerStageDynamic(ActionDone, TabIndex, RecoveringURL);
			AustraliaDR = null;
			break;

		case AUSTRIA:
			AustriaECRF AustriaDR = new AustriaECRF(EP);
			AustriaDR.TannerStageDynamic(ActionDone, TabIndex, RecoveringURL);
			AustriaDR = null;
			break;

		case COLOMBIA:
			ColombiaECRF ColombiaDR = new ColombiaECRF(EP);
			ColombiaDR.TannerStageDynamic(ActionDone, TabIndex, RecoveringURL);
			ColombiaDR = null;
			break;

		case ARGENTINA:
			ArgentinaECRF ArgentinaDR = new ArgentinaECRF(EP);
			ArgentinaDR.TannerStageDynamic(ActionDone, TabIndex, RecoveringURL);
			ArgentinaDR = null;
			break;

		case TAIWAN:
			TaiwanECRF TaiwanDR = new TaiwanECRF(EP);
			TaiwanDR.TannerStageDynamic(ActionDone, TabIndex, RecoveringURL);
			TaiwanDR = null;
			break;

		case INDONESIA:
			IndonesiaECRF IndonesiaDR = new IndonesiaECRF(EP);
			IndonesiaDR.TannerStageDynamic(ActionDone, TabIndex, RecoveringURL);
			IndonesiaDR = null;
			break;

		case CZECHREPUBLIC:
			CzechRepublicECRF CzechDR = new CzechRepublicECRF(EP);
			CzechDR.TannerStageDynamic(ActionDone, TabIndex, RecoveringURL);
			CzechDR = null;
			break;

		case FINLAND:
			FinlandECRF FinlandDR = new FinlandECRF(EP);
			FinlandDR.TannerStageDynamic(ActionDone, TabIndex, RecoveringURL);
			FinlandDR = null;
			break;

		case GREECE:
			GreeceECRF GreeceDR = new GreeceECRF(EP);
			GreeceDR.TannerStageDynamic(ActionDone, TabIndex, RecoveringURL);
			GreeceDR = null;
			break;
		case SAUDIARABIA:
			SaudiArabiaECRF SaudiArabiaDR = new SaudiArabiaECRF(EP);
			SaudiArabiaDR.TannerStageDynamic(ActionDone, TabIndex,
					RecoveringURL);
			SaudiArabiaDR = null;
			break;

		case HUNGARY:
			HungaryECRF HungaryDR = new HungaryECRF(EP);
			HungaryDR.TannerStageDynamic(ActionDone, TabIndex, RecoveringURL);
			HungaryDR = null;
			break;

		case ICELAND:
			IcelandECRF IcelandDR = new IcelandECRF(EP);
			IcelandDR.TannerStageDynamic(ActionDone, TabIndex, RecoveringURL);
			IcelandDR = null;
			break;

		case ITALY:
			ItalyECRF ItalyDR = new ItalyECRF(EP);
			ItalyDR.TannerStageDynamic(ActionDone, TabIndex, RecoveringURL);
			ItalyDR = null;
			break;

		case KOREA:
			KoreaECRF KoreaDR = new KoreaECRF(EP);
			KoreaDR.TannerStageDynamic(ActionDone, TabIndex, RecoveringURL);
			KoreaDR = null;
			break;

		case NORWAY:
			NorwayECRF NorwayDR = new NorwayECRF(EP);
			NorwayDR.TannerStageDynamic(ActionDone, TabIndex, RecoveringURL);
			NorwayDR = null;
			break;

		case SLOVAKIA:
			SlovakiaECRF SlovakiaDR = new SlovakiaECRF(EP);
			SlovakiaDR.TannerStageDynamic(ActionDone, TabIndex, RecoveringURL);
			SlovakiaDR = null;
			break;

		case SPAIN:
			SpainECRF SpainDR = new SpainECRF(EP);
			SpainDR.TannerStageDynamic(ActionDone, TabIndex, RecoveringURL);
			SpainDR = null;
			break;

		case SINGAPORE:
			SingaporeECRF SingaporeDR = new SingaporeECRF(EP);
			SingaporeDR.TannerStageDynamic(ActionDone, TabIndex, RecoveringURL);
			SingaporeDR = null;
			break;

		case SWEDEN:
			SwedenECRF SwedenDR = new SwedenECRF(EP);
			SwedenDR.TannerStageDynamic(ActionDone, TabIndex, RecoveringURL);
			SwedenDR = null;
			break;

		case UNITEDKINGDOM:
			UnitedKingdomECRF UnitedKingdomDR = new UnitedKingdomECRF(EP);
			UnitedKingdomDR.TannerStageDynamic(ActionDone, TabIndex,
					RecoveringURL);
			UnitedKingdomDR = null;
			break;

		case UNITEDARABEMIRATES:
			UnitedArabEmiratesECRF UnitedArabEmiratesDR = new UnitedArabEmiratesECRF(
					EP);
			UnitedArabEmiratesDR.TannerStageDynamic(ActionDone, TabIndex,
					RecoveringURL);
			UnitedArabEmiratesDR = null;
			break;

		case FRANCE:
			FranceECRF FranceDR = new FranceECRF(EP);
			FranceDR.TannerStageDynamic(ActionDone, TabIndex, RecoveringURL);
			FranceDR = null;
			break;

		case CANADA:
			CanadaECRF CanadaDR = new CanadaECRF(EP);
			CanadaDR.TannerStageDynamic(ActionDone, TabIndex, RecoveringURL);
			CanadaDR = null;
			break;
		case MEXICO:
			MexicoECRF MexicoDR = new MexicoECRF(EP);
			MexicoDR.TannerStageDynamic(ActionDone, TabIndex, RecoveringURL);
			MexicoDR = null;
			break;

		// case CHINA:
		// case ARGENTINA

		case GERMANY:

			EP.TakeStaticScreenshot("TannerStageAddEdit");
			EP.VerifyText("#NavigationTab_TannerStage_LK"); // Navigation Tab
			EP.VerifyText(TabName + "TannerStageAssesmentDate_ST");
			EP.VerifyMonth(TabName + "TannerStageAssesmentDate_EB");
			EP.VerifyToolTip(TabName + "TannerStageAssesmentDate_IM");
			EP.VerifyText(TabName + "TannerStageAxillaryHair_ST");
			EP.CompareDropDownValues(TabName + "TannerStageAxillaryHair_DD");
			EP.VerifyText(TabName + "TannerStagePubicHairStage_ST");
			EP.CompareDropDownValues(TabName + "TannerStagePubicHairStage_DD");
			EP.VerifyText(TabName
					+ "TannerStagePubertalAssessmentGenitalStage_ST");
			EP.CompareDropDownValues(TabName
					+ "TannerStagePubertalAssessmentGenitalStageAT_DD");// New
			EP.VerifyText(TabName + "TannerStageTestisVolLeft_ST");
			EP.VerifyTextContained(TabName + "TannerStageTestisVolLeftml_ST");
			EP.VerifyText(TabName + "TannerStageTestisVolRight_ST");
			EP.VerifyTextContained(TabName + "TannerStageTestisVolRightml_ST");
			EP.VerifyText(TabName + "TannerStageEnteredPuberty_ST");
			EP.VerifyText(TabName
					+ "TannerStageHasTheDateEnteredPubertyYesAT_ST");// Yes
			EP.VerifyText(TabName
					+ "TannerStageHasTheDateEnteredPubertyNoAT_ST");// No

			// Verify save and cancel buttons
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

			// Check for dynamic text after clicking yes button
			EP.Rbtn_SetOption(TabName
					+ "TannerStageHasTheDateEnteredPubertyYesAT_RB", "ON");
			EP.VerifyText(TabName + "TannerStageHasTheDateEnteredPuberty_ST");// Date
																				// Entered
																				// Puberty
			EP.VerifyToolTip(TabName + "TannerStageEnteredPuberty_IM");// Tool
																		// tip
																		// for
																		// second
																		// calendar

			// Mandatory and Incorrect format field error
			EP.RunJavaScript("document.getElementById('dateEnteredPuberty').value = '';"); // Clear
																							// the
																							// date
			EP.Edit_SetValue(TabName + "TannerStageTestisVolLeft_EB",
					IncorrectFormat);
			EP.Edit_SetValue(TabName + "TannerStageTestisVolRight_EB",
					IncorrectFormat);
			EP.Btn_Click("#General_AddRecordSave_BN");
			EP.TakeStaticScreenshot("FieldIsMandatoryError");
			EP.CompareText("#General_VisitsErrMsg_DT",
					EP.LangData("VerifyIncorrectFormats"),
					EP.GetText("#General_VisitsErrMsg_DT"));// Incorrect format
															// error msg
			// EP.CompareText(TabName+"TannerStagePubertyDateErrMsg_DT",
			// EP.LangData("FieldIsMandatory"),
			// EP.GetText(TabName+"TannerStagePubertyDateErrMsg_DT"));//Its not
			// a mandatory field so commemnted
			EP.CompareText(TabName + "TannerStageTestisVolLeftErrMsg_DT",
					EP.LangData("IncorrectFormat"),
					EP.GetText(TabName + "TannerStageTestisVolLeftErrMsg_DT"));
			EP.CompareText(TabName + "TannerStageTestisVolRightErrMsg_DT",
					EP.LangData("IncorrectFormat"),
					EP.GetText(TabName + "TannerStageTestisVolRightErrMsg_DT"));
			EP.ClearStaticScreenshot();
			// Greater than max shows incorrect format

			// Lesser Than the min value
			EP.DrpDwn_SelectIndex(TabName + "TannerStageAxillaryHair_DD", 1);
			EP.DrpDwn_SelectIndex(TabName + "TannerStagePubicHairStage_DD", 1);
			EP.DrpDwn_SelectIndex(TabName
					+ "TannerStagePubertalAssessmentGenitalStage_DD", 1);
			EP.Rbtn_SetOption(TabName
					+ "TannerStageHasTheDateEnteredPubertyNoAT_RB", "ON");
			EP.Edit_SetValue(TabName + "TannerStageTestisVolLeft_EB", MinValue);
			EP.Edit_SetValue(TabName + "TannerStageTestisVolRight_EB", MinValue);
			EP.Btn_Click("#General_AddRecordSave_BN");
			EP.TakeStaticScreenshot("ValueMustBeError");
			EP.CompareText("#General_VisitsErrMsg_DT",
					EP.LangData("OutOfRangeError"),
					EP.GetText("#General_VisitsErrMsg_DT"));// Incorrect format
															// error msg
			EP.VerifyRegExp(TabName + "AxillaryHairMinValueErr_DT", "\\d{1,3}");
			EP.VerifyRegExp(TabName + "PubicHairStageMinValueErr_DT",
					"\\d{1,3}");
			EP.VerifyRegExp(TabName + "GenitalStageMinValueErr_DT");
			EP.VerifyRegExp(TabName + "TannerStageTestisVolLeftErrMsg_DT");
			EP.VerifyRegExp(TabName + "TannerStageTestisVolRightErrMsg_DT");
			EP.ClearStaticScreenshot();
			// Date Validations
			// No Date validations

			// Success Message validation
			// EP.Rbtn_SetOption(TabName+"TannerStageHasTheDateEnteredPubertyYesAT_RB",
			// "ON");
			// EP.DrpDwn_SelectIndex(TabName+"TannerStagePubertalAssessmentGenitalStage_DD",
			// 0);
			// EP.Btn_Click("#General_AddRecordSave_BN");

			// //Check Chapter Status - Partial
			// VerifyChapterStatus(ActionDone, TabIndex,
			// EP.LangData("Partial"));

			// Check Chapter Status - Complete
			// EP.Btn_Click("#General_Edit_IM");
			EP.DrpDwn_SelectIndex(
					TabName + "TannerStageAxillaryHair_DD",
					EP.DrpDwn_GetCount(TabName + "TannerStageAxillaryHair_DD") - 2);
			EP.DrpDwn_SelectIndex(
					TabName + "TannerStagePubicHairStage_DD",
					EP.DrpDwn_GetCount(TabName + "TannerStagePubicHairStage_DD") - 2);
			EP.DrpDwn_SelectIndex(
					TabName + "TannerStagePubertalAssessmentGenitalStage_DD",
					EP.DrpDwn_GetCount(TabName
							+ "TannerStagePubertalAssessmentGenitalStage_DD") - 2);
			EP.Edit_SetValue(TabName + "TannerStageTestisVolLeft_EB", "2");
			EP.Edit_SetValue(TabName + "TannerStageTestisVolRight_EB", "2");
			// EP.Rbtn_SetOption(TabName+"TannerStageHasTheDateEnteredPubertyNoAT_RB",
			// "ON");
			EP.Rbtn_SetOption(TabName
					+ "TannerStageHasTheDateEnteredPubertyYesAT_RB", "ON");
			EP.SelectDate(TabName + "TannerStageEnteredPuberty_IM",
					EP.TestData("ECOSPatientDOB"));
			EP.Btn_Click("#General_AddRecordSave_BN");
			VerifySuccessMessage(EP.LangData("TannerStage"));
			// Tooltip for Edit and Delete
			EP.VerifyToolTip("#General_Edit_IM");
			EP.VerifyToolTip("#General_Delete_IM");
			VerifyChapterStatus(ActionDone, TabIndex, EP.LangData("Complete"));

			// Verify tooltip and values in the table
			CheckTableText(TabName + "Tanner_TE", 1, 1, 1,
					EP.LangData("AssessmentDate"), EP.MonthShortAll());
			CheckTableText(TabName + "Tanner_TE", 1, 2, 1,
					EP.LangData("AxillaryHair"),
					EP.ORData(TabName + "TannerStageAxillaryHair_DD"));
			CheckTableText(TabName + "Tanner_TE", 1, 3, 1,
					EP.LangData("PubicHairStage"),
					EP.ORData(TabName + "TannerStagePubicHairStage_DD"));
			CheckTableText(
					TabName + "Tanner_TE",
					1,
					4,
					1,
					EP.LangData("PubertalAssessmentGenitalStage"),
					EP.ORData(TabName
							+ "TannerStagePubertalAssessmentGenitalStageAT_DD"));
			CheckTableText(TabName + "Tanner_TE", 1, 5, 2,
					EP.LangData("TestisVolumeLeft"),
					EP.LangData("UnitsMIllilitre"));
			CheckTableText(TabName + "Tanner_TE", 1, 5, 3,
					EP.LangData("TestisVolumeRight"),
					EP.LangData("UnitsMIllilitre"));
			CheckTableText(TabName + "Tanner_TE", 1, 6, 1,
					EP.LangData("PatientEnteredPuberty"),
					EP.ORData("#AddVisit_HasThePatientColValue1_DT"));
			CheckTableText(TabName + "Tanner_TE", 1, 6, 2,
					EP.LangData("DataEnteredPuberty"), EP.MonthShortAll());

			// Delete msg validation (Should be done only for VisitsRecords)
			if (EP.CompareText(ActionDone, "VisitsRecord")) {
				DeleteRecord("#General_Delete_IM", false);
				VerifyDeleteMessage(EP.LangData("TannerStage"));
			}
			break;

		default:

		}
	}

	public void BoneAgeStatic(String ActionDone, String TabIndex) {
		String TabName = AppendHash(ActionDone);
		VerifyChapterName(ActionDone, TabIndex, EP.LangData("BoneAge"));
		if (EP.CompareText(ActionDone, "AddVisit")) {
			VerifyChapterStatus(ActionDone, TabIndex, EP.LangData("NoData"));
		} else {
			VerifyChapterStatus(ActionDone, TabIndex,
					EP.ORData("#General_ChapterStatus_ST"));
		}
		switch (switchCountryName) {

		case AUSTRALIA:
			AustraliaECRF AustraliaDR = new AustraliaECRF(EP);
			AustraliaDR.BoneAgeStatic(ActionDone, TabIndex);
			AustraliaDR = null;
			break;

		case AUSTRIA:
			AustriaECRF AustriaDR = new AustriaECRF(EP);
			AustriaDR.BoneAgeStatic(ActionDone, TabIndex);
			AustriaDR = null;
			break;

		case COLOMBIA:
			ColombiaECRF ColombiaDR = new ColombiaECRF(EP);
			ColombiaDR.BoneAgeStatic(ActionDone, TabIndex);
			ColombiaDR = null;
			break;

		case ARGENTINA:
			ArgentinaECRF ArgentinaDR = new ArgentinaECRF(EP);
			ArgentinaDR.BoneAgeStatic(ActionDone, TabIndex);
			ArgentinaDR = null;
			break;

		case INDONESIA:
			IndonesiaECRF IndonesiaDR = new IndonesiaECRF(EP);
			IndonesiaDR.BoneAgeStatic(ActionDone, TabIndex);
			IndonesiaDR = null;
			break;

		case CZECHREPUBLIC:
			CzechRepublicECRF CzechDR = new CzechRepublicECRF(EP);
			CzechDR.BoneAgeStatic(ActionDone, TabIndex);
			CzechDR = null;
			break;

		case FINLAND:
			FinlandECRF FinlandDR = new FinlandECRF(EP);
			FinlandDR.BoneAgeStatic(ActionDone, TabIndex);
			FinlandDR = null;
			break;

		case GREECE:
			GreeceECRF GreeceDR = new GreeceECRF(EP);
			GreeceDR.BoneAgeStatic(ActionDone, TabIndex);
			GreeceDR = null;
			break;

		case TAIWAN:
			TaiwanECRF TaiwanDR = new TaiwanECRF(EP);
			TaiwanDR.BoneAgeStatic(ActionDone, TabIndex);
			TaiwanDR = null;
			break;

		case HUNGARY:
			HungaryECRF HungaryDR = new HungaryECRF(EP);
			HungaryDR.BoneAgeStatic(ActionDone, TabIndex);
			HungaryDR = null;
			break;

		case ICELAND:
			IcelandECRF IcelandDR = new IcelandECRF(EP);
			IcelandDR.BoneAgeStatic(ActionDone, TabIndex);
			IcelandDR = null;
			break;

		case ITALY:
			ItalyECRF ItalyDR = new ItalyECRF(EP);
			ItalyDR.BoneAgeStatic(ActionDone, TabIndex);
			ItalyDR = null;
			break;

		case KOREA:
			KoreaECRF KoreaDR = new KoreaECRF(EP);
			KoreaDR.BoneAgeStatic(ActionDone, TabIndex);
			KoreaDR = null;
			break;

		case NORWAY:
			NorwayECRF NorwayDR = new NorwayECRF(EP);
			NorwayDR.BoneAgeStatic(ActionDone, TabIndex);
			NorwayDR = null;
			break;

		case SLOVAKIA:
			SlovakiaECRF SlovakiaDR = new SlovakiaECRF(EP);
			SlovakiaDR.BoneAgeStatic(ActionDone, TabIndex);
			SlovakiaDR = null;
			break;

		case SPAIN:
			SpainECRF SpainDR = new SpainECRF(EP);
			SpainDR.BoneAgeStatic(ActionDone, TabIndex);
			SpainDR = null;
			break;

		case SWEDEN:
			SwedenECRF SwedenDR = new SwedenECRF(EP);
			SwedenDR.BoneAgeStatic(ActionDone, TabIndex);
			SwedenDR = null;
			break;

		case SINGAPORE:
			SingaporeECRF SingaporeDR = new SingaporeECRF(EP);
			SingaporeDR.BoneAgeStatic(ActionDone, TabIndex);
			SingaporeDR = null;
			break;

		case SAUDIARABIA:
			SaudiArabiaECRF SaudiArabiaDR = new SaudiArabiaECRF(EP);
			SaudiArabiaDR.BoneAgeStatic(ActionDone, TabIndex);
			SaudiArabiaDR = null;
			break;

		case UNITEDARABEMIRATES:
			UnitedArabEmiratesECRF UnitedArabEmiratesDR = new UnitedArabEmiratesECRF(
					EP);
			UnitedArabEmiratesDR.BoneAgeStatic(ActionDone, TabIndex);
			UnitedArabEmiratesDR = null;
			break;

		case UNITEDKINGDOM:
			UnitedKingdomECRF UnitedKingdomDR = new UnitedKingdomECRF(EP);
			UnitedKingdomDR.BoneAgeStatic(ActionDone, TabIndex);
			UnitedKingdomDR = null;
			break;

		case CANADA:
			CanadaECRF CanadaDR = new CanadaECRF(EP);
			CanadaDR.BoneAgeStatic(ActionDone, TabIndex);
			CanadaDR = null;
			break;

		case FRANCE:
			FranceECRF FranceDR = new FranceECRF(EP);
			FranceDR.BoneAgeStatic(ActionDone, TabIndex);
			FranceDR = null;
			break;
		case MEXICO:
			MexicoECRF MexicoDR = new MexicoECRF(EP);
			MexicoDR.BoneAgeStatic(ActionDone, TabIndex);
			MexicoDR = null;
			break;

		// case CHINA:
		// case ARGENTINA:

		case GERMANY:

			VerifyChapterHeading(ActionDone, TabIndex, 1,
					EP.LangData("BoneAgeAssessmentResults"));
			EP.VerifyTableHeader(TabName + "BoneAge_TE");

			if (EP.CompareText(ActionDone, "RecordOverview")) {
				CheckTableText("#AddVisit_BoneAge_TE", 1, 1, 1,
						EP.LangData("XMLAssessmentNotDone"),
						EP.ORData("#AddVisit_BoneAge1Col_DT"));
				CheckTableText("#AddVisit_BoneAge_TE", 1, 2, 1,
						EP.LangData("AssessmentDate"), EP.MonthShortAll());
				CheckTableText("#AddVisit_BoneAge_TE", 1, 3, 1,
						EP.LangData("Method"),
						EP.ORData("#AddVisit_BoneMethodFR_DD"));
				CheckTableText("#AddVisit_BoneAge_TE", 1, 3, 2,
						EP.LangData("BoneAgeYearsMonthsEx"), "");
			}
			break;

		default:
		}
	}

	public void BoneAgeDynamic(String ActionDone, String TabIndex,
			String RecoveringURL) {
		String TabName = AppendHash(ActionDone);
		String EditBoxXpath = "";
		int ValueSelected = 0;
		EP.CompareText("#General_First_BN", EP.LangData("Add"),
				EP.GetText("#General_First_BN"));
		EP.CompareText("#AddVisit_AssessmentNotDone_BN",
				EP.LangData("XMLAssessmentNotDone"),
				EP.GetText("#AddVisit_AssessmentNotDone_BN")); // Assessment not
																// done Button
		if (EP.ObjectExists("#General_Delete_IM")) {
			DeleteRecord("#General_Delete_IM", false);
			EP.Btn_Click("#General_First_BN");
		} else {
			EP.Btn_Click("#General_First_BN");
			EP.WaitForPageload();
		}
		switch (switchCountryName) {

		case AUSTRALIA:
			AustraliaECRF AustraliaDR = new AustraliaECRF(EP);
			AustraliaDR.BoneAgeDynamic(ActionDone, TabIndex, RecoveringURL);
			AustraliaDR = null;
			break;

		case ARGENTINA:
			ArgentinaECRF ArgentinaDR = new ArgentinaECRF(EP);
			ArgentinaDR.BoneAgeDynamic(ActionDone, TabIndex, RecoveringURL);
			ArgentinaDR = null;
			break;

		case AUSTRIA:
			AustriaECRF AustriaDR = new AustriaECRF(EP);
			AustriaDR.BoneAgeDynamic(ActionDone, TabIndex, RecoveringURL);
			AustriaDR = null;
			break;

		case COLOMBIA:
			ColombiaECRF ColombiaDR = new ColombiaECRF(EP);
			ColombiaDR.BoneAgeDynamic(ActionDone, TabIndex, RecoveringURL);
			ColombiaDR = null;
			break;

		case INDONESIA:
			IndonesiaECRF IndonesiaDR = new IndonesiaECRF(EP);
			IndonesiaDR.BoneAgeDynamic(ActionDone, TabIndex, RecoveringURL);
			IndonesiaDR = null;
			break;

		case CZECHREPUBLIC:
			CzechRepublicECRF CzechDR = new CzechRepublicECRF(EP);
			CzechDR.BoneAgeDynamic(ActionDone, TabIndex, RecoveringURL);
			CzechDR = null;
			break;

		case FINLAND:
			FinlandECRF FinlandDR = new FinlandECRF(EP);
			FinlandDR.BoneAgeDynamic(ActionDone, TabIndex, RecoveringURL);
			FinlandDR = null;
			break;

		case TAIWAN:
			TaiwanECRF TaiwanDR = new TaiwanECRF(EP);
			TaiwanDR.BoneAgeDynamic(ActionDone, TabIndex, RecoveringURL);
			TaiwanDR = null;
			break;

		case GREECE:
			GreeceECRF GreeceDR = new GreeceECRF(EP);
			GreeceDR.BoneAgeDynamic(ActionDone, TabIndex, RecoveringURL);
			GreeceDR = null;
			break;

		case HUNGARY:
			HungaryECRF HungaryDR = new HungaryECRF(EP);
			HungaryDR.BoneAgeDynamic(ActionDone, TabIndex, RecoveringURL);
			HungaryDR = null;
			break;

		case ICELAND:
			IcelandECRF IcelandDR = new IcelandECRF(EP);
			IcelandDR.BoneAgeDynamic(ActionDone, TabIndex, RecoveringURL);
			IcelandDR = null;
			break;

		case ITALY:
			ItalyECRF ItalyDR = new ItalyECRF(EP);
			ItalyDR.BoneAgeDynamic(ActionDone, TabIndex, RecoveringURL);
			ItalyDR = null;
			break;

		case KOREA:
			KoreaECRF KoreaDR = new KoreaECRF(EP);
			KoreaDR.BoneAgeDynamic(ActionDone, TabIndex, RecoveringURL);
			KoreaDR = null;
			break;

		case NORWAY:
			NorwayECRF NorwayDR = new NorwayECRF(EP);
			NorwayDR.BoneAgeDynamic(ActionDone, TabIndex, RecoveringURL);
			NorwayDR = null;
			break;

		case SLOVAKIA:
			SlovakiaECRF SlovakiaDR = new SlovakiaECRF(EP);
			SlovakiaDR.BoneAgeDynamic(ActionDone, TabIndex, RecoveringURL);
			SlovakiaDR = null;
			break;

		case SPAIN:
			SpainECRF SpainDR = new SpainECRF(EP);
			SpainDR.BoneAgeDynamic(ActionDone, TabIndex, RecoveringURL);
			SpainDR = null;
			break;

		case SWEDEN:
			SwedenECRF SwedenDR = new SwedenECRF(EP);
			SwedenDR.BoneAgeDynamic(ActionDone, TabIndex, RecoveringURL);
			SwedenDR = null;
			break;

		case SAUDIARABIA:
			SaudiArabiaECRF SaudiArabiaDR = new SaudiArabiaECRF(EP);
			SaudiArabiaDR.BoneAgeDynamic(ActionDone, TabIndex, RecoveringURL);
			SaudiArabiaDR = null;
			break;

		case SINGAPORE:
			SingaporeECRF SingaporeDR = new SingaporeECRF(EP);
			SingaporeDR.BoneAgeDynamic(ActionDone, TabIndex, RecoveringURL);
			SingaporeDR = null;
			break;

		case UNITEDKINGDOM:
			UnitedKingdomECRF UnitedKingdomDR = new UnitedKingdomECRF(EP);
			UnitedKingdomDR.BoneAgeDynamic(ActionDone, TabIndex, RecoveringURL);
			UnitedKingdomDR = null;
			break;

		case UNITEDARABEMIRATES:
			UnitedArabEmiratesECRF UnitedArabEmiratesDR = new UnitedArabEmiratesECRF(
					EP);
			UnitedArabEmiratesDR.BoneAgeDynamic(ActionDone, TabIndex,
					RecoveringURL);
			UnitedArabEmiratesDR = null;
			break;

		case FRANCE:
			FranceECRF FranceDR = new FranceECRF(EP);
			FranceDR.BoneAgeDynamic(ActionDone, TabIndex, RecoveringURL);
			FranceDR = null;
			break;

		case CANADA:
			CanadaECRF CanadaDR = new CanadaECRF(EP);
			CanadaDR.BoneAgeDynamic(ActionDone, TabIndex, RecoveringURL);
			CanadaDR = null;
			break;
		case MEXICO:
			MexicoECRF MexicoDR = new MexicoECRF(EP);
			MexicoDR.BoneAgeDynamic(ActionDone, TabIndex, RecoveringURL);
			MexicoDR = null;
			break;

		// case CHINA:
		// case ARGENTINA:

		case GERMANY:

			EP.TakeStaticScreenshot("BoneAgeAddEdit");
			// Navigation
			EP.VerifyText("#NavigationTab_BoneAge_LK");
			EP.VerifyText(TabName + "BoneAssessmentDate_ST");
			EP.VerifyMonth(TabName + "BoneAssessmentDate_EB");
			EP.VerifyToolTip(TabName + "BoneAssessmentDate_IM");
			EP.VerifyText(TabName + "BoneMethod_ST");
			EP.CompareDropDownValues(TabName + "BoneMethod_DD");

			// Verify save and cancel buttons
			EP.CompareText("#General_AddRecordSave_BN", EP.LangData("Save"),
					EP.GetText("#General_AddRecordSave_BN"));
			EP.CompareText("#General_AddRecordCancel_BN",
					EP.LangData("Cancel"),
					EP.GetText("#General_AddRecordCancel_BN"));

			EP.VerifyPageLayout();
			if (Env.GetBrowserUsed().startsWith("SA")
					|| Env.GetBrowserUsed().startsWith("OP")) {
				EP.NavigateTo(RecoveringURL);
				EP.WaitForPageload();
				EP.ShowTab(TabIndex);
				EP.Btn_Click("#General_First_BN");
			}

			// Verify the Dynamic Strings
			ValueSelected = EP.RandomValue(1, 4);
			EP.DrpDwn_SelectIndex(TabName + "BoneMethod_DD", ValueSelected);
			// ValueSelected = ValueSelected+1;
			EP.WaitForPageload();
			EP.CompareText(
					TabName + "BoneBoneAgeYearsMonth_ST",
					EP.ORData(TabName + "BoneBoneAgeYearsMonth_ST"),
					EP.GetText(EP.ORXPath(TabName + "BoneBoneAgeYearsMonth_ST")
							+ "[" + ValueSelected + "]")); // Since Xpath
															// changes based on
															// the value
															// selected
			EditBoxXpath = EP.ORXPath(TabName + "BoneBoneAgeYear_EB") + "["
					+ ValueSelected + "]";
			// Incorrect Format
			EP.Edit_SetValue(EditBoxXpath, IncorrectFormat);
			EP.Btn_Click("#General_AddRecordSave_BN");
			EP.TakeStaticScreenshot("IncorrectFormatError");
			EP.CompareText("#General_VisitsErrMsg_DT",
					EP.LangData("OutOfRangeError"),
					EP.GetText("#General_VisitsErrMsg_DT"));
			EP.CompareText(TabName + "BoneBoneAgeYearErrMsg_DT",
					EP.LangData("IncorrectFormat"),
					EP.GetText(TabName + "BoneBoneAgeYearErrMsg_DT"));
			// EP.CompareText(TabName+"BoneBoneAgeMonthErrMsg_DT",
			// EP.LangData("IncorrectFormat"),
			// EP.GetText(TabName+"BoneBoneAgeYearErrMsg_DT"));
			EP.ClearStaticScreenshot();

			// Lesser than
			// No error message

			// //Sucess message
			// EP.DrpDwn_SelectIndex(TabName+"BoneMethod_DD", 0);
			// EP.Btn_Click("#General_AddRecordSave_BN");
			//
			//
			// //Status - Partial
			// VerifyChapterStatus(ActionDone, TabIndex,
			// EP.LangData("Partial"));
			//
			// //Check Chapter Status - Complete
			// EP.Btn_Click("#General_Edit_IM");
			// EP.WaitForPageload();
			ValueSelected = EP.RandomValue(1, 6);
			EditBoxXpath = EP.ORXPath(TabName + "BoneBoneAgeYear_EB") + "["
					+ ValueSelected + "]";
			EP.DrpDwn_SelectIndex(TabName + "BoneMethod_DD", ValueSelected);
			if (ValueSelected <= 4) {
				EP.Edit_SetValue(EditBoxXpath, "5, 3");
			}

			EP.Btn_Click("#General_AddRecordSave_BN"); // click save button
			VerifySuccessMessage(EP.LangData("BoneAge"));
			VerifyChapterStatus(ActionDone, TabIndex, EP.LangData("Complete"));

			// Tooltip for Edit and Delete
			EP.VerifyToolTip("#General_Edit_IM");
			EP.VerifyToolTip("#General_Delete_IM");

			// Verify tooltip and values in the table
			CheckTableText(TabName + "BoneAge_TE", 1, 1, 1,
					EP.LangData("AssessmentDate"), EP.MonthShortAll());
			CheckTableText(TabName + "BoneAge_TE", 1, 2, 1,
					EP.LangData("Method"), EP.ORData(TabName + "BoneMethod_DD"));
			if (ValueSelected <= 4) {
				CheckTableText(TabName + "BoneAge_TE", 1, 2, 2,
						EP.LangData("BoneAgeYearsMonthsEx"), "");
				// CheckTableText(TabName+"BoneAge_TE", 1, 2, 3,
				// EP.LangData("BoneAgeBracketsYearsMonths"), "");
			} else {
				CheckTableText(TabName + "BoneAge_TE", 1, 2, 1,
						EP.LangData("Method"),
						EP.ORData("#AddVisit_BoneAgeCol2_DT"));

			}

			// Delete msg validation (Should be done only for VisitsRecords)
			if (EP.CompareText(ActionDone, "VisitsRecord")) {
				DeleteRecord("#General_Delete_IM", false);
				VerifyDeleteMessage(EP.LangData("BoneAge"));
			}
			break;

		default:
		}
	}

	public void IGF1Static(String ActionDone, String TabIndex) {

		String TabName = AppendHash(ActionDone);
		if (("France;Sweden;Korea").contains(Env.CountryUsed)) {
			VerifyChapterName(ActionDone, TabIndex,
					EP.LangData("BiochemistryIGF1"));// Verify chapter name

		} else {
			VerifyChapterName(ActionDone, TabIndex, EP.LangData("IGF1"));
		}
		if (EP.CompareText(ActionDone, "AddVisit")) {
			VerifyChapterStatus(ActionDone, TabIndex, EP.LangData("NoData"));
		} else {
			VerifyChapterStatus(ActionDone, TabIndex,
					EP.ORData("#General_ChapterStatus_ST"));
		}

		switch (switchCountryName) {

		case AUSTRALIA:
			AustraliaECRF AustraliaDR = new AustraliaECRF(EP);
			AustraliaDR.IGF1Static(ActionDone, TabIndex);
			AustraliaDR = null;
			break;

		case AUSTRIA:
			AustriaECRF AustriaDR = new AustriaECRF(EP);
			AustriaDR.IGF1Static(ActionDone, TabIndex);
			AustriaDR = null;
			break;

		case CZECHREPUBLIC:
			CzechRepublicECRF CzechDR = new CzechRepublicECRF(EP);
			CzechDR.IGF1Static(ActionDone, TabIndex);
			CzechDR = null;
			break;

		case FINLAND:
			FinlandECRF FinlandDR = new FinlandECRF(EP);
			FinlandDR.IGF1Static(ActionDone, TabIndex);
			FinlandDR = null;
			break;

		case COLOMBIA:
			ColombiaECRF ColombiaDR = new ColombiaECRF(EP);
			ColombiaDR.IGF1Static(ActionDone, TabIndex);
			ColombiaDR = null;
			break;

		case INDONESIA:
			IndonesiaECRF IndonesiaDR = new IndonesiaECRF(EP);
			IndonesiaDR.IGF1Static(ActionDone, TabIndex);
			IndonesiaDR = null;
			break;

		case TAIWAN:
			TaiwanECRF TaiwanDR = new TaiwanECRF(EP);
			TaiwanDR.IGF1Static(ActionDone, TabIndex);
			TaiwanDR = null;
			break;

		case ARGENTINA:
			ArgentinaECRF ArgentinaDR = new ArgentinaECRF(EP);
			ArgentinaDR.IGF1Static(ActionDone, TabIndex);
			ArgentinaDR = null;
			break;

		case GREECE:
			GreeceECRF GreeceDR = new GreeceECRF(EP);
			GreeceDR.IGF1Static(ActionDone, TabIndex);
			GreeceDR = null;
			break;

		case HUNGARY:
			HungaryECRF HungaryDR = new HungaryECRF(EP);
			HungaryDR.IGF1Static(ActionDone, TabIndex);
			HungaryDR = null;
			break;

		case ICELAND:
			IcelandECRF IcelandDR = new IcelandECRF(EP);
			IcelandDR.IGF1Static(ActionDone, TabIndex);
			IcelandDR = null;
			break;

		case ITALY:
			ItalyECRF ItalyDR = new ItalyECRF(EP);
			ItalyDR.IGF1Static(ActionDone, TabIndex);
			ItalyDR = null;
			break;

		case KOREA:
			KoreaECRF KoreaDR = new KoreaECRF(EP);
			KoreaDR.IGF1Static(ActionDone, TabIndex);
			KoreaDR = null;
			break;

		case NORWAY:
			NorwayECRF NorwayDR = new NorwayECRF(EP);
			NorwayDR.IGF1Static(ActionDone, TabIndex);
			NorwayDR = null;
			break;

		case SLOVAKIA:
			SlovakiaECRF SlovakiaDR = new SlovakiaECRF(EP);
			SlovakiaDR.IGF1Static(ActionDone, TabIndex);
			SlovakiaDR = null;
			break;

		case SPAIN:
			SpainECRF SpainDR = new SpainECRF(EP);
			SpainDR.IGF1Static(ActionDone, TabIndex);
			SpainDR = null;
			break;

		case SINGAPORE:
			SingaporeECRF SingaporeDR = new SingaporeECRF(EP);
			SingaporeDR.IGF1Static(ActionDone, TabIndex);
			SingaporeDR = null;
			break;

		case SWEDEN:
			SwedenECRF SwedenDR = new SwedenECRF(EP);
			SwedenDR.IGF1Static(ActionDone, TabIndex);
			SwedenDR = null;
			break;
		case SAUDIARABIA:
			SaudiArabiaECRF SaudiArabiaDR = new SaudiArabiaECRF(EP);
			SaudiArabiaDR.IGF1Static(ActionDone, TabIndex);
			SaudiArabiaDR = null;
			break;

		case UNITEDKINGDOM:
			UnitedKingdomECRF UnitedKingdomDR = new UnitedKingdomECRF(EP);
			UnitedKingdomDR.IGF1Static(ActionDone, TabIndex);
			UnitedKingdomDR = null;
			break;

		case UNITEDARABEMIRATES:
			UnitedArabEmiratesECRF UnitedArabEmiratesDR = new UnitedArabEmiratesECRF(
					EP);
			UnitedArabEmiratesDR.IGF1Static(ActionDone, TabIndex);
			UnitedArabEmiratesDR = null;
			break;

		case CANADA:
			CanadaECRF CanadaDR = new CanadaECRF(EP);
			CanadaDR.IGF1Static(ActionDone, TabIndex);
			CanadaDR = null;
			break;

		case MEXICO:
			MexicoECRF MexicoDR = new MexicoECRF(EP);
			MexicoDR.IGF1Static(ActionDone, TabIndex);
			MexicoDR = null;
			break;

		// case CHINA:
		// case ARGENTINA:

		case GERMANY:

			VerifyChapterHeading(ActionDone, TabIndex, 1,
					EP.LangData("IGF1StatusBeforeGHTreatment"));
			VerifyChapterHeading(ActionDone, TabIndex, 2,
					EP.LangData("IGF1Status"));
			EP.VerifyTableHeader(TabName + "IGF1Status_TE");
			if (!EP.CompareText(ActionDone, "RecordOverview")) {
				EP.VerifyText(TabName + "IGF1StatusDateBeforeGH_ST");
				EP.VerifyText(TabName + "IGF1StatusBeforeGH_ST");
				EP.VerifyText(TabName + "IGF1Level_ST");
				EP.VerifyText(TabName + "IGF1StatusUnit_ST");
				EP.VerifyText(TabName + "LaboratoryValueRange_ST");
			} else {
				EP.VerifyText(TabName + "IGF1StatusDateBeforeGHAT_ST");
				EP.VerifyText(TabName + "IGF1StatusBeforeGHAT_ST");
				EP.VerifyText(TabName + "IGF1LevelAT_ST");
				EP.VerifyText(TabName + "IGF1StatusUnitAT_ST");
				EP.VerifyText(TabName + "LaboratoryValueRangeAT_ST");
				EP.VerifyText(TabName + "LaboratoryValueRangeMinimumCA_ST");
				EP.VerifyText(TabName + "LaboratoryValueRangeMaximumCA_ST");

				// verify tooltip and values in the table
				CheckTableText(TabName + "IGF1Status_TE", 1, 1, 1,
						EP.LangData("IGF1StatusDate"), EP.MonthShortAll());
				CheckTableText(TabName + "IGF1Status_TE", 1, 2, 1,
						EP.LangData("IGF1Status"),
						EP.ORData("#AddVisit_AddIGF1Status_DD"));
				CheckTableText(TabName + "IGF1Status_TE", 1, 3, 1,
						EP.LangData("IGF1Level"), "");
				CheckTableText(TabName + "IGF1Status_TE", 1, 4, 1,
						EP.LangData("IGF1StatusUnit"),
						EP.ORData("#AddVisit_AddIGF1StatusUnit_DD"));
				CheckTableText(TabName + "IGF1Status_TE", 1, 5, 1,
						EP.LangData("LaboratoryValueRange"), "");

			}

			break;

		case FRANCE:
			FranceECRF FranceDR = new FranceECRF(EP);
			FranceDR.IGF1Static(ActionDone, TabIndex);
			FranceDR = null;
			break;

		default:

		}
	}

	public void IGF1Dynamic(String ActionDone, String TabIndex,
			String RecoveringURL) {
		String TabName = AppendHash(ActionDone);
		String ActionPerformed;

		EP.CompareText("#General_First_BN", EP.LangData("Add"),
				EP.GetText("#General_First_BN"));
		EP.CompareText("#AddVisit_AssessmentNotDone_BN",
				EP.LangData("AssessmentNotDone"),
				EP.GetText("#AddVisit_AssessmentNotDone_BN")); // Assessment not
																// done Button

		// Navigate to Add Page
		if (EP.ObjectExists("#General_Delete_IM")) {
			DeleteRecord("#General_Delete_IM", false);
			if (EP.ObjectExists("#General_Delete_IM")) { // For Biochemistry IGF
															// two Web table
															// will be avilable,
															// So deleting both
															// records.
				DeleteRecord("#General_Delete_IM", false);
			}
			EP.Btn_Click("#General_First_BN");
		} else {
			EP.Btn_Click("#General_First_BN");
			EP.WaitForPageload();
		}

		switch (switchCountryName) {

		case AUSTRALIA:
			AustraliaECRF AustraliaDR = new AustraliaECRF(EP);
			AustraliaDR.IGF1Dynamic(ActionDone, TabIndex, RecoveringURL);
			AustraliaDR = null;
			break;

		case AUSTRIA:
			AustriaECRF AustriaDR = new AustriaECRF(EP);
			AustriaDR.IGF1Dynamic(ActionDone, TabIndex, RecoveringURL);
			AustriaDR = null;
			break;

		case COLOMBIA:
			ColombiaECRF ColombiaDR = new ColombiaECRF(EP);
			ColombiaDR.IGF1Dynamic(ActionDone, TabIndex, RecoveringURL);
			ColombiaDR = null;
			break;

		case INDONESIA:
			IndonesiaECRF IndonesiaDR = new IndonesiaECRF(EP);
			IndonesiaDR.IGF1Dynamic(ActionDone, TabIndex, RecoveringURL);
			IndonesiaDR = null;
			break;

		case CZECHREPUBLIC:
			CzechRepublicECRF CzechDR = new CzechRepublicECRF(EP);
			CzechDR.IGF1Dynamic(ActionDone, TabIndex, RecoveringURL);
			CzechDR = null;
			break;

		case FINLAND:
			FinlandECRF FinlandDR = new FinlandECRF(EP);
			FinlandDR.IGF1Dynamic(ActionDone, TabIndex, RecoveringURL);
			FinlandDR = null;
			break;

		case ARGENTINA:
			ArgentinaECRF ArgentinaDR = new ArgentinaECRF(EP);
			ArgentinaDR.IGF1Dynamic(ActionDone, TabIndex, RecoveringURL);
			ArgentinaDR = null;
			break;

		case TAIWAN:
			TaiwanECRF TaiwanDR = new TaiwanECRF(EP);
			TaiwanDR.IGF1Dynamic(ActionDone, TabIndex, RecoveringURL);
			TaiwanDR = null;
			break;

		case GREECE:
			GreeceECRF GreeceDR = new GreeceECRF(EP);
			GreeceDR.IGF1Dynamic(ActionDone, TabIndex, RecoveringURL);
			GreeceDR = null;
			break;

		case HUNGARY:
			HungaryECRF HungaryDR = new HungaryECRF(EP);
			HungaryDR.IGF1Dynamic(ActionDone, TabIndex, RecoveringURL);
			HungaryDR = null;
			break;

		case ICELAND:
			IcelandECRF IcelandDR = new IcelandECRF(EP);
			IcelandDR.IGF1Dynamic(ActionDone, TabIndex, RecoveringURL);
			IcelandDR = null;
			break;

		case ITALY:
			ItalyECRF ItalyDR = new ItalyECRF(EP);
			ItalyDR.IGF1Dynamic(ActionDone, TabIndex, RecoveringURL);
			ItalyDR = null;
			break;

		case KOREA:
			KoreaECRF KoreaDR = new KoreaECRF(EP);
			KoreaDR.IGF1Dynamic(ActionDone, TabIndex, RecoveringURL);
			KoreaDR = null;
			break;

		case NORWAY:
			NorwayECRF NorwayDR = new NorwayECRF(EP);
			NorwayDR.IGF1Dynamic(ActionDone, TabIndex, RecoveringURL);
			NorwayDR = null;
			break;

		case SLOVAKIA:
			SlovakiaECRF SlovakiaDR = new SlovakiaECRF(EP);
			SlovakiaDR.IGF1Dynamic(ActionDone, TabIndex, RecoveringURL);
			SlovakiaDR = null;
			break;

		case SPAIN:
			SpainECRF SpainDR = new SpainECRF(EP);
			SpainDR.IGF1Dynamic(ActionDone, TabIndex, RecoveringURL);
			SpainDR = null;
			break;

		case SINGAPORE:
			SingaporeECRF SingaporeDR = new SingaporeECRF(EP);
			SingaporeDR.IGF1Dynamic(ActionDone, TabIndex, RecoveringURL);
			SingaporeDR = null;
			break;

		case SAUDIARABIA:
			SaudiArabiaECRF SaudiArabiaDR = new SaudiArabiaECRF(EP);
			SaudiArabiaDR.IGF1Dynamic(ActionDone, TabIndex, RecoveringURL);
			SaudiArabiaDR = null;
			break;

		case SWEDEN:
			SwedenECRF SwedenDR = new SwedenECRF(EP);
			SwedenDR.IGF1Dynamic(ActionDone, TabIndex, RecoveringURL);
			SwedenDR = null;
			break;

		case UNITEDKINGDOM:
			UnitedKingdomECRF UnitedKingdomDR = new UnitedKingdomECRF(EP);
			UnitedKingdomDR.IGF1Dynamic(ActionDone, TabIndex, RecoveringURL);
			UnitedKingdomDR = null;
			break;

		case UNITEDARABEMIRATES:
			UnitedArabEmiratesECRF UnitedArabEmiratesDR = new UnitedArabEmiratesECRF(
					EP);
			UnitedArabEmiratesDR.IGF1Dynamic(ActionDone, TabIndex,
					RecoveringURL);
			UnitedArabEmiratesDR = null;
			break;

		case CANADA:
			CanadaECRF CanadaDR = new CanadaECRF(EP);
			CanadaDR.IGF1Dynamic(ActionDone, TabIndex, RecoveringURL);
			CanadaDR = null;
			break;
		case MEXICO:
			MexicoECRF MexicoDR = new MexicoECRF(EP);
			MexicoDR.IGF1Dynamic(ActionDone, TabIndex, RecoveringURL);
			MexicoDR = null;
			break;
		// case CHINA:
		// case ARGENTINA:

		case GERMANY:

			// EP.VerifyToolTip(TabName+"IGF1StatusDateBeforeGH_IM");
			// EP.CompareDropDownValues(TabName+"IGF1StatusBeforeGH_DD");
			// EP.CompareDropDownValues(TabName+"IGF1StatusUnit_DD");
			// //EP.CompareText("#General_First_BN",
			// EP.LangData("PopulateValues"), EP.GetText("#General_First_BN"));
			// EP.CompareText("#General_Second_BN", EP.LangData("Save"),
			// EP.GetText("#General_First_BN"));
			// EP.CompareText("#General_Third_BN", EP.LangData("Cancel"),
			// EP.GetText("#General_Second_BN"));
			EP.CompareText("#General_First_BN", EP.LangData("Add"),
					EP.GetText("#General_First_BN"));

			// //Invalid Format
			// EP.DrpDwn_SelectIndex(TabName+"IGF1StatusBeforeGH_DD", 0);
			// EP.Edit_SetValue(TabName+"IGF1Level_EB", IncorrectFormat);
			// //JavaScript was changed to normal
			// EP.Edit_SetValue(TabName+"IGF1LaboratoryValueRangeMax_EB",
			// IncorrectFormat);//JavaScript was changed to normal
			// EP.Edit_SetValue(TabName+"IGF1LaboratoryValueRangeMin_EB",
			// IncorrectFormat);//JavaScript was changed to normal
			// EP.Btn_Click("#General_First_BN");
			//
			// EP.CompareText(TabName+"IGF1ErrMsg_DT",
			// EP.LangData("VerifyIncorrectFormats"),
			// EP.GetText(TabName+"IGF1ErrMsg_DT"));//At the top
			// EP.VerifyText(TabName+"IGF1StatusBeforeErrMsg_ST");
			// EP.CompareText(TabName+"IGF1LevelErrMsg_DT",
			// EP.LangData("IncorrectFormat"),
			// EP.GetText(TabName+"IGF1LevelErrMsg_DT"));
			// EP.CompareText(TabName+"IGF1LaboratoryValueRangeMinErrMsg_DT",
			// EP.LangData("IncorrectFormat"),
			// EP.GetText(TabName+"IGF1LaboratoryValueRangeMinErrMsg_DT"));
			// EP.CompareText(TabName+"IGF1LaboratoryValueRangeMaxErrMsg_DT",
			// EP.LangData("IncorrectFormat"),
			// EP.GetText(TabName+"IGF1LaboratoryValueRangeMaxErrMsg_DT"));
			//
			// //Success Message
			// EP.DrpDwn_SelectIndex(TabName+"IGF1StatusBeforeGH_DD",
			// EP.RandomValue(1, 5));
			// EP.Edit_SetValue(TabName+"IGF1Level_EB", "50");
			// EP.DrpDwn_SelectIndex(TabName+"IGF1StatusUnit_DD",
			// EP.RandomValue(1, 6));
			// EP.Edit_SetValue(TabName+"IGF1LaboratoryValueRangeMax_EB", "50");
			// EP.Edit_SetValue(TabName+"IGF1LaboratoryValueRangeMin_EB", "50");
			// EP.Btn_Click("#General_First_BN");

			// Navigate to Add Page
			if (!EP.ObjectExists("#General_Edit_IM")) { // If the Record is
														// present then Edit it
														// or else create a new
														// record
				ActionPerformed = "#General_First_BN";
				EP.Btn_Click(ActionPerformed); // Click Add button
				EP.WaitForPageload();
				EP.CompareText("#General_FormHeader_ST", EP.LangData("Add"),
						EP.GetText("#General_FormHeader_ST")); // Form Header
			} else {
				ActionPerformed = "#General_Edit_IM";
				EP.Btn_Click(ActionPerformed); // Click Edit button
				EP.WaitForPageload();
				EP.CompareText("#General_FormHeader_ST", EP.LangData("Edit"),
						EP.GetText("#General_FormHeader_ST")); // Form Header
			}
			EP.TakeStaticScreenshot("IGF1AddEdit");

			// Static in Add Page
			EP.VerifyText("#NavigationTab_IGF1_LK"); // Navigation Tab

			EP.VerifyText(TabName + "AddIGF1StatusDate_ST");
			EP.VerifyMonth(TabName + "AddIGF1StatusDate_EB");
			EP.VerifyToolTip(TabName + "AddIGF1StatusDate_IM");
			EP.VerifyText(TabName + "AddIGF1Status_ST");

			EP.CompareDropDownValues(TabName + "AddIGF1Status_DD");
			EP.VerifyText(TabName + "AddIGF1Level_ST");
			EP.VerifyText(TabName + "AddIGF1StatusUnit_ST");
			EP.CompareDropDownValues(TabName + "AddIGF1StatusUnit_DD");
			EP.VerifyText(TabName + "AddIGF1LaboratoryRange_ST");
			EP.CompareText("#General_AddRecordSave_BN", EP.LangData("Save"),
					EP.GetText("#General_AddRecordSave_BN"));
			EP.CompareText("#General_AddRecordCancel_BN",
					EP.LangData("Cancel"),
					EP.GetText("#General_AddRecordCancel_BN"));

			EP.VerifyPageLayout();
			if (Env.GetBrowserUsed().startsWith("SA")
					|| Env.GetBrowserUsed().startsWith("OP")) {
				EP.NavigateTo(RecoveringURL);
				EP.WaitForPageload();
				EP.ShowTab(TabIndex);
				EP.Btn_Click(ActionPerformed);
			}

			// Invalid Format
			EP.DrpDwn_SelectIndex(TabName + "AddIGF1Status_DD", 0);
			EP.Edit_SetValue(TabName + "AddIGF1Level_EB", IncorrectFormat);
			EP.Edit_SetValue(TabName + "AddIGF1LaboratoryValueRangeMin_EB",
					IncorrectFormat);
			EP.Edit_SetValue(TabName + "AddIGF1LaboratoryValueRangeMax_EB",
					IncorrectFormat);
			EP.Btn_Click("#General_AddRecordSave_BN");
			EP.CompareText(TabName + "IGF1ErrMsg_DT",
					EP.LangData("VerifyIncorrectFormats"),
					EP.GetText(TabName + "IGF1ErrMsg_DT"));// At the top
			EP.CompareText(TabName + "AddIGF1StatusErrMsg_DT",
					EP.LangData("FieldIsMandatory"),
					EP.GetText(TabName + "IGF1StatusBeforeErrMsg_ST"));
			EP.CompareText(TabName + "AddIGF1LevelErrMsg_DT",
					EP.LangData("IncorrectFormat"),
					EP.GetText(TabName + "AddIGF1LevelErrMsg_DT"));
			EP.CompareText(
					TabName + "AddIGF1LaboratoryValueRangeMinErrMsg_DT",
					EP.LangData("IncorrectFormat"),
					EP.GetText(TabName
							+ "AddIGF1LaboratoryValueRangeMinErrMsg_DT"));
			EP.CompareText(
					TabName + "AddIGF1LaboratoryValueRangeMaxErrMsg_DT",
					EP.LangData("IncorrectFormat"),
					EP.GetText(TabName
							+ "AddIGF1LaboratoryValueRangeMaxErrMsg_DT"));

			// //Success Message
			// EP.DrpDwn_SelectIndex(TabName+"AddIGF1Status_DD", 0);
			// EP.DrpDwn_SelectIndex(TabName+"AddIGF1StatusUnit_DD", 0);
			// EP.Edit_SetValue(TabName+"AddIGF1Level_EB", "50");
			// EP.Edit_SetValue(TabName+"AddIGF1LaboratoryValueRangeMin_EB",
			// "50");
			// EP.Edit_SetValue(TabName+"AddIGF1LaboratoryValueRangeMax_EB",
			// "50");
			// EP.Btn_Click("#General_AddRecordSave_BN");
			//
			//
			// //Check Chapter Status - Complete
			// EP.Btn_Click("#General_Edit_IM");
			EP.DrpDwn_SelectIndex(TabName + "AddIGF1Status_DD",
					EP.RandomValue(1, 5));
			EP.DrpDwn_SelectIndex(TabName + "AddIGF1StatusUnit_DD",
					EP.RandomValue(1, 6));
			EP.Edit_SetValue(TabName + "AddIGF1Level_EB", "50");
			EP.Edit_SetValue(TabName + "AddIGF1LaboratoryValueRangeMin_EB",
					"50");
			EP.Edit_SetValue(TabName + "AddIGF1LaboratoryValueRangeMax_EB",
					"50");
			EP.Btn_Click("#General_AddRecordSave_BN");
			VerifySuccessMessage(EP.LangData("IGF1"));

			VerifyChapterStatus(ActionDone, TabIndex, EP.LangData("Complete"));

			// Tooltip for Edit and Delete
			EP.VerifyToolTip("#General_Edit_IM");
			EP.VerifyToolTip("#General_Delete_IM");

			// verify tooltip and values in the table
			CheckTableText(TabName + "IGF1Status_TE", 1, 1, 1,
					EP.LangData("IGF1StatusDate"), EP.MonthShortAll());
			CheckTableText(TabName + "IGF1Status_TE", 1, 2, 1,
					EP.LangData("IGF1Status"),
					EP.ORData(TabName + "AddIGF1Status_DD"));
			CheckTableText(TabName + "IGF1Status_TE", 1, 3, 1,
					EP.LangData("IGF1Level"), "");
			CheckTableText(TabName + "IGF1Status_TE", 1, 4, 1,
					EP.LangData("IGF1StatusUnit"),
					EP.ORData(TabName + "AddIGF1StatusUnit_DD"));
			CheckTableText(TabName + "IGF1Status_TE", 1, 5, 1,
					EP.LangData("LaboratoryValueRange"), "");

			if (EP.CompareText(ActionDone, "VisitsRecord")) {
				DeleteRecord("#General_Delete_IM", false);
				VerifyDeleteMessage(EP.LangData("IGF1"));
			}
			break;

		case FRANCE:
			FranceECRF FranceDR = new FranceECRF(EP);
			FranceDR.IGF1Dynamic(ActionDone, TabIndex, RecoveringURL);
			FranceDR = null;
			break;

		default:
		}
	}

	public void ConcomitantMedicationStatic(String ActionDone, String TabIndex) {

		switch (switchCountryName) {

		case SWEDEN:
			SwedenECRF SwedenDR = new SwedenECRF(EP);
			SwedenDR.ConcomitantMedicationStatic(ActionDone, TabIndex);
			SwedenDR = null;
			break;
		default:
		}
	}

	public void ConcomitantMedicationDynamic(String ActionDone,
			String TabIndex, String RecoveringURL) {

		String ActionPerformed;
		EP.CompareText("#General_First_BN", EP.LangData("Add"),
				EP.GetText("#General_First_BN")); // Add Button

		// Navigate to Add Page
		if (!EP.ObjectExists("#General_Edit_IM")) { // If the Record is present
													// then Edit it or else
													// create a new record
			ActionPerformed = "#General_First_BN";
			EP.Btn_Click(ActionPerformed); // Click Add button
			EP.WaitForPageload();
			EP.CompareText("#General_FormHeader_ST", EP.LangData("Add"),
					EP.GetText("#General_FormHeader_ST")); // Form Header
		} else {
			ActionPerformed = "#General_Edit_IM";
			EP.Btn_Click(ActionPerformed); // Click Edit button
			EP.WaitForPageload();
			EP.CompareText("#General_FormHeader_ST", EP.LangData("Edit"),
					EP.GetText("#General_FormHeader_ST")); // Form Header
		}
		switch (switchCountryName) {

		case SWEDEN:
			SwedenECRF SwedenDR = new SwedenECRF(EP);
			SwedenDR.ConcomitantMedicationDynamic(ActionDone, TabIndex,
					RecoveringURL);
			SwedenDR = null;
			break;
		default:

		}
	}

	public void TannerStageDynamicFemale(String ActionDone, String TabIndex,
			String RecoveringURL) {
		String TabName = AppendHash(ActionDone);
		switch (switchCountryName) {

		case AUSTRALIA:
			AustraliaECRF AustraliaDR = new AustraliaECRF(EP);
			AustraliaDR.TannerStageDynamicFemale(ActionDone, TabIndex,
					RecoveringURL);
			AustraliaDR = null;
			break;

		case AUSTRIA:
			AustriaECRF AustriaDR = new AustriaECRF(EP);
			AustriaDR.TannerStageDynamicFemale(ActionDone, TabIndex,
					RecoveringURL);
			AustriaDR = null;
			break;

		case CZECHREPUBLIC:
			CzechRepublicECRF CzechDR = new CzechRepublicECRF(EP);
			CzechDR.TannerStageDynamicFemale(ActionDone, TabIndex,
					RecoveringURL);
			CzechDR = null;
			break;

		case FINLAND:
			FinlandECRF FinlandDR = new FinlandECRF(EP);
			FinlandDR.TannerStageDynamicFemale(ActionDone, TabIndex,
					RecoveringURL);
			FinlandDR = null;
			break;

		case ARGENTINA:
			ArgentinaECRF ArgentinaDR = new ArgentinaECRF(EP);
			ArgentinaDR.TannerStageDynamicFemale(ActionDone, TabIndex,
					RecoveringURL);
			ArgentinaDR = null;
			break;

		case COLOMBIA:
			ColombiaECRF ColombiaDR = new ColombiaECRF(EP);
			ColombiaDR.TannerStageDynamicFemale(ActionDone, TabIndex,
					RecoveringURL);
			ColombiaDR = null;
			break;

		case TAIWAN:
			TaiwanECRF TaiwanDR = new TaiwanECRF(EP);
			TaiwanDR.TannerStageDynamicFemale(ActionDone, TabIndex,
					RecoveringURL);
			TaiwanDR = null;
			break;

		case INDONESIA:
			IndonesiaECRF IndonesiaDR = new IndonesiaECRF(EP);
			IndonesiaDR.TannerStageDynamicFemale(ActionDone, TabIndex,
					RecoveringURL);
			IndonesiaDR = null;
			break;

		case GREECE:
			GreeceECRF GreeceDR = new GreeceECRF(EP);
			GreeceDR.TannerStageDynamicFemale(ActionDone, TabIndex,
					RecoveringURL);
			GreeceDR = null;
			break;

		case SAUDIARABIA:
			SaudiArabiaECRF SaudiArabiaDR = new SaudiArabiaECRF(EP);
			SaudiArabiaDR.TannerStageDynamicFemale(ActionDone, TabIndex,
					RecoveringURL);
			SaudiArabiaDR = null;
			break;
		case HUNGARY:
			HungaryECRF HungaryDR = new HungaryECRF(EP);
			HungaryDR.TannerStageDynamicFemale(ActionDone, TabIndex,
					RecoveringURL);
			HungaryDR = null;
			break;

		case ICELAND:
			IcelandECRF IcelandDR = new IcelandECRF(EP);
			IcelandDR.TannerStageDynamicFemale(ActionDone, TabIndex,
					RecoveringURL);
			IcelandDR = null;
			break;

		case ITALY:
			ItalyECRF ItalyDR = new ItalyECRF(EP);
			ItalyDR.TannerStageDynamicFemale(ActionDone, TabIndex,
					RecoveringURL);
			ItalyDR = null;
			break;

		case KOREA:
			KoreaECRF KoreaDR = new KoreaECRF(EP);
			KoreaDR.TannerStageDynamicFemale(ActionDone, TabIndex,
					RecoveringURL);
			KoreaDR = null;
			break;

		case NORWAY:
			NorwayECRF NorwayDR = new NorwayECRF(EP);
			NorwayDR.TannerStageDynamicFemale(ActionDone, TabIndex,
					RecoveringURL);
			NorwayDR = null;
			break;

		case SLOVAKIA:
			SlovakiaECRF SlovakiaDR = new SlovakiaECRF(EP);
			SlovakiaDR.TannerStageDynamicFemale(ActionDone, TabIndex,
					RecoveringURL);
			SlovakiaDR = null;
			break;

		case SPAIN:
			SpainECRF SpainDR = new SpainECRF(EP);
			SpainDR.TannerStageDynamicFemale(ActionDone, TabIndex,
					RecoveringURL);
			SpainDR = null;
			break;

		case SINGAPORE:
			SingaporeECRF SingaporeDR = new SingaporeECRF(EP);
			SingaporeDR.TannerStageDynamicFemale(ActionDone, TabIndex,
					RecoveringURL);
			SingaporeDR = null;
			break;

		case SWEDEN:
			SwedenECRF SwedenDR = new SwedenECRF(EP);
			SwedenDR.TannerStageDynamicFemale(ActionDone, TabIndex,
					RecoveringURL);
			SwedenDR = null;
			break;

		case UNITEDKINGDOM:
			UnitedKingdomECRF UnitedKingdomDR = new UnitedKingdomECRF(EP);
			UnitedKingdomDR.TannerStageDynamicFemale(ActionDone, TabIndex,
					RecoveringURL);
			UnitedKingdomDR = null;
			break;

		case UNITEDARABEMIRATES:
			UnitedArabEmiratesECRF UnitedArabEmiratesDR = new UnitedArabEmiratesECRF(
					EP);
			UnitedArabEmiratesDR.TannerStageDynamicFemale(ActionDone, TabIndex,
					RecoveringURL);
			UnitedArabEmiratesDR = null;
			break;

		case FRANCE:
			FranceECRF FranceDR = new FranceECRF(EP);
			FranceDR.TannerStageDynamicFemale(ActionDone, TabIndex,
					RecoveringURL);
			FranceDR = null;
			break;

		// case CHINA:
		// case ARGENTINA:
		case MEXICO:
			MexicoECRF MexicoDR = new MexicoECRF(EP);
			MexicoDR.TannerStageDynamicFemale(ActionDone, TabIndex,
					RecoveringURL);
			MexicoDR = null;
			break;
		case GERMANY:

			EP.VerifyText("#AddVisit_TannerStageMenarcheGR_ST");
			EP.Rbtn_SetOption(TabName + "TannerStageMenarcheYesAT_RB", "ON");
			EP.VerifyText(TabName + "TannerStageMenarcheYesAT_ST");
			EP.VerifyText(TabName + "TannerStageMenarcheNoAT_ST");
			EP.VerifyText(TabName + "TannerStageMenarcheNotAvailableAT_ST");
			EP.VerifyText(TabName + "TannerMenarcheDateCZ_ST");
			EP.VerifyTextContained(TabName + "TannerStageMenarcheYear_ST");
			EP.VerifyTextContained(TabName + "TannerStageMenarcheMonth_ST");

			// Dynamic
			EP.Rbtn_SetOption(TabName + "TannerStageMenarcheYesAT_RB", "ON");
			EP.Edit_SetValue(TabName + "TannerStageMenarcheYearAT_EB",
					IncorrectFormat);
			EP.Edit_SetValue(TabName + "TannerStageMenarcheMonthAT_EB",
					IncorrectFormat);
			EP.Btn_Click("#General_AddRecordSave_BN");
			EP.CompareText("#General_VisitsErrMsg_DT",
					EP.LangData("VerifyIncorrectFormats"),
					EP.GetText("#General_VisitsErrMsg_DT"));
			EP.VerifyText(TabName + "TannerStageMenarcheYearErrMsgAT_DT");
			EP.VerifyText(TabName + "TannerStageMenarcheMonthErrMsgAT_DT");
			break;

		case CANADA:
			CanadaECRF CanadaDR = new CanadaECRF(EP);
			CanadaDR.TannerStageDynamicFemale(ActionDone, TabIndex,
					RecoveringURL);
			CanadaDR = null;
			break;

		default:
		}
	}

	public void AuxologicalDataVisitsAdd(boolean IsValid, String TabIndex) {
		// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*AUXOLOGICAL
		// DATA(VISITS)*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
		EP.ShowTab(TabIndex);
		String[] InputValues;
		switch (switchCountryName) {
		case UNITEDKINGDOM:
		case SWEDEN:
			if (IsValid) {
				InputValues = new String[] { "55", "55", "55", "55", "55", "55" };
			} else {
				InputValues = new String[] { "200", "200", "200", "200", "200",
						"200" };
			}

			if (EP.ObjectExists("#General_Delete_IM")) {
				DeleteRecord("#General_Delete_IM", false);
			}
			EP.Btn_Click("#General_First_BN"); // Click Add button
			EP.DrpDwn_SelectIndex("#AddVisit_AuxologicalSitting_DD", 1);
			EP.DrpDwn_SelectIndex("#AddVisit_AuxologicalWaist_DD", 1);
			EP.DrpDwn_SelectIndex("#AddVisit_AuxologicalHipMeasrnt_DD", 1);
			EP.Edit_SetValue("#AddVisit_AuxologicalHeight_EB", InputValues[0]);
			EP.HideCursor();
			EP.WaitForPageload();
			EP.Edit_SetValue("#AddVisit_AuxologicalWeight_EB", InputValues[1]);
			EP.Edit_SetValue("#AddVisit_AuxologicalSitting_EB", InputValues[2]);
			EP.Edit_SetValue("#AddVisit_AuxologicalWaist_EB", InputValues[3]);
			EP.WaitForPageload();
			EP.Wait(5);
			EP.Edit_SetValueByJavaScript("#AddVisit_AuxologicalHip_EB",
					InputValues[4]);
			EP.WaitForPageload();
			// EP.Edit_SetValue("#AddVisit_AuxologicalHipWaist_EB",
			// InputValues[5]);//not there for sweden
			if (IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN"); // click save button
			} else {
				EP.Btn_Click("#General_AddRecordSave_BN");
				EP.Btn_Click("#General_AddRecordSave_BN");
			}
			break;

		case AUSTRIA:
		case FRANCE:
		case SPAIN:
		case ITALY:
		case CANADA:
		case CZECHREPUBLIC:
		case SINGAPORE:
		case SAUDIARABIA:
		case UNITEDARABEMIRATES:
		case ARGENTINA:
		case COLOMBIA:
		case TAIWAN:
		case INDONESIA:
		case KOREA:
			EP.ShowTab(TabIndex);
			if (IsValid) {
				InputValues = new String[] { "55", "55" };
			} else {
				InputValues = new String[] { "200", "200" };
			}
			if (EP.ObjectExists("#General_Delete_IM")) {
				DeleteRecord("#General_Delete_IM", false);
			}
			EP.Btn_Click("#General_First_BN"); // Click Add button
			EP.Edit_SetValue("#AddVisit_AuxologicalHeight_EB", InputValues[0]
					+ Keys.TAB);
			EP.HideCursor();
			EP.WaitForPageload();
			EP.Edit_SetValue("#AddVisit_AuxologicalWeight_EB", InputValues[1]);
			EP.Edit_SetValue("#AddVisit_AuxologicalHeight_EB", InputValues[0]
					+ Keys.TAB);
			EP.HideCursor();
			EP.WaitForPageload();
			EP.Edit_SetValue("#AddVisit_AuxologicalWeight_EB", InputValues[1]);
			EP.Btn_Click("#General_AddRecordSave_BN");
			if (EP.ObjectExists("#General_AddRecordSave_BN")) {
				EP.Btn_Click("#General_AddRecordSave_BN");
			}
			if (!IsValid) {
				if (EP.ObjectExists("#General_AddRecordSave_BN")) {
					EP.Btn_Click("#General_AddRecordSave_BN");
				}
			}
			break;
		case AUSTRALIA:
			if (IsValid) {
				InputValues = new String[] { "55", "55" };
			} else {
				InputValues = new String[] { "200", "200" };
			}
			if (EP.ObjectExists("#General_Delete_IM")) {
				DeleteRecord("#General_Delete_IM", false);
			}
			EP.Btn_Click("#General_First_BN"); // Click Add button
			EP.Edit_SetValue("#AddVisit_AuxologicalHeight_EB", InputValues[0]
					+ Keys.TAB);
			EP.HideCursor();
			EP.WaitForPageload();
			EP.Edit_SetValue("#AddVisit_AuxologicalWeight_EB", InputValues[1]);
			EP.Edit_SetValue("#AddVisit_AuxologicalHeight_EB", InputValues[0]
					+ Keys.TAB);
			EP.HideCursor();
			EP.WaitForPageload();
			EP.Edit_SetValue("#AddVisit_AuxologicalWeight_EB", InputValues[1]);
			EP.Btn_Click("#General_AddRecordSave_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN");
			}
			break;
		case ICELAND:
			if (IsValid) {
				InputValues = new String[] { "55", "55" };
			} else {
				InputValues = new String[] { "200", "200" };
			}
			if (EP.ObjectExists("#General_Delete_IM")) {
				DeleteRecord("#General_Delete_IM", false);
			}
			EP.Btn_Click("#General_First_BN"); // Click Add button
			EP.Edit_SetValue("#AddVisit_AuxologicalHeight_EB", InputValues[0]
					+ Keys.TAB);
			EP.HideCursor();
			EP.WaitForPageload();
			EP.Edit_SetValue("#AddVisit_AuxologicalWeight_EB", InputValues[1]);
			EP.Edit_SetValue("#AddVisit_AuxologicalHeight_EB", InputValues[0]
					+ Keys.TAB);
			EP.HideCursor();
			EP.WaitForPageload();
			EP.Edit_SetValue("#AddVisit_AuxologicalWeight_EB", InputValues[1]);
			EP.Btn_Click("#General_AddRecordSave_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN");
			}
			break;
		case FINLAND:
			if (IsValid) {
				InputValues = new String[] { "55", "55" };
			} else {
				InputValues = new String[] { "200", "200" };
			}
			if (EP.ObjectExists("#General_Delete_IM")) {
				DeleteRecord("#General_Delete_IM", false);
			}
			EP.Btn_Click("#General_First_BN"); // Click Add button
			EP.Edit_SetValue("#AddVisit_AuxologicalHeight_EB", InputValues[0]
					+ Keys.TAB);
			EP.HideCursor();
			EP.WaitForPageload();
			EP.Edit_SetValue("#AddVisit_AuxologicalWeight_EB", InputValues[1]);
			EP.Edit_SetValue("#AddVisit_AuxologicalHeight_EB", InputValues[0]
					+ Keys.TAB);
			EP.HideCursor();
			EP.WaitForPageload();
			EP.Edit_SetValue("#AddVisit_AuxologicalWeight_EB", InputValues[1]);
			EP.Btn_Click("#General_AddRecordSave_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN");
			}
		case NORWAY:
		case GREECE:
		case HUNGARY:
		case MEXICO:
		case SLOVAKIA:
		case GERMANY:
			// case CHINA:
			// case ARGENTINA:

			if (IsValid) {
				InputValues = new String[] { "55", "55" };
			} else {
				InputValues = new String[] { "200", "200" };
			}
			if (EP.ObjectExists("#General_Delete_IM")) {
				DeleteRecord("#General_Delete_IM", false);
			}
			EP.Btn_Click("#General_First_BN"); // Click Add button
			EP.Edit_SetValue("#AddVisit_AuxologicalHeight_EB", InputValues[0]
					+ Keys.TAB);
			EP.HideCursor();
			EP.WaitForPageload();
			EP.Edit_SetValue("#AddVisit_AuxologicalWeight_EB", InputValues[1]);
			EP.Edit_SetValue("#AddVisit_AuxologicalHeight_EB", InputValues[0]
					+ Keys.TAB);
			EP.HideCursor();
			EP.WaitForPageload();
			EP.Edit_SetValue("#AddVisit_AuxologicalWeight_EB", InputValues[1]);
			EP.Btn_Click("#General_AddRecordSave_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN");
			}
			break;
		default:
		}
		VerifySuccessMessage(EP.LangData("AuxologicalDataVisits"));
	}

	public void SocioEconomicVisitsAdd(boolean IsValid, String TabIndex) {
		// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*SOCIOECONOMIC*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
		EP.ShowTab(TabIndex);
		String[] InputValues;
		switch (switchCountryName) {
		case AUSTRIA:// No Invalidata
			if (EP.ObjectExists("#General_Delete_IM")) {
				DeleteRecord("#General_Delete_IM", false);
			}
			EP.Btn_Click("#General_First_BN");
			EP.DrpDwn_SelectIndex("#AddVisit_SocioParentIT_DD",
					EP.RandomValue(1, 5));
			EP.ChkBx_SetOption(
					EP.ORXPath("#AddVisit_SocioeconomicParentMaritialIT_CB")
							+ "[" + EP.RandomValue(1, 5) + "]", "ON");
			Random = EP.RandomValue(1, 7);
			if (Random == 5) {
				Random = Random - 1;
				EP.ChkBx_SetOption(
						EP.ORXPath("#AddVisit_SocioeconomicParentsEmploymentIT_CB")
								+ "[" + Random + "]", "ON");
			} else {
				EP.ChkBx_SetOption(
						EP.ORXPath("#AddVisit_SocioeconomicParentsEmploymentIT_CB")
								+ "[" + Random + "]", "ON");
			}
			Random = EP.RandomValue(1, 5);
			if (Random == 3) {
				Random = Random - 1;
				EP.DrpDwn_SelectIndex("#AddVisit_EducationStatusOfPerson_DD",
						Random);
			} else {
				EP.DrpDwn_SelectIndex("#AddVisit_EducationStatusOfPerson_DD",
						Random);
			}
			EP.Btn_Click("#General_AddRecordSave_BN");

			break;
		case SINGAPORE:
			if (EP.ObjectExists("#General_Delete_IM")) {
				DeleteRecord("#General_Delete_IM", false);
			}
			EP.Btn_Click("#General_First_BN");
			EP.DrpDwn_SelectIndex("#AddVisit_SocioParentIT_DD",
					EP.RandomValue(1, 3));
			EP.ChkBx_SetOption(
					EP.ORXPath("#AddVisit_SocioeconomicParentMaritialIT_CB")
							+ "[" + EP.RandomValue(1, 3) + "]", "ON");
			Random = EP.RandomValue(1, 5);
			if (Random == 5) {
				Random = Random - 1;
				EP.ChkBx_SetOption(
						EP.ORXPath("#AddVisit_SocioeconomicParentsEmploymentIT_CB")
								+ "[" + Random + "]", "ON");
			} else {
				EP.ChkBx_SetOption(
						EP.ORXPath("#AddVisit_SocioeconomicParentsEmploymentIT_CB")
								+ "[" + Random + "]", "ON");
			}
			Random = EP.RandomValue(1, 3);
			if (Random == 3) {
				Random = Random - 1;
				EP.DrpDwn_SelectIndex("#AddVisit_EducationStatusOfPerson_DD",
						Random);
			} else {
				EP.DrpDwn_SelectIndex("#AddVisit_EducationStatusOfPerson_DD",
						Random);
			}
			EP.Btn_Click("#General_AddRecordSave_BN");

			break;
		case SAUDIARABIA:
		case MEXICO:
		case TAIWAN:
		case INDONESIA:
		case COLOMBIA:
			if (EP.ObjectExists("#General_Delete_IM")) {
				DeleteRecord("#General_Delete_IM", false);
			}
			EP.Btn_Click("#General_First_BN");
			EP.DrpDwn_SelectIndex("#AddVisit_SocioParentSG_DD",
					EP.RandomValue(1, 3));
			EP.ChkBx_SetOption(
					EP.ORXPath("#AddVisit_SocioeconomicParentMaritialTW_CB")
							+ "[" + EP.RandomValue(1, 3) + "]", "ON");
			Random = EP.RandomValue(1, 5);
			if (Random == 5) {
				Random = Random - 1;
				EP.ChkBx_SetOption(
						EP.ORXPath("#AddVisit_SocioeconomicParentsEmploymentTW_CB")
								+ "[" + Random + "]", "ON");
			} else {
				EP.ChkBx_SetOption(
						EP.ORXPath("#AddVisit_SocioeconomicParentsEmploymentTW_CB")
								+ "[" + Random + "]", "ON");
			}
			Random = EP.RandomValue(1, 3);
			if (Random == 3) {
				Random = Random - 1;
				EP.DrpDwn_SelectIndex("#AddVisit_EducationStatusOfPersonTW_DD",
						Random);
			} else {
				EP.DrpDwn_SelectIndex("#AddVisit_EducationStatusOfPersonTW_DD",
						Random);
			}
			EP.Btn_Click("#General_AddRecordSave_BN");

			break;
		case ARGENTINA:
			if (EP.ObjectExists("#General_Delete_IM")) {
				DeleteRecord("#General_Delete_IM", false);
			}
			EP.Btn_Click("#General_First_BN");
			EP.DrpDwn_SelectIndex("#AddVisit_SocioParentSG_DD",
					EP.RandomValue(1, 3));
			EP.ChkBx_SetOption(
					EP.ORXPath("#AddVisit_SocioeconomicParentMaritialTW_CB")
							+ "[" + EP.RandomValue(1, 3) + "]", "ON");
			Random = EP.RandomValue(1, 5);
			if (Random == 5) {
				Random = Random - 1;
				EP.ChkBx_SetOption(
						EP.ORXPath("#AddVisit_SocioeconomicParentsEmploymentTW_CB")
								+ "[" + Random + "]", "ON");
			} else {
				EP.ChkBx_SetOption(
						EP.ORXPath("#AddVisit_SocioeconomicParentsEmploymentTW_CB")
								+ "[" + Random + "]", "ON");
			}

			EP.Btn_Click("#General_AddRecordSave_BN");

			break;

		case UNITEDARABEMIRATES:
			if (EP.ObjectExists("#General_Delete_IM")) {
				DeleteRecord("#General_Delete_IM", false);
			}
			EP.Btn_Click("#General_First_BN");
			EP.DrpDwn_SelectIndex("#AddVisit_SocioParentIT_DD",
					EP.RandomValue(1, 3));
			EP.ChkBx_SetOption(
					EP.ORXPath("#AddVisit_SocioeconomicParentMaritialIT_CB")
							+ "[" + EP.RandomValue(1, 3) + "]", "ON");
			Random = EP.RandomValue(1, 5);
			if (Random == 5) {
				Random = Random - 1;
				EP.ChkBx_SetOption(
						EP.ORXPath("#AddVisit_SocioeconomicParentsEmploymentIT_CB")
								+ "[" + Random + "]", "ON");
			} else {
				EP.ChkBx_SetOption(
						EP.ORXPath("#AddVisit_SocioeconomicParentsEmploymentIT_CB")
								+ "[" + Random + "]", "ON");
			}
			Random = EP.RandomValue(1, 3);
			if (Random == 3) {
				Random = Random - 1;
				EP.DrpDwn_SelectIndex("#AddVisit_EducationStatusOfPerson_DD",
						Random);
			} else {
				EP.DrpDwn_SelectIndex("#AddVisit_EducationStatusOfPerson_DD",
						Random);
			}
			EP.Btn_Click("#General_AddRecordSave_BN");

			break;
		case KOREA:
			if (EP.ObjectExists("#General_Delete_IM")) {
				DeleteRecord("#General_Delete_IM", false);
			}
			EP.Btn_Click("#General_First_BN");
			EP.DrpDwn_SelectIndex("#AddVisit_SocioParentES_DD",
					EP.RandomValue(1, 3));
			EP.ChkBx_SetOption(
					EP.ORXPath("#AddVisit_SocioeconomicParentMaritialIT_CB")
							+ "[" + EP.RandomValue(1, 4) + "]", "ON");
			EP.ChkBx_SetOption(
					EP.ORXPath("#AddVisit_SocioeconomicParentsEmploymentIT_CB")
							+ "[" + EP.RandomValue(1, 4) + "]", "ON");
			Random = EP.RandomValue(1, 4);
			if (Random == 3) {
				Random = Random - 1;
				EP.DrpDwn_SelectIndex("#AddVisit_EducationStatusOfPersonES_DD",
						Random);
			} else {
				EP.DrpDwn_SelectIndex("#AddVisit_EducationStatusOfPersonES_DD",
						Random);
			}
			EP.Btn_Click("#General_AddRecordSave_BN");
			break;

		case AUSTRALIA:
			if (EP.ObjectExists("#General_Delete_IM")) {
				DeleteRecord("#General_Delete_IM", false);
			}
			EP.Btn_Click("#General_First_BN");
			EP.DrpDwn_SelectIndex("#AddVisit_SocioParentES_DD",
					EP.RandomValue(1, 3));
			EP.ChkBx_SetOption(
					EP.ORXPath("#AddVisit_SocioeconomicParentMaritialIT_CB")
							+ "[" + EP.RandomValue(1, 4) + "]", "ON");
			if (IsValid) {
				EP.ChkBx_SetOption(
						EP.ORXPath("#AddVisit_SocioeconomicParentsEmploymentIT_CB")
								+ "[" + EP.RandomValue(1, 4) + "]", "ON");
				Random = EP.RandomValue(1, 4);
				if (Random == 3) {
					Random = Random - 1;
					EP.DrpDwn_SelectIndex(
							"#AddVisit_EducationStatusOfPersonES_DD", Random);
				} else {
					EP.DrpDwn_SelectIndex(
							"#AddVisit_EducationStatusOfPersonES_DD", Random);
				}
			}
			EP.Btn_Click("#General_AddRecordSave_BN");
			break;

		case ICELAND: // No Invalid data
			if (EP.ObjectExists("#General_Delete_IM")) {
				DeleteRecord("#General_Delete_IM", false);
			}
			EP.Btn_Click("#General_First_BN");
			EP.DrpDwn_SelectIndex("#AddVisit_SocioParentES_DD",
					EP.RandomValue(1, 3));
			EP.ChkBx_SetOption(
					EP.ORXPath("#AddVisit_SocioeconomicParentMaritialIT_CB")
							+ "[" + EP.RandomValue(1, 4) + "]", "ON");
			if (IsValid) {
				EP.ChkBx_SetOption(
						EP.ORXPath("#AddVisit_SocioeconomicParentsEmploymentIT_CB")
								+ "[" + EP.RandomValue(1, 4) + "]", "ON");
				Random = EP.RandomValue(1, 4);
				if (Random == 3) {
					Random = Random - 1;
					EP.DrpDwn_SelectIndex(
							"#AddVisit_EducationStatusOfPersonES_DD", Random);
				} else {
					EP.DrpDwn_SelectIndex(
							"#AddVisit_EducationStatusOfPersonES_DD", Random);
				}
			}
			EP.Btn_Click("#General_AddRecordSave_BN");
			break;

		case FRANCE:
			if (IsValid) {
				InputValues = new String[] { "50" };
			} else {
				InputValues = new String[] { "56" };
			}
			if (EP.ObjectExists("#General_Delete_IM")) {
				DeleteRecord("#General_Delete_IM", false);
			}
			EP.Btn_Click("#General_First_BN");
			EP.DrpDwn_SelectIndex("#AddVisit_SocioParentFR_DD",
					EP.RandomValue(1, 3));
			EP.ChkBx_SetOption(
					EP.ORXPath("#AddVisit_SocioeconomicParentMaritialIT_CB")
							+ "[" + EP.RandomValue(1, 3) + "]", "ON");
			Random = EP.RandomValue(1, 5);
			if (Random == 5) {
				Random = Random - 1;
				EP.ChkBx_SetOption(
						EP.ORXPath("#AddVisit_SocioeconomicParentsEmploymentIT_CB")
								+ "[" + Random + "]", "ON");
			} else {
				EP.ChkBx_SetOption(
						EP.ORXPath("#AddVisit_SocioeconomicParentsEmploymentIT_CB")
								+ "[" + Random + "]", "ON");
			}
			EP.Edit_SetValue("#AddVisit_SocioPatientsNumberFR_EB",
					InputValues[0]);
			EP.Btn_Click("#General_AddRecordSave_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN"); // click save button
			}

			break;
		case SPAIN:// Invalida data not available
			if (EP.ObjectExists("#General_Delete_IM")) {
				DeleteRecord("#General_Delete_IM", false);
			}
			EP.Btn_Click("#General_First_BN");
			EP.DrpDwn_SelectIndex("#AddVisit_SocioParentES_DD",
					EP.RandomValue(1, 3));
			EP.ChkBx_SetOption(
					EP.ORXPath("#AddVisit_SocioeconomicParentMaritialIT_CB")
							+ "[" + EP.RandomValue(1, 4) + "]", "ON");
			EP.ChkBx_SetOption(
					EP.ORXPath("#AddVisit_SocioeconomicParentsEmploymentIT_CB")
							+ "[" + EP.RandomValue(1, 4) + "]", "ON");
			Random = EP.RandomValue(1, 4);
			if (Random == 3) {
				Random = Random - 1;
				EP.DrpDwn_SelectIndex("#AddVisit_EducationStatusOfPersonES_DD",
						Random);
			} else {
				EP.DrpDwn_SelectIndex("#AddVisit_EducationStatusOfPersonES_DD",
						Random);
			}
			EP.Btn_Click("#General_AddRecordSave_BN");

			break;
		case ITALY:// Invalida data not available
			if (EP.ObjectExists("#General_Delete_IM")) {
				DeleteRecord("#General_Delete_IM", false);
			}
			EP.Btn_Click("#General_First_BN");
			EP.DrpDwn_SelectIndex("#AddVisit_SocioParentIT_DD",
					EP.RandomValue(1, 5));
			// EP.ChkBx_SetOptionForAll("//input[@type='checkbox']", "OFF");
			EP.ChkBx_SetOption(
					EP.ORXPath("#AddVisit_SocioeconomicParentMaritialIT_CB")
							+ "[" + EP.RandomValue(1, 3) + "]", "ON");
			Random = EP.RandomValue(1, 7);
			if (Random == 5) {
				Random = Random - 1;
				EP.ChkBx_SetOption(
						EP.ORXPath("#AddVisit_SocioeconomicParentsEmploymentIT_CB")
								+ "[" + Random + "]", "ON");
			} else {
				EP.ChkBx_SetOption(
						EP.ORXPath("#AddVisit_SocioeconomicParentsEmploymentIT_CB")
								+ "[" + Random + "]", "ON");
			}

			Random = EP.RandomValue(1, 5);
			if (Random == 3) {
				Random = Random - 1;
				EP.DrpDwn_SelectIndex("#AddVisit_EducationStatusOfPerson_DD",
						Random);
			} else {
				EP.DrpDwn_SelectIndex("#AddVisit_EducationStatusOfPerson_DD",
						Random);
			}
			EP.Btn_Click("#General_AddRecordSave_BN");

			break;
		case UNITEDKINGDOM:
			if (EP.ObjectExists("#General_Delete_IM")) {
				DeleteRecord("#General_Delete_IM", false);
			}
			EP.Btn_Click("#General_First_BN");
			EP.DrpDwn_SelectIndex("#AddVisit_SocioParentUK_DD",
					EP.RandomValue(1, 3));
			EP.DrpDwn_SelectIndex("#AddVisit_ParentMaritalStatusUK_DD",
					EP.RandomValue(1, 4));
			EP.DrpDwn_SelectIndex("#AddVisit_ParentEmploymentUK_DD",
					EP.RandomValue(1, 5));
			Random = EP.RandomValue(1, 5);
			if (Random == 3) {
				Random = Random - 1;
				EP.DrpDwn_SelectIndex("#AddVisit_SocioEduStatus_DD", Random);
			} else {
				EP.DrpDwn_SelectIndex("#AddVisit_SocioEduStatus_DD", Random);
			}
			EP.Btn_Click("#General_AddRecordSave_BN");
			break;
		case GREECE:
			if (EP.ObjectExists("#General_Delete_IM")) {
				DeleteRecord("#General_Delete_IM", false);
			}
			EP.Btn_Click("#General_First_BN");
			EP.DrpDwn_SelectIndex("#AddVisit_SocioParentES_DD",
					EP.RandomValue(1, 5));
			EP.ChkBx_SetOption(
					EP.ORXPath("#AddVisit_SocioeconomicParentMaritialIT_CB")
							+ "[" + EP.RandomValue(1, 5) + "]", "ON");
			Random = EP.RandomValue(1, 7);
			if (Random == 5) {
				Random = Random - 1;
				EP.ChkBx_SetOption(
						EP.ORXPath("#AddVisit_SocioeconomicParentsEmploymentIT_CB")
								+ "[" + Random + "]", "ON");
			} else {
				EP.ChkBx_SetOption(
						EP.ORXPath("#AddVisit_SocioeconomicParentsEmploymentIT_CB")
								+ "[" + Random + "]", "ON");
			}
			Random = EP.RandomValue(1, 4);
			if (Random == 3) {
				Random = Random - 1;
				EP.DrpDwn_SelectIndex("#AddVisit_EducationStatusOfPersonES_DD",
						Random);
			} else {
				EP.DrpDwn_SelectIndex("#AddVisit_EducationStatusOfPersonES_DD",
						Random);
			}
			EP.Btn_Click("#General_AddRecordSave_BN");

			break;
		case SLOVAKIA:
			if (EP.ObjectExists("#General_Delete_IM")) {
				DeleteRecord("#General_Delete_IM", false);
			}
			EP.Btn_Click("#General_First_BN");
			EP.DrpDwn_SelectIndex("#AddVisit_SocioParentES_DD",
					EP.RandomValue(1, 3));
			EP.ChkBx_SetOption(
					EP.ORXPath("#AddVisit_SocioeconomicParentMaritialIT_CB")
							+ "[" + EP.RandomValue(1, 5) + "]", "ON");
			Random = EP.RandomValue(1, 7);
			if (Random == 5) {
				Random = Random - 1;
				EP.ChkBx_SetOption(
						EP.ORXPath("#AddVisit_SocioeconomicParentsEmploymentIT_CB")
								+ "[" + Random + "]", "ON");
			} else {
				EP.ChkBx_SetOption(
						EP.ORXPath("#AddVisit_SocioeconomicParentsEmploymentIT_CB")
								+ "[" + Random + "]", "ON");
			}
			Random = EP.RandomValue(1, 4);
			if (Random == 3) {
				Random = Random - 1;
				EP.DrpDwn_SelectIndex("#AddVisit_EducationStatusOfPersonES_DD",
						Random);
			} else {
				EP.DrpDwn_SelectIndex("#AddVisit_EducationStatusOfPersonES_DD",
						Random);
			}
			EP.Btn_Click("#General_AddRecordSave_BN");

			break;
		case CZECHREPUBLIC:
		case HUNGARY:
		case NORWAY:
			if (EP.ObjectExists("#General_Delete_IM")) {
				DeleteRecord("#General_Delete_IM", false);
			}
			EP.Btn_Click("#General_First_BN"); // Click Add button
			EP.DrpDwn_SelectIndex("#AddVisit_ParentAT_DD", EP.RandomValue(1, 3));
			// All check boxes ON
			EP.ChkBx_SetOption(
					EP.ORXPath("#AddVisit_SocioeconomicParentMaritialIT_CB")
							+ "[" + EP.RandomValue(1, 4) + "]", "ON");
			EP.ChkBx_SetOption(
					EP.ORXPath("#AddVisit_SocioeconomicParentsEmploymentIT_CB")
							+ "[" + EP.RandomValue(1, 4) + "]", "ON");
			Random = EP.RandomValue(1, 4);
			if (Random == 3) {
				Random = Random - 1;
				EP.DrpDwn_SelectIndex("#AddVisit_EducationStatusOfPerson_DD",
						Random);
			} else {
				EP.DrpDwn_SelectIndex("#AddVisit_EducationStatusOfPerson_DD",
						Random);
			}
			EP.Btn_Click("#General_AddRecordSave_BN"); // click save button
			break;

		case FINLAND:
			if (EP.ObjectExists("#General_Delete_IM")) {
				DeleteRecord("#General_Delete_IM", false);
			}
			EP.Btn_Click("#General_First_BN"); // Click Add button
			EP.DrpDwn_SelectIndex("#AddVisit_ParentAT_DD", EP.RandomValue(1, 3));
			// All check boxes ON
			EP.ChkBx_SetOption(
					EP.ORXPath("#AddVisit_SocioeconomicParentMaritialFI_CB")
							+ "[" + EP.RandomValue(1, 4) + "]", "ON");
			EP.ChkBx_SetOption(
					EP.ORXPath("#AddVisit_SocioeconomicParentsEmploymentFI_CB")
							+ "[" + EP.RandomValue(1, 4) + "]", "ON");
			EP.DrpDwn_SelectIndex("#AddVisit_EducationStatusOfPerson_DD",
					EP.RandomValue(1, 2));
			EP.Btn_Click("#General_AddRecordSave_BN"); // click save button
			break;

		case SWEDEN:

			if (EP.ObjectExists("#General_Delete_IM")) {
				DeleteRecord("#General_Delete_IM", false);
			}
			EP.Btn_Click("#General_First_BN"); // Click Add button
			EP.DrpDwn_SelectIndex("#AddVisit_ParentAT_DD", EP.RandomValue(1, 3));
			EP.ChkBx_SetOption(
					EP.ORXPath("#AddVisit_SocioeconomicParentMaritialAT_CB")
							+ "[" + EP.RandomValue(1, 5) + "]", "ON");
			EP.DrpDwn_SelectIndex("#AddVisit_EducationStatusOfPerson_DD",
					EP.RandomValue(1, 2));
			int Random = EP.RandomValue(1, 7);
			if (IsValid) {
				String Text = EP
						.GetText(EP
								.ORXPath("#AddVisit_SocioeconomicParentsEmploymentAT_CB")
								+ "["
								+ Random
								+ "]"
								+ "//following-sibling::span");
				if (!EP.CompareText(Text, EP.LangData("OtherPleaseSpecify"))) {
					EP.ChkBx_SetOption(
							EP.ORXPath("#AddVisit_SocioeconomicParentsEmploymentAT_CB")
									+ "[" + Random + "]", "ON");
				} else {
					Random = Random - 2;
					EP.ChkBx_SetOption(
							EP.ORXPath("#AddVisit_SocioeconomicParentsEmploymentAT_CB")
									+ "[" + Random + "]", "ON");
				}
				Random = EP.RandomValue(1, 4);
				Text = EP.GetText(EP
						.ORXPath("#AddVisit_EducationStatusOfPerson_DD")
						+ "//option" + "[" + Random + "]");
				if (!EP.CompareText(Text, EP.LangData("OtherSpecify"))) {
					// if(Text != EP.LangData("OtherSpecify")){
					EP.DrpDwn_SelectIndex(
							"#AddVisit_EducationStatusOfPerson_DD", Random);
				} else {
					EP.DrpDwn_SelectIndex(
							"#AddVisit_EducationStatusOfPerson_DD", Random - 2);
				}
				EP.Btn_Click("#General_AddRecordSave_BN");
			} else {
				EP.ChkBx_SetOption("#AddVisit_OthersPlsSpecifyAT_CB", "ON");
				EP.DrpDwn_SelectIndex("#AddVisit_EducationStatusOfPerson_DD", 3);
				EP.Btn_Click("#General_AddRecordSave_BN");
				EP.Btn_Click("#General_AddRecordSave_BN");
			}
			break;

		case CANADA:

			if (EP.ObjectExists("#General_Delete_IM")) {
				DeleteRecord("#General_Delete_IM", false);
			}
			EP.Btn_Click("#General_First_BN"); // Click Add button
			EP.DrpDwn_SelectIndex("#AddVisit_SocioParentSK_DD",
					EP.RandomValue(1, 3));
			EP.ChkBx_SetOption(
					EP.ORXPath("#AddVisit_SocioeconomicParentMaritialIT_CB")
							+ "[" + EP.RandomValue(1, 3) + "]", "ON");
			Random = EP.RandomValue(1, 5);
			if (Random == 5) {
				Random = Random - 1;
				EP.ChkBx_SetOption(
						EP.ORXPath("#AddVisit_SocioeconomicParentsEmploymentIT_CB")
								+ "[" + Random + "]", "ON");
			} else {
				EP.ChkBx_SetOption(
						EP.ORXPath("#AddVisit_SocioeconomicParentsEmploymentIT_CB")
								+ "[" + Random + "]", "ON");
			}
			EP.Btn_Click("#General_AddRecordSave_BN");

			break;

		default:
		}
		VerifySuccessMessage(EP.LangData("SocioEconomicInformation"));

	}

	public void CurrentGHAdd(boolean IsValid, String TabIndex, String PatientDOB) {
		// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*CURRENT GH
		// TREATMENT*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
		EP.ShowTab(TabIndex);
		String[] InputValues;
		switch (switchCountryName) {

		case SINGAPORE:
			if (IsValid) {
				InputValues = new String[] { "0.05" };
			} else {
				InputValues = new String[] { "1" };
			}
			// if(!IsValid){
			// EP.Lnk_Click("#NavigationTab_RecordOverview_LK");
			// EP.WaitForPageload();
			// EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");
			// EP.DrpDwn_SelectIndex("#CompleteTermination_StatusOfStudy_DD",
			// 2);
			// String Date = EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"), -1,
			// Calendar.DATE);
			// EP.SelectDate("//input[@id='ghIniStopDateTerminate']//following-sibling::img",
			// Date);
			// EP.Btn_Click("#CompleteTermination_Save_BN");
			// if(EP.ObjectExists("#CompleteTermination_Save_BN")){
			// EP.Btn_Click("#CompleteTermination_Save_BN");
			// }
			// EP.NavigateTo(Env.ECOSURL);
			// EP.SearchForPatient(EP.TestData("PatientName") + " 01");
			// EP.Rbtn_SetOption("#MyPatients_ECOSPatient_RB", "ON");
			// EP.Btn_Click("#MyPatients_VisitsRecord_BN");
			// EP.WbTbl_SelectRbtn("#AddVisit_VisitDate_TE", 2, "03 Mar 2013");
			// EP.Btn_Click("#AddVisit_Continue_BN");
			//
			// EP.ShowTab(TabIndex);
			// EP.SelectDate("#AddVisit_CurrentGHTreatmentStartDateIT_IM",EP.GetTimeStamp("dd MMM yyyy"));
			// }
			// EP.Edit_SetValue("#AddVisit_CurrentGHTreatmentDailyDose_EB",
			// InputValues[0]);
			// EP.ChkBx_SetOption("#AddVisit_SaizenFreezeDriedIT_CB", "ON");
			// EP.Btn_Click("#General_First_BN");
			// if(!IsValid){
			// EP.Btn_Click("#General_First_BN");
			// }

			if (EP.ObjectExists("#General_Delete_IM")) {
				DeleteRecord("#General_Delete_IM", false);
			}
			EP.Edit_SetValue("#AddVisit_CurrentGHTreatmentDailyDose_EB", "0.05");
			EP.ChkBx_SetOption("#AddVisit_SaizenFreezeDriedIT_CB", "ON");
			EP.Btn_Click("#General_First_BN"); // Click Add button
			EP.Btn_Click("#AddVisit_CurrentGHAddSG_BN");
			EP.Rbtn_SetOption("#AddVisit_CurrentGHDoseChangedYes_RB", "ON");// Dose
																			// changed
																			// since
																			// previous
																			// visit?
			EP.Rbtn_SetOption("#AddVisit_CurrentGHMissedInjectionYes_RB", "ON");// Missed
																				// Injection
																				// since
																				// previous
																				// visit?
			EP.Rbtn_SetOption("#AddVisit_CurrentGHTemporaryCessationYes_RB",
					"ON");

			EP.ChkBx_SetOption(
					EP.ORXPath("#AddVisit_GHtreatmentMajorityInjection_CB")
							+ "[" + EP.RandomValue(1, 3) + "]", "ON"); // Select
																		// a
																		// random
																		// checkbox
																		// in
																		// Majority
																		// Injections
			// String dailyDose = EP.Converter("0.05");
			EP.Edit_SetValue("#AddVisit_CurrentGHNewDailyDose_EB",
					EP.Converter(InputValues[0])); // Enter a value of
													// Formulation Unit

			EP.SelectDate("#AddVisit_CurrentGHTreatmentStopDate_IM", EP
					.Date_AddDays(EP.TestData("ECOSPatientDOB"), 5,
							Calendar.DATE));// New daily dose start date
			EP.HideCursor();
			EP.SelectDate("#AddVisit_CurrentGHTreatmentStartDate_IM",
					EP.TestData("ECOSPatientDOB"));// Previous daily dose stop
													// date

			EP.SelectDate("#AddVisit_CurrentGHTemporaryCessationStopFrom_IM",
					EP.TestData("ECOSPatientDOB"));// Cessation of treatment
													// stop from
			EP.HideCursor();
			EP.SelectDate("#AddVisit_CurrentGHTemporaryCessationStopTo_IM", EP
					.Date_AddDays(EP.TestData("ECOSPatientDOB"), 5,
							Calendar.DATE));// Cessation of treatment stop to

			if (IsValid) {
				Random = EP.RandomValue(1, 3);
				EP.ChkBx_SetOption(
						EP.ORXPath("#AddVisit_CurrentGHReasonForDose_CB") + "["
								+ Random + "]", "ON");

				Random = EP.RandomValue(1, 7);

				EP.ChkBx_SetOption(
						EP.ORXPath("#AddVisit_GHtreatmentMissedInjection_CB")
								+ "[" + Random + "]", "ON");

				// EP.ChkBx_SetOption(TabName+"CurrentGHMissedReasonForgot_CB",
				// "ON"); //Select the checkbox for missed injection, needed for
				// table verication
				EP.Rbtn_SetOption(
						EP.ORXPath("#AddVisit_AddTreatmentStopAccordancePhysician_RB")
								+ "[" + EP.RandomValue(1, 2) + "]", "ON");

				Random = EP.RandomValue(1, 2);
				EP.ChkBx_SetOption(
						EP.ORXPath("#AddVisit_CurrentGHReasonForTemp_CB") + "["
								+ Random + "]", "ON");

			} else {
				// Remove all the checkbox
				EP.ChkBx_SetOptionForAll(
						"//div[@class='RecordOverview']//input[@class='checkbox']",
						"OFF");

				// Select the Required Checkboxes
				EP.ChkBx_SetOption("#AddVisit_CurrentGHAdverseEvent_CB", "ON");
				EP.ChkBx_SetOption("#AddVisit_CurrentGHOther_CB", "ON");
				EP.ChkBx_SetOption("#AddVisit_CurrentGHOthers_CB", "ON");
				EP.ChkBx_SetOption(
						"#AddVisit_CurrentGHReasonsOthersPlsSpecify_CB", "ON");
				EP.Btn_Click("#General_AddRecordSave_BN"); // Click save
			}
			EP.Btn_Click("#General_AddRecordSave_BN");
			break;

		case TAIWAN:
		case INDONESIA:

			if (IsValid) {
				InputValues = new String[] { "0.05" };
			} else {
				InputValues = new String[] { "1" };
			}
			//

			if (EP.ObjectExists("#General_Delete_IM")) {
				DeleteRecord("#General_Delete_IM", false);
			}
			// EP.Edit_SetValue("#AddVisit_CurrentGHTreatmentDailyDose_EB",
			// "0.05");
			// EP.ChkBx_SetOption("#AddVisit_SaizenFreezeDriedIT_CB", "ON");
			EP.Btn_Click("#General_First_BN"); // Click Add button
			// EP.Btn_Click("#AddVisit_CurrentGHAddSG_BN");
			EP.Rbtn_SetOption("#AddVisit_CurrentGHDoseChangedYes_RB", "ON");// Dose
																			// changed
																			// since
																			// previous
																			// visit?
			EP.Rbtn_SetOption("#AddVisit_CurrentGHMissedInjectionYes_RB", "ON");// Missed
																				// Injection
																				// since
																				// previous
																				// visit?
			EP.Rbtn_SetOption("#AddVisit_CurrentGHTemporaryCessationYes_RB",
					"ON");

			EP.ChkBx_SetOption(
					EP.ORXPath("#AddVisit_GHtreatmentMajorityInjection_CB")
							+ "[" + EP.RandomValue(1, 3) + "]", "ON"); // Select
																		// a
																		// random
																		// checkbox
																		// in
																		// Majority
																		// Injections
			// String dailyDose = EP.Converter("0.05");
			EP.Edit_SetValue("#AddVisit_CurrentGHNewDailyDose_EB",
					EP.Converter(InputValues[0])); // Enter a value of
													// Formulation Unit

			EP.SelectDate("#AddVisit_CurrentGHOthersStopDate_IM", EP
					.Date_AddDays(EP.TestData("ECOSPatientDOB"), 5,
							Calendar.DATE));// New daily dose start date
			EP.HideCursor();
			EP.SelectDate("#AddVisit_CurrentGHOthersStartDateTW_IM",
					EP.TestData("ECOSPatientDOB"));// Previous daily dose stop
													// date

			EP.SelectDate("#AddVisit_CurrentGHTemporaryCessationStopFrom_IM",
					EP.TestData("ECOSPatientDOB"));// Cessation of treatment
													// stop from
			EP.HideCursor();
			EP.SelectDate("#AddVisit_CurrentGHTemporaryCessationStopTo_IM", EP
					.Date_AddDays(EP.TestData("ECOSPatientDOB"), 5,
							Calendar.DATE));// Cessation of treatment stop to

			if (IsValid) {
				Random = EP.RandomValue(1, 3);
				EP.ChkBx_SetOption(
						EP.ORXPath("#AddVisit_CurrentGHReasonForDose_CB") + "["
								+ Random + "]", "ON");

				Random = EP.RandomValue(1, 7);

				EP.ChkBx_SetOption(
						EP.ORXPath("#AddVisit_GHtreatmentMissedInjection_CB")
								+ "[" + Random + "]", "ON");

				// EP.ChkBx_SetOption(TabName+"CurrentGHMissedReasonForgot_CB",
				// "ON"); //Select the checkbox for missed injection, needed for
				// table verication
				EP.Rbtn_SetOption(
						EP.ORXPath("#AddVisit_AddTreatmentStopAccordancePhysician_RB")
								+ "[" + EP.RandomValue(1, 2) + "]", "ON");

				Random = EP.RandomValue(1, 2);
				EP.ChkBx_SetOption(
						EP.ORXPath("#AddVisit_CurrentGHReasonForTemp_CB") + "["
								+ Random + "]", "ON");

			} else {
				// Remove all the checkbox
				EP.ChkBx_SetOptionForAll(
						"//div[@class='RecordOverview']//input[@class='checkbox']",
						"OFF");

				// Select the Required Checkboxes
				EP.ChkBx_SetOption("#AddVisit_CurrentGHAdverseEvent_CB", "ON");
				EP.ChkBx_SetOption("#AddVisit_CurrentGHOther_CB", "ON");
				EP.ChkBx_SetOption("#AddVisit_CurrentGHOthers_CB", "ON");
				EP.ChkBx_SetOption(
						"#AddVisit_CurrentGHReasonsOthersPlsSpecify_CB", "ON");
				EP.Btn_Click("#General_AddRecordSave_BN"); // Click save
			}
			EP.Btn_Click("#General_AddRecordSave_BN");
			break;
		case UNITEDARABEMIRATES:
		case AUSTRIA:
		case SAUDIARABIA:
		case MEXICO:

			if (IsValid) {
				InputValues = new String[] { "0.05" };
			} else {
				InputValues = new String[] { "1" };
			}
			if (!IsValid) {
				EP.Lnk_Click("#NavigationTab_RecordOverview_LK");
				EP.WaitForPageload();
				EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");
				EP.DrpDwn_SelectIndex("#CompleteTermination_StatusOfStudy_DD",
						2);
				String Date = EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"),
						-1, Calendar.DATE);
				EP.SelectDate(
						"//input[@id='ghIniStopDateTerminate']//following-sibling::img",
						Date);
				EP.Btn_Click("#CompleteTermination_Save_BN");
				if (EP.ObjectExists("#CompleteTermination_Save_BN")) {
					EP.Btn_Click("#CompleteTermination_Save_BN");
				}
				EP.NavigateTo(Env.ECOSURL);
				EP.SearchForPatient(EP.TestData("PatientName") + " 01");
				EP.Rbtn_SetOption("#MyPatients_ECOSPatient_RB", "ON");
				EP.Btn_Click("#MyPatients_VisitsRecord_BN");
				EP.WbTbl_SelectRbtn("#AddVisit_VisitDate_TE", 2, "03 Mar 2013");
				EP.Btn_Click("#AddVisit_Continue_BN");

				EP.ShowTab(TabIndex);
				EP.SelectDate("#AddVisit_CurrentGHTreatmentStartDateIT_IM",
						EP.GetTimeStamp("dd MMM yyyy"));
			}
			EP.Edit_SetValue("#AddVisit_CurrentGHTreatmentDailyDose_EB",
					InputValues[0]);
			EP.ChkBx_SetOption("#AddVisit_SaizenFreezeDriedIT_CB", "ON");
			EP.Btn_Click("#General_First_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_First_BN");
			}

			if (EP.ObjectExists("#General_Delete_IM")) {
				DeleteRecord("#General_Delete_IM", false);
			}

			EP.Btn_Click("#General_Third_BN"); // Click Add button

			EP.Rbtn_SetOption("#AddVisit_CurrentGHDoseChangedYes_RB", "ON");// Dose
																			// changed
																			// since
																			// previous
																			// visit?
			EP.Rbtn_SetOption("#AddVisit_CurrentGHMissedInjectionYes_RB", "ON");// Missed
																				// Injection
																				// since
																				// previous
																				// visit?
			EP.Rbtn_SetOption("#AddVisit_CurrentGHTemporaryCessationYes_RB",
					"ON");

			EP.ChkBx_SetOption(
					EP.ORXPath("#AddVisit_GHtreatmentMajorityInjection_CB")
							+ "[" + EP.RandomValue(1, 3) + "]", "ON"); // Select
																		// a
																		// random
																		// checkbox
																		// in
																		// Majority
																		// Injections
			// String dailyDose = EP.Converter("0.05");
			EP.Edit_SetValue("#AddVisit_CurrentGHNewDailyDose_EB",
					EP.Converter(InputValues[0])); // Enter a value of
													// Formulation Unit

			EP.SelectDate("#AddVisit_CurrentGHTreatmentStopDate_IM", EP
					.Date_AddDays(EP.TestData("ECOSPatientDOB"), 5,
							Calendar.DATE));// New daily dose start date
			EP.HideCursor();
			EP.SelectDate("#AddVisit_CurrentGHTreatmentStartDate_IM",
					EP.TestData("ECOSPatientDOB"));// Previous daily dose stop
													// date

			EP.SelectDate("#AddVisit_CurrentGHTemporaryCessationStopFrom_IM",
					EP.TestData("ECOSPatientDOB"));// Cessation of treatment
													// stop from
			EP.HideCursor();
			EP.SelectDate("#AddVisit_CurrentGHTemporaryCessationStopTo_IM", EP
					.Date_AddDays(EP.TestData("ECOSPatientDOB"), 5,
							Calendar.DATE));// Cessation of treatment stop to

			if (IsValid) {
				Random = EP.RandomValue(1, 3);
				EP.ChkBx_SetOption(
						EP.ORXPath("#AddVisit_CurrentGHReasonForDose_CB") + "["
								+ Random + "]", "ON");

				Random = EP.RandomValue(1, 7);

				EP.ChkBx_SetOption(
						EP.ORXPath("#AddVisit_GHtreatmentMissedInjection_CB")
								+ "[" + Random + "]", "ON");

				// EP.ChkBx_SetOption(TabName+"CurrentGHMissedReasonForgot_CB",
				// "ON"); //Select the checkbox for missed injection, needed for
				// table verication
				EP.Rbtn_SetOption(
						EP.ORXPath("#AddVisit_AddTreatmentStopAccordancePhysician_RB")
								+ "[" + EP.RandomValue(1, 2) + "]", "ON");

				Random = EP.RandomValue(1, 2);
				EP.ChkBx_SetOption(
						EP.ORXPath("#AddVisit_CurrentGHReasonForTemp_CB") + "["
								+ Random + "]", "ON");

			} else {
				// Remove all the checkbox
				EP.ChkBx_SetOptionForAll(
						"//div[@class='RecordOverview']//input[@class='checkbox']",
						"OFF");

				// Select the Required Checkboxes
				EP.ChkBx_SetOption("#AddVisit_CurrentGHAdverseEvent_CB", "ON");
				EP.ChkBx_SetOption("#AddVisit_CurrentGHOther_CB", "ON");
				EP.ChkBx_SetOption("#AddVisit_CurrentGHOthers_CB", "ON");
				EP.ChkBx_SetOption(
						"#AddVisit_CurrentGHReasonsOthersPlsSpecify_CB", "ON");
				EP.Btn_Click("#General_AddRecordSave_BN"); // Click save
			}
			EP.Btn_Click("#General_AddRecordSave_BN");
			break;
		case FRANCE:
			if (IsValid) {
				InputValues = new String[] { "0.05" };
			} else {
				InputValues = new String[] { "1" };
			}
			if (!IsValid) {
				EP.Lnk_Click("#NavigationTab_RecordOverview_LK");
				EP.WaitForPageload();
				EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");
				EP.DrpDwn_SelectIndex("#CompleteTermination_StatusOfStudy_DD",
						2);
				String Date = EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"),
						-1, Calendar.DATE);
				EP.SelectDate(
						"//input[@id='ghIniStopDateTerminate']//following-sibling::img",
						Date);
				EP.Btn_Click("#CompleteTermination_Save_BN");
				if (EP.ObjectExists("#CompleteTermination_Save_BN")) {
					EP.Btn_Click("#CompleteTermination_Save_BN");
				}
				EP.NavigateTo(Env.ECOSURL);
				EP.SearchForPatient(EP.TestData("PatientName") + " 01");
				EP.Rbtn_SetOption("#MyPatients_ECOSPatient_RB", "ON");
				EP.Btn_Click("#MyPatients_VisitsRecord_BN");
				EP.WbTbl_SelectRbtn("#AddVisit_VisitDate_TE", 2, "03 Mar 2013");
				EP.Btn_Click("#AddVisit_Continue_BN");

				EP.ShowTab(TabIndex);
				EP.SelectDate("#AddVisit_CurrentGHTreatmentStartDateIT_IM",
						EP.GetTimeStamp("dd MMM yyyy"));
			}
			EP.Edit_SetValue("#AddVisit_CurrentGHTreatmentDailyDose_EB",
					InputValues[0]);
			EP.DrpDwn_SelectIndex("#AddVisit_CurrentHGFormaultionFR_DD",
					EP.RandomValue(1, 2));
			if (IsValid) {
				EP.Btn_Click("#General_First_BN");// click save button
			} else {
				EP.Btn_Click("#General_First_BN");
				EP.Btn_Click("#General_First_BN");
			}
			if (EP.ObjectExists("#General_Delete_IM")) {
				DeleteRecord("#General_Delete_IM", false);
			}
			EP.Btn_Click("#General_Third_BN");
			EP.Rbtn_SetOption("#AddVisit_CurrentGHDoseChangedYes_RB", "ON");
			EP.Rbtn_SetOption("#AddVisit_CurrentGHFormulationChangeYes_RB",
					"ON");
			EP.Rbtn_SetOption("#AddVisit_CurrentGHMissedInjectionYes_RB", "ON");
			EP.Rbtn_SetOption("#AddVisit_CurrentGHTemporaryCessationYes_RB",
					"ON");
			EP.SelectDate("#AddVisit_CurrentGHOthersStopDate_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.Wait(3);
			EP.HideCursor();
			EP.SelectDate("#AddVisit_CurrentGHOthersStartDate_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate("#AddVisit_CurrentGHFormaultionStartDate_IM",
					EP.GetTimeStamp("dd MMM yyyy"));// Previous daily dose stop
													// date
			EP.Wait(3);
			EP.HideCursor();
			EP.SelectDate("#AddVisit_CurrentGHFormaultionStopDate_IM",
					EP.GetTimeStamp("dd-MMM-yyyy"));// New daily dose start date
			EP.SelectDate("#AddVisit_CurrentGHTemporaryCessationStopFrom_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.Wait(3);
			EP.HideCursor();
			EP.SelectDate("#AddVisit_CurrentGHTemporaryCessationStopTo_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.Edit_SetValue("#AddVisit_CurrentGHNewDailyDose_EB",
					InputValues[0]);// Invalid Data getting this error only.
			EP.ChkBx_SetOption(
					EP.ORXPath("#AddVisit_GHtreatmentMajorityInjection_CB")
							+ "[" + EP.RandomValue(1, 4) + "]", "ON"); // Select
																		// a
																		// random
																		// checkbox
																		// in
																		// Majority
																		// Injections
			EP.ChkBx_SetOption(EP.ORXPath("#AddVisit_GHtreatmentReasonFor_CB")
					+ "[" + EP.RandomValue(1, 3) + "]", "ON");
			EP.Rbtn_SetOption(EP.ORXPath("#AddVisit_FormulationFR_CB") + "["
					+ EP.RandomValue(1, 2) + "]", "ON");// Formulation
			EP.ChkBx_SetOption(
					EP.ORXPath("#AddVisit_GHtreatmentMissedInjection_CB") + "["
							+ EP.RandomValue(1, 7) + "]", "ON");
			EP.ChkBx_SetOption(
					EP.ORXPath("#AddVisit_CurrentGHReasonForTemp_CB") + "["
							+ EP.RandomValue(1, 2) + "]", "ON");
			EP.Btn_Click("#General_AddRecordSave_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN");
			}
			break;
		case SPAIN:
			if (IsValid) {
				InputValues = new String[] { "0.05" };
			} else {
				InputValues = new String[] { "1" };
			}
			if (!IsValid) {
				EP.Lnk_Click("#NavigationTab_RecordOverview_LK");
				EP.WaitForPageload();
				EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");
				EP.DrpDwn_SelectIndex("#CompleteTermination_StatusOfStudy_DD",
						2);
				String Date = EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"),
						-1, Calendar.DATE);
				EP.SelectDate(
						"//input[@id='ghIniStopDateTerminate']//following-sibling::img",
						Date);
				EP.Btn_Click("#CompleteTermination_Save_BN");
				if (EP.ObjectExists("#CompleteTermination_Save_BN")) {
					EP.Btn_Click("#CompleteTermination_Save_BN");
				}
				EP.NavigateTo(Env.ECOSURL);
				EP.SearchForPatient(EP.TestData("PatientName") + " 01");
				EP.Rbtn_SetOption("#MyPatients_ECOSPatient_RB", "ON");
				EP.Btn_Click("#MyPatients_VisitsRecord_BN");
				EP.WbTbl_SelectRbtn("#AddVisit_VisitDate_TE", 2, "03 Mar 2013");
				EP.Btn_Click("#AddVisit_Continue_BN");

				EP.ShowTab(TabIndex);
				EP.SelectDate("#AddVisit_CurrentGHTreatmentStartDateIT_IM",
						EP.GetTimeStamp("dd MMM yyyy"));
			}
			EP.Edit_SetValue("#AddVisit_CurrentGHTreatmentDailyDose_EB",
					InputValues[0]);
			EP.DrpDwn_SelectIndex("#AddVisit_CurrentHGFormaultionES_DD",
					EP.RandomValue(1, 3));
			if (IsValid) {
				EP.Btn_Click("#General_First_BN");
			} else {
				EP.Btn_Click("#General_First_BN");
				EP.Btn_Click("#General_First_BN");
			}
			if (EP.ObjectExists("#General_Delete_IM")) {
				DeleteRecord("#General_Delete_IM", false);
			}
			EP.Btn_Click("#General_Third_BN");
			EP.Rbtn_SetOption("#AddVisit_CurrentGHDoseChangedYes_RB", "ON");
			EP.Rbtn_SetOption("#AddVisit_CurrentGHMissedInjectionYes_RB", "ON");
			EP.Rbtn_SetOption("#AddVisit_CurrentGHTemporaryCessationYes_RB",
					"ON");
			EP.ChkBx_SetOptionForAll(
					"//div[@class='RecordOverview']//input[@class='checkbox']",
					"OFF");
			EP.Edit_SetValue("#AddVisit_CurrentGHNewDailyDose_EB",
					InputValues[0]);// Invalid Data getting this error only.
			EP.SelectDate("#AddVisit_CurrentGHOthersStopDate_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.Wait(3);
			EP.HideCursor();
			EP.SelectDate("#AddVisit_CurrentGHOthersStartDate_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.HideCursor();
			EP.SelectDate("#AddVisit_CurrentGHTemporaryCessationStopFrom_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.Wait(3);
			EP.HideCursor();
			EP.SelectDate("#AddVisit_CurrentGHTemporaryCessationStopTo_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.HideCursor();
			EP.ChkBx_SetOption(
					EP.ORXPath("#AddVisit_GHtreatmentMajorityInjection_CB")
							+ "[" + EP.RandomValue(1, 3) + "]", "ON"); // Select
																		// a
																		// random
																		// checkbox
																		// in
																		// Majority
																		// Injections
			EP.ChkBx_SetOption(EP.ORXPath("#AddVisit_GHtreatmentReasonFor_CB")
					+ "[" + EP.RandomValue(1, 3) + "]", "ON");
			EP.ChkBx_SetOption(
					EP.ORXPath("#AddVisit_GHtreatmentMissedInjection_CB") + "["
							+ EP.RandomValue(1, 7) + "]", "ON");
			Random = EP.RandomValue(1, 2);
			EP.ChkBx_SetOption(
					EP.ORXPath("#AddVisit_CurrentGHReasonForTemp_CB") + "["
							+ Random + "]", "ON");

			EP.Btn_Click("#General_AddRecordSave_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN");
			}

			break;

		case ITALY:
			if (IsValid) {
				InputValues = new String[] { "0.05" };
			} else {
				InputValues = new String[] { "1" };
			}
			if (!IsValid) {
				EP.Lnk_Click("#NavigationTab_RecordOverview_LK");
				EP.WaitForPageload();
				EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");
				EP.DrpDwn_SelectIndex("#CompleteTermination_StatusOfStudy_DD",
						2);
				String Date = EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"),
						-1, Calendar.DATE);
				EP.SelectDate(
						"//input[@id='ghIniStopDateTerminate']//following-sibling::img",
						Date);
				EP.Btn_Click("#CompleteTermination_Save_BN");
				if (EP.ObjectExists("#CompleteTermination_Save_BN")) {
					EP.Btn_Click("#CompleteTermination_Save_BN");
				}
				EP.NavigateTo(Env.ECOSURL);
				EP.SearchForPatient(EP.TestData("PatientName") + " 01");
				EP.Rbtn_SetOption("#MyPatients_ECOSPatient_RB", "ON");
				EP.Btn_Click("#MyPatients_VisitsRecord_BN");
				EP.WbTbl_SelectRbtn("#AddVisit_VisitDate_TE", 2, "03 Mar 2013");
				EP.Btn_Click("#AddVisit_Continue_BN");

				EP.ShowTab(TabIndex);
				EP.SelectDate("#AddVisit_CurrentGHTreatmentStartDateIT_IM",
						EP.GetTimeStamp("dd MMM yyyy"));
			}
			EP.Edit_SetValue("#AddVisit_CurrentGHTreatmentDailyDose_EB",
					InputValues[0]);
			EP.ChkBx_SetOption("#AddVisit_SaizenFreezeDriedIT_CB", "ON");
			if (IsValid) {
				EP.Btn_Click("#General_First_BN");// click save button
			} else {
				EP.Btn_Click("#General_First_BN");
				EP.Btn_Click("#General_First_BN");
			}
			if (EP.ObjectExists("#General_Delete_IM")) {
				DeleteRecord("#General_Delete_IM", false);
			}
			EP.Btn_Click("#General_Third_BN");
			EP.WaitForPageload();
			EP.Rbtn_SetOption("#AddVisit_CurrentGHDoseChangedYes_RB", "ON");
			EP.Rbtn_SetOption("#AddVisit_CurrentGHMissedInjectionYes_RB", "ON");
			EP.Rbtn_SetOption("#AddVisit_CurrentGHTemporaryCessationYes_RB",
					"ON");

			// EP.ChkBx_SetOptionForAll("//div[@class='RecordOverview']//input[@class='checkbox']",
			// "OFF");

			// To check complete
			EP.ChkBx_SetOption(
					EP.ORXPath("#AddVisit_GHtreatmentMajorityInjection_CB")
							+ "[" + EP.RandomValue(1, 5) + "]", "ON"); // Select
																		// a
																		// random
																		// checkbox
																		// in
																		// Majority
																		// Injections
			EP.ChkBx_SetOption(EP.ORXPath("#AddVisit_GHtreatmentReasonFor_CB")
					+ "[" + EP.RandomValue(1, 3) + "]", "ON");
			EP.Edit_SetValue("#AddVisit_CurrentGHNewDailyDose_EB",
					InputValues[0]);
			EP.SelectDate("#AddVisit_CurrentGHOthersStopDate_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate("#AddVisit_CurrentGHOthersStartDate_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.ChkBx_SetOption(
					EP.ORXPath("#AddVisit_GHtreatmentMissedInjection_CB") + "["
							+ EP.RandomValue(1, 7) + "]", "ON");
			EP.SelectDate("#AddVisit_CurrentGHTemporaryCessationStopFrom_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate("#AddVisit_CurrentGHTemporaryCessationStopTo_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			int Random = EP.RandomValue(1, 5);
			if (Random == 3) {
				Random = Random - 1;
				EP.ChkBx_SetOption(
						EP.ORXPath("#AddVisit_CurrentGHReasonForTemp_CB") + "["
								+ Random + "]", "ON");
			} else {
				EP.ChkBx_SetOption(
						EP.ORXPath("#AddVisit_CurrentGHReasonForTemp_CB") + "["
								+ Random + "]", "ON");
			}
			if (IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN");
			} else {
				EP.Btn_Click("#General_AddRecordSave_BN");
				EP.Btn_Click("#General_AddRecordSave_BN");
			}
			break;
		case UNITEDKINGDOM:
			if (IsValid) {
				InputValues = new String[] { "0.05" };
			} else {
				InputValues = new String[] { "1" };
			}
			if (!IsValid) {
				EP.Lnk_Click("#NavigationTab_RecordOverview_LK");
				EP.WaitForPageload();
				EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");
				EP.DrpDwn_SelectIndex("#CompleteTermination_StatusOfStudy_DD",
						2);
				String Date = EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"),
						-1, Calendar.DATE);
				EP.SelectDate(
						"//input[@id='ghIniStopDateTerminate']//following-sibling::img",
						Date);
				EP.Btn_Click("#CompleteTermination_Save_BN");
				if (EP.ObjectExists("#CompleteTermination_Save_BN")) {
					EP.Btn_Click("#CompleteTermination_Save_BN");
				}
				EP.NavigateTo(Env.ECOSURL);
				EP.SearchForPatient(EP.TestData("PatientName") + " 01");
				EP.Rbtn_SetOption("#MyPatients_ECOSPatient_RB", "ON");
				EP.Btn_Click("#MyPatients_VisitsRecord_BN");
				EP.WbTbl_SelectRbtn("#AddVisit_VisitDate_TE", 2, "03 Mar 2013");
				EP.Btn_Click("#AddVisit_Continue_BN");

				EP.ShowTab(TabIndex);
				EP.SelectDate("#AddVisit_CurrentGHTreatmentStartDateIT_IM",
						EP.GetTimeStamp("dd MMM yyyy"));
			}
			EP.Edit_SetValue("#AddVisit_CurrentGHTreatmentDailyDose_EB",
					InputValues[0]);
			EP.Rbtn_SetOption(EP.ORXPath("#AddVisit_CurrentHGFormaultionUK_RB")
					+ "[" + EP.RandomValue(1, 2) + "]", "ON");// Formulation
			if (IsValid) {
				EP.Btn_Click("#General_First_BN");// click save button
			} else {
				EP.Btn_Click("#General_First_BN");
				EP.Btn_Click("#General_First_BN");
			}
			if (EP.ObjectExists("#General_Delete_IM")) {
				DeleteRecord("#General_Delete_IM", false);
			}
			EP.Btn_Click("#General_Third_BN");
			EP.WaitForPageload();
			EP.Rbtn_SetOption("#AddVisit_CurrentGHDoseChangedYes_RB", "ON");
			EP.Rbtn_SetOption("#AddVisit_CurrentGHFormulationChangeYes_RB",
					"ON");
			EP.Rbtn_SetOption("#AddVisit_CurrentGHMissedInjectionYes_RB", "ON");
			EP.Rbtn_SetOption("#AddVisit_CurrentGHTemporaryCessationYes_RB",
					"ON");

			// Select all dates
			EP.SelectDate("#AddVisit_CurrentGHOthersStopDate_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate("#AddVisit_CurrentGHOthersStartDate_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.Edit_SetValue("#AddVisit_CurrentGHNewDailyDose_EB",
					InputValues[0]);
			EP.SelectDate("#AddVisit_CurrentGHFormaultionStartDate_IM",
					EP.GetTimeStamp("dd MMM yyyy"));// Previous daily dose stop
													// date
			EP.SelectDate("#AddVisit_CurrentGHFormaultionStopDate_IM",
					EP.GetTimeStamp("dd-MMM-yyyy"));// New daily dose start date
			EP.SelectDate("#AddVisit_CurrentGHTemporaryCessationStopFrom_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate("#AddVisit_CurrentGHTemporaryCessationStopTo_IM",
					EP.GetTimeStamp("dd MMM yyyy"));

			EP.ChkBx_SetOption(
					EP.ORXPath("#AddVisit_GHtreatmentMajorityInjection_CB")
							+ "[" + EP.RandomValue(1, 5) + "]", "ON"); // Select
																		// a
																		// random
																		// checkbox
																		// in
																		// Majority
																		// Injections
			// Reason for Dose Change
			Random = EP.RandomValue(1, 7);
			if (Random == 4 || Random == 5) {
				Random = Random - 2;
				EP.ChkBx_SetOption(
						EP.ORXPath("#AddVisit_GHtreatmentReasonFor_CB") + "["
								+ Random + "]", "ON");
			} else {
				EP.ChkBx_SetOption(
						EP.ORXPath("#AddVisit_GHtreatmentReasonFor_CB") + "["
								+ Random + "]", "ON");
			}
			EP.HideCursor();
			EP.DrpDwn_SelectIndex("#AddVisit_FormulationCA_DD",
					EP.RandomValue(1, 2));

			// Reason for Missed Injections
			Random = EP.RandomValue(1, 10);
			if (Random == 8) {
				Random = Random - 1;
				EP.ChkBx_SetOption(
						EP.ORXPath("#AddVisit_GHtreatmentMissedInjection_CB")
								+ "[" + Random + "]", "ON");
			} else {
				EP.ChkBx_SetOption(
						EP.ORXPath("#AddVisit_GHtreatmentMissedInjection_CB")
								+ "[" + Random + "]", "ON");
			}

			Random = EP.RandomValue(1, 5);
			if (Random == 3) {
				Random = Random - 1;
				EP.ChkBx_SetOption(
						EP.ORXPath("#AddVisit_CurrentGHReasonForTemp_CB") + "["
								+ Random + "]", "ON");
			} else {
				EP.ChkBx_SetOption(
						EP.ORXPath("#AddVisit_CurrentGHReasonForTemp_CB") + "["
								+ Random + "]", "ON");
			}
			EP.Btn_Click("#General_AddRecordSave_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN");
			}

			break;

		case GREECE:
			if (IsValid) {
				InputValues = new String[] { "0.05" };
			} else {
				InputValues = new String[] { "1" };
			}
			if (!IsValid) {
				EP.Lnk_Click("#NavigationTab_RecordOverview_LK");
				EP.WaitForPageload();
				EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");
				EP.DrpDwn_SelectIndex("#CompleteTermination_StatusOfStudy_DD",
						2);
				String Date = EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"),
						-1, Calendar.DATE);
				EP.SelectDate(
						"//input[@id='ghIniStopDateTerminate']//following-sibling::img",
						Date);
				EP.Btn_Click("#CompleteTermination_Save_BN");
				if (EP.ObjectExists("#CompleteTermination_Save_BN")) {
					EP.Btn_Click("#CompleteTermination_Save_BN");
				}
				EP.NavigateTo(Env.ECOSURL);
				EP.SearchForPatient(EP.TestData("PatientName") + " 01");
				EP.Rbtn_SetOption("#MyPatients_ECOSPatient_RB", "ON");
				EP.Btn_Click("#MyPatients_VisitsRecord_BN");
				EP.WbTbl_SelectRbtn("#AddVisit_VisitDate_TE", 2, "03 Mar 2013");
				EP.Btn_Click("#AddVisit_Continue_BN");

				EP.ShowTab(TabIndex);
				EP.SelectDate("#AddVisit_CurrentGHTreatmentStartDateIT_IM",
						EP.GetTimeStamp("dd MMM yyyy"));
			}
			EP.Edit_SetValue("#AddVisit_CurrentGHTreatmentDailyDose_EB",
					InputValues[0]);
			EP.DrpDwn_SelectIndex("#AddVisit_CurrentGHFormulationGR_DD",
					EP.RandomValue(1, 3));
			if (IsValid) {
				EP.Btn_Click("#General_First_BN");// click save button
			} else {
				EP.Btn_Click("#General_First_BN");
				EP.Btn_Click("#General_First_BN");
			}
			if (EP.ObjectExists("#General_Delete_IM")) {
				DeleteRecord("#General_Delete_IM", false);
			}
			EP.Btn_Click("#General_Third_BN");
			EP.Rbtn_SetOption("#AddVisit_CurrentGHDoseChangedYes_RB", "ON");
			EP.Rbtn_SetOption("#AddVisit_CurrentGHMissedInjectionYes_RB", "ON");
			EP.Rbtn_SetOption("#AddVisit_CurrentGHTemporaryCessationYes_RB",
					"ON");
			EP.SelectDate("#AddVisit_CurrentGHOthersStopDate_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.Wait(3);
			EP.HideCursor();
			EP.SelectDate("#AddVisit_CurrentGHOthersStartDate_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.HideCursor();
			EP.SelectDate("#AddVisit_CurrentGHTemporaryCessationStopFrom_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.Wait(3);
			EP.HideCursor();
			EP.SelectDate("#AddVisit_CurrentGHTemporaryCessationStopTo_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.ChkBx_SetOption(
					EP.ORXPath("#AddVisit_GHtreatmentMajorityInjection_CB")
							+ "[" + EP.RandomValue(1, 5) + "]", "ON"); // Select
																		// a
																		// random
																		// checkbox
																		// in
																		// Majority
																		// Injections
			EP.Edit_SetValue("#AddVisit_CurrentGHNewDailyDose_EB",
					InputValues[0]);
			Random = EP.RandomValue(1, 7);
			if (Random == 4 || Random == 5) {
				Random = Random - 2;
				EP.ChkBx_SetOption(
						EP.ORXPath("#AddVisit_GHtreatmentReasonFor_CB") + "["
								+ Random + "]", "ON");
			} else {
				EP.ChkBx_SetOption(
						EP.ORXPath("#AddVisit_GHtreatmentReasonFor_CB") + "["
								+ Random + "]", "ON");
			}
			Random = EP.RandomValue(1, 10);
			if (Random == 8) {
				Random = Random - 1;
				EP.ChkBx_SetOption(
						EP.ORXPath("#AddVisit_GHtreatmentMissedInjection_CB")
								+ "[" + Random + "]", "ON");
			} else {
				EP.ChkBx_SetOption(
						EP.ORXPath("#AddVisit_GHtreatmentMissedInjection_CB")
								+ "[" + Random + "]", "ON");
			}

			EP.Edit_SetValue("#AddVisit_CurrentGHNewDailyDose_EB",
					InputValues[0]);
			Random = EP.RandomValue(1, 5);
			if (Random == 3) {
				Random = Random - 1;
				EP.ChkBx_SetOption(
						EP.ORXPath("#AddVisit_CurrentGHReasonForTemp_CB") + "["
								+ Random + "]", "ON");
			} else {
				EP.ChkBx_SetOption(
						EP.ORXPath("#AddVisit_CurrentGHReasonForTemp_CB") + "["
								+ Random + "]", "ON");
			}
			if (IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN");
			} else {
				EP.Btn_Click("#General_AddRecordSave_BN");
				EP.Btn_Click("#General_AddRecordSave_BN");
			}

			break;
		case SLOVAKIA:

			if (IsValid) {
				InputValues = new String[] { "0.05" };
			} else {
				InputValues = new String[] { "1" };
			}
			if (!IsValid) {
				EP.Lnk_Click("#NavigationTab_RecordOverview_LK");
				EP.WaitForPageload();
				EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");
				EP.DrpDwn_SelectIndex("#CompleteTermination_StatusOfStudy_DD",
						2);
				String Date = EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"),
						-1, Calendar.DATE);
				EP.SelectDate(
						"//input[@id='ghIniStopDateTerminate']//following-sibling::img",
						Date);
				EP.Btn_Click("#CompleteTermination_Save_BN");
				if (EP.ObjectExists("#CompleteTermination_Save_BN")) {
					EP.Btn_Click("#CompleteTermination_Save_BN");
				}
				EP.NavigateTo(Env.ECOSURL);
				EP.SearchForPatient(EP.TestData("PatientName") + " 01");
				EP.Rbtn_SetOption("#MyPatients_ECOSPatient_RB", "ON");
				EP.Btn_Click("#MyPatients_VisitsRecord_BN");
				EP.WbTbl_SelectRbtn("#AddVisit_VisitDate_TE", 2, "03 Mar 2013");
				EP.Btn_Click("#AddVisit_Continue_BN");

				EP.ShowTab(TabIndex);
				EP.SelectDate("#AddVisit_CurrentGHTreatmentStartDateIT_IM",
						EP.GetTimeStamp("dd MMM yyyy"));
			}
			EP.Edit_SetValue("#AddVisit_CurrentGHTreatmentDailyDose_EB",
					InputValues[0]);
			EP.ChkBx_SetOption("#AddVisit_CurrentHGFormaultionUK_RB", "ON");

			EP.Btn_Click("#General_First_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_First_BN");
			}

			if (EP.ObjectExists("#General_Delete_IM")) {
				DeleteRecord("#General_Delete_IM", false);
			}
			EP.Btn_Click("#General_Third_BN");
			EP.Rbtn_SetOption("#AddVisit_CurrentGHDoseChangedYes_RB", "ON");
			EP.Rbtn_SetOption("#AddVisit_CurrentGHMissedInjectionYes_RB", "ON");
			EP.Rbtn_SetOption("#AddVisit_CurrentGHTemporaryCessationYes_RB",
					"ON");
			EP.SelectDate("#AddVisit_CurrentGHOthersStopDate_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.Wait(3);
			EP.HideCursor();
			EP.SelectDate("#AddVisit_CurrentGHOthersStartDate_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.HideCursor();
			EP.SelectDate("#AddVisit_CurrentGHTemporaryCessationStopFrom_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.Wait(3);
			EP.HideCursor();
			EP.SelectDate("#AddVisit_CurrentGHTemporaryCessationStopTo_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.ChkBx_SetOption(
					EP.ORXPath("#AddVisit_GHtreatmentMajorityInjection_CB")
							+ "[" + EP.RandomValue(1, 5) + "]", "ON"); // Select
																		// a
																		// random
																		// checkbox
																		// in
																		// Majority
																		// Injections
			EP.Edit_SetValue("#AddVisit_CurrentGHNewDailyDose_EB",
					InputValues[0]);
			Random = EP.RandomValue(1, 7);
			if (Random == 4 || Random == 5) {
				Random = Random - 2;
				EP.ChkBx_SetOption(
						EP.ORXPath("#AddVisit_GHtreatmentReasonFor_CB") + "["
								+ Random + "]", "ON");
			} else {
				EP.ChkBx_SetOption(
						EP.ORXPath("#AddVisit_GHtreatmentReasonFor_CB") + "["
								+ Random + "]", "ON");
			}
			Random = EP.RandomValue(1, 10);
			if (Random == 8) {
				Random = Random - 1;
				EP.ChkBx_SetOption(
						EP.ORXPath("#AddVisit_GHtreatmentMissedInjection_CB")
								+ "[" + Random + "]", "ON");
			} else {
				EP.ChkBx_SetOption(
						EP.ORXPath("#AddVisit_GHtreatmentMissedInjection_CB")
								+ "[" + Random + "]", "ON");
			}

			EP.Edit_SetValue("#AddVisit_CurrentGHNewDailyDose_EB",
					InputValues[0]);
			Random = EP.RandomValue(1, 5);
			if (Random == 3) {
				Random = Random - 1;
				EP.ChkBx_SetOption(
						EP.ORXPath("#AddVisit_CurrentGHReasonForTemp_CB") + "["
								+ Random + "]", "ON");
			} else {
				EP.ChkBx_SetOption(
						EP.ORXPath("#AddVisit_CurrentGHReasonForTemp_CB") + "["
								+ Random + "]", "ON");
			}
			EP.Btn_Click("#General_AddRecordSave_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN");
			}
			break;
		case CZECHREPUBLIC:
			if (IsValid) {
				InputValues = new String[] { "0.05" };
			} else {
				InputValues = new String[] { "1" };
			}
			if (!IsValid) {
				EP.Lnk_Click("#NavigationTab_RecordOverview_LK");
				EP.WaitForPageload();
				EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");
				EP.DrpDwn_SelectIndex("#CompleteTermination_StatusOfStudy_DD",
						2);
				String Date = EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"),
						-1, Calendar.DATE);
				EP.SelectDate(
						"//input[@id='ghIniStopDateTerminate']//following-sibling::img",
						Date);
				EP.Btn_Click("#CompleteTermination_Save_BN");
				if (EP.ObjectExists("#CompleteTermination_Save_BN")) {
					EP.Btn_Click("#CompleteTermination_Save_BN");
				}
				EP.NavigateTo(Env.ECOSURL);
				EP.SearchForPatient(EP.TestData("PatientName") + " 01");
				EP.Rbtn_SetOption("#MyPatients_ECOSPatient_RB", "ON");
				EP.Btn_Click("#MyPatients_VisitsRecord_BN");
				EP.WbTbl_SelectRbtn("#AddVisit_VisitDate_TE", 2, "03 Mar 2013");
				EP.Btn_Click("#AddVisit_Continue_BN");

				EP.ShowTab(TabIndex);
				EP.SelectDate("#AddVisit_CurrentGHTreatmentStartDateIT_IM",
						EP.GetTimeStamp("dd MMM yyyy"));
			}
			EP.Edit_SetValue("#AddVisit_CurrentGHTreatmentDailyDose_EB",
					InputValues[0]);
			EP.Rbtn_SetOption(EP.ORXPath("#AddVisit_CurrentHGFormaultionUK_RB")
					+ "[" + EP.RandomValue(1, 2) + "]", "ON");// Formulation
			if (IsValid) {
				EP.Btn_Click("#General_First_BN");// click save button
			} else {
				EP.Btn_Click("#General_First_BN");
				EP.Btn_Click("#General_First_BN");
			}
			if (EP.ObjectExists("#General_Delete_IM")) {
				DeleteRecord("#General_Delete_IM", false);
			}
			EP.Btn_Click("#General_Third_BN");
			EP.WaitForPageload();
			EP.Rbtn_SetOption("#AddVisit_CurrentGHDoseChangedYes_RB", "ON");
			EP.Rbtn_SetOption("#AddVisit_CurrentGHFormulationChangeYes_RB",
					"ON");
			EP.Rbtn_SetOption("#AddVisit_CurrentGHMissedInjectionYes_RB", "ON");
			EP.Rbtn_SetOption("#AddVisit_CurrentGHTemporaryCessationYes_RB",
					"ON");

			// Select all dates
			EP.SelectDate("#AddVisit_CurrentGHOthersStopDate_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate("#AddVisit_CurrentGHOthersStartDate_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate("#AddVisit_CurrentGHFormaultionStartDate_IM",
					EP.GetTimeStamp("dd MMM yyyy"));// Previous daily dose stop
													// date
			EP.SelectDate("#AddVisit_CurrentGHFormaultionStopDate_IM",
					EP.GetTimeStamp("dd-MMM-yyyy"));// New daily dose start date
			EP.SelectDate("#AddVisit_CurrentGHTemporaryCessationStopFrom_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate("#AddVisit_CurrentGHTemporaryCessationStopTo_IM",
					EP.GetTimeStamp("dd MMM yyyy"));

			EP.ChkBx_SetOption(
					EP.ORXPath("#AddVisit_GHtreatmentMajorityInjection_CB")
							+ "[" + EP.RandomValue(1, 4) + "]", "ON"); // Select
																		// a
																		// random
																		// checkbox
																		// in
																		// Majority
																		// Injections
			// Reason for Dose Change
			EP.ChkBx_SetOption(EP.ORXPath("#AddVisit_GHtreatmentReasonFor_CB")
					+ "[" + EP.RandomValue(1, 3) + "]", "ON");
			EP.Edit_SetValue("#AddVisit_CurrentGHNewDailyDose_EB",
					InputValues[0]);
			EP.Rbtn_SetOption(EP.ORXPath("#AddVisit_FormulationFR_CB") + "["
					+ EP.RandomValue(1, 2) + "]", "ON");// Formulation
			EP.ChkBx_SetOption(
					EP.ORXPath("#AddVisit_GHtreatmentMissedInjection_CB") + "["
							+ EP.RandomValue(1, 7) + "]", "ON");// Reason for
																// Missed
																// Injections
			EP.ChkBx_SetOption(
					EP.ORXPath("#AddVisit_CurrentGHReasonForTemp_CB") + "["
							+ EP.RandomValue(1, 2) + "]", "ON");// Reasons for
																// temporary
																// cessation of
																// treatment

			if (IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN");
			} else {
				EP.Btn_Click("#General_AddRecordSave_BN");
				EP.Btn_Click("#General_AddRecordSave_BN");
			}

			break;
		case HUNGARY:

			if (IsValid) {
				InputValues = new String[] { "0.05" };
			} else {
				InputValues = new String[] { "1" };
			}
			if (!IsValid) {
				EP.Lnk_Click("#NavigationTab_RecordOverview_LK");
				EP.WaitForPageload();
				EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");
				EP.DrpDwn_SelectIndex("#CompleteTermination_StatusOfStudy_DD",
						2);
				String Date = EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"),
						-1, Calendar.DATE);
				EP.SelectDate(
						"//input[@id='ghIniStopDateTerminate']//following-sibling::img",
						Date);
				EP.Btn_Click("#CompleteTermination_Save_BN");
				if (EP.ObjectExists("#CompleteTermination_Save_BN")) {
					EP.Btn_Click("#CompleteTermination_Save_BN");
				}
				EP.NavigateTo(Env.ECOSURL);
				EP.SearchForPatient(EP.TestData("PatientName") + " 01");
				EP.Rbtn_SetOption("#MyPatients_ECOSPatient_RB", "ON");
				EP.Btn_Click("#MyPatients_VisitsRecord_BN");
				EP.WbTbl_SelectRbtn("#AddVisit_VisitDate_TE", 2, "03 Mar 2013");
				EP.Btn_Click("#AddVisit_Continue_BN");

				EP.ShowTab(TabIndex);
				EP.SelectDate("#AddVisit_CurrentGHTreatmentStartDateIT_IM",
						EP.GetTimeStamp("dd MMM yyyy"));
			}

			EP.Edit_SetValue("#AddVisit_CurrentGHTreatmentDailyDose_EB",
					InputValues[0]);
			EP.Rbtn_SetOption(EP.ORXPath("#AddVisit_CurrentHGFormaultionUK_RB")
					+ "[" + EP.RandomValue(1, 2) + "]", "ON");// Formulation
			if (IsValid) {
				EP.Btn_Click("#General_First_BN");// click save button
			} else {
				EP.Btn_Click("#General_First_BN");
				EP.Btn_Click("#General_First_BN");
			}
			if (EP.ObjectExists("#General_Delete_IM")) {
				DeleteRecord("#General_Delete_IM", false);
			}
			EP.Btn_Click("#General_Third_BN");
			EP.WaitForPageload();
			EP.Rbtn_SetOption("#AddVisit_CurrentGHDoseChangedYes_RB", "ON");
			EP.Rbtn_SetOption("#AddVisit_CurrentGHFormulationChangeYes_RB",
					"ON");
			EP.Rbtn_SetOption("#AddVisit_CurrentGHMissedInjectionYes_RB", "ON");
			EP.Rbtn_SetOption("#AddVisit_CurrentGHTemporaryCessationYes_RB",
					"ON");

			// Select all dates
			EP.SelectDate("#AddVisit_CurrentGHOthersStopDate_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.Wait(4);
			EP.HideCursor();
			EP.SelectDate("#AddVisit_CurrentGHOthersStartDate_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.HideCursor();
			EP.SelectDate("#AddVisit_CurrentGHFormaultionStartDate_IM",
					EP.GetTimeStamp("dd MMM yyyy"));// Previous daily dose stop
													// date
			EP.Wait(4);
			EP.HideCursor();
			EP.SelectDate("#AddVisit_CurrentGHFormaultionStopDate_IM",
					EP.GetTimeStamp("dd-MMM-yyyy"));// New daily dose start date
			EP.HideCursor();
			EP.SelectDate("#AddVisit_CurrentGHTemporaryCessationStopFrom_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.Wait(4);
			EP.HideCursor();
			EP.SelectDate("#AddVisit_CurrentGHTemporaryCessationStopTo_IM",
					EP.GetTimeStamp("dd MMM yyyy"));

			EP.ChkBx_SetOption(
					EP.ORXPath("#AddVisit_GHtreatmentMajorityInjection_CB")
							+ "[" + EP.RandomValue(1, 3) + "]", "ON"); // Select
																		// a
																		// random
																		// checkbox
																		// in
																		// Majority
																		// Injections
			// Reason for Dose Change
			EP.ChkBx_SetOption(EP.ORXPath("#AddVisit_GHtreatmentReasonFor_CB")
					+ "[" + EP.RandomValue(1, 3) + "]", "ON");
			EP.Edit_SetValue("#AddVisit_CurrentGHNewDailyDose_EB",
					InputValues[0]);
			EP.Rbtn_SetOption(EP.ORXPath("#AddVisit_FormulationFR_CB") + "["
					+ EP.RandomValue(1, 2) + "]", "ON");// Formulation
			EP.ChkBx_SetOption(
					EP.ORXPath("#AddVisit_GHtreatmentMissedInjection_CB") + "["
							+ EP.RandomValue(1, 7) + "]", "ON");// Reason for
																// Missed
																// Injections
			EP.ChkBx_SetOption(
					EP.ORXPath("#AddVisit_CurrentGHReasonForTemp_CB") + "["
							+ EP.RandomValue(1, 2) + "]", "ON");// Reasons for
																// temporary
																// cessation of
																// treatment
			EP.Btn_Click("#General_AddRecordSave_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN");
			}

			break;
		case NORWAY:
			if (IsValid) {
				InputValues = new String[] { "0.05" };
			} else {
				InputValues = new String[] { "1" };
			}
			if (!IsValid) {
				EP.Lnk_Click("#NavigationTab_RecordOverview_LK");
				EP.WaitForPageload();
				EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");
				EP.DrpDwn_SelectIndex("#CompleteTermination_StatusOfStudy_DD",
						2);
				String Date = EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"),
						-1, Calendar.DATE);
				EP.SelectDate(
						"//input[@id='ghIniStopDateTerminate']//following-sibling::img",
						Date);
				EP.Btn_Click("#CompleteTermination_Save_BN");
				if (EP.ObjectExists("#CompleteTermination_Save_BN")) {
					EP.Btn_Click("#CompleteTermination_Save_BN");
				}
				EP.NavigateTo(Env.ECOSURL);
				EP.SearchForPatient(EP.TestData("PatientName") + " 01");
				EP.Rbtn_SetOption("#MyPatients_ECOSPatient_RB", "ON");
				EP.Btn_Click("#MyPatients_VisitsRecord_BN");
				EP.WbTbl_SelectRbtn("#AddVisit_VisitDate_TE", 2, "03 Mar 2013");
				EP.Btn_Click("#AddVisit_Continue_BN");

				EP.ShowTab(TabIndex);
				EP.SelectDate("#AddVisit_CurrentGHTreatmentStartDateIT_IM",
						EP.GetTimeStamp("dd MMM yyyy"));
			}

			EP.Edit_SetValue("#AddVisit_CurrentGHTreatmentDailyDose_EB",
					InputValues[0]);
			EP.Rbtn_SetOption(EP.ORXPath("#AddVisit_CurrentHGFormaultionUK_RB")
					+ "[" + EP.RandomValue(1, 2) + "]", "ON");// Formulation
			if (IsValid) {
				EP.Btn_Click("#General_First_BN");// click save button
			} else {
				EP.Btn_Click("#General_First_BN");
				EP.Btn_Click("#General_First_BN");
			}
			if (EP.ObjectExists("#General_Delete_IM")) {
				DeleteRecord("#General_Delete_IM", false);
			}
			EP.Btn_Click("#General_Third_BN");
			EP.WaitForPageload();
			EP.Rbtn_SetOption("#AddVisit_CurrentGHDoseChangedYes_RB", "ON");
			EP.Rbtn_SetOption("#AddVisit_CurrentGHFormulationChangeYes_RB",
					"ON");
			EP.Rbtn_SetOption("#AddVisit_CurrentGHMissedInjectionYes_RB", "ON");
			EP.Rbtn_SetOption("#AddVisit_CurrentGHTemporaryCessationYes_RB",
					"ON");

			EP.Edit_SetValue("#AddVisit_CurrentGHNewDailyDose_EB",
					InputValues[0]);
			EP.Rbtn_SetOption(EP.ORXPath("#AddVisit_FormulationFR_CB") + "["
					+ EP.RandomValue(1, 2) + "]", "ON");// Formulation
			// Select all dates
			EP.SelectDate("#AddVisit_CurrentGHOthersStopDate_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.Wait(4);
			EP.SelectDate("#AddVisit_CurrentGHOthersStartDate_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate("#AddVisit_CurrentGHFormaultionStopDate_IM",
					EP.GetTimeStamp("dd-MMM-yyyy"));// New daily dose start date
			EP.Wait(5);
			EP.SelectDate("#AddVisit_CurrentGHFormaultionStartDate_IM",
					EP.GetTimeStamp("dd MMM yyyy"));// Previous daily dose stop
													// date
			EP.SelectDate("#AddVisit_CurrentGHTemporaryCessationStopFrom_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.Wait(5);
			EP.SelectDate("#AddVisit_CurrentGHTemporaryCessationStopTo_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			if (IsValid) {
				EP.ChkBx_SetOption(
						EP.ORXPath("#AddVisit_GHtreatmentMajorityInjection_CB")
								+ "[" + EP.RandomValue(1, 3) + "]", "ON"); // Select
																			// a
																			// random
																			// checkbox
																			// in
																			// Majority
																			// Injections
				// Reason for Dose Change
				EP.ChkBx_SetOption(
						EP.ORXPath("#AddVisit_GHtreatmentReasonFor_CB") + "["
								+ EP.RandomValue(1, 3) + "]", "ON");
				EP.ChkBx_SetOption(
						EP.ORXPath("#AddVisit_GHtreatmentMissedInjection_CB")
								+ "[" + EP.RandomValue(1, 7) + "]", "ON");// Reason
																			// for
																			// Missed
																			// Injections
				EP.ChkBx_SetOption(
						EP.ORXPath("#AddVisit_CurrentGHReasonForTemp_CB") + "["
								+ EP.RandomValue(1, 2) + "]", "ON");// Reasons
																	// for
																	// temporary
																	// cessation
																	// of
																	// treatment
			} else {
				// Remove all the checkbox
				EP.ChkBx_SetOptionForAll(
						"//div[@class='RecordOverview']//input[@class='checkbox']",
						"OFF");

				// Select the Required Checkboxes
				EP.ChkBx_SetOption("#AddVisit_CurrentGHAdverseEvent_CB", "ON");
				EP.ChkBx_SetOption("#AddVisit_CurrentGHOther_CB", "ON");
				EP.ChkBx_SetOption("#AddVisit_CurrentGHOthers_CB", "ON");
				EP.ChkBx_SetOption(
						"#AddVisit_CurrentGHReasonsOthersPlsSpecify_CB", "ON");
				EP.Btn_Click("#General_AddRecordSave_BN"); // Click save
			}
			EP.Btn_Click("#General_AddRecordSave_BN");
			break;

		case CANADA:

			if (IsValid) {
				InputValues = new String[] { "0.05" };
			} else {
				InputValues = new String[] { "1" };
			}
			/*
			 * EP.Edit_SetValue("#AddVisit_CurrentGHTreatmentDailyDose_EB",
			 * InputValues[0]); if(IsValid){
			 * EP.Btn_Click("#General_First_BN");//click save button }else{
			 * EP.Btn_Click("#General_First_BN");
			 * EP.Btn_Click("#General_First_BN"); }
			 */
			if (EP.ObjectExists("#General_Delete_IM")) {
				DeleteRecord("#General_Delete_IM", false);
			}
			EP.Btn_Click("#General_First_BN");
			EP.Wait(1);
			EP.Rbtn_SetOption("#AddVisit_CurrentGHDoseChangedYes_RB", "ON");
			EP.Rbtn_SetOption("#AddVisit_CurrentGHFormulationChangeYes_RB",
					"ON");
			EP.Rbtn_SetOption("#AddVisit_CurrentGHMissedInjectionYes_RB", "ON");
			EP.Rbtn_SetOption("#AddVisit_CurrentGHTemporaryCessationYes_RB",
					"ON");

			// Check Chapter Status - Complete
			EP.ChkBx_SetOption(
					EP.ORXPath("#AddVisit_GHtreatmentMajorityInjection_CB")
							+ "[" + EP.RandomValue(1, 5) + "]", "ON"); // Select
																		// a
																		// random
																		// checkbox
																		// in
																		// Majority
																		// Injections
			Random = EP.RandomValue(1, 7);// Reason for Dose Change
			if (Random == 5 || Random == 4) {
				Random = Random - 2;
				EP.ChkBx_SetOption(
						EP.ORXPath("#AddVisit_CurrentGHReasonForDose_CB") + "["
								+ Random + "]", "ON");
			} else {
				EP.ChkBx_SetOption(
						EP.ORXPath("#AddVisit_CurrentGHReasonForDose_CB") + "["
								+ Random + "]", "ON");
			}
			EP.Edit_SetValue("#AddVisit_CurrentGHNewDailyDose_EB",
					InputValues[0]);
			EP.SelectDate("#AddVisit_CurrentGHOthersStopDate_IM", EP
					.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"), -1,
							Calendar.DATE));
			EP.Wait(3);
			EP.SelectDate("#AddVisit_CurrentGHOthersStartDate_IM", EP
					.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"), -1,
							Calendar.DATE));
			EP.SelectDate("#AddVisit_CurrentGHFormaultionStopDate_IM", EP
					.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"), -1,
							Calendar.DATE));
			EP.Wait(3);
			EP.SelectDate("#AddVisit_CurrentGHFormaultionStartDate_IM", EP
					.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"), -1,
							Calendar.DATE));
			EP.SelectDate("#AddVisit_CurrentGHTemporaryCessationStopFrom_IM",
					EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"), -1,
							Calendar.DATE));
			EP.Wait(3);
			EP.SelectDate("#AddVisit_CurrentGHTemporaryCessationStopTo_IM", EP
					.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"), -1,
							Calendar.DATE));
			EP.DrpDwn_SelectIndex("#AddVisit_Formulation_DD",
					EP.RandomValue(1, 2));// Formulation Dropdown
			Random = EP.RandomValue(1, 10);// Reason for Missed Injections
			if (Random == 8) {
				Random = Random - 1;
				EP.ChkBx_SetOption(
						EP.ORXPath("#AddVisit_GHtreatmentMissedInjection_CB")
								+ "[" + Random + "]", "ON");
			} else {
				EP.ChkBx_SetOption(
						EP.ORXPath("#AddVisit_GHtreatmentMissedInjection_CB")
								+ "[" + Random + "]", "ON");
			}

			// Reasons for temporary cessation of treatment
			Random = EP.RandomValue(1, 5);
			if (Random == 3) {
				Random = Random - 1;
				EP.ChkBx_SetOption(
						EP.ORXPath("#AddVisit_CurrentGHReasonForTemp_CB") + "["
								+ Random + "]", "ON");
			} else {
				EP.ChkBx_SetOption(
						EP.ORXPath("#AddVisit_CurrentGHReasonForTemp_CB") + "["
								+ Random + "]", "ON");
			}

			if (IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN");
			} else {
				EP.Btn_Click("#General_AddRecordSave_BN");
				EP.Btn_Click("#General_AddRecordSave_BN");
			}
			break;

		case ARGENTINA:
		case COLOMBIA:

			if (IsValid) {
				InputValues = new String[] { "0.05" };
			} else {
				InputValues = new String[] { "1" };
			}
			/*
			 * EP.Edit_SetValue("#AddVisit_CurrentGHTreatmentDailyDose_EB",
			 * InputValues[0]); if(IsValid){
			 * EP.Btn_Click("#General_First_BN");//click save button }else{
			 * EP.Btn_Click("#General_First_BN");
			 * EP.Btn_Click("#General_First_BN"); }
			 */
			if (EP.ObjectExists("#General_Delete_IM")) {
				DeleteRecord("#General_Delete_IM", false);
			}
			EP.Btn_Click("#General_First_BN");
			EP.Wait(1);
			EP.Rbtn_SetOption("#AddVisit_CurrentGHDoseChangedYes_RB", "ON");
			EP.Rbtn_SetOption("#AddVisit_CurrentGHMissedInjectionYes_RB", "ON");
			EP.Rbtn_SetOption("#AddVisit_CurrentGHTemporaryCessationYes_RB",
					"ON");

			// Check Chapter Status - Complete
			EP.ChkBx_SetOption(
					EP.ORXPath("#AddVisit_GHtreatmentMajorityInjection_CB")
							+ "[" + EP.RandomValue(1, 3) + "]", "ON"); // Select
																		// a
																		// random
																		// checkbox
																		// in
																		// Majority
																		// Injections
			Random = EP.RandomValue(1, 5);// Reason for Dose Change
			if (Random == 5 || Random == 4) {
				Random = Random - 2;
				EP.ChkBx_SetOption(
						EP.ORXPath("#AddVisit_CurrentGHReasonForDose_CB") + "["
								+ Random + "]", "ON");
			} else {
				EP.ChkBx_SetOption(
						EP.ORXPath("#AddVisit_CurrentGHReasonForDose_CB") + "["
								+ Random + "]", "ON");
			}
			EP.Edit_SetValue("#AddVisit_CurrentGHNewDailyDose_EB",
					InputValues[0]);
			EP.SelectDate("#AddVisit_CurrentGHOthersStartDateTW_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.Wait(3);
			EP.HideCursor();
			EP.SelectDate("#AddVisit_CurrentGHOthersStopDate_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.HideCursor();
			EP.SelectDate("#AddVisit_CurrentGHTemporaryCessationStopFrom_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.Wait(3);
			EP.HideCursor();
			EP.SelectDate("#AddVisit_CurrentGHTemporaryCessationStopTo_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.HideCursor();

			EP.DrpDwn_SelectIndex("#AddVisit_Formulation_DD",
					EP.RandomValue(1, 2));// Formulation Dropdown
			Random = EP.RandomValue(1, 8);// Reason for Missed Injections
			if (Random == 8) {
				Random = Random - 1;
				EP.ChkBx_SetOption(
						EP.ORXPath("#AddVisit_GHtreatmentMissedInjection_CB")
								+ "[" + Random + "]", "ON");
			} else {
				EP.ChkBx_SetOption(
						EP.ORXPath("#AddVisit_GHtreatmentMissedInjection_CB")
								+ "[" + Random + "]", "ON");
			}

			// Reasons for temporary cessation of treatment
			Random = EP.RandomValue(1, 3);
			if (Random == 3) {
				Random = Random - 1;
				EP.ChkBx_SetOption(
						EP.ORXPath("#AddVisit_CurrentGHReasonForTemp_CB") + "["
								+ Random + "]", "ON");
			} else {
				EP.ChkBx_SetOption(
						EP.ORXPath("#AddVisit_CurrentGHReasonForTemp_CB") + "["
								+ Random + "]", "ON");
			}

			if (IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN");
			} else {
				EP.Btn_Click("#General_AddRecordSave_BN");
				EP.Btn_Click("#General_AddRecordSave_BN");
			}
			break;

		case SWEDEN:

			if (IsValid) {
				InputValues = new String[] { "0.05" };
			} else {
				InputValues = new String[] { "1" };
			}
			if (EP.ObjectExists("#General_Delete_IM")) {
				DeleteRecord("#General_Delete_IM", false);
			}

			EP.Btn_Click("#General_First_BN"); // Click Add button

			EP.Rbtn_SetOption("#AddVisit_CurrentGHDoseChangedYes_RB", "ON");// Dose
																			// changed
																			// since
																			// previous
																			// visit?
			EP.Rbtn_SetOption("#AddVisit_CurrentGHFormulationChangeYes_RB",
					"ON");// Formulation change since previous visit?
			EP.Rbtn_SetOption("#AddVisit_CurrentGHMissedInjectionYes_RB", "ON");// Missed
																				// Injection
																				// since
																				// previous
																				// visit?

			EP.ChkBx_SetOption(
					EP.ORXPath("#AddVisit_GHtreatmentMajorityInjection_CB")
							+ "[" + EP.RandomValue(1, 5) + "]", "ON"); // Select
																		// a
																		// random
																		// checkbox
																		// in
																		// Majority
																		// Injections
			// String dailyDose = EP.Converter("0.05");
			EP.Edit_SetValue("#AddVisit_CurrentGHNewDailyDose_EB",
					EP.Converter(InputValues[0])); // Enter a value of
													// Formulation Unit
			EP.SelectDate("#AddVisit_CurrentGHTreatmentStartDate_IM",
					EP.TestData("ECOSPatientDOB"));// Previous daily dose stop
													// date
			EP.SelectDate("#AddVisit_CurrentGHTreatmentStopDate_IM", EP
					.Date_AddDays(EP.TestData("ECOSPatientDOB"), 5,
							Calendar.DATE));// New daily dose start date
			EP.SelectDate("#AddVisit_CurrentGHFormaultionStartDate_IM",
					EP.TestData("ECOSPatientDOB")); // New Formulation Start
													// Date
			EP.SelectDate("#AddVisit_CurrentGHFormaultionStopDate_IM", EP
					.Date_AddDays(EP.TestData("ECOSPatientDOB"), 6,
							Calendar.DATE));// New Formulation Stop Date
			EP.Rbtn_SetOption("#AddVisit_CurrentGHTemporaryCessationYes_RB",
					"ON");
			EP.SelectDate("#AddVisit_IGF1StatusDateBeforeGH_IM",
					EP.TestData("ECOSPatientDOB"));// Cessation of treatment
													// stop from
			EP.SelectDate("#AddVisit_PriorHbA1cStatusDate_IM", EP.Date_AddDays(
					EP.TestData("ECOSPatientDOB"), 5, Calendar.DATE));// Cessation
																		// of
																		// treatment
																		// stop
																		// to

			if (IsValid) {

				Random = EP.RandomValue(1, 7);
				String Text = EP.GetText(EP
						.ORXPath("#AddVisit_CurrentGHReasonForDose_CB")
						+ "["
						+ Random + "]" + "//following-sibling::span");
				if (EP.CompareText(Text, EP.LangData("AdverseEvent"))
						|| EP.CompareText(Text, EP.LangData("XMLOther"))) {
					Random = Random - 2;
					EP.ChkBx_SetOption(
							EP.ORXPath("#AddVisit_CurrentGHReasonForDose_CB")
									+ "[" + Random + "]", "ON");

				} else {
					EP.ChkBx_SetOption(
							EP.ORXPath("#AddVisit_CurrentGHReasonForDose_CB")
									+ "[" + Random + "]", "ON");

				}

				EP.DrpDwn_SelectIndex("#AddVisit_Formulation_DD",
						EP.RandomValue(1, 3));

				// EP.ChkBx_SetOption(TabName+"CurrentGHMissedReasonForgot_CB",
				// "ON"); //Select the checkbox for missed injection, needed for
				// table verication

				Random = EP.RandomValue(1, 10);
				Text = EP.GetText(EP
						.ORXPath("#AddVisit_GHtreatmentMissedInjection_CB")
						+ "[" + Random + "]" + "//following-sibling::span");

				if (!EP.CompareText(Text, EP.LangData("OtherPleaseSpecify"))) {
					// if(Text!=EP.LangData("OtherPleaseSpecify"))//Reason for
					// Missed Injections Checkboxes

					EP.ChkBx_SetOption(
							EP.ORXPath("#AddVisit_GHtreatmentMissedInjection_CB")
									+ "[" + Random + "]", "ON");

				} else {
					Random = Random - 2;
					EP.ChkBx_SetOption(
							EP.ORXPath("#AddVisit_GHtreatmentMissedInjection_CB")
									+ "[" + Random + "]", "ON");

				}

				Random = EP.RandomValue(1, 5);
				Text = EP.GetText(EP
						.ORXPath("#AddVisit_CurrentGHReasonForTemp_CB")
						+ "["
						+ Random + "]" + "//following-sibling::span");
				if (!EP.CompareText(Text, EP.LangData("OtherPleaseSpecify"))) {
					EP.ChkBx_SetOption(
							EP.ORXPath("#AddVisit_CurrentGHReasonForTemp_CB")
									+ "[" + Random + "]", "ON");
				} else {
					Random = Random - 2;
					EP.ChkBx_SetOption(
							EP.ORXPath("#AddVisit_CurrentGHReasonForTemp_CB")
									+ "[" + Random + "]", "ON");

				}
				EP.Btn_Click("#General_AddRecordSave_BN"); // Click save
			} else {
				// Remove all the checkbox
				EP.ChkBx_SetOptionForAll(
						"//div[@class='RecordOverview']//input[@class='checkbox']",
						"OFF");

				// Select the Required Checkboxes
				EP.ChkBx_SetOption("#AddVisit_CurrentGHAdverseEvent_CB", "ON");
				EP.ChkBx_SetOption("#AddVisit_CurrentGHOther_CB", "ON");
				EP.ChkBx_SetOption("#AddVisit_CurrentGHOthers_CB", "ON");
				EP.ChkBx_SetOption(
						"#AddVisit_CurrentGHReasonsOthersPlsSpecify_CB", "ON");
				EP.Btn_Click("#General_AddRecordSave_BN"); // Click save
				EP.Btn_Click("#General_AddRecordSave_BN"); // Click save
			}

			break;

		case AUSTRALIA:
			if (IsValid) {
				InputValues = new String[] { "0.05" };
			} else {
				InputValues = new String[] { "1" };
			}
			if (!IsValid) {
				EP.Lnk_Click("#NavigationTab_RecordOverview_LK");
				EP.WaitForPageload();
				EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");
				EP.DrpDwn_SelectIndex("#CompleteTermination_StatusOfStudy_DD",
						2);
				String Date = EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"),
						-1, Calendar.DATE);
				EP.SelectDate(
						"//input[@id='ghIniStopDateTerminate']//following-sibling::img",
						Date);
				EP.Btn_Click("#CompleteTermination_Save_BN");
				if (EP.ObjectExists("#CompleteTermination_Save_BN")) {
					EP.Btn_Click("#CompleteTermination_Save_BN");
				}
				EP.NavigateTo(Env.ECOSURL);
				EP.SearchForPatient(EP.TestData("PatientName") + " 01");
				EP.Rbtn_SetOption("#MyPatients_ECOSPatient_RB", "ON");
				EP.Btn_Click("#MyPatients_VisitsRecord_BN");
				EP.WbTbl_SelectRbtn("#AddVisit_VisitDate_TE", 2, "03 Mar 2013");
				EP.Btn_Click("#AddVisit_Continue_BN");

				EP.ShowTab(TabIndex);
				EP.SelectDate("#AddVisit_CurrentGHTreatmentStartDateIT_IM",
						EP.GetTimeStamp("dd MMM yyyy"));
			}
			EP.Edit_SetValue("#AddVisit_CurrentGHTreatmentDailyDose_EB",
					InputValues[0]);
			EP.Rbtn_SetOption(EP.ORXPath("#AddVisit_FormulationSaizen_RB")
					+ "[" + EP.RandomValue(1, 2) + "]", "ON");
			EP.Btn_Click("#General_First_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_First_BN");
			}

			if (EP.ObjectExists("#General_Delete_IM")) {
				DeleteRecord("#General_Delete_IM", false);
			}

			EP.Btn_Click("#General_Third_BN"); // Click Add button

			EP.Rbtn_SetOption("#AddVisit_CurrentGHDoseChangedYes_RB", "ON");// Dose
																			// changed
																			// since
																			// previous
																			// visit?
			EP.Rbtn_SetOption("#AddVisit_CurrentGHFormulationChangeYes_RB",
					"ON");// Formulation change since previous visit?
			EP.Rbtn_SetOption("#AddVisit_CurrentGHMissedInjectionYes_RB", "ON");// Missed
																				// Injection
																				// since
																				// previous
																				// visit?
			EP.Rbtn_SetOption("#AddVisit_CurrentGHTemporaryCessationYes_RB",
					"ON");

			EP.ChkBx_SetOption(
					EP.ORXPath("#AddVisit_GHtreatmentMajorityInjection_CB")
							+ "[" + EP.RandomValue(1, 3) + "]", "ON"); // Select
																		// a
																		// random
																		// checkbox
																		// in
																		// Majority
																		// Injections
			// String dailyDose = EP.Converter("0.05");
			EP.Edit_SetValue("#AddVisit_CurrentGHNewDailyDose_EB",
					EP.Converter(InputValues[0])); // Enter a value of
													// Formulation Unit

			EP.SelectDate("#AddVisit_CurrentGHTreatmentStopDate_IM", EP
					.Date_AddDays(EP.TestData("ECOSPatientDOB"), 5,
							Calendar.DATE));// New daily dose start date
			EP.Wait(3);
			EP.HideCursor();
			EP.SelectDate("#AddVisit_CurrentGHTreatmentStartDate_IM",
					EP.TestData("ECOSPatientDOB"));// Previous daily dose stop
													// date

			EP.SelectDate("#AddVisit_CurrentGHFormaultionStopDate_IM", EP
					.Date_AddDays(EP.TestData("ECOSPatientDOB"), 6,
							Calendar.DATE));// New Formulation Stop Date
			EP.Wait(3);
			EP.HideCursor();
			EP.SelectDate("#AddVisit_CurrentGHFormaultionStartDate_IM",
					EP.TestData("ECOSPatientDOB")); // New Formulation Start
													// Date

			EP.SelectDate("#AddVisit_PriorHbA1cStatusDate_IM", EP.Date_AddDays(
					EP.TestData("ECOSPatientDOB"), 5, Calendar.DATE));// Cessation
																		// of
																		// treatment
																		// stop
																		// to
			EP.Wait(3);
			EP.HideCursor();
			EP.SelectDate("#AddVisit_IGF1StatusDateBeforeGH_IM",
					EP.TestData("ECOSPatientDOB"));// Cessation of treatment
													// stop from

			if (IsValid) {

				Random = EP.RandomValue(1, 3);
				EP.ChkBx_SetOption(
						EP.ORXPath("#AddVisit_CurrentGHReasonForDose_CB") + "["
								+ Random + "]", "ON");

				EP.Rbtn_SetOption(
						EP.ORXPath("#AddVisit_CurrentGHFormulation_RB") + "["
								+ EP.RandomValue(1, 2) + "]", "ON");

				Random = EP.RandomValue(1, 7);

				EP.ChkBx_SetOption(
						EP.ORXPath("#AddVisit_GHtreatmentMissedInjection_CB")
								+ "[" + Random + "]", "ON");

				// EP.ChkBx_SetOption(TabName+"CurrentGHMissedReasonForgot_CB",
				// "ON"); //Select the checkbox for missed injection, needed for
				// table verication
				EP.Rbtn_SetOption(
						EP.ORXPath("#AddVisit_AddTreatmentStopAccordancePhysician_RB")
								+ "[" + EP.RandomValue(1, 2) + "]", "ON");

				Random = EP.RandomValue(1, 2);
				EP.ChkBx_SetOption(
						EP.ORXPath("#AddVisit_CurrentGHReasonForTemp_CB") + "["
								+ Random + "]", "ON");

			} else {
				// Remove all the checkbox
				EP.ChkBx_SetOptionForAll(
						"//div[@class='RecordOverview']//input[@class='checkbox']",
						"OFF");

				// Select the Required Checkboxes
				EP.ChkBx_SetOption("#AddVisit_CurrentGHAdverseEvent_CB", "ON");
				EP.ChkBx_SetOption("#AddVisit_CurrentGHOther_CB", "ON");
				EP.ChkBx_SetOption("#AddVisit_CurrentGHOthers_CB", "ON");
				EP.ChkBx_SetOption(
						"#AddVisit_CurrentGHReasonsOthersPlsSpecify_CB", "ON");
				EP.Btn_Click("#General_AddRecordSave_BN"); // Click save
			}
			EP.Btn_Click("#General_AddRecordSave_BN");
			break;

		case FINLAND:
			if (IsValid) {
				InputValues = new String[] { "0.05" };
			} else {
				InputValues = new String[] { "1" };
			}
			if (!IsValid) {
				EP.Lnk_Click("#NavigationTab_RecordOverview_LK");
				EP.WaitForPageload();
				EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");
				EP.DrpDwn_SelectIndex("#CompleteTermination_StatusOfStudy_DD",
						2);
				String Date = EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"),
						-1, Calendar.DATE);
				EP.SelectDate(
						"//input[@id='ghIniStopDateTerminate']//following-sibling::img",
						Date);
				EP.Btn_Click("#CompleteTermination_Save_BN");
				if (EP.ObjectExists("#CompleteTermination_Save_BN")) {
					EP.Btn_Click("#CompleteTermination_Save_BN");
				}
				EP.NavigateTo(Env.ECOSURL);
				EP.SearchForPatient(EP.TestData("PatientName") + " 01");
				EP.Rbtn_SetOption("#MyPatients_ECOSPatient_RB", "ON");
				EP.Btn_Click("#MyPatients_VisitsRecord_BN");
				EP.WbTbl_SelectRbtn("#AddVisit_VisitDate_TE", 2, "03 Mar 2013");
				EP.Btn_Click("#AddVisit_Continue_BN");

				EP.ShowTab(TabIndex);
				EP.SelectDate("#AddVisit_CurrentGHTreatmentStartDateIT_IM",
						EP.GetTimeStamp("dd MMM yyyy"));
			}
			EP.Edit_SetValue("#AddVisit_CurrentGHTreatmentDailyDose_EB",
					InputValues[0]);
			EP.DrpDwn_SelectIndex("#AddVisit_FormulationCA_DD",
					EP.RandomValue(1, 2));
			EP.Btn_Click("#General_First_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_First_BN");
			}

			if (EP.ObjectExists("#General_Delete_IM")) {
				DeleteRecord("#General_Delete_IM", false);
			}

			EP.Btn_Click("#General_Third_BN"); // Click Add button

			EP.Rbtn_SetOption("#AddVisit_CurrentGHDoseChangedYes_RB", "ON");// Dose
																			// changed
																			// since
																			// previous
																			// visit?
			EP.Rbtn_SetOption("#AddVisit_CurrentGHFormulationChangeYes_RB",
					"ON");// Formulation change since previous visit?
			EP.Rbtn_SetOption("#AddVisit_CurrentGHMissedInjectionYes_RB", "ON");// Missed
																				// Injection
																				// since
																				// previous
																				// visit?
			EP.Rbtn_SetOption("#AddVisit_CurrentGHTemporaryCessationYes_RB",
					"ON");

			EP.ChkBx_SetOption(
					EP.ORXPath("#AddVisit_GHtreatmentMajorityInjection_CB")
							+ "[" + EP.RandomValue(1, 3) + "]", "ON"); // Select
																		// a
																		// random
																		// checkbox
																		// in
																		// Majority
																		// Injections
			EP.Edit_SetValue("#AddVisit_CurrentGHNewDailyDose_EB",
					EP.Converter(InputValues[0])); // Enter a value of
													// Formulation Unit

			EP.SelectDate("#AddVisit_CurrentGHTreatmentStopDate_IM", EP
					.Date_AddDays(EP.TestData("ECOSPatientDOB"), 5,
							Calendar.DATE));// New daily dose start date
			EP.HideCursor();
			EP.SelectDate("#AddVisit_CurrentGHTreatmentStartDate_IM",
					EP.TestData("ECOSPatientDOB"));// Previous daily dose stop
													// date

			EP.SelectDate("#AddVisit_CurrentGHFormaultionStopDate_IM", EP
					.Date_AddDays(EP.TestData("ECOSPatientDOB"), 6,
							Calendar.DATE));// New Formulation Stop Date
			EP.HideCursor();
			EP.SelectDate("#AddVisit_CurrentGHFormaultionStartDate_IM",
					EP.TestData("ECOSPatientDOB")); // New Formulation Start
													// Date

			EP.SelectDate("#AddVisit_PriorHbA1cStatusDate_IM", EP.Date_AddDays(
					EP.TestData("ECOSPatientDOB"), 5, Calendar.DATE));// Cessation
																		// of
																		// treatment
																		// stop
																		// to
			EP.HideCursor();
			EP.SelectDate("#AddVisit_IGF1StatusDateBeforeGH_IM",
					EP.TestData("ECOSPatientDOB"));// Cessation of treatment
													// stop from

			if (IsValid) {

				Random = EP.RandomValue(1, 3);
				EP.ChkBx_SetOption(
						EP.ORXPath("#AddVisit_CurrentGHReasonForDose_CB") + "["
								+ Random + "]", "ON");

				EP.Rbtn_SetOption(
						EP.ORXPath("#AddVisit_CurrentGHFormulation_RB") + "["
								+ EP.RandomValue(1, 2) + "]", "ON");

				Random = EP.RandomValue(1, 7);

				EP.ChkBx_SetOption(
						EP.ORXPath("#AddVisit_GHtreatmentMissedInjection_CB")
								+ "[" + Random + "]", "ON");
				EP.Rbtn_SetOption(
						EP.ORXPath("#AddVisit_AddTreatmentStopAccordancePhysician_RB")
								+ "[" + EP.RandomValue(1, 2) + "]", "ON");

				Random = EP.RandomValue(1, 2);
				EP.ChkBx_SetOption(
						EP.ORXPath("#AddVisit_CurrentGHReasonForTemp_CB") + "["
								+ Random + "]", "ON");

			} else {
				// Remove all the checkbox
				EP.ChkBx_SetOptionForAll(
						"//div[@class='RecordOverview']//input[@class='checkbox']",
						"OFF");

				// Select the Required Checkboxes
				EP.ChkBx_SetOption("#AddVisit_CurrentGHAdverseEvent_CB", "ON");
				EP.ChkBx_SetOption("#AddVisit_CurrentGHOther_CB", "ON");
				EP.ChkBx_SetOption("#AddVisit_CurrentGHOthers_CB", "ON");
				EP.ChkBx_SetOption(
						"#AddVisit_CurrentGHReasonsOthersPlsSpecify_CB", "ON");
				EP.Btn_Click("#General_AddRecordSave_BN"); // Click save
			}
			EP.Btn_Click("#General_AddRecordSave_BN");
			break;

		case KOREA:
			if (IsValid) {
				InputValues = new String[] { EP.Converter("0.05") };
			} else {
				InputValues = new String[] { EP.Converter("1") };
			}

			if (EP.ObjectExists("#General_Delete_IM")) {
				DeleteRecord("#General_Delete_IM", false);
			}
			EP.SelectDate("#AddVisit_CurrentGHTreatmentStopDate_IM",
					EP.Date_AddDays(PatientDOB, 5, Calendar.DATE));
			EP.Edit_SetValue("#AddVisit_CurrentGHTreatmentDailyDose_EB",
					InputValues[0]);
			EP.Btn_Click("#General_First_BN");// click save button
			if (!IsValid) {
				EP.Btn_Click("#General_First_BN");// click save button
			}

			EP.Btn_Click("#General_Third_BN"); // Click Add button

			EP.ChkBx_SetOption(
					EP.ORXPath("#AddVisit_GHtreatmentMajorityInjection_CB")
							+ "[" + EP.RandomValue(1, 3) + "]", "ON"); // Select
																		// a
																		// random
																		// checkbox
																		// in
																		// Majority
																		// Injections

			EP.Rbtn_SetOption("#AddVisit_CurrentGHDoseChangedYes_RB", "ON");
			// EP.Rbtn_SetOption(TabName+"CurrentGHFormulationChangeYes_RB",
			// "ON");
			EP.Rbtn_SetOption("#AddVisit_CurrentGHMissedInjectionYes_RB", "ON");
			EP.Rbtn_SetOption("#AddVisit_CurrentGHTemporaryCessationYes_RB",
					"ON");

			EP.Edit_SetValue("#AddVisit_CurrentGHNewDailyDose_EB",
					EP.Converter(InputValues[0])); // Enter a value of
													// Formulation Unit
			EP.ChkBx_SetOption("#AddVisit_CurrentGHPuberty_CB", "ON");

			EP.SelectDate("#AddVisit_CurrentGHTreatmentStopDate_IM",
					EP.GetTimeStamp("dd MMM yyyy"));// New daily dose start date
			EP.SelectDate("#AddVisit_CurrentGHTreatmentStartDate_IM",
					EP.GetTimeStamp("dd MMM yyyy"));// Previous daily dose stop
													// date
			// EP.Rbtn_SetOption("#AddVisit_CurrentGHTemporaryCessationYes_RB",
			// "ON");

			EP.SelectDate("#AddVisit_CurrentGHTemporaryCessationStopTo_IM",
					EP.GetTimeStamp("dd MMM yyyy"));// Cessation of treatment
													// stop to
			EP.SelectDate("#AddVisit_CurrentGHTemporaryCessationStopFrom_IM",
					EP.GetTimeStamp("dd MMM yyyy")); // Enter Formulation Date
			EP.ChkBx_SetOption("#AddVisit_MedicalReason_CB", "ON");

			EP.ChkBx_SetOption(
					EP.ORXPath("#AddVisit_GHtreatmentMissedInjection_CB") + "["
							+ EP.RandomValue(1, 7) + "]", "ON");
			EP.Btn_Click("#General_AddRecordSave_BN"); // Click save
			if (!IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN");// click save button
			}

			break;
		case ICELAND:
			if (IsValid) {
				InputValues = new String[] { "0.05" };
			} else {
				InputValues = new String[] { "1" };
			}
			if (!IsValid) {
				EP.Lnk_Click("#NavigationTab_RecordOverview_LK");
				EP.WaitForPageload();
				EP.Btn_Click("#NavigationTab_CompleteTerminateStudy_BN");
				EP.DrpDwn_SelectIndex("#CompleteTermination_StatusOfStudy_DD",
						2);
				String Date = EP.Date_AddDays(EP.GetTimeStamp("dd MMM yyyy"),
						-1, Calendar.DATE);
				EP.SelectDate(
						"//input[@id='ghIniStopDateTerminate']//following-sibling::img",
						Date);
				EP.Btn_Click("#CompleteTermination_Save_BN");
				EP.NavigateTo(Env.ECOSURL);
				EP.SearchForPatient(EP.TestData("PatientName") + " 01");
				EP.Rbtn_SetOption("#MyPatients_ECOSPatient_RB", "ON");
				EP.Btn_Click("#MyPatients_VisitsRecord_BN");
				EP.WbTbl_SelectRbtn("#AddVisit_VisitDate_TE", 2, "03 Mar 2013");
				EP.Btn_Click("#AddVisit_Continue_BN");

				EP.ShowTab(TabIndex);
				EP.SelectDate("#AddVisit_CurrentGHTreatmentStartDateIT_IM",
						EP.GetTimeStamp("dd MMM yyyy"));
			}
			EP.Edit_SetValue("#AddVisit_CurrentGHTreatmentDailyDose_EB",
					InputValues[0]);
			EP.ChkBx_SetOption("#AddVisit_SaizenFreezeDriedIT_CB", "ON");
			EP.Btn_Click("#General_First_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_First_BN");
			}

			if (EP.ObjectExists("#General_Delete_IM")) {
				DeleteRecord("#General_Delete_IM", false);
			}

			EP.Btn_Click("#General_Third_BN"); // Click Add button

			EP.Rbtn_SetOption("#AddVisit_CurrentGHDoseChangedYes_RB", "ON");// Dose
																			// changed
																			// since
																			// previous
																			// visit?
			EP.Rbtn_SetOption("#AddVisit_CurrentGHMissedInjectionYes_RB", "ON");// Missed
																				// Injection
																				// since
																				// previous
																				// visit?
			EP.Rbtn_SetOption("#AddVisit_CurrentGHTemporaryCessationYes_RB",
					"ON");

			EP.ChkBx_SetOption(
					EP.ORXPath("#AddVisit_GHtreatmentMajorityInjection_CB")
							+ "[" + EP.RandomValue(1, 3) + "]", "ON"); // Select
																		// a
																		// random
																		// checkbox
																		// in
																		// Majority
																		// Injections
			// String dailyDose = EP.Converter("0.05");
			EP.Edit_SetValue("#AddVisit_CurrentGHNewDailyDose_EB",
					EP.Converter(InputValues[0])); // Enter a value of
													// Formulation Unit

			EP.SelectDate("#AddVisit_CurrentGHTreatmentStopDate_IM", EP
					.Date_AddDays(EP.TestData("ECOSPatientDOB"), 5,
							Calendar.DATE));// New daily dose start date
			EP.SelectDate("#AddVisit_CurrentGHTreatmentStartDate_IM",
					EP.TestData("ECOSPatientDOB"));// Previous daily dose stop
													// date

			EP.SelectDate("#AddVisit_CurrentGHTemporaryCessationStopFrom_IM",
					EP.TestData("ECOSPatientDOB"));// Cessation of treatment
													// stop from
			EP.SelectDate("#AddVisit_CurrentGHTemporaryCessationStopTo_IM", EP
					.Date_AddDays(EP.TestData("ECOSPatientDOB"), 5,
							Calendar.DATE));// Cessation of treatment stop to

			if (IsValid) {
				Random = EP.RandomValue(1, 3);
				EP.ChkBx_SetOption(
						EP.ORXPath("#AddVisit_CurrentGHReasonForDose_CB") + "["
								+ Random + "]", "ON");

				Random = EP.RandomValue(1, 7);

				EP.ChkBx_SetOption(
						EP.ORXPath("#AddVisit_GHtreatmentMissedInjection_CB")
								+ "[" + Random + "]", "ON");

				// EP.ChkBx_SetOption(TabName+"CurrentGHMissedReasonForgot_CB",
				// "ON"); //Select the checkbox for missed injection, needed for
				// table verication
				EP.Rbtn_SetOption(
						EP.ORXPath("#AddVisit_AddTreatmentStopAccordancePhysician_RB")
								+ "[" + EP.RandomValue(1, 2) + "]", "ON");

				Random = EP.RandomValue(1, 2);
				EP.ChkBx_SetOption(
						EP.ORXPath("#AddVisit_CurrentGHReasonForTemp_CB") + "["
								+ Random + "]", "ON");

			} else {
				// Remove all the checkbox
				EP.ChkBx_SetOptionForAll(
						"//div[@class='RecordOverview']//input[@class='checkbox']",
						"OFF");

				// Select the Required Checkboxes
				EP.ChkBx_SetOption("#AddVisit_CurrentGHAdverseEvent_CB", "ON");
				EP.ChkBx_SetOption("#AddVisit_CurrentGHOther_CB", "ON");
				EP.ChkBx_SetOption("#AddVisit_CurrentGHOthers_CB", "ON");
				EP.ChkBx_SetOption(
						"#AddVisit_CurrentGHReasonsOthersPlsSpecify_CB", "ON");
				EP.Btn_Click("#General_AddRecordSave_BN"); // Click save
			}
			EP.Btn_Click("#General_AddRecordSave_BN");
			break;
		// case CHINA:
		// case ARGENTINA:

		case GERMANY:

			if (IsValid) {
				InputValues = new String[] { "0.05" };
			} else {
				InputValues = new String[] { "1" };
			}

			if (EP.ObjectExists("#General_Delete_IM")) {
				DeleteRecord("#General_Delete_IM", false);
			}
			EP.SelectDate("#AddVisit_CurrentGHTreatmentStartDate_IM",
					EP.Date_AddDays(PatientDOB, 5, Calendar.DATE));
			EP.SelectDate("#AddVisit_CurrentGHTreatmentStopDate_IM",
					EP.Date_AddDays(PatientDOB, 10, Calendar.DATE));
			EP.Edit_SetValue("#AddVisit_CurrentGHTreatmentDailyDose_EB",
					InputValues[0]);
			if (IsValid) {
				EP.Btn_Click("#General_Second_BN");// click save button
			} else {
				EP.Btn_Click("#General_Second_BN");
				EP.Btn_Click("#General_Second_BN");
			}

			EP.Btn_Click("#General_Fourth_BN"); // Click Add button

			EP.ChkBx_SetOption(
					EP.ORXPath("#AddVisit_GHtreatmentMajorityInjection_CB")
							+ "[" + EP.RandomValue(1, 3) + "]", "ON"); // Select
																		// a
																		// random
																		// checkbox
																		// in
																		// Majority
																		// Injections

			EP.Edit_SetValue("#AddVisit_CurrentGHNewDailyDose_EB",
					EP.Converter(InputValues[0])); // Enter a value of
													// Formulation Unit
			EP.ChkBx_SetOption("#AddVisit_CurrentGHPuberty_CB", "ON");
			EP.SelectDate("#AddVisit_CurrentGHTreatmentStartDate_IM",
					EP.TestData("ECOSPatientDOB"));// Previous daily dose stop
													// date
			String Date = EP.Date_AddDays(EP.TestData("ECOSPatientDOB"), 5,
					Calendar.MONTH);
			EP.SelectDate("#AddVisit_CurrentGHTreatmentStopDate_IM", Date);// New
																			// daily
																			// dose
																			// start
																			// date
			EP.Rbtn_SetOption("#AddVisit_CurrentGHTemporaryCessationYes_RB",
					"ON");
			EP.SelectDate(
					"#CompleteTermination_DiscontinuedLastStudyDayCalendar_TT",
					EP.TestData("ECOSPatientDOB"));// Cessation of treatment
													// stop from
			EP.SelectDate("#AddVisit_CurrentGHFormaultionStopDate_IM", Date);// Cessation
																				// of
																				// treatment
																				// stop
																				// to
			EP.ChkBx_SetOption("#AddVisit_MedicalReason_CB", "ON");
			EP.SelectDate("#AddVisit_CurrentGHOthersStopDate_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate("#AddVisit_CurrentGHOthersStartDate_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate("#AddVisit_CurrentGHFormaultionStartDate_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate("#AddVisit_CurrentGHFormaultionStopDate_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate("#AddVisit_CurrentGHTemporaryCessationStopFrom_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			EP.SelectDate("#AddVisit_CurrentGHTemporaryCessationStopTo_IM",
					EP.GetTimeStamp("dd MMM yyyy"));
			if (IsValid) {
				EP.ChkBx_SetOption(
						EP.ORXPath("AddVisitGHtreatmentMissedInjection_CB")
								+ "[" + EP.RandomValue(1, 7) + "]", "ON");
				EP.Btn_Click("#General_AddRecordSave_BN"); // Click save
			} else {
				EP.ChkBx_SetOption("#AddVisit_CurrentGHOther_CB", "ON");
				EP.ChkBx_SetOption("#AddVisit_CurrentGHOthers_CB", "ON");

				EP.Btn_Click("#General_AddRecordSave_BN");
				EP.Btn_Click("#General_AddRecordSave_BN");
			}

			break;

		default:
		}
		VerifySuccessMessage(EP.LangData("CurrentGHTreatment"));

	}

	public void TannerStageAdd(boolean IsValid, String TabIndex, String DOB,
			boolean isNotAvailable, String PatientGender,
			String TannerMenarche, String VisitDate) {
		PatientGender = PatientGender.toUpperCase();
		TannerMenarche = TannerMenarche.toUpperCase();
		// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*TANNER
		// STAGE*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
		EP.ShowTab(TabIndex);
		String[] InputValues;

		switch (switchCountryName) {

		case SINGAPORE:
			if (IsValid) {
				InputValues = new String[] {
						EP.Date_AddDays(EP.TestData("ECOSPatientDOBFemale"), 2,
								Calendar.YEAR), "2", "2" };
			} else {
				InputValues = new String[] {
						EP.Date_AddDays(DOB, -2, Calendar.YEAR), "89", "89" };
			}
			if (EP.ObjectExists("#General_Delete_IM")) {
				DeleteRecord("#General_Delete_IM", false);
			}
			EP.Btn_Click("#General_First_BN"); // Click Add button

			if (IsValid) {
				EP.DrpDwn_SelectIndex(
						"#AddVisit_TannerStagePubertalAssessmentGenitalStage_DD",
						EP.DrpDwn_GetCount("#AddVisit_TannerStagePubertalAssessmentGenitalStage_DD") - 1);
			} else {
				if (VisitDate.compareTo("02-Mar-2010") == 0) {
					EP.DrpDwn_SelectIndex(
							"#AddVisit_TannerStagePubertalAssessmentGenitalStage_DD",
							EP.DrpDwn_GetCount("#AddVisit_TannerStagePubertalAssessmentGenitalStage_DD") - 1);
				} else if (VisitDate.compareTo("03-Mar-2010") == 0) {
					EP.DrpDwn_SelectIndex(
							"#AddVisit_TannerStagePubertalAssessmentGenitalStage_DD",
							1);
				}
			}
			if (PatientGender.compareTo("MALE") == 0) {
				// Male
				EP.Edit_SetValue("#AddVisit_TannerStageTestisVolLeft_EB",
						InputValues[1]);
				EP.Edit_SetValue("#AddVisit_TannerStageTestisVolRight_EB",
						InputValues[2]);
			} else {
				// Female
				if (TannerMenarche.endsWith("YES")) {
					EP.DrpDwn_SelectIndex("#AddVisit_TannerStageMenarche_DD", 1);
					EP.Edit_SetValue("#AddVisit_TannerStageMenarcheYearAT_EB",
							InputValues[1]);
					EP.Edit_SetValue("#AddVisit_TannerStageMenarcheMonthAT_EB",
							InputValues[2]);
				}
				if (TannerMenarche.endsWith("NO")) {
					EP.DrpDwn_SelectIndex("#AddVisit_TannerStageMenarche_DD", 2);
				}
				if (TannerMenarche.endsWith("NOT AVAILABLE")) {
					EP.DrpDwn_SelectIndex("#AddVisit_TannerStageMenarche_DD", 2); // "Not available"
																					// is
																					// not
																					// available
																					// there
																					// so
																					// selecting
																					// No.
				}
			}
			if (!isNotAvailable) {
				EP.Rbtn_SetOption(
						"#AddVisit_TannerStageHasTheDateEnteredPubertyYesAT_RB",
						"ON");
				EP.WaitForPageload();
				EP.SelectDate(
						"#AddVisit_TannerStageEnteredPuberty_IM",
						EP.Date_AddDays(
								EP.Edit_GetValue("#AddVisit_TannerStageAssesmentDate_EB"),
								-1, Calendar.DATE));
			} else {
				EP.Rbtn_SetOption(
						"#AddVisit_TannerStageHasTheDateEnteredPubertyNoAT_RB",
						"ON");
			}
			EP.Btn_Click("#General_AddRecordSave_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN"); // Click Save button
			}
			break;
		case AUSTRIA:
		case UNITEDARABEMIRATES:
		case SAUDIARABIA:
		case MEXICO:
			if (IsValid) {
				InputValues = new String[] {
						EP.Date_AddDays(EP.TestData("ECOSPatientDOBFemale"), 2,
								Calendar.YEAR), "2", "2" };
			} else {
				InputValues = new String[] {
						EP.Date_AddDays(DOB, -2, Calendar.YEAR), "89", "89" };
			}
			if (EP.ObjectExists("#General_Delete_IM")) {
				DeleteRecord("#General_Delete_IM", false);
			}
			EP.Btn_Click("#General_First_BN"); // Click Add button

			if (IsValid) {
				EP.DrpDwn_SelectIndex(
						"#AddVisit_TannerStagePubertalAssessmentGenitalStage_DD",
						EP.DrpDwn_GetCount("#AddVisit_TannerStagePubertalAssessmentGenitalStage_DD") - 1);
			} else {
				if (VisitDate.compareTo("02-Mar-2010") == 0) {
					EP.DrpDwn_SelectIndex(
							"#AddVisit_TannerStagePubertalAssessmentGenitalStage_DD",
							EP.DrpDwn_GetCount("#AddVisit_TannerStagePubertalAssessmentGenitalStage_DD") - 1);
				} else if (VisitDate.compareTo("03-Mar-2010") == 0) {
					EP.DrpDwn_SelectIndex(
							"#AddVisit_TannerStagePubertalAssessmentGenitalStage_DD",
							1);
				}
			}
			if (PatientGender.compareTo("MALE") == 0) {
				// Male
				EP.Edit_SetValue("#AddVisit_TannerStageTestisVolLeft_EB",
						InputValues[1]);
				EP.Edit_SetValue("#AddVisit_TannerStageTestisVolRight_EB",
						InputValues[2]);
			} else {
				// Female
				if (TannerMenarche.endsWith("YES")) {
					EP.DrpDwn_SelectIndex("#AddVisit_TannerStageMenarche_DD", 1);
					EP.Edit_SetValue("#AddVisit_TannerStageMenarcheYearAT_EB",
							InputValues[1]);
					EP.Edit_SetValue("#AddVisit_TannerStageMenarcheMonthAT_EB",
							InputValues[2]);
				}
				if (TannerMenarche.endsWith("NO")) {
					EP.DrpDwn_SelectIndex("#AddVisit_TannerStageMenarche_DD", 2);
				}
				if (TannerMenarche.endsWith("NOT AVAILABLE")) {
					EP.DrpDwn_SelectIndex("#AddVisit_TannerStageMenarche_DD", 2); // "Not available"
																					// is
																					// not
																					// available
																					// there
																					// so
																					// selecting
																					// No.
				}
			}
			if (!isNotAvailable) {
				EP.Rbtn_SetOption(
						"#AddVisit_TannerStageHasTheDateEnteredPubertyYesAT_RB",
						"ON");
				EP.WaitForPageload();
				EP.SelectDate(
						"#AddVisit_TannerStageEnteredPuberty_IM",
						EP.Date_AddDays(
								EP.Edit_GetValue("#AddVisit_TannerStageAssesmentDate_EB"),
								-1, Calendar.DATE));
			} else {
				EP.Rbtn_SetOption(
						"#AddVisit_TannerStageHasTheDateEnteredPubertyNoAT_RB",
						"ON");
			}
			EP.Btn_Click("#General_AddRecordSave_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN"); // Click Save button
			}
			break;

		case FRANCE:
			if (IsValid) {
				InputValues = new String[] {
						EP.Date_AddDays(EP.TestData("ECOSPatientDOBFemale"), 2,
								Calendar.YEAR), "2", "2" };
			} else {
				InputValues = new String[] {
						EP.Date_AddDays(DOB, -2, Calendar.YEAR), "89", "89" };
			}

			if (EP.ObjectExists("#General_Delete_IM")) {
				DeleteRecord("#General_Delete_IM", false);
			}
			EP.Btn_Click("#General_First_BN"); // Click Add button
			if (IsValid) {
				EP.DrpDwn_SelectIndex(
						"#AddVisit_TannerStagePubertalAssessmentGenitalStage_DD",
						EP.DrpDwn_GetCount("#AddVisit_TannerStagePubertalAssessmentGenitalStage_DD") - 1);
			} else {
				if (VisitDate.compareTo("02-Mar-2010") == 0) {
					EP.DrpDwn_SelectIndex(
							"#AddVisit_TannerStagePubertalAssessmentGenitalStage_DD",
							EP.DrpDwn_GetCount("#AddVisit_TannerStagePubertalAssessmentGenitalStage_DD") - 1);
				} else if (VisitDate.compareTo("03-Mar-2010") == 0) {
					EP.DrpDwn_SelectIndex(
							"#AddVisit_TannerStagePubertalAssessmentGenitalStage_DD",
							1);
				}
			}

			if (PatientGender.compareTo("MALE") == 0) {
				// Male
				EP.Edit_SetValue("#AddVisit_TannerStageTestisVolLeft_EB",
						InputValues[1]);
				EP.Edit_SetValue("#AddVisit_TannerStageTestisVolRight_EB",
						InputValues[2]);
			} else {
				// Female
				if (TannerMenarche.endsWith("YES")) {
					// EP.DrpDwn_SelectIndex("#AddVisit_TannerStageMenarche_DD",
					// 1);
					EP.Rbtn_SetOption("#AddVisit_TannerStageMenarcheYesAT_RB",
							"ON");
					EP.Edit_SetValue("#AddVisit_TannerStageMenarcheYearAT_EB",
							InputValues[1]);
					EP.Edit_SetValue("#AddVisit_TannerStageMenarcheMonthAT_EB",
							InputValues[2]);
				}
				if (TannerMenarche.endsWith("NO")) {
					EP.Rbtn_SetOption("#AddVisit_TannerStageMenarcheNoAT_RB",
							"ON");
				}
				if (TannerMenarche.endsWith("NOT AVAILABLE")) {
					EP.Rbtn_SetOption("#AddVisit_TannerStageMenarcheNoAT_RB",
							"ON");
				}
			}
			if (!isNotAvailable) {
				EP.Rbtn_SetOption(
						"#AddVisit_TannerStageHasTheDateEnteredPubertyYesAT_RB",
						"ON");
				EP.WaitForPageload();
				EP.SelectDate(
						"#AddVisit_TannerStageEnteredPuberty_IM",
						EP.Date_AddDays(
								EP.Edit_GetValue("#AddVisit_TannerStageAssesmentDate_EB"),
								-1, Calendar.DATE));
			}
			if (IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN"); // Click Save button
			} else {
				EP.Btn_Click("#General_AddRecordSave_BN");
				EP.Btn_Click("#General_AddRecordSave_BN");
			}
			break;

		case ITALY:
			if (IsValid) {
				InputValues = new String[] {
						EP.Date_AddDays(EP.TestData("ECOSPatientDOBFemale"), 2,
								Calendar.YEAR), "2", "2" };
			} else {
				InputValues = new String[] {
						EP.Date_AddDays(DOB, -2, Calendar.YEAR), "89", "89" };
			}
			if (EP.ObjectExists("#General_Delete_IM")) {
				DeleteRecord("#General_Delete_IM", false);
			}
			EP.Btn_Click("#General_First_BN"); // Click Add button

			if (IsValid) {
				EP.DrpDwn_SelectIndex(
						"#AddVisit_TannerStagePubertalAssessmentGenitalStage_DD",
						EP.DrpDwn_GetCount("#AddVisit_TannerStagePubertalAssessmentGenitalStage_DD") - 1);
			} else {
				if (VisitDate.compareTo("02-Mar-2010") == 0) {
					EP.DrpDwn_SelectIndex(
							"#AddVisit_TannerStagePubertalAssessmentGenitalStage_DD",
							EP.DrpDwn_GetCount("#AddVisit_TannerStagePubertalAssessmentGenitalStage_DD") - 1);
				} else if (VisitDate.compareTo("03-Mar-2010") == 0) {
					EP.DrpDwn_SelectIndex(
							"#AddVisit_TannerStagePubertalAssessmentGenitalStage_DD",
							1);
				}
			}
			if (PatientGender.compareTo("MALE") == 0) {
				// Male
				EP.Edit_SetValue("#AddVisit_TannerStageTestisVolLeft_EB",
						InputValues[1]);
				EP.Edit_SetValue("#AddVisit_TannerStageTestisVolRight_EB",
						InputValues[2]);
			} else {
				// Female
				if (TannerMenarche.endsWith("YES")) {
					EP.DrpDwn_SelectIndex("#AddVisit_TannerStageMenarche_DD", 1);
					EP.Edit_SetValue("#AddVisit_TannerStageMenarcheYearAT_EB",
							InputValues[1]);
					EP.Edit_SetValue("#AddVisit_TannerStageMenarcheMonthAT_EB",
							InputValues[2]);
				}
				if (TannerMenarche.endsWith("NO")) {
					EP.DrpDwn_SelectIndex("#AddVisit_TannerStageMenarche_DD", 2);
				}
				if (TannerMenarche.endsWith("NOT AVAILABLE")) {
					EP.DrpDwn_SelectIndex("#AddVisit_TannerStageMenarche_DD", 2); // "Not available"
																					// is
																					// not
																					// available
																					// there
																					// so
																					// selecting
																					// No.
				}
			}
			if (!isNotAvailable) {
				EP.Rbtn_SetOption(
						"#AddVisit_TannerStageHasTheDateEnteredPubertyYesAT_RB",
						"ON");
				EP.WaitForPageload();
				EP.SelectDate(
						"#AddVisit_TannerStageEnteredPuberty_IM",
						EP.Date_AddDays(
								EP.Edit_GetValue("#AddVisit_TannerStageAssesmentDate_EB"),
								-1, Calendar.DATE));
			} else {
				EP.Rbtn_SetOption(
						"#AddVisit_TannerStageHasTheDateEnteredPubertyNoAT_RB",
						"ON");
			}
			if (IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN"); // Click Save button
			} else {
				EP.Btn_Click("#General_AddRecordSave_BN");
				EP.Btn_Click("#General_AddRecordSave_BN");
			}
			break;

		case SPAIN:
			if (IsValid) {
				InputValues = new String[] {
						EP.Date_AddDays(EP.TestData("ECOSPatientDOBFemale"), 2,
								Calendar.YEAR), "2", "2" };
			} else {
				InputValues = new String[] {
						EP.Date_AddDays(DOB, -2, Calendar.YEAR), "89", "89" };
			}
			if (EP.ObjectExists("#General_Delete_IM")) {
				DeleteRecord("#General_Delete_IM", false);
			}
			EP.Btn_Click("#General_First_BN"); // Click Add button

			if (IsValid) {
				EP.DrpDwn_SelectIndex(
						"#AddVisit_TannerStagePubertalAssessmentGenitalStage_DD",
						5);
			} else {
				if (VisitDate.compareTo("02-Mar-2010") == 0) {
					EP.DrpDwn_SelectIndex(
							"#AddVisit_TannerStagePubertalAssessmentGenitalStage_DD",
							5);
				} else if (VisitDate.compareTo("03-Mar-2010") == 0) {
					EP.DrpDwn_SelectIndex(
							"#AddVisit_TannerStagePubertalAssessmentGenitalStage_DD",
							1);
				}
			}
			if (PatientGender.compareTo("MALE") == 0) {
				// Male
				EP.Edit_SetValue("#AddVisit_TannerStageTestisVolLeft_EB",
						InputValues[1]);
				EP.Edit_SetValue("#AddVisit_TannerStageTestisVolRight_EB",
						InputValues[2]);
			} else {
				// Female
				if (TannerMenarche.endsWith("YES")) {
					EP.DrpDwn_SelectIndex("#AddVisit_TannerStageMenarche_DD", 1);
					EP.Edit_SetValue("#AddVisit_TannerStageMenarcheYearAT_EB",
							InputValues[1]);
					EP.Edit_SetValue("#AddVisit_TannerStageMenarcheMonthAT_EB",
							InputValues[2]);
				}
				if (TannerMenarche.endsWith("NO")) {
					EP.DrpDwn_SelectIndex("#AddVisit_TannerStageMenarche_DD", 2);
				}
				if (TannerMenarche.endsWith("NOT AVAILABLE")) {
					EP.DrpDwn_SelectIndex("#AddVisit_TannerStageMenarche_DD", 2); // "Not available"
																					// is
																					// not
																					// available
																					// there
																					// so
																					// selecting
																					// No.
				}
			}
			if (!isNotAvailable) {
				EP.Rbtn_SetOption(
						"#AddVisit_TannerStageHasTheDateEnteredPubertyYesAT_RB",
						"ON");
				EP.WaitForPageload();
				EP.SelectDate(
						"#AddVisit_TannerStageEnteredPuberty_IM",
						EP.Date_AddDays(
								EP.Edit_GetValue("#AddVisit_TannerStageAssesmentDate_EB"),
								-1, Calendar.DATE));
				EP.Wait(3);
				EP.HideCursor();
			} else {
				EP.Rbtn_SetOption(
						"#AddVisit_TannerStageHasTheDateEnteredPubertyNoAT_RB",
						"ON");
			}
			EP.Btn_Click("#General_AddRecordSave_BN"); // Click Save button
			if (!IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN"); // Click Save button
			}
			break;

		case UNITEDKINGDOM:
			if (IsValid) {
				InputValues = new String[] {
						EP.Date_AddDays(EP.TestData("ECOSPatientDOBFemale"), 2,
								Calendar.YEAR), "2", "2" };
			} else {
				InputValues = new String[] {
						EP.Date_AddDays(DOB, -2, Calendar.YEAR), "89", "89" };
			}

			if (EP.ObjectExists("#General_Delete_IM")) {
				DeleteRecord("#General_Delete_IM", false);
			}
			EP.Btn_Click("#General_First_BN"); // Click Add button
			if (isNotAvailable) {
				// EP.DrpDwn_SelectIndex("#AddVisit_TannerStageAxillaryHair_DD",
				// 4);
				// EP.DrpDwn_SelectIndex("#AddVisit_TannerStagePubicHairStage_DD",
				// 7);
				EP.DrpDwn_SelectIndex(
						"#AddVisit_TannerStagePubertalAssessmentGenitalStage_DD",
						7);
			} else {
				// EP.DrpDwn_SelectIndex("#AddVisit_TannerStageAxillaryHair_DD",
				// 3);
				// EP.DrpDwn_SelectIndex("#AddVisit_TannerStagePubicHairStage_DD",
				// 6);
				EP.DrpDwn_SelectIndex(
						"#AddVisit_TannerStagePubertalAssessmentGenitalStage_DD",
						5);
			}

			if (PatientGender.compareTo("MALE") == 0) {
				// Male
				EP.Edit_SetValue("#AddVisit_TannerStageTestisVolLeft_EB",
						InputValues[1]);
				EP.Edit_SetValue("#AddVisit_TannerStageTestisVolRight_EB",
						InputValues[2]);

			} else {
				// Female
				if (TannerMenarche.endsWith("YES")) {
					TannerMenarche = "1";
				}
				if (TannerMenarche.endsWith("NO")) {
					TannerMenarche = "2";
				}
				if (TannerMenarche.endsWith("NOT AVAILABLE")) {
					TannerMenarche = "3";
				}
				EP.DrpDwn_SelectIndex("#AddVisit_TannerStageMenarche_DD",
						Integer.valueOf(TannerMenarche));
				if (TannerMenarche.compareTo("1") == 0) {
					EP.SelectDate("#AddVisit_TannerStageMenarcheDate_IM",
							InputValues[0]);
				}
			}
			if (!isNotAvailable) {
				EP.ChkBx_SetOption("#AddVisit_TannerStageEnteredPuberty_CB",
						"ON");
				EP.Wait(2);
				EP.WaitForPageload();
				EP.SelectDate(
						"#AddVisit_TannerStageEnteredPuberty_IM",
						EP.Date_AddDays(
								EP.Edit_GetValue("#AddVisit_TannerStageAssesmentDate_EB"),
								-1, Calendar.DATE));
			}
			if (IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN"); // Click Save button
			} else {
				EP.Btn_Click("#General_AddRecordSave_BN");
				EP.Btn_Click("#General_AddRecordSave_BN");
			}
			break;
		case ARGENTINA:
		case COLOMBIA:
			if (IsValid) {
				InputValues = new String[] {
						EP.Date_AddDays(EP.TestData("ECOSPatientDOBFemale"), 2,
								Calendar.YEAR), "2", "2" };
			} else {
				InputValues = new String[] {
						EP.Date_AddDays(DOB, -2, Calendar.YEAR), "89", "89" };
			}

			if (EP.ObjectExists("#General_Delete_IM")) {
				DeleteRecord("#General_Delete_IM", false);
			}
			EP.Btn_Click("#General_First_BN"); // Click Add button
			if (isNotAvailable) {

				EP.DrpDwn_SelectIndex(
						"#AddVisit_TannerStagePubertalAssessmentGenitalStage_DD",
						5);
			} else {

				EP.DrpDwn_SelectIndex(
						"#AddVisit_TannerStagePubertalAssessmentGenitalStageCA_DD",
						5);
			}

			if (PatientGender.compareTo("MALE") == 0) {
				// Male
				EP.Edit_SetValue("#AddVisit_TannerStageTestisVolLeft_EB",
						InputValues[1]);
				EP.Edit_SetValue("#AddVisit_TannerStageTestisVolRight_EB",
						InputValues[2]);

			} else {
				// Female
				if (TannerMenarche.endsWith("YES")) {
					TannerMenarche = "1";
				}
				if (TannerMenarche.endsWith("NO")) {
					TannerMenarche = "2";
				}
				if (TannerMenarche.endsWith("NOT AVAILABLE")) {
					TannerMenarche = "3";
				}
				EP.DrpDwn_SelectIndex("#AddVisit_TannerStageMenarche_DD",
						Integer.valueOf(TannerMenarche));
				if (TannerMenarche.compareTo("1") == 0) {
					EP.SelectDate("#AddVisit_TannerStageMenarcheDate_IM",
							InputValues[0]);
				}
			}
			if (!isNotAvailable) {
				EP.Rbtn_SetOption(
						"#AddVisit_TannerStageHasTheDateEnteredPubertyYesAT_RB",
						"ON");
				EP.WaitForPageload();
				EP.SelectDate(
						"#AddVisit_TannerStageEnteredPuberty_IM",
						EP.Date_AddDays(
								EP.Edit_GetValue("#AddVisit_TannerStageAssesmentDate_EB"),
								-1, Calendar.DATE));
				EP.Wait(3);
				EP.HideCursor();
			} else {
				EP.Rbtn_SetOption(
						"#AddVisit_TannerStageHasTheDateEnteredPubertyNoAT_RB",
						"ON");
			}
			EP.Btn_Click("#General_AddRecordSave_BN"); // Click Save button
			if (!IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN"); // Click Save button
			}
			break;

		case GREECE:
			if (IsValid) {
				InputValues = new String[] {
						EP.Date_AddDays(EP.TestData("ECOSPatientDOBFemale"), 2,
								Calendar.YEAR), "2", "2" };
			} else {
				InputValues = new String[] {
						EP.Date_AddDays(DOB, -2, Calendar.YEAR), "89", "89" };
			}

			if (EP.ObjectExists("#General_Delete_IM")) {
				DeleteRecord("#General_Delete_IM", false);
			}
			EP.Btn_Click("#General_First_BN"); // Click Add button

			if (IsValid) {
				EP.DrpDwn_SelectIndex(
						"#AddVisit_TannerStagePubertalAssessmentGenitalStage_DD",
						EP.DrpDwn_GetCount("#AddVisit_TannerStagePubertalAssessmentGenitalStage_DD") - 1);
			} else {
				if (VisitDate.compareTo("02-Mar-2010") == 0) {
					EP.DrpDwn_SelectIndex(
							"#AddVisit_TannerStagePubertalAssessmentGenitalStage_DD",
							5);
				} else if (VisitDate.compareTo("03-Mar-2010") == 0) {
					EP.DrpDwn_SelectIndex(
							"#AddVisit_TannerStagePubertalAssessmentGenitalStage_DD",
							1);
				}
			}
			if (PatientGender.compareTo("MALE") == 0) {
				// Male
				EP.Edit_SetValue("#AddVisit_TannerStageTestisVolLeft_EB",
						InputValues[1]);
				EP.Edit_SetValue("#AddVisit_TannerStageTestisVolRight_EB",
						InputValues[2]);
			} else {
				// Female
				if (TannerMenarche.endsWith("YES")) {
					EP.DrpDwn_SelectIndex("#AddVisit_TannerStageMenarche_DD", 1);
					EP.Edit_SetValue("#AddVisit_TannerStageMenarcheYearAT_EB",
							InputValues[1]);
					EP.Edit_SetValue("#AddVisit_TannerStageMenarcheMonthAT_EB",
							InputValues[2]);
				}
				if (TannerMenarche.endsWith("NO")) {
					EP.DrpDwn_SelectIndex("#AddVisit_TannerStageMenarche_DD", 2);
				}
				if (TannerMenarche.endsWith("NOT AVAILABLE")) {
					EP.DrpDwn_SelectIndex("#AddVisit_TannerStageMenarche_DD", 2); // "Not available"
																					// is
																					// not
																					// available
																					// there
																					// so
																					// selecting
																					// No.
				}
			}
			if (!isNotAvailable) {
				EP.Rbtn_SetOption(
						"#AddVisit_TannerStageHasTheDateEnteredPubertyYesAT_RB",
						"ON");
				EP.WaitForPageload();
				EP.SelectDate(
						"#AddVisit_TannerStageEnteredPuberty_IM",
						EP.Date_AddDays(
								EP.Edit_GetValue("#AddVisit_TannerStageAssesmentDate_EB"),
								-1, Calendar.DATE));
			} else {
				EP.Rbtn_SetOption(
						"#AddVisit_TannerStageHasTheDateEnteredPubertyNoAT_RB",
						"ON");
			}
			if (IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN"); // Click Save button
			} else {
				EP.Btn_Click("#General_AddRecordSave_BN");
				EP.Btn_Click("#General_AddRecordSave_BN");
			}

			break;
		case SLOVAKIA:

			if (IsValid) {
				InputValues = new String[] {
						EP.Date_AddDays(EP.TestData("ECOSPatientDOBFemale"), 2,
								Calendar.YEAR), "2", "2" };
			} else {
				InputValues = new String[] {
						EP.Date_AddDays(DOB, -2, Calendar.YEAR), "89", "89" };
			}

			if (EP.ObjectExists("#General_Delete_IM")) {
				DeleteRecord("#General_Delete_IM", false);
			}
			EP.Btn_Click("#General_First_BN"); // Click Add button

			if (IsValid) {
				EP.DrpDwn_SelectIndex(
						"#AddVisit_TannerStagePubertalAssessmentGenitalStage_DD",
						EP.DrpDwn_GetCount("#AddVisit_TannerStagePubertalAssessmentGenitalStage_DD") - 1);
			} else {
				if (VisitDate.compareTo("02-Mar-2010") == 0) {
					EP.DrpDwn_SelectIndex(
							"#AddVisit_TannerStagePubertalAssessmentGenitalStage_DD",
							5);
				} else if (VisitDate.compareTo("03-Mar-2010") == 0) {
					EP.DrpDwn_SelectIndex(
							"#AddVisit_TannerStagePubertalAssessmentGenitalStage_DD",
							1);
				}
			}
			if (PatientGender.compareTo("MALE") == 0) {
				// Male
				EP.Edit_SetValue("#AddVisit_TannerStageTestisVolLeft_EB",
						InputValues[1]);
				EP.Edit_SetValue("#AddVisit_TannerStageTestisVolRight_EB",
						InputValues[2]);
			} else {
				// Female
				if (TannerMenarche.endsWith("YES")) {
					EP.Rbtn_SetOption("#AddVisit_TannerStageMenarcheYesAT_RB",
							"ON");
					EP.Edit_SetValue("#AddVisit_TannerStageMenarcheYearAT_EB",
							InputValues[1]);
					EP.Edit_SetValue("#AddVisit_TannerStageMenarcheMonthAT_EB",
							InputValues[2]);
				}
				if (TannerMenarche.endsWith("NO")) {
					EP.Rbtn_SetOption("#AddVisit_TannerStageMenarcheNoAT_RB",
							"ON");
				}
				if (TannerMenarche.endsWith("NOT AVAILABLE")) {
					EP.Rbtn_SetOption("#AddVisit_TannerStageMenarcheNoAT_RB",
							"ON");// "Not available" is not available there so
									// selecting No.
				}
			}
			if (!isNotAvailable) {
				EP.Rbtn_SetOption(
						"#AddVisit_TannerStageHasTheDateEnteredPubertyYesAT_RB",
						"ON");
				EP.WaitForPageload();
				EP.SelectDate(
						"#AddVisit_TannerStageEnteredPuberty_IM",
						EP.Date_AddDays(
								EP.Edit_GetValue("#AddVisit_TannerStageAssesmentDate_EB"),
								-1, Calendar.DATE));
			} else {
				EP.Rbtn_SetOption(
						"#AddVisit_TannerStageHasTheDateEnteredPubertyNoAT_RB",
						"ON");
			}
			if (IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN"); // Click Save button
			} else {
				EP.Btn_Click("#General_AddRecordSave_BN");
				EP.Btn_Click("#General_AddRecordSave_BN");
			}
			break;

		case CZECHREPUBLIC:
		case HUNGARY:
			if (IsValid) {
				InputValues = new String[] {
						EP.Date_AddDays(EP.TestData("ECOSPatientDOBFemale"), 2,
								Calendar.YEAR), "2", "2" };
			} else {
				InputValues = new String[] {
						EP.Date_AddDays(DOB, -2, Calendar.YEAR), "89", "89" };
			}

			if (EP.ObjectExists("#General_Delete_IM")) {
				DeleteRecord("#General_Delete_IM", false);
			}
			EP.Btn_Click("#General_First_BN"); // Click Add button

			if (IsValid) {
				EP.DrpDwn_SelectIndex(
						"#AddVisit_TannerStagePubertalAssessmentGenitalStage_DD",
						EP.DrpDwn_GetCount("#AddVisit_TannerStagePubertalAssessmentGenitalStage_DD") - 2);
			} else {
				if (VisitDate.compareTo("02-Mar-2010") == 0) {
					EP.DrpDwn_SelectIndex(
							"#AddVisit_TannerStagePubertalAssessmentGenitalStage_DD",
							5);
				} else if (VisitDate.compareTo("03-Mar-2010") == 0) {
					EP.DrpDwn_SelectIndex(
							"#AddVisit_TannerStagePubertalAssessmentGenitalStage_DD",
							1);
				}
			}
			if (PatientGender.compareTo("MALE") == 0) {
				// Male
				EP.Edit_SetValue("#AddVisit_TannerStageTestisVolLeft_EB",
						InputValues[1]);
				EP.Edit_SetValue("#AddVisit_TannerStageTestisVolRight_EB",
						InputValues[2]);
			} else {
				// Female
				if (TannerMenarche.endsWith("YES")) {
					EP.Rbtn_SetOption("#AddVisit_TannerStageMenarcheYesAT_RB",
							"ON");
					EP.Edit_SetValue("#AddVisit_TannerStageMenarcheYearAT_EB",
							InputValues[1]);
					EP.Edit_SetValue("#AddVisit_TannerStageMenarcheMonthAT_EB",
							InputValues[2]);
				}
				if (TannerMenarche.endsWith("NO")) {
					EP.Rbtn_SetOption("#AddVisit_TannerStageMenarcheNoAT_RB",
							"ON");
				}
				if (TannerMenarche.endsWith("NOT AVAILABLE")) {
					EP.Rbtn_SetOption(
							"#AddVisit_TannerStageMenarcheNotAvailableAT_RB",
							"ON");// "Not available" is not available there so
									// selecting No.
				}
			}
			if (!isNotAvailable) {
				EP.Rbtn_SetOption(
						"#AddVisit_TannerStageHasTheDateEnteredPubertyYesAT_RB",
						"ON");
				EP.WaitForPageload();
				EP.SelectDate(
						"#AddVisit_TannerStageEnteredPuberty_IM",
						EP.Date_AddDays(
								EP.Edit_GetValue("#AddVisit_TannerStageAssesmentDate_EB"),
								-1, Calendar.DATE));
			} else {
				EP.Rbtn_SetOption(
						"#AddVisit_TannerStageHasTheDateEnteredPubertyNoAT_RB",
						"ON");
			}
			if (IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN"); // Click Save button
			} else {
				EP.Btn_Click("#General_AddRecordSave_BN");
				EP.Btn_Click("#General_AddRecordSave_BN");
			}
			break;
		case NORWAY:
			if (IsValid) {
				InputValues = new String[] {
						EP.Date_AddDays(EP.TestData("ECOSPatientDOBFemale"), 2,
								Calendar.YEAR), "2", "2" };
			} else {
				InputValues = new String[] {
						EP.Date_AddDays(DOB, -2, Calendar.YEAR), "89", "89" };
			}

			if (EP.ObjectExists("#General_Delete_IM")) {
				DeleteRecord("#General_Delete_IM", false);
			}
			EP.Btn_Click("#General_First_BN"); // Click Add button

			if (IsValid) {
				if (isNotAvailable) {
					EP.DrpDwn_SelectIndex(
							"#AddVisit_TannerStagePubertalAssessmentGenitalStage_DD",
							EP.DrpDwn_GetCount("#AddVisit_TannerStagePubertalAssessmentGenitalStage_DD") - 1);
				} else {
					EP.DrpDwn_SelectIndex(
							"#AddVisit_TannerStagePubertalAssessmentGenitalStage_DD",
							EP.DrpDwn_GetCount("#AddVisit_TannerStagePubertalAssessmentGenitalStage_DD") - 2);
				}
			} else {
				if (VisitDate.compareTo("02-Mar-2010") == 0) {
					EP.DrpDwn_SelectIndex(
							"#AddVisit_TannerStagePubertalAssessmentGenitalStage_DD",
							5);
				} else if (VisitDate.compareTo("03-Mar-2010") == 0) {
					EP.DrpDwn_SelectIndex(
							"#AddVisit_TannerStagePubertalAssessmentGenitalStage_DD",
							1);
				}
			}
			if (PatientGender.compareTo("MALE") == 0) {
				// Male
				EP.Edit_SetValue("#AddVisit_TannerStageTestisVolLeft_EB",
						InputValues[1]);
				EP.Edit_SetValue("#AddVisit_TannerStageTestisVolRight_EB",
						InputValues[2]);
			} else {
				// Female
				if (TannerMenarche.endsWith("YES")) {
					EP.Rbtn_SetOption("#AddVisit_TannerStageMenarcheYesNO_RB",
							"ON");
					EP.Edit_SetValue("#AddVisit_TannerStageMenarcheYearAT_EB",
							InputValues[1]);
					EP.Edit_SetValue("#AddVisit_TannerStageMenarcheMonthAT_EB",
							InputValues[2]);
				}
				if (TannerMenarche.endsWith("NO")) {
					EP.Rbtn_SetOption("#AddVisit_TannerStageMenarcheNoNO_RB",
							"ON");
				}
				if (TannerMenarche.endsWith("NOT AVAILABLE")) {
					EP.Rbtn_SetOption(
							"#AddVisit_TannerStageMenarcheNotavailableNO_RB",
							"ON");
				}
			}
			if (!isNotAvailable) {
				EP.Rbtn_SetOption(
						"#AddVisit_TannerStageHasTheDateEnteredPubertyYesAT_RB",
						"ON");
				EP.WaitForPageload();
				EP.SelectDate(
						"#AddVisit_TannerStageEnteredPuberty_IM",
						EP.Date_AddDays(
								EP.Edit_GetValue("#AddVisit_TannerStageAssesmentDate_EB"),
								-1, Calendar.DATE));
			} else {
				EP.Rbtn_SetOption(
						"#AddVisit_TannerStageHasTheDateEnteredPubertyNoAT_RB",
						"ON");
			}
			EP.Btn_Click("#General_AddRecordSave_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN"); // Click Save button
			}
			break;
		case CANADA:
		case TAIWAN:
		case INDONESIA:
			if (IsValid) {
				InputValues = new String[] {
						EP.Date_AddDays(EP.TestData("ECOSPatientDOBFemale"), 2,
								Calendar.YEAR), "2", "2" };
			} else {
				InputValues = new String[] {
						EP.Date_AddDays(DOB, -2, Calendar.YEAR), "89", "89" };
			}
			if (EP.ObjectExists("#General_Delete_IM")) {
				DeleteRecord("#General_Delete_IM", false);
			}
			EP.Btn_Click("#General_First_BN"); // Click Add button
			if (IsValid) {
				EP.DrpDwn_SelectIndex(
						"#AddVisit_TannerStagePubertalAssessmentGenitalStage_DD",
						5);
			} else {
				if (VisitDate.compareTo("02-Mar-2010") == 0) {
					EP.DrpDwn_SelectIndex(
							"#AddVisit_TannerStagePubertalAssessmentGenitalStage_DD",
							5);
				} else if (VisitDate.compareTo("03-Mar-2010") == 0) {
					EP.DrpDwn_SelectIndex(
							"#AddVisit_TannerStagePubertalAssessmentGenitalStage_DD",
							1);
				}
			}

			EP.DrpDwn_SelectIndex(
					"#AddVisit_TannerStagePubertalAssessmentGenitalStage_DD", 1);
			if (PatientGender.compareTo("MALE") == 0) {
				// Male
				EP.Edit_SetValue("#AddVisit_TannerStageTestisVolLeft_EB",
						InputValues[1]);
				EP.Edit_SetValue("#AddVisit_TannerStageTestisVolRight_EB",
						InputValues[2]);
			} else {
				// Female
				if (TannerMenarche.endsWith("YES")) {
					EP.DrpDwn_SelectIndex("#AddVisit_TannerStageMenarche_DD", 1);
					EP.Edit_SetValue("#AddVisit_TannerStageMenarcheYearAT_EB",
							InputValues[1]);
					EP.Edit_SetValue("#AddVisit_TannerStageMenarcheMonthAT_EB",
							InputValues[2]);
				}
				if (TannerMenarche.endsWith("NO")) {
					EP.DrpDwn_SelectIndex("#AddVisit_TannerStageMenarche_DD", 2);

				}
				if (TannerMenarche.endsWith("NOT AVAILABLE")) {
					EP.DrpDwn_SelectIndex("#AddVisit_TannerStageMenarche_DD", 2); // Selecting
																					// No
																					// as
																					// Not
																					// available
																					// is
																					// not
																					// displayed
				}
			}
			if (!isNotAvailable) {
				EP.Rbtn_SetOption(
						"#AddVisit_TannerStageHasTheDateEnteredPubertyYesAT_RB",
						"ON");
				EP.WaitForPageload();
				EP.SelectDate(
						"#AddVisit_TannerStageEnteredPuberty_IM",
						EP.Date_AddDays(
								EP.Edit_GetValue("#AddVisit_TannerStageAssesmentDate_EB"),
								-1, Calendar.DATE));
			} else {
				EP.Rbtn_SetOption(
						"#AddVisit_TannerStageHasTheDateEnteredPubertyNoAT_RB",
						"ON");
			}
			EP.Btn_Click("#General_AddRecordSave_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN"); // Click Save button
			}
			break;
		case FINLAND:
			if (IsValid) {
				InputValues = new String[] {
						EP.Date_AddDays(EP.TestData("ECOSPatientDOBFemale"), 2,
								Calendar.YEAR), "2", "2" };
			} else {
				InputValues = new String[] {
						EP.Date_AddDays(DOB, -2, Calendar.YEAR), "89", "89" };
			}
			if (EP.ObjectExists("#General_Delete_IM")) {
				DeleteRecord("#General_Delete_IM", false);
			}
			EP.Btn_Click("#General_First_BN"); // Click Add button

			if (IsValid) {
				if (isNotAvailable) {
					EP.DrpDwn_SelectIndex(
							"#AddVisit_TannerStagePubertalAssessmentGenitalStage_DD",
							EP.DrpDwn_GetCount("#AddVisit_TannerStagePubertalAssessmentGenitalStage_DD") - 1);
				} else {
					EP.DrpDwn_SelectIndex(
							"#AddVisit_TannerStagePubertalAssessmentGenitalStage_DD",
							EP.DrpDwn_GetCount("#AddVisit_TannerStagePubertalAssessmentGenitalStage_DD") - 2);
				}
			} else {
				if (VisitDate.compareTo("02-Mar-2010") == 0) {
					EP.DrpDwn_SelectIndex(
							"#AddVisit_TannerStagePubertalAssessmentGenitalStage_DD",
							EP.DrpDwn_GetCount("#AddVisit_TannerStagePubertalAssessmentGenitalStage_DD") - 2);
				} else if (VisitDate.compareTo("03-Mar-2010") == 0) {
					EP.DrpDwn_SelectIndex(
							"#AddVisit_TannerStagePubertalAssessmentGenitalStage_DD",
							1);
				}
			}
			if (PatientGender.compareTo("MALE") == 0) {
				// Male
				EP.Edit_SetValue("#AddVisit_TannerStageTestisVolLeft_EB",
						InputValues[1]);
				EP.Edit_SetValue("#AddVisit_TannerStageTestisVolRight_EB",
						InputValues[2]);
			} else {
				// Female
				if (TannerMenarche.endsWith("YES")) {
					EP.Rbtn_SetOption("#AddVisit_TannerStageMenarcheYesAT_RB",
							"ON");
					EP.Edit_SetValue("#AddVisit_TannerStageMenarcheYearAT_EB",
							InputValues[1]);
					EP.Edit_SetValue("#AddVisit_TannerStageMenarcheMonthAT_EB",
							InputValues[2]);
				}
				if (TannerMenarche.endsWith("NO")) {
					EP.Rbtn_SetOption("#AddVisit_TannerStageMenarcheNoAT_RB",
							"ON");
				}
				if (TannerMenarche.endsWith("NOT AVAILABLE")) {
					EP.Rbtn_SetOption(
							"#AddVisit_TannerStageMenarcheNotAvailableAT_RB",
							"ON"); // "Not available" is not available there so
									// selecting No.
				}
			}
			if (!isNotAvailable) {
				EP.Rbtn_SetOption(
						"#AddVisit_TannerStageHasTheDateEnteredPubertyYesFI_RB",
						"ON");
				EP.WaitForPageload();
				EP.SelectDate(
						"#AddVisit_TannerStageEnteredPuberty_IM",
						EP.Date_AddDays(
								EP.Edit_GetValue("#AddVisit_TannerStageAssesmentDate_EB"),
								-1, Calendar.DATE));
			} else {
				EP.Rbtn_SetOption(
						"#AddVisit_TannerStageHasTheDateEnteredPubertyNoFI_RB",
						"ON");
			}
			EP.Btn_Click("#General_AddRecordSave_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN"); // Click Save button
			}
			break;

		// case CHINA:
		case SWEDEN:

			if (IsValid) {
				InputValues = new String[] {
						EP.Date_AddDays(EP.TestData("ECOSPatientDOBFemale"), 2,
								Calendar.YEAR), "2", "2" };
			} else {
				InputValues = new String[] {
						EP.Date_AddDays(DOB, -2, Calendar.YEAR), "89", "89" };
			}
			if (EP.ObjectExists("#General_Delete_IM")) {
				DeleteRecord("#General_Delete_IM", false);
			}
			EP.Btn_Click("#General_First_BN"); // Click Add button

			if (IsValid) {
				if (isNotAvailable) {
					EP.DrpDwn_SelectIndex(
							"#AddVisit_TannerStagePubertalAssessmentGenitalStage_DD",
							EP.DrpDwn_GetCount("#AddVisit_TannerStagePubertalAssessmentGenitalStage_DD") - 1);
				} else {
					EP.DrpDwn_SelectIndex(
							"#AddVisit_TannerStagePubertalAssessmentGenitalStage_DD",
							EP.DrpDwn_GetCount("#AddVisit_TannerStagePubertalAssessmentGenitalStage_DD") - 2);
				}
			} else {
				if (VisitDate.compareTo("02-Mar-2010") == 0) {
					EP.DrpDwn_SelectIndex(
							"#AddVisit_TannerStagePubertalAssessmentGenitalStage_DD",
							EP.DrpDwn_GetCount("#AddVisit_TannerStagePubertalAssessmentGenitalStage_DD") - 2);
				} else if (VisitDate.compareTo("03-Mar-2010") == 0) {
					EP.DrpDwn_SelectIndex(
							"#AddVisit_TannerStagePubertalAssessmentGenitalStage_DD",
							1);
				}
			}
			if (PatientGender.compareTo("MALE") == 0) {
				// Male
				EP.Edit_SetValue("#AddVisit_TannerStageTestisVolLeft_EB",
						InputValues[1]);
				EP.Edit_SetValue("#AddVisit_TannerStageTestisVolRight_EB",
						InputValues[2]);
			} else {
				// Female
				if (TannerMenarche.endsWith("YES")) {
					EP.Rbtn_SetOption("#AddVisit_TannerStageMenarcheYesAT_RB",
							"ON");
					EP.Edit_SetValue("#AddVisit_TannerStageMenarcheYearAT_EB",
							InputValues[1]);
					EP.Edit_SetValue("#AddVisit_TannerStageMenarcheMonthAT_EB",
							InputValues[2]);
				}
				if (TannerMenarche.endsWith("NO")) {
					EP.Rbtn_SetOption("#AddVisit_TannerStageMenarcheNoAT_RB",
							"ON");
				}
				if (TannerMenarche.endsWith("NOT AVAILABLE")) {
					EP.Rbtn_SetOption(
							"#AddVisit_TannerStageMenarcheNotAvailableAT_RB",
							"ON"); // "Not available" is not available there so
									// selecting No.
				}
			}
			if (!isNotAvailable) {
				EP.Rbtn_SetOption(
						"#AddVisit_TannerStageHasTheDateEnteredPubertyYesKR_RB",
						"ON");
				EP.WaitForPageload();
				EP.SelectDate(
						"#AddVisit_TannerStageEnteredPuberty_IM",
						EP.Date_AddDays(
								EP.Edit_GetValue("#AddVisit_TannerStageAssesmentDate_EB"),
								-1, Calendar.DATE));
			} else {
				EP.Rbtn_SetOption(
						"#AddVisit_TannerStageHasTheDateEnteredPubertyNoKR_RB",
						"ON");
			}
			EP.Btn_Click("#General_AddRecordSave_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN"); // Click Save button
			}
			break;

		case AUSTRALIA:
			if (IsValid) {
				InputValues = new String[] {
						EP.Date_AddDays(EP.TestData("ECOSPatientDOBFemale"), 2,
								Calendar.YEAR), "2", "2" };
			} else {
				InputValues = new String[] {
						EP.Date_AddDays(DOB, -2, Calendar.YEAR), "89", "89" };
			}
			if (EP.ObjectExists("#General_Delete_IM")) {
				DeleteRecord("#General_Delete_IM", false);
			}
			EP.Btn_Click("#General_First_BN"); // Click Add button

			if (IsValid) {
				if (isNotAvailable) {
					EP.DrpDwn_SelectIndex(
							"#AddVisit_TannerStagePubertalAssessmentGenitalStage_DD",
							EP.DrpDwn_GetCount("#AddVisit_TannerStagePubertalAssessmentGenitalStage_DD") - 1);
				} else {
					EP.DrpDwn_SelectIndex(
							"#AddVisit_TannerStagePubertalAssessmentGenitalStage_DD",
							EP.DrpDwn_GetCount("#AddVisit_TannerStagePubertalAssessmentGenitalStage_DD") - 2);
				}
			} else {
				if (VisitDate.compareTo("02-Mar-2010") == 0) {
					EP.DrpDwn_SelectIndex(
							"#AddVisit_TannerStagePubertalAssessmentGenitalStage_DD",
							EP.DrpDwn_GetCount("#AddVisit_TannerStagePubertalAssessmentGenitalStage_DD") - 2);
				} else if (VisitDate.compareTo("03-Mar-2010") == 0) {
					EP.DrpDwn_SelectIndex(
							"#AddVisit_TannerStagePubertalAssessmentGenitalStage_DD",
							1);
				}
			}
			if (PatientGender.compareTo("MALE") == 0) {
				// Male
				EP.Edit_SetValue("#AddVisit_TannerStageTestisVolLeft_EB",
						InputValues[1]);
				EP.Edit_SetValue("#AddVisit_TannerStageTestisVolRight_EB",
						InputValues[2]);
			} else {
				// Female
				if (TannerMenarche.endsWith("YES")) {
					EP.Rbtn_SetOption("#AddVisit_TannerStageMenarcheYesAT_RB",
							"ON");
					EP.Edit_SetValue("#AddVisit_TannerStageMenarcheYearAT_EB",
							InputValues[1]);
					EP.Edit_SetValue("#AddVisit_TannerStageMenarcheMonthAT_EB",
							InputValues[2]);
				}
				if (TannerMenarche.endsWith("NO")) {
					EP.Rbtn_SetOption("#AddVisit_TannerStageMenarcheNoAT_RB",
							"ON");
				}
				if (TannerMenarche.endsWith("NOT AVAILABLE")) {
					EP.Rbtn_SetOption(
							"#AddVisit_TannerStageMenarcheNotAvailableAT_RB",
							"ON"); // "Not available" is not available there so
									// selecting No.
				}
			}
			if (!isNotAvailable) {
				EP.Rbtn_SetOption(
						"#AddVisit_TannerStageHasTheDateEnteredPubertyYesAU_RB",
						"ON");
				EP.WaitForPageload();
				EP.SelectDate(
						"#AddVisit_TannerStageEnteredPuberty_IM",
						EP.Date_AddDays(
								EP.Edit_GetValue("#AddVisit_TannerStageAssesmentDate_EB"),
								-1, Calendar.DATE));
			} else {
				EP.Rbtn_SetOption(
						"#AddVisit_TannerStageHasTheDateEnteredPubertyNoAU_RB",
						"ON");
			}

			EP.Btn_Click("#General_AddRecordSave_BN");// Click Save button
			if (!IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN");
			}
			break;
		case ICELAND:
			if (IsValid) {
				InputValues = new String[] {
						EP.Date_AddDays(EP.TestData("ECOSPatientDOBFemale"), 2,
								Calendar.YEAR), "2", "2" };
			} else {
				InputValues = new String[] {
						EP.Date_AddDays(DOB, -2, Calendar.YEAR), "89", "89" };
			}
			if (EP.ObjectExists("#General_Delete_IM")) {
				DeleteRecord("#General_Delete_IM", false);
			}
			EP.Btn_Click("#General_First_BN"); // Click Add button

			if (IsValid) {
				if (isNotAvailable) {
					EP.DrpDwn_SelectIndex(
							"#AddVisit_TannerStagePubertalAssessmentGenitalStage_DD",
							EP.DrpDwn_GetCount("#AddVisit_TannerStagePubertalAssessmentGenitalStage_DD") - 1);
				} else {
					EP.DrpDwn_SelectIndex(
							"#AddVisit_TannerStagePubertalAssessmentGenitalStage_DD",
							EP.DrpDwn_GetCount("#AddVisit_TannerStagePubertalAssessmentGenitalStage_DD") - 2);
				}
			} else {
				if (VisitDate.compareTo("02-Mar-2010") == 0) {
					EP.DrpDwn_SelectIndex(
							"#AddVisit_TannerStagePubertalAssessmentGenitalStage_DD",
							EP.DrpDwn_GetCount("#AddVisit_TannerStagePubertalAssessmentGenitalStage_DD") - 2);
				} else if (VisitDate.compareTo("03-Mar-2010") == 0) {
					EP.DrpDwn_SelectIndex(
							"#AddVisit_TannerStagePubertalAssessmentGenitalStage_DD",
							1);
				}
			}
			if (PatientGender.compareTo("MALE") == 0) {
				// Male
				EP.Edit_SetValue("#AddVisit_TannerStageTestisVolLeft_EB",
						InputValues[1]);
				EP.Edit_SetValue("#AddVisit_TannerStageTestisVolRight_EB",
						InputValues[2]);
			} else {
				// Female
				if (TannerMenarche.endsWith("YES")) {
					EP.Rbtn_SetOption("#AddVisit_TannerStageMenarcheYesAT_RB",
							"ON");
					EP.Edit_SetValue("#AddVisit_TannerStageMenarcheYearAT_EB",
							InputValues[1]);
					EP.Edit_SetValue("#AddVisit_TannerStageMenarcheMonthAT_EB",
							InputValues[2]);
				}
				if (TannerMenarche.endsWith("NO")) {
					EP.Rbtn_SetOption("#AddVisit_TannerStageMenarcheNoAT_RB",
							"ON");
				}
				if (TannerMenarche.endsWith("NOT AVAILABLE")) {
					EP.Rbtn_SetOption(
							"#AddVisit_TannerStageMenarcheNotAvailableAT_RB",
							"ON"); // "Not available" is not available there so
									// selecting No.
				}
			}
			if (!isNotAvailable) {
				EP.Rbtn_SetOption(
						"#AddVisit_TannerStageHasTheDateEnteredPubertyYesKR_RB",
						"ON");
				EP.WaitForPageload();
				EP.SelectDate(
						"#AddVisit_TannerStageEnteredPuberty_IM",
						EP.Date_AddDays(
								EP.Edit_GetValue("#AddVisit_TannerStageAssesmentDate_EB"),
								-1, Calendar.DATE));
			} else {
				EP.Rbtn_SetOption(
						"#AddVisit_TannerStageHasTheDateEnteredPubertyNoKR_RB",
						"ON");
			}

			EP.Btn_Click("#General_AddRecordSave_BN");// Click Save button
			if (!IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN");
			}
			break;
		case GERMANY:
			// case ARGENTINA:

			if (IsValid) {
				InputValues = new String[] {
						EP.Date_AddDays(EP.TestData("ECOSPatientDOBFemale"), 2,
								Calendar.YEAR), "2", "2" };
			} else {
				InputValues = new String[] {
						EP.Date_AddDays(DOB, -2, Calendar.YEAR), "89", "89" };
			}

			if (EP.ObjectExists("#General_Delete_IM")) {
				DeleteRecord("#General_Delete_IM", false);
			}
			EP.Btn_Click("#General_First_BN"); // Click Add button
			if (IsValid) {
				if (isNotAvailable) {
					EP.DrpDwn_SelectIndex(
							"#AddVisit_TannerStagePubertalAssessmentGenitalStage_DD",
							6);
				} else {
					EP.DrpDwn_SelectIndex(
							"#AddVisit_TannerStagePubertalAssessmentGenitalStage_DD",
							5);
				}
			} else {
				if (VisitDate.compareTo("02-Mar-2010") == 0) {
					EP.DrpDwn_SelectIndex(
							"#AddVisit_TannerStagePubertalAssessmentGenitalStage_DD",
							5);
				} else if (VisitDate.compareTo("03-Mar-2010") == 0) {
					EP.DrpDwn_SelectIndex(
							"#AddVisit_TannerStagePubertalAssessmentGenitalStage_DD",
							1);
				}
			}

			if (PatientGender.compareTo("MALE") == 0) {
				// Male
				EP.Edit_SetValue("#AddVisit_TannerStageTestisVolLeft_EB",
						InputValues[1]);
				EP.Edit_SetValue("#AddVisit_TannerStageTestisVolRight_EB",
						InputValues[2]);
			} else {
				// Female
				if (TannerMenarche.endsWith("YES")) {
					// EP.DrpDwn_SelectIndex("#AddVisit_TannerStageMenarche_DD",
					// 1);
					EP.Rbtn_SetOption("#AddVisit_TannerStageMenarcheYesAT_RB",
							"ON");
					EP.Edit_SetValue("#AddVisit_TannerStageMenarcheYearAT_EB",
							InputValues[1]);
					EP.Edit_SetValue("#AddVisit_TannerStageMenarcheMonthAT_EB",
							InputValues[2]);
				}
				if (TannerMenarche.endsWith("NO")) {
					EP.Rbtn_SetOption("#AddVisit_TannerStageMenarcheYNoAT_RB",
							"ON");
				}
				if (TannerMenarche.endsWith("NOT AVAILABLE")) {
					EP.Rbtn_SetOption(
							"#AddVisit_TannerStageMenarcheNotAvailableAT_RB",
							"ON");
				}
			}
			if (!isNotAvailable) {
				EP.Rbtn_SetOption(
						"#AddVisit_TannerStageHasTheDateEnteredPubertyYesAT_RB",
						"ON");
				EP.WaitForPageload();
				EP.SelectDate(
						"#AddVisit_TannerStageEnteredPuberty_IM",
						EP.Date_AddDays(
								EP.Edit_GetValue("#AddVisit_TannerStageAssesmentDate_EB"),
								-1, Calendar.DATE));
			}
			if (IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN"); // Click Save button
			} else {
				EP.Btn_Click("#General_AddRecordSave_BN");
				EP.Btn_Click("#General_AddRecordSave_BN");
			}
			break;

		case KOREA:
			if (IsValid) {
				InputValues = new String[] {
						EP.Date_AddDays(EP.TestData("ECOSPatientDOBFemale"), 2,
								Calendar.YEAR), "2", "2" };
			} else {
				InputValues = new String[] {
						EP.Date_AddDays(DOB, -2, Calendar.YEAR), "89", "89" };
			}
			if (EP.ObjectExists("#General_Delete_IM")) {
				DeleteRecord("#General_Delete_IM", false);
			}
			EP.Btn_Click("#General_First_BN"); // Click Add button

			if (IsValid) {
				if (isNotAvailable) {
					EP.DrpDwn_SelectIndex(
							"#AddVisit_TannerStagePubertalAssessmentGenitalStage_DD",
							EP.DrpDwn_GetCount("#AddVisit_TannerStagePubertalAssessmentGenitalStage_DD") - 1);
				} else {
					EP.DrpDwn_SelectIndex(
							"#AddVisit_TannerStagePubertalAssessmentGenitalStage_DD",
							EP.DrpDwn_GetCount("#AddVisit_TannerStagePubertalAssessmentGenitalStage_DD") - 2);
				}
			} else {
				if (VisitDate.compareTo("02-Mar-2010") == 0) {
					EP.DrpDwn_SelectIndex(
							"#AddVisit_TannerStagePubertalAssessmentGenitalStage_DD",
							EP.DrpDwn_GetCount("#AddVisit_TannerStagePubertalAssessmentGenitalStage_DD") - 2);
				} else if (VisitDate.compareTo("03-Mar-2010") == 0) {
					EP.DrpDwn_SelectIndex(
							"#AddVisit_TannerStagePubertalAssessmentGenitalStage_DD",
							1);
				}
			}
			if (PatientGender.compareTo("MALE") == 0) {
				// Male
				EP.Edit_SetValue("#AddVisit_TannerStageTestisVolLeft_EB",
						InputValues[1]);
				EP.Edit_SetValue("#AddVisit_TannerStageTestisVolRight_EB",
						InputValues[2]);
			} else {
				// Female
				if (TannerMenarche.endsWith("YES")) {
					EP.Rbtn_SetOption("#AddVisit_TannerStageMenarcheYesAT_RB",
							"ON");
					EP.Edit_SetValue("#AddVisit_TannerStageMenarcheYearAT_EB",
							InputValues[1]);
					EP.Edit_SetValue("#AddVisit_TannerStageMenarcheMonthAT_EB",
							InputValues[2]);
				}
				if (TannerMenarche.endsWith("NO")) {
					EP.Rbtn_SetOption("#AddVisit_TannerStageMenarcheNoAT_RB",
							"ON");
				}
				if (TannerMenarche.endsWith("NOT AVAILABLE")) {
					EP.Rbtn_SetOption(
							"#AddVisit_TannerStageMenarcheNotAvailableAT_RB",
							"ON"); // "Not available" is not available there so
									// selecting No.
				}
			}
			if (!isNotAvailable) {
				EP.Rbtn_SetOption(
						"#AddVisit_TannerStageHasTheDateEnteredPubertyYesKR_RB",
						"ON");
				EP.WaitForPageload();
				EP.SelectDate(
						"#AddVisit_TannerStageEnteredPuberty_IM",
						EP.Date_AddDays(
								EP.Edit_GetValue("#AddVisit_TannerStageAssesmentDate_EB"),
								-1, Calendar.DATE));
				EP.HideCursor();
				EP.WaitForObject("#General_AddRecordSave_BN");
			} else {
				EP.Rbtn_SetOption(
						"#AddVisit_TannerStageHasTheDateEnteredPubertyNoKR_RB",
						"ON");
			}

			EP.Btn_Click("#General_AddRecordSave_BN");// Click Save button
			if (!IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN");
			}
			break;

		default:
		}
		VerifySuccessMessage(EP.LangData("TannerStage"));

	}

	public void BoneAgeAdd(boolean IsValid, String TabIndex) {
		// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*BONE
		// AGE*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
		EP.ShowTab(TabIndex);
		String[] InputValues;

		switch (switchCountryName) {
		case AUSTRALIA:
			if (IsValid) {
				InputValues = new String[] { "5, 3" };
			} else {
				InputValues = new String[] { "9999" };
			}

			if (EP.ObjectExists("#General_Delete_IM")) {
				DeleteRecord("#General_Delete_IM", false);
			}
			EP.Btn_Click("#General_First_BN"); // Click Add button
			int ValueSelected = EP.RandomValue(1, 4);
			EP.DrpDwn_SelectIndex("#AddVisit_BoneMethod_DD", ValueSelected);
			String EditBoxXpath = EP.ORXPath("#AddVisit_BoneBoneAgeYear_EB")
					+ "[" + ValueSelected + "]";

			// Incorrect Format
			EP.Edit_SetValue(EditBoxXpath, InputValues[0]);
			EP.Btn_Click("#General_AddRecordSave_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN");
			}

			break;
		case ICELAND:
			if (IsValid) {
				InputValues = new String[] { "5, 3" };
			} else {
				InputValues = new String[] { "9999" };
			}

			if (EP.ObjectExists("#General_Delete_IM")) {
				DeleteRecord("#General_Delete_IM", false);
			}
			EP.Btn_Click("#General_First_BN"); // Click Add button
			ValueSelected = EP.RandomValue(1, 4);
			EP.DrpDwn_SelectIndex("#AddVisit_BoneMethod_DD", ValueSelected);
			EditBoxXpath = EP.ORXPath("#AddVisit_BoneBoneAgeYear_EB") + "["
					+ ValueSelected + "]";

			// Incorrect Format
			EP.Edit_SetValue(EditBoxXpath, InputValues[0]);
			EP.Btn_Click("#General_AddRecordSave_BN"); // click save button
			if (!IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN");
			}
		case FINLAND:
			if (IsValid) {
				InputValues = new String[] { "5, 3" };
			} else {
				InputValues = new String[] { "9999" };
			}

			if (EP.ObjectExists("#General_Delete_IM")) {
				DeleteRecord("#General_Delete_IM", false);
			}
			EP.Btn_Click("#General_First_BN"); // Click Add button
			ValueSelected = EP.RandomValue(1, 4);
			EP.DrpDwn_SelectIndex("#AddVisit_BoneMethod_DD", ValueSelected);
			EditBoxXpath = EP.ORXPath("#AddVisit_BoneBoneAgeYear_EB") + "["
					+ ValueSelected + "]";

			// Incorrect Format
			EP.Edit_SetValue(EditBoxXpath, InputValues[0]);
			EP.Btn_Click("#General_AddRecordSave_BN"); // click save button
			if (!IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN");
			}
			break;
		case UNITEDKINGDOM:
		case AUSTRIA:
		case FRANCE:
		case SWEDEN:
		case SPAIN:
		case ITALY:
		case CANADA:
		case CZECHREPUBLIC:
			// case CHINA:
		case GREECE:
		case HUNGARY:
		case NORWAY:
		case MEXICO:
		case SLOVAKIA:
		case GERMANY:
		case SINGAPORE:
		case ARGENTINA:
		case COLOMBIA:
		case TAIWAN:
		case INDONESIA:
		case SAUDIARABIA:
		case UNITEDARABEMIRATES:

			InputValues = new String[] { "5, 3" };

			if (EP.ObjectExists("#General_Delete_IM")) {
				DeleteRecord("#General_Delete_IM", false);
			}
			EP.Btn_Click("#General_First_BN"); // Click Add button
			ValueSelected = EP.RandomValue(1, 4);
			EP.DrpDwn_SelectIndex("#AddVisit_BoneMethod_DD", ValueSelected);
			EditBoxXpath = EP.ORXPath("#AddVisit_BoneBoneAgeYear_EB") + "["
					+ ValueSelected + "]";

			// Incorrect Format
			EP.Edit_SetValue(EditBoxXpath, InputValues[0]);
			EP.Btn_Click("#General_AddRecordSave_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN"); // click save button
			}
			break;

		case KOREA:
			if (IsValid) {
				InputValues = new String[] { "5, 3" };
			} else {
				InputValues = new String[] { "9999" };
			}

			if (EP.ObjectExists("#General_Delete_IM")) {
				DeleteRecord("#General_Delete_IM", false);
			}
			EP.Btn_Click("#General_First_BN"); // Click Add button
			int ValueSelectedKR = EP.RandomValue(1, 4);
			EP.DrpDwn_SelectIndex("#AddVisit_BoneMethod_DD", ValueSelectedKR);
			String EditBoxXpathKR = EP.ORXPath("#AddVisit_BoneBoneAgeYear_EB")
					+ "[" + ValueSelectedKR + "]";

			// Incorrect Format
			EP.Edit_SetValue(EditBoxXpathKR, InputValues[0]);
			EP.Btn_Click("#General_AddRecordSave_BN"); // click save button

			if (!IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN");
			}
			break;

		default:
		}
		VerifySuccessMessage(EP.LangData("BoneAgeCaps"));

	}

	public void IGF1Add(boolean IsValid, String TabIndex) {
		// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*IGF-1*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
		EP.ShowTab(TabIndex);
		String[] InputValues;
		switch (switchCountryName) {
		case ICELAND:

			if (IsValid) {
				InputValues = new String[] { "50", "2", "5" };
			} else {
				InputValues = new String[] { "9876543210", "9876543210",
						"9876543210" };
			}
			if (EP.ObjectExists("#General_Delete_IM")) {
				DeleteRecord("#General_Delete_IM", false);
			}
			EP.Btn_Click("#General_First_BN"); // Click Add button
			EP.DrpDwn_SelectIndex("#AddVisit_AddIGF1Status_DD",
					EP.RandomValue(1, 3));
			EP.Edit_SetValueByJavaScript("#AddVisit_AddIGF1Level_EB",
					InputValues[0]);
			EP.DrpDwn_SelectIndex("#AddVisit_AddIGF1StatusUnit_DD",
					EP.RandomValue(1, 5));
			EP.Edit_SetValueByJavaScript(
					"#AddVisit_AddIGF1LaboratoryValueRangeMin_EB",
					InputValues[1]);
			EP.Edit_SetValueByJavaScript(
					"#AddVisit_AddIGF1LaboratoryValueRangeMax_EB",
					InputValues[2]);
			EP.Btn_Click("#General_AddRecordSave_BN");// Click Save button
			break;

		case FINLAND:
			if (IsValid) {
				InputValues = new String[] { "50", "2", "5" };
			} else {
				InputValues = new String[] { "9876543210", "9876543210",
						"9876543210" };
			}
			if (EP.ObjectExists("#General_Delete_IM")) {
				DeleteRecord("#General_Delete_IM", false);
			}
			EP.Btn_Click("#General_First_BN"); // Click Add button
			EP.DrpDwn_SelectIndex("#AddVisit_AddIGF1Status_DD",
					EP.RandomValue(1, 3));
			EP.Edit_SetValueByJavaScript("#AddVisit_AddIGF1Level_EB",
					InputValues[0]);
			EP.DrpDwn_SelectIndex("#AddVisit_AddIGF1StatusUnit_DD",
					EP.RandomValue(1, 5));
			EP.Edit_SetValueByJavaScript(
					"#AddVisit_AddIGF1LaboratoryValueRangeMin_EB",
					InputValues[1]);
			EP.Edit_SetValueByJavaScript(
					"#AddVisit_AddIGF1LaboratoryValueRangeMax_EB",
					InputValues[2]);
			EP.Btn_Click("#General_AddRecordSave_BN");// Click Save button
			break;

		case UNITEDKINGDOM:
		case AUSTRIA:
		case UNITEDARABEMIRATES:
		case SAUDIARABIA:
		case SINGAPORE:
		case SPAIN:
		case ITALY:
			// case CHINA:
			// case ARGENTINA:
		case CZECHREPUBLIC:
		case GREECE:
		case HUNGARY:
		case NORWAY:
		case MEXICO:
		case SLOVAKIA:
		case GERMANY:

			if (IsValid) {
				InputValues = new String[] { "50", "2", "5" };
			} else {
				InputValues = new String[] { "9876543210", "9876543210",
						"9876543210" };
			}
			if (EP.ObjectExists("#General_Delete_IM")) {
				DeleteRecord("#General_Delete_IM", false);
			}
			EP.Btn_Click("#General_First_BN"); // Click Add button
			EP.DrpDwn_SelectIndex("#AddVisit_AddIGF1Status_DD",
					EP.RandomValue(1, 3));
			EP.Edit_SetValueByJavaScript("#AddVisit_AddIGF1Level_EB",
					InputValues[0]);
			EP.DrpDwn_SelectIndex("#AddVisit_AddIGF1StatusUnit_DD",
					EP.RandomValue(1, 5));
			EP.Edit_SetValueByJavaScript(
					"#AddVisit_AddIGF1LaboratoryValueRangeMin_EB",
					InputValues[1]);
			EP.Edit_SetValueByJavaScript(
					"#AddVisit_AddIGF1LaboratoryValueRangeMax_EB",
					InputValues[2]);
			EP.Btn_Click("#General_AddRecordSave_BN");// Click Save button
			break;
		case CANADA:
		case ARGENTINA:
		case COLOMBIA:
		case TAIWAN:
		case INDONESIA:

			if (IsValid) {
				InputValues = new String[] { "50", "2", "5" };
			} else {
				InputValues = new String[] { "9876543210", "9876543210",
						"9876543210" };
			}
			if (EP.ObjectExists("#General_Delete_IM")) {
				DeleteRecord("#General_Delete_IM", false);
			}
			EP.Btn_Click("#General_First_BN"); // Click Add button
			EP.DrpDwn_SelectIndex("#AddVisit_AddIGF1Status_DD",
					EP.RandomValue(1, 3));
			EP.Edit_SetValueByJavaScript("#AddVisit_AddIGF1Level_EB",
					InputValues[0]);
			EP.DrpDwn_SelectIndex("#AddVisit_AddIGF1StatusUnit_DD",
					EP.RandomValue(1, 5));
			EP.Edit_SetValueByJavaScript(
					"#AddVisit_AddIGF1LaboratoryValueRangeMin_EB",
					InputValues[1]);
			EP.Edit_SetValueByJavaScript(
					"#AddVisit_AddIGF1LaboratoryValueRangeMax_EB",
					InputValues[2]);

			EP.Btn_Click("#General_AddRecordSave_BN");// Click Save button

			break;
		case AUSTRALIA:
			if (EP.ObjectExists("#General_Delete_IM")) {
				DeleteRecord("#General_Delete_IM", false);
			}
			EP.Btn_Click("#General_First_BN"); // Click Add button
			EP.DrpDwn_SelectIndex("#AddVisit_AddIGF1StatusAUS_DD",
					EP.RandomValue(1, 4));
			EP.Btn_Click("#General_AddRecordSave_BN");// Click Save button

			break;
		case FRANCE:
			if (IsValid) {
				InputValues = new String[] { "50", "70" };
			} else {
				InputValues = new String[] { "9876543210", "9876543210" };
			}
			if (EP.ObjectExists("#General_Delete_IM")) {
				DeleteRecord("#General_Delete_IM", false);
			}

			EP.Btn_Click("#General_First_BN"); // Click Add button

			EP.Edit_SetValue("#AddVisit_AddValueFR_EB", InputValues[0]);
			EP.DrpDwn_SelectIndex("#AddVisit_AddUnitFR_DD",
					EP.RandomValue(1, 5));
			EP.Edit_SetValue("#AddVisit_AddLaboratoryValueRangeMinFR_EB",
					InputValues[0]);
			EP.Edit_SetValue("#AddVisit_AddLaboratoryValueRangeMaxFR_EB",
					InputValues[1]);
			EP.DrpDwn_SelectIndex("#AddVisit_AddStatusFR1_DD",
					EP.RandomValue(1, 3));

			int Random = EP.RandomValue(1, 10);
			if (Random == 10) {
				Random = Random - 1;
				EP.ChkBx_SetOption(
						EP.ORXPath("#AddVisit_AddKitnameCheckBoxesFR_CB") + "["
								+ Random + "]", "ON");
			} else {
				EP.ChkBx_SetOption(
						EP.ORXPath("#AddVisit_AddKitnameCheckBoxesFR_CB") + "["
								+ Random + "]", "ON");
			}
			EP.ChkBx_SetOption(
					EP.ORXPath("#AddVisit_AddIGF1AssayTypeCheckBoxesFR_CB")
							+ "[" + EP.RandomValue(1, 7) + "]", "ON"); // Select
																		// a
																		// random
																		// checkbox
																		// in
																		// Majority
																		// Injections
			Random = EP.RandomValue(1, 4);
			if (Random == 4) {
				Random = Random - 1;
				EP.ChkBx_SetOption(
						EP.ORXPath("#AddVisit_AddIGF1RefPreparationCheckBoxesFR_CB")
								+ "[" + Random + "]", "ON"); // Select a random
																// checkbox in
																// Majority
																// Injections
			} else {
				EP.ChkBx_SetOption(
						EP.ORXPath("#AddVisit_AddIGF1RefPreparationCheckBoxesFR_CB")
								+ "[" + Random + "]", "ON");
			}
			EP.Btn_Click("#General_AddRecordSave_BN");
			if (!IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN");
			}
			break;

		case SWEDEN:
			if (IsValid) {
				InputValues = new String[] { "50", "5", "2", "30" };
			} else {
				InputValues = new String[] { "9876543210", "9876543210",
						"9876543210", "9876543210" };
			}

			if (EP.ObjectExists("#General_Delete1_IM")) {
				DeleteRecord("#General_Delete1_IM", false);
			}
			if (EP.ObjectExists("#General_Delete1_IM")) {
				DeleteRecord("#General_Delete1_IM", false);
			}
			// EP.SelectDate("#AddVisit_IGF1StatusDateBeforeGH_IM",
			// EP.GetTimeStamp("dd-MMM-yyyy"));
			// EP.DrpDwn_SelectIndex("#AddVisit_IGF1StatusBeforeGH_DD",
			// EP.RandomValue(1, 4));
			// EP.Edit_SetValue("#AddVisit_IGF1Level_EB", InputValues[0]);
			//
			// EP.Edit_SetValue("#AddVisit_IGF1LaboratoryValueRangeMax_EB",
			// InputValues[1]);
			// EP.Edit_SetValue("#AddVisit_IGF1LaboratoryValueRangeMin_EB",
			// InputValues[2]);
			// if(IsValid){
			// EP.Btn_Click("#General_First_BN"); //Click Save button
			// }
			// else
			// {
			// EP.Btn_Click("#General_First_BN"); //Click Save button
			// EP.Btn_Click("#General_First_BN"); //Click Save button
			// }

			EP.Btn_Click("#General_First_BN"); // Click Add button
			EP.DrpDwn_SelectIndex("#AddVisit_AddIGF1Status_DD",
					EP.RandomValue(1, 5));
			EP.DrpDwn_SelectIndex("#AddVisit_AddIGF1StatusUnit_DD",
					EP.RandomValue(1, 6));
			EP.Edit_SetValueByJavaScript("#AddVisit_AddIGF1Level_EB",
					InputValues[0]);
			EP.Edit_SetValueByJavaScript(
					"#AddVisit_AddIGF1LaboratoryValueRangeMin_EB",
					InputValues[1]);
			EP.Edit_SetValueByJavaScript(
					"#AddVisit_AddIGF1LaboratoryValueRangeMax_EB",
					InputValues[2]);
			EP.Btn_Click("#General_AddRecordSave_BN");

			EP.Btn_Click("#General_Second_BN");// 2nd Add button
			EP.Edit_SetValue("#AddVisit_CurrentHbA1c_EB", InputValues[3]);
			if (IsValid) {
				EP.Btn_Click("#General_AddRecordSave_BN");
			} else {
				EP.Btn_Click("#General_AddRecordSave_BN");
				EP.Btn_Click("#General_AddRecordSave_BN");
			}
			break;

		case KOREA:
			if (IsValid) {
				InputValues = new String[] { "50", "5", "2", "30" };
			} else {
				InputValues = new String[] { "9876543210", "9876543210",
						"9876543210", "9876543210" };
			}
			if (EP.ObjectExists("#General_Delete1_IM")) {
				DeleteRecord("#General_Delete1_IM", false);
			}
			if (EP.ObjectExists("#General_Delete1_IM")) {
				DeleteRecord("#General_Delete1_IM", false);
			}
			EP.Lnk_Click("#General_First_BN");
			if (IsValid) {
				Random = EP.RandomValue(1, 4);
				EP.SelectDate("#AddVisit_DateOfAssessmentFR_IM",
						EP.GetTimeStamp("dd-MMM-yyyy"));
				EP.DrpDwn_SelectIndex("#AddVisit_BioChemistryIGF1Status_DD",
						EP.RandomValue(1, 3));
				EP.Edit_SetValue("#AddVisit_BioChemistryIGF1Value_EB",
						InputValues[0]);
				EP.ChkBx_SetOption(EP.ORXPath("#AddVisit_IGF1UnitAll_CB") + "["
						+ Random + "]", "ON");
				Random = EP.RandomValue(1, 9);
				EP.ChkBx_SetOption(EP.ORXPath("#AddVisit_IGF1KitnameAll_CB")
						+ "[" + Random + "]", "ON");
				Random = EP.RandomValue(1, 5);
				EP.ChkBx_SetOption(EP.ORXPath("#AddVisit_IGF1AssaytypeAll_CB")
						+ "[" + Random + "]", "ON");
				EP.ChkBx_SetOption(
						"#AddVisit_BioChemistryIGF1RefPreparation86720_CB",
						"ON");

				EP.Btn_Click("#General_AddRecordSave_BN");

				// Click 2nd Add button
				EP.Lnk_Click("#General_Second_BN");
				EP.WaitForPageload();
				EP.ChkBx_SetOption(
						EP.ORXPath("#AddVisit_BiochemistryUnitCheckboxes_CB")
								+ "[" + EP.RandomValue(1, 4) + "]", "ON");
				EP.Edit_SetValue("#AddVisit_BioChemistryIGFBP3Value_EB",
						InputValues[0]);
				EP.Btn_Click("#General_AddRecordSave_BN");

			} else {
				EP.Btn_Click("#General_AddRecordSave_BN");
			}
			break;

		default:
		}
		if (("France;Sweden;Korea").contains(Env.CountryUsed)) {
			VerifySuccessMessage(EP.LangData("BiochemistryIGF1"));

		} else {
			VerifySuccessMessage(EP.LangData("IGF1"));
		}

	}

	public void ConcomitantMedicationAdd(boolean IsValid, String TabIndex) {
		// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*Concomitant
		// Medication*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

		EP.ShowTab(TabIndex);
		switch (switchCountryName) {

		case SWEDEN:
			if (EP.ObjectExists("#General_Delete_IM")) {
				DeleteRecord("#General_Delete_IM", false);
			}
			EP.Btn_Click("#General_First_BN"); // Click Add button
			EP.Edit_SetValue("#AddVisit_OtherConcomitant_EB", "TestMedicine123");
			EP.Btn_Click("#General_AddRecordSave_BN");

			break;
		default:
		}
		VerifySuccessMessage(EP.LangData("ConcomitantMedication"));

	}

}
