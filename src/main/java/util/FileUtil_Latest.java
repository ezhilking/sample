package functions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.log4j.Level;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.openqa.selenium.Cookie;

import com.google.common.io.Files;

public class FileUtil_Latest{

	protected String SourcePath;
	protected String DestinationPath;
	private String FileExtension;
	private String DownloadedFileName = "";
	private String ActualTextFile = "";
	private Easypod RunningSet;
	private String UserInCaseOfError;
	private String ParsedText = "Error";
	private int ValidateText = 0;
	public String ExpectedLocalizedFileName = null;
	private String PDFPassword = null;

	protected FileUtil_Latest(Easypod RunningApp, String Username){
		this.RunningSet = RunningApp;
		this.UserInCaseOfError = Username;
	}

	protected void SetFilePath(String FilePath){
		this.DestinationPath = FilePath;
		this.ValidateText = 0;
	}

	private Set<Cookie> GetCookieFromFox(){
		String URLToLaunch = Env.ECKOURL;
		if (Env.AppModule.startsWith("ECOS")){
			URLToLaunch = Env.ECOSURL;
		}
		this.RunningSet.OpenSecondBrowser(URLToLaunch);
		this.RunningSet.Login(UserInCaseOfError, UserInCaseOfError);
		if(UserInCaseOfError.startsWith(this.RunningSet.TestData("HCPName"))){
			this.RunningSet.EnterSecurePharse(this.RunningSet.TestData("HCPSecurityPharse"));
		}
		Set<Cookie> CookiesFromFox = UtilClass.browser.manage().getCookies();
		this.RunningSet.SwitchActiveBrowser();
		this.RunningSet.CloseSecondBrowser();
		return CookiesFromFox;

	}

	private BasicCookieStore mimicCookieState(Set<Cookie> seleniumCookieSet) {

		if (!seleniumCookieSet.toString().contains("hcl.com")){//For Email Downloads, do not modify the cookies.
			if(seleniumCookieSet.isEmpty() || !seleniumCookieSet.toString().contains("JSESSIONID")){
				seleniumCookieSet = GetCookieFromFox();
			}
		}

		BasicCookieStore mimicWebDriverCookieStore = new BasicCookieStore();
		URL CurrenetURL = null;

		try {
			CurrenetURL = new URL(UtilClass.browser.getCurrentUrl());
		} catch (MalformedURLException e) {

		}

		for (Cookie seleniumCookie : seleniumCookieSet) {

			BasicClientCookie duplicateCookie = new BasicClientCookie(seleniumCookie.getName(), seleniumCookie.getValue());

			//Set Cookie Domain
			if (seleniumCookie.getDomain() != null){
				duplicateCookie.setDomain(seleniumCookie.getDomain());
			}else{
				if (!seleniumCookieSet.toString().contains("hcl.com")){//For Email Downloads, do not modify the cookies.
					String Domain = CurrenetURL.getAuthority();
					int PortIndex = Domain.lastIndexOf(":");
					if (PortIndex != -1){
						Domain = Domain.substring(0,PortIndex);
					}
					duplicateCookie.setDomain(Domain);
				}
			}

			//Set Cookie Path
			if (seleniumCookie.getPath().compareToIgnoreCase("/") !=0 ){
				duplicateCookie.setPath(seleniumCookie.getPath());
			}else{
				if (!seleniumCookieSet.toString().contains("hcl.com")){//For Email Downloads, do not modify the cookies.
					if (Env.AppModule.compareTo("ECKO") == 0){
						duplicateCookie.setPath("/ecko");
					}else{
						duplicateCookie.setPath("/ecos");
					}
				}
			}

			//Set Security
			duplicateCookie.setSecure(seleniumCookie.isSecure());

			//Set Expiry
			duplicateCookie.setExpiryDate(null);

			//Add the cookie
			mimicWebDriverCookieStore.addCookie(duplicateCookie);
		}

		return mimicWebDriverCookieStore;
	}


