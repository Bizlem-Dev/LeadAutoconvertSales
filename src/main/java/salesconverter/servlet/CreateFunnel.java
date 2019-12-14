package salesconverter.servlet;

import java.io.IOException;
import java.io.PrintWriter;
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

import Dashboard.mongodbdata;
import salesconverter.create.rule.CreateRuleEngine;
import salesconverter.freetrail.FreetrialShoppingCartUpdate;
import salesconverter.mongo.EditFunnel;
import salesconverter.mongo.FunnelDetailsMongoDAO;
import salesconverter.mongo.SaveFunnelDetails;
import services.CallGetService;
import services.CallPostService;
import services.CreateXLSFile;

@Component(immediate = true, metatype = false)
@Service(value = javax.servlet.Servlet.class)
@Properties({ @Property(name = "service.description", value = "Save product Servlet"),
		@Property(name = "service.vendor", value = "VISL Company"),
		@Property(name = "sling.servlet.paths", value = { "/servlet/service/saveFunnel" }),
		@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
		@Property(name = "sling.servlet.extensions", value = { "hotproducts", "cat", "latestproducts", "brief",
				"prodlist", "catalog", "viewcart", "productslist", "addcart", "createproduct", "checkmodelno",
				"productEdit" }) })
@SuppressWarnings("serial")
public class CreateFunnel extends SlingAllMethodsServlet {
//  saveFunnel.welcomesales456
	@Reference
	private SlingRepository repo;
	final String FILEEXTENSION[] = { "csv" };

	final int NUMBEROFRESULTSPERPAGE = 10;
	private static final long serialVersionUID = 1L;
	String fileType = "file";
	JSONObject mainjsonobject = null;

	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();

