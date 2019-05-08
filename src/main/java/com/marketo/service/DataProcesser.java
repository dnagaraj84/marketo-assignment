package com.marketo.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.marketo.utils.Constants;

/**
 * The class used for processing the JSON Object.
 * 
 * @author Nags
 *
 */
public class DataProcesser
{
	/**
	 * NOTE : Using two HashMap to achieve O(1) time complexity for searching the record based on ID and Email.
	 * ID HashMap stores ID as key and Record as Object and Email HashMap stores Email as key and Object as value.
	 * Problem solving time complexity is O(n*1) = O(n).
	 */

	final static Logger logger = Logger.getLogger(DataProcesser.class);
	
	final Map<String, JSONObject> idRecordMap = new HashMap<>();
	final Map<String, JSONObject> emailRecordMap = new HashMap<>();	
	
	
	/**
	 * Process the JSONArray and store the data into ID HashMap and Email HashMap for reference.
	 * 
	 * @param leadsInfo The JSON Array containing JSON Objects.
	 */
	public Map<String, JSONObject> createDataDictionary(JSONArray leadsInfo)
	{	
		if(leadsInfo != null)
		{
			for(Object obj: leadsInfo)
			{
				if ( obj instanceof JSONObject ) 
				{
					final JSONObject newRecord = (JSONObject) obj;				
					
					// Process the ID HashMap.
					processDataInIdMap(newRecord);
					
					// Process the Email HashMap.
					processDataInEmailMap(newRecord);				
				}			
			}	
		}		
		
		return idRecordMap;
	}
	
	
	/**
	 * Process the ID HashMap. Insert if not present, update if duplicate exists.
	 * 
	 * @param newRecord The new record which is been processed. 
	 */
	private void processDataInIdMap(final JSONObject newRecord)
	{
		final String recordId = (String) newRecord.get(Constants.RECORD_ID);
		
		if(idRecordMap.containsKey(recordId))
		{
			logger.info("ID RecordMap found a duplicate. Updating the ID HashMap.");
			
			final JSONObject oldRecord = idRecordMap.get(recordId);	
			
			final JSONObject result = JsonHelper.compareRecords(oldRecord, newRecord);
			
			JsonHelper.writeDiff(oldRecord, result);
			
			idRecordMap.put(recordId, result);
			
			if(emailRecordMap.containsKey(oldRecord.get(Constants.RECORD_EMAIL)))
			{				
				
				final JSONObject duplicateRecord = emailRecordMap.get(oldRecord.get(Constants.RECORD_EMAIL));
				
				if(duplicateRecord.get(Constants.RECORD_ID).equals(oldRecord.get(Constants.RECORD_ID)))
				{
					logger.info("Duplicate record found in Email HashMap");
					
					emailRecordMap.remove(duplicateRecord.get(Constants.RECORD_ID));
				}
			}
		}
		else
		{
			logger.info("New record inserted into ID HashMap.");
			
			idRecordMap.put(recordId, newRecord);
		}	
	}
	
	
	/**
	 * Process the Email HashMap. Insert if not present, update if duplicate exists.
	 * 
	 * @param newRecord The new record which is been processed. 
	 */
	private void processDataInEmailMap(final JSONObject newRecord)
	{
		final String recordEmail = (String) newRecord.get(Constants.RECORD_EMAIL);
		
		if(emailRecordMap.containsKey(recordEmail))
		{
			logger.info("Email RecordMap found a duplicate. Updating the Email HashMap.");
			
			final JSONObject oldRecord = emailRecordMap.get(recordEmail);
			final JSONObject result = JsonHelper.compareRecords(oldRecord, newRecord);
			
			JsonHelper.writeDiff(oldRecord, result);
			
			// Update the Email HashMap.
			emailRecordMap.put(recordEmail, result);
			
			if(idRecordMap.containsKey(oldRecord.get(Constants.RECORD_ID)))
			{
				final JSONObject duplicateRecord = idRecordMap.get(oldRecord.get(Constants.RECORD_ID));
				
				if(duplicateRecord.get(Constants.RECORD_ID).equals(oldRecord.get(Constants.RECORD_ID)))
				{
					idRecordMap.remove(duplicateRecord.get(Constants.RECORD_ID));
				}
			}
		}
		else
		{
			logger.info("New record inserted into Email HashMap.");
			
			emailRecordMap.put(recordEmail, newRecord);
		}
	}
}