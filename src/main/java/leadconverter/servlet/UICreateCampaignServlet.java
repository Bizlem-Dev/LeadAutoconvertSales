package leadconverter.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.ResourceBundle;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
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
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.jcr.api.SlingRepository;

import leadconverter.doctiger.LogByFileWriter;
import leadconverter.freetrail.FreetrialShoppingCartUpdate;
import leadconverter.mongo.CampaignMongoDAO;
import leadconverter.mongo.FunnelDetailsMongoDAO;

@Component(immediate = true, metatype = false)
@Service(value = javax.servlet.Servlet.class)
@Properties({ @Property(name = "service.description", value = "Save product Servlet"),
		@Property(name = "service.vendor", value = "VISL Company"),
		@Property(name = "sling.servlet.paths", value = { "/servlet/service/createCampaign" }),
		@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
		@Property(name = "sling.servlet.extensions", value = { "hotproducts", "cat", "latestproducts", "brief",
				"prodlist", "catalog", "viewcart", "productslist", "addcart", "createproduct", "checkmodelno",
				"productEdit" }) })
//createCampaign.getFunnel
@SuppressWarnings("serial")
public class UICreateCampaignServlet extends SlingAllMethodsServlet {

	@Reference
	private SlingRepository repo;
	final String FILEEXTENSION[] = { "csv" };

	final int NUMBEROFRESULTSPERPAGE = 10;
	private static final long serialVersionUID = 1L;
	String fileType = "file";
	JSONObject mainjsonobject = new JSONObject();
	String unnsubscriber_link = "<p><small><a href='[UNSUBSCRIBEURL]'>unsubscribe me</a></small></p>";

	@SuppressWarnings("deprecation")
	@Override
	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		JSONArray mainarray = new JSONArray();
		JSONObject jsonobject = new JSONObject();
		String temp_list_id = null;

		String remoteuser = request.getRemoteUser();

