package com.comcast.test.citf.common.cima.persistence.beans;

import com.comcast.test.citf.common.cima.persistence.beans.ChannelUserMap.UserChannelId;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

public class UsersTest {

	private Users objUsers;
	private static final String sValid="VALID";
	private static final String sNull=null;
	private static final String sBlank="";
	private static final String password = "testPassword";

	private Collection<ChannelUserMap> chanelUserMap;
	private ChannelUserMap objChannelUserMap1;
	private ChannelUserMap objChannelUserMap2;
	private Collection<AccountUserMap> accountUserMap ;


	@Before
	public void setup()
	{
		objUsers=new Users();
		chanelUserMap=new ArrayList<ChannelUserMap>();
		accountUserMap=new ArrayList<AccountUserMap>();
	}


	@Test
	public void testUserId()
	{
		objUsers.setUserId(sValid);
		assertEquals(sValid,objUsers.getUserId());

		objUsers.setUserId(sNull);
		assertEquals(sNull,objUsers.getUserId());

		objUsers.setUserId(sBlank);
		assertEquals(sBlank,objUsers.getUserId());
	}

	@Test
	public void testPassword()
	{
		objUsers.setPassword(password);
		assertEquals(password,objUsers.getPassword());

	}

	@Test
	public void testCategory()
	{
		objUsers.setCategory(sValid);
		assertEquals(sValid,objUsers.getCategory());

		objUsers.setCategory(sNull);
		assertEquals(sNull,objUsers.getCategory());

		objUsers.setCategory(sBlank);
		assertEquals(sBlank,objUsers.getCategory());
	}


	@Test
	public void testEnvironment()
	{
		objUsers.setEnvironment(sValid);
		assertEquals(sValid,objUsers.getEnvironment());

		objUsers.setEnvironment(sNull);
		assertEquals(sNull,objUsers.getEnvironment());

		objUsers.setEnvironment(sBlank);
		assertEquals(sBlank,objUsers.getEnvironment());

	}


	@Test
	public void testLoginStatus()
	{
		objUsers.setLoginStatus(sValid);
		assertEquals(sValid,objUsers.getLoginStatus());

		objUsers.setLoginStatus(sNull);
		assertEquals(sNull,objUsers.getLoginStatus());

		objUsers.setLoginStatus(sBlank);
		assertEquals(sBlank,objUsers.getLoginStatus());

	}

	@Test
	public void testPin()
	{
		objUsers.setPin(sValid);
		assertEquals(sValid,objUsers.getPin());

		objUsers.setPin(sNull);
		assertEquals(sNull,objUsers.getPin());

		objUsers.setPin(sBlank);
		assertEquals(sBlank,objUsers.getPin());

	}

	@Test
	public void testTvRating()
	{
		objUsers.setTvRating(sValid);
		assertEquals(sValid,objUsers.getTvRating());

		objUsers.setTvRating(sNull);
		assertEquals(sNull,objUsers.getTvRating());

		objUsers.setTvRating(sBlank);
		assertEquals(sBlank,objUsers.getTvRating());

	}


	@Test
	public void testMovieRating()
	{
		objUsers.setMovieRating(sValid);
		assertEquals(sValid,objUsers.getMovieRating());

		objUsers.setMovieRating(sNull);
		assertEquals(sNull,objUsers.getMovieRating());

		objUsers.setMovieRating(sBlank);
		assertEquals(sBlank,objUsers.getMovieRating());

	}

	@Test
	public void testChannelMap()
	{
		/*Users user=new Users();
		Channels channel=new Channels();
		 */
		 String subscription="abc";
		 UserChannelId primaryKey1=new UserChannelId();
		 UserChannelId primaryKey2=new UserChannelId();
		 objChannelUserMap1=new ChannelUserMap(primaryKey1,subscription);
		 objChannelUserMap2=new ChannelUserMap(primaryKey2,subscription);

		 chanelUserMap.add(objChannelUserMap1);
		 chanelUserMap.add(objChannelUserMap2);

		 objUsers.setChannelMap(chanelUserMap);
		 assertEquals(chanelUserMap,objUsers.getChannelMap());
	}


	@Test
	public void testAccountMap()
	{
		AccountUserMap obj1=new AccountUserMap();
		AccountUserMap obj2=new AccountUserMap();
		accountUserMap.add(obj1);
		accountUserMap.add(obj2);	

		objUsers.setAccoutMap(accountUserMap);
		assertEquals(accountUserMap,objUsers.getAccoutMap());

	}

}
