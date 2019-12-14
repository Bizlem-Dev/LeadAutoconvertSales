package Dashboard;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.DistinctIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;


import salesconverter.mongo.FunnelDetailsMongoDAO;

public class mongodbdata {

//	private static MongoClient mongoClient;

	static String message;

	public static void main(String[] args) {

		// Funnellist("viki_gmail.com");

	}

	public static JSONObject Funnellist(String username, SlingHttpServletResponse resp) throws IOException {
		JSONObject mainjson = null;
		PrintWriter out = resp.getWriter();

		JSONArray dataarray = new JSONArray();
		JSONObject dataobj = new JSONObject();
		try {
//String mainjshardcod = "{\"standout\":[{\"C3\":{\"upgrade\":1000,\"session time\":3000,\"traffic\":300},\"C1\":{\"upgrade\":300,\"session time\":1000,\"traffic\":200},\"C2\":{\"upgrade\":500,\"session time\":2000,\"traffic\":400}}],\"upgradationRate\":{\"data\":[{\"rate\":\"50%\",\"subFunnel\":\"SubFunnel 1\",\"tableData\":[{\"rate\":\"2%\",\"source\":\"facebook\"},{\"rate\":\"4%\",\"source\":\"twitter\"},{\"rate\":\"2%\",\"source\":\"insta\"}]},{\"rate\":\"50%\",\"subFunnel\":\"SubFunnel 2\",\"tableData\":[{\"rate\":\"2%\",\"source\":\"facebook\"},{\"rate\":\"4%\",\"source\":\"twitter\"},{\"rate\":\"2%\",\"source\":\"insta\"}]}],\"subFunnel\":[\"SubFunnel 1\",\"SubFunnel 2\"]},\"funnelLeadCategory\":[{\"week1\":[{\"warm\":200,\"inform\":300,\"explore\":1000,\"entice\":500,\"connect\":100}],\"week2\":[{\"warm\":400,\"inform\":500,\"explore\":2000,\"entice\":1000,\"connect\":100}],\"week3\":[{\"warm\":300,\"inform\":1000,\"explore\":3000,\"entice\":1500,\"connect\":200}],\"week4\":[{\"warm\":500,\"inform\":1000,\"explore\":4000,\"entice\":2000,\"connect\":500}],\"week5\":[{\"warm\":10,\"inform\":30,\"explore\":100,\"entice\":50,\"connect\":10}]}],\"missedOppurtinities\":{\"Others\":15,\"Price\":10,\"Quality\":20,\"Competitiors\":40,\"Specs\":30},\"active user\":{\"chartData\":[{\"dec-18\":{\"funnel end\":900,\"unsubscribe\":300,\"headroom\":400,\"active user\":300,\"spam\":1500},\"jan-18\":{\"funnel end\":600,\"unsubscribe\":300,\"headroom\":600,\"active user\":400,\"spam\":2000},\"nov-18\":{\"funnel end\":800,\"unsubscribe\":200,\"headroom\":300,\"active user\":200,\"spam\":1000},\"feb-18\":{\"funnel end\":1500,\"unsubscribe\":2000,\"headroom\":50,\"active user\":100,\"spam\":20}}],\"tableData\":{\"funnel end\":600,\"unsubscribe\":400,\"headroom\":1200,\"active user\":100,\"spam\":500}},\"outcome\":[{\"Parameter\":\"Reveneue\",\"Actual\":85,\"planned\":92},{\"Parameter\":\"Conversation\",\"Actual\":82,\"planned\":95},{\"Parameter\":\"Free trial\",\"Actual\":75,\"planned\":80}]}";
			String mainjshardcod = "{\"missedOppurtinities\":{\"Others\":15,\"Price\":10,\"Quality\":20,\"Competitiors\":40,\"Specs\":30},\"outcome\":[{\"Parameter\":\"Reveneue\",\"Actual\":85,\"planned\":92},{\"Parameter\":\"Conversation\",\"Actual\":82,\"planned\":95},{\"Parameter\":\"Free trial\",\"Actual\":75,\"planned\":80}]}";
			JSONArray funnellist = new JSONArray();
			funnellist.put("fun2611");
			funnellist.put("NewTestNov26");
			funnellist.put("fun28");

			funnellist = FunnelDetailsMongoDAO.getFunnelList(username);
//					new JSONArray(sendGet(
//					"http://development.bizlem.io:8082/portal/servlet/service/createCampaign.getFunnel?userName="
//							+ username));

			// out.println("funnel list : "+funnellist);
			for (int i = 0; i < funnellist.length(); i++) {
				if (funnellist.get(i) != null && funnellist.get(i) != "" && !funnellist.get(i).equals("null")
						&& !funnellist.get(i).equals("undefined")) {
					mainjson = new JSONObject(mainjshardcod);

//				out.println(" funnellist.get(i) : "+ funnellist.get(i));
					mainjson.put("funnelName", funnellist.get(i));
					CampaignFunnelGetNoOFLeads(mainjson, username, funnellist.getString(i));

					upcomingCampaigndetails(mainjson, username, funnellist.getString(i));
					weekfunnellead(mainjson, username, funnellist.getString(i));
					geolocation(mainjson, username, funnellist.getString(i));
					activeusers(mainjson, username, funnellist.getString(i));
					standout(mainjson, username, funnellist.getString(i));
					if (!mainjson.has("hotLeads")) {
						mainjson.put("hotLeads", 0);

					}
					if (!mainjson.has("convRate")) {
						mainjson.put("convRate", 0);

					}
					if (!mainjson.has("convLeads")) {
						mainjson.put("convLeads", 0);

					}
					if (!mainjson.has("campaignFunnel")) {

						JSONObject funncatjson = new JSONObject();
						funncatjson.put("warm", 0);
						funncatjson.put("inform", 0);
						funncatjson.put("explore", 0);
						funncatjson.put("entice", 0);
						funncatjson.put("connect", 0);
						JSONArray campaignfunnel = new JSONArray();

						campaignfunnel.put(funncatjson);
						mainjson.put("campaignFunnel", campaignfunnel);

					}
					// convLeads
//				convLeads
					// mainjson.put("convLeads", "35");
					dataarray.put(mainjson);
//				out.println("mainjson : "+mainjson);
				}
			}
			dataobj.put("data", dataarray);
			dataobj.put("funnelList", funnellist);
//			out.println(dataobj);
//			System.out.println("mainjson : " + mainjson);
// }

			// }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Exception ex in Funnel_list :: " + e.getMessage());
		}

		return dataobj;
	}

