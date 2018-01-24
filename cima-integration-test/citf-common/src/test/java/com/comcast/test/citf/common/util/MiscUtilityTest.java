package com.comcast.test.citf.common.util;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import com.comcast.test.citf.common.ui.router.PageError;

public class MiscUtilityTest{
	
	private String strDate="09/10/2015";
	private String strDateLater="10/10/2015";
	private String dateFormat="MM/dd/yyyy";


	@Test
	public void testGetCurrentTimeInMili() throws InterruptedException
	{	
		 //Valid 
		Long timestamp1=MiscUtility.getCurrentTimeInMillis();		
		Thread.sleep(1000);		
		Long timestamp2=MiscUtility.getCurrentTimeInMillis();
		assertTrue(timestamp2 > timestamp1);
			
	}	
	
	
	@Test
	public void testIsExpired() throws InterruptedException
	{	
		 //Valid 
		Long timestamp1=MiscUtility.getCurrentTimeInMillis()-500000L;		
		Long expiry=100000L;	
		assertTrue(MiscUtility.isExpired(timestamp1, expiry));
		expiry=2000000L;
		assertFalse(MiscUtility.isExpired(timestamp1, expiry));
		
	}	
	
	
	@Test
	public void testGetPageErrorMessage() throws InterruptedException
	{	
		String error = "Mock Error Message";
		String errorDescription = "Mock Error Message Description";
		String sourcePage = "mockSource";
		
		PageError page = new PageError(error, errorDescription, sourcePage);
		String err = MiscUtility.getPageErrorMessage(page);
		
		
		assertThat(err, is("Mock Error Message Description"));
	}	
	
	
	@Test	
	public void testDateFormatter() throws InterruptedException
	{	
		String df = MiscUtility.getFormattedDate("yyyy-MM-dd HH:mm:ss");
		
		Pattern p = Pattern.compile("\\d{4}\\-\\d{2}\\-\\d{2}\\s+\\d{2}\\:\\d{2}\\:\\d{2}");
		Matcher match = p.matcher(df);
		boolean found = match.find();
		
		assertThat(found, is(true));
			
	}
	
	
	@Test	
	public void testgetCurrentSqlDate() throws InterruptedException
	{	
		Timestamp t = MiscUtility.getCurrentSqlDate();
		
		Pattern p = Pattern.compile("\\d{4}\\-\\d{2}\\-\\d{2}.*");
		Matcher match = p.matcher(t.toString());
		boolean found = match.find();
		
		assertThat(found, is(true));
		
	}
	
	@Test	
	public void testgetSqlDate() throws InterruptedException, ParseException
	{	
		//2015-10-10
		Date d = MiscUtility.getSqlDate(strDateLater, "MM/dd/yyyy");
		
		Pattern p = Pattern.compile("\\d{4}\\-\\d{2}\\-\\d{2}");
		Matcher match = p.matcher(d.toString());
		boolean found = match.find();
		
		assertThat(found, is(true));
	}
	
	
	@Test	
	public void testgenerateUniqueId() throws InterruptedException, ParseException
	{	
		String id = MiscUtility.generateUniqueId();
		assertThat(id.length(), is(12));	
		
		String anotherId = MiscUtility.generateUniqueId();
		assertThat(anotherId.length(), is(12));	
		
		assertNotEquals(id, anotherId);
	}
	
	
	@Test	
	public void testGetDateInMili() throws InterruptedException, ParseException
	{	
		Long dateInstanse1	= MiscUtility.getDateInMillis(strDate, dateFormat);
		Thread.sleep(3000);		
		Long dateInstanse2	= MiscUtility.getDateInMillis(strDateLater, dateFormat);
		assertTrue(dateInstanse1<dateInstanse2);
				
	}
	
	@Test	
	public void testGetDiffFromGMTInMinutes() throws InterruptedException, ParseException
	{	
		int dateInstanse1 = MiscUtility.getDiffFromGMTInMinutes("EST");
		
		//Since the EST-GMT offset changes depending on whether DST is on/off
		assertThat(dateInstanse1, anyOf(is(-300), is(-240)));
	}
	
	
	@Test	
	public void testGetDateInMilis() throws InterruptedException, ParseException
	{	
		Long dateInstanse1	= MiscUtility.getDateInMillis(strDate, dateFormat,0);
		Thread.sleep(3000);		
		Long dateInstanse2	= MiscUtility.getDateInMillis(strDateLater, dateFormat,0);
		assertTrue(dateInstanse1<dateInstanse2);
				
	}
	
	/*@Test	
	public void testGetDateOfBirthForSSN() throws ParseException
	{	
	 
		DateFormat formatter = new SimpleDateFormat("MM/dd/yy");
		Date date1 = formatter.parse("01/29/02");
		Date date2 = formatter.parse("01/29/03");
		java.sql.Date sqlDate1 = new java.sql.Date(date1.getTime());
		java.sql.Date sqlDate2 = new java.sql.Date(date2.getTime());
		
		String res = MiscUtility.getDateOfBirthForSSN(sqlDate1, sqlDate2);
		//assertThat(res, anyOf(is("04/29/2015"),is("04/29/2015"),is("04/29/2015"),is("04/29/2015"),is("04/29/2015"),is("04/29/2015")));
		
		
    }*/
	
	
	
	
}
