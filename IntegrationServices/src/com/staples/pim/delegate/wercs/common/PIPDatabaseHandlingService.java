package com.staples.pim.delegate.wercs.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.base.util.IntgSrvUtils;
import com.staples.pim.delegate.datamigration.utils.DatamigrationCommonUtil;

import oracle.jdbc.pool.OracleDataSource;


public class PIPDatabaseHandlingService {

	public OracleDataSource pipdatasource;
	
	public final String pipRegStatUpdateQuery = "UPDATE VENDOR_OWN.MASTERVENDORITEMGENERAL SET WERCSREGCD = ? WHERE ITEMID = ?";
	
	public OracleDataSource getPipdatasource() {
		
		return pipdatasource;
	}

	public void setPipdatasource(OracleDataSource pipdatasource) {
	
		this.pipdatasource = pipdatasource;
	}
	
	public PIPDatabaseHandlingService(OracleDataSource oracleDatasource){
		this.pipdatasource = oracleDatasource;
	}
	
	public void pipRegistrationStatusUpdate(String upcNo, String pipID, int registrationStatus,IntgSrvLogger logger){

		logger.info("Updating registrationstatus in PIP");
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;

		try {
			dbConnection = pipdatasource.getConnection();
			if(dbConnection!=null){
				preparedStatement = dbConnection.prepareStatement(pipRegStatUpdateQuery);

				preparedStatement.setString(1, Integer.toString(registrationStatus));
				preparedStatement.setString(2, pipID);
				
				int result = preparedStatement.executeUpdate();

				DatamigrationCommonUtil.printConsole("Procedure executed without exceptions. Result="+result);
			}

		} catch (SQLException e) {
			logger.error("Exception caught while calling stored procedure :"+e.getErrorCode()+" :"+e.getMessage());
			DatamigrationCommonUtil.printConsole(e.getMessage());
			IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(), "");
		} finally {

			if (preparedStatement != null) {
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			if (dbConnection != null) {
				try {
					dbConnection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

		}
	
	}
}
