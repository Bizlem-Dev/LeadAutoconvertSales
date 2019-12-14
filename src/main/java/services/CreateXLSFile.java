package services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import java.util.ResourceBundle;

public class CreateXLSFile {
	
	public static void main(String args[]) throws JSONException {
		String st="{\r\n" + 
				"\"Fields\":[\r\n" + 
				"\"name\",\r\n" + 
				"\"addr\",\r\n" + 
				"\"city\"\r\n" + 
				"],\r\n" + 
				"\"MailTemplateName\":\"mail1\",\r\n" + 
				"\"DataSourceName\":\"test\",\r\n" + 
				"\"DatasourceType\":\"form\"\r\n" + 
				"}";
		 File directory = new File("E:\\TEJAL_Work\\LEADAUTOCONVERT\\sll");
	        if (directory.exists()) {
	            System.out.println("Directory already exists ...");

	        } else {
	            System.out.println("Directory not exists, creating now");

	             directory.mkdir();
	             
	        }
		JSONObject js=null;
		try {
			 js=new JSONObject(st);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
}
	public String Createxls(JSONObject mailtempjs,String email) {
		String filepath=ResourceBundle.getBundle("config").getString("datasourcefilepath");
		String sling_ip=ResourceBundle.getBundle("config").getString("sling_ip");
		String filelink=null;
		try {
			String mailtempname;
	
			mailtempname = mailtempjs.getString("MailTemplateName");
	
		JSONArray headersarr=mailtempjs.getJSONArray("Fields");
			
		Workbook workbook = new HSSFWorkbook();
		Sheet sheet = workbook.createSheet("EDR Raw Data");

		//for (int rn=0; rn<headers.length; rn++) {
		   Row r = sheet.createRow(0);
		   for (int rn=0; rn<headersarr.length(); rn++) {
			   r.createCell(rn).setCellValue(headersarr.get(rn).toString());
		  
		}
		   
		   // Creating new directory in Java, if it doesn't exists
		   String dir=filepath+email+"/";
	        File directory = new File(dir);
	        if (directory.exists()) {
	            System.out.println("Directory already exists ...");

	        } else {
	            System.out.println("Directory not exists, creating now");

	             directory.mkdir();
	        }
		   
		   
		 try {  
			 FileOutputStream outputStream = new FileOutputStream(dir+mailtempname+".xls");
	            workbook.write(outputStream);
	            filelink="http://"+sling_ip+":8087/leadMailTemplate/"+email+"/"+mailtempname+".xls";
	        } catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		System.out.println("done");
	
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		
		return filelink;
		
		
	}
	
	}