	public static JSONObject weekfunnellead(JSONObject mainjson, String CreatedBy, String funnel) throws JSONException {

		MongoClient mongoClient = null;
		MongoDatabase database = null;
		MongoCollection<Document> collection = null;
		MongoCursor<Document> funnelDetailsCursor = null;
		JSONArray funnelLeadCategoryarr = new JSONArray();

		Document campaign_list_doc = null;

		Map<String, Integer> week1data = new HashMap<String, Integer>();

		try {
			mongoClient = new mongodbdata().getConnection();
			database = mongoClient.getDatabase("salesautoconvert");
			collection = database.getCollection("FirstCategoryMails");
			JSONObject funnelListJson = null;
			String[] catedashboard = { "explore", "warm", "inform", "entice", "connect" };

			String[] dbcate = { "Explore", "Warm", "Inform", "Entice", "Connect" };
			String[] weeklist = { "week1", "week2", "week3", "week4", "week5" };
			Bson filter = null;

			int rawleads = 0;
			String campaignName = null;
			String campcreateddate = null;
			String mailtempname = null;

			Date set_date = null;
			DateFormat df = new SimpleDateFormat("dd-MM-yy");
			// Date dateobj = new Date();
			String Current_Date = null;

			JSONObject allweek = new JSONObject();

			JSONArray week1arr = new JSONArray();
			JSONArray week2arr = new JSONArray();
			JSONArray week3arr = new JSONArray();
			JSONArray week4arr = new JSONArray();
			JSONArray week5arr = new JSONArray();

			JSONObject week1json = new JSONObject();
			JSONObject week2json = new JSONObject();
			JSONObject week3json = new JSONObject();
			JSONObject week4json = new JSONObject();
			JSONObject week5json = new JSONObject();

			JSONObject catgjson = null;
			//
			JSONObject allcatgjson = new JSONObject();

			for (int j = 0; j < dbcate.length; j++) {
				filter = and(eq("funnelName", funnel), eq("CreatedBy", CreatedBy), eq("Category", dbcate[j]));
				funnelDetailsCursor = collection.find(filter).iterator();
				if (funnelDetailsCursor.hasNext()) {
					while (funnelDetailsCursor.hasNext()) {
						try {
							int nolead = 0;
							catgjson = new JSONObject();
							int entnolead = 0;
							funnelListJson = new JSONObject();
							campaign_list_doc = funnelDetailsCursor.next();
							JSONObject docobj = new JSONObject(campaign_list_doc.toJson());

							if (docobj.getString("Category").equals("Explore")) {
								if (docobj.getJSONArray("Contacts").length() == 0) {
									nolead = docobj.getJSONArray("SentExploreContacts").length();
								} else {
									nolead = docobj.getJSONArray("Contacts").length();
								}

								if (docobj.has("expleadweek")) {
									catgjson.put(docobj.getString("expleadweek"), nolead);
									allcatgjson.put("Explore", catgjson);
								} else {

									catgjson.put("week1", nolead);
									allcatgjson.put("Explore", catgjson);
								}

							} else {
								// leadcount
								for (int i = 0; i < weeklist.length; i++) {
									String wk = "leadcount" + weeklist[i];

									if (docobj.has(wk)) {

										entnolead = docobj.getJSONArray(wk).length();
										catgjson.put(weeklist[i], entnolead);
										allcatgjson.put(dbcate[j], catgjson);

									} else {

										catgjson.put(weeklist[i], entnolead);
										allcatgjson.put(dbcate[j], catgjson);

									}

								}

							}

						} catch (Exception e) {
							// TODO: handle exception
						}
					}
				}
			}

			JSONObject weekjson = new JSONObject();
			for (int k = 0; k < allcatgjson.length(); k++) {
				for (int l = 0; l < dbcate.length; l++) {
					if (allcatgjson.has(dbcate[l])) {

						JSONObject newjs = allcatgjson.getJSONObject(dbcate[l]);

						if (newjs.has("week1")) {
							JSONArray newjsar = new JSONArray();
							week1json.put(catedashboard[l], newjs.getInt("week1"));
							newjsar.put(week1json);
							weekjson.put("week1", newjsar);
						}
						if (newjs.has("week2")) {
							JSONArray newjsar = new JSONArray();
							week2json.put(catedashboard[l], newjs.getInt("week2"));
							newjsar.put(week2json);
							weekjson.put("week2", newjsar);
						}
						if (newjs.has("week3")) {
							JSONArray newjsar = new JSONArray();
							week3json.put(catedashboard[l], newjs.getInt("week3"));
							newjsar.put(week3json);
							weekjson.put("week3", newjsar);
						}
						if (newjs.has("week4")) {
							JSONArray newjsar = new JSONArray();
							week4json.put(catedashboard[l], newjs.getInt("week4"));
							newjsar.put(week4json);
							weekjson.put("week4", newjsar);
						}

						if (newjs.has("week5")) {
							JSONArray newjsar = new JSONArray();
							week5json.put(catedashboard[l], newjs.getInt("week5"));
							newjsar.put(week5json);
							weekjson.put("week5", newjsar);
						}
					}
				}

			}
			funnelLeadCategoryarr.put(weekjson);

			mainjson.put("funnelLeadCategory", funnelLeadCategoryarr);

		} catch (Exception e) {
			// TODO: handle exception
		}

		return mainjson;

	}

