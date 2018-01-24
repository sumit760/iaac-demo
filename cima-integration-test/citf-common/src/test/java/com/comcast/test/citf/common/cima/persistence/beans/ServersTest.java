package com.comcast.test.citf.common.cima.persistence.beans;

import static org.junit.Assert.assertEquals;
import java.util.Collection;
import org.junit.Before;
import org.junit.Test;
import com.comcast.test.citf.common.cima.persistence.beans.ChannelUserMap;
import com.comcast.test.citf.common.cima.persistence.beans.Servers;
import com.comcast.test.citf.common.cima.persistence.beans.Servers.ServerPrimaryKeys;


public class ServersTest {

	Servers objServers;
	private  String sValid="VALID";
	private String sNull=null;
	private String sBlank="";

	ServerPrimaryKeys objServerPrimaryKeys;

	@Before
	public void setup() {
		objServers=new Servers();
		objServerPrimaryKeys=new ServerPrimaryKeys();
	}


	@Test
	public void testUserId() {

		objServers.setServerPK(objServerPrimaryKeys);
		assertEquals(objServerPrimaryKeys,objServers.getServerPK());
	}


	@Test
	public void testHost() {

		objServers.setHost(sValid);
		assertEquals(sValid,objServers.getHost());

		objServers.setHost(sNull);
		assertEquals(sNull,objServers.getHost());

		objServers.setHost(sBlank);
		assertEquals(sBlank,objServers.getHost());
	}


	@Test
	public void testPort() {

		objServers.setPort(sValid);
		assertEquals(sValid,objServers.getPort());

		objServers.setPort(sNull);
		assertEquals(sNull,objServers.getPort());

		objServers.setPort(sBlank);
		assertEquals(sBlank,objServers.getPort());
	}


	@Test
	public void testUserID() {

		objServers.setUserId(sValid);
		assertEquals(sValid,objServers.getUserId());

		objServers.setUserId(sNull);
		assertEquals(sNull,objServers.getUserId());

		objServers.setUserId(sBlank);
		assertEquals(sBlank,objServers.getUserId());
	}


	@Test
	public void testPassword() {
		objServers.setPassword(sValid);
		assertEquals(sValid,objServers.getPassword());

		objServers.setPassword(sNull);
		assertEquals(sNull,objServers.getPassword());

		objServers.setPassword(sBlank);
		assertEquals(sBlank,objServers.getPassword());
	}


	@Test
	public void testStatus() {
		objServers.setStatus(sValid);
		assertEquals(sValid,objServers.getStatus());

		objServers.setStatus(sNull);
		assertEquals(sNull,objServers.getStatus());

		objServers.setStatus(sBlank);
		assertEquals(sBlank,objServers.getStatus());
	}


	@Test
	public void testServerType() {
		objServerPrimaryKeys.setServerType(sValid);
		assertEquals(sValid,objServerPrimaryKeys.getServerType());

		objServerPrimaryKeys.setServerType(sNull);
		assertEquals(sNull,objServerPrimaryKeys.getServerType());

		objServerPrimaryKeys.setServerType(sBlank);
		assertEquals(sBlank,objServerPrimaryKeys.getServerType());
	}


	@Test
	public void testEnvironment() {
		objServerPrimaryKeys.setEnvironment(sValid);
		assertEquals(sValid,objServerPrimaryKeys.getEnvironment());

		objServerPrimaryKeys.setEnvironment(sNull);
		assertEquals(sNull,objServerPrimaryKeys.getEnvironment());

		objServerPrimaryKeys.setEnvironment(sBlank);
		assertEquals(sBlank,objServerPrimaryKeys.getEnvironment());
	}

	@Test
	public void testPriority() {
		objServerPrimaryKeys.setPriority(12);
		assertEquals(12,objServerPrimaryKeys.getPriority());
	}
}
