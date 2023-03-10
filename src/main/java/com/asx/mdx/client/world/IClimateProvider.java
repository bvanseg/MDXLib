package com.asx.mdx.client.world;

public interface IClimateProvider
{
    public ICloudProvider getCloudProvider();
    
    public IStormProvider getStormProvider();
}
