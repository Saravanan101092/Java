package com.staples.pim.delegate.wercs.common;

import java.io.File;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import oracle.jdbc.pool.OracleDataSource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.base.util.EmailUtil;
import com.staples.pim.base.util.IntgSrvAppConstants;
import com.staples.pim.base.util.IntgSrvUtils;
import com.staples.pim.delegate.datamigration.utils.DatamigrationCommonUtil;
import com.staples.pim.delegate.wercs.corpdmztostep.processor.CorpdmzToStepMSDSProcessor;
import com.staples.pim.delegate.wercs.corpdmztostep.runner.WercsRegulatoryDataFeedJobListner;
import com.staples.pim.delegate.wercs.model.MasterTableVO;

import oracle.jdbc.pool.OracleDataSource;

public class SBDatabaseHandlingService {

	public static String		infoLogString;
	protected IntgSrvLogger		traceLogger									= IntgSrvLogger
																					.getInstance(IntgSrvAppConstants.FREEFORM_TRACE_LOGGER);
	protected String			clazzname									= this.getClass().getName();
	protected IntgSrvLogger		ehfLogger									= IntgSrvLogger.getInstance(IntgSrvAppConstants.EHF_LOGGER);
	public final String			getUPCforRegData							= "{call SB_OWNER.getupcforregdata(?,?,?,?,?)}";
	public final String			getUPCforMSDSDoc							= "{call SB_OWNER.getupcformsdsdoc(?,?,?,?,?)}";
	public static int			REGULATORYDATA_UNSOLICITED_SKUNOTAVAILABLE	= 508;
	public static int			MSDS_UNSOLICITED_SKUNOTAVAILABLE			= 510;
	public final String			wercsUPCCrossRefTableInsertProcedure		= "{call SB_OWNER.wercs_crossreference_insert(?,?,?,?)}";

	public OracleDataSource		datasource;

	public static final String	DBACCESS									= "databaseAccess";

	public final String			masterTableInsertUpdateProcedure			= "{call SB_OWNER.wercs_mastertable_insertupdate(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	public final String			registrationStatusUpdateProcedure			= "{call SB_OWNER.WERCS_REGSTATUS_UPDATE(?,?,?)}";
	public final String			wercsStatusAuditTableInsertProcedure		= "{call SB_OWNER.wercs_status_audit_insert(?,?,?)}";
	public final String			wercsRetryUPCsQuery							= "select upc, pip_id, transaction_type, created_date, registration_status, wercs_out_trigger, regulatory_data_status, step_id, model_number from SB_OWNER.wercs_status_master where registration_status <> 1 and registration_status <> 30 and wercs_out_trigger = 'Get Status' and regulatory_data_status = '0'";
	public final String			rejectedItemUpdateQuery						= "{call SB_OWNER.wercs_rejecteditem_update(?,?)}";
	public final String			getRulesTrippedDetailsQuery					= "select UPC,WERCS_ID,PIP_ID,SKU,STEP_ID,MODEL_NUMBER,ITEM_DESCRIPTION,SUPPLIER_NAME,REQUESTOR_NAME,SUPPLIER_ID,REQUESTOR_ID,PS_ID, MERCHANT_ID,TRANSACTION_TYPE,CREATED_DATE from SB_OWNER.wercs_status_master where WERCS_OUT_TRIGGER='Rules Tripped Mail' and eventid = 0";
	public final String			udpateEventIDRulesTrippedQuery				= "update SB_owner.wercs_status_master set eventid=? where PIP_ID=?";
	public final String			regStatMailItemsQuery						= "select UPC,WERCS_ID,PIP_ID,SKU,STEP_ID,MODEL_NUMBER,ITEM_DESCRIPTION,SUPPLIER_NAME,REQUESTOR_NAME,SUPPLIER_ID,REQUESTOR_ID,PS_ID, MERCHANT_ID,TRANSACTION_TYPE,CREATED_DATE,EVENTID, REGISTRATION_STATUS from SB_OWNER.WERCS_STATUS_MASTER where Registration_Status  in ('0','2') and WERCS_OUT_TRIGGER = 'Get Status' and REGULATORY_DATA_STATUS in (0,30) and eventid != 10";
	public final String			updateMailSentStatus						= "UPDATE SB_OWNER.WERCS_STATUS_MASTER SET EVENTID=? WHERE PIP_ID=?";
	public final String			getEmailIDfromUsernameQuery					= "SELECT EMAIL_ADDRESS FROM SB_OWNER.PCM_EMAIL_LIST_MEMBERS WHERE EMAIL_LIST_CD =?";