	public boolean DispatchFile() {

		String ActualFileName = "FILE NAME NOT FOUND.";
		URL fileToDownload;
		File downloadedFile;
		HttpGet httpget;
		CloseableHttpClient client = null;
		HttpClientContext localContext;
		HttpResponse response;
		CredentialsProvider credsProvider = null;
		RequestConfig requestConfig = null;

		try{

			fileToDownload = new URL(this.SourcePath.replaceAll(" ", "%20"));
			downloadedFile = new File(this.DestinationPath);
			if (downloadedFile.canWrite() == false) downloadedFile.setWritable(true);

			//GetUserPhrase if applicable
			if(Env.isProxyRequired && Env.UserPhrase == null){
				String PhrasePath = "C:/Users/"+Env.UserID+"/Desktop/Spring.txt";
				try{//Get UserPhrase
					BufferedReader SpringReader = Files.newReader(new File(PhrasePath), Charset.forName("UTF-8"));
					String Spring = SpringReader.readLine();
					SpringReader = null;
					Env.UserPhrase = UtilClass.TripleIn(Spring.replace("Sprinkler", "").replace("Viola", "").toCharArray());
				}catch(Exception e){
					Env.logger.log(Level.ERROR,"Error while reading UserPhrase from the file " + PhrasePath,e);
					Env.logger.log(Level.ERROR,"Since error occured, setting Proxy Required as false",e);
					Env.isProxyRequired = false;
				}
			}

			//Prepare Client
			if(Env.isProxyRequired){
				credsProvider = new BasicCredentialsProvider();
				credsProvider.setCredentials(new AuthScope(Env.ProxyServer, 80),new UsernamePasswordCredentials(Env.UserID, UtilClass.TripleOut(Env.UserPhrase)));
				client = HttpClients.custom().setDefaultCredentialsProvider(credsProvider).build();
			}else{
				client = HttpClients.custom().build();
			}
			client = WrapClient(client);

			//Prepare httpget
			if(Env.isProxyRequired){
				HttpHost proxyServer = new HttpHost(Env.ProxyServer, 80);
				requestConfig = RequestConfig.custom().setProxy(proxyServer).setRedirectsEnabled(true).build();
			}else{
				requestConfig = RequestConfig.custom().setRedirectsEnabled(true).build();
			}
			httpget = new HttpGet(fileToDownload.toURI());
			httpget.setConfig(requestConfig);

			//Prepare LocalContext
			localContext = HttpClientContext.create();
			localContext.setCookieStore(mimicCookieState(UtilClass.browser.manage().getCookies()));

			//Execute Client with httpget and LocalContext
			RunningSet.ObjectExists("General_HideCursor_ST"); //To keep the node session alive, this is done to avoid HUB clean session Timeout
			response = client.execute(httpget,localContext);
			RunningSet.ObjectExists("General_HideCursor_ST"); //To keep the node session alive, this is done to avoid HUB clean session Timeout
			if (ExpectedLocalizedFileName != null){
				if (response.getFirstHeader("Content-disposition") != null){
					ActualFileName = response.getFirstHeader("Content-disposition").getElements()[0].getParameter(0).getValue();
					//File Name validation
					if (ActualFileName.contains(".")){
						String FileNameWithoutExtension =  ActualFileName.substring(0,ActualFileName.lastIndexOf(".")).trim();
						if (FileNameWithoutExtension.length()<1){
							RunningSet.Report.Write("FileName_DT","Downloaded File should have a file name","File Name is not present : " + FileNameWithoutExtension,"Fail",false);
						}
					}else{
						RunningSet.Report.Write("FileName_DT","Downloaded File should have a file extension","File extension is not present : " + ActualFileName,"Fail",false);
					}
				}
			}
			int StatusCode = response.getStatusLine().getStatusCode();
			FileUtils.copyInputStreamToFile(response.getEntity().getContent(), downloadedFile);
			this.DownloadedFileName = downloadedFile.getName();
			response.getEntity().getContent().close();
			if (StatusCode == 200){
				//Commenting the PDF File name validation, due to Unicode validation.
				//				//Download is complete, so check whether the filename is translated correctly
				//				if (ExpectedLocalizedFileName != null){
				//					if (ActualFileName.startsWith(ExpectedLocalizedFileName)){
				//						RunningSet.Report.Write("FileName_DT",ExpectedLocalizedFileName,ActualFileName,"Pass",this.DownloadedFileName);
				//					}else{
				//						RunningSet.Report.Write("FileName_DT",ExpectedLocalizedFileName,ActualFileName,"Fail",this.DownloadedFileName);	
				//					}
				//				}

				String ActualFileType = "";
				String ExpectedFileType = this.DownloadedFileName.substring(this.DownloadedFileName.lastIndexOf(".")+1, this.DownloadedFileName.length());
				boolean FileTypeValidation = false;
				if (ExpectedFileType.endsWith("xls") || ExpectedFileType.endsWith("csv")){
					FileTypeValidation = true; 
				}else{
					ActualFileType = response.getEntity().getContentType().getValue();
					FileTypeValidation = ActualFileType.contains(ExpectedFileType);

					if (!FileTypeValidation && Env.CurrentTestName.toUpperCase().contains("AUDIT")){//Handled for Export Audit Log
						if (ActualFileType.contains("application/octet-stream")){
							FileTypeValidation = true;
						}else{
							FileTypeValidation = false;
						}
					}


					if (!FileTypeValidation && Env.CurrentTestName.contains("EnrollmentReports") && ExpectedFileType.endsWith("pdf")){//Handled for EnrollmentReports
						FileTypeValidation = true;
					}


				}

				if (FileTypeValidation){
					Env.logger.log(Level.INFO,"URL: " + this.SourcePath);
					Env.logger.log(Level.INFO,"File downloaded to '" + downloadedFile.getAbsolutePath() + "'");
					return true;
				}else{
					Env.logger.log(Level.ERROR,"URL: " + this.SourcePath);
					Env.logger.log(Level.ERROR,"HTTP GET request status: " + StatusCode);
					Env.logger.log(Level.ERROR,"Incorrect Content Type: " + ActualFileType);
					return false;
				}
			}else{
				Env.logger.log(Level.ERROR,"URL: " + this.SourcePath);
				Env.logger.log(Level.ERROR,"HTTP GET request status: " + StatusCode);
				return false;
			}


		}catch(Exception e){
			Env.logger.log(Level.ERROR,this.DestinationPath,e);
			return false;
		}finally{
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			fileToDownload = null;
			downloadedFile = null;
			httpget = null;
			client = null;
			localContext = null;
			response = null;
		}
	}

