package com.arisux.mdx.lib.client.render.world;

public interface IClimateProvider
{
    public ICloudProvider getCloudProvider();
    
    public IStormProvider getStormProvider();
}
