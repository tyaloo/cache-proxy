package com.ctriposs.cache.foroldlogic.Entities;

import com.ctriposs.cache.foroldlogic.Logic.KetamaHash;

import java.util.UUID;

public class MCluster
{


	private int privateProductID;
	public final int getProductID()
	{
		return privateProductID;
	}
	public final void setProductID(int value)
	{
		privateProductID = value;
	}

	private String privateProductName;
	public final String getProductName()
	{
		return privateProductName;
	}
	public final void setProductName(String value)
	{
		privateProductName = value;
	}

	private int privateSubSystemID;
	public final int getSubSystemID()
	{
		return privateSubSystemID;
	}
	public final void setSubSystemID(int value)
	{
		privateSubSystemID = value;
	}

	private String privateSubSystemName;
	public final String getSubSystemName()
	{
		return privateSubSystemName;
	}
	public final void setSubSystemName(String value)
	{
		privateSubSystemName = value;
	}

	private UUID privateClusterId = UUID.randomUUID();
	public final UUID getClusterId()
	{
		return privateClusterId;
	}
	public final void setClusterId(UUID value)
	{
		privateClusterId = value;
	}

	private String privateClusterName;
	public final String getClusterName()
	{
		return privateClusterName;
	}
	public final void setClusterName(String value)
	{
		privateClusterName = value;
	}

	private String privateEmail;
	public final String getEmail()
	{
		return privateEmail;
	}
	public final void setEmail(String value)
	{
		privateEmail = value;
	}

	private java.util.ArrayList<MGroup> privateGroups;
	public final java.util.ArrayList<MGroup> getGroups()
	{
		return privateGroups;
	}
	public final void setGroups(java.util.ArrayList<MGroup> value)
	{
		privateGroups = value;
	}

	public final MGroup GetGroup(String key)
	{
		if (_hash == null)
		{
            try {
                _hash = new KetamaHash(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
		return _hash.GetGroup(key);
	}

	private KetamaHash _hash;
}