	public static CloseableHttpClient WrapClient(CloseableHttpClient base){
		try{
			SSLContext ctx = SSLContext.getInstance("TLS");
			X509TrustManager tm = new X509TrustManager() {
				public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {}
				public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {                }
				public X509Certificate[] getAcceptedIssuers() {return null;}
			};

			X509HostnameVerifier verifier = new X509HostnameVerifier() {
				@Override
				public void verify(String string, SSLSocket ssls) throws IOException {
				}
				@Override
				public void verify(String string, X509Certificate xc) throws SSLException {
				}
				@Override
				public void verify(String string, String[] strings, String[] strings1) throws SSLException {
				}
				@Override
				public boolean verify(String string, SSLSession ssls) {
					return true;
				}
			};

			ctx.init(null, new TrustManager[]{tm}, null);
			//            SSLSocketFactory ssf = new SSLSocketFactory(ctx);
			//            ssf.setHostnameVerifier(verifier);
			SSLConnectionSocketFactory ssf = new SSLConnectionSocketFactory(ctx,verifier);

			//            ClientConnectionManager ccm = base.getConnectionManager();
			//            SchemeRegistry sr = ccm.getSchemeRegistry();//here
			//            sr.register(new Scheme("https", ssf, 443));

			Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
			.register("http", PlainConnectionSocketFactory.INSTANCE) 
			.register("https", ssf)
			.build();

			PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
			CloseableHttpClient httpclient = HttpClients.custom()
			.setConnectionManager(connManager)
			.build();

			//            return new DefaultHttpClient(ccm, base.getParams());
			return httpclient;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}

	}

	public void ExtractText() {

		File FileToExtract;
		PDFTextStripper pdfStripper;
		BufferedReader ExpectedBuffer;
		StringBuffer sb;
		BufferedWriter writer = null;
		File ActualFile;
		FileInputStream fin = null;
		COSDocument cosDoc = null;
		PDDocument pdDoc =null;

		try {

			FileExtension  = this.DestinationPath.substring(this.DestinationPath.lastIndexOf(".")+1,this.DestinationPath.length()).toUpperCase();
			ActualFile = new File(this.DestinationPath.replace("."+FileExtension, "-"+FileExtension.toUpperCase()) + ".txt");
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(ActualFile),Charset.forName("UTF-8")));

