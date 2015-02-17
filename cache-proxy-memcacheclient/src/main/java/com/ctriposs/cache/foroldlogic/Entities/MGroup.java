package com.ctriposs.cache.foroldlogic.Entities;

import java.util.UUID;

public class MGroup
{

	private UUID privateGroupId ;
	public final UUID getGroupId()
	{
		return privateGroupId;
	}
	public final void setGroupId(UUID value)
	{
		privateGroupId = value;
	}

	private java.util.ArrayList<MInstance> privateInstances;
	public final java.util.ArrayList<MInstance> getInstances()
	{
		return privateInstances;
	}
	public final void setInstances(java.util.ArrayList<MInstance> value)
	{
		privateInstances = value;
	}

	private MCluster privateCluster;
	public final MCluster getCluster()
	{
		return privateCluster;
	}
	public final void setCluster(MCluster value)
	{
		privateCluster = value;
	}


}