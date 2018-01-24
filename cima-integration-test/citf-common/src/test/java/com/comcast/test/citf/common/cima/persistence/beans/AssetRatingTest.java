package com.comcast.test.citf.common.cima.persistence.beans;

import  static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.comcast.test.citf.common.cima.persistence.beans.AssetRatings;
import com.comcast.test.citf.common.cima.persistence.beans.AssetRatings.RatingId;


public class AssetRatingTest{

	private   AssetRatings objAssetRatings;
	private   String sValid="VALID";
	private   String sNull=null;
	private   String sBlank="";
	private   RatingId objRatingId;	
	private   int iValid=12;
	private   int iZero=0;
	private   int iNegative=-1;


	@Before	
	public  void setup()
	{
		objAssetRatings=new AssetRatings();
		objRatingId=new RatingId();
	}


	@Test
	public void testID()
	{
		//valid
		objAssetRatings.setId(objRatingId);
		assertTrue(objAssetRatings.getId() instanceof RatingId);

		objAssetRatings.setId(objRatingId);	
		assertFalse(objAssetRatings.getId().equals(sNull));
	}


	@Test
	public void testPriority()
	{
		//valid
		objAssetRatings.setPriority(iValid);		
		assertEquals(iValid,objAssetRatings.getPriority());	

		objAssetRatings.setPriority(iZero);		
		assertEquals(iZero,objAssetRatings.getPriority());

		objAssetRatings.setPriority(iNegative);		
		assertEquals(iNegative,objAssetRatings.getPriority());			
	}


	@Test
	public void testName()
	{
		//valid
		objRatingId.setName(sValid);
		assertEquals(sValid,objRatingId.getName());

		objRatingId.setName(sBlank);
		assertEquals(sBlank,objRatingId.getName());

		objRatingId.setName(sNull);
		assertEquals(sNull,objRatingId.getName());				
	}


	@Test
	public void testType()
	{
		//valid
		objRatingId.setType(sValid);
		assertEquals(sValid,objRatingId.getType());

		objRatingId.setType(sBlank);
		assertEquals(sBlank,objRatingId.getType());

		objRatingId.setType(sNull);
		assertEquals(sNull,objRatingId.getType());				
	}

}
