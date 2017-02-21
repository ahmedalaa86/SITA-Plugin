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

public class InsertMetaDataService extends PluginService {

	@Override
	public void execute(PluginServiceCallbacks callback,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String id = request.getParameter(Constants.COL_ID);
		String accountCode = request.getParameter(Constants.COL_ACCOUNT_CODE);
		String allocationAmount = request
				.getParameter(Constants.COL_ALLOCATION_AMOUNT);
		String allocationCode = request
				.getParameter(Constants.COL_ALLOCATION_CODE);
		String analysisCode = request.getParameter(Constants.COL_ANALYSIS_CODE);
		String descriptionLine = request
				.getParameter(Constants.COL_DESCRIPTION_LINE);
		String invoiceNumber = request
				.getParameter(Constants.COL_INVOICE_NUMBER);
		String reportAmount = request.getParameter(Constants.COL_REPORT_AMOUNT);

		MetaData data = new MetaData();

		data.setAccountCode(accountCode);
		data.setAllocationAmount(allocationAmount);
		data.setAllocationCode(allocationCode);
		data.setAnalysisCode(analysisCode);
		data.setInvoiceNumber(invoiceNumber);
		data.setReportAmount(reportAmount);
		data.setDescriptionLine(descriptionLine);

		PrintWriter responseWriter = response.getWriter();

		DatabaseUtils database = new DatabaseUtils();
		boolean isSucceeded = false;
		database.connect();

		System.out.println("id" + id);

		if (id != null) {
			data.setId(Integer.parseInt(id));
			isSucceeded = database.updateMetadataRecord(data);

		} else {
			isSucceeded = database.insertMetadataRecord(data);
		}
		database.disconnect();

		JSONObject object = new JSONObject();
		object.put("Result", isSucceeded);

		responseWriter.print(object);

	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return "InsertMetaDataService";
	}

}
