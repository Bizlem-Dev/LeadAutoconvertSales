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
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.jcr.api.SlingRepository;
import org.bson.conversions.Bson;
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
import leadconverter.mongo.MongoDAO;

@Component(immediate = true, metatype = false)
@Service(value = javax.servlet.Servlet.class)
@Properties({ @Property(name = "service.description", value = "Save product Servlet"),
		@Property(name = "service.vendor", value = "VISL Company"),
		@Property(name = "sling.servlet.paths", value = { "/servlet/service/ui" }),
		@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
		@Property(name = "sling.servlet.extensions", value = { "hotproducts", "cat", "latestproducts", "brief",
				"prodlist", "catalog", "viewcart", "productslist", "addcart", "createproduct", "checkmodelno",
				"productEdit" }) })
@SuppressWarnings("serial")
public class UIServlet extends SlingAllMethodsServlet {

	@Reference
	private SlingRepository repo;
	final String FILEEXTENSION[] = { "csv" };

	final int NUMBEROFRESULTSPERPAGE = 10;
	private static final long serialVersionUID = 1L;
	String fileType = "file";
	JSONObject mainjsonobject = null;
	Session session = null;
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		// http://development.bizlem.io:8082/portal/servlet/service/ui.shopping45?email=viki@gmail.com&group=G1&SubscriberCount=10
		//http://development.bizlem.io:8082/portal/servlet/service/ui.shopping45?email=viki@gmail.comcat=Explore&fun=today302019&campid=1628
	
