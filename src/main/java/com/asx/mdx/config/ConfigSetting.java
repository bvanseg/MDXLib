package com.asx.mdx.config;

import net.minecraftforge.common.config.Property;

public abstract class ConfigSetting
{
    protected Property property;
    protected IFlexibleConfiguration configuration;
    protected boolean requiresRestart;

    public ConfigSetting(IFlexibleConfiguration configuration, Property property)
    {
        this.configuration = configuration;
        this.property = property;
        this.requiresRestart = false; 
        this.configuration.allSettings().add(this);
    }
    
    public IFlexibleConfiguration getConfiguration()
    {
        return configuration;
    }

    public Property property()
    {
        return property;
    }
    
    public ConfigSetting setRequiresRestart()
    {
        this.requiresRestart = true;
        return this;
    }
    
    public boolean getRequiresRestart()
    {
        return this.requiresRestart;
    }

    public abstract Object value();

    public abstract void toggle();
    
    public String getStringValue()
    {
        return String.valueOf(value());
    }
}