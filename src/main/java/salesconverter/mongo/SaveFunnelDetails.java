package salesconverter.mongo;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import salesconverter.doctiger.LogByFileWriter;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import javax.jcr.Node;
import javax.jcr.Session;

import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.util.JSON;

import salesconverter.servlet.BizUtil;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;

import org.apache.log4j.Logger;

import services.CallGetService;
import services.CallPostService;

public class SaveFunnelDetails {

	public static void addFunnelDetails(String CreatedBy, String funnelName, String FromName, String FromEmailAddress,
			String category, String mailtemp, String campaignName, String DistanceBtnCampaign, String group,
			SlingHttpServletResponse res) throws IOException {
		PrintWriter out = res.getWriter();
		MongoClient mongoClient = null;
		MongoDatabase database = null;
		MongoCollection<Document> collection = null;
		String schedulegap = null;
		MongoCursor<Document> monitordata = null;
		DateFormat df = new SimpleDateFormat("dd-MM-yy HH:mm:ss");
		Date dateobj = new Date();
		String Current_Date = df.format(dateobj);
		out.println(df.format(dateobj));
		String mainfunnel = null;
		String formatdate = null;
		SimpleDateFormat formatter2 = new SimpleDateFormat("dd-MM-yy");
		String Week = null;
		try {
			Document camp_details_doc = new Document();
			camp_details_doc.put("CreatedBy", CreatedBy);
			camp_details_doc.put("funnelName", funnelName);
			camp_details_doc.put("Category", category);

			camp_details_doc.put("FromName", FromName);
			camp_details_doc.put("campaignName", campaignName);
			camp_details_doc.put("FromEmailAddress", FromEmailAddress);
			camp_details_doc.put("Campaign_id", mailtemp);
			camp_details_doc.put("Created_date", Current_Date);

			camp_details_doc.put("week", DistanceBtnCampaign);
			camp_details_doc.put("group", group);
			List<Document> data_sourcearr = new ArrayList<Document>();

			// camp_details_doc.put("Datasource", data_sourcearr);
			List<Document> schedulearr = new ArrayList<Document>();
			camp_details_doc.put("scheduleday", schedulearr);
			camp_details_doc.put("scheduleTime", "");
			camp_details_doc.put("updateflag", "-1");

			camp_details_doc.put("leadMailIdCount", "");
			camp_details_doc.put("subFunnelCampCount", "0");

			Date set_date = null;
			try {
				set_date = new SimpleDateFormat("dd-MM-yy").parse(Current_Date);
				// SimpleDateFormat formatter2=new SimpleDateFormat("dd-MM-yy");
				// formatter2.format(dateobj);
			} catch (ParseException e) {
				// TODO Auto-generated catch block

			}

			if (funnelName.contains("_EC_") || funnelName.contains("_EnC_") || funnelName.contains("_IC_")
					|| funnelName.contains("_WC_") || funnelName.contains("_CC_")) {
				mainfunnel = funnelName.substring(0, funnelName.lastIndexOf("_"));
				mainfunnel = funnelName.substring(0, mainfunnel.lastIndexOf("_"));
				out.println("mainfunnel = " + mainfunnel);
				camp_details_doc.put("Parentfunnel", mainfunnel);
				String campnumber = funnelName.substring(funnelName.lastIndexOf("_") + 1, funnelName.length());
				out.println("campnumber = " + campnumber);
				// System.out.println("campnumber = "+campnumber);
				int gapdays = Integer.parseInt(campnumber) - 1;
				gapdays = gapdays * 3;
				schedulegap = Integer.toString(gapdays);
				out.println(gapdays + "=gapdays schedulegapdate = " + schedulegap);
				set_date.setDate(set_date.getDate() + Integer.parseInt(schedulegap));
				String scheduletime = new SaveFunnelDetails().getmainscheduletime("Explore", CreatedBy, mainfunnel,
						res);
				formatdate = formatter2.format(set_date);
				out.println("schedulegapdate = " + formatdate);
				if(category.equals("Explore")) {
				camp_details_doc.put("ScheduleGapDate", formatdate + " " + scheduletime);// formatdate + " " +
																							// scheduletime
				camp_details_doc.put("multipleflag", "5");
				}
				//Week = GetWeekFromDate(formatdate);

			} else {
				mainfunnel = funnelName;
				camp_details_doc.put("Parentfunnel", mainfunnel);
				Week = GetWeekFromDate(Current_Date);
				camp_details_doc.put("expleadweek", Week);
			}
		

			mongoClient = ConnectionHelper.getConnection();
			database = mongoClient.getDatabase("salesautoconvert");
			collection = database.getCollection("FirstCategoryMails");
//			collection.insertOne(camp_details_doc);

			Bson updtfilter = and(eq("Category", category), eq("funnelName", funnelName), eq("CreatedBy", CreatedBy));
			collection.updateOne(updtfilter, new Document("$set", camp_details_doc), new UpdateOptions().upsert(true));

			// update subFunnelCampCount in main funnel
//			try {
//			if(mainfunnel!=null) {
//			Bson updtfiltercount = and(eq("Category", category), eq("funnelName", mainfunnel), eq("CreatedBy", CreatedBy));
//			
//			FindIterable<Document> filerdata = collection.find(updtfiltercount);
//			monitordata = filerdata.iterator();
//
//			Document datsrc_doc = null;
//			String subfuncount=null;
//			String newsubfuncount=null;
//			 int	newsubcount=0;
//			while (monitordata.hasNext()) {
//				  out.println("in newsubfuncount =");
//				datsrc_doc=monitordata.next();
//				subfuncount = datsrc_doc.getString("subFunnelCampCount");
//				  out.println("in old subfuncount ="+subfuncount);
//			    newsubcount=Integer.parseInt(subfuncount)+1;
//			    newsubfuncount=Integer.toString(newsubcount);
//			    out.println("newsubfuncount ="+newsubfuncount);
//			}
//			
//			Document subfunnelcountdoc = new Document();
//			subfunnelcountdoc.put("subFunnelCampCount", newsubfuncount);
//			 out.println("subfunnelcountdoc ="+subfunnelcountdoc);
//			collection.updateOne(updtfiltercount, new Document("$set", subfunnelcountdoc), new UpdateOptions().upsert(true));
//			}}catch (Exception e) {
//				// TODO: handle exception
//			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (mongoClient != null) {
				ConnectionHelper.closeConnection(mongoClient);
				mongoClient = null;
			}
		}
	}

	private static String GetWeekFromDate(String date) {
		String Week = null;
		try {
			DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
			Date date1 = df.parse(date);

			Calendar calendar = Calendar.getInstance();

			calendar.setTime(date1);
			int weekno = calendar.get(Calendar.WEEK_OF_MONTH);
			Week = "week" + Integer.toString(weekno);
		} catch (Exception e) {

			// TODO Auto-generated catch block
			System.out.println(e);
		}
		return Week;
	}

	public static String updateDataSource(String xlsjson, String CreatedBy, String funnelName, String category,
			String campaign_id) {

		// insert datasource array to FirstCategoryMails

		MongoClient mongoClient = null;
		MongoDatabase database = null;
		MongoCollection<Document> collection = null;
		Bson document = null;
		try {
			mongoClient = ConnectionHelper.getConnection();
			database = mongoClient.getDatabase("salesautoconvert");

			collection = database.getCollection("FirstCategoryMails");
			document = Document.parse(xlsjson);

			Bson searchQuery = new Document().append("CreatedBy", CreatedBy).append("funnelName", funnelName)
					.append("Category", category).append("Campaign_id", campaign_id);
			collection.updateOne(searchQuery, new Document("$set", document), new UpdateOptions().upsert(true));
			// collection.updateOne(searchQuery, new Document("$addToSet", document), new
			// UpdateOptions().upsert(true));

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != mongoClient) {
				mongoClient.close();
				mongoClient = null;
			}

		}
		return "success";
	}

	public static String GetExploredata(String xlsjson, String CreatedBy, String funnelName, String category,
			String campaign_id) {

		// insert datasource array to FirstCategoryMails

		MongoClient mongoClient = null;
		MongoDatabase database = null;
		MongoCollection<Document> collection = null;
		Bson document = null;
		try {
			mongoClient = ConnectionHelper.getConnection();
			database = mongoClient.getDatabase("salesautoconvert");

			collection = database.getCollection("FirstCategoryMails");
			document = Document.parse(xlsjson);

			Bson searchQuery = new Document().append("CreatedBy", CreatedBy).append("funnelName", funnelName)
					.append("Category", category).append("Campaign_id", campaign_id);

			collection.updateOne(searchQuery, new Document("$set", document), new UpdateOptions().upsert(true));

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != mongoClient) {
				mongoClient.close();
				mongoClient = null;
			}

		}
		return "success";
	}

	public static String findupdateflagforexplore(SlingHttpServletResponse resps, Node massmailratenode,
			Session session) throws JSONException, IOException {
		String resp = "";// ,SlingHttpServletResponse res
		PrintWriter out = resps.getWriter();
		MongoClientURI connectionString = null;
		MongoClient mongoClient = null;
		MongoDatabase database = null;
		MongoCollection<Document> OtherCategoryMailsData = null;
		MongoCursor<Document> subcursor = null;
		MongoCursor<Document> monitordata = null;
//		BasicDBList productList = null;
		String mailtemp = null;
		String group = null;
		String email = null;

		JSONObject dependencyjson = null;
		JSONArray multidrpdown = new JSONArray();
		multidrpdown.put("1");
//		multidrpdown.put("2");
		multidrpdown.put("3");

		int rate = 0;
		try {
			Date d = new Date();
			SimpleDateFormat formatter2 = new SimpleDateFormat("dd-MM-yy");
			SimpleDateFormat simpleDateformat = new SimpleDateFormat("EEEE"); // the day of the week spelled out
																				// completely
			StringBuilder selday = new StringBuilder(simpleDateformat.format(d));
			// System.out.println(selday.toString());
			StringBuilder hhmmformat = new StringBuilder(new SimpleDateFormat("HH:mm").format(d));
//			mongoClient = ConnectionHelper.getConnection();

			System.setProperty("javax.net.ssl.trustStore", "/etc/ssl/firstTrustStore");
			System.setProperty("javax.net.ssl.trustStorePassword", "bizlem123");
			System.setProperty("javax.net.ssl.keyStore", "/etc/ssl/MongoClientKeyCert.jks");
			System.setProperty("javax.net.ssl.keyStorePassword", "bizlem123");
			//// MongoClientOptions.builder().sslEnabled(true).sslInvalidHostNameAllowed(true).build();
			String uri = "mongodb://localhost:27017/?ssl=true";
			connectionString = new MongoClientURI(uri);
			mongoClient = new MongoClient(connectionString);

			database = mongoClient.getDatabase("salesautoconvert");

			OtherCategoryMailsData = database.getCollection("FirstCategoryMails");
//"scheduleday"
			resp = "start";
			Bson filter2 = null;
			LogByFileWriter.logger_info(d + "  hhmmformat.toString()) : " + hhmmformat.toString()
					+ "selday.toString() :: " + selday.toString());
			filter2 = and(eq("Category", "Explore"), eq("updateflag", "1"), eq("scheduleTime", hhmmformat.toString()));// ,
																														// eq("scheduleTime",
																														// hhmmformat.toString())
																														// eq("scheduleTime",
			LogByFileWriter.logger_info(d + " filter2 : " + filter2); // hhmmformat.toString())
			// out.println("hhmmformat is = "+hhmmformat+" ::selday.toString() =
			// "+selday.toString());
			Iterator datasrcitr = null;
			Iterator uploadeddatasrcitr = null;
			FindIterable<Document> filerdata = OtherCategoryMailsData.find(filter2);
			monitordata = filerdata.iterator();

			Document datsrc_doc = null;
			Document uploddatsrc_doc = null;
			JSONArray uploadedkeys = null;
			// JSONArray hash_Setstatickeys=new JSONArray();
			JSONObject statickeysobj = new JSONObject();

			// Set<String> hash_Setstatickeys = new HashSet<String>();
			statickeysobj.put("Email", "");
			statickeysobj.put("FirstName", "");
			statickeysobj.put("LastName", "");
			statickeysobj.put("PhoneNumber", "");
			statickeysobj.put("Country", "");
			statickeysobj.put("CompanyName", "");
			statickeysobj.put("CompanyHeadCount", "");
			statickeysobj.put("Industry", "");
			statickeysobj.put("Institute", "");
			statickeysobj.put("Source", "");

			if (monitordata.hasNext()) {
				while (monitordata.hasNext()) {
					int mailsentcount = 0;
					JSONObject newdatasrcobj = new JSONObject();
					boolean existflag = false;
					// out.println("in exploreWisedata =-====================== ");
					LogByFileWriter.logger_info(d
							+ "===================================   START  ===================================================== ");

					dependencyjson = new JSONObject();
					Document exploreWisedata = monitordata.next();

//				productList = null;
					out.println("in exploreWisedata = " + exploreWisedata);
					try {
						Document statusQuery = new Document("schedule", selday.toString());
						Document fields = new Document("$elemMatch", statusQuery);
						Document query = new Document("scheduleday", fields);
						FindIterable<Document> subfi = OtherCategoryMailsData.find(query);
						subcursor = subfi.iterator();
						while (subcursor.hasNext()) {
							subcursor.next().toJson();
							existflag = true;
						}
						if (subcursor != null) {
							subcursor.close();
							subcursor = null;
						}
						LogByFileWriter.logger_info(d + "existflag=" + existflag + " :: query=" + query
								+ " :: selday : " + selday.toString() + " :: funnelName "
								+ exploreWisedata.getString("funnelName"));
//					 out.println("existflag = "+existflag); //com by tj
						if (!existflag) {
							continue;
						} else {
							// out.println("else existflag = "+existflag);

						}
//						LogByFileWriter.logger_info(d + "existflag 316 : " + existflag);
						mailtemp = exploreWisedata.getString("Campaign_id");
						group = exploreWisedata.getString("group");
						email = exploreWisedata.getString("CreatedBy");
						Node funnelnode = null;
						Node emailnode = null;
						List<Document> datasourcesarr = (List<Document>) exploreWisedata.get("Contacts");
						LogByFileWriter.logger_info(d + "datasourcesarr = " + datasourcesarr);
						try {
							if (datasourcesarr.isEmpty()) {
								// System.out.println("No data found");
								// update flag to 2

//	BasicDBObject querypull = new BasicDBObject();
//	// "updateflag", "1""Campaign_id" CreatedBy group
//	querypull.put("Category", "Explore");
//	querypull.put("updateflag", "1");
//	querypull.put("Campaign_id", mailtemp);
//	querypull.put("CreatedBy", email);
//	querypull.put("group", group);
//	querypull.put("funnelName", exploreWisedata.getString("funnelName"));

								BasicDBObject newDocument1 = new BasicDBObject();
								newDocument1.put("updateflag", "2");

//	BasicDBObject updateObj = new BasicDBObject();
//	updateObj.put("$set", newDocument);

								// OtherCategoryMailsData.updateOne(querypull, updateObj);

								Bson searchQuery1 = new Document().append("CreatedBy", email)
										.append("funnelName", exploreWisedata.getString("funnelName"))
										.append("Category", "Explore").append("Campaign_id", mailtemp)
										.append("updateflag", "1");

								// out.println("searchQuery= "+searchQuery);
								OtherCategoryMailsData.updateOne(searchQuery1, new Document("$set", newDocument1),
										new UpdateOptions().upsert(true));

								break;
							}

							JSONObject rateobj = new CallGetService().getmassMailType(email, group);
							LogByFileWriter.logger_info(d + "==rateobj= " + rateobj);
							if (rateobj != null && rateobj.has("AccountType")
									&& rateobj.get("AccountType").equals("Normal")) {
								if (!massmailratenode.hasProperty("Normal")) {
									massmailratenode.setProperty("Normal", "75");
								} else {

								}

								rate = Integer.parseInt(massmailratenode.getProperty("Normal").getString());

							} else {
								if (!massmailratenode.hasProperty("MassMailer")) {
									massmailratenode.setProperty("MassMailer", "80");
								}
								rate = Integer.parseInt(massmailratenode.getProperty("MassMailer").getString());
							}

							if (!massmailratenode.hasNode(email.replaceAll("@", "_"))) {
								emailnode = massmailratenode.addNode(email.replaceAll("@", "_"));
							} else {
								emailnode = massmailratenode.getNode(email.replaceAll("@", "_"));

							}

							if (datasourcesarr.size() > 0) {
								if (!emailnode.hasNode(exploreWisedata.getString("funnelName"))) {
									funnelnode = emailnode.addNode(exploreWisedata.getString("funnelName"));
								} else {
									funnelnode = emailnode.getNode(exploreWisedata.getString("funnelName"));

								}

								if (!funnelnode.hasProperty("MailSentCount")) {
									funnelnode.setProperty("MailSentCount", "0");
								}
							}

							// LogByFileWriter.logger_info(d + "==funnelnode= " + funnelnode);
						} catch (Exception e) {
							LogByFileWriter.logger_info(d + "==exc= " + e);

						}
//					else {
//							mailsentcount=Integer.parseInt(funnelnode.getProperty("MailSentCount").getString());
//						}

						// MassMailer

						JSONObject fetchPrimaryKey = new JSONObject();
						fetchPrimaryKey.put("email", email);
						fetchPrimaryKey.put("group", group);
						fetchPrimaryKey.put("MailTempName", mailtemp);

						dependencyjson.put("MailTempName", mailtemp);
						dependencyjson.put("templateName", "");
						dependencyjson.put("typeDataSource", "Enter manually");
						dependencyjson.put("AttachtempalteType", "");
						dependencyjson.put("esignature", "false");
						dependencyjson.put("twofactor", "false");
						dependencyjson.put("esigntype", "");
						dependencyjson.put("Email", email);
						dependencyjson.put("group", group);
						dependencyjson.put("lgtype", "null");

						dependencyjson.put("multipeDropDown", multidrpdown);

						dependencyjson.put("Type", "Generation");
						String url = null;
						String response = null;
						String apiurl = null;
						out.println("dependencyjson=" + dependencyjson);

						try {
							// get uploaded datasource fields from datasource

							List<Document> uploadeddatasourcesarr = (List<Document>) exploreWisedata.get("Datasource");
							LogByFileWriter.logger_info(d + "uploadeddatasourcesarr.size=" + uploadeddatasourcesarr);
							if (uploadeddatasourcesarr.size() > 0) {
								uploadeddatasrcitr = uploadeddatasourcesarr.listIterator();
								JSONObject uploddata_json_obj = null;
								while (uploadeddatasrcitr.hasNext()) {
									uploddatsrc_doc = (Document) uploadeddatasrcitr.next();
//							out.println("uploddatsrc_doc = "+uploddatsrc_doc);
									uploddata_json_obj = new JSONObject(uploddatsrc_doc.toJson());
									uploadedkeys = new SaveFunnelDetails().getjsonKey(uploddata_json_obj);
									break;
								}
								out.println("uploddata_json_obj =" + uploddata_json_obj.toString());
								if (uploddata_json_obj != null) {
									for (int k = 0; k < uploadedkeys.length(); k++) {
										if (!statickeysobj.has(uploadedkeys.getString(k))) {
//							newdatasrcobj=new JSONObject();
											newdatasrcobj.put(uploadedkeys.getString(k),
													uploddata_json_obj.get(uploadedkeys.getString(k)));
										} else {
//							out.println("else newdatasrcobj ="+newdatasrcobj);
										}
									}
								}
								// Contacts

							} else {
//							out.println("else uploadeddatasourcesarr.size="+uploadeddatasourcesarr.size());
							}
							out.println("newdatasrcobj =" + newdatasrcobj);
							JSONObject data_json_obj = null;

							if (datasourcesarr.size() > 0) {
								int da = datasourcesarr.size();
								if (!funnelnode.hasProperty("TotalMailCount")) {
									funnelnode.setProperty("TotalMailCount", Integer.toString(da));
								}

							}

							JSONArray dataarr = new JSONArray();
//						productList = new BasicDBList();
							datasrcitr = datasourcesarr.listIterator();
							int count = 0;
							url = ResourceBundle.getBundle("config").getString("mailtemplatekey");
							response = BizUtil.callPostAPIJSON(url, fetchPrimaryKey);
							out.println("url response = " + response);
							if (!BizUtil.isNullString(response)) {
								JSONObject outresponse = new JSONObject(response);
								LogByFileWriter.logger_info(d + "datasourcesarr " + datasourcesarr.size()
										+ "outresponse=== " + outresponse);
								while (datasrcitr.hasNext()) {
									try {
										datsrc_doc = (Document) datasrcitr.next();
										data_json_obj = new JSONObject(datsrc_doc.toJson());
										LogByFileWriter
												.logger_info(d + "count = " + data_json_obj + " ---- count= " + count);

//										
										out.println("count = " + count + " ---- rate= " + rate);
										if (count < rate) {

//								out.println("dataarr= "+dataarr);
											// productList.add(dbObject);
											// add in sentcontactsExplore

											BasicDBObject querypull = new BasicDBObject();
											// "updateflag", "1""Campaign_id" CreatedBy group
											querypull.put("Category", "Explore");
											querypull.put("updateflag", "1");
											querypull.put("Campaign_id", mailtemp);
											querypull.put("CreatedBy", email);
											querypull.put("group", group);
											querypull.put("funnelName", exploreWisedata.getString("funnelName"));

											if (!exploreWisedata.getString("funnelName").toString()
													.equals("LocalFunnel21Nov")) {
												BasicDBObject fieldspull = new BasicDBObject("Contacts",
														new BasicDBObject(outresponse.getString("To"),
																data_json_obj.get(outresponse.getString("To"))));
												BasicDBObject update = new BasicDBObject("$pull", fieldspull);
												OtherCategoryMailsData.updateOne(querypull, update);
											}
											// add in sentexplorecontact

											BasicDBObject dbObject = (BasicDBObject) JSON
													.parse(data_json_obj.toString());

											BasicDBObject updateQuery = new BasicDBObject("$addToSet",
													new BasicDBObject("SentExploreContacts", dbObject));
											OtherCategoryMailsData.updateOne(querypull, updateQuery,
													new UpdateOptions().upsert(true));
											if (newdatasrcobj.length() > 0 && newdatasrcobj != null) {
												JSONArray newuploadedkeys = new SaveFunnelDetails()
														.getjsonKey(newdatasrcobj);
//									out.println("newuploadedkeys= "+newuploadedkeys);
												for (int l = 0; l < newuploadedkeys.length(); l++) {

													data_json_obj.put(newuploadedkeys.getString(l),
															newdatasrcobj.get(newuploadedkeys.getString(l)));
												}
											}

//										check bounced status in bounce collection
//										dataarr.put(newdatasrcobj);
											JSONObject bouncejs = new SaveFunnelDetails().getBouncedMailFlag(
													data_json_obj.get(outresponse.getString("To")).toString(), resps);
											LogByFileWriter.logger_info(d + "data_json_obj" + data_json_obj
													+ "::: bouncejs  : " + bouncejs);
											if (bouncejs.has("flag") && bouncejs.get("flag").equals("true")) {
												dataarr.put(data_json_obj);
											}
											if (datasourcesarr.size() == 1) {
												StringBuilder cuurTime = updateScheduleTime(
														exploreWisedata.getString("scheduleTime"));

												BasicDBObject newDocument2 = new BasicDBObject();
												newDocument2.put("scheduleTime", cuurTime.toString());

//										BasicDBObject updateObj = new BasicDBObject();
//										updateObj.put("$set", newDocument);
//
//										OtherCategoryMailsData.updateOne(querypull, updateObj);

												Bson searchQuery2 = new Document().append("CreatedBy", email)
														.append("funnelName", exploreWisedata.getString("funnelName"))
														.append("Category", "Explore").append("Campaign_id", mailtemp)
														.append("updateflag", "1");

												LogByFileWriter.logger_info(d + " searchQuery : " + searchQuery2
														+ " :: newDocument2" + newDocument2);
												// out.println("searchQuery= "+searchQuery);
												OtherCategoryMailsData.updateOne(searchQuery2,
														new Document("$set", newDocument2),
														new UpdateOptions().upsert(true));

											}

										} else {
											// update schedule time increasing it to one hour.

											out.println("else = " + count);
											StringBuilder cuurTime = updateScheduleTime(
													exploreWisedata.getString("scheduleTime"));
											out.println("cuurTime =" + cuurTime.toString());
//									BasicDBObject querypull = new BasicDBObject();
//									// "updateflag", "1""Campaign_id" CreatedBy group
//									querypull.put("Category", "Explore");
//									querypull.put("updateflag", "1");
//									querypull.put("Campaign_id", mailtemp);
//									querypull.put("CreatedBy", email);
//									querypull.put("group", group);
//									querypull.put("funnelName", exploreWisedata.getString("funnelName"));
//
											BasicDBObject newDocument = new BasicDBObject();
											newDocument.put("scheduleTime", cuurTime.toString());
//
//									BasicDBObject updateObj = new BasicDBObject();
//									updateObj.put("$set", newDocument);

											Bson searchQuery = new Document().append("CreatedBy", email)
													.append("funnelName", exploreWisedata.getString("funnelName"))
													.append("Category", "Explore").append("Campaign_id", mailtemp);
											LogByFileWriter.logger_info(d + " searchQuery : " + searchQuery
													+ " exploreWisedata.getString scheduleTime="
													+ exploreWisedata.getString("scheduleTime")
													+ " :cuurTime.toString() =" + cuurTime.toString());
											// out.println("searchQuery= "+searchQuery);
											OtherCategoryMailsData.updateOne(searchQuery,
													new Document("$set", newDocument),
													new UpdateOptions().upsert(true));

											// OtherCategoryMailsData.updateOne(querypull, updateObj, new
											// UpdateOptions().upsert(true));
											// Bson searchQuery = new Document().append("CreatedBy",
											// CreatedBy).append("funnelName", funnelName)
//									.append("Category", category).append("Campaign_id", campaign_id);
//									collection.updateOne(searchQuery, new Document("$set", document), new UpdateOptions().upsert(true));
//								
											break;
										}
										count++;

									} catch (Exception e) {
										LogByFileWriter.logger_info(d + "exc : " + e);
									}
								}

								if (dataarr.length() > 0 && funnelnode.hasProperty("MailSentCount")) {
//								mailsentcount=Integer.parseInt(funnelnode.getProperty("MailSentCount").getString());
									mailsentcount = Integer.parseInt(
											funnelnode.getProperty("MailSentCount").getString()) + dataarr.length();
									funnelnode.setProperty("MailSentCount", Integer.toString(mailsentcount));
								} else {

								}
								LogByFileWriter.logger_info(d + " SentEmail dataarr.length  : " + dataarr.length()
										+ "::mailsentcount = " + mailsentcount);
								
								//update trafic
								new SaveFunnelDetails().getmailsentfortrafic(exploreWisedata.getString("funnelName"), exploreWisedata.getString("CreatedBy"), OtherCategoryMailsData, dataarr.length(),exploreWisedata.getString("Campaign_id"));
								dependencyjson.put("data", dataarr);
								/*
								 * if(!(datasourcesarr.isEmpty())){ BasicDBObject querypull = new
								 * BasicDBObject(); //"updateflag", "1""Campaign_id" CreatedBy group
								 * querypull.put("Category", "Explore"); querypull.put("updateflag", "1");
								 * querypull.put("Campaign_id", mailtemp); querypull.put("CreatedBy", email);
								 * querypull.put("group", group); querypull.put("funnelName",
								 * exploreWisedata.getString("funnelName"));
								 * 
								 * // BasicDBObject listItem = new BasicDBObject("tempObjData",arrdata);new
								 * BasicDBObject("simulatedProduct", productList) BasicDBObject updateQuery =
								 * new BasicDBObject("$push", new BasicDBObject("OriginalDataSource",
								 * productList)); OtherCategoryMailsData.updateOne(querypull, updateQuery,new
								 * UpdateOptions().upsert(true)); }
								 */
								apiurl = ResourceBundle.getBundle("config").getString("doctigerapi");

								// out.println("apiurl= " + apiurl);
								out.println("dependencyjson : " + dependencyjson);
								LogByFileWriter.logger_info(d + "dependencyjson 586 : " + dependencyjson);
								int res = CallPostService.callPostAPIJSON(apiurl, dependencyjson, resps);
								LogByFileWriter.logger_info(d + "dependencyjson resp : " + res);
								if (res == 200) {
									// exploreWisedata.getString("funnelName")
									try {

										/*
										 * Bson updtfilter = and(eq("Category", "Explore"), eq("funnelName",
										 * exploreWisedata.getString("funnelName")), eq("updateflag", "1")); Bson
										 * updateDoc = new Document("updateflag", "2"); // out.println("funnel= " +
										 * exploreWisedata.getString("funnelName")); Bson updateCollection = new
										 * Document("$set", updateDoc);
										 * 
										 * OtherCategoryMailsData.updateOne(updtfilter, updateCollection); //
										 * out.println("updated= = ");
										 */} catch (Exception e) {
										e.printStackTrace();
									}
								}

								if (datasourcesarr.size() == 1) {
//									System.out.println("No data found");
									// update flag to 2

									BasicDBObject querypull = new BasicDBObject();
									// "updateflag", "1""Campaign_id" CreatedBy group
									querypull.put("Category", "Explore");
									querypull.put("updateflag", "1");
									querypull.put("Campaign_id", mailtemp);
									querypull.put("CreatedBy", email);
									querypull.put("group", group);
									querypull.put("funnelName", exploreWisedata.getString("funnelName"));

									BasicDBObject newDocument = new BasicDBObject();
									newDocument.put("updateflag", "2");

									BasicDBObject updateObj = new BasicDBObject();
									updateObj.put("$set", newDocument);

									OtherCategoryMailsData.updateOne(querypull, updateObj);
									break;
								}

							}

						} catch (Exception e) {
							LogByFileWriter.logger_info(d + "exc 111 " + e);
							e.printStackTrace();
						}

					} catch (Exception e) {
						LogByFileWriter.logger_info(d + "exc 2 " + e);
						e.printStackTrace();
					}
					// out.println("dependencyjson= " + dependencyjson);
					session.save();
					LogByFileWriter
							.logger_info(d + "===================================END============================== ");
				}
			} else {
				LogByFileWriter.logger_info(d + "else no record found ");

			}

		} catch (Exception e) {
			LogByFileWriter.logger_info("exc last 3 " + e);
			e.printStackTrace();
		} finally {

			if (monitordata != null) {
				monitordata.close();
				monitordata = null;
			}
			if (mongoClient != null) {
				mongoClient.close();
				mongoClient = null;
			}

		}
		return "success";
	}

	private static StringBuilder updateScheduleTime(String obj) {
		StringBuilder schtime = null;
		StringBuilder cuurTime = null;
		try {
			schtime = new StringBuilder(obj);

			String[] split = schtime.toString().split(":", -1);
			cuurTime = new StringBuilder();

			int currScheduletime = Integer.parseInt(split[0]) + 1;
			if (currScheduletime >= 24) {
				currScheduletime = 1;
			}
//			cuurTime.append(currScheduletime);
			if (currScheduletime < 10) {
				cuurTime.append("0" + currScheduletime);
			} else {
				cuurTime.append(currScheduletime);
			}
			cuurTime.append(":");
			cuurTime.append(split[1]);
//			System.out.println("Schedule Time is " + cuurTime.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cuurTime;
	}

	public static String SentMailforOtherCategory(SlingHttpServletResponse resps) throws JSONException, IOException {
		String resp = "";// ,SlingHttpServletResponse res
		PrintWriter out = resps.getWriter();
		MongoClient mongoClient = null;
		MongoDatabase database = null;
		MongoCollection<Document> OtherCategoryMailsData = null;
		MongoCollection<Document> collection = null;

		String mailtemp = null;
		String group = null;
		String email = null;
		JSONObject dependencyjson = null;
		JSONArray multidrpdown = new JSONArray();
		multidrpdown.put("1");
//		multidrpdown.put("2");
		multidrpdown.put("3");
		try {
			mongoClient = ConnectionHelper.getConnection();

			database = mongoClient.getDatabase("salesautoconvert");

			OtherCategoryMailsData = database.getCollection("OtherCategoryMails");
			collection = database.getCollection("FirstCategoryMails");
			JSONObject statickeysobj = new JSONObject();

			// Set<String> hash_Setstatickeys = new HashSet<String>();
			statickeysobj.put("Email", "");
			statickeysobj.put("FirstName", "");
			statickeysobj.put("LastName", "");
			statickeysobj.put("PhoneNumber", "");
			statickeysobj.put("Country", "");
			statickeysobj.put("CompanyName", "");
			statickeysobj.put("CompanyHeadCount", "");
			statickeysobj.put("Industry", "");
			statickeysobj.put("Institute", "");
			statickeysobj.put("Source", "");

			resp = "start";
			Document uploddatsrc_doc = null;
			Iterator datasrcitr = null;
			Iterator uploadeddatasrcitr = null;
			JSONArray uploadedkeys = null;

			FindIterable<Document> OthercatgoryWisedata = OtherCategoryMailsData.find();

			Document datsrc_doc = null;
			String funnelName = null;
			String mainfunnel = null;
			for (Document doc : OthercatgoryWisedata) {

				dependencyjson = new JSONObject();
				JSONObject newdatasrcobj = new JSONObject();
				try {
					JSONObject newdocjsonobj = new JSONObject(doc.toJson());

					mailtemp = newdocjsonobj.getString("Campaign_id");
					group = newdocjsonobj.getString("group");
					email = newdocjsonobj.getString("CreatedBy");
					dependencyjson.put("MailTempName", mailtemp);
					dependencyjson.put("templateName", "");
					dependencyjson.put("typeDataSource", "Enter manually");
					dependencyjson.put("AttachtempalteType", "");
					dependencyjson.put("esignature", "false");
					dependencyjson.put("twofactor", "false");
					dependencyjson.put("esigntype", "");
					dependencyjson.put("Email", email);
					dependencyjson.put("group", group);

					dependencyjson.put("multipeDropDown", multidrpdown);

					dependencyjson.put("Type", "Generation");
					try {
//get uploaded datasource
						funnelName = newdocjsonobj.getString("funnelName");
						if (funnelName.contains("_EC_") || funnelName.contains("_EnC_") || funnelName.contains("_IC_")
								|| funnelName.contains("_WC_") || funnelName.contains("_CC_")) {
							mainfunnel = funnelName.substring(0, funnelName.lastIndexOf("_"));
							mainfunnel = funnelName.substring(0, mainfunnel.lastIndexOf("_"));

						} else {
							mainfunnel = funnelName;
						}
						List<Document> uploadeddatasourcesarr = new SaveFunnelDetails().getContactarrofstartcatg(
								newdocjsonobj.getString("Category"), "Datasource", newdocjsonobj.getString("CreatedBy"),
								mainfunnel, resps);
						
						Document camp_details_doc=new Document();
							Bson updtfiltercount = and(eq("Category", "Explore"), eq("funnelName", mainfunnel),
									eq("CreatedBy", newdocjsonobj.getString("CreatedBy")));
						
						out.println("uploadeddatasourcesarr.size=" + uploadeddatasourcesarr.size());
						if (uploadeddatasourcesarr.size() > 0) {
							uploadeddatasrcitr = uploadeddatasourcesarr.listIterator();
							JSONObject uploddata_json_obj = null;
							while (uploadeddatasrcitr.hasNext()) {
								uploddatsrc_doc = (Document) uploadeddatasrcitr.next();
//						out.println("uploddatsrc_doc = "+uploddatsrc_doc);
								uploddata_json_obj = new JSONObject(uploddatsrc_doc.toJson());
								uploadedkeys = new SaveFunnelDetails().getjsonKey(uploddata_json_obj);
								break;
							}
							out.println("uploddata_json_obj =" + uploddata_json_obj.toString());
							if (uploddata_json_obj != null) {
								for (int k = 0; k < uploadedkeys.length(); k++) {
									if (!statickeysobj.has(uploadedkeys.getString(k))) {
//						newdatasrcobj=new JSONObject();
										newdatasrcobj.put(uploadedkeys.getString(k),
												uploddata_json_obj.get(uploadedkeys.getString(k)));
									} else {
//						out.println("else newdatasrcobj ="+newdatasrcobj);
									}
								}
							}
							// Contacts

						}

						JSONObject data_json_obj = null;
						JSONArray datasourcesarr = newdocjsonobj.getJSONArray("Contacts");
						JSONArray dataarr = new JSONArray();

						for (int j = 0; j < datasourcesarr.length(); j++) {

							data_json_obj = datasourcesarr.getJSONObject(j);

							if (newdatasrcobj.length() > 0 && newdatasrcobj != null) {
								JSONArray newuploadedkeys = new SaveFunnelDetails().getjsonKey(newdatasrcobj);
//				out.println("newuploadedkeys= "+newuploadedkeys);
								for (int l = 0; l < newuploadedkeys.length(); l++) {

									data_json_obj.put(newuploadedkeys.getString(l),
											newdatasrcobj.get(newuploadedkeys.getString(l)));
								}
							}
							dataarr.put(data_json_obj);
						}
						dependencyjson.put("data", dataarr);
						
						camp_details_doc.put("ActiveUsersCount", Integer.toString(dataarr.length()));
						collection.updateOne(updtfiltercount, new Document("$set", camp_details_doc), new UpdateOptions().upsert(true));
						//update trafic count
					
						new SaveFunnelDetails().getmailsentfortrafic(funnelName, newdocjsonobj.getString("CreatedBy"), collection, dataarr.length(),newdocjsonobj.getString("Campaign_id"));
					
					} catch (Exception e) {

//						out.println("exc= " + e);
					}

				} catch (Exception e) {
//					out.println("exccc = " + e);
				}
				out.println("dependencyjson= " + dependencyjson);
				String apiurl = ResourceBundle.getBundle("config").getString("doctigerapi");

				out.println("apiurl= " + apiurl);
				int response = CallPostService.callPostAPIJSON(apiurl, dependencyjson, resps);
				if (response == 200) {

					try {

						out.println("updated= = " + response);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}

			// remove othercategory collection
			OtherCategoryMailsData.deleteMany(new Document());

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			if (mongoClient != null) {
				mongoClient.close();
				mongoClient = null;
			}

		}
		return "success";
	}

	public static String AddContactsDetails(String CreatedBy, String funnelName, String group, String category,
			JSONArray contactsarr, SlingHttpServletResponse res) throws IOException {
		PrintWriter out = res.getWriter();
		MongoClient mongoClient = null;
		MongoDatabase database = null;
		MongoCollection<Document> collection = null;

		BasicDBList productList = new BasicDBList();
		try {
			mongoClient = ConnectionHelper.getConnection();
			database = mongoClient.getDatabase("salesautoconvert");
			collection = database.getCollection("FirstCategoryMails");
			// JSONObject data_json_obj=null;
			BasicDBObject querypull = new BasicDBObject();
			// "updateflag", "1""Campaign_id" CreatedBy group

			querypull.put("Category", "Explore");
			querypull.put("CreatedBy", CreatedBy);
			querypull.put("group", group);
			querypull.put("funnelName", funnelName);
			Document datsrc_doc = new Document();
			datsrc_doc.put("rawleads", contactsarr.length());

			collection.updateOne(querypull, new Document("$set", datsrc_doc), new UpdateOptions().upsert(true));

			if ((contactsarr.length() > 0)) {
				for (int i = 0; i < contactsarr.length(); i++) {

					JSONObject data_json_obj = contactsarr.getJSONObject(i);
					BasicDBObject dbObject = (BasicDBObject) JSON.parse(data_json_obj.toString());
					// out.println("dbObject ="+dbObject);

					BasicDBObject updateQuery = new BasicDBObject("$addToSet", new BasicDBObject("Contacts", dbObject));
					collection.updateOne(querypull, updateQuery, new UpdateOptions().upsert(true));

				}
			}
			// out.println("productList ="+productList);

			// BasicDBObject listItem = new BasicDBObject("tempObjData",arrdata);new
			// BasicDBObject("simulatedProduct", productList)
//				BasicDBObject updateQuery = new BasicDBObject("$push", new BasicDBObject("Contacts", productList));
//				collection.updateOne(querypull, updateQuery,new UpdateOptions().upsert(true));

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			if (mongoClient != null) {
				mongoClient.close();
				mongoClient = null;
			}

		}
		return "success";
	}

	public JSONArray getjsonKey(JSONObject data_json_obj) {
		Iterator<String> keysItr = data_json_obj.keys();
		String key = null;
		JSONArray keysarr = new JSONArray();

		while (keysItr.hasNext()) {
			key = keysItr.next();

			keysarr.put(key);

		}
		return keysarr;
	}

	public static String findMultipleCampaigns(SlingHttpServletResponse resps, Node massmailratenode, Session session)
			throws JSONException, IOException {
		String resp = "";// ,SlingHttpServletResponse res
		PrintWriter out = resps.getWriter();
		MongoClient mongoClient = null;
		MongoClientURI connectionString = null;

		MongoDatabase database = null;
		MongoCollection<Document> OtherCategoryMailsData = null;
		MongoCursor<Document> subcursor = null;
		MongoCursor<Document> monitordata = null;
//		BasicDBList productList = null;
		String mailtemp = null;
		String group = null;
		String email = null;
		JSONObject dependencyjson = null;
		JSONArray multidrpdown = new JSONArray();
		multidrpdown.put("1");
//		multidrpdown.put("2");
		multidrpdown.put("3");
		boolean existflag = false;
		int rate = 0;
		try {
			Date d = new Date();
			SimpleDateFormat formatter2 = new SimpleDateFormat("dd-MM-yy HH:mm");
			String formatdatetomatch = formatter2.format(d);

			// System.out.println(selday.toString());
			StringBuilder hhmmformat = new StringBuilder(new SimpleDateFormat("HH:mm").format(d));
			// mongoClient = ConnectionHelper.getConnection();

			System.setProperty("javax.net.ssl.trustStore", "/etc/ssl/firstTrustStore");
			System.setProperty("javax.net.ssl.trustStorePassword", "bizlem123");
			System.setProperty("javax.net.ssl.keyStore", "/etc/ssl/MongoClientKeyCert.jks");
			System.setProperty("javax.net.ssl.keyStorePassword", "bizlem123");
			//// MongoClientOptions.builder().sslEnabled(true).sslInvalidHostNameAllowed(true).build();
			String uri = "mongodb://localhost:27017/?ssl=true";
			connectionString = new MongoClientURI(uri);
			mongoClient = new MongoClient(connectionString);
			database = mongoClient.getDatabase("salesautoconvert");

			OtherCategoryMailsData = database.getCollection("FirstCategoryMails");
//"scheduleday"
			resp = "start";
			Bson filter2 = null;
			// filter2 = and( eq("updateflag", "1"), eq("scheduleTime",
			// hhmmformat.toString()));// ,
			filter2 = and(eq("ScheduleGapDate", formatdatetomatch), eq("multipleflag", "5"));// ,
			// eq("scheduleTime",
			// hhmmformat.toString())
			// out.println("hhmmformat is = "+hhmmformat+" ::selday.toString() =
			// "+selday.toString());
			Iterator datasrcitr = null;
			Iterator uploadeddatasrcitr = null;
			FindIterable<Document> filerdata = OtherCategoryMailsData.find(filter2);
			monitordata = filerdata.iterator();
			LogByFileWriter
					.logger_info_MultipleMails(d + "===================================== findMultipleCampaigns : "); // hhmmformat.toString())
			LogByFileWriter.logger_info_MultipleMails(d + " filter2 : " + filter2); // hhmmformat.toString())
			Document datsrc_doc = null;
			Document uploddatsrc_doc = null;
			JSONArray uploadedkeys = null;
			// JSONArray hash_Setstatickeys=new JSONArray();
			JSONObject statickeysobj = new JSONObject();

			// Set<String> hash_Setstatickeys = new HashSet<String>();
			statickeysobj.put("Email", "");
			statickeysobj.put("FirstName", "");
			statickeysobj.put("LastName", "");
			statickeysobj.put("PhoneNumber", "");
			statickeysobj.put("Country", "");
			statickeysobj.put("CompanyName", "");
			statickeysobj.put("CompanyHeadCount", "");
			statickeysobj.put("Industry", "");
			statickeysobj.put("Institute", "");
			statickeysobj.put("Source", "");
			if (monitordata.hasNext()) {
				while (monitordata.hasNext()) {

					JSONObject newdatasrcobj = new JSONObject();

					// out.println("in exploreWisedata =-====================== ");

					dependencyjson = new JSONObject();
					Document exploreWisedata = monitordata.next();
					JSONObject newdocumntjsonobj = new JSONObject(exploreWisedata.toJson());
					LogByFileWriter
							.logger_info_MultipleMails(d + " exploreWisedata.toJson() : " + exploreWisedata.toJson());

//				productList = null;
//			out.println("in exploreWisedata = "+exploreWisedata);
					try {

						mailtemp = exploreWisedata.getString("Campaign_id");
						group = exploreWisedata.getString("group");
						email = exploreWisedata.getString("CreatedBy");

//					JSONObject rateobj = new CallGetService().getmassMailType(email, group);
//					if (rateobj.has("AccountType") && rateobj.get("AccountType").equals("Normal")) {
//						if (!massmailratenode.hasProperty("Normal")) {
//							massmailratenode.setProperty("Normal", "75");
//						} else {
//
//						}
//
//						rate = Integer.parseInt(massmailratenode.getProperty("Normal").getString());
//
//					} else {
//						if (!massmailratenode.hasProperty("MassMailer")) {
//							massmailratenode.setProperty("MassMailer", "80");
//						}
//						rate = Integer.parseInt(massmailratenode.getProperty("MassMailer").getString());
//					}

						session.save();

						// MassMailer

						JSONObject fetchPrimaryKey = new JSONObject();
						fetchPrimaryKey.put("email", email);
						fetchPrimaryKey.put("group", group);
						fetchPrimaryKey.put("MailTempName", mailtemp);

						dependencyjson.put("MailTempName", mailtemp);
						dependencyjson.put("templateName", "");
						dependencyjson.put("typeDataSource", "Enter manually");
						dependencyjson.put("AttachtempalteType", "");
						dependencyjson.put("esignature", "false");
						dependencyjson.put("twofactor", "false");
						dependencyjson.put("esigntype", "");
						dependencyjson.put("Email", email);
						dependencyjson.put("group", group);
						dependencyjson.put("lgtype", "null");

						dependencyjson.put("multipeDropDown", multidrpdown);

						dependencyjson.put("Type", "Generation");
						String url = null;
						String response = null;
						String apiurl = null;
//					out.println("dependencyjson="+dependencyjson);

						try {
							// get uploaded datasource fields from datasource
							// JSONArray MoveEmailList = newdocumntjsonobj.getJSONArray("MoveEmailList");
							List<Document> uploadeddatasourcesarr = (List<Document>) exploreWisedata.get("Datasource");
//						out.println("uploadeddatasourcesarr.size="+uploadeddatasourcesarr.size());
							if (uploadeddatasourcesarr.size() > 0) {
								uploadeddatasrcitr = uploadeddatasourcesarr.listIterator();
								JSONObject uploddata_json_obj = null;
								while (uploadeddatasrcitr.hasNext()) {
									uploddatsrc_doc = (Document) uploadeddatasrcitr.next();
									out.println("uploddatsrc_doc = " + uploddatsrc_doc);
									uploddata_json_obj = new JSONObject(uploddatsrc_doc.toJson());
									uploadedkeys = new SaveFunnelDetails().getjsonKey(uploddata_json_obj);
									break;
								}
								out.println("uploddata_json_obj =" + uploddata_json_obj.toString());
								if (uploddata_json_obj != null) {
									for (int k = 0; k < uploadedkeys.length(); k++) {
										if (!statickeysobj.has(uploadedkeys.getString(k))) {
//							newdatasrcobj=new JSONObject();
											newdatasrcobj.put(uploadedkeys.getString(k),
													uploddata_json_obj.get(uploadedkeys.getString(k)));
										} else {
//							out.println("else newdatasrcobj ="+newdatasrcobj);
										}
									}
								}
								// Contacts

							} else {
//							out.println("else uploadeddatasourcesarr.size="+uploadeddatasourcesarr.size());
							}
							out.println("newdatasrcobj =" + newdatasrcobj);
							JSONObject data_json_obj = null;
							// get contacts from explore funnel
							List<Document> datasourcesarr = new SaveFunnelDetails().getContactarrofstartcatg("Explore",
									"SentExploreContacts", exploreWisedata.getString("CreatedBy"),
									exploreWisedata.getString("Parentfunnel"), resps);
							// (List<Document>) exploreWisedata.get("Contacts");
							if (datasourcesarr.isEmpty()) {
								// System.out.println("No data found");
								// update flag to 2

								BasicDBObject querypull = new BasicDBObject();
								// "updateflag", "1""Campaign_id" CreatedBy group
								querypull.put("Category", "Explore");

								querypull.put("Campaign_id", mailtemp);
								querypull.put("CreatedBy", email);
								querypull.put("group", group);
								querypull.put("funnelName", exploreWisedata.getString("funnelName"));

								BasicDBObject newDocument = new BasicDBObject();
								newDocument.put("multipleflag", "6");

								BasicDBObject updateObj = new BasicDBObject();
								updateObj.put("$set", newDocument);

								OtherCategoryMailsData.updateOne(querypull, updateObj);
								break;
							} else {
//								out.println("else 411 ="+datasourcesarr.size());
							}

							JSONArray dataarr = new JSONArray();
//						productList = new BasicDBList();
							datasrcitr = datasourcesarr.listIterator();
							int count = 0;
							url = ResourceBundle.getBundle("config").getString("mailtemplatekey");
							// response = BizUtil.callPostAPIJSON(url, fetchPrimaryKey);
//						out.println("url response = "+response);
//						if (!BizUtil.isNullString(response)) {
							// JSONObject outresponse = new JSONObject(response);

							while (datasrcitr.hasNext()) {
								datsrc_doc = (Document) datasrcitr.next();

								boolean emailmovedOrNot = new SaveFunnelDetails().getmovedemailflag(
										exploreWisedata.getString("CreatedBy"), datsrc_doc.getString("Email"),
										exploreWisedata.getString("Category"), exploreWisedata.getString("funnelName"),
										exploreWisedata.getString("Campaign_id"), resps);
								LogByFileWriter
										.logger_info_MultipleMails(d + "emailmovedOrNot flag: " + emailmovedOrNot);
								if (!emailmovedOrNot) {
									data_json_obj = new JSONObject(datsrc_doc.toJson());
								} else {
									break;
								}

//								if (count < rate) {

//								out.println("dataarr= "+dataarr);
								// productList.add(dbObject);
								// add in sentcontactsExplore

								// add in sentexplorecontact

								// BasicDBObject dbObject = (BasicDBObject)
								// JSON.parse(data_json_obj.toString());

								if (newdatasrcobj.length() > 0) {
									JSONArray newuploadedkeys = new SaveFunnelDetails().getjsonKey(newdatasrcobj);
//									out.println("newuploadedkeys= "+newuploadedkeys);
									for (int l = 0; l < newuploadedkeys.length(); l++) {

										data_json_obj.put(newuploadedkeys.getString(l),
												newdatasrcobj.get(newuploadedkeys.getString(l)));
									}
								}
//										data_json_obj.p
//										dataarr.put(newdatasrcobj);

								JSONObject bouncejs = new SaveFunnelDetails()
										.getBouncedMailFlag(data_json_obj.get("Email").toString(), resps);
								LogByFileWriter.logger_info_MultipleMails(
										d + "data_json_obj" + data_json_obj + "::: bouncejs  : " + bouncejs);
								if (bouncejs.has("flag") && bouncejs.get("flag").equals("true")) {
									dataarr.put(data_json_obj);
								}

//									if(!data_json_obj.get("Email").equals("")) {
//									dataarr.put(data_json_obj);
//									}

								if (datasourcesarr.size() == 1) {
//										StringBuilder cuurTime = updateScheduleTime(
//												exploreWisedata.getString("scheduleTime"));
//
//										BasicDBObject newDocument = new BasicDBObject();
//										newDocument.put("scheduleTime", cuurTime.toString());
//
//										BasicDBObject updateObj = new BasicDBObject();
//										updateObj.put("$set", newDocument);

								}

//								} 
								count++;

							}
							dependencyjson.put("data", dataarr);
							
							new SaveFunnelDetails().getmailsentfortrafic(exploreWisedata.getString("funnelName"), exploreWisedata.getString("CreatedBy"), OtherCategoryMailsData, dataarr.length(),exploreWisedata.getString("Campaign_id"));
							/*
							 * if(!(datasourcesarr.isEmpty())){ BasicDBObject querypull = new
							 * BasicDBObject(); //"updateflag", "1""Campaign_id" CreatedBy group
							 * querypull.put("Category", "Explore"); querypull.put("updateflag", "1");
							 * querypull.put("Campaign_id", mailtemp); querypull.put("CreatedBy", email);
							 * querypull.put("group", group); querypull.put("funnelName",
							 * exploreWisedata.getString("funnelName"));
							 * 
							 * // BasicDBObject listItem = new BasicDBObject("tempObjData",arrdata);new
							 * BasicDBObject("simulatedProduct", productList) BasicDBObject updateQuery =
							 * new BasicDBObject("$push", new BasicDBObject("OriginalDataSource",
							 * productList)); OtherCategoryMailsData.updateOne(querypull, updateQuery,new
							 * UpdateOptions().upsert(true)); }
							 */
							apiurl = ResourceBundle.getBundle("config").getString("doctigerapi");

							// out.println("apiurl= " + apiurl);
							out.println("dependencyjson : " + dependencyjson);
							LogByFileWriter.logger_info_MultipleMails(d + " dependencyjson : " + dependencyjson); // hhmmformat.toString())
							int res = CallPostService.callPostAPIJSON(apiurl, dependencyjson, resps);

							if (res == 200) {
//								System.out.println("No data found");
								// update flag to 2
								try {
									BasicDBObject newDocument = new BasicDBObject();
									newDocument.put("multipleflag", "6");
									Bson searchQuery = new Document().append("CreatedBy", email)
											.append("funnelName", exploreWisedata.getString("funnelName"))
											.append("Category", exploreWisedata.getString("Category"))
											.append("Campaign_id", mailtemp);
									OtherCategoryMailsData.updateOne(searchQuery, new Document("$set", newDocument),
											new UpdateOptions().upsert(true));

//	break;	 
								} catch (Exception e) {
									// TODO: handle exception
								}
							}

//						}

						} catch (Exception e) {

							e.printStackTrace();
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
					// out.println("dependencyjson= " + dependencyjson);

				}
			} else {
				LogByFileWriter.logger_info_MultipleMails(d + " No record found");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (monitordata != null) {
				monitordata.close();
				monitordata = null;
			}

			if (mongoClient != null) {
				mongoClient.close();
				mongoClient = null;
			}

		}
		return "success";
	}

	public String getmainscheduletime(String category, String CreatedBy, String mainfunnel,
			SlingHttpServletResponse res) throws IOException {
		// update subFunnelCampCount in main funnel

		MongoClient mongoClient = null;
		MongoDatabase database = null;
		MongoCollection<Document> collection = null;
		String schedulegap = null;
		MongoCursor<Document> monitordata = null;
		PrintWriter out = res.getWriter();
		String scheduletime = null;
		try {

			mongoClient = ConnectionHelper.getConnection();
			database = mongoClient.getDatabase("salesautoconvert");
			collection = database.getCollection("FirstCategoryMails");
			if (mainfunnel != null) {
				Bson updtfiltercount = and(eq("Category", category), eq("funnelName", mainfunnel),
						eq("CreatedBy", CreatedBy));

				FindIterable<Document> filerdata = collection.find(updtfiltercount);
				monitordata = filerdata.iterator();

				Document datsrc_doc = null;
				if (mainfunnel != null && mainfunnel != "") {
					while (monitordata.hasNext()) {
						// out.println("in newsubfuncount =");
						datsrc_doc = monitordata.next();
						scheduletime = datsrc_doc.getString("scheduleTime");
						break;
					}
				}

//		Document subfunnelcountdoc = new Document();
//		subfunnelcountdoc.put("subFunnelCampCount", newsubfuncount);
//		 out.println("subfunnelcountdoc ="+subfunnelcountdoc);
//		collection.updateOne(updtfiltercount, new Document("$set", subfunnelcountdoc), new UpdateOptions().upsert(true));
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		return scheduletime;
	}

	public List<Document> getContactarrofstartcatg(String category, String arrayname, String CreatedBy,
			String mainfunnel, SlingHttpServletResponse res) throws IOException {
		// update subFunnelCampCount in main funnel

		MongoClient mongoClient = null;
		MongoDatabase database = null;
		MongoCollection<Document> collection = null;
		String schedulegap = null;
		MongoCursor<Document> monitordata = null;
		PrintWriter out = res.getWriter();
		String scheduletime = null;
		List<Document> contactarr = null;
		try {

			// mongoClient = ConnectionHelper.getConnection();
			MongoClientURI connectionString = null;
			System.setProperty("javax.net.ssl.trustStore", "/etc/ssl/firstTrustStore");
			System.setProperty("javax.net.ssl.trustStorePassword", "bizlem123");
			System.setProperty("javax.net.ssl.keyStore", "/etc/ssl/MongoClientKeyCert.jks");
			System.setProperty("javax.net.ssl.keyStorePassword", "bizlem123");
			//// MongoClientOptions.builder().sslEnabled(true).sslInvalidHostNameAllowed(true).build();
			String uri = "mongodb://localhost:27017/?ssl=true";
			connectionString = new MongoClientURI(uri);
			mongoClient = new MongoClient(connectionString);
			database = mongoClient.getDatabase("salesautoconvert");
			collection = database.getCollection("FirstCategoryMails");
			if (mainfunnel != null) {
				Bson updtfiltercount = and(eq("Category", category), eq("funnelName", mainfunnel),
						eq("CreatedBy", CreatedBy));

				FindIterable<Document> filerdata = collection.find(updtfiltercount);
				monitordata = filerdata.iterator();

				Document datsrc_doc = null;

				if (mainfunnel != null && mainfunnel != "") {
					while (monitordata.hasNext()) {
						// out.println("in newsubfuncount =");
						datsrc_doc = monitordata.next();
						contactarr = (List<Document>) datsrc_doc.get(arrayname);
						break;
					}
				}
				if (monitordata != null) {
					monitordata.close();
					monitordata = null;
				}

//		Document subfunnelcountdoc = new Document();
//		subfunnelcountdoc.put("subFunnelCampCount", newsubfuncount);
//		 out.println("subfunnelcountdoc ="+subfunnelcountdoc);
//		collection.updateOne(updtfiltercount, new Document("$set", subfunnelcountdoc), new UpdateOptions().upsert(true));
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			if (null != mongoClient) {
				mongoClient.close();
				mongoClient = null;
			}

		}
		return contactarr;
	}

	public JSONObject getalldocument(String category, String CreatedBy, String mainfunnel, SlingHttpServletResponse res)
			throws IOException, JSONException {
		// update subFunnelCampCount in main funnel
		String docobj = null;
		MongoClient mongoClient = null;
		MongoDatabase database = null;
		MongoCollection<Document> collection = null;
		String schedulegap = null;
		MongoCursor<Document> monitordata = null;
		PrintWriter out = res.getWriter();
		String scheduletime = null;
		List<Document> contactarr = null;
		try {

			mongoClient = ConnectionHelper.getConnection();
			database = mongoClient.getDatabase("salesautoconvert");
			collection = database.getCollection("FirstCategoryMails");
			if (mainfunnel != null) {
				Bson updtfiltercount = and(eq("Category", "Explore"), eq("funnelName", mainfunnel),
						eq("CreatedBy", CreatedBy));

				FindIterable<Document> filerdata = collection.find(updtfiltercount);
				monitordata = filerdata.iterator();

				Document datsrc_doc = null;

				if (mainfunnel != null && mainfunnel != "") {
					while (monitordata.hasNext()) {
						// out.println("in newsubfuncount =");
						datsrc_doc = monitordata.next();
//			out.println("datsrc_doc ="+datsrc_doc);
						docobj = datsrc_doc.toJson();
						break;
					}
				}

//		Document subfunnelcountdoc = new Document();
//		subfunnelcountdoc.put("subFunnelCampCount", newsubfuncount);
//		 out.println("subfunnelcountdoc ="+subfunnelcountdoc);
//		collection.updateOne(updtfiltercount, new Document("$set", subfunnelcountdoc), new UpdateOptions().upsert(true));
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		JSONObject newjs = new JSONObject(docobj);

		return newjs;
	}

	public static String updateBouncedEmail(JSONArray contactsarr, SlingHttpServletResponse res) {

		try {
			PrintWriter out = res.getWriter();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		MongoClient mongoClient = null;
		MongoDatabase database = null;
		MongoCollection<Document> collection = null;

		try {
			mongoClient = ConnectionHelper.getConnection();
			database = mongoClient.getDatabase("salesautoconvert");
			collection = database.getCollection("BouncedEmail");
			// JSONObject data_json_obj=null;
			BasicDBObject querypull = null;
			JSONObject data_json_obj = null;
			Document doc = null;
			String email = null;
			String status = null;
			if ((contactsarr.length() > 0)) {
				for (int i = 0; i < contactsarr.length(); i++) {
					try {
						doc = new Document();
						data_json_obj = contactsarr.getJSONObject(i);
						email = data_json_obj.get("Email").toString().trim();

						Bson updtfilter = and(eq("Email", email));
						doc.put("Email", email);
						if (data_json_obj.has("Status") && data_json_obj.get("Status") != "") {
							doc.put("Status", data_json_obj.get("Status").toString());
						} else {
							doc.put("Status", "NA");
						}
						collection.updateOne(updtfilter, new Document("$set", doc), new UpdateOptions().upsert(true));

					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			if (mongoClient != null) {
				mongoClient.close();
				mongoClient = null;
			}

		}
		return "success";
	}

	public JSONObject getBouncedMailFlag(String email, SlingHttpServletResponse res) {
		// update subFunnelCampCount in main funnel

		MongoClient mongoClient = null;
		MongoDatabase database = null;
		MongoCollection<Document> collection = null;
		MongoClientURI connectionString = null;
		JSONObject newobj = new JSONObject();
		MongoCursor<Document> monitordata = null;
		try {
			PrintWriter out = res.getWriter();

			System.setProperty("javax.net.ssl.trustStore", "/etc/ssl/firstTrustStore");
			System.setProperty("javax.net.ssl.trustStorePassword", "bizlem123");
			System.setProperty("javax.net.ssl.keyStore", "/etc/ssl/MongoClientKeyCert.jks");
			System.setProperty("javax.net.ssl.keyStorePassword", "bizlem123");
			//// MongoClientOptions.builder().sslEnabled(true).sslInvalidHostNameAllowed(true).build();
			String uri = "mongodb://localhost:27017/?ssl=true";
			connectionString = new MongoClientURI(uri);
			mongoClient = new MongoClient(connectionString);
			// mongoClient = ConnectionHelper.getConnection();
			database = mongoClient.getDatabase("salesautoconvert");
			collection = database.getCollection("BouncedEmail");
			if (email != null && email != "") {
				Bson updtfiltercount = and(eq("Email", email));

				FindIterable<Document> filerdata = collection.find(updtfiltercount);
				monitordata = filerdata.iterator();

				Document datsrc_doc = null;

				if (monitordata.hasNext()) {
					while (monitordata.hasNext()) {

						datsrc_doc = monitordata.next();
//			out.println("datsrc_doc ="+datsrc_doc);
						// docobj = datsrc_doc.toJson();
						newobj.put("Email", datsrc_doc.get("Email").toString());
						newobj.put("Status", datsrc_doc.get("Status").toString());
						newobj.put("flag", "false");
						break;
					}

				} else {
					newobj.put("Email", email);
					newobj.put("Status", "NotBouncedYet");
					newobj.put("flag", "true");
				}

			}
			if (monitordata != null) {
				monitordata.close();
				monitordata = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != mongoClient) {
				mongoClient.close();
				mongoClient = null;
			}

		}

		return newobj;
	}

	public boolean getmovedemailflag(String CreatedBy, String email, String category, String funnelName,
			String campaignid, SlingHttpServletResponse res) {
		// update subFunnelCampCount in main funnel

		MongoClient mongoClient = null;
		MongoDatabase database = null;
		MongoCollection<Document> collection = null;
		Boolean mailflag = false;

		MongoCursor<Document> monitordata = null;
		Bson filter1 = null;
		try {
			MongoClientURI connectionString = null;
			System.setProperty("javax.net.ssl.trustStore", "/etc/ssl/firstTrustStore");
			System.setProperty("javax.net.ssl.trustStorePassword", "bizlem123");
			System.setProperty("javax.net.ssl.keyStore", "/etc/ssl/MongoClientKeyCert.jks");
			System.setProperty("javax.net.ssl.keyStorePassword", "bizlem123");
			//// MongoClientOptions.builder().sslEnabled(true).sslInvalidHostNameAllowed(true).build();
			String uri = "mongodb://localhost:27017/?ssl=true";
			connectionString = new MongoClientURI(uri);
			mongoClient = new MongoClient(connectionString);

			// mongoClient = ConnectionHelper.getConnection();
			database = mongoClient.getDatabase("salesautoconvert");
			collection = database.getCollection("BouncedEmail");
			if (email != null && email != "") {
				filter1 = and(eq("CreatedBy", CreatedBy), eq("Campaign_id", campaignid), eq("funnelName", funnelName),
						eq("Category", category));

				FindIterable<Document> filerdata = collection.find(filter1);
				monitordata = filerdata.iterator();

				Document datsrc_doc = null;

				if (monitordata.hasNext()) {
					while (monitordata.hasNext()) {

						datsrc_doc = monitordata.next();
						mailflag = true;
//			out.println("datsrc_doc ="+datsrc_doc);
						// docobj = datsrc_doc.toJson();
						break;
					}

				} else {

				}

			}
			if (null != monitordata) {
				monitordata.close();
				monitordata = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != mongoClient) {
				mongoClient.close();
				mongoClient = null;
			}

		}

		return mailflag;
	}
	
	public int getmailsentfortrafic(String mainfunnel,String CreatedBy, MongoCollection<Document> collection,int mailsentcount,String mailtemp) {
		try {
			// update trafic all mail sent count
			Bson updtfiltercount = and( eq("funnelName", mainfunnel), eq("CreatedBy", CreatedBy), eq("Campaign_id", mailtemp));
			MongoCursor<Document> monitordata = null;
			FindIterable<Document> filerdata = collection.find(updtfiltercount);
			monitordata = filerdata.iterator();

			Document datsrc_doc = null;
			String Trafic=null;
			String newmailsent=null;
			
			
			 int newmailcount=0;
			while (monitordata.hasNext()) {
				 
				datsrc_doc=monitordata.next();
				if(datsrc_doc.containsKey("Trafic")) {
					Trafic = datsrc_doc.getString("Trafic");
				//  out.println("in old subfuncount ="+subfuncount);
					newmailcount=Integer.parseInt(Trafic)+mailsentcount;
			  
				}else {
					newmailcount=mailsentcount;
				}
				newmailsent=Integer.toString(newmailcount);
			 break;
			}
			
			Document subfunnelcountdoc = new Document();
			subfunnelcountdoc.put("Trafic", newmailsent);
			
			collection.updateOne(updtfiltercount, new Document("$set", subfunnelcountdoc), new UpdateOptions().upsert(true));
			}catch (Exception e) {
				// TODO: handle exception
			}
		
	return	0;
	}
	
	public static String updateScheduletime(String xlsjson, String CreatedBy, String funnelName) {

		// insert datasource array to FirstCategoryMails

		MongoClient mongoClient = null;
		MongoDatabase database = null;
		MongoCollection<Document> collection = null;
		Bson document = null;
		try {
			mongoClient = ConnectionHelper.getConnection();
			database = mongoClient.getDatabase("salesautoconvert");

			collection = database.getCollection("FirstCategoryMails");
			document = Document.parse(xlsjson);

			Bson searchQuery = new Document().append("CreatedBy", CreatedBy).append("funnelName", funnelName)
					.append("Category", "Explore");
			collection.updateOne(searchQuery, new Document("$set", document), new UpdateOptions().upsert(true));
			// collection.updateOne(searchQuery, new Document("$addToSet", document), new
			// UpdateOptions().upsert(true));

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != mongoClient) {
				mongoClient.close();
				mongoClient = null;
			}

		}
		return "success";
	}

}
