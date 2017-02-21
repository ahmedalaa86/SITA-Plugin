package hk.com.sita.model;

import hk.com.sita.utils.Constants;

import org.apache.commons.json.JSONException;
import org.apache.commons.json.JSONObject;

public class MetaData {
	

	private int id;
	private String invoiceNumber;
	private String allocationCode;
	private String accountCode;
	private String analysisCode;
	private String allocationAmount;
	private String descriptionLine;
	private String reportAmount;
	public String getAccountCode() {
		return accountCode;
	}
	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getInvoiceNumber() {
		return invoiceNumber;
	}
	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}
	public String getAllocationCode() {
		return allocationCode;
	}
	public void setAllocationCode(String allocationCode) {
		this.allocationCode = allocationCode;
	}
	public String getAllocationAmount() {
		return allocationAmount;
	}
	public void setAllocationAmount(String allocationAmount) {
		this.allocationAmount = allocationAmount;
	}
	public String getAnalysisCode() {
		return analysisCode;
	}
	public void setAnalysisCode(String analysisCode) {
		this.analysisCode = analysisCode;
	}
	public String getDescriptionLine() {
		return descriptionLine;
	}
	public void setDescriptionLine(String descriptionLine) {
		this.descriptionLine = descriptionLine;
	}
	public String getReportAmount() {
		return reportAmount;
	}
	public void setReportAmount(String reportAmount) {
		this.reportAmount = reportAmount;
	}
	
	public JSONObject getAsJsonObject()
	{
		JSONObject jsonObject=null;
		try {
			jsonObject = new JSONObject();
			
			jsonObject.put(Constants.COL_ACCOUNT_CODE, accountCode);
			jsonObject.put(Constants.COL_ALLOCATION_AMOUNT, allocationAmount);
			jsonObject.put(Constants.COL_ALLOCATION_CODE, allocationCode);
			jsonObject.put(Constants.COL_ANALYSIS_CODE, analysisCode);
			jsonObject.put(Constants.COL_DESCRIPTION_LINE, descriptionLine);
			jsonObject.put(Constants.COL_ID, id);
			jsonObject.put(Constants.COL_INVOICE_NUMBER, invoiceNumber);
			jsonObject.put(Constants.COL_REPORT_AMOUNT, reportAmount);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonObject;
		
	}
	
	public static void main(String [] arg)
	{
		
		MetaData metadata=new MetaData();
		metadata.setAccountCode("19");
		metadata.setAllocationAmount("19");
		metadata.setAllocationCode("19");
		metadata.setAnalysisCode("19");
		metadata.setDescriptionLine("19");
		metadata.setId(4);
		metadata.setInvoiceNumber("19");
		metadata.setReportAmount("19");
		
		
		String query = "update " + Constants.TABLE_NAME + " set "
				+ Constants.COL_ALLOCATION_CODE + " ='"
				+ metadata.getAllocationCode() + "' ,"
				+ Constants.COL_ACCOUNT_CODE + " ='"
				+ metadata.getAccountCode() + "' ,"
				+ Constants.COL_ANALYSIS_CODE + " ='"
				+ metadata.getAnalysisCode() + "' ,"
				+ Constants.COL_ALLOCATION_AMOUNT + " ='"
				+ metadata.getAllocationAmount() + "' ,"
				+ Constants.COL_DESCRIPTION_LINE + " ='"
				+ metadata.getDescriptionLine() + "' ,"
				+ Constants.COL_REPORT_AMOUNT + " ='"
				+ metadata.getReportAmount() + "'" + " where "
				+ Constants.COL_ID + " =" + metadata.getId();
		System.out.println(query);
		
		
	}

}
