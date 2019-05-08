package com.marketo.service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.marketo.utils.Constants;


/**
 * The JsonHelper class used for reading, writing and processing the JSONObjects.
 *  
 * @author Nags
 */
public class JsonHelper 
{	
	/**
	 * Logger class.
	 */
	final static Logger logger = Logger.getLogger(JsonHelper.class);
	
	
	/**
	 * The method to read the input file from the provided path.
	 * 
	 * @param fileName The file path. Must not be {@code null}.
	 * 
	 * @return The JSONArray data which needs to be processed.
	 */
	public static JSONArray readJSONInput()
	{
		JSONArray jsonArray = new JSONArray();
		
		try 
		{
			
			final InputStream inputStream = JsonHelper.class.getResourceAsStream(Constants.INPUT_FILENAME);
			final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));			
			final JSONParser jsonParser = new JSONParser();
			final Object obj = jsonParser.parse(reader);
			final JSONObject jsonObject = (JSONObject) obj;
			
			jsonArray = (JSONArray) jsonObject.get(Constants.RECORD_LEADS);
		} 
		catch (FileNotFoundException e) 
		{
			logger.error("Error occurred unable to locate the file.");
		}
		catch (IOException e) 
		{
			logger.error("Error occurred while accessing the file.");
		} 
		catch (org.json.simple.parser.ParseException e)
		{
			logger.error("Error occurred while processing the file.");
		}
		
		return jsonArray;
	}
	
	
	/**
	 * Method writes the result data to output file.
	 * 
	 * @param resultMap The HashMap containing non-duplicated records. Must not be {@code null}.
	 * @param fileName The path and name of the file where output to be generated. Must not be {@code null}.
	 * 
	 * @throws IOException Anything goes wrong if accessing the file.
	 */
	@SuppressWarnings("unchecked")
	public static void writeJSONOutput(final Map<String, JSONObject> resultMap, final String fileName)
	{
		final List<JSONObject> values = new ArrayList<>(resultMap.values());
		final JSONObject obj = new JSONObject();
		
		obj.put(Constants.RECORD_LEADS, values);
		
		try (FileWriter file = new FileWriter(fileName)) 
		{
            file.write(obj.toJSONString());
        }
		catch (IOException e) 
		{
			logger.error("Error occurred while writing into the file.");
        }		
	}
	
	
	/**
	 * Compare objects based on entry dates.
	 * 
	 * @param object1 The JSONObject. Must not be {@code null}.
	 * @param object2 The JSONObject. Must not be {@code null}.
	 * 
	 * @return The newest object based on logic. Will never be {@code null}.
	 */
	public static JSONObject compareRecords(final JSONObject object1 , final JSONObject object2)
	{		
		try
		{
			final Date date1 = new SimpleDateFormat(Constants.RECORD_DATE_FORMAT).parse((String) object1.get(Constants.RECORD_ENTRY_DATE));
			final Date date2 = new SimpleDateFormat(Constants.RECORD_DATE_FORMAT).parse((String) object2.get(Constants.RECORD_ENTRY_DATE));
			logger.info(String.format("Date comparison between first %s and second %s records. %n", date1, date2));
			
			if(date1.before(date2) || date1.equals(date2))
			{
				return object2;
			}
			else
			{
				return object1;
			}
			
		}
		catch(final ParseException exception)
		{
			logger.error("Wrong Date Format");
		}
		
		return null;
	}
	
	
	/**
	 * Print the value of each field if change. 
	 * 
	 * @param oldObject The Old JSON Object.
	 * @param newObject The New JSON Object.
	 * 
	 * @throws IOException If something is wrong.
	 */
	public static void writeDiff(final JSONObject oldObject, final JSONObject newObject)
	{
		logger.info("---------------------------------------------------");
		
		logger.info("Following fields got updated from an old value to new value.");
		
		final Iterator iterator = oldObject.keySet().iterator();
		
		while(iterator.hasNext())
		{
			final Object key = iterator.next();
			
			if (!oldObject.get(key).equals(newObject.get(key)))
			{
				logger.info(String.format("Updating field  %s: from %s to %s", key, oldObject.get(key), newObject.get(key)));
			}
		}
		
		logger.info("---------------------------------------------------");
	}
}
