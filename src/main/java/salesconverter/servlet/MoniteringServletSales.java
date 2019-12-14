package salesconverter.servlet;


import static com.mongodb.client.model.Filters.eq;

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import jxl.*;
import jxl.write.*;
import jxl.write.biff.RowsExceededException;
import salesconverter.mongo.ConnectionHelper;
import salesconverter.mongo.FunnelDetailsMongoDAO;
import salesconverter.mongo.GetDataMongo;
import salesconverter.mongo.GetGADataForMonitoringLeads;

import javax.jcr.LoginException;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PropertyIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.Value;
import javax.jcr.Workspace;
import javax.jcr.query.Query;
import javax.jcr.query.QueryResult;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestParameter;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.jcr.api.SlingRepository;
import org.bson.conversions.Bson;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.osgi.service.http.HttpService;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.client.DistinctIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;


@Component(immediate = true, metatype = false)
@Service(value = javax.servlet.Servlet.class)
@Properties({ @Property(name = "service.description", value = "Save product Servlet"),
		@Property(name = "service.vendor", value = "VISL Company"),
		@Property(name = "sling.servlet.paths", value = { "/servlet/service/Moniteringui" }),
		@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
		@Property(name = "sling.servlet.extensions", value = { "hotproducts", "cat", "latestproducts", "brief",
				"prodlist", "catalog", "viewcart", "productslist", "addcart", "createproduct", "checkmodelno",
				"productEdit" }) })
@SuppressWarnings("serial")
public class MoniteringServletSales extends SlingAllMethodsServlet {

	@Reference
	private SlingRepository repo;
	final String FILEEXTENSION[] = { "csv" };

	final int NUMBEROFRESULTSPERPAGE = 10;
	private static final long serialVersionUID = 1L;
	String fileType = "file";
	JSONObject mainjsonobject = null;
	Session session = null;
	String slingip = ResourceBundle.getBundle("config").getString("sling_ip");
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		//  http://development.bizlem.io:8082/portal/servlet/service/Moniteringui?email=viki_gmail.com&funnelName=today302019&SubFunnelName=Entice
		// http://development.bizlem.io:8082/portal/servlet/service/Moniteringui.SubscriberData
		//http://development.bizlem.io:8082/portal/servlet/service/ui.shopping45?email=viki@gmail.comcat=Explore&fun=today302019&campid=1628
	
		out.println("GET Moniteringui");
		org.apache.sling.commons.json.JSONObject noofleadjs;
		try {
//			noofleadjs = GetGADataForMonitoringLeads.GettotalLeadsbyCat(request.getParameter("email"), request.getParameter("funnelName"),request.getParameter("SubFunnelName"),response);
//			out.println(noofleadjs.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	@Override
	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		if (request.getRequestPathInfo().getExtension().equals("CampaignList")) {
		PrintWriter out=response.getWriter();
		String subfunnelname=request.getParameter("Subfunnelname");
		String username=request.getParameter("username");
		String funnelname=request.getParameter("funnelnamemain");
		org.apache.sling.commons.json.JSONObject funnelListJson =GetDataMongo.GetCampaignID(username, funnelname, subfunnelname);
		
//		org.apache.sling.commons.json.JSONObject funnellist=GetDataMongo.campaignlist(username,funnelname,subfunnelname);
		out.println(funnelListJson);
		

		
		}else if (request.getRequestPathInfo().getExtension().equals("SubscriberData")) {
			
			PrintWriter out=response.getWriter();
			String seletedsubfunnel=request.getParameter("subfunnelname");
			String username=request.getParameter("username");
			String funnelname=request.getParameter("funnelname");
			
			 
		    String allsubdata=null;;
			try {
				allsubdata = GetGADataForMonitoringLeads.moveuidata(username,seletedsubfunnel, funnelname,response);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//String servletre=GetDataMongo.uploadToServer("http://"+slingip+":8082/portal/servlet/service/ui.movedata", datajson);
			out.println(allsubdata);
			
			}else if (request.getRequestPathInfo().getExtension().equals("FunnelList")) {

				PrintWriter out=response.getWriter();
				String selectfunnel=request.getParameter("selectedfunnel");
				String email=request.getParameter("email");
				
				
				
				JSONObject datajson = new JSONObject();
			
				ArrayList<String> funnellist=FunnelDetailsMongoDAO.getcategorybyfunnel(email, selectfunnel);
				datajson.put("FunnelList", funnellist);
				datajson.put("username", email);
				datajson.put("FunnelName", selectfunnel);
				
				
			//	System.out.println(selectfunnel);
//				org.apache.sling.commons.json.JSONObject funnellist=FunnelDetailsMongoDAO.getFunnelList(email,selectfunnel);
//				out.println(funnellist);
				
				
				}
		
			else if (request.getRequestPathInfo().getExtension().equals("MoveSubfunnel")) {
		PrintWriter out=response.getWriter();
	//	String Subfunnelname=request.getParameter("Subfunnelname");
		//String username=request.getParameter("username");
//		String campid=request.getParameter("campid");	
//		String subid=request.getParameter("subid");	
//			String subinsidemove=request.getParameter("subinsidemove");	
		
		
		String subemail=request.getParameter("subemail");
		
		
		String username=request.getParameter("username");
		String movetofunnel=request.getParameter("insidemainfunnel");	
		 out.println(FunnelDetailsMongoDAO.moveContacts(movetofunnel, subemail,username,response));
		
		
//		JSONObject sendjson=new JSONObject();
//		sendjson.put("CampaignId", campid);
//		sendjson.put("SubscriberId", subid);
//		sendjson.put("FunnelName", insidemainfunnel);
//		sendjson.put("Category", subinsidemove);
//		sendjson.put("SubscriberEmail", subemail);
//		sendjson.put("SubFunnelName", subinsidemove);
//		sendjson.put("Output", subinsidemove);
//		sendjson.put("CreatedBy", username);
//		
//		System.out.println("Sendjson : "+sendjson);
//		String serverreturn =GetDataMongo.uploadToServer("http://"+slingip+":8082/portal/servlet/service/subscribermanger.list_subscriber_move_rulengine",sendjson);
//	    out.println("Server Return : "+serverreturn);
	
		//FunnelList
			}
		
	}
}
