package edu.wustl.common.idgenerator;

import java.util.UUID;


public class UUIDKeyGenerator implements IUniqueKeyGenerator
{
	public String getKey() 
    {
        return UUID.randomUUID().toString();
    }
}
