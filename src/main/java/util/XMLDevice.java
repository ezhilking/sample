package functions;

import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Level;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLDevice {
	
	/**
	0 - Completed
	1 - Partial
	2 - New Cartidge
	3 - New Dosage Set
	**/
	
	private DocumentBuilder docBuilder;
	private Document XMLDoc;
	private String XMLPath;
	private String XMLDeviceID;
	private XPath XPathEngine;
	private String DateXpath = "concat(substring(./InjectionDate/text(),1,4),substring(./InjectionDate/text(),6,2),substring(./InjectionDate/text(),9,2),substring(./InjectionTime/text(),1,2),substring(./InjectionTime/text(),4,2),substring(./InjectionTime/text(),7,2))";
	private String EventXpath = "./InjectionType/text()='EVENTVALUE'";
	private boolean IsDayStartTime4PM;
	protected UtilClass EPObject  = null;

	public XMLDevice(String XMLFileName, Easypod PassedEP){
		try {
			docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			XMLPath = Env.DataPath + "/DeviceData/EP1Devices/XMLFormat/" +XMLFileName;
			XMLDoc = docBuilder.parse (new File(XMLPath));
			XPathEngine =  XPathFactory.newInstance().newXPath();
			EPObject = PassedEP;
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, "Exception occured while parsing XML. " + e.getMessage());
		}
	}
	
	public void SetDayStartingTime(boolean Is4PM){
		IsDayStartTime4PM = Is4PM;
	}
	
	public void UpdateDeviceID(String NewDeviceID){
//		Long NewNumber = Long.parseLong(CurrentEP.GetDeviceID("NEWDEVICE")) + 500;
		XMLDeviceID = String.valueOf(NewDeviceID);
		XMLDoc.getElementsByTagName("SerialNo").item(0).setTextContent(XMLDeviceID);
		SaveChanges();
	}
	
	public static String PrepareXMLForUpload(String DeviceType){
		DeviceType = DeviceType.trim().toUpperCase();
		String BasePath = "/DeviceData/XMLFormat/";
		String NewDeviceName = DeviceType + "_Country.xml";
		String DeviceTemplate = "EP2Template.xml";
		if (DeviceType.startsWith("EP1")){
			DeviceTemplate = "EP1Template.xml";
		}
		
		try{
			File OriginalFile = new File(Env.DataPath + BasePath + DeviceTemplate);
			File NewFile = new File (Env.DataPath + BasePath + NewDeviceName);
			FileUtils.copyFile(OriginalFile, NewFile);
			return NewDeviceName;
		}catch (Exception e){
			Env.logger.log(Level.ERROR, "Unable to create New XML Device. " + e.getMessage());
			return null;
		}
		
	}
	
	private void SaveChanges(){
		try {
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(XMLDoc);
			File XMLFile = new File(XMLPath);
			FileOutputStream fos = new FileOutputStream(XMLFile);
			StreamResult result = new StreamResult(fos);
			transformer.transform(source, result);
		}catch (Exception e){
			System.out.println(e);
		}
	}
	
	private String GetTimeForEmptyValue(String DateType){
		DateType = DateType.trim().toUpperCase();
			String TimeToConvert = "00:00:00";
			if (DateType.startsWith("END")){
				TimeToConvert = "24:59:59"; //Max Future time 24:59:59 although only 23:59:59 is feasible
			}
		return TimeToConvert;
	}
	
	private String GetIDforDateStamp(String DateToConvert, String TimeToConvert, String DateType) throws ParseException{

		if (DateToConvert.isEmpty()){
			Env.logger.log(Level.DEBUG, "Value is empty for " + DateType + " date");
			throw new RuntimeException("Value is empty for " + DateType + " date");
		}

		DateType = DateType.trim().toUpperCase();
		if (TimeToConvert.isEmpty()){ 
			TimeToConvert = GetTimeForEmptyValue(DateType);
		}
		
		DateToConvert = DateToConvert.trim().replaceAll(" ", "-");
		String[] SplittedText;
		String MyDay;
		String MyMonth;
		String MyYear;
		String MyHour;
		String MyMinutes;
		String MySeconds;
		
		//Form the Calendar object
		SimpleDateFormat formatter= new SimpleDateFormat("dd-MMM-yyyy");
		SimpleDateFormat fullformatter= new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
		Date MyDate = (fullformatter).parse(DateToConvert + TimeToConvert);
		Calendar cal = Calendar.getInstance();
		cal.setTime(MyDate);
		
		//Update Time as per the day start time from Admin Settings
		if (IsDayStartTime4PM){
			cal.add(Calendar.HOUR, -1 * 4);
		}
		
		//Get the new Date
		String NewDate = formatter.format(cal.getTime());
		MyDate = (formatter).parse(NewDate);
		
		//To get the Month
		MyMonth = String.valueOf(cal.get(Calendar.MONTH)+1);
		if (MyMonth.length() == 1 ){
			MyMonth = "0" + MyMonth;
		}
		
		//Get the Day and Year
		SplittedText = DateToConvert.split("-");
		MyDay = SplittedText[0];
		MyYear = SplittedText[2];
		
		//Get the Hour, Minute and Seconds
		SplittedText = TimeToConvert.trim().split(":");
		MyHour = SplittedText[0];
		MyMinutes = SplittedText[1];
		MySeconds = SplittedText[2];
		
		return MyYear + MyMonth + MyDay + MyHour + MyMinutes + MySeconds;
		
	}
	
	public void PrintTheList(NodeList nodeList){
		String Output;
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node oi = nodeList.item(i);
//			System.out.println(oi.getNodeValue());
			NodeList ChildList = oi.getChildNodes();
			for (int j = 1; j <=1; j++) {
				Output = ChildList.item(j).getTextContent();
				System.out.println(Output);
			}
		}
	}
	
	
	public List<Node> QueryDeviceData(String DeviceType,String StartDate, String StartTime, String EndDate, String EndTime, String InjectionEvent,String TimeZoneName) throws ParseException{
		
		if (StartTime.trim().isEmpty()){
			StartTime = GetTimeForEmptyValue("START");
		}
		
		if (EndTime.trim().isEmpty()){
			EndTime = GetTimeForEmptyValue("END");
		}
		Calendar GCal=null;
		int Nodevalue;
		
		SimpleDateFormat fullformatter= new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date FromDate = (fullformatter).parse(StartDate +" "+ StartTime);
		Calendar Fromcal = Calendar.getInstance();
		Fromcal.setTime(FromDate);
		
		Date ToDate = (fullformatter).parse(EndDate +" "+ EndTime);
		Calendar Tocal = Calendar.getInstance();
		Tocal.setTime(ToDate);
		
		//Get all the NodeList
		NodeList NodeCollection = XMLDoc.getElementsByTagName("InjectionHistory");
		Node Mynode = null;
		String NodeDateValue = "";
		String NodeTimeValue = "";
		Date NodeDate = null;
		//String TimeZoneName="";
		String ClinicTimeZone = TimeZoneName;
		if(DeviceType.compareToIgnoreCase("EP1")!=0){
		GCal = Calendar.getInstance(TimeZone.getTimeZone(ClinicTimeZone));
		Nodevalue=7;
	   }else{
		   GCal = Calendar.getInstance();
		   Nodevalue=5;
	   }
		   
		List<Node> MyReturnValues = new ArrayList<Node> ();
		for (int i=0; i<=NodeCollection.getLength()-1; i++){
			Mynode = NodeCollection.item(i);
		
			NodeDateValue = Mynode.getChildNodes().item(1).getTextContent();
			NodeTimeValue = Mynode.getChildNodes().item(3).getTextContent();

			NodeDate =(fullformatter).parse(NodeDateValue +" "+ NodeTimeValue);
			GCal.setTime(NodeDate);
//			if(GCal.after(Tocal)){
//				break;
//			}else{
				if (GCal.after(Fromcal) && GCal.before(Tocal)){ //Check FromDate and ToDate
					if (InjectionEvent.contains(Mynode.getChildNodes().item(Nodevalue).getTextContent())){
						MyReturnValues.add(Mynode);
						//System.out.println("MyNode"+Mynode.getTextContent());
						
					}
				}
			//}
		}
//System.out.println(MyReturnValues);
		return MyReturnValues;
		
	}
	
	
	public NodeList EP1QueryDeviceData(String StartDate, String StartTime, String EndDate, String EndTime, String InjectionEvent){
		try {
			String Query;
			String DateQuery = "//InjectionHistory";
			String EventQuery = "";
			String StartID, EndID;
			StartID = GetIDforDateStamp(StartDate,StartTime,"START");
			EndID = GetIDforDateStamp(EndDate,EndTime,"END");
			NodeList ReturnNodes = null;
			
			if (!StartID.startsWith("00000000")){
				DateQuery = DateQuery + "[" +DateXpath +">=" + StartID;
			}
			
			if (!EndID.startsWith("00000000")){
				DateQuery = DateQuery + " and " + DateXpath + "<=" + EndID ; 
			}
			
			InjectionEvent = InjectionEvent.trim().toUpperCase();
			if (!(InjectionEvent.startsWith("ALL") || InjectionEvent.isEmpty())){
				List<String> EventList = Arrays.asList(InjectionEvent.split("\\|"));
				for (String EventValue : EventList){
					EventQuery = EventQuery + EventXpath.replace("EVENTVALUE", EventValue) + " or ";
				}
				
				//Truncate the last "or" and add brackets
				EventQuery = EventQuery.substring(0,EventQuery.length()-3);
				EventQuery = "(" + EventQuery + ")";
			}
			
			if (EventQuery.isEmpty()){
				Query = DateQuery + "]";
			}else{
				Query = DateQuery + " and " +  EventQuery + "]";
			}
			
			
			System.out.println(Query);
			
			ReturnNodes = (NodeList) XPathEngine.compile(Query).evaluate(XMLDoc, XPathConstants.NODESET);
//			System.out.println(ReturnNodes.getLength());
//			PrintTheList(ReturnNodes);

			return ReturnNodes;
			
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, "Exception occured while parsing XML. " + e.getMessage());
			return null;
		}
		
	}
	
	public String QueryDeviceSettings(String XPathForSettings){
		try {
			Node ReturnNode = (Node) XPathEngine.compile(XPathForSettings).evaluate(XMLDoc, XPathConstants.NODE);
			return ReturnNode.getTextContent();
		} catch (Exception e) {
			Env.logger.log(Level.ERROR, "Exception occured while parsing XML. " + e.getMessage());
			return null;
		}
	}
	
	
	
}