		try {
			Session session = null;

			session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
			// Node content = session.getRootNode().getNode("content");
			Node ip = session.getRootNode().getNode("content");
			if (request.getRequestPathInfo().getExtension().equals("CampaignNodeAdd")) {
				try {
					long count_Value1 = 0;
					Node campaignnode = null;
					Node funnelNode = null;// this variable created by Akhil
//					Node content = session.getRootNode().getNode("content").getNode("user");
//					Node usernode = null;
					Node remoteusernode = null;
					Node leadconverternode = null;
					Node emailnode = null;
					String addcampaignnode = null;
					Node addcampaigninsubscribernode = null;

					String footer = request.getParameter("footer");
					// String send_a_webpage_url = request.getParameter("send_a_webpage_url");
					// String compose_message_txt = request.getParameter("compose_message_txt");
					String body = request.getParameter("ckcontent");
					body = body + unnsubscriber_link;
					String subject = request.getParameter("subject");
					String campaignName = request.getParameter("campaignName");
					String fromName = request.getParameter("fromName");
					String fromEmailAddress = request.getParameter("fromEmailAddress");
					String funnelName = request.getParameter("funnelName");
					String type = request.getParameter("SubFunnelName");
					String DistanceBtnCampaign = request.getParameter("DistanceBtnCampaign");
					String list_id = request.getParameter("listid");
					// String logged_in_user = request.getParameter("logged_in_user");
					String logged_in_user = request.getParameter("logged_in_user").replace("@", "_");

					String fromfield = "Bizlem"; // request.getRemoteUser();
					String replyto = "";//request.getRemoteUser();
					// String embargo ="2019-01-07 06:43:02 PM"; //request.getParameter("date");
					// 2019-01-07 06:43:02 PM
					String embargo = "";
					String campaignvalue = request.getParameter("campaignvalue");

					// out.println("body : "+body);
					String bodyvalue = URLEncoder.encode(body);
					String campaignaddurl = ResourceBundle.getBundle("config").getString("Campaign_Add_Url_New");
					String campaignaddapiurlparameters = "subject=" + subject + "&fromfield=" + fromfield + "&replyto="
							+ replyto + "&message=" + body + "&textmessage=hii&footer=" + footer
							+ "&status=draft&sendformat=html&template=&embargo=" + embargo
							+ "&rsstemplate=&owner=1&htmlformatted=&repeatinterval=&repeatuntil=&requeueinterval=&requeueuntil=";
//					String campaignaddapiurlparameters = "subject=" + subject + "&fromfield=" + fromfield + "&message=" + body + "&textmessage=hii&footer=" + footer
//							+ "&status=draft&sendformat=html&template=&embargo=" + embargo
//							+ "&rsstemplate=&owner=1&htmlformatted=&repeatinterval=&repeatuntil=&requeueinterval=&requeueuntil=";
				
					
					String campaignresponse = this.sendHttpPostData(campaignaddurl,
							campaignaddapiurlparameters.replace(" ", "%20").replace("\r", "").replace("\n", ""),
							response).replace("<pre>", "");

					JSONObject campaignjson = new JSONObject(campaignresponse);
					String campaignstatus = campaignjson.getString("status");
					JSONObject data = campaignjson.getJSONObject("data");
					String campaignid = data.getString("id");
					String campaign_status = data.getString("status");
					// status

					JSONObject res_json_obj = new JSONObject();
					res_json_obj.put("campaignid", campaignid);
					// res_json_obj.put("body", body);
					// res_json_obj.put("bodyvalue", bodyvalue);

					out.println(res_json_obj.toString());
					//

					Node shoppingnode = null;

					String expstatus = new FreetrialShoppingCartUpdate().checkFreeTrialExpirationStatus(logged_in_user);
					String group = "";
					group = request.getParameter("group");
					shoppingnode = new FreetrialShoppingCartUpdate().getLeadAutoConverterNode(expstatus, logged_in_user,
							group, session, response);

					if (shoppingnode != null) {
						// String logged_in_user="viki_gmail.com";
						if (campaignstatus.equals(("success"))) {

//						if (!content.hasNode(logged_in_user)) {
//							usernode = content.addNode(logged_in_user);
//				     	} else {
//							usernode = content.getNode(logged_in_user);
//						}
//						
//								 		
//				 		if (!shoppingnode.hasNode("Lead_Converter")) {
//							leadconverternode = usernode.addNode("Lead_Converter");
//						} else {
//						    leadconverternode = usernode.getNode("Lead_Converter");
//	   					}

//						if (!shoppingnode.hasNode("Lead_Converter")) {
//							leadconverternode = shoppingnode.addNode("Lead_Converter");
//						} else {
//						    leadconverternode = shoppingnode.getNode("Lead_Converter");
//	   					}
							if (!shoppingnode.hasNode("Email")) {
								emailnode = shoppingnode.addNode("Email");
							} else {
								emailnode = shoppingnode.getNode("Email");
							}
							if (!emailnode.hasNode("Funnel")) {
								funnelNode = emailnode.addNode("Funnel");
							} else {
								funnelNode = emailnode.getNode("Funnel");
							}

							Node UserFunnelNode = null;// this variable created by Akhil
							Node UserSubFunnelNode = null;// this variable created by Akhil
							Node ListNode = null;// this variable created by Akhil
							Node ListCampaignNode = null;// this variable created by Akhil
							Node DraftListNode = null;
							Node ActiveListNode = null;
							Node ActiveListChildNode = null;
							if (!funnelNode.hasNode(funnelName)) {
								UserFunnelNode = funnelNode.addNode(funnelName);
							} else {
								UserFunnelNode = funnelNode.getNode(funnelName);
							}
							int date_distance = 0;
							if (!UserFunnelNode.hasNode(type)) {
								// out.println("if UserFunnelNode");
								campaignvalue = logged_in_user + "_" + type + "_" + String.valueOf(count_Value1 + 1);
								UserSubFunnelNode = UserFunnelNode.addNode(type);
								// Setting Subfunnel property
								UserSubFunnelNode.setProperty("Counter", count_Value1);
								UserSubFunnelNode.setProperty("Current_Campaign", campaignvalue);
								UserSubFunnelNode.setProperty("Distance", DistanceBtnCampaign);
								DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 2019-03-15
																									// 01:00:00
								// System.out.println("Today's Date : "+dateFormat.format(new Date()));
								UserSubFunnelNode.setProperty("Current_Date", dateFormat.format(new Date()));
								// UserSubFunnelNode.setProperty("No_of_days", noofdays);
								if (UserSubFunnelNode.hasNode("List")) {
									ListNode = UserSubFunnelNode.getNode("List");
									if (ListNode.hasNode("DraftList")) {
										DraftListNode = ListNode.getNode("DraftList");
									} else {
										DraftListNode = ListNode.addNode("DraftList");
										DraftListNode.setProperty("DraftListCounter", "0");
									}
									if (ListNode.hasNode("ActiveList")) {
										ActiveListNode = ListNode.getNode("ActiveList");
										if (type.equals("Explore")) {
											ActiveListChildNode = ActiveListNode.addNode(list_id);
											if (ActiveListChildNode.hasNode("Campaign")) {
												ListCampaignNode = ActiveListChildNode.getNode("Campaign");

											} else {
												ListCampaignNode = ActiveListChildNode.addNode("Campaign");
											}
										} else {

											String listname = "List_" + campaignvalue;
											String listurl = ResourceBundle.getBundle("config")
													.getString("List_Add_Url");
											String listparameter = "?name=" + listname + "&description=This Belongs to "
													+ "&listorder=" + 90 + "&active=" + 1;
											String listresponse = this
													.sendpostdata(listurl, listparameter.replace(" ", "%20"), response)
													.replace("<pre>", "");
											JSONObject listjson = new JSONObject(listresponse);
											String liststatusresponse = listjson.getString("status");
											// out.println("List Status Response : " +
											JSONObject getjsonid = listjson.getJSONObject("data");
											temp_list_id = getjsonid.getString("id");
											ActiveListChildNode = ActiveListNode.addNode(temp_list_id);
											if (ActiveListChildNode.hasNode("Campaign")) {
												ListCampaignNode = ActiveListChildNode.getNode("Campaign");
											} else {
												ListCampaignNode = ActiveListChildNode.addNode("Campaign");
											}
										}
									} else {
										ActiveListNode = ListNode.addNode("ActiveList");
										ActiveListNode.setProperty("ActiveListCounter", "1");
										if (type.equals("Explore")) {
											ActiveListChildNode = ActiveListNode.addNode(list_id);
											if (ActiveListChildNode.hasNode("Campaign")) {
												ListCampaignNode = ActiveListChildNode.getNode("Campaign");
											} else {
												ListCampaignNode = ActiveListChildNode.addNode("Campaign");
											}
										} else {
											String listname = "List_" + campaignvalue;
											// String listurl = content_ip.getNode("ip")
											// .getProperty("List_Add_Url").getString();
											String listurl = ResourceBundle.getBundle("config")
													.getString("List_Add_Url");

											String listparameter = "?name=" + listname + "&description=This Belongs to "
													+ "&listorder=" + 90 + "&active=" + 1;
											String listresponse = this
													.sendpostdata(listurl, listparameter.replace(" ", "%20"), response)
													.replace("<pre>", "");
											// out.println("List Response " + listresponse);
											JSONObject listjson = new JSONObject(listresponse);
											String liststatusresponse = listjson.getString("status");
											// out.println("List Status Response : " +
											// liststatusresponse);
											JSONObject getjsonid = listjson.getJSONObject("data");
											temp_list_id = getjsonid.getString("id");

											ActiveListChildNode = ActiveListNode.addNode(temp_list_id);
											if (ActiveListChildNode.hasNode("Campaign")) {
												ListCampaignNode = ActiveListChildNode.getNode("Campaign");

											} else {
												ListCampaignNode = ActiveListChildNode.addNode("Campaign");
											}

										}

									}
								} else {
									// out.println("else List");
									ListNode = UserSubFunnelNode.addNode("List");
									if (ListNode.hasNode("DraftList")) {
										DraftListNode = ListNode.getNode("DraftList");

									} else {
										DraftListNode = ListNode.addNode("DraftList");
										DraftListNode.setProperty("DraftListCounter", "0");
									}
									if (ListNode.hasNode("ActiveList")) {
										// out.println("if ActiveList");
										ActiveListNode = ListNode.getNode("ActiveList");
										if (type.equals("Explore")) {
											// out.println("if Explore");
											ActiveListChildNode = ActiveListNode.addNode(list_id);
											if (ActiveListChildNode.hasNode("Campaign")) {
												ListCampaignNode = ActiveListChildNode.getNode("Campaign");

											} else {
												ListCampaignNode = ActiveListChildNode.addNode("Campaign");
											}
										} else {
											// out.println("else Explore");
											String listname = "List_" + campaignvalue;
											// String listurl = content_ip.getNode("ip")
											// .getProperty("List_Add_Url").getString();
											String listurl = ResourceBundle.getBundle("config")
													.getString("List_Add_Url");

											String listparameter = "?name=" + listname + "&description=This Belongs to "
													+ "&listorder=" + 90 + "&active=" + 1;
											String listresponse = this
													.sendpostdata(listurl, listparameter.replace(" ", "%20"), response)
													.replace("<pre>", "");
											// out.println("List Response " + listresponse);
											JSONObject listjson = new JSONObject(listresponse);
											String liststatusresponse = listjson.getString("status");
											// out.println("List Status Response : " +
											// liststatusresponse);
											JSONObject getjsonid = listjson.getJSONObject("data");
											temp_list_id = getjsonid.getString("id");

											ActiveListChildNode = ActiveListNode.addNode(temp_list_id);
											if (ActiveListChildNode.hasNode("Campaign")) {
												ListCampaignNode = ActiveListChildNode.getNode("Campaign");

											} else {
												ListCampaignNode = ActiveListChildNode.addNode("Campaign");
											}

										}
									} else {
										// out.println("if ActiveList");
										ActiveListNode = ListNode.addNode("ActiveList");
										ActiveListNode.setProperty("ActiveListCounter", "1");
										if (type.equals("Explore")) {
											ActiveListChildNode = ActiveListNode.addNode(list_id);
											if (ActiveListChildNode.hasNode("Campaign")) {
												ListCampaignNode = ActiveListChildNode.getNode("Campaign");

											} else {
												ListCampaignNode = ActiveListChildNode.addNode("Campaign");
											}
										} else {
											String listname = "List_" + campaignvalue;
											// String listurl = content_ip.getNode("ip")
											// .getProperty("List_Add_Url").getString();
											String listurl = ResourceBundle.getBundle("config")
													.getString("List_Add_Url");

											String listparameter = "?name=" + listname + "&description=This Belongs to "
													+ "&listorder=" + 90 + "&active=" + 1;
											String listresponse = this
													.sendpostdata(listurl, listparameter.replace(" ", "%20"), response)
													.replace("<pre>", "");
											// out.println("List Response " + listresponse);
											JSONObject listjson = new JSONObject(listresponse);
											String liststatusresponse = listjson.getString("status");
											// out.println("List Status Response : " +
											// liststatusresponse);
											JSONObject getjsonid = listjson.getJSONObject("data");
											temp_list_id = getjsonid.getString("id");

											ActiveListChildNode = ActiveListNode.addNode(temp_list_id);
											if (ActiveListChildNode.hasNode("Campaign")) {
												ListCampaignNode = ActiveListChildNode.getNode("Campaign");

											} else {
												ListCampaignNode = ActiveListChildNode.addNode("Campaign");
											}

										}

									}
								}

								date_distance = 0;
								// out.println("step 1");
							} else {
								// out.println("else UserFunnelNode");
								UserSubFunnelNode = UserFunnelNode.getNode(type);

								count_Value1 = UserSubFunnelNode.getProperty("Counter").getLong();
								UserSubFunnelNode.setProperty("Distance", DistanceBtnCampaign);
								if (UserSubFunnelNode.hasProperty("Distance")) {
									String distance = UserSubFunnelNode.getProperty("Distance").getString()
											.replace(" Week", "");
									date_distance = Integer.parseInt(distance);
								} else {
									date_distance = 0;
								}

								ListNode = UserSubFunnelNode.getNode("List");

								if (ListNode.hasNode("ActiveList")) {
									ActiveListNode = ListNode.getNode("ActiveList");

									if (ActiveListNode.getNodes().hasNext()) {
										// ActiveListChildNode=ActiveListNode.getNode(list_id);
										ActiveListChildNode = ActiveListNode.getNodes().nextNode();
										// out.println("ActiveListChildNode Name : "+ActiveListChildNode.getName());
										// temp_list_id=ActiveListChildNode.getName();
									}

									if (ActiveListChildNode.hasNode("Campaign")) {
										ListCampaignNode = ActiveListChildNode.getNode("Campaign");

									} else {
										ListCampaignNode = ActiveListChildNode.addNode("Campaign");
									}
								}
								// out.println("step 2");
							}
							// out.println("step 3");
							String Campaign_Date = null;
							DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							Date date = new Date();
							if (date_distance == 0) {
								int date_difference = ((int) count_Value1) * date_distance * 7;
								date.setDate(date.getDate() + date_difference);
								Campaign_Date = dateFormat.format(date);
							} else {
								int date_difference = ((int) count_Value1) * date_distance * 7;
								date.setDate(date.getDate() + date_difference);
								Campaign_Date = dateFormat.format(date);
							}
							count_Value1 = count_Value1 + 1;
							UserSubFunnelNode.setProperty("Counter", count_Value1);
							addcampaignnode = logged_in_user + "_" + type + "_" + String.valueOf(count_Value1);

							Node ListCampaignChildNode = ListCampaignNode.addNode(addcampaignnode);
							ListCampaignChildNode.setProperty("Campaign_Id", campaignid);
							ListCampaignChildNode.setProperty("List_Id", list_id);
							ListCampaignChildNode.setProperty("campaign_status", campaign_status);
							ListCampaignChildNode.setProperty("Campaign_Date", Campaign_Date);
							// out.println("step 4");
							if (!UserSubFunnelNode
									.hasNode(logged_in_user + "_" + type + "_" + String.valueOf(count_Value1))) {

								remoteusernode = UserSubFunnelNode
										.addNode(logged_in_user + "_" + type + "_" + String.valueOf(count_Value1));

								remoteusernode.setProperty("CreatedBy", logged_in_user);
								remoteusernode.setProperty("Subject", subject);
								remoteusernode.setProperty("Body", body);
								remoteusernode.setProperty("Type", type);
								remoteusernode.setProperty("Campaign_Id", campaignid);
								if (type.equals("Explore")) {
									remoteusernode.setProperty("List_Id", list_id);
								} else {
									remoteusernode.setProperty("List_Id", temp_list_id);
								}

								remoteusernode.setProperty("campaign_status", campaign_status);

								remoteusernode.setProperty("Campaign_Date", Campaign_Date);
							} else {
								remoteusernode = UserSubFunnelNode
										.getNode(logged_in_user + "_" + type + "_" + String.valueOf(count_Value1));

								remoteusernode.setProperty("CreatedBy", logged_in_user);
								remoteusernode.setProperty("Subject", subject);
								remoteusernode.setProperty("Body", body);
								remoteusernode.setProperty("Type", type);
								remoteusernode.setProperty("Campaign_Id", campaignid);
								if (type.equals("Explore")) {
									remoteusernode.setProperty("List_Id", list_id);
								} else {
									remoteusernode.setProperty("List_Id", temp_list_id);
								}
								remoteusernode.setProperty("campaign_status", campaign_status);
								remoteusernode.setProperty("Campaign_Date", Campaign_Date);
							}
							// Create a HashMap object called subscribers
							HashMap<String, String> subscribers = new HashMap<String, String>();

							// For Adding Campaign in SubscriberNode

							// url
							// http://localhost/restapi/campaign-list/listCampaignAdd.php?listid=2&campid=92

							// String campaignlisturl =
							// ip.getNode("ip").getProperty("Campaign_List_Url").getString();

							if (type.equals("Explore")) {
								// This method call will delete List From Campaign
								String campaignlisturl = ResourceBundle.getBundle("config")
										.getString("Campaign_List_Url");
								String campaignparameter = "?listid=" + list_id + "&campid=" + campaignid;
								String campaignlistresponse = this
										.sendpostdata(campaignlisturl, campaignparameter.replace(" ", "%20"), response)
										.replace("<pre>", "");
							}
							// out.println("Campaign_List_Response : " + campaignlistresponse);
							// String subscriberdataurl =
							// ip.getNode("ip").getProperty("Subscriber_Data_Url").getString();
							String subscriberdataurl = ResourceBundle.getBundle("config")
									.getString("Subscriber_Data_Url");
							String subscriberdataparameters = "?list_id=" + list_id;
							String subscriberdataresponse = this
									.sendpostdata(subscriberdataurl, subscriberdataparameters, response)
									.replace("<pre>", "");
							JSONObject subscriberdatajson = new JSONObject(subscriberdataresponse);
							JSONArray subscriberdata = subscriberdatajson.getJSONArray("data");
							for (int subscriberdataloop = 0; subscriberdataloop < subscriberdata
									.length(); subscriberdataloop++) {

								// Node For Adding Campaign Reference with subscribers
								JSONObject data_subscriber = subscriberdata.getJSONObject(subscriberdataloop);
								String subscriberemail = data_subscriber.getString("email");
								String subscriberid = data_subscriber.getString("id");
								subscribers.put(subscriberid, subscriberemail);

								Node subscribernode = session.getRootNode().getNode("content").getNode("LEAD_CONVERTER")
										.getNode("SUBSCRIBER");

								Node campaigninemailnode = null;
								Node subscriberemailnode = null;
								Node campaigninsubscribernode = null;

								if (!subscribernode.hasNode(subscriberemail.replace("@", "_"))) {
									subscriberemailnode = subscribernode.addNode(subscriberemail.replace("@", "_"));
								} else {

									subscriberemailnode = subscribernode.getNode(subscriberemail.replace("@", "_"));

								}

								if (!subscriberemailnode.hasNode("Campaign")) {

									campaignnode = subscriberemailnode.addNode("Campaign");

								} else {

									campaignnode = subscriberemailnode.getNode("Campaign");

								}
								if (!campaignnode.hasNode(addcampaignnode)) {

									addcampaigninsubscribernode = campaignnode.addNode(addcampaignnode);
									addcampaigninsubscribernode.setProperty("CreatedBy", logged_in_user);
									addcampaigninsubscribernode.setProperty("Subject", subject);
									addcampaigninsubscribernode.setProperty("Body", body);
									addcampaigninsubscribernode.setProperty("Type", type);
									addcampaigninsubscribernode.setProperty("List_Id", list_id);
								}

								else {

									addcampaigninsubscribernode = campaignnode.getNode(addcampaignnode);

									addcampaigninsubscribernode.setProperty("CreatedBy", logged_in_user);
									addcampaigninsubscribernode.setProperty("Subject", subject);
									addcampaigninsubscribernode.setProperty("Body", body);
									addcampaigninsubscribernode.setProperty("Type", type);
									addcampaigninsubscribernode.setProperty("List_Id", list_id);

								}

							}
							CampaignMongoDAO.addCampaignDetails(logged_in_user, funnelName, type,
									logged_in_user + "_" + type + "_" + String.valueOf(count_Value1), fromName,
									fromEmailAddress, campaignName, subject, body, type, campaignid, list_id,
									campaign_status, Campaign_Date, subscribers);
							/*
							 * if(!type.equals("Explore")){ // This method call will delete List From
							 * Campaign System.out.println("campaign_list_delete_response : "); new
							 * CampaignSheduleMongoDAO().campaignListDelete(list_id,campaignid); }
							 */

							session.save();
							// out.println("uploaded");

						}

					} else {
						out.println("User is not Valid");
					}
					// request.getRequestDispatcher("/content/mainui/.findex").forward(request,
					// response);
				} catch (Exception ex) {
					out.println("Message : " + ex.getMessage());
				}

			} else if (request.getRequestPathInfo().getExtension().equals("CampaignNodeAddBackUp")) {
				// out.println("CampaignNodeAdd Called");
				try {
					// String list_id = request.getParameter("list_id");

					long count_Value1 = 0;
					long funnel_count = 1;// this variable created by Akhil
					long sub_funnel_count = 1;// this variable created by Akhil
					Node campaignnode = null;
					Node funnelNode = null;// this variable created by Akhil
					Node subFunnelNode = null;// this variable created by Akhil
					Node content = session.getRootNode().getNode("content").getNode("user");
					Node content_ip = session.getRootNode().getNode("content");
					Node usernode = null;
					Node typenode = null;
					Node remoteusernode = null;
					Node leadconverternode = null;
					Node emailnode = null;
					String addcampaignnode = null;
					Node addcampaigninsubscribernode = null;

					String footer = request.getParameter("footer");
					// footer=footer+unnsubscriber_link;
					String send_a_webpage_url = request.getParameter("send_a_webpage_url");
					String compose_message_txt = request.getParameter("compose_message_txt");
					String body = request.getParameter("ckcontent");
					body = body + unnsubscriber_link;
					String subject = request.getParameter("subject");
					String campaignName = request.getParameter("campaignName");
					String fromName = request.getParameter("fromName");
					String fromEmailAddress = request.getParameter("fromEmailAddress");
					String funnelName = request.getParameter("funnelName");
					String type = request.getParameter("SubFunnelName");
					String DistanceBtnCampaign = request.getParameter("DistanceBtnCampaign");
					String list_id = request.getParameter("listid");
					// String logged_in_user = request.getParameter("logged_in_user");
					String logged_in_user = request.getParameter("logged_in_user").replace("@", "_");

					String fromfield = "Akhilesh Yadav UI"; // request.getRemoteUser();
					String replyto = request.getRemoteUser();
					// String embargo ="2019-01-07 06:43:02 PM"; //request.getParameter("date");
					// 2019-01-07 06:43:02 PM
					String embargo = "";
					String campaignvalue = request.getParameter("campaignvalue");

					// out.println("body : "+body);
					String bodyvalue = URLEncoder.encode(body);
					String campaignaddurl = ResourceBundle.getBundle("config").getString("Campaign_Add_Url_New");
					String campaignaddapiurlparameters = "subject=" + subject + "&fromfield=" + fromfield + "&replyto="
							+ replyto + "&message=" + body + "&textmessage=hii&footer=" + footer
							+ "&status=draft&sendformat=html&template=&embargo=" + embargo
							+ "&rsstemplate=&owner=1&htmlformatted=&repeatinterval=&repeatuntil=&requeueinterval=&requeueuntil=";
					String campaignresponse = this.sendHttpPostData(campaignaddurl,
							campaignaddapiurlparameters.replace(" ", "%20").replace("\r", "").replace("\n", ""),
							response).replace("<pre>", "");

					JSONObject campaignjson = new JSONObject(campaignresponse);
					String campaignstatus = campaignjson.getString("status");
					JSONObject data = campaignjson.getJSONObject("data");
					String campaignid = data.getString("id");
					String campaign_status = data.getString("status");
					// status

					JSONObject res_json_obj = new JSONObject();
					res_json_obj.put("campaignid", campaignid);
					// res_json_obj.put("body", body);
					// res_json_obj.put("bodyvalue", bodyvalue);

					out.println(res_json_obj.toString());

					// String logged_in_user="viki_gmail.com";
					if (campaignstatus.equals(("success"))) {

						if (!content.hasNode(logged_in_user)) {
							usernode = content.addNode(logged_in_user);
						} else {
							usernode = content.getNode(logged_in_user);
						}
						if (!usernode.hasNode("Lead_Converter")) {
							leadconverternode = usernode.addNode("Lead_Converter");
						} else {
							leadconverternode = usernode.getNode("Lead_Converter");
						}
						if (!leadconverternode.hasNode("Email")) {
							emailnode = leadconverternode.addNode("Email");
						} else {
							emailnode = leadconverternode.getNode("Email");
						}
						if (!emailnode.hasNode("Funnel")) {
							funnelNode = emailnode.addNode("Funnel");
						} else {
							funnelNode = emailnode.getNode("Funnel");
						}

						Node UserFunnelNode = null;// this variable created by Akhil
						Node UserSubFunnelNode = null;// this variable created by Akhil
						Node ListNode = null;// this variable created by Akhil
						Node ListCampaignNode = null;// this variable created by Akhil
						Node DraftListNode = null;
						Node ActiveListNode = null;
						Node ActiveListChildNode = null;
						if (!funnelNode.hasNode(funnelName)) {
							UserFunnelNode = funnelNode.addNode(funnelName);
						} else {
							UserFunnelNode = funnelNode.getNode(funnelName);
						}
						int date_distance = 0;
						if (!UserFunnelNode.hasNode(type)) {
							// out.println("if UserFunnelNode");
							campaignvalue = logged_in_user + "_" + type + "_" + String.valueOf(count_Value1 + 1);
							UserSubFunnelNode = UserFunnelNode.addNode(type);
							// Setting Subfunnel property
							UserSubFunnelNode.setProperty("Counter", count_Value1);
							UserSubFunnelNode.setProperty("Current_Campaign", campaignvalue);
							UserSubFunnelNode.setProperty("Distance", DistanceBtnCampaign);
							DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 2019-03-15 01:00:00
							// System.out.println("Today's Date : "+dateFormat.format(new Date()));
							UserSubFunnelNode.setProperty("Current_Date", dateFormat.format(new Date()));
							// UserSubFunnelNode.setProperty("No_of_days", noofdays);
							if (UserSubFunnelNode.hasNode("List")) {
								// out.println("if List");

								ListNode = UserSubFunnelNode.getNode("List");
								if (ListNode.hasNode("DraftList")) {
									DraftListNode = ListNode.getNode("DraftList");

								} else {
									DraftListNode = ListNode.addNode("DraftList");
									DraftListNode.setProperty("DraftListCounter", "0");
								}
								if (ListNode.hasNode("ActiveList")) {
									ActiveListNode = ListNode.getNode("ActiveList");
									if (type.equals("Explore")) {
										ActiveListChildNode = ActiveListNode.addNode(list_id);
										if (ActiveListChildNode.hasNode("Campaign")) {
											ListCampaignNode = ActiveListChildNode.getNode("Campaign");

										} else {
											ListCampaignNode = ActiveListChildNode.addNode("Campaign");
										}
									} else {

										String listname = "List_" + campaignvalue;
										// String listurl = content_ip.getNode("ip")
										// .getProperty("List_Add_Url").getString();
										String listurl = ResourceBundle.getBundle("config").getString("List_Add_Url");

										String listparameter = "?name=" + listname + "&description=This Belongs to "
												+ "&listorder=" + 90 + "&active=" + 1;
										String listresponse = this
												.sendpostdata(listurl, listparameter.replace(" ", "%20"), response)
												.replace("<pre>", "");
										// out.println("List Response " + listresponse);
										JSONObject listjson = new JSONObject(listresponse);
										String liststatusresponse = listjson.getString("status");
										// out.println("List Status Response : " +
										// liststatusresponse);
										JSONObject getjsonid = listjson.getJSONObject("data");
										temp_list_id = getjsonid.getString("id");
										// out.println("Created Enitce list id : "+listid);
										ActiveListChildNode = ActiveListNode.addNode(temp_list_id);
										if (ActiveListChildNode.hasNode("Campaign")) {
											ListCampaignNode = ActiveListChildNode.getNode("Campaign");

										} else {
											ListCampaignNode = ActiveListChildNode.addNode("Campaign");
										}

									}
								} else {
									ActiveListNode = ListNode.addNode("ActiveList");
									ActiveListNode.setProperty("ActiveListCounter", "1");
									if (type.equals("Explore")) {
										ActiveListChildNode = ActiveListNode.addNode(list_id);
										if (ActiveListChildNode.hasNode("Campaign")) {
											ListCampaignNode = ActiveListChildNode.getNode("Campaign");

										} else {
											ListCampaignNode = ActiveListChildNode.addNode("Campaign");
										}
									} else {
										String listname = "List_" + campaignvalue;
										// String listurl = content_ip.getNode("ip")
										// .getProperty("List_Add_Url").getString();
										String listurl = ResourceBundle.getBundle("config").getString("List_Add_Url");

										String listparameter = "?name=" + listname + "&description=This Belongs to "
												+ "&listorder=" + 90 + "&active=" + 1;
										String listresponse = this
												.sendpostdata(listurl, listparameter.replace(" ", "%20"), response)
												.replace("<pre>", "");
										// out.println("List Response " + listresponse);
										JSONObject listjson = new JSONObject(listresponse);
										String liststatusresponse = listjson.getString("status");
										// out.println("List Status Response : " +
										// liststatusresponse);
										JSONObject getjsonid = listjson.getJSONObject("data");
										temp_list_id = getjsonid.getString("id");

										ActiveListChildNode = ActiveListNode.addNode(temp_list_id);
										if (ActiveListChildNode.hasNode("Campaign")) {
											ListCampaignNode = ActiveListChildNode.getNode("Campaign");

										} else {
											ListCampaignNode = ActiveListChildNode.addNode("Campaign");
										}

									}

								}
							} else {
								// out.println("else List");
								ListNode = UserSubFunnelNode.addNode("List");
								if (ListNode.hasNode("DraftList")) {
									DraftListNode = ListNode.getNode("DraftList");

								} else {
									DraftListNode = ListNode.addNode("DraftList");
									DraftListNode.setProperty("DraftListCounter", "0");
								}
								if (ListNode.hasNode("ActiveList")) {
									// out.println("if ActiveList");
									ActiveListNode = ListNode.getNode("ActiveList");
									if (type.equals("Explore")) {
										// out.println("if Explore");
										ActiveListChildNode = ActiveListNode.addNode(list_id);
										if (ActiveListChildNode.hasNode("Campaign")) {
											ListCampaignNode = ActiveListChildNode.getNode("Campaign");

										} else {
											ListCampaignNode = ActiveListChildNode.addNode("Campaign");
										}
									} else {
										// out.println("else Explore");
										String listname = "List_" + campaignvalue;
										// String listurl = content_ip.getNode("ip")
										// .getProperty("List_Add_Url").getString();
										String listurl = ResourceBundle.getBundle("config").getString("List_Add_Url");

										String listparameter = "?name=" + listname + "&description=This Belongs to "
												+ "&listorder=" + 90 + "&active=" + 1;
										String listresponse = this
												.sendpostdata(listurl, listparameter.replace(" ", "%20"), response)
												.replace("<pre>", "");
										// out.println("List Response " + listresponse);
										JSONObject listjson = new JSONObject(listresponse);
										String liststatusresponse = listjson.getString("status");
										// out.println("List Status Response : " +
										// liststatusresponse);
										JSONObject getjsonid = listjson.getJSONObject("data");
										temp_list_id = getjsonid.getString("id");

										ActiveListChildNode = ActiveListNode.addNode(temp_list_id);
										if (ActiveListChildNode.hasNode("Campaign")) {
											ListCampaignNode = ActiveListChildNode.getNode("Campaign");

										} else {
											ListCampaignNode = ActiveListChildNode.addNode("Campaign");
										}

									}
								} else {
									// out.println("if ActiveList");
									ActiveListNode = ListNode.addNode("ActiveList");
									ActiveListNode.setProperty("ActiveListCounter", "1");
									if (type.equals("Explore")) {
										ActiveListChildNode = ActiveListNode.addNode(list_id);
										if (ActiveListChildNode.hasNode("Campaign")) {
											ListCampaignNode = ActiveListChildNode.getNode("Campaign");

										} else {
											ListCampaignNode = ActiveListChildNode.addNode("Campaign");
										}
									} else {
										String listname = "List_" + campaignvalue;
										// String listurl = content_ip.getNode("ip")
										// .getProperty("List_Add_Url").getString();
										String listurl = ResourceBundle.getBundle("config").getString("List_Add_Url");

										String listparameter = "?name=" + listname + "&description=This Belongs to "
												+ "&listorder=" + 90 + "&active=" + 1;
										String listresponse = this
												.sendpostdata(listurl, listparameter.replace(" ", "%20"), response)
												.replace("<pre>", "");
										// out.println("List Response " + listresponse);
										JSONObject listjson = new JSONObject(listresponse);
										String liststatusresponse = listjson.getString("status");
										// out.println("List Status Response : " +
										// liststatusresponse);
										JSONObject getjsonid = listjson.getJSONObject("data");
										temp_list_id = getjsonid.getString("id");

										ActiveListChildNode = ActiveListNode.addNode(temp_list_id);
										if (ActiveListChildNode.hasNode("Campaign")) {
											ListCampaignNode = ActiveListChildNode.getNode("Campaign");

										} else {
											ListCampaignNode = ActiveListChildNode.addNode("Campaign");
										}

									}

								}
							}
							date_distance = 0;
							// out.println("step 1");
						} else {
							// out.println("else UserFunnelNode");
							UserSubFunnelNode = UserFunnelNode.getNode(type);

							count_Value1 = UserSubFunnelNode.getProperty("Counter").getLong();
							UserSubFunnelNode.setProperty("Distance", DistanceBtnCampaign);
							if (UserSubFunnelNode.hasProperty("Distance")) {
								String distance = UserSubFunnelNode.getProperty("Distance").getString().replace(" Week",
										"");
								date_distance = Integer.parseInt(distance);
							} else {
								date_distance = 0;
							}

							ListNode = UserSubFunnelNode.getNode("List");

							if (ListNode.hasNode("ActiveList")) {
								ActiveListNode = ListNode.getNode("ActiveList");

								if (ActiveListNode.getNodes().hasNext()) {
									// ActiveListChildNode=ActiveListNode.getNode(list_id);
									ActiveListChildNode = ActiveListNode.getNodes().nextNode();
									// out.println("ActiveListChildNode Name : "+ActiveListChildNode.getName());
								}

								if (ActiveListChildNode.hasNode("Campaign")) {
									ListCampaignNode = ActiveListChildNode.getNode("Campaign");

								} else {
									ListCampaignNode = ActiveListChildNode.addNode("Campaign");
								}
							}
							// out.println("step 2");
						}
						// out.println("step 3");
						String Campaign_Date = null;
						DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						Date date = new Date();
						if (date_distance == 0) {
							int date_difference = ((int) count_Value1) * date_distance * 7;
							date.setDate(date.getDate() + date_difference);
							Campaign_Date = dateFormat.format(date);
						} else {
							int date_difference = ((int) count_Value1) * date_distance * 7;
							date.setDate(date.getDate() + date_difference);
							Campaign_Date = dateFormat.format(date);
						}
						count_Value1 = count_Value1 + 1;
						UserSubFunnelNode.setProperty("Counter", count_Value1);
						addcampaignnode = logged_in_user + "_" + type + "_" + String.valueOf(count_Value1);

						Node ListCampaignChildNode = ListCampaignNode.addNode(addcampaignnode);
						ListCampaignChildNode.setProperty("Campaign_Id", campaignid);
						ListCampaignChildNode.setProperty("List_Id", list_id);
						ListCampaignChildNode.setProperty("campaign_status", campaign_status);
						ListCampaignChildNode.setProperty("Campaign_Date", Campaign_Date);
						// out.println("step 4");
						if (!UserSubFunnelNode
								.hasNode(logged_in_user + "_" + type + "_" + String.valueOf(count_Value1))) {

							remoteusernode = UserSubFunnelNode
									.addNode(logged_in_user + "_" + type + "_" + String.valueOf(count_Value1));

							remoteusernode.setProperty("CreatedBy", logged_in_user);
							remoteusernode.setProperty("Subject", subject);
							remoteusernode.setProperty("Body", body);
							remoteusernode.setProperty("Type", type);
							remoteusernode.setProperty("Campaign_Id", campaignid);
							if (type.equals("Explore")) {
								remoteusernode.setProperty("List_Id", list_id);
							} else {
								remoteusernode.setProperty("List_Id", "NoList");
							}

							remoteusernode.setProperty("campaign_status", campaign_status);

							remoteusernode.setProperty("Campaign_Date", Campaign_Date);
						} else {
							remoteusernode = UserSubFunnelNode
									.getNode(logged_in_user + "_" + type + "_" + String.valueOf(count_Value1));

							remoteusernode.setProperty("CreatedBy", logged_in_user);
							remoteusernode.setProperty("Subject", subject);
							remoteusernode.setProperty("Body", body);
							remoteusernode.setProperty("Type", type);
							remoteusernode.setProperty("Campaign_Id", campaignid);
							if (type.equals("Explore")) {
								remoteusernode.setProperty("List_Id", list_id);
							} else {
								remoteusernode.setProperty("List_Id", "NoList");
							}
							remoteusernode.setProperty("campaign_status", campaign_status);
							remoteusernode.setProperty("Campaign_Date", Campaign_Date);
						}
						// Create a HashMap object called subscribers
						HashMap<String, String> subscribers = new HashMap<String, String>();

						// For Adding Campaign in SubscriberNode

						// url
						// http://localhost/restapi/campaign-list/listCampaignAdd.php?listid=2&campid=92

						// String campaignlisturl =
						// ip.getNode("ip").getProperty("Campaign_List_Url").getString();
						String campaignlisturl = ResourceBundle.getBundle("config").getString("Campaign_List_Url");
						String campaignparameter = "?listid=" + list_id + "&campid=" + campaignid;
						String campaignlistresponse = this
								.sendpostdata(campaignlisturl, campaignparameter.replace(" ", "%20"), response)
								.replace("<pre>", "");
						// out.println("Campaign_List_Response : " + campaignlistresponse);
						// String subscriberdataurl =
						// ip.getNode("ip").getProperty("Subscriber_Data_Url").getString();
						String subscriberdataurl = ResourceBundle.getBundle("config").getString("Subscriber_Data_Url");
						String subscriberdataparameters = "?list_id=" + list_id;
						String subscriberdataresponse = this
								.sendpostdata(subscriberdataurl, subscriberdataparameters, response)
								.replace("<pre>", "");
						JSONObject subscriberdatajson = new JSONObject(subscriberdataresponse);
						JSONArray subscriberdata = subscriberdatajson.getJSONArray("data");
						for (int subscriberdataloop = 0; subscriberdataloop < subscriberdata
								.length(); subscriberdataloop++) {

							// Node For Adding Campaign Reference with subscribers
							JSONObject data_subscriber = subscriberdata.getJSONObject(subscriberdataloop);
							String subscriberemail = data_subscriber.getString("email");
							String subscriberid = data_subscriber.getString("id");
							subscribers.put(subscriberid, subscriberemail);

							Node subscribernode = session.getRootNode().getNode("content").getNode("LEAD_CONVERTER")
									.getNode("SUBSCRIBER");

							Node campaigninemailnode = null;
							Node subscriberemailnode = null;
							Node campaigninsubscribernode = null;

							if (!subscribernode.hasNode(subscriberemail.replace("@", "_"))) {
								subscriberemailnode = subscribernode.addNode(subscriberemail.replace("@", "_"));
							} else {

								subscriberemailnode = subscribernode.getNode(subscriberemail.replace("@", "_"));

							}

							if (!subscriberemailnode.hasNode("Campaign")) {

								campaignnode = subscriberemailnode.addNode("Campaign");

							} else {

								campaignnode = subscriberemailnode.getNode("Campaign");

							}
							if (!campaignnode.hasNode(addcampaignnode)) {

								addcampaigninsubscribernode = campaignnode.addNode(addcampaignnode);
								addcampaigninsubscribernode.setProperty("CreatedBy", logged_in_user);
								addcampaigninsubscribernode.setProperty("Subject", subject);
								addcampaigninsubscribernode.setProperty("Body", body);
								addcampaigninsubscribernode.setProperty("Type", type);
								addcampaigninsubscribernode.setProperty("List_Id", list_id);
							}

							else {

								addcampaigninsubscribernode = campaignnode.getNode(addcampaignnode);

								addcampaigninsubscribernode.setProperty("CreatedBy", logged_in_user);
								addcampaigninsubscribernode.setProperty("Subject", subject);
								addcampaigninsubscribernode.setProperty("Body", body);
								addcampaigninsubscribernode.setProperty("Type", type);
								addcampaigninsubscribernode.setProperty("List_Id", list_id);

							}

						}
						CampaignMongoDAO.addCampaignDetails(logged_in_user, funnelName, type,
								logged_in_user + "_" + type + "_" + String.valueOf(count_Value1), fromName,
								fromEmailAddress, campaignName, subject, body, type, campaignid, list_id,
								campaign_status, Campaign_Date, subscribers);

						session.save();
						// out.println("uploaded");

					}

					// request.getRequestDispatcher("/content/mainui/.findex").forward(request,
					// response);
				} catch (Exception ex) {
					out.println("Message : " + ex.getMessage());
				}

			} else if (request.getRequestPathInfo().getExtension().equals("updateCampaign")) {
				// out.println("updateCampaign");
				// String id="569";
				String id = request.getParameter("id");
				String body = request.getParameter("ckcontent");
				String subject = request.getParameter("subject");
				// String type = request.getParameter("type");
				String fromfield = "Akhilesh Yadav UI"; // request.getRemoteUser();
				String replyto = request.getRemoteUser();
				// String embargo ="2019-01-07 06:43:02 PM"; //request.getParameter("date");
				// 2019-01-07 06:43:02 PM
				String embargo = request.getParameter("embargo");
				String campaignvalue = request.getParameter("campaignvalue");

				String campaignName = request.getParameter("campaignName");
				String fromName = request.getParameter("fromName");
				String fromEmailAddress = request.getParameter("fromEmailAddress");
				String funnelName = request.getParameter("funnelName");
				String type = request.getParameter("SubFunnelName");
				String DistanceBtnCampaign = request.getParameter("DistanceBtnCampaign");
				String list_id = request.getParameter("listid");

				// String bodyvalue=URLEncoder.encode(body);
				// String campaignaddurl =
				// ip.getNode("ip").getProperty("Campaign_Update_Url").getString();
				// String campaignaddurl =
				// ResourceBundle.getBundle("config").getString("phplist_api_campaignUpdate");
				String campaignaddurl = ResourceBundle.getBundle("config").getString("embargoupdateurl");
				String campaignaddapiurlparameters = "id=" + id + "&subject=" + subject;
				/*
				 * String campaignaddapiurlparameters = "?id="+id+"&subject=" + subject +
				 * "&fromfield=" + fromfield + "&replyto=" + replyto + "&message="+body+
				 * "&textmessage=hii&footer=footer&status=draft&sendformat=html&template=&embargo="
				 * + embargo+"&rsstemplate=&owner=1&htmlformatted=";
				 * 
				 * String campaignaddapiurlparameters1 = "?id="+id+"&subject=" + subject +
				 * "&fromfield=" + fromfield + "&replyto=" + replyto + "&message="+body+
				 * "&textmessage=hii&footer=footer&status=draft&sendformat=html&template=&embargo="
				 * + embargo+"&rsstemplate=&owner=1&htmlformatted=";
				 * 
				 * String campaignaddapiurlparameters2 = "?id="+id+"&subject=" + subject +
				 * "&fromfield=" + fromfield + "&replyto=" + replyto + "&message="+body+
				 * "&textmessage=hii&footer=footer&status=draft&sendformat=html&template=&embargo="
				 * + embargo+
				 * "&rsstemplate=&owner=1&htmlformatted=&repeatinterval=&repeatuntil=&requeueinterval=&requeueuntil=";
				 * 
				 * String campaignresponse = this.sendpostdata(campaignaddurl,
				 * campaignaddapiurlparameters.replace(" ", "%20").replace("\r",
				 * "").replace("\n", ""), response) .replace("<pre>", "");
				 */
				String campaignresponse = this.sendHttpPostData(campaignaddurl,
						campaignaddapiurlparameters.replace(" ", "%20").replace("\r", "").replace("\n", ""), response);

				out.println("campaignresponse : " + campaignresponse);

				/*
				 * JSONObject campaignjson = new JSONObject(campaignresponse); String
				 * campaignstatus = campaignjson.getString("status"); JSONObject data =
				 * campaignjson.getJSONObject("data"); String campaignid = data.getString("id");
				 * 
				 * JSONObject res_json_obj=new JSONObject(); res_json_obj.put("campaignid",
				 * campaignid); //res_json_obj.put("body", body);
				 * //res_json_obj.put("bodyvalue", bodyvalue);
				 * 
				 * out.println(res_json_obj.toString());
				 */
			} else if (request.getRequestPathInfo().getExtension().equals("updateEmbargo")) {

				try {
					JSONObject mainjson = new JSONObject();
					String group = "";
					group = request.getParameter("group");
					String logged_in_user = request.getParameter("remoteuser").replace("@", "_");
					// shopping cart method call
					Node shoppingnode = null;

					String expstatus = new FreetrialShoppingCartUpdate().checkFreeTrialExpirationStatus(logged_in_user);

					shoppingnode = new FreetrialShoppingCartUpdate().getLeadAutoConverterNode(expstatus, logged_in_user,
							group, session, response);
					// shoppingnode /content/services/64234/g1/LeadAutoConverter

					String campaignCatogory = request.getParameter("camp_catogery");

					String campaignid = request.getParameter("campaignid");
					String embargo = request.getParameter("embargo");

					String FName = request.getParameter("funnelName");
					// String SubFunnelName = request.getParameter("SubFunnelName");
					String subscriber_listid = request.getParameter("listid");

					// String id="569";
					String campaignaddurl = ResourceBundle.getBundle("config").getString("embargoupdateurl");
					String campaignaddapiurlparameters = "id=" + campaignid + "&embargo=" + embargo;
					out.println("campaignaddapiurlparameters : " + campaignaddapiurlparameters);
					String campaignlistresponse = this
							.sendHttpPostData(campaignaddurl, campaignaddapiurlparameters.replace(" ", "%20"), response)
							.replace("<pre>", "");
					CampaignMongoDAO.updateCampaignDateInCampaignDetails(campaignid, embargo);
					/*
					 * out.println("campaignresponse : "+campaignlistresponse); String
					 * status_response=this.campaignStatusUpdate(campaignid,"submitted",response);
					 * out.println("status_response : "+status_response);
					 */
					out.println("logged_in_user : " + logged_in_user);

					// Node usernode_direct
					// =session.getRootNode().getNode("content").getNode("user").getNode(logged_in_user);
					// out.println("usernode_direct : "+usernode_direct);

//						Node usernode = session.getRootNode().getNode("content").getNode("user").getNode(logged_in_user);
//						out.println("updateCampaignDate : Step 1 usernode Name "+usernode.getName());
					if (shoppingnode != null) {

						if (shoppingnode.getNode("Email").getNode("Funnel").hasNode(FName)) {
							out.println("updateCampaignDate : Step 1");
							Node funnelNode = shoppingnode.getNode("Email").getNode("Funnel").getNode(FName);
							out.println("updateCampaignDate : Step 1.1");
							String funnelName = funnelNode.getName();// currentcampaign
							out.println("updateCampaignDate : Step 1.1 funnelName : " + funnelName);
							if (funnelNode.hasNode(campaignCatogory)) {
								out.println("updateCampaignDate : Step 2");
								Node subFunnelNode = funnelNode.getNode(campaignCatogory);
								out.println("subFunnelNode Name : " + subFunnelNode.getName());
								if (subFunnelNode.hasNode("List")) {
									Node ListNode = subFunnelNode.getNode("List");
									if (ListNode.hasNode("ActiveList")) {
										Node ActiveListNode = ListNode.getNode("ActiveList");

										if (ActiveListNode.hasNodes()) {
											NodeIterator ActiveListNodeItr = ActiveListNode.getNodes();
											while (ActiveListNodeItr.hasNext()) {
												Node ActiveListChildNode = ActiveListNodeItr.nextNode();
												if (ActiveListChildNode.hasNode("Campaign")) {
													Node ActiveListChildCampaignNode = ActiveListChildNode
															.getNode("Campaign");
													NodeIterator ActiveListChildCampaignNodeItr = ActiveListChildCampaignNode
															.getNodes();
													while (ActiveListChildCampaignNodeItr.hasNext()) {
														Node ActiveListChildCampaignChildNode = ActiveListChildCampaignNodeItr
																.nextNode();
														out.println("ActiveListChildCampaignChildNode Name : "
																+ ActiveListChildCampaignChildNode.getName());
														if (ActiveListChildCampaignChildNode
																.hasProperty("Campaign_Id")) {
															String Temp_Campaign_Id = ActiveListChildCampaignChildNode
																	.getProperty("Campaign_Id").getString();
															if (Temp_Campaign_Id.equals(campaignid)) {
																out.println("ActiveListChildCampaignChildNode Name : "
																		+ ActiveListChildCampaignChildNode.getName());
																ActiveListChildCampaignChildNode
																		.setProperty("Campaign_Date", embargo);
															}
														}
														/*
														 * if(ActiveListChildCampaignChildNode.hasProperty(
														 * "Campaign_Date")){
														 * ActiveListChildCampaignChildNode.setProperty("Campaign_Date",
														 * embargo); }
														 */
													}
												}
											}
										}
									}
								}
								NodeIterator subFunnelNodeItr = subFunnelNode.getNodes();
								while (subFunnelNodeItr.hasNext()) {
									Node ActiveListChildNode = subFunnelNodeItr.nextNode();
									if (ActiveListChildNode.hasProperty("Campaign_Id")) {
										String Temp_Campaign_Id = ActiveListChildNode.getProperty("Campaign_Id")
												.getString();
										if (Temp_Campaign_Id.equals(campaignid)) {
											out.println("ActiveListChildCampaignChildNode Name : "
													+ ActiveListChildNode.getName());
											ActiveListChildNode.setProperty("Campaign_Date", embargo);
										}
									}
								}
							}
						}
					}else {
						out.println("User is not Valid");
					}

					/*
					 * NodeIterator useritr =
					 * session.getRootNode().getNode("content").getNode("user").getNodes();
					 * 
					 * while (useritr.hasNext()) { Node usernode = useritr.nextNode(); }
					 */
					out.println(mainjson);
					session.save();
				} catch (Exception ex) {
					// out.println("Exception ex :=" + ex.getMessage() + ex.getCause());

					try {
						JSONObject errordatajson = new JSONObject();
						errordatajson.put("Error", "Null");
						errordatajson.put("Exception", ex.getMessage().toString());
						errordatajson.put("Cause", ex.getCause().toString());
						out.println(errordatajson);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						out.println("Exception ex :=" + ex.getMessage() + ex.getCause());
					}

				}

			} else if (request.getRequestPathInfo().getExtension().equals("addCampaign")) {
				String body = request.getParameter("ckcontent");
				String subject = request.getParameter("subject");
				// String type = request.getParameter("type");
				String fromfield = "Akhilesh Yadav UI"; // request.getRemoteUser();
				String replyto = request.getRemoteUser();
				// String embargo ="2019-01-07 06:43:02 PM"; //request.getParameter("date");
				// 2019-01-07 06:43:02 PM
				String embargo = "";
				String campaignvalue = request.getParameter("campaignvalue");

				String campaignName = request.getParameter("campaignName");
				String fromName = request.getParameter("fromName");
				String fromEmailAddress = request.getParameter("fromEmailAddress");
				String funnelName = request.getParameter("funnelName");
				String type = request.getParameter("SubFunnelName");
				String DistanceBtnCampaign = request.getParameter("DistanceBtnCampaign");
				String list_id = request.getParameter("listid");

				// out.println("body : "+body);
				String bodyvalue = URLEncoder.encode(body);
				// out.println("bodyvalue : "+bodyvalue);
				// String campaignaddurl =
				// ip.getNode("ip").getProperty("Campaign_Add_Url").getString();
				String campaignaddurl = ResourceBundle.getBundle("config").getString("Campaign_Add_Url_New");
				String campaignaddapiurlparameters = "subject=" + subject + "&fromfield=" + fromfield + "&replyto="
						+ replyto + "&message=" + body
						+ "&textmessage=hii&footer=footer&status=draft&sendformat=html&template=&embargo=" + embargo
						+ "&rsstemplate=&owner=1&htmlformatted=&repeatinterval=&repeatuntil=&requeueinterval=&requeueuntil=";
				// out.println("campaignaddapiurlparameters : "+campaignaddapiurlparameters);
				String campaignresponse = this.sendHttpPostData(campaignaddurl,
						campaignaddapiurlparameters.replace(" ", "%20").replace("\r", "").replace("\n", ""), response)
						.replace("<pre>", "");
				out.println("campaignresponse : " + campaignresponse);
			} else if (request.getRequestPathInfo().getExtension().equals("placeCampaignInQueueforSending1")) {

				String id = request.getParameter("id");
				String status = request.getParameter("status");
				String campaignaddurl = ResourceBundle.getBundle("config").getString("campaignStatusUpdate");
				String campaignaddapiurlparameters = "id=" + id + "&status=" + status;
				String campaignresponse = this.sendHttpPostData(campaignaddurl,
						campaignaddapiurlparameters.replace(" ", "%20").replace("\r", "").replace("\n", ""), response)
						.replace("<pre>", "");
				out.println("campaignresponse : " + campaignresponse);
			} else if (request.getRequestPathInfo().getExtension().equals("placeCampaignInQueueforSending")) {
				// out.println("placeCampaignInQueueforSending2 step 1 ");
				String id = request.getParameter("id");
				String status = request.getParameter("status");
				// out.println("placeCampaignInQueueforSending2 step 1 id : "+id+" status :
				// "+status);
				String campaignaddurl = ResourceBundle.getBundle("config").getString("campaignStatusUpdate");
				String campaignaddapiurlparameters = "?id=" + id + "&status=" + status;
				// out.println("campaignaddapiurlparameters : "+campaignaddapiurlparameters);
				String campaignresponse = this.sendpostdata(campaignaddurl,
						campaignaddapiurlparameters.replace(" ", "%20").replace("\r", "").replace("\n", ""), response)
						.replace("<pre>", "");
				// out.println("placeCampaignInQueueforSending2 step 3 : ");
				out.println("campaignresponse : " + campaignresponse);
			} else if (request.getRequestPathInfo().getExtension().equals("processQueue")) {

				String campid = request.getParameter("campid");
				String campaignlisturl = ResourceBundle.getBundle("config").getString("processqueue");
				// String campaignparameter = "?listid=" + "680" + "&campid=" + "650";
				String campaignparameter = "?campid=" + campid;
				String campaignlistresponse = this
						.sendpostdata(campaignlisturl, campaignparameter.replace(" ", "%20"), response)
						.replace("<pre>", "");
				out.println("Process Queue Response :" + campaignlistresponse);
				LogByFileWriter
						.logger_info("UICreateCampaignServlet : " + "Process Queue Response :" + campaignlistresponse);
			}

		} catch (Exception e) {

			out.println("Exception ex : : : " + e.getStackTrace());
		}

	}

	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		JSONArray mainarray = new JSONArray();
		JSONObject jsonobject = new JSONObject();
		String listid = null;

		String remoteuser = request.getRemoteUser();

		if (request.getRequestPathInfo().getExtension().equals("processQueue")) {

			String campaignlisturl = ResourceBundle.getBundle("config").getString("processqueue");
			// String campaignparameter = "?listid=" + "680" + "&campid=" + "650";
			String campaignparameter = "?campid=680"; // unuse parameter
			String campaignlistresponse = this
					.sendpostdata(campaignlisturl, campaignparameter.replace(" ", "%20"), response)
					.replace("<pre>", "");
			out.println("Process Queue Response :" + campaignlistresponse);
			LogByFileWriter
					.logger_info("UICreateCampaignServlet : " + "Process Queue Response :" + campaignlistresponse);
		} else if (request.getRequestPathInfo().getExtension().equals("updateCampaignDate")) {
			try {

				Session session = null;
				session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
				out.println("updateCampaignDate : Step 0");
				JSONObject mainjson = new JSONObject();
				String campaignCatogory = request.getParameter("camp_catogery");
				String campaignid = request.getParameter("campaignid");
				String embargo = request.getParameter("embargo");
				out.println("updateCampaignDate : Step 0.1");
				String FName = request.getParameter("funnelName");
				// String SubFunnelName = request.getParameter("SubFunnelName");
				String subscriber_listid = request.getParameter("listid");
				out.println("updateCampaignDate : Step 0.2 FName FName : " + FName);

				NodeIterator useritr = session.getRootNode().getNode("content").getNode("user").getNodes();
				while (useritr.hasNext()) {
					Node usernode = useritr.nextNode();
					out.println("updateCampaignDate : Step 1 usernode Name " + usernode.getName());
					if (!usernode.getName().equals("<%=request.getRemoteUser()%>")) {
						if (usernode.hasNode("Lead_Converter")) {
							if (usernode.getNode("Lead_Converter").getNode("Email").getNode("Funnel").hasNode(FName)) {
								out.println("updateCampaignDate : Step 1");
								Node funnelNode = usernode.getNode("Lead_Converter").getNode("Email").getNode("Funnel")
										.getNode(FName);
								out.println("updateCampaignDate : Step 1.1");
								String funnelName = funnelNode.getName();// currentcampaign
								out.println("updateCampaignDate : Step 1.1 funnelName : " + funnelName);
								if (funnelNode.hasNode(campaignCatogory)) {
									out.println("updateCampaignDate : Step 2");
									Node subFunnelNode = funnelNode.getNode(campaignCatogory);
									out.println("subFunnelNode Name : " + subFunnelNode.getName());
									if (subFunnelNode.hasNode("List")) {
										Node ListNode = subFunnelNode.getNode("List");
										if (ListNode.hasNode("ActiveList")) {
											Node ActiveListNode = ListNode.getNode("ActiveList");

											if (ActiveListNode.hasNodes()) {
												NodeIterator ActiveListNodeItr = ActiveListNode.getNodes();
												while (ActiveListNodeItr.hasNext()) {
													Node ActiveListChildNode = ActiveListNodeItr.nextNode();
													if (ActiveListChildNode.hasNode("Campaign")) {
														Node ActiveListChildCampaignNode = ActiveListChildNode
																.getNode("Campaign");
														NodeIterator ActiveListChildCampaignNodeItr = ActiveListChildCampaignNode
																.getNodes();
														while (ActiveListChildCampaignNodeItr.hasNext()) {
															Node ActiveListChildCampaignChildNode = ActiveListChildCampaignNodeItr
																	.nextNode();
															out.println("ActiveListChildCampaignChildNode Name : "
																	+ ActiveListChildCampaignChildNode.getName());
															if (ActiveListChildCampaignChildNode
																	.hasProperty("Campaign_Date")) {
																ActiveListChildCampaignChildNode
																		.setProperty("Campaign_Date", embargo);
															}
														}
													}
												}
											}
										}
									}
									NodeIterator subFunnelNodeItr = subFunnelNode.getNodes();
									while (subFunnelNodeItr.hasNext()) {
										Node ActiveListChildNode = subFunnelNodeItr.nextNode();
										if (ActiveListChildNode.hasProperty("Campaign_Id")) {
											String Temp_Campaign_Id = ActiveListChildNode.getProperty("Campaign_Id")
													.getString();
											if (Temp_Campaign_Id.equals(campaignid)) {
												out.println("ActiveListChildCampaignChildNode Name : "
														+ ActiveListChildNode.getName());
												ActiveListChildNode.setProperty("Campaign_Date", embargo);
											}
										}
									}
								}
							}
						}
					}

				}
				out.println(mainjson);
				session.save();
			} catch (Exception ex) {
				// out.println("Exception ex :=" + ex.getMessage() + ex.getCause());

				try {
					JSONObject errordatajson = new JSONObject();
					errordatajson.put("Error", "Null");
					errordatajson.put("Exception", ex.getMessage().toString());
					errordatajson.put("Cause", ex.getCause().toString());
					out.println(errordatajson);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					out.println("Exception ex :=" + ex.getMessage() + ex.getCause());
				}

			}
		}else if (request.getRequestPathInfo().getExtension().equals("getFunnel")) {
			String userName = request.getParameter("userName").replace("@", "_");
			
			try{
				JSONArray funnel_json_obj=FunnelDetailsMongoDAO.getFunnelList(userName);
				out.println(funnel_json_obj);
			}catch (Exception e) {
				// TODO: handle exception
				out.println("exc"+e);
			}
			//out.println(funnel_json_obj);
			
		}else if (request.getRequestPathInfo().getExtension().equals("getListFunnelDetail")) {
			String userName = request.getParameter("userName").replace("@", "_");
			String funnelName = request.getParameter("funnelName");
			JSONObject category_json_obj=FunnelDetailsMongoDAO.getListFunnelDetail(userName,funnelName,"");
			out.println(category_json_obj);
			
		}
		
		//
	}

	public String campaignStatusUpdate(String id, String status, SlingHttpServletResponse response)
			throws ServletException, IOException {
		String campaignaddurl = ResourceBundle.getBundle("config").getString("campaignStatusUpdate");
		String campaignaddapiurlparameters = "?id=" + id + "&status=" + status;
		// out.println("campaignaddapiurlparameters : "+campaignaddapiurlparameters);
		String campaignresponse = this
				.sendpostdata(campaignaddurl,
						campaignaddapiurlparameters.replace(" ", "%20").replace("\r", "").replace("\n", ""), response)
				.replace("<pre>", "");
		return campaignresponse;
	}

	public String sendHttpPostData(String campaignaddurl, String campaignaddapiurlparameters,
			SlingHttpServletResponse response) throws ServletException, IOException {

		PrintWriter out = response.getWriter();
		URL url = new URL(campaignaddurl);
		/*
		 * Map<String,Object> params = new LinkedHashMap<String,Object>();
		 * params.put("name", "Freddie the Fish"); params.put("email",
		 * "fishie@seamail.example.com"); params.put("reply_to_thread", 10394);
		 * params.put("message",
		 * "Shark attacks in Botany Bay have gotten out of control. We need more defensive dolphins to protect the schools here, but Mayor Porpoise is too busy stuffing his snout with lobsters. He's so shellfish."
		 * );
		 * 
		 * StringBuilder postData = new StringBuilder(); for (Map.Entry<String,Object>
		 * param : params.entrySet()) { if (postData.length() != 0)
		 * postData.append('&'); postData.append(URLEncoder.encode(param.getKey(),
		 * "UTF-8")); postData.append('=');
		 * postData.append(URLEncoder.encode(String.valueOf(param.getValue()),
		 * "UTF-8")); }
		 */
		// System.out.print("postData.toString() " +postData.toString());

		byte[] postDataBytes = campaignaddapiurlparameters.toString().getBytes("UTF-8");

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
		conn.setDoOutput(true);
		conn.getOutputStream().write(postDataBytes);

		Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

		// for (int c; (c = in.read()) >= 0;)
		// System.out.print((char)c);

		StringBuffer buffer = new StringBuffer();
		for (int c; (c = in.read()) >= 0;)
			buffer.append((char) c);
		System.out.println("response : " + buffer.toString());
		return buffer.toString();

	}

	public String sendpostdata(String callurl, String urlParameters, SlingHttpServletResponse response)
			throws ServletException, IOException {

		PrintWriter out = response.getWriter();
		// out.println("inside sendpostdata urlParameters :" + urlParameters);
		URL url = new URL(callurl + urlParameters.replace("\\", ""));
		// out.println("inside sendpostdata Url :" + url);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setUseCaches(false);
		conn.setRequestMethod("POST");

		//
		OutputStream writer = conn.getOutputStream();

		writer.write(urlParameters.getBytes());
		// out.println("Writer Url : "+writer);
		int responseCode = conn.getResponseCode();
		// out.println("POST Response Code :: " + responseCode);
		StringBuffer buffer = new StringBuffer();
		//
		if (responseCode == 200) { // success
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String inputLine;

			while ((inputLine = in.readLine()) != null) {
				buffer.append(inputLine);
			}
			in.close();
			//
			// out.println(buffer.toString());
		} else {
			out.println("POST request not worked");
		}
		writer.flush();
		writer.close();
		return buffer.toString();

	}

	public String sendpostdataToCreateList(String callurl, String urlParameters, SlingHttpServletResponse response)
			throws ServletException, IOException {

		PrintWriter out = response.getWriter();
		// out.println("urlParameters :" + urlParameters);
		// URL url = new URL(callurl + urlParameters.replace("\\", ""));
		URL url = new URL(callurl);
		// out.println("Url :" + url);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setDoOutput(true);
		conn.setUseCaches(false);
		conn.setRequestMethod("POST");

		//
		OutputStream writer = conn.getOutputStream();

		writer.write(urlParameters.getBytes());
		// out.println("Writer Url : "+writer);
		int responseCode = conn.getResponseCode();
		// out.println("POST Response Code :: " + responseCode);
		StringBuffer buffer = new StringBuffer();
		//
		if (responseCode == 200) { // success
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String inputLine;

			while ((inputLine = in.readLine()) != null) {
				buffer.append(inputLine);
			}
			in.close();
			//
			// out.println(buffer.toString());
		} else {
			out.println("POST request not worked");
		}
		writer.flush();
		writer.close();
		return buffer.toString();

	}
	/*
	
	public static JSONArray fetchGADataForRuleEngine(String coll_name,SlingHttpServletResponse response) throws IOException {
		PrintWriter out=response.getWriter();
		MongoClient mongoClient = null;
	    MongoDatabase database  = null;
	    MongoCollection<Document> google_analytics_data_temp = null;
	    MongoCollection<Document> google_analytics_url_view_collection = null;
	    JSONArray pagePathJsonArr=null;
	    JSONArray funnelListJsonArr=new JSONArray();
	    String pagePath=null;
	    String subscriber_email=null;
	    ArrayList <Double> timeOnPageArrList = null;
	    ArrayList <Double> avgTimeOnPageArrList = null;
	    ArrayList <Integer> sessionCountArrList = null;
		ArrayList <String> dateHourMinuteArrList = null;
		ArrayList <Integer> sessionDurationBucket = null;
		ArrayList <String> hostNameArrList = null;
	    TreeSet<String> UniqueSubscribers = new TreeSet<String>();
	    org.json.JSONArray finalJsonArrayForRuleEngine=new org.json.JSONArray();
	    JSONObject rule_json_object=null;
	    String source=null;
	    Map<String,String> campaignDetailsMap=null;
	    
	    String Source=null;
	    String Subscriber_Email=null;
	    String campaign_id=null;
	    String ga_user=null;
	    String hostname=null;
	    String sourceMedium=null;
	    String url=null;
	    double TotalTimeOnPage=0;
	    int NoOfUrlClicks=0;
	    int TotalSesionDuration=0;
	    int SessionCount=0;
	    String Created_By=null;
	    String Funnel_Name=null;
	    String SubFunnel_Name=null;
	    String Category=null;
	    String Campaign_Id=null;
	    String List_Id=null;
	    String Subscriber_Id=null;
	    String lastclick=null;
	    double AvgTimeOnPage=0;
	    double MinTimeOnPage=0;
	    double MaxTimeOnPage=0;
	    int LastSessionCount=0;
	    double AvgSesionDuration=0;
	    int MinSesionDuration=0;
	    int MaxSesionDuration=0;
	    String firstclick=null;
	    ArrayList <Integer> LastSessionCountArrList = null;
	    ArrayList <Double> AvgSesionDurationArrList = null;
		out.println("Calling fetchGADataForRuleEngine : ");
		MongoClientURI connectionString = null;
	    
	    try {
	    	
	    	connectionString = new MongoClientURI("mongodb://localhost:27017");
	    	mongoClient =	new MongoClient(connectionString);
	       // mongoClient=ConnectionHelper.getConnection();
	        database=mongoClient.getDatabase("phplisttest");
	        google_analytics_data_temp=database.getCollection(coll_name);
	        google_analytics_url_view_collection=database.getCollection("google_analytics_url_view_collection");
	        
	        DistinctIterable<String> dimension2Di = google_analytics_data_temp.distinct("dimension2", String.class);
	        MongoCursor<String> dimension2Cursor = dimension2Di.iterator();
	        //int count=0;
	        try {
				while(dimension2Cursor.hasNext()) {
					subscriber_email=dimension2Cursor.next().toString();
					if(subscriber_email.contains("@")){
						//System.out.println("subscriber_email : "+ count++ + " : "+subscriber_email);
						UniqueSubscribers.add(subscriber_email);
					}
				}
			} finally {
				dimension2Cursor.close();
			}
	        //System.out.println("Total Number of Subscribers Found  : "+UniqueSubscribers.size());
	        out.println("Total Number of Subscribers Found  : "+UniqueSubscribers.size());
	        for (String temp_subscriber_email : UniqueSubscribers) {
	        	//System.out.println("temp_subscriber_email : "+temp_subscriber_email);
	        	out.println("Subscriber Email : "+temp_subscriber_email);
	        	Bson filter1 =and(eq("dimension2", temp_subscriber_email));
	        	DistinctIterable<String> sourceMediumDi = google_analytics_data_temp.distinct("sourceMedium", filter1,String.class);
		        MongoCursor<String> sourceMediumCursor = sourceMediumDi.iterator();
		        
		        try {
		        	while(sourceMediumCursor.hasNext()) {
						sourceMedium=sourceMediumCursor.next().toString();
						if(sourceMedium.contains("phplist")){
							campaign_id=sourceMedium.substring(0, sourceMedium.indexOf("/")-1).replace("phplist", "");
							source="Email";
						}else if(sourceMedium.contains("(direct)")){
							source="Direct";
							campaign_id="NULL";
						}
						out.println("Campaign Source For Subscriber : "+sourceMedium);
						rule_json_object=new JSONObject();
			        	//rule_json_object.put("Subscriber_Email",temp_subscriber_email);
						//rule_json_object.put("sourceMedium",sourceMedium);
						Bson filter2 =null;
						//System.out.println("-----------Unique sourceMedium : "+sourceMedium+"  : sourceMedium Unique-------------------------");
						out.println("Campaign Source For Subscriber : "+sourceMedium);
						Bson page_path_filter =and(eq("dimension2", temp_subscriber_email),eq("sourceMedium", sourceMedium));
						Bson filter =and(eq("dimension2", temp_subscriber_email),eq("sourceMedium", sourceMedium));
						pagePathJsonArr=GetUrlsBasedOnCampaignIdAndSubscriberId(google_analytics_data_temp,page_path_filter);
						LastSessionCountArrList = new ArrayList<Integer>();
						AvgSesionDurationArrList = new ArrayList<Double>();
						out.println("Total Url Found In Campaign : "+pagePathJsonArr.length());
						for(int i=0;i<pagePathJsonArr.length();i++){
							// Creating New Document Object For URL VIEW
							pagePath=(String) pagePathJsonArr.get(i);
							
							Bson host_name_filter =and(eq("pagePath", pagePath),eq("dimension2", temp_subscriber_email),eq("sourceMedium", sourceMedium));
							hostname=GetHostNameBasedOnCampaignIdAndSubscriberId(google_analytics_data_temp,host_name_filter);
							
							out.println("hostname= "+hostname);
							if(pagePath.length()==1){
								pagePath=hostname;
								out.println("pagePath= "+pagePath);
						    }else{
						       if(pagePath.contains("/?")){
						    	   pagePath=hostname+pagePath.substring(0, pagePath.indexOf("/?"));
						    	   //System.out.println(path1);
						       }else if(pagePath.contains("?")){
						    	   pagePath=hostname+pagePath.substring(0, pagePath.indexOf("?"));
						       }else if(pagePath.contains("/")){
						    	   pagePath=hostname+pagePath;
						       }
						    }
							
							//System.out.println("pagePath : "+pagePath);
							out.println("URL Found In Campaign : "+pagePath);
							timeOnPageArrList = new ArrayList<Double>();
							sessionCountArrList = new ArrayList<Integer>();
							dateHourMinuteArrList = new ArrayList<String>();
							sessionDurationBucket = new ArrayList<Integer>();
							hostNameArrList = new ArrayList<String>();
							avgTimeOnPageArrList = new ArrayList<Double>();
							
							filter2 =and(eq("sourceMedium", sourceMedium),eq("url", pagePath),eq("Subscriber_Email", temp_subscriber_email));
							FindIterable<Document> campaignWisePagePathFi = google_analytics_url_view_collection.find(filter2);
							MongoCursor<Document> campaignWisePagePathCursor = campaignWisePagePathFi.iterator();
							int count=0;
							while(campaignWisePagePathCursor.hasNext()) {
								Document campaignWisePagePath=campaignWisePagePathCursor.next();
								if(count==0){
									Source=campaignWisePagePath.getString("Source");
									Subscriber_Email=campaignWisePagePath.getString("Subscriber_Email");
									campaign_id=campaignWisePagePath.getString("campaign_id");
									ga_user=campaignWisePagePath.getString("ga_user");
									hostname=campaignWisePagePath.getString("hostname");
									sourceMedium=campaignWisePagePath.getString("sourceMedium");
									url=campaignWisePagePath.getString("url");
									TotalTimeOnPage=campaignWisePagePath.getDouble("TotalTimeOnPage");
									NoOfUrlClicks=campaignWisePagePath.getInteger("NoOfUrlClicks");
									TotalSesionDuration=campaignWisePagePath.getInteger("TotalSesionDuration");
									SessionCount=campaignWisePagePath.getInteger("SessionCount");
									Created_By=campaignWisePagePath.getString("Created_By");
									Funnel_Name=campaignWisePagePath.getString("Funnel_Name");
									SubFunnel_Name=campaignWisePagePath.getString("SubFunnel_Name");
									Category=campaignWisePagePath.getString("Category");
									Campaign_Id=campaignWisePagePath.getString("Campaign_Id");
									List_Id=campaignWisePagePath.getString("List_Id");
									Subscriber_Id=campaignWisePagePath.getString("Subscriber_Id");
									lastclick=campaignWisePagePath.getString("lastclick");
									AvgTimeOnPage=campaignWisePagePath.getDouble("AvgTimeOnPage");
									MinTimeOnPage=campaignWisePagePath.getDouble("MinTimeOnPage");
									MaxTimeOnPage=campaignWisePagePath.getDouble("MaxTimeOnPage");
									LastSessionCount=campaignWisePagePath.getInteger("LastSessionCount");
									AvgSesionDuration=campaignWisePagePath.getDouble("AvgSesionDuration");
									MinSesionDuration=campaignWisePagePath.getInteger("MinSesionDuration");
									MaxSesionDuration=campaignWisePagePath.getInteger("MaxSesionDuration");
									firstclick=campaignWisePagePath.getString("firstclick");
									
									rule_json_object.put("TotalSesionDuration",TotalSesionDuration);
									
									rule_json_object.put("Source",Source);
									rule_json_object.put("SubscriberEmail",Subscriber_Email);
									rule_json_object.put("CampaignId",campaign_id);
									rule_json_object.put("GAUser",ga_user);
									rule_json_object.put("HostName",hostname);
									rule_json_object.put("SourceMedium",sourceMedium);
									//rule_json_object.put("url",url);
									rule_json_object.put("CreatedBy",Created_By);
									rule_json_object.put("FunnelName",Funnel_Name);
									rule_json_object.put("SubFunnelName",SubFunnel_Name);
									rule_json_object.put("Category",SubFunnel_Name);
									rule_json_object.put("CampaignId",Campaign_Id);
									rule_json_object.put("ListId",List_Id);
									rule_json_object.put("SubscriberId",Subscriber_Id);
									//rule_json_object.put("url",url);
									//rule_json_object.put("firstclick",firstclick);
									//rule_json_object.put("lastclick",lastclick);
									
									
									//rule_json_object.put("NoOfUrlClicks",NoOfUrlClicks);
									//rule_json_object.put("TotalSesionDuration",TotalSesionDuration);
									//rule_json_object.put("SessionCount",SessionCount);
									
									//rule_json_object.put("AvgTimeOnPage",AvgTimeOnPage);
									//rule_json_object.put("MinTimeOnPage",MinTimeOnPage);
									//rule_json_object.put("MaxTimeOnPage",MaxTimeOnPage);

									//rule_json_object.put("LastSessionCount",LastSessionCount);
									
									//rule_json_object.put("AvgSesionDuration",AvgSesionDuration);
									//rule_json_object.put("MinSesionDuration",MinSesionDuration);
									//rule_json_object.put("MaxSesionDuration",MaxSesionDuration);
									
								}
								AvgTimeOnPage=campaignWisePagePath.getDouble("AvgTimeOnPage");
								rule_json_object.put(pagePath,AvgTimeOnPage);
								AvgSesionDurationArrList.add(campaignWisePagePath.getDouble("AvgSesionDuration"));
								LastSessionCountArrList.add(campaignWisePagePath.getInteger("LastSessionCount"));
								finalJsonArrayForRuleEngine.put(campaignWisePagePath.toJson());
								++count;
							}
						}
						
						Map<String,String> AvgSesionDurationMap=ArrayListOperationsForDoubleValue(AvgSesionDurationArrList);
					           rule_json_object.put("AvgSesionDuration",AvgSesionDurationMap.get("Average"));
					    //System.out.println("AvgSesionDurationMap Hi Akhilesh : "+AvgSesionDurationMap.get("Average"));
					    //System.out.println("AvgSesionDurationMap Hi Akhilesh : "+AvgSesionDurationMap.get("Average"));
					    Map<String,String> LastSessionCountMap=ArrayListOperationsForIntegerValue(LastSessionCountArrList);
					           rule_json_object.put("SessionCount",LastSessionCountMap.get("Max"));
					    //System.out.println("Before Merge rule_json_object : "+rule_json_object);       
					    JSONObject recent_gadata_json_obj=fetchRecentGADataForRuleEngine("google_analytics_recent_data_temp",sourceMedium,temp_subscriber_email);       
					    JSONObject most_recent_gadata_json_obj=fetchMostRecentGADataForRuleEngine("google_analytics_recent_data_temp",sourceMedium,temp_subscriber_email);
					    rule_json_object=mergeJSONObject(rule_json_object,mergeJSONObject(recent_gadata_json_obj,most_recent_gadata_json_obj));
					    System.out.println("After Merge rule_json_object : "+rule_json_object);
					    out.println("Call  rule_json_object as INPUT : "+rule_json_object+"      ::::::: Created_By ::::"+Created_By+":: Funnel_Name :: "+Funnel_Name);
					    
//					    String free_trail_status=null;
//					    
//					    free_trail_status=new FreeTrialandCart().checkFreeTrialExpirationStatus(Created_By.replace("_", "@"));
					    //System.out.println(campaign_details_doc);
					    // callrule engine and fire rule
					    // call shopping cart method
					    
//					  String validuserresp = CheckValidUserforFreetrialAndCart.checkValiditytrialCart(Created_By);
////					    logger.info("free_trail_status = "+free_trail_status);
////					    if(free_trail_status.equals("0")){
//					  JSONParser parser = new JSONParser();
//					  JSONObject validjs = (JSONObject) parser.parse(validuserresp);
//					  out.println("validjs = "+validjs);
//						if(validjs.containsKey("status")&& validjs.get("status").equals("true")) {
//					    	try {
//					    		logger.info("callRuleEngine : "+Funnel_Name);
//					    	callRuleEngine(rule_json_object.toString(),Funnel_Name);
//					    	}catch (Exception e) {
//					    		logger.info("exc in callRuleEngine"+e);
//							}
//					    }else{
//					    	System.out.println("Freetrail Expired for User : "+Created_By.replace("_", "@"));
//					    	logger.info("Freetrail Expired for User : "+Created_By.replace("_", "@"));
//					    }
					    //break;
					}
				} finally {
					sourceMediumCursor.close();
				}
		        
		        //System.out.println("finalJsonArrayForRuleEngine : "+finalJsonArrayForRuleEngine);
		        
				//break;
		   }			
	    } catch (Exception e) {
            e.printStackTrace();
            //throw new RuntimeException(e);
            System.out.println("Exception : "+e.getMessage());
		} finally {
			if(mongoClient!=null){
				mongoClient.close();
				mongoClient= null;
			}
			//ConnectionHelper.closeConnection(mongoClient);
		}
        return funnelListJsonArr;
	}  */
