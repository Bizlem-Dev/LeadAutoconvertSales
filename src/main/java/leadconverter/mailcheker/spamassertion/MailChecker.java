package leadconverter.mailcheker.spamassertion;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ResourceBundle;

import leadconverter.doctiger.LogByFileWriter;

import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;

public class MailChecker {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		  String email_detail1=sendGet("tejal.jabade10@gmail.com");
		  System.out.println(email_detail1);
//        String email_details=emailValidation("tejal.jabade10@gmail.com");//emailValidation("tejal.jabade10@gmail.com".replace(" ", ""));//
       // System.out.println(email_details);
        //Exist : akhileshyadav0308@gmail.com
        //Does not Exist : akhileshyadav0308asd@gmail.com
		//emailValidationByApiLayer("akhileshyadav0308@gmail.com");
	}
	public static String emailValidationByApiLayer(String userid) {
		String email_status ="NA";
		String apilayer_email_checker_api = ResourceBundle.getBundle("config").getString("apilayer_email_checker_api") + userid+"&smtp=1&format=0";
		try {
			URL url = new URL(apilayer_email_checker_api);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.connect();
			InputStream in = conn.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String email_checker_response = reader.readLine();
			JSONObject email_checker_response_json=new JSONObject(email_checker_response);
			System.out.println(email_checker_response_json);
            //JSONObject obj = new JSONObject(text);
			//expireFlag = obj.getInt("expireFlag");
			//System.out.println(Integer.toString(expireFlag));
			conn.disconnect();
		} catch (Exception ex) {
			System.out.println("Error : "+ex);
		}
		return email_status;
	}
	
	public static String emailValidation(String userid) {
		//LogByFileWriter.logger_info("MailChecker : emailValidation() " + userid);
		String email_status ="NA";
		String email_details =null;
		URL url = null;
		HttpURLConnection conn = null;
		BufferedReader reader = null;
		String email_checker_api=null;
		InputStream in = null;
		try {
			email_checker_api = ResourceBundle.getBundle("config").getString("email_checker_api") + userid;
		     url = new URL(email_checker_api);
			//System.out.println("Step 1");
			conn = (HttpURLConnection) url.openConnection();
			//System.out.println("Step 2");
			conn.setRequestMethod("GET");
			conn.connect();
			//System.out.println("Step 3");
		    in = conn.getInputStream();
			
			reader = new BufferedReader(new InputStreamReader(in));
			System.out.println("Step  reader.readLine() "+ reader.readLine());
			email_details = reader.readLine();
			//System.out.println("Step 5");
			/*
			System.out.println(text);
            if(text.contains("Does not Exist")){
            	email_status="Invalid";
            	System.out.println("Invalid");
			}else{
				email_status="Valid";
				System.out.println("Valid");
			}
			*/
			//JSONObject obj = new JSONObject(text);
			//System.out.println("obj : "+obj);
			//expireFlag = obj.getInt("expireFlag");
			//System.out.println(Integer.toString(expireFlag));
			
		} catch (Exception ex) {
			System.out.println("Error : "+ex);
		}
		finally {
		
			conn.disconnect();
			
			try {
				if(null !=in) {
					
				in.close();
				in=null;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if(null !=reader) {
					reader.close();
					reader=null;
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			conn =null;
		}
		return email_details;
	}

	public static String sendGet(String userid) {
		StringBuilder response = null;
		URL url = null;
		String email_status ="NA";
		HttpURLConnection con = null;
		try {
/*{  "catch_all": "null",   "did_you_mean": "",   "disposable": false,   "domain": "gmail.com",   "email": "tejal.jabade10@gmail.com",   "format_valid": true,   "free": false,   "mx_found": "alt2.gmail-smtp-in.l.google.com.",   "role": false,   "score": 0.96,   "smtp_check": true,   "user": "mohit.raj"}   */
		int responseCode = 0;
		String email_checker_api=null;
		
		email_checker_api = ResourceBundle.getBundle("config").getString("email_checker_api") + userid;
	
		url = new URL(email_checker_api);
		con = (HttpURLConnection) url.openConnection();
		con.setConnectTimeout(10 * 1000);
		con.setRequestMethod("GET");
		con.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
		con.addRequestProperty("User-Agent", "Mozilla");
		con.addRequestProperty("Referer", "google.com");

//			System.out.println("Request URL ... " + url);
		con.setDoOutput(true);

		responseCode = con.getResponseCode();

		// System.out.println("Response Code : " + responseCode);
		boolean redirect = false;

		// normally, 3xx is redirect
		if (responseCode != HttpURLConnection.HTTP_OK) {
		if (responseCode == 308 ||
		responseCode == HttpURLConnection.HTTP_MOVED_TEMP
		|| responseCode == HttpURLConnection.HTTP_MOVED_PERM
		|| responseCode == HttpURLConnection.HTTP_SEE_OTHER){
		redirect = true;
		}}
//		System.out.println("Response "+responseCode +" redirct " + redirect);

		if (redirect) {

		// get redirect url from "location" header field
		String newUrl = con.getHeaderField("Location");

//		System.out.println("Location "+newUrl+" for url "+url);
		// get the cookie if need, for login
		String cookies = con.getHeaderField("Set-Cookie");
//		System.out.println("Location "+newUrl+" and cookie "+cookies+" for url "+url);
		// open the new connnection again
		con = (HttpURLConnection) new URL(newUrl).openConnection();
		con.setInstanceFollowRedirects(true); //you still need to handle redirect manully.
		HttpURLConnection.setFollowRedirects(true);
		con.setRequestProperty("Cookie", cookies);
		con.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
		con.addRequestProperty("User-Agent", "Mozilla");
		con.addRequestProperty("Referer", "google.com");

//			System.out.println("Redirect to URL : " + newUrl);

		}
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		response = new StringBuilder();

		while ((inputLine = in.readLine()) != null) {
		response.append(inputLine);
		}
		in.close();
		in = null;
		} catch (Exception e) {
			JSONObject excjson=new JSONObject();
			try {
				excjson.put("Email_status", "NA");
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
System.out.println("e = "+e);
		return excjson.toString();
		}
		finally{
		try {
		con.disconnect();
		con = null;
		} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		con = null;
		}
		}
		System.out.println("response.toString()= "+response.toString());
		try {
			JSONObject respjs=new JSONObject(response.toString());
			if(respjs.has("smtp_check")) {
			email_status=respjs.getString("smtp_check");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			System.out.println("exc= "+e);
		}
		
		return response.toString();

		}
}
