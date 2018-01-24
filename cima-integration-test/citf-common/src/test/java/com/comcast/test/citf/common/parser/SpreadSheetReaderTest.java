package com.comcast.test.citf.common.parser;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;


public class SpreadSheetReaderTest {

	@Test
	public void testReadExcel() {
		
		String filename = "Test_Users_Login_QA.xlsx";
		Set<String> columnNames = new HashSet<String>();
		columnNames.add("User Id");
		columnNames.add("Password");
		columnNames.add("Billing Id");
		columnNames.add("Role");
		columnNames.add("Login Status");
		
		SpreadSheetReader reader = new SpreadSheetReader();
		List<Map<String, Object>> userServiceMaps = reader.readExcel(filename, columnNames, null);
		
		assertThat(
				"Expected valid response",
				userServiceMaps, notNullValue());

		//Check first row
		assertThat(
				(String)userServiceMaps.get(0).get("User Id"), is("loginCitfTU02"));
		
		assertThat(
				(String)userServiceMaps.get(0).get("Password"), is("Comcast123"));
		
		assertThat(
				(String)userServiceMaps.get(0).get("Billing Id"), is("9999628585001"));
		
		assertThat(
				(String)userServiceMaps.get(0).get("Role"), is("P"));
		
		assertThat(
				(String)userServiceMaps.get(0).get("Login Status"), is("ACTIVE"));

		
		//Check fifth row
		assertThat(
				(String)userServiceMaps.get(4).get("User Id"), is("loginCitfTU10"));
		
		assertThat(
				(String)userServiceMaps.get(4).get("Password"), is("Comcast123"));
		
		assertThat(
				(String)userServiceMaps.get(4).get("Billing Id"), is("9999628584901"));
		
		assertThat(
				(String)userServiceMaps.get(4).get("Role"), is("P"));
		
		assertThat(
				(String)userServiceMaps.get(4).get("Login Status"), is("ACTIVE"));
		
	}
	
}