			FileToExtract = new File(this.DestinationPath);
			if (this.DestinationPath.endsWith("pdf")){

				//For PDF
				fin = new FileInputStream(FileToExtract);
				pdDoc = PDDocument.load(fin);
				RunningSet.ObjectExists("General_HideCursor_ST"); //To keep the node session alive, this is done to avoid HUB clean session Timeout

				//If the PDF is protected with PDF, then decrypt it before extracting the text
				if (pdDoc.isEncrypted()){
					pdDoc = PDDocument.load(fin,PDFPassword);
				}

				pdfStripper = new org.apache.pdfbox.text.PDFTextStripper();
				RunningSet.ObjectExists("General_HideCursor_ST"); //To keep the node session alive, this is done to avoid HUB clean session Timeout
				pdfStripper.writeText(pdDoc, writer);
				RunningSet.ObjectExists("General_HideCursor_ST"); //To keep the node session alive, this is done to avoid HUB clean session Timeout
				writer.flush();

				ExpectedBuffer = Files.newReader(ActualFile, Charset.forName("UTF-8"));
				sb = new StringBuffer();
				String CurrentLine;
				while ((CurrentLine=ExpectedBuffer.readLine())!=null) {
					sb.append(CurrentLine + " ") ;
					sb.append("\n");
				}
				this.ParsedText=  sb.toString();
				ExpectedBuffer.close();


			}else{
				//For CSV
				String UTFformat = "UTF-16LE";

				//By TestName
				if (Env.CurrentTestName.contains("Auxological") || Env.CurrentTestName.contains("AdherenceList")||Env.CurrentTestName.contains("CombinationGraph")){
					UTFformat = "UTF-8";
				}

				//By BrowserTitle
				String CurrentBrowserTitle = RunningSet.GetBrowserTitle();
				if (CurrentBrowserTitle.contains(RunningSet.LangData("AdherenceReport")) || CurrentBrowserTitle.contains(RunningSet.LangData("AuxologicalReports"))){
					UTFformat = "UTF-8";
				}

				ExpectedBuffer = Files.newReader(FileToExtract, Charset.forName(UTFformat));
				sb = new StringBuffer();
				String CurrentLine;
				while ((CurrentLine=ExpectedBuffer.readLine())!=null) {
					sb.append(CurrentLine + " ") ;
					sb.append("\n");
				}
				this.ParsedText=  sb.toString();
				ExpectedBuffer.close();
				writer.write(ParsedText);
				writer.flush();
			}



