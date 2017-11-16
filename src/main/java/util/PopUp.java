package functions;

import org.apache.log4j.Level;
import org.sikuli.remote.client.RemoteScreen;

public class PopUp implements Runnable {

	private static RemoteScreen RS;
	private static String ImgPath;
	private String ScriptToRun;

	public PopUp(String ScriptName) {
		this.ScriptToRun = ScriptName.toUpperCase();
	}

	protected PopUp() {

	}

	public static void GetScreen() {

		String RSPlatform;
		String RSBrowser;

		try {
			// RS = new RemoteScreen(Env.MachineName + ".hclt.corp.hcl.in");
			RS = new RemoteScreen(Env.MachineName);
			RS.setMinSimilarity(0.9);

			// Set the RSPlatform
			if (Env.OSUsed.startsWith("WINXP")) {
				RSPlatform = "WINXP";
			} else if (Env.OSUsed.startsWith("WINVISTA")) {
				RSPlatform = "WINVISTA";
			} else if (Env.OSUsed.startsWith("WIN7")) {
				RSPlatform = "WIN7";
			} else if (Env.OSUsed.startsWith("WIN8")) {
				RSPlatform = "WIN8";
			} else {
				RSPlatform = "MAC";
			}

			// Set the RSBrowser
			RSBrowser = Env.GetBrowserUsed().substring(0, 2);
			if (RSBrowser.compareToIgnoreCase("IE") == 0) {
				if (Env.BrowserName.contains("ver6")
						|| Env.BrowserName.contains("ver7")) {
					RSBrowser = "IE-Old"; // Old Security PopUp
				} else if (Env.BrowserName.contains("ver8")
						&& (RSPlatform.contains("WINXP") || RSPlatform
								.contains("WINVISTA"))) {
					RSBrowser = "IE-Old"; // Old Security PopUp
				} else {
					RSBrowser = "IE-New"; // Old Security PopUp
				}
			}

			if (RSPlatform.compareToIgnoreCase("MAC") != 0) {
				ImgPath = Env.ImagePath + "\\" + RSPlatform + "\\" + RSBrowser
						+ "\\";
			} else {
				// ImgPath =
				// "/Volumes/Merck_Serono/LS/Automation/MS Automation/Common/img/"
				// +RSPlatform + "/" + RSBrowser + "/";
				ImgPath = "/Users/TestPC/Desktop/Automation/MAC/" + RSBrowser
						+ "/";
			}

		} catch (Exception e) {
			if (Env.IsRunningFromDriver) {
				Env.logger.log(Level.ERROR, e);
			}
		}
	}

	public void run() {
		if (this.ScriptToRun.compareTo("BROWSERSTARTUP") == 0) {
			BrowserStartUp();
		} else if (this.ScriptToRun.compareTo("CONFIRMCERT") == 0) {
			ConfirmCert();
		} else if (this.ScriptToRun.compareTo("PROXYLOGIN") == 0) {
			ProxyLogin();
		} else if (this.ScriptToRun.compareTo("DATAPREVENTION") == 0) {
			DataPrevention();
		} else if (this.ScriptToRun.compareTo("OPERAPOPUPHANDLING") == 0) {
			OperaPopUpHandling();
		}
	}

	public void ConfirmCert() {
		// ConfirmCert is not appearing nowadays... (appearing again in Integ)
		if (Env.RunEnvironment.compareToIgnoreCase("LocalTestServer") != 0
				&& !Env.GetBrowserUsed().startsWith("IE")) {
			try {
				RS.waitUntil(ImgPath + "CertAllowMe.png", 15);
				RS.click(ImgPath + "CertAllowMe.png");
			} catch (Exception e) {
				Env.logger
						.log(Level.INFO, "Certificate error is not displayed");
			}
		}
	}

	public void ServerLogin() {
		// if (Env.RunEnvironment.compareToIgnoreCase("LocalTestServer")!=0){
		// try {
		// //For all browser except OP
		// if (!Env.GetBrowserUsed().startsWith("OP")){
		// RS.waitUntil(ImgPath+"ServerLogin.png",15);
		// RS.type(ImgPath+"UserName.png",Env.ServerLoginID);
		// RS.type(ImgPath+"Password.png",Env.ServerLoginPhrase);
		// if (ImgPath.endsWith("SA\\")){ //Remember Me checkbox, should be
		// checked if the browser is SA or OP
		// RS.click(ImgPath+"RememberMe.png");
		// }
		// RS.click(ImgPath+"Login.png");
		// }else{
		// //For OP browser alone
		// RS.waitUntil(ImgPath+"ServerLogin.png",15);
		// RS.type(ImgPath+"UserName.png",Env.ServerLoginID);
		// RS.type(ImgPath+"Password.png",Env.ServerLoginPhrase);
		// RS.click(ImgPath+"RememberMe.png"); //Remember Me checkbox, should be
		// checked if the browser is SA or OP
		// RS.click(ImgPath+"Login.png");
		// }
		// } catch (Exception e) {
		// Env.logger.log(Level.INFO,"ServerLogin is not displayed");
		// }
		// }
	}

