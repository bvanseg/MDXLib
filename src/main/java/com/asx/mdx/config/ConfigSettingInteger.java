package com.asx.mdx.config;

import net.minecraftforge.common.config.Property;

public class ConfigSettingInteger extends ConfigSetting
{
    public ConfigSettingInteger(IFlexibleConfiguration configuration, Property property)
    {
        super(configuration, property);
    }

    @Override
    public void toggle()
    {
        return;
    }

    @Override
    public Integer value()
    {
        return property().getInt();
    }
}