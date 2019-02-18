package com.asx.mdx.config;

import net.minecraftforge.common.config.Property;

public class ConfigSettingBoolean extends ConfigSetting
{
    public ConfigSettingBoolean(IFlexibleConfiguration configuration, Property property)
    {
        super(configuration, property);
    }

    @Override
    public void toggle()
    {
        this.property.set(!this.property.getBoolean());
    }

    @Override
    public Boolean value()
    {
        return property().getBoolean();
    }
    
    @Override
    public String getStringValue()
    {
        return value() ? "Enabled" : "Disabled";
    }
}