package com.marketo.service;

import java.io.File;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;


public class JsonHelperTest 
{
	@Rule
	public TemporaryFolder tempFolder = new TemporaryFolder();
	
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	@Test
	public void testReadJSONInput() throws Exception 
	{
		final JSONArray jsonArray = JsonHelper.readJSONInput();
		
		Assert.assertEquals(10, jsonArray.size());
	}
	
	
	@Test
	public void testWriteJSONOutput() throws Exception
	{
		final String jsonRecord = "{\n" +
				  "    \"_id\": \"jkj238238jdsnfsj23\",\n" +
				  "    \"email\": \"mae@bar.com\",\n" +
				  "    \"firstName\": \"John\",\n" +
				  "    \"lastName\": \"Smith\",\n" +
				  "    \"address\": \"888 Mayberry St\",\n" +
				  "    \"entryDate\": \"2014-05-07T17:33:20+00:00\"\n" +
				  "  }";
		final File tempFile = tempFolder.newFile("output.json");

		final JSONObject record = (JSONObject) new JSONParser().parse(jsonRecord);
		final Map<String, JSONObject> idHashMap = new HashMap<>();
		
		idHashMap.put("jkj238238jdsnfsj23", record);
		
		JsonHelper.writeJSONOutput(idHashMap, tempFile.getAbsolutePath());
		
		Assert.assertTrue(tempFile.exists());
	}
	
	
	@Test
	public void testCompareRecords() throws Exception
	{
		final String jsonRecordOne = "{\n" +
				  "    \"_id\": \"jkj238238jdsnfsj23\",\n" +
				  "    \"email\": \"foo@bar.com\",\n" +
				  "    \"firstName\": \"John\",\n" +
				  "    \"lastName\": \"Smith\",\n" +
				  "    \"address\": \"888 Mayberry St\",\n" +
				  "    \"entryDate\": \"2014-05-07T17:30:20+00:00\"\n" +
				  "  }";
		
		final String jsonRecordTwo = "{\n" +
				  "    \"_id\": \"jkj238238jdsnfsj23\",\n" +
				  "    \"email\": \"mae@bar.com\",\n" +
				  "    \"firstName\": \"John\",\n" +
				  "    \"lastName\": \"Smith\",\n" +
				  "    \"address\": \"888 Mayberry St\",\n" +
				  "    \"entryDate\": \"2014-05-07T17:33:20+00:00\"\n" +
				  "  }";
		
		
		
		final JSONObject recordOne = (JSONObject) new JSONParser().parse(jsonRecordOne);
		final JSONObject recordTwo = (JSONObject) new JSONParser().parse(jsonRecordTwo);
		
		
		final JSONObject result = JsonHelper.compareRecords(recordOne, recordTwo);
		
		Assert.assertTrue("mae@bar.com".equals((String)result.get("email").toString()));
		Assert.assertTrue("2014-05-07T17:33:20+00:00".equals((String)result.get("entryDate")));		
	}
	
	
	@Test
	public void testCompareRecordsWithSameDate() throws Exception
	{
		final String jsonRecordOne = "{\n" +
				  "    \"_id\": \"jkj238238jdsnfsj23\",\n" +
				  "    \"email\": \"mae@bar.com\",\n" +
				  "    \"firstName\": \"John\",\n" +
				  "    \"lastName\": \"Smith\",\n" +
				  "    \"address\": \"888 Mayberry St\",\n" +
				  "    \"entryDate\": \"2014-05-07T17:33:20+00:00\"\n" +
				  "  }";
		
		final String jsonRecordTwo = "{\n" +
				  "    \"_id\": \"jkj238238jdsnfsj23\",\n" +
				  "    \"email\": \"foo@bar.com\",\n" +
				  "    \"firstName\": \"John\",\n" +
				  "    \"lastName\": \"Smith\",\n" +
				  "    \"address\": \"888 Mayberry St\",\n" +
				  "    \"entryDate\": \"2014-05-07T17:33:20+00:00\"\n" +
				  "  }";
		
		
		
		final JSONObject recordOne = (JSONObject) new JSONParser().parse(jsonRecordOne);
		final JSONObject recordTwo = (JSONObject) new JSONParser().parse(jsonRecordTwo);
		
		
		final JSONObject result = JsonHelper.compareRecords(recordOne, recordTwo);
		
		Assert.assertTrue("foo@bar.com".equals((String)result.get("email").toString()));
		Assert.assertTrue("2014-05-07T17:33:20+00:00".equals((String)result.get("entryDate")));		
	}
}
