package leadconverter.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.Workspace;
import javax.jcr.query.Query;
import javax.jcr.query.QueryResult;
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

@Component(immediate = true, metatype = false)
@Service(value = javax.servlet.Servlet.class)
@Properties({ @Property(name = "service.description", value = "Save product Servlet"),
		@Property(name = "service.vendor", value = "VISL Company"),
		@Property(name = "sling.servlet.paths", value = { "/servlet/service/checkingavailabledata" }),
		@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
		@Property(name = "sling.servlet.extensions", value = { "hotproducts", "cat", "latestproducts", "brief",
				"prodlist", "catalog", "viewcart", "productslist", "addcart", "createproduct", "checkmodelno",
				"productEdit" }) })
@SuppressWarnings("serial")
public class checkingavailable extends SlingAllMethodsServlet {

	@Reference
	private SlingRepository repo;
	final String FILEEXTENSION[] = { "csv" };

	final int NUMBEROFRESULTSPERPAGE = 10;
	private static final long serialVersionUID = 1L;
	String fileType = "file";
	JSONObject mainjsonobject = new JSONObject();

	@Override
	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		JSONArray mainarray = new JSONArray();
		JSONObject jsonobject = new JSONObject();

		String remoteuser = request.getRemoteUser();

		try {
			Session session = null;
            Node tempRulNode=null;
			session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
			Node content = session.getRootNode().getNode("content");
			
		if (request.getRequestPathInfo().getExtension().equals("data")) {
			String ajaxlistname = request.getParameter("listnameresult");
		//	out.println("List Name from AJax : : :"+ajaxlistname);

			
			String querystr ="select [LIST_NAME] from [nt:base] where (contains('LIST_NAME','*"+ajaxlistname+"*'))  and ISDESCENDANTNODE('/content/LEAD_CONVERTER/LIST/')";
			out.println(querystr);

		    Workspace workspace = session.getWorkspace();
		    Query query = workspace.getQueryManager().createQuery(querystr, Query.JCR_SQL2);
		    QueryResult result = query.execute();
		    NodeIterator iterator = result.getNodes();
		    // rs= result.toString();
		  //out.println(iterator.hasNext());
		    //while (iterator.hasNext()) {
		     //
		    //	tempRulNode = iterator.nextNode();
		   //  out.println("Node Value : : : :"+tempRulNode);
		   //tempRulNode.setProperty("queryresult", "found");
		     
		    //}			
			
			if(iterator.hasNext()) {
				out.print("true");
			}else {
				out.print("false");
			}
			
			}
		
		} catch (Exception e) {

			out.println("Exception ex : : : " + e.getMessage());
		}

	}

	
}
