package salesconverter.mongo;

import java.util.Map;
import java.util.WeakHashMap;

import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONObject;
import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Updates;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

import org.bson.conversions.Bson;

//import org.bson.conversions.Bson;
public class EditFunnel {

	public JSONObject editFunnel(String destinationFunnel, String category,String email) {
		MongoClient mongoClient = null;
		MongoDatabase database = null;
		MongoCollection<Document> collection = null;
		MongoCursor<Document> cursor = null;
		MongoClientURI connectionString = null;
		JSONObject respJson = null;
		String uri = null;
		JSONArray datajson = null;
		JSONObject alldata=new JSONObject();
		try {
			System.setProperty("javax.net.ssl.trustStore", "/etc/ssl/firstTrustStore");
			System.setProperty("javax.net.ssl.trustStorePassword", "bizlem123");
			System.setProperty("javax.net.ssl.keyStore", "/etc/ssl/MongoClientKeyCert.jks");
			System.setProperty("javax.net.ssl.keyStorePassword", "bizlem123");
			uri = "mongodb://localhost:27017/?ssl=true";
			connectionString = new MongoClientURI(uri);
			mongoClient = new MongoClient(connectionString);
			database = mongoClient.getDatabase("salesautoconvert");
			collection = database.getCollection("FirstCategoryMails");
//			respJson = new WeakHashMap<String, JSONArray>();
			
			String[] dbcate = { "Explore", "Warm", "Inform", "Entice", "Connect" };
		//	JSONArray allcat=new JSONArray();//dbcate.length
				for(int i=0;i<dbcate.length;i++) {
					
					datajson = new JSONArray();
			Bson query = and(eq("Parentfunnel", destinationFunnel), eq("Category", dbcate[i]),eq("CreatedBy", email));
			FindIterable<Document> fi = collection.find(query);
			cursor = fi.iterator();
			JSONArray contarr=new JSONArray();
			while (cursor.hasNext()) {
				JSONObject obj = new JSONObject(cursor.next().toJson());
				
				if(obj.has("_id")) {obj.remove("_id");}
				datajson.put(obj);
				
				if(dbcate[i].equals("Explore") && obj.has("Contacts") && obj.getJSONArray("Contacts").length()>0) {
					contarr=obj.getJSONArray("Contacts");
										
				}
			}

			//contarr
			if(contarr.length()>0) {
				alldata.put("Contacts", contarr);
			}
			if(datajson.length()>0) {
				alldata.put(dbcate[i], datajson);
			}
			
				}
			

		} catch (Exception e) {
			
		} finally {
			if (null != cursor) {
				cursor.close();
				cursor = null;
			}
			if (null != mongoClient) {
				mongoClient.close();
				mongoClient = null;
			}
		}

		return alldata;
	}

	public String moveContacts(String sourceFunnel, String destinationFunnel, String dataArr) {
		MongoClient mongoClient = null;
		MongoDatabase database = null;
		MongoCollection<Document> collection = null;
		// MongoCursor<Document> cursor = null;
		MongoClientURI connectionString = null;
		String uri = null;
		String res = null;
		try {
			System.setProperty("javax.net.ssl.trustStore", "/etc/ssl/firstTrustStore");
			System.setProperty("javax.net.ssl.trustStorePassword", "bizlem123");
			System.setProperty("javax.net.ssl.keyStore", "/etc/ssl/MongoClientKeyCert.jks");
			System.setProperty("javax.net.ssl.keyStorePassword", "bizlem123");
			uri = "mongodb://localhost:27017/?ssl=true";
			connectionString = new MongoClientURI(uri);
			mongoClient = new MongoClient(connectionString);
			database = mongoClient.getDatabase("salesautoconvert");
			collection = database.getCollection("FirstCategoryMails");
			/*
			 * Bson condition = new Document("$eq", sourceFunnel);
			 * 
			 * Bson filter = new Document("funnelName", condition).append("Category",
			 * "Explore");
			 * 
			 * FindIterable<Document> fi = collection.find(filter); cursor = fi.iterator();
			 */
			// Bson conditionMove = new Document("$eq", destinationFunnel);
			Bson query = and(eq("funnelName", destinationFunnel), eq("Category", "Explore"));
			// Bson query = new Document("funnelName", destinationFunnel).append("Category",
			// "Explore");
			/* while (cursor.hasNext()) { */
			// JSONArray arrdata= new JSONArray();
			/* JSONObject obj=new JSONObject(cursor.next().toJson()); */
			JSONObject obj = new JSONObject(dataArr);
			if (obj.has("Contacts")) {
				JSONArray arr = obj.getJSONArray("Contacts");
				// scheduleTime

				for (int k = 0; k < arr.length(); k++) {
					JSONObject contact = arr.getJSONObject(k);
					Document newContact = Document.parse(contact.toString());
					/*
					 * Document newAddress = new Document().append("type", "secondaryAddress")
					 * .append("street", "#24 niceton") .append("city","Nice");
					 */

					collection.updateOne(query, Updates.addToSet("Contacts", newContact));

				}
				Document newDocument = new Document();
				newDocument.put("updateflag", "1");

				Document updateObj = new Document();
				updateObj.put("$set", newDocument);

				collection.updateOne(query, updateObj);
			}

			/* } */

			// collection.deleteOne(filter);
			res = "Success";

		} catch (Exception e) {
			res = "Fail";
		} finally {
			/*
			 * if(null!=cursor){ cursor.close(); cursor = null; }
			 */
			if (null != mongoClient) {
				mongoClient.close();
				mongoClient = null;
			}
		}

		return res;
	}

}