	public static JSONObject geolocation(JSONObject mainjson, String username, String funnelname) {
		MongoClient mongoClient = null;
		try {

			JSONArray geolocationarr = new JSONArray();
			ArrayList<String> contents = new ArrayList<String>();
			ArrayList<String> contentvalues = null;

			MongoDatabase db = null;
			MongoCollection<Document> collection = null;

			mongoClient = new mongodbdata().getConnection();
			db = mongoClient.getDatabase("salesautoconvert");

			MongoCursor<Document> cursorDoc = null;

			collection = db.getCollection("RuleEngineCalledForSubscriberData");

			contents.add("Country");
			contents.add("HotLeads");
			contents.add("Converted");
			geolocationarr.put(contents);
			Bson filter = and(eq("CreatedBy", username), eq("FunnelName", funnelname), eq("SubFunnelName", "Connect"));

			DistinctIterable<String> di = collection.distinct("location", filter, String.class);
			MongoCursor<String> cursor = di.iterator();
			JSONArray coutryarr = new JSONArray();

			String countryloc = null;
			while (cursor.hasNext()) {
				countryloc = cursor.next();
				coutryarr.put(countryloc);
			}
			cursor.close();
			String country = null;
			if (coutryarr.length() > 0) {
				try {
					for (int j = 0; j < coutryarr.length(); j++) {
						country = coutryarr.getString(j);
						Bson filterforcontry = and(eq("CreatedBy", username), eq("FunnelName", funnelname),
								eq("SubFunnelName", "Connect"), eq("location", country));
						cursorDoc = collection.find(filterforcontry).iterator();
						contentvalues = new ArrayList<String>();
						contentvalues.add(country);
						int leadcount = 0;
						if (cursorDoc.hasNext()) {
							while (cursorDoc.hasNext()) {
								leadcount++;
								contentvalues.add(Integer.toString(leadcount));
								contentvalues.add(Integer.toString(leadcount));
							}
							cursorDoc.close();
							cursorDoc = null;
						}
						geolocationarr.put(contentvalues);
					}
				} catch (Exception e) {

				}
			} else {
				contentvalues = new ArrayList<String>();
				contentvalues.add("India");
				contentvalues.add("0");
				contentvalues.add("0");
				geolocationarr.put(contentvalues);
			}
			mainjson.put("Geolocation", geolocationarr);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			if (null != mongoClient) {
				mongoClient.close();
				mongoClient = null;
			}

		}
		return mainjson;
	}

