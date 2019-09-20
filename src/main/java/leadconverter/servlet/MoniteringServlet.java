package leadconverter.servlet;

import leadconverter.mongo.GetGADataForMonitoringLeads;
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
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;

import leadconverter.freetrail.FreetrialShoppingCartUpdate;
import leadconverter.freetrail.GetValidityInfoFromEmail;
import leadconverter.mongo.ConnectionHelper;
import leadconverter.mongo.FunnelDetailsMongoDAO;
import leadconverter.mongo.GetDataMongo;
import leadconverter.mongo.MongoDAO;

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
public class MoniteringServlet extends SlingAllMethodsServlet {

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
		String username=request.getParameter("username").replace("@", "_");
		String funnelname=request.getParameter("funnelnamemain");

		org.apache.sling.commons.json.JSONObject funnellist=GetDataMongo.campaignlist(username,funnelname,subfunnelname);
		out.println(funnellist);
		}else if (request.getRequestPathInfo().getExtension().equals("SubscriberData")) {
			
			PrintWriter out=response.getWriter();
			String seletedfunnel=request.getParameter("subfunnelname");
			String username=request.getParameter("username").replace("@", "_");
			String funnelname=request.getParameter("funnelname");
			JSONObject datajson = new JSONObject();
			ArrayList<String> funnellist=new ArrayList<String>();
		    funnellist.add(seletedfunnel);
		    datajson.put("FunnelList", funnellist);
		    datajson.put("username", username);
		    datajson.put("FunnelName", funnelname);

			//sling_ip
			String servletre=GetDataMongo.uploadToServer("http://"+slingip+":8082/portal/servlet/service/ui.movedata", datajson);
			out.println(servletre);
			
			}else if (request.getRequestPathInfo().getExtension().equals("FunnelList")) {

				PrintWriter out=response.getWriter();
				String selectfunnel=request.getParameter("selectedfunnel");
				String email=request.getParameter("email").replace("@", "_");
				System.out.println(selectfunnel);
				org.apache.sling.commons.json.JSONObject funnellist=GetDataMongo.phplistdata(email,selectfunnel);
				out.println(funnellist);
				
				
				}
		
			else if (request.getRequestPathInfo().getExtension().equals("MoveSubfunnel")) {
		PrintWriter out=response.getWriter();
	//	String Subfunnelname=request.getParameter("Subfunnelname");
		//String username=request.getParameter("username");
		String campid=request.getParameter("campid");	
		String subid=request.getParameter("subid");	
		String insidemainfunnel=request.getParameter("insidemainfunnel");	
		String subinsidemove=request.getParameter("subinsidemove");	
		String subemail=request.getParameter("subemail");
		String username=request.getParameter("username").replace("@", "_");
	
		JSONObject sendjson=new JSONObject();
		sendjson.put("CampaignId", campid);
		sendjson.put("SubscriberId", subid);
		sendjson.put("FunnelName", insidemainfunnel);
		sendjson.put("Category", subinsidemove);
		sendjson.put("SubscriberEmail", subemail);
		sendjson.put("SubFunnelName", subinsidemove);
		sendjson.put("Output", subinsidemove);
		sendjson.put("CreatedBy", username);
		
		System.out.println("Sendjson : "+sendjson);
		String serverreturn =GetDataMongo.uploadToServer("http://"+slingip+":8082/portal/servlet/service/subscribermanger.list_subscriber_move_rulengine",sendjson);
	out.println("Server Return : "+serverreturn);
	
		//FunnelList
			}
		
	}
}
