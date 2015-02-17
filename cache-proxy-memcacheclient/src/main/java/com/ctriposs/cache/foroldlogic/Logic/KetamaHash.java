package com.ctriposs.cache.foroldlogic.Logic;
import com.ctriposs.cache.foroldlogic.Entities.MCluster;
import com.ctriposs.cache.foroldlogic.Entities.MGroup;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.util.*;

public class KetamaHash
{
	private MCluster _cluster;
	private Long[] _values;
	private MGroup[] _groups;
    private static MessageDigest MD5_DIGEST = null;
    static {
        try {
            MD5_DIGEST = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 not supported", e);
        }
    }

    /**
     * Get the md5 of the given key.
     */
    protected static byte[] computeMd5(String k) {
        MessageDigest md5=MD5_DIGEST;
        /*
        try {
            // I believe this is done to prevent multi-threading/synchronization problems
            //md5 = (MessageDigest) MD5_DIGEST.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("clone of MD5 not supported", e);
        }*/
        md5.reset();
        return md5.digest(getKeyBytes(k));
    }

    protected long hash(final String k) {
        final byte[] bKey = computeMd5(k);
        final long rv = ((long) (bKey[3] & 0xFF& 0xFF) << 24)
                | ((long) (bKey[2] & 0xFF& 0xFF) << 16)
                | ((long) (bKey[1] & 0xFF& 0xFF) << 8)
                | (bKey[0] & 0xFF& 0xFF);
        return rv & 0xffffffffL; /* Truncate to 32-bits */
    }

    /**
     * Get the bytes for a key.
     *
     * @param k the key
     * @return the bytes
     */
    protected static byte[] getKeyBytes(String k) {
        try {
            return k.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

	public KetamaHash(MCluster cluster) throws Exception {
        int copyCount=10000;
		_cluster = cluster;
        Map<Long,MGroup> list = new HashMap<>(copyCount*cluster.getGroups().size());
		if ((_cluster.getGroups().size() * copyCount) > 320 * 1000)
		{
			throw new Exception("There is too many copyCount or real nodes! nodes.Count multiply copyNodes must be not greater than 320*1000 ");
		}

		for (MGroup group : _cluster.getGroups())
		{
			for (int i = 0; i < copyCount; i++)
			{
				long m = hash(group.getGroupId() + "_" + i);
				list.put(m,group);
			}
		}
		_values = new Long[list.size()];
		_groups = new MGroup[list.size()];

        Iterator iter = list.entrySet().iterator();
        int index =0;

        for ( Map.Entry<Long,MGroup> entry :list.entrySet()){
            _values[index]=entry.getKey();
            _groups[index]=entry.getValue();
            index++;

        }

	}

	public final MGroup GetGroup(String key)
	{
		if (key==null || key.trim().length()==0)
		{
			//throw new ArgumentNullException("key");
		}
		Long value = hash(key);
        System.out.println(value);
		int result = Arrays.binarySearch(_values, value);
		if (result < 0)
		{
			result = ~result;
		}
		if (result >= _groups.length)
		{
			return _groups[_groups.length - 1];
		}
		else
		{
			return _groups[result];
		}
	}
}