	public void ProxyLogin() {

		if (Env.GetBrowserUsed().startsWith("SA")
				|| Env.GetBrowserUsed().startsWith("OP")) {
			// boolean exit = true;
			try {
				RS.waitUntil(ImgPath + "ProxyLogin.png", 15);
				// For all browser except OP
				if (!Env.GetBrowserUsed().startsWith("OP")) {
					RS.type(ImgPath + "UserName.png", "hcltech\\" + Env.UserID);
					RS.type(ImgPath + "Password.png",
							UtilClass.TripleOut(Env.UserPhrase));
					RS.click(ImgPath + "RememberMe.png");
					RS.click(ImgPath + "Login.png");

				} else { // For Opear alone
					RS.type(ImgPath + "UserName.png", "hcltech\\" + Env.UserID);// If
																				// not,
																				// procced
																				// with
																				// the
																				// Normal
																				// flow
					RS.type(ImgPath + "Password.png",
							UtilClass.TripleOut(Env.UserPhrase));
					RS.click(ImgPath + "RememberMe.png"); // Remember Me
															// checkbox, should
															// be checked if the
															// browser is SA or
															// OP
					RS.click(ImgPath + "Login.png");
				}

				// //Wait until the popup is closed
				// do{
				// try{
				// RS.waitUntil(ImgPath+"ProxyLogin.png",3);
				// Thread.sleep(5000);
				// }catch(Exception e){
				// exit = false;
				// }
				// }while(exit);

			} catch (Exception e) {
				Env.logger.log(Level.INFO, "ProxyLogin is not displayed");
			}
		}
	}

	public void BrowserStartUp() {
		int iLoop = 0;

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		do {
			iLoop++;
			try {
				if (Env.GetBrowserUsed().startsWith("SA")
						|| Env.GetBrowserUsed().startsWith("OP")) {
					RS.waitUntil(ImgPath + "CertAllowMe.png", 5);
					ConfirmCert();
				}
			} catch (Exception ignore) {
			}
			try {
				if (Env.GetBrowserUsed().startsWith("SA")
						|| Env.GetBrowserUsed().startsWith("OP")) {
					RS.waitUntil(ImgPath + "ProxyLogin.png", 5);
					ProxyLogin();
				}
			} catch (Exception ignore) {
			}
			try {
				if (Env.RunEnvironment.compareToIgnoreCase("LocalTestServer") != 0) {
					RS.waitUntil(ImgPath + "ServerLogin.png", 3);
					ServerLogin();
				}
			} catch (Exception ignore) {
			}
		} while (iLoop < 3);

	}

	public void OperaPopUpHandling() {
		// int iLoop = 0;
		Env.logger.log(Level.INFO, "Starting Opera Proxy PopUp...");
		do {
			// iLoop++;
			try {
				RS.waitUntil(ImgPath + "ProxyLogin.png", 2);
				Env.logger.log(Level.INFO,
						"Opera popup is displayed in between test run");
				OperaStatus("HandlePopUp");
			} catch (Exception ignore) {
			}
			// }while(iLoop < 2);
		} while (Env.BrowserObj != null);
		Env.logger.log(Level.INFO, "Ending Opera Proxy PopUp...");
	}

	public synchronized void OperaStatus(String ThreadName) {
		if (ThreadName.startsWith("HandlePopUp")) {
			Env.logger.log(Level.INFO, "Opera Proxy PopUp is displaying....");
			ProxyLogin();
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else {
			// ThreadName is CheckStatus -- Nothing needs to be done. This
			// thread will wait until HandlePopUp is completed.
		}
	}

	public void DataPrevention() {

		// To Close Data Prevention Execution which happens only in Safari on
		// WinXP32
		try {
			RS.waitUntil(ImgPath + "DataPreventionClose.png", 15);
			RS.click(ImgPath + "DataPreventionClose.png");
			Env.logger.log(Level.INFO,
					"DataPreventionExecution Popup is displayed and handled");
		} catch (Exception e) {
			Env.logger.log(Level.INFO,
					"DataPreventionExecution Popup is not displayed");
		}

		// To Close the Invalid Certificate error
		try {
			RS.waitUntil(ImgPath + "CertAllowMe.png", 5);
			ConfirmCert();
		} catch (Exception e) {

		}

	}

}
