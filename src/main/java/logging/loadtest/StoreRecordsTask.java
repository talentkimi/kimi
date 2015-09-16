package logging.loadtest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import core.xml.Xml;
import core.xml.XmlException;

import logging.HttpLogRecord;
import logging.TpLogRecord;

class StoreRecordsTask implements Runnable {
	
	private PreparedStatement pstatement;
	private Connection connection;
	private BlockingQueue<TpLogRecord> recordsToStore;
	private int recordingID;
	private Pattern bookingRef = Pattern.compile("<TFBookingReference>(.*?)</TFBookingReference>"); 
	
	StoreRecordsTask(Connection connection, BlockingQueue<TpLogRecord> recordsToStore, int recordingID){
		this.connection = connection;
		this.recordsToStore = recordsToStore;
		this.recordingID = recordingID;
	}

	@Override
	public void run() {
		try{
			try {
				pstatement = connection.prepareStatement(
								"INSERT INTO LTRequest "+
								" (requestID, timestamp, recordingID, " + 
								"	loginID, routingID, firstCommandName, " +
								"   bookingRef, isErrorInResponse, headers, " +
								"   content) " +
								" VALUES (?,?,?,?,?,?,?,?,?,?)");
			} catch (SQLException e) {
				e.printStackTrace();
				return;
			}
		
			while(!Thread.currentThread().isInterrupted()){
				try {
					TpLogRecord rec = recordsToStore.take();
					if(rec instanceof HttpLogRecord){
						HttpLogRecord record = (HttpLogRecord)rec; 
						
						String firstCommandName;
						Xml root = Xml.READER.read(record.getRequestContent());
						if(root.children() > 0){
							firstCommandName = root.getChild(0).getName().toLowerCase();
						} else {
							System.out.println("no commands found in command list for :\n" + 
									record.getRequestContent());
							continue;
						}
						
						String tfBookingRef = null; 
						Matcher matcher = bookingRef.matcher(record.getResponseContent());
						if((matcher.find())){
							tfBookingRef = matcher.group(1);
						}
						
						pstatement.setLong(1, 0);
						pstatement.setLong(2, record.getTime());
						pstatement.setInt(3, recordingID); 
						pstatement.setString(4, record.getLoginId());
						pstatement.setString(5, record.getRoutingId());
						pstatement.setString(6, firstCommandName);
						pstatement.setString(7, tfBookingRef);
						pstatement.setBoolean(8, record.getResponseContent().contains("etext"));
						pstatement.setString(9, record.getRequestHeaders());
						pstatement.setString(10, record.getRequestContent());
						
						pstatement.execute();
					}
				} catch (XmlException e) {
					e.printStackTrace();
				} catch (SQLException e) {
					e.printStackTrace();
				} catch(InterruptedException e){
					if(Thread.currentThread().isInterrupted()){
						e.printStackTrace();
						return;
					}
				} 
			}
		} catch(Exception e){
			e.printStackTrace();
		} finally {
			if(pstatement != null){
				try {
					pstatement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(connection != null){
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
