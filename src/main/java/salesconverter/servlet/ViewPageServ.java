package salesconverter.servlet;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.jcr.LoginException;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.servlet.ServletException;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.jcr.api.SlingRepository;
import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import Dashboard.mongodbdata;
import salesconverter.create.rule.CreateRuleEngine;
import salesconverter.freetrail.FreetrialShoppingCartUpdate;
import salesconverter.mongo.FunnelDetailsMongoDAO;
import salesconverter.mongo.SaveFunnelDetails;
import services.CallPostService;
import services.CreateXLSFile;

@Component(immediate = true, metatype = false)
@Service(value = javax.servlet.Servlet.class)
@Properties({ @Property(name = "service.description", value = "Save product Servlet"),
		@Property(name = "service.vendor", value = "VISL Company"),
		@Property(name = "sling.servlet.paths", value = { "/servlet/service/ViewCmpaigns" }),
		@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
		@Property(name = "sling.servlet.extensions", value = { "hotproducts", "cat", "latestproducts", "brief",
				"prodlist", "catalog", "viewcart", "productslist", "addcart", "createproduct", "checkmodelno",
				"productEdit" }) })
@SuppressWarnings("serial")
public class ViewPageServ extends SlingAllMethodsServlet {

	@Reference
	private SlingRepository repo;
	final String FILEEXTENSION[] = { "csv" };

	final int NUMBEROFRESULTSPERPAGE = 10;
	private static final long serialVersionUID = 1L;
	String fileType = "file";
	JSONObject mainjsonobject = null;
//ViewCmpaigns.getcmp?funnel=fun2611&email=salesautoconvertuser1@gmail.com
	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out= response.getWriter();
	
		
	}
	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out= response.getWriter();
		//ViewCmpaigns.ViewPageDetails?email=salesautoconvertuser1@gmail.com&funnel=newfun24
		//ViewCmpaigns.SalesFunnelList?email=salesautoconvertuser1@gmail.com
		 if (request.getRequestPathInfo().getExtension().equals("ViewPageDetails")) {
				try {

				String email=request.getParameter("email");
				String funnel=request.getParameter("funnel");
								
				
				out.println(FunnelDetailsMongoDAO.ViewFunneldetails(email,funnel));
					
				} catch (Exception ex) {
					out.print(ex.getMessage());
				}
			} if (request.getRequestPathInfo().getExtension().equals("SalesFunnelList")) {
				try {

				String email=request.getParameter("email");
				
								
				
				out.println(FunnelDetailsMongoDAO.getFunnelList(email));
					
				} catch (Exception ex) {
					out.print(ex.getMessage());
				}
			}	  if(request.getRequestPathInfo().getExtension().equals("ClickedData")) {
				try {
					
					
				
				//remoteuser=viki@gmail.com&category=Explore&funnel=&campid=
				String remoteuser=request.getParameter("email");
				
				String resp = FunnelDetailsMongoDAO.findGAdatapersubscriber(remoteuser, request.getParameter("category"), request.getParameter("funnel"), request.getParameter("campid"),response);
				out.println(resp);

			
			}
			
		catch (Exception e) {

		System.out.println("Exception ex : "+e.getMessage());
		}
		}
			if(request.getRequestPathInfo().getExtension().equals("getcmp")) {
				JSONObject mainjson=new JSONObject();
				String funnelname=request.getParameter("funnel");
				String username=request.getParameter("email");
				JSONArray campaignFunneldata = new JSONArray();

				MongoClient mongoClient = null;
				MongoDatabase database = null;
				MongoCollection<Document> collection = null;
				MongoCursor<Document> monitordatahotleads = null;
				MongoCursor<Document> subcursor = null;
				JSONArray funnelLeadCategoryarr = new JSONArray();
				JSONObject newjs=null;
				Document campaign_list_doc = null;

			    Map <String,Integer> week1data = new HashMap<String,Integer>();
				
				try {
					mongoClient = new mongodbdata().getConnection();
					database = mongoClient.getDatabase("salesautoconvert");
					collection = database.getCollection("FirstCategoryMails");
				
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
						JSONObject frndjs=new JSONObject();
						JSONObject fbjs=new JSONObject();
						JSONObject lnkdjs=new JSONObject();
						JSONObject otherjs=new JSONObject();
						if (monitordatahotleads.hasNext()) {
							while (monitordatahotleads.hasNext()) {

								datsrc_doc2 = monitordatahotleads.next();

								newjs = new JSONObject(datsrc_doc2.toJson());
								out.println( "in newjs=== "+newjs);
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
													out.println( "in nfrnd== "+frnd);
												} else if (catedashboard[i].equals("Facebook")) {
													fb++;
													out.println( "in newjs=== "+fb);
												} else if (catedashboard[i].equals("Linkedin")) {
													lnkd++;
													out.println( "in newjs=== "+lnkd);
												} else {
													other++;
													out.println( "in newjs=== "+other);
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
								
								frndjs.put("source", "Friend");
								frndjs.put("leads", frnd);
								frndjs.put("rate", 0);
								
								fbjs.put("source", "Facebook");
								fbjs.put("leads", fb);
								fbjs.put("rate", 0);
								
								lnkdjs.put("source", "Linkedin");//linkedin
								lnkdjs.put("leads", lnkd);
								lnkdjs.put("rate", 0);
								
								otherjs.put("source", "Other");
								otherjs.put("leads", other);
								otherjs.put("rate", 0);
								
								break;
							}
						}
				
						campaignFunneldata.put(frndjs);
						campaignFunneldata.put(fbjs);
						campaignFunneldata.put(lnkdjs);
						campaignFunneldata.put(otherjs);

					} catch (Exception e) {
						e.printStackTrace();
					}
					
				out.println( "in Other weekjson all ======== "+campaignFunneldata);

					
				out.println("mainjson = "+mainjson);

				
					mainjson.put("funnelLeadCategory", funnelLeadCategoryarr);
					
				} catch (Exception e) {
					// TODO: handle exception
				}

			out.println("mainjson = "+mainjson);

			
				
		
			
			}
		
	}

	
}
