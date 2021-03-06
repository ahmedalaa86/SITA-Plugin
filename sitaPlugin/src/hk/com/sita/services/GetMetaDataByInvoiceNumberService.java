package hk.com.sita.services;

import hk.com.sita.model.MetaData;
import hk.com.sita.utils.Constants;
import hk.com.sita.utils.DatabaseUtils;

import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.json.JSONArray;
import org.apache.commons.json.JSONObject;

import com.ibm.ecm.extension.PluginService;
import com.ibm.ecm.extension.PluginServiceCallbacks;

public class GetMetaDataByInvoiceNumberService extends PluginService {

	@Override
	public void execute(PluginServiceCallbacks callback,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String invoiceNumber=request.getParameter(Constants.COL_INVOICE_NUMBER);
		//String supplierName=request.getParameter(Constants.COL_SUPPLIER_NAME);
		String caseId=request.getParameter(Constants.COL_CASE_ID);
		PrintWriter responseWriter = response.getWriter();
		JSONArray jsonArray = new JSONArray();

		DatabaseUtils database = new DatabaseUtils();
		database.connect();
		ArrayList<MetaData> metaList = database.selectRecordByInvoiceNumber(invoiceNumber,caseId);
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
		return "GetMetaDataByInvoiceNumberService";
	}

}
