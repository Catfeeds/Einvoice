package com.invoice.util;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class NewHashMap<K, V> extends HashMap<K, V> implements Map<K, V>, Cloneable, Serializable
{
	private static final long serialVersionUID = 362498820763181265L;

	public NewHashMap(int paramInt, float paramFloat)
	{
		super(paramInt, paramFloat);
	}

	public NewHashMap(int paramInt)
	{
		super(paramInt);
	}

	public NewHashMap()
	{
		super();
	}

	public NewHashMap(Map<? extends K, ? extends V> paramMap)
	{
		super(paramMap);
	}

	public V get(Object paramObject)
	{
		return super.get(paramObject.toString().toLowerCase());
	}

	public boolean containsKey(Object paramObject)
	{
		return super.containsKey(paramObject.toString().toLowerCase());
	}

	@SuppressWarnings("unchecked")
	public V put(K paramK, V paramV)
	{
		// BigDecimal浠ュ瓧绗︿覆杩涜淇濆瓨,閬垮厤鍦↗SON杞崲鏃朵涪澶辩簿搴�
		if (paramV instanceof BigDecimal)
		{
			return super.put((K) paramK.toString().toLowerCase(), (V)((BigDecimal)paramV).toString());
		}
		else 
		{
			return super.put((K) paramK.toString().toLowerCase(), paramV);
		}
	}

	public V remove(Object paramObject)
	{
		return super.remove(paramObject.toString().toLowerCase());
	}
}