//	public static JSONArray GetUrlsBasedOnCampaignIdAndSubscriberId(MongoCollection<Document> collection,Bson filter) {
//		JSONArray pagePathJsonArr=new JSONArray();
//		String pagePath=null;
//    	DistinctIterable<String> pagePathDi = collection.distinct("pagePath", filter,String.class);
//        MongoCursor<String> pagePathCursor = pagePathDi.iterator();
//        try {
//			while(pagePathCursor.hasNext()) {
//				pagePath=pagePathCursor.next().toString();
//				System.out.println("pagePath : "+pagePath);
//				pagePathJsonArr.put(pagePath);
//			}
//		} finally {
//			pagePathCursor.close();
//		}
//        //System.out.println("pagePathJsonArr : "+pagePathJsonArr);
//        
//		return pagePathJsonArr;
//	}
//	public static String GetHostNameBasedOnCampaignIdAndSubscriberId(MongoCollection<Document> collection,Bson filter) {
//		JSONArray pagePathJsonArr=new JSONArray();
//		String pagePath=null;
//		String hostName=null;
//		MongoCursor<Document> campaignCursor = collection.find(filter).iterator();
//        
//		try {
//			if(campaignCursor.hasNext()==true){
//				while(campaignCursor.hasNext()) {
//					Document doc=campaignCursor.next();
//					hostName=doc.getString("hostname");
//				}
//			}
//    	} finally {
//    		campaignCursor.close();
//		}
//        //System.out.println("pagePathJsonArr : "+pagePathJsonArr);
//        
//		return hostName;
//	}
//	
//	public static Map<String,String> ArrayListOperationsForDoubleValue(ArrayList <Double> timeOnPageArrList) {
//		Map<String,String> timeOnPageMap=new HashMap<String,String>();
//		DecimalFormat decimal_formatter = new DecimalFormat("0.00");
//		//System.out.println("timeOnPageArrList.size() = " + timeOnPageArrList.size());
//		if(timeOnPageArrList.size()>0){
//			double sum = 0;
//			for (Double i : timeOnPageArrList) {
//	            sum += i;
//	        }
//	        double average = sum / timeOnPageArrList.size();
//	        timeOnPageMap.put("Min",String.valueOf(Collections.min(timeOnPageArrList)));
//	        timeOnPageMap.put("Max",String.valueOf(Collections.max(timeOnPageArrList)));
//	        timeOnPageMap.put("Sum",String.valueOf(decimal_formatter.format(sum)));
//	        timeOnPageMap.put("Average",String.valueOf(decimal_formatter.format(average)));
//	        timeOnPageMap.put("Count",String.valueOf(timeOnPageArrList.size()));
//	    }else{
//	    	timeOnPageMap.put("Min","0");
//	        timeOnPageMap.put("Max","0");
//	        timeOnPageMap.put("Sum","0");
//	        timeOnPageMap.put("Average","0");
//	        timeOnPageMap.put("Count","0");
//		}
//        return timeOnPageMap;
//	}
//	public static Map<String,String> ArrayListOperationsForIntegerValue(ArrayList <Integer> sessionCountArrList) {
//		Map<String,String> sessionCountMap=new HashMap<String,String>();
//		//System.out.println("sessionCountArrList.size() = " + sessionCountArrList.size());
//		if(sessionCountArrList.size()>0){
//			int sum = 0;
//			for (Integer i : sessionCountArrList) {
//	            sum += i;
//	        }
//	        double average = sum / sessionCountArrList.size();
//	        sessionCountMap.put("Min",String.valueOf(Collections.min(sessionCountArrList)));
//	        sessionCountMap.put("Max",String.valueOf(Collections.max(sessionCountArrList)));
//	        sessionCountMap.put("Sum",String.valueOf(sum));
//	        sessionCountMap.put("Average",String.valueOf(average));
//	        sessionCountMap.put("Count",String.valueOf(sessionCountArrList.size()));
//		}else{
//			sessionCountMap.put("Min","0");
//			sessionCountMap.put("Max","0");
//			sessionCountMap.put("Sum","0");
//			sessionCountMap.put("Average","0");
//			sessionCountMap.put("Count","0");
//		}
//        return sessionCountMap;
//	}
//	public static JSONObject fetchRecentGADataForRuleEngine(String coll_name,String sourceMedium,String temp_subscriber_email) {
//	MongoClient mongoClient = null;
//	MongoDatabase database  = null;
//    MongoCollection<Document> collection = null;
//    JSONArray pagePathJsonArr=null;
//    JSONArray funnelListJsonArr=new JSONArray();
//    String pagePath=null;
//    String hostname=null;
//    ArrayList <Double> timeOnPageArrList = null;
//	ArrayList <Integer> sessionCountArrList = null;
//	ArrayList <String> dateHourMinuteArrList = null;
//	ArrayList <Integer> sessionDurationBucket = null;
//	ArrayList <String> hostNameArrList = null;
//    TreeSet<String> UniqueSubscribers = new TreeSet<String>();
//    org.json.JSONArray finalJsonArrayForRuleEngine=new org.json.JSONArray();
//    org.json.JSONObject rule_json_object=null;
//    String campaign_id=null;
//    String ga_user=null;
//    String source=null;
//    Map<String,String> campaignDetailsMap=null;
//    int  int_recent_days=0;
//    Date recent_date = null;
//    MongoClientURI connectionString = null;
//    
//    try {
//    	
//    	connectionString = new MongoClientURI("mongodb://localhost:27017");
//    	mongoClient =	new MongoClient(connectionString);
//      //  mongoClient=ConnectionHelper.getConnection();
//        database=mongoClient.getDatabase("phplisttest");
//        collection=database.getCollection(coll_name);
//        
//        
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String recent_days_1=ResourceBundle.getBundle("config").getString("recent_days");
//        int_recent_days=Integer.parseInt(recent_days_1);
//        //int_recent_days=25;
//	  	Date date_campare1 = new Date();
//	  	     date_campare1.setDate(date_campare1.getDate()-int_recent_days);
//	  	recent_date = dateFormat.parse(dateFormat.format(date_campare1));
//	        
//    	    if(sourceMedium.contains("phplist")){
//				campaign_id=sourceMedium.substring(0, sourceMedium.indexOf("/")-1).replace("phplist", "");
//				source="Email";
//			}else if(sourceMedium.contains("(direct)")){
//				source="Direct";
//				campaign_id="NULL";
//			}
//			rule_json_object=new org.json.JSONObject();
//        	//rule_json_object.put("Subscriber_Email",temp_subscriber_email);
//			//rule_json_object.put("Source_Medium",sourceMedium);
//			Bson filter2 =null;
//			System.out.println("-----------Unique sourceMedium : "+sourceMedium+"  : sourceMedium Unique-------------------------");
//			Bson page_path_filter =and(gt("dateHourMinuteInDateFormat", recent_date),eq("dimension2", temp_subscriber_email),eq("sourceMedium", sourceMedium));
//			Bson filter =and(gt("dateHourMinuteInDateFormat", recent_date),eq("sourceMedium", sourceMedium),eq("dimension2", temp_subscriber_email));
//			pagePathJsonArr=GetUrlsBasedOnCampaignIdAndSubscriberId(collection,page_path_filter);
//			if(pagePathJsonArr.length()>0){
//				for(int i=0;i<pagePathJsonArr.size();i++){
//					// Creating New Document Object For URL VIEW
//					pagePath=(String) pagePathJsonArr.get(i);
//					System.out.println("pagePath : "+pagePath);
//					timeOnPageArrList = new ArrayList<Double>();
//					sessionCountArrList = new ArrayList<Integer>();
//					dateHourMinuteArrList = new ArrayList<String>();
//					sessionDurationBucket = new ArrayList<Integer>();
//					hostNameArrList = new ArrayList<String>();
//					filter2 =and(eq("sourceMedium", sourceMedium),eq("pagePath", pagePath),eq("dimension2", temp_subscriber_email));
//					FindIterable<Document> campaignWisePagePathFi = collection.find(filter2);
//					MongoCursor<Document> campaignWisePagePathCursor = campaignWisePagePathFi.iterator();
//					while(campaignWisePagePathCursor.hasNext()) {
//						Document campaignWisePagePath=campaignWisePagePathCursor.next();
//						System.out.println("campaignWisePagePath : "+campaignWisePagePath);
//						ga_user=campaignWisePagePath.get("ga_username").toString();
//						/*
//						System.out.println("campaignWisePagePath : "+campaignWisePagePath.getString("pagePath")
//								+"    timeOnPage : "+campaignWisePagePath.getString("timeOnPage")
//								+"    dateHourMinute : "+campaignWisePagePath.getString("dateHourMinute"));
//					    */			
//						timeOnPageArrList.add(Double.parseDouble(campaignWisePagePath.get("timeOnPage").toString()));
//						sessionCountArrList.add(Integer.parseInt(campaignWisePagePath.get("sessionCount").toString()));
//						System.out.println("sessionCount : "+campaignWisePagePath.get("sessionCount").toString());
//						dateHourMinuteArrList.add(campaignWisePagePath.get("dateHourMinute").toString());
//						sessionDurationBucket.add(Integer.parseInt(campaignWisePagePath.get("sessionDurationBucket").toString()));
//						hostNameArrList.add(campaignWisePagePath.get("hostname").toString());
//					}
//					//rule_json_object.put("GA_User",ga_user);
//					if(dateHourMinuteArrList.size()>0){
//					    System.out.println("dateHourMinuteArrList : "+dateHourMinuteArrList.get(dateHourMinuteArrList.size()-1));
//					}
//					if(hostNameArrList.size()>0){
//						hostname=hostNameArrList.get(hostNameArrList.size()-1);
//					    //System.out.println("hostname : "+hostname);
//					    //rule_json_object.put("Host_Name",hostname);
//					}
//					if(pagePath.length()==1){
//						pagePath=hostname;
//				    	System.out.println(pagePath);
//				    }else{
//				       if(pagePath.contains("/?")){
//				    	   pagePath=hostname+pagePath.substring(0, pagePath.indexOf("/?"));
//				    	   //System.out.println(path1);
//				       }else if(pagePath.contains("?")){
//				    	   pagePath=hostname+pagePath.substring(0, pagePath.indexOf("?"));
//				       }else if(pagePath.contains("/")){
//				    	   pagePath=hostname+pagePath;
//				       }
//				    }
//					
//					Map<String,String> timeOnPageMap=ArrayListOperationsForDoubleValue(timeOnPageArrList);
//						rule_json_object.put("Recent_"+pagePath,timeOnPageMap.get("Average").toString());
//						//rule_json_object.put(pagePath,timeOnPageMap.get("Sum").toString());
//						System.out.println("timeOnPageMap : "+timeOnPageMap);
//						
//					Map<String,String> sessionCountMap=ArrayListOperationsForIntegerValue(sessionCountArrList);
//					    //rule_json_object.put("Session_Count",sessionCountMap.get("Max"));
//					    //rule_json_object.put("No_OF_Clicks",sessionCountMap.get("Count"));
//						//url_view_doc_object.put("Last_Session_Count",Integer.parseInt(sessionCountMap.get("Max")));
//					    rule_json_object.put("Recent_SessionCount",Integer.parseInt(sessionCountMap.get("Max")));
//						
//					Map<String,String> sessionDurationBucketMap=ArrayListOperationsForIntegerValue(sessionDurationBucket);
//					    //rule_json_object.put("AvgSesionDuration",Double.parseDouble(sessionDurationBucketMap.get("Average")));
//					    //rule_json_object.put("MinSesionDuration",Integer.parseInt(sessionDurationBucketMap.get("Min")));
//					    //rule_json_object.put("MaxSesionDuration",Integer.parseInt(sessionDurationBucketMap.get("Max")));
//					    //rule_json_object.put("TotalSesionDuration",Integer.parseInt(sessionDurationBucketMap.get("Sum")));
//						
//						//rule_json_object.put("Source",source);
//				    Map<String,String> AvgSessionDurationBucketMap=GetAvgSessionDurationBasedOnCampaignIdAndSubscriberId(collection,filter);
//						//rule_json_object.put("RecentTotalSessionCount",AvgSessionDurationBucketMap.get("Count"));
//						//rule_json_object.put("RecentTotalSesionDuration",AvgSessionDurationBucketMap.get("Sum"));
//						//rule_json_object.put("RecentAvgSesionDuration",AvgSessionDurationBucketMap.get("Average"));
//						//rule_json_object.put("RecentMinSesionDuration",AvgSessionDurationBucketMap.get("Min"));
//						//rule_json_object.put("RecentMaxSesionDuration",AvgSessionDurationBucketMap.get("Max"));
//						
//						rule_json_object.put("Recent_AvgSesionDuration",AvgSessionDurationBucketMap.get("Average"));
//				}
//				finalJsonArrayForRuleEngine.put(rule_json_object);
//			    
//			}else{
//				rule_json_object.put("Recent_"+pagePath,"No most recent url found");
//				rule_json_object.put("Recent_SessionCount","0");
//				rule_json_object.put("Recent_AvgSesionDuration","0");
//				//rule_json_object.put("GA_User","");
//				//rule_json_object.put("Host_Name","");
//				//rule_json_object.put("Recent_"+pagePath,"0");
//				//rule_json_object.put("Source",source);
//				//rule_json_object.put("RecentTotalSessionCount","0");
//				//rule_json_object.put("RecentTotalSesionDuration","0");
//				//rule_json_object.put("RecentAvgSesionDuration","0");
//				//rule_json_object.put("RecentMinSesionDuration","0");
//				//rule_json_object.put("RecentMaxSesionDuration","0");
//				
//			}
//	        System.out.println(finalJsonArrayForRuleEngine);
//					
//    } catch (Exception ex) {
//        System.out.println("Exception : "+ex.getMessage());
//	} finally {
//		Bson filter2 =lte("dateHourMinuteInDateFormat", recent_date);
//		//collection.deleteMany(filter2);
//		
//			if(mongoClient!=null){
//				mongoClient.close();
//				mongoClient= null;
//			}
//			//ConnectionHelper.closeConnection(mongoClient);
//		
//		
//	}
//    return rule_json_object;
//    
//	}
//	public static JSONObject fetchMostRecentGADataForRuleEngine(String coll_name,String sourceMedium,String temp_subscriber_email) {
//		MongoClient mongoClient = null;
//		MongoDatabase database  = null;
//	    MongoCollection<Document> collection = null;
//	    JSONArray pagePathJsonArr=null;
//	    JSONArray funnelListJsonArr=new JSONArray();
//	    String pagePath=null;
//	    String hostname=null;
//	    ArrayList <Double> timeOnPageArrList = null;
//		ArrayList <Integer> sessionCountArrList = null;
//		ArrayList <String> dateHourMinuteArrList = null;
//		ArrayList <Integer> sessionDurationBucket = null;
//		ArrayList <String> hostNameArrList = null;
//	    TreeSet<String> UniqueSubscribers = new TreeSet<String>();
//	    org.json.JSONArray finalJsonArrayForRuleEngine=new org.json.JSONArray();
//	    org.json.JSONObject rule_json_object=null;
//	    String campaign_id=null;
//	    String ga_user=null;
//	    String source=null;
//	    Map<String,String> campaignDetailsMap=null;
//	    int  int_most_recent_days=0;
//	    Date most_recent_date = null;
//	    MongoClientURI connectionString = null;
//	    try {
////	        mongoClient=ConnectionHelper.getConnection();
//	    	
//	    	connectionString = new MongoClientURI("mongodb://localhost:27017");
//	    	mongoClient =	new MongoClient(connectionString);
//	        database=mongoClient.getDatabase("phplisttest");
//	        collection=database.getCollection(coll_name);
//	        
//	        
//	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//	        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//	        String recent_days_1=ResourceBundle.getBundle("config").getString("most_recent_days");
//	        int_most_recent_days=Integer.parseInt(recent_days_1);
//	        //int_most_recent_days=25;
//		  	Date date_campare1 = new Date();
//		  	     date_campare1.setDate(date_campare1.getDate()-int_most_recent_days);
//		  	    most_recent_date = dateFormat.parse(dateFormat.format(date_campare1));
//		        
//	    	    if(sourceMedium.contains("phplist")){
//					campaign_id=sourceMedium.substring(0, sourceMedium.indexOf("/")-1).replace("phplist", "");
//					source="Email";
//				}else if(sourceMedium.contains("(direct)")){
//					source="Direct";
//					campaign_id="NULL";
//				}
//				rule_json_object=new org.json.JSONObject();
//	        	//rule_json_object.put("Subscriber_Email",temp_subscriber_email);
//				//rule_json_object.put("Source_Medium",sourceMedium);
//				Bson filter2 =null;
//				System.out.println("-----------Unique sourceMedium : "+sourceMedium+"  : sourceMedium Unique-------------------------");
//				Bson page_path_filter =and(gt("dateHourMinuteInDateFormat", most_recent_date),eq("dimension2", temp_subscriber_email),eq("sourceMedium", sourceMedium));
//				Bson filter =and(gt("dateHourMinuteInDateFormat", most_recent_date),eq("sourceMedium", sourceMedium),eq("dimension2", temp_subscriber_email));
//				pagePathJsonArr=GetUrlsBasedOnCampaignIdAndSubscriberId(collection,page_path_filter);
//				if(pagePathJsonArr.size()>0){
//					for(int i=0;i<pagePathJsonArr.size();i++){
//						// Creating New Document Object For URL VIEW
//						pagePath=(String) pagePathJsonArr.get(i);
//						System.out.println("pagePath : "+pagePath);
//						timeOnPageArrList = new ArrayList<Double>();
//						sessionCountArrList = new ArrayList<Integer>();
//						dateHourMinuteArrList = new ArrayList<String>();
//						sessionDurationBucket = new ArrayList<Integer>();
//						hostNameArrList = new ArrayList<String>();
//						filter2 =and(eq("sourceMedium", sourceMedium),eq("pagePath", pagePath),eq("dimension2", temp_subscriber_email));
//						FindIterable<Document> campaignWisePagePathFi = collection.find(filter2);
//						MongoCursor<Document> campaignWisePagePathCursor = campaignWisePagePathFi.iterator();
//						while(campaignWisePagePathCursor.hasNext()) {
//							Document campaignWisePagePath=campaignWisePagePathCursor.next();
//							System.out.println("campaignWisePagePath : "+campaignWisePagePath);
//							ga_user=campaignWisePagePath.get("ga_username").toString();
//							/*
//							System.out.println("campaignWisePagePath : "+campaignWisePagePath.getString("pagePath")
//									+"    timeOnPage : "+campaignWisePagePath.getString("timeOnPage")
//									+"    dateHourMinute : "+campaignWisePagePath.getString("dateHourMinute"));
//						    */			
//							timeOnPageArrList.add(Double.parseDouble(campaignWisePagePath.get("timeOnPage").toString()));
//							sessionCountArrList.add(Integer.parseInt(campaignWisePagePath.get("sessionCount").toString()));
//							System.out.println("sessionCount : "+campaignWisePagePath.get("sessionCount").toString());
//							dateHourMinuteArrList.add(campaignWisePagePath.get("dateHourMinute").toString());
//							sessionDurationBucket.add(Integer.parseInt(campaignWisePagePath.get("sessionDurationBucket").toString()));
//							hostNameArrList.add(campaignWisePagePath.get("hostname").toString());
//						}
//						//rule_json_object.put("GA_User",ga_user);
//						if(dateHourMinuteArrList.size()>0){
//						    System.out.println("dateHourMinuteArrList : "+dateHourMinuteArrList.get(dateHourMinuteArrList.size()-1));
//						}
//						if(hostNameArrList.size()>0){
//							hostname=hostNameArrList.get(hostNameArrList.size()-1);
//						    System.out.println("Host_Name : "+hostname);
//						    //rule_json_object.put("Host_Name",hostname);
//						}
//						if(pagePath.length()==1){
//							pagePath=hostname;
//					    	System.out.println(pagePath);
//					    }else{
//					       if(pagePath.contains("/?")){
//					    	   pagePath=hostname+pagePath.substring(0, pagePath.indexOf("/?"));
//					    	   //System.out.println(path1);
//					       }else if(pagePath.contains("?")){
//					    	   pagePath=hostname+pagePath.substring(0, pagePath.indexOf("?"));
//					       }else if(pagePath.contains("/")){
//					    	   pagePath=hostname+pagePath;
//					       }
//					    }
//						
//						Map<String,String> timeOnPageMap=ArrayListOperationsForDoubleValue(timeOnPageArrList);
//							rule_json_object.put("MostRecent_"+pagePath,timeOnPageMap.get("Average").toString());
//							System.out.println("timeOnPageMap : "+timeOnPageMap);
//							
//						Map<String,String> sessionCountMap=ArrayListOperationsForIntegerValue(sessionCountArrList);
//						    //rule_json_object.put("Session_Count",sessionCountMap.get("Max"));
//						    //rule_json_object.put("No_OF_Clicks",sessionCountMap.get("Count"));
//							//url_view_doc_object.put("Last_Session_Count",Integer.parseInt(sessionCountMap.get("Max")));MostRecentSessionCount
//						    rule_json_object.put("MostRecent_SessionCount",Integer.parseInt(sessionCountMap.get("Max")));
//							
//						Map<String,String> sessionDurationBucketMap=ArrayListOperationsForIntegerValue(sessionDurationBucket);
//						    //rule_json_object.put("AvgSesionDuration",Double.parseDouble(sessionDurationBucketMap.get("Average")));
//						    //rule_json_object.put("MinSesionDuration",Integer.parseInt(sessionDurationBucketMap.get("Min")));
//						    //rule_json_object.put("MaxSesionDuration",Integer.parseInt(sessionDurationBucketMap.get("Max")));
//						    //rule_json_object.put("TotalSesionDuration",Integer.parseInt(sessionDurationBucketMap.get("Sum")));
//							
//							//rule_json_object.put("Source",source);
//						Map<String,String> AvgSessionDurationBucketMap=GetAvgSessionDurationBasedOnCampaignIdAndSubscriberId(collection,filter);
//							//rule_json_object.put("MostRecentTotalSessionCount",AvgSessionDurationBucketMap.get("Count"));
//							//rule_json_object.put("MostRecentTotalSessionTime",AvgSessionDurationBucketMap.get("Sum"));
//							//rule_json_object.put("MostRecentAvgSessionTime",AvgSessionDurationBucketMap.get("Average"));
//							//rule_json_object.put("MostRecentMinSessionTime",AvgSessionDurationBucketMap.get("Min"));
//							//rule_json_object.put("MostRecentMaxSessionTime",AvgSessionDurationBucketMap.get("Max"));
//							rule_json_object.put("MostRecent_AvgSesionDuration",AvgSessionDurationBucketMap.get("Average"));
//					}
//					finalJsonArrayForRuleEngine.put(rule_json_object);
//				    
//				}else{
//					rule_json_object.put("MostRecent_"+pagePath,"No most recent url found");
//					rule_json_object.put("MostRecent_SessionCount","0");
//					rule_json_object.put("MostRecent_AvgSesionDuration","0");
//					//rule_json_object.put("GA_User","");
//					//rule_json_object.put("Host_Name","");
//					//rule_json_object.put("MostRecent_"+pagePath,"0");
//					//rule_json_object.put("Source",source);
//					//rule_json_object.put("MostRecentTotalSessionCount","0");
//					//rule_json_object.put("MostRecentTotalSesionDuration","0");
//					//rule_json_object.put("MostRecentAvgSesionDuration","0");
//					//rule_json_object.put("MostRecentMinSesionDuration","0");
//					//rule_json_object.put("MostRecentMaxSesionDuration","0");
//				}
//		        System.out.println(finalJsonArrayForRuleEngine);
//						
//	    } catch (Exception ex) {
//	        System.out.println("Exception : "+ex.getMessage());
//		} finally {
//			Bson filter2 =lte("dateHourMinuteInDateFormat", most_recent_date);
//			//collection.deleteMany(filter2);
//		
//				if(mongoClient!=null){
//					mongoClient.close();
//					mongoClient= null;
//				}
//				//ConnectionHelper.closeConnection(mongoClient);
//			
//		}
//	    return rule_json_object;
//	    
//		}
//	public static JSONObject mergeJSONObject(JSONObject rule_json_object,JSONObject most_recent_gadata_json_obj) throws JSONException, org.apache.sling.commons.json.JSONException{
//		for(String key : org.json.JSONObject.getNames(most_recent_gadata_json_obj))
//		{
//			rule_json_object.put(key, most_recent_gadata_json_obj.get(key));
//		}
//		return rule_json_object;
//	}
}