	public SBDatabaseHandlingService(OracleDataSource oracleDatasource){
		this.datasource=oracleDatasource;
	}
	
	public boolean masterTableInsertUpdate(MasterTableVO masterTableRow, IntgSrvLogger logger) {

		logger.info("Insert/Update master table");

		Connection dbConnection = null;
		CallableStatement callableStatement = null;

		try {
			dbConnection = datasource.getConnection();
			logger.info("Connection obtained.");
			if (dbConnection != null) {
				callableStatement = dbConnection.prepareCall(masterTableInsertUpdateProcedure);

				callableStatement.setString(1, masterTableRow.getUPCNo());
				callableStatement.setString(2, masterTableRow.getWercsID());
				callableStatement.setString(3, masterTableRow.getPipid());
				callableStatement.setString(4, masterTableRow.getSkuno());
				callableStatement.setString(5, masterTableRow.getStepid());
				callableStatement.setString(6, masterTableRow.getModelno());
				callableStatement.setString(7, masterTableRow.getItemdesc());
				callableStatement.setString(8, masterTableRow.getSupplierName());
				callableStatement.setString(9, masterTableRow.getRequestorName());
				callableStatement.setString(10, masterTableRow.getRequestorID());
				callableStatement.setString(11, masterTableRow.getSupplierId());
				callableStatement.setString(12, masterTableRow.getPsId());
				callableStatement.setString(13, masterTableRow.getMerchantId());
				callableStatement.setString(14, masterTableRow.getTranstype());
				callableStatement.setInt(15, masterTableRow.getRegistrationStatus());
				callableStatement.setString(16, masterTableRow.getWercsTrigger());
				callableStatement.setString(17, masterTableRow.getRegulatoryStatus());
				callableStatement.setInt(18, masterTableRow.getEventID());

				// execute getDBUSERByUserId store procedure
				int result = callableStatement.executeUpdate();

				if (result == 1) {
					infoLogString = "Procedure executed without exceptions. Inserted values into database.";
					traceLogger.info(clazzname, "run", infoLogString);
					ehfLogger.info(infoLogString);
					IntgSrvUtils.printConsole(infoLogString);
				}
			}

		} catch (SQLException e) {
			logger.error("Exception caught while calling stored procedure :" + e.getErrorCode() + " :" + e.getMessage());
			DatamigrationCommonUtil.printConsole(e.getMessage());
			IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(), "");
			return false;
		} finally {

			if (callableStatement != null) {
				try {
					callableStatement.close();
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
		return true;
	}

	public void registrationStatusUpdate(String upcNo, String pipID, int registrationStatus, IntgSrvLogger logger) {

		logger.info("Updating RegistrationStatus in Database");

		Connection dbConnection = null;
		CallableStatement callableStatement = null;

		try {
			dbConnection = datasource.getConnection();
			if (dbConnection != null) {
				callableStatement = dbConnection.prepareCall(registrationStatusUpdateProcedure);

				callableStatement.setString(1, upcNo);
				callableStatement.setInt(2, registrationStatus);
				callableStatement.setString(3, pipID);

				// execute getDBUSERByUserId store procedure
				int result = callableStatement.executeUpdate();

				DatamigrationCommonUtil.printConsole("Procedure executed without exceptions. Result=" + result);
			}

		} catch (SQLException e) {
			logger.error("Exception caught while calling stored procedure :" + e.getErrorCode() + " :" + e.getMessage());
			DatamigrationCommonUtil.printConsole(e.getMessage());
			IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(), "");
		} finally {

			if (callableStatement != null) {
				try {
					callableStatement.close();
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

	public void auditTableInsert(String upcNo, String pipID, int statusno, IntgSrvLogger logger) {

		logger.info("Inserting row in Audit table with status=" + statusno + " for pipid=" + pipID);

		Connection dbConnection = null;
		CallableStatement callableStatement = null;

		try {
			dbConnection = datasource.getConnection();
			if (dbConnection != null) {
				callableStatement = dbConnection.prepareCall(wercsStatusAuditTableInsertProcedure);

				callableStatement.setString(1, upcNo);
				callableStatement.setInt(3, statusno);
				callableStatement.setString(2, pipID);

				// execute getDBUSERByUserId store procedure
				int result = callableStatement.executeUpdate();

				DatamigrationCommonUtil.printConsole("Procedure executed without exceptions.Result=" + result);
			}

		} catch (SQLException e) {
			logger.error("Exception caught while calling stored procedure :" + e.getErrorCode() + " :" + e.getMessage());
			DatamigrationCommonUtil.printConsole(e.getMessage());
			IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(), "");
		} finally {

			if (callableStatement != null) {
				try {
					callableStatement.close();
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

	public List<MasterTableVO> getWercsRetryUPCs(IntgSrvLogger logger) {

		logger.info("Getting UPCs to Retry");

		Connection dbConnection = null;
		Statement statement = null;
		List<MasterTableVO> masterTableVOList = new ArrayList<MasterTableVO>();
		try {
			dbConnection = datasource.getConnection();
			if (dbConnection != null) {
				logger.info("Connection obtained");
				statement = dbConnection.createStatement();

				// execute getDBUSERByUserId store procedure
				ResultSet resultSet = statement.executeQuery(wercsRetryUPCsQuery);

				while (resultSet.next()) {

					MasterTableVO masterTableVO = new MasterTableVO();
					masterTableVO.setUPCNo(resultSet.getString("upc"));
					masterTableVO.setPipid(resultSet.getString("pip_id"));
					masterTableVO.setTranstype(resultSet.getString("transaction_type"));
					masterTableVO.setCreatedDate(resultSet.getTimestamp("created_date"));
					masterTableVO.setPreviousRegistrationStatus(resultSet.getInt("registration_status"));
					masterTableVO.setStepid(resultSet.getString("step_id"));
					masterTableVO.setModelno(resultSet.getString("model_number"));
					masterTableVOList.add(masterTableVO);
				}
				logger.info(masterTableVOList.size() + " items retrieved from DB to re check WERCS Registration status");
				DatamigrationCommonUtil.printConsole("Query executed without exceptions.");

			}

		} catch (SQLException e) {
			logger.error("Exception caught while calling stored procedure :" + e.getErrorCode() + " :" + e.getMessage());
			DatamigrationCommonUtil.printConsole(e.getMessage());
			IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(), "");
		} finally {

			if (statement != null) {
				try {
					statement.close();
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

		return masterTableVOList;
	}

	public void rejectedItemUpdate(List<MasterTableVO> rejectedItems, IntgSrvLogger logger) {

		logger.info("updating the rejected status in Database");

		Connection dbConnection = null;
		CallableStatement callableStatement = null;

		try {
			dbConnection = datasource.getConnection();
			if (dbConnection != null) {
				callableStatement = dbConnection.prepareCall(rejectedItemUpdateQuery);

				for (MasterTableVO masterTableVO : rejectedItems) {
					callableStatement.setString(1, masterTableVO.getUPCNo());
					callableStatement.setString(2, masterTableVO.getPipid());
					// execute getDBUSERByUserId store procedure
					int result = callableStatement.executeUpdate();
					DatamigrationCommonUtil.printConsole("Procedure executed without exceptions. Result=" + result);
				}
			}

		} catch (SQLException e) {
			logger.error("Exception caught while calling stored procedure :" + e.getErrorCode() + " :" + e.getMessage());
			DatamigrationCommonUtil.printConsole(e.getMessage());
			IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(), "");

		} finally {

			if (callableStatement != null) {
				try {
					callableStatement.close();
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

	public List<String> getWercsUPCforRegData(String UPCValue, IntgSrvLogger logger) throws IOException {

		logger.info("Getting UPC details from DataBase for RegulatoryData");

		Statement statement = null;
		DatamigrationCommonUtil.printConsole("UPCValue : " + UPCValue);
		List<String> listofUPCDetails = new ArrayList<String>();
		Connection dbConnection = null;
		CallableStatement callableStatement = null;
		try {
			dbConnection = datasource.getConnection();
			if (dbConnection != null) {
				callableStatement = dbConnection.prepareCall(getUPCforRegData);

				callableStatement.setString(1, UPCValue);
				callableStatement.registerOutParameter(2, java.sql.Types.VARCHAR);
				callableStatement.registerOutParameter(3, java.sql.Types.VARCHAR);
				callableStatement.registerOutParameter(4, java.sql.Types.VARCHAR);
				callableStatement.registerOutParameter(5, java.sql.Types.VARCHAR);
				// execute getDBUSERByUserId store procedure
				int result = callableStatement.executeUpdate();
				DatamigrationCommonUtil.printConsole("Procedure executed without exceptions.Result=====" + result);
				if (result > 0) {
					if (callableStatement.getString(2) != null) {
						listofUPCDetails.add(callableStatement.getString(2));
						listofUPCDetails.add(callableStatement.getString(3));
						listofUPCDetails.add(callableStatement.getString(4));
						listofUPCDetails.add(callableStatement.getString(5));
					} else {
						auditTableInsert(UPCValue, "", REGULATORYDATA_UNSOLICITED_SKUNOTAVAILABLE, logger);
						crossrefTableInsert("", "", "", UPCValue);
						System.out.println("Email test start ...");
						EmailUtil emailUtil = new EmailUtil();
						String mailList = "sankar.suganya@staples.com";
						emailUtil.sendEmail("WERCS CORPDMZ INTEGRATION REGULATORY DATA- UPC NOT FOUND", "Hello TEAM,\n Upc could not be found in SpringBatch database : "+UPCValue+"\n", mailList);
						System.out.println("Email test end!");
					}
				}
			}
		} catch (SQLException e) {
			logger.error("Exception caught while calling stored procedure :" + e.getErrorCode() + " :" + e.getMessage());
			DatamigrationCommonUtil.printConsole(e.getMessage());
			IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(), "");
		} finally {

			if (statement != null) {
				try {
					statement.close();
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
		return listofUPCDetails;
	}

	public String getWercsUPCforMSDSDoc(String UPCValue, String FileName, IntgSrvLogger logger) throws IOException {

		logger.info("Getting SKU Value for Corresponding UPC from DataBase for MSDSDoc");

		Statement statement = null;
		DatamigrationCommonUtil.printConsole("UPCValue : " + UPCValue);
		String SKUValue = null;
		Connection dbConnection = null;
		CallableStatement callableStatement = null;
		try {
			dbConnection = datasource.getConnection();
			if (dbConnection != null) {
				callableStatement = dbConnection.prepareCall(getUPCforMSDSDoc);

				callableStatement.setString(1, UPCValue);
				callableStatement.registerOutParameter(2, java.sql.Types.VARCHAR);
				callableStatement.registerOutParameter(3, java.sql.Types.VARCHAR);
				callableStatement.registerOutParameter(4, java.sql.Types.VARCHAR);
				callableStatement.registerOutParameter(5, java.sql.Types.VARCHAR);
				// execute getDBUSERByUserId store procedure
				int result = callableStatement.executeUpdate();
				DatamigrationCommonUtil.printConsole("Procedure executed without exceptions.Result==" + result);
				if (result > 0) {
					if (callableStatement.getString(2) != null && callableStatement.getString(3) != null) {
						SKUValue = callableStatement.getString(3);
					} else {
						if (FileName.startsWith(CorpdmzToStepMSDSProcessor.PREFIX_FOR_MSDSDOC)) {
							DatamigrationCommonUtil.printConsole("No Need to Update Status into AuditTable and CrossRefTable");
						} else {
							auditTableInsert(UPCValue, "", MSDS_UNSOLICITED_SKUNOTAVAILABLE, logger);
							crossrefTableInsert("", "", "", UPCValue);
							System.out.println("Email test start ...");
							EmailUtil emailUtil = new EmailUtil();
							String mailList = "sankar.suganya@staples.com";
							emailUtil.sendEmail("WERCS CORPDMZ INTEGRATION MSDS DOCUMENT- UPC NOT FOUND", "Hello TEAM,\n Upc could not be found in SpringBatch database : "+UPCValue+"\n", mailList);
							System.out.println("Email test end!");
						}
					}
				}
			}
		} catch (SQLException e) {
			logger.error("Exception caught while calling stored procedure :" + e.getErrorCode() + " :" + e.getMessage());
			DatamigrationCommonUtil.printConsole(e.getMessage());
			IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(), "");
		} finally {

			if (statement != null) {
				try {
					statement.close();
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
		return SKUValue;
	}


	public void crossrefTableInsert(String sku, String stepID, String pipID, String upcNo) {

		Connection dbConnection = null;
		CallableStatement callableStatement = null;

		try {
			dbConnection = datasource.getConnection();
			if (dbConnection != null) {
				callableStatement = dbConnection.prepareCall(wercsUPCCrossRefTableInsertProcedure);

				callableStatement.setString(1, sku);
				callableStatement.setString(2, stepID);
				callableStatement.setString(3, pipID);
				callableStatement.setString(4, upcNo);

				int result = callableStatement.executeUpdate();

				DatamigrationCommonUtil.printConsole("Procedure executed without exceptions.Result=" + result);
			}

		} catch (SQLException e) {
			DatamigrationCommonUtil.printConsole(e.getMessage());
			IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(), "");
		} finally {

			if (callableStatement != null) {
				try {
					callableStatement.close();
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

	public List<MasterTableVO> getRulesTrippedDetails(IntgSrvLogger logger) {

		logger.info("Getting Rules tripped details");
		Connection dbConnection = null;
		Statement statement = null;
		List<MasterTableVO> masterTableVOList = new ArrayList<MasterTableVO>();
		try {
			dbConnection = datasource.getConnection();
			if (dbConnection != null) {
				logger.info("Connection obtained");
				statement = dbConnection.createStatement();

				// execute getDBUSERByUserId store procedure
				ResultSet resultSet = statement.executeQuery(getRulesTrippedDetailsQuery);

				while (resultSet.next()) {

					MasterTableVO masterTableVO = new MasterTableVO();
					masterTableVO.setUPCNo(resultSet.getString("upc"));
					masterTableVO.setPipid(resultSet.getString("pip_id"));
					masterTableVO.setTranstype(resultSet.getString("transaction_type"));
					masterTableVO.setCreatedDate(resultSet.getTimestamp("created_date"));
					masterTableVO.setStepid(resultSet.getString("step_id"));
					masterTableVO.setModelno(resultSet.getString("model_number"));
					masterTableVO.setWercsID(resultSet.getString("WERCS_ID"));
					masterTableVO.setSkuno(resultSet.getString("SKU"));
					masterTableVO.setItemdesc(resultSet.getString("ITEM_DESCRIPTION"));
					masterTableVO.setSupplierName(resultSet.getString("SUPPLIER_NAME"));
					masterTableVO.setRequestorName(resultSet.getString("REQUESTOR_NAME"));
					masterTableVO.setSupplierId(resultSet.getString("SUPPLIER_ID"));
					masterTableVO.setRequestorID(resultSet.getString("REQUESTOR_ID"));
					masterTableVO.setPsId(resultSet.getString("PS_ID"));
					masterTableVO.setMerchantId(resultSet.getString("MERCHANT_ID"));

					masterTableVOList.add(masterTableVO);
				}

				logger.info(masterTableVOList.size() + " items retrieved from DB to send Rules tripped mail");
				DatamigrationCommonUtil.printConsole("Query executed without exceptions.");

			}

		} catch (SQLException e) {
			logger.error("Exception caught while calling stored procedure :" + e.getErrorCode() + " :" + e.getMessage());
			DatamigrationCommonUtil.printConsole(e.getMessage());
//			IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(), "");
			e.printStackTrace();
		} finally {

			if (statement != null) {
				try {
					statement.close();
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

		return masterTableVOList;
	}

	/*
	 * public void updateMasterTableEventID(String eventID,String
	 * PIPid,IntgSrvLogger logger){
	 * logger.info("updating the rejected status in Database");
	 * 
	 * Connection dbConnection = null; PreparedStatement preparedStatement =
	 * null;
	 * 
	 * try { dbConnection = datasource.getConnection(); if(dbConnection!=null){
	 * preparedStatement =
	 * dbConnection.prepareStatement(udpateEventIDRulesTrippedQuery);
	 * preparedStatement.setString(1, eventID); preparedStatement.setString(2,
	 * PIPid); int result = preparedStatement.executeUpdate();
	 * 
	 * }
	 * 
	 * } catch (SQLException e) {
	 * logger.error("Exception caught while executing query:"
	 * +e.getErrorCode()+" :"+e.getMessage());
	 * DatamigrationCommonUtil.printConsole(e.getMessage());
	 * IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(), "");
	 * 
	 * } finally {
	 * 
	 * if (preparedStatement != null) { try { preparedStatement.close(); } catch
	 * (SQLException e) { e.printStackTrace(); } }
	 * 
	 * if (dbConnection != null) { try { dbConnection.close(); } catch
	 * (SQLException e) { e.printStackTrace(); } }
	 * 
	 * }
	 * 
	 * }
	 */

	public List<MasterTableVO> getRegStatusMailNotificationFromDB(IntgSrvLogger logger) {

		logger.info("Getting Mail notification details");

		Connection dbConnection = null;
		Statement statement = null;
		List<MasterTableVO> masterTableVOList = new ArrayList<MasterTableVO>();
		try {
			dbConnection = datasource.getConnection();
			if (dbConnection != null) {
				logger.info("Connection obtained");
				statement = dbConnection.createStatement();

				// execute getDBUSERByUserId store procedure
				ResultSet resultSet = statement.executeQuery(regStatMailItemsQuery);

				while (resultSet.next()) {

					MasterTableVO masterTableVO = new MasterTableVO();
					masterTableVO.setUPCNo(resultSet.getString("upc"));
					masterTableVO.setPipid(resultSet.getString("pip_id"));
					masterTableVO.setTranstype(resultSet.getString("transaction_type"));
					masterTableVO.setCreatedDate(resultSet.getTimestamp("created_date"));
					masterTableVO.setStepid(resultSet.getString("step_id"));
					masterTableVO.setModelno(resultSet.getString("model_number"));
					masterTableVO.setWercsID(resultSet.getString("WERCS_ID"));
					masterTableVO.setSkuno(resultSet.getString("SKU"));
					masterTableVO.setItemdesc(resultSet.getString("ITEM_DESCRIPTION"));
					masterTableVO.setSupplierName(resultSet.getString("SUPPLIER_NAME"));
					masterTableVO.setRequestorName(resultSet.getString("REQUESTOR_NAME"));
					masterTableVO.setSupplierId(resultSet.getString("SUPPLIER_ID"));
					masterTableVO.setRequestorID(resultSet.getString("REQUESTOR_ID"));
					masterTableVO.setPsId(resultSet.getString("PS_ID"));
					masterTableVO.setMerchantId(resultSet.getString("MERCHANT_ID"));
					masterTableVO.setEventID(resultSet.getInt("EVENTID"));
					masterTableVO.setRegistrationStatus(resultSet.getInt("REGISTRATION_STATUS"));
					masterTableVOList.add(masterTableVO);
				}

				logger.info(masterTableVOList.size() + " items retrieved from DB to send mail.");
				DatamigrationCommonUtil.printConsole("Query executed without exceptions.");

			}

		} catch (SQLException e) {
			logger.error("Exception caught while calling stored procedure :" + e.getErrorCode() + " :" + e.getMessage());
			DatamigrationCommonUtil.printConsole(e.getMessage());
//			IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(), "");
		} finally {

			if (statement != null) {
				try {
					statement.close();
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

		return masterTableVOList;
	}

	public void updateMailSentStatus(List<MasterTableVO> mailSentItems, IntgSrvLogger logger) {

		logger.info("updating the rejected status in Database");

		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;

		try {
			dbConnection = datasource.getConnection();
			if (dbConnection != null) {

				for (MasterTableVO masterTablevo : mailSentItems) {
					try {
						preparedStatement = dbConnection.prepareStatement(updateMailSentStatus);
						preparedStatement.setInt(1, masterTablevo.getEventID());
						preparedStatement.setString(2, masterTablevo.getPipid());
						int result = preparedStatement.executeUpdate();
					} catch (SQLException e) {
						logger.error("Exception caught while updating Rules tripped status in DB for " + masterTablevo.getPipid() + ":"
								+ e.getErrorCode() + " :" + e.getMessage());
						DatamigrationCommonUtil.printConsole(e.getMessage());
						IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(), "");
					}
				}
			}

		} catch (SQLException e) {
			logger.error("Exception caught while executing query:" + e.getErrorCode() + " :" + e.getMessage());
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

	public String getUserEmailID(String supplierID, IntgSrvLogger logger) {

		String emailID = "";

		logger.info("updating the rejected status in Database");

		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;

		try {
			dbConnection = datasource.getConnection();
			if (dbConnection != null) {

				preparedStatement = dbConnection.prepareStatement(getEmailIDfromUsernameQuery);
				preparedStatement.setString(1, supplierID);
				ResultSet resultSet = preparedStatement.executeQuery();
				resultSet.next();
				emailID = resultSet.getString("EMAIL_ADDRESS");
			}

		} catch (SQLException e) {
			logger.error("Exception caught while executing query:" + e.getErrorCode() + " :" + e.getMessage());
			DatamigrationCommonUtil.printConsole(e.getMessage());
//			IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(), "");

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
		return emailID;
	}
}
