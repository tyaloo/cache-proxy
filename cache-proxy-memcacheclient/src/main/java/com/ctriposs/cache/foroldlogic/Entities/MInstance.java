package com.ctriposs.cache.foroldlogic.Entities;

import com.ctriposs.cache.memcached.MemcachedClient;

public class MInstance
{

	private String privateIpAddress;
	public final String getIpAddress()
	{
		return privateIpAddress;
	}
	public final void setIpAddress(String value)
	{
		privateIpAddress = value;
	}

	private int privatePort;
	public final int getPort()
	{
		return privatePort;
	}
	public final void setPort(int value)
	{
		privatePort = value;
	}


	private int privateMaxConnections;
	public final int getMaxConnections()
	{
		return privateMaxConnections;
	}
	public final void setMaxConnections(int value)
	{
		privateMaxConnections = value;
	}


	private MGroup privateGroup;
	public final MGroup getGroup()
	{
		return privateGroup;
	}
	public final void setGroup(MGroup value)
	{
		privateGroup = value;
	}


	private MemcachedClient _client;

	public final MemcachedClient getClient()
	{
		if(_client == null)
		{
			_client = null;//MemcachedClient.Create(getIpAddress() + ":" + getPort());
		}
		return _client;
	}

}