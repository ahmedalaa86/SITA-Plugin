package hk.com.sita.utils;

import hk.com.sita.model.MetaData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class DatabaseUtils {

	private static Connection dbConnection = null;
	private static DataSource dataSource = null;

	public void connect() {
		String dataSourceName = Constants.Database_JNDI;

		try {
			if (dataSource == null) {
				Context ctx = new InitialContext();
				dataSource = (DataSource) ctx.lookup(dataSourceName);
			}

			dbConnection = dataSource.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void disconnect() {

		try {
			if (dbConnection != null && !dbConnection.isClosed()) {
				dbConnection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public ArrayList<MetaData> selectAllRecords() {
		String query = "Select * from " + Constants.TABLE_NAME;

		ArrayList<MetaData> metadataRecords = new ArrayList<MetaData>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = dbConnection.prepareStatement(query);
			rs = stmt.executeQuery();
			while (rs.next()) {
				MetaData metaData = new MetaData();
				metaData.setId(rs.getInt(Constants.COL_ID));
				metaData.setAccountCode(rs
						.getString(Constants.COL_ACCOUNT_CODE));
				metaData.setAllocationAmount((rs
						.getString(Constants.COL_ALLOCATION_AMOUNT)));
				metaData.setAllocationCode((rs
						.getString(Constants.COL_ALLOCATION_CODE)));
				metaData.setAnalysisCode((rs
						.getString(Constants.COL_ANALYSIS_CODE)));
				metaData.setDescriptionLine((rs
						.getString(Constants.COL_DESCRIPTION_LINE)));
				metaData.setInvoiceNumber((rs
						.getString(Constants.COL_INVOICE_NUMBER)));
				metaData.setReportAmount((rs
						.getString(Constants.COL_REPORT_AMOUNT)));
				metadataRecords.add(metaData);

			}

		} catch (SQLException e) {

			e.printStackTrace();

			return new ArrayList<MetaData>();
		} finally {
			closeResource(rs);
			closeResource(stmt);

		}
		return metadataRecords;

	}

	public ArrayList<MetaData> selectRecordByInvoiceNumber(String invoiceNumber)

	{
		String query = "Select * from " + Constants.TABLE_NAME + " Where \""
				+ Constants.COL_INVOICE_NUMBER + "\" = '" + invoiceNumber + "'";
		ArrayList<MetaData> metadataRecords = new ArrayList<MetaData>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = dbConnection.prepareStatement(query);
			rs = stmt.executeQuery();
			while (rs.next()) {
				MetaData metaData = new MetaData();
				metaData.setId(rs.getInt(Constants.COL_ID));
				metaData.setAccountCode(rs
						.getString(Constants.COL_ACCOUNT_CODE));
				metaData.setAllocationAmount((rs
						.getString(Constants.COL_ALLOCATION_AMOUNT)));
				metaData.setAllocationCode((rs
						.getString(Constants.COL_ALLOCATION_CODE)));
				metaData.setAnalysisCode((rs
						.getString(Constants.COL_ANALYSIS_CODE)));
				metaData.setDescriptionLine((rs
						.getString(Constants.COL_DESCRIPTION_LINE)));
				metaData.setInvoiceNumber((rs
						.getString(Constants.COL_INVOICE_NUMBER)));
				metaData.setReportAmount((rs
						.getString(Constants.COL_REPORT_AMOUNT)));
				metadataRecords.add(metaData);

			}

		} catch (SQLException e) {

			e.printStackTrace();

			return new ArrayList<MetaData>();
		} finally {
			closeResource(rs);
			closeResource(stmt);

		}
		return metadataRecords;

	}

	public MetaData selectRecordById(String id)

	{
		String query = "Select * from " + Constants.TABLE_NAME + " Where "
				+ Constants.COL_ID + " = " + id;
		MetaData metaData = new MetaData();

		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = dbConnection.prepareStatement(query);
			rs = stmt.executeQuery();
			while (rs.next()) {

				metaData.setId(rs.getInt(Constants.COL_ID));
				metaData.setAccountCode(rs
						.getString(Constants.COL_ACCOUNT_CODE));
				metaData.setAllocationAmount((rs
						.getString(Constants.COL_ALLOCATION_AMOUNT)));
				metaData.setAllocationCode((rs
						.getString(Constants.COL_ALLOCATION_CODE)));
				metaData.setAnalysisCode((rs
						.getString(Constants.COL_ANALYSIS_CODE)));
				metaData.setDescriptionLine((rs
						.getString(Constants.COL_DESCRIPTION_LINE)));
				metaData.setInvoiceNumber((rs
						.getString(Constants.COL_INVOICE_NUMBER)));
				metaData.setReportAmount((rs
						.getString(Constants.COL_REPORT_AMOUNT)));

			}

		} catch (SQLException e) {

			e.printStackTrace();

			return new MetaData();
		} finally {
			closeResource(rs);
			closeResource(stmt);

		}
		return metaData;

	}

	public boolean updateMetadataRecord(MetaData metadata) {

		PreparedStatement stmt = null;

		String query = "update " + Constants.TABLE_NAME + " set \""
				+ Constants.COL_ALLOCATION_CODE + "\" ='"
				+ metadata.getAllocationCode() + "' ,\""
				+ Constants.COL_ACCOUNT_CODE + "\" ='"
				+ metadata.getAccountCode() + "' ,\""
				+ Constants.COL_ANALYSIS_CODE + "\" ='"
				+ metadata.getAnalysisCode() + "' ,\""
				+ Constants.COL_ALLOCATION_AMOUNT + "\" ='"
				+ metadata.getAllocationAmount() + "' ,\""
				+ Constants.COL_DESCRIPTION_LINE + "\" ='"
				+ metadata.getDescriptionLine() + "' ,\""
				+ Constants.COL_REPORT_AMOUNT + "\" ='"
				+ metadata.getReportAmount() + "'" + " where "
				+ Constants.COL_ID + " =" + metadata.getId();
		try {

			stmt = dbConnection.prepareStatement(query);
			stmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} finally {

			closeResource(stmt);

		}

		return true;

	}

	public boolean insertMetadataRecord(MetaData metadata) {
		String query = "insert into " + Constants.TABLE_NAME + "(\"Invoice_Number\", \"Allocation_Code\", \"Account_Code\", \"Analysis_Code\", \"FC_Allocation_Amount\", \"Description_Line\",\"Report_Amount\") values (" + "'"
				+ metadata.getInvoiceNumber() + "'," + "'"
				+ metadata.getAllocationCode() + "'," + "'"
				+ metadata.getAccountCode() + "'," + "'"
				+ metadata.getAnalysisCode() + "'," + "'"
				+ metadata.getAllocationAmount() + "'," + "'"
				+ metadata.getDescriptionLine() + "'," + "'"
				+ metadata.getReportAmount() + "'" + ")";

		System.err.println("############################################ "+query);
		PreparedStatement stmt = null;
		try {

			stmt = dbConnection.prepareStatement(query);
			stmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} finally {

			closeResource(stmt);

		}
		return true;
	}

	public boolean deleteMetaDataRecord(String id) {
		String query = "delete from " + Constants.TABLE_NAME + " where "
				+ Constants.COL_ID + " =" + id;
		
		  PreparedStatement stmt = null;
		try {
				
				
				stmt = dbConnection.prepareStatement(query);
			    stmt.executeUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			} finally
		    {
		    	 
		    	closeResource(stmt);
		    	
		    }
		return true;
	}

 
	private void closeResource(ResultSet resultSet) {
		try {
			if (resultSet != null && !resultSet.isClosed()) {
				resultSet.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
 
	private void closeResource(Statement statement) {
		try {
			if (statement != null && !statement.isClosed()) {
				statement.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
