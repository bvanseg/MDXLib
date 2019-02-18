package com.arisux.mdx.lib.client.world;

public interface IClimateProvider
{
    public ICloudProvider getCloudProvider();
    
    public IStormProvider getStormProvider();
}
