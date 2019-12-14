package services;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.util.ResourceBundle;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONObject;



public class CallGetService {
	public JSONObject getmassMailType(String email, String group) {
	
		JSONObject respobj=null;
			JSONObject obj = null;
			// https://bizlem.io:8083/portal/servlet/service/mailSelectedKey?email=salesautoconvertuser1@gmail.com&group=G1
			// http://development.bizlem.io:8087/com.carrotruleangular/GetRuleEngine_Name
			String CheckfreetrailCart_api = ResourceBundle.getBundle("config").getString("rateapimailSelectedKey")
					+ "?email=" + email + "&group=" + group+"&lgtype=normal";
			System.out.println("CheckfreetrailCart_api = " + CheckfreetrailCart_api);
			StringBuffer response = null;
			int responseCode = 0;
			try {
				TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
					public java.security.cert.X509Certificate[] getAcceptedIssuers() {
						return null;
					}

					public void checkClientTrusted(X509Certificate[] certs, String authType) {
					}

					public void checkServerTrusted(X509Certificate[] certs, String authType) {
					}

				} };

				try {
					SSLContext sc = SSLContext.getInstance("SSL");
					sc.init(null, trustAllCerts, new java.security.SecureRandom());
					HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				// Create all-trusting host name verifier
				HostnameVerifier allHostsValid = new HostnameVerifier() {
					public boolean verify(String hostname, SSLSession session) {
						return true;
					}
				};
				// Install the all-trusting host verifier
				HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
				
				
				URL url = new URL(CheckfreetrailCart_api);
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.setRequestMethod("GET");
	
				con.setDoOutput(true);
	
				responseCode = con.getResponseCode();
				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String inputLine;
				response = new StringBuffer();
				while ((inputLine = in.readLine()) != null) {
	
					response.append(inputLine);
				}
				in.close();
	
				String resp = response.toString();
				System.out.println("responseCode : " + responseCode + "\n" + "ResponseBody== : " + resp);
				obj = new JSONObject(resp);
				
				// System.out.println("obj.toString() : "+obj.toString());
	
			} catch (Exception e) {
				System.out.println("Exception ex  upload to server callnewscript: " + e.getMessage() + e.getStackTrace());
	
			}
	
			return obj;
	
		}
	public static void main(String args[]) {

		JSONObject rs = new CallGetService().getmassMailType("salesautoconvertuser1@gmail.com", "G1");
		System.out.println("method : " + rs);
	}
}

