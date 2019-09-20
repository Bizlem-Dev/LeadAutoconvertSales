package leadconverter.mongo;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.bson.Document;
import org.bson.conversions.Bson;
//import org.json.JSONObject;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class GetGADataForMonitoringLeads {
	public static void main(String args[]) throws JSONException, IOException {

//		String jj = findGAdatapersubscriber("viki_gmail.com", "Explore", "today302019", "1628");
//		System.out.println("jj== " + jj);
		// JSONObject subscjs=findsubscriberdetails( "mohit.raj@bizlem.com ",
		// "viki@gmail.com", "funnel903");
//		System.out.println("subscjs"+subscjs);

	}

	public static String findGAdatapersubscriber(String CreatedBy, String SubFunnelName, String FunnelName,
			String CampaignId, SlingHttpServletResponse res) throws JSONException, IOException {
		String resp = "";// ,SlingHttpServletResponse res
		JSONObject subscriber_json_obj = null;

		MongoClientURI connectionString = null;
		MongoClient mongoClient = null;
		MongoDatabase database = null;
		MongoCollection<Document> RuleEngineCalledForSubscriberData = null;
		JSONObject Allsubscrdata = new JSONObject();
		PrintWriter out = res.getWriter();
		try {
			System.out.println("Method Called");
	mongoClient=ConnectionHelper.getConnection();

			// mongoClient=ConnectionHelper.getConnection();
			database = mongoClient.getDatabase("phplisttest");

			RuleEngineCalledForSubscriberData = database.getCollection("RuleEngineCalledForSubscriberData");

			resp = "start";
			Bson filter2 = null;
			filter2 = and(eq("CreatedBy", CreatedBy), eq("SubFunnelName", SubFunnelName), eq("FunnelName", FunnelName),
					eq("CampaignId", CampaignId));
			System.out.println("query : " + filter2);
			FindIterable<Document> filerdata = RuleEngineCalledForSubscriberData.find(filter2);
			MongoCursor<Document> monitordata = filerdata.iterator();
			System.out.println("Iterator has next : " + monitordata.hasNext());
			JSONArray subscriberjsarr = new JSONArray();

			double totalSessiontime = 0;
			String Email = null;
			String recentSessiontime = "0";
			double AlltotalSessiontime = 0;
			double allrecentSessiontime = 0;
			double rst = 0;
			int noclick;
			int TotalClicks = 0;
			 Iterator urlitr=null;
			 Document url_doc=null;
			JSONArray url_json_arr=null;
//			resp = resp + "start3 GAEmail: " + GAEmail;
			// out.println("RuleEngineCalledForSubscriberData ");
			while (monitordata.hasNext()) {
				System.out.println("inside------- monitor data");

				Document campaignWisedata = monitordata.next();
				try {
					System.out.println("ga_user_json_obj= campaignWisedata= " + campaignWisedata);
					subscriber_json_obj = new JSONObject();
					Email = campaignWisedata.getString("SubscriberEmail");
					subscriber_json_obj.put("Email", Email);
					System.out.println("Email= " + Email);
					noclick = campaignWisedata.getInteger("NoOfUrlClicks");
					System.out.println("noclick= " + noclick);
					TotalClicks =TotalClicks + noclick;
//					TotalClicks = (int) (TotalClicks + noclick);
					System.out.println("TotalClicks= " + TotalClicks);
					totalSessiontime = campaignWisedata.getInteger("TotalSesionDuration");
					System.out.println("totalSessiontime= " + totalSessiontime);
					subscriber_json_obj.put("totalSessiontime", totalSessiontime);
					AlltotalSessiontime = AlltotalSessiontime + totalSessiontime;
					subscriber_json_obj.put("totalVisits", "0");
					recentSessiontime = campaignWisedata.getString("Recent_AvgSesionDuration");
					System.out.print("recentSessiontime " + recentSessiontime);
					rst = Double.parseDouble(recentSessiontime);

					allrecentSessiontime = allrecentSessiontime + rst;
					subscriber_json_obj.put("recentSessiontime", recentSessiontime);
					subscriber_json_obj.put("recentVisits", "0");
					// out.println("subscriber_json_obj= "+subscriber_json_obj);

					try {
						JSONObject url_json_obj=null;
						List<Document> PageUrls = (List<Document>) campaignWisedata.get("PageUrls");
						System.out.println("PageUrls.size()= " + PageUrls.size());
						url_json_arr=new JSONArray();
						urlitr= PageUrls.listIterator();
						while(urlitr.hasNext()){
							url_doc=(Document) urlitr.next();
							//System.out.println("campaign_doc : "+campaign_doc);
							url_json_obj = new JSONObject();
							url_json_obj.put("URL", url_doc.getString("url"));
							
							try {
								url_json_obj.put("AvgTime", url_doc.getInteger("AvgTimeOnPage"));
							}catch (Exception e) {
								url_json_obj.put("AvgTime", url_doc.getDouble("AvgTimeOnPage"));
//								out.println("url_json_obj= " + e);
							}
						
								
							
//							out.println("url_json_obj= " + url_json_obj);
						    url_json_arr.put(url_json_obj);
						}
//						out.println("url_json_arr= " + url_json_arr);
						
						subscriber_json_obj.put("PageUrls", url_json_arr);
					} catch (Exception e) {
						// TODO: handle exception
						out.println("exc= " + e);
					}
					// TotalClicks=TotalClicks+NoOfUrlClicks;
					try {
						JSONObject subscjs = findsubscriberdetails(Email, CreatedBy, FunnelName);
						System.out.println("subscjs" + subscjs);
						subscriber_json_obj.put("Name", subscjs.get("name"));
						subscriber_json_obj.put("Source", subscjs.get("source"));
						// SubscriberId
					} catch (Exception e) {
						// TODO: handle exception
						subscriber_json_obj.put("Name", "NA");
						subscriber_json_obj.put("Source", "NA");

					}
					subscriberjsarr.put(subscriber_json_obj);
					System.out.println("ga_user_json_obj= " + subscriber_json_obj);
				} catch (Exception e) {
					out.println("exccc = " + e);
				}
			}
			monitordata.close();

			JSONObject totalllead = new JSONObject();
			totalllead.put("totalSessiontime", AlltotalSessiontime);
			totalllead.put("recentSessiontime", allrecentSessiontime);
			totalllead.put("clicked", TotalClicks);

			// out.println("AlltotalSessiontime= " + AlltotalSessiontime);///
			Allsubscrdata.put("CampaignId", CampaignId);
			Allsubscrdata.put("Category", SubFunnelName);
			Allsubscrdata.put("funnelname", FunnelName);
			Allsubscrdata.put("TotalLeadData", totalllead);
			Allsubscrdata.put("ActiveUsers", subscriberjsarr);

//			logger.info("Data for  GoogleAnalytics User Found ga_user_json_obj : " + ga_user_json_obj);
		} catch (Exception e) {
			// out.println("exc in findGAUserCredentials: " + e);
			Allsubscrdata.put("excmongo", e.toString());
		} finally {

			if (mongoClient != null) {
				mongoClient.close();
				mongoClient = null;
			}
			// ConnectionHelper.closeConnection(mongoClient);

		}
		return Allsubscrdata.toString();
	}

	// move ui data

	public static String moveuidata(String CreatedBy, String SubFunnelName, String FunnelName,
			SlingHttpServletResponse res) throws JSONException, IOException {
		String resp = "";
		JSONObject subscriber_json_obj = null;

		MongoClientURI connectionString = null;
		MongoClient mongoClient = null;
		MongoDatabase database = null;
		MongoCollection<Document> RuleEngineCalledForSubscriberData = null;
		JSONObject Allsubscrdata = new JSONObject();
		PrintWriter out = res.getWriter();
		try {
			System.out.println("Method Called");
	    	mongoClient=ConnectionHelper.getConnection();
			// mongoClient=ConnectionHelper.getConnection();
			database = mongoClient.getDatabase("phplisttest");

			RuleEngineCalledForSubscriberData = database.getCollection("RuleEngineCalledForSubscriberData");

			resp = "start";
			Bson filter2 = null;
			filter2 = and(eq("CreatedBy", CreatedBy), eq("SubFunnelName", SubFunnelName), eq("FunnelName", FunnelName));
			System.out.println("query : " + filter2);
			FindIterable<Document> filerdata = RuleEngineCalledForSubscriberData.find(filter2);
			MongoCursor<Document> monitordata = filerdata.iterator();
			JSONArray subscriberjsarr = new JSONArray();

			double totalSessiontime = 0;
			String Email = null;
			String recentSessiontime = "0";
			double AlltotalSessiontime = 0;
			double allrecentSessiontime = 0;
			double rst = 0;
			int NoOfUrlClicks = 0;
			int TotalClicks = 0;
//			resp = resp + "start3 GAEmail: " + GAEmail;
			// out.println("RuleEngineCalledForSubscriberData ");
			while (monitordata.hasNext()) {
				System.out.println("inside------- monitor data");
				resp = resp + "start666";
				try {

					Document campaignWisedata = monitordata.next();
					System.out.println("ga_user_json_obj= campaignWisedata= " + campaignWisedata);
					subscriber_json_obj = new JSONObject();
					Email = campaignWisedata.getString("SubscriberEmail");
					subscriber_json_obj.put("Email", Email);
					System.out.println("Email= " + Email);
					totalSessiontime = campaignWisedata.getInteger("TotalSesionDuration");
					subscriber_json_obj.put("totalSessiontime", totalSessiontime);
					AlltotalSessiontime = AlltotalSessiontime + totalSessiontime;
					subscriber_json_obj.put("totalVisits", "0");
					recentSessiontime = campaignWisedata.getString("Recent_AvgSesionDuration");
					System.out.print("recentSessiontime " + recentSessiontime);
					rst = Double.parseDouble(recentSessiontime);

					allrecentSessiontime = allrecentSessiontime + rst;
					subscriber_json_obj.put("recentSessiontime", recentSessiontime);
					subscriber_json_obj.put("recentVisits", "0");
					// out.println("subscriber_json_obj= "+subscriber_json_obj);
					// NoOfUrlClicks=campaignWisedata.getInteger("NoOfUrlClicks");
					try {

						List<Document> PageUrls = (List<Document>) campaignWisedata.get("PageUrls");
						System.out.println("PageUrls.size()= " + PageUrls.size());
						TotalClicks = TotalClicks + PageUrls.size();

					} catch (Exception e) {
						// TODO: handle exception
						out.println("exc= " + e);
					}
					// TotalClicks=TotalClicks+NoOfUrlClicks;
					try {
						JSONObject subscjs = findsubscriberdetails(Email, CreatedBy, FunnelName);
						System.out.println("subscjs" + subscjs);
						subscriber_json_obj.put("Name", subscjs.get("name"));
						subscriber_json_obj.put("Source", subscjs.get("source"));// SubscriberId
						subscriber_json_obj.put("SubscriberId", subscjs.get("SubscriberId"));
					} catch (Exception e) {
						// TODO: handle exception
						subscriber_json_obj.put("Name", "NA");
						subscriber_json_obj.put("Source", "NA");

					}
					subscriberjsarr.put(subscriber_json_obj);
					System.out.println("ga_user_json_obj= " + subscriber_json_obj);
				} catch (Exception e) {
					out.println("exccc = " + e);
				}
			}
			monitordata.close();

			JSONObject totalllead = new JSONObject();
			totalllead.put("totalSessiontime", AlltotalSessiontime);
			totalllead.put("recentSessiontime", allrecentSessiontime);
			totalllead.put("clicked", TotalClicks);

			Allsubscrdata.put("Category", SubFunnelName);
			Allsubscrdata.put("funnelname", FunnelName);
			Allsubscrdata.put("TotalLeadData", totalllead);
			Allsubscrdata.put("ActiveUsers", subscriberjsarr);

//			logger.info("Data for  GoogleAnalytics User Found ga_user_json_obj : " + ga_user_json_obj);
		} catch (Exception e) {
			// out.println("exc in findGAUserCredentials: " + e);
			Allsubscrdata.put("excmongo", e.toString());
		} finally {

			if (mongoClient != null) {
				mongoClient.close();
				mongoClient = null;
			}
			// ConnectionHelper.closeConnection(mongoClient);

		}
		return Allsubscrdata.toString();
	}

	public static JSONObject findsubscriberdetails(String subscriberEmail, String CreatedBy, String FunnelName)
			throws JSONException {

		String resp = "";
		JSONObject subscriber_json_obj = subscriber_json_obj = new JSONObject();

		MongoClientURI connectionString = null;
		MongoClient mongoClient = null;
		MongoDatabase database = null;
		MongoCollection<Document> RuleEngineCalledForSubscriberData = null;

		try {

	    	mongoClient=ConnectionHelper.getConnection();
			// mongoClient=ConnectionHelper.getConnection();
			database = mongoClient.getDatabase("phplisttest");

			RuleEngineCalledForSubscriberData = database.getCollection("subscribers_details");

			resp = "start";
			Bson filter2 = null;
			filter2 = and(eq("CreatedBy", CreatedBy), eq("EmailAddress", subscriberEmail),
					eq("FunnelName", FunnelName));//
			FindIterable<Document> filerdata = RuleEngineCalledForSubscriberData.find(filter2);
			MongoCursor<Document> monitordata = filerdata.iterator();

			String source = null;
			String name = null;
			while (monitordata.hasNext()) {

				try {

					Document campaignWisedata = monitordata.next();
					System.out.println("email= " + campaignWisedata);
					name = campaignWisedata.getString("FirstName");
					source = campaignWisedata.getString("Source");
					subscriber_json_obj.put("name", name);
					subscriber_json_obj.put("subscriberEmail", subscriberEmail);
					// SubscriberId
					subscriber_json_obj.put("SubscriberId", campaignWisedata.getString("SubscriberId"));
					subscriber_json_obj.put("source", source);
				} catch (Exception e) {
					System.out.println("exc = " + e);
				}
			}

//			logger.info("Data for  GoogleAnalytics User Found ga_user_json_obj : " + ga_user_json_obj);
		} catch (Exception e) {
			System.out.println("exc in findGAUserCredentials: " + e);
			subscriber_json_obj.put("excmongo", e.toString());

		}
		return subscriber_json_obj;
	}
}
