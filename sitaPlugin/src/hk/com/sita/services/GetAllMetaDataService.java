package hk.com.sita.services;

import hk.com.sita.model.MetaData;
import hk.com.sita.utils.DatabaseUtils;

import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.json.JSONArray;
import org.apache.commons.json.JSONObject;

import com.ibm.ecm.extension.PluginService;
import com.ibm.ecm.extension.PluginServiceCallbacks;

public class GetAllMetaDataService extends PluginService {

	@Override
	public void execute(PluginServiceCallbacks callback,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		PrintWriter responseWriter = response.getWriter();
		JSONArray jsonArray = new JSONArray();

		DatabaseUtils database = new DatabaseUtils();
		database.connect();
		ArrayList<MetaData> metaList = database.selectAllRecords();
		database.disconnect();

		for (int i = 0; i < metaList.size(); i++) {
			JSONObject jsonObject = metaList.get(i).getAsJsonObject();
			jsonArray.add(jsonObject);
		}

		responseWriter.print(jsonArray);

	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return "GetAllMetaDataService";
	}

}