	public static JSONObject standout(JSONObject mainjson, String username, String funnelname) {
		MongoClient mongoClient = null;
		try {
			MongoCollection<Document> firstcollection = null;
			MongoCollection<Document> collection = null;
			mongoClient = new mongodbdata().getConnection();
			MongoDatabase db = null;
			db = mongoClient.getDatabase("salesautoconvert");
			firstcollection = db.getCollection("FirstCategoryMails");
			collection = db.getCollection("RuleEngineCalledForSubscriberData");

			MongoCursor<Document> monitordatahotleads;
			String hotleadobj;
			JSONObject newjs = null;
			JSONObject campjs = new JSONObject();
			;
			Bson filtercount2 = and(eq("Parentfunnel", funnelname), eq("CreatedBy", username));

			FindIterable<Document> filerdata = firstcollection.find(filtercount2);
			monitordatahotleads = filerdata.iterator();

			Document datsrc_doc2 = null;
			int hotleads = 0;
			JSONArray standout = new JSONArray();
			if (monitordatahotleads.hasNext()) {
				while (monitordatahotleads.hasNext()) {
					int upgrade = 0;
					int trafic = 0;
					int session = 0;
					try {
						datsrc_doc2 = monitordatahotleads.next();

						newjs = new JSONObject(datsrc_doc2.toJson());

						if (newjs.has("campaignName")) {
							JSONObject onecampjs = new JSONObject();

							if (newjs.has("MoveEmailList") && newjs.getJSONArray("MoveEmailList").length() > 0) {

								upgrade = newjs.getJSONArray("MoveEmailList").length();
							}
							if (newjs.has("Trafic")) {
								trafic = Integer.parseInt(newjs.getString("Trafic"));

							}
// get total session time
							session = GetTotalSessionTimePerCamp(newjs.getString("CreatedBy"),
									newjs.getString("Category"), newjs.getString("Parentfunnel"),
									newjs.getString("Campaign_id"), collection);
							onecampjs.put("traffic", trafic);
							onecampjs.put("upgrade", upgrade);
							onecampjs.put("session time", session);

							campjs.put(newjs.getString("campaignName"), onecampjs);
							standout.put(campjs);
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			monitordatahotleads.close();
			//
			if (standout.length() == 0) {
				JSONObject onecampjs = new JSONObject();
				onecampjs.put("traffic", 0);
				onecampjs.put("upgrade", 0);
				onecampjs.put("session time", 0);

				campjs.put("No Campaign Found", onecampjs);
				standout.put(campjs);
			}
			mainjson.put("standout", standout);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (null != mongoClient) {
				mongoClient.close();
				mongoClient = null;
			}

		}

		return mainjson;
	}

	public static JSONObject outcome(JSONObject mainjson, String username) {
		try {

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mainjson;
	}

	public static JSONObject activeusers(JSONObject mainjson, String username, String mainfunnel) {

		MongoDatabase db = null;
		MongoCollection<Document> collection = null;

		MongoClient mongoClient = null;
		mongoClient = new mongodbdata().getConnection();
		db = mongoClient.getDatabase("salesautoconvert");
		collection = db.getCollection("FirstCategoryMails");
		MongoCursor<Document> cursorDoc = null;
		MongoCursor<Document> monitordata = null;
		Document data = null;
		JSONObject actuseobj = new JSONObject();
		String[] monthArray = { "jan", "feb", "mar", "apr", "may", "jun", "jul", "aug", "sep", "oct", "nov", "dec" };
		try {
			int unsubscrb = 0;
			JSONObject tableData = new JSONObject();

			Bson updtfiltercount = and(eq("Category", "Explore"), eq("funnelName", mainfunnel),
					eq("CreatedBy", username));

			FindIterable<Document> filerdata = collection.find(updtfiltercount);
			monitordata = filerdata.iterator();
			int activeuser = 0;
			Document datsrc_doc = null;
			JSONObject jsobj = null;
			JSONObject montjsobj = null;
			JSONObject chardatajsobj = new JSONObject();
			JSONObject chardatamainobj = new JSONObject();
			JSONArray chararr = new JSONArray();
			if (mainfunnel != null && mainfunnel != "") {
				while (monitordata.hasNext()) {
					// out.println("in newsubfuncount =");
					datsrc_doc = monitordata.next();
					jsobj = new JSONObject(datsrc_doc.toJson());
					if (jsobj.has("ActiveUsersCount")) {
						activeuser = Integer.parseInt(jsobj.getString("ActiveUsersCount"));
					}

					for (int i = 0; i < monthArray.length; i++) {
						if (jsobj.has(monthArray[i])) {

							montjsobj = new JSONObject();
							montjsobj.put("unsubscribe", unsubscrb);
							montjsobj.put("headroom", 0);
							montjsobj.put("spam", 0);
//							connmonth  connect
							montjsobj.put("active user", jsobj.getJSONArray(monthArray[i]).length());
							if (jsobj.has("connect" + monthArray[i])) {
								montjsobj.put("funnel end", jsobj.getJSONArray("connect" + monthArray[i]).length());
							} else {
								montjsobj.put("funnel end", 0);
							}

							chardatajsobj.put(monthArray[i], montjsobj);
						}

					}
					if (chardatajsobj.length() < 1) {
						montjsobj = new JSONObject();
						montjsobj.put("unsubscribe", 0);
						montjsobj.put("headroom", 0);
						montjsobj.put("spam", 0);
						montjsobj.put("funnel end", 0);
						montjsobj.put("active user", 0);
						chardatajsobj.put("No Month Yet", montjsobj);

						chararr.put(chardatajsobj);
					}

					break;
				}

//				chardatamainobj.put("chartData", chararr);
				monitordata.close();
				monitordata = null;
			}

//connect

			Bson updtfilterconn = and(eq("Category", "Connect"), eq("funnelName", mainfunnel),
					eq("CreatedBy", username));

			FindIterable<Document> filerdatacon = collection.find(updtfilterconn);
			monitordata = filerdatacon.iterator();
			int funnelend = 0;
			Document datsrc_docconn = null;

			while (monitordata.hasNext()) {
				// out.println("in newsubfuncount =");
				datsrc_docconn = monitordata.next();//
				JSONObject dcjs = new JSONObject(datsrc_docconn.toJson());
				funnelend = dcjs.getJSONArray("SentEmailList").length();
				break;
			}
			monitordata.close();
			monitordata = null;

			tableData.put("active user", activeuser);
			tableData.put("funnel end", funnelend);
			tableData.put("unsubscribe", unsubscrb);
			tableData.put("headroom", 0);
			tableData.put("spam", 0);

			actuseobj.put("tableData", tableData);
			actuseobj.put("chartData", chararr);

			mainjson.put("active user", actuseobj);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mainjson;
	}

	//

	private static String sendGet(String url) throws Exception {

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		// print result
		return response.toString();

	}

	public MongoClient getConnection() {
		MongoClient mongoClient = null;
		try {

			System.setProperty("javax.net.ssl.trustStore", "/etc/ssl/firstTrustStore");
			System.setProperty("javax.net.ssl.trustStorePassword", "bizlem123");
			System.setProperty("javax.net.ssl.keyStore", "/etc/ssl/MongoClientKeyCert.jks");
			System.setProperty("javax.net.ssl.keyStorePassword", "bizlem123");
			String uri = "mongodb://localhost:27017/?ssl=true";

			MongoClientURI connectionString = new MongoClientURI(uri);

			mongoClient = new MongoClient(connectionString);
			System.out.println("Connecting to MongoDB  Server new version.......  " + mongoClient.getAddress());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mongoClient;
	}

	public static JSONObject CampaignFunnelGetNoOFLeads(JSONObject mainjson, String username, String funnelname) {

//		Mongo mongo = new Mongo("localhost", 27017);
//
//		DB db = mongo.getDB("phplisttest");

		MongoClient mongoClient = null;

		MongoDatabase db = null;
		MongoCollection<Document> collection = null;

		MongoCursor<Document> monitordata = null;
		MongoCursor<Document> monitordatahotleads = null;
		String docobj = null;
		String hotleadobj = null;
		JSONObject newjs = null;
		try {

			mongoClient = new mongodbdata().getConnection();
			db = mongoClient.getDatabase("salesautoconvert");
			collection = db.getCollection("FirstCategoryMails");

			Bson updtfiltercount = and(eq("Category", "Explore"), eq("funnelName", funnelname),
					eq("CreatedBy", username));

			FindIterable<Document> filerdata = collection.find(updtfiltercount);
			monitordata = filerdata.iterator();

			Document datsrc_doc = null;
			int rawleads = 0;
			try {
				if (monitordata.hasNext()) {
					while (monitordata.hasNext()) {
						try {
							datsrc_doc = monitordata.next();

							docobj = datsrc_doc.toJson();
							newjs = new JSONObject(docobj);
							if (newjs.has("Contacts") && newjs.getJSONArray("Contacts").length() > 0) {
								rawleads = newjs.getInt("rawleads");
							} else {

								rawleads = newjs.getJSONArray("SentExploreContacts").length();
							}
							break;
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			// for hotleads

			int hotleads = 0;

			hotleads = GetNoOfLeadsByCategory(username, funnelname, collection, "Connect");
//			int expleads = GetNoOfLeadsByCategory(username, funnelname, collection, "Explore");
			int entcleads = GetNoOfLeadsByCategory(username, funnelname, collection, "Entice");
			int infleads = GetNoOfLeadsByCategory(username, funnelname, collection, "Inform");
			int warmleads = GetNoOfLeadsByCategory(username, funnelname, collection, "Warm");

			JSONObject funncatjson = new JSONObject();
			funncatjson.put("warm", warmleads);
			funncatjson.put("inform", infleads);
			funncatjson.put("explore", rawleads);
			funncatjson.put("entice", entcleads);
			funncatjson.put("connect", hotleads);
			JSONArray campaignfunnel = new JSONArray();

			campaignfunnel.put(funncatjson);
			JSONObject driprate = new JSONObject();
			mainjson.put("rawLeads", rawleads); // hardcoded
			// for getting conv % dividing hotleads by rawlead

			driprate.put("convRate", String.valueOf((hotleads / rawleads) * 100) + "%");
			driprate.put("rawLead", rawleads);
			driprate.put("conv", hotleads);
			driprate.put("rate", 0);

			int convrate = hotleads / rawleads;
			mainjson.put("convRate", convrate);
			mainjson.put("dripRate", driprate);
			mainjson.put("hotLeads", hotleads);
			mainjson.put("campaignFunnel", campaignfunnel);
			mainjson.put("convLeads", hotleads);
			// convLeads
//			campaignFunneldata

			JSONArray campaignFunneldataar = GetNoOfLeadsBySource(username, funnelname, collection);

			// import contacts from linkedin, facebook , salesforce and Friend Other.
//			for (int i = 0; i < 3; i++) {
//				JSONObject campaignFunneldata = new JSONObject();
//				campaignFunneldata.put("rate", 5);
//				campaignFunneldata.put("leads", 200);
//				campaignFunneldata.put("source", "facebook");
//				campaignFunneldataar.put(campaignFunneldata);
//			}
			mainjson.put("campaignFunneldata", campaignFunneldataar);

			JSONArray subfunnelarr = new JSONArray();
			// upgradationRate obj

			subfunnelarr.put("Cold");
			subfunnelarr.put("Warm");
			subfunnelarr.put("Hot");
			subfunnelarr.put("Connect");

			JSONArray tablearr = new JSONArray();
			JSONObject tableobj = new JSONObject();
			tableobj.put("source", "facebook");
			tableobj.put("rate", "2%");
			tablearr.put(tableobj);

			JSONObject coldobj = new JSONObject();
			JSONObject Warmobj = new JSONObject();
			JSONObject Hotobj = new JSONObject();
			JSONObject connobj = new JSONObject();

			coldobj.put("rate", String.valueOf((entcleads / rawleads) * 100) + "%"); // String.valueOf((entcleads /
																						// rawleads) * 100) +
			coldobj.put("subFunnel", "Cold");
			// coldobj.put("tableData", tablearr);

			Warmobj.put("rate", String.valueOf((infleads / rawleads) * 100) + "%");
			Warmobj.put("subFunnel", "Warm");
			// Warmobj.put("tableData", tablearr);

			Hotobj.put("rate", String.valueOf((warmleads / rawleads) * 100) + "%");
			Hotobj.put("subFunnel", "Hot");
			// Hotobj.put("tableData", tablearr);

			connobj.put("rate", String.valueOf((hotleads / rawleads) * 100) + "%");
			connobj.put("subFunnel", "Connect");
			// connobj.put("tableData", tablearr);

			JSONArray dataarr = new JSONArray();
			dataarr.put(coldobj);
			dataarr.put(Warmobj);
			dataarr.put(Hotobj);
			dataarr.put(connobj);

			JSONObject dtaobj = new JSONObject();
			dtaobj.put("data", dataarr);
			dtaobj.put("subFunnel", subfunnelarr);

			mainjson.put("upgradationRate", dtaobj);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Exception ex : " + e.getMessage());

		} finally {
			if (null != mongoClient) {
				mongoClient.close();
				mongoClient = null;
			}

		}
		return mainjson;

	}

	private static int GetNoOfLeadsByCategory(String username, String funnelname, MongoCollection<Document> collection,
			String category) {
		MongoCursor<Document> monitordatahotleads = null;
		String hotleadobj;
		JSONObject newjs;
		int hotleads = 0;
		try {
			Bson filtercount2 = and(eq("Category", category), eq("funnelName", funnelname), eq("CreatedBy", username));

			FindIterable<Document> filerdata = collection.find(filtercount2);
			monitordatahotleads = filerdata.iterator();

			Document datsrc_doc2 = null;

			if (monitordatahotleads.hasNext()) {
				while (monitordatahotleads.hasNext()) {
					try {
						datsrc_doc2 = monitordatahotleads.next();

						hotleadobj = datsrc_doc2.toJson();
						newjs = new JSONObject(hotleadobj);
						if (newjs.has("SentEmailList") && newjs.getJSONArray("SentEmailList").length() > 0) {

							hotleads = newjs.getJSONArray("SentEmailList").length();
						}
						break;
					} catch (Exception e) {

					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		monitordatahotleads.close();
		return hotleads;
	}

	public static JSONObject upcomingCampaigndetails(JSONObject viewmainjs, String CreatedBy, String funnel) {
		MongoClient mongoClient = null;
		MongoDatabase database = null;
		MongoCollection<Document> collection = null;
		MongoCursor<Document> funnelDetailsCursor = null;
		JSONArray allcategjson = new JSONArray();

		Document campaign_list_doc = null;
		// JSONObject viewmainjs = new JSONObject();

		try {
			mongoClient = new mongodbdata().getConnection();
			database = mongoClient.getDatabase("salesautoconvert");
			collection = database.getCollection("FirstCategoryMails");
			JSONObject funnelListJson = null;

			try {

				Bson filter = and(eq("Parentfunnel", funnel), eq("CreatedBy", CreatedBy));
				funnelDetailsCursor = collection.find(filter).iterator();
				int rawleads = 0;
				String campaignName = null;
				String campcreateddate = null;
				String mailtempname = null;
				String docobj = null;
				Date set_date = null;
				DateFormat df = new SimpleDateFormat("dd-MM-yy");
				// Date dateobj = new Date();
				String Current_Date = null;
				boolean flg = true;
				int noleads = 0;
				if (funnelDetailsCursor.hasNext()) {
					while (funnelDetailsCursor.hasNext()) {
						try {
							funnelListJson = new JSONObject();
							campaign_list_doc = funnelDetailsCursor.next();
							JSONObject obj = new JSONObject(campaign_list_doc.toJson());

							if (obj.has("Contacts") && obj.getJSONArray("Contacts").length() == 0) {
								flg = false;
								continue;
							}

							if (flg) {

								campaignName = campaign_list_doc.getString("campaignName");
								if (campaign_list_doc.containsKey("Created_date")
										&& campaign_list_doc.containsKey("ActivateDate")) {
									Current_Date = campaign_list_doc.getString("ActivateDate");
								} else {
									campcreateddate = campaign_list_doc.getString("Created_date");

									Date date1 = df.parse(campcreateddate);
									Current_Date = df.format(date1);
								}

								funnelListJson.put("subFunnelName", campaignName);
								funnelListJson.put("date", Current_Date);

								if (obj.has("Contacts") && obj.getJSONArray("Contacts").length() > 0) {
									noleads = obj.getJSONArray("Contacts").length();
								} else {
									if (obj.has("campaignleadEmailList")) {
										noleads = obj.getJSONArray("campaignleadEmailList").length();
									}
								}
								funnelListJson.put("leads", noleads);

								allcategjson.put(funnelListJson);
							}

						} catch (Exception e) {

							e.printStackTrace();
						}

					}
				}

			} catch (Exception e) {
				e.printStackTrace();

			}

//	        for(int i=0;i<categarr.length();i++) {
//	        JSONObject	funnelListJsonnew=  ViewFunneldetailsForOtherCat(CreatedBy, funnel, categarr.getString(i));
//	        allcategjson.put(funnelListJsonnew);
//	        }	
			if (allcategjson.length() == 0) {
				funnelListJson = new JSONObject();
				funnelListJson.put("subFunnelName", "No campaign Found");
				funnelListJson.put("date", "dd-MM-yy");
				funnelListJson.put("leads", 0);

				allcategjson.put(funnelListJson);
			}

			viewmainjs.put("upcomingCampaign", allcategjson);

		} catch (Exception ex) {
			ex.printStackTrace();

		}
		return viewmainjs;
	}

	public static int GetTotalSessionTimePerCamp(String CreatedBy, String SubFunnelName, String FunnelName,
			String CampaignId, MongoCollection<Document> RuleEngineCalledForSubscriberData)
			throws JSONException, IOException {
		int AlltotalSessiontime = 0;

		Bson filter2 = null;
		filter2 = and(eq("CreatedBy", CreatedBy), eq("SubFunnelName", SubFunnelName), eq("FunnelName", FunnelName),
				eq("CampaignId", CampaignId));
		FindIterable<Document> filerdata = RuleEngineCalledForSubscriberData.find(filter2);
		MongoCursor<Document> monitordata = filerdata.iterator();

		int totalSessiontime = 0;

//			resp = resp + "start3 GAEmail: " + GAEmail;
		// out.println("RuleEngineCalledForSubscriberData ");
		try {
			while (monitordata.hasNext()) {

				Document campaignWisedata = monitordata.next();
				try {
					totalSessiontime = campaignWisedata.getInteger("TotalSesionDuration");
					AlltotalSessiontime = AlltotalSessiontime + totalSessiontime;

				} catch (Exception e) {
				}
			}
			monitordata.close();

		} catch (Exception e) {
			// out.println("exc in findGAUserCredentials: " + e);

		}
		return AlltotalSessiontime;
	}

	private static JSONArray GetNoOfLeadsBySource(String username, String funnelname,
			MongoCollection<Document> collection) {
		MongoCursor<Document> monitordatahotleads = null;
		MongoCursor<Document> subcursor = null;
		String hotleadobj;
		JSONObject newjs;
		int noleads = 0;
		JSONArray campaignFunneldata = new JSONArray();
		try {
			Bson filtercount2 = and(eq("Category", "Explore"), eq("funnelName", funnelname), eq("CreatedBy", username));

			FindIterable<Document> filerdata = collection.find(filtercount2);
			monitordatahotleads = filerdata.iterator();

			Document datsrc_doc2 = null;
			String[] catedashboard = { "Friend", "Facebook", "Linkedin", "Other" };
			Document query = null;
			int frnd = 0;
			int fb = 0;
			int lnkd = 0;
			int other = 0;
			JSONObject frndjs = new JSONObject();
			JSONObject fbjs = new JSONObject();
			JSONObject lnkdjs = new JSONObject();
			JSONObject otherjs = new JSONObject();
			if (monitordatahotleads.hasNext()) {
				while (monitordatahotleads.hasNext()) {

					datsrc_doc2 = monitordatahotleads.next();

					newjs = new JSONObject(datsrc_doc2.toJson());

					for (int i = 0; i < catedashboard.length; i++) {
						Document statusQuery = new Document("Source", catedashboard[i]);
						Document fields = new Document("$elemMatch", statusQuery);

						if (newjs.has("Contacts") && newjs.getJSONArray("Contacts").length() > 0) {

							query = new Document("Contacts", fields);

						} else {
							query = new Document("SentExploreContacts", fields);

						}
						if (query != null) {
							FindIterable<Document> subfi = collection.find(query);
							subcursor = subfi.iterator();
							while (subcursor.hasNext()) {
								try {
									subcursor.next().toJson();
									if (catedashboard[i].equals("Friend")) {
										frnd++;
									} else if (catedashboard[i].equals("Facebook")) {
										fb++;
									} else if (catedashboard[i].equals("Linkedin")) {
										lnkd++;
									} else {
										other++;
									}
								} catch (Exception e) {
									// TODO: handle exception
								}
							}
							if (subcursor != null) {
								subcursor.close();
								subcursor = null;
							}

						}
					}

					break;
				}
			}

			frndjs.put("source", "Friend");
			frndjs.put("leads", frnd);
			// frndjs.put("rate", 0);

			fbjs.put("source", "Facebook");
			fbjs.put("leads", fb);
			// fbjs.put("rate", 0);

			lnkdjs.put("source", "Linkedin");// linkedin
			lnkdjs.put("leads", lnkd);
			// lnkdjs.put("rate", 0);

			otherjs.put("source", "Other");
			otherjs.put("leads", other);
			// otherjs.put("rate", 0);

			campaignFunneldata.put(frndjs);
			campaignFunneldata.put(fbjs);
			campaignFunneldata.put(lnkdjs);
			campaignFunneldata.put(otherjs);

		} catch (Exception e) {
			e.printStackTrace();
		}
		monitordatahotleads.close();
		return campaignFunneldata;
	}

}