	 if (request.getRequestPathInfo().getExtension().equals("funnel")) {
			try {
				JSONObject dataJsonObj = new JSONObject(request.getParameter("data").toString());
				// out.print(dataJsonObj);
				// out.print("dataJsonObj : "+dataJsonObj);
				Session session = null;
				session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
				Node ip = session.getRootNode().getNode("content");
				String remoteuser = null;
				String funnelName = null;
				String fromName = null;
				String fromEmailAddress = null;
				String category = null;
				String mailtemp = null;

				remoteuser = dataJsonObj.getString("remoteuser");

				String group = "";
				group = dataJsonObj.getString("group");
				Node shoppingnode = null;
				// shopping cart method call
				String expstatus = new FreetrialShoppingCartUpdate()
						.checkFreeTrialExpirationStatus(remoteuser.replace("@", "_"));
				shoppingnode = new FreetrialShoppingCartUpdate().getLeadAutoConverterNode(expstatus,
						remoteuser.replace("@", "_"), group, session, response);
				funnelName = dataJsonObj.getString("funnelName").trim();

				fromName = dataJsonObj.getString("fromName");
				fromEmailAddress = dataJsonObj.getString("fromEmailAddress");
				category = dataJsonObj.getString("category");
				mailtemp = dataJsonObj.getString("mailtemplate");
				String campaignName = dataJsonObj.getString("campaignName");
				String DistanceBtnCampaign = dataJsonObj.getString("DistanceBtnCampaign");
				if (shoppingnode != null) {
					if (funnelName != null && funnelName.length()>0 && !funnelName.equals("undefined") ){
						SaveFunnelDetails.addFunnelDetails(remoteuser, funnelName, fromName, fromEmailAddress, category,
								mailtemp, campaignName, DistanceBtnCampaign, group, response);
						out.println("Success");
					}
					session.save();
				} else {
					out.println("User Not Valid");

				}

			} catch (Exception ex) {

				out.println("Exception ex " + ex.getMessage());
			}
		} else if (request.getRequestPathInfo().getExtension().equals("Updatescheduletime")) {
			try {
				JSONObject dataJsonObj = new JSONObject(request.getParameter("data").toString());
				// out.print(dataJsonObj);
				// out.print("dataJsonObj : "+dataJsonObj);
				Session session = null;
				session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
				Node ip = session.getRootNode().getNode("content");
				String remoteuser = null;
				String funnelName = null;
				String category = null;
				String mailtemp = null;

				remoteuser = dataJsonObj.getString("remoteuser");
				funnelName = dataJsonObj.getString("funnelName");
				category = dataJsonObj.getString("category");
				mailtemp = dataJsonObj.getString("mailtemplate");
				String scheduleTime = dataJsonObj.getString("scheduleTime");
				JSONArray schedjsonarr = dataJsonObj.getJSONArray("scheduleday");
				JSONObject Schedulejson = new JSONObject();
				Schedulejson.put("scheduleday", schedjsonarr);
				Schedulejson.put("CreatedBy", remoteuser);
				Schedulejson.put("scheduleTime", scheduleTime);

			//	if ("Explore".equalsIgnoreCase(category)) {
					Schedulejson.put("updateflag", "1");
				
				SaveFunnelDetails.updateScheduletime(Schedulejson.toString(), remoteuser, funnelName);
				out.println("Success");
			} catch (Exception ex) {
				out.print(ex.getMessage());
			}
		} else if (request.getRequestPathInfo().getExtension().equals("datasourceupdate")) {
			try {

				JSONObject dataJsonObj = new JSONObject(request.getParameter("data").toString());
				// out.print(dataJsonObj);
				// out.print("dataJsonObj : "+dataJsonObj);
				Session session = null;
				session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
				Node ip = session.getRootNode().getNode("content");
				String remoteuser = null;
				String funnelName = null;
				String category = null;
				String mailtemp = null;

				remoteuser = dataJsonObj.getString("remoteuser");

				String group = "";
				group = dataJsonObj.getString("group");
				Node shoppingnode = null;
				// shopping cart method call
				String expstatus = new FreetrialShoppingCartUpdate()
						.checkFreeTrialExpirationStatus(remoteuser.replace("@", "_"));
				shoppingnode = new FreetrialShoppingCartUpdate().getLeadAutoConverterNode(expstatus,
						remoteuser.replace("@", "_"), group, session, response);
				funnelName = dataJsonObj.getString("funnelName");
				category = dataJsonObj.getString("category");
				mailtemp = dataJsonObj.getString("mailtemplate");
				JSONArray xlsjsonarr = dataJsonObj.getJSONArray("xlsjson");

				JSONArray newarr = new JSONArray();
				newarr.put(xlsjsonarr.get(0));
				JSONObject xlsjson = new JSONObject();
				xlsjson.put("Datasource", newarr);
				// xlsjson.put("CreatedBy", remoteuser);
				if ("Explore".equalsIgnoreCase(category)) {
					xlsjson.put("updateflag", "1");
				} else {
					xlsjson.put("updateflag", "0");
				}

				int subscount = xlsjsonarr.length();
				int quantity = 0;
				String checkquantity = "";

				// out.println("User shoppingnode Valid"+shoppingnode);
				if (shoppingnode != null) {

					if (expstatus.equalsIgnoreCase("1")) {
						Node groupnode = shoppingnode.getParent();
						Node servicenode = groupnode.getParent();
						if (servicenode.hasProperty("quantity")) {
							quantity = Integer.parseInt(servicenode.getProperty("quantity").getString());
							String respupdate = new FreetrialShoppingCartUpdate().updateSubscriberCounter(remoteuser,
									expstatus, shoppingnode, session, response, subscount);
							if (quantity >= subscount) {
								checkquantity = "true";
							} else {
								checkquantity = "false";
							}

						}
					} else {
						checkquantity = "true";
					}
					if (checkquantity.equalsIgnoreCase("true") && checkquantity != "") {

						SaveFunnelDetails.updateDataSource(xlsjson.toString(), remoteuser, funnelName, category,
								mailtemp);
						out.println("Success");
						session.save();
					} else {
						JSONObject res_json_obj = new JSONObject();
						res_json_obj.put("quantity", quantity);
						out.println(res_json_obj.toString());
//				 			out.println("You can not upload leads more than "+quantity);
					}
				} else {
					out.println("User Not Valid");
				}

			} catch (Exception ex) {
				out.print(ex.getMessage());
			}
		} else if (request.getRequestPathInfo().getExtension().equals("savemailtempXLS")) {
			try {

				Session session = null;
				session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
				// String email=request.getRemoteUser();
				String st = "{\r\n" + "\"Fields\":[\r\n" + "\"name\",\r\n" + "\"addr\",\r\n" + "\"city\"\r\n" + "],\r\n"
						+ "\"MailTemplateName\":\"mail1\",\r\n" + "\"DataSourceName\":\"test\",\r\n"
						+ "\"DatasourceType\":\"form\"\r\n" + "}";
//				JSONObject dataJsonObj = new JSONObject(st);
				JSONObject dataJsonObj = new JSONObject(request.getParameter("data").toString());
				String remoteuser = dataJsonObj.getString("remoteuser");

				out.println(new CreateXLSFile().Createxls(dataJsonObj, remoteuser.replace("@", "_")));

			} catch (Exception ex) {
				out.print(ex.getMessage());
			}
		} else if (request.getRequestPathInfo().getExtension().equals("SendToMe")) {
			try {

				JSONObject dependencyjson = new JSONObject();
				JSONObject dataJsonObj = new JSONObject(request.getParameter("data").toString());
				String mailtemp = dataJsonObj.getString("mailtemplate");
				String group = dataJsonObj.getString("group");
				String email = dataJsonObj.getString("remoteuser");
				JSONArray datasourcearr = dataJsonObj.getJSONArray("xlsjson");
				JSONArray newdatasourcearr = new JSONArray();
				if (datasourcearr.length() > 0) {
					if (datasourcearr.length() > 1) {
						for (int i = 0; i < 2; i++) {
							newdatasourcearr.put(datasourcearr.get(i));
						}

					} else {
						for (int i = 0; i < 1; i++) {
							newdatasourcearr.put(datasourcearr.get(i));
						}

					}
					JSONArray multidrpdown = new JSONArray();
					multidrpdown.put("2");

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
					dependencyjson.put("data", newdatasourcearr);
					out.println("dependencyjson : " + dependencyjson);
					String apiurl = ResourceBundle.getBundle("config").getString("doctigerapi");

					out.println("apiurl= " + apiurl);
					int resp = CallPostService.callPostAPIJSON(apiurl, dependencyjson, response);
					if (resp == 200) {

						try {

							out.println("updated= = " + resp);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}

			} catch (Exception ex) {
				out.print(ex.getMessage());
			}
		} else if (request.getRequestPathInfo().getExtension().equals("AddContacts")) {

			try {
				JSONObject dataJsonObj = new JSONObject(request.getParameter("data").toString());

				String CreatedBy = null;
				String funnelName = null;

				out.print("post");

				CreatedBy = dataJsonObj.getString("remoteuser");

				String group = null;
				group = dataJsonObj.getString("group");
				Session session = null;
				try {
					session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
					Node ip = session.getRootNode().getNode("content");
				
				
				Node shoppingnode = null;
				// shopping cart method call
				String expstatus = new FreetrialShoppingCartUpdate()
						.checkFreeTrialExpirationStatus(CreatedBy.replace("@", "_"));
				shoppingnode = new FreetrialShoppingCartUpdate().getLeadAutoConverterNode(expstatus,
						CreatedBy.replace("@", "_"), group, session, response);
				
				
				
				
				funnelName = dataJsonObj.getString("funnelName").trim();
				String category = null;
				category = dataJsonObj.getString("Category");

				JSONArray contactsarr = dataJsonObj.getJSONArray("contacts");
				int subscount = contactsarr.length();
				int quantity = 0;
				String checkquantity = "";

				
				
//				if (funnelName != null && funnelName != "" && funnelName != "undefined") {
//					SaveFunnelDetails.AddContactsDetails(CreatedBy, funnelName, group, category, contactsarr, response);
//					out.println("Success");
//				}
//				
				
				if (shoppingnode != null) {

					if (expstatus.equalsIgnoreCase("1")) {
						Node groupnode = shoppingnode.getParent();
						Node servicenode = groupnode.getParent();
						if (servicenode.hasProperty("quantity")) {
							quantity = Integer.parseInt(servicenode.getProperty("quantity").getString());
							String respupdate = new FreetrialShoppingCartUpdate().updateSubscriberCounter(CreatedBy,
									expstatus, shoppingnode, session, response, subscount);
							if (quantity >= subscount) {
								checkquantity = "true";
							} else {
								checkquantity = "false";
							}

						}
					} else {
						checkquantity = "true";
					}
					if (checkquantity.equalsIgnoreCase("true") && checkquantity != "") {

						if (funnelName != null && funnelName != "" && funnelName != "undefined") {
							SaveFunnelDetails.AddContactsDetails(CreatedBy, funnelName, group, category, contactsarr, response);
							out.println("Success");
						}
						
						
						
					} else {
						JSONObject res_json_obj = new JSONObject();
						res_json_obj.put("quantity", quantity);
						out.println(res_json_obj.toString());
//				 			out.println("You can not upload leads more than "+quantity);
					}
				} else {
					out.println("User Not Valid");
				}
				session.save();
				} catch (LoginException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (RepositoryException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			
				
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (request.getRequestPathInfo().getExtension().equals("UpdateBouncedMail")) {
//http://bizlem.io:8082/portal/servlet/service/saveFunnel.UpdateBouncedMail
			try {
				JSONObject dataJsonObj = new JSONObject(request.getParameter("data").toString());

				JSONArray contactsarr = dataJsonObj.getJSONArray("xlsjson");

				if (contactsarr.length() > 0) {

					out.println(SaveFunnelDetails.updateBouncedEmail(contactsarr, response));
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		else {
			out.print("Requested extension is not an ESP resource");
		}
	}

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		Session session = null;
		try {
			session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
		} catch (LoginException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (RepositoryException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	
		 //saveFunnel.welcomemainsales
		 
		 if (request.getRequestPathInfo().getExtension().equals("welcomemainsales")) {
				try {
					request.getRequestDispatcher("/content/mainui/.Welcomepagemain").forward(request, response);
//					request.getRequestDispatcher("/content/mainui/.welpagemain").forward(request, response);
				} catch (Exception ex) {
					out.print(ex.getMessage());
				}
			}

//		saveFunnel.checkexplorewithflag?email=testsales@gmail.com&group=G1
		if (request.getRequestPathInfo().getExtension().equals("checkexplorewithflag")) {
			try {
//					String remoteuser=request.getParameter("email");
//					String group=request.getParameter("group");
//					
//					String expstatus = new FreetrialShoppingCartUpdate().checkFreeTrialExpirationStatus(remoteuser.replace("@", "_"));
//					out.print("expstatus== "+expstatus);
//					Node	shoppingnode = new FreetrialShoppingCartUpdate().getLeadAutoConverterNode(expstatus, remoteuser.replace("@", "_"), group,
//						session, response);
//					out.print("shoppingnode== "+shoppingnode);

				Node contentNode = session.getRootNode().getNode("content");
				Node massmailratenode = null;
				if (session.getRootNode().hasNode("content")) {
					contentNode = session.getRootNode().getNode("content");
				}

				if (contentNode.hasNode("MailRateMarketingLAC")) {
					massmailratenode = contentNode.getNode("MailRateMarketingLAC");
				} else {
					massmailratenode = contentNode.addNode("MailRateMarketingLAC");

				}

				// new CallGetService().getmassMailType(email, group);

				try {
				out.print("resp== " + SaveFunnelDetails.findupdateflagforexplore(response, massmailratenode, session));
				}catch (Exception e) {
					// TODO: handle exception
				}
			
				session.save();
			} catch (Exception ex) {
				out.print(ex.getMessage());
			}
		}

		if (request.getRequestPathInfo().getExtension().equals("SendOtherCategoryMail")) {
			try {
//check flag for other category and send mail using DCT
				out.print("resp== " + SaveFunnelDetails.SentMailforOtherCategory(response));

			} catch (Exception ex) {
				out.print(ex.getMessage());
			}
		}
//		 getFunnelList(String CreatedBy)
		if (request.getRequestPathInfo().getExtension().equals("funnellist")) {
			try {
//check flag for other category and send mail using DCT
				String email = request.getParameter("email");

				out.print(FunnelDetailsMongoDAO.getFunnelList(email));

			} catch (Exception ex) {
				out.print(ex.getMessage());
			}
		}

		if (request.getRequestPathInfo().getExtension().equals("Editfunnel")) {
			try {
//check flag for other category and send mail using DCT
				String email = request.getParameter("email");
				String funnel = request.getParameter("funnel");

				out.print(FunnelDetailsMongoDAO.getFunneldetails(email, funnel));

			} catch (Exception ex) {
				out.print(ex.getMessage());
			}
		}
		if (request.getRequestPathInfo().getExtension().equals("Getmatchdoc")) {
			try {
//check flag for other category and send mail using DCT
				// http://bizlem.io:8082/portal/servlet/service/saveFunnel.Getmatchdoc?category=Explore&funnel=GULF_IT_DIRECTOR&CreatedBy=salesautoconvertuser1@gmail.com
				// http://bizlem.io:8082/portal/servlet/service/saveFunnel.Getmatchdoc?category=Explore&funnel=GULF_HR_DirectorCreatedBy=jobs@bizlem.com
				String category = request.getParameter("category");
				String mainfunnel = request.getParameter("funnel");
				String CreatedBy = request.getParameter("CreatedBy");
				// out.println("in data");
				out.println(new SaveFunnelDetails().getalldocument(category, CreatedBy, mainfunnel, response));

			} catch (Exception ex) {
				out.print(ex.getMessage());
			}
		}
		if (request.getRequestPathInfo().getExtension().equals("GetSentMailCount")) {
//				https://bizlem.io:8083/portal/servlet/service/saveFunnel.GetSentMailCount?Email=jobs@bizlem.com
			//https://bluealgo.com:8083/portal/servlet/service/saveFunnel.GetSentMailCount?Email=jobs@bizlem.com
			try {
//check flag for other category and send mail using DCT
				// http://bizlem.io:8082/portal/servlet/service/SaveFunnel.Getmatchdoc?category=Explore&funnel=GULF_HR_DirectorCreatedBy=jobs@bizlem.com
				String email = request.getParameter("Email").replaceAll("@", "_");
				Node contentNode = session.getRootNode().getNode("content");
				Node emailNode = null;
				if (session.getRootNode().hasNode("content")) {
					contentNode = session.getRootNode().getNode("content");
				}
				JSONObject jsob = new JSONObject();
				JSONArray ar = new JSONArray();
				String funnel = null;
				Node funnode = null;
				JSONObject funjs = null;
				if (contentNode.hasNode("MailRateMarketingLAC")
						&& contentNode.getNode("MailRateMarketingLAC").hasNode(email.replace("@", "_"))) {
					emailNode = contentNode.getNode("MailRateMarketingLAC").getNode(email.replace("@", "_"));
//						if (emailNode.hasNode("services") && emailNode.getNode("services").hasNode("salesautoconvert")
//								&& emailNode.getNode("services").getNode("salesautoconvert").hasNodes()) {
					NodeIterator itr = emailNode.getNodes();
					// salesautoconvert
					while (itr.hasNext()) {
						funnode = itr.nextNode();
						funjs = new JSONObject();
						funnel = funnode.getName();
						funjs.put("funnel", funnel);
						if (funnode.hasProperty("MailSentCount")) {
							funjs.put("MailSentCount", funnode.getProperty("MailSentCount").getString());
						}
						ar.put(funjs);
					}
				}
				jsob.put("MailSentDetails", ar);
				out.println(jsob);

			} catch (Exception ex) {
				out.print(ex.getMessage());
			}
		}
		if (request.getRequestPathInfo().getExtension().equals("Getbouncedmailflag")) {
			try {
//check flag for other category and send mail using DCT
				// http://bizlem.io:8082/portal/servlet/service/saveFunnel.Getbouncedmailflag?email=teja
			
				String email = request.getParameter("email").trim();
					out.println(new SaveFunnelDetails().getBouncedMailFlag(email, response));

			} catch (Exception ex) {
				out.print(ex.getMessage());
			}
		}	if (request.getRequestPathInfo().getExtension().equals("readbouncedmail")) {
			try {
//check flag for other category and send mail using DCT
				// http://bizlem.io:8082/portal/servlet/service/saveFunnel.Getbouncedmailflag?email=teja
			
				
				String host = "imap.gmail.com";
				String port = "993";
				String userName = "sales@doctiger.com";
				String password = "doctiger123";
				String sender="mailer-daemon@googlemail.com";
				
				
				//new ReadBounceEmail().searchBouncedEmail(host, port, userName, password,sender, response);
			//		out.println(new SaveFunnelDetails().getBouncedMailFlag(email, response));

			} catch (Exception ex) {
				out.print(ex.getMessage());
			}
		}
		//getmainscheduletime
		if (request.getRequestPathInfo().getExtension().equals("getscheduletime")) {

		}
//http://bizlem.io:8082/portal/servlet/service/saveFunnel.sendMultipleCategoryMails
		if (request.getRequestPathInfo().getExtension().equals("sendMultipleCategoryMails")) {
			try {
//					String remoteuser=request.getParameter("email");
//					String group=request.getParameter("group");
//					
//					String expstatus = new FreetrialShoppingCartUpdate().checkFreeTrialExpirationStatus(remoteuser.replace("@", "_"));
//					out.print("expstatus== "+expstatus);
//					Node	shoppingnode = new FreetrialShoppingCartUpdate().getLeadAutoConverterNode(expstatus, remoteuser.replace("@", "_"), group,
//						session, response);
//					out.print("shoppingnode== "+shoppingnode);

				Node contentNode = session.getRootNode().getNode("content");
				Node massmailratenode = null;
				if (session.getRootNode().hasNode("content")) {
					contentNode = session.getRootNode().getNode("content");
				}

				if (contentNode.hasNode("MailRateMarketingLAC")) {
					massmailratenode = contentNode.getNode("MailRateMarketingLAC");
				} else {
					massmailratenode = contentNode.addNode("MailRateMarketingLAC");

				}

				// new CallGetService().getmassMailType(email, group);

				
				try {
				SaveFunnelDetails.findMultipleCampaigns(response, massmailratenode, session);
				}catch (Exception e) {
					// TODO: handle exception
				}
				session.save();
			} catch (Exception ex) {
				out.print(ex.getMessage());
			}
		}
		if (request.getRequestPathInfo().getExtension().equals("EditFunnelAlldata")) {
			try {
//check flag for other category and send mail using DCT
				// http://bizlem.io:8082/portal/servlet/service/saveFunnel.EditFunnel?email=salesautoconvertuser1@gmail.com&funnel=fun2611
			
				String email = request.getParameter("email").trim();
				String funnel = request.getParameter("funnel").trim();
				String[] dbcate = { "Explore", "Warm", "Inform", "Entice", "Connect" };
				JSONArray allcat=new JSONArray();//dbcate.length
					for(int i=0;i<dbcate.length;i++) {
						allcat.put(new EditFunnel().editFunnel(funnel, dbcate[i],email));
					}
//					JSONObject js=new JSONObject();
//					if(allcat.length()>0) {
//					js.put("data", allcat);
//					}
					out.println(new EditFunnel().editFunnel(funnel, "Explore",email));
			} catch (Exception ex) {
				out.print(ex.getMessage());
			}
		}

	}

}
