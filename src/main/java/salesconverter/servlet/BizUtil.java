package salesconverter.servlet;

import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;

import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;



public class BizUtil {

	public static boolean isNullString (String p_text){
		if(p_text != null && p_text.trim().length() > 0 && !"null".equalsIgnoreCase(p_text.trim())){
		return false;
		}
		else{
		return true;
		}
		}

		public static boolean isBlank(final CharSequence cs) {
		int strLen;
		if (cs == null || (strLen = cs.length()) == 0) {
		return true;
		}
		for (int i = 0; i < strLen; i++) {
		if (!Character.isWhitespace(cs.charAt(i))) {
		return false;
		}
		}
		return true;
		}
		public static String callPostAPIJSON(String urlstr, JSONObject Obj) {



		StringBuilder response =null;
		int responseCode = 0;

		try {

		HttpURLConnection con = null;


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


		URL url = new URL(urlstr);
		con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("POST");

		con.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
		con.setRequestProperty("Accept-Charset", "UTF-8");

		con.setDoOutput(true);

		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(wr, "UTF-8"));
		writer.write(Obj.toString());
		writer.close();
		wr.close();

		responseCode = con.getResponseCode();

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		response = new StringBuilder();

		while ((inputLine = in.readLine()) != null) {
		response.append(inputLine);
		}
		in.close();



		}
		catch (Exception e) {
		e.printStackTrace();
		//e.getMessage();
		}
		return response.toString();

		}
		public static void main(String args[]) throws JSONException {
			JSONObject fetchPrimaryKey = new JSONObject();
			fetchPrimaryKey.put("email", "salesautoconvertuser1@gmail.com");
			fetchPrimaryKey.put("group", "G1");
			fetchPrimaryKey.put("MailTempName", "SalesTestLead");
			fetchPrimaryKey.put("lgtype", "");
			String response=BizUtil.callPostAPIJSON("https://bizlem.io:8083/portal/LeadAutoConvert_To_Key", fetchPrimaryKey);
			System.out.println("response = "+response);
//			https://bizlem.io:8083/portal/LeadAutoConvert_To_Key
			
			
		}
}