		if (request.getRequestPathInfo().getExtension().equals("InsertRuleURLS")) {
				try {
					
					System.out.println("In monitor Data If");
					session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
				
//					Mongo mongo = new Mongo("localhost", 27017);
//					DB db = mongo.getDB("phplisttest");
//					
//					DBCollection collection = db.getCollection("RuleEngineCalledForSubscriberData");
//
//				String remoteuser=request.getParameter("email").replace("@", "_");
//				System.out.println("Remote user :"+remoteuser);
//				JSONObject rulejssave=new JSONObject();
//				DBObject dbObject = (DBObject)JSON.parse(rulejssave.toString());
//				
//				collection.insert(dbObject);
				
			}
			
		catch (Exception e) {

		System.out.println("Exception ex : "+e.getMessage());
		}
		}

		
		if (request.getRequestPathInfo().getExtension().equals("verify")) {
			try {
				request.getRequestDispatcher("/content/mainui/.fverify").forward(request, response);
			} catch (Exception ex) {
				out.print(ex.getMessage());
			}
		} else if (request.getRequestPathInfo().getExtension().equals("texts-only-template")) {
			try {
				request.getRequestDispatcher("/content/mainui/.ftexts-only-template").forward(request, response);
			} catch (Exception ex) {
				out.print(ex.getMessage());
			}
		} else if (request.getRequestPathInfo().getExtension().equals("text-photos-template")) {
			try {
				request.getRequestDispatcher("/content/mainui/.ftext-photos-template").forward(request, response);
			} catch (Exception ex) {
				out.print(ex.getMessage());
			}
		} else if (request.getRequestPathInfo().getExtension().equals("strat-from-scratch")) {
			try {
				request.getRequestDispatcher("/content/mainui/.fstrat-from-scratch").forward(request, response);
			} catch (Exception ex) {
				out.print(ex.getMessage());
			}
		} else if (request.getRequestPathInfo().getExtension().equals("smtp-setup")) {
			try {
				request.getRequestDispatcher("/content/mainui/.fsmtp-setup").forward(request, response);
			} catch (Exception ex) {
				out.print(ex.getMessage());
			}
		} else if (request.getRequestPathInfo().getExtension().equals("simpleLayout")) {
			try {
				request.getRequestDispatcher("/content/mainui/.fsimpleLayout").forward(request, response);
			} catch (Exception ex) {
				out.print(ex.getMessage());
			}
		} else if (request.getRequestPathInfo().getExtension().equals("set-up-campaign")) {
			try {
				request.getRequestDispatcher("/content/mainui/.fset-up-campaign").forward(request, response);
			} catch (Exception ex) {
				out.print(ex.getMessage());
			}
		} else if (request.getRequestPathInfo().getExtension().equals("set-up-campaign-complete")) {
			try {
				request.getRequestDispatcher("/content/mainui/.fset-up-campaign-complete").forward(request, response);
			} catch (Exception ex) {
				out.print(ex.getMessage());
			}
		} else if (request.getRequestPathInfo().getExtension().equals("select-tamplate")) {
			try {
				request.getRequestDispatcher("/content/mainui/.fselect-tamplate").forward(request, response);
			} catch (Exception ex) {
				out.print(ex.getMessage());
			}
		} else if (request.getRequestPathInfo().getExtension().equals("select-tamplate-file")) {
			try {
				request.getRequestDispatcher("/content/mainui/.fselect-tamplate-file").forward(request, response);
			} catch (Exception ex) {
				out.print(ex.getMessage());
			}
		} else if (request.getRequestPathInfo().getExtension().equals("news-letter-tamplate")) {
			try {
				request.getRequestDispatcher("/content/mainui/.fnews-letter-tamplate").forward(request, response);
			} catch (Exception ex) {
				out.print(ex.getMessage());
			}
		} else if (request.getRequestPathInfo().getExtension().equals("list")) {
			try {
				request.getRequestDispatcher("/content/mainui/.flist").forward(request, response);
			} catch (Exception ex) {
				out.print(ex.getMessage());
			}
		} else if (request.getRequestPathInfo().getExtension().equals("index")) {
			try {
			//	request.getRequestDispatcher("/content/mainui/.findex").forward(request, response);
				 request.getRequestDispatcher("/content/static/.Home").forward(request, response);
			} catch (Exception ex) {
				out.print(ex.getMessage());
			}
		} else if (request.getRequestPathInfo().getExtension().equals("gallery-template")) {
			try {
				request.getRequestDispatcher("/content/mainui/.fgallery-template").forward(request, response);
			} catch (Exception ex) {
				out.print(ex.getMessage());
			}
		} else if (request.getRequestPathInfo().getExtension().equals("funnel")) {
			try {
				request.getRequestDispatcher("/content/mainui/.ffunnel").forward(request, response);
			} catch (Exception ex) {
				out.print(ex.getMessage());
			}
		} else if (request.getRequestPathInfo().getExtension().equals("configuration")) {
			try {
				request.getRequestDispatcher("/content/mainui/.fconfiguration").forward(request, response);
			} catch (Exception ex) {
				out.print(ex.getMessage());
			}
		} else if (request.getRequestPathInfo().getExtension().equals("googleanalytics")) {
			try {
				request.getRequestDispatcher("/content/mainui/.googleAnalytics").forward(request, response);
			} catch (Exception ex) {
				out.print(ex.getMessage());
			}
		}

