package hk.com.sita.services;

import hk.com.sita.model.MetaData;
import hk.com.sita.utils.Constants;
import hk.com.sita.utils.DatabaseUtils;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.json.JSONObject;

import com.ibm.ecm.extension.PluginService;
import com.ibm.ecm.extension.PluginServiceCallbacks;

public class GetMetaDataByIDService extends PluginService {

	@Override
	public void execute(PluginServiceCallbacks callback,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

	 String id=request.getParameter(Constants.COL_ID);
		PrintWriter responseWriter = response.getWriter();
 
		DatabaseUtils database = new DatabaseUtils();
		database.connect();
		MetaData metadata = database.selectRecordById(id);
		database.disconnect();

		JSONObject object=new JSONObject();
		object=metadata.getAsJsonObject();
		
		responseWriter.print(object);

	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return "GetMetaDataByIDService";
	}

}
