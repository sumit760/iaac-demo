package com.comcast.test.citf.common.cima.persistence.beans;


import static org.junit.Assert.*;

import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.comcast.test.citf.common.cima.persistence.beans.AccountUserMap;
import com.comcast.test.citf.common.cima.persistence.beans.Assets;
import com.comcast.test.citf.common.cima.persistence.beans.Channels;

public class AssetTest{

	private  Assets objAssetsTest;
	private  String sValid="VALID";
	private  String sNull=null;
	private  String sBlank="";
	private  Collection<AccountUserMap> userMap;
	private  List<String> userIds;
	private  Channels channelId;

	@Before	
	public void setup()
	{
		objAssetsTest=new Assets();
		channelId=new Channels();
	}


	@Test
	public void testBillingAccountId()
	{
		//valid
		objAssetsTest.setName(sValid);		
		assertEquals(sValid,objAssetsTest.getName());	

		//invalid
		objAssetsTest.setName(sNull);		
		assertEquals(sNull,objAssetsTest.getName());	

		//invalid
		objAssetsTest.setName(sBlank);		
		assertEquals(sBlank,objAssetsTest.getName());	
	}


	@Test
	public void testChannelId()
	{
		//valid
		objAssetsTest.setChannelId(channelId);		
		assertTrue(objAssetsTest.getChannelId()instanceof Channels);

		objAssetsTest.setChannelId(channelId);		
		assertFalse(objAssetsTest.getChannelId().equals(sNull));
	}


	@Test
	public void testTVRating()
	{
		//valid
		objAssetsTest.setTvRating(sValid);		
		assertEquals(sValid,objAssetsTest.getTvRating());	

		//invalid
		objAssetsTest.setTvRating(sNull);		
		assertEquals(sNull,objAssetsTest.getTvRating());	

		//invalid
		objAssetsTest.setTvRating(sBlank);		
		assertEquals(sBlank,objAssetsTest.getTvRating());	
	}


	@Test
	public void testMovieRating()
	{
		//valid
		objAssetsTest.setMovieRating(sValid);		
		assertEquals(sValid,objAssetsTest.getMovieRating());	

		//invalid
		objAssetsTest.setMovieRating(sNull);		
		assertEquals(sNull,objAssetsTest.getMovieRating());	

		//invalid
		objAssetsTest.setMovieRating(sBlank);		
		assertEquals(sBlank,objAssetsTest.getMovieRating());	
	}


	@Test
	public void testSeriesName()
	{
		//valid
		objAssetsTest.setSeriesName(sValid);		
		assertEquals(sValid,objAssetsTest.getSeriesName());	

		//invalid
		objAssetsTest.setSeriesName(sNull);		
		assertEquals(sNull,objAssetsTest.getSeriesName());	

		//invalid
		objAssetsTest.setSeriesName(sBlank);		
		assertEquals(sBlank,objAssetsTest.getSeriesName());		
	}


	@Test
	public void testSeriesID()
	{
		//valid
		objAssetsTest.setSeriesId(sValid);		
		assertEquals(sValid,objAssetsTest.getSeriesId());	

		//invalid
		objAssetsTest.setSeriesId(sNull);		
		assertEquals(sNull,objAssetsTest.getSeriesId());	

		//invalid
		objAssetsTest.setSeriesId(sBlank);		
		assertEquals(sBlank,objAssetsTest.getSeriesId());		
	}


	@Test
	public void testProgramID()
	{
		//valid
		objAssetsTest.setProgramId(sValid);		
		assertEquals(sValid,objAssetsTest.getProgramId());	

		//invalid
		objAssetsTest.setProgramId(sNull);		
		assertEquals(sNull,objAssetsTest.getProgramId());	

		//invalid
		objAssetsTest.setProgramId(sBlank);		
		assertEquals(sBlank,objAssetsTest.getProgramId());		
	}


	@Test
	public void testStatus()
	{
		//valid
		objAssetsTest.setStatus(sValid);		
		assertEquals(sValid,objAssetsTest.getStatus());	

		//invalid
		objAssetsTest.setStatus(sNull);		
		assertEquals(sNull,objAssetsTest.getStatus());	

		//invalid
		objAssetsTest.setStatus(sBlank);		
		assertEquals(sBlank,objAssetsTest.getStatus());		
	}

}
