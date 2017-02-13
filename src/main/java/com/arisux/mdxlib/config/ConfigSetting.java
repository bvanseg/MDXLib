package com.arisux.mdxlib.config;

import net.minecraftforge.common.config.Property;

public abstract class ConfigSetting
{
    protected Property property;
    protected IFlexibleConfiguration configuration;

    public ConfigSetting(IFlexibleConfiguration configuration, Property property)
    {
        this.configuration = configuration;
        this.property = property;
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

    public abstract Object value();

    public abstract void toggle();
    
    public String getStringValue()
    {
        return String.valueOf(value());
    }
}