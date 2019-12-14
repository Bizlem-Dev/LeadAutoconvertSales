//package services;
//
//import static com.mongodb.client.model.Filters.and;
//import static com.mongodb.client.model.Filters.eq;
//
//import java.io.BufferedReader;
//import java.io.BufferedWriter;
//import java.io.DataOutputStream;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.OutputStreamWriter;
//import java.io.PrintWriter;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.security.cert.X509Certificate;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Properties;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//import javax.mail.BodyPart;
//import javax.mail.Flags;
//import javax.mail.Folder;
//import javax.mail.Message;
//import javax.mail.MessagingException;
//import javax.mail.Session;
//import javax.mail.Store;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeMessage;
//import javax.mail.internet.MimeMultipart;
//import javax.mail.search.AndTerm;
//import javax.mail.search.FlagTerm;
//import javax.mail.search.FromTerm;
//import javax.mail.search.SearchTerm;
//import javax.net.ssl.HostnameVerifier;
//import javax.net.ssl.HttpsURLConnection;
//import javax.net.ssl.SSLContext;
//import javax.net.ssl.SSLSession;
//import javax.net.ssl.TrustManager;
//import javax.net.ssl.X509TrustManager;
//import javax.servlet.http.HttpServletResponse;
//
//import org.apache.log4j.Logger;
//import org.apache.sling.api.SlingHttpServletResponse;
//import org.apache.sling.commons.json.JSONArray;
//import org.apache.sling.commons.json.JSONObject;
//import org.bson.Document;
//import org.bson.conversions.Bson;
//
//import org.jsoup.Jsoup;
//
//import com.mongodb.BasicDBObject;
//import com.mongodb.MongoClient;
//import com.mongodb.client.MongoCollection;
//import com.mongodb.client.MongoDatabase;
//import com.mongodb.client.model.UpdateOptions;
//
//import salesconverter.mongo.SaveFunnelDetails;
//
//
//
//
//
//public class ReadBounceEmail {
////	final static Logger logger = Logger.getLogger(ReadBounceEmail.class);
//
//	private Folder folderInbox = null;
//	private  Store store = null;
//
//	public void searchBouncedEmail(String host, String port, String userName,String password,String sender,SlingHttpServletResponse response) {
//		PrintWriter out=null;
//		try {
//			 out=response.getWriter();
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//
//		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
//			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
//				return null;
//			}
//
//			public void checkClientTrusted(X509Certificate[] certs, String authType) {
//			}
//
//			public void checkServerTrusted(X509Certificate[] certs, String authType) {
//			}
//
//		} };
//
//		try {
//			SSLContext sc = SSLContext.getInstance("SSL");
//			sc.init(null, trustAllCerts, new java.security.SecureRandom());
//			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
//
//		} catch (Exception e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//
//		// Create all-trusting host name verifier
//		HostnameVerifier allHostsValid = new HostnameVerifier() {
//			public boolean verify(String hostname, SSLSession session) {
//				return true;
//			}
//		};
//		// Install the all-trusting host verifier
//		HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
//		
//		Properties properties = new Properties();
//		JSONArray bouncedemail=new JSONArray();
//		properties.setProperty("mail.imap.ssl.enable", "true");
//		properties.put("mail.imap.ssl.trust", host);
//		properties.put("mail.imap.host", host);
//		properties.put("mail.imap.port", port);
//		properties.put("mail.imap.auth.plain.disable", true);
//		properties.setProperty("mail.imap.socketFactory.class","javax.net.ssl.SSLSocketFactory");
//		properties.setProperty("mail.imap.socketFactory.fallback", "false");
//		properties.setProperty("mail.imap.socketFactory.port",String.valueOf(port));
////		properties.setProperty("mail.debug", "true");
//		properties.put("mail.smtp.starttls.enable", "true");
//    
//		
//		Session session = Session.getDefaultInstance(properties);
//		JSONObject emailjsmail=new JSONObject();
//		 HashMap<String, String> emailmap =null;
//out.println("start");
//		try {
//			Date now = new Date();
//			SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMYYYY");
//			String time = dateFormat.format(now);
//			// Get system properties
//		     
//			store = session.getStore("imap");
//			store.connect(userName, password);
//
//			folderInbox = store.getFolder("Delivery Failed");
//			folderInbox.open(Folder.READ_WRITE);
//			Flags seen = new Flags(Flags.Flag.SEEN);
//			FlagTerm unseenFlagTerm = new FlagTerm(seen, true);
//			SearchTerm sender1 = new FromTerm(new InternetAddress(sender));// postmaster@moia.gov.bh   mailer-daemon@googlemail.com
//			SearchTerm searchTerm = new AndTerm(unseenFlagTerm,sender1);
//			Message[] foundMessages =folderInbox.getMessages();// folderInbox.search(searchTerm);
//			out.println("Message length found is "+foundMessages.length);
//			out.println("Message length found is "+foundMessages.length);
//			for (int i = 0; i < foundMessages.length; i++) {
//				try {
//				 MimeMessage cmsg =null;
//				try {
//				emailmap= new HashMap<String, String>();
//				Message message = foundMessages[i];
//				   MimeMessage msg = (MimeMessage)message;
//				    cmsg = new MimeMessage(msg);
////				System.out.println("getEmailContent ; "+getEmailContent(message));
//			
//				String subject =message.getSubject();
//				  if (subject != null&&subject.equals("Delivery Status Notification (Failure)")) {
//						System.out.println(i+"The content returned in the body is -================================ ==========");
//				//  System.out.println("Subject: " + subject);
//				
//				
//				   processMimeMessage(cmsg);
//				if (message.isMimeType("multipart/*")) {
//					MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
//				String	result = getTextFromMimeMultipart(mimeMultipart);
////				System.out.println("result 155 = ======="+result);
//				String  newresult =Jsoup.parse(result).text();// result + "\n" + 
//				out.println("newresult 155 = =========+++++++++++++++++"+newresult);
//				
//				Matcher m = Pattern.compile("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+").matcher(newresult);
//				String email=null;
//				while (m.find()) {
//					
//				 email=m.group();
//			
//				}
//				
//			
//				
//				if(email.endsWith(".") ){
//					email=email.substring(0,email.lastIndexOf("."));
//					//System.out.println("email1 = "+email);
//					}else {
//						//System.out.println("ddd = "+email);	
//					}
//				emailmap.put("Email", email);
//			//	emailmap.put("Subject", subject);
//				emailmap.put("Status", "Message not delivered");
//				out.println("emailmap="+emailmap);
//				bouncedemail.put(emailmap);
//			//	System.out.println("newresult 155 = =========+++++++++++++++++  end");
//				}else {
//				
//				}
////				System.out.println("The description message "+message);
//				//System.out.println("The content returned in the body is "+message.getContent());
//				
//			//	System.out.println("The description provided is "+message.getDescription());
//				
//				// store attachment file name, separated by comma
//				String attachFiles = "";
//				String sentDate = message.getSentDate().toString();
//			//	System.out.println("The description returned is"+sentDate);
//			
//				
//		
//				
//			}
//				}catch (Exception e) {
//					out.println("The exc returned is"+e);
//					processMimeMessage(cmsg);
//				}
//				}catch (Exception e) {
//					// TODO: handle exception
//					out.println("The exc returned is"+e);
//				}
//			}
//			emailjsmail.put("xlsjson", bouncedemail);
//			out.println("emailjsmail= "+emailjsmail);
//			out.println("emailjsmail="+emailjsmail);
//			SaveFunnelDetails.updateBouncedEmail(bouncedemail, response);
//			
//			
//			out.println("emailjsmail= inserted");
//		} catch (Exception messEx) {
//			//ex.printStackTrace();
//		out.println("messEx="+messEx);
//			 if (messEx.getMessage() != null && messEx.getMessage().toLowerCase().
//				        contains("unable to load " +"bodystructure"))
//				        {
////				            creating local copy of given MimeMessage
////				            MimeMessage msgDownloaded = new MimeMessage((MimeMessage) messEx);
////				            calling same method with local copy of given MimeMessage
////				            processMimeMessage(msgDownloaded);
//				        }
//				        else
//				        {
//				         //   throw messEx;
//				        }
//
//		}
//		finally{
//			try {
//				folderInbox.close(false);
//
//				store.close();
//				folderInbox = null;
//				store = null;
//			} catch (Exception e) {
//				folderInbox = null;
//				store = null;
//				e.printStackTrace();
//			}
//
//		}
//	}
//	public void processMimeMessage(MimeMessage msg) throws MessagingException
//	{
//	try {
//	//some processing
//	}
//	catch (Exception messEx)
//	{
//	//making sure that it's a BODYSTRUCTURE error
//	if (messEx.getMessage() != null && messEx.getMessage().toLowerCase().
//	contains("unable to load " +"bodystructure"))
//	{
//	//creating local copy of given MimeMessage
//	MimeMessage msgDownloaded = new MimeMessage((MimeMessage) msg);
//	//calling same method with local copy of given MimeMessage
//	processMimeMessage(msgDownloaded);
//	System.out.println("msgDownloaded"+msgDownloaded);
//	}
//	else
//	{
//	throw messEx;
//	}
//	}
//	}
//	 private static String getTextFromMimeMultipart(MimeMultipart mimeMultipart) throws Exception {
//			String result = "";
//			int partCount = mimeMultipart.getCount();
//			System.out.println("MultipartCount returned is "+partCount);
//			for (int i = 0; i < partCount; i++) {
//				BodyPart bodyPart = mimeMultipart.getBodyPart(i);
//				/*if (bodyPart.isMimeType("text/plain")) {
//					result = result + "\n" + bodyPart.getContent();
//					break; // without break same text appears twice in my tests
//				} else*/ 
//				if (bodyPart.isMimeType("text/html")) {
//					String html = (String) bodyPart.getContent();
//					//System.out.println("getTextFromMimeMultipart()  "+html);
//					// result = result + "\n" + org.jsoup.Jsoup.parse(html).text();
//					result = html;
//					break;
//				}else if (bodyPart.getContent() instanceof MimeMultipart) {
//					result = result + getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent());
//					//System.out.println("getTextFromMimeMultipart()  "+result);
//				}
//			}
//			return result;
//		}
//	
//	 public static void main(String[] args){
//		 
//			String host = "imap.gmail.com";
//			String port = "993";
//			String userName = "sales@doctiger.com";
//			String password = "doctiger123";
//			String sender="mailer-daemon@googlemail.com";
//			
//		//	new ReadBounceEmail().searchBouncedEmailLocal(host, port, userName, password,sender);
//		
//	 }
//	 
////		public void searchBouncedEmailLocal(String host, String port, String userName,String password,String sender) {
////			
////
////			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
////				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
////					return null;
////				}
////
////				public void checkClientTrusted(X509Certificate[] certs, String authType) {
////				}
////
////				public void checkServerTrusted(X509Certificate[] certs, String authType) {
////				}
////
////			} };
////
////			try {
////				SSLContext sc = SSLContext.getInstance("SSL");
////				sc.init(null, trustAllCerts, new java.security.SecureRandom());
////				HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
////
////			} catch (Exception e1) {
////				// TODO Auto-generated catch block
////				e1.printStackTrace();
////			}
////
////			// Create all-trusting host name verifier
////			HostnameVerifier allHostsValid = new HostnameVerifier() {
////				public boolean verify(String hostname, SSLSession session) {
////					return true;
////				}
////			};
////			// Install the all-trusting host verifier
////			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
////			
////			Properties properties = new Properties();
////			JSONArray bouncedemail=new JSONArray();
////			properties.setProperty("mail.imap.ssl.enable", "true");
////			properties.put("mail.imap.ssl.trust", host);
////			properties.put("mail.imap.host", host);
////			properties.put("mail.imap.port", port);
////			properties.put("mail.imap.auth.plain.disable", true);
////			properties.setProperty("mail.imap.socketFactory.class","javax.net.ssl.SSLSocketFactory");
////			properties.setProperty("mail.imap.socketFactory.fallback", "false");
////			properties.setProperty("mail.imap.socketFactory.port",String.valueOf(port));
//////			properties.setProperty("mail.debug", "true");
////			properties.put("mail.smtp.starttls.enable", "true");
////	    
////			
////			Session session = Session.getDefaultInstance(properties);
////			JSONObject emailjsmail=new JSONObject();
////			 HashMap<String, String> emailmap =null;
////	System.out.println("start");
////			try {
////				Date now = new Date();
////				SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMYYYY");
////				String time = dateFormat.format(now);
////				// Get system properties
////			     
////				store = session.getStore("imap");
////				store.connect(userName, password);
////
////				folderInbox = store.getFolder("Delivery Failed");
////				folderInbox.open(Folder.READ_WRITE);
////				Flags seen = new Flags(Flags.Flag.SEEN);
////				FlagTerm unseenFlagTerm = new FlagTerm(seen, true);
////				SearchTerm sender1 = new FromTerm(new InternetAddress(sender));// postmaster@moia.gov.bh   mailer-daemon@googlemail.com
////				SearchTerm searchTerm = new AndTerm(unseenFlagTerm,sender1);
////				Message[] foundMessages =folderInbox.getMessages();// folderInbox.search(searchTerm);
////				logger.info("Message length found is "+foundMessages.length);
////				System.out.println("Message length found is "+foundMessages.length);
////				for (int i = 0; i < 100; i++) {
////					try {
////					 MimeMessage cmsg =null;
////					try {
////					emailmap= new HashMap<String, String>();
////					Message message = foundMessages[i];
////					   MimeMessage msg = (MimeMessage)message;
////					    cmsg = new MimeMessage(msg);
//////					System.out.println("getEmailContent ; "+getEmailContent(message));
////				
////					String subject =message.getSubject();
////					  if (subject != null&&subject.equals("Delivery Status Notification (Failure)")) {
////							System.out.println(i+"The content returned in the body is -================================ ==========");
////					//  System.out.println("Subject: " + subject);
////					
////					
////					   processMimeMessage(cmsg);
////					if (message.isMimeType("multipart/*")) {
////						MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
////					String	result = getTextFromMimeMultipart(mimeMultipart);
//////					System.out.println("result 155 = ======="+result);
////					String  newresult =Jsoup.parse(result).text();// result + "\n" + 
////					logger.info("newresult 155 = =========+++++++++++++++++"+newresult);
////					
////					Matcher m = Pattern.compile("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+").matcher(newresult);
////					String email=null;
////					while (m.find()) {
////						
////					 email=m.group();
////				
////					}
////					
////				
////					
////					if(email.endsWith(".") ){
////						email=email.substring(0,email.lastIndexOf("."));
////						//System.out.println("email1 = "+email);
////						}else {
////							//System.out.println("ddd = "+email);	
////						}
////					emailmap.put("Email", email);
////				//	emailmap.put("Subject", subject);
////					emailmap.put("Status", "Message not delivered");
////					logger.info("emailmap="+emailmap);
////					bouncedemail.put(emailmap);
////					System.out.println("newresult 155 = =========+++++++++++++++++  end");
////					}else {
////					
////					}
//////					System.out.println("The description message "+message);
////					//System.out.println("The content returned in the body is "+message.getContent());
////					
////				//	System.out.println("The description provided is "+message.getDescription());
////					
////					// store attachment file name, separated by comma
////					String attachFiles = "";
////					String sentDate = message.getSentDate().toString();
////				//	System.out.println("The description returned is"+sentDate);
////				
////					
////			
////					
////				}
////					}catch (Exception e) {
////						System.out.println("The exc returned is"+e);
////						processMimeMessage(cmsg);
////					}
////					}catch (Exception e) {
////						// TODO: handle exception
////						System.out.println("The exc returned is"+e);
////					}
////				}
////				emailjsmail.put("xlsjson", bouncedemail);
////				System.out.println("emailjsmail= "+emailjsmail);
////				logger.info("emailjsmail="+emailjsmail);
//////				updateBouncedEmail(bouncedemail);
////				
////				System.out.println("emailjsmail= inserted");
////			} catch (Exception messEx) {
////				//ex.printStackTrace();
////				System.out.println("messEx="+messEx);
////				 if (messEx.getMessage() != null && messEx.getMessage().toLowerCase().
////					        contains("unable to load " +"bodystructure"))
////					        {
//////					            creating local copy of given MimeMessage
//////					            MimeMessage msgDownloaded = new MimeMessage((MimeMessage) messEx);
//////					            calling same method with local copy of given MimeMessage
//////					            processMimeMessage(msgDownloaded);
////					        }
////					        else
////					        {
////					         //   throw messEx;
////					        }
////
////			}
////			finally{
////				try {
////					folderInbox.close(false);
////
////					store.close();
////					folderInbox = null;
////					store = null;
////				} catch (Exception e) {
////					folderInbox = null;
////					store = null;
////					e.printStackTrace();
////				}
////
////			}
////		}
//}
