package services;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ResourceBundle;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;

public class CallPostService {
	public static void main(String args[]) throws JSONException, IOException {
		String apiurl=	ResourceBundle.getBundle("config").getString("doctigerapi");
		String jss="{\"MailTempName\":\"mailexplore\",\"templateName\":\"\",\"typeDataSource\":\"Enter manually\",\"AttachtempalteType\":\"\",\"esignature\":\"false\",\"twofactor\":\"false\",\"esigntype\":\"\",\"Email\":\"jobs@bizlem.com\",\"group\":\"Salesdoc\",\"lgtype\":\"null\",\"multipeDropDown\":[\"1\",\"3\"],\"Type\":\"Generation\",\"data\":[{\"Email\":\"mohit.raj@bizlem.io\",\"FirstName\":\"mohit\",\"LastName\":\"mohit\",\"PhoneNumber\":\"+971 (4) 3914900\",\"Country\":\"UAE \",\"CompanyName\":\"3i infotech\",\"CompanyHeadCount\":100,\"Industry\":\"Software\",\"Source\":\"Friend\",\"SessionCount\":0,\"AvgSesionDuration\":0},{\"Email\":\"tejal.jabade10@gmail.com\",\"FirstName\":\"tejal\",\"LastName\":\"vivek\",\"PhoneNumber\":\"+971 (4) 3914900\",\"Country\":\"UAE \",\"CompanyName\":\"3i infotech\",\"CompanyHeadCount\":12,\"Industry\":\"Software\",\"Source\":\"Friend\",\"SessionCount\":0,\"AvgSesionDuration\":0},{\"Email\":\"tejal.bizlem@gmail.com\",\"FirstName\":\"tejal\",\"LastName\":\"jabade\",\"PhoneNumber\":\"+971 (4) 3914900\",\"Country\":\"UAE \",\"CompanyName\":\"3i infotech\",\"CompanyHeadCount\":12,\"Industry\":\"Software\",\"Source\":\"Friend\",\"SessionCount\":0,\"AvgSesionDuration\":0},{\"Email\":\"mohitraj.ranu@gmail.com\",\"FirstName\":\"mohit\",\"LastName\":\"mohit\",\"PhoneNumber\":\"+971 (4) 3914901\",\"Country\":\"UAE \",\"CompanyName\":\"3i infotech\",\"CompanyHeadCount\":12,\"Industry\":\"Software\",\"Source\":\"Friend\",\"SessionCount\":0,\"AvgSesionDuration\":0},{\"Email\":\"vivek@bizlem.io\",\"FirstName\":\"vivek\",\"LastName\":\"vivek\",\"PhoneNumber\":\"+971 (4) 3914902\",\"Country\":\"UAE \",\"CompanyName\":\"3i infotech\",\"CompanyHeadCount\":12,\"Industry\":\"Software\",\"Source\":\"Friend\",\"SessionCount\":0,\"AvgSesionDuration\":0}]}";
		JSONObject jsa=new JSONObject(jss);
//		callPostAPIJSON(apiurl,jsa);
		String urlcall="https://bluealgo.com:8088/ParseEmailId/ReadExcelServ";
		String hj78="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";
		JSONObject  js=new JSONObject();
		js.put("data", hj78);
		new CallPostService().ReadExcel(urlcall, js);
	}

	public static int callPostAPIJSON(String urlstr, JSONObject Obj,SlingHttpServletResponse res) throws IOException {

		
		
		StringBuffer response =null;
		int responseCode = 0;
		PrintWriter out=res.getWriter();
		try {

			HttpURLConnection con = null;
			String response_code = null;
			

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
		response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
		response.append(inputLine);
		}
		in.close();
		out.println("response api = "+response.toString());
		System.out.println("resp = "+response.toString());
		
		
		}
		catch (Exception e) {
			e.printStackTrace();
	//e.getMessage();
		}
		return responseCode;

		}
	public static String callPostAPIJSON(String urlstr, JSONObject Obj) throws IOException {

		StringBuffer response =null;
		int responseCode = 0;
	PrintWriter out=null;
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
			

		URL url = new URL(urlstr);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
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
		System.out.println("resp = "+responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
		response.append(inputLine);
		}
		in.close();
		//out.println("response api = "+response.toString());
		System.out.println("resp = "+response.toString());
		
		
		}
		catch (Exception e) {
			e.printStackTrace();
	//e.getMessage();
		}
		return response.toString();

		}
	public static String ReadExcel(String urlstr, JSONObject Obj) throws IOException {

		StringBuffer response =null;
		int responseCode = 0;
	PrintWriter out=null;
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
			

		URL url = new URL(urlstr);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
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
		System.out.println("resp = "+responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		response = new StringBuffer();
		int BUFFER_SIZE=1024;
		char[] buffer = new char[BUFFER_SIZE]; // or some other size, 
		int charsRead = 0;
		System.out.println(new String(buffer));
		try {
		while ( (charsRead  = in.read(buffer, 0, BUFFER_SIZE)) != -1) {
			System.out.println(new String(buffer));
			response.append(buffer, 0, charsRead);
		}
		}catch (IOException e) {
			System.out.println("in exc io ="+e);
			// TODO: handle exception
		}
		in.close();
		//out.println("response api = "+response.toString());
		System.out.println("resp = "+response.toString());
		
		
		}
		catch (Exception e) {
			e.printStackTrace();
	//e.getMessage();
		}
		return response.toString();

		}
}