		else if (request.getRequestPathInfo().getExtension().equals("loginuser")) {
			try {
				request.getRequestDispatcher("/content/mainui/.loginuser").forward(request, response);
			} catch (Exception ex) {
				out.print(ex.getMessage());
			}
		} else if (request.getRequestPathInfo().getExtension().equals("getloginuser")) {
			try {
				request.getRequestDispatcher("/content/mainui/.getloginuser").forward(request, response);
			} catch (Exception ex) {
				out.print(ex.getMessage());
			}
		} else if (request.getRequestPathInfo().getExtension().equals("set-up-rule")) {
			try {
				request.getRequestDispatcher("/content/mainui/.fset-up-rule").forward(request, response);
			} catch (Exception ex) {
				out.print(ex.getMessage());
			}
		} else if (request.getRequestPathInfo().getExtension().equals("test-rule-engine")) {
			try {
				request.getRequestDispatcher("/content/mainui/.ftest-rule-engine").forward(request, response);
			} catch (Exception ex) {
				out.print(ex.getMessage());
			}
		} else if (request.getRequestPathInfo().getExtension().equals("free-trail-expire")) {
			try {
				request.getRequestDispatcher("/content/mainui/.ffree-trail-expire").forward(request, response);
			} catch (Exception ex) {
				out.print(ex.getMessage());
			}
		} else if (request.getRequestPathInfo().getExtension().equals("get_subscriber_status")) {
			// out.println("in callservice");
			String logged_in_user_email = request.getParameter("rm_email");
			// FreeTrialandCart cart = new FreeTrialandCart();
			// String freetrialstatus = cart.checkfreetrial(logged_in_user_email);
			// mailtangynode = cart.getMailtangyNode(freetrialstatus, logged_in_user_email,
			// "", session, response);

			MongoDAO mdao = new MongoDAO();
			long subscribers_count = mdao.getSubscriberCountForLoggedInUserForFreeTrail("subscribers_details",
					logged_in_user_email);
			String free_trail_status = new FreetrialShoppingCartUpdate().checkfreetrial(logged_in_user_email);
			// long subscribers_count=2000;
			// String free_trail_status="0";
			// out.println("logged_in_user_email : "+logged_in_user_email);
			// out.println("subscribers_count : "+subscribers_count);
			// out.println("free_trail_status : "+free_trail_status);
			if (subscribers_count <= 2000 && free_trail_status.equals("0")) {
				System.out.println("Free Train is Active");
				out.println("Free Train is Active");
			} else if (free_trail_status.equals("1")) {
				System.out.println("Free Train Date Expired");
				out.println("Free Train Date Expired");
			} else if (subscribers_count > 2000) {
				System.out.println("Subscriber Count is More");
				out.println("Subscriber Count is More");
			}

		} else if (request.getRequestPathInfo().getExtension().equals("grouplist")) {
			try {
				session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));

