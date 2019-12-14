package salesconverter.freetrail;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;

import salesconverter.mongo.SaveFunnelDetails;

public class testing {

	public static void main(String args[]) throws ParseException, JSONException {
//		201912021942
		//  19000101
		
		String value = "201912021942";//yyyyMMddHHmmss
		SimpleDateFormat originalFormat = new SimpleDateFormat("yyyyMMddHHmm");
		Date date = originalFormat.parse(value);
		
		SimpleDateFormat newFormat = new SimpleDateFormat("dd-MM-yy HH:mm");
		String formatedDate = newFormat.format(date);
		System.out.println("formatedDate= "+formatedDate);
		String Week =null;
		String sourceMedium="dec3temptest / (not set) ";
		String	campaign_id = sourceMedium.substring(0, sourceMedium.indexOf("/") - 1)
				.replace("not set", "").trim();
		
		String createddate= "13-08-19 22:30:26";
		try {
			String[] monthArray = {"jan","feb","mar","apr","may","jun","jul","aug","sep","oct","nov","dec"}; 
			
			DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
			Date date1=new Date();
			
			Calendar calendar = Calendar.getInstance();
		
			calendar.setTime(date1); 
			int n=calendar.get(Calendar.MONTH);
			System.out.println("weeklist : "+monthArray[n]);
			System.out.println("month = "+calendar.get(Calendar.MONTH));
			int weekno=calendar.get(Calendar.WEEK_OF_MONTH);
			Week="week"+Integer.toString(weekno);
			System.out.println("Week = "+Week);
			} catch (Exception e) {
			
			// TODO Auto-generated catch block
			System.out.println(e);
			}
		String js="{\"data\":[{\"convLeads\":250,\"Geolocation\":[[\"Country\",\"HotLeads\",\"Converted\",\"Explore\"],[\"India\",\"900\",\"100\",\"10\"],[\"USA\",\"700\",\"200\",\"100\"],[\"Spain\",\"700\",\"200\",\"100\"],[\"France\",\"500\",\"50\",\"100\"],[\"Australia\",\"200\",\"20\",\"100\"]],\"standout\":[{\"C3\":{\"upgrade\":1000,\"session time\":3000,\"traffic\":300},\"C1\":{\"upgrade\":300,\"session time\":1000,\"traffic\":200},\"C2\":{\"upgrade\":500,\"session time\":2000,\"traffic\":400}}],\"upcomingCampaign\":[{\"date\":\"dd-MM-yyyy\",\"subFunnelName\":\"ABC\",\"leads\":10000},{\"date\":\"dd-MM-yyyy\",\"subFunnelName\":\"DEF\",\"leads\":10000},{\"date\":\"dd-MM-yyyy\",\"subFunnelName\":\"GHI\",\"leads\":10000},{\"date\":\"dd-MM-yyyy\",\"subFunnelName\":\"JKL\",\"leads\":10000},{\"date\":\"dd-MM-yyyy\",\"subFunnelName\":\"MNO\",\"leads\":10000}],\"funnelName\":\"Funnel1\",\"dripRate\":{\"rate\":\"80\",\"convRate\":\"30%\",\"rawLead\":1000,\"conv\":300},\"upgradationRate\":{\"data\":[{\"rate\":\"50%\",\"subFunnel\":\"SubFunnel 1\",\"tableData\":[{\"rate\":\"2%\",\"source\":\"facebook\"},{\"rate\":\"4%\",\"source\":\"twitter\"},{\"rate\":\"2%\",\"source\":\"insta\"}]},{\"rate\":\"50%\",\"subFunnel\":\"SubFunnel 2\",\"tableData\":[{\"rate\":\"2%\",\"source\":\"facebook\"},{\"rate\":\"4%\",\"source\":\"twitter\"},{\"rate\":\"2%\",\"source\":\"insta\"}]}],\"subFunnel\":[\"SubFunnel 1\",\"SubFunnel 2\"]},\"funnelLeadCategory\":[{\"week1\":[{\"warm\":200,\"inform\":300,\"explore\":1000,\"entice\":500,\"connect\":100}],\"week2\":[{\"warm\":400,\"inform\":500,\"explore\":2000,\"entice\":1000,\"connect\":100}],\"week3\":[{\"warm\":300,\"inform\":1000,\"explore\":3000,\"entice\":1500,\"connect\":200}],\"week4\":[{\"warm\":500,\"inform\":1000,\"explore\":4000,\"entice\":2000,\"connect\":500}],\"week5\":[{\"warm\":10,\"inform\":30,\"explore\":100,\"entice\":50,\"connect\":10}]}],\"hotLeads\":250,\"convRate\":50,\"missedOppurtinities\":{\"Others\":15,\"Price\":10,\"Quality\":20,\"Competitiors\":40,\"Specs\":30},\"campaignFunnel\":[{\"warm\":200,\"inform\":300,\"explore\":1000,\"entice\":500,\"connect\":100}],\"active user\":{\"chartData\":[{\"dec-18\":{\"funnel end\":900,\"unsubscribe\":300,\"headroom\":400,\"active user\":300,\"spam\":1500},\"jan-18\":{\"funnel end\":600,\"unsubscribe\":300,\"headroom\":600,\"active user\":400,\"spam\":2000},\"nov-18\":{\"funnel end\":800,\"unsubscribe\":200,\"headroom\":300,\"active user\":200,\"spam\":1000},\"feb-18\":{\"funnel end\":1500,\"unsubscribe\":2000,\"headroom\":50,\"active user\":100,\"spam\":20}}],\"tableData\":{\"funnel end\":600,\"unsubscribe\":400,\"headroom\":1200,\"active user\":100,\"spam\":500}},\"campaignFunneldata\":[{\"rate\":5,\"leads\":200,\"source\":\"facebook\"},{\"rate\":1,\"leads\":50,\"source\":\"instagram\"},{\"rate\":3,\"leads\":250,\"source\":\"mail\"}],\"outcome\":[{\"Parameter\":\"Reveneue\",\"Actual\":85,\"planned\":92},{\"Parameter\":\"Conversation\",\"Actual\":82,\"planned\":95},{\"Parameter\":\"Free trial\",\"Actual\":75,\"planned\":80}],\"rawLeads\":500},{\"convLeads\":150,\"Geolocation\":[[\"Country\",\"HotLeads\",\"Converted\"],[\"Algeria\",\"900\",\"100\"],[\"Angola\",\"700\",\"200\"],[\"Cameroon\",\"700\",\"200\"],[\"Brazil\",\"500\",\"50\"],[\"China\",\"200\",\"20\"]],\"standout\":[{\"C3\":{\"upgrade\":1000,\"session time\":3000,\"traffic\":300},\"C1\":{\"upgrade\":300,\"session time\":1000,\"traffic\":200},\"C2\":{\"upgrade\":500,\"session time\":2000,\"traffic\":400}}],\"upcomingCampaign\":[{\"date\":\"dd-MM-yyyy\",\"subFunnelName\":\"ABC\",\"leads\":10000},{\"date\":\"dd-MM-yyyy\",\"subFunnelName\":\"DEF\",\"leads\":10000},{\"date\":\"dd-MM-yyyy\",\"subFunnelName\":\"GHI\",\"leads\":10000},{\"date\":\"dd-MM-yyyy\",\"subFunnelName\":\"JKL\",\"leads\":10000},{\"date\":\"dd-MM-yyyy\",\"subFunnelName\":\"FUnnel2\",\"leads\":10000}],\"funnelName\":\"Funnel2\",\"dripRate\":{\"rate\":\"80\",\"convRate\":\"30%\",\"rawLead\":1000,\"conv\":300},\"mostActiveLeads\":{\"visit\":[{\"lessthan\":2,\"greaterthan\":4,\"lead\":100},{\"lessthan\":5,\"greaterthan\":9,\"lead\":130},{\"lessthan\":10,\"greaterthan\":15,\"lead\":185},{\"lessthan\":15,\"greaterthan\":20,\"lead\":235}],\"session\":[{\"lessthan\":29,\"greaterthan\":30,\"lead\":200},{\"lessthan\":30,\"greaterthan\":59,\"lead\":180},{\"lessthan\":60,\"greaterthan\":119,\"lead\":115},{\"lessthan\":120,\"greaterthan\":180,\"lead\":15}]},\"upgradationRate\":{\"data\":[{\"rate\":\"50%\",\"subFunnel\":\"SubFunnel 1\",\"tableData\":[{\"rate\":\"2%\",\"source\":\"facebook\"},{\"rate\":\"4%\",\"source\":\"twitter\"},{\"rate\":\"2%\",\"source\":\"insta\"}]},{\"rate\":\"50%\",\"subFunnel\":\"SubFunnel 2\",\"tableData\":[{\"rate\":\"2%\",\"source\":\"facebook\"},{\"rate\":\"4%\",\"source\":\"twitter\"},{\"rate\":\"2%\",\"source\":\"insta\"}]}],\"subFunnel\":[\"SubFunnel 1\",\"SubFunnel 2\"]},\"funnelLeadCategory\":[{\"week1\":[{\"warm\":100,\"inform\":300,\"explore\":1000,\"entice\":500,\"connect\":100}],\"week2\":[{\"warm\":200,\"inform\":500,\"explore\":2000,\"entice\":1000,\"connect\":100}],\"week3\":[{\"warm\":400,\"inform\":1000,\"explore\":3000,\"entice\":1500,\"connect\":200}],\"week4\":[{\"warm\":600,\"inform\":2000,\"explore\":4000,\"entice\":5000,\"connect\":500}],\"week5\":[{\"warm\":10,\"inform\":30,\"explore\":100,\"entice\":50,\"connect\":10}]}],\"hotLeads\":250,\"convRate\":50,\"missedOppurtinities\":{\"Others\":15,\"Price\":10,\"Quality\":20,\"Competitiors\":40,\"Specs\":30},\"campaignFunnel\":[{\"warm\":200,\"inform\":300,\"explore\":1000,\"entice\":500,\"connect\":100}],\"active user\":{\"chartData\":[{\"dec-18\":{\"funnel end\":900,\"unsubscribe\":300,\"headroom\":400,\"active user\":300,\"spam\":1500},\"jan-18\":{\"funnel end\":600,\"unsubscribe\":300,\"headroom\":600,\"active user\":400,\"spam\":2000},\"nov-18\":{\"funnel end\":800,\"unsubscribe\":200,\"headroom\":300,\"active user\":200,\"spam\":1000},\"feb-18\":{\"funnel end\":1500,\"unsubscribe\":2000,\"headroom\":50,\"active user\":100,\"spam\":20}}],\"tableData\":{\"funnel end\":600,\"unsubscribe\":400,\"headroom\":1200,\"active user\":100,\"spam\":500}},\"campaignFunneldata\":[{\"rate\":15,\"leads\":20,\"source\":\"facebook\"},{\"rate\":12,\"leads\":50,\"source\":\"instagram\"},{\"rate\":3,\"leads\":250,\"source\":\"mail\"}],\"outcome\":[{\"Parameter\":\"Reveneue\",\"Actual\":85,\"planned\":92},{\"Parameter\":\"Conversation\",\"Actual\":82,\"planned\":95},{\"Parameter\":\"Free trial\",\"Actual\":75,\"planned\":80}],\"rawLeads\":100}],\"funnelList\":[\"Funnel1\",\"Funnel2\"]}\r\n" + 
				"";
		
		JSONObject dashboardjson=new JSONObject(js);
		System.out.println("dashboardjson= "+dashboardjson.toString());//
		boolean emailmovedOrNot =true;
		if (!emailmovedOrNot) {
			System.out.println("emailmovedOrNot= "+emailmovedOrNot);
		} 
		Date d = new Date();
		System.out.println("hhmmformat= "+d);
		
	Date	set_date1 = new SimpleDateFormat("dd-MM-yy HH:mm:ss").parse(createddate);
		StringBuilder hhmmformat = new StringBuilder(new SimpleDateFormat("HH:mm").format(set_date1));
		System.out.println("hhmmformat= "+hhmmformat);
		StringBuilder schtime = null;
		StringBuilder cuurTime = null;
		
			schtime = new StringBuilder("22:30");
			

			String[] split = schtime.toString().split(":", -1);
		
			cuurTime = new StringBuilder();
			System.out.println("Schedule split " + split[0]);
			int currScheduletime = Integer.parseInt(split[0]) + 1;
			System.out.println("Schedule currScheduletime 1 " + currScheduletime);
			if (currScheduletime >= 24) {
				currScheduletime = 1;
				System.out.println("Schedule currScheduletime if" + currScheduletime);
			}
			
			
			System.out.println("Schedule currScheduletime " + currScheduletime);
			if(currScheduletime<10) {
				cuurTime.append("0"+currScheduletime);
			}else {
				cuurTime.append(currScheduletime);
			}
			cuurTime.append(":");
			cuurTime.append(split[1]);
			System.out.println("Schedule Time is " + cuurTime.toString());
 JSONObject data_json_obj=new JSONObject();
 
 data_json_obj.put("ke", "aca");
 data_json_obj.put("ke12", "acasd");
 Iterator<String> keysItr = data_json_obj.keys();
		String key=null;
		JSONArray keysarr=new JSONArray();
		
		while (keysItr.hasNext()) {
			 key = keysItr.next();
			
			 keysarr.put(key);
			
		}
		System.out.println("keysarr "+keysarr);
		String str="testmul1_EC_2";
		String campnumb=str.substring(0,str.lastIndexOf("_"));
		campnumb=str.substring(0,campnumb.lastIndexOf("_"));
		System.out.println("campnumb = "+campnumb);
		System.out.println(str.lastIndexOf("_")+" ss "+str.length());
		String schedulegap=null;
		
		Date set_date=null;
		SimpleDateFormat formatter2=new SimpleDateFormat("dd-MM-yy");
		try {
			Date dateobj = new Date();
			set_date = new SimpleDateFormat("dd-MM-yy").parse(createddate);
			System.out.println("set_date = "+set_date);
		  
//		    Date date2=formatter2.format(dateobj);  
			System.out.println("set_datenn  = "+formatter2.format(dateobj));
		    
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			
		}

		
		if(str.contains("_EC_") || str.contains("_EnC_") ||str.contains("_IC_") ||str.contains("_WC_") ||str.contains("_CC_") ) {
			String campnumber=str.substring(str.lastIndexOf("_")+1,str.length());
			System.out.println("campnumber = "+campnumber);
			int gapdays=Integer.parseInt(campnumber)-1;
			gapdays=gapdays*3;
			schedulegap=Integer.toString(gapdays);
			System.out.println("schedulegapdate = "+schedulegap);
			set_date.setDate(set_date.getDate() + Integer.parseInt(schedulegap));
			System.out.println("set_date ---  "+set_date+" = formatter2 ="+formatter2.format(set_date));
		//	System.out.println("schedulegapdate === "+new SimpleDateFormat("dd-MM-yy").parse(set_date.toString()));
			
		}else {
			System.out.println("no ec ");
		}
		
	}
	
	
}
