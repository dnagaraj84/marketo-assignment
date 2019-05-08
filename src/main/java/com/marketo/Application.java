/**
 * 
 */
package com.marketo;

import java.util.Map;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.marketo.service.DataProcesser;
import com.marketo.service.JsonHelper;
import com.marketo.utils.Constants;

/**
 * The application starting point. It parse the JSON file, remove the duplicate and generate the output.
 * 
 * @author Nags
 *
 */
public class Application
{
	final static Logger logger = Logger.getLogger(Application.class);
	
	public static void main(String[] args) 
	{		
		logger.debug("Processing starts.");
		
		// Read the input file.
		final JSONArray jsonArray = JsonHelper.readJSONInput();
		
		// Process data and create data dictionary for reference.
		final Map<String, JSONObject> resultObj = new DataProcesser().createDataDictionary(jsonArray);
		
		// Write data to the output file.
		JsonHelper.writeJSONOutput(resultObj, Constants.OUTPUT_FILENAME);	
		
		logger.debug("Processing completed.");
	}	

}
