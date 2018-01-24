package com.comcast.test.citf.common.cima.jsonObjs;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.comcast.test.citf.common.cima.jsonObjs.IDMGetSecretQuestionResponseJSON;


public class IDMGetSecretQuestionResponseJSONTest{

	private  IDMGetSecretQuestionResponseJSON objIDMGetSecretQuestionResponseJSON;

	private  Map<String,Object> parameterList=null;


	@Before
	public void setup()
	{
		objIDMGetSecretQuestionResponseJSON=new IDMGetSecretQuestionResponseJSON();

		parameterList = new HashMap<String, Object>();		
		parameterList.put("sValid","Valid");
		parameterList.put("sNull",null);

	}



	@Test
	public void TestAdditionalProperties()
	{
		//Valid 		
		objIDMGetSecretQuestionResponseJSON.setAdditionalProperties(parameterList);
		assertEquals(parameterList.get("sValid"),objIDMGetSecretQuestionResponseJSON.getAdditionalProperties().get("sValid"));
		assertEquals(parameterList.get("sNull"),objIDMGetSecretQuestionResponseJSON.getAdditionalProperties().get("sNull"));

	}	
}