				String email = request.getParameter("email").replace("@", "_");
				JSONObject grjsa = new FreetrialShoppingCartUpdate().getLeadAutoConverterGroupList(email, session,
						response);
				out.println(grjsa);
			} catch (Exception ex) {
				out.print(ex.getMessage());
			}
		} else if (request.getRequestPathInfo().getExtension().equals("checkValidUser")) {

			try {
				session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
				String email = request.getParameter("email").replace("@", "_");
				String jsresp;
				try {
					jsresp = new GetValidityInfoFromEmail().getValidityInfo(email, session, response);
					out.println(jsresp);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				

			} catch (LoginException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RepositoryException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}else if (request.getRequestPathInfo().getExtension().equals("testlac1")) {
			try {
//				request.getRequestDispatcher("/content/static/.LeadAutoConvert").forward(request, response);
				RequestDispatcher dis = request.getRequestDispatcher("/content/static/.Home");
				dis.forward(request, response);
				
			} catch (Exception ex) {
				out.print(ex.getMessage());
			}
		}else if (request.getRequestPathInfo().getExtension().equals("LAC")) {
			try {
				//out.println("get");
//				request.getRequestDispatcher("/content/static/.LeadAutoConvert").forward(request, response);
				RequestDispatcher dis = request.getRequestDispatcher("/content/static/.LACMAIN");
				dis.forward(request, response);
				
			} catch (Exception ex) {
				out.print(ex.getMessage());
			}
		}  else if(request.getRequestPathInfo().getExtension().equals("googleanalytics")) {
		     try {
				  request.getRequestDispatcher("/content/mainui/.googleAnalytics").forward(request, response);
			     } catch (Exception ex) {
				     out.print(ex.getMessage());
			     }
		 } else if(request.getRequestPathInfo().getExtension().equals("moveMonit")) {
		     try {
				  request.getRequestDispatcher("/content/static/.MoveFunnelMonitertest").forward(request, response);
			     } catch (Exception ex) {
				     out.print(ex.getMessage());
			     }
		 }else if(request.getRequestPathInfo().getExtension().equals("provisioning")) {
		     try {
				  request.getRequestDispatcher("/content/static/.SetupProvisioning").forward(request, response);
			     } catch (Exception ex) {
				     out.print(ex.getMessage());
			     }
		 }else if(request.getRequestPathInfo().getExtension().equals("webservice")) {
		     try {
				  request.getRequestDispatcher("/content/static/.WebServiceIntegration").forward(request, response);
			     } catch (Exception ex) {
				     out.print(ex.getMessage());
			     }
		 }

		 else if(request.getRequestPathInfo().getExtension().equals("MonitorData")) {
				try {
					
					System.out.println("In monitor Data If");
					session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
				
				//remoteuser=viki@gmail.com&category=Explore&funnel=&campid=
				String remoteuser=request.getParameter("email").replace("@", "_");
				System.out.println("Remote user :"+remoteuser);
				String jj = GetGADataForMonitoringLeads.findGAdatapersubscriber(remoteuser, request.getParameter("category"), request.getParameter("funnel"), request.getParameter("campid"),response);
				out.println(jj);
//				String jj12 = GetGADataForMonitoringLeads.findGAdatapersubscriber("viki_gmail.com", "Explore", "today302019", "1628");
//				out.println("jj== ========= " + jj12);
			
			}
			
		catch (Exception e) {

		System.out.println("Exception ex : "+e.getMessage());
		}
		} else if(request.getRequestPathInfo().getExtension().equals("Move")) {
			try {
				
				System.out.println("In monitor Data If");
				session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
			
			
			String remoteuser=request.getParameter("email").replace("@", "_");
			String group=request.getParameter("group").replace("@", "_");
			System.out.println("Remote user :"+remoteuser);
			String expstatus= new FreetrialShoppingCartUpdate().checkFreeTrialExpirationStatus(remoteuser);
		 	int quantity=0;
		 	out.println("Remote expstatus :"+expstatus+"= group::"+group);
		 	String checkquantity="";
	 		Node shoppingnode=new FreetrialShoppingCartUpdate().getLeadAutoConverterNode(expstatus, remoteuser.replace("@", "_"), group, session, response);
	 		out.println("Remote shoppingnode :"+shoppingnode);
//			String jj = GetGADataForMonitoringLeads.findGAdatapersubscriber(remoteuser, request.getParameter("category"), request.getParameter("funnel"), request.getParameter("campid"),response);
//			out.println(jj);
//			String jj12 = GetGADataForMonitoringLeads.findGAdatapersubscriber("viki_gmail.com", "Explore", "today302019", "1628");
//			out.println("jj== ========= " + jj12);
		
		}
		
	catch (Exception e) {

	System.out.println("Exception ex : "+e.getMessage());
	}
	}
		 else {
			out.print("Rrquested extension is not an ESP resource");
			String remoteuser = request.getRemoteUser();
			out.print("Logged In User Is (remoteuser): " + remoteuser);
		}

		
	}
	@Override
	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		 if(request.getRequestPathInfo().getExtension().equals("movedata")) {
			try {
				PrintWriter out=response.getWriter();
				System.out.println("In move Data If");
				session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));

				StringBuilder builder = new StringBuilder();
				BufferedReader bufferedReaderCampaign = request.getReader();
				String line;
				while ((line = bufferedReaderCampaign.readLine()) != null) {
				builder.append(line + "\n");
				}
				JSONObject jsondata = new JSONObject(builder.toString());
				JSONArray arrJson = jsondata.getJSONArray("FunnelList");
				String[] farr = new String[arrJson.length()];
				for(int i = 0; i < arrJson.length(); i++) {
				farr[i] = arrJson.getString(i);
				String remoteuser=jsondata.getString("username").replace("@", "_");
				String funnelname=jsondata.getString("FunnelName");
				String jj = GetGADataForMonitoringLeads.moveuidata(remoteuser,farr[i], funnelname,response);
				out.println(jj);

				}

//					String jj12 = GetGADataForMonitoringLeads.findGAdatapersubscriber("viki_gmail.com", "Explore", "today302019", "1628");
//					out.println("jj== ========= " + jj12);

				}

				catch (Exception e) {

				System.out.println("Exception ex : "+e.getMessage());
				}
				}
		
	}
}
