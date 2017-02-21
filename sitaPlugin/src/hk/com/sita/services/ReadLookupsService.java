package hk.com.sita.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.json.JSONObject;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

import com.ibm.ecm.extension.PluginService;
import com.ibm.ecm.extension.PluginServiceCallbacks;

public class ReadLookupsService extends PluginService {

	@Override
	public void execute(PluginServiceCallbacks callback,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String propId = request.getParameter("id");
		String propName = request.getParameter("name");
		String output="";
		try 
		{
			
			String docUrlString = request.getParameter("docUrl");
			URL docUrl = new URL(new URL(request.getRequestURL().toString()), docUrlString);

			Cookie[] cookies = request.getCookies();
			StringBuffer cookieProperty = new StringBuffer();
			if (cookies != null) {
				for (int j = 0; j < cookies.length; j++) {
					cookieProperty.append(cookies[j].getName());
					cookieProperty.append("=");
					cookieProperty.append(cookies[j].getValue());
					cookieProperty.append(",");
				}
			}
			HttpURLConnection docConnection = (HttpURLConnection) docUrl.openConnection();
			docConnection.setRequestProperty("COOKIE", cookieProperty.toString());
			InputStream docStream = docConnection.getInputStream();

			
			
			
			System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
			File file=new File("/lookups.xls");
			System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
			System.out.println(file.getAbsolutePath());
			FileInputStream fin = new FileInputStream(file);

			//Get the workbook instance for XLS file
			HSSFWorkbook workbook = new HSSFWorkbook(docStream);
			int sheetNumber=0;
			String sheetName="";
			int keyIndex=0;
			int valueIndex=1;
			//Get first sheet from the workbook
			if("location".equals(propName)){
				sheetNumber=1;
				keyIndex=1;
				valueIndex=2;
				sheetName="location";
			} else if("supplier".equals(propName)){
				sheetNumber=2;
				sheetName="supplier";
			} else if("account".equals(propName)){
				sheetNumber=3;
				sheetName="acc. code";
			} else if("allocation_code".equals(propName)){
				sheetNumber=4;
				sheetName="allocation code";
			}

			//Get first sheet from the workbook
			HSSFSheet sheet = workbook.getSheet(sheetName);
			    //Iterate through each rows from first sheet
			Iterator<Row> rowIterator = sheet.iterator();
			
			while(rowIterator.hasNext()) {
			    Row row = rowIterator.next();
			    //For each row, iterate through each columns
			    String key="";
			    String value="";
			    try{
			    	key = row.getCell(keyIndex).getStringCellValue();
			    	value = row.getCell(valueIndex).getStringCellValue();
			    }
			    catch(Exception ex){
			    	System.out.println("Ignoring Row");
			    }
			    
			    if(propId.equalsIgnoreCase(key)){
			    	output=value;
			    	break;
			    }
			}
			fin.close();

		}

		catch (FileNotFoundException e) 
		{
			e.printStackTrace(System.out);
		} 
		catch (IOException e) 
		{
			e.printStackTrace(System.out);
		}
		catch (Exception e) 
		{
			e.printStackTrace(System.out);
		}
	
		
		PrintWriter responseWriter = response.getWriter();
		
		JSONObject object = new JSONObject();
		object.put("Result", output);
		responseWriter.print(object);

	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return "ReadLookupsService";
	}

}