			this.ActualTextFile = ActualFile.getName();
			ParsedText = ParsedText.replaceAll("\\n", " ");//replacing carriage return with space.
			ParsedText = ParsedText.replaceAll("\\r", " ");
			ParsedText = ParsedText.replaceAll("\u00a0", " ");//replacing all Unicode space with space
			ParsedText = ParsedText.replaceAll(" {1,}", ""); //replacing one or more space, with null


		}catch (Exception e){
			Env.logger.log(Level.ERROR, "Error occured while extracting text from " + this.DownloadedFileName,e);
		}finally{
			try {
				if (fin != null){
					fin.close();
					fin = null;
				}
				if (pdDoc != null){
					pdDoc.close();
					pdDoc = null;
				}
				if (cosDoc != null){
					cosDoc.close();
					cosDoc = null;
				}
				if (writer != null){
					writer.close();
					writer = null;
				}
				FileToExtract = null;
				cosDoc = null;
				pdfStripper = null;
				pdDoc = null;
				ExpectedBuffer = null;
				sb = null;
				ActualFile = null;
			} catch (IOException e) {
				Env.logger.log(Level.ERROR, "Error occured while extracting text.",e);
			}
		}
	}


	public boolean isTextContained(String ExpectedText, boolean CheckInFullFile){
		String StringToSearchIn = null;
		if (CheckInFullFile){
			StringToSearchIn = ParsedText;
		}else{
			StringToSearchIn = ParsedText.substring(ValidateText);
		}
		return StringToSearchIn.contains(ExpectedText.replaceAll(" ", ""));
	}

	private boolean FileContainsText(String BiggerText, String SmallerText){
		boolean isPass = false;
		//		int PrevCharIndex;
		//		int NextCharIndex;
		//		String PrevChar;
		//		String NextChar;
		//		BiggerText = " " + BiggerText + " ";

		//		while (BiggerText.contains(SmallerText)){
		//			PrevCharIndex = BiggerText.indexOf(SmallerText) - 1;
		//			PrevChar = BiggerText.substring(PrevCharIndex, PrevCharIndex+1);
		//			NextCharIndex = BiggerText.indexOf(SmallerText) + SmallerText.length();
		//			NextChar = BiggerText.substring(NextCharIndex, NextCharIndex+1);
		//			
		//			if(!(Pattern.matches("[0-9.?!;:,\\–\\-\\)\\(= ]+", NextChar))) {
		//				isPass = false;
		//				BiggerText = BiggerText.substring(NextCharIndex,BiggerText.length());
		//			}else if(!(Pattern.matches("[0-9.?!;:,\\–\\-\\)\\(= ]+", PrevChar))) {
		//				isPass = false;
		//				BiggerText = BiggerText.substring(NextCharIndex,BiggerText.length());
		//			}else{
		if(BiggerText.contains(SmallerText)){
			isPass = true;
			int IndexLocation = BiggerText.indexOf(SmallerText) + SmallerText.length();
			ValidateText = ValidateText + IndexLocation;
			//				break;
			//			}
		}
		return isPass;
	}

	public void FileReporter(String StepID, String ExpectedText, String StepStatus){
		if (StepStatus.endsWith("Fail")){
			StepStatus = "Fail";
		}
		this.RunningSet.Report.Write(StepID,ExpectedText,ActualTextFile,StepStatus,this.DownloadedFileName);
	}

	public boolean ValidateText(String StepID, String ExpectedText){
		RunningSet.ObjectExists("General_HideCursor_ST"); //To keep the node session alive, this is done to avoid HUB clean session Timeout
		StepID = StepID + "_" + FileExtension;
		String TempParsedText = ParsedText.substring(ValidateText);
		if (FileContainsText(TempParsedText,ExpectedText.replaceAll(" ", ""))){
			FileReporter(StepID,ExpectedText,"Pass");
			return true;
		}else{
			FileReporter(StepID,ExpectedText,"Fail");
			return false;
		}

	}

	public String ValidateWithList(String StepID,int ToHowManyCharacters ,String ExpectedListOfValues)
	{
		String ReturnValue = "Not Found";
		StepID = StepID + "_" + FileExtension;
		String TempParsedText;
		int IndexLocation = 0;

		if (ToHowManyCharacters >30 || ToHowManyCharacters < 1){ //should be between 1 and 20
			ToHowManyCharacters = 30;
		}

		if (ValidateText+ ToHowManyCharacters >= ParsedText.length()){
			TempParsedText = ParsedText.substring(ValidateText,ParsedText.length());
		}else{
			TempParsedText = ParsedText.substring(ValidateText,ValidateText+ ToHowManyCharacters);
		}

		final List<String> ExpectedList = Arrays.asList(ExpectedListOfValues.split("\\|"));

		String ExpectedValueNoSpace = "";
		for (String ExpectedValue : ExpectedList){
			ExpectedValueNoSpace = ExpectedValue.replaceAll(" ", "");
			if (TempParsedText.contains(ExpectedValueNoSpace) && !ExpectedValueNoSpace.isEmpty()){
				FileReporter(StepID,ExpectedValue,"Pass");
				IndexLocation = TempParsedText.indexOf(ExpectedValueNoSpace) + ExpectedValueNoSpace.length();
				ValidateText = ValidateText + IndexLocation;
				ReturnValue =  ExpectedValue;
				break;
			}
		}

		if (ReturnValue.compareTo("Not Found")==0){
			FileReporter(StepID,ExpectedListOfValues,"Fail");
		}
		return ReturnValue;
	}

	public boolean SkipString(String SkipToString){
		SkipToString = SkipToString.replaceAll(" ", "");
		String TempParsedText = ParsedText.substring(ValidateText);
		if (TempParsedText.contains(SkipToString)){
			int IndexLocation = TempParsedText.indexOf(SkipToString) + SkipToString.length();
			ValidateText = ValidateText + IndexLocation;
			return true;
		}else{
			return false;
		}
	}

	public ExcelUtil OpenExcelObject(){
		RunningSet.ObjectExists("General_HideCursor_ST"); //To keep the node session alive, this is done to avoid HUB clean session Timeout
		File ExcelFile = new File(this.DestinationPath);
		Env.StaticScreenshotName = ExcelFile.getName();
		ExcelFile = null;
		try{
			return new ExcelUtil(this.DestinationPath,false);
		}catch(Exception e){
			Env.logger.log(Level.ERROR, "Unable to Open Excel file",e);
			return null;
		}
	}

	public void CloseExcelObject(){
		RunningSet.ObjectExists("General_HideCursor_ST"); //To keep the node session alive, this is done to avoid HUB clean session Timeout
		this.DestinationPath = null;
		Env.StaticScreenshotName = null;
	}

	public int ContainsText(String StepID, String EndString, String ExpectedListOfValues){

		RunningSet.ObjectExists("General_HideCursor_ST"); //To keep the node session alive, this is done to avoid HUB clean session Timeout
		String TempParsedText = ParsedText.substring(ValidateText);
		int toindex=TempParsedText.indexOf(EndString.replaceAll(" ", ""));

		int ToHowManyCharacters=toindex+ValidateText;
		int IndexLocation = 0;
		int i_loop=0;
		int temp=0;
		temp=i_loop+1;
		if (ValidateText+ ToHowManyCharacters >= ParsedText.length()){
			TempParsedText = ParsedText.substring(ValidateText,ParsedText.length());
		}else{
			TempParsedText = ParsedText.substring(ValidateText,ValidateText+ ToHowManyCharacters);
		}

		final List<String> ExpectedList = Arrays.asList(ExpectedListOfValues.split("\\|"));

		String ExpectedValueNoSpace = "";
		for (String ExpectedValue : ExpectedList){
			ExpectedValueNoSpace = ExpectedValue.replaceAll(" ", "");
			if (TempParsedText.contains(ExpectedValueNoSpace) && !ExpectedValueNoSpace.isEmpty()){				
				IndexLocation = TempParsedText.indexOf(ExpectedValueNoSpace) + ExpectedValueNoSpace.length();
				//TempParsedText=TempParsedText.replace(ExpectedValueNoSpace, "");	
				TempParsedText=TempParsedText.replaceFirst(ExpectedValueNoSpace, "");	
				FileReporter(StepID+"_"+temp,ExpectedValue,"Pass");
			}else{
				FileReporter(StepID+"_"+temp,ExpectedValue,"Fail");
			}
			i_loop=i_loop+1;
			temp=temp+1;
		}	
		return IndexLocation;
	}



	public void CheckAdherenceList(Easypod EPUsed){

		//String[] InjectionEvent  = {"Date","Time","Event","DeviceID","PrescribedDose","ActualDose"};
		String[] Completeinjection  = {"01 Mar 2012","07:31", "CompleteInjection","EP1DEVICE",EPUsed.Converter("1.0"),EPUsed.Converter("1.0")};
		String[] Partialinjection  = {"02 Mar 2012","08:32", "PartialInjection","EP1DEVICE",EPUsed.Converter("1.0"),EPUsed.Converter("0.02")};
		String[] Noinjection  = {"04 Mar 2012","00:00", "NoInjection","-",EPUsed.Converter("1.0"),EPUsed.Converter("0.0")};
		String[] Newcartridge  = {"06 Mar 2012","09:37", "NewCartridge","EP1DEVICE","-","-"};
		String[] NewDosageSet  = {"01 Mar 2012","07:30", "NewDosageSet","EP1DEVICE",EPUsed.Converter("1.0"),"-"};

		//Injection List

		ValidateText("NewDosageSet_date", EPUsed.TranslateDate(NewDosageSet[0],"dd MMM yyyy", false));
		ValidateWithList("NewDosageSet_time", 8, NewDosageSet[1]);
		ValidateText("NewDosageSet_event", EPUsed.LangData(NewDosageSet[2])+" (6 "+EPUsed.LangData("Day")+")");
		ValidateWithList("NewDosageSet_device", 15, EPUsed.GetDeviceID(NewDosageSet[3]));
		ValidateWithList("NewDosageSet_prescribeddose", 8, NewDosageSet[4]);
		ValidateWithList("NewDosageSet_actualdose", 3, NewDosageSet[5]);
		ValidateText("Completeinjection_date", EPUsed.TranslateDate(Completeinjection[0],"dd MMM yyyy", false));
		ValidateWithList("Completeinjection_time", 8, Completeinjection[1]);
		ValidateWithList("Completeinjection_event", 20, EPUsed.LangData(Completeinjection[2]));
		ValidateWithList("Completeinjection_device", 15, EPUsed.GetDeviceID(Completeinjection[3]));
		ValidateWithList("Completeinjection_prescribed_dose", 8, Completeinjection[4]);
		ValidateWithList("Completeinjection_actual_dose", 8, Completeinjection[5]);
		ValidateText("Partialinjection_date", EPUsed.TranslateDate(Partialinjection[0],"dd MMM yyyy", false));
		ValidateWithList("Partialinjection_time", 8, Partialinjection[1]);
		ValidateWithList("Partialinjection_event", 20, EPUsed.LangData(Partialinjection[2]));
		ValidateWithList("Partialinjection_device", 15, EPUsed.GetDeviceID(Partialinjection[3]));
		ValidateWithList("Partialinjection_prescribed_dose", 8, Partialinjection[4]);
		ValidateWithList("Partialinjection_actual_dose", 8, Partialinjection[5]);
		ValidateText("Noinjection_date", EPUsed.TranslateDate(Noinjection[0],"dd MMM yyyy", false));
		ValidateWithList("Noinjection_time", 8, Noinjection[1]);
		ValidateWithList("Noinjection_event", 18, EPUsed.LangData(Noinjection[2]));
		ValidateWithList("Noinjection_device", 15, Noinjection[3]);
		ValidateWithList("Noinjection_prescribed_dose", 8, Noinjection[4]);
		ValidateWithList("Noinjection_actual_dose", 8, Noinjection[5]);
		ValidateText("Newcartridge_date", EPUsed.TranslateDate(Newcartridge[0],"dd MMM yyyy", false));
		SkipString("2012");
		ValidateWithList("Newcartridge_time", 8, Newcartridge[1]);
		ValidateWithList("Newcartridge_event", 30, EPUsed.LangData(Newcartridge[2])+" (8.63 "+EPUsed.LangData("UnitMg")+")");
		ValidateWithList("Newcartridge_device", 15, EPUsed.GetDeviceID(Newcartridge[3]));
		ValidateWithList("Newcartridge_prescribed_dose", 8, Newcartridge[4]);
		ValidateWithList("Newcartridge_actual_dose", 8, Newcartridge[5]);

	}


	public void CheckAdherenceCalendar(Easypod EPUsed){

		String[] MonthlyAdherence = {"100.00%","100.00%","100.00%","100.00%","100.00%","41.38%","0.00%","0.00%","3.23%","3.23%","0.00%","17.67%"}; //Starting April 2011 and ending on Mar 2012. Adhernce value for sept 2011 has been updated due to EPCV-6,Updated September month adherence value from 40.00 to 41.38 
		String[] WeeklyAdherence = {"32.50%","69.23%","0.00%","0.00%","0.00%"};//Weekly Adherence for all the weeks in March 2012 starts with Sunday
		String[] WeeklyAdherenceMon = {"32.50%","64.29%","0.00%","0.00%","0.00%"}; //Weekly Adherence for all the weeks in March 2012 starts with Monday
		if (EPUsed.IsWeekStartingMonday()){
			WeeklyAdherence = WeeklyAdherenceMon;
			WeeklyAdherenceMon = null;
		}

		//		ValidateText("CalendarOverview", EPUsed.LangData("CalendarOverview"));

		ValidateText("Month"+4,EPUsed.LangData("MonthLong"+4) + " 2011"); //
		ValidateWithList("Aprilmonth", 8, EPUsed.Converter(MonthlyAdherence[0]));
		ValidateText("Month"+5,EPUsed.LangData("MonthLong"+5) + " 2011"); 
		ValidateWithList("Maymonth", 8, EPUsed.Converter(MonthlyAdherence[1]));
		ValidateText("Month"+6,EPUsed.LangData("MonthLong"+6) + " 2011"); //
		ValidateWithList("Junemonth", 8, EPUsed.Converter(MonthlyAdherence[2]));
		ValidateText("Month"+7,EPUsed.LangData("MonthLong"+7) + " 2011"); //
		ValidateWithList("Julymonth", 8, EPUsed.Converter(MonthlyAdherence[3]));
		ValidateText("Month"+8,EPUsed.LangData("MonthLong"+8) + " 2011"); //
		ValidateWithList("Augmonth", 8, EPUsed.Converter(MonthlyAdherence[4]));
		ValidateText("Month"+9,EPUsed.LangData("MonthLong"+9) + " 2011"); //
		ValidateWithList("Sepmonth", 8, EPUsed.Converter(MonthlyAdherence[5]));
		ValidateText("Month"+10,EPUsed.LangData("MonthLong"+10) + " 2011"); //
		ValidateWithList("Octmonth", 8, EPUsed.Converter(MonthlyAdherence[6]));
		ValidateText("Month"+11,EPUsed.LangData("MonthLong"+11) + " 2011"); //
		ValidateWithList("Novmonth", 8, EPUsed.Converter(MonthlyAdherence[7]));
		ValidateText("Month"+12,EPUsed.LangData("MonthLong"+12) + " 2011"); //
		ValidateWithList("Decmonth", 8, EPUsed.Converter(MonthlyAdherence[8]));
		ValidateText("Month"+1,EPUsed.LangData("MonthLong"+1) + " 2012"); //
		ValidateWithList("Janmonth", 8, EPUsed.Converter(MonthlyAdherence[9]));
		ValidateText("Month"+2,EPUsed.LangData("MonthLong"+2) + " 2012"); //
		ValidateWithList("Febmonth", 8, EPUsed.Converter(MonthlyAdherence[10]));
		ValidateText("Month"+3,EPUsed.LangData("MonthLong"+3) + " 2012"); //
		ValidateWithList("Marmonth", 8, EPUsed.Converter(MonthlyAdherence[11])); 
		SkipString(" 3");
		ValidateWithList("March_adherence", 8, EPUsed.Converter(WeeklyAdherence[0]));
		SkipString(" 10");
		ValidateWithList("March_adherence", 8, EPUsed.Converter(WeeklyAdherence[1]));
		SkipString(" 17");
		ValidateWithList("March_adherence", 8, EPUsed.Converter(WeeklyAdherence[2]));
		SkipString(" 24");
		ValidateWithList("March_adherence", 8, EPUsed.Converter(WeeklyAdherence[3]));
		SkipString(" 31");
		ValidateWithList("March_adherence", 8, EPUsed.Converter(WeeklyAdherence[4]));
	}

	public void SetPasswordForPDF(String PasswordOfThePDF){
		PDFPassword = PasswordOfThePDF;
	}

	public void VerifyAuxologicalPDF(String GraphType, String Range,boolean StandardDeviation,String YAxisText,String XAxisText,String PatientDetail,String GirlsorBoys, String HCPName, String ClinicName){ 		

		//Graph type
		ValidateText("Gender",GirlsorBoys);
		ValidateText("Range",Range);
		ValidateText("Percentiles",RunningSet.LangData("Percentiles"));
		ValidateText("Patient",RunningSet.LangData("Patient"));
		if(StandardDeviation){
			ValidateText("StandardDeviation",RunningSet.LangData("StandardDeviation"));
		}
		ValidateText("YAxis",YAxisText);
		ValidateText("XAxis",XAxisText);

		//Report Title
		ValidateText("ReportTitle",RunningSet.LangData("PatientReport"));
		ValidateText("PatientDetails",RunningSet.FormatPatientName(PatientDetail));
		if(!(PatientDetail).startsWith("jsn")){
			if(!RunningSet.CompareText(Env.CountryUsed, "United Kingdom")){
				ValidateText("UniqueID", RunningSet.TestData("UniqueID"));
			}
		}
		ValidateText("GraphType",GraphType);

		//Side Text
		ValidateText("ReportDate",RunningSet.LangData("Date"));
		ValidateWithList("ReportDateValue",10,RunningSet.MonthShortAll());
		ValidateText("HCP",RunningSet.LangData("HCP")); 
		ValidateText("HCPName",HCPName);
		ValidateText("Clinic",RunningSet.LangData("Clinic"));
		ValidateText("ClinicName",ClinicName);

		//Footer Text
		ValidateText("Disclaimer",RunningSet.LangData("Disclaimer"));
		ValidateText("DisclaimerText",RunningSet.LangData("PDFDisclaimer"));
	}

	public void VerifyAuxologicalExcel(String Age, String GrowthType){
		ExcelUtil Excel = this.OpenExcelObject();
		RunningSet.CompareText("Age_XLS", Age, Excel.GetData(0,0));
		RunningSet.CompareText("GrowthType_XLS", GrowthType, Excel.GetData(1,0));
		this.CloseExcelObject();
	}

}
