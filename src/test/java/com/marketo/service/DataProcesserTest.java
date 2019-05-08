package com.marketo.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.marketo.utils.Constants;

public class DataProcesserTest 
{
	private static DataProcesser dataProcesser;
	
	@BeforeClass
	public static void setUp() 
	{
		System.setProperty("log4j.configurationFile","log4j2-text.properties");
		dataProcesser = new DataProcesser();
	}
	
	@Test
	public void testCreateDataDictionary() throws Exception 
	{
		final InputStream inputStream = DataProcesserTest.class.getResourceAsStream(Constants.INPUT_FILENAME);
		final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));			
		final JSONParser jsonParser = new JSONParser();
		final Object obj = jsonParser.parse(reader);
		final JSONObject jsonObject = (JSONObject) obj;
		
		JSONArray jsonArray = (JSONArray) jsonObject.get(Constants.RECORD_LEADS);
		
		final Map<String, JSONObject> results = dataProcesser.createDataDictionary(jsonArray);
		
		Assert.assertEquals(5, results.size());
	}
	
	
	@Test
	public void testCreateDataDictionaryWithEmptyArray() throws Exception
	{
		JSONArray jsonArray = new JSONArray();
		
		final Map<String, JSONObject> results = dataProcesser.createDataDictionary(jsonArray);
		
		Assert.assertNotNull(results);
		Assert.assertTrue(results.isEmpty());
	}
	
	
	@Test
	public void testCreateDataDictionaryWithNullArray() throws Exception
	{
		JSONArray jsonArray = null;
		
		final Map<String, JSONObject> results = dataProcesser.createDataDictionary(jsonArray);
		
		Assert.assertNotNull(results);
		Assert.assertTrue(results.isEmpty());
	}
	